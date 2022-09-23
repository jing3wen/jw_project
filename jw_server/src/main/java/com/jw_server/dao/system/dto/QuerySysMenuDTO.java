package com.jw_server.dao.system.dto;

import com.jw_server.core.common.MyPageDTO;
import lombok.Data;

/**
 * Description: 菜单查询表单
 * Author: jingwen
 * DATE: 2022/9/7 12:51
 */
@Data
public class QuerySysMenuDTO extends MyPageDTO {

    private String menuName;

}
