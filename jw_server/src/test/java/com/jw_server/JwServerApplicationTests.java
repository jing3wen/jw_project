package com.jw_server;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jw_server.core.utils.JwtUtils;
import com.jw_server.dao.system.entity.SysUser;
import com.jw_server.dao.system.mapper.SysMenuMapper;
import com.jw_server.dao.system.mapper.SysRoleMapper;
import com.jw_server.dao.system.mapper.SysUserMapper;
import com.jw_server.dao.system.vo.SysMenuVO;
import com.jw_server.dao.system.vo.SysUserVO;
import com.jw_server.service.system.ISysMenuService;
import com.jw_server.service.system.ISysUserService;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SpringBootTest
class JwServerApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    public void testBCryptPasswordEncoder(){
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        //加密
        String encode1 = bCryptPasswordEncoder.encode("123");

        String encode2 = bCryptPasswordEncoder.encode(encode1);
        System.out.println(encode1+", "+encode2);
//        bCryptPasswordEncoder.matches()

        System.out.println(bCryptPasswordEncoder.matches("123",
                encode1));
        System.out.println(bCryptPasswordEncoder.matches("123",
                encode2));
        System.out.println(bCryptPasswordEncoder.matches(encode1,
                encode2));
    }

    @Test
    public void testJWT() throws Exception{
//        String token1 = "eyJhbGciOiJIUzI1NiJ9." +
//                "eyJqdGkiOiJmMzBhZTY5MzgyMzg0Y2FiYjM5N2RmOTNkYzg4YTFmZiIsInN1YiI6IjEiLCJpc3MiOiJqdyIsImlhdCI6MTY2MTg0OTIyNCwiZXhwIjoxNjYxODUyODI0fQ." +
//                "b46s9kspGB968Va3IgNesRcqrDmxVSNyU09uNUyxkSE";
//        String token2 = "eyJhbGciOiJIUzI1NiJ9." +
//                "eyJqdGkiOiI3YTVhMTUyMmMyNTE0NzJmODJlNzk2MDI4MGU3MTRiNiIsInN1YiI6IjEiLCJpc3MiOiJqdyIsImlhdCI6MTY2MTg0ODgwNCwiZXhwIjoxNjYxODUyNDA0fQ." +
//                "dKeG53XnAi4HGslZ4zNQhn5hRp_JBNvXmMTQcIK6pJI";
//        List<String> tokens = new ArrayList<>();
//        tokens.add(token1);
//        tokens.add(token2);
//        for (String token:tokens){
//            Claims claims = JwtUtils.parseJWT(token);
//            System.out.println(claims);
//        }

        for (Integer i=0;i<5;i++){
            String jwt = JwtUtils.createJWT("1");
            System.out.println(jwt);
            String keySub = StringUtils.substringAfterLast(jwt, ".");
            System.out.println(keySub);
        }
    }


    @Resource
    private SysMenuMapper sysMenuMapper;
    @Test
    public void testselectPermissionsByUserId(){
        List<String> permissions = sysMenuMapper.selectPermissionsByUserId(1);
        System.out.println(permissions);
    }


    @Resource
    private ISysMenuService sysMenuService;
    @Test
    public void testSelectMenusByUserId(){
        List<SysMenuVO> menuVOList = sysMenuService.selectMenusByUserId(1, false);
        System.out.println(menuVOList);
        List<SysMenuVO> menuVOTree = sysMenuService.buildTree(menuVOList);
        System.out.println(menuVOTree);
    }


    @Resource
    private ISysUserService sysUserService;
    @Test
    public void testPageList(){
        IPage<SysUser> mapIPage = sysUserService.page(new Page<>(0,10));
        System.out.println(mapIPage.getPages());
        System.out.println(mapIPage.getCurrent());
        System.out.println(mapIPage.getSize());
        System.out.println(mapIPage.getTotal());
        System.out.println(mapIPage.getRecords());

    }


    @Resource
    private SysRoleMapper sysRoleMapper;
    @Test
    public void testSelectAllRoleNameByUserId(){
        System.out.println(sysRoleMapper.selectAllRoleByUserId(1));
    }




}
