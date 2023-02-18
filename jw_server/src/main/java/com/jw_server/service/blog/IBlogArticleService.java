package com.jw_server.service.blog;

import com.jw_server.core.common.MyPageVO;
import com.jw_server.dao.blog.dto.*;
import com.jw_server.dao.blog.entity.BlogArticle;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jw_server.dao.blog.vo.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * Description 博客文章表 服务类
 * Author jingwen
 * Date 2022-12-03 16:11:56
 **/
public interface IBlogArticleService extends IService<BlogArticle> {


    /**
     * Description: 前台查询文章列表/根据文章类别查询文章列表
     * Author: jingwen
     * Date: 2022/12/3 17:00
     **/
    MyPageVO<BlogFrontArticlePageVO> getFrontArticlePage(BlogFrontQueryArticlePageDTO blogFrontQueryArticlePageDTO);

    /**
     * Description: 浏览文章详细信息
     * Author: jingwen
     * Date: 2022/12/4 12:10
     **/
    BlogFrontArticleDetailsVO getFrontArticleDetails(Integer articleId, HttpServletRequest request);

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
    MyPageVO<BlogAdminArticlePageVO> getAdminBlogArticlePage(BlogAdminQueryArticlePageDTO blogAdminQueryArticlePageDTO);

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

    /**
     * Description: 后台修改文章顶置状态
     * Author: jingwen
     * Date: 2023/1/26 12:48
     **/
    void updateArticleTop(BlogAdminUpdateArticleTopDTO updateTopDTO);

    /**
     * Description: 要注意同时删除文章评论和标签
     * Author: jingwen
     * Date: 2023/1/26 13:19
     **/
    void deleteBatchArticle(List<Integer> ids);

    /**
     * Description: 后台修改文章审核状态
     * Author: jingwen
     * Date: 2023/1/26 12:48
     **/
    void updateArticleCheck(BlogAdminUpdateArticleCheckDTO updateCheckDTO);

    /**
     * Description: 前台获取热门文章————浏览量最多的3篇文章
     * Author: jingwen
     * Date: 2023/1/28 13:54
     **/
    List<BlogFrontHotArticleVO> getHotArticle(Integer pageNum, Integer pageSize);


    /**
     * Description: 前台获取文章归档
     * Author: jingwen
     * Date: 2023/2/18 21:29
     **/
    List<BlogFrontArticleArchiveVO> getArticleArchive(Integer pageNum, Integer pageSize);
}
