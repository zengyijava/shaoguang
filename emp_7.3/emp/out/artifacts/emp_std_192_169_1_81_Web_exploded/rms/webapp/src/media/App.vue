<template>
  <div id="app">
    <router-view name="navBar"></router-view>
    <div class="stage">
      <div class="editor-wrapper">
        <router-view name="editor"></router-view>
      </div>
      <div v-if="showEditorTable" class="footer-nav">
        <el-radio-group v-model="route" @change="handleChange">
          <el-radio-button label="editor">{{ $t('media.richMedia') }}</el-radio-button>
          <el-radio-button label="msg">{{ $t('media.supplementStyle') }}</el-radio-button>
        </el-radio-group>
      </div>
    </div>
    <!-- 浏览器支持提示 -->
    <div v-if="showBrowserTips" class="show-browser-tips">
      <div class="browser-cont">
        <p class="title">{{ $t('media.user') }}</p>
        <p class="desc">{{ $t('media.noCanUse') }}</p>
        <ul class="browser-icon-items clearfix">
          <li class="browser-icon">
            <img src="./assets/img/IE10_icon.png">
            <p>IE 10</p>
          </li>
          <li class="browser-icon">
            <img src="./assets/img/IE11_icon.png">
            <p>IE 11</p>
          </li>
          <li class="browser-icon">
            <img src="./assets/img/chrome_icon.png">
            <p>Chrome</p>
          </li>
          <li class="browser-icon">
            <img src="./assets/img/sougou_icon.png">
            <p>{{ $t('media.sougo') }}</p>
          </li>
          <li class="browser-icon">
            <img src="./assets/img/safe360_icon.png">
            <p>{{ $t('media.tsz') }}</p>
          </li>
        </ul>
      </div>
    </div>
    <!-- 搜狗兼容模式提示 -->
    <div v-if="sougouBrowserTips" class="show-sougou-tips">
      <p>{{ $t('media.sougoTipsA') }}<span>{{ $t('media.sougoTipsB') }}</span>{{ $t('media.sougoTipsC') }}<span>{{ $t('media.sougoTipsD') }}</span>。</p>
    </div>
  </div>
</template>

<script>
import {mapGetters, mapMutations, mapActions} from 'vuex'
import utils from '../libs/utils'
import * as types from '../libs/store/mutation-type'

export default {
  name: 'App',
  data () {
    return {
      route: 'editor',
      showBrowserTips: false,
      sougouBrowserTips: false,
      func: undefined
    }
  },
  created () {
    this.getBrowserVersion()
    this.getDegree()
    const id = utils.getUrlParameters('id', false, 'url')
    if (id !== '') {
      this.getTempDetail({
        tmId: id,
        previewType: 1
      })
    }
  },
  mounted () {
    let _userAgent = window.navigator.userAgent.toLowerCase()
    if (_userAgent.indexOf('se 2.x') > -1 && _userAgent.indexOf('trident') > -1) {
      this.sougouBrowserTips = true
    }
    // Subscriptions for mutation
    this.func = this.$store.subscribe(mutation => {
      if (mutation.payload) {
        const msg = mutation.payload.msg
        switch (mutation.type) {
          case types.ADD_TEMPLATE_SUCCESS:
            this.$message.success(msg)
            if (window.opener && !window.opener.closed) {
              window.opener.syncData()
            }
            break
          case 'error':
            this.$message.error(msg)
            break
          default:
            break
        }
      }
    })
    this.card.tmpType = 11
  },

  beforeDestroy () {
    this.$store.subscribe(this.func)
  },
  computed: {
    ...mapGetters(['card', 'mediaText', 'msgText', 'userInfo', 'params']),
    showEditorTable: function () {
      if (this.userInfo.hasOwnProperty('data')) {
        const _modulePer = this.userInfo.data.modulePer
        const _mediaModulePer = _modulePer.find((item) => {
          return item.type === 11
        })
        const _mediaSubLen = _mediaModulePer.suppStyle.length

        if (_mediaSubLen > 0) {
          return true
        } else {
          return false
        }
      } else {
        return true
      }
    }
  },
  methods: {
    ...mapMutations(['updateParams', 'updateMessage']),
    ...mapActions(['getDegree', 'getTempDetail']),
    handleChange () {
      this.$router.push(this.route)
    },

    // 判断浏览器版本是否小于等于ie9
    getBrowserVersion () {
      let _userAgent = navigator.userAgent
      let isIE = _userAgent.indexOf('compatible') > -1 && _userAgent.indexOf('MSIE') > -1

      if (isIE) {
        let reIE = new RegExp('MSIE (\\d+\\.\\d+);')
        reIE.test(_userAgent)
        let fIEVersion = parseFloat(RegExp['$1'])
        if (fIEVersion <= 9) {
          this.showBrowserTips = true
        }
      }
    }
  },
  watch: {
    '$route' (newValue, oldValue) {
      this.route = newValue.name
    },
    mediaText (newValue, oldValue) {
      if (this.params.length > 0) {
        // 交集
        const intersects = utils.getIntersectParams(newValue, this.params)
        // 同步参数
        this.updateParams(intersects, this.params)
      }
      // 删除时处理
      if (oldValue && oldValue.length > newValue.length) {
        // 差集
        const difference = utils.getDifferentParams(newValue, oldValue)
        // 遍历短信参数数组，对比已有参数数组，删除短信中无用参数
        utils.removeMsgParam(difference, this)
      }
    }
  }
}
</script>
<style lang="less">
  @import '../libs/assets/less/editor-app';

  .footer-nav {
    .el-radio-button, .el-radio-button__inner {
      min-width: 160px;
    }
  }
  .show-sougou-tips{
    position: fixed;
    top: 60px;
    left: 0;
    z-index: 999;
    width: 100%;
    height: 50px;
    background: #fff;
    font-size: 16px;
    text-align: center;
    p{
      margin: 0;
      line-height: 50px;
    }
    span{
      color: #F56C6C;
    }
  }
  .show-browser-tips {
    position: fixed;
    top: 0;
    left: 0;
    z-index: 999;
    width: 100%;
    height: 100%;
    background: #e5e6e8;
    font-size: 16px;
    p {
      margin: 0;
    }
    .browser-cont {
      position: absolute;
      top: 50%;
      left: 50%;
      width: 460px;
      height: 240px;
      padding: 20px;
      margin-left: -240px;
      margin-top: -120px;
      background: #fff;
      overflow: hidden;
      border-radius: 5px;
      .title {
        margin-top: 10px;
        margin-bottom: 10px;
      }
      .desc {
        text-indent: 2em;
        line-height: 1.6;
      }
      .browser-icon-items {
        margin-top: 40px;
      }
    }
    .browser-icon {
      width: 60px;
      height: 60px;
      padding-left: 16px;
      padding-right: 16px;
      float: left;
      font-size: 12px;
      text-align: center;
      img {
        width: 100%;
      }
    }
  }
</style>
