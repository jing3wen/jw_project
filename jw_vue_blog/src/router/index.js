import {createRouter, createWebHistory} from 'vue-router'

const routes = [
  {
    path: '/',
    name: '首页',
    component: () => import('../layout/Index'),
    children:[
      {
        path: '/article',
        name: '文章',
        component: () => import('../views/article/Article')
      },{
        // 搜索
        path: "/searchPage",
        name: '搜索结果',
        component: () => import('../views/searchPage/SearchPage')
      }
    ]
  },

]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
