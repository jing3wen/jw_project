<template>
  <el-row :gutter="20">
    <el-col :xs="0" :sm="1" :md="1" :lg="3" :xl="4">
      <!-- 左侧间隙 -->
    </el-col>
    <el-col :xs="24" :sm="22" :md="22" :lg="18" :xl="16">
      <el-row :gutter="20">
        <el-col :xs="24" :sm="24" :md="18" :lg="18" :xl="18">

          <!-- 文章内容 -->
          <div class="articleBox">
            <!-- 文章头部 -->
            <div class="articleHead">
              <p class="round"></p>
              <h2>{{articleInfo.articleTitle}}</h2>
              <p class="articleInfo">
                <!-- 作者 -->
                <span><el-icon><User /></el-icon>
                  {{articleInfo.userName}}
                </span>
                <!-- 时间 -->
                <span><el-icon><Timer /></el-icon>
                  {{articleInfo.publishTime}}
                </span>
                <!-- 分类 -->
                <span><el-icon><FolderOpened /></el-icon>
                  {{articleInfo.articleClassifyName}}
                </span>
                <!-- 浏览量 -->
                <span>
                  <el-icon><View /></el-icon>
                  {{articleInfo.click}}
                </span>
              </p>
            </div>
            <!-- 文章内容 -->
            <div class="articleBody">
              <div v-highlight v-html="articleInfo.articleContent" class="article"></div>
              <!--       //TODO: 高亮自定义指令  标签注释掉         -->
              <!-- <div v-html="articleInfo.articleContent" class="article"></div> -->

            </div>
            <!-- 文章尾部 -->
            <div class="articleFloot">
              <p>
                版权声明：本站所提供的图文等内容部分源于网络，仅供学习参考，如有侵权，联系删除。转载本站内容请注明相关出处。
              </p>
              <p>
                本文链接：<a :href="wPath">{{wPath}}</a>
              </p>
            </div>
          </div>

          <!-- 评论区 判断作者是否开启评论 -->
          <CommentSection v-if="articleInfo.commentState === 1" :f_articleInfo="articleInfo"></CommentSection>
        </el-col>
        <el-col class="hidden-sm-and-down" :md="6" :lg="6" :xl="6">
          <!-- 其他内容 -->

          <!-- 搜索功能 -->
          <SearchCard></SearchCard>
          <!-- Featured精选 -->
          <Featured></Featured>
        </el-col>
      </el-row>
    </el-col>
    <el-col :xs="0" :sm="1" :md="1" :lg="3" :xl="4">
      <!-- 右侧间隙 -->
    </el-col>
  </el-row>
</template>

<script>
import SearchCard from "@/components/searchCard/SearchCard";
import Featured from "@/components/featured/Featured";
import CommentSection from "../../components/commentSection/CommentSection";
import {formatDate} from "../../utils/common";
import {useRoute} from "vue-router";

export default {
  name: "ArticleDetails",
  components:{SearchCard, Featured, CommentSection},
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
      //TODO 文章浏览量+1，可以由后端业务代码完成
      // 点击的文章浏览量+1
      this.request.get("/api/article/updateArticleClick", {params}).then(res => {

      })
      //查询该文章数据
      this.request.get("http://localhost:80/article/showArticleInfo", {params}).then(res => {
        // 查询的文章数据
        let article = res[0];

        // 网页标题 = 当前文章标题
        document.title = article.articleTitle
        // 时间戳格式化
        let time = article.publishTime; // 当前发布文章的时间戳
        const date = new Date(time); // 初始化日期
        const month = date.getMonth() + 1; // 获取月份
        const day = date.getDate(); // 获取具体日
        article.publishTime = formatDate(time) +
            ("(" +
                (month < 10 ? ("0" + month) : month) // 三元表达式 日期小于10加上0 例如01
                +
                "-" + day + ")"); // 简化时间 输出格式例如 3天前(7.28)
        // 图片 根url
        const url = process.env.VUE_APP_URL;
        // 缩略图 判断是点击上传的还是，网络图片
        if (article.articleImgLitimg !== "" && !article.articleImgLitimg.includes('http') && !article.articleImgLitimg.includes('https')) {
          article.articleImgLitimg = url + article.articleImgLitimg
        }
        this.articleInfo = article
      })

    },



  }
}
</script>

<style scoped>
.articleBox {
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
</style>
