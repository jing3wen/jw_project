<template>
  <el-row :gutter="20">
    <el-col :xs="0" :sm="1" :md="1" :lg="3" :xl="4">
      <!-- 左侧间隙 -->
    </el-col>
    <el-col :xs="24" :sm="22" :md="22" :lg="18" :xl="16">
      <el-row :gutter="20">
        <el-col :xs="24" :sm="24" :md="18" :lg="18" :xl="18">
          <!-- 文章内容 -->
          <div class="articleDetailBox">
            <!-- 文章头部 -->
            <div class="articleHead">
              <p class="round"></p>
              <h2>{{articleInfo.articleTitle}}</h2>
              <p class="articleInfo">
                <!-- 作者 -->
                <span><el-icon><User /></el-icon>
                  {{articleInfo.nickName}}
                </span>
                <!-- 时间 -->
                <span><el-icon><Calendar /></el-icon>
                  {{articleInfo.createTime}}
                </span>
                <!-- 分类 -->
                <span><el-icon><FolderOpened /></el-icon>
                  {{articleInfo.categoryName}}
                </span>
                <!-- 浏览量 -->
                <span>
                  <el-icon><View /></el-icon>
                  {{articleInfo.viewCounts}}
                </span>
              </p>
            </div>
            <!-- 文章内容 -->
            <div class="articleBody">
              <!--
              boxShadow="false"  关闭边框阴影
              previewBackground="transparent"  清楚背景颜色
              codeStyle="rainbow"   代码高亮风格rainbow
              :subfield="false"   只显示一个框
              :defaultOpen="'preview'"  只显示的框为预览框
              :toolbarsFlag="false"  关闭工具栏
              -->
              <mavon-editor
                  v-model="articleInfo.articleContent"
                  :boxShadow="false"
                  previewBackground="transparent"
                  codeStyle="rainbow"
                  :subfield="false"
                  :defaultOpen="'preview'"
                  :toolbarsFlag="false"
              />

            </div>
            <!-- 文章尾部 -->
            <div class="articleFloot">
              <p v-if="(articleInfo.updateTime)">
                作者最后更新时间: {{articleInfo.updateTime}}
              </p>
              <p>
                版权声明：本站所提供的图文等内容部分源于网络，仅供学习参考，如有侵权，联系删除。转载本站内容请注明相关出处。
              </p>
              <p>
                本文链接：<a :href="wPath">{{wPath}}</a>
              </p>
            </div>
          </div>

          <!-- 评论区 判断作者是否开启评论 -->
          <div v-if="articleInfo.commentAllowed === '1'">
            <CommentSection :f_articleInfo="articleInfo"></CommentSection>
          </div>
          <div v-else class="commentCloseBox" >
            <span>作者已关闭评论区</span>
          </div>
        </el-col>
        <el-col class="hidden-sm-and-down" :md="6" :lg="6" :xl="6">
          <!-- 其他内容 -->
          <!--&lt;!&ndash; 站点信息 &ndash;&gt;-->
          <WebsiteInfo></WebsiteInfo>
          <!-- 精选文章 -->
          <HotArticle></HotArticle>


        </el-col>
      </el-row>
    </el-col>
    <el-col :xs="0" :sm="1" :md="1" :lg="3" :xl="4">
      <!-- 右侧间隙 -->
    </el-col>
  </el-row>
</template>

<script>
import WebsiteInfo from "../../components/websiteInfo/WebsiteInfo";
import CommentSection from "../../components/comment/CommentSection";
import {getYearMonthDay} from "../../utils/common";
import {useRoute} from "vue-router";
import HotArticle from "../../components/hotArticle/HotArticle";


export default {
  name: "ArticleDetails",
  components:{HotArticle, CommentSection, WebsiteInfo},
  data() {
    return{
      articleInfo: {},
      wPath: window.document.location.href
    }
  },
  created(){
    this.loadData()
  },
  methods: {
    /**
     * @desc 加载获取数据
     */
    loadData(){
      // router是全局路由对象，route= userRoute()是当前路由对象
      // let route = useRoute();
      const params = {
        // route.params.id 跳转到当前路由携带着文章id
        articleId: useRoute().params.id
      }

      //查询该文章数据
      this.request.get("/api/blogArticle/front/getBlogFrontArticleDetails", {params}).then(res => {

        // 查询的文章数据
        let article = res.data;

        if(!this.verifyArticleVisible(article)){
          this.$notify({
            title: '您无权查看该文章',
            message: '该文章作者设置为仅自己可见',
            type: 'error',
            position: 'top-left',
            offset: 100,
          })
          return
        }
        // 网页标题 = 当前文章标题
        document.title = article.articleTitle
        // 时间格式化
        article.createTime = getYearMonthDay(article.createTime)

        this.articleInfo = article
      })

    },
    verifyArticleVisible(article){
      //TODO 验证当前文章是否只有自己能查看
      //当前文章所有人可看
      if(article.articleVisible === '1'){
        return true
      }
      //当前文章只有自己（或管理员）能查看
      let loginUserId = this.$store.state.currentLoginUser.userId
      let loginRoleList = this.$store.state.currentLoginUser.roleList
      if(loginUserId === article.userId || loginRoleList.includes('admin')){
          return true
      }
      return false
    },

  }
}
</script>

<style scoped>
.articleDetailBox {
  height: auto;
  overflow: hidden;
  background-color: white;
  padding: 25px;
  line-height: 30px;
  color: #474749;
  border-radius: 10px;
  margin-bottom: 20px;
}

/** 3个点 */
.round {
  width: 12px;
  height: 12px;
  background-color: #fc625d;
  border-radius: 16px;
  box-shadow: 20px 0 #fdbc40, 40px 0 #35cd4b;
  margin-bottom: 10px;
}

.articleHead {
  border-bottom: #dbdbdb 1px dashed;
  margin-bottom: 20px;
}

.articleHead h2 {
  line-height: 35px;
}

.articleInfo {
  line-height: 45px;
  color: #888;
  font-size: 13px;
}

.articleInfo span {
  margin-right: 20px;
  display: inline-flex;
  align-items: center;
}

.articleInfo .el-icon {
  margin-right: 8px;
}


/* 尾部样式 */
.articleFloot {
  margin: 20px 0;
  border-left: 5px solid #2fa7b9;
  padding: 10px 15px;
  background: #f8f8f8;
  font-size: 14px;
  color: #888;
  line-height: 22px;
  box-sizing: border-box;
  overflow: hidden;
  align-items: center;
}

.commentCloseBox {
  height: auto;
  overflow: hidden;
  background-color: white;
  padding: 25px;
  color: #474749;
  border-radius: 10px;
  margin-bottom: 20px;
  text-align: center;
  font-weight: bolder;
}
</style>
