package com.jw_server.core.constants;

/**
 * Description: 人脸检测常量字
 * Author: jingwen
 * DATE: 2022/9/13 19:55
 */
public class FaceDetectConst {

    /*
     * 初始上传人脸路径
     **/
    public static final String UNDEFINED_FACE = "undefined_face";

    /*
     * 上传到人脸库的人脸路径
     **/
    public static final String DEFINED_FACE = "defined_face";


    /*
     * 待检测的文件路径
     **/
    public static final String TO_DETECT_FILE = "to_detect_file";

    /*
     * 前端传过来的默认检测状态
     **/
    public static final Integer INIT_DETECT_STATUS = -99;

    /*
     * 检测结果文件路径
     **/
    public static final String DETECT_FILE_RESULT = "detect_file_result";

    /*
     * 缓存检测任务线程池的状态
     **/
    public static final String DETECT_TASK_CACHE = "dl:detect_task_cache";

    /*
     * 检测任务线程池参数
     * 核心线程大小
     * 最大线程大小
     * 工作队列大小
     * 空闲线程等待时间
     **/
    public static final Integer CORE_POOL_SIZE = 2;
    public static final Integer MAX_POOL_SIZE = 2;
    public static final Integer WORK_QUEUE_SIZE = 5;
    public static final Integer KEEP_ALIVE_SECONDS = 60;




}
