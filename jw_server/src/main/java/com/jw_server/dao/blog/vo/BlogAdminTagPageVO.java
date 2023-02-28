package com.jw_server.dao.blog.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Description: 后台查询博客标签分页
 * Author: jingwen
 * DATE: 2023/1/12 22:19
 */
@Data
public class BlogAdminTagPageVO {

    //标签ID
    private Integer tagId;

    //标签名称
    private String tagName;

    //备注
    private String remark;

    //文章量
    private Integer articleCount;

    //创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
