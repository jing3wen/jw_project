package com.jw_server.service.blog.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jw_server.core.common.MyPageVO;
import com.jw_server.dao.blog.dto.FrontAddCommentDTO;
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
     * floorCommentId = 0 表示查询一级评论分页
     *  首先查询文章的第一级评论，(floorCommentId=0的评论)
     *  再分别查询该一级评论下的二级评论分页
     *
     * floorCommentId != 0 表示查询二级评论分页
     **/
    @Override
    public MyPageVO<BlogFrontCommentVO> getFrontCommentByArticleId(Integer articleId, Integer floorCommentId, Integer pageNum, Integer pageSize) {

        IPage<BlogFrontCommentVO> commentIPage = blogCommentMapper.getFrontCommentPageVO(articleId,
                floorCommentId,
                new Page<>(pageNum,pageSize));

        //前台查询二级评论, 直接返回
        if (floorCommentId!=0){
            return new MyPageVO<>(commentIPage);
        }else{
            //前台查询一级评论,需要封装一级评论下的二级评论, 其实这个else可以省去,但是为了方便后续查看我还是写上了
            List<BlogFrontCommentVO> parentList= commentIPage.getRecords();
            /*
             * 开始查询二级评论
             **/
            parentList.forEach(parent -> {
                IPage<BlogFrontCommentVO> childPage = blogCommentMapper.getFrontCommentPageVO(articleId,
                        parent.getCommentId(),
                        new Page<>(0, 5));
                parent.setReplyCommentCounts(childPage.getTotal());
                parent.setReplyCommentList(childPage.getRecords());
            });

            return new MyPageVO<>(commentIPage.getPages(),
                    commentIPage.getCurrent(),
                    commentIPage.getSize(),
                    commentIPage.getTotal(),
                    parentList);
        }
    }

    /**
     * 获取文章评论总数量
     **/
    @Override
    public Integer getFrontCommentCounts(Integer articleId) {

        return blogCommentMapper.getFrontCommentCounts(articleId);
    }

    /**
     * 查询二级评论分页
     **/
    public List<BlogFrontCommentVO> getChildCommentByArticleId(Integer articleId, Integer floorCommentId,Integer pageNum, Integer pageSize) {

        return blogCommentMapper.getFrontCommentPageVO(articleId,
                floorCommentId,
                new Page<>(pageNum, pageSize)).getRecords();
    }

    /**
     * 新增评论
     **/
    @Override
    public void addComment(FrontAddCommentDTO frontAddCommentDTO) {
        //新增一条评论
        BlogComment addComment = new BlogComment();
        BeanUtil.copyProperties(frontAddCommentDTO, addComment);
        save(addComment);
    }

    /**
     * 前台删除评论
     **/
    @Override
    public void deleteComment(Integer commentId) {

        BlogComment findOne = getOne(new LambdaQueryWrapper<BlogComment>()
                .select(BlogComment::getArticleId)
                .eq(BlogComment::getCommentId,commentId));

//        if(ObjectUtil.isNotEmpty(findOne)){
//            int deleteNum = blogCommentMapper.deleteComment(commentId);
//            logger.warn("用户删除了 "+ deleteNum +" 条文章评论, 开始更新文章评论数");
//            blogArticleMapper.updateArticleCommentCounts(findOne.getArticleId(), (-1)*deleteNum);
//        }
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
