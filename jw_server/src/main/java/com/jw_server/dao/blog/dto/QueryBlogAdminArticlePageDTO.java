package com.jw_server.dao.blog.dto;

import com.jw_server.core.common.MyPageDTO;
import lombok.Data;

/**
 * Description: 博客后台查询文章列表DTO
 * Author: jingwen
 * DATE: 2023/1/10 11:20
 */
@Data
public class QueryBlogAdminArticlePageDTO extends MyPageDTO {

    //文章类别名称
    private String categoryName;


}
