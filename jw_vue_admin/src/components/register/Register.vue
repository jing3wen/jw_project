<template>
  <el-drawer
      :visible.sync="registerVisable"
      direction="rtl"
      ref="drawer"
      :before-close="cancelForm"
      size="20%"
      class="drawerStyle"
  >
    <el-card class="border-radius-25 ml-5 mr-5">
      <div class="drawer_register">
        <div class="register-form-title"><b>jw后台管理系统注册</b></div>
        <el-form :model="user" :rules="rules" ref="userForm" class="form">
          <el-form-item prop="username">
            <el-input style="margin: 10px 0" prefix-icon="el-icon-user" v-model="user.username" placeholder="用户名"></el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input style="margin: 10px 0" prefix-icon="el-icon-lock" show-password v-model="user.password" placeholder="密码"></el-input>
          </el-form-item>
          <el-form-item prop="confirmPassword">
            <el-input style="margin: 10px 0" prefix-icon="el-icon-lock" show-password v-model="user.confirmPassword" placeholder="确认密码"></el-input>
          </el-form-item>
        </el-form>
        <div class="drawer_buttons">
          <el-button class="drawer_button" type="primary" round @click="registerUser" :loading="registerLoading">{{ registerLoading ? '注册中 ...' : '确 定' }}</el-button>
          <el-button class="drawer_button" round @click="cancelForm">取 消</el-button>
        </div>
      </div>

    </el-card>

  </el-drawer>
</template>

<script>
export default {
  name: "Register",
  data() {
    return{
      user: {},
      rules: {
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
        ]
      },
      registerLoading: false,
    }
  },
  computed: {
    registerVisable(){
      return this.$store.state.user.registerDrawerVisable
    }
  },
  methods: {
    cancelForm(){
      this.$store.commit('user/changeRegisterDrawerVisable')
      this.registerLoading = false
    },
    registerUser(){
      this.$refs['userForm'].validate((valid) => {
        if (valid) {  // 表单校验合法
          this.registerLoading = true
          if (this.user.password !== this.user.confirmPassword) {
            this.$message.error("两次输入的密码不一致")
            this.registerLoading = false
            return false
          }
          this.request.post("/api/sysUser/register", this.user).then(res => {
            if(res.code === 200) {
              this.$message.success("注册成功")
              this.cancelForm()
            } else {
              this.$message.error(res.msg)
              this.registerLoading = false
            }
          })
        }
      });
    }
  },

}
</script>

<style scoped>
.drawer_register{
  margin: 0 20px;
}
.register-form-title{
  margin: 20px 0;
  text-align: center;
  font-size: 24px;
}
.drawer_buttons{
  margin: 0 30% 0;
  display:flex;
}
.drawer_button{
  margin-left: 5px;
  text-align: center;
}

</style>
