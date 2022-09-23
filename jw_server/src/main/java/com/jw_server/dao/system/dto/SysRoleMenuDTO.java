package com.jw_server.dao.system.dto;

import lombok.Data;

import java.util.List;

/**
 * @author jingwen
 * @Description 角色权限绑定类
 * @DATE 2022/8/22 20:31
 */
@Data
public class SysRoleMenuDTO {

    Integer roleId;

    List<Integer> menuIds;
}
