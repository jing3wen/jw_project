<template>
  <div>
    <div>
      <el-image style="animation: header-effect 2s"
                class="background-image"
                v-once
                lazy
                :src="require('@/assets/file/backimage/messageBG.jpg')"
                fit="cover">
        <div slot="error" class="image-slot background-image-error"></div>
      </el-image>
      <!-- 输入框 -->
      <div class="message-in" style="text-align: center">
        <h2 class="message-title">树洞</h2>
        <div>
          <input class="message-input"
                 type="text"
                 style="outline: none;width: 70%"
                 placeholder="留下点什么啦~"
                 v-model="messageContent"
                 @click="show = true"
                 maxlength="60"/>
          <button v-show="show"
                  style="margin-left: 12px;cursor: pointer;width: 20%"
                  @click="submitMessage"
                  class="message-input">发射
          </button>
        </div>
      </div>
      <!-- 弹幕 -->
      <div class="barrage-container">
        <vue-baberrage :barrageList="barrageList" :loop="true"></vue-baberrage>
      </div>
    </div>
    <div class="comment-wrap">
      <div class="comment-content">
        <comment :source="$constant.source" :type="'message'" :userId="$constant.userId"></comment>
      </div>
      <myFooter></myFooter>
    </div>
  </div>
</template>

<script>
  const comment = () => import( "./comment/comment");
  const myFooter = () => import( "./common/myFooter");
  import messageDefaultAvatar from "../assets/file/messageDefault.png"

  export default {
    components: {
      comment,
      myFooter
    },
    data() {
      return {
        show: false,
        messageContent: "",
        // background: {"background": "url(" + this.$store.state.webInfo.backgroundImage + ") center center / cover no-repeat"},
        barrageList: [],
        messageDefaultAvatar: messageDefaultAvatar,
      };
    },
    created() {
      this.getTreeHole();
    },
    methods: {
      getTreeHole() {
        this.$http.get("/api/blogMessage/front/getMessageList")
          .then((res) => {
            if (!this.$common.isEmpty(res.data)) {
              res.data.forEach(m => {
                this.barrageList.push({
                  id: m.messageId,
                  avatar: !this.$common.isEmpty(m.messageAvatar)? m.messageAvatar: this.messageDefaultAvatar,
                  msg: m.messageContent,
                  time: Math.floor(Math.random() * 10 + 5)
                });
              });
            }
          })
          .catch((error) => {
            this.$message({
              message: error.message,
              type: "error"
            });
          });
      },
      submitMessage() {
        if (this.messageContent.trim() === "") {
          this.$message({
            message: "你还没写呢~",
            type: "warning",
          });
          return;
        }

        let message = {
          messageContent: this.messageContent.trim(),
          messageCheck: this.$store.state.webInfo.messageCheck
        };

        if (!this.$common.isEmpty(this.$store.state.currentUser)) {
          if(!this.$common.isEmpty(this.$store.state.currentUser.avatar)){
            message.messageAvatar = this.$store.state.currentUser.avatar;
          }
          if(!this.$common.isEmpty(this.$store.state.currentUser.nickname)){
            message.messageNickname = this.$store.state.currentUser.nickname;
          }
          if(!this.$common.isEmpty(this.$store.state.currentUser.email)){
            message.messageEmail = this.$store.state.currentUser.email;
          }
        }
        this.$http.post("/api/blogMessage/front/addMessage", message)
          .then((res) => {
            this.getTreeHole()
          })
          .catch((error) => {
            this.$message({
              message: error.message,
              type: "error"
            });
          });

        this.messageContent = "";
        this.show = false;
      }
    }
  }
</script>

<style scoped>

  .message-in {
    position: absolute;
    left: 50%;
    top: 40%;
    transform: translate(-50%, -50%);
    color: var(--white);
    animation: hideToShow 2.5s;
    width: 360px;
    z-index: 10;
  }

  .message-title {
    user-select: none;
    text-align: center;
  }

  .message-input {
    border-radius: 1.2rem;
    border: var(--white) 1px solid;
    color: var(--white);
    background: var(--transparent);
    padding: 10px 10px;
  }

  .message-input::-webkit-input-placeholder {
    color: var(--white);
  }

  .barrage-container {
    position: absolute;
    top: 50px;
    left: 0;
    right: 0;
    bottom: 0;
    height: calc(100% - 50px);
    width: 100%;
    user-select: none;
    overflow: hidden;
  }

  .comment-wrap {
    background: var(--background);
    position: absolute;
    top: 100vh;
    width: 100%;
  }

  .comment-content {
    max-width: 800px;
    margin: 0 auto;
    padding: 40px 20px;
  }
</style>
