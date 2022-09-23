<template>
  <div style="margin-top:10px; padding-left: 10px">
    <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page.sync="pageNum"
        :page-size.sync="pageSize"
        :page-sizes="[10, 20, 50]"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total">
    </el-pagination>
  </div>
</template>

<script>
//f_pageNum,f_pageSize来自父组件
export default {
  name: "Pagination",
  props:{
    total: { // 总页数
      type: Number,
      default: 0
    },
    f_pageNum: { // 当前页
      type: Number,
    },
    f_pageSize: { // 每页显示条数
      type: Number,
    }
  },
  computed: {
    pageNum: {
      get() {
        return this.f_pageNum
      },
      set(val) {
        this.$emit('update:f_pageNum', val)  //改变的第几页的值赋值给父组件
      }
    },
    pageSize: {
      get() {
        return this.f_pageSize
      },
      set(val) {
        this.$emit('update:f_pageSize', val) //改变的当前页几条数据的值赋值给父组件
      }
    }
  },
  methods:{
    handleSizeChange(val){
      if (this.pageNum * val > this.total) {
        this.pageNum = 1
      }
      this.$emit('pageList', { pageNum: this.pageNum, pageSize:val})
    },
    handleCurrentChange(val){
      this.$emit('pageList', { pageNum: val, pageSize:this.pageSize})
    },

  }
}
</script>

<style scoped>

</style>