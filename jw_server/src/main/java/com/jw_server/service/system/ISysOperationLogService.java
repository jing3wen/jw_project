package com.jw_server.service.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jw_server.dao.system.dto.QuerySysOperationLogDTO;
import com.jw_server.dao.system.entity.SysOperationLog;
import com.baomidou.mybatisplus.extension.service.IService;


/**
 * Description 系统操作日志表 服务类
 * Author jingwen
 * Date 2022-09-11 09:32:13
 **/
public interface ISysOperationLogService extends IService<SysOperationLog> {

    /**
     * Description: 查询操作日志
     * Author: jingwen
     * Date: 2022/9/11 12:28
     **/
    IPage<SysOperationLog> getOperationLogPageList(QuerySysOperationLogDTO querySysOperationLogDTO);
}
