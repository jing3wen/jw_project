<template>
  <el-menu-item v-if="menu.menuType === 'menu' && menu.visible==='1'" :key="menu.id" :index="menu.path">
    <i :class="menu.icon"></i>
    <span slot="title">{{menu.menuName}}</span>
  </el-menu-item>
  <el-submenu v-else-if="menu.visible==='1'" :index="menu.id  + ''">
    <template slot="title">
      <i :class="menu.icon"></i>
      <span slot="title">{{menu.menuName}}</span>
    </template>
    <MenuItems v-for="child in menu.children" :menu="child" :key="child.id"></MenuItems>
  </el-submenu>
</template>

<script>
export default {
  name: "MenuItems",
  props: {
    menu: {
      type: Object,
      required: true
    }
  }
}
</script>


<style scoped>

</style>
