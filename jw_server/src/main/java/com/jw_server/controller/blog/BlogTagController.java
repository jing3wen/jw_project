package com.jw_server.controller.blog;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import com.jw_server.service.blog.IBlogTagService;
import com.jw_server.dao.blog.entity.BlogTag;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;

import com.jw_server.core.aop.logAspect.SysLog;
import com.jw_server.core.common.ResponseResult;
import java.util.List;

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
//    /**
//     * Description 新增
//     * Author jingwen
//     * Date 2023-02-04 15:20:40
//     **/
//    @SysLog(logModule="", logType = ADD, logDesc = "新增")
//    @PostMapping("/add")
//    public ResponseResult add(@RequestBody BlogTag blogTag) {
//        blogTagService.save(blogTag);
//        return ResponseResult.success();
//    }
//
//    /**
//     * Description 更新
//     * Author jingwen
//     * Date 2023-02-04 15:20:40
//     **/
//    @SysLog(logModule="", logType = UPDATE, logDesc = "更新")
//    @PostMapping("/update")
//    public ResponseResult update(@RequestBody BlogTag blogTag) {
//        blogTagService.updateById(blogTag);
//        return ResponseResult.success();
//    }
//
//    /**
//     * Description 批量删除
//     * Author jingwen
//     * Date 2023-02-04 15:20:40
//     **/
//    @SysLog(logModule="", logType = DELETE, logDesc = "删除")
//    @PostMapping("/deleteBatch")
//    public ResponseResult deleteBatch(@RequestBody List<Integer> ids) {
//        blogTagService.removeByIds(ids);
//        return ResponseResult.success();
//    }
//
//    /**
//     * Description 查询所有数据
//     * Author jingwen
//     * Date 2023-02-04 15:20:40
//     **/
//    @GetMapping("/findAll")
//    public ResponseResult findAll() {
//        return ResponseResult.success(blogTagService.list());
//    }
//
//    /**
//     * Description 根据id查询数据
//     * Author jingwen
//     * Date 2023-02-04 15:20:40
//     **/
//    @GetMapping("/findOne")
//    public ResponseResult findOne(@RequestParam Integer id) {
//        return ResponseResult.success(blogTagService.getById(id));
//    }
//
//    /**
//     * Description 分页查询
//     * Author jingwen
//     * Date 2023-02-04 15:20:40
//     **/
//    @GetMapping("/getPageList")
//    public ResponseResult getPageList(@RequestParam Integer pageNum,@RequestParam Integer pageSize) {
//        QueryWrapper<BlogTag> queryWrapper = new QueryWrapper<>();
//        return ResponseResult.success(blogTagService.page(new Page<>(pageNum, pageSize), queryWrapper));
//    }

    /**
     * Description: 前台查询所有标签
     * Author: jingwen
     * Date: 2023/2/8 10:03
     **/
    @GetMapping("/front/getAllFrontTag")
    public ResponseResult getAllFrontTag() {
        return ResponseResult.success(blogTagService.getAllFrontTag());
    }

}

