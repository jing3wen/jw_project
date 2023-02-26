package com.jw_server.dao.blog.dto;

import lombok.Data;

/**
 * Description: 前台绑定邮箱/更改绑定邮箱/忘记密码 DTO
 * Author: jingwen
 * DATE: 2023/2/25 23:57
 */
@Data
public class BlogFrontForgetPasswordOrUpdateBindDTO {

    //邮箱
    private String email;

    //电话
    private String phone;

    //验证码
    private String code;

    //新密码
    private String password;
}
