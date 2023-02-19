package com.jw_server.service.blog.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jw_server.core.common.MyPageVO;
import com.jw_server.core.utils.IpUtils;
import com.jw_server.core.utils.RedisUtils;
import com.jw_server.dao.blog.dto.*;
import com.jw_server.dao.blog.entity.BlogArticle;
import com.jw_server.dao.blog.mapper.*;
import com.jw_server.dao.blog.vo.*;
import com.jw_server.dao.system.vo.LoginUserVO;
import com.jw_server.service.blog.IBlogArticleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jw_server.service.system.ISysUserService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
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
    private BlogCategoryMapper blogCategoryMapper;

    @Resource
    private BlogCommentMapper blogCommentMapper;

    @Resource
    private BlogArticleTagMapper blogArticleTagMapper;

    @Resource
    private ISysUserService sysUserService;


    @Resource
    private RedisUtils redisUtils;

    /**
     * /根据文章类别查询文章列表
     **/
    @Override
    public MyPageVO<BlogFrontArticlePageVO> getFrontArticlePage(BlogFrontQueryArticlePageDTO blogFrontQueryArticlePageDTO) {


        /*
         * 先联表blog_article, sys_user, blog_category 查询（筛选）出文章列表
         *
         * 再对每个文章进行遍历, 查询(筛选)文章标签和点赞量
         **/
        IPage<BlogFrontArticlePageVO> articlePage;
        LoginUserVO loginUserVO = sysUserService.getCurrentLoginUser();
        //登录用户，能查看自己的所有文章和其他人的公开文章
        if(ObjectUtil.isNotEmpty(loginUserVO)){
             articlePage = blogArticleMapper.getFrontLoginUserArticlePage(
                    new Page<>(blogFrontQueryArticlePageDTO.getPageNum(), blogFrontQueryArticlePageDTO.getPageSize()),
                    loginUserVO.getId(),
                    blogFrontQueryArticlePageDTO);
        }else { //匿名访问，只能查看公开文章
            articlePage = blogArticleMapper.getFrontPublicArticlePage(
                    new Page<>(blogFrontQueryArticlePageDTO.getPageNum(), blogFrontQueryArticlePageDTO.getPageSize()),
                    blogFrontQueryArticlePageDTO);
        }


        List<BlogFrontArticlePageVO> articleVOList = articlePage.getRecords();
        if(CollectionUtil.isNotEmpty(articleVOList)){
            articleVOList.forEach(articleVO->{
                //封装文章标签
                articleVO.setTagList(blogArticleTagMapper.getArticleTagsByArticleId(articleVO.getArticleId()));
                //封装评论数量
                articleVO.setCommentCounts(blogCommentMapper.getFrontCommentCounts(articleVO.getArticleId()));
                //TODO 封装点赞量
                articleVO.setLikedCounts(0);
            });
        }

        return new MyPageVO<>(articlePage.getPages(),
                articlePage.getCurrent(),
                articlePage.getSize(),
                articlePage.getTotal(),
                articleVOList);
    }


    //TODO 后续可考虑把查询的文章放到缓存里面，修改文章时只用把redis里面的数据修改
    @Override
    @Transactional
    public BlogFrontArticleDetailsVO getFrontArticleDetails(Integer articleId, HttpServletRequest request) {

        BlogFrontArticleDetailsVO frontArticleDetailsVO;
        String viewIpAddress = IpUtils.getIpAddress(request);
        String key = "blogArticle:isViewed:articleId-"+articleId+":"+viewIpAddress;
        if(ObjectUtil.isEmpty(redisUtils.getCacheObject(key))){
            //当前ip第一次浏览文章，更新文章浏览量
            logger.info("ip地址:"+viewIpAddress+", 浏览文章 articleId="+articleId+"");
            logger.info("记录到redis中, redis过期时间8小时, 更新数据库:浏览量+1");
            blogArticleMapper.updateArticleViewCounts(articleId);
            frontArticleDetailsVO = blogArticleMapper.getFrontArticleDetails(articleId);
            //封装标签列表
            frontArticleDetailsVO.setTagList(blogArticleTagMapper.getArticleTagsByArticleId(articleId));
            //封装评论数量
            frontArticleDetailsVO.setCommentCounts(blogCommentMapper.getFrontCommentCounts(frontArticleDetailsVO.getArticleId()));
            //TODO 封装点赞量
            frontArticleDetailsVO.setLikedCounts(0);
            redisUtils.setCacheObject(key, "浏览量"+frontArticleDetailsVO.getViewCounts(),8, TimeUnit.HOURS);
        }else {
            //当前ip 重复浏览文章，不更新文章浏览量
            logger.info("redis中查询到当前ip已经看过文章，不更新浏览量");
            frontArticleDetailsVO =  blogArticleMapper.getFrontArticleDetails(articleId);
            //封装标签列表
            frontArticleDetailsVO.setTagList(blogArticleTagMapper.getArticleTagsByArticleId(articleId));
            //封装评论数量
            frontArticleDetailsVO.setCommentCounts(blogCommentMapper.getFrontCommentCounts(frontArticleDetailsVO.getArticleId()));
            //TODO 封装点赞量
            frontArticleDetailsVO.setLikedCounts(0);
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
    public MyPageVO<BlogAdminArticlePageVO> getAdminBlogArticlePage(BlogAdminQueryArticlePageDTO queryArticleDTO) {

        IPage<BlogAdminArticlePageVO> page = blogArticleMapper.getAdminBlogArticlePage(
                new Page<>(queryArticleDTO.getPageNum(), queryArticleDTO.getPageSize()),
                queryArticleDTO);
        List<BlogAdminArticlePageVO> articleVOList = page.getRecords();
        if(CollectionUtil.isNotEmpty(articleVOList)){
            articleVOList.forEach(articleVO->{
                //封装文章标签
                articleVO.setTagList(blogArticleTagMapper.getArticleTagsByArticleId(articleVO.getArticleId()));
                //封装评论数量
                articleVO.setCommentCounts(blogCommentMapper.getFrontCommentCounts(articleVO.getArticleId()));
                //TODO 封装点赞量
                articleVO.setLikedCounts(0);
            });
        }
        return new MyPageVO<>(page);
    }

    /**
     * 后台查询需要编辑的文章信息
     **/
    @Override
    public BlogAdminUpdateArticleVO getUpdateArticle(Integer articleId) {
        BlogAdminUpdateArticleVO updateArticleVO = blogArticleMapper.getUpdateArticle(articleId);
        if (updateArticleVO.getCategoryId() != null){
            updateArticleVO.setCategoryName(
                    blogCategoryMapper.getCategoryNameById(updateArticleVO.getCategoryId()));
        }
        return updateArticleVO;
    }

    /**
     * 后台更新文章信息
     **/
    @Override
    public void updateBlogArticle(BlogArticle updateArticle) {
        updateById(updateArticle);
    }

    /**
     * 后台修改文章顶置状态
     **/
    @Override
    public void updateArticleTop(BlogAdminUpdateArticleTopDTO updateTopDTO) {
        update(new LambdaUpdateWrapper<BlogArticle>()
                .eq(BlogArticle::getArticleId, updateTopDTO.getArticleId())
                .set(BlogArticle::getIsTop, updateTopDTO.getIsTop()));
    }

    /**
     * 要注意同时删除文章评论和标签
     **/
    @Override
    public void deleteBatchArticle(List<Integer> ids) {

        ids.forEach(deleteArticleId->{
            //删除评论
            blogCommentMapper.deleteCommentByArticleId(deleteArticleId);

            //删除标签

        });
        //删除文章
        removeBatchByIds(ids);
    }

    /**
     * 后台修改文章审核状态
     **/
    @Override
    public void updateArticleCheck(BlogAdminUpdateArticleCheckDTO updateCheckDTO) {
        update(new LambdaUpdateWrapper<BlogArticle>()
                .eq(BlogArticle::getArticleId, updateCheckDTO.getArticleId())
                .set(BlogArticle::getArticleCheck, updateCheckDTO.getArticleCheck()));
    }

    /**
     * 前台获取热门文章————浏览量最多的3篇文章
     **/
    @Override
    public List<BlogFrontHotArticleVO> getHotArticle(Integer pageNum, Integer pageSize) {

        return blogArticleMapper.getHotArticle(pageNum, pageSize);
    }

    /**
     * 前台获取文章归档
     **/
    @Override
    public MyPageVO<BlogFrontArticleArchiveVO> getArticleArchive(Integer pageNum, Integer pageSize) {
        IPage<BlogFrontArticleArchiveVO> archiveVOIPage;
        LoginUserVO loginUserVO = sysUserService.getCurrentLoginUser();

        //登录用户，能查看自己的所有文章和其他人的公开文章
        if(ObjectUtil.isNotEmpty(loginUserVO)){
            archiveVOIPage = blogArticleMapper.getArticleArchivePage(new Page<>(pageNum,pageSize),
                    loginUserVO.getId());
        }else { //匿名用户，只能查看公开文章
            archiveVOIPage = blogArticleMapper.getArticleArchivePage(new Page<>(pageNum,pageSize),
                    null);
        }
        return new MyPageVO<>(archiveVOIPage);
    }
}
