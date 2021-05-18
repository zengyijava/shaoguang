import * as types from './mutation-type'

const mutations = {
  /**
    * 获取列表成功
    * @param state
    * @param payload
    */
  [types.GET_H5_LIST_SUCCESS] (state, payload) {
    state.h5List = payload
  },
  /**
    * 获取详情成功
    * @param state
    * @param payload
    */
  [types.GET_DETAIL_SUCCESS] (state, payload) {
    state.details = payload
  },
  /**
    * 新增H5成功
    * @param state
    * @param payload
    */
  [types.ADD_H5_SUCCESS] (state, payload) { },
  /**
    * 编辑修改H5成功
    * @param state
    * @param payload
    */
  [types.EDIT_H5_SUCCESS] (state, payload) { },
  /**
    * 删除H5成功
    * @param state
    * @param payload
    */
  [types.DELETE_H5_SUCCESS] (state, payload) {
    let index = state.h5List.data.list.indexOf(payload)
    state.h5List.data.list.splice(index, 1)
  }
}
export default mutations
