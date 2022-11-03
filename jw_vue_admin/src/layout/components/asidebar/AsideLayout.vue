<template>
  <div class="side-layout">
    <el-menu :default-openeds="['1', '3']"
             background-color="#304156"
             text-color="#BFCBD9"
             active-text-color="#5a9cf8"
             :collapse="this.$store.state.layout.isCollapse"
             router
    >
      <div class="logo" >
        <img class="logo-img" src="@/assets/image/layout/logo2_400.png" alt="" >
        <b class="logo-b" v-show="logoTextShow">后台管理系统</b>
      </div>
<!--   一, 二级菜单渲染方式：通过循环完成
      <div v-for="item in menus" :key="item.id">
        <div v-if="item.path">
          <el-menu-item :index="item.path">
            <i :class="item.icon"></i>
            <span slot="title">{{ item.name }}</span>
          </el-menu-item>
        </div>
        <div v-else>
          <el-submenu :index="item.id + ''">
            <template slot="title">
              <i :class="item.icon"></i>
              <span slot="title">{{ item.name }}</span>
            </template>
            <div  v-for="subItem in item.children" :key="subItem.id">
              <el-menu-item :index="subItem.path">
                <i :class="subItem.icon"></i>
                <span slot="title">{{ subItem.name }}</span>
              </el-menu-item>
            </div>
          </el-submenu>
        </div>
      </div>
-->
      <!--   多级菜单渲染：递归   -->
      <MenuItems v-for="item in menus" :key="item.id" :menu="item"></MenuItems>
    </el-menu>
  </div>
</template>

<script>
import MenuItems from './MenuItems'
export default {
  name: "AsideLayout",
  components:{MenuItems },
  data(){
    return{
      isCollapse:false,
      menus: this.$store.state.user.menuAndDirectoryTree,

    }
  },
  computed:{
    logoTextShow(){
      return this.$store.state.layout.isCollapse? false:true;
    }
  },

}
</script>

<style scoped>
.side-layout{
  height:100%;
  display: flex;
  flex-direction: column;
  background-color: rgb(238, 241, 246);
  box-shadow: 2px 0 6px rgb(0 21 41); /*仿照若依框架阴影效果*/
}
.el-menu:not(.el-menu--collapse) {
  width: 200px;
}
.el-menu{
  min-height: 100%;
  overflow-x: hidden;
}
.logo{
  height: 60px;
  line-height: 60px;
  padding-left: 20px;
  padding-right: 20px;
}
.logo-img{
  width: 20px;
  position: relative;
  top: 5px;
  margin-right: 5px
}
.logo-b{
  color: white
}


</style>
