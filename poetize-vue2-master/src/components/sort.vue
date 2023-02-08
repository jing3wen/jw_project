<template>
  <div>
    <!-- 两句诗 -->
    <div class="my-animation-slide-top">
      <twoPoem></twoPoem>
    </div>

    <div style="background: var(--background);padding-top: 40px;" class="my-animation-slide-bottom">
      <!-- 标签 -->
      <div class="sort-warp shadow-box" v-if="!$common.isEmpty(categoryList) && !$common.isEmpty(categoryId)">
        <div v-for="(category, index) in categoryList" :key="index"
             :class="{isActive: !$common.isEmpty(categoryId) && parseInt(categoryId) === category.categoryId}"
             @click="listArticle(category, null)">
          <proTag :info="category.categoryName+' （'+category.articleCounts+'篇文章）'"
                  :color="$constant.before_color_list[Math.floor(Math.random() * 6)]"
                  style="margin: 12px">
          </proTag>
        </div>
      </div>
      <div class="sort-warp shadow-box" v-if="!$common.isEmpty(tagList) && !$common.isEmpty(tagId)" style="margin-top:20px">
        <div v-for="(tag, index) in tagList" :key="index"
             :class="{isActive: !$common.isEmpty(tagId) && parseInt(tagId) === tag.tagId}"
             @click="listArticle(null, tag)">
          <proTag :info="tag.tagName+' （'+tag.articleCounts+'篇文章）'"
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
        categoryId: this.$route.query.sortId,
        tagId: this.$route.query.labelId,
        categoryList: null,
        tagList:null,
        pagination: {
          pageNum: 1,
          pageSize: 10,
          categoryId: this.$route.query.sortId,
          tagId: this.$route.query.labelId,
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
          categoryId: this.$route.query.sortId,
          tagId: this.$route.query.labelId,
          keywords: "",
        };
        this.articles.splice(0, this.articles.length);
        this.categoryId = this.$route.query.sortId;
        this.tagId = this.$route.query.labelId;
        this.getCategory();
        this.getArticles();
      }
    },

    created() {
      if(!this.$common.isEmpty(this.categoryId)) {
        this.getCategory();
      }
      if(!this.$common.isEmpty(this.tagId)) {
        this.getTag()
      }
      this.getArticles();
    },

    mounted() {
    },

    methods: {
      pageArticles() {
        this.pagination.pageNum = this.pagination.pageNum + 1;
        this.getArticles();
      },

      getCategory() {
        // TODO 此处可以参考博客2.0用缓存
        // let sortInfo = this.$store.state.sortInfo;
        // if (!this.$common.isEmpty(sortInfo)) {
        //   let sortArray = sortInfo.filter(f => {
        //     return f.id === parseInt(this.sortId);
        //   });
        //   if (!this.$common.isEmpty(sortArray)) {
        //     this.sort = sortArray[0];
        //   }
        // }
        this.$http.get("http://localhost:9090/blogCategory/front/getAllCategory")
          .then((res) => {
            if (!this.$common.isEmpty(res.data)) {
              this.categoryList = res.data;
            }
          })
          .catch((error) => {
            this.$message({
              message: error.message,
              type: "error"
            });
          });

      },
      getTag() {
        // TODO 此处可以参考博客2.0用缓存
        // let sortInfo = this.$store.state.sortInfo;
        // if (!this.$common.isEmpty(sortInfo)) {
        //   let sortArray = sortInfo.filter(f => {
        //     return f.id === parseInt(this.sortId);
        //   });
        //   if (!this.$common.isEmpty(sortArray)) {
        //     this.sort = sortArray[0];
        //   }
        // }
        this.$http.get("http://localhost:9090/blogTag/front/getAllFrontTag")
          .then((res) => {
            if (!this.$common.isEmpty(res.data)) {
              this.tagList = res.data;
            }
          })
          .catch((error) => {
            this.$message({
              message: error.message,
              type: "error"
            });
          });

      },
      listArticle(category, tag) {
        if(!this.$common.isEmpty(category)) {
          this.categoryId = category.categoryId;
        }
        if (!this.$common.isEmpty(tag)){
          this.tagId = tag.tagId;
        }
        this.pagination = {
          pageNum: 1,
          pageSize: 10,
          categoryId: this.categoryId,
          tagId: this.tagId,
          keywords: "",
        };
        this.articles.splice(0, this.articles.length);
        this.$nextTick(() => {
          this.getArticles();
        });
      },
      getArticles() {
        this.total = 0
        this.$http.post("http://localhost:9090/blogArticle/front/getFrontArticlePage", this.pagination)
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
