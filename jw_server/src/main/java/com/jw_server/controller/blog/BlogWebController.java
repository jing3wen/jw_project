package com.jw_server.controller.blog;

import cn.hutool.core.util.ObjectUtil;
import com.jw_server.core.constants.BlogConst;
import com.jw_server.core.utils.RedisUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import com.jw_server.service.blog.IBlogWebService;
import com.jw_server.dao.blog.entity.BlogWeb;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import com.jw_server.core.aop.logAspect.SysLog;
import com.jw_server.core.common.ResponseResult;

import static com.jw_server.core.constants.LogModuleConst.BlogWebModule;
import static com.jw_server.core.constants.LogTypeConst.*;


/**
 * author jingwen
 * Description 网站配置表 前端控制器
 * Date 2023-02-04 15:21:28
 */
@RestController
@RequestMapping("/blogWeb")
public class BlogWebController {

    @Resource
    private IBlogWebService blogWebService;

    @Resource
    private RedisUtils redisUtils;



    /**
     * Description 获取网站配置
     * Author jingwen
     * Date 2023-02-04 15:21:28
     **/
    @GetMapping("/front/getWebInfo")
    public ResponseResult findAll() {
        return ResponseResult.success(blogWebService.getWebInfo());
    }

    /**
     * Description: 获取看板娘消息
     * Author: jingwen 
     * Date: 2023/2/26 21:06
     **/
    @GetMapping("/front/getWaifuJson")
    public String getWaifuJson() {
        return blogWebService.getWaifuJson();
    }



    /**
     * Description 后台更新网站配置
     * Author jingwen
     * Date 2023-02-04 15:21:28
     **/
    @SysLog(logModule=BlogWebModule, logType = UPDATE, logDesc = "后台更新网站配置")
    @PostMapping("/admin/updateBlogWeb")
    public ResponseResult update(@RequestBody BlogWeb blogWeb) {
        blogWebService.updateById(blogWeb);
        //删除缓存
        if(ObjectUtil.isNotEmpty(redisUtils.getCacheObject(BlogConst.BLOG_WEB))){
            System.out.println("删除缓存");
            redisUtils.deleteObject(BlogConst.BLOG_WEB);
        }
        return ResponseResult.success();
    }
    /**
     * Description 后台获取网站配置
     * Author jingwen
     * Date 2023-02-04 15:21:28
     **/
    @GetMapping("/admin/getWebInfo")
    public ResponseResult getAdminWebInfo() {
        return ResponseResult.success(blogWebService.getWebInfo());
    }


}

