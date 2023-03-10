package com.jw_server.core.aop.asyncTaskMonitor;

import com.jw_server.core.enums.DetectFileStatusEnum;
import com.jw_server.dao.deeplearning.entity.DetectFileTaskInfo;
import com.jw_server.service.deeplearning.impl.DetectFileTaskManager;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Description: 异步任务监控
 * Author: jingwen
 * DATE: 2023/3/9 21:32
 */
@Slf4j
@Aspect
@Component
public class DetectFileTaskMonitor {

    @Resource
    DetectFileTaskManager manager;


    @Around("execution(* com.jw_server.service.deeplearning.impl.DlFaceDetectFileServiceImpl.asyncDetectedFile(..))")
    public void taskHandle(ProceedingJoinPoint pjp) {
        //获取taskId
        String taskId = pjp.getArgs()[1].toString();
        //获取任务信息, 在提交任务的时候就已经放入到redis中了
        DetectFileTaskInfo taskInfo = manager.getTaskInfo(taskId);
        log.info("监视器——正在监视任务:[taskId="+taskId+"]");
        taskInfo.setStatus(DetectFileStatusEnum.RUNNING);
        taskInfo.setStartTime(new Date());
        manager.setTaskInfo(taskInfo);
        DetectFileStatusEnum status = null;
        try {
            pjp.proceed();
            log.info("监视器发现任务:[taskId"+taskId+"]执行完成");
            status = DetectFileStatusEnum.SUCCESS;
        } catch (Throwable throwable) {
            status = DetectFileStatusEnum.FAILED;
            log.error("监视器——发现任务taskId{} 执行失败. Error info:{}", taskId, throwable.getMessage());
        }
        taskInfo.setEndTime(new Date());
        taskInfo.setStatus(status);
        taskInfo.setTotalTime();
        manager.setTaskInfo(taskInfo);

    }
}
