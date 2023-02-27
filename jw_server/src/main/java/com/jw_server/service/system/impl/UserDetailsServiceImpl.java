package com.jw_server.service.system.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jw_server.core.constants.HttpCode;
import com.jw_server.core.exception.ServiceException;
import com.jw_server.dao.system.entity.SysUser;
import com.jw_server.dao.system.mapper.SysRoleMapper;
import com.jw_server.dao.system.mapper.SysUserMapper;
import com.jw_server.dao.system.vo.UserDetailsVO;
import com.jw_server.service.system.ISysMenuService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * Description:
 * Author: jingwen
 * DATE: 2022/8/29 20:44
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private SysRoleMapper sysRoleMapper;

    @Resource
    private ISysMenuService sysMenuService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // 查询用户信息
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getUsername, username)
                    .or().eq(SysUser::getEmail, username);
        queryWrapper.select(SysUser::getId,
                SysUser::getUsername,
                SysUser::getPassword,
                SysUser::getNickname,
                SysUser::getAvatar,
                SysUser::getEmail,
                SysUser::getPhone,
                SysUser::getSex,
                SysUser::getRemark,
                SysUser::getStatus);
        SysUser queryUser = sysUserMapper.selectOne(queryWrapper);
        /*
         * Description:
         * 丢出用户名不存在的异常，供userLogin方法捕获
         */
        if (Objects.isNull(queryUser)){
            throw new ServiceException(HttpCode.CODE_403,"用户名不存在");
        }
        if (queryUser.getStatus().equals("0")){
            throw new ServiceException(HttpCode.CODE_403,"用户已被停用");
        }

        return createLoginSysUserVO(queryUser);
    }


    /**
     * 封装登陆返回类
     * param queryUser
     * return
     */
    public UserDetailsVO createLoginSysUserVO(SysUser queryUser){
        //查询用户所有角色信息
        List<String> roleList = sysRoleMapper.selectRoleNameListByUserId(queryUser.getId());
        //存储所有权限按钮
        List<String> permissionList = sysMenuService.selectPermissionsByUserId(queryUser.getId());
        // 把数据封装成UserDetails返回
        return UserDetailsVO.builder()
                .id(queryUser.getId())
                .username(queryUser.getUsername())
                .password(queryUser.getPassword())
                .nickname(queryUser.getNickname())
                .avatar(queryUser.getAvatar())
                .roleList(roleList)
                .permissionList(permissionList)
                .email(queryUser.getEmail())
                .phone(queryUser.getPhone())
                .sex(queryUser.getSex())
                .remark(queryUser.getRemark())
                .build();
    }
}
