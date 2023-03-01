package com.jw_server.controller.blog;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import com.jw_server.service.blog.IBlogTagService;
import com.jw_server.dao.blog.entity.BlogTag;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import com.jw_server.core.aop.logAspect.SysLog;
import com.jw_server.core.common.ResponseResult;
import java.util.List;

import static com.jw_server.core.constants.LogModuleConst.BlogTagModule;
import static com.jw_server.core.constants.LogTypeConst.*;


/**
 * author jingwen
 * Description 博客标签表 前端控制器
 * Date 2023-02-04 15:20:40
 */
@RestController
@RequestMapping("/blogTag")
public class BlogTagController {

    @Resource
    private IBlogTagService blogTagService;


    /**
     * Description: 前台查询所有标签
     * Author: jingwen
     * Date: 2023/2/8 10:03
     **/
    @GetMapping("/front/getAllFrontTag")
    public ResponseResult getAllFrontTag() {
        return ResponseResult.success(blogTagService.getAllFrontTag());
    }


    /**
     * Description: 博客后台新增文章标签
     * Author: jingwen
     * Date: 2023/2/28 12:39
     **/
    @SysLog(logModule= BlogTagModule, logType = ADD, logDesc = "博客后台新增文章标签")
    @PreAuthorize("hasAuthority('blog:blogTag:add')")
    @PostMapping("/admin/addBlogTag")
    public ResponseResult addBlogTag(@RequestBody BlogTag blogTag) {
        blogTagService.addOrUpdateBlogTag(blogTag);
        return ResponseResult.success();
    }

    /**
     * Description: 博客后台更新文章标签
     * Author: jingwen
     * Date: 2023/2/28 12:38
     **/
    @SysLog(logModule=BlogTagModule, logType = UPDATE, logDesc = "博客后台更新文章标签")
    @PreAuthorize("hasAuthority('blog:blogTag:update')")
    @PostMapping("/admin/updateBlogTag")
    public ResponseResult updateBlogTag(@RequestBody BlogTag blogTag) {
        blogTagService.addOrUpdateBlogTag(blogTag);
        return ResponseResult.success();
    }

    /**
     * Description: 博客后台批量删除文章标签
     * Author: jingwen
     * Date: 2023/2/28 12:39
     **/
    @SysLog(logModule=BlogTagModule, logType = DELETE, logDesc = "博客后台批量删除文章标签")
    @PreAuthorize("hasAuthority('blog:blogTag:delete')")
    @DeleteMapping("/admin/deleteBatch")
    public ResponseResult deleteBatch(@RequestBody List<Integer> ids) {
        blogTagService.deleteBlogTagBatch(ids);
        return ResponseResult.success();
    }

    /**
     * Description: 博客后台分页查询文章标签
     * Author: jingwen
     * Date: 2023/2/28 12:42
     **/
    @PreAuthorize("hasAuthority('blog:blogTag:query')")
    @GetMapping("/admin/getBlogTagPage")
    public ResponseResult getPageList(@RequestParam("pageNum") Integer pageNum,
                                      @RequestParam("pageSize") Integer pageSize,
                                      @RequestParam("tagName") String tagName) {

        return ResponseResult.success(blogTagService.getAdminTagPage(pageNum, pageSize, tagName));
    }

    /**
     * Description:
     * Author: jingwen
     * Date: 2023/2/28 20:24
     **/
    @GetMapping("/admin/getAllBlogTagList")
    public ResponseResult getAllBlogTagList() {
        return ResponseResult.success(blogTagService.getTagListByTageNameOrNot(null));
    }

    /**
     * Description: 后台根据文章标签名搜索标签列表
     * Author: jingwen
     * Date: 2023/1/25 16:53
     **/
    @GetMapping("/admin/searchBlogTagList")
    public ResponseResult searchBlogCategoryList(@RequestParam String tagName) {
        return ResponseResult.success(blogTagService.getTagListByTageNameOrNot(tagName));
    }

}

