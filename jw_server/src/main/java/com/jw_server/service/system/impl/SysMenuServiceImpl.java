package com.jw_server.service.system.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jw_server.core.constants.HttpCode;
import com.jw_server.core.exception.ServiceException;
import com.jw_server.dao.system.dto.QuerySysMenuDTO;
import com.jw_server.dao.system.entity.SysMenu;
import com.jw_server.dao.system.mapper.SysMenuMapper;
import com.jw_server.dao.system.mapper.SysRoleMenuMapper;
import com.jw_server.dao.system.mapper.SysUserRoleMapper;
import com.jw_server.dao.system.vo.SysMenuVO;
import com.jw_server.service.system.ISysMenuService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Description 菜单表 服务实现类
 * Author jingwen
 * Date 2022-08-30 18:58:10
 **/
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements ISysMenuService {

    @Resource
    private SysUserRoleMapper sysUserRoleMapper;
    @Resource
    private SysRoleMenuMapper sysRoleMenuMapper;


    public List<SysMenuVO> getMenuPageList(QuerySysMenuDTO querySysMenuDTO) {
        LambdaQueryWrapper<SysMenu> queryWrapper = new LambdaQueryWrapper<>();
        if(StrUtil.isNotEmpty(querySysMenuDTO.getMenuName())){
            queryWrapper.like(SysMenu::getMenuName, querySysMenuDTO.getMenuName());
        }
        //查询所有数据并将数据复制到VO中
        List<SysMenu> menuList = list(queryWrapper);
        List<SysMenuVO> menuVOList = new ArrayList<>();
        for (SysMenu menu: menuList){
            SysMenuVO menuVO = new SysMenuVO();
            BeanUtil.copyProperties(menu, menuVO);
            menuVOList.add(menuVO);
        }

        //找出pid为null的一级菜单, 使用lambda表达式过滤出结果,java8以上
        /**
         * 或者采用博客如下方法：获取根节点也能得到父节点
         * https://www.cnblogs.com/lucky-pin/p/10740037.html
         **/
        return buildTree(menuVOList);
    }
    /**
     * @Description
     * 采用广度优先搜索来构建菜单树，
     * 为什么不采用深度优先遍历
     *  答： 因为我想对已遍历的节点不重复遍历，这时有两种方案：
     *      ①需要一个变量来记录已经遍历的节点，这个变量为全局变量
     *      ②将遍历的数组设置为全局变量，遍历一个节点就删除一个节点。
     *      上述两种方案都需要全局变量
     *
     *      *******有个更重要的是广度搜出来时可以逐层排序******
     * @Author jingwen
     * @Date 2022/8/22 14:48
     **/
    @Override
    public List<SysMenuVO> sysMenuList() {
        //查询所有数据并将数据复制到VO中
        List<SysMenu> menuList = list();
        List<SysMenuVO> menuVOList = new ArrayList<>();
        for (SysMenu menu: menuList){
            SysMenuVO menuVO = new SysMenuVO();
            BeanUtil.copyProperties(menu, menuVO);
            menuVOList.add(menuVO);
        }

        //找出pid为null的一级菜单, 使用lambda表达式过滤出结果,java8以上
        /**
         * 或者采用博客如下方法：获取根节点也能得到父节点
         * https://www.cnblogs.com/lucky-pin/p/10740037.html
         **/
        return buildTree(menuVOList);
    }

    /**
     * 根据用户id查询所有菜单(目录，菜单，按钮)，根据参数是否判断需要构建成树结构
     * @param userId
     * @param buildTree
     * @return
     */
    @Override
    public List<SysMenuVO> selectMenusByUserId(Integer userId, Boolean buildTree) {
        //查询该用户的所有角色ids
        List<Integer> roleIds = sysUserRoleMapper.selectRoleIdByUserId(userId);
        //根据所有角色ids查询关联的菜单id
        List<Integer> menuIds = new ArrayList<>();
        for(Integer roleId : roleIds){
             List<Integer> ids= sysRoleMenuMapper.selectMenuByRoleId(roleId);
             ids.forEach(mid->{
                 menuIds.add(mid);
             });
        }
        //去除关联的菜单id列表中重复数据
        List<Integer> distinctMenuIds = menuIds.stream().distinct().collect(Collectors.toList());

        //查询菜单表中所有未删除的数据
        LambdaQueryWrapper<SysMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysMenu::getIsDeleted,"0");
        List<SysMenu> menuList = list(queryWrapper);
        List<SysMenuVO> menuVOList = new ArrayList<>();

        for (SysMenu menu: menuList){
            if(distinctMenuIds.contains(menu.getId())){
                SysMenuVO menuVO = new SysMenuVO();
                BeanUtil.copyProperties(menu, menuVO);
                menuVOList.add(menuVO);
            }
        }
        if(buildTree)
            return buildTree(menuVOList);
        else
            return menuVOList;
    }



    /**
     * @Description 根据输入的所有菜单数据来构建菜单树
     * @Author jingwen
     * @Date 2022/8/23 14:02
     **/
    public List<SysMenuVO> buildTree(List<SysMenuVO> menuVOList){
        //按menuSort排序
        menuVOList = menuVOList.stream()
                .sorted(Comparator.comparingInt(menu -> (menu.getMenuSort() == null ? 0 : menu.getMenuSort())))
                .collect(Collectors.toList());

        List<SysMenuVO> rootNodelist = menuVOList.stream()
                .filter(menuVO -> menuVO.getParentId()==0)
                .collect(Collectors.toList());
        //删除总菜单列表里面的一级菜单 => 删除已经遍历的节点, 减少后续遍历节点
        menuVOList.removeIf(menuVO -> menuVO.getParentId()==0);

        //通过递归函数遍历子菜单

        //找出一级菜单的子菜单
        for (SysMenuVO root: rootNodelist){
            root.setChildren(new ArrayList<>());  // 不加这条root.getChildren()=null会直接报错
            buildChildTree(root, menuVOList);
        }

        //对根节点按menuSort排序
//        rootNodelist = rootNodelist.stream()
//                .sorted(Comparator.comparingInt(menu -> (menu.getMenuSort() == null ? 0 : menu.getMenuSort())))
//                .collect(Collectors.toList());
        return rootNodelist;
    }

    /**
     * Description 遍历子菜单的递归函数,
     * Author jingwen
     * Date 2022/8/22 14:27
     **/
    public void buildChildTree(SysMenuVO parent, List<SysMenuVO> menuVOList){
        if(menuVOList.size()==0) {
            return;
        }
        // childIds记录当前parent的子节点
        List<Integer> childIds = new ArrayList<>();
        // 开始遍历当前parent的子节点
        for (SysMenuVO child : menuVOList){
            if(parent.getId().equals(child.getParentId())){
                child.setChildren(new ArrayList<>());
                parent.getChildren().add(child);
                childIds.add(child.getId());
            }
        }
        //删除当前parent的子节点 => 删除已经遍历的节点, 减少后续遍历节点
        menuVOList.removeIf(menuVO -> childIds.contains(menuVO.getId()));
        // 若当前parent有子节点, 继续遍历子节点
        if(parent.getChildren().size()>0){
            for(SysMenuVO child : parent.getChildren()){
                buildChildTree(child, menuVOList);
            }
        }
    }

    @Override
    public void deleteMenu(List<Integer> menuIds) {

        //删除时要注意该菜单是否有子选项
        menuIds.forEach(menuId -> {
            LambdaQueryWrapper<SysMenu> queryWrapper = new LambdaQueryWrapper<>();
            //不用查询所有数据，取个id就行了，节省时间
            queryWrapper.select(SysMenu::getId);
            queryWrapper.eq(SysMenu::getParentId, menuId);
            if (!list(queryWrapper).isEmpty()) {
                throw new ServiceException(HttpCode.CODE_400, "要删除的菜单有子菜单, 请先删除子菜单");
            }
        });
        //删除菜单时绑定的角色信息也要删除
        menuIds.forEach(menuId -> {
            sysRoleMenuMapper.deleteRoleMenuByMenuId(menuId);
        });
        //删除菜单
        removeBatchByIds(menuIds);
    }

}
