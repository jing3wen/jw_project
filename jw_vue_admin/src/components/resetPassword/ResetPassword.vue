<template>
  <el-dialog :title="dialogTitle" :visible.sync="openResetPassword" width="600px" @close="clickCancel">
    <div style="margin-bottom: 10px; font-size:Medium; ">重置用户:
      <el-tag type="success" style="font-size:large; font-weight:bold">
        {{nickname}}
      </el-tag>
      密码
    </div>
    <el-card class="border-radius-25">
      <el-form :model="passwordForm" ref="passwordForm" :rules="passwordFormRules" label-width="120px" label-position="right">
        <el-form-item label="原密码:" prop="password">
          <el-input prefix-icon="el-icon-lock" show-password
                    v-model="passwordForm.password" placeholder="原密码"></el-input>
        </el-form-item>
        <el-form-item label="新密码:" prop="newPassword">
          <el-input prefix-icon="el-icon-lock" show-password
                    v-model="passwordForm.newPassword" placeholder="新密码"></el-input>
        </el-form-item>
        <el-form-item label="确认新密码:" prop="confirmPassword">
          <el-input prefix-icon="el-icon-lock" show-password
                    v-model="passwordForm.confirmPassword" placeholder="确认新密码"></el-input>
        </el-form-item>
      </el-form>
    </el-card>
    <div slot="footer" class="dialog-footer">
      <el-button type="text" align="left" @click="setInitPassword">设置成系统默认密码</el-button>
      <el-button @click="clickCancel()">取 消</el-button>
      <el-button type="primary" @click="clickConfirm">确 定</el-button>
    </div>
  </el-dialog>
</template>

<script>
import {initPassword} from "@/assets/js/config";
import Element from "element-ui";
import store from "@/store";
import router from "@/router";
import {logout} from "@/api/system/common";

export default {
  name: "ResetPassword",
  props:{
    f_userId: '',
    f_nickname:'',
    f_openResetPassword:false,
  },
  data(){
    return{
      dialogTitle:'重置密码',
      passwordForm:{
        userId: '',
        password:'',
        newPassword:'',
        confirmPassword:'',
      },
      passwordFormRules:{
        password:[
          { required: true, message: '请输入原密码', trigger: 'blur' },
          { min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: 'blur' }
        ],
        newPassword:[
          { required: true, message: '请输入新密码', trigger: 'blur' },
          { min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: 'blur' }
        ],
        confirmPassword:[
          { required: true, message: '请输入确认密码', trigger: 'blur' },
          { min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: 'blur' }
        ],
      }
    }
  },
  computed:{
    openResetPassword: {
      get() {
        return this.f_openResetPassword
      },
      set(val) {
        this.$emit('update:f_openResetPassword', val)  //改变的值赋值给父组件
      }
    },
    nickname(){
      return this.f_nickname
    }
  },
  methods:{
    clickCancel(){
      this.openResetPassword = false;
      this.resetForm()
    },
    clickConfirm(){
      this.$refs['passwordForm'].validate((valid) => {
        if(valid){
          if(this.passwordForm.newPassword !== this.passwordForm.confirmPassword){
            this.$message.error("两次输入的密码不一致")
            this.openResetPassword = false
            return false
          }else {
            this.passwordForm.userId = this.f_userId
            this.request.post('/api/sysUser/resetPassword', this.passwordForm).then(res => {
              if(res.code === 200) {
                if(this.f_userId === this.$store.state.user.currentLoginUser.id){
                  this.$message.warning('当前用户密码已被修改，请重新登录')
                  logout().then(res =>{
                    store.commit('user/logout')
                    router.push("/login")
                  })
                  return
                }
                this.$message.success("修改成功")
                this.openResetPassword = false
                this.resetForm()
              }
              else this.$message.error(res.msg)
            })
          }
        }else {
          return false
        }
      })
    },
    setInitPassword(){
      this.passwordForm.newPassword = initPassword
      this.passwordForm.confirmPassword = initPassword
    },
    resetForm(){
      //重置验证状态
      if(this.$refs['passwordForm'] !== undefined){
        this.$refs['passwordForm'].resetFields()
      }
      //重置表单
      this.passwordForm = this.$options.data().passwordForm
    }
  }

}
</script>

<style scoped>

</style>
