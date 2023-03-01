package com.jw_server.controller.blog;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jw_server.dao.blog.dto.BlogAdminUpdateCheckBatchDTO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import com.jw_server.service.blog.IBlogFriendService;
import com.jw_server.dao.blog.entity.BlogFriend;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import com.jw_server.core.aop.logAspect.SysLog;
import com.jw_server.core.common.ResponseResult;

import java.util.List;

import static com.jw_server.core.constants.LogModuleConst.BlogFriendModule;
import static com.jw_server.core.constants.LogTypeConst.*;


/**
 * author jingwen
 * Description  前端控制器
 * Date 2023-02-09 21:59:28
 */
@RestController
@RequestMapping("/blogFriend")
public class BlogFriendController {

    @Resource
    private IBlogFriendService blogFriendService;


    /**
     * Description 前台申请友链
     * Author jingwen
     * Date 2023-02-09 21:59:28
     **/
    @SysLog(logModule=BlogFriendModule, logType = ADD, logDesc = "前台申请友链")
    @PostMapping("/front/addFriend")
    public ResponseResult add(@RequestBody BlogFriend blogFriend) {
        blogFriend.setStatus("0");
        blogFriendService.save(blogFriend);
        return ResponseResult.success();
    }

    /**
     * Description 前台查询所有友链
     * Author jingwen
     * Date 2023-02-09 21:59:28
     **/
    @GetMapping("/front/getAllFriend")
    public ResponseResult getAllFriend() {
        return ResponseResult.success(blogFriendService.getAllFriend());
    }

    /**
     * Description: 后台批量更新友链审核状态
     * Author: jingwen
     * Date: 2023/2/28 17:18
     **/
    @SysLog(logModule=BlogFriendModule, logType = UPDATE, logDesc = "后台批量更新友链审核状态")
    @PreAuthorize("hasAuthority('blog:blogFriend:check')")
    @PostMapping("/admin/updateCheckBatch")
    public ResponseResult updateCheckBatch(@RequestBody BlogAdminUpdateCheckBatchDTO updateCheckBatchDTO) {
        blogFriendService.updateFriendCheckBatch(updateCheckBatchDTO);
        return ResponseResult.success();
    }

    /**
     * Description: 后台批量删除友链
     * Author: jingwen
     * Date: 2023/2/28 17:21
     **/
    @SysLog(logModule=BlogFriendModule, logType = DELETE, logDesc = "后台批量删除友链")
    @PreAuthorize("hasAuthority('blog:blogFriend:delete')")
    @DeleteMapping("/admin/deleteBatch")
    public ResponseResult deleteBatch(@RequestBody List<Integer> ids) {
        blogFriendService.removeByIds(ids);
        return ResponseResult.success();
    }

    /**
     * Description 后台分页查询友链
     * Author jingwen
     * Date 2023-02-09 21:59:28
     **/
    @PreAuthorize("hasAuthority('blog:blogFriend:query')")
    @GetMapping("/admin/getBlogFriendPage")
    public ResponseResult getPageList(@RequestParam("pageNum") Integer pageNum,
                                      @RequestParam("pageSize") Integer pageSize,
                                      @RequestParam("status") String status) {

        return ResponseResult.success(blogFriendService.getAdminFriendPage(pageNum, pageSize, status));
    }

}

