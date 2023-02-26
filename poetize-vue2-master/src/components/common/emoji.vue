<template>
  <div>
    <transition name="body">
      <el-card v-show="showEmoji">
        <el-tabs v-model="activeName" @tab-click="changeEmojiType">
          <el-tab-pane v-for="(item, index) in emojiTypeList" :key="index" :label="item.label" :name="item.name">
            <span class="emoji-item"
                  v-for="(value, key, index) in getEmojiList(item)"
                  :key="index"
                  @click="addEmoji(key)">
              <img class="emoji" :src="value" :title="key" width="24px" height="24px"/>
            </span>
          </el-tab-pane>
        </el-tabs>
      </el-card>
    </transition>
  </div>
</template>

<script>
  export default {
    props: {
      showEmoji: {
        type: Boolean
      }
    },
    data() {
      return {
        emojiTypeList:this.$constant.emojiTypeList,
        activeName:"default",
      };
    },
    created() {

    },
    methods: {
      changeEmojiType(tab, event){
        this.activeName = tab.name
      },
      addEmoji(key) {
        this.$emit("addEmoji", key);
      },
      getEmojiList(item) {
        let emojiList = item.list
        let result = {}
        for (let i = 0; i < emojiList.length; i++) {
          let emojiName = "["+item.name+":"+emojiList[i]+"]";
          result[emojiName] = "/static/upload/blog/emoji/" + item.name + "/" + emojiList[i] + item.suffix;
        }
        return result;
      }
    }
  }
</script>

<style scoped>

  .emoji-item {
    cursor: pointer;
    display: inline-block;
  }

  .emoji-item:hover {
    transition: all 0.2s;
    border-radius: 0.25rem;
    background: var(--lightGray);
  }

  .emoji {
    margin: 0.25rem;
    /* 把此元素放置在父元素的中部 */
    vertical-align: middle;
  }

  .body-enter-active, .body-leave-active {
    transition: all 0.3s;
  }

  .body-enter, .body-leave-to {
    opacity: 0;
    transform: scale(0.5);
  }
</style>
