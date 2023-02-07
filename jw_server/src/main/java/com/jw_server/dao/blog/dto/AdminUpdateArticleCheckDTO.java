package com.jw_server.dao.blog.dto;

import lombok.Data;

/**
 * Description: 后台更新文章审核状态DTO
 * Author: jingwen
 * DATE: 2023/1/26 17:07
 */
@Data
public class AdminUpdateArticleCheckDTO {

    //文章id
    private Integer articleId;

    //顶置状态
    private String articleCheck;
}
