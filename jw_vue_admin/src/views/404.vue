<template>
  <div class="error-404-container">
    <div class="error-404">
      <div class="left">
        <img src="@/assets/image/404.png">
        <h1 style="font-size: 26px;">找不到网页</h1>
        <p></p>
        <div class="opera">
          <el-button type="primary" @click="toPage('/index')">返回主页</el-button>
          <el-button type="danger" @click="logout">点我退出</el-button>
        </div>
      </div>
      <div class="right">
        <h2>你可以尝试以下操作</h2>
        <p>1. 如果是手动更改的网页地址，请检查网页地址是否正确</p>
        <p>2. 点击【返回】按钮刷新页面后重试</p>
        <p>3. 联系系统管理员</p>
      </div>
    </div>
  </div>
</template>

<script>
import {logout} from "@/api/system/common";

export default {
  name: 'NotFound',
  methods:{
    toPage(url){
      this.$router.push(url)
    },
    logout(){
      logout().then(res =>{
        if(res.code === 200) {
          this.$store.commit('user/logout')
          this.$message.success('注销成功')
          this.$router.push("/login")
        }
        else this.$message.error(res.msg)
      })
    }
  }
}
</script>

<style scoped>
.error-404-container {
  height: 100vh;
  text-align: center;
  display: flex;
  align-items: center;
  justify-content: center;
}
.error-404{
  margin-top: -100px;
  display: flex;
}
.opera {
  margin-top: 30px;
}

.right {
  text-align: left;
  margin-top: 16px;
  margin-left: 40px;
  padding: 30px 50px;
  border-radius: 16px;
  background: url("../assets/image/404-tip.png") no-repeat;
  background-size: 100% 100%;
  box-sizing: border-box;
}
h2 {
  font-size: 16px;
  margin-bottom: 16px;
}
p {
  margin: 0;
  padding: 0;
  font-size: 14px;
  line-height: 26px;
}
</style>
