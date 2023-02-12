package com.jw_server.dao.blog.dto;

import com.jw_server.core.common.MyPageDTO;
import lombok.Data;

/**
 * Description:
 * Author: jingwen
 * DATE: 2023/2/11 19:31
 */
@Data
public class BlogFrontCommentPageDTO extends MyPageDTO {

    //评论类型 文章评论/留言
    private String commentType;

    //楼层id
    private Integer floorCommentId;

    //文章id
    private Integer articleId;
}
