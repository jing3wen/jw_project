package com.jw_server.dao.blog.vo;

import lombok.Data;

/**
 * Description:
 * Author: jingwen
 * DATE: 2023/1/8 21:57
 */
@Data
public class BlogFrontCategoryVO {

    //类别ID
    private Integer categoryId;

    //类别名称
    private String categoryName;

    //类别优先级
    private Integer categorySort;

    //该类别下的文章数量
    private Integer articleCounts;
}
