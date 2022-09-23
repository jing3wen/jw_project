package com.jw_server.service.deeplearning;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jw_server.core.common.ResponseResult;
import com.jw_server.dao.deeplearning.dto.QueryDlFaceDetectFileDTO;
import com.jw_server.dao.deeplearning.entity.DlFaceDetectFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


/**
 * Description 检测人脸文件表 服务类
 * Author jingwen
 * Date 2022-09-22 17:31:09
 **/
public interface IDlFaceDetectFileService extends IService<DlFaceDetectFile> {

    /**
     * Description: 获取检测文件列表
     * Author: jingwen
     * Date: 2022/9/22 17:57
     **/
    IPage<DlFaceDetectFile> getDetectFilePageList(QueryDlFaceDetectFileDTO dlFaceDetectFileDTO);

    /**
     * Description: 上传待检测的文件
     * Author: jingwen
     * Date: 2022/9/22 18:06
     **/
    String uploadDetectFile(MultipartFile file);

    /**
     * Description: 添加检测文件
     * Author: jingwen
     * Date: 2022/9/22 18:15
     **/
    void addDetectFile(DlFaceDetectFile dlFaceDetectFile);

    /**
     * Description: 删除检测文件和检测结果
     * Author: jingwen
     * Date: 2022/9/22 20:14
     **/
    void deleteFaceDetectFile(List<Integer> ids);

    /**
     * Description: 检测文件
     * Author: jingwen
     * Date: 2022/9/22 22:33
     **/
    ResponseResult detectFaceFile(DlFaceDetectFile detectFile);
}
