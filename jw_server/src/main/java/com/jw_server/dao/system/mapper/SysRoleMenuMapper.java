package com.jw_server.dao.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jw_server.dao.system.entity.SysRoleMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * Description 角色菜单关联表 Mapper 接口
 * Author jingwen
 * Date 2022-08-31 11:17:36
 **/
@Mapper
public interface SysRoleMenuMapper extends BaseMapper<SysRoleMenu> {

    /**
     * 根据角色id查询所有菜单
     */
    List<Integer> selectMenuByRoleId(Integer RoleId);


    /**
     * Description: 删除角色所有绑定的菜单信息
     * Author: jingwen
     * Date: 2022/9/6 13:47
     **/
    int deleteRoleMenuByRoleId(@Param("roleId") Integer roleId);

    /**
     * Description: 删除菜单所有绑定的角色信息
     * Author: jingwen
     * Date: 2022/9/6 13:47
     **/
    int deleteRoleMenuByMenuId(@Param("menuId") Integer menuId);


    /**
     * Description: 批量新增角色菜单关系
     * Author: jingwen
     * Date: 2022/9/11 15:24
     **/
    int insertBatchRoleMenu(List<SysRoleMenu> roleMenuList);
}
