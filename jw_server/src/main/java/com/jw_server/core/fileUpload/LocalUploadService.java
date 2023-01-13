package com.jw_server.core.fileUpload;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.jw_server.core.constants.HttpCode;
import com.jw_server.core.exception.ServiceException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;

/**
 * Description: 本地上传服务
 * Author: jingwen
 * DATE: 2022/9/10 15:16
 */
@Service
public class LocalUploadService {

    @Value("${upload.local.path}")
    private String localPath;

    private static final Log logger = LogFactory.getLog(LocalUploadService.class);

    /**
     * Description: 本地文件上传
     * Author: jingwen
     * Date: 2022/9/10 15:52
     **/
    public String fileUpload(MultipartFile file, String path) throws Exception{
        //获取文件原始名字和类型
        /*
         * 注意：不建议用md5作为文件名, 虽然能节省空间，但是容易造成冲突
         **/
        String uuid = IdUtil.simpleUUID();
        String originalFileName = file.getOriginalFilename();
        String type = FileUtil.extName(originalFileName);
        // 定义新的文件名
        String fileName = DateUtil.today()+"-"+uuid+ StrUtil.DOT+type;
        // eg 保存路径为 D:/IDEA/project/jw_project/static/upload/avatar/2022-8-20-5as5d4a1d5asd1asd.png
        File uploadFile = new File(localPath+path+fileName);
        //判断文件是否已经存在，存在就直接返回url
        if (FileUtil.exist(uploadFile)){
            return "/static/upload/"+path+fileName;
        }
        // 创建文件
        // 判断配置的文件目录是否存在，若不存在则创建一个新的文件目录
        File parentFile = uploadFile.getParentFile();
        if(!parentFile.exists()) {
            logger.info("创建文件夹--"+parentFile);
            parentFile.mkdirs();
        }
        logger.info("存储文件--"+uploadFile.getPath());
        file.transferTo(uploadFile);
        return "/static/upload/"+path+fileName;
    }

    /**
     * Description: 本地文件删除
     * Author: jingwen
     * Date: 2022/9/10 15:52
     * Param: fileUrl = /static/upload/avatar/2022-8-20-5as5d4a1d5asd1asd.png
     **/
    public void fileDelete(String fileUrl){
        String fileName = fileUrl.replace("/static/upload/","");
        if(FileUtil.exist(localPath+fileName) && StrUtil.isNotBlank(fileUrl)){
            logger.info("文件删除--文件存在，准备删除");
            FileUtil.del(localPath+fileName);
//            File deleteFile = new File(localPath+fileName);
//            deleteFile.delete();
        } else{
            logger.error("文件删除--错误--文件不存在");
            throw new ServiceException(HttpCode.CODE_400, "文件删除错误");
        }
    }

    /**
     * Description: 文件或者文件夹重命名
     * eg1: /static/upload/test1/test1.jpg  ->  /static/upload/test1/test2.jpg
     * eg2:/static/upload/test1  ->  /static/upload/test2
     * Author: jingwen
     * Date: 2022/9/13 22:36
     **/
    public void fileRename(String oldFileUrl, String newFileUrl) {
        String oldFileName = oldFileUrl.replace("/static/upload/","");
        String newFileName = newFileUrl.replace("/static/upload/","");
        if(FileUtil.exist(localPath+oldFileName)){
            File oldFile = new File(localPath+oldFileName);
            File newFile = new File(localPath+newFileName);
            File parentFile = newFile.getParentFile();
            if(!parentFile.exists()){
                parentFile.mkdirs();
            }
            oldFile.renameTo(newFile);
        } else{
            throw new ServiceException(HttpCode.CODE_400, "要重命名的文件不存在");
        }
    }

    /**
     * Description: 根据文件url获取本地存储路径，此方法专供本地上传使用，切勿使用oss
     * Author: jingwen
     * Date: 2022/9/22 22:42
     **/
    public String fileUrlToFilePath(String fileUrl) {
        String fileName = fileUrl.replace("/static/upload/","");
        if(FileUtil.exist(localPath+fileName) && StrUtil.isNotBlank(fileUrl)){
            return localPath+fileName;
        } else{
            throw new ServiceException(HttpCode.CODE_400, "文件转化错误");
        }


    }

    /**
     * Description: 根据文件本地路径转化成url，此方法专供本地上传使用，切勿使用oss
     * Author: jingwen
     * Date: 2022/9/22 22:42
     **/
    public String filePathToFileUrl(String filePath) {
        if(FileUtil.exist(filePath) && StrUtil.isNotBlank(filePath)){
            return filePath.replace(localPath, "/static/upload/");
        } else{
            throw new ServiceException(HttpCode.CODE_400, "文件转化错误");
        }
    }
}
