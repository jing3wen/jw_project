package com.jw_server.dao.blog.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Description: 后台查询博客评论分页
 * Author: jingwen
 * DATE: 2023/1/13 11:17
 */
@Data
public class BlogAdminCommentPageVO {

    //评论ID
    private Integer commentId;

    //评论用户昵称
    private String nickname;

    //评论用户头像
    private String avatar;

    //回复用户昵称
    private String toNickname;

    //评论类型
    private String commentType;

    //评论内容
    private String commentContent;

    //评论审核状态
    private String commentCheck;

    //创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;



}
