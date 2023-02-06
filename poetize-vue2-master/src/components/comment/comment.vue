<template>
  <div>
    <!-- 评论框 -->
    <div style="margin-bottom: 40px">
      <div class="comment-head">
        <i class="el-icon-edit-outline" style="font-weight: bold;font-size: 22px;"></i> 留言
      </div>
      <div>
        <!-- 文字评论 -->
        <div v-show="!isGraffiti">
          <commentBox @showGraffiti="isGraffiti = !isGraffiti"
                      @submitComment="submitComment">
          </commentBox>
        </div>
        <!-- 画笔 -->
<!--        <div v-show="isGraffiti">-->
<!--          <graffiti @showComment="isGraffiti = !isGraffiti"-->
<!--                    @addGraffitiComment="addGraffitiComment">-->
<!--          </graffiti>-->
<!--        </div>-->
      </div>
    </div>

    <!-- 评论内容 -->
    <div v-if="comments.length > 0">
      <!-- 评论数量 -->
      <div class="commentInfo-title">
        <span style="font-size: 1.15rem">Comments | </span>
        <span>{{ total }} 条留言</span>
      </div>
      <!-- 评论详情 -->
      <div id="comment-content" class="commentInfo-detail"
           v-for="(item, index) in comments"
           :key="index">
        <!-- 头像 -->
        <el-avatar shape="square" class="commentInfo-avatar" :size="35" :src="item.avatar"></el-avatar>

        <div style="flex: 1;padding-left: 12px">
          <!-- 评论信息 -->
          <div style="display: flex;justify-content: space-between">
            <div>
              <span class="commentInfo-username">{{ item.nickname }}</span>
              <span class="commentInfo-master" v-if="item.userId === userId">主人翁</span>
              <span class="commentInfo-other">{{ $common.getDateDiff(item.createTime) }}</span>
            </div>
            <div class="commentInfo-reply" @click="replyDialog(item, item)">
              <span v-if="item.replyCommentCounts > 0">{{item.replyCommentCounts}} </span><span>回复</span>
            </div>
          </div>
          <!-- 评论内容 -->
          <div class="commentInfo-content">
            <span v-html="item.commentContent"></span>
          </div>
          <!-- 回复模块 -->
          <div v-if="!$common.isEmpty(item.replyCommentList)">
            <div class="commentInfo-detail" v-for="(childItem, i) in item.replyCommentList" :key="i">
              <!-- 头像 -->
              <el-avatar shape="square" class="commentInfo-avatar" :size="30" :src="childItem.avatar"></el-avatar>

              <div style="flex: 1;padding-left: 12px">
                <!-- 评论信息 -->
                <div style="display: flex;justify-content: space-between">
                  <div>
                    <span class="commentInfo-username-small">{{ childItem.nickname }}</span>
                    <span class="commentInfo-master" v-if="childItem.userId === userId">主人翁</span>
                    <span class="commentInfo-other">{{ $common.getDateDiff(childItem.createTime) }}</span>
                  </div>
                  <div>
                    <span class="commentInfo-reply" @click="replyDialog(childItem, item)">回复</span>
                  </div>
                </div>
                <!-- 评论内容 -->
                <div class="commentInfo-content">
                  <template v-if="childItem.replyCommentId !== item.commentId &&
                                  childItem.replyUserId !== childItem.userId">
                    <span style="color: var(--blue)">@{{ childItem.replyNickname }} </span>:
                  </template>
                  <span v-html="childItem.commentContent"></span>
                </div>
              </div>
            </div>
            <!-- 分页 -->
            <div class="pagination-wrap" v-if="item.replyCommentList.length < item.replyCommentCounts">
              <div class="pagination"
                   @click="toChildPage(item)">
                展开
              </div>
            </div>
          </div>
        </div>
      </div>
      <!-- 分页 -->
      <proPage :current="pagination.pageNum"
               :size="pagination.pageSize"
               :total="floorCommentTotal"
               :buttonSize="6"
               :color="$constant.commentPageColor"
               @toPage="toPage">
      </proPage>
    </div>

    <div v-else class="myCenter" style="color: var(--greyFont)">
      <i>来发第一个留言啦~</i>
    </div>

    <el-dialog title="留言"
               :visible.sync="replyDialogVisible"
               width="30%"
               :before-close="handleClose"
               :append-to-body="true"
               destroy-on-close
               center>
      <div>
        <commentBox :disableGraffiti="true"
                    @submitComment="submitReply">
        </commentBox>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  // const graffiti = () => import( "./graffiti");
  const commentBox = () => import( "./commentBox");
  const proPage = () => import( "../common/proPage");

  export default {
    components: {
      // graffiti,
      commentBox,
      proPage
    },
    props: {
      source: {
        type: Number
      },
      type: {
        type: String
      },
      userId: {
        type: Number
      }
    },
    data() {
      return {
        isGraffiti: false,
        total: 0,
        replyDialogVisible: false,
        floorComment: {},
        replyComment: {},
        comments: [],
        pagination: {
          articleId: this.source,
          floorCommentId: 0,
          pageNum : 1,
          pageSize : 10,
        },
        floorCommentTotal:0,
        childCommentCurrent:1,
      };
    },

    computed: {},

    created() {
      this.getComments(this.pagination);
      this.getTotal();
    },
    methods: {
      toPage(page) {
        this.pagination.floorCommentId = 0;
        this.pagination.pageNum = page;
        window.scrollTo({
          top: document.getElementById('comment-content').offsetTop
        });
        this.getComments(this.pagination);
      },
      getTotal() {
        this.$http.get("http://localhost:9090/blogComment/front/getFrontCommentCounts", {articleId: this.source})
          .then((res) => {
            if (!this.$common.isEmpty(res.data)) {
              this.total = res.data;
            }
          })
          .catch((error) => {
            this.$message({
              message: error.message,
              type: "error"
            });
          });
      },
      toChildPage(floorComment) {
        floorComment.childComments.current += 1;
        let pagination = {
          current: floorComment.childComments.current,
          size: 5,
          articleId: this.source,
          floorCommentId: floorComment.commentId
        }
        this.getComments(pagination, floorComment, true);
      },
      //表情渲染
      emoji(comments, flag) {
        comments.forEach(c => {
          c.commentContent = c.commentContent.replace(/\n/g, '<br/>');
          c.commentContent = this.$common.faceReg(c.commentContent);
          c.commentContent = this.$common.pictureReg(c.commentContent);
          if (flag) {
            if (!this.$common.isEmpty(c.replyCommentList)) {
              c.replyCommentList.forEach(cc => {
                c.commentContent = c.commentContent.replace(/\n/g, '<br/>');
                cc.commentContent = this.$common.faceReg(cc.commentContent);
                cc.commentContent = this.$common.pictureReg(cc.commentContent);
              });
            }
          }
        });
      },
      //获取评论
      getComments(pagination, floorComment = {}, isToPage = false) {

        //此处建议分开写，一级评论相关查询已经写完，下面写二级评论相关操作
        //TODO 二级评论相关操作

        this.$http.get("http://localhost:9090/blogComment/front/getFrontComment", this.pagination)
          .then((res) => {
            if (!this.$common.isEmpty(res.data) && !this.$common.isEmpty(res.data.records)) {
              if (this.$common.isEmpty(floorComment)) {
                //查询一级评论
                this.comments = res.data.records;
                this.floorCommentTotal = res.data.total
                this.emoji(this.comments, true);
              } else {
                //查询floorComment的二级评论分页
                if (isToPage === false) {
                  //二级评论翻页
                  floorComment.replyCommentList = res.data;
                } else {
                  //二级评论展开
                  floorComment.replyCommentCounts = res.data.total;
                  floorComment.replyCommentList = floorComment.replyCommentList.concat(res.data.records);
                }
                this.emoji(floorComment.replyCommentList, false);
              }
            }
          })
          .catch((error) => {
            this.$message({
              message: error.message,
              type: "error"
            });
          });
      },
      //回复涂鸦功能
      addGraffitiComment(graffitiComment) {
        this.submitComment(graffitiComment);
      },
      //提交评论
      submitComment(commentContent) {
        let comment = {
          articleId: this.source,
          userId: this.$store.state.currentUser.id,
          commentContent: commentContent,
          commentCheck: this.$store.state.commentCheck,
        };

        this.$http.post("http://localhost:9090/blogComment/front/addComment", comment)
          .then((res) => {
            this.$message({
              type: 'success',
              message: '保存成功！'
            });
            //重置分页数据
            this.pagination = {
              articleId: this.source,
              floorCommentId: 0,
              pageNum : 1,
              pageSize : 10,
            }
            this.getComments(this.pagination);
            this.getTotal();
          })
          .catch((error) => {
            this.$message({
              message: error.message,
              type: "error"
            });
          });
      },
      //提交回复
      submitReply(commentContent) {

        let comment = {
          articleId: this.source,
          userId: this.$store.state.currentUser.id,
          floorCommentId: this.floorComment.commentId,
          replyCommentId: this.replyComment.commentId,
          replyUserId: this.replyComment.userId,
          commentContent: commentContent,
          commentCheck: this.$store.state.commentCheck,
        };
        let floorComment = this.floorComment;

        this.$http.post("http://localhost:9090/blogComment/front/addComment", comment)
          .then((res) => {
            let pagination = {
              articleId: this.source,
              floorCommentId: floorComment.commentId,
              current: 1,
              size: 5,
            }
            this.getComments(pagination, floorComment);
            this.getTotal();
          })
          .catch((error) => {
            this.$message({
              message: error.message,
              type: "error"
            });
          });
        this.handleClose();
      },
      replyDialog(comment, floorComment) {
        this.replyComment = comment;
        this.floorComment = floorComment;
        this.replyDialogVisible = true;
      },
      handleClose() {
        this.replyDialogVisible = false;
        this.floorComment = {};
        this.replyComment = {};
      }
    }
  }
