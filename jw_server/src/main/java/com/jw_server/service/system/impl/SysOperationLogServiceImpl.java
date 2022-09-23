package com.jw_server.service.system.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jw_server.dao.system.dto.QuerySysOperationLogDTO;
import com.jw_server.dao.system.entity.SysOperationLog;
import com.jw_server.dao.system.entity.SysRole;
import com.jw_server.dao.system.mapper.SysOperationLogMapper;
import com.jw_server.service.system.ISysOperationLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * Description 系统操作日志表 服务实现类
 * Author jingwen
 * Date 2022-09-11 09:32:13
 **/
@Service
public class SysOperationLogServiceImpl extends ServiceImpl<SysOperationLogMapper, SysOperationLog> implements ISysOperationLogService {

    @Override
    public IPage<SysOperationLog> getOperationLogPageList(QuerySysOperationLogDTO querySysOperationLogDTO) {

        LambdaQueryWrapper<SysOperationLog> queryWrapper = new LambdaQueryWrapper<>();
        //操作模块
        if(StrUtil.isNotEmpty(querySysOperationLogDTO.getOptModule())){
            queryWrapper.like(SysOperationLog::getOptModule, querySysOperationLogDTO.getOptModule());
        }
        //操作类型
        if(StrUtil.isNotEmpty(querySysOperationLogDTO.getOptType())){
            queryWrapper.like(SysOperationLog::getOptType, querySysOperationLogDTO.getOptType());
        }
        //操作人
        if(StrUtil.isNotEmpty(querySysOperationLogDTO.getOptUser())){
            queryWrapper.like(SysOperationLog::getOptUser, querySysOperationLogDTO.getOptUser());
        }
        //操作状态
        if(StrUtil.isNotEmpty(querySysOperationLogDTO.getStatus())){
            queryWrapper.like(SysOperationLog::getStatus, querySysOperationLogDTO.getStatus());
        }
        queryWrapper.orderByDesc(SysOperationLog::getCreateTime);
        return page(new Page<>(querySysOperationLogDTO.getPageNum(),
                querySysOperationLogDTO.getPageSize()),
                queryWrapper);
    }
}
