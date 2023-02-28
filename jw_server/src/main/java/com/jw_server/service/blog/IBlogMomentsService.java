package com.jw_server.service.blog;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jw_server.core.common.MyPageVO;
import com.jw_server.dao.blog.dto.BlogFrontMomentsPageDTO;
import com.jw_server.dao.blog.entity.BlogMoments;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jw_server.dao.blog.vo.BlogAdminMomentsVO;
import com.jw_server.dao.blog.vo.BlogFrontMomentsPageVO;


/**
 * Description 朋友圈表 服务类
 * Author jingwen
 * Date 2023-02-11 20:48:10
 **/
public interface IBlogMomentsService extends IService<BlogMoments> {

    /**
     * Description 前台查询朋友圈分页
     * Author jingwen
     * Date 2023-02-11 20:48:10
     **/
    MyPageVO<BlogFrontMomentsPageVO> getFrontMomentsPage(BlogFrontMomentsPageDTO frontMomentsPageDTO);

    /**
     * Description: 后台查询朋友圈分页
     * Author: jingwen
     * Date: 2023/2/28 17:03
     **/
    MyPageVO<BlogAdminMomentsVO> getAdminMomentsPage(Integer pageNum, Integer pageSize, String nickname);
}
