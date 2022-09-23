<template>
  <!--  面包屑导航  -->
  <el-breadcrumb separator="/" class="breadcrumb-header">
    <el-breadcrumb-item :to="'/index'">主页</el-breadcrumb-item>
    <el-breadcrumb-item >
      <!--        <router-link :to="this.$route.fullPath"></router-link>-->
      {{ currentPathName }}
    </el-breadcrumb-item>
  </el-breadcrumb>
</template>

<script>
export default {
  name: "BreadHeader",
  computed:{
    currentPathName(){
      return this.$store.state.layout.currentPathName
    },
  }
}
</script>

<style scoped>
.breadcrumb-header {
  margin: 24px 0;
  font-size: 16px;
}
</style>
