import axios from '../../../libs/axios'
import AJAXURL from '../../../libs/ajax.address'
import * as types from './mutation-type'

export default {
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
  }
}
