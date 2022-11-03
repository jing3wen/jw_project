package com.jw_server.dao.system.vo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Description: UserDetails返回类, 属性和UserDetailsVO相同，该类主要返回给前端，UserDetailsVO主要是写入缓存
 * Author: jingwen
 * DATE: 2022/8/29 21:02
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsVO implements UserDetails {

    private Integer id;

    private String username;

    private String password;

    private String nickname;

    private String avatarUrl;

    private String token;

    private List<String> roleList;

    // permissions只有权限按钮，没有权限菜单
    private List<String> permissionList;


    @JSONField(serialize = false)  // authorities不加入到redis中，不然编译会出bug，
    private List<SimpleGrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 把permissions中字符串类型的权限信息转换成GrantedAuthority对象存入authorities中
        if(authorities!=null){
            return authorities;
        }
        //字节流, 函数式编程
        authorities = permissionList.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        return authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @JSONField(serialize = false)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JSONField(serialize = false)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JSONField(serialize = false)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JSONField(serialize = false)
    @Override
    public boolean isEnabled() {
        return true;
    }
}
