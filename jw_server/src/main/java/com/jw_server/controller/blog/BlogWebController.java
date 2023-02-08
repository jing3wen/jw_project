package com.jw_server.controller.blog;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import com.jw_server.service.blog.IBlogWebService;
import com.jw_server.dao.blog.entity.BlogWeb;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import com.jw_server.core.aop.logAspect.SysLog;
import com.jw_server.core.common.ResponseResult;
import java.util.List;

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
    /**
     * Description 新增
     * Author jingwen
     * Date 2023-02-04 15:21:28
     **/
    @SysLog(logModule="", logType = ADD, logDesc = "新增")
    @PostMapping("/add")
    public ResponseResult add(@RequestBody BlogWeb blogWeb) {
        blogWebService.save(blogWeb);
        return ResponseResult.success();
    }

    /**
     * Description 更新
     * Author jingwen
     * Date 2023-02-04 15:21:28
     **/
    @SysLog(logModule="", logType = UPDATE, logDesc = "更新")
    @PostMapping("/update")
    public ResponseResult update(@RequestBody BlogWeb blogWeb) {
        blogWebService.updateById(blogWeb);
        return ResponseResult.success();
    }

    /**
     * Description 获取网站配置
     * Author jingwen
     * Date 2023-02-04 15:21:28
     **/
    //TODO 此处可设置缓存切片
    @GetMapping("/getWebInfo")
    public ResponseResult findAll() {
        return ResponseResult.success(blogWebService.getWebInfo());
    }


}

