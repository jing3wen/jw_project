package com.jw_server.controller.system;


import com.jw_server.dao.system.dto.QuerySysLoginLogDTO;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import com.jw_server.core.common.ResponseResult;
import java.util.List;
import javax.annotation.Resource;

import com.jw_server.service.system.ISysLoginLogService;

import org.springframework.web.bind.annotation.RestController;


/**
 * author jingwen
 * Description 用户登录日志 前端控制器
 * Date 2022-09-11 17:06:04
 */
@RestController
@RequestMapping("/sysLoginLog")
public class SysLoginLogController {

    @Resource
    private ISysLoginLogService sysLoginLogService;

    /**
     * Description 批量删除
     * Author jingwen
     * Date 2022-09-11 17:06:04
     **/
    @PostMapping("/deleteBatch")
    public ResponseResult deleteBatch(@RequestBody List<Integer> ids) {
        sysLoginLogService.removeByIds(ids);
        return ResponseResult.success();
    }


    /**
     * Description 分页查询
     * Author jingwen
     * Date 2022-09-11 17:06:04
     **/
    @PostMapping("/getPageList")
    public ResponseResult getPageList(@RequestBody QuerySysLoginLogDTO querySysLoginLogDTO) {

        return ResponseResult.success(sysLoginLogService.getSysLoginPageList(querySysLoginLogDTO));
    }

}

