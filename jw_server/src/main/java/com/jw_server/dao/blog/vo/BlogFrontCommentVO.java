package com.jw_server.dao.blog.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Description: 博客前台查询文章评论列表
 * Author: jingwen
 * DATE: 2023/1/3 23:29
 */
@Data
public class BlogFrontCommentVO {

    //评论ID
    private Integer commentId;

    //文章ID
    private Integer articleId;

    //父评论id
    private Integer parentId;

    //评论用户ID
    private Integer userId;

    //评论用户昵称
    private String nickname;

    //评论用户头像
    private String avatar;

    //回复用户id
    private Integer toUserId;

    //回复用户昵称
    private String toNickname;

    //评论内容
    private String commentContent;

    //评论点赞数量
    private Integer commentAgree;

    //评论点踩数量
    private Integer commentDisagree;

    //回复数量
    private Integer replyToNum;

    //创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    //子评论列表
    List<BlogFrontCommentVO> toCommentList;
}
