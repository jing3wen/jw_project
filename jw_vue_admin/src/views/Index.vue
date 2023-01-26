<template>
  <div class="index">
    <h1>这是主页, 后期添加内容</h1>
    <h1>停用数据不能编辑</h1>
    <h1>是否开启默认审核: {{this.$store.state.openArticleCheck}}</h1>
    <el-button class="mt-10" v-if="this.$store.state.openArticleCheck" @click="setArticleCheckStatus" >
      关闭默认审核
    </el-button>
    <el-button class="mt-10" v-else @click="setArticleCheckStatus" >
      开启默认审核
    </el-button>
  </div>

</template>

<script>
// @ is an alias to /src

export default {
  name: 'Index',
  methods:{
    setArticleCheckStatus(){
      this.$store.commit('setArticleCheck',!this.$store.state.openArticleCheck)
    }
  }
}
</script>
