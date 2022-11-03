package com.jw_server.service.system;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jw_server.dao.system.dto.QuerySysMenuDTO;
import com.jw_server.dao.system.entity.SysMenu;
import com.jw_server.dao.system.vo.SysMenuVO;

import java.util.List;


/**
 * Description 菜单表 服务类
 * Author jingwen
 * Date 2022-08-30 18:58:10
 **/
public interface ISysMenuService extends IService<SysMenu> {

    /**
     * Description: 分页查询
     * Author: jingwen
     * Date: 2022/9/7 13:07
     **/
    List<SysMenuVO> getMenuPageList(QuerySysMenuDTO querySysMenuDTO);

    /**
     * Description 构建菜单树
     * Author jingwen
     * Date 2022/8/22 9:26
     **/
    List<SysMenuVO> sysMenuList();

    /**
     * 根据用户id查询所有菜单(目录，菜单，按钮)，根据参数是否判断需要构建成树结构
     * @param userId
     * @param buildTree
     * @return
     */
    List<SysMenuVO> selectMenusAndDirectoryByUserId(Integer userId, Boolean buildTree);

    /**
     * Description: 根据用户id查询所有权限按钮
     * Author: jingwen
     * Date: 2022/11/3 18:06
     **/
    List<String> selectPermissionsByUserId(Integer userId);

    /**
     * @Description 根据输入的所有数据构建树
     * @Author jingwen
     * @Date 2022/8/23 14:02
     **/
    List<SysMenuVO> buildTree(List<SysMenuVO> menuVOList);

    /**
     * Description:
     * Author: jingwen
     * Date: 2022/9/7 10:29
     **/
    void deleteMenu(List<Integer> menuId);
}
