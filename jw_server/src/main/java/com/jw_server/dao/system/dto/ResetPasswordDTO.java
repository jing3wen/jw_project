package com.jw_server.dao.system.dto;

import lombok.Data;

/**
 * Description: 重置密码DTO
 * Author: jingwen
 * DATE: 2022/9/10 20:15
 */
@Data
public class ResetPasswordDTO {

    Integer userId;

    String password;

    String newPassword;
}
