package com.jw_server.controller.blog;

import com.jw_server.core.aop.logAspect.SysLog;
import com.jw_server.core.common.MyPageVO;
import com.jw_server.dao.blog.dto.BlogFrontCommentDTO;
import com.jw_server.dao.blog.dto.QueryBlogAdminCommentPageDTO;
import com.jw_server.dao.blog.entity.BlogComment;
import com.jw_server.dao.blog.vo.BlogAdminCommentPageVO;
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

//
//    /**
//     * Description 更新
//     * Author jingwen
//     * Date 2022-12-03 17:17:39
//     **/
//    @SysLog(logModule="", logType = UPDATE, logDesc = "更新")
//    @PostMapping("/update")
//    public ResponseResult update(@RequestBody BlogComment blogComment) {
//        blogCommentService.updateById(blogComment);
//        return ResponseResult.success();
//    }
//
//    /**
//     * Description 批量删除
//     * Author jingwen
//     * Date 2022-12-03 17:17:39
//     **/
//    @SysLog(logModule="", logType = DELETE, logDesc = "删除")
//    @PostMapping("/deleteBatch")
//    public ResponseResult deleteBatch(@RequestBody List<Integer> ids) {
//        blogCommentService.removeByIds(ids);
//        return ResponseResult.success();
//    }
//
//    /**
//     * Description 查询所有数据
//     * Author jingwen
//     * Date 2022-12-03 17:17:39
//     **/
//    @GetMapping("/findAll")
//    public ResponseResult findAll() {
//        return ResponseResult.success(blogCommentService.list());
//    }
//
//    /**
//     * Description 根据id查询数据
//     * Author jingwen
//     * Date 2022-12-03 17:17:39
//     **/
//    @GetMapping("/findOne")
//    public ResponseResult findOne(@RequestParam Integer id) {
//        return ResponseResult.success(blogCommentService.getById(id));
//    }
//
//    /**
//     * Description 分页查询
//     * Author jingwen
//     * Date 2022-12-03 17:17:39
//     **/
//    @PostMapping("/getPageList")
//    public ResponseResult getPageList(@RequestParam Integer pageNum,@RequestParam Integer pageSize) {
//        QueryWrapper<BlogComment> queryWrapper = new QueryWrapper<>();
//        return ResponseResult.success(blogCommentService.page(new Page<>(pageNum, pageSize), queryWrapper));
//    }
    
    /**
     * Description: 前台查询文章评论
     * Author: jingwen 
     * Date: 2023/1/4 16:17
     **/
    @GetMapping("/front/getCommentByArticleId")
    public ResponseResult getCommentByArticleId(@RequestParam Integer articleId,
                                                @RequestParam Integer pageNum,
                                                @RequestParam Integer pageSize){

        return ResponseResult.success(blogCommentService.getCommentByArticleId(articleId, pageNum, pageSize));
    }

    /**
     * Description 前台新增一条评论，该文章评论数+1
     * Author jingwen
     * Date 2022-12-03 17:17:39
     **/
    @SysLog(logModule=BlogCommentModule, logType = ADD, logDesc = "前台新增一条评论")
    @PostMapping("/front/addComment")
    public ResponseResult addComment(@RequestBody BlogFrontCommentDTO blogFrontCommentDTO) {
        blogCommentService.addComment(blogFrontCommentDTO);
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

