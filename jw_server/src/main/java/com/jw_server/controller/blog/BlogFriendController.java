package com.jw_server.controller.blog;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import com.jw_server.service.blog.IBlogFriendService;
import com.jw_server.dao.blog.entity.BlogFriend;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import com.jw_server.core.aop.logAspect.SysLog;
import com.jw_server.core.common.ResponseResult;
import java.util.List;

import static com.jw_server.core.constants.LogTypeConst.*;


/**
 * author jingwen
 * Description  前端控制器
 * Date 2023-02-09 21:59:28
 */
@RestController
@RequestMapping("/blogFriend")
public class BlogFriendController {

    @Resource
    private IBlogFriendService blogFriendService;
//    /**
//     * Description 新增
//     * Author jingwen
//     * Date 2023-02-09 21:59:28
//     **/
//    @SysLog(logModule="", logType = ADD, logDesc = "新增")
//    @PostMapping("/add")
//    public ResponseResult add(@RequestBody BlogFriend blogFriend) {
//        blogFriendService.save(blogFriend);
//        return ResponseResult.success();
//    }
//
//    /**
//     * Description 更新
//     * Author jingwen
//     * Date 2023-02-09 21:59:28
//     **/
//    @SysLog(logModule="", logType = UPDATE, logDesc = "更新")
//    @PostMapping("/update")
//    public ResponseResult update(@RequestBody BlogFriend blogFriend) {
//        blogFriendService.updateById(blogFriend);
//        return ResponseResult.success();
//    }
//
//    /**
//     * Description 批量删除
//     * Author jingwen
//     * Date 2023-02-09 21:59:28
//     **/
//    @SysLog(logModule="", logType = DELETE, logDesc = "删除")
//    @PostMapping("/deleteBatch")
//    public ResponseResult deleteBatch(@RequestBody List<Integer> ids) {
//        blogFriendService.removeByIds(ids);
//        return ResponseResult.success();
//    }
//
//    /**
//     * Description 查询所有数据
//     * Author jingwen
//     * Date 2023-02-09 21:59:28
//     **/
//    @GetMapping("/findAll")
//    public ResponseResult findAll() {
//        return ResponseResult.success(blogFriendService.list());
//    }
//
//    /**
//     * Description 根据id查询数据
//     * Author jingwen
//     * Date 2023-02-09 21:59:28
//     **/
//    @GetMapping("/findOne")
//    public ResponseResult findOne(@RequestParam Integer id) {
//        return ResponseResult.success(blogFriendService.getById(id));
//    }
//
//    /**
//     * Description 分页查询
//     * Author jingwen
//     * Date 2023-02-09 21:59:28
//     **/
//    @GetMapping("/getPageList")
//    public ResponseResult getPageList(@RequestParam Integer pageNum,@RequestParam Integer pageSize) {
//        QueryWrapper<BlogFriend> queryWrapper = new QueryWrapper<>();
//        return ResponseResult.success(blogFriendService.page(new Page<>(pageNum, pageSize), queryWrapper));
//    }

    /**
     * Description 查询所有友链
     * Author jingwen
     * Date 2023-02-09 21:59:28
     **/
    @GetMapping("/front/getAllFriend")
    public ResponseResult getAllFriend() {
        return ResponseResult.success(blogFriendService.getAllFriend());
    }

}

