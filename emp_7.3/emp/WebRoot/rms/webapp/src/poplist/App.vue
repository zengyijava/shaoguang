<template>
  <div id="app">
    <pop-list-cont
      :pageType="tableType"
      :auditStatus="auditStatus"
      :callFrom="'outside'"
      @immediateUse="immediateUse">
    </pop-list-cont>
  </div>
</template>

<script>
import PopListCont from '../libs/components/PopListCont'
import utils from '../libs/utils'
import config from '../libs/config'

export default {
  name: 'App',
  data () {
    return {
    }
  },
  components: {
    'pop-list-cont': PopListCont
  },
  computed: {
    // 弹层列表类型，my我的，common模板库
    tableType () {
      return utils.getUrlParameters('type', false, config.GET_URL_PARAMS.POPLIST) || 'my'
    },
    // 审核状态，列表数据加载哪一类数据，1位审核通过，外部使用则是统一为1或者根据外部传值决定
    auditStatus () {
      let _auditStatus = utils.getUrlParameters('auditStatus', false, config.GET_URL_PARAMS.POPLIST) || 1
      return Number(_auditStatus)
    }
  },
  methods: {
    // 立即使用
    immediateUse (id) {
      // 调用外部iframe方法
      let _iframeName = parent.parent.layer.getFrameIndex(window.parent.name)
      window.parent.parent.getTmpId(id)
      parent.parent.layer.close(_iframeName)
    }
  }
}
</script>
<style scoped>
  body {
    margin: 0 auto;
    overflow: hidden;
  }
</style>
