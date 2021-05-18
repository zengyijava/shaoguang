import * as types from '../../../libs/store/mutation-type'

const mutations = {
  /**
   * 添加参数
   * @param state
   * @param payload
   */
  addParam (state, payload) {
    state.paramArr.push(payload)
  },

  /**
   * 更新参数
   * @param state
   * @param payload
   */
  updateParam (state, payload) {
    state.param = payload
  },

  /**
   * 更新可插入
   * @param state
   * @param payload
   */
  updateCanInsert (state, payload) {
    state.canInsert = payload
  },

  /**
   * 添加历史记录
   * @param state
   * @param payload
   */
  addHistory (state, payload) {
    state.histories.push(payload)
  },

  /**
   * 添加模板成功
   * @param state
   * @param payload
   */
  [types.ADD_TEMPLATE_SUCCESS] (state, payload) {
    state.editor.tmid = payload.data.tmpId
  },

  /**
   * 获取模板详情成功
   * @param state
   * @param payload
   */
  [types.GET_TEMP_DETAIL_SUCCESS] (state, payload) {
    const detail = payload.data
    if (detail.paramArr.length > 0) {
      state.paramArr = detail.paramArr
    }
  },

  /**
   * 获取行业用途列表成功
   * @param state
   * @param payload
   */
  [types.GET_INDUSTRY_USES_SUCCESS] (state, payload) {
    state.useIds = payload.data
  },
  /**
   * 获取档位容量成功
   * @param state
   * @param payload
   */
  [types.GET_DEGREE_SUCCESS] (state, payload) {
    state.degrees = payload.data
  }
}
export default mutations
