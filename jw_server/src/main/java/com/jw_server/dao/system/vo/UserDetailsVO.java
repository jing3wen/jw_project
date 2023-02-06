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
 * Description: UserDetails返回类, 属性和LoginUserVO相同，
 * 该类主要用在spring security框架中存储，供框架权限校验，并写入缓存供token验证
 * Author: jingwen
 * DATE: 2022/8/29 21:02
 *
 * 注意！！！
 * Date: 2023/2/6 16:50 修改:
 * 为了照顾博客子系统的配置，增添一些属性 (邮箱,电话,性别,备注)
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

    private String avatar;

    private String token;

    private List<String> roleList;

    // permissions只有权限按钮，没有权限菜单
    private List<String> permissionList;

    //下列为新增属性
    private String email;

    private String phone;

    private String sex;

    private String remark;


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
