package com.jw_server.dao.system.dto;

import com.jw_server.core.common.MyPageDTO;
import lombok.Data;

/**
 * Description: 角色查询表单
 * Author: jingwen
 * DATE: 2022/9/2 17:55
 */
@Data
public class QuerySysUserDTO extends MyPageDTO {

    private String username;

    private String nickname;
}
