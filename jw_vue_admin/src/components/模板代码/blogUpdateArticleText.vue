<template>
  <!-- 富文本格式的新增文章 -->
  <div class="addArticle_box">
    <h3>
      <el-icon style="margin-right: 10px;" name="edit"></el-icon>
      编辑文章
    </h3>

    <div class="editor-form">
      <el-row :gutter="20">
        <el-col :xs="24" :sm="24" :md="24" :lg="17" :xl="17">
          <div class="write_abstract">
            <h4>标题</h4>
            <el-input v-model="article.articleTitle" placeholder="请输入文章标题" maxlength="50" show-word-limit
                      size="large" clearable />
            <p>标示文章、作品内容的简明语句，填写醒目的</p>
          </div>
          <div class="write_abstract">
            <!-- 富文本编辑器 -->
            <Toolbar style="border-bottom: 1px solid #ccc" :editor="editor" :defaultConfig="toolbarConfig" :mode="mode" />
            <Editor style="height: 420px; overflow-y: hidden;" v-model="article.articleContent" :defaultConfig="editorConfig"
                    :mode="mode" @onCreated="onCreated" @onChanged="onChanged" class="article" />
          </div>
        </el-col>
        <el-col :xs="24" :sm="24" :md="24" :lg="7" :xl="7">
          <div class="write_abstract">
            <h4>简介</h4>
            <el-input v-model="article.articleSummary" :autosize="{ minRows: 3, maxRows: 5 }" maxlength="200"
                      show-word-limit type="textarea" placeholder="请简单概述文章内容" clearable />
            <p>筛选文章主要内容，让读者初步了解文章</p>
          </div>
          <div class="write_abstract">
            <h4>缩略图</h4>
            <el-input size="large" v-model="article.articleCover" placeholder="请点击上传图片或手动输入图片地址">
              <template #append>
                <el-upload ref="uploadEle" class="upload-demo" :action="uploadURL"
                           :on-success="handleAvatarSuccess" :before-upload="beforeUploadFunction"
                           :show-file-list="false">
                  <template #trigger>
                    <el-button>上传图片</el-button>
                  </template>
                </el-upload>
              </template>
            </el-input>
            <p>注：点击上传的图片地址由系统生成，修改会导致图片失效</p>
          </div>
          <div class="write_abstract">
            <h4>分类</h4>
            <el-select v-model="article.categoryId" placeholder="请选择文章分类" size="large">
              <el-option v-for="item in categoryList" :label="item.categoryName" :value="item.categoryId" :key="item.categoryId"/>
            </el-select>
            <p>选择分类可让读者更便捷的查找文章</p>
          </div>
          <el-row :gutter="20">
            <el-col :span="12">
              <div class="write_abstract">
                <h4>文章可见</h4>
                <el-select v-model="article.articleVisible" placeholder="文章状态" size="large">
                  <el-option label="私密" value="0" />
                  <el-option label="公开" value="1" />
                </el-select>
                <p>私密可视为草稿</p>
              </div>
            </el-col>
            <el-col :span="12">
              <div class="write_abstract">
                <h4>评论</h4>
                <el-select v-model="article.commentAllowed" placeholder="评论是否开启" size="large">
                  <el-option label="关闭" value="0" />
                  <el-option label="开启" value="1" />
                </el-select>
                <p>开启评论，开启新世界</p>
              </div>
            </el-col>
          </el-row>
          <div class="write_abstract" style="text-align: center;background: none;">
            <el-button text @click="previewArticleDialogFormVisible = true">预览</el-button>
            <el-button style="width: 50%;" plain color="#2fa7b9" @click="updateArticle">修改</el-button>
            <p>注：普通用户提交文章需要等待管理员审核</p>
          </div>
        </el-col>
      </el-row>
    </div>

  </div>
  <!-- 预览文章 -->
