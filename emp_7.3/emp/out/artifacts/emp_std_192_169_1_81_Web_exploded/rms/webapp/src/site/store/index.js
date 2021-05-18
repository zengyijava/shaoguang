import mutations from './mutations'
import actions from './actions'
import getters from './getters'

const state = {
  h5List: {
    data: {
      list: []
    }
  },
  details: {
    data: {}
  }
}

export default {
  state,
  getters,
  actions,
  mutations
}
