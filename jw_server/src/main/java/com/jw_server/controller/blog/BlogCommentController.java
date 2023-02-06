package com.jw_server.controller.blog;

import com.jw_server.core.aop.logAspect.SysLog;
import com.jw_server.dao.blog.dto.addFrontCommentDTO;
import com.jw_server.dao.blog.dto.QueryBlogAdminCommentPageDTO;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import com.jw_server.service.blog.IBlogCommentService;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import com.jw_server.core.common.ResponseResult;
import java.util.List;

import static com.jw_server.core.constants.LogModuleConst.BlogCommentModule;
import static com.jw_server.core.constants.LogTypeConst.*;


/**
 * author jingwen
 * Description 博客评论表 前端控制器
 * Date 2022-12-03 17:17:39
 */
@RestController
@RequestMapping("/blogComment")
public class BlogCommentController {

    @Resource
    private IBlogCommentService blogCommentService;

    /**
     * Description: 前台查询文章评论
     * Author: jingwen 
     * Date: 2023/1/4 16:17
     **/
    @GetMapping("/front/getFrontComment")
    public ResponseResult getFrontComment(@RequestParam Integer articleId,
                                                @RequestParam Integer floorCommentId,
                                                @RequestParam Integer pageNum,
                                                @RequestParam Integer pageSize){

        return ResponseResult.success(blogCommentService.getFrontCommentByArticleId(articleId, floorCommentId, pageNum, pageSize));
    }


    /**
     * Description: 获取文章评论总数量
     * Author: jingwen
     * Date: 2023/2/6 20:01
     **/
    @GetMapping("/front/getFrontCommentCounts")
    public ResponseResult getFrontCommentCounts(@RequestParam Integer articleId){

        return ResponseResult.success(blogCommentService.getFrontCommentCounts(articleId));
    }


    /**
     * Description 前台新增一条评论
     * Author jingwen
     * Date 2022-12-03 17:17:39
     **/
    @SysLog(logModule=BlogCommentModule, logType = ADD, logDesc = "前台新增一条评论")
    @PostMapping("/front/addComment")
    public ResponseResult addComment(@RequestBody addFrontCommentDTO addFrontCommentDTO) {
        blogCommentService.addComment(addFrontCommentDTO);
        return ResponseResult.success();
    }

    /**
     * Description: 前台删除评论
     * Author: jingwen
     * Date: 2023/1/4 22:48
     **/
    @SysLog(logModule=BlogCommentModule, logType = DELETE, logDesc = "前台删除评论")
    @DeleteMapping("/front/deleteComment")
    public ResponseResult deleteComment(@RequestParam Integer commentId) {
        blogCommentService.deleteComment(commentId);
        return ResponseResult.success();
    }


    /**
     * Description: 后台批量审核博客文章评论
     * Author: jingwen
     * Date: 2023/1/13 10:27
     **/
    @SysLog(logModule=BlogCommentModule, logType = UPDATE, logDesc = "后台批量审核博客文章评论")
    @PostMapping("/admin/updateCommentCheckBatch")
    public ResponseResult updateCommentCheckBatch(@RequestBody List<Integer> ids) {
        blogCommentService.updateCommentCheckBatch(ids);
        return ResponseResult.success();
    }


    /**
     * Description 后台批量删除博客文章评论
     * Author jingwen
     * Date 2022-12-03 17:17:39
     **/
    @SysLog(logModule=BlogCommentModule, logType = DELETE, logDesc = "后台批量删除博客文章评论")
    @DeleteMapping("/admin/deleteBatch")
    public ResponseResult deleteBatch(@RequestBody List<Integer> ids) {
        blogCommentService.deleteBatchComment(ids);
        return ResponseResult.success();
    }

    /**
     * Description: 后台查询评论分页
     * Author: jingwen
     * Date: 2023/1/13 11:00
     **/
    @PostMapping("/admin/getPageList")
    public ResponseResult getPageList(@RequestBody QueryBlogAdminCommentPageDTO queryCommentPageDTO) {

        return ResponseResult.success(blogCommentService.getAdminCommentPageList(queryCommentPageDTO));
    }
}

