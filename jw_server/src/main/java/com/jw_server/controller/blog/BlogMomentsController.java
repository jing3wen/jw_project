package com.jw_server.controller.blog;

import com.jw_server.dao.blog.dto.BlogFrontMomentsPageDTO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import com.jw_server.service.blog.IBlogMomentsService;
import com.jw_server.dao.blog.entity.BlogMoments;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;

import com.jw_server.core.aop.logAspect.SysLog;
import com.jw_server.core.common.ResponseResult;
import java.util.List;

import static com.jw_server.core.constants.LogModuleConst.BlogMomentsModule;
import static com.jw_server.core.constants.LogTypeConst.*;


/**
 * author jingwen
 * Description 朋友圈表 前端控制器
 * Date 2023-02-11 20:48:10
 */
@RestController
@RequestMapping("/blogMoments")
public class BlogMomentsController {

    @Resource
    private IBlogMomentsService blogMomentsService;
    /**
     * Description 博客前台发布朋友圈
     * Author jingwen
     * Date 2023-02-11 20:48:10
     **/
    @SysLog(logModule=BlogMomentsModule, logType = ADD, logDesc = "博客前台发布朋友圈")
    @PostMapping("/front/addMoments")
    public ResponseResult addMoments(@RequestBody BlogMoments blogMoments) {
        blogMomentsService.save(blogMoments);
        return ResponseResult.success();
    }

    /**
     * Description 博客前台删除朋友圈
     * Author jingwen
     * Date 2023-02-11 20:48:10
     **/
    @SysLog(logModule=BlogMomentsModule, logType = DELETE, logDesc = "博客前台删除朋友圈")
    @DeleteMapping("/front/deleteBatch")
    public ResponseResult deleteBatch(@RequestBody List<Integer> ids) {
        blogMomentsService.removeByIds(ids);
        return ResponseResult.success();
    }

    /**
     * Description 前台查询朋友圈分页,
     * 若viewMe=true，则只看登录用户的所有朋友圈
     * 若viewMe=false, 则浏览登录用户的所有朋友圈，和所有用户的公开朋友圈
     *
     * Author jingwen
     * Date 2023-02-11 20:48:10
     **/
    @PostMapping("/front/getFrontMomentsPage")
    public ResponseResult getFrontMomentsPage(@RequestBody BlogFrontMomentsPageDTO frontMomentsPageDTO) {

        return ResponseResult.success(blogMomentsService.getFrontMomentsPage(frontMomentsPageDTO));
    }

    /**
     * Description 博客后台批量删除朋友圈——可以和"博客前台删除朋友圈"合并
     * Author jingwen
     * Date 2023-02-11 20:48:10
     **/
    @SysLog(logModule=BlogMomentsModule, logType = DELETE, logDesc = "博客后台批量删除朋友圈")
    @PreAuthorize("hasAuthority('blog:blogMoments:delete')")
    @DeleteMapping("/admin/deleteBatch")
    public ResponseResult deleteAdminBatch(@RequestBody List<Integer> ids) {
        blogMomentsService.removeByIds(ids);
        return ResponseResult.success();
    }

    /**
     * Description: 后台查询朋友圈分页
     * Author: jingwen
     * Date: 2023/2/28 17:00
     **/
    @PreAuthorize("hasAuthority('blog:blogMoments:query')")
    @GetMapping("/admin/getAdminMomentsPage")
    public ResponseResult getAdminMomentsPage(@RequestParam("pageNum") Integer pageNum,
                                              @RequestParam("pageSize") Integer pageSize,
                                              @RequestParam("nickname") String nickname) {

        return ResponseResult.success(blogMomentsService.getAdminMomentsPage(pageNum, pageSize, nickname));
    }


}

