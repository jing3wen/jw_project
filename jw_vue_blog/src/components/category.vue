<template>
  <div>
    <!-- 两句诗 -->
    <div class="my-animation-slide-top">
      <twoPoem></twoPoem>
    </div>
    <div style="background: var(--background);padding-top: 40px;" class="my-animation-slide-bottom">
      <!-- 类别 -->
      <div class="sort-warp shadow-box" v-if="!$common.isEmpty(categoryList)">
        <div v-for="(category, index) in categoryList" :key="index"
             :class="{isActive: !$common.isEmpty(categoryId) && parseInt(categoryId) === category.categoryId}"
             @click="listArticle(category, null)">
          <proTag :info="category.categoryName+' （'+category.articleCounts+'篇文章）'"
                  :color="$constant.before_color_list[Math.floor(Math.random() * 6)]"
                  style="margin: 12px">
          </proTag>
        </div>
      </div>

      <!-- 文章 -->
      <div class="article-wrap">
        <articleList :articleList="articles"></articleList>
        <div class="pagination-wrap">
          <div @click="pageArticles()" class="pagination" v-if="total !== articles.length">
            下一页
          </div>
          <div v-else style="user-select: none">
            ~~到底啦~~
          </div>
        </div>
      </div>
      <!-- 页脚 -->
      <myFooter></myFooter>
    </div>
  </div>
</template>

<script>
  const twoPoem = () => import( "./common/twoPoem");
  const proTag = () => import( "./common/proTag");
  const articleList = () => import( "./articleList");
  const myFooter = () => import( "./common/myFooter");

  export default {
    components: {
      twoPoem,
      proTag,
      articleList,
      myFooter
    },

    data() {
      return {
        categoryId: this.$route.query.categoryId,
        categoryList: null,
        tagList:null,
        pagination: {
          pageNum: 1,
          pageSize: 10,
          categoryId: this.$route.query.categoryId,
          keywords: "",
        },
        total: 0,
        articles: []
      }
    },

    computed: {},

    watch: {
      $route() {
        this.pagination = {
          pageNum: 1,
          pageSize: 10,
          categoryId: this.$route.query.categoryId,
          keywords: "",
        };
        this.articles.splice(0, this.articles.length);
        this.categoryId = this.$route.query.categoryId;
        this.getCategory();
        this.getArticles();
      }
    },

    created() {
      this.getCategory();
      this.getArticles();
    },

    mounted() {
    },
    destroyed() {
      this.categoryId = ''
    },
    methods: {
      pageArticles() {
        this.pagination.pageNum = this.pagination.pageNum + 1;
        this.getArticles();
      },

      getCategory() {
        // 原博客是只显示该文章的类别
        this.categoryList = this.$store.state.categoryList;
      },
      listArticle(category, tag) {
        if(!this.$common.isEmpty(category)) {
          this.categoryId = category.categoryId;
        }
        this.pagination = {
          pageNum: 1,
          pageSize: 10,
          categoryId: this.categoryId,
          keywords: "",
        };
        this.articles.splice(0, this.articles.length);
        this.$nextTick(() => {
          this.getArticles();
        });
      },
      getArticles() {
        this.total = 0
        this.$http.post("/api/blogArticle/front/getFrontArticlePage", this.pagination)
          .then((res) => {
            if (!this.$common.isEmpty(res.data)) {
              this.articles = this.articles.concat(res.data.records);
              this.total = res.data.total;
            }
          })
          .catch((error) => {
            this.$message({
              message: error.message,
              type: "error"
            });
          });
      }
    }
  }
</script>

<style scoped>

  .sort-warp {
    width: 70%;
    max-width: 780px;
    margin: 0 auto;
    padding: 20px;
    border-radius: 10px;
    display: flex;
    flex-wrap: wrap;
  }

  .article-wrap {
    width: 70%;
    margin: 40px auto;
    min-height: 600px;
  }

  .isActive {
    animation: scale 1.5s ease-in-out infinite;
  }

  .pagination-wrap {
    display: flex;
    justify-content: center;
    margin-top: 40px;
  }

  .pagination {
    padding: 13px 15px;
    border: 1px solid var(--lightGray);
    border-radius: 3rem;
    color: var(--greyFont);
    width: 100px;
    user-select: none;
    cursor: pointer;
    text-align: center;
  }

  .pagination:hover {
    border: 1px solid var(--themeBackground);
    color: var(--themeBackground);
    box-shadow: 0 0 5px var(--themeBackground);
  }


  @media screen and (max-width: 900px) {
    .sort-warp {
      width: 90%;
    }

    .article-wrap {
      width: 90%;
    }
  }
</style>
