package com.jw_server.service.system.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jw_server.dao.system.entity.SysUserRole;
import com.jw_server.dao.system.mapper.SysUserRoleMapper;
import com.jw_server.service.system.ISysUserRoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Description 用户角色关联表 服务实现类
 * Author jingwen
 * Date 2022-08-31 11:18:06
 **/
@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements ISysUserRoleService {

    @Resource
    private SysUserRoleMapper sysUserRoleMapper;

    @Override
    public void updateUserRole(Integer userId, List<Integer> roleIds) {
        sysUserRoleMapper.deleteUserRoleByUserId(userId);
        List<SysUserRole> addUserRoleList = new ArrayList<>();
        roleIds.forEach(roleId -> {
            SysUserRole addUserRole = SysUserRole.builder()
                    .userId(userId)
                    .roleId(roleId)
                    .build();
            addUserRoleList.add(addUserRole);
        });
        sysUserRoleMapper.insertBatchUserRole(addUserRoleList);
    }

    @Override
    public void deleteUserRoleByUserId(Integer userId) {
        sysUserRoleMapper.deleteUserRoleByUserId(userId);
    }
}
