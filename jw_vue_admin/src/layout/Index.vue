<template>
  <el-container>
    <el-aside width="auto">
      <AsideLayout></AsideLayout>
    </el-aside>
    <el-container class="'main-container ' + isHide">
      <el-header style="height:98px">
        <HeaderLayout ></HeaderLayout>
      </el-header>
      <el-main style="padding-top: 0px">
        <div class="fade-transform-box">
          <transition name="fade-transform" mode="out-in">
            <router-view :key="$route.fullPath" ></router-view>
          </transition>
        </div>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
import AsideLayout from "./components/asidebar/AsideLayout";
import HeaderLayout from "./components/navbar/HeaderLayout";

export default {
  name:'Index',
  components:{ AsideLayout, HeaderLayout},
  data(){
    return {

    }
  },
  computed: {
    isHide() {
      return this.$store.state.layout.collapse ? "hideSideLayout" : "";
    },
  },
  created() {
    this.getWebInfo()
  },
  methods: {
    //获取博客网站配置
    getWebInfo(){
      this.request.get("/api/blogWeb/admin/getWebInfo").then(res =>{
        if (res.code === 200){
          this.$store.commit("setBlogWeb", res.data)
        }else {
          this.$message.error(res.msg)
        }
      })
    },
  }

};

</script>

<style scoped>
.el-container{
  min-height: 100vh;

}
.main-container{
  transition: margin-left 0.45s;
  min-height: 100vh;
  margin-left: 200px;
}
.hideSideLayout {
  margin-left: 64px;
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
