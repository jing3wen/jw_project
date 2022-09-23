package com.jw_server.dao.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Description 角色菜单关联表
 * Author jingwen
 * Date 2022-08-31 11:17:36
 **/
@Data
@TableName("sys_role_menu")
public class SysRoleMenu implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer roleId;

    private Integer menuId;


}
