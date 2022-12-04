package com.jw_server.service.blog.impl;

import com.jw_server.dao.blog.entity.BlogCategory;
import com.jw_server.dao.blog.mapper.BlogCategoryMapper;
import com.jw_server.service.blog.IBlogCategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * Description 文章类别表 服务实现类
 * Author jingwen
 * Date 2022-12-03 16:13:45
 **/
@Service
public class BlogCategoryServiceImpl extends ServiceImpl<BlogCategoryMapper, BlogCategory> implements IBlogCategoryService {

}
