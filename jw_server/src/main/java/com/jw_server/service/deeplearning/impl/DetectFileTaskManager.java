package com.jw_server.service.deeplearning.impl;

import com.jw_server.core.constants.FaceDetectConst;
import com.jw_server.core.constants.HttpCode;
import com.jw_server.core.enums.DetectFileStatusEnum;
import com.jw_server.core.exception.ServiceException;
import com.jw_server.core.utils.RedisUtils;
import com.jw_server.dao.deeplearning.entity.DetectFileTaskInfo;
import com.jw_server.dao.deeplearning.entity.DlFaceDetectFile;
import com.jw_server.dao.deeplearning.mapper.DlFaceDetectFileMapper;
import com.jw_server.service.deeplearning.IDlFaceDetectFileService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayDeque;
import java.util.Date;
import java.util.Queue;

/**
 * Description: 异步检测任务管理类
 * Author: jingwen
 * DATE: 2023/3/9 21:19
 */
@Component
public class DetectFileTaskManager {
    //使用redis来缓存任务状态
    @Resource
    private RedisUtils redisUtils;

    @Resource
    private IDlFaceDetectFileService dlFaceDetectFileService;

    //正在检测的队列
    private final Queue<Integer> detectingQueue = new ArrayDeque<>();
    //等待队列
    private final Queue<Integer> waitingQueue = new ArrayDeque<>();



    /**
     * 初始化任务
     *
     * @return taskInfo
     */
    public DetectFileTaskInfo submitDetectTask(DlFaceDetectFile detectFile){
        //判断该任务是否已经提交
        if(waitingQueue.contains(detectFile.getId())){
            throw new ServiceException(HttpCode.CODE_400, "检测任务已经提交，正在等待cpu调度");
        }
        //判断该任务是否正在执行
        if(detectingQueue.contains(detectFile.getId())){
            throw new ServiceException(HttpCode.CODE_400, "检测任务正在执行, 请勿操作");
        }

        //拒绝
        if(waitingQueue.size() == FaceDetectConst.WORK_QUEUE_SIZE){
            throw new ServiceException(HttpCode.CODE_500,"当前等待任务已达上限, 请稍后提交");
        }
        //新建一个任务
        DetectFileTaskInfo taskInfo = new DetectFileTaskInfo();
        taskInfo.setTaskId(detectFile.getId());
        //根据正在执行的任务数量决定加入到等待队列还是执行队列
        if(detectingQueue.size() == FaceDetectConst.CORE_POOL_SIZE){
            taskInfo.setStatus(DetectFileStatusEnum.WAITING);
        }else {
            taskInfo.setStatus(DetectFileStatusEnum.RUNNING);
        }
        taskInfo.setSubmitTime(new Date());
        setTaskInfo(taskInfo);
        dlFaceDetectFileService.asyncDetectedFile(detectFile, String.valueOf(detectFile.getId()));
        return taskInfo;
    }

    /**
     * 缓存任务信息: redis中缓存检测任务信息
     * 更新数据库信息: 注意任务状态RUNNING, SUCCESS, FAILED的更新已经在异步方法asyncDetectedFile()中实现，所以只更新WAITING状态即可实现
     * @param taskInfo 任务信息
     */
    public void setTaskInfo(DetectFileTaskInfo taskInfo) {
        /*
         * WAITING(4,"等待中"); 已创建任务等待调度
         **/
        if(taskInfo.getStatus().equals(DetectFileStatusEnum.WAITING)){
            //进入等待队列
            waitingQueue.add(taskInfo.getTaskId());
            dlFaceDetectFileService.updateFileDetectStatus(taskInfo.getTaskId(), taskInfo.getStatus().getState());
            redisUtils.setCacheObject(FaceDetectConst.DETECT_TASK_CACHE+"_id_"+taskInfo.getTaskId() , taskInfo);
            return;
        }
        /*
         * RUNNING(1, "正在运行"),
         * 执行任务两种情况：
         * 1.创建任务直接运行：若是cpu时间片到了直接运行，则直接加入到执行队列
         * 2.等待队列取出任务：若线程是从等待队列中取出任务则需要先将任务移出等待队列，再加入到执行队列
         **/
        if(taskInfo.getStatus().equals(DetectFileStatusEnum.RUNNING)){
            waitingQueue.remove(taskInfo.getTaskId());  //remove=false表示为情况1: 创建任务直接运行
            //加入执行队列
            if(!detectingQueue.contains(taskInfo.getTaskId())) {
                detectingQueue.add(taskInfo.getTaskId());
            }
            //dlFaceDetectFileMapper.updateFileDetectStatus(taskInfo.getTaskId(), taskInfo.getStatus().getState());
            redisUtils.setCacheObject(FaceDetectConst.DETECT_TASK_CACHE+"_id_"+taskInfo.getTaskId() , taskInfo);
            return;
        }
        /*
         * 任务执行完成/抛出异常时, 将任务移出工作队列
         * 删除redis缓存
         **/
        if (taskInfo.getStatus().equals(DetectFileStatusEnum.SUCCESS) || taskInfo.getStatus().equals(DetectFileStatusEnum.FAILED)){
            detectingQueue.remove(taskInfo.getTaskId());
            //dlFaceDetectFileMapper.updateFileDetectStatus(taskInfo.getTaskId(), taskInfo.getStatus().getState());
            redisUtils.deleteObject(FaceDetectConst.DETECT_TASK_CACHE+"_id_"+taskInfo.getTaskId());
            return;
        }

    }

    /**
     * 获取任务信息
     * @param taskId 任务ID
     * @return
     */
    public DetectFileTaskInfo getTaskInfo(String taskId) {
        return redisUtils.getCacheObject(FaceDetectConst.DETECT_TASK_CACHE+"_id_"+taskId);
    }

    /**
     * 获取任务状态
     *
     * @param taskId 任务ID
     * @return
     */
    public DetectFileStatusEnum getTaskStatus(String taskId) {
        return getTaskInfo(taskId).getStatus();
    }

    /**
     * Description: 获取执行队列
     * Author: jingwen
     * Date: 2023/3/10 11:19
     **/
    public Queue<Integer> getDetectingQueue(){
        return detectingQueue;
    }


    /**
     * Description: 获取等待队列
     * Author: jingwen
     * Date: 2023/3/10 11:19
     **/
    public Queue<Integer> getWaitingQueue(){
        return waitingQueue;
    }
}
