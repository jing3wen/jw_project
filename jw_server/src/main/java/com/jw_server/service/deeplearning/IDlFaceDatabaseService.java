package com.jw_server.service.deeplearning;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jw_server.dao.deeplearning.dto.QueryDlFaceDataDTO;
import com.jw_server.dao.deeplearning.entity.DlFaceDatabase;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


/**
 * Description 人脸库 服务类
 * Author jingwen
 * Date 2022-09-13 17:21:26
 **/
public interface IDlFaceDatabaseService extends IService<DlFaceDatabase> {

    /**
     * Description: 上传人脸图片
     * Author: jingwen
     * Date: 2022/9/13 17:28
     **/
    String uploadFaceImage(MultipartFile file);

    /**
     * Description: 添加人脸库
     * Author: jingwen
     * Date: 2022/9/13 20:04
     **/
    void addFaceDatabase(DlFaceDatabase dlFaceDatabase);

    /**
     * Description: 分页查询
     * Author: jingwen
     * Date: 2022/9/13 21:02
     **/
    IPage<DlFaceDatabase> getFacePageList(QueryDlFaceDataDTO queryDlFaceDataDTO);


    /**
     * Description: 删除人脸，同时将人脸图片删除
     * Author: jingwen
     * Date: 2022/9/13 23:22
     **/
    void deleteFaceData(List<Integer> faceIds);

    /**
     * Description: 更新人脸信息
     * Author: jingwen
     * Date: 2022/9/15 16:16
     **/
    void updateFace(DlFaceDatabase dlFaceDatabase);

    /**
     * Description: 检测要删除的人脸是不是写入了数据库，若写入了数据库需要更新数据库中的数据
     * Author: jingwen
     * Date: 2022/9/22 15:01
     **/
    void updateImageAddressWhenDeleteFile(String imageUrl);
}
