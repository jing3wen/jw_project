package com.jw_server.dao.blog.dto;

import lombok.Data;

/**
 * Description: 修改文章顶置状态
 * Author: jingwen
 * DATE: 2023/1/26 12:47
 */
@Data
public class BlogAdminUpdateArticleTopDTO {

    //文章id
    private Integer articleId;

    //顶置状态
    private String isTop;
}
