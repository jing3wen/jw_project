<template>
  <div class="">
    <el-container>
      <!-- 页眉 -->
      <el-header class="layout_header">
        <Header></Header>
      </el-header>
      <!-- 主要的 -->
      <el-main class="layout-main">
        <div class="fade-transform-box">
          <Transition name="fade-transform" mode="out-in">
            <MainContext></MainContext>
          </Transition>
        </div>
      </el-main>
      <!-- 页尾\页脚 -->
      <el-footer class="layout_footer">
        <Copyright></Copyright>
      </el-footer>
    </el-container>
    <!--Backtop 回到顶部-->
    <BackTop></BackTop>
    <!-- 打开菜单栏固钉 -->
    <!--    <MenuRight></MenuRight>-->
  </div>
</template>

<script>

import Header from "./header/Header";
import Copyright from "./footer/Copyright";
import BackTop from "./BackTop";
import MainContext from "./mainContext/MainContext";

export default {
  name: "Index",
  components:{MainContext, BackTop, Copyright, Header},
  data(){
    return {

    }
  },
  methods: {

  }

}
</script>

<style scoped>
/* 顶部菜单栏 */
.layout_header {
  width: 100%;
  --el-header-padding: 0px 50px;
  box-shadow: 0 1px 10px 0 rgb(7 17 27 / 10%);
  background: white;
  position: fixed;
  top: 0;
  left: 0;
  z-index: 1000;
}

/*主体*/
.layout-main {
  margin-top: 60px;
}
/* 首页底部版权样式 */
.layout_footer {
  border-top: 1px solid #ebebeb;
  background: #fff;
  height: auto;
}

.fade-transform-enter-active,
.fade-transform-leave-active {
  transition: all 0.5s ease 0s;
}
.fade-transform-enter {
  opacity: 0;
  transform: translateX(-30px);
}
.fade-transform-leave-to {
  opacity: 0;
  transform: translateX(30px);
}
.fade-transform-box {
  position: relative;
  top: 0px;
  bottom: 0px;
  width: 100%;
  overflow: hidden;
}

</style>
