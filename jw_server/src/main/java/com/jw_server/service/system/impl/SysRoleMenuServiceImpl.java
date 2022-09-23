package com.jw_server.service.system.impl;

import com.jw_server.dao.system.entity.SysRoleMenu;
import com.jw_server.dao.system.mapper.SysRoleMenuMapper;
import com.jw_server.service.system.ISysRoleMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Description 角色菜单关联表 服务实现类
 * Author jingwen
 * Date 2022-08-31 11:17:36
 **/
@Service
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuMapper, SysRoleMenu> implements ISysRoleMenuService {

    @Resource
    private SysRoleMenuMapper sysRoleMenuMapper;

    @Transactional  //该注解可以保证删除过程和插入过程同步，有一个失败就都会回滚
    @Override
    public void updateRoleMenu(Integer roleId, List<Integer> menuIds) {
        //先根据roleId删除该角色id的所有菜单绑定关系
        sysRoleMenuMapper.deleteRoleMenuByRoleId(roleId);
        // 在把前端传过来的菜单id绑定到该角色roleId上
        List<SysRoleMenu> addList = new ArrayList<>();
        for(Integer menuId : menuIds){
            SysRoleMenu addRoleMenu = new SysRoleMenu();
            addRoleMenu.setRoleId(roleId);
            addRoleMenu.setMenuId(menuId);
            addList.add(addRoleMenu);
        }
        sysRoleMenuMapper.insertBatchRoleMenu(addList);

    }

    @Override
    public List<Integer> getMenuIdsByRoleId(Integer roleId) {
        return sysRoleMenuMapper.selectMenuByRoleId(roleId);
    }

}
