<template>
  <div class="boutique_article">
    <div class="titleBox">
      <h3>精品文章</h3>
      <p>包含本站所有分类文章，提供技术、资源、生活等文章，阅读愉快~</p>
    </div>
    <div class="contentBox" v-loading="loading" element-loading-text="玩命加载中...">
      <div class="titleWrapper">
        <h3 class="title"><b>最新</b></h3>
      </div>
      <div class="articleBox" v-for="item in articleInfo">
        <a :href="'/articleDetails/'+item.articleId">
          <h4 class="title"><span>{{item.articleTitle}}</span></h4>
          <div class="article">
            <img v-if="item.articleImgLitimg" class="focus" :src="item.articleImgLitimg"
                 :alt="item.articleTitle">
            <!-- 内容样式根据有没有图片改变 -->
            <div class="textBox" :class="[item.articleImgLitimg ? '':'textBox2']">
              <p style="margin-bottom: 25px;">{{item.articleDase}}</p>
              <!--<p>Bpvank · 11月前 · 分类</p>-->
              <p class="hidden-sm-and-up">{{item.userName}} · {{item.publishTime}} ·
                {{item.articleClassifyName}}</p>
              <p class="articleMessage hidden-xs-only">
                <span>
                  <el-icon><User /></el-icon>
                  {{item.userName}}
                </span>
                <span>
                  <el-icon><FolderOpened /></el-icon>
                  {{item.articleClassifyName}}
                </span>
                <span>
                  <el-icon><Timer /></el-icon>
                  {{item.publishTime}}
                </span>
                <span>
                  <el-icon><View /></el-icon>
                  {{item.click}}
                </span>
              </p>
            </div>
          </div>
        </a>
      </div>
      <!--
    total 总行数
    page-size	每页显示条目个数
    current-change 改变页码时触发
    currentPage:当前页码
 -->
<!--      <el-pagination class="showPaging" background layout="prev, pager, next" :total="total" :page-size="pageSize"-->
<!--                     :disabled="disabled" @current-change="changePage" v-model:currentPage="currentPage"-->
<!--                     @size-change="handleSizeChange" :pager-count="3" :hide-on-single-page="true"  />-->
<!--      <el-pagination class="hidePaging" background layout="prev, pager, next, jumper" :total="total"-->
<!--                     :page-size="pageSize" :disabled="disabled" @current-change="changePage"-->
<!--                     v-model:currentPage="currentPage" @size-change="handleSizeChange" :pager-count="3" :hide-on-single-page="true" />-->
    </div>
  </div>
</template>

<script>
import {formatDate} from "../../utils/common";

export default {
  name: "Article",
  data() {
    return{
      // 展示文章信息
      articleInfo: [],
      total: 0, //总条数
      pageSize: 10, //每页显示行数
      currentPage: 1, //当前页码
      loading:false
    }
  },
  created() {
    this.loadData()
  },
  methods: {
    loadData(){
      this.loading = true;

      var params = {
        'currentPage': this.currentPage,
        'pageSize': this.pageSize,
      }
      this.request.get("/api/article/page/showAll", {params}).then(res => {
        // 先清空数据
        this.articleInfo = [];
        res.data.list.forEach(element => {
          // 时间戳格式化
          element.publishTime = formatDate(element.publishTime);
          // 图片 根url
          const url = process.env.VUE_APP_URL;
          // 缩略图 判断是点击上传的还是，网络图片
          if (element.articleImgLitimg != "" && !element.articleImgLitimg.includes('http') && !element.articleImgLitimg.includes('https')) {
            element.articleImgLitimg = url + element.articleImgLitimg
          }
          // 添加
          this.articleInfo.push(element)
        });
        //JSON.parse 将从后台得到的数据转换为标准JSON格式
        //前台展示的是需要数组，JSON.parse转换后的数据，element-plus可以解析
        // state.tableData = res.data.data.list;
        this.total = res.data.total;
        this.pageSize = res.data.pageSize;
        this.currentPage = res.data.currentPage;
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

/* 分页样式 */
:deep(.el-pagination.is-background .el-pager li:not(.is-disabled).is-active) {
  background-color: #2fa7b9;
}

.el-pagination {
  margin-top: 20px;
  justify-content: center;
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
