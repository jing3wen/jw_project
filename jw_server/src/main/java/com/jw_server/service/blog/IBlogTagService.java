package com.jw_server.service.blog;

import com.jw_server.dao.blog.entity.BlogTag;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jw_server.dao.blog.vo.BlogFrontTagVO;

import java.util.List;


/**
 * Description 博客标签表 服务类
 * Author jingwen
 * Date 2023-02-04 15:20:40
 **/
public interface IBlogTagService extends IService<BlogTag> {

    /**
     * Description: 前台查询所有标签
     * Author: jingwen
     * Date: 2023/2/8 10:04
     **/
    List<BlogFrontTagVO> getAllFrontTag();
}
