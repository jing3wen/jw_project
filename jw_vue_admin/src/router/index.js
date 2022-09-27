import Vue from 'vue'
import VueRouter from 'vue-router'
import store from '../store'
import Element from 'element-ui'
import {readForestBFS} from "@/assets/js/readTree";
import NProgress from '@/utils/NProgress'

Vue.use(VueRouter)
const originalPush = VueRouter.prototype.push

VueRouter.prototype.push = function push(location) {
    return originalPush.call(this, location).catch(err => err)
}

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login')
  },
  {
    path: '/404',
    name: '404',
    component: () => import('@/views/404')
  },

]

const router = new VueRouter({
  mode: 'history',
  base: process.env.NODE_ENV === 'production' ? 'jw_vue_admin/':'/',
  routes
})

export const setRoute = () =>{

    //返回的数据为列表
    const userMenus = readForestBFS(store.state.user.currentLoginUser.menuAndDirectoryList, 'menu')
    if(userMenus.length > 0){
        // 获取当前的路由对象名称数组
        const currentRouteNames = router.getRoutes().map(v => v.name)
        //防止重复添加路由信息
        if(!currentRouteNames.includes('框架')){
            const frameRoute = {path: '/', name: '框架', component: () => import('@/layout/Index'), redirect: '/index', children:[]}

            frameRoute.children.push({path:'system/sysPerson', name:'个人信息', component:() => import('@/views/system/sysPerson')})
            if(userMenus.length > 0){
                userMenus.forEach(item =>{
                    let itemMenu = {path: item.path.substr(1), name: item.menuName, component:() => import('@/views'+item.component)}  //substr(1)去掉路由地址path第一个字符  '/'
                    frameRoute.children.push(itemMenu)
                })
            }
            // 动态添加到现在的路由对象中去
            router.addRoute(frameRoute)
        }
    }
}
setRoute()


// 路由守卫
router.beforeEach((to, from, next) => {
    NProgress.start()
    // 获取当前的路由对象名称数组
    const currentRouteNames = router.getRoutes().map(v => v.name)
    if(to.path == '/login'){
        if(store.state.user.currentLoginUser.token){
            Element.Message.info('该用户已登录')
            next('/index')
        }
        next()
    }else if(to.path=='/404'){
        next()
    } else if (!store.state.user.currentLoginUser.id){    //验证当前用户是否登录
        Element.Message.warning('请登录')
        next('/login')
    }else if(!currentRouteNames.includes(to.name)){
        next('/404')
    }else{
        store.commit("layout/setPath", to.name)  // 触发store的数据更新
        next()  // 放行路由
    }
})

router.afterEach((to, from)=>{
    NProgress.done()
})

export default router
