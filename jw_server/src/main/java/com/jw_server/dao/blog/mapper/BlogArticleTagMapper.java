package com.jw_server.dao.blog.mapper;

import com.jw_server.dao.blog.entity.BlogArticleTag;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jw_server.dao.blog.vo.FrontTagVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


/**
 * Description 博客标签关系表 Mapper 接口
 * Author jingwen
 * Date 2023-02-04 15:21:08
 **/
@Mapper
public interface BlogArticleTagMapper extends BaseMapper<BlogArticleTag> {

    /**
     * Description: 根据文章id查询所有标签名
     * Author: jingwen
     * Date: 2023/2/4 15:29
     **/
    List<FrontTagVO> getArticleTagsByArticleId(Integer articleId);


}