</script>

<style scoped>

  .comment-head {
    display: flex;
    align-items: center;
    font-size: 20px;
    font-weight: bold;
    margin: 40px 0 20px 0;
    user-select: none;
    color: var(--themeBackground);
  }

  .commentInfo-title {
    margin-bottom: 20px;
    color: var(--greyFont);
    user-select: none;
  }

  .commentInfo-detail {
    display: flex;
  }

  .commentInfo-avatar {
    border-radius: 5px;
  }

  .commentInfo-username {
    color: var(--orangeRed);
    font-size: 16px;
    font-weight: 600;
    margin-right: 5px;
  }

  .commentInfo-username-small {
    color: var(--orangeRed);
    font-size: 14px;
    font-weight: 600;
    margin-right: 5px;
  }

  .commentInfo-master {
    color: var(--green);
    border: 1px solid var(--green);
    border-radius: 0.2rem;
    font-size: 12px;
    padding: 2px 4px;
    margin-right: 5px;
  }

  .commentInfo-other {
    font-size: 12px;
    color: var(--greyFont);
    user-select: none;
  }

  .commentInfo-reply {
    font-size: 12px;
    cursor: pointer;
    user-select: none;
    color: var(--white);
    background: var(--themeBackground);
    border-radius: 0.2rem;
    padding: 3px 6px;
  }

  .commentInfo-content {
    margin: 15px 0 25px;
    padding: 18px 20px;
    background: var(--commentContent);
    border-radius: 12px;
    color: var(--black);
    word-break: break-word;
  }

  .pagination-wrap {
    display: flex;
    justify-content: center;
    margin-bottom: 10px;
  }

  .pagination {
    padding: 6px 20px;
    border: 1px solid var(--lightGray);
    border-radius: 3rem;
    color: var(--greyFont);
    user-select: none;
    cursor: pointer;
    text-align: center;
    font-size: 12px;
  }

  .pagination:hover {
    border: 1px solid var(--themeBackground);
    color: var(--themeBackground);
    box-shadow: 0 0 5px var(--themeBackground);
  }
</style>
