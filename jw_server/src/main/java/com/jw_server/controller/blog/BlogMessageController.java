package com.jw_server.controller.blog;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import com.jw_server.service.blog.IBlogMessageService;
import com.jw_server.dao.blog.entity.BlogMessage;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;

import com.jw_server.core.aop.logAspect.SysLog;
import com.jw_server.core.common.ResponseResult;
import java.util.List;

import static com.jw_server.core.constants.LogModuleConst.BlogMessageModule;
import static com.jw_server.core.constants.LogTypeConst.*;


/**
 * author jingwen
 * Description  前端控制器
 * Date 2023-02-09 14:24:46
 */
@RestController
@RequestMapping("/blogMessage")
public class BlogMessageController {

    @Resource
    private IBlogMessageService blogMessageService;
    /**
     * Description 前台新增一条留言
     * Author jingwen
     * Date 2023-02-09 14:24:46
     **/
    @SysLog(logModule=BlogMessageModule, logType = ADD, logDesc = "前台新增一条留言")
    @PostMapping("/front/addMessage")
    public ResponseResult add(@RequestBody BlogMessage blogMessage) {
        blogMessageService.save(blogMessage);
        return ResponseResult.success();
    }

    /**
     * Description 更新
     * Author jingwen
     * Date 2023-02-09 14:24:46
     **/
    @SysLog(logModule="", logType = UPDATE, logDesc = "更新")
    @PostMapping("/update")
    public ResponseResult update(@RequestBody BlogMessage blogMessage) {
        blogMessageService.updateById(blogMessage);
        return ResponseResult.success();
    }

    /**
     * Description 批量删除
     * Author jingwen
     * Date 2023-02-09 14:24:46
     **/
    @SysLog(logModule="", logType = DELETE, logDesc = "删除")
    @PostMapping("/deleteBatch")
    public ResponseResult deleteBatch(@RequestBody List<Integer> ids) {
        blogMessageService.removeByIds(ids);
        return ResponseResult.success();
    }

    /**
     * Description 获取所有留言
     * Author jingwen
     * Date 2023-02-09 14:24:46
     **/
    @GetMapping("/front/getMessageList")
    public ResponseResult getMessageList() {
        return ResponseResult.success(blogMessageService.getMessageList());
    }


}

