package com.jw_server.controller.blog;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import com.jw_server.service.blog.IBlogCommentService;
import com.jw_server.dao.blog.entity.BlogComment;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import com.jw_server.core.aop.logAspect.SysLog;
import com.jw_server.core.common.ResponseResult;
import java.util.List;

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
     * Description 新增
     * Author jingwen
     * Date 2022-12-03 17:17:39
     **/
    @SysLog(logModule="", logType = ADD, logDesc = "新增")
    @PostMapping("/add")
    public ResponseResult add(@RequestBody BlogComment blogComment) {
        blogCommentService.save(blogComment);
        return ResponseResult.success();
    }

    /**
     * Description 更新
     * Author jingwen
     * Date 2022-12-03 17:17:39
     **/
    @SysLog(logModule="", logType = UPDATE, logDesc = "更新")
    @PostMapping("/update")
    public ResponseResult update(@RequestBody BlogComment blogComment) {
        blogCommentService.updateById(blogComment);
        return ResponseResult.success();
    }

    /**
     * Description 批量删除
     * Author jingwen
     * Date 2022-12-03 17:17:39
     **/
    @SysLog(logModule="", logType = DELETE, logDesc = "删除")
    @PostMapping("/deleteBatch")
    public ResponseResult deleteBatch(@RequestBody List<Integer> ids) {
        blogCommentService.removeByIds(ids);
        return ResponseResult.success();
    }

    /**
     * Description 查询所有数据
     * Author jingwen
     * Date 2022-12-03 17:17:39
     **/
    @GetMapping("/findAll")
    public ResponseResult findAll() {
        return ResponseResult.success(blogCommentService.list());
    }

    /**
     * Description 根据id查询数据
     * Author jingwen
     * Date 2022-12-03 17:17:39
     **/
    @GetMapping("/findOne")
    public ResponseResult findOne(@RequestParam Integer id) {
        return ResponseResult.success(blogCommentService.getById(id));
    }

    /**
     * Description 分页查询
     * Author jingwen
     * Date 2022-12-03 17:17:39
     **/
    @PostMapping("/getPageList")
    public ResponseResult getPageList(@RequestParam Integer pageNum,@RequestParam Integer pageSize) {
        QueryWrapper<BlogComment> queryWrapper = new QueryWrapper<>();
        return ResponseResult.success(blogCommentService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

}

