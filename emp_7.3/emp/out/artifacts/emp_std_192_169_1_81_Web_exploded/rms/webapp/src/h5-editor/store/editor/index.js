import mutations from './mutations'
import h5actions from './h5actions'
import actions from '../../../libs/store/actions'
import getters from './getters'
import Page from '../../model/Page'
import appConfig from '../../model/appConfig'
const page = new Page()
const state = {
  count: 0,
  h5: {
    tmid: 0,
    width: 320,
    height: 508
  },
  app: appConfig,
  corpId: '',
  pages: [page],
  needRealTime: false,
  swiper: {
    direction: 'vertical',
    effect: 'cube',
    loop: true,
    pageAlign: 'right',
    autoPlay: false
  },
  isBackGround: true,
  currentPage: page,
  element: {},
  music: {
    active: true,
    src: '',
    filename: '',
    autoPlay: true,
    loop: true,
    reused: true
  },
  addType: '',
  materialList: {
    image: [],
    audio: [],
    video: []
  },
  paramArr: [],
  param: {},
  canInsert: false
}

export default {
  state,
  getters,
  actions: {
    ...actions,
    ...h5actions
  },
  mutations
}
