package com.jw_server.controller.blog;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import com.jw_server.service.blog.IBlogArticleTagService;
import com.jw_server.dao.blog.entity.BlogArticleTag;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import com.jw_server.core.aop.logAspect.SysLog;
import com.jw_server.core.common.ResponseResult;
import java.util.List;

import static com.jw_server.core.constants.LogTypeConst.*;


/**
 * author jingwen
 * Description 博客标签关系表 前端控制器
 * Date 2023-02-04 15:21:08
 */
@RestController
@RequestMapping("/blogArticleTag")
public class BlogArticleTagController {

    @Resource
    private IBlogArticleTagService blogArticleTagService;
    /**
     * Description 新增
     * Author jingwen
     * Date 2023-02-04 15:21:08
     **/
    @SysLog(logModule="", logType = ADD, logDesc = "新增")
    @PostMapping("/add")
    public ResponseResult add(@RequestBody BlogArticleTag blogArticleTag) {
        blogArticleTagService.save(blogArticleTag);
        return ResponseResult.success();
    }

    /**
     * Description 更新
     * Author jingwen
     * Date 2023-02-04 15:21:08
     **/
    @SysLog(logModule="", logType = UPDATE, logDesc = "更新")
    @PostMapping("/update")
    public ResponseResult update(@RequestBody BlogArticleTag blogArticleTag) {
        blogArticleTagService.updateById(blogArticleTag);
        return ResponseResult.success();
    }

    /**
     * Description 批量删除
     * Author jingwen
     * Date 2023-02-04 15:21:08
     **/
    @SysLog(logModule="", logType = DELETE, logDesc = "删除")
    @PostMapping("/deleteBatch")
    public ResponseResult deleteBatch(@RequestBody List<Integer> ids) {
        blogArticleTagService.removeByIds(ids);
        return ResponseResult.success();
    }

    /**
     * Description 查询所有数据
     * Author jingwen
     * Date 2023-02-04 15:21:08
     **/
    @GetMapping("/findAll")
    public ResponseResult findAll() {
        return ResponseResult.success(blogArticleTagService.list());
    }

    /**
     * Description 根据id查询数据
     * Author jingwen
     * Date 2023-02-04 15:21:08
     **/
    @GetMapping("/findOne")
    public ResponseResult findOne(@RequestParam Integer id) {
        return ResponseResult.success(blogArticleTagService.getById(id));
    }

    /**
     * Description 分页查询
     * Author jingwen
     * Date 2023-02-04 15:21:08
     **/
    @GetMapping("/getPageList")
    public ResponseResult getPageList(@RequestParam Integer pageNum,@RequestParam Integer pageSize) {
        QueryWrapper<BlogArticleTag> queryWrapper = new QueryWrapper<>();
        return ResponseResult.success(blogArticleTagService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

}

