package com.jw_server.dao.system.dto;

import com.jw_server.core.common.MyPageDTO;
import lombok.Data;

/**
 * Description: 用户查询表单
 * Author: jingwen
 * DATE: 2022/9/7 12:37
 */
@Data
public class QuerySysRoleDTO extends MyPageDTO {

    private String roleCode;

    private String roleName;
}
