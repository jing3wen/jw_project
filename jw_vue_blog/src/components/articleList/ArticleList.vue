<template>
  <div class="boutique_article">
    <div class="titleBox">
      <h3>最新文章</h3>
      <p>包含本站所有分类文章，提供技术、资源、生活等文章，阅读愉快~</p>
    </div>
    <div class="contentBox" v-loading="loading" element-loading-text="玩命加载中...">
      <div class="titleWrapper">
        <h3 class="title"><b>最新</b></h3>
      </div>
      <el-card class="articleBox card-hover-transition" v-for="item in articleList" :key="item.articleId">
        <a :href="'/articleDetails/'+item.articleId">
          <h4 class="title">
            <span>{{item.articleTitle}}</span>
          </h4>
          <div class="article">
            <img v-if="item.articleCover" class="focus" :src="item.articleCover" :alt="item.articleTitle">
            <!-- 内容样式根据有没有图片改变 -->
            <div class="textBox" :class="[item.articleCover ? '':'textBox2']">
              <p style="margin-bottom: 25px;">{{item.articleSummary}}</p>
              <!--<p>Bpvank · 11月前 · 分类</p>-->
              <p class="hidden-sm-and-up">{{item.nickName}} · {{item.createTime}} ·
                {{item.categoryName}}</p>
              <p class="articleMessage hidden-xs-only">
                <el-tag v-if="item.isTop === '1'">
                  <span style="font-weight:bold; color: red;">
                    <el-icon><CollectionTag /></el-icon>顶置
                  </span>
                </el-tag>
                <span>
                  <el-icon><User /></el-icon>
                  {{item.nickName}}
                </span>
                <span>
                  <el-icon><FolderOpened /></el-icon>
                  {{item.categoryName}}
                </span>
                <span>
                  <el-icon><Calendar /></el-icon>
                  {{item.createTime}}
                </span>
                <span>
                  <el-icon><View /></el-icon>
                  {{item.viewCounts}}
                </span>
              </p>
            </div>
          </div>
        </a>
      </el-card>

      <!-- 封装的分页组件 -->
      <Pagination
          :total="total"
          v-model:f_pageNum="queryArticlePage.pageNum"
          v-model:f_pageSize="queryArticlePage.pageSize"
          @pageList = "getPageList">
      </Pagination>
    </div>
  </div>
</template>

<script>
import {getYearMonthDay} from "../../utils/common";
import Pagination from "../pagination/Pagination";

export default {
  name: "Article",
  components: {Pagination},
  data() {
    return{
      // 展示文章信息
      articleList: [],

      queryArticlePage:{
        pageNum: 0, //当前页码
        pageSize: 5, //每页显示行数
      },
      total: 0, //总条数
      loading:false,

    }
  },
  created() {
    this.getPageList()
  },
  methods: {
    getPageList(){
      this.loading = true;
      this.request.post("/api/blogArticle/front/getBlogFrontArticlePage", this.queryArticlePage).then(res => {
        // 先清空数据
        this.articleList = [];
        res.data.records.forEach(element => {
          // 时间格式化, 后端传过来的时间yyyy-mm-dd hh-mm-ss -> yyyy-mm-dd
          element.createTime = getYearMonthDay(element.createTime);
          this.articleList.push(element)
        });
        //JSON.parse 将从后台得到的数据转换为标准JSON格式
        //前台展示的是需要数组，JSON.parse转换后的数据，element-plus可以解析
        this.queryArticlePage.pageNum = res.data.current;
        this.queryArticlePage.pageSize = res.data.size;
        this.total = res.data.total;
        this.loading = false
      })
    }
  }
}
</script>

<style scoped>
.boutique_article .titleBox {
  width: 100%;
  background-color: white;
  padding: 20px;
  /* 添加此属性 padding间距不扩大div */
  box-sizing: border-box;
  border-radius: 10px;
  margin-bottom: 20px;
}

.boutique_article .titleBox h3 {
  font-size: 20px;
  font-weight: 700;
  /* padding-left: 10px; */
  margin-bottom: 10px;
  /* border-left: 4px solid #2FA7B9; */
  color: #474749;
}

.boutique_article .titleBox p {
  color: #666;
}

.boutique_article .contentBox {
  width: 100%;
  padding: 20px;
  /* 添加此属性 padding间距不扩大div */
  box-sizing: border-box;
  background: white;
  border-radius: 20px;
}

.boutique_article .titleWrapper {
  /* margin-bottom: 15px; */
  margin-top: -10px;
  border-bottom: 1px solid #eee;
  overflow: hidden;
  display: flex;
  justify-content: space-between;
}

.boutique_article .titleWrapper .title {
  line-height: 40px;
  color: #454545;
}

.boutique_article .titleWrapper .title b {
  display: inline-block;
  border-bottom: 4px solid #2FA7B9;
}


/* 文章样式 */
.boutique_article .articleBox {
  border-radius: 20px;
  padding: 20px 0;
  background-color: white;
  border-bottom: 1px solid #eee;
  margin-top: 12px;
}

.boutique_article .articleBox .title {
  font-size: 18px;
  line-height: 45px;
  color: #454545;
  overflow: hidden;
  -webkit-box-orient: vertical;
  display: -webkit-box;
  -webkit-line-clamp: 1;
  text-align: justify;
}

.boutique_article .articleBox .title:hover {
  color: #2FA7B9;
}

.boutique_article .article {
  display: flex;
  justify-content: space-between;
  font-size: 12px;
  color: #999;
}

.boutique_article .article img {
  width: 140px;
  height: 90px;
  border-radius: 4px;
  object-fit: cover;
  overflow: hidden;
}

.boutique_article .articleMessage span {
  font-size: 13px;
  color: #999;
  line-height: 20px;
  margin-right: 15px;
  display: inline-flex;
  align-items: center;
}

.boutique_article .articleMessage .el-icon {
  font-size: 15px;
  margin-right: 8px;
}

.boutique_article .textBox {
  width: 750px;
  /* height: 100px; */
  line-height: 22px;
  font-size: 13px;
  color: #999;
  margin-left: 10px;
}

.boutique_article .textBox2 {
  width: auto;
  margin-left: 0px;
  overflow: hidden;
}

.boutique_article .textBox p {
  color: #999;
  margin-bottom: 15px;
  -webkit-box-orient: vertical;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  text-align: justify;
  cursor: pointer;
  overflow: hidden;
}

@media screen and (max-width: 768px) {
  .boutique_article .textBox {
    /* width: 500px; */
  }
}



:deep(.el-table .cell) {
  -webkit-box-orient: vertical;
  display: -webkit-box;
  -webkit-line-clamp: 2;
}

@media screen and (max-width: 500px) {
  .hidePaging {
    display: none;
  }
}

@media screen and (min-width: 500px) {
  .showPaging {
    display: none;
  }
}
</style>
