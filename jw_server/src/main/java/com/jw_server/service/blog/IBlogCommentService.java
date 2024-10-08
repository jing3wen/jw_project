package com.jw_server.service.blog;

import com.jw_server.core.common.MyPageVO;
import com.jw_server.dao.blog.dto.BlogAdminUpdateCheckBatchDTO;
import com.jw_server.dao.blog.dto.BlogFrontAddCommentDTO;
import com.jw_server.dao.blog.dto.BlogAdminQueryCommentPageDTO;
import com.jw_server.dao.blog.dto.BlogFrontCommentPageDTO;
import com.jw_server.dao.blog.entity.BlogComment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jw_server.dao.blog.vo.BlogAdminCommentPageVO;
import com.jw_server.dao.blog.vo.BlogFrontCommentVO;


import java.util.List;


/**
 * Description 博客评论表 服务类
 * Author jingwen
 * Date 2022-12-03 17:17:39
 **/
public interface IBlogCommentService extends IService<BlogComment> {

    /**
     * Description: 根据文章id查询该文章的评论分页
     * Author: jingwen
     * Date: 2023/1/4 16:33
     **/
    MyPageVO<BlogFrontCommentVO> getFrontCommentByArticleId(BlogFrontCommentPageDTO frontCommentPageDTO);

    /**
     * Description: 获取文章评论总数量
     * Author: jingwen
     * Date: 2023/2/6 20:03
     **/
    Integer getFrontCommentCounts(Integer articleId);

    /**
     * Description: 新增一条评论，该文章评论数+1
     * Author: jingwen
     * Date: 2023/1/4 21:51
     **/
    void addComment(BlogFrontAddCommentDTO frontAddCommentDTO);

    /**
     * Description: 后台审核博客文章评论
     * Author: jingwen
     * Date: 2023/1/13 14:26
     **/
    void updateCommentCheckBatch(BlogAdminUpdateCheckBatchDTO updateCheckBatchDTO);

    /**
     * Description: 后台批量删除博客文章评论，子评论也会被删除
     * Author: jingwen
     * Date: 2023/1/13 10:38
     **/
    void deleteBatchComment(List<Integer> ids);

    /**
     * Description: 后台查询评论分页
     * Author: jingwen
     * Date: 2023/1/13 11:22
     **/
    MyPageVO<BlogAdminCommentPageVO> getAdminCommentPageList(BlogAdminQueryCommentPageDTO queryCommentPageDTO);

}
