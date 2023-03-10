package com.jw_server.dao.deeplearning.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jw_server.core.enums.DetectFileStatusEnum;
import lombok.Data;

import java.util.Date;

/**
 * Description: 异步任务信息
 * Author: jingwen
 * DATE: 2023/3/9 21:11
 */
@Data
public class DetectFileTaskInfo {

    //任务id
    private Integer taskId;

    private DetectFileStatusEnum status;

    //提交时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date submitTime;

    //开始执行时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    //结束时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    private String totalTime;


    public void setTotalTime() {
        this.totalTime = (this.endTime.getTime() - this.startTime.getTime()) + "ms";
    }
}
