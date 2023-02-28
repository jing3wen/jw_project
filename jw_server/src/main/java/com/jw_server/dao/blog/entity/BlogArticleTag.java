package com.jw_server.dao.blog.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description 博客标签关系表
 * Author jingwen
 * Date 2023-02-04 15:21:08
 **/
@Data
@Builder
@TableName("blog_article_tag")
@ApiModel(value = "BlogArticleTag对象", description = "博客标签关系表")
public class BlogArticleTag implements Serializable {

  private static final long serialVersionUID = 1L;

      @ApiModelProperty("文章ID")
        private Integer articleId;

      @ApiModelProperty("标签ID	")
        private Integer tagId;


}
