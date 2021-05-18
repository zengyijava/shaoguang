import axios from '../../../libs/axios'
import * as types from '../../../libs/store/mutation-type'
import AJAXURL from '../../../libs/ajax.address'

export default {
  /**
   * 生成图表图片
   * @param commit
   * @param payload
   */
  createChartImg: ({commit}, payload) => {
    axios.post(AJAXURL.CREATE_CHART, payload).then(res => {
      if (res.data) {
        commit(types.CHART_IMG_SUCCESS, res.data)
      }
    })
  }
}
