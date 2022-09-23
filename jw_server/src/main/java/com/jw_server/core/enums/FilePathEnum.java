package com.jw_server.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Description: 文件路径枚举
 * Author: jingwen
 * Date: 2022/9/10 16:10
 **/
@Getter
@AllArgsConstructor
public enum FilePathEnum {

    /**
     * 头像路径
     */
    AVATAR("avatar/"),


    /**
     * 人脸库路径
     */
    FACE_DATABASE("deep_learning/face_detect/face_database/"),

    /**
     * 检测文件路径
     */
    FACE_DETECT_FILE("deep_learning/face_detect/face_detect_file/");


    /**
     * 路径
     */
    private final String path;

}
