package com.jw_server.service.system.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jw_server.dao.system.dto.QuerySysLoginLogDTO;
import com.jw_server.dao.system.entity.SysLoginLog;
import com.jw_server.dao.system.entity.SysOperationLog;
import com.jw_server.dao.system.mapper.SysLoginLogMapper;
import com.jw_server.service.system.ISysLoginLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * Description 用户登录日志 服务实现类
 * Author jingwen
 * Date 2022-09-11 17:06:04
 **/
@Service
public class SysLoginLogServiceImpl extends ServiceImpl<SysLoginLogMapper, SysLoginLog> implements ISysLoginLogService {

    @Override
    public IPage<SysLoginLog> getSysLoginPageList(QuerySysLoginLogDTO querySysLoginLogDTO) {
        LambdaQueryWrapper<SysLoginLog> queryWrapper = new LambdaQueryWrapper<>();
        //操作模块
        if(StrUtil.isNotEmpty(querySysLoginLogDTO.getUsername())){
            queryWrapper.like(SysLoginLog::getUsername, querySysLoginLogDTO.getUsername());
        }
        //操作状态
        if(StrUtil.isNotEmpty(querySysLoginLogDTO.getStatus())){
            queryWrapper.like(SysLoginLog::getStatus, querySysLoginLogDTO.getStatus());
        }
        queryWrapper.orderByDesc(SysLoginLog::getCreateTime);
        return page(new Page<>(querySysLoginLogDTO.getPageNum(),
                        querySysLoginLogDTO.getPageSize()),
                queryWrapper);
    }
}
