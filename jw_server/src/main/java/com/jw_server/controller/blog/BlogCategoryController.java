package com.jw_server.controller.blog;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import com.jw_server.service.blog.IBlogCategoryService;
import com.jw_server.dao.blog.entity.BlogCategory;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import com.jw_server.core.aop.logAspect.SysLog;
import com.jw_server.core.common.ResponseResult;
import java.util.List;

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
     * Description 新增
     * Author jingwen
     * Date 2022-12-03 16:13:45
     **/
    @SysLog(logModule="", logType = ADD, logDesc = "新增")
    @PostMapping("/add")
    public ResponseResult add(@RequestBody BlogCategory blogCategory) {
        blogCategoryService.save(blogCategory);
        return ResponseResult.success();
    }

    /**
     * Description 更新
     * Author jingwen
     * Date 2022-12-03 16:13:45
     **/
    @SysLog(logModule="", logType = UPDATE, logDesc = "更新")
    @PostMapping("/update")
    public ResponseResult update(@RequestBody BlogCategory blogCategory) {
        blogCategoryService.updateById(blogCategory);
        return ResponseResult.success();
    }

    /**
     * Description 批量删除
     * Author jingwen
     * Date 2022-12-03 16:13:45
     **/
    @SysLog(logModule="", logType = DELETE, logDesc = "删除")
    @PostMapping("/deleteBatch")
    public ResponseResult deleteBatch(@RequestBody List<Integer> ids) {
        blogCategoryService.removeByIds(ids);
        return ResponseResult.success();
    }

    /**
     * Description 查询所有数据
     * Author jingwen
     * Date 2022-12-03 16:13:45
     **/
    @GetMapping("/findAll")
    public ResponseResult findAll() {
        return ResponseResult.success(blogCategoryService.list());
    }

    /**
     * Description 根据id查询数据
     * Author jingwen
     * Date 2022-12-03 16:13:45
     **/
    @GetMapping("/findOne")
    public ResponseResult findOne(@RequestParam Integer id) {
        return ResponseResult.success(blogCategoryService.getById(id));
    }

    /**
     * Description 分页查询
     * Author jingwen
     * Date 2022-12-03 16:13:45
     **/
    @PostMapping("/getPageList")
    public ResponseResult getPageList(@RequestParam Integer pageNum,@RequestParam Integer pageSize) {
        QueryWrapper<BlogCategory> queryWrapper = new QueryWrapper<>();
        return ResponseResult.success(blogCategoryService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

}

