<template>
  <el-card >
    <el-form ref="blogWebForm" :model="blogWebForm" :rules="blogWebFormRules" size="medium" label-width="120px" >
      <el-form-item label="网站名称:" prop="webName">
        <el-input style="width: 30%" v-model="blogWebForm.webName" placeholder="请输入网站名称" :maxlength="10" show-word-limit clearable></el-input>
      </el-form-item>
      <el-form-item label="网站标题:" prop="webTitle">
        <el-input style="width: 40%" v-model="blogWebForm.webTitle" placeholder="请输入网站标题" :maxlength="30" show-word-limit ></el-input>
      </el-form-item>
      <el-form-item label="网站公告:" prop="webNotices">
        <el-input style="width: 60%" v-model="blogWebForm.webNotices" type="textarea" placeholder="请输入网站公告" :maxlength="100" :autosize="{minRows: 2, maxRows: 2}"></el-input>
      </el-form-item>
      <el-form-item label="网站页脚:" prop="webFooter">
        <el-input style="width: 60%" v-model="blogWebForm.webFooter" type="textarea" placeholder="请输入网站页脚" :maxlength="100" :autosize="{minRows: 3, maxRows: 3}"></el-input>
      </el-form-item>
      <el-form-item label="首页背景:" prop="backgroundImage">
        <ImageVideoUpload
            ref="image-upload"
            :f_action.sync="uploadBlogWebUrl"
            :f_file-url.sync="blogWebForm.backgroundImage"
            :f_remove-file-url="removeBlogWebUrl">
        </ImageVideoUpload>
      </el-form-item>
      <el-form-item label="网站头像:" prop="webAvatar">
        <ImageVideoUpload
            ref="image-upload"
            :f_action.sync="uploadBlogWebUrl"
            :f_file-url.sync="blogWebForm.webAvatar"
            :f_remove-file-url="removeBlogWebUrl">
        </ImageVideoUpload>
      </el-form-item>
      <el-form-item label="文章审核:" prop="articleCheck" required>
        <el-switch v-model="blogWebForm.articleCheck" active-text="发布文章直接通过" inactive-text="发布文章需要审核"
                   active-color="#13CE66" :active-value='"1"' :inactive-value='"0"'  @change="submitForm">
        </el-switch>
      </el-form-item>
      <el-form-item label="评论审核:" prop="commentCheck" required>
        <el-switch v-model="blogWebForm.commentCheck" active-text="发布评论直接通过" inactive-text="发布评论需要审核"
                   active-color="#13CE66" :active-value='"1"' :inactive-value='"0"'  @change="submitForm">
        </el-switch>
      </el-form-item>
      <el-form-item label="留言板审核:" prop="messageCheck" required>
        <el-switch v-model="blogWebForm.messageCheck" active-text="发布留言直接通过" inactive-text="发布留言需要审核"
                   active-color="#13CE66" :active-value='"1"' :inactive-value='"0"'  @change="submitForm">
        </el-switch>
      </el-form-item>
      <el-form-item label="开启看板娘:" prop="webKanban" required>
        <el-switch v-model="blogWebForm.webKanban" active-text="开启" inactive-text="关闭"
                   active-color="#13CE66" :active-value='"1"' :inactive-value='"0"'  @change="submitForm">
        </el-switch>
      </el-form-item>
      <el-form-item label="网站状态:" prop="status" required>
        <el-switch v-model="blogWebForm.status" active-text="正常" inactive-text="维护"
                   active-color="#13CE66" inactive-color="#F90606" :active-value='"1"' :inactive-value='"0"' @change="submitForm">
        </el-switch>
      </el-form-item>
      <el-form-item size="large">
        <el-button type="primary" @click="submitForm">更新</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>

<script>
import ImageVideoUpload from "@/components/file/upload/imageVideoUpload/ImageVideoUpload";
export default {
  name: "blogWeb",
  components:{ImageVideoUpload},
  data() {
    return {
      blogWebForm: {
        webName: "",
        webTitle: "",
        webNotices: "",
        webFooter: "",
        backgroundImage: "",
        webAvatar: "",
        webKanban: "1",
        articleCheck: "1",
        commentCheck: "1",
        messageCheck: "1",
        status: "1",
      },
      blogWebFormRules: {
        webName: [
            {required: true, message: '请输入网站名称', trigger: 'blur'}
        ],
        webTitle: [
            {required: true, message: '请输入网站标题', trigger: 'blur'}
        ],
        webNotices: [
            {required: true, message: '请输入网站公告', trigger: 'blur'}
        ],
        webFooter: [
            {required: true, message: '请输入网站页脚', trigger: 'blur'}
        ],
      },
      uploadBlogWebUrl:"/api/file/fileUpload/blog/blogWeb",
      removeBlogWebUrl:"/api/file/deleteUploadFile/blog/blogWeb",
    }
  },
  created() {
    this.getWebInfo()
  },
  watch:{

  },
  methods:{
    //获取网站配置
    getWebInfo(){
      this.request.get("/api/blogWeb/admin/getWebInfo").then(res =>{
        if (res.code === 200){
          this.blogWebForm = res.data
          this.$store.commit("setBlogWeb", res.data)
        }else {
          this.$message.error(res.msg)
        }
      })
    },
    submitForm(){
      this.$refs.blogWebForm.validate((valid) => {
        if(valid){
          this.request.post("/api/blogWeb/admin/updateBlogWeb", this.blogWebForm).then(res =>{
            if (res.code === 200){
              this.$message.success("更新成功")
              this.getWebInfo()
            }else {
              this.$message.error(res.msg)
            }
          })
        }else {
          return false
        }
      })
    }
  }
}
</script>

<style scoped>
.query-form{

}

</style>
