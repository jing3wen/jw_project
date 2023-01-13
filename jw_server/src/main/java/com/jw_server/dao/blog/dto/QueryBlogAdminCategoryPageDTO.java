package com.jw_server.dao.blog.dto;

import com.jw_server.core.common.MyPageDTO;
import lombok.Data;

/**
 * Description:
 * Author: jingwen
 * DATE: 2023/1/12 22:15
 */
@Data
public class QueryBlogAdminCategoryPageDTO extends MyPageDTO {

    //文章类别名称
    private String categoryName;
}
