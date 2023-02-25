package com.jw_server.dao.system.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Description: 注册用户DTO
 * Author: jingwen
 * DATE: 2023/2/25 20:39
 */
@Data
@ApiModel(value = "RegisterUserDTO", description = "用户注册DTO")
public class RegisterUserDTO {

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("用户邮箱")
    private String email;

    @ApiModelProperty("验证码")
    private String code;

    @ApiModelProperty("用户类型（模块名）")
    private String userType;
}
