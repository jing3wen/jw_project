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
 * Description 系统操作日志表
 * Author jingwen
 * Date 2022-09-11 09:32:13
 **/
@Data
@TableName("sys_operation_log")
@ApiModel(value = "SysOperationLog对象", description = "系统操作日志表")
public class SysOperationLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("操作日志ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("操作模块名")
    private String optModule;

    @ApiModelProperty("操作类型")
    private String optType;

    @ApiModelProperty("操作描述")
    private String optDesc;

    @ApiModelProperty("请求url")
    private String requestUrl;

    @ApiModelProperty("请求方式")
    private String requestMethod;

    @ApiModelProperty("方法名称")
    private String optMethod;

    @ApiModelProperty("请求参数")
    private String requestParam;

    @ApiModelProperty("响应参数")
    private String responseResult;

    @ApiModelProperty("操作状态（0正常 1异常）")
    private Integer status;

    @ApiModelProperty("错误信息")
    private String errorMsg;

    @ApiModelProperty("操作人")
    private String optUser;

    @ApiModelProperty("操作ip")
    private String optIp;

    @ApiModelProperty("操作地址")
    private String optLocation;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

}
