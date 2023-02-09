package com.jw_server.service.blog;

import com.jw_server.dao.blog.entity.BlogFriend;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


/**
 * Description  服务类
 * Author jingwen
 * Date 2023-02-09 21:59:28
 **/
public interface IBlogFriendService extends IService<BlogFriend> {

    /**
     * Description: 
     * Author: jingwen 
     * Date: 2023/2/9 22:04
     **/
    List<BlogFriend> getAllFriend();
}
