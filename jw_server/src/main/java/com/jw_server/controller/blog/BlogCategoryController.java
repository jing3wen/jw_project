package com.jw_server.controller.blog;

import com.jw_server.core.aop.logAspect.SysLog;
import com.jw_server.dao.blog.dto.BlogAdminQueryCategoryPageDTO;
import com.jw_server.dao.blog.entity.BlogCategory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import com.jw_server.service.blog.IBlogCategoryService;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;

import com.jw_server.core.common.ResponseResult;

import java.util.List;

import static com.jw_server.core.constants.LogModuleConst.BlogCategoryModule;
import static com.jw_server.core.constants.LogTypeConst.*;


/**
 * author jingwen
 * Description 文章类别表 前端控制器
 * Date 2022-12-03 16:13:45
 */
@RestController
@RequestMapping("/blogCategory")
public class BlogCategoryController {

    @Resource
    private IBlogCategoryService blogCategoryService;



    /**
     * Description 查询所有文章分类
    * Author jingwen
    * Date 2022-12-03 16:13:45
    **/
    @GetMapping("/front/getAllCategory")
    public ResponseResult getAllCategory() {
        return ResponseResult.success(blogCategoryService.getAllCategory());
    }


    /**
     * Description: 新增文章类别
     * Author: jingwen
     * Date: 2023/1/12 22:09
     **/
    @SysLog(logModule=BlogCategoryModule, logType = ADD, logDesc = "后台新增博客文章类别")
    @PostMapping("/admin/addBlogCategory")
    public ResponseResult addBlogCategory(@RequestBody BlogCategory blogCategory) {
        blogCategoryService.addOrUpdateBlogCategory(blogCategory);
        return ResponseResult.success();
    }

    /**
     * Description: 更新文章类别
     * Author: jingwen
     * Date: 2023/1/12 22:09
     **/
    @SysLog(logModule=BlogCategoryModule, logType = UPDATE, logDesc = "后台更新博客文章类别")
    @PostMapping("/admin/updateBlogCategory")
    public ResponseResult updateBlogCategory(@RequestBody BlogCategory blogCategory) {
        blogCategoryService.addOrUpdateBlogCategory(blogCategory);
        return ResponseResult.success();
    }

    /**
     * Description: 后台批量删除博客文章类别
     * Author: jingwen
     * Date: 2023/1/12 22:36
     **/
    @SysLog(logModule=BlogCategoryModule, logType = DELETE, logDesc = "后台批量删除博客文章类别")
    @DeleteMapping("/admin/deleteBatch")
    public ResponseResult deleteBatch(@RequestBody List<Integer> ids) {
        blogCategoryService.deleteCategoryByIds(ids);
        return ResponseResult.success();
    }

    /**
     * Description 后台查询博客类别分页
     * Author jingwen
     * Date 2023/1/12 22:09
     **/
    @PostMapping("/admin/getBlogCategoryPageList")
    public ResponseResult getBlogCategoryPageList(@RequestBody BlogAdminQueryCategoryPageDTO queryCategoryDTO) {
        return ResponseResult.success(blogCategoryService.getBlogCategoryPageList(queryCategoryDTO));
    }


    /**
     * Description: 后台根据文章类别名搜索类别列表
     * Author: jingwen
     * Date: 2023/1/25 16:53
     **/
    @GetMapping("/admin/searchBlogCategoryList")
    public ResponseResult searchBlogCategoryList(@RequestParam String categoryName) {
        return ResponseResult.success(blogCategoryService.searchBlogCategoryList(categoryName));
    }

}

