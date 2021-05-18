import utils from '../../../libs/utils'
import * as types from '../../../libs/store/mutation-type'

const mutations = {
  /**
   * 更换二维码
   * @param state
   * @param payload
   */
  updateQrCode (state, payload) {
    if (payload.url) {
      state.element.src = payload.url
      state.element.size = payload.size
      state.element.codeType = '0'
      // state.element.name = ''
      state.element.w = utils.getFitWidth({width: payload.width, height: payload.height})
      state.element.h = utils.getFitHeight({width: payload.width, height: payload.height})
    }
  },

  updateQrcodeParam (state, payload) {
    state.element.name = payload.name
    state.element.codeType = payload.codeType
  },
  /**
   * 更换音频
   * @param state
   * @param payload
   */
  updateAudio (state, payload) {
    state.element.filename = payload.original
    state.element.title = payload.original
    state.element.src = payload.url
    state.element.size = payload.size
  },

  /**
   * 更换视频
   * @param state
   * @param payload
   */
  updateVideo (state, payload) {
    state.element.size = payload.size
    state.element.src = payload.url
  },

  /**
   * 裁剪图片成功
   * @param state
   * @param payload
   */
  [types.CUT_IMAGE_SUCCESS] (state, payload) {
    state.element.src = payload.path
  },

  /**
   * 选择元素
   * @param state
   * @param payload
   */
  selectElement (state, payload) {
    state.element = payload
    // 设置当前激活状态
    this.getters.elements.forEach(item => {
      if (item === payload) {
        item.active = true
      } else {
        item.active = false
      }
    })
  },

  /**
   * 图层上移
   * @param state
   * @param index
   * */
  moveUp (state, index) {
    const elements = this.getters.elements
    elements[index].z += 1
    elements[index + 1].z -= 1
    elements.sort((a, b) => a['z'] - b['z'])
  },

  /**
   * 图层下移
   * @param state
   * @param index
   * */
  moveDown (state, index) {
    const elements = this.getters.elements
    elements[index].z -= 1
    elements[index - 1].z += 1
    elements.sort((a, b) => a['z'] - b['z'])
  },

  /**
   * 图层置顶
   * @param state
   * @param index
   * */
  moveTop (state, index) {
    const elements = this.getters.elements
    elements[index].z = elements.length
    elements.sort((a, b) => a['z'] - b['z'])
    elements.forEach((item, i) => {
      item.z = i
    })
  },

  /**
   * 图层置底
   * @param state
   * @param index
   * */
  moveBottom (state, index) {
    const elements = this.getters.elements
    elements[index].z = -1
    elements.sort((a, b) => a['z'] - b['z'])
    elements.forEach((item, i) => {
      item.z = i
    })
  },

  /**
   * 隐藏图层
   * @param state
   * @param index
   * */
  layerHide (state, index) {
    const elements = this.getters.elements
    let visible = elements[index].visible
    elements[index].visible = !visible
  },

  /**
   * 锁定图层
   * @param state
   * @param index
   * */
  layerLocked (state, index) {
    const elements = this.getters.elements
    let locked = elements[index].locked
    elements[index].locked = !locked
  },

  /**
   * 添加历史记录
   * @param state
   * @param payload
   */
  addHistory (state, payload) {
    let elements = utils.deepClone(payload)
    elements[state.element.type + 's'].find(item => item.tag === state.element.tag).active = false
    // 当前元素active为true, 但保存在histories中为false，不然撤销时会闪烁
    state.histories.push(utils.deepClone(elements))
  },

  /**
   * 更新按钮事件类型
   * @param state
   * @param payload
   */
  updateAction (state, payload) {
    state.element.action = payload
  }
}
export default mutations
