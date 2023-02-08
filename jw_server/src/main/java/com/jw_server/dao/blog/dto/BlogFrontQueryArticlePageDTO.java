package com.jw_server.dao.blog.dto;

import com.jw_server.core.common.MyPageDTO;
import lombok.Data;

/**
 * Description: 前台查询文章列表
 * Author: jingwen
 * DATE: 2023/2/7 21:06
 */
@Data
public class BlogFrontQueryArticlePageDTO extends MyPageDTO {

    //类别
    private Integer categoryId;

    //标签
    private Integer tagId;

    //关键词
    private String keywords;
}