<!--  <PreviewArticle :previewArticleDialogFormVisible="previewArticleDialogFormVisible" :valueHtml="valueHtml"-->
<!--                  @onPreviewArticleCloseDialog="previewArticleCloseDialog"></PreviewArticle>-->
</template>

<script>
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'
export default {
  name: "blogUpdateArticleText",
  components: { Editor, Toolbar },
  data() {
    return {
      //Editor编辑器配置
      editor: null,
      toolbarConfig: { },
      editorConfig: {
        placeholder: '该编辑器为富文本编辑器',
        MENU_CONF: {}
      },
      mode: 'default', // 或 'simple'

      // 编写信息
      article: {
        articleId: null,  //文章id
        userId: '',  //文章作者
        categoryId: '', // 文章类别id
        isTop:'',  //是否顶置
        articleCover: '', // 文章封面
        articleTitle: '', // 文章标题
        articleSummary: '', // 文章简介
        articleContent: '', // 文章内容
        articleVisible: '1', //文章是否可见（0代表仅自己可见，1表示所有人可见)
        commentAllowed: '1', // 文章是否允许评论(0表示不能评论，1表示可以评论)
        articleCheck: '1', //文章审核状态（1表示通过，0表示未通过）TODO 此处默认审核通过，后续可更改
      },
      articleIsChange: false, //判断文章是否修改
      // 分类信息
      categoryList: [],
      // 缩略图上传到服务器的路径
      uploadURL: process.env.VUE_APP_URL + "/thumbnailUpload",
      // 预览对话框状态
      previewArticleDialogFormVisible: false
    }
  },
  created() {
    //获取待编辑的文章信息
    this.getUpdateArticle()
    //获取所有文章类别
    this.getAllCategory()
    //加载编辑器
    this.createEditorConfig()
  },
  beforeDestroy() {
    const editor = this.editor
    if (editor == null) return
    editor.destroy() // 组件销毁时，及时销毁编辑器
  },
  destroyed() {
    this.cacheArticleWhenChangeVue()
  },
  methods: {
    //获取所有文章分类
    getAllCategory(){
      this.request.get('/api/blogCategory/front/getAllCategory').then(res =>{
        if (res.code === 200){
          this.categoryList = res.data
        }else {
          this.$message.error(res.msg)
        }
      })
    },
    //根据url路径获取待编辑的文章内容
    getUpdateArticle(){
      //获取url中的文章id
      const path = this.$route.path;
      const index = path.lastIndexOf("\/")
      const articleId = parseInt(path.substring(index + 1,path.length))
      if (articleId) {
        this.request.get("/api/blogArticle/admin/getUpdateArticle", {params: {articleId: articleId}}).then(res => {
          this.article = res.data
          //缓存 数据库版本article
          sessionStorage.setItem('update-oldArticleId-'+this.article.articleId, JSON.stringify(this.article))
          //检测浏览器中是否缓存了 新版本article
          const newArticle = sessionStorage.getItem('update-newArticleId-'+this.article.articleId)
          if(newArticle){
            this.$notify({
              title: '提示',
              message: '已自动恢复到上次编辑状态',
              type:'success'
            });
            this.article = JSON.parse(newArticle)
          }
        })
      }
    },
    // 新增或编辑文章
    updateArticle() {
      // 判断内容是否填写完整
      if (this.article.articleTitle !== "" && this.article.articleSummary !== "" &&
          this.article.categoryId !== "" && this.article.articleVisible !== "" &&
          this.article.commentAllowed !== "") {
        // 更新文章
        this.request.post("/api/blogArticle/admin/updateArticle", this.article).then((res) => {
          if (res.code === 200) {
            //重置编辑器表单
            this.$notify({
              title: '提示',
              message: this.article.commentAllowed === '1' ? "文章更新成功（已开启默认审核通过）" :
                  "文章更新成功，等待管理员审核",
              type: 'success',
            })
            //任务已提交，清空缓存
            sessionStorage.removeItem('update-oldArticleId-'+this.article.articleId)
            sessionStorage.removeItem('update-newArticleId-'+this.article.articleId)
            this.$store.commit('layout/removeTab', '编辑文章')
            this.resetArticleForm()
            this.$router.push('/blog/blogArticle')
          }
        })
      } else {
        this.$message.error('除缩略图之外的内容都是必填项哦~')
      }
    },
    /**
     * 销毁组件时需要判断内容是否被修改，若修改则缓存内容
     * oldArticle表示数据库中的文章内容
     * newArticle表示当前页面的内容
     **/
    cacheArticleWhenChangeVue(){
      const oldArticle = JSON.parse(sessionStorage.getItem('update-oldArticleId-'+this.article.articleId))
      const newArticle = this.article
      if (!(oldArticle.categoryId === newArticle.categoryId &&
          oldArticle.isTop === newArticle.isTop &&
          oldArticle.articleCover === newArticle.articleCover &&
          oldArticle.articleTitle === newArticle.articleTitle &&
          oldArticle.articleSummary === newArticle.articleSummary &&
          oldArticle.articleContent === newArticle.articleContent &&
          oldArticle.articleVisible === newArticle.articleVisible &&
          oldArticle.commentAllowed === newArticle.commentAllowed &&
          oldArticle.articleCheck === newArticle.articleCheck)) {
        this.$notify({
          title: '提示',
          message: '发现文章修改，已自动缓存数据',
          type:'success'
        });
        sessionStorage.setItem('update-newArticleId-'+this.article.articleId, JSON.stringify(this.article))
      }else {
        //防止用户手动恢复到默认状态后 还存在缓存数据
        sessionStorage.removeItem('update-oldArticleId-'+this.article.articleId)
        sessionStorage.removeItem('update-newArticleId-'+this.article.articleId)
      }
    },
    //重置表单, 重置验证状态
    resetArticleForm(){
      //重置验证状态
      // if(this.$refs['userForm']){
      //   this.$refs['userForm'].resetFields()
      // }
      //重置表单
      this.article = this.$options.data().article
    },

    /*
    * 下面为编辑器相关函数
    */
    onCreated(editor) {
      this.editor = Object.seal(editor) // 一定要用 Object.seal() ，否则会报错
      // console.log("onCreated", editor.getHtml());
    },
    onChanged(editor){
      // console.log("onChange", editor.getHtml());
    },
    //创建编辑器相关配置
    createEditorConfig() {
      const that = this;
      this.editorConfig.MENU_CONF['uploadImage'] = {
        // wangEditor上传文件模型name属性为'wangeditor-uploaded-image'，
        // 注意 springBoot 中的 MultipartFile 默认属性为"file" 要更改为file
        fieldName: 'file',
        // 上传图片的配置
        server: "/api/file/fileUpload/blog/article/image",
        // 单个文件的最大体积限制，默认为 2M
        maxFileSize: 5 * 1024 * 1024, // 5M
        headers: {
          'token' : this.$store.state.user.currentLoginUser.token
        },
        //自定义插入图片
        customInsert(res, insertFn) {
          // res 即服务端的返回结果
          if(res.code === 200){
            that.$message.success('图片上传成功')
            // 从 res 中找到 url alt href ，然后插入图片  insertFn(url, alt, href)
            insertFn(res.data, '', '')
          }else {
            that.$message.error('图片上传失败 '+res.msg)
          }
        },
      }

      this.editorConfig.MENU_CONF['uploadVideo'] = {
        // wangEditor上传文件模型name属性为'wangeditor-uploaded-image'，
        // 注意 springBoot 中的 MultipartFile 默认属性为"file" 要更改为file
        fieldName: 'file',
        // 上传视频的配置
        server: "/api/file/fileUpload/blog/article/video",
        // 单个文件的最大体积限制，默认为 20M
        maxFileSize: 100 * 1024 * 1024, // 100M
        headers: {
          'token' : this.$store.state.user.currentLoginUser.token
        },
        //自定义插入图片
        customInsert(res, insertFn) {
          // res 即服务端的返回结果
          if(res.code === 200){
            that.$message.success('视频上传成功')
            // 从 res 中找到 url alt href ，然后插入图片  insertFn(url, alt, href)
            insertFn(res.data, '', '')
          }else {
            that.$message.error('视频上传失败 '+res.msg)
          }
        },
      }
      this.editorConfig.MENU_CONF['codeSelectLang'] = {
        // 代码语言
        codeLangs: [
          {text: 'CSS', value: 'css'},
          {text: 'HTML', value: 'html'},
          {text: 'XML', value: 'xml'},
          {text: 'Javascript', value: 'javascript'},
          {text: 'Typescript', value: 'typescript'},
          {text: 'JSX', value: 'jsx'},
          {text: 'Go', value: 'go'},
          {text: 'PHP', value: 'php'},
          {text: 'C', value: 'c'},
          {text: 'Python', value: 'python'},
          {text: 'Java', value: 'java'},
          {text: 'C++', value: 'cpp'},
          {text: 'C#', value: 'csharp'},
          {text: 'Visual Basic', value: 'visual-basic'},
          {text: 'SQL', value: 'sql'},
          {text: 'Ruby', value: 'ruby'},
          {text: 'Swift', value: 'swift'},
          {text: 'Bash', value: 'bash'},
          {text: 'Markdown', value: 'markdown'}
        ]
      }
      // 以上是富文本框的相关配置
    },
    // 图片上传格式、大小要求
    beforeUploadFunction(rawFile) {
      if (rawFile.type !== 'image/jpeg' &&
          rawFile.type !== 'image/jpg' &&
          rawFile.type !== 'image/png' &&
          rawFile.type !== 'image/gif') {
        this.$message.error('仅支持图片格式.png | .jpg | .jpeg | .gif ')
        return false
      } else if (rawFile.size / 1024 / 1024 > 5) {
        this.$message.error('仅支持大小不超过5MB的图片!')
        return false
      }
      return true
    },
    // 图片上传成功后执行的函数
    handleAvatarSuccess(response) {
      this.article.articleCover = response;
    },
    // 关闭预览窗口
    previewArticleCloseDialog(visible) {
      this.previewArticleDialogFormVisible = visible;
    },
  },

}
</script>
<style src="../../../node_modules/@wangeditor/editor/dist/css/style.css"></style>
<style scoped>
.addArticle_box {
  width: 100%;
  height: auto;
  padding: 0 10px;

}
.addArticle_box>h3{
  color: #2fa7b9;
  margin-bottom:10px;
  padding: 20px 20px;
  background-color: white;
  box-shadow: 2px 2px 2px 1px rgba(0, 0, 0, 0.2);
}


.write_abstract {
  background: white;
  text-align: left;
  padding: 10px 20px;
  margin-bottom: 10px;
  border: 1px solid #ccc;
  box-shadow: 2px 2px 2px 1px rgba(0, 0, 0, 0.2);
  border-radius: 10px;
}

.write_abstract h4 {
  line-height: 45px;
  color: #8f8f8f;
}

.write_abstract p {
  line-height: 35px;
  color: #8f8f8f;
  font-size: 12px;
}


:deep(.el-input-group__append){
  width: 70px;
}

/* 富文本编辑器样式 */
:deep(.w-e-bar) {
  background-color: #fff;
  color: #8b8b8b;
  font-size: 15px;
  padding: 15px 5px;
  border-bottom: 1px solid #ececec;
}

:deep(.w-e-bar-divider) {
  background-color: white;
}

:deep(.w-e-bar svg) {
  fill: #8b8b8b;
}

:deep(.w-e-bar-item button) {
  color: #8b8b8b;
}

:global(.w-e-full-screen-container) {
  z-index: 999 !important;
}

:deep(.w-e-bar-item .active) {
  background-color: #e8e8e8;
}
</style>
