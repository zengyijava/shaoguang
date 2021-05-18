import utils from '../../../libs/utils'
import * as types from '../../../libs/store/mutation-type'

let count = 0
const mutations = {
  /**
   * 添加图片文本
   * @param state
   * @param payload
   * @param text
   */
  addImageText (state, payload) {
    count++
    const text = {
      active: true,
      tag: 'imageText_' + count,
      text: '请输入文本',
      type: 'text',
      x: 40,
      y: 20,
      w: 180,
      width: 180,
      h: 20,
      rotate: 0,
      scale: 1,
      style: {
        textAlign: 'left',
        fontSize: '14px',
        fontFamily: '黑体',
        fontWeight: 400,
        fontStyle: 400,
        color: '#333',
        backgroundColor: '',
        overflowY: 'hidden',
        rotate: 0,
        lineHeight: '14px'
      }
    }
    state.textEditable.push(text)
    if (payload.type === 'text') {
      payload.image.textEditable.push(text)
    } else {
      payload.textEditable.push(text)
    }
  },

  /**
   * 添加弹出层图片
   * @param state
   * @param payload
   * @param text
   */
  addInnerImage (state, payload) {
    count++
    const image = {
      active: true,
      type: 'image',
      tag: 'image_' + count,
      src: payload.image.url,
      size: payload.image.size,
      width: payload.image.width,
      height: payload.image.height,
      ratio: payload.image.ratio,
      text: '',
      scale: 1,
      x: 40,
      y: 20,
      rotate: 0,
      w: payload.image.ratio > 1 ? 240 : parseInt(240 * payload.image.ratio),
      h: payload.image.ratio > 1 ? parseInt(240 / payload.image.ratio) : 240
    }
    state.textEditable.push(image)
    if (payload.element.type === 'text') {
      payload.element.image.textEditable.push(image)
    } else {
      payload.element.textEditable.push(image)
    }
  },

  /**
   * 更换音频
   * @param state
   * @param payload
   */
  updateAudio (state, payload) {
    state.loading = false
    state.element.title = payload.original.slice(0, 20)
    state.element.size = payload.size
    state.element.src = payload.url
    state.element.filename = payload.original
  },

  /**
   * 更换视频
   * @param state
   * @param payload
   */
  updateVideo (state, payload) {
    state.element.size = payload.size
    state.element.duration = payload.duration
    state.element.src = payload.url
    state.element.width = payload.width
    state.element.height = payload.height
  },

  /**
   * 选择元素
   * @param state
   * @param payload
   */
  selectCurrentElement (state, payload) {
    state.currentElement = payload
  },

  /**
  * 清空textEitable
  */
  clearTextEitable (state) {
    state.textEitable = []
  },

  /**
   * 选择元素
   * @param state
   * @param payload
   */
  selectElement (state, payload) {
    state.element = payload
  },

  /**
   * 添加历史记录
   * @param state
   * @param payload
   */
  addHistory (state, payload) {
    state.histories.push(utils.deepClone(payload))
  },

  /**
   * 添加短信历史记录
   * @param state
   * @param payload
   */
  addMsgHistory (state, payload) {
    if (payload) {
      state.messages.push(payload)
    }
  },

  /**
   * 添加已使用参数数组
   * @param state
   * @param payload
   */
  addUsedParam (state, payload) {
    state.usedParamArr.push(payload)
  },

  /**
   * 重新设置已使用参数数组
   * @param state
   * @param payload
   */
  updateUsedParams (state, payload) {
    state.usedParamArr = payload
  },

  /**
   * 裁剪图片成功
   * @param state
   * @param payload
   */
  [types.CUT_IMAGE_SUCCESS] (state, payload) {
    state.element.size = payload.size
    state.element.src = payload.path
  },

  /**
   * 生成图表图片成功
   * @param state
   * @param payload
   */
  [types.CHART_IMG_SUCCESS] (state, payload) {
    state.chartInfo = payload
    const pUrl = payload.data.pictureUrl
    const pSize = payload.data.pictureSize
    if (state.element.type === 'chart') {
      state.element.src = pUrl
      state.element.pictureUrl = pUrl
      state.element.pictureSize = pSize
    } else if (state.element.type === 'text') {
      state.element.src = pUrl
      state.element.chart.pictureUrl = pUrl
      state.element.chart.pictureSize = pSize
    }
  }
}
export default mutations
