import axios from './axios'
import AJAXURL from './ajax.address'

let IO = function (url, params, successCB, errorCB) {
  axios.post(url, params).then(response => {
    successCB && successCB(response)
  }).catch(error => {
    const _errMsg = error.msg || error.toString() || '加载失败'
    errorCB && errorCB(_errMsg)
  })
}

const actions = {
  // 获取用户信息
  getUserInfos: function (payload, successCB, errorCB) {
    IO(AJAXURL.GET_USER_INFOS, payload, successCB, errorCB)
  },
  // 获取列表数据
  getListData: function (payload, successCB, errorCB) {
    IO(AJAXURL.GET_TEMPS_LIST, payload, successCB, errorCB)
  },
  // 获取瀑布流图片
  getFodder: function (payload, successCB, errorCB) {
    IO(AJAXURL.GET_FODDER_LIST, payload, successCB, errorCB)
  },
  // 获取单个模板详细信息
  getDetailInfo: function (payload, successCB, errorCB) {
    IO(AJAXURL.GET_TEMPS_DETAILS, payload, successCB, errorCB)
  },

  // 获取行业和用途
  getUse: (payload, successCB, errorCB) => {
    IO(AJAXURL.GET_INDUSTRY_AND_USE, payload, successCB, errorCB)
  },

  // 删除模板
  deleModel: (payload, successCB, errorCB) => {
    IO(AJAXURL.DELE_TEMP, payload, successCB, errorCB)
  },

  // 模板启用和禁用
  setModelState: (payload, successCB, errorCB) => {
    IO(AJAXURL.SET_TEMP_STATE, payload, successCB, errorCB)
  },

  // 模板快捷方式设置
  setShortcut: (payload, successCB, errorCB) => {
    IO(AJAXURL.SET_SHOTCUT, payload, successCB, errorCB)
  },

  // 添加行业和用途
  addIndustryAndUse: function (payload, successCB, errorCB) {
    IO(AJAXURL.ADD_INDUSTRY_AND_USE, payload, successCB, errorCB)
  },

  // 删除行业和用途
  deleIndustryAndUse: function (payload, successCB, errorCB) {
    IO(AJAXURL.DELE_INDUSTRY_AND_USE, payload, successCB, errorCB)
  },

  // 更新行业和用途
  updateIndustryAndUse: function (payload, successCB, errorCB) {
    IO(AJAXURL.UPDATE_INDUSTRY_AND_USE, payload, successCB, errorCB)
  },

  // 获取企业信息
  getCorpInfos: function (payload, successCB, errorCB) {
    IO(AJAXURL.GET_CORP_INFO, payload, successCB, errorCB)
  },

  // RX获取记录列表
  getRecordList: function (payload, successCB, errorCB) {
    IO(AJAXURL.GET_RECORD_LIST, payload, successCB, errorCB)
  },

  // RX获取记录预览
  getRecordPreview: function (payload, successCB, errorCB) {
    IO(AJAXURL.GET_RECORD_PREVIEW, payload, successCB, errorCB)
  },

  // RX短地址续期
  renewalTime: function (payload, successCB, errorCB) {
    IO(AJAXURL.RENEWAL_TIME, payload, successCB, errorCB)
  },

  // RX获取记录提交信息
  commitResourceInfo: function (payload, successCB, errorCB) {
    IO(AJAXURL.COMMIT_RESOURCE_INFO, payload, successCB, errorCB)
  }
}

export default actions
