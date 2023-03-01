<template>
  <el-container class="login-container">
    <!--  登录表单  -->
    <div class="login-form">
      <el-card class="border-radius-25">
        <div class="login-form-title">
          <img src="../assets/image/layout/logo200.png" style="width: 33px" alt=""/>
          <b>后台管理系统</b>
        </div>
        <el-form :model="loginUser" :rules="loginUserRules" ref="loginUser">
          <el-form-item prop="username">
            <el-input size="medium" style="margin: 10px 0" prefix-icon="el-icon-user" v-model="loginUser.username" placeholder="用户名"></el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input size="medium" style="margin: 10px 0" prefix-icon="el-icon-lock" show-password v-model="loginUser.password" placeholder="密码" @keyup.enter.native="login" ></el-input>
          </el-form-item>
          <el-form-item style="margin: 40px 0 30px; text-align: right">
            <el-button class="login-button" type="primary" round size="medium" autocomplete="off" @keyup.enter.native="login" @click="login" :loading="loginLoading">{{ loginLoading ? '登录中 ...' : '登 录' }}</el-button>
          </el-form-item>
          <el-form-item>
            <el-button class="register-button" type="text"  size="medium" autocomplete="off" @click="registerVisible = true">没有账号?点我注册</el-button>
          </el-form-item>
        </el-form>
      </el-card>
    </div>
    <!--  注册抽屉  -->
    <el-drawer
        :visible="registerVisible"
        direction="rtl"
        ref="drawer"
        :before-close="cancelRegisterForm"
        size="20%"
        class="drawerStyle"
    >
      <el-card class="border-radius-25 ml-5 mr-5">
        <div class="drawer_register">
          <div class="register-form-title"><b>jw后台管理系统注册</b></div>
          <el-form :model="registerForm" :rules="registerFormRules" ref="registerForm" class="form">
            <el-form-item prop="email">
              <el-input style="margin: 10px 0" prefix-icon="el-icon-message" v-model="registerForm.email" placeholder="邮箱"></el-input>
            </el-form-item>
            <el-form-item prop="username">
              <el-input style="margin: 10px 0" prefix-icon="el-icon-user" v-model="registerForm.username" placeholder="用户名"></el-input>
            </el-form-item>
            <el-form-item prop="password">
              <el-input style="margin: 10px 0" prefix-icon="el-icon-lock" show-password v-model="registerForm.password" placeholder="密码"></el-input>
            </el-form-item>
            <el-form-item prop="confirmPassword">
              <el-input style="margin: 10px 0" prefix-icon="el-icon-lock" show-password v-model="registerForm.confirmPassword" placeholder="确认密码"></el-input>
            </el-form-item>
            <el-form-item prop="code">
              <el-input style="margin: 10px 0" prefix-icon="el-icon-files" v-model="registerForm.code" placeholder="验证码"></el-input>
            </el-form-item>
          </el-form>
          <div class="drawer_buttons">
            <el-button class="drawer_button" type="primary" round @click="getCode">获取验证码</el-button>
            <el-button class="drawer_button" type="primary" round @click="registerUser" :loading="registerLoading">{{ registerLoading ? '注册中 ...' : '注   册' }}</el-button>
            <el-button class="drawer_button" round @click="cancelRegisterForm">取 消</el-button>
          </div>
        </div>
      </el-card>

    </el-drawer>
  </el-container>
</template>

<script>
import {setRoute} from "@/router";
import {registerUserCodeType, registerType} from "@/assets/js/config";
import {encrypt} from "@/assets/js/common";

export default {
  name: "Login",
  data() {
    return {
      loginUser: {
        username: "showuser",
        password: "123456",
      },
      loginUserRules: {
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

      registerVisible:false,
      //注册用户表单
      registerForm: {},
      registerFormRules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
          { min: 3, max: 10, message: '长度在 3 到 5 个字符', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: 'blur' }
        ],
        confirmPassword:[
          { required: true, message: '请确认密码', trigger: 'blur' },
          { min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: 'blur' }
        ],email: [
          { required: true, message: '请输入邮箱', trigger: 'blur' },
          { type: "email", message: "请输入正确的邮箱地址", trigger: ["blur", "change"]}
        ],code: [
          { required: true, message: '请输入验证码', trigger: 'blur' },
        ]
      },
      registerLoading: false,
    }
  },
  methods: {
    //登录
    login() {
      this.$refs['loginUser'].validate((valid) => {
        if (valid) {  // 表单校验合法
          this.loginLoading = true
          this.loginUser.password = encrypt(this.loginUser.password)
          this.request.post("/api/login/userLogin", this.loginUser).then(res => {
            if(res.code === 200) {
              this.$store.commit("user/loginUser", res.data)
              this.$message.success("登录成功")
              //加载路由信息
              this.getMenusAndDirectoryByUserId()
            }
            else {
              this.$message.error(res.msg)
              this.loginUser.password=""
              this.loginLoading=false
            }
          })
        } else {
          return false;
        }
      });
    },
    //获取登录用户路由信息
    getMenusAndDirectoryByUserId(){
      this.request.get('/api/sysMenu/getMenusAndDirectoryByUserId',
          {params: {userId : this.$store.state.user.currentLoginUser.id}}
      ).then(res=>{
        if(res.code === 200) {
          this.$store.state.user.menuAndDirectoryTree = res.data
          //动态设置当前登录用户的路由
          setRoute()
          this.loginLoading = false
          this.$router.push("/index")
        }else {
          this.$message.error(res.msg)
        }
      })
    },

    //获取验证码
    getCode(){
      const reg = /^([a-zA-Z0-9]+[-_\.]?)+@[a-zA-Z0-9]+\.[a-z]+$/;
      if(this.registerForm.email === '' || !reg.test(this.registerForm.email)){
        this.$message.error("请输入正确邮箱")
        return false
      }
      let codeForm = {
        email: this.registerForm.email,
        phone: '',
        type: registerUserCodeType
      }
      this.request.get("/api/sysUser/getCodeForType", {params: codeForm}).then(res => {
        if(res.code === 200) {
          this.$message.success("验证码已经发送，请注意查收")
        } else {
          this.$message.error(res.msg)
          this.registerLoading = false
        }
      })
    },
    //注册表单隐藏
    cancelRegisterForm(){
      this.registerVisible = false
      this.registerLoading = false
    },
    //注册用户
    registerUser(){
      this.registerForm.userType = registerType
      this.$refs['registerForm'].validate((valid) => {
        if (valid) {  // 表单校验合法
          this.registerLoading = true
          if (this.registerForm.password !== this.registerForm.confirmPassword) {
            this.$message.error("两次输入的密码不一致")
            this.registerLoading = false
            return false
          }
          this.registerForm.password = encrypt(this.registerForm.password)
          this.request.post("/api/sysUser/register", this.registerForm).then(res => {
            if(res.code === 200) {
              this.$message.success("注册成功")
              this.cancelRegisterForm()
            } else {
              this.$message.error(res.msg)
              this.registerForm.password=""
              this.registerLoading = false
            }
          })
        }
      });
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

.drawer_register{
  margin: 0 20px;
}
.register-form-title{
  margin: 20px 0;
  text-align: center;
  font-size: 24px;
}
.drawer_buttons{
  display:flex;
  margin: 0 auto;
}
.drawer_button{
  margin-left: 5px;
  text-align: center;
}

</style>
