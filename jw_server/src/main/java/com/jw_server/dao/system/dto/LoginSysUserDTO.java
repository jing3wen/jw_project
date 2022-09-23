package com.jw_server.dao.system.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Description: 用户登录类
 * Author: jingwen
 * DATE: 2022/8/30 9:35
 */
@Data
@ApiModel(value = "LoginSysUser对象", description = "用户登录类")
public class LoginSysUserDTO {

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("验证码")
    private String code;
}
