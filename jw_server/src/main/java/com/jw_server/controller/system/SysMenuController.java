package com.jw_server.controller.system;


import com.jw_server.core.common.ResponseResult;
import com.jw_server.core.aop.logAspect.SysLog;
import com.jw_server.dao.system.dto.QuerySysMenuDTO;
import com.jw_server.dao.system.entity.SysMenu;
import com.jw_server.service.system.ISysMenuService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

import static com.jw_server.core.constants.LogModuleConst.SysMenuModule;
import static com.jw_server.core.constants.LogTypeConst.*;

/**
 * author jingwen
 * Description 菜单表 前端控制器
 * Date 2022-08-30 18:58:10
 */
@RestController
@RequestMapping("/sysMenu")
public class SysMenuController {

    @Resource
    private ISysMenuService sysMenuService;

    /**
     * Description 新增
     * Author jingwen
     * Date 2022-08-30 18:58:10
     **/
    @SysLog(logModule=SysMenuModule, logType = ADD, logDesc = "新增菜单")
    @PreAuthorize("hasAuthority('system:sysMenu:add')")
    @PostMapping("/add")
    public ResponseResult add(@RequestBody SysMenu sysMenu) {
        sysMenuService.save(sysMenu);
        return ResponseResult.success();
    }
    /**
     * Description 更新
     * Author jingwen
     * Date 2022-08-30 18:58:10
     **/
    @SysLog(logModule=SysMenuModule, logType = UPDATE, logDesc = "更新菜单")
    @PreAuthorize("hasAuthority('system:sysMenu:update')")
    @PostMapping("/update")
    public ResponseResult update(@RequestBody SysMenu sysMenu) {
        sysMenuService.updateById(sysMenu);
        return ResponseResult.success();
    }

    /**
     * Description 批量删除
     * Author jingwen
     * Date 2022-08-30 18:58:10
     **/
    @SysLog(logModule=SysMenuModule, logType = DELETE, logDesc = "删除菜单")
    @PreAuthorize("hasAuthority('system:sysMenu:delete')")
    @PostMapping("/deleteBatch")
    public ResponseResult deleteBatch(@RequestBody List<Integer> ids) {

        sysMenuService.deleteMenu(ids);
        return ResponseResult.success();
    }

    /**
     * Description 查询所有数据
     * Author jingwen
     * Date 2022-08-30 18:58:10
     **/
    @GetMapping("/findAll")
    public ResponseResult findAll() {
        return ResponseResult.success(sysMenuService.sysMenuList());
    }


    /**
     * Description 分页查询
     * Author jingwen
     * Date 2022-08-30 18:58:10
     **/
    @PreAuthorize("hasAuthority('system:sysMenu:query')")
    @PostMapping("/getPageList")
    public ResponseResult getPageList(@RequestBody QuerySysMenuDTO querySysMenuDTO) {
        return ResponseResult.success(sysMenuService.getMenuPageList(querySysMenuDTO));
    }


    /**
     * Description: 根据用户id查询该用户的路由信息(目录和子菜单)
     * Author: jingwen
     * Date: 2022/11/3 20:17
     **/
    @GetMapping("/getMenusAndDirectoryByUserId")
    public ResponseResult getMenusAndDirectoryByUserId(Integer userId){
        return ResponseResult.success(sysMenuService.selectMenusAndDirectoryByUserId(userId,true));
    }

}

