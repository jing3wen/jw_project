package com.jw_server.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Description:
 * Author: jingwen
 * DATE: 2022/9/24 11:24
 */
@Service
public class TestService {

    public void func(){
        System.out.println("直接请求");
        TestAsync();
    }


    public void TestAsync(){

        System.out.println("异步执行开始");
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("异步执行结束");
    }

}
