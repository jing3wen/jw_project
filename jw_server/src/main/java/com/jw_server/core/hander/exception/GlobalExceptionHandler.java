package com.jw_server.core.hander.exception;

import com.jw_server.core.common.ResponseResult;
import com.jw_server.core.exception.ServiceException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author jingwen
 * @Description 全局异常处理器
 * @DATE 2022/8/19 20:24
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 如果抛出的的是ServiceException，则调用该方法
     * @param se 业务异常
     * @return Result
     */
    @ExceptionHandler(ServiceException.class)
    @ResponseBody
    public ResponseResult handle(ServiceException se){
        return ResponseResult.error(se.getCode(), se.getMessage());
    }

}
