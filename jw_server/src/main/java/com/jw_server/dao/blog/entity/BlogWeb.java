package com.jw_server.dao.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Description 网站配置表
 * Author jingwen
 * Date 2023-02-04 15:21:28
 **/
@Data
  @TableName("blog_web")
@ApiModel(value = "BlogWeb对象", description = "网站配置表")
public class BlogWeb implements Serializable {

  private static final long serialVersionUID = 1L;

      @ApiModelProperty("网站配置ID")
      @TableId(value = "web_id", type = IdType.AUTO)
      private Integer webId;

      @ApiModelProperty("网站名称")
      private String webName;

      @ApiModelProperty("网站标题")
      private String webTitle;

      @ApiModelProperty("网站公告")
      private String webNotices;

      @ApiModelProperty("网站页脚")
      private String webFooter;

      @ApiModelProperty("背景")
      private String backgroundImage;

      @ApiModelProperty("网站头像")
      private String webAvatar;

      @ApiModelProperty("随机头像")
      private String randomAvatar;

      @ApiModelProperty("随机名称")
      private String randomName;

      @ApiModelProperty("随机封面")
      private String randomCover;

      @ApiModelProperty("看板娘消息")
      private String waifuJson;

      @ApiModelProperty("是否启用[0:否，1:是]")
      private String status;


}
