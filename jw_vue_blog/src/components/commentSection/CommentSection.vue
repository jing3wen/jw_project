<template>
  <div class="commentBox">
    <h3>文章评论</h3>
    <p>评论出精彩，精彩原与你！</p>
    <div class="comment_issue">
      <el-input @click="VerifyingLogin(ref)" :readonly="readonly" v-model="commentContent" maxlength="100"
                placeholder="留下你的精彩评论吧" :autosize="{ minRows: 4, maxRows: 6 }" show-word-limit type="textarea"
                style="margin-bottom: 10px;" />
      <div style=" text-align:right">
        <el-button plain color="#2fa7b9" @click="submitAdd(0)">提交</el-button>
      </div>

    </div>
    <h3>最新评论</h3>
    <p>{{commentCount}}条评论</p>
    <div class="all_comment">
      <div class="comment" v-for="(item,index) in commentInfo" :key="index">
        <div class="comment_left">
          <!-- 用户没设置头像的情况下使用系统默认的 -->
          <img v-if="item.userIcon !== null" :src="url+item.userIcon" />
          <img v-if="item.userIcon === null" src="../../assets/image/avatar/default_avatar.svg" alt="">
        </div>
        <div class="comment_right">
          <div class="comment_name">
            <p>{{item.userName}}<span v-if="item.userId == articleInfo.userId"
                                      class="comment_author">作者</span></p>
          </div>
          <div class="comment_content">
            <p>{{item.content}}</p>
          </div>
          <div class="comment_else">
            <p>
              <span>{{item.commentDate}}</span>
              <!-- 显示删除功能   1、当前文章为当前登录用户的 2、管理员用户 -->
              <el-popconfirm confirm-button-text="确定" cancel-button-text="取消" :icon="Delete"
                             icon-color="#F56C6C" confirm-button-type="danger" :title="'确定删除该评论吗？'"
                             @confirm="deleteComment(item.commentId)">
                <template #reference>
                  <span class="reply" v-if="userInfo.userId === articleInfo.userId || userInfo.userType === 0 || item.userId === userInfo.userId">
                    <el-icon><Delete /></el-icon>
                    删除
                  </span>
                </template>
              </el-popconfirm>
              <span class="reply" @click="reply(item.commentId)">
                <el-icon><ChatDotSquare /></el-icon>
                <span v-if="item.commentId !== replyId">回复</span>
                <span v-if="item.commentId === replyId">取消回复</span>
              </span>
            </p>
          </div>
          <div v-show="item.commentId === replyId" class="comment_reply_box">
            <el-input v-model="replyIdContent" maxlength="100" :placeholder="'回复@' + item.userName+ ':'"
                      :autosize="{ minRows: 4, maxRows: 6 }" show-word-limit type="textarea"
                      style="margin-bottom: 10px;" />
            <div style=" text-align:right">
              <el-button plain color="#2fa7b9" @click="submitAdd(item.commentId)">提交</el-button>
            </div>
          </div>
          <!-- 子评论 -->
          <div class="comment_child" v-if="item.child.length > 0" v-for="(val,itemIndex) in item.child"
               :key="itemIndex">
            <div class="comment_child_content">
              <div class="comment_child_left">
                <!-- 用户没设置头像的情况下使用系统默认的 -->
                <img v-if="val.userIcon !== null" :src="url+val.userIcon" />
                <img v-if="val.userIcon === null" src="../../assets/image/avatar/default_avatar.svg"
                     alt="">
              </div>
              <div class="comment_child_right">
                <div class="comment_child_name">
                  <p>{{val.userName}}<span v-if="val.userId == articleInfo.userId"
                                           class="comment_author">作者</span></p>
                </div>
                <div class="comment_content">
                  <p>{{val.content}}</p>
                </div>
                <div class="comment_else">
                  <p>
                    <span>{{val.commentDate}}</span>
                    <!-- 显示删除功能   1、当前文章为当前登录用户的 2、管理员用户 -->
                    <el-popconfirm confirm-button-text="确定" cancel-button-text="取消" :icon="Delete"
                                   icon-color="#F56C6C" confirm-button-type="danger" :title="'确定删除该评论吗？'"
                                   @confirm="deleteComment(val.commentId)">
                      <template #reference>
                        <span class="reply" v-if="userInfo.userId === articleInfo.userId || userInfo.userType ===0 || val.userId === userInfo.userId">
                          <el-icon><Delete /></el-icon>
                          删除
                        </span>
                      </template>
                    </el-popconfirm>
                    <span class="reply" @click="reply(val.commentId)">
                      <el-icon><ChatDotSquare /></el-icon>
                      <span v-if="val.commentId !== replyId">回复</span>
                      <span v-if="val.commentId === replyId">取消回复</span>
                    </span>
                  </p>
                </div>
                <div v-show="val.commentId === replyId" class="comment_reply_box">
                  <el-input v-model="replyIdContent" maxlength="100"
                            :placeholder="'回复@' + val.userName+ ':'" :autosize="{ minRows: 4, maxRows: 6 }"
                            show-word-limit type="textarea" style="margin-bottom: 10px;" />
                  <div style=" text-align:right">
                    <el-button plain color="#2fa7b9" @click="submitAdd(val.commentId)">提交</el-button>
                  </div>
                </div>
              </div>
            </div>
            <div v-if="val.child">
              <CommentItem :commentChild="val.child" :reply="val.userName" :authorId="articleInfo.userId"
                           :replyId="replyId" @onGetValue="reply" @onGetReplyIdContent="submitAdd2"
                           :userInfo="userInfo" @onDeleteComment="deleteComment">
              </CommentItem>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import CommentItem from "@/components/commentSection/commentItem/CommentItem";
