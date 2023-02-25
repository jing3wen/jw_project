package com.jw_server.core.utils;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import java.util.Date;
import java.util.List;

/**
 * Description: 发送邮箱工具类
 * Author: jingwen
 * DATE: 2023/2/25 17:37
 */
@Component
@Slf4j
public class EMailUtils {

    @Resource
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String sendMailer;

    @Async
    public void sendMailMessage(List<String> to, String subject, String text) {
        log.info(sendMailer+" 发送邮件===================");
        log.info("to：{}", JSON.toJSONString(to));
        log.info("subject：{}", subject);
        log.info("text：{}", text);
        try {
            //true代表支持复杂的类型
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mailSender.createMimeMessage(), true);
            //邮件发信人
            mimeMessageHelper.setFrom(sendMailer);
            //邮件收信人1或多个
            mimeMessageHelper.setTo(to.toArray(new String[0]));
            //邮件主题
            mimeMessageHelper.setSubject(subject);
            //邮件内容
            mimeMessageHelper.setText(text, true);
            //邮件发送时间
            mimeMessageHelper.setSentDate(new Date());

            //发送邮件
            mailSender.send(mimeMessageHelper.getMimeMessage());

            log.info("发送成功==================");
        } catch (MessagingException e) {
            log.info("发送失败==================");
            log.error(e.getMessage());
        }
    }


}
