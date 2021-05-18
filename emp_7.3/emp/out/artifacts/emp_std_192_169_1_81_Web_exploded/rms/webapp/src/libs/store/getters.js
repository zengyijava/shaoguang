import utils from '../utils'

const getter = {
  card: (state, getters) => state.card,
  template: (state, getters) => state.template,
  content: (state, getters) => getters.template.content,
  elements: (state, getters) => utils.obj2List(getters.template.content.elements).sort((a, b) => a['z'] - b['z']),
  mediaTemplate: (state, getters) => state.mediaTemplate,
  mediaContent: (state, getters) => getters.mediaTemplate.content,
  mediaText: (state, getters) => {
    return getters.mediaContent
      .filter(item => item.type === 'text' || item.type === 'image')
      .map(item => item.text).join('')
  },
  msgTemplate: (state, getters) => state.msgTemplate,
  msgText: (state, getters) => getters.msgTemplate.content.template || '',
  useIds: (state, getters) => state.useIds,
  degrees: (state, getters) => state.degrees,
  params: (state, getters) => state.paramArr,
  intersects: (state, getters) => state.intersects,
  param: (state, getters) => state.param,
  canInsert: (state, getters) => state.canInsert,
  imageSize: (state, getters) => state.imageSize,
  isLoading: (state, getters) => state.isLoading
}
export default getter
