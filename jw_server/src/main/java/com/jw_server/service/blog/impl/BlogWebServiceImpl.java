package com.jw_server.service.blog.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jw_server.core.constants.BlogConst;
import com.jw_server.core.utils.RedisUtils;
import com.jw_server.dao.blog.entity.BlogWeb;
import com.jw_server.dao.blog.mapper.BlogWebMapper;
import com.jw_server.service.blog.IBlogWebService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Description 网站配置表 服务实现类
 * Author jingwen
 * Date 2023-02-04 15:21:28
 **/
@Service
@Slf4j
public class BlogWebServiceImpl extends ServiceImpl<BlogWebMapper, BlogWeb> implements IBlogWebService {

    @Resource
    private RedisUtils redisUtils;

    /**
     * 获取网站配置
     **/
    @Override
    public BlogWeb getWebInfo() {
        BlogWeb cacheBlogWeb = redisUtils.getCacheObject(BlogConst.BLOG_WEB);
        if(ObjectUtil.isNotEmpty(cacheBlogWeb)){
            log.info("返回缓存——博客网站配置");
            return redisUtils.getCacheObject(BlogConst.BLOG_WEB);
        }
        BlogWeb blogWeb = getOne(new LambdaQueryWrapper<BlogWeb>().orderByDesc(BlogWeb::getUpdateTime));
        redisUtils.setCacheObject(BlogConst.BLOG_WEB, blogWeb);
        return blogWeb;
    }

    /**
     * 获取看板娘消息
     **/
    public String getWaifuJson(){
        BlogWeb blogWeb = getWebInfo();
        if (ObjectUtil.isNotEmpty(blogWeb) && StrUtil.isNotEmpty(blogWeb.getWaifuJson())) {
            return blogWeb.getWaifuJson();
        }
        return "{}";
    }
}
