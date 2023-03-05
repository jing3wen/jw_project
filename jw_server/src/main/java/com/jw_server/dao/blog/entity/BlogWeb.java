package com.jw_server.dao.blog.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Description 网站配置表
 * Author jingwen
 * Date 2023-02-04 15:21:28
 **/
@Data
@TableName("blog_web")
@ApiModel(value = "BlogWeb对象", description = "网站配置表")
public class BlogWeb implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("网站配置ID")
    @TableId(value = "web_id", type = IdType.AUTO)
    private Integer webId;

    @ApiModelProperty("网站名称")
    private String webName;

    @ApiModelProperty("网站标题")
    private String webTitle;

    @ApiModelProperty("网站公告")
    private String webNotices;

    @ApiModelProperty("网站页脚")
    private String webFooter;

    @ApiModelProperty("背景")
    private String backgroundImage;

    @ApiModelProperty("网站头像")
    private String webAvatar;

    @ApiModelProperty("随机头像")
    private String randomAvatar;

    @ApiModelProperty("随机名称")
    private String randomName;

    @ApiModelProperty("随机封面")
    private String randomCover;

    @ApiModelProperty("开启看板娘(1:开启, 0:不开启)")
    private String webKanban;

    @ApiModelProperty("开启文章审核(1:不开启, 0:开启)")
    private String articleCheck;

    @ApiModelProperty("开启评论审核(1:不开启, 0:开启)")
    private String commentCheck;

    @ApiModelProperty("开启留言审核(1:不开启, 0:开启)")
    private String messageCheck;

    @ApiModelProperty("是否启用[0:否，1:是]")
    private String status;

    @ApiModelProperty("更新者")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    @ApiModelProperty("更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;


}
