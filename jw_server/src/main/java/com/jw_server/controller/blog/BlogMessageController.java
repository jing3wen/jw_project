package com.jw_server.controller.blog;

import com.jw_server.dao.blog.dto.BlogAdminUpdateCheckBatchDTO;
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
     * Description 前台获取所有留言
     * Author jingwen
     * Date 2023-02-09 14:24:46
     **/
    @GetMapping("/front/getMessageList")
    public ResponseResult getMessageList() {
        return ResponseResult.success(blogMessageService.getMessageList());
    }

    /**
     * Description 后台批量更新留言板审核状态
     * Author jingwen
     * Date 2023-02-09 14:24:46
     **/
    @SysLog(logModule=BlogMessageModule, logType = UPDATE, logDesc = "后台批量更新留言板审核状态")
    @PostMapping("/admin/updateCheckBatch")
    public ResponseResult updateCheckBatch(@RequestBody BlogAdminUpdateCheckBatchDTO updateCheckBatchDTO) {
        blogMessageService.updateMessageCheckBatch(updateCheckBatchDTO);
        return ResponseResult.success();
    }

    /**
     * Description 后台批量更新留言板留言
     * Author jingwen
     * Date 2023-02-09 14:24:46
     **/
    @SysLog(logModule=BlogMessageModule, logType = DELETE, logDesc = "后台批量更新留言板留言")
    @DeleteMapping("/admin/deleteBatch")
    public ResponseResult deleteBatch(@RequestBody List<Integer> ids) {
        blogMessageService.removeByIds(ids);
        return ResponseResult.success();
    }

    /**
     * Description: 后台获取留言版分页
     * Author: jingwen
     * Date: 2023/2/28 16:35
     **/
    @GetMapping("/admin/getBlogMessagePage")
    public ResponseResult getPageList(@RequestParam("pageNum") Integer pageNum,
                                      @RequestParam("pageSize") Integer pageSize,
                                      @RequestParam("messageCheck") String messageCheck) {

        return ResponseResult.success(blogMessageService.getAdminMessagePage(pageNum, pageSize, messageCheck));
    }




}

