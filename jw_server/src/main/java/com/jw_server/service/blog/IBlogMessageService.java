package com.jw_server.service.blog;

import com.jw_server.dao.blog.entity.BlogMessage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


/**
 * Description  服务类
 * Author jingwen
 * Date 2023-02-09 14:24:46
 **/
public interface IBlogMessageService extends IService<BlogMessage> {

    /**
     * Description: 查询留言，若数量太多，则限制数量
     * Author: jingwen
     * Date: 2023/2/9 20:42
     **/
    List<BlogMessage> getMessageList();
}
