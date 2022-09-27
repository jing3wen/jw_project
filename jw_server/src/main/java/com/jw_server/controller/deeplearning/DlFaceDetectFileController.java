package com.jw_server.controller.deeplearning;

import com.jw_server.core.aop.logAspect.SysLog;
import com.jw_server.core.common.ResponseResult;
import com.jw_server.core.constants.HttpCode;
import com.jw_server.core.exception.ServiceException;
import com.jw_server.core.fileUpload.FileUploadUtils;
import com.jw_server.dao.deeplearning.dto.QueryDlFaceDetectFileDTO;
import com.jw_server.dao.deeplearning.entity.DlFaceDetectFile;
import com.jw_server.service.deeplearning.IDlFaceDetectFileService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

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

    private static final Log logger = LogFactory.getLog(DlFaceDetectFileController.class);
    /**
     * Description 新增
     * Author jingwen
     * Date 2022-09-22 17:31:09
     **/
    @SysLog(logModule=DlFaceDetectFileModule, logType = ADD, logDesc = "新增")
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
    @GetMapping("/deleteFaceDetectFile")
    public ResponseResult deleteFaceDetectFile(@RequestParam String fileUrl){
        logger.info(DlFaceDetectFileModule+"--删除上传的检测文件:"+fileUrl);
        fileUploadUtils.fileDelete(fileUrl);
        return ResponseResult.success();
    }

    /**
     * Description: 异步检测文件
     * Author: jingwen
     * Date: 2022/9/22 22:28
     **/
    @SysLog(logModule = DlFaceDetectFileModule, logType = UPDATE, logDesc = "检测文件")
    @PostMapping("/detectFaceFile")
    public ResponseResult detectFaceFile(@RequestBody DlFaceDetectFile detectFile){

        Integer detectStatus = dlFaceDetectFileService.getDetectStatus(detectFile.getId());
        if(detectStatus==1){
            return ResponseResult.error(HttpCode.CODE_202,"文件正在检测中, 请稍等");
        }
        if(detectStatus == 2){  //文件检测完成
            return ResponseResult.success();
        }
        if(detectStatus == -1){
            return ResponseResult.error(HttpCode.CODE_400,"文件检测失败");
        }
        try{
            dlFaceDetectFileService.asyncDetectedFile(detectFile);
        }catch(Exception e){
            throw new ServiceException(HttpCode.CODE_400, "调用检测服务器异常");
        }

        return ResponseResult.error(HttpCode.CODE_202,"文件正在检测中, 请稍等");
    }
}

