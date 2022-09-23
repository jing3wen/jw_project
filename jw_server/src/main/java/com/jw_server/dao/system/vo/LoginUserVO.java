package com.jw_server.dao.system.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Description: 登录用户返回类，属性和UserDetailsVO相同，该类主要返回给前端，UserDetailsVO主要是写入缓存
 * Author: jingwen
 * DATE: 2022/8/31 14:43
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginUserVO {

    private Integer id;

    private String username;

    private String password;

    private String nickname;

    private String avatarUrl;

    private String token;

    private List<String> roleList;

    // permissions只有权限按钮，没有权限菜单
    private List<String> permissionList;

    // 用户权限菜单信息, 不含权限按钮
    private List<SysMenuVO> menuAndDirectoryList;
}
