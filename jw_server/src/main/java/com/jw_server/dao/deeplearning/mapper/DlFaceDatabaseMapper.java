package com.jw_server.dao.deeplearning.mapper;

import com.jw_server.dao.deeplearning.entity.DlFaceDatabase;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;


/**
 * Description 人脸库 Mapper 接口
 * Author jingwen
 * Date 2022-09-13 17:21:26
 **/
@Mapper
public interface DlFaceDatabaseMapper extends BaseMapper<DlFaceDatabase> {

    void updateImageAddressWhenDelete(String imageAddress);
}
