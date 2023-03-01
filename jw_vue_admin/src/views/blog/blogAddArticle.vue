<template>
  <el-card>
    <h3 class="add-article-top">
      <el-icon style="margin-right: 10px;" name="edit"></el-icon>
      新增文章
    </h3>

    <!-- 文章详情 -->
    <el-card>
      <h3>文章详情:</h3>
      <el-form ref="articleForm" :model="article" :rules="articleFormRules" label-width="100px" class="mt-10">
        <el-row :gutter="15">
          <el-col :span="24">
            <el-form-item label="标题:" prop="articleTitle">
              <el-input v-model="article.articleTitle" placeholder="请输入标题" :maxlength="100" show-word-limit
                        clearable :style="{width: '100%'}"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="简介:" prop="articleSummary">
              <el-input v-model="article.articleSummary" type="textarea" placeholder="请输入简介" :maxlength="300"
                        show-word-limit :autosize="{minRows: 4, maxRows: 4}" :style="{width: '100%'}"></el-input>
            </el-form-item>
          </el-col>
          <!-- 文章类别 -->
          <el-col :span="24">
            <el-form-item label="文章分类:" prop="categoryId">
              <el-tag
                  type="success"
                  v-show="article.categoryId"
                  style="margin:0 1rem 0 0"
                  :closable="true"
                  @close="removeCategory"
              >
                {{ article.categoryName }}
              </el-tag>
              <!-- 分类选项 -->
              <el-popover placement="bottom-start" width="460" trigger="click" v-if="!article.categoryId">
                <div class="popover-title">分类</div>
                <!-- 搜索框 -->
                <el-autocomplete
                    style="width:100%"
                    v-model="searchCategoryName"
                    :fetch-suggestions="searchCategory"
                    placeholder="请输入分类名搜索，enter可添加自定义分类"
                    :trigger-on-focus="false"
                    @keyup.enter.native="addCategory"
                    @select="selectCategory">
                  <template slot-scope="{ item }">
                    <div>{{ item.categoryName }}</div>
                  </template>
                </el-autocomplete>
                <!-- 分类 -->
                <div class="popover-container">
                  <div v-for="item of categoryList" :key="item.categoryId" class="category-item" @click="selectCategory(item)">
                    {{ item.categoryName }}
                  </div>
                </div>
                <el-button type="success" plain slot="reference">
                  添加分类
                </el-button>
              </el-popover>
            </el-form-item>
          </el-col>
          <!-- 文章标签 -->
          <el-col :span="24">
            <el-form-item label="文章标签:" prop="">
              <el-tag
                  v-for="(item, index) of article.tagNameList"
                  :key="index"
                  style="margin:0 1rem 0 0"
                  :closable="true"
                  @close="removeTag(item)">
                {{ item }}
              </el-tag>
              <!-- 分类选项 -->
              <el-popover placement="bottom-start" width="460" trigger="click" v-if="article.tagNameList.length < 3">
                <div class="popover-title">标签</div>
                <!-- 搜索框 -->
                <el-autocomplete
                    style="width:100%"
                    v-model="searchTagName"
                    :fetch-suggestions="searchTag"
                    placeholder="请输入标签名搜索，enter可添加自定义标签"
                    :trigger-on-focus="false"
                    @keyup.enter.native="addTag"
                    @select="selectTag">
                  <template slot-scope="{ item }">
                    <div>{{ item.tagName }}</div>
                  </template>
                </el-autocomplete>
                <!-- 标签 -->
                <div class="popover-container">
                  <div style="margin-bottom:1rem">添加标签</div>
                  <el-tag
                      v-for="(item, index) of tagList"
                      :key="index"
                      :class="tagClass(item)"
                      @click="selectTag(item)"
                  >
                    {{ item.tagName }}
                  </el-tag>
                </div>
                <el-button type="success" plain slot="reference">
                  添加标签
                </el-button>
              </el-popover>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="文章可见:" prop="articleVisible">
              <el-radio-group v-model="article.articleVisible">
                <el-radio :label="'1'">公开</el-radio>
                <el-radio :label="'0'">私密</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="允许评论:" prop="commentAllowed">
              <el-radio-group v-model="article.commentAllowed">
                <el-radio :label="'1'">开启</el-radio>
                <el-radio :label="'0'">关闭</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="文章封面:" prop="articleCover">
              <ImageVideoUpload
                  ref="image-upload"
                  :f_action.sync="action"
                  :f_file-url.sync="article.articleCover"
                  :f_remove-file-url="removeFileUrl">
              </ImageVideoUpload>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </el-card>

    <!-- 编辑器 -->
    <el-card class="mt-10">
      <div class="article-editor">
        <h3 style="flex: 1">文章内容:</h3>
        <el-button type="danger" plain class="ml-10"  v-if="recoverCacheArticleButton" @click="recoverCachedArticle">
          恢复上次编辑状态
        </el-button>
        <el-button type="primary" plain class="ml-10" @click="addArticle()">
          发布文章
        </el-button>
      </div>
      <mavon-editor
          v-model="article.articleContent"
          codeStyle="rainbow"
          ref="md"
          @imgAdd="uploadImage"
          @imgDel="deleteUploadImage"
          style="min-height: 600px"
      />
    </el-card>
  </el-card>

