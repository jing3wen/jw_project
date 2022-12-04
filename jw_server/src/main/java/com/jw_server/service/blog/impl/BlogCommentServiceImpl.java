package com.jw_server.service.blog.impl;

import com.jw_server.dao.blog.entity.BlogComment;
import com.jw_server.dao.blog.mapper.BlogCommentMapper;
import com.jw_server.service.blog.IBlogCommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * Description 博客评论表 服务实现类
 * Author jingwen
 * Date 2022-12-03 17:17:39
 **/
@Service
public class BlogCommentServiceImpl extends ServiceImpl<BlogCommentMapper, BlogComment> implements IBlogCommentService {

}
