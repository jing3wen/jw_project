package com.jw_server.core.exception;

import lombok.Getter;

/**
 * @author jingwen
 * @Description 自定义异常服务
 * @DATE 2022/8/19 20:25
 */
@Getter
public class ServiceException extends RuntimeException{

    private Integer code;

    public ServiceException(Integer code, String msg) {
        super(msg);
        this.code = code;
    }
}
