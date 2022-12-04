package com.jw_server.controller.blog;

import com.jw_server.core.utils.IpUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import com.jw_server.service.blog.IBlogArticleService;
import com.jw_server.dao.blog.entity.BlogArticle;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.jw_server.core.aop.logAspect.SysLog;
import com.jw_server.core.common.ResponseResult;
import java.util.List;

import static com.jw_server.core.constants.LogTypeConst.*;


/**
 * author jingwen
 * Description 博客文章表 前端控制器
 * Date 2022-12-03 16:11:56
 */
@RestController
@RequestMapping("/blogArticle")
public class BlogArticleController {

    @Resource
    private IBlogArticleService blogArticleService;
    /**
     * Description 新增
     * Author jingwen
     * Date 2022-12-03 16:11:56
     **/
    @SysLog(logModule="", logType = ADD, logDesc = "新增")
    @PostMapping("/add")
    public ResponseResult add(@RequestBody BlogArticle blogArticle) {
        blogArticleService.save(blogArticle);
        return ResponseResult.success();
    }

    /**
     * Description 更新
     * Author jingwen
     * Date 2022-12-03 16:11:56
     **/
    @SysLog(logModule="", logType = UPDATE, logDesc = "更新")
    @PostMapping("/update")
    public ResponseResult update(@RequestBody BlogArticle blogArticle) {
        blogArticleService.updateById(blogArticle);
        return ResponseResult.success();
    }

    /**
     * Description 批量删除
     * Author jingwen
     * Date 2022-12-03 16:11:56
     **/
    @SysLog(logModule="", logType = DELETE, logDesc = "删除")
    @PostMapping("/deleteBatch")
    public ResponseResult deleteBatch(@RequestBody List<Integer> ids) {
        blogArticleService.removeByIds(ids);
        return ResponseResult.success();
    }


    /**
     * Description 前台 分页查询所有文章列表
     * Author jingwen
     * Date 2022-12-03 16:11:56
     **/
    @GetMapping("/front/getBlogFrontArticlePage")
    public ResponseResult getBlogFrontArticlePage(@RequestParam Integer pageNum,@RequestParam Integer pageSize) {

        return ResponseResult.success(blogArticleService.getBlogFrontArticlePage(pageNum, pageSize));
    }

    /**
     * Description 浏览博客文章详细信息，同时设置浏览量+1
     * Author jingwen
     * Date 2022-12-03 16:11:56
     **/
    @GetMapping("/front/getBlogFrontArticleDetails")
    public ResponseResult getBlogFrontArticleDetails(@RequestParam Integer articleId, HttpServletRequest request) {

        return ResponseResult.success(blogArticleService.getBlogFrontArticleDetails(articleId,request));
    }

}

