package com.jw_server.dao.blog.vo;

import lombok.Data;

/**
 * Description:
 * Author: jingwen
 * DATE: 2023/2/4 16:11
 */
@Data
public class BlogFrontTagVO {

    //标签id
    private Integer tagId;

    //标签名称
    private String tagName;

    //标签名下的文章数量
    private Integer articleCounts;
}
