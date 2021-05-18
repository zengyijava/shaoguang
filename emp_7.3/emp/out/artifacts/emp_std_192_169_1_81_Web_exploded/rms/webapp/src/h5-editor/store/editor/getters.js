// import utils from '../../../libs/utils'

const getter = {
  count: (state, getters) => state.count,
  h5: (state, getters) => state.h5,
  app: (state, getters) => state.app,
  pages: (state, getters) => state.pages,
  swiper: (state, getters) => state.swiper,
  currentPage: (state, getters) => state.currentPage,
  background: (state, getters) => getters.currentPage.background,
  elements: (state, getters) => getters.currentPage.elements,
  texts: (state, getters) => getters.elements.filter(item => item.type === 'text'),
  buttons: (state, getters) => getters.elements.filter(item => item.type === 'button'),
  images: (state, getters) => getters.elements.filter(item => item.type === 'image'),
  audios: (state, getters) => getters.elements.filter(item => item.type === 'audio'),
  videos: (state, getters) => getters.elements.filter(item => item.type === 'video'),
  isBackGround: (state, getters) => state.isBackGround,
  needRealTime: (state, getters) => state.needRealTime,
  music: (state, getters) => state.music,
  element: (state, getters) => state.element,
  materialList: (state, getters) => state.materialList,
  style: (state, getters) => getters.element.style,
  addType: (state, getters) => state.addType,
  imageList: (state, getters) => getters.materialList.image,
  audioList: (state, getters) => getters.materialList.audio,
  videoList: (state, getters) => getters.materialList.video,
  corpId: (state, getters) => state.corpId,
  params: (state, getters) => state.paramArr,
  param: (state, getters) => state.param,
  canInsert: (state, getters) => state.canInsert
}
export default getter
