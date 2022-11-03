package com.jw_server.dao.system.mapper;

import com.jw_server.dao.system.entity.SysRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


/**
 * Description 系统角色表 Mapper 接口
 * Author jingwen
 * Date 2022-08-31 11:17:21
 **/
@Mapper
public interface SysRoleMapper extends BaseMapper<SysRole> {

    /**
     * Description: 二表联合查询，根据用户id查找其关联的所有角色名
     * Author: jingwen
     * Date: 2022/9/2 18:14
     **/
    List<SysRole> selectAllRoleByUserId(Integer userId);


    /**
     * 根据用户id查询所有角色名
     */
    List<String> selectRoleNameListByUserId(Integer userId);

}
