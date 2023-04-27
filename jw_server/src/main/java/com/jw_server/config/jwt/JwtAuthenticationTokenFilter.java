package com.jw_server.config.jwt;

import cn.hutool.core.util.StrUtil;
import com.jw_server.core.constants.HttpCode;
import com.jw_server.core.exception.ServiceException;
import com.jw_server.core.utils.JwtUtils;
import com.jw_server.core.utils.RedisUtils;
import com.jw_server.dao.system.vo.UserDetailsVO;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * Description: JWT过滤器
 * Author: jingwen
 * DATE: 2022/8/30 15:27
 */
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Resource
    private RedisUtils redisUtils;

    @Value("${jwt.data.tokenHeader}")
    private String jwtTokenHeader;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //获取token
        String token = request.getHeader(jwtTokenHeader);
        if (StrUtil.isBlank(token)) {
            //放行, 交给SpringSecurity来处理, 进行登录
            filterChain.doFilter(request, response);
            return;
        }
        //解析token
        String userId;
        try {
            Claims claims = JwtUtils.parseJWT(token);
            userId = claims.getSubject();
        }catch (Exception e) {
            //此处抛出ServiceException会被spring security的认证失败处理器AuthenticationEntryPointImpl捕获，自定义捕获其捕获不到
            throw new ServiceException(HttpCode.CODE_401, "token验证失败，请重新登录");
        }
        //从redis中获取用户信息
        String subToken = StringUtils.substringAfterLast(token, ".");
        String redisKey = "login-tokens:"+ subToken;
        UserDetailsVO userDetailsVO = redisUtils.getCacheObject(redisKey);
        if(Objects.isNull(userDetailsVO)){
            //此处抛出ServiceException会被spring security的认证失败处理器AuthenticationEntryPointImpl捕获，自定义捕获其捕获不到
            throw new ServiceException(HttpCode.CODE_401, "用户不存在，请重新登录");
        }
        //存入SecurityContextHolder
        //获取权限信息封装到Authentication中
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDetailsVO, null, userDetailsVO.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        //放行
        filterChain.doFilter(request, response);
    }
}
