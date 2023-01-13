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
 * Description 博客评论表
 * Author jingwen
 * Date 2022-12-03 17:17:39
 **/
@Data
@TableName("blog_comment")
@ApiModel(value = "BlogComment对象", description = "博客评论表")
public class BlogComment implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("评论ID")
    @TableId(value = "comment_id", type = IdType.AUTO)
    private Integer commentId;

    @ApiModelProperty("评论用户ID")
    private Integer userId;

    @ApiModelProperty("文章ID")
    private Integer articleId;

    @ApiModelProperty("评论内容")
    private String commentContent;

    @ApiModelProperty("回复用户id")
    private Integer toUserId;

    @ApiModelProperty("父评论id")
    private Integer parentId;

    @ApiModelProperty("评论点赞数量")
    private Integer commentAgree;

    @ApiModelProperty("评论点踩数量")
    private Integer commentDisagree;

    @ApiModelProperty("评论审核状态（1表示通过，0表示未通过）")
    private String commentCheck;

    @ApiModelProperty("是否删除(0代表存在 1代表删除)")
    private String isDeleted;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty("更新者")
    private String updateBy;

    @ApiModelProperty("更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;


}
