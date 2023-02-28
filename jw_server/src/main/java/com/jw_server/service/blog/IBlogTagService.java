package com.jw_server.service.blog;

import com.jw_server.core.common.MyPageVO;
import com.jw_server.dao.blog.entity.BlogTag;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jw_server.dao.blog.vo.BlogAdminTagPageVO;
import com.jw_server.dao.blog.vo.BlogFrontTagVO;

import java.util.List;


/**
 * Description 博客标签表 服务类
 * Author jingwen
 * Date 2023-02-04 15:20:40
 **/
public interface IBlogTagService extends IService<BlogTag> {

    /**
     * Description: 前台查询所有标签
     * Author: jingwen
     * Date: 2023/2/8 10:04
     **/
    List<BlogFrontTagVO> getAllFrontTag();

    /**
     * Description: 新增文章标签, 要检查同名
     * Author: jingwen
     * Date: 2023/2/28 12:33
     **/
    void addOrUpdateBlogTag(BlogTag blogTag);

    /**
     * Description: 后台批量删除博客标签
     * Author: jingwen
     * Date: 2023/2/28 15:52
     **/
    void deleteBlogTagBatch(List<Integer> ids);

    /**
     * Description: 博客后台分页查询文章标签
     * Author: jingwen
     * Date: 2023/2/28 16:17
     **/
    MyPageVO<BlogAdminTagPageVO> getAdminTagPage(Integer pageNum, Integer pageSize, String tagName);

    /**
     * Description: 后台根据文章类别名搜索类别列表
     * Author: jingwen
     * Date: 2023/1/25 16:53
     **/
    List<BlogFrontTagVO> getTagListByTageNameOrNot(String tagName);
}
