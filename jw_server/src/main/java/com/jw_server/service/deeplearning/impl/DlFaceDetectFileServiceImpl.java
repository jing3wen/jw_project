package com.jw_server.service.deeplearning.impl;

import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jw_server.core.common.ResponseResult;
import com.jw_server.core.constants.HttpCode;
import com.jw_server.core.exception.ServiceException;
import com.jw_server.core.fileUpload.FileUploadUtils;
import com.jw_server.core.utils.socketToPython.DetectFileUtils;
import com.jw_server.dao.deeplearning.dto.QueryDlFaceDetectFileDTO;
import com.jw_server.dao.deeplearning.entity.DlFaceDetectFile;
import com.jw_server.dao.deeplearning.mapper.DlFaceDetectFileMapper;
import com.jw_server.service.deeplearning.IDlFaceDetectFileService;
import com.jw_server.service.system.ISysUserService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

import static com.jw_server.core.constants.FaceDetectConst.*;
import static com.jw_server.core.enums.FilePathEnum.FACE_DETECT_FILE;


/**
 * Description 检测人脸文件表 服务实现类
 * Author jingwen
 * Date 2022-09-22 17:31:09
 **/
@Service
public class DlFaceDetectFileServiceImpl extends ServiceImpl<DlFaceDetectFileMapper, DlFaceDetectFile> implements IDlFaceDetectFileService {

    @Resource
    private FileUploadUtils fileUploadUtils;

    @Resource
    private DetectFileUtils detectFileUtils;

    @Resource
    private ISysUserService sysUserService;

    @Resource
    private DlFaceDetectFileMapper dlFaceDetectFileMapper;

    private static final Log logger = LogFactory.getLog(IDlFaceDetectFileService.class);

    @Override
    public IPage<DlFaceDetectFile> getDetectFilePageList(QueryDlFaceDetectFileDTO dlFaceDetectFileDTO) {
        LambdaQueryWrapper<DlFaceDetectFile> queryWrapper = new LambdaQueryWrapper<>();
        if(StrUtil.isNotEmpty(dlFaceDetectFileDTO.getFileName())){
            queryWrapper.like(DlFaceDetectFile::getFileName, dlFaceDetectFileDTO.getFileName());
        }
        if(dlFaceDetectFileDTO.getDetectStatus()!= null){
            queryWrapper.like(DlFaceDetectFile::getDetectStatus, dlFaceDetectFileDTO.getDetectStatus());
        }
        return page(new Page<>(dlFaceDetectFileDTO.getPageNum(),
                        dlFaceDetectFileDTO.getPageSize()),
                queryWrapper);
    }

    @Override
    public String uploadDetectFile(MultipartFile file) {

        return fileUploadUtils.fileUpload(file, FACE_DETECT_FILE.getPath()+TO_DETECT_FILE+"/");
    }

    @Override
    public void addDetectFile(DlFaceDetectFile addFaceDetectFile) {
        //更新上传的检测文件名
        addFaceDetectFile.setFileAddress(getNewDetectedFileAddress(addFaceDetectFile.getFileAddress()));
        save(addFaceDetectFile);
    }

    @Override
    public void deleteFaceDetectFile(List<Integer> ids) {
        //删除检测文件和检测结果文件
        ids.forEach(deleteFileId ->{
            DlFaceDetectFile deleteFile = getOne(new LambdaQueryWrapper<DlFaceDetectFile>()
                    .select(DlFaceDetectFile::getId, DlFaceDetectFile::getFileAddress, DlFaceDetectFile::getResultFileAddress)
                    .eq(DlFaceDetectFile::getId, deleteFileId));

            System.out.println(deleteFile);
            if(StrUtil.isNotBlank(deleteFile.getFileAddress())){
                logger.info("删除检测文件--"+deleteFile.getFileAddress());
                fileUploadUtils.fileDelete(deleteFile.getFileAddress());
            }


            if(StrUtil.isNotBlank(deleteFile.getResultFileAddress())){
                logger.info("删除检测文件--"+deleteFile.getFileAddress()+" 的检测结果--"+deleteFile.getResultFileAddress());
                fileUploadUtils.fileDelete(deleteFile.getResultFileAddress());
            }

        });
        removeByIds(ids);
    }

    @Override
    public Integer getDetectStatus(Integer detectFileId) {

        //TODO 此处用redis比较好
        return dlFaceDetectFileMapper.getFileDetectStatus(detectFileId);
    }

