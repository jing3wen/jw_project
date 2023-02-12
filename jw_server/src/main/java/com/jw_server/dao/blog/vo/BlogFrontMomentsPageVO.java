package com.jw_server.dao.blog.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Description: 前台查询朋友圈分页
 * Author: jingwen
 * DATE: 2023/2/11 20:50
 */
@Data
public class BlogFrontMomentsPageVO {

    @ApiModelProperty("朋友圈ID")
    private Integer momentsId;

    @ApiModelProperty("用户ID")
    private Integer userId;

    @ApiModelProperty("用户昵称")
    private String nickname;

    @ApiModelProperty("用户头像")
    private String avatar;

    @ApiModelProperty("内容")
    private String momentsContent;

    @ApiModelProperty("是否公开(0代表私密 1代表公开)")
    private String isPublic;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

}
