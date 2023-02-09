package com.jw_server.service.blog.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.jw_server.dao.blog.entity.BlogMessage;
import com.jw_server.dao.blog.mapper.BlogMessageMapper;
import com.jw_server.service.blog.IBlogMessageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

import static com.jw_server.core.constants.BlogConst.*;

/**
 * Description  服务实现类
 * Author jingwen
 * Date 2023-02-09 14:24:46
 **/
@Service
public class BlogMessageServiceImpl extends ServiceImpl<BlogMessageMapper, BlogMessage> implements IBlogMessageService {

    @Resource
    private BlogMessageMapper blogMessageMapper;

    /**
     * 查询留言，若数量太多，则限制数量
     **/
    @Override
    public List<BlogMessage> getMessageList() {

        List<BlogMessage> messageList;
        Integer messageCount = Integer.parseInt(
                String.valueOf(
                        count(new LambdaQueryWrapper<BlogMessage>()
                        .eq(BlogMessage::getMessageCheck, MESSAGE_CHECK_PASS))));
        if (messageCount > MAX_MESSAGE_COUNT && MAX_MESSAGE_COUNT != 0){
            messageList =  list(new LambdaQueryWrapper<BlogMessage>()
                    .eq(BlogMessage::getMessageCheck, MESSAGE_CHECK_PASS)
                    .orderByDesc(BlogMessage::getCreateTime)
                   .last("LIMIT ".concat(MAX_MESSAGE_COUNT.toString())));
        }else {
            messageList =  list(new LambdaQueryWrapper<BlogMessage>()
                    .eq(BlogMessage::getMessageCheck, MESSAGE_CHECK_PASS)
                    .orderByDesc(BlogMessage::getCreateTime));
        }
        return messageList;
    }
}
