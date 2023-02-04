import { createStore } from 'vuex'

export default createStore({
  state: {
    currentLoginUser: {
      userId: 7,
      nickname: 'jingwen',
      roleList:['admin','user'],
      avatar: '/static/upload/avatar/2022-09-13-1df66f0dfd0017b90e9dc0fb850224f4.jpeg',

    }
  },
  mutations: {
  },
  actions: {
  },
  modules: {
  }
})
