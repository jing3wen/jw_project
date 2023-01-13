package com.jw_server.service.blog;

import com.jw_server.core.common.MyPageVO;
import com.jw_server.dao.blog.dto.BlogAdminAddArticleDTO;
import com.jw_server.dao.blog.dto.QueryBlogAdminArticlePageDTO;
import com.jw_server.dao.blog.entity.BlogArticle;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jw_server.dao.blog.vo.BlogAdminArticlePageVO;
import com.jw_server.dao.blog.vo.BlogAdminUpdateArticleVO;
import com.jw_server.dao.blog.vo.BlogFrontArticleDetailsVO;
import com.jw_server.dao.blog.vo.BlogFrontArticlePageVO;

import javax.servlet.http.HttpServletRequest;


/**
 * Description 博客文章表 服务类
 * Author jingwen
 * Date 2022-12-03 16:11:56
 **/
public interface IBlogArticleService extends IService<BlogArticle> {


    /**
     * Description: 前台查询文章列表
     * Author: jingwen
     * Date: 2022/12/3 17:00
     **/
    MyPageVO<BlogFrontArticlePageVO> getBlogFrontArticlePage(Integer pageNum, Integer pageSize);

    /**
     * Description: 浏览文章详细信息
     * Author: jingwen
     * Date: 2022/12/4 12:10
     **/
    BlogFrontArticleDetailsVO getBlogFrontArticleDetails(Integer articleId, HttpServletRequest request);

    /**
     * Description: 博客后台新增文章
     * Author: jingwen
     * Date: 2023/1/9 21:19
     **/
    void addBlogArticle(BlogAdminAddArticleDTO blogAdminAddArticleDTO);

    /**
     * Description: 博客后台查询文章列表
     * Author: jingwen
     * Date: 2023/1/10 11:22
     **/
    MyPageVO<BlogAdminArticlePageVO> getAdminBlogArticlePage(QueryBlogAdminArticlePageDTO queryBlogAdminArticlePageDTO);

    /**
     * Description: 后台查询需要编辑的文章信息
     * Author: jingwen
     * Date: 2023/1/12 12:23
     **/
    BlogAdminUpdateArticleVO getUpdateArticle(Integer articleId);

    /**
     * Description: 后台更新文章信息
     * Author: jingwen
     * Date: 2023/1/12 14:18
     **/
    void updateBlogArticle(BlogArticle updateArticle);
}
