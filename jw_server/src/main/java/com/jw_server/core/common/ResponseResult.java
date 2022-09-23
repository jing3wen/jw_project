package com.jw_server.core.common;

import com.jw_server.core.constants.HttpCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @author jingwen
 * @Description
 * @DATE 2022/8/17 10:22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseResult {

    private Integer code;
    private String msg;
    private Object data;

    public static ResponseResult success() {
        return new ResponseResult(HttpCode.CODE_200, "", null);
    }

    public static ResponseResult success(Object data) {
        return new ResponseResult(HttpCode.CODE_200, "", data);
    }

    public static ResponseResult error(Integer code, String msg) {
        return new ResponseResult(code, msg, null);
    }

    public static ResponseResult error() {
        return new ResponseResult(HttpCode.CODE_500, "系统错误", null);
    }

}
