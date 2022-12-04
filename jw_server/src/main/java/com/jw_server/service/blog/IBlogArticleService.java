package com.jw_server.service.blog;

import com.jw_server.core.common.MyPageVO;
import com.jw_server.dao.blog.entity.BlogArticle;
import com.baomidou.mybatisplus.extension.service.IService;
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
}
