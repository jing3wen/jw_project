package com.jw_server.service.blog.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jw_server.dao.blog.entity.BlogWeb;
import com.jw_server.dao.blog.mapper.BlogWebMapper;
import com.jw_server.service.blog.IBlogWebService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * Description 网站配置表 服务实现类
 * Author jingwen
 * Date 2023-02-04 15:21:28
 **/
@Service
public class BlogWebServiceImpl extends ServiceImpl<BlogWebMapper, BlogWeb> implements IBlogWebService {

    /**
     * 获取网站配置
     **/
    @Override
    public BlogWeb getWebInfo() {

        return getOne(new LambdaQueryWrapper<BlogWeb>().orderByDesc(BlogWeb::getUpdateTime));
    }
}
