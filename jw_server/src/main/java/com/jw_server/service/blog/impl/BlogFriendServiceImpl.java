package com.jw_server.service.blog.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jw_server.core.common.MyPageVO;
import com.jw_server.core.constants.HttpCode;
import com.jw_server.core.exception.ServiceException;
import com.jw_server.dao.blog.dto.BlogAdminUpdateCheckBatchDTO;
import com.jw_server.dao.blog.entity.BlogFriend;
import com.jw_server.dao.blog.mapper.BlogFriendMapper;
import com.jw_server.service.blog.IBlogFriendService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description  服务实现类
 * Author jingwen
 * Date 2023-02-09 21:59:28
 **/
@Service
public class BlogFriendServiceImpl extends ServiceImpl<BlogFriendMapper, BlogFriend> implements IBlogFriendService {

    /**
     * 查询所有友链
     **/
    @Override
    public List<BlogFriend> getAllFriend() {

        return list(new LambdaQueryWrapper<BlogFriend>()
                .eq(BlogFriend::getStatus,"1")
                .orderByDesc(BlogFriend::getCreateTime)
        );
    }


    /**
     * 后台批量更新友链审核状态
     **/
    @Override
    public void updateFriendCheckBatch(BlogAdminUpdateCheckBatchDTO updateCheckBatchDTO) {

        if(StrUtil.isEmpty(updateCheckBatchDTO.getCheckStatus())){
            throw new ServiceException(HttpCode.CODE_400, "更新的状态参数有误");
        }
        if(ObjectUtil.isEmpty(updateCheckBatchDTO.getIds()) || updateCheckBatchDTO.getIds().size()==0){
            throw new ServiceException(HttpCode.CODE_400, "请选择要更新的数据");
        }
        update(new LambdaUpdateWrapper<BlogFriend>()
                .set(BlogFriend::getStatus, updateCheckBatchDTO.getCheckStatus())
                .in(BlogFriend::getFriendId, updateCheckBatchDTO.getIds()));
    }

    /**
     * 后台分页查询友链
     **/
    @Override
    public MyPageVO<BlogFriend> getAdminFriendPage(Integer pageNum, Integer pageSize, String status) {

        LambdaQueryWrapper<BlogFriend> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if(StrUtil.isNotEmpty(status)) {
            lambdaQueryWrapper.eq(BlogFriend::getStatus, status);
        }
        return new MyPageVO<>(
                page(new Page<>(pageNum,pageSize),lambdaQueryWrapper)
        );
    }
}
