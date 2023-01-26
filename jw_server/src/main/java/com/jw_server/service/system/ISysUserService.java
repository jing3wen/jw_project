package com.jw_server.service.system;

import com.jw_server.core.common.MyPageVO;
import com.jw_server.core.common.ResponseResult;
import com.jw_server.dao.system.dto.LoginSysUserDTO;
import com.jw_server.dao.system.dto.QuerySysUserDTO;
import com.jw_server.dao.system.dto.ResetPasswordDTO;
import com.jw_server.dao.system.entity.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jw_server.dao.system.vo.LoginUserVO;
import com.jw_server.dao.system.vo.SysUserVO;


/**
 * Description 系统用户表 服务类
 * Author jingwen
 * Date 2022-08-29 16:21:58
 **/
public interface ISysUserService extends IService<SysUser> {

    /**
     * Description: 用户登录方法，校验验证码功能在LoginController中完成，所以该方法只是认证username和password
     * Author: jingwen
     * Date: 2022/8/30 9:39
     **/
    ResponseResult userLogin(LoginSysUserDTO loginSysUserDTO);

    /**
     * Description: 登出
     * Author: jingwen
     * Date: 2022/8/30 16:29
     **/
    ResponseResult userLogout(String token);

    /**
     * @Description 用户注册服务
     * @Author jingwen
     * @Date 2022/8/20 10:42
     **/
    ResponseResult register(SysUser sysUser);

    /**
     * @Description 根据用户名查询用户信息服务
     * @Author jingwen
     * @Date 2022/8/20 10:42
     **/
    SysUser getUserByUserName(String username);


    /**
     * Description: 用户列表分页
     * Author: jingwen
     * Date: 2022/9/2 16:40
     **/
    MyPageVO<SysUserVO> getUserPageList(QuerySysUserDTO querySysUserDTO);


    /**
     * Description: 获取当前认证用户信息
     * Author: jingwen
     * Date: 2022/9/2 14:59
     **/
    LoginUserVO getCurrentLoginUser();

    /**
     * Description: 重置用户密码
     * Author: jingwen
     * Date: 2022/9/10 20:20
     **/
    void resetPassword(ResetPasswordDTO resetPasswordDTO);
}
