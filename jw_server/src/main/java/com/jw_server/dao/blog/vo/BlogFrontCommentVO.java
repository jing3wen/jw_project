package com.jw_server.dao.blog.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    //楼层评论ID (NULL表示为第1级评论)
    private Integer floorCommentId;

    //评论用户ID
    private Integer userId;

    //评论用户昵称
    private String nickname;

    //评论用户头像
    private String avatar;

    //回复的评论ID (0表示为第1级评论)
    private Integer replyCommentId;

    //回复的用户ID (NULL表示为第1级评论)
    private Integer replyUserId;

    //回复用户昵称
    private String replyNickname;

    //评论内容
    private String commentContent;

    //创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    //回复评论数量
    private Long replyCommentCounts;
    //回复评论列表
    List<BlogFrontCommentVO> replyCommentList;
}
