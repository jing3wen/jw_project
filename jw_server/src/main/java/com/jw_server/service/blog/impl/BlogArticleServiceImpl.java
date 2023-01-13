package com.jw_server.service.blog.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jw_server.core.common.MyPageVO;
import com.jw_server.core.utils.IpUtils;
import com.jw_server.core.utils.RedisUtils;
import com.jw_server.dao.blog.dto.BlogAdminAddArticleDTO;
import com.jw_server.dao.blog.dto.QueryBlogAdminArticlePageDTO;
import com.jw_server.dao.blog.entity.BlogArticle;
import com.jw_server.dao.blog.mapper.BlogArticleMapper;
import com.jw_server.dao.blog.vo.BlogAdminArticlePageVO;
import com.jw_server.dao.blog.vo.BlogAdminUpdateArticleVO;
import com.jw_server.dao.blog.vo.BlogFrontArticleDetailsVO;
import com.jw_server.dao.blog.vo.BlogFrontArticlePageVO;
import com.jw_server.service.blog.IBlogArticleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

/**
 * Description 博客文章表 服务实现类
 * Author jingwen
 * Date 2022-12-03 16:11:56
 **/
@Service
public class BlogArticleServiceImpl extends ServiceImpl<BlogArticleMapper, BlogArticle> implements IBlogArticleService {


    Log logger = LogFactory.getLog(BlogArticleServiceImpl.class);

    @Resource
    private BlogArticleMapper blogArticleMapper;


    @Resource
    private RedisUtils redisUtils;

    @Override
    public MyPageVO<BlogFrontArticlePageVO> getBlogFrontArticlePage(Integer pageNum, Integer pageSize) {

        return new MyPageVO<>(blogArticleMapper.getFrontArticlePage(new Page<>(pageNum, pageSize)));
    }

    //TODO 后续可考虑把查询的文章放到缓存里面，修改文章时只用把redis里面的数据修改
    @Override
    @Transactional
    public BlogFrontArticleDetailsVO getBlogFrontArticleDetails(Integer articleId, HttpServletRequest request) {

        BlogFrontArticleDetailsVO frontArticleDetailsVO;
        String viewIpAddress = IpUtils.getIpAddress(request);
        String key = "blogArticle:isViewed:articleId-"+articleId+":"+viewIpAddress;
        if(ObjectUtil.isEmpty(redisUtils.getCacheObject(key))){
            //当前ip第一次浏览文章，更新文章浏览量
            logger.info("ip地址:"+viewIpAddress+", 浏览文章 articleId="+articleId+"");
            logger.info("记录到redis中, redis过期时间8小时, 更新数据库:浏览量+1");
            blogArticleMapper.updateArticleViewCounts(articleId);
            frontArticleDetailsVO = blogArticleMapper.getBlogFrontArticleDetails(articleId);
            redisUtils.setCacheObject(key, "浏览量"+frontArticleDetailsVO.getViewCounts(),8, TimeUnit.HOURS);
        }else {
            //当前ip 重复浏览文章，不更新文章浏览量
            logger.info("redis中查询到当前ip已经看过文章，不更新浏览量");
            frontArticleDetailsVO =  blogArticleMapper.getBlogFrontArticleDetails(articleId);
        }
        return frontArticleDetailsVO;
    }

    /**
     * 博客后台新增文章
     **/
    @Override
    public void addBlogArticle(BlogAdminAddArticleDTO blogAdminAddArticleDTO) {
        BlogArticle addArticle = new BlogArticle();
        BeanUtil.copyProperties(blogAdminAddArticleDTO, addArticle);
        save(addArticle);
    }

    /**
     * 博客后台查询文章列表
     **/
    @Override
    public MyPageVO<BlogAdminArticlePageVO> getAdminBlogArticlePage(QueryBlogAdminArticlePageDTO queryArticleDTO) {

        IPage<BlogAdminArticlePageVO> page = blogArticleMapper.getAdminBlogArticlePage(
                new Page<>(queryArticleDTO.getPageNum(), queryArticleDTO.getPageSize()),
                queryArticleDTO);
        return new MyPageVO<>(page);
    }

    /**
     * 后台查询需要编辑的文章信息
     **/
    @Override
    public BlogAdminUpdateArticleVO getUpdateArticle(Integer articleId) {
        return blogArticleMapper.getUpdateArticle(articleId);
    }

    /**
     * 后台更新文章信息
     **/
    @Override
    public void updateBlogArticle(BlogArticle updateArticle) {
        updateById(updateArticle);
    }
}
