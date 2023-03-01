package com.jw_server.controller.system;


import com.jw_server.core.common.ResponseResult;
import com.jw_server.core.fileUpload.FileUploadUtils;
import com.jw_server.core.aop.logAspect.SysLog;
import com.jw_server.dao.blog.dto.BlogFrontForgetPasswordOrUpdateBindDTO;
import com.jw_server.dao.system.dto.QuerySysUserDTO;
import com.jw_server.dao.system.dto.RegisterUserDTO;
import com.jw_server.dao.system.dto.ResetPasswordDTO;
import com.jw_server.dao.system.dto.UserRoleDTO;
import com.jw_server.dao.system.entity.SysUser;
import com.jw_server.service.system.ISysRoleService;
import com.jw_server.service.system.ISysUserRoleService;
import com.jw_server.service.system.ISysUserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

import static com.jw_server.core.constants.LogModuleConst.SysUserModule;
import static com.jw_server.core.constants.LogTypeConst.*;
import static com.jw_server.core.enums.FilePathEnum.AVATAR;


/**
 * author jingwen
 * Description 系统用户表 前端控制器
 * Date 2022-08-29 16:21:58
 */
@RestController
@RequestMapping("/sysUser")
public class SysUserController {

    // 初始化密码
    @Value("${password.initPassword}")
    private String initPassword;

    @Resource
    private ISysUserService sysUserService;

    @Resource
    private ISysRoleService sysRoleService;

    @Resource
    private ISysUserRoleService sysUserRoleService;

    @Resource
    private FileUploadUtils fileUploadUtils;

    /**
     * Description 新增
     * Author jingwen
     * Date 2022-08-29 16:21:58
     **/
    @SysLog(logModule= SysUserModule, logType = ADD, logDesc = "新增用户")
    @PreAuthorize("hasAuthority('system:sysUser:add')")
    @PostMapping("/add")
    public ResponseResult add(@RequestBody RegisterUserDTO registerUserDTO) {
        //添加用户时，对密码进行加密
        if (registerUserDTO.getPassword()==null || registerUserDTO.getPassword().equals("")){
            registerUserDTO.setPassword(initPassword);
        }
        sysUserService.register(registerUserDTO);
        return ResponseResult.success();
    }


    /**
     * Description 更新
     * Author jingwen
     * Date 2022-08-29 16:21:58
     **/
    @SysLog(logModule=SysUserModule, logType = UPDATE, logDesc = "编辑用户")
//    @PreAuthorize("hasAuthority('system:sysUser:update')")  //防止注册新用户不能更新个人信息，或者改为为注册用户默认分配一个带权限的角色
    @PostMapping("/update")
    public ResponseResult update(@RequestBody SysUser sysUser) {
        sysUserService.updateById(sysUser);
        return ResponseResult.success();
    }

    /**
     * Description 批量删除
     * Author jingwen
     * Date 2022-08-29 16:21:58
     **/
    @SysLog(logModule=SysUserModule, logType = DELETE, logDesc = "删除用户")
    @PreAuthorize("hasAuthority('system:sysUser:delete')")
    @PostMapping("/deleteBatch")
    public ResponseResult deleteBatch(@RequestBody List<Integer> ids) {
        sysUserService.deleteUserBatch(ids);
        return ResponseResult.success();
    }

    /**
     * Description 根据id查询数据
     * Author jingwen
     * Date 2022-08-29 16:21:58
     **/
    @GetMapping("/findOne")
    public ResponseResult findOne(@RequestParam Integer id) {

        return ResponseResult.success(sysUserService.getById(id));
    }

    /**
     * Description 分页查询
     * Author jingwen
     * Date 2022-08-29 16:21:58
     **/
    @PreAuthorize("hasAuthority('system:sysUser:query')")
    @PostMapping("/getPageList")
    public ResponseResult getPageList(@RequestBody QuerySysUserDTO querySysUserDTO) {
        return ResponseResult.success(sysUserService.getUserPageList(querySysUserDTO));
    }

