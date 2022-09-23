//权限按钮显示
import {readForestBFS} from "@/assets/js/readTree";
import store from '../store'

function checkPermissions(el, binding, v_node) {
    // //获取用户菜单树
    // const menuAndDirectoryList = v_node.context.$store.state.user.currentLoginUser.menuVOList
    // if(menuAndDirectoryList.length<=0){
    //     el.parentNode && el.parentNode.removeChild(el)
    //     return
    // }
    //获取菜单树的按钮
    //store.state.user.currentLoginUser.permissionList
    const userPermissionList = store.state.user.currentLoginUser.permissionList
    // console.log(userPermissionList)
    //获取要检查的权限
    const checkPermissions = binding.value
    if (checkPermissions && checkPermissions instanceof Array && checkPermissions.length > 0){

        if (checkPermissions.findIndex(code => userPermissionList.findIndex(p => p === code) > -1) === -1) {
            el.parentNode && el.parentNode.removeChild(el)
        }
    }else {
        throw new Error(`v-permission的使用方式如下: v-permission="['system:sysUser:add','system:sysUser:update']"`);
    }
}

export default {
    inserted :function (el, binding, v_node){
        checkPermissions(el, binding, v_node)
    }
}


