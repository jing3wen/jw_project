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
    openArticleCheck: true,  //是否开启文章审核
  },
  mutations: {
    setArticleCheck(state, status){
      this.state.openArticleCheck = status
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

