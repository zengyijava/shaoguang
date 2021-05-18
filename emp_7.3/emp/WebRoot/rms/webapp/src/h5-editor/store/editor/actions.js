import axios from '../axios'
import * as types from './mutation-type'
import AJAXURL from '../../../libs/ajax.address'

export default {
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
   * 删除素材
   * @param commit
   * @param payload
   */
  deleteFodder: ({commit}, payload) => {
    axios.post(AJAXURL.DELE_FODDER, payload).then(res => {
      commit(types.DELETE_FODDER_SUCCESS, {data: res.data, id: payload.fodderIds})
    })
  },

  /**
   * 获取素材
   * @param commit
   * @param payload
   */
  getFodder: ({commit}, payload) => {
    axios.post(AJAXURL.GET_FODDER_LIST, payload).then(res => {
      commit(types.GET_FODDER_SUCCESS, res.data.data)
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
   * 裁剪图片
   * @param commit
   * @param payload
   */
  cutImage: ({commit}, payload) => {
    commit('preload')
    axios
      .post(AJAXURL.IMG_CROPPER, payload)
      .then(res => {
        commit('loaded')
        commit(types.CUT_IMAGE_SUCCESS, res.data)
      })
      .catch(error => {
        console.log(error)
      })
  }
}
