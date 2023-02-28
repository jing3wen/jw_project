package com.jw_server.service.blog;

import com.jw_server.core.common.MyPageVO;
import com.jw_server.dao.blog.dto.BlogAdminUpdateCheckBatchDTO;
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
     * Description: 查询所有友链
     * Author: jingwen 
     * Date: 2023/2/9 22:04
     **/
    List<BlogFriend> getAllFriend();

    /**
     * Description: 后台批量更新友链审核状态
     * Author: jingwen
     * Date: 2023/2/28 17:19
     **/
    void updateFriendCheckBatch(BlogAdminUpdateCheckBatchDTO updateCheckBatchDTO);

    /**
     * Description: 后台分页查询友链
     * Author: jingwen
     * Date: 2023/2/28 17:25
     **/
    MyPageVO<BlogFriend> getAdminFriendPage(Integer pageNum, Integer pageSize, String status);
}
