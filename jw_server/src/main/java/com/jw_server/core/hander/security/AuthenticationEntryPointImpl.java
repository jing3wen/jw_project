package com.jw_server.core.hander.security;

import com.alibaba.fastjson.JSON;
import com.jw_server.core.constants.HttpCode;
import com.jw_server.core.common.ResponseResult;
import com.jw_server.core.utils.WebUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Description: 自定义认证失败处理，此处没必要写，因为我已经在userLogin中提前捕获了异常
 * Author: jingwen
 * DATE: 2022/8/30 20:48
 */
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint{
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        ResponseResult result = new ResponseResult(HttpCode.CODE_401, "用户已过期，请重新登陆", null);
        String json = JSON.toJSONString(result);
        WebUtils.renderString(response,json);
    }
}
