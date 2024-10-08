package com.jw_server.controller.system;


import com.jw_server.dao.system.dto.QuerySysOperationLogDTO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import com.jw_server.core.common.ResponseResult;
import java.util.List;
import javax.annotation.Resource;

import com.jw_server.service.system.ISysOperationLogService;

import org.springframework.web.bind.annotation.RestController;

/**
 * author jingwen
 * Description 系统操作日志表 前端控制器
 * Date 2022-09-11 09:32:13
 */
@RestController
@RequestMapping("/sysOperationLog")
public class SysOperationLogController {

    @Resource
    private ISysOperationLogService sysOperationLogService;


    /**
     * Description 批量删除
     * Author jingwen
     * Date 2022-09-11 09:32:13
     **/
    @PreAuthorize("hasAuthority('system:sysOperationLog:delete')")
    @PostMapping("/deleteBatch")
    public ResponseResult deleteBatch(@RequestBody List<Integer> ids) {
        sysOperationLogService.removeByIds(ids);
        return ResponseResult.success();
    }

    /**
     * Description 分页查询
     * Author jingwen
     * Date 2022-09-11 09:32:13
     **/
    @PreAuthorize("hasAuthority('system:sysOperationLog:query')")
    @PostMapping("/getPageList")
    public ResponseResult getPageList(@RequestBody QuerySysOperationLogDTO querySysOperationLogDTO) {

        return ResponseResult.success(sysOperationLogService.getOperationLogPageList(querySysOperationLogDTO));
    }

}

