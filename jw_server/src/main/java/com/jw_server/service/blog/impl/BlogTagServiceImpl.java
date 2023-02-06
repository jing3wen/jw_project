package com.jw_server.service.blog.impl;

import com.jw_server.dao.blog.entity.BlogTag;
import com.jw_server.dao.blog.mapper.BlogTagMapper;
import com.jw_server.service.blog.IBlogTagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * Description 博客标签表 服务实现类
 * Author jingwen
 * Date 2023-02-04 15:20:40
 **/
@Service
public class BlogTagServiceImpl extends ServiceImpl<BlogTagMapper, BlogTag> implements IBlogTagService {

}
