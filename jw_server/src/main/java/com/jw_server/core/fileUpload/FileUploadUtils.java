package com.jw_server.core.fileUpload;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * Description: 文件上传工具类
 * Author: jingwen
 * DATE: 2022/9/10 15:00
 */
@Component
public class FileUploadUtils {

    @Value("${upload.mode}")
    private String uploadMode;

    @Resource
    private LocalUploadService localUploadService;

    /**
     * Description: 文件上传
     * Author: jingwen
     * Date: 2022/9/10 15:59
     **/
    public String fileUpload(MultipartFile file, String path){
        String url = "";
        if (uploadMode.equals("local")){
            try {
                url = localUploadService.fileUpload(file, path);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }else {
            throw new RuntimeException("配置文件中文件上传模式有误");
        }

        return url;
    }

    /**
     * Description: 文件删除
     * Author: jingwen
     * Date: 2022/9/10 15:59
     **/
    public void fileDelete(String fileUrl){
        if(uploadMode.equals("local")){
            localUploadService.fileDelete(fileUrl);
        }else {
            throw new RuntimeException("配置文件中文件上传模式有误");
        }
    }

    /**
     * Description: 文件或者文件夹重命名
     * eg1: /static/upload/test1/test1.jpg  ->  /static/upload/test1/test2.jpg
     * eg2:/static/upload/test1  ->  /static/upload/test2
     * Author: jingwen
     * Date: 2022/9/13 22:36
     **/
    public void fileRename(String oldFileUrl, String newFileUrl){
        if(uploadMode.equals("local")){
            localUploadService.fileRename(oldFileUrl, newFileUrl);
        }else {
            throw new RuntimeException("配置文件中文件上传模式有误");
        }
    }

    /**
     * Description: 根据文件url获取本地存储路径，此方法专供本地上传使用，切勿使用oss
     * Author: jingwen
     * Date: 2022/9/22 22:42
     **/
    public String fileUrlToFilePath(String fileUrl){
        if(uploadMode.equals("local")){
            return localUploadService.fileUrlToFilePath(fileUrl);
        }else {
            throw new RuntimeException("配置文件中文件上传模式有误");
        }
    }

    /**
     * Description: 根据文件本地路径转化成url，此方法专供本地上传使用，切勿使用oss
     * Author: jingwen
     * Date: 2022/9/22 22:42
     **/
    public String filePathToFileUrl(String filePath){
        if(uploadMode.equals("local")){
            return localUploadService.filePathToFileUrl(filePath);
        }else {
            throw new RuntimeException("配置文件中文件上传模式有误");
        }
    }

}
