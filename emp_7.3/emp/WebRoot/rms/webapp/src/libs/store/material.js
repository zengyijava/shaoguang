import axios from '../axios'
import AJAXURL from '../ajax.address'
import * as types from './mutation-type'

export default {
  state: {
    busy: false,
    useIds: [],
    totalPage: 0,
    addType: '',
    templateType: 11,
    materialList: {
      image: [],
      audio: [],
      video: []
    }
  },
  getters: {
    busy: (state, getters) => state.busy,
    addType: (state, getters) => state.addType,
    templateType: (state, getters) => state.templateType,
    materialList: (state, getters) => state.materialList,
    useIds: (state, getters) => state.useIds,
    imageList: (state, getters) => getters.materialList.image,
    audioList: (state, getters) => getters.materialList.audio,
    videoList: (state, getters) => getters.materialList.video
  },
  mutations: {
    /**
     * 设置addType
     * @param state
     * @param payload
     */
    setAddType (state, payload) {
      state.addType = payload
      state.busy = false
      state.totalPage = 0
      state.materialList.image = []
      state.materialList.audio = []
      state.materialList.video = []
    },

    /**
     * 重置数据
     * @param state
     * @param payload
     */
    resetList (state, payload) {
      state.busy = false
      state.totalPage = 0
      state.materialList[payload] = []
    },

    /**
     * 设置templateType
     * @param state
     * @param payload
     */
    updateTempType (state, payload) {
      state.templateType = payload
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
     * 删除素材成功
     * @param state
     * @param payload
     */
    [types.DELETE_FODDER_SUCCESS] (state, payload) {
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
    [types.GET_FODDER_SUCCESS] (state, payload) {
      state.totalPage = payload.totalPage
      const list = payload.list
      switch (state.addType) {
        case 'video':
          state.materialList.video.push(...list)
          break
        case 'image':
        case 'bgImage':
          state.materialList.image.push(...list)
          break
        case 'audio':
        case 'music':
          state.materialList.audio.push(...list)
          break
      }
    },
    /**
     * 获取行业用途列表成功
     * @param state
     * @param payload
     */
    [types.GET_INDUSTRY_USES_SUCCESS] (state, payload) {
      state.useIds = payload.data
    }
  },
  actions: {
    /**
     * 删除素材
     * @param commit
     * @param payload
     */
    deleteFodder: ({commit}, payload) => {
      commit('preload')
      axios.post(AJAXURL.DELE_FODDER, payload).then(res => {
        commit('loaded')
        commit(types.DELETE_FODDER_SUCCESS, {data: res.data, id: payload.fodderIds})
      })
    },

    /**
     * 获取素材
     * @param commit
     * @param payload
     */
    getFodder: ({commit, state}, payload) => {
      payload.tmpType = state.templateType
      state.busy = true
      // 判断是否显示加载更多
      if (state.totalPage === 0 || payload.page <= state.totalPage) {
        axios.post(AJAXURL.GET_FODDER_LIST, payload).then(res => {
          state.busy = false
          payload.page++;
          commit(types.GET_FODDER_SUCCESS, res.data.data)
        })
      }
    },
    /**
     * 获取行业用途列表
     * @param commit
     * @param payload
     */
    getIndustryUses: ({commit}, payload) => {
      commit('preload')
      axios.post(AJAXURL.GET_INDUSTRY_AND_USE, payload).then(res => {
        commit('loaded')
        commit(types.GET_INDUSTRY_USES_SUCCESS, res.data)
      })
    }
  }
}
