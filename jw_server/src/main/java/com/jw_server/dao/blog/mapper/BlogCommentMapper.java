package com.jw_server.dao.blog.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jw_server.dao.blog.dto.BlogAdminQueryCommentPageDTO;
import com.jw_server.dao.blog.entity.BlogComment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jw_server.dao.blog.vo.BlogAdminCommentPageVO;
import com.jw_server.dao.blog.vo.BlogFrontCommentVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * Description 博客评论表 Mapper 接口
 * Author jingwen
 * Date 2022-12-03 17:17:39
 **/
@Mapper
public interface BlogCommentMapper extends BaseMapper<BlogComment> {

    /**
     * Description:
     *  parentId = 0,（按评论时间降序）查询第一级评论
     * Author: jingwen 
     * Date: 2023/1/4 16:44
     **/
    IPage<BlogFrontCommentVO> getFrontCommentPageVO(String commentType, Integer articleId, Integer floorCommentId, Page<BlogFrontCommentVO> page);


    /**
     * Description: 删除一条评论及子评论
     * Author: jingwen
     * Date: 2023/1/13 14:31
     **/
    int deleteComment(Integer commentId);


    /**
     * Description: 后台查询评论分页
     * Author: jingwen
     * Date: 2023/1/13 11:25
     **/
    IPage<BlogAdminCommentPageVO> getAdminCommentPageList(Page<BlogAdminCommentPageVO> objectPage,
                                                          @Param("queryCommentPageDTO") BlogAdminQueryCommentPageDTO queryCommentPageDTO);

    /**
     * Description: 批量审核评论
     * Author: jingwen
     * Date: 2023/1/13 14:32
     **/
    void updateCommentCheckBatch(List<Integer> ids);


    /**
     * Description: 根据文章ID删除所有评论
     * Author: jingwen
     * Date: 2023/1/26 13:24
     **/
    void deleteCommentByArticleId(Integer articleId);

    /**
     * Description: 获取文章评论总数量
     * Author: jingwen
     * Date: 2023/2/6 20:43
     **/
    Integer getFrontCommentCounts(Integer articleId);
}
