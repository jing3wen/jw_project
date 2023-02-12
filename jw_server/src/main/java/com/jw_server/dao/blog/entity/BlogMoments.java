package com.jw_server.dao.blog.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Description 朋友圈表
 * Author jingwen
 * Date 2023-02-11 20:48:10
 **/
@Data
  @TableName("blog_moments")
@ApiModel(value = "BlogMoments对象", description = "朋友圈表")
public class BlogMoments implements Serializable {

  private static final long serialVersionUID = 1L;

      @ApiModelProperty("朋友圈ID")
      @TableId(value = "moments_id", type = IdType.AUTO)
      private Integer momentsId;

      @ApiModelProperty("用户ID")
      private Integer userId;

      @ApiModelProperty("内容")
      private String momentsContent;

      @ApiModelProperty("是否公开(0代表私密 1代表公开)")
      private String isPublic;

      @ApiModelProperty("是否删除(0代表存在 1代表删除)")
      private String isDeleted;

      @ApiModelProperty("创建时间")
        @TableField(fill = FieldFill.INSERT)
      @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

      @ApiModelProperty("更新时间")
      @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;


}
