package com.jw_server.dao.blog.mapper;

import com.jw_server.dao.blog.entity.BlogTag;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jw_server.dao.blog.vo.BlogFrontTagVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


/**
 * Description 博客标签表 Mapper 接口
 * Author jingwen
 * Date 2023-02-04 15:20:40
 **/
@Mapper
public interface BlogTagMapper extends BaseMapper<BlogTag> {

    /**
     * Description: 前台查询所有标签
     * Author: jingwen
     * Date: 2023/2/8 9:55
     **/
    List<BlogFrontTagVO> getAllFrontTag();
}
