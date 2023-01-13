package com.jw_server.dao.blog.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jw_server.dao.blog.dto.QueryBlogAdminArticlePageDTO;
import com.jw_server.dao.blog.entity.BlogArticle;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jw_server.dao.blog.vo.BlogAdminArticlePageVO;
import com.jw_server.dao.blog.vo.BlogAdminUpdateArticleVO;
import com.jw_server.dao.blog.vo.BlogFrontArticleDetailsVO;
import com.jw_server.dao.blog.vo.BlogFrontArticlePageVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


/**
 * Description 博客文章表 Mapper 接口
 * Author jingwen
 * Date 2022-12-03 16:11:56
 **/
@Mapper
public interface BlogArticleMapper extends BaseMapper<BlogArticle> {

    /**
     * Description: 查询博客前台文章分页
     * Author: jingwen
     * Date: 2022/12/4 13:39
     **/
    IPage<BlogFrontArticlePageVO> getFrontArticlePage(Page<BlogFrontArticlePageVO> page);

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
    BlogFrontArticleDetailsVO getBlogFrontArticleDetails(Integer articleId);

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
                                                         @Param("queryArticleDTO") QueryBlogAdminArticlePageDTO queryArticleDTO);

    /**
     * Description:
     * Author: jingwen
     * Date: 2023/1/12 12:24
     **/
    BlogAdminUpdateArticleVO getUpdateArticle(Integer articleId);
}
