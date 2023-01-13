package com.jw_server.controller.blog;

import com.jw_server.core.aop.logAspect.SysLog;
import com.jw_server.dao.blog.dto.BlogAdminAddArticleDTO;
import com.jw_server.dao.blog.dto.QueryBlogAdminArticlePageDTO;
import com.jw_server.dao.blog.entity.BlogArticle;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import com.jw_server.service.blog.IBlogArticleService;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.jw_server.core.common.ResponseResult;

import java.util.List;

import static com.jw_server.core.constants.LogModuleConst.BlogArticleModule;
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

    /**
     * Description: 博客后台新增文章
     * Author: jingwen
     * Date: 2023/1/9 21:18
     **/
    @SysLog(logModule=BlogArticleModule, logType = ADD, logDesc = "博客后台新增文章")
    @PostMapping("/admin/addBlogArticle")
    public ResponseResult addBlogArticle(@RequestBody BlogAdminAddArticleDTO blogAdminAddArticleDTO) {
        blogArticleService.addBlogArticle(blogAdminAddArticleDTO);
        return ResponseResult.success();
    }


    /**
     * Description: 博客后台查询文章列表
     * Author: jingwen
     * Date: 2023/1/9 21:18
     **/
    @PostMapping("/admin/getAdminBlogArticlePage")
    public ResponseResult getAdminBlogArticlePage(@RequestBody QueryBlogAdminArticlePageDTO queryBlogAdminArticlePageDTO) {

        return ResponseResult.success(blogArticleService.getAdminBlogArticlePage(queryBlogAdminArticlePageDTO));
    }


    /**
     * Description: 批量删除文章
     * Author: jingwen
     * Date: 2023/1/11 16:20
     **/
    @SysLog(logModule=BlogArticleModule, logType = DELETE, logDesc = "批量删除文章")
    @DeleteMapping("/admin/deleteBatch")
    public ResponseResult deleteBatch(@RequestBody List<Integer> ids) {
        blogArticleService.removeByIds(ids);
        return ResponseResult.success();
    }

    /**
     * Description: 后台查询需要编辑的文章信息
     * Author: jingwen
     * Date: 2023/1/12 12:18
     **/
    @GetMapping("/admin/getUpdateArticle")
    public ResponseResult getUpdateArticle(@RequestParam Integer articleId) {

        return ResponseResult.success(blogArticleService.getUpdateArticle(articleId));
    }

    /**
     * Description: 后台更新文章信息
     * Author: jingwen
     * Date: 2023/1/12 12:18
     **/
    @SysLog(logModule=BlogArticleModule, logType = UPDATE, logDesc = "后台更新文章信息")
    @PostMapping("/admin/updateArticle")
    public ResponseResult updateArticle(@RequestBody BlogArticle updateArticle) {
        blogArticleService.updateBlogArticle(updateArticle);
        return ResponseResult.success();
    }
}

