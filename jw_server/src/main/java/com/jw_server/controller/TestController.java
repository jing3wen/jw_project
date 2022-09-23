package com.jw_server.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description:
 * Author: jingwen
 * DATE: 2022/8/29 16:44
 */
@RestController
@RequestMapping("/")
public class TestController {

    @GetMapping("/hello")
    @PreAuthorize("hasAuthority('stem:sysMenu:list')")
    public String hello(){
        return "hello";
    }
}
