package com.jw_server.controller.system;


import com.jw_server.core.common.ResponseResult;
import com.jw_server.core.aop.logAspect.SysLog;
import com.jw_server.dao.system.dto.QuerySysRoleDTO;
import com.jw_server.dao.system.dto.SysRoleMenuDTO;
import com.jw_server.dao.system.entity.SysRole;
import com.jw_server.service.system.ISysRoleMenuService;
import com.jw_server.service.system.ISysRoleService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

import static com.jw_server.core.constants.LogModuleConst.SysRoleModule;
import static com.jw_server.core.constants.LogTypeConst.*;

/**
 * author jingwen
 * Description 系统角色表 前端控制器
 * Date 2022-08-31 11:17:21
 */
@RestController
@RequestMapping("/sysRole")
public class SysRoleController {

    @Resource
    private ISysRoleService sysRoleService;

    @Resource
    private ISysRoleMenuService sysRoleMenuService;

    /**
     * Description 新增
     * Author jingwen
     * Date 2022-08-31 11:17:21
     **/
    @SysLog(logModule=SysRoleModule, logType = ADD, logDesc = "新增角色")
    @PreAuthorize("hasAuthority('system:sysRole:add')")
    @PostMapping("/add")
    public ResponseResult add(@RequestBody SysRole sysRole) {
        sysRoleService.addSysRole(sysRole);
        return ResponseResult.success();
    }

    /**
     * Description 更新
     * Author jingwen
     * Date 2022-08-31 11:17:21
     **/
    @SysLog(logModule=SysRoleModule, logType = UPDATE, logDesc = "更新角色")
    @PreAuthorize("hasAuthority('system:sysRole:update')")
    @PostMapping("/update")
    public ResponseResult update(@RequestBody SysRole sysRole) {
        sysRoleService.updateById(sysRole);
        return ResponseResult.success();
    }

    /**
     * Description 批量删除
     * Author jingwen
     * Date 2022-08-31 11:17:21
     **/
    @SysLog(logModule=SysRoleModule, logType = DELETE, logDesc = "删除角色")
    @PreAuthorize("hasAuthority('system:sysRole:delete')")
    @PostMapping("/deleteBatch")
    public ResponseResult deleteBatch(@RequestBody List<Integer> ids) {
        for(Integer roleId:ids){
            sysRoleService.deleteUserRoleAndRoleMenuWhenRemoveRole(roleId);
            sysRoleService.removeById(roleId);
        }
        return ResponseResult.success();
    }

    /**
     * Description 查询所有数据
     * Author jingwen
     * Date 2022-08-31 11:17:21
     **/
    @GetMapping("/findAll")
    public ResponseResult findAll() {
        return ResponseResult.success(sysRoleService.list());
    }

    /**
     * Description 根据id查询数据
     * Author jingwen
     * Date 2022-08-31 11:17:21
     **/
    @GetMapping("/findOne")
    public ResponseResult findOne(@RequestParam Integer id) {
        return ResponseResult.success(sysRoleService.getById(id));
    }

    /**
     * Description 分页查询
     * Author jingwen
     * Date 2022-08-31 11:17:21
     **/
    @PreAuthorize("hasAuthority('system:sysRole:query')")
    @PostMapping("/getPageList")
    public ResponseResult getPageList(@RequestBody QuerySysRoleDTO querySysRoleDTO) {
        System.out.println(querySysRoleDTO);
        return ResponseResult.success(sysRoleService.getRolePageList(querySysRoleDTO));
    }

    /**
     * Description 根据角色id查询分配的菜单
     * Author jingwen
     * Date 2022/8/22 19:43
     **/
    @PreAuthorize("hasAuthority('system:sysRole:permission')")
    @GetMapping("/getRoleMenu")
    public ResponseResult getRoleMenu(@RequestParam Integer roleId){
        return ResponseResult.success(sysRoleMenuService.getMenuIdsByRoleId(roleId));
    }


    /**
     * Description 绑定角色id和菜单关系
     * Author jingwen
     * Date 2022/8/22 19:43
     **/
    @PreAuthorize("hasAuthority('system:sysRole:permission')")
    @SysLog(logModule=SysRoleModule, logType = UPDATE, logDesc = "更新角色菜单")
    @PostMapping("/updateRoleMenu")
    public ResponseResult updateRoleMenu(@RequestBody SysRoleMenuDTO sysRoleMenuDTO){
        /*
          先删除再增加
         */
        sysRoleMenuService.updateRoleMenu(sysRoleMenuDTO.getRoleId(), sysRoleMenuDTO.getMenuIds());
        return ResponseResult.success();
    }



}

