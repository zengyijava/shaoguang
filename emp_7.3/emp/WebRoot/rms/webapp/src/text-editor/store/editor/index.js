import mutations from './mutations'
import actions from '../../../libs/store/actions'
import getters from './getters'

const state = {
  degrees: [],
  editor: {
    tmid: 0,
    tmName: '',
    tmpType: 13,
    industryId: '',
    useId: '',
    corpId: '',
    isPublic: 0
  },
  useIds: [],
  paramArr: [],
  param: {
    type: 1,
    name: '参数1',
    hasLength: 1,
    lengthRestrict: 0,
    minLength: 0,
    maxLength: 32
  },
  canInsert: false,
  histories: []
}

export default {
  state,
  getters,
  actions,
  mutations
}
