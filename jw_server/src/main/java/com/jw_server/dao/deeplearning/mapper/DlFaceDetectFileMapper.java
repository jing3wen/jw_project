package com.jw_server.dao.deeplearning.mapper;

import com.jw_server.dao.deeplearning.entity.DlFaceDetectFile;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;


/**
 * Description 检测人脸文件表 Mapper 接口
 * Author jingwen
 * Date 2022-09-22 17:31:09
 **/
@Mapper
public interface DlFaceDetectFileMapper extends BaseMapper<DlFaceDetectFile> {

    /**
     * Description: 获取检测文件状态
     * Author: jingwen
     * Date: 2022/9/22 23:02
     **/
    Integer getFileDetectStatus(Integer id);

    /**
     * Description: 更新文件检测状态
     * Author: jingwen
     * Date: 2022/9/22 23:19
     **/
    void updateFileDetectStatus(Integer fileId, Integer detectStatus);
}
