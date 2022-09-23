package com.jw_server.dao.system.dto;

import lombok.Data;

import java.util.List;

/**
 * Description: 更新用户角色信息
 * Author: jingwen
 * DATE: 2022/9/6 8:59
 */
@Data
public class UserRoleDTO {

    private Integer userId;

    private List<Integer> roleIds;
}
