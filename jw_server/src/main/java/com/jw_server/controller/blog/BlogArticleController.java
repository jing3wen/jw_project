package com.jw_server.controller.blog;

import com.jw_server.core.aop.logAspect.SysLog;
import com.jw_server.core.common.MyPageDTO;
import com.jw_server.dao.blog.dto.*;
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
     * Description 前台 分页查询所有文章列表/根据文章类别查询文章列表
     * Author jingwen
     * Date 2022-12-03 16:11:56
     **/
    @PostMapping("/front/getFrontArticlePage")
    public ResponseResult getFrontArticlePage(@RequestBody FrontQueryArticlePageDTO frontQueryArticlePageDTO) {
        System.out.println(frontQueryArticlePageDTO.toString());
        return ResponseResult.success(blogArticleService.getFrontArticlePage(frontQueryArticlePageDTO));
    }

    /**
     * Description 浏览博客文章详细信息，同时设置浏览量+1
     * Author jingwen
     * Date 2022-12-03 16:11:56
     **/
    @GetMapping("/front/getFrontArticleDetails")
    public ResponseResult getFrontArticleDetails(@RequestParam Integer articleId, HttpServletRequest request) {

        return ResponseResult.success(blogArticleService.getFrontArticleDetails(articleId,request));
    }

    /**
     * Description: 前台获取热门文章————浏览量最多的3篇文章
     * Author: jingwen
     * Date: 2023/1/28 13:54
     **/
    @GetMapping("/front/getHotArticle")
    public ResponseResult getHotArticle() {

        return ResponseResult.success(blogArticleService.getHotArticle(0, 3));
    }

    /**
     * Description: 前台根据文章标签查询文章分页
     * Author: jingwen
     * Date: 2023/2/7 21:22
     **/
    @PostMapping("/front/getFrontArticleByTag")
    public ResponseResult getFrontArticleByTag(@RequestBody FrontQueryArticlePageDTO frontQueryArticlePageDTO){


        return ResponseResult.success(blogArticleService.getFrontArticleByTag(frontQueryArticlePageDTO));
    }


    /**
     * Description: 博客后台新增文章
     * Author: jingwen
     * Date: 2023/1/9 21:18
     **/
    @SysLog(logModule=BlogArticleModule, logType = ADD, logDesc = "博客后台新增文章")
    @PostMapping("/admin/addBlogArticle")
    public ResponseResult addBlogArticle(@RequestBody AdminAddArticleDTO adminAddArticleDTO) {
        blogArticleService.addBlogArticle(adminAddArticleDTO);
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
     * Description: 批量删除文章，同时删除该文章评论和标签表关系
     * Author: jingwen
     * Date: 2023/1/11 16:20
     **/
    @SysLog(logModule=BlogArticleModule, logType = DELETE, logDesc = "批量删除文章,同时删除该文章评论和标签表关系")
    @DeleteMapping("/admin/deleteBatch")
    public ResponseResult deleteBatch(@RequestBody List<Integer> ids) {
        blogArticleService.deleteBatchArticle(ids);
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


    /**
     * Description: 后台修改文章顶置状态
     * Author: jingwen
     * Date: 2023/1/26 12:48
     **/
    @SysLog(logModule=BlogArticleModule, logType = UPDATE, logDesc = "后台修改文章顶置状态")
    @PostMapping("/admin/updateArticleTop")
    public ResponseResult updateArticleTop(@RequestBody AdminUpdateArticleTopDTO updateTopDTO) {
        blogArticleService.updateArticleTop(updateTopDTO);
        return ResponseResult.success();
    }

    /**
     * Description: 后台修改文章审核状态
     * Author: jingwen
     * Date: 2023/1/26 12:48
     **/
    @SysLog(logModule=BlogArticleModule, logType = UPDATE, logDesc = "后台审核文章")
    @PostMapping("/admin/updateArticleCheck")
    public ResponseResult updateArticleCheck(@RequestBody AdminUpdateArticleCheckDTO updateCheckDTO) {
        blogArticleService.updateArticleCheck(updateCheckDTO);
        return ResponseResult.success();
    }
}

