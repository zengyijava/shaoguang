import axios from '../../libs/axios'
import * as types from './mutation-type'
import AJAXURL from '../../libs/ajax.address'
export default {
  /**
   * 获取h5列表
   * @param commit
   * @param payload
   */
  getH5List: ({commit}, payload) => {
    axios.post(AJAXURL.GET_H5_LIST, payload).then(res => {
      if (res.data) {
        commit(types.GET_H5_LIST_SUCCESS, res.data)
      }
    })
  },
  /**
   * 获取h5详情
   * @param commit
   * @param payload
   */
  getDetail: ({commit}, payload) => {
    axios.post(AJAXURL.GET_H5_DETAILS, payload).then(res => {
      if (res.data) {
        console.log(res.data)
        commit(types.GET_DETAIL_SUCCESS, res.data)
      }
    })
  },
  /**
   * 保存编辑器内容
   * @param commit
   * @param payload
   */
  saveEditor: ({commit}, payload) => {
    axios.post(AJAXURL.ADD_H5_MODULE, payload).then(res => {
      if (res.data) {
        console.log(res.data)
        commit(types.ADD_H5_SUCCESS, res.data)
      }
    })
  },
  /**
   * 修改编辑器内容
   * @param commit
   * @param payload
   */
  editorH5: ({commit}, payload) => {
    axios.post(AJAXURL.UPDATE_H5_MODULE, payload).then(res => {
      if (res.data) {
        console.log(res.data)
        commit(types.EDIT_H5_SUCCESS, res.data)
      }
    })
  },
  /**
   * 删除列表
   * @param commit
   * @param payload
   */
  deleteEditor: ({commit}, payload) => {
    axios.post(AJAXURL.DELE_H5_MODULE, {hId: payload.hId}).then(res => {
      if (res.data) {
        console.log(res.data)
        commit(types.DELETE_H5_SUCCESS, payload)
      }
    })
  }
}
