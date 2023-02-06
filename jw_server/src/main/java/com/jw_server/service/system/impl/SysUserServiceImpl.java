package com.jw_server.service.system.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jw_server.core.constants.HttpCode;
import com.jw_server.core.common.MyPageVO;
import com.jw_server.core.common.ResponseResult;
import com.jw_server.core.exception.ServiceException;
import com.jw_server.core.utils.JwtUtils;
import com.jw_server.core.utils.RedisUtils;
import com.jw_server.dao.system.dto.LoginSysUserDTO;
import com.jw_server.dao.system.dto.QuerySysUserDTO;
import com.jw_server.dao.system.dto.ResetPasswordDTO;
import com.jw_server.dao.system.entity.SysRole;
import com.jw_server.dao.system.entity.SysUser;
import com.jw_server.dao.system.mapper.SysRoleMapper;
import com.jw_server.dao.system.mapper.SysUserMapper;
import com.jw_server.dao.system.vo.LoginUserVO;
import com.jw_server.dao.system.vo.SysUserVO;
import com.jw_server.dao.system.vo.UserDetailsVO;
import com.jw_server.service.system.ISysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;


/**
 * Description 系统用户表 服务实现类
 * Author jingwen
 * Date 2022-08-29 16:21:58
 **/
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private RedisUtils redisUtils;

    @Resource
    private SysRoleMapper sysRoleMapper;

    @Resource
    private SysUserMapper sysUserMapper;

    //jwt过期时间
    @Value("${jwt.data.jwtTtl}")
    private String tokenTime;

    private static final Log logger = LogFactory.getLog(SysUserServiceImpl.class);


    /**
     * Description: 用户登录方法，校验验证码功能在LoginController中完成，所以该方法只是认证username和password
     * Author: jingwen
     * Date: 2022/8/30 9:39
     **/
    @Override
    public ResponseResult userLogin(LoginSysUserDTO loginSysUserDTO) {
        //AuthenticationManager的authenticate方法来进行用户认证, authenticate方法会调用UserDetailsService的loadUserByUsername方法来认证
        UsernamePasswordAuthenticationToken authenticationToken = new
                UsernamePasswordAuthenticationToken(loginSysUserDTO.getUsername(), loginSysUserDTO.getPassword());
        Authentication authenticateResult;
        try {
            authenticateResult = authenticationManager.authenticate(authenticationToken);
        } catch (Exception e) {
            logger.error(e);
            if(e.getMessage().equals("用户名不存在"))
                throw new ServiceException(HttpCode.CODE_403,"用户名不存在");
            else if(e.getMessage().equals("用户已被停用"))
                throw new ServiceException(HttpCode.CODE_403,"用户已被停用");
            throw new ServiceException(HttpCode.CODE_403,"密码错误");
        }
        if (Objects.isNull(authenticateResult)){
            throw new ServiceException(HttpCode.CODE_500,"登录失败");
        }
        //如果认证通过，使用userId生成一个jwt
        UserDetailsVO userDetailsVO = (UserDetailsVO) authenticateResult.getPrincipal();
        String userId = userDetailsVO.getId().toString();
        String token = JwtUtils.createJWT(userId);
        userDetailsVO.setToken(token);

        String subToken = StringUtils.substringAfterLast(token, ".");

        /*
        * 不建议直接返回userDetailsVO，因为里面有一些属性前端不需要
         */
        LoginUserVO loginUserVO = copyBeanFromUserDetailsVO(userDetailsVO);

        //更新登录时间
        updateUserLastLoginTime(loginUserVO.getId());
        //把完整的用户信息存入redis, token的第三段字符串为key
        //登录用户redis中的过期时间和jwt的过期时间一样
        redisUtils.setCacheObject("login-tokens:"+subToken, userDetailsVO, Integer.valueOf(tokenTime), TimeUnit.MILLISECONDS);
        return ResponseResult.success(loginUserVO);
    }

    @Override
    public ResponseResult userLogout(String token) {
        //删除redis中的缓存信息
        String subToken = StringUtils.substringAfterLast(token, ".");
        redisUtils.deleteObject("login-tokens:"+subToken);
        return ResponseResult.success();
    }

    @Override
    public ResponseResult register(SysUser sysUser) {
        String username = sysUser.getUsername();
        String password = sysUser.getPassword();
        if(StrUtil.isEmpty(username) || StrUtil.isEmpty(password)){
            throw new ServiceException(HttpCode.CODE_400,"用户名或密码为空");
        }

        // 检查是否有同名用户, 只需要随便查找一个属性（主键）即可
        SysUser findSameUserNameUser = getUserByUserName(username);

        if (ObjectUtil.isNotEmpty(findSameUserNameUser)){
            throw new ServiceException(HttpCode.CODE_600,"用户名已存在, 请修改用户名");
        }
        //对密码进行加密
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        sysUser.setPassword(bCryptPasswordEncoder.encode(sysUser.getPassword()));
        save(sysUser);
        return ResponseResult.success();
    }

    /**
     * 检查是否有同名用户, 只需要随便查找一个属性（主键）即可
     **/
    @Override
    public SysUser getUserByUserName(String username){
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<SysUser>()
                .select(SysUser::getId)
                .eq(SysUser::getUsername, username);
        SysUser sysUser;
        try {
            sysUser = getOne(queryWrapper);
        }catch (Exception e) {
            // 数据库人为添加了坏数据, 出现多个同名用户
            throw new ServiceException(HttpCode.CODE_500, "系统错误");
        }
        return sysUser;
    }

    @Override
    public MyPageVO<SysUserVO> getUserPageList(QuerySysUserDTO querySysUserDTO) {
        //查询条件
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        if(StrUtil.isNotEmpty(querySysUserDTO.getUsername())){
            queryWrapper.like(SysUser::getUsername, querySysUserDTO.getUsername());
        }
        if(StrUtil.isNotEmpty(querySysUserDTO.getNickname())){
            queryWrapper.like(SysUser::getNickname, querySysUserDTO.getNickname());
        }
        IPage<SysUser> userIPage = page(new Page<>(querySysUserDTO.getPageNum(), querySysUserDTO.getPageSize()), queryWrapper);

        //新建userVOList用来存储返回的用户
        List<SysUserVO> userVOList = new ArrayList<>();
        //封装角色信息
        List<SysUser> userList = userIPage.getRecords();
        userList.forEach(user -> {
            SysUserVO userVO = new SysUserVO();
            BeanUtil.copyProperties(user, userVO);
            List<SysRole> roleList = sysRoleMapper.selectAllRoleByUserId(user.getId());
            if (roleList.size() > 0)
                userVO.setRoleList(roleList);
            userVOList.add(userVO);
        });


        return new MyPageVO<>(userIPage.getPages(),
                userIPage.getCurrent(),
                userIPage.getSize(),
                userIPage.getTotal(),
                userVOList);
    }

    @Override
    public LoginUserVO getCurrentLoginUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsVO userDetailsVO = (UserDetailsVO) authentication.getPrincipal();
        LoginUserVO loginUserVO = copyBeanFromUserDetailsVO(userDetailsVO);
        return loginUserVO;
    }

    @Override
    public void resetPassword(ResetPasswordDTO resetPasswordDTO) {
        String findPassword = sysUserMapper.selectPasswordByUserId(resetPasswordDTO.getUserId());
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        if (!bCryptPasswordEncoder.matches(resetPasswordDTO.getPassword(), findPassword)) {
            throw new ServiceException(HttpCode.CODE_400, "用户原密码输入错误");
        }
        String encodePassword = bCryptPasswordEncoder.encode(resetPasswordDTO.getNewPassword());
        sysUserMapper.updatePasswordByUserId(resetPasswordDTO.getUserId(), encodePassword);
    }

    /**
     * Description: 将userDetailsVO信息复制到LoginUserVO
     * Author: jingwen
     * Date: 2022/9/2 15:05
     **/
    public LoginUserVO copyBeanFromUserDetailsVO(UserDetailsVO userDetailsVO) {
        LoginUserVO loginUserVO = LoginUserVO.builder()
                .id(userDetailsVO.getId())
                .username(userDetailsVO.getUsername())
                .nickname(userDetailsVO.getNickname())
                .avatar(userDetailsVO.getAvatar())
                .roleList(userDetailsVO.getRoleList())
                .token(userDetailsVO.getToken())
                .permissionList(userDetailsVO.getPermissionList())
                .email(userDetailsVO.getEmail())
                .phone(userDetailsVO.getPhone())
                .sex(userDetailsVO.getSex())
                .remark(userDetailsVO.getRemark())
                .build();
        return loginUserVO;
    }

    /**
     * Description: 更新用户最近登录时间
     * Author: jingwen
     * Date: 2022/9/10 18:05
     **/
    public void updateUserLastLoginTime(Integer userId){
        LambdaUpdateWrapper<SysUser> updateWrapper =new LambdaUpdateWrapper<>();
        updateWrapper.eq(SysUser::getId, userId);
        updateWrapper.set(SysUser::getLastLoginTime, LocalDateTime.now());
        update(updateWrapper);
    }

}
