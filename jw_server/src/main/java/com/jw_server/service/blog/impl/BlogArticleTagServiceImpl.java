package com.jw_server.service.blog.impl;

import com.jw_server.dao.blog.entity.BlogArticleTag;
import com.jw_server.dao.blog.mapper.BlogArticleTagMapper;
import com.jw_server.service.blog.IBlogArticleTagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * Description 博客标签关系表 服务实现类
 * Author jingwen
 * Date 2023-02-04 15:21:08
 **/
@Service
public class BlogArticleTagServiceImpl extends ServiceImpl<BlogArticleTagMapper, BlogArticleTag> implements IBlogArticleTagService {

}