    /**
     * Description: 修改检测文件名称
     * /static/upload/path1/name1.jpg  ->  /static/upload/path1/name1_undetected.jpg
     * Author: jingwen
     * Date: 2022/9/13 20:23
     **/
    public String getNewDetectedFileAddress(String fileUrl){
        /*
         * 重命名文件名
         * /static/upload/define_face/65sga72s462n.jpg  ->  /static/upload/define_face/65sga72s462n_undetected.jpg
         **/
        String originalFileName = FileNameUtil.getPrefix(fileUrl);
        String newFileNameUrl = fileUrl.replace(originalFileName, originalFileName+"_undetected");
        fileUploadUtils.fileRename(fileUrl, newFileNameUrl);
        return newFileNameUrl;
    }


    /**
     * Description: 异步调用检测请求
     * Author: jingwen
     * Date: 2022/9/24 10:55
     **/
    @Async
    public void asyncDetectedFile(DlFaceDetectFile detectFile){
        /**
         * jingwen:
         * 此处调用socket对文件进行检测
         * res格式: {
         * 				"code":200
         *              "message":"识别成功"
         *              "data":{
         *              		"recognition_face_list": ["yuwenxing"],
         *              		"save_file_path": "D:/SpringBoot_v2-master_upload/my_upload/detection_result/jingwen_detected.jpg"
         *              		//视频时  'recognition_face_list': [],
         *              	    //	     'student_course_score_dict': {'lixian': 2773, 'huge': 4148, 'wangyibo': 1}
         *              	    //		 "save_file_path": "D:/SpringBoot_v2-master_upload/my_upload/detection_result/123/123/jingwen_detected.jpg"
         *
         *              	}
         *
         *
         *          }
         * **/
        //开始检测, 更新文件检测状态 0(未检测) -> 1(检测中)
        dlFaceDetectFileMapper.updateFileDetectStatus(detectFile.getId(), 1);

        //要检测的文件路径
        String filePath = fileUploadUtils.fileUrlToFilePath(detectFile.getFileAddress());
        //读取返回res的data，即res.data
        JSONObject resData=new JSONObject();
        /**
         * //定义返回给前端的数据，recognitionFaceList识别出的人脸,studentCourseScoreDict:学生得分，二选一
         * 若检测类型为图片，返回结果为recognitionFaceList（列表）
         * 若检测类型为视频，返回结果为studentCourseScoreDict（json数据）
         **/
        JSONArray recognitionFaceList=new JSONArray();
        JSONObject studentCourseScoreDict=new JSONObject();
        //定义检测结果
        JSONObject detectionResult=new JSONObject();
        if(detectFile.getFileType().equals("image")){
            try {
                resData = detectFileUtils.remoteCall_predictImageOrVideo(200,
                        "call_SocketService",
                        "predictImage()",
                        filePath,
                        Integer.parseInt(detectFile.getSaveResult())).getJSONObject("data");
            }catch(Exception e){
                throw new ServiceException(HttpCode.CODE_400,"调用检测服务器异常");
            }
            recognitionFaceList=resData.getJSONArray("recognition_face_list");
        }if(detectFile.getFileType().equals("video")){
            try {
                resData = detectFileUtils.remoteCall_predictImageOrVideo(200,
                        "call_SocketService",
                        "predictVideo()",
                        filePath,
                        Integer.parseInt(detectFile.getSaveResult())).getJSONObject("data");
            }catch(Exception e){
                throw new ServiceException(HttpCode.CODE_400,"调用检测服务器异常");
            }
            recognitionFaceList=resData.getJSONArray("recognition_face_list");
        }
        //System.out.println("resData:"+resData);
        String detectedFilePath=resData.getString("save_file_path");
        //定义检测文件结果地址
        String file_detected_url=fileUploadUtils.filePathToFileUrl(detectedFilePath);
        // 更新检测文件的信息
        detectFile.setDetectStatus(2);
        detectFile.setResultFileAddress(file_detected_url);
        detectFile.setResultMsg("检测到的人脸: "+recognitionFaceList.toString());
        detectFile.setUpdateBy(sysUserService.getCurrentLoginUser().getUsername());
        detectFile.setUpdateTime(LocalDateTime.now());
        updateById(detectFile);
        //TODO 检测视频过程很长，检测完成前，前端的axios报异常会超时，后续可通过webSocket主动向前端发送消息
    }
}
