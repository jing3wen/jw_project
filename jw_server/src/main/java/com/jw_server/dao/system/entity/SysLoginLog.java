package com.jw_server.dao.system.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Description 用户登录日志
 * Author jingwen
 * Date 2022-09-11 17:06:04
 **/
@Data
  @TableName("sys_login_log")
@ApiModel(value = "SysLoginLog对象", description = "用户登录日志")
public class SysLoginLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("登录日志ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("登录账户")
    private String username;

    @ApiModelProperty("登录ip")
    private String loginIp;

    @ApiModelProperty("操作地址")
    private String loginLocation;

    @ApiModelProperty("登录浏览器")
    private String loginBrowser;

    @ApiModelProperty("操作系统")
    private String loginOs;

    @ApiModelProperty("登录状态（0正常 1异常）")
    private Integer status;

    @ApiModelProperty("登录结果")
    private String loginMsg;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;


}
