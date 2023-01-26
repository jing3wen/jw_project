package com.jw_server.service.blog;

import com.jw_server.core.common.MyPageVO;
import com.jw_server.dao.blog.dto.QueryBlogAdminCategoryPageDTO;
import com.jw_server.dao.blog.entity.BlogCategory;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jw_server.dao.blog.vo.BlogAdminCategoryPageVO;
import com.jw_server.dao.blog.vo.BlogFrontCategoryVO;


import java.util.List;


/**
 * Description 文章类别表 服务类
 * Author jingwen
 * Date 2022-12-03 16:13:45
 **/
public interface IBlogCategoryService extends IService<BlogCategory> {

    /**
     * Description: 查询所有文章分类
     * Author: jingwen
     * Date: 2023/1/8 21:59
     **/
    List<BlogFrontCategoryVO> getAllCategory();

    /**
     * Description 后台查询博客类别分页
     * Author jingwen
     * Date 2023/1/12 22:09
     **/
    MyPageVO<BlogAdminCategoryPageVO> getBlogCategoryPageList(QueryBlogAdminCategoryPageDTO queryCategoryDTO);

    /**
     * Description: 新增或更新文章类别
     * Author: jingwen
     * Date: 2023/1/12 23:04
     **/
    void addOrUpdateBlogCategory(BlogCategory blogCategory);

    /**
     * Description: 后台批量删除博客文章类别
     * 需要判断该类别下是否还有文章，若还有就提示不能删除
     * Author: jingwen
     * Date: 2023/1/12 23:45
     **/
    void deleteCategoryByIds(List<Integer> ids);

    /**
     * Description: 后台根据文章类别名搜索类别列表
     * Author: jingwen
     * Date: 2023/1/25 16:53
     **/
    List<BlogFrontCategoryVO> searchBlogCategoryList(String categoryName);

}
