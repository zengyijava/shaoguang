import axios from '../axios'
import * as types from '../../libs/store/mutation-type'
import AJAXURL from '../ajax.address'

const actions = {
  /**
   * 添加模板
   * @param commit
   * @param payload
   */
  addTemplate: ({commit}, payload) => {
    commit('preload')
    axios.post(AJAXURL.ADD_TEMPS, payload).then(res => {
      commit('loaded')
      commit(types.ADD_TEMPLATE_SUCCESS, res.data)
    })
  },

  /**
   * 获取模板详情
   * @param commit
   * @param payload
   */
  getTempDetail: ({commit}, payload) => {
    axios.post(AJAXURL.GET_TEMPS_DETAILS, payload).then(res => {
      commit(types.GET_TEMP_DETAIL_SUCCESS, res.data)
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
  },
  /**
   * 获取行业用途列表
   * @param commit
   * @param payload
   */
  getIndustryUses: ({commit}, payload) => {
    commit('preload')
    axios.post(AJAXURL.GET_INDUSTRY_AND_USE, payload).then(res => {
      commit('loaded')
      commit(types.GET_INDUSTRY_USES_SUCCESS, res.data)
    })
  },

  /**
   * 裁剪图片
   * @param commit
   * @param payload
   */
  cutImage: ({commit}, payload) => {
    commit('preload')
    axios.post(AJAXURL.IMG_CROPPER, payload).then(res => {
      commit('loaded')
      commit(types.CUT_IMAGE_SUCCESS, res.data)
    })
  }
}

export default actions
