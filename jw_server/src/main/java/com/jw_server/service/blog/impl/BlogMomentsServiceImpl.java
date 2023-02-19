package com.jw_server.service.blog.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jw_server.core.common.MyPageVO;
import com.jw_server.dao.blog.dto.BlogFrontMomentsPageDTO;
import com.jw_server.dao.blog.entity.BlogMoments;
import com.jw_server.dao.blog.mapper.BlogMomentsMapper;
import com.jw_server.dao.blog.vo.BlogFrontMomentsPageVO;
import com.jw_server.service.blog.IBlogMomentsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Description 朋友圈表 服务实现类
 * Author jingwen
 * Date 2023-02-11 20:48:10
 **/
@Service
public class BlogMomentsServiceImpl extends ServiceImpl<BlogMomentsMapper, BlogMoments> implements IBlogMomentsService {

    @Resource
    private BlogMomentsMapper blogMomentsMapper;

    /**
     * 前台查询朋友圈分页
     **/
    @Override
    public MyPageVO<BlogFrontMomentsPageVO> getFrontMomentsPage(BlogFrontMomentsPageDTO frontMomentsPageDTO) {
        IPage<BlogFrontMomentsPageVO> page;
        //当前用户为登录用户，可以查询当前用户的所有朋友圈和其他用户的公开朋友圈
        if(frontMomentsPageDTO.getLoginUserId() != null) {
            page = blogMomentsMapper.getFrontLoginUserMomentsPage(
                    new Page<>(frontMomentsPageDTO.getPageNum(), frontMomentsPageDTO.getPageSize()),
                    frontMomentsPageDTO.getLoginUserId(),
                    frontMomentsPageDTO.getViewMe());

        }else {
            //当前用户为游客，只查询当前所有用户的公开朋友圈
            page= blogMomentsMapper.getFrontAllPublicMomentsPage(
                    new Page<>(frontMomentsPageDTO.getPageNum(), frontMomentsPageDTO.getPageSize()));
        }
        return  new MyPageVO<>(page);
    }
}