import {useRoute} from "vue-router";
import router from "@/router";


export default {
  name: "CommentSection",
  components:{CommentItem},
  props: {
    f_articleInfo: Object
  },
  computed:{
    // 当前文章信息
    articleInfo(){
      return this.f_articleInfo
    }
  },
  data() {
    return{
      // 当前文章对应的评论信息
      commentInfo: [],
      // 评论内容
      commentContent: "",
      // 回复内容
      replyIdContent: '',
      // 回复状态
      replyId: false,
      // 评论框 只读
      readonly: true,
      // 总评论数
      commentCount: '',
      // 登录信息
      userInfo: {},
      // 服务器路径
      url: process.env.VUE_APP_URL,

    }
  },
  created() {
    this.loadData()
    // 获取当前登录的用户信息
    var tokenStr = sessionStorage.getItem("userInfo");
    if (tokenStr) {
      this.userInfo = JSON.parse(tokenStr).data[0]
    }
  },
  updated() {
    //TODO 此处是查询页面标签，建议改成后端查询
    this.commentCount = document.querySelectorAll('.commentSection').length + document.querySelectorAll(
        '.comment_child').length
  },
  methods: {
    loadData() {
      const params = {
        articleId: this.articleInfo.articleId
      }
      this.request.get("/api/showCommentByArticleId", {params}).then(res => {
        this.commentInfo = res;
      })
    },
    //提示登录的弹窗
    loginMessageBox(){
      let route = useRoute()
      this.$messageBox.confirm(
          '当前系统检测到您未登录，登录即可解锁更多精彩内容',
          '提示', {
            confirmButtonText: '前往登录',
            cancelButtonText: '取消',
            type: 'warning',
          }
      ).then(() => {
        router.push({
          path: '/login',
          query: {
            redirect: route.path
          } // 登录成功后返回当前页面
        })
      })
    },

    // 评论表单获得焦点 校验是否登录
    VerifyingLogin(event){
      if (!this.userInfo) {
        this.loginMessageBox()
      } else {
        this.readonly = false
      }
    },

    // 回复评论
    reply(id){
      // 判断是否登录，没有登录前往登录界面
      if (!this.userInfo) {
        this.loginMessageBox()
      } else {
        // 关闭回复
        if (this.replyId === id) {
          this.replyId = false;
        }
        // 当前评论没有关闭又点击其他回复时
        else if (this.replyId !== id && this.replyId !== false) {
          this.replyId = id
        } else { // 为false时
          this.replyId = id
        }
      }
    },

    // 提交评论
    submitAdd(parentId){
      // 判断是否登录，没有登录前往登录界面
      if (!this.userInfo) {
        this.loginMessageBox()
      } else {
        if (this.commentContent !== "" || this.replyIdContent !== "") {
          const params = {
            // 当前文章id
            articleId: this.articleInfo.articleId,
            // 评论用户id
            userId: this.userInfo.userId,
            // 评论内容
            content: parentId === 0 ? this.commentContent : this.replyIdContent,
            // 父评论  没有为0
            parentId: parentId
          }
          this.request.post("/api/commentSection/insertCommentInfo",
              params
          ).then(res => {
            if (parentId === 0) {
              // 正常评论
              this.commentContent = ""
            } else {
              // 回复
              this.replyIdContent = ""
              // 回复表单
              this.replyId = false
            }
            // 刷新评论数据
            this.loadData();
            this.$message.success('评论成功')
          })
        } else {
          this.$message.error('表达你的态度再评论吧')
        }

      }
    },

    // 回复评论
    submitAdd2(parentId, replyContent){
      // 判断是否登录，没有登录前往登录界面
      if (!this.userInfo) {
        this.loginMessageBox()
      } else {
        if (replyContent !== "") {
          const params = {
            // 当前文章id
            articleId: this.articleInfo.articleId,
            // 评论用户id
            userId: this.userInfo.userId,
            // 评论内容
            content: replyContent,
            // 父评论  没有为0
            parentId: parentId
          }

          this.request.post("/api/commentSection/insertCommentInfo", params).then(res => {
            if (parentId == 0) {
              // 正常评论
              this.commentContent = ""
            } else {
              // 回复
              this.replyIdContent = ""
              // 回复表单
              this.replyId = false
            }
            // 刷新评论数据
            this.loadData();
            this.$message.success('评论成功')
          })
        } else {
          this.$message.error('表达你的态度再评论吧')
        }

      }
    },

    // 删除评论
    deleteComment(commentId) {
      this.request.delete("/api/commentSection/delete", {params: {commentId: commentId}}).then(res => { // 删除成功
        if (res.code === 0) {
          // 刷新评论数据
          this.loadData();
        } else {
          this.$message.error('删除失败')
        }
      })
    }


  }
}
</script>

