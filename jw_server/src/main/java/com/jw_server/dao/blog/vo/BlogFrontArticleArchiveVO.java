package com.jw_server.dao.blog.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Description: 文章归档类
 * Author: jingwen
 * DATE: 2023/2/18 21:24
 */
@Data
@ApiModel(value = "BlogArticle对象", description = "文章归档类")
public class BlogFrontArticleArchiveVO {

    @ApiModelProperty("文章ID")
    private Integer articleId;

    @ApiModelProperty("文章标题")
    private String articleTitle;

    @ApiModelProperty("是否可见（0代表仅自己可见，1表示所有人可见）")
    private String articleVisible;

    @ApiModelProperty("归档时间")
    private String archiveTime;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