</template>

<script>
import { mavonEditor  } from "mavon-editor";
import "mavon-editor/dist/css/index.css";
import ImageVideoUpload from "../../components/file/upload/imageVideoUpload/ImageVideoUpload";

export default {
  name: "blogAddArticle",
  components: {mavonEditor, ImageVideoUpload},
  data() {
    return {
      // 文章表单
      article: {
        articleId: null,  //文章id
        userId: '',  //文章作者
        categoryId: '', // 文章类别id
        categoryName:'', //文章类别名
        tagIdList:[], //文章标签id
        tagNameList:[], //文章标签名称
        isTop:'',  //是否顶置
        articleCover: '', // 文章封面
        articleTitle: '', // 文章标题
        articleSummary: '', // 文章简介
        articleContent: '', // 文章内容
        articleVisible: '1', //文章是否可见（0代表仅自己可见，1表示所有人可见)
        commentAllowed: '1', // 文章是否允许评论(0表示不能评论，1表示可以评论)
        articleCheck: '1', //文章审核状态（1表示通过，0表示未通过
      },
      articleFormRules: {
        articleTitle: [
            {required: true, message: '请输入标题', trigger: 'blur'}
        ],
        articleSummary: [
            {required: true, message: '请输入简介', trigger: 'blur'
        }],
        categoryId: [
            {required: true, message: '请选择文章分类', trigger: 'change'}
        ],
      },
      // 文章类别列表
      categoryList: [],
      //搜索框中的类别名称
      searchCategoryName:'',
      //文章标签列表
      tagList: [],
      //搜索框中的类别名称
      searchTagName:'',
      //上传组件参数
      action:"/api/file/fileUpload/blog/article/cover",
      removeFileUrl:'/api/file/deleteUploadFile/blog/article/cover',
      //恢复缓存文章按钮
      recoverCacheArticleButton: false,

    };
  },
  computed: {
    tagClass() {
      return function(item) {
        const index = this.article.tagNameList.indexOf(item.tagName);
        return index !== -1 ? "tag-item-select" : "tag-item";
      };
    }
  },
  created() {
    //检查缓存
    this.checkCacheArticle()
    this.getAllCategory()
    this.getAllTag()
  },
  destroyed() {
    this.cacheArticleWhenChangeVue()
  },
  methods: {
    /**
     * 文章类别相关操作
     **/
    //获取所有文章分类
    getAllCategory(){
      this.request.get('/api/blogCategory/admin/getAllBlogCategoryList').then(res =>{
        if (res.code === 200){
          this.categoryList = res.data
        }else {
          this.$message.error(res.msg)
        }
      })
    },
    //移除选择的文章类别
    removeCategory(){
      this.article.categoryId = '';
      this.article.categoryName = '';
    },
    //选择文章类别
    selectCategory(item){
      this.article.categoryId = item.categoryId;
      this.article.categoryName = item.categoryName;
    },
    //按回车新增文章类别
    addCategory(){
      if(this.searchCategoryName.length === 0){
        this.$message.error('类别名为空')
      }else if(this.searchCategoryName.length > 50){
        this.$message.warning('类别名字数应小于50')
      }else{
        this.request.post('/api/blogCategory/admin/addBlogCategory', {categoryName: this.searchCategoryName}).then(res => {
          if (res.code === 200) {
            this.$message.success("新增类别成功")
            this.getAllCategory()
          } else this.$message.error(res.msg)
        })
      }
    },
    searchCategory(keyword, cb){
      this.request.get('/api/blogCategory/admin/searchBlogCategoryList',
          {params: {categoryName: keyword}}).then(res => {
        if (res.code === 200) {
         cb(res.data)
        } else this.$message.error(res.msg)
      })
    },

    //获取所有文章标签
    getAllTag(){
      this.request.get('/api/blogTag/admin/getAllBlogTagList').then(res =>{
        if (res.code === 200){
          this.tagList = res.data
        }else {
          this.$message.error(res.msg)
        }
      })
    },
    //移除选择的文章标签
    removeTag(item){
      const index = this.article.tagNameList.indexOf(item.tagName);
      this.article.tagIdList.splice(index, 1);
      this.article.tagNameList.splice(index, 1);
    },
    //选择文章标签
    selectTag(item){
      if(this.article.tagIdList.indexOf(item.tagId) === -1){
        this.article.tagIdList.push(item.tagId);
        this.article.tagNameList.push(item.tagName);
      }
    },
    //按回车新增文章类别
    addTag(){
      if(this.searchTagName.length === 0){
        this.$message.error('标签名为空')
      }else if(this.searchTagName.length > 50){
        this.$message.warning('标签名字数应小于50')
      }else{
        this.request.post('/api/blogTag/admin/addBlogTag', {tagName: this.searchTagName}).then(res => {
          if (res.code === 200) {
            this.$message.success("新增标签成功")
            this.getAllTag()
          } else this.$message.error(res.msg)
        })
      }
    },
    searchTag(keyword, cb){
      this.request.get('/api/blogTag/admin/searchBlogTagList',
          {params: {tagName: keyword}}).then(res => {
        if (res.code === 200) {
          cb(res.data)
        } else this.$message.error(res.msg)
      })
    },

    /**
     * 编辑器相关操作
     */
    // 将图片上传到服务器
    uploadImage(pos, file) {
      const formData = new FormData();
      formData.append("file", file);
      //将下面上传接口替换为你自己的服务器接口
      this.request.post('/api/file/fileUpload/blog/article/context/image', formData).then(res=>{
        if (res.code === 200) {
          this.$message.success('上传成功')
          this.$refs.md.$img2Url(pos, res.data)
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    //删除上传的图片
    deleteUploadImage(pos){
      const fileUrl = pos[0];
      this.request.delete('/api/file/deleteUploadFile/blog/article/context/image', {data: fileUrl}).then(res=>{
        if (res.code === 200) {
          this.$message.info('已删除上传图片')
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    //查看是否有缓存数据
    checkCacheArticle(){
      const article = sessionStorage.getItem('add-article')
      if (article){
        this.recoverCacheArticleButton = true
      }else {
        this.recoverCacheArticleButton = false
      }
    },
    //恢复缓存状态, 移除缓存
    recoverCachedArticle(){
      const article = sessionStorage.getItem('add-article')
      this.$notify({
        title: '提示',
        message: '已自动恢复到上次编辑状态',
        type:'success'
      });
      this.article = JSON.parse(article)
      sessionStorage.removeItem('add-article')
      this.checkCacheArticle()
    },
    //当摧毁组件时判断页面是否已经写入数据，若写入就缓存该数据, 始终存储最近一次的缓存版本
    cacheArticleWhenChangeVue() {
      if (!(this.article.articleId === null &&
          this.article.userId === '' &&
          this.article.categoryId === '' &&
          this.article.tagIdList.length ===0 &&
          this.article.isTop === '' &&
          this.article.articleCover === '' &&
          this.article.articleTitle === '' &&
          this.article.articleSummary === '' &&
          this.article.articleContent === ''  &&
          this.article.articleVisible === '1' &&
          this.article.commentAllowed === '1' &&
          this.article.articleCheck === '1')) {
        this.$notify({
          title: '提示',
          message: '已自动缓存数据',
          type: 'success'
        });
        sessionStorage.setItem('add-article', JSON.stringify(this.article))
      }
    },
    addArticle() {
      if(this.article.tagIdList.length===0){
        this.$message.error("请选择文章标签")
        return false;
      }
      this.$refs['articleForm'].validate((valid) => {
        if(valid){
          // 添加文章
          this.article.articleCheck = this.$store.state.blogWeb.articleCheck
          this.article.userId = this.$store.state.user.currentLoginUser.id
          this.request.post("/api/blogArticle/admin/addBlogArticle", this.article).then((res) => {
            if (res.code === 200) {
              //重置编辑器表单
              this.$notify({
                title: '提示',
                message: this.article.articleCheck === '1' ? "文章发布成功（已开启默认审核通过）" :
                    "文章提交成功，等待管理员审核",
                type: 'success',
              })
              //任务已提交，清空缓存
              sessionStorage.removeItem('add-article')
              this.$store.commit('layout/removeTab', '新增文章')
              this.resetWriteForm()
              this.$router.push('/blog/blogArticle')
            }
          })
        }else {
          return false
        }
      })
    },
    //重置表单, 重置验证状态
    resetWriteForm(){
      //重置验证状态
      if(this.$refs['articleForm']){
        this.$refs['articleForm'].resetFields()
      }
      //重置表单
      this.article = this.$options.data().article
    },
  }
}
</script>

<style scoped>
.add-article-top {

  margin-bottom:10px;
  padding: 20px 20px;
  background-color: white;
  box-shadow: 2px 2px 2px 1px rgba(0, 0, 0, 0.2);
}
.article-editor {
  display: flex;
  margin-bottom: 10px;
}
.category-item {
  cursor: pointer;
  padding: 0.6rem 0.5rem;
}
.category-item:hover {
  background-color: #f0f9eb;
  color: #67c23a;
}
.popover-title {
  margin-bottom: 1rem;
  text-align: center;
}
.popover-container {
  margin-top: 1rem;
  height: 260px;
  overflow-y: auto;
}

.tag-item {
  margin-right: 1rem;
  margin-bottom: 1rem;
  cursor: pointer;
}
.tag-item-select {
  margin-right: 1rem;
  margin-bottom: 1rem;
  cursor: not-allowed;
  color: #ccccd8 !important;
}

</style>
