package com.jw_server.service.system;

import com.jw_server.dao.system.entity.SysUserRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


/**
 * Description 用户角色关联表 服务类
 * Author jingwen
 * Date 2022-08-31 11:18:06
 **/
public interface ISysUserRoleService extends IService<SysUserRole> {

    /**
     * Description: 更新用户角色信息
     * Author: jingwen
     * Date: 2022/9/6 9:11
     **/
    void updateUserRole(Integer userId, List<Integer> roleIds);

    /**
     * Description: 删除用户绑定的所有的角色
     * Author: jingwen
     * Date: 2022/9/6 13:41
     **/
    void deleteUserRoleByUserId(Integer userId);
}
