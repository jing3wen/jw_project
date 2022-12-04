package com.jw_server.dao.blog.mapper;

import com.jw_server.dao.blog.entity.BlogComment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;


/**
 * Description 博客评论表 Mapper 接口
 * Author jingwen
 * Date 2022-12-03 17:17:39
 **/
@Mapper
public interface BlogCommentMapper extends BaseMapper<BlogComment> {

}
