package com.jw_server.dao.system.vo;

import com.jw_server.dao.system.entity.SysMenu;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

/**
 * Description:
 * Author: jingwen
 * DATE: 2022/8/30 22:29
 */
@Data
@ApiModel(description = "返回给前端的菜单树")
public class SysMenuVO extends SysMenu {

    List<SysMenuVO> children;
}
