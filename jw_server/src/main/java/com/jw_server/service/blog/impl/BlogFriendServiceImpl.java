package com.jw_server.service.blog.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jw_server.dao.blog.entity.BlogFriend;
import com.jw_server.dao.blog.mapper.BlogFriendMapper;
import com.jw_server.service.blog.IBlogFriendService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description  服务实现类
 * Author jingwen
 * Date 2023-02-09 21:59:28
 **/
@Service
public class BlogFriendServiceImpl extends ServiceImpl<BlogFriendMapper, BlogFriend> implements IBlogFriendService {

    @Override
    public List<BlogFriend> getAllFriend() {

        return list(new LambdaQueryWrapper<BlogFriend>()
                .eq(BlogFriend::getStatus,"1")
                .orderByDesc(BlogFriend::getCreateTime)
        );
    }
}
