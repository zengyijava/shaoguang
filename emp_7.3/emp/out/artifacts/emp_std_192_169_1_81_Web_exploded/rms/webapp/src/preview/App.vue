<template>
  <div id="app">
    <preview :previewModule="previewModule" :previewData="previewData" :mainTmpType="mainTmpType" :params="params"></preview>
  </div>
</template>

<script>
import Preview from '../libs/components/Preview'
import utils from '../libs/utils'
import config from '../libs/config'
import actions from '../libs/api'

export default {
  name: 'App',
  data: function () {
    return {
      params: [],
      // tmID
      previewId: '',
      // 1获取所有数据0获取主数据
      previewType: 1,
      // 详细数据
      previewData: null,
      // 主数据模板类型，11富媒体，12卡片，13富文本，14短信
      mainTmpType: 11,
      // 预览是否显示标题和提示信息
      previewModule: {
        title: true,
        hint: true
      }
    }
  },
  components: {
    'preview': Preview
  },
  created: function () {
    // 请求数据返回是否为多条数据0只给一个主数据 1给所有数据
    this.previewType = utils.getUrlParameters('previewType', false, config.GET_URL_PARAMS.PREVIEW) || 1
    // tmID
    this.previewId = utils.getUrlParameters('id', false, config.GET_URL_PARAMS.PREVIEW) || ''
    // 是否显示标题
    let title = utils.getUrlParameters('title', false, config.GET_URL_PARAMS.PREVIEW) || 1
    // 是否显示提示信息
    let hint = utils.getUrlParameters('hint', false, config.GET_URL_PARAMS.PREVIEW) || 1
    // 预览参数
    let _params = utils.getUrlParameters('params', true, config.GET_URL_PARAMS.PREVIEW)

    _params.length > 0 ? this.params = JSON.parse(_params) : this.params = []

    Number(title) === 1 ? this.previewModule.title = true : this.previewModule.title = false
    Number(hint) === 1 ? this.previewModule.hint = true : this.previewModule.hint = false

    // 获取详细信息参数
    let _getDetailsDataParams = {
      tmId: this.previewId,
      previewType: this.previewType
    }
    let _self = this

    // 执行详细信息获取
    actions.getDetailInfo(_getDetailsDataParams, response => {
      if (response.data.code === 200 || response.data.state === '0') {
        _self.previewData = response.data.data.list
        _self.mainTmpType = response.data.data.tmpType
      } else {
        _self.$message.error(response.data.msg)
      }
    }, errMsg => {
      _self.$message.error(errMsg)
    })
  }
}
</script>
<style lang="less">
  body {
    margin: 0 auto;
    overflow: hidden;
  }
  body.preview-body{
    min-width: 0;
  }
</style>
