package com.jw_server.service.system.impl;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jw_server.core.constants.HttpCode;
import com.jw_server.core.exception.ServiceException;
import com.jw_server.dao.system.entity.SysRole;
import com.jw_server.dao.system.entity.SysUser;
import com.jw_server.dao.system.mapper.SysRoleMapper;
import com.jw_server.dao.system.mapper.SysUserMapper;
import com.jw_server.dao.system.mapper.SysUserRoleMapper;
import com.jw_server.dao.system.vo.SysMenuVO;
import com.jw_server.dao.system.vo.UserDetailsVO;
import com.jw_server.service.system.ISysMenuService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
    private SysUserRoleMapper sysUserRoleMapper;

    @Resource
    private SysRoleMapper sysRoleMapper;

    @Resource
    private ISysMenuService sysMenuService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // 查询用户信息
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getUsername, username);
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
        List<String> roleList = new ArrayList<>();
        List<Integer> roleIds = sysUserRoleMapper.selectRoleIdByUserId(queryUser.getId());
        for(Integer roleId : roleIds){
            LambdaQueryWrapper<SysRole> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(SysRole::getId, roleId);
            queryWrapper.eq(SysRole::getStatus, "1");
            queryWrapper.select(SysRole::getRoleName);
            if(sysRoleMapper.selectOne(queryWrapper)!=null){
                roleList.add(sysRoleMapper.selectOne(queryWrapper).getRoleName());
            }

        }

        //查询所有菜单信息（目录，菜单，按钮）
        List<SysMenuVO> menuVOList = sysMenuService.selectMenusByUserId(queryUser.getId(), false);
        //存储所有权限按钮
        List<String> permissionList = new ArrayList<>();
        //存储菜单目录树
        List<SysMenuVO> menuAndDirectoryTree = new ArrayList<>();
        if(menuVOList.size()>0){
            //取出所有菜单信息的按钮
            List<SysMenuVO> permissionMenuVOList = menuVOList.stream().filter(menuVO -> menuVO.getMenuType().equals("button")).collect(Collectors.toList());
            for (SysMenuVO permissionMenuVO: permissionMenuVOList){
                //防止数据库中有错误的数据
                if(StrUtil.isNotEmpty(permissionMenuVO.getPerms()))
                    permissionList.add(permissionMenuVO.getPerms());
            }

            //取出所有菜单信息的目录和菜单，并构建成菜单树
            List<SysMenuVO> menuAndDirectoryList = menuVOList.stream()
                    .filter(menuVO -> menuVO.getMenuType().equals("directory")||menuVO.getMenuType().equals("menu"))
                    .collect(Collectors.toList());
            menuAndDirectoryTree = sysMenuService.buildTree(menuAndDirectoryList);
        }


        // 把数据封装成UserDetails返回
        return UserDetailsVO.builder()
                .id(queryUser.getId())
                .username(queryUser.getUsername())
                .password(queryUser.getPassword())
                .nickname(queryUser.getNickname())
                .avatarUrl(queryUser.getAvatar())
                .roleList(roleList)
                .permissionList(permissionList)
                .menuAndDirectoryList(menuAndDirectoryTree)
                .build();
    }
}
