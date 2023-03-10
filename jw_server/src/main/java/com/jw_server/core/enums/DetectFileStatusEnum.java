package com.jw_server.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Description: 检测状态枚举
 * Author: jingwen
 * DATE: 2023/3/9 20:54
 */
@Getter
@AllArgsConstructor
public enum DetectFileStatusEnum {

    STARTED(0, "任务已创建"),
    RUNNING(1, "正在运行"),
    SUCCESS(2, "执行成功"),
    FAILED(-1, "执行失败"),
    WAITING(4,"等待中");


    /** 执行状态编码 */
    private final int state;

    /** 执行状态描述 */
    private final String stateInfo;
}
