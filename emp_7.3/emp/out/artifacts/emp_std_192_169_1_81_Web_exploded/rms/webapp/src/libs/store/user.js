import axios from '../axios'
import AJAXURL from '../ajax.address'
import * as types from './mutation-type'

export default {
  state: {
    userInfo: {}
  },
  getters: {
    userInfo: (state, getters) => state.userInfo
  },
  mutations: {
    /**
     * 获取用户信息成功
     * @param state
     * @param payload
     */
    [types.GET_USER_INFO_SUCCESS] (state, payload) {
      state.userInfo = payload
    }

  },
  actions: {
    /**
     * 获取用户信息
     * @param commit
     */
    getUserInfos: ({commit}) => {
      commit('preload')
      axios.get(AJAXURL.GET_USER_INFOS).then(response => {
        commit('loaded')
        commit(types.GET_USER_INFO_SUCCESS, response.data)
      })
    }
  }
}
