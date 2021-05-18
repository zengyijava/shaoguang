import axios from '../axios'
import * as types from '../../../libs/store/mutation-type'
import AJAXURL from '../../../libs/ajax.address'

export default {
  /**
   * 获取卡片样式列表
   * @param commit
   * @param payload
   */
  getCardStyles: ({ commit }, payload) => {
    axios.post(AJAXURL.GET_CARD_STYLE, payload).then(res => {
      commit(types.GET_CARD_STYLES_SUCCESS, res.data)
    })
  },
  /**
   * 裁剪视频
   * @param commit
   * @param payload
   */
  updateVideo: ({commit}, payload) => {
    commit('preload', null, { root: true })
    axios
      .post(AJAXURL.VIDEO_CROPPER, payload)
      .then(res => {
        commit('loaded', null, { root: true })
        commit(types.UPDATE_VIDEO_SUCCESS, res.data)
      })
      .catch(error => {
        commit(types.UPDATE_VIDEO_FAIL, error)
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
