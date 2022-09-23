<template>
  <el-card style="width:600px" class="border-radius-25">
    <el-form :model="userForm" :rules="userFormRules" ref="userForm" label-width="100px" label-position="right" >
      <el-form-item label="头像:">
        <el-card class="border-radius-25">
          <el-upload
              class="avatar-uploader"
              :action="action"
              :headers="config"
              :show-file-list="false"
              :before-upload="onBeforeUpload"
              :on-progress="uploadVideoProcess"
              :on-success="handleAvatarSuccess"
          >
            <img v-if="userForm.avatar" :src="userForm.avatar" class="avatar">
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            <div slot="tip" class="el-upload__tip">只能上传 {{avatarTypeList.toString()}} 文件，且不超过 {{avatarSizeLimit}} M</div>
          </el-upload>
          <!-- 进度条 -->
          <el-progress v-if="progressFlag" :percentage="loadProgress"></el-progress>
        </el-card>
      </el-form-item>
      <el-row>
        <el-col :span="12">
          <el-form-item label="用户名: " prop="username">
            <el-input v-model="userForm.username" autocomplete="off" disabled ></el-input>
            <span style="color:red; margin-top: 10px; font-size: 8px">*登录名不能修改</span>
          </el-form-item>
        </el-col>
        <el-col :span="12"  style="padding-left: 12px">
          <el-form-item label="昵称: " prop="nickname">
            <el-input v-model="userForm.nickname" autocomplete="off"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item label="用户类型:">
            <el-input v-model="userForm.userType" autocomplete="off"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12"  style="padding-left: 12px">
          <el-form-item label="性别:" prop="sex">
            <el-radio-group v-model="userForm.sex">
              <el-radio label="男">男</el-radio>
              <el-radio label="女">女</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item label="邮箱:" prop="email">
            <el-input v-model="userForm.email" autocomplete="off"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12" style="padding-left: 12px">
          <el-form-item label="电话:" prop="phone">
            <el-input v-model="userForm.phone" autocomplete="off"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-form-item label="最后登录时间:">
        <span type="textarea" v-model="userForm.lastLoginTime">{{userForm.lastLoginTime}}</span>
      </el-form-item>
      <el-form-item label="备注:">
        <el-input type="textarea" v-model="userForm.remark" autocomplete="off"></el-input>
      </el-form-item>
    </el-form>
    <div align="right">
      <el-button type="primary" @click="addOrUpdate">修 改</el-button>
    </div>
  </el-card>

</template>

<script>

import {avatarSizeLimit, avatarTypeList} from "@/assets/js/config";
export default {
  name: "Person",
  data(){
    return {
      action:"/api/sysUser/uploadAvatar",
      userForm: {
        id:null,
        username: '',
        nickname: '',
        userType:'',
        sex: '',
        email: '',
        phone: '',
        remark:'',
        lastLoginTime:'',
      },
      userFormRules: {
        username: [
          { required: true, message: "请输入用户名", trigger: "blur" },
          { min: 2, max: 50, message: '用户名称长度必须介于 2 和 50 之间', trigger: 'blur' }
        ],
        nickname:[
          { required: true, message: '请输入用户昵称', trigger: 'blur' },
          { min: 1, max: 50, message: '长度在 1 到 50 个字符', trigger: 'blur' }
        ],
        sex:[
          { required: true, message: '请选择用户性别', trigger: 'blur'}
        ],
        email: [
          { type: "email", message: "请输入正确的邮箱地址", trigger: ["blur", "change"]}
        ],
        phone: [
          { pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/, message: "请输入正确的手机号码", trigger: "blur"}
        ]
      },
      avatarSizeLimit:avatarSizeLimit,
      avatarTypeList:avatarTypeList,
      loadProgress: 0, // 动态显示进度条
      progressFlag: false, // 关闭进度条
    }
  },
  created() {
    this.getUserByUserId()
  },
  computed:{
    config(){
      return {'token' : this.$store.state.user.currentLoginUser.token};
    }
  },
  methods:{
    onBeforeUpload(file){
      let index = file.type.indexOf('/')
      let fileType = file.type.substr(index + 1,file.type.length)
      const isIMAGE = avatarTypeList.indexOf(fileType) !== -1 ;
      const isLimit = file.size / 1024 / 1024 < avatarSizeLimit;

      if (!isIMAGE) {
        this.$message.error('上传文件只能是 '+avatarTypeList.toString()+' 格式!');
        return false
      }
      if (!isLimit) {
        this.$message.error('上传文件大小不能超过'+avatarSizeLimit+'MB!');
        return false
      }
      return isIMAGE && isLimit;
    },
    uploadVideoProcess(event, file, fileList) {
      this.progressFlag = true; // 显示进度条
      this.loadProgress = parseInt(event.percent); // 动态获取文件上传进度
      if (this.loadProgress >= 100) {
        this.loadProgress = 100
        setTimeout( () => {this.progressFlag = false}, 1000) // 一秒后关闭进度条
      }
    },
    addOrUpdate(){
      this.$refs['userForm'].validate((valid) => {
        if(valid){
          this.request.post('/api/sysUser/update',this.userForm).then(res =>{
            if(res.code === 200) {
              this.$message.success("更新用户成功, 请重新登录")
            }
            else {
              this.$message.error(res.msg)
            }
          })
        }else {
          return false;
        }
      })
    },
    getUserByUserId(){
      this.request.get('/api/sysUser/findOne',
          {params: {id : this.$store.state.user.currentLoginUser.id}}
      ).then(res =>{
        this.userForm = res.data
      })
    },
    handleAvatarSuccess(res) {
      this.$message.success('上传成功')
      this.userForm.avatar = res.data
    },
    //更新当前登录用户信息
    updateLoginUser(){

    }
  }
}
</script>

<style scoped>
.avatar-uploader {
  text-align: center;
  padding-bottom: 10px;
}
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 138px;
  height: 138px;
  line-height: 138px;
  text-align: center;
}
.avatar {
  width: 138px;
  height: 138px;
  display: block;
}
</style>
