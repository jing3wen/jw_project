package com.jw_server.service.blog.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jw_server.core.common.MyPageVO;
import com.jw_server.core.constants.HttpCode;
import com.jw_server.core.exception.ServiceException;
import com.jw_server.dao.blog.dto.BlogAdminQueryCategoryPageDTO;
import com.jw_server.dao.blog.entity.BlogArticle;
import com.jw_server.dao.blog.entity.BlogCategory;
import com.jw_server.dao.blog.mapper.BlogArticleMapper;
import com.jw_server.dao.blog.mapper.BlogCategoryMapper;
import com.jw_server.dao.blog.vo.BlogAdminCategoryPageVO;
import com.jw_server.dao.blog.vo.BlogFrontCategoryVO;
import com.jw_server.service.blog.IBlogCategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Description 文章类别表 服务实现类
 * Author jingwen
 * Date 2022-12-03 16:13:45
 **/
@Service
public class BlogCategoryServiceImpl extends ServiceImpl<BlogCategoryMapper, BlogCategory> implements IBlogCategoryService {

    @Resource
    private BlogCategoryMapper blogCategoryMapper;

    @Resource
    private BlogArticleMapper blogArticleMapper;

    /**
     * 查询所有文章分类
     **/
    @Override
    public List<BlogFrontCategoryVO> getAllCategory() {


        return blogCategoryMapper.getAllCategory();
    }

    /**
     * 后台查询博客类别分页
     **/
    @Override
    public MyPageVO<BlogAdminCategoryPageVO> getBlogCategoryPageList(BlogAdminQueryCategoryPageDTO queryCategoryDTO) {

        IPage<BlogAdminCategoryPageVO> page = blogCategoryMapper.getBlogCategoryPageList(
                new Page<>(queryCategoryDTO.getPageNum(),queryCategoryDTO.getPageSize()),
                queryCategoryDTO);

        return new MyPageVO<>(page);
    }

    /**
     * 新增或更新文章类别
     **/
    @Override
    public void addOrUpdateBlogCategory(BlogCategory blogCategory) {
        //查询是否有同名类别
        BlogCategory findOne = blogCategoryMapper.selectOne(new LambdaQueryWrapper<BlogCategory>()
                        .select(BlogCategory::getCategoryId)
                        .eq(BlogCategory::getCategoryName,blogCategory.getCategoryName()));
        if(ObjectUtil.isNotEmpty(findOne) && !(findOne.getCategoryId().equals(blogCategory.getCategoryId()))){
            throw new ServiceException(HttpCode.CODE_400,"该名称已存在，请更换名称");
        }
        saveOrUpdate(blogCategory);
    }

    /**
     * 后台批量删除博客文章类别
     * 需要判断该类别下是否还有文章，若还有就提示不能删除
     **/
    @Override
    public void deleteCategoryByIds(List<Integer> ids) {
        Long count = blogArticleMapper.selectCount(new LambdaQueryWrapper<BlogArticle>()
                .in(BlogArticle::getCategoryId, ids));
        if (count > 0){
            throw new ServiceException(HttpCode.CODE_400, "该类别下还有文章，请先删除文章");
        }
        removeByIds(ids);
    }

    /**
     * 后台根据文章类别名搜索类别列表/直接搜索所有类别
     **/
    @Override
    public List<BlogFrontCategoryVO> getAllCategoryByCategoryNameOrNot(String categoryName) {
        return blogCategoryMapper.getAllCategoryByCategoryNameOrNot(categoryName);
    }

}
