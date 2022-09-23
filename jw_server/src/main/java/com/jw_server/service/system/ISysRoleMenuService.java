package com.jw_server.service.system;

import com.jw_server.dao.system.entity.SysRoleMenu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


/**
 * Description 角色菜单关联表 服务类
 * Author jingwen
 * Date 2022-08-31 11:17:36
 **/
public interface ISysRoleMenuService extends IService<SysRoleMenu> {

    /**
     * @Description 更新角色菜单信息
     * @Author jingwen
     * @Date 2022/8/22 19:52
     **/
    void updateRoleMenu(Integer roleId, List<Integer> menuIds);

    /**
     * @Description 根据roleId查询其绑定的菜单id
     * @Author jingwen
     * @Date 2022/8/22 20:47
     **/
    List<Integer> getMenuIdsByRoleId(Integer roleId);

}
