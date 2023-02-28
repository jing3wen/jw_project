package com.jw_server.dao.blog.dto;

import lombok.Data;

import java.util.List;

/**
 * Description: 后台批量更新状态公共DTO
 * Author: jingwen
 * DATE: 2023/2/28 16:41
 */
@Data
public class BlogAdminUpdateCheckBatchDTO {

    //主键
    private List<Integer> ids;

    //状态
    private String checkStatus;
}
