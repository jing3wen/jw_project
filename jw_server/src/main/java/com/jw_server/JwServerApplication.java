package com.jw_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


@SpringBootApplication
@EnableWebMvc
@EnableAsync  //开启异步任务
public class JwServerApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(JwServerApplication.class, args);
        System.out.println("项目启动成功");
    }

}
