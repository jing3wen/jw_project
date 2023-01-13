package com.jw_server.service.blog.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jw_server.core.common.MyPageVO;
import com.jw_server.dao.blog.dto.BlogFrontCommentDTO;
import com.jw_server.dao.blog.dto.QueryBlogAdminCommentPageDTO;
import com.jw_server.dao.blog.entity.BlogComment;
import com.jw_server.dao.blog.mapper.BlogArticleMapper;
import com.jw_server.dao.blog.mapper.BlogCommentMapper;
import com.jw_server.dao.blog.vo.BlogAdminCommentPageVO;
import com.jw_server.dao.blog.vo.BlogFrontCommentVO;
import com.jw_server.service.blog.IBlogCommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Description 博客评论表 服务实现类
 * Author jingwen
 * Date 2022-12-03 17:17:39
 **/
@Service
public class BlogCommentServiceImpl extends ServiceImpl<BlogCommentMapper, BlogComment> implements IBlogCommentService {

    @Resource
    private BlogCommentMapper blogCommentMapper;
    @Resource
    private BlogArticleMapper blogArticleMapper;

    Log logger = LogFactory.getLog(BlogCommentServiceImpl.class);


    /**
     * 首先查询文章的第一级评论，
     * 再分别查询该一级评论下的子评论
     **/
    @Override
    public MyPageVO<BlogFrontCommentVO> getCommentByArticleId(Integer articleId, Integer pageNum, Integer pageSize) {

        IPage<BlogFrontCommentVO> commentIPage = blogCommentMapper.getBlogFrontParentCommentPageVO(articleId,
                0,
                new Page<>(pageNum,pageSize));

        List<BlogFrontCommentVO> parentList= commentIPage.getRecords();
        /*
         * 开始查询二级评论
         **/
        parentList.forEach(parent -> {
            //List<BlogFrontCommentVO> childList = getChildCommentByArticleId(articleId, parent.getCommentId(), 0, 3);
            List<BlogFrontCommentVO> childList = blogCommentMapper.getAllBlogFrontChildCommentPageVO(articleId,parent.getCommentId());
            parent.setToCommentList(childList);
        });

        return new MyPageVO<>(commentIPage.getPages(),
                commentIPage.getCurrent(),
                commentIPage.getSize(),
                commentIPage.getTotal(),
                parentList);
    }

    /**
     * 查询二级评论
     * TODO 代码中有一些可以优化的地方，我的一级评论设置成分页，二级评论没有设置分页，后续可以改进
     **/
    public List<BlogFrontCommentVO> getChildCommentByArticleId(Integer articleId, Integer parentId,Integer pageNum, Integer pageSize) {

        return blogCommentMapper.getBlogFrontChildCommentPageVO(articleId,
                parentId,
                new Page<>(pageNum, pageSize)).getRecords();
    }

    /**
     * 新增评论，更新文章评论数
     **/
    @Override
    public void addComment(BlogFrontCommentDTO blogFrontCommentDTO) {
        //新增一条评论
        BlogComment addComment = new BlogComment();
        BeanUtil.copyProperties(blogFrontCommentDTO,addComment);
        save(addComment);
        //更新评论数
        blogArticleMapper.updateArticleCommentCounts(blogFrontCommentDTO.getArticleId(),+1);
    }

    /**
     * 前台删除评论
     **/
    @Override
    public void deleteComment(Integer commentId) {

        BlogComment findOne = getOne(new LambdaQueryWrapper<BlogComment>()
                .select(BlogComment::getArticleId)
                .eq(BlogComment::getCommentId,commentId));

        if(ObjectUtil.isNotEmpty(findOne)){
            int deleteNum = blogCommentMapper.deleteComment(commentId);
            logger.warn("用户删除了 "+ deleteNum +" 条文章评论, 开始更新文章评论数");
            blogArticleMapper.updateArticleCommentCounts(findOne.getArticleId(), (-1)*deleteNum);
        }
    }

    /**
     * 后台审核博客文章评论
     **/
    @Override
    public void updateCommentCheckBatch(List<Integer> ids) {

        blogCommentMapper.updateCommentCheckBatch(ids);
    }

    /**
     * 后台批量删除博客文章评论
     **/
    @Override
    public void deleteBatchComment(List<Integer> ids) {
        ids.forEach(deleteId->{
            deleteComment(deleteId);
        });
    }

    /**
     * 后台查询评论分页
     **/
    @Override
    public MyPageVO<BlogAdminCommentPageVO> getAdminCommentPageList(QueryBlogAdminCommentPageDTO queryCommentPageDTO) {

        IPage<BlogAdminCommentPageVO> page = blogCommentMapper.getAdminCommentPageList(
                new Page<>(queryCommentPageDTO.getPageNum(),queryCommentPageDTO.getPageSize()),
                queryCommentPageDTO);
        return new MyPageVO<>(page);
    }
}
