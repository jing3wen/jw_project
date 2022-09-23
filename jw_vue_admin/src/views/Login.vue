<template>
  <el-container class="login-container">
    <div class="login-form">
      <el-card class="border-radius-25">
        <div class="login-form-title">
          <img src="@/assets/image/layout/logo200.png" style="width: 33px"/>
          <b>后台管理系统</b>
        </div>
        <el-form :model="user" :rules="rules" ref="userForm">
          <el-form-item prop="username">
            <el-input size="medium" style="margin: 10px 0" prefix-icon="el-icon-user" v-model="user.username" placeholder="用户名"></el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input size="medium" style="margin: 10px 0" prefix-icon="el-icon-lock" show-password v-model="user.password" placeholder="密码" @keyup.enter.native="login" ></el-input>
          </el-form-item>
          <el-form-item style="margin: 40px 0 30px; text-align: right">
            <el-button class="login-button" type="primary" round size="medium" autocomplete="off" @keyup.enter.native="login" @click="login" :loading="loginLoading">{{ loginLoading ? '登录中 ...' : '登 录' }}</el-button>
          </el-form-item>
          <el-form-item>
            <el-button class="register-button" type="text"  size="medium" autocomplete="off" @click="openRegisterDrawer">没有账号?点我注册</el-button>
          </el-form-item>
        </el-form>
      </el-card>
    </div>
    <Register></Register>
  </el-container>
</template>

<script>
import Register from "@/components/register/Register";
import {setRoute} from "@/router";

export default {
  name: "Login",
  components:{
    Register
  },
  data() {
    return {
      user: {
        username: "jingwen",
        password: "123456",
      },
      rules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
          { min: 3, max: 10, message: '长度在 3 到 5 个字符', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: 'blur' }
        ],
      },
      loginLoading:false,
    }
  },
  methods: {
    login() {
      this.$refs['userForm'].validate((valid) => {
        if (valid) {  // 表单校验合法
          this.loginLoading = true
          this.request.post("/api/login/userLogin", this.user).then(res => {
            if(res.code === 200) {
              this.$store.commit("user/loginUser", res.data)
              //动态设置当前登录用户的路由
              setRoute()
              this.$message.success("登录成功")
              this.loginLoading = false
              this.$router.push("/index")
            }
            else {
              this.$message.error(res.msg)
            }
          })
        } else {
          return false;
        }
      });
    },
    openRegisterDrawer(){
      this.$store.commit('user/changeRegisterDrawerVisable')
    }
  }
}
</script>

<style>
.login-container {
  position: absolute;
  top: 0;
  bottom: 0;
  right: 0;
  left: 0;
  background: url(../assets/image/login_background_img.jpg) center center
  / cover no-repeat;
}
.login-form {
  height: 100vh;
  position: absolute;
  top: 0;
  bottom: 0;
  right: 0;
  background: #fff;
  padding: 250px 60px 180px;
  width: 430px;
  border-radius: 10px
}
.login-form-title{
  padding-left: 10px;
  margin: 20px 0;
  text-align: center;
  font-size: 24px;
}
.login-button {
  width: 100%;
}
.register-button{

}

</style>
