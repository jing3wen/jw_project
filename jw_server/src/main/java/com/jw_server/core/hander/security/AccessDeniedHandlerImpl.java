package com.jw_server.core.hander.security;

import com.alibaba.fastjson.JSON;
import com.jw_server.core.common.ResponseResult;
import com.jw_server.core.utils.WebUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Description: 自定义授权失败处理
 * Author: jingwen
 * DATE: 2022/8/30 20:50
 */
@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        ResponseResult result = new ResponseResult(HttpStatus.FORBIDDEN.value(), "权限不足", null);
        String json = JSON.toJSONString(result);
        WebUtils.renderString(response,json);
    }
}
