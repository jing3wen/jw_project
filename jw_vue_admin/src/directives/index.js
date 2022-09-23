import v_permission from './v-permission'

//注册指令
export default{
    install(Vue){
        Vue.directive('permission', v_permission)
    }
}