package com.jw_server.controller.system;

import com.jw_server.core.aop.logAspect.SysLog;
import com.jw_server.core.common.ResponseResult;
import com.jw_server.dao.system.dto.LoginSysUserDTO;
import com.jw_server.service.system.ISysUserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import static com.jw_server.core.constants.LogModuleConst.UserLoginModule;
import static com.jw_server.core.constants.LogTypeConst.*;
/**
 * Description: 登录controller类
 * Author: jingwen
 * DATE: 2022/8/30 9:04
 */
@RestController
@RequestMapping("/login")
public class LoginController {

    @Value("${jwt.data.tokenHeader}")
    private String jwtTokenHeader;

    @Resource
    private ISysUserService sysUserService;


    /**
     * Description: 用户登录
     * Author: jingwen
     * Date: 2022/8/30 9:29
     **/
    @SysLog(logModule = UserLoginModule, logType = USER_LOGIN)
    @PostMapping("/userLogin")
    public ResponseResult userLogin(@RequestBody LoginSysUserDTO loginSysUserDTO){
        //校验验证码

        return sysUserService.userLogin(loginSysUserDTO);
    }

    /**
     * Description: 登出
     * Author: jingwen
     * Date: 2022/8/30 16:23
     **/
    @PostMapping("/userLogout")
    public ResponseResult userLogout(HttpServletRequest request){
        String token = request.getHeader(jwtTokenHeader);
        return sysUserService.userLogout(token);
    }
}
