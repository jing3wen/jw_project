package com.jw_server.config.mybatis_plus;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.jw_server.dao.system.vo.UserDetailsVO;
import com.jw_server.service.system.ISysUserService;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * Description: 数据库字段自动填充
 * Author: jingwen
 * DATE: 2022/8/31 18:15
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    private static String CREATE_USER = "createBy";
    private static String CREATE_DATE = "createTime";

    private static String UPDATE_USER = "updateBy";
    private static String UPDATE_DATE = "updateTime";

    /**
     * 插入时的填充策略
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        if (metaObject.hasSetter(CREATE_DATE)){
            //实现填充业务逻辑
            this.setFieldValByName(CREATE_DATE, LocalDateTime.now(), metaObject);
            this.setFieldValByName(UPDATE_DATE, LocalDateTime.now(), metaObject);
        }
        if (metaObject.hasSetter(CREATE_USER)){
            //实现填充业务逻辑
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            // authentication.getPrincipal() = "anonymousUser"时一般为用户注册
            if(authentication.getPrincipal()!= "anonymousUser"){
                UserDetailsVO userDetailsVO = (UserDetailsVO)authentication.getPrincipal();
                this.setFieldValByName(CREATE_USER, userDetailsVO.getUsername(), metaObject);
                this.setFieldValByName(UPDATE_USER, userDetailsVO.getUsername(), metaObject);
            }else {
                this.setFieldValByName(CREATE_USER, "unknown", metaObject);
                this.setFieldValByName(UPDATE_USER, "unknown", metaObject);
            }
        }

    }

    /**
     * 更新时的填充策略
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        if (metaObject.hasSetter(UPDATE_DATE)) {
            //实现填充业务逻辑
            this.setFieldValByName(UPDATE_DATE, LocalDateTime.now(), metaObject);
        }
        if (metaObject.hasSetter(UPDATE_USER)){
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if(authentication.getPrincipal()!="anonymousUser"){
                UserDetailsVO userDetailsVO = (UserDetailsVO)authentication.getPrincipal();
                this.setFieldValByName(UPDATE_USER, userDetailsVO.getUsername(), metaObject);
            }else {
                this.setFieldValByName(UPDATE_USER, "unknown", metaObject);
            }
        }
    }
}
