<template>
  <div class="articleCategoriesBox card-hover-transition" v-loading="loading" element-loading-text="玩命加载中...">
    <div class="articleCategories_title">
      <h3 class="title"><b>文章分类</b></h3>
    </div>
    <div class="articleCategories_content">
      <ul>
        <li v-for="item in categoryList">
          <el-icon>
            <ArrowRight />
          </el-icon>
          <a :title="item.categoryName" :href="'/classify/'+item.categoryId">{{item.categoryName}}</a>
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
export default {
  name: "ArticleCategories",
  data(){
    return{
      categoryList:[],
      loading: false,
    }
  },
  created() {
    this.loadData()
  },
  methods: {
    loadData(){
      this.loading = true;
      this.request.get("http://localhost:9090/blogCategory/front/getAllCategory").then(res => {
        this.categoryList = res.data
        this.loading = false;
      })
}
  }
}
</script>

<style scoped>
.articleCategoriesBox {
  background: white;
  height: auto;
  border-radius: 10px;
  margin-bottom: 20px;
  padding: 20px;
}

.articleCategories_title {
  margin-bottom: 15px;
  margin-top: -10px;
  display: flex;
  justify-content: space-between;
  border-bottom: 1px solid rgb(238, 238, 238);
  overflow: hidden;
}

.articleCategories_title .title {
  line-height: 40px;
  color: #474749;
}

.articleCategories_title .title b {
  display: inline-block;
  border-bottom: 4px solid #2fa7b9;
}

.articleCategories_content ul {
  overflow: hidden;
  zoom: 1;
}

.articleCategories_content li {
  height: 36px;
  line-height: 36px;
  border-bottom: 1px dotted #e4e8eb;
  white-space: nowrap;
  text-overflow: ellipsis;
  overflow: hidden;
  width: 50%;
  float: left;
  display: inline-flex;
  align-items: center;
}

.articleCategories_content li .el-icon {
  margin-right: 8px;
  font-size: 12px;
  color: grey;
}
</style>
