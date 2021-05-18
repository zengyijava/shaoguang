<template>
  <div id="app">
    <router-view name="navBar"></router-view>
    <div class="stage">
      <router-view name="editor"></router-view>
      <div v-if="isShowEditorTable()" class="footer-nav">
        <el-radio-group class="footer-nav" v-model="route" @change="handleChange">
          <el-radio-button label="editor">{{ $t('cardTabs.cardStyle') }}</el-radio-button>
          <el-radio-button v-if="showMediaEditor" label="media">{{ $t('cardTabs.supplementStyleOne') }}</el-radio-button>
          <el-radio-button v-if="showMsgEditor" label="msg">{{ $t('cardTabs.supplementStyleTwo') }}</el-radio-button>
        </el-radio-group>
      </div>
    </div>
  </div>
</template>

<script>
import { mapGetters, mapActions } from 'vuex'
import utils from '../libs/utils'
import * as types from '../libs/store/mutation-type'

export default {
  name: 'App',
  data () {
    return {
      route: '',
      showMediaEditor: true,
      showMsgEditor: true,
      func: undefined
    }
  },
  created () {
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
    this.card.tmpType = 12
  },
  beforeDestroy () {
    this.$store.subscribe(this.func)
  },
  computed: {
    ...mapGetters(['card', 'userInfo'])
  },
  methods: {
    ...mapActions(['getDegree', 'getTempDetail']),
    isShowEditorTable () {
      if (this.userInfo.hasOwnProperty('data')) {
        const _modulePer = this.userInfo.data.modulePer
        const _sceneModulePer = _modulePer.find((item) => {
          return item.type === 12
        })
        const _sceneSubLen = _sceneModulePer.suppStyle.length

        if (_sceneSubLen > 1) {
          this.showMediaEditor = true
          this.showMsgEditor = true
        } else if (_sceneSubLen === 1) {
          if (+_sceneModulePer.suppStyle[0] === 11) {
            this.showMsgEditor = false
            this.showMediaEditor = true
          } else if (+_sceneModulePer.suppStyle[0] === 14) {
            this.showMsgEditor = true
            this.showMediaEditor = false
          }
        }

        if (_sceneSubLen > 0) {
          return true
        } else {
          return false
        }
      } else {
        return true
      }
    },
    handleChange () {
      this.$router.push(this.route)
    }
  },
  watch: {
    '$route' (newValue, oldValue) {
      this.route = newValue.name
    }
  }
}
</script>
<style lang="less">
@import "../libs/assets/less/editor-app";
.footer-nav {
  .el-radio-button__inner {
    // width: 106px;
    padding: 12px !important;
  }
}
</style>