<style scoped>
.commentBox {
  height: auto;
  overflow: hidden;
  background-color: white;
  padding: 25px;
  color: #474749;
  border-radius: 10px;
  margin-bottom: 20px;
}

.commentBox>p {
  border-bottom: #dbdbdb 1px dashed;
  line-height: 30px;
  color: #888;
  font-size: 13px;
  padding-bottom: 5px;
  margin-bottom: 20px;
}

/* 评论区样式 */
.comment {
  /* width: 100%;
  float: left; */
  display: flex;
}

/* .comment_left {
    width: 40px;
    height: auto;
    margin-right: 10px;
} */

.comment_left>img {
  width: 40px;
  border-radius: 50px;
  object-fit: cover;
  overflow: hidden;
  margin-right: 10px;
}

.comment_right {
  /* width: 85%; */
  height: auto;
  /* float: left; */
  line-height: 25px;
}

.comment_name {
  color: #6F6F6F;
  font-size: 14px;
}

.comment_author {
  padding: 0 3px;
  background: #2fa7b9;
  color: white;
  margin-left: 5px;
  border-radius: 3px;
}

.comment_else {
  font-size: 14px;
  color: #b1b1b1;
  margin-bottom: 10px;
}

.comment_else span {
  margin-right: 15px;
  margin-right: 15px;
  display: inline-flex;
  justify-content: center;
  align-items: center;
}

.comment_else span .el-icon {
  margin-right: 5px;
}

.comment_else .reply {
  color: #777777;
  cursor: pointer;
  font-size: 12px;
}

.comment_child_content {
  display: flex;
}

/* .comment_child_left {
    width: 30px;
    height: auto;
    margin-right: 8px;
} */

.comment_child_name {
  color: #6F6F6F;
  font-size: 13px;
}

.comment_child_left img {
  width: 40px;
  border-radius: 50px;
  object-fit: cover;
  overflow: hidden;
  margin-right: 10px;
}

.comment_child_right {
  /* width: 90%;
  height: auto;
  float: left; */
  height: auto;
  line-height: 25px;
}
</style>
