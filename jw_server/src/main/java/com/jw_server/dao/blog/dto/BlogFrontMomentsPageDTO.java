package com.jw_server.dao.blog.dto;

import com.jw_server.core.common.MyPageDTO;
import lombok.Data;

/**
 * Description: 前台浏览朋友圈分页
 * Author: jingwen
 * DATE: 2023/2/11 20:57
 */
@Data
public class BlogFrontMomentsPageDTO extends MyPageDTO {

    private Boolean viewMe;
}
