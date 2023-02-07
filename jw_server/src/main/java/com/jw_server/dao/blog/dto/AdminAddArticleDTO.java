package com.jw_server.dao.blog.dto;

import lombok.Data;

/**
 * Description: 新增文章
 * Author: jingwen
 * DATE: 2023/1/9 20:50
 */
@Data
public class AdminAddArticleDTO {

    //作者id
    private Integer userId;

    //类别id
    private Integer categoryId;

    //文章封面
    private String articleCover;

    //文章标题
    private String articleTitle;

    //文章简介
    private String articleSummary;

    //文章内容
    private String articleContent;

    //可见（0代表仅自己可见，1表示所有人可见)
    private String articleVisible;

    //是否允许评论(0表示不能评论，1表示可以评论)
    private String commentAllowed;

    //文章审核状态（1表示通过，0表示未通过）
    private String articleCheck;

}
