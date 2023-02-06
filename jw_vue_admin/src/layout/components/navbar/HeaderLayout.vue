<template>
  <div>
    <div class="header-layout">
      <!--  左侧工具栏  -->
      <div class="left-header-layout">
        <!--  折叠按钮  -->
        <div class="collapse-button">
          <span :class="collapseBtnClass" style="cursor: pointer" @click="collapse"></span>
        </div>
        <!--  面包屑导航  -->
        <BreadHeader></BreadHeader>
      </div>
      <!--  右侧工具栏  -->
      <div class="right-header-layout">
        <el-tooltip content="jing3wen的Github" placement="bottom">
          <img @click="toGithub" class="icon" aria-hidden="true" :src="github_avatar"/>
        </el-tooltip>
        <el-tooltip content="布局大小" placement="bottom">
          <el-dropdown trigger="click" class="layout-dropdown" @command="handleChangeUiCommand">
            <img class="layout-img" aria-hidden="true" :src="layoutImg"/>
            <el-dropdown-menu slot="dropdown" style="text-align: center">
              <el-dropdown-item v-for="item of sizeOptions" :key="item.value" :disabled="currentUiSize===item.value" :command="item.value" >
                {{ item.label }}
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </el-tooltip>
        <el-tooltip content="用户身份" placement="bottom">
          <el-tag class="level">{{ loginUserRoleName }}</el-tag>
        </el-tooltip>
        <el-dropdown class="dropdown" trigger="click" @command="handleUserCommand">
          <div style="display: inline-block">
            <span class="mr-5" >{{currentNickname}}</span>
            <el-avatar class="avatar ml-5" :src="currentUseravatar"></el-avatar>
            <i class="el-icon-arrow-down" style="margin-left: 5px"></i>
          </div>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item icon="el-icon-s-custom" command="person">个人信息</el-dropdown-item>
            <el-dropdown-item icon="el-icon-edit-outline" command="resetPassword">重置密码</el-dropdown-item>
            <el-dropdown-item icon="el-icon-back" command="logout">退出</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
    </div>
    <TabsHeader class="navList" :key="$route.path + 1"></TabsHeader>
    <!--  重置密码  -->
    <ResetPassword
        :f_userId.sync="resetPasswordUserId"
        :f_nickname="f_nickname"
        :f_openResetPassword.sync="f_openResetPassword">
    </ResetPassword>
  </div>
</template>

<script>

import github_avatar from '@/assets/image/layout/github_avatar.png'
import layoutSize from '@/assets/image/layout/layoutSize.png'
import BreadHeader from './BreadHeader'
import TabsHeader from  './TabsHeader'
import {logout } from '@/api/system/common'
import ResetPassword from "@/components/resetPassword/ResetPassword";

export default {
  name: "HeaderLayout",
  components:{ResetPassword, BreadHeader, TabsHeader},
  data(){
    return{
      github_avatar: github_avatar,
      layoutImg: layoutSize,
      sizeOptions: [
        { label: 'Medium', value: 'medium' },
        { label: 'Small', value: 'small' },
        { label: 'Mini', value: 'mini' }
      ],
      currentUiSize: this.$store.state.layout.currentUiSize,
      //重置密码表单
      resetPasswordUserId:'',
      f_nickname:'',
      f_openResetPassword: false
    }
  },
  computed: {
    collapseBtnClass(){
      return this.$store.state.layout.isCollapse? 'el-icon-s-unfold':'el-icon-s-fold';
    },
    currentNickname(){
      return this.$store.state.user.currentLoginUser.nickname? this.$store.state.user.currentLoginUser.nickname:'未命名';
    },
    currentUseravatar(){
      return this.$store.state.user.currentLoginUser.avatar;
    },
    loginUserRoleName(){
      let roleName = this.$store.state.user.currentLoginUser.roleList[0]
      if(roleName)
        return roleName;
      else return '未分配角色'
    }

  },
  methods: {
    // 点击收缩按钮触发
    collapse() {
      this.$store.commit("layout/collapse")
    },
    toGithub() {
      window.open("https://gitee.com/jing3wen");
    },
    //更改布局大小
    handleChangeUiCommand(command){
      this.$store.commit('layout/setElementUiSize', command)
    },
    //用户头像下拉框
    handleUserCommand(command){
      if(command === "logout"){
        this.logoutConfirm()
      } else if(command==="person"){
        this.$router.push("/system/sysPerson")
      }else if(command==='resetPassword'){
        this.resetPasswordUserId  = this.$store.state.user.currentLoginUser.id
        this.f_nickname = this.$store.state.user.currentLoginUser.nickname
        this.f_openResetPassword = !this.f_openResetPassword
      }
    },
    // 登出确认弹框
    logoutConfirm(){
      this.$confirm('确定要退出系统吗','提示',{type:'warning'}).then(() =>{
        logout().then(res =>{
          if(res.code === 200) {
            this.$store.commit('user/logout')
            this.$router.push("/login")
            this.$message.success('注销成功')
          }
          else this.$message.error(res.msg)
        })
      }).catch(()=>{

      })
    },
  },
}
</script>

<style scoped>
.header-layout{
  font-size: 12px;
  border-bottom: 1px solid #ccc;
  line-height: 60px;
  display: flex
}
.left-header-layout{
  display: flex;
  justify-content: left;
  align-items: center;
}
.collapse-button {
  font-size: 1.25rem;
  cursor: pointer;
  margin-right: 24px;
}

.right-header-layout{
  margin-left: auto;
  display: flex;
  align-items: center;
}
.right-header-layout .icon {
  cursor: pointer;
  width: 30px;
  height: 30px;
  margin-right: 15px;
}
.layout-dropdown{
  width:30px;
  height:30px;
  margin-right: 15px;
}
.layout-img {
  cursor: pointer;
  width: 30px;
  height: 30px;
  margin-bottom: 5px;
}
.right-header-layout .level {
  cursor: pointer;
  margin-right: 15px;
}

.el-icon-arrow-down{
  margin-left: 0.5rem;
  font-size: 0.75rem;
}
.dropdown{
  width: auto;
  cursor: pointer;
}
.avatar{
  height: 30px;
  width: 30px;
  border-radius: 50%;
  position: relative;
  top:10px;
}
.navList {
  margin: -5px -15px 10px -15px;
}
</style>
