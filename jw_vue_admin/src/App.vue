<template>
  <div id="app">
    <router-view></router-view>


  </div>
</template>

<script>
import HelloWorld from './components/HelloWorld.vue'
import current from "element-ui/packages/table/src/store/current";

export default {
  name: 'app',
  components: {
    HelloWorld
  },
  data(){
    return{

    }
  },
  methods:{

  }
}
</script>

<style>
#app {
  height:100%;

}
</style>
