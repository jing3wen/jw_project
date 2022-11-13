<template>
  <el-backtop :bottom="100" :visibility-height="100" class="backtop_box">
    <el-icon :size="20">
      <CaretTop />
    </el-icon>
  </el-backtop>
</template>

<script>

export default {
  name: "BackTop"
}
</script>

<style scoped>
/* 回到顶部 */
.backtop_box {
  background-color: white;
  text-align: center;
  border-radius: 5px;
  border: 1px solid #E9E9E9;
  font-size: 13px;
  color: #646464;
  z-index: 999;
}

.el-backtop:hover {
  /* background-color: rgba(47, 167, 185); */
  /* 1、linear-gradient() 线性渐变 2、radial-gradient() 径向渐变 3、repeating-linear-gradient() 重复线性渐变 4、repeating-radial-gradient() 重复径向渐变  */
  background: repeating-linear-gradient(#2fa7b9, #535b9a);
  color: white;
}
</style>
