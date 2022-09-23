package com.jw_server.core.aop.logAspect;

import cn.hutool.http.useragent.UserAgent;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jw_server.core.utils.IpUtils;
import com.jw_server.dao.system.dto.LoginSysUserDTO;
import com.jw_server.dao.system.entity.SysLoginLog;
import com.jw_server.dao.system.entity.SysOperationLog;
import com.jw_server.service.system.ISysLoginLogService;
import com.jw_server.service.system.ISysOperationLogService;
import com.jw_server.service.system.ISysUserService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;

import static com.jw_server.core.constants.LogModuleConst.*;


/**
 * Description: aop, 用户操作日志
 * Author: jingwen
 * DATE: 2022/9/10 23:47
 */
@Aspect
@Component
public class SysLogAspect {

    @Resource
    private ISysOperationLogService sysOperationLogService;

    @Resource
    private ISysLoginLogService sysLoginLogService;

    @Resource
    private ISysUserService sysUserService;

    /** 排除敏感属性字段 */
    public static final String[] sensitive_words = { "password", "newPassword", "confirmPassword" };


    /**
     * 设置日志切入点 记录操作日志 在注解的位置切入代码
     */
    @Pointcut("@annotation(com.jw_server.core.aop.logAspect.SysLog)")
    public void LogPointCut() {}

    /**
     * 正常返回通知，记录日志， 如果连接点抛出异常，则不会执行
     */
    @AfterReturning(value = "LogPointCut()", returning = "jsonResult")
    public void logAfterReturning(JoinPoint joinPoint ,Object jsonResult){
        //获取切片的模块名，判断是登录日志还是操作日志
        SysLog sysLog = getLogFromJoinPoint(joinPoint);
        if(sysLog.logModule().equals(UserLoginModule)){
            saveLoginLog(joinPoint, null);
        }
        else saveOptLog(joinPoint, null, jsonResult);
    }

    /**
     * 异常返回通知，记录异常
     */
    @AfterThrowing(value = "LogPointCut()", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Exception e) {
        //获取切片的模块名，判断是登录日志还是操作日志
        SysLog sysLog = getLogFromJoinPoint(joinPoint);
        if(sysLog.logModule().equals(UserLoginModule)){
            saveLoginLog(joinPoint, e);
        }
        else saveOptLog(joinPoint, e, null);
    }

    public void saveOptLog(JoinPoint joinPoint, final Exception e, Object keys){
        // 获取RequestAttributes
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        // 从获取RequestAttributes中获取HttpServletRequest的信息
        HttpServletRequest request = (HttpServletRequest) requestAttributes
                .resolveReference(RequestAttributes.REFERENCE_REQUEST);

        SysOperationLog sysOperationLog = new SysOperationLog();
        try {
            // 从切面织入点处通过反射机制获取织入点处的方法
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            // 获取切入点所在的方法
            Method method = signature.getMethod();
            // **获取操作
            SysLog sysLog = method.getAnnotation(SysLog.class);

            sysOperationLog.setOptModule(sysLog.logModule());
            sysOperationLog.setOptType(sysLog.logType());
            sysOperationLog.setOptDesc(sysLog.logDesc());
            /*
              Description:请求url
             */
            if (request != null) {
                sysOperationLog.setRequestUrl(request.getRequestURI());
            }
            /*
              Description:请求方式，get,post...
             */
            sysOperationLog.setRequestMethod(Objects.requireNonNull(request).getMethod());
            // 获取请求的类名
            String className = joinPoint.getTarget().getClass().getName();
            // 获取请求的方法名
            String methodName = method.getName();
            /*
              Description:处理请求的方法名, controller中的方法名
             */
            sysOperationLog.setOptMethod(className + "." + methodName);
            /*
              Description:请求参数(过滤敏感信息)
             */
            sysOperationLog.setRequestParam(strFilter(JSON.toJSONString(joinPoint.getArgs())));
            /*
             * Description:响应结果
             */

            String respParam = JSON.toJSONString(keys);
            if(respParam.length()>1000){//防止过长
                respParam = respParam.substring(0,1000);
            }
            if(e == null){
                //响应正常
                sysOperationLog.setResponseResult(respParam);
            }else {
                //响应异常
                sysOperationLog.setStatus(1);
                sysOperationLog.setErrorMsg(e.getMessage());
            }

            /*
             * Description:操作人用户名, ip, ip地址
             */
            sysOperationLog.setOptUser(sysUserService.getCurrentLoginUser().getUsername());
            // 请求IP
            String ipAddress = IpUtils.getIpAddress(request);
            sysOperationLog.setOptIp(ipAddress);
            sysOperationLog.setOptLocation(IpUtils.getIpSource(ipAddress));

            sysOperationLogService.save(sysOperationLog);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Description: 记录登录日志
     * Author: jingwen
     * Date: 2022/9/11 17:20
     **/
    public void saveLoginLog(JoinPoint joinPoint, final Exception e){
        // 获取RequestAttributes
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        // 从获取RequestAttributes中获取HttpServletRequest的信息
        HttpServletRequest request = (HttpServletRequest) requestAttributes
                .resolveReference(RequestAttributes.REFERENCE_REQUEST);

        SysLoginLog sysLoginLog = new SysLoginLog();
        try {
            /*
              Description: 获取登录用户名
             */
            JSONArray jsonArray = JSONArray.parseArray(JSON.toJSONString(joinPoint.getArgs()));
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            LoginSysUserDTO loginSysUserDTO = jsonObject.toJavaObject(LoginSysUserDTO.class);
            sysLoginLog.setUsername(loginSysUserDTO.getUsername());

            // 登录IP, 登录地，登录浏览器, 操作系统
            String ipAddress = IpUtils.getIpAddress(request);
            UserAgent userAgent = IpUtils.getUserAgent(request);
            sysLoginLog.setLoginIp(ipAddress);
            sysLoginLog.setLoginLocation(IpUtils.getIpSource(ipAddress));
            sysLoginLog.setLoginBrowser(userAgent.getBrowser().toString());
            sysLoginLog.setLoginOs(userAgent.getOs().toString());

            if(e == null){
                //响应正常
                sysLoginLog.setStatus(0);
                sysLoginLog.setLoginMsg("登录成功");
            }else {
                //响应异常
                sysLoginLog.setStatus(1);
                sysLoginLog.setLoginMsg(e.getMessage());
            }
            sysLoginLogService.save(sysLoginLog);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Description: 通过切点获取日志
     * Author: jingwen
     * Date: 2022/9/13 15:40
     **/
    SysLog getLogFromJoinPoint(JoinPoint joinPoint){
        // 从切面织入点处通过反射机制获取织入点处的方法
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        // 获取切入点所在的方法
        Method method = signature.getMethod();
        // **获取操作
        return method.getAnnotation(SysLog.class);
    }

    /**
     * Description: 过滤敏感信息
     * Author: jingwen
     * Date: 2022/9/11 16:12
     **/
    public String strFilter(String jsonStr) {
        JSONArray jsonArray = JSONArray.parseArray(jsonStr);
        for(String word: sensitive_words){
            if(jsonStr.contains(word)){
                for(int i = 0; i < jsonArray.size(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    for(String key: jsonObject.keySet()){
                        if(Arrays.asList(sensitive_words).contains(key)){
                            jsonObject.put(key,"******");
                        }
                    }
                }
                break;
            }
        }
        return jsonArray.toJSONString();
    }
}
