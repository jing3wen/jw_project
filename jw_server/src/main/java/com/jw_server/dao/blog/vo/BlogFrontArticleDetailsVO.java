package com.jw_server.dao.blog.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Description: 文章细节返回实体类
 * Author: jingwen
 * DATE: 2022/12/4 13:48
 */
@Data
@ApiModel(value = "BlogFrontArticleDetailsVO对象", description = "文章细节返回实体类")
public class BlogFrontArticleDetailsVO {

    @ApiModelProperty("文章ID")
    private Integer articleId;

    @ApiModelProperty("作者ID")
    private Integer userId;

    @ApiModelProperty("作者昵称")
    private String nickname;

    @ApiModelProperty("类别ID")
    private Integer categoryId;

    @ApiModelProperty("类别名称")
    private String categoryName;

    @ApiModelProperty("标签列表")
    private List<BlogFrontTagVO> tagList;

    @ApiModelProperty("顶置（0表示不顶置，1表示顶置）")
    private String isTop;

    @ApiModelProperty("文章封面")
    private String articleCover;

    @ApiModelProperty("文章标题")
    private String articleTitle;

    @ApiModelProperty("文章简介")
    private String articleSummary;

    @ApiModelProperty("文章内容")
    private String articleContent;

    @ApiModelProperty("可见（0代表仅自己可见，1表示所有人可见）")
    private String articleVisible;

    @ApiModelProperty("浏览数量")
    private Integer viewCounts;

    @ApiModelProperty("是否允许评论(0表示不能评论，1表示可以评论)")
    private String commentAllowed;

    @ApiModelProperty("评论数量")
    private Integer commentCounts;

    @ApiModelProperty("点赞数量")
    private Integer likedCounts;

    @ApiModelProperty("文章审核状态（1表示通过，0表示未通过）")
    private String articleCheck;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}
