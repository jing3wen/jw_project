package com.jw_server.core.aop.logAspect;

import java.lang.annotation.*;

/**
 * Description: 自定义操作日志注解
 * Author: jingwen
 * Date: 2022/9/10 23:49
 **/
@Target(ElementType.METHOD) //注解放置的目标位置,METHOD是可注解在方法级别上
@Retention(RetentionPolicy.RUNTIME) //注解在哪个阶段执行
@Documented
public @interface SysLog {

    /**
     * Description: 日志模块
     **/
    String logModule() default "";

    /**
     * Description: 日志类型
     **/
    String logType() default "";

    /**
     * Description: 日志说明
     **/
    String logDesc() default "";
}
