package com.jw_server.dao.blog.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Description: 后台查询博客类别分页
 * Author: jingwen
 * DATE: 2023/1/12 22:19
 */
@Data
@ApiModel(value = "BlogAdminCategoryPageVO对象", description = "后台查询博客类别分页")
public class BlogAdminCategoryPageVO {

    @ApiModelProperty("类别ID")
    private Integer categoryId;

    @ApiModelProperty("类别名称")
    private String categoryName;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("文章量")
    private Integer articleCount;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
