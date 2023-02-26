import Vue from 'vue'
import Vuex from 'vuex'
import createPersistedState from "vuex-persistedstate";

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    toolbar: {
      visible: false,
      enter: true
    },
    categoryList: [],
    currentUser: {},
    webInfo: {
      webName: "",
      webTitle: [],
      webNotices: [],
      webFooter: "",
      backgroundImage: "",
      webAvatar: "",
      articleCheck:"",
      commentCheck:"",
      messageCheck:"",
    },
  },
  getters: {
    //根据类别数量计算文章总数
    articleTotal: state => {
      if (state.categoryList !== null && state.categoryList.length !== 0) {
        if (state.categoryList.length === 1) {
          return state.categoryList[0].articleCounts;
        } else {
          return state.categoryList.reduce((prev, curr) => {
            if (typeof prev === "number") {
              return prev + curr.articleCounts;
            } else {
              return prev.articleCounts + curr.articleCounts;
            }
          });
        }
      } else {
        return 0;
      }
    },
  },
  mutations: {
    changeToolbarStatus(state, toolbarState) {
      state.toolbar = toolbarState;
    },
    loadCategoryList(state, categoryList) {
      state.categoryList = categoryList;
    },
    loadCurrentUser(state, user) {
      state.currentUser = user;
    },
    loadWebInfo(state, webInfo) {
      webInfo.webTitle = webInfo.webTitle.split('');
      webInfo.webNotices = JSON.parse(webInfo.webNotices);
      state.webInfo = webInfo;
    }
  },
  actions: {},
  modules: {},
  plugins: [
    createPersistedState({
      storage: window.localStorage
    })
  ]
})
