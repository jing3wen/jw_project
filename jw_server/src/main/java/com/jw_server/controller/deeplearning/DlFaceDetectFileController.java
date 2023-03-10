package com.jw_server.controller.deeplearning;

import com.jw_server.core.aop.logAspect.SysLog;
import com.jw_server.core.common.ResponseResult;
import com.jw_server.core.constants.HttpCode;
import com.jw_server.core.exception.ServiceException;
import com.jw_server.core.fileUpload.FileUploadUtils;
import com.jw_server.dao.deeplearning.dto.QueryDlFaceDetectFileDTO;
import com.jw_server.dao.deeplearning.entity.DetectFileTaskInfo;
import com.jw_server.dao.deeplearning.entity.DlFaceDetectFile;
import com.jw_server.service.deeplearning.IDlFaceDetectFileService;
import com.jw_server.service.deeplearning.impl.DetectFileTaskManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.Future;

import static com.jw_server.core.constants.LogModuleConst.DlFaceDetectFileModule;
import static com.jw_server.core.constants.LogTypeConst.*;


/**
 * author jingwen
 * Description 检测人脸文件表 前端控制器
 * Date 2022-09-22 17:31:09
 */
@RestController
@RequestMapping("/dlFaceDetectFile")
public class DlFaceDetectFileController {

    @Resource
    private IDlFaceDetectFileService dlFaceDetectFileService;

    @Resource
    private FileUploadUtils fileUploadUtils;

    @Resource
    private DetectFileTaskManager detectFileTaskManager;

    private static final Log logger = LogFactory.getLog(DlFaceDetectFileController.class);


    /**
     * Description 新增
     * Author jingwen
     * Date 2022-09-22 17:31:09
     **/
    @SysLog(logModule=DlFaceDetectFileModule, logType = ADD, logDesc = "新增")
    @PreAuthorize("hasAuthority('dl:dlFaceDetectFile:addDetectFile')")
    @PostMapping("/add")
    public ResponseResult add(@RequestBody DlFaceDetectFile dlFaceDetectFile) {
        dlFaceDetectFileService.addDetectFile(dlFaceDetectFile);
        return ResponseResult.success();
    }

    /**
     * Description 更新
     * Author jingwen
     * Date 2022-09-22 17:31:09
     **/
    @SysLog(logModule=DlFaceDetectFileModule, logType = UPDATE, logDesc = "更新")
    @PreAuthorize("hasAuthority('dl:dlFaceDetectFile:update')")
    @PostMapping("/update")
    public ResponseResult update(@RequestBody DlFaceDetectFile dlFaceDetectFile) {
        dlFaceDetectFileService.updateById(dlFaceDetectFile);
        return ResponseResult.success();
    }

    /**
     * Description 批量删除
     * Author jingwen
     * Date 2022-09-22 17:31:09
     **/
    @SysLog(logModule=DlFaceDetectFileModule, logType = DELETE, logDesc = "删除")
    @PreAuthorize("hasAuthority('dl:dlFaceDetectFile:delete')")
    @PostMapping("/deleteBatch")
    public ResponseResult deleteBatch(@RequestBody List<Integer> ids) {
        dlFaceDetectFileService.deleteFaceDetectFile(ids);
        return ResponseResult.success();
    }


    /**
     * Description 分页查询
     * Author jingwen
     * Date 2022-09-22 17:31:09
     **/
    @PreAuthorize("hasAuthority('dl:dlFaceDetectFile:query')")
    @PostMapping("/getPageList")
    public ResponseResult getPageList(@RequestBody QueryDlFaceDetectFileDTO dlFaceDetectFileDTO) {
//        System.out.println(dlFaceDetectFileDTO);
        return ResponseResult.success(dlFaceDetectFileService.getDetectFilePageList(dlFaceDetectFileDTO));
    }

    /**
     * Description: 上传检测图片
     * Author: jingwen
     * Date: 2022/9/13 17:23
     **/
    @SysLog(logModule=DlFaceDetectFileModule, logType = UPLOAD, logDesc = "上传检测图片", saveRequestParam = false)
    @PreAuthorize("hasAuthority('dl:dlFaceDetectFile:uploadDetectFile')")
    @PostMapping("/uploadFaceDetectFile")
    public ResponseResult uploadFaceDetectFile(@RequestParam MultipartFile file){
        logger.info(DlFaceDetectFileModule+"--上传检测文件");
        return ResponseResult.success(dlFaceDetectFileService.uploadDetectFile(file));
    }


    /**
     * Description: 删除上传的检测文件
     * Author: jingwen
     * Date: 2022/9/22 19:13
     **/
    @SysLog(logModule = DlFaceDetectFileModule, logType = DELETE, logDesc = "删除上传的检测文件")
    @PreAuthorize("hasAuthority('dl:dlFaceDetectFile:deleteUploadDetectFile')")
    @DeleteMapping("/deleteFaceDetectFile")
    public ResponseResult deleteFaceDetectFile(@RequestBody String fileUrl){
        logger.info(DlFaceDetectFileModule+"--删除上传的检测文件:"+fileUrl);
        fileUploadUtils.fileDelete(fileUrl);
        return ResponseResult.success();
    }


    /**
     * Description: 异步检测文件——线程池监控版本
     * Author: jingwen
     * Date: 2023/3/9 20:16
     **/
    @SysLog(logModule = DlFaceDetectFileModule, logType = UPDATE, logDesc = "检测文件")
    @PreAuthorize("hasAuthority('dl:dlFaceDetectFile:detecting')")
    @PostMapping("/detectFaceFile")
    public ResponseResult submitDetectTask(@RequestBody DlFaceDetectFile detectFile){
        detectFileTaskManager.submitDetectTask(detectFile);
        return ResponseResult.success();
    }

}

