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
import com.jw_server.core.enums.DetectFileStatusEnum;
import com.jw_server.core.exception.ServiceException;
import com.jw_server.core.fileUpload.FileUploadUtils;
import com.jw_server.core.utils.socketToPython.DetectFileUtils;
import com.jw_server.dao.deeplearning.dto.QueryDlFaceDetectFileDTO;
import com.jw_server.dao.deeplearning.entity.DlFaceDetectFile;
import com.jw_server.dao.deeplearning.mapper.DlFaceDetectFileMapper;
import com.jw_server.service.deeplearning.IDlFaceDetectFileService;
import com.jw_server.service.system.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static com.jw_server.core.constants.FaceDetectConst.*;
import static com.jw_server.core.enums.FilePathEnum.FACE_DETECT_FILE;


/**
 * Description 检测人脸文件表 服务实现类
 * Author jingwen
 * Date 2022-09-22 17:31:09
 **/
@Service
@Slf4j
public class DlFaceDetectFileServiceImpl extends ServiceImpl<DlFaceDetectFileMapper, DlFaceDetectFile> implements IDlFaceDetectFileService {

    @Resource
    private FileUploadUtils fileUploadUtils;

    @Resource
    private DetectFileUtils detectFileUtils;

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
            queryWrapper.eq(DlFaceDetectFile::getDetectStatus, dlFaceDetectFileDTO.getDetectStatus());
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
                    .select(DlFaceDetectFile::getId,
                            DlFaceDetectFile::getFileAddress,
                            DlFaceDetectFile::getResultFileAddress,
                            DlFaceDetectFile::getDetectStatus)
                    .eq(DlFaceDetectFile::getId, deleteFileId));
            if(deleteFile.getDetectStatus().equals(DetectFileStatusEnum.RUNNING.getState())
                    || deleteFile.getDetectStatus().equals(DetectFileStatusEnum.WAITING.getState())){
                throw new ServiceException(HttpCode.CODE_500,"任务已经提交, 暂时不能删除");
            }

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
    @Async("detectFileThreadPool")
    public void asyncDetectedFile(DlFaceDetectFile detectFile, String taskId){
        //开始检测, 更新文件检测状态 0(未检测) -> 1(检测中)
        dlFaceDetectFileMapper.updateFileDetectStatus(detectFile.getId(), DetectFileStatusEnum.RUNNING.getState());

        /**
         * 发送数据格式
         *{
         *   "code":200,
         *   "message": "call_SocketService",
         *   "data":{
         *       "service_name":"predictImage()",
         *        "service_params":{
         *               "image_path":"你的application.yml中的本地上传的路径/deep_learning/face_detect/face_detect_file/to_detect_file/jingwen_undetected.jpg",
         *               "save_iamge":1   # True
         *       }
         *   }
         *}
         **/
        /**
         * 此处调用socket对文件进行检测
         * res格式: {
         * 				"code":200
         *              "message":"识别成功"
         *              "data":{
         *              		"recognition_face_list": ["yuwenxing"],
         *              		"save_file_path": "你的application.yml中的本地上传的路径/deep_learning/face_detect/face_detect_file/detect_file_result/jingwen_detected.jpg"
         *              		//视频时  'recognition_face_list': ["yuwenxing", "luohong"],
         *              	    //		 "save_file_path": "你的application.yml中的本地上传的路径/deep_learning/face_detect/face_detect_file/detect_file_result/jingwen_detected_voice.mp4"
         *
         *              	}
         *
         *
         *          }
         * **/


        //要检测的文件路径
        String filePath = fileUploadUtils.fileUrlToFilePath(detectFile.getFileAddress());


        //根据文件类别来获取调用方法
        String serviceName = "";
        if(detectFile.getFileType().equals("image")){
            serviceName = "predictImage()";
        }else if(detectFile.getFileType().equals("video")){
            serviceName = "predictVideo()";
        }
        try {
            //读取返回res的data，即res.data
            JSONObject resData = detectFileUtils.remoteCall_predictImageOrVideo(200,
                    "call_SocketService",
                    serviceName,
                    filePath,
                    Integer.parseInt(detectFile.getSaveResult())).getJSONObject("data");
            log.error("[taskId="+detectFile.getId()+"]检测成功");
            /*
             * 检测成功——获取检测结果，
             * detectedFilePath检测结果存储位置
             * recognitionFaceList识别出的人脸
             **/
            String detectedFilePath=resData.getString("save_file_path");
            JSONArray recognitionFaceList=resData.getJSONArray("recognition_face_list");
            //定义检测文件结果地址
            String file_detected_url=fileUploadUtils.filePathToFileUrl(detectedFilePath);
            // 更新检测文件的信息
            detectFile.setDetectStatus(DetectFileStatusEnum.SUCCESS.getState());
            detectFile.setResultFileAddress(file_detected_url);
            detectFile.setResultMsg("检测到的人脸: "+recognitionFaceList.toString());
            updateById(detectFile);
        }catch(Exception e){
            log.error("[taskId="+detectFile.getId()+"] 检测失败");
            // 检测失败——更新检测文件的信息
            detectFile.setDetectStatus(DetectFileStatusEnum.FAILED.getState());
            detectFile.setResultMsg("调用检测服务器异常");
            updateById(detectFile);
            throw new ServiceException(HttpCode.CODE_400,"调用检测服务器异常");
        }
    }

    /**
     * Description: 更新文件检测状态
     **/
    @Override
    public void updateFileDetectStatus(Integer fileId, Integer detectStatus) {
        dlFaceDetectFileMapper.updateFileDetectStatus(fileId, detectStatus);
    }
}
