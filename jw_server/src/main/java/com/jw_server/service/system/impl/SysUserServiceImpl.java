package com.jw_server.service.system.impl;

import cn.hutool.core.bean.BeanUtil;
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
 * Description ??????????????? ???????????????
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

    //jwt????????????
    @Value("${jwt.data.jwtTtl}")
    private String tokenTime;

    private static final Log logger = LogFactory.getLog(SysUserServiceImpl.class);


    /**
     * Description: ?????????????????????????????????????????????LoginController???????????????????????????????????????username???password
     * Author: jingwen
     * Date: 2022/8/30 9:39
     **/
    @Override
    public ResponseResult userLogin(LoginSysUserDTO loginSysUserDTO) {
        //AuthenticationManager???authenticate???????????????????????????, authenticate???????????????UserDetailsService???loadUserByUsername???????????????
        UsernamePasswordAuthenticationToken authenticationToken = new
                UsernamePasswordAuthenticationToken(loginSysUserDTO.getUsername(), loginSysUserDTO.getPassword());
        Authentication authenticateResult;
        try {
            authenticateResult = authenticationManager.authenticate(authenticationToken);
        } catch (Exception e) {
            logger.error(e);
            if(e.getMessage().equals("??????????????????"))
                throw new ServiceException(HttpCode.CODE_403,"??????????????????");
            else if(e.getMessage().equals("??????????????????"))
                throw new ServiceException(HttpCode.CODE_403,"??????????????????");
            throw new ServiceException(HttpCode.CODE_403,"????????????");
        }
        if (Objects.isNull(authenticateResult)){
            throw new ServiceException(HttpCode.CODE_500,"????????????");
        }
        //???????????????????????????userId????????????jwt
        UserDetailsVO userDetailsVO = (UserDetailsVO) authenticateResult.getPrincipal();
        String userId = userDetailsVO.getId().toString();
        String token = JwtUtils.createJWT(userId);
        userDetailsVO.setToken(token);

        String subToken = StringUtils.substringAfterLast(token, ".");

        /*
        * ?????????????????????userDetailsVO?????????????????????????????????????????????
         */
        LoginUserVO loginUserVO = copyBeanFromUserDetailsVO(userDetailsVO);

        //??????????????????
        updateUserLastLoginTime(loginUserVO.getId());
        //??????????????????????????????redis, token????????????????????????key
        //????????????redis?????????????????????jwt?????????????????????
        redisUtils.setCacheObject("login-tokens:"+subToken, userDetailsVO, Integer.valueOf(tokenTime), TimeUnit.MILLISECONDS);
        return ResponseResult.success(loginUserVO);
    }

    @Override
    public ResponseResult userLogout(String token) {
        //??????redis??????????????????
        String subToken = StringUtils.substringAfterLast(token, ".");
        redisUtils.deleteObject("login-tokens:"+subToken);
        return ResponseResult.success();
    }

    @Override
    public ResponseResult register(SysUser sysUser) {
        String username = sysUser.getUsername();
        String password = sysUser.getPassword();
        if(StrUtil.isEmpty(username) || StrUtil.isEmpty(password)){
            throw new ServiceException(HttpCode.CODE_400,"????????????????????????");
        }

        // ???????????????????????????, ???????????????????????????????????????????????????
        SysUser findSameUserNameUser = getUserByUserName(username);

        if (findSameUserNameUser != null){
            throw new ServiceException(HttpCode.CODE_600,"??????????????????, ??????????????????");
        }
        //?????????????????????
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        sysUser.setPassword(bCryptPasswordEncoder.encode(sysUser.getPassword()));
        save(sysUser);
        return ResponseResult.success();
    }

    @Override
    public SysUser getUserByUserName(String username){
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getUsername, username);
        SysUser sysUser;
        try {
            sysUser = getOne(queryWrapper);
        }catch (Exception e) {
            // ?????????????????????????????????, ????????????????????????
            throw new ServiceException(HttpCode.CODE_500, "????????????");
        }
        return sysUser;
    }

    @Override
    public MyPageVO getUserPageList(QuerySysUserDTO querySysUserDTO) {
        //????????????
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        if(StrUtil.isNotEmpty(querySysUserDTO.getUsername())){
            queryWrapper.like(SysUser::getUsername, querySysUserDTO.getUsername());
        }
        if(StrUtil.isNotEmpty(querySysUserDTO.getNickname())){
            queryWrapper.like(SysUser::getNickname, querySysUserDTO.getNickname());
        }
        IPage<SysUser> userIPage = page(new Page<>(querySysUserDTO.getPageNum(), querySysUserDTO.getPageSize()), queryWrapper);

        //??????userVOList???????????????????????????
        List<SysUserVO> userVOList = new ArrayList<>();
        //??????????????????
        List<SysUser> userList = userIPage.getRecords();
        userList.forEach(user -> {
            SysUserVO userVO = new SysUserVO();
            BeanUtil.copyProperties(user, userVO);
            List<SysRole> roleList = sysRoleMapper.selectAllRoleByUserId(user.getId());
            if (roleList.size() > 0)
                userVO.setRoleList(roleList);
            userVOList.add(userVO);
        });


        MyPageVO<SysUserVO> myPageVO = new MyPageVO<SysUserVO>().setMyPageVO(userIPage.getPages(),
                userIPage.getCurrent(),
                userIPage.getSize(),
                userIPage.getTotal(),
                userVOList);
        return myPageVO;
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
            throw new ServiceException(HttpCode.CODE_400, "???????????????????????????");
        }
        String encodePassword = bCryptPasswordEncoder.encode(resetPasswordDTO.getNewPassword());
        sysUserMapper.updatePasswordByUserId(resetPasswordDTO.getUserId(), encodePassword);
    }

    /**
     * Description: ???userDetailsVO???????????????LoginUserVO
     * Author: jingwen
     * Date: 2022/9/2 15:05
     **/
    public LoginUserVO copyBeanFromUserDetailsVO(UserDetailsVO userDetailsVO) {
        LoginUserVO loginUserVO = LoginUserVO.builder()
                .id(userDetailsVO.getId())
                .username(userDetailsVO.getUsername())
                .nickname(userDetailsVO.getNickname())
                .avatarUrl(userDetailsVO.getAvatarUrl())
                .roleList(userDetailsVO.getRoleList())
                .token(userDetailsVO.getToken())
                .permissionList(userDetailsVO.getPermissionList())
                .build();
        return loginUserVO;
    }

    /**
     * Description: ??????????????????????????????
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
