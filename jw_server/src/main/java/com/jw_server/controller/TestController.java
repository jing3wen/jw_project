package com.jw_server.controller;

import com.jw_server.service.TestService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Description:
 * Author: jingwen
 * DATE: 2022/8/29 16:44
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/hello")
    public String hello(){
        return "hello";
    }


    @Resource
    private TestService testService;
    @GetMapping("/async")
    public String testAsync(){

        testService.TestAsync();
//        testService.func();
        return "async return";
    }
}
