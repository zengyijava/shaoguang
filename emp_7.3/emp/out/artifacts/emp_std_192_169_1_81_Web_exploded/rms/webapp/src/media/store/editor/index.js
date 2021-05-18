import mutations from './mutations'
import actions from './actions'
import getters from './getters'

const state = {
  element: {},
  chartInfo: {},
  histories: [],
  messages: [],
  textEditable: [],
  currentElement: {},
  usedParamArr: [],
  insertDisabled: true,
  cropper: {}
}

export default {
  namespaced: true,
  state,
  getters,
  actions,
  mutations
}
