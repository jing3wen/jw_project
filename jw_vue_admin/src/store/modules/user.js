// 登陆用户相关设置
import store from '../index'
const state = {
    registerDrawerVisable: false,

    currentLoginUser:{

    },  //存储当前登录的用户信息
}

const getters = {

}

const mutations = {
    changeRegisterDrawerVisable(state){
        state.registerDrawerVisable = !state.registerDrawerVisable
    },
    loginUser(state, loginUserInfo){
        state.currentLoginUser = loginUserInfo
    },
    logout(state) {
        //重置登陆用户状态
        state.currentLoginUser = {}
        //重置标签栏
        store.commit('layout/resetTab')
    }
}

const actions = {

}

export default {
    namespaced: true,
    state,
    getters,
    mutations,
    actions
}