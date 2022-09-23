package com.jw_server.dao.system.mapper;

import com.jw_server.dao.system.entity.SysUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


/**
 * Description 系统用户表 Mapper 接口
 * Author jingwen
 * Date 2022-08-29 16:21:58
 **/
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

    /**
     * Description: 根据用户Id查询密码
     * Author: jingwen
     * Date: 2022/9/10 20:28
     **/
    String selectPasswordByUserId(Integer userId);

    /**
     * Description: 更新用户密码
     * Author: jingwen
     * Date: 2022/9/10 20:30
     **/
    void updatePasswordByUserId(Integer userId, String password);

}
