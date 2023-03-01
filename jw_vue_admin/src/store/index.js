import Vue from 'vue'
import Vuex from 'vuex'
import createPersistedState from "vuex-persistedstate";
import layout from './modules/layout'
import user from './modules/user'

Vue.use(Vuex)

export default new Vuex.Store({
  modules: {
    layout,
    user,
  },
  state: {
    blogWeb:{}
  },
  mutations: {
    setArticleCheck(state, status){
      this.state.openArticleCheck = status
    },
    setBlogWeb(state, status){
      this.state.blogWeb = status
    },
    removeBlogWeb(){
      this.state.blogWeb = {}
    }
  },
  actions: {
  },

  plugins: [
      createPersistedState({
        storage: window.sessionStorage
      })
  ]
})

