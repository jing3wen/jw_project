package com.jw_server.dao.blog.entity;

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
 * Description 文章类别表
 * Author jingwen
 * Date 2022-12-03 16:13:45
 **/
@Data
@TableName("blog_category")
@ApiModel(value = "BlogCategory对象", description = "文章类别表")
public class BlogCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("类别ID")
    @TableId(value = "category_id", type = IdType.AUTO)
    private Integer categoryId;

    @ApiModelProperty("类别名称")
    private String categoryName;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("状态(1正常 0停用)")
    private String status;

    @ApiModelProperty("是否删除(0代表存在 1代表删除)")
    private String isDeleted;

    @ApiModelProperty("创建者")
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty("更新者")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    @ApiModelProperty("更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;


}
