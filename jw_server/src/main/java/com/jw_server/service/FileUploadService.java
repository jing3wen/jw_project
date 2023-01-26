package com.jw_server.service;

import com.jw_server.core.constants.HttpCode;
import com.jw_server.core.exception.ServiceException;
import com.jw_server.core.fileUpload.FileUploadUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 *
 * Description:
 * Author: jingwen
 * Date: 2023/1/9 21:58
 **/
@Service
public class FileUploadService {

    @Resource
    private FileUploadUtils fileUploadUtils;

    private static final Log logger = LogFactory.getLog(FileUploadService.class);

    /**
     * 解析请求路径，构建文件夹
     **/

    public String fileUpload(MultipartFile file, String requestUrl) {
        String filePath = "";
        if(requestUrl.contains("/file/fileUpload/")){
            filePath = requestUrl.replace("/file/fileUpload/","");
        }else{
            throw new ServiceException(HttpCode.CODE_400, "上传文件url有误");
        }
        if (!filePath.endsWith("/")){
            filePath = filePath+"/";
        }
        return fileUploadUtils.fileUpload(file,filePath);
    }

    /**
     * 删除上传文件
     **/
    public void deleteUploadFile(String fileUrl) {

        fileUploadUtils.fileDelete(fileUrl);

    }
}
