<template>
  <div class="hidden-xs-only header-menu">
<!--    :default-active="route.path" @select="handleSect"  -->
    <el-menu  :router="true" :ellipsis="false" class="el-menu-demo"
             mode="horizontal" active-text-color='#2FA7B9'>
      <img src="../../assets/image/logo.svg" style="cursor: pointer;width:140px;" alt="Bpvank" @click="router.push({path:'/'})">
      <div class="flex-grow" />
      <el-menu-item index="/">
        <span>首页</span>
      </el-menu-item>
      <el-menu-item index="/article">
        <span>精品文章</span>
      </el-menu-item>
      <el-menu-item index="/website">
        <span>学习网站</span>
      </el-menu-item>
      <!-- <el-menu-item index="/message">
          <span>留言</span>
      </el-menu-item> -->
      <el-menu-item index="/about">
        <span>关于</span>
      </el-menu-item>
<!--      <a href="/login" target="_blank" class="loginA" v-show="!userInfo">-->
<!--        登录/注册-->
<!--      </a>-->
<!--      <el-dropdown v-show="userInfo" style="cursor: pointer;">-->
<!--        <div class="el-dropdown-link" style="margin: auto;">-->
<!--          <img v-if="userInfo.userIcon !== null && userInfo.userIcon !== ''" :src="url+userInfo.userIcon"-->
<!--               style="width: 30px;margin: 0px 10px;vertical-align:middle;border-radius: 50px;" />-->
<!--          <img v-if="userInfo.userIcon === null || userInfo.userIcon === ''" src="../../../assets/default_avatar.svg"-->
<!--               style="width: 30px;margin: 0px 10px;vertical-align:middle;border-radius: 50px;">-->
<!--          <span>{{userInfo.userName}}</span>-->

<!--          <el-icon class="el-icon&#45;&#45;right">-->
<!--            <arrow-down />-->
<!--          </el-icon>-->
<!--        </div>-->
<!--        <template #dropdown>-->
<!--          <el-dropdown-menu>-->
<!--            <el-dropdown-item @click="toPath('/addArticle')">新建文章</el-dropdown-item>-->
<!--            <el-dropdown-item @click="toPath('/articleManager')">文章管理</el-dropdown-item>-->
<!--            <el-dropdown-item @click="toPath('/commentManager')">评论管理</el-dropdown-item>-->
<!--            <el-dropdown-item @click="toPath('/personSetup')">个人设置</el-dropdown-item>-->
<!--            <el-dropdown-item divided @click="exit">退出登录</el-dropdown-item>-->
<!--          </el-dropdown-menu>-->
<!--        </template>-->
<!--      </el-dropdown>-->
    </el-menu>
  </div>

  <div class="hidden-sm-and-up header-img">
    <img src="../../assets/image/logo.svg" style="cursor: pointer;width:140px;margin: 0 auto;" alt="Bpvank" @click="router.push({path:'/'})">
  </div>
</template>

<script>
export default {
  name: "Header",
  data() {
    return {

    }
  },
  methods: {

  }
}
</script>

<style scoped>
/* 顶部菜单栏 */
.flex-grow {
  flex-grow: 1;
}

.el-menu--horizontal {
  border-bottom: none;
}

.el-menu--horizontal .el-menu-item:not(.is-disabled):focus,
.el-menu--horizontal .el-menu-item:not(.is-disabled):hover {
  color: #2FA7B9;
  background-color: rgba(47, 167, 185, 0.1);
}

.header-img {
  display: flex;
  align-items: center;
  text-align: center;
  line-height: 90px;
}

.el-menu--horizontal>.el-menu-item a {
  color: #303133;
}

.el-menu--horizontal>.el-menu-item a:hover {
  color: #2FA7B9;
}

.loginA {
  line-height: 56px;
  font-size: 14px;
  margin-left: 10px;
}

.example-showcase .el-dropdown-link {
  cursor: pointer;
  color: var(--el-color-primary);
  display: flex;
  align-items: center;
}

:deep(.el-dropdown-menu__item:not(.is-disabled):focus) {
  background-color: rgba(47, 167, 185, 0.1);
  color: #2fa7b9;
}

</style>
