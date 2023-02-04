import {createRouter, createWebHistory} from 'vue-router'

const routes = [
  {
    path: '/',
    redirect:'/home',
    name: 'index',
    component: () => import('../layout/Index'),
    children:[
      {
        path: '/home',
        name: '首页',
        component: () => import('../views/home/Home')
      },
      {
        path: '/article',
        name: '文章',
        component: () => import('../views/article/ArticleMainLayout')
      },{
        // 搜索
        path: "/searchPage",
        name: '搜索结果',
        component: () => import('../views/searchPage/SearchPageMainLayout')
      },{
        // 搜索
        path: "/classify/:id",
        name: '类别',
        component: () => import('../views/classifyPage/ClassifyMainLayout')
      },{
        // 文章详情
        path: "/articleDetails/:id",
        name: '文章详情',
        component: () => import('../views/articleDetails/ArticleDetails')
      },{
        // 关于我
        path: "/about",
        name: '关于我',
        component: () => import('../views/about/About')
      },{
        // 关于我
        path: "/friendLink",
        name: '友链',
        component: () => import('../views/friendLink/FriendLink')
      },
    ]
  },

]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
