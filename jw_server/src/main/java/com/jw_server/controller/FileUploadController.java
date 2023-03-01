package com.jw_server.controller;

import com.jw_server.core.aop.logAspect.SysLog;
import com.jw_server.core.common.ResponseResult;
import com.jw_server.service.FileUploadService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import static com.jw_server.core.constants.LogModuleConst.FileUploadModule;
import static com.jw_server.core.constants.LogTypeConst.*;


/**
 * Description: 文件上传controller类
 * Author: jingwen
 * DATE: 2023/1/9 21:51
 */
@RestController
@RequestMapping("/file")
public class FileUploadController {

    @Resource
    private FileUploadService fileUploadService;

    private static final Log logger = LogFactory.getLog(FileUploadController.class);


    /**
     * Description: 文件上传
     * Author: jingwen
     * Date: 2023/1/9 21:54
     **/
    @SysLog(logModule=FileUploadModule, logType = UPLOAD, logDesc = "上传文件", saveRequestParam = false)
    @PreAuthorize("hasAuthority('file:uploadFile')")
    @PostMapping("/fileUpload/**")
    public ResponseResult fileUpload(HttpServletRequest request,@RequestParam MultipartFile file){
        String requestUrl = request.getRequestURI();
        logger.info("--上传文件--请求url为: "+requestUrl);
        String fileUrl= fileUploadService.fileUpload(file, requestUrl);
        return ResponseResult.success(fileUrl);
    }


    /**
     * Description: 删除上传文件
     * Author: jingwen
     * Date: 2023/1/13 18:27
     **/
    @SysLog(logModule=FileUploadModule, logType = DELETE, logDesc = "删除上传文件")
    @PreAuthorize("hasAuthority('file:deleteUploadFile')")
    @DeleteMapping("/deleteUploadFile/**")
    public ResponseResult deleteUploadFile(@RequestBody String fileUrl){
        logger.info("--删除上传文件--文件存储地址: "+fileUrl);
        fileUploadService.deleteUploadFile(fileUrl);
        return ResponseResult.success();
    }






}
