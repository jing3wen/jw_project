package com.jw_server.controller;

import com.jw_server.core.common.ResponseResult;
import com.jw_server.service.FileUploadService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Description: 文件上传controller类
 * Author: jingwen
 * DATE: 2023/1/9 21:51
 */
@RestController
@RequestMapping("/fileUpload")
public class FileUploadController {

    @Resource
    private FileUploadService fileUploadService;

    private static final Log logger = LogFactory.getLog(FileUploadController.class);


    /**
     * Description: 文件上传
     * Author: jingwen
     * Date: 2023/1/9 21:54
     **/
    @PostMapping("/**")
    public ResponseResult fileUpload(HttpServletRequest request,@RequestParam MultipartFile file){
        String requestUrl = request.getRequestURI();
        logger.info("--上传文件--请求url为: "+requestUrl);
        String fileUrl= fileUploadService.fileUpload(file, requestUrl);
        return ResponseResult.success(fileUrl);
    }





}
