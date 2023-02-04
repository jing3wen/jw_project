package com.jw_server.dao.blog.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Description: 热门文章（按浏览量排序）
 * Author: jingwen
 * DATE: 2023/1/28 14:00
 */
@Data
public class BlogFrontHotArticleVO {

    //文章id
    private Integer articleId;

    //文章封面
    private String articleCover;

    //文章标题
    private String articleTitle;

    //浏览量
    private Integer viewCounts;

    //创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createTime;
}
