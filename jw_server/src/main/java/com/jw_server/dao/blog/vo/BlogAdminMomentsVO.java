package com.jw_server.dao.blog.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Description: 后台查询朋友圈分页
 * Author: jingwen
 * DATE: 2023/2/28 17:04
 */
@Data
public class BlogAdminMomentsVO {

    //主键
    private Integer momentsId;

    //用户ID
    private Integer userId;

    //用户名
    private String nickname;

    //用户头像
    private String avatar;

    //内容
    private String momentsContent;

    //是否公开(0代表私密 1代表公开)
    private String isPublic;

    //创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
