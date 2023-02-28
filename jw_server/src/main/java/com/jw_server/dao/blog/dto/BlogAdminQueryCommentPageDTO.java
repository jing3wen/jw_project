package com.jw_server.dao.blog.dto;

import com.jw_server.core.common.MyPageDTO;
import lombok.Data;

/**
 * Description: 后台查询评论分页
 * Author: jingwen
 * DATE: 2023/1/13 11:04
 */
@Data
public class BlogAdminQueryCommentPageDTO extends MyPageDTO {

    //评论人昵称
    private String nickname;

    //评论类型
    private String commentType;

    //评论审核状态
    private String commentCheck;
}
