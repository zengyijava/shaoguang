import * as types from '../../../libs/store/mutation-type'
import * as h5types from './mutation-type'
import utils from '../../../libs/utils'

const mutations = {
  /**
   * 更新计数
   * @param state
   * @param payload
   */
  updateCount (state, payload) {
    state.count++
  },

  /**
   * 选择企业
   * @param state
   * @param payload
  */
  selectCorp (state, payload) {
    state.corpId = payload
  },

  /**
   * 选择元素
   * @param state
   * @param payload
   */
  selectElement (state, payload) {
    state.isBackGround = false
    state.element = payload
    state.currentPage.elements.forEach(item => {
      if (item === payload) {
        item.active = true
      } else {
        item.active = false
      }
    })
  },

  /**
   * 更换背景
   * @param state
   * @param payload
   */
  setBgState (state, payload) {
    state.isBackGround = payload
    if (payload) {
      state.currentPage.elements.forEach(item => {
        item.active = false
      })
    }
  },

  /**
   * 删除背景
   * @param state
   * @param payload
   */
  removeBgImg (state, payload) {
    state.currentPage.background.src = ''
  },

  /**
   * 开始实时预览
   * @param state
   * @param payload
   */
  startRealTime (state, payload) {
    state.needRealTime = true
  },

  /**
   * 设置h5-editor addType
   * @param state
   * @param payload
   */
  setAddType (state, payload) {
    state.addType = payload
  },

  pushImage (state, payload) {
    state.materialList.image.unshift(payload)
  },

  pushAudio (state, payload) {
    state.materialList.audio.unshift(payload)
  },

  pushVideo (state, payload) {
    state.materialList.video.unshift(payload)
  },

  /**
   * 添加历史记录
   * @param state
   * @param payload
   */
  addHistory (state, payload) {
    let elements = utils.deepClone(payload)
    elements.find(item => item.tag === state.element.tag).active = false
    // 当前元素active为true, 但保存在histories中为false，不然撤销时会闪烁
    state.currentPage.histories.push(utils.deepClone(elements))
  },

  /**
   * 更新Elements
   * @param state
   * @param payload
   */
  updateElements (state, payload) {
    state.currentPage.elements = payload ? utils.deepClone(payload) : []
  },

  /**
   * 更新按钮事件类型
   * @param state
   * @param payload
   */
  updateAction (state, payload) {
    state.element.action = payload
  },

  /**
   * 设置/切换当前页
   * @param state
   * @param payload
   */
  selectPage (state, payload) {
    state.currentPage = payload
    state.needRealTime = false
    // 激活背景设置
    this.commit('setBgState', true)
  },

  /**
   * 图层排序
   * @param state
   * @param payload
   */
  sortEleByZ (state, payload) {
    state.currentPage.elements = payload
    state.currentPage.elements.forEach((item, i) => {
      item.z = i
    })
  },

  /**
   * 删除图层
   * @param state
   * @param index
   * */
  removeLayer (state, index) {
    state.currentPage.elements.splice(index, 1)
    state.currentPage.elements.forEach((item, i) => {
      item.z = i
    })
  },

  /**
   * 复制图层
   * @param state
   * @param payload
   * */
  copyLayer (state, payload) {
    let eleTag = payload.tag.slice(0, 2)
    const elements = state.currentPage.elements
    const eleArr = elements.filter(item => {
      if (item.type === payload.type) {
        return elements
      }
    })
    let len = eleArr.length
    let eleLen = len + 1
    const ele = utils.deepClone(payload)
    ele.tag = eleTag + ' ' + eleLen
    ele.z = len
    state.currentPage.elements.push(ele)
  },

  /**
   * 添加参数
   * @param state
   * @param payload
   */
  addParam (state, payload) {
    state.paramArr.push(payload)
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

  /**
   * 添加模板成功
   * @param state
   * @param payload
   */
  [types.ADD_TEMPLATE_SUCCESS] (state, payload) {
    state.h5.tmid = payload.data.tmpId
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
    if (detail.paramArr.length > 0) {
      state.paramArr = detail.paramArr
    }
    const template = detail.list[0]
    if (template) {
      state.app = JSON.parse(template.app)
      const content = JSON.parse(template.content)
      content.pages.forEach(item => {
        item.histories = []
        if (item.elements.length > 0) {
          const tmpElements = [...utils.deepClone(item.elements)]
          item.histories.unshift(tmpElements)
        }
        // 后台未保存active所以需要初始active
        item.elements.forEach(element => {
          element.active = false
        })
      })
      state.pages = content.pages
      state.currentPage = content.pages[0]
      state.music = {
        ...state.music,
        ...content.music
      }
      state.swiper = {
        ...state.swiper,
        ...content.swiper
      }
    }
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
   * 删除素材成功
   * @param state
   * @param payload
   */
  [h5types.DELETE_FODDER_SUCCESS] (state, payload) {
    let leftList
    if (payload.data.code === 200) {
      switch (state.addType) {
        case 'video':
          leftList = state.materialList.video.filter(item => {
            return (payload.id).indexOf(item.id) < 0
          })
          state.materialList.video = leftList
          break
        case 'image':
          leftList = state.materialList.image.filter(item => {
            return (payload.id).indexOf(item.id) < 0
          })
          state.materialList.image = leftList
          break
        case 'audio':
        case 'music':
          leftList = state.materialList.audio.filter(item => {
            return (payload.id).indexOf(item.id) < 0
          })
          state.materialList.audio = leftList
          break
      }
    }
  },

  /**
   * 获取素材列表成功
   * @param state
   * @param payload
   */
  [h5types.GET_FODDER_SUCCESS] (state, payload) {
    let laveList
    laveList = payload.list.filter(item => {
      if (item.status === 1) {
        return payload.list
      }
    })
    switch (state.addType) {
      case 'video':
        state.materialList.video = laveList
        break
      case 'image':
        state.materialList.image = payload.list
        break
      case 'bgImage':
        state.materialList.image = payload.list
        break
      case 'audio':
        state.materialList.audio = laveList
        break
      case 'music':
        state.materialList.audio = laveList
        break
    }
  }
}
export default mutations
