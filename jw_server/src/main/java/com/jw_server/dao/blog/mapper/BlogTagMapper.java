package com.jw_server.dao.blog.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jw_server.dao.blog.entity.BlogTag;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jw_server.dao.blog.vo.BlogAdminTagPageVO;
import com.jw_server.dao.blog.vo.BlogFrontTagVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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

    /**
     * Description: 博客后台分页查询文章标签
     * Author: jingwen
     * Date: 2023/2/28 16:22
     **/
    IPage<BlogAdminTagPageVO> getAdminTagPage(Page<BlogAdminTagPageVO> blogAdminTagPageVOPage, String tagName);


    /**
     * Description: 根据标签名查询所有标签或者直接查询所有标签
     * Author: jingwen
     * Date: 2023/2/28 20:19
     **/
    List<BlogFrontTagVO> getAllTagByTagNameOrNot(String tagName);
}
