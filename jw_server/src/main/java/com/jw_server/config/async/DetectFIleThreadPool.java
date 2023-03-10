package com.jw_server.config.async;

import com.alibaba.fastjson.JSON;
import com.jw_server.core.constants.FaceDetectConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Description: 检测文件线程池
 * Author: jingwen
 * DATE: 2023/3/9 20:33
 */
@EnableAsync
@Configuration
@Slf4j
public class DetectFIleThreadPool implements AsyncConfigurer {

    //检测文件线程池
    @Bean("detectFileThreadPool")
    public Executor getAsyncExecutor() {
        // 创建线程池
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(FaceDetectConst.CORE_POOL_SIZE); // 设置核心池大小, python检测服务器只有一个, 别设置太大了
        executor.setMaxPoolSize(FaceDetectConst.MAX_POOL_SIZE); // 设置最大池大小，只有在缓冲队列满了之后才会申请超过核心线程数的线程
        executor.setQueueCapacity(FaceDetectConst.WORK_QUEUE_SIZE); // 设置队列容量
        executor.setKeepAliveSeconds(FaceDetectConst.KEEP_ALIVE_SECONDS); // 设置保持活动秒数，当超过了核心线程数之外的线程在空闲时间到达之后会被销毁
        executor.setThreadNamePrefix("startDeleteFile-"); // 设置线程名称前缀
        // 设置拒绝的执行处理策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return executor;
    }

    /**
     * 指定系统中的异步任务在出现异常时使用到的处理器
     */
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new AsyncExceptionHandler();
    }

    /**
     * 异步任务异常捕获处理器
     */
    @SuppressWarnings("all")
    class AsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

        @Override
        public void handleUncaughtException(Throwable throwable, Method method,
                                            Object... objects) {

            throwable.printStackTrace();
            log.error("Async Error: [{}], Method: [{}], Param: [{}]",
                    throwable.getMessage(), method.getName(),
                    JSON.toJSONString(objects));


        }

    }
}