    /**
     * Description 注册
     * Author jingwen
     * Date 2022/8/20 10:41
     **/
    @SysLog(logModule=SysUserModule, logType = ADD, logDesc = "用户注册")
    @PostMapping("/register")
    public ResponseResult register(@RequestBody RegisterUserDTO sysUser){

        return sysUserService.register(sysUser);
    }


    /**
     * Description: 根据用户id查询该用户所有角色
     * Author: jingwen
     * Date: 2022/9/5 22:54
     **/
    @PreAuthorize("hasAuthority('system:sysUser:editRole')")
    @GetMapping("/findRoleByUserId")
    public ResponseResult findRoleByUserId(@RequestBody SysUser sysUser){

        return ResponseResult.success(sysRoleService.selectAllRoleByUserId(sysUser.getId()));
    }


    /**
     * Description: 编辑用户角色信息
     * Author: jingwen
     * Date: 2022/9/6 8:50
     **/
    @SysLog(logModule=SysUserModule, logType = UPDATE, logDesc = "更新用户角色信息")
    @PreAuthorize("hasAuthority('system:sysUser:editRole')")
    @PostMapping("/updateUserRole")
    public ResponseResult updateUserRole(@RequestBody UserRoleDTO userRoleDTO){

        sysUserRoleService.updateUserRole(userRoleDTO.getUserId(), userRoleDTO.getRoleIds());
        return ResponseResult.success();
    }

    /**
     * Description: 用户头像上传
     * Author: jingwen
     * Date: 2022/9/10 16:05
     **/
    @SysLog(logModule=SysUserModule, logType = UPLOAD, logDesc = "用户头像上传")
    @PostMapping("/uploadAvatar")
    public ResponseResult uploadAvatar(@RequestParam MultipartFile file){

        return ResponseResult.success(fileUploadUtils.fileUpload(file, AVATAR.getPath()));
    }

    /**
     * Description: 重置用户密码
     * Author: jingwen
     * Date: 2022/9/10 20:17
     **/
    @SysLog(logModule=SysUserModule, logType = UPDATE, logDesc = "重置用户密码")
    @PostMapping("/resetPassword")
    public ResponseResult resetPassword(@RequestBody ResetPasswordDTO resetPasswordDTO){
        sysUserService.resetPassword(resetPasswordDTO);
        return ResponseResult.success();
    }


    /**
     * Description: 获取验证码
     *
     * type表示获取验证码的作用：注册用户/忘记密码/更改绑定绑定信息
     *
     * Author: jingwen
     * Date: 2023/2/25 19:42
     **/
    @GetMapping("/getCodeForType")
    public ResponseResult getCodeForType(@RequestParam("email") String email,
                                         @RequestParam("phone") String phone,
                                         @RequestParam("type") String type){
        sysUserService.getCodeForType(email, phone, type);
        return ResponseResult.success();
    }

    /**
     * Description: 找回密码
     * Author: jingwen
     * Date: 2023/2/25 23:26
     **/
    @PostMapping("/updateForgetPassword")
    public ResponseResult updateForgetPassword(@RequestBody BlogFrontForgetPasswordOrUpdateBindDTO forgetPasswordDTO){

        sysUserService.updateForgetPassword(forgetPasswordDTO);
        return ResponseResult.success();
    }

    /**
     * Description: 根据密码 绑定邮箱/手机号 或 更改绑定邮箱/手机号
     * Author: jingwen
     * Date: 2023/2/26 13:46
     **/
    @PostMapping("/updateBindByPassword")
    public ResponseResult updateBindByPassword(@RequestBody BlogFrontForgetPasswordOrUpdateBindDTO updateBindDTO){

        sysUserService.updateBindByPassword(updateBindDTO);
        return ResponseResult.success();
    }
}

