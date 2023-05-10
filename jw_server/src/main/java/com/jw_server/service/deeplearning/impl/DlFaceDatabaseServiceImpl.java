package com.jw_server.service.deeplearning.impl;

import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jw_server.core.constants.HttpCode;
import com.jw_server.core.exception.ServiceException;
import com.jw_server.core.fileUpload.FileUploadUtils;
import com.jw_server.dao.deeplearning.dto.QueryDlFaceDataDTO;
import com.jw_server.dao.deeplearning.entity.DlFaceDatabase;
import com.jw_server.dao.deeplearning.mapper.DlFaceDatabaseMapper;
import com.jw_server.service.deeplearning.IDlFaceDatabaseService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

import static com.jw_server.core.constants.FaceDetectConst.DEFINED_FACE;
import static com.jw_server.core.constants.FaceDetectConst.UNDEFINED_FACE;
import static com.jw_server.core.enums.FilePathEnum.FACE_DATABASE;

/**
 * Description 人脸库 服务实现类
 * Author jingwen
 * Date 2022-09-13 17:21:26
 **/
@Service
public class DlFaceDatabaseServiceImpl extends ServiceImpl<DlFaceDatabaseMapper, DlFaceDatabase> implements IDlFaceDatabaseService {

    @Value("${dlFaceDetect.autoUpdateFaceDatabase}")
    private Boolean autoUpdateFaceDatabase;

    @Resource
    private FileUploadUtils fileUploadUtils;

    @Resource
    private DlFaceDatabaseMapper dlFaceDatabaseMapper;

    private static final Log logger = LogFactory.getLog(IDlFaceDatabaseService.class);

    @Override
    public String uploadFaceImage(MultipartFile file) {

        /*
         * eg: deep_learning/face_detect/face_database/undefine_face/2022-9-13-7gg8h7s61j2s42s.jpg
         **/

        return fileUploadUtils.fileUpload(file, FACE_DATABASE.getPath()+UNDEFINED_FACE+"/");
    }

    @Override
    public void addFaceDatabase(DlFaceDatabase addFaceData) {
        /*
         * 先检查是否有同名人脸，没有再更改人脸路径信息，然后写入数据库
         **/
        /*
         * TODO 后续版本中对人脸库可以取消同名限制，改成自动添加后缀修改
         * eg: jin_1 , jin_2, jin_3
         */
        DlFaceDatabase findSameOne = dlFaceDatabaseMapper.selectOne(new LambdaQueryWrapper<DlFaceDatabase>()
                .select(DlFaceDatabase::getId)
                .eq(DlFaceDatabase::getFaceName, addFaceData.getFaceName()));
        if(ObjectUtil.isNotEmpty(findSameOne)){
            throw new ServiceException(HttpCode.CODE_400, "发现同名人脸，请更名");
        }
        /*
         * 更换图片初始名称
         **/
        addFaceData.setImageAddress(getNewFaceImageAddress(addFaceData.getImageAddress(), addFaceData.getFaceName()));
        if(autoUpdateFaceDatabase){
            //TODO 更新人脸库，建议异步实现
            System.out.println("自动更新人脸库");
        }
        dlFaceDatabaseMapper.insert(addFaceData);
    }

    @Override
    public IPage<DlFaceDatabase> getFacePageList(QueryDlFaceDataDTO queryDlFaceDataDTO) {
        LambdaQueryWrapper<DlFaceDatabase> queryWrapper = new LambdaQueryWrapper<>();
        if(StrUtil.isNotEmpty(queryDlFaceDataDTO.getFaceName())){
            queryWrapper.like(DlFaceDatabase::getFaceName, queryDlFaceDataDTO.getFaceName());
        }
        return page(new Page<>(queryDlFaceDataDTO.getPageNum(),
                        queryDlFaceDataDTO.getPageSize()),
                queryWrapper);
    }

    @Override
    public void deleteFaceData(List<Integer> faceIds) {
        faceIds.forEach(faceId->{
            String fileUrl = getOne(new LambdaQueryWrapper<DlFaceDatabase>()
                    .select(DlFaceDatabase::getImageAddress)
                    .eq(DlFaceDatabase::getId, faceId)).getImageAddress();
            if(StrUtil.isNotEmpty(fileUrl))
                fileUploadUtils.fileDelete(fileUrl);
        });
        removeByIds(faceIds);
    }

    @Override
    public void updateFace(DlFaceDatabase updateFace) {

        /*
         * 图片路径修改, 图片名称修改均涉及到文件的变化
         **/
        DlFaceDatabase oldFace = getOne(new LambdaQueryWrapper<DlFaceDatabase>()
                .select(DlFaceDatabase::getFaceName, DlFaceDatabase::getImageAddress)
                .eq(DlFaceDatabase::getId ,updateFace.getId()));
        /*
         * 图片路径修改（更新图片）：对新人脸图片重命名+删除旧人脸图片
         * 图片名称修改，图片路径不变：对人脸图片重命名
         **/
        if (!updateFace.getImageAddress().equals(oldFace.getImageAddress())){
            //对上传的新图片重命名
            updateFace.setImageAddress(getNewFaceImageAddress(updateFace.getImageAddress(), updateFace.getFaceName()));
            //删除原图片
            if(StrUtil.isNotBlank(oldFace.getImageAddress())){
                fileUploadUtils.fileDelete(oldFace.getImageAddress());
            }
        }
        if (updateFace.getImageAddress().equals(oldFace.getImageAddress()) && !updateFace.getFaceName().equals(oldFace.getFaceName())){
            //对原图片重命名
            updateFace.setImageAddress(getNewFaceImageAddress(updateFace.getImageAddress(), updateFace.getFaceName()));
        }
        updateById(updateFace);
    }

    @Override
    public void updateImageAddressWhenDeleteFile(String imageUrl) {
        if(imageUrl.contains(DEFINED_FACE)){
            logger.info("检测到要删除的图片已经写入数据库，先更新数据库信息");
        }
        dlFaceDatabaseMapper.updateImageAddressWhenDelete(imageUrl);
    }

    /**
     * Description: 修改图片名称
     * /static/upload/path1/name1.jpg  ->  /static/upload/path2/name2_1.jpg
     * Author: jingwen
     * Date: 2022/9/13 20:23
     **/
    public String getNewFaceImageAddress(String oldFileUrl, String faceName){
        /*
         * 重命名路径上的名称
         * /static/upload/undefine_face/65sga72s462n.jpg  ->  /static/upload/define_face/65sga72s462n.jpg
         **/

        String newFilePathUrl = oldFileUrl;
        // 操作的图片来自于undefined_face，需要将图片转移到defined_face
        if(newFilePathUrl.contains(UNDEFINED_FACE)){
            newFilePathUrl=newFilePathUrl.replace(UNDEFINED_FACE, DEFINED_FACE);
            fileUploadUtils.fileRename(oldFileUrl, newFilePathUrl);
        }

        /*
         * 重命名文件名
         * /static/upload/define_face/65sga72s462n.jpg  ->  /static/upload/define_face/name_1.jpg
         **/
        String originalFileName = FileNameUtil.getPrefix(oldFileUrl);
        //TODO 后续版本中对人脸库可以取消同名限制，改成自动添加后缀修改
        String newFileNameUrl = newFilePathUrl.replace(originalFileName, faceName+"_1");
        fileUploadUtils.fileRename(newFilePathUrl, newFileNameUrl);
        return newFileNameUrl;
    }


}
