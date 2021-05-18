import axios from 'axios'
import * as types from './mutation-type'
import AJAXURL from '../../../libs/ajax.address'

const actions = {
  /**
   * 添加模板
   * @param commit
   * @param payload
   */
  addTemplate: ({ commit }, payload) => {
    commit('preload')
    axios.post(AJAXURL.ADD_TEMPS, payload).then(res => {
      commit('loaded')
      commit(types.ADD_TEMPLATE_SUCCESS, res.data)
    })
  },
  /**
   * 获取行业用途列表
   * @param commit
   * @param payload
   */
  getIndustryUses: ({ commit }, payload) => {
    axios.post(AJAXURL.GET_INDUSTRY_AND_USE, payload).then(res => {
      commit(types.GET_INDUSTRY_USES_SUCCESS, res.data)
    })
  },
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
  },

  /**
   * 获取模板档位容量
   * @param commit
   * @param payload
   */
  getDegree: ({commit}, payload) => {
    axios.post(AJAXURL.GET_DEGREE, payload).then(res => {
      commit(types.GET_DEGREE_SUCCESS, res.data)
    })
  }
}

export default actions
