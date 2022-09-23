package com.jw_server.service.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jw_server.dao.system.dto.QuerySysLoginLogDTO;
import com.jw_server.dao.system.entity.SysLoginLog;
import com.baomidou.mybatisplus.extension.service.IService;


/**
 * Description 用户登录日志 服务类
 * Author jingwen
 * Date 2022-09-11 17:06:04
 **/
public interface ISysLoginLogService extends IService<SysLoginLog> {

    IPage<SysLoginLog> getSysLoginPageList(QuerySysLoginLogDTO querySysLoginLogDTO);
}
