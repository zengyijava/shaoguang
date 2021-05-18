import * as types from './mutation-type'
import utils from '../utils';

const mutations = {
  /**
   * 添加参数
   * @param state
   * @param payload
   */
  addParam (state, payload) {
    state.paramArr.push(payload)
    state.intersects.push(payload)
  },

  /**
   * 更新参数数组
   * @param state
   * @param payload
   */
  updateParams (state, payload) {
    state.intersects = payload
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

  setImageSize (state, payload) {
    state.imageSize.width = payload.width
    state.imageSize.height = payload.height
  },
  /**
   * 设置isLoading
   * @param state
   * @param payload
   */
  setLoading (state, payload) {
    state.isLoading = payload
  },

  /**
   * 更新content
   * @param state
   * @param payload
   */
  updateContent (state, payload) {
    state.template.content.elements = utils.deepClone(payload) || {
      texts: [],
      images: [],
      audios: [],
      videos: [],
      buttons: [],
      qrcodes: []
    }
  },

  /**
   * 更新富媒体Content
   * @param state
   * @param payload
   */
  updateMediaContent (state, payload) {
    state.mediaTemplate.content = utils.deepClone(payload) || []
  },

  /**
   * 更新短信
   * @param state
   * @param payload
   */
  updateMessage (state, payload) {
    state.msgTemplate.content.template = payload || ' '
  },

  /**
   * 删除场景图层
   * @param state
   * @param payload
   * */
  removeLayer (state, payload) {
    let eleType = payload.type + 's'
    const elements = state.template.content.elements['' + eleType]
    let index = elements.findIndex(value => {
      return value === payload
    })
    elements.splice(index, 1)
    this.getters.elements.forEach((item, i) => {
      item.z = i
    })
  },

  /**
   * 复制图层
   * @param state
   * @param payload
   * */
  copyLayer (state, payload) {
    let len = this.getters.elements.length
    let eleType = payload.type
    let eleTag = payload.tag.slice(0, 2)
    const elements = state.template.content.elements['' + eleType + 's']
    const ele = utils.deepClone(payload)
    if (eleType === 'text') {
      let str = ele.text.replace(/<input(.+?)>/g, '')
      ele.text = str
    }
    ele.tag = eleTag + ' ' + (elements.length + 1)
    ele.z = len
    elements.push(ele)
  },

  /**
   * 添加模板成功
   * @param state
   * @param payload
   */
  [types.ADD_TEMPLATE_SUCCESS] (state, payload) {
    state.card.tmid = payload.data.tmpId
    if (payload.data.spTmpId) {
      setTimeout(() => {
        if (window.opener && !window.opener.closed) {
          window.close()
        }
      }, 1000)
    }
  },

  /**
   * 获取模板详情成功
   * @param state
   * @param payload
   */
  [types.GET_TEMP_DETAIL_SUCCESS] (state, payload) {
    const detail = payload.data
    const {industryId, useId, tmpType} = detail
    state.card = {industryId, useId, tmpType}
    state.card.industryId = detail.industryId === -1 ? '' : detail.industryId
    state.card.useId = (detail.useId === -2 || detail.useId === 0) ? '' : detail.useId
    const tempArr = detail.list.map(item => {
      if (item.content) {
        const obj = JSON.parse(item.content)
        item.content = obj
      }
      const {tmpType, cardHtml, degree, content} = item
      // 拿到容量要除以100，数据库不能存带小数点容量
      let degreeSize = item.degreeSize / 100
      return {tmpType, cardHtml, degree, degreeSize, content}
    })
    state.card.tempArr = tempArr
    state.template = tempArr.find(item => item.tmpType === 12)
    if (state.template) {
      // 用paramText作兼容处理
      state.template.content.elements.texts.forEach(item => {
        if (item.hasOwnProperty('paramText')) {
          item.text = item.paramText
        }
      })
    }
    state.mediaTemplate = tempArr.find(item => item.tmpType === 11)
    state.mediaTemplate.content.forEach(item => {
      if (item.hasOwnProperty('paramText')) {
        item.text = item.paramText
      }
    })
    state.msgTemplate = tempArr.find(item => item.tmpType === 14)
    // 重新赋值
    if (detail.paramArr.length > 0) {
      state.paramArr = detail.paramArr
      state.intersects = [...detail.paramArr]
    }
  },

  /**
   * 获取档位容量成功
   * @param state
   * @param payload
   */
  [types.GET_DEGREE_SUCCESS] (state, payload) {
    state.degrees = payload.data
  },

  /**
   * 获取行业用途列表成功
   * @param state
   * @param payload
   */
  [types.GET_INDUSTRY_USES_SUCCESS] (state, payload) {
    state.useIds = payload.data
  }
}
export default mutations
