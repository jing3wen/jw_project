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
 * Description 博客文章表
 * Author jingwen
 * Date 2022-12-03 16:11:56
 **/
@Data
@TableName("blog_article")
@ApiModel(value = "BlogArticle对象", description = "博客文章表")
public class BlogArticle implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("文章ID")
    @TableId(value = "article_id", type = IdType.AUTO)
    private Integer articleId;

    @ApiModelProperty("作者ID")
    private Integer userId;

    @ApiModelProperty("类别ID")
    private Integer categoryId;

    @ApiModelProperty("顶置（0表示不顶置，1表示顶置）")
    private String isTop;

    @ApiModelProperty("文章封面")
    private String articleCover;

    @ApiModelProperty("文章标题")
    private String articleTitle;

    @ApiModelProperty("文章简介")
    private String articleSummary;

    @ApiModelProperty("文章内容")
    private String articleContent;

    @ApiModelProperty("是否可见（0代表仅自己可见，1表示所有人可见）")
    private String articleVisible;

    @ApiModelProperty("浏览数量")
    private Integer viewCounts;

    @ApiModelProperty("是否允许评论(0表示不能评论，1表示可以评论)")
    private String commentAllowed;

    @ApiModelProperty("评论数量")
    private Integer commentCounts;

    @ApiModelProperty("文章审核状态（1表示通过，0表示未审核, f表示未通过）")
    private String articleCheck;

    @ApiModelProperty("是否删除(0代表存在 1代表删除)")
    private String isDeleted;

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
