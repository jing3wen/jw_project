package com.jw_server.dao.blog.dto;

import lombok.Data;

/**
 * Description: 前台文章评论DTO
 * Author: jingwen
 * DATE: 2023/1/4 21:47
 */
@Data
public class BlogFrontCommentDTO {

    //文章ID
    private Integer articleId;

    //父评论id
    private Integer parentId;

    //评论用户ID
    private Integer userId;

    //回复用户id
    private Integer toUserId;

    //评论内容
    private String commentContent;

    //评论是否要审核
    private String commentCheck;

}
