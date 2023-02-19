package com.jw_server.dao.blog.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jw_server.dao.blog.dto.BlogFrontQueryArticlePageDTO;
import com.jw_server.dao.blog.dto.BlogAdminQueryArticlePageDTO;
import com.jw_server.dao.blog.entity.BlogArticle;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jw_server.dao.blog.vo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * Description 博客文章表 Mapper 接口
 * Author jingwen
 * Date 2022-12-03 16:11:56
 **/
@Mapper
public interface BlogArticleMapper extends BaseMapper<BlogArticle> {

    /**
     * Description: 查询博客前台文章分页
     * 匿名访问， 查询所用公开文章
     * Author: jingwen
     * Date: 2022/12/4 13:39
     **/
    IPage<BlogFrontArticlePageVO> getFrontPublicArticlePage(Page<BlogFrontArticlePageVO> page,
                                                      @Param("queryArticleDTO") BlogFrontQueryArticlePageDTO queryArticlePageDTO);

    /**
     * Description: 查询博客前台文章分页
     * 登录用户访问：查询当前用户所有文章和其他用户公开文章
     * Author: jingwen
     * Date: 2022/12/4 13:39
     **/
    IPage<BlogFrontArticlePageVO> getFrontLoginUserArticlePage(Page<BlogFrontArticlePageVO> page,
                                                               @Param("loginUserId") Integer loginUserId,
                                                               @Param("queryArticleDTO") BlogFrontQueryArticlePageDTO queryArticlePageDTO);

    /**
     * Description: 更新文章浏览量
     * Author: jingwen
     * Date: 2022/12/4 13:39
     **/
    void updateArticleViewCounts(Integer articleId);

    /**
     * Description: 查询文章细节
     * Author: jingwen
     * Date: 2022/12/4 13:54
     **/
    BlogFrontArticleDetailsVO getFrontArticleDetails(Integer articleId);

    /**
     * Description: 更新文章评论数
     * Author: jingwen
     * Date: 2022/12/4 13:39
     **/
    void updateArticleCommentCounts(Integer articleId, Integer updateNumber);

    /**
     * Description: 博客后台查询文章列表
     * Author: jingwen
     * Date: 2023/1/10 21:52
     **/
    IPage<BlogAdminArticlePageVO> getAdminBlogArticlePage(Page<BlogAdminArticlePageVO> page,
                                                         @Param("queryArticleDTO") BlogAdminQueryArticlePageDTO queryArticleDTO);

    /**
     * Description:
     * Author: jingwen
     * Date: 2023/1/12 12:24
     **/
    BlogAdminUpdateArticleVO getUpdateArticle(Integer articleId);

    /**
     * Description: 前台获取热门文章————浏览量最多的3篇文章
     * Author: jingwen
     * Date: 2023/1/28 14:05
     **/
    List<BlogFrontHotArticleVO> getHotArticle(Integer pageNum, Integer pageSize);

    /**
     * Description: 前台获取文章归档
     * loginUserId==null 匿名访问，只查询所有公开文章
     * loginUserId!=null 登录用户访问，查询登录用户所有文章和其他用户公开文章
     * Author: jingwen
     * Date: 2023/2/18 21:33
     **/
    IPage<BlogFrontArticleArchiveVO> getArticleArchivePage(Page<BlogFrontArticlePageVO> page,
                                                          @Param("loginUserId") Integer loginUserId);
}
