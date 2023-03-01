package com.jw_server.controller.deeplearning;

import com.jw_server.core.aop.logAspect.SysLog;
import com.jw_server.core.common.ResponseResult;
import com.jw_server.core.constants.HttpCode;
import com.jw_server.core.exception.ServiceException;
import com.jw_server.core.fileUpload.FileUploadUtils;
import com.jw_server.core.utils.socketToPython.DetectFileUtils;
import com.jw_server.dao.deeplearning.dto.QueryDlFaceDataDTO;
import com.jw_server.dao.deeplearning.entity.DlFaceDatabase;
import com.jw_server.service.deeplearning.IDlFaceDatabaseService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

import static com.jw_server.core.constants.LogModuleConst.DlFaceDatabaseModule;
import static com.jw_server.core.constants.LogTypeConst.*;


/**
 * author jingwen
 * Description 人脸库 前端控制器
 * Date 2022-09-13 17:21:26
 */
@RestController
@RequestMapping("/dlFaceDatabase")
public class DlFaceDatabaseController {

    @Resource
    private IDlFaceDatabaseService dlFaceDatabaseService;

    @Resource
    private FileUploadUtils fileUploadUtils;

    @Resource
    private DetectFileUtils detectFileUtils;

    private static final Log logger = LogFactory.getLog(DlFaceDatabaseController.class);

    /**
     * Description 新增
     * Author jingwen
     * Date 2022-09-13 17:21:26
     **/
    @SysLog(logModule=DlFaceDatabaseModule, logType = ADD, logDesc = "新增人脸库")
    @PreAuthorize("hasAuthority('dl:dlFaceDatabase:addFace')")
    @PostMapping("/add")
    public ResponseResult add(@RequestBody DlFaceDatabase dlFaceDatabase) {
        dlFaceDatabaseService.addFaceDatabase(dlFaceDatabase);
        return ResponseResult.success();
    }

    /**
     * Description 更新
     * Author jingwen
     * Date 2022-09-13 17:21:26
     **/
    @SysLog(logModule=DlFaceDatabaseModule, logType = UPDATE, logDesc = "更新人脸库")
    @PreAuthorize("hasAuthority('dl:dlFaceDatabase:update')")
    @PostMapping("/update")
    public ResponseResult update(@RequestBody DlFaceDatabase dlFaceDatabase) {
        dlFaceDatabaseService.updateFace(dlFaceDatabase);
        return ResponseResult.success();
    }

    /**
     * Description 批量删除
     * Author jingwen
     * Date 2022-09-13 17:21:26
     **/
    @SysLog(logModule=DlFaceDatabaseModule, logType = DELETE, logDesc = "删除人脸库")
    @PreAuthorize("hasAuthority('dl:dlFaceDatabase:delete')")
    @PostMapping("/deleteBatch")
    public ResponseResult deleteBatch(@RequestBody List<Integer> ids) {
        dlFaceDatabaseService.deleteFaceData(ids);
        return ResponseResult.success();
    }

    /**
     * Description 分页查询
     * Author jingwen
     * Date 2022-09-13 17:21:26
     **/
    @PreAuthorize("hasAuthority('dl:dlFaceDatabase:query')")
    @PostMapping("/getPageList")
    public ResponseResult getPageList(@RequestBody QueryDlFaceDataDTO queryDlFaceDataDTO) {
        return ResponseResult.success(dlFaceDatabaseService.getFacePageList(queryDlFaceDataDTO));
    }


    /**
     * Description: 上传人脸图片
     * Author: jingwen
     * Date: 2022/9/13 17:23
     **/
    @SysLog(logModule=DlFaceDatabaseModule, logType = UPLOAD, logDesc = "上传人脸图片", saveRequestParam = false)
    @PreAuthorize("hasAuthority('dl:dlFaceDatabase:uploadFaceImage')")
    @PostMapping("/uploadFaceDatabase")
    public ResponseResult uploadFaceImage(@RequestParam MultipartFile file){
        logger.info(DlFaceDatabaseModule+"--上传人脸图片");
        return ResponseResult.success(dlFaceDatabaseService.uploadFaceImage(file));
    }

    /**
     * Description: 删除上传的人脸图片
     * 检测要删除的人脸是不是写入了数据库，若写入了数据库需要更新数据库中的数据
     * Author: jingwen
     * Date: 2022/9/14 11:48
     **/
    @SysLog(logModule=DlFaceDatabaseModule, logType = DELETE, logDesc = "删除上传的人脸图片")
    @PreAuthorize("hasAuthority('dl:dlFaceDatabase:deleteUploadFaceImage')")
    @DeleteMapping("/deleteFaceImage")
    public ResponseResult deleteFaceImage(@RequestBody String fileUrl){
        logger.info(DlFaceDatabaseModule+"--删除上传的人脸图片:"+fileUrl);
        dlFaceDatabaseService.updateImageAddressWhenDeleteFile(fileUrl);
        fileUploadUtils.fileDelete(fileUrl);
        return ResponseResult.success();
    }

    /**
     * Description: 手动更新人脸库
     * Author: jingwen
     * Date: 2022/9/22 22:10
     **/
    @SysLog(logModule=DlFaceDatabaseModule, logType = UPDATE, logDesc = "手动更新检测服务器人脸库")
    @PreAuthorize("hasAuthority('dl:dlFaceDatabase:updateFaceDatabase')")
    @GetMapping("/updateFaceDatabase")
    public ResponseResult updateFaceDatabase(){
        try{
            detectFileUtils.remoteCall_update_face_dataset();
        }catch(Exception e){
            throw new ServiceException(HttpCode.CODE_400, "调用检测服务器异常");
        }
        return ResponseResult.success();
    }
}

