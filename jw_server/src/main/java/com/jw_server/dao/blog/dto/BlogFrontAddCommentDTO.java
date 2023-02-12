package com.jw_server.dao.blog.dto;

import lombok.Data;

/**
 * Description: 前台文章评论DTO
 * Author: jingwen
 * DATE: 2023/1/4 21:47
 */
@Data
public class BlogFrontAddCommentDTO {

    //评论类型 文章评论/留言
    private String commentType;

    //文章ID
    private Integer articleId;

    //评论用户ID
    private Integer userId;

    //楼层评论id
    private Integer floorCommentId;

    //回复评论id
    private Integer replyCommentId;

    //回复用户id
    private Integer replyUserId;

    //评论内容
    private String commentContent;

    //评论是否要审核
    private String commentCheck;

}
