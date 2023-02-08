package com.jw_server.dao.blog.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Description: 后台查询文章分页
 * Author: jingwen
 * DATE: 2023/1/10 11:23
 */
@Data
public class BlogAdminArticlePageVO {

    //文章id
    private Integer articleId;

    //用户昵称
    private String nickName;

    //类别名
    private String categoryName;

    //标签名
    private List<BlogFrontTagVO> tagList;

    //是否顶置
    private String isTop;

    //文章封面
    private String articleCover;

    //文章标题
    private String articleTitle;

    //是否可见（0代表仅自己可见，1表示所有人可见）
    private String articleVisible;

    //浏览量
    private Integer viewCounts;

    //是否允许评论(0表示不能评论，1表示可以评论)
    private String commentAllowed;

    //评论量
    private Integer commentCounts;

    //点赞量
    private Integer likedCounts;

    //文章审核状态（1表示通过，0表示未通过）
    private String articleCheck;

    //创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
