<template>
  <div class="preview">
    <div class="phone-preview">
      <div class="phone-box">
        <el-tooltip effect="dark" :content="$t('H5.back_btn')" placement="right-start">
          <el-button type="text" class="btn-back" icon="el-icon-arrow-left" @click="step = 1"></el-button>
        </el-tooltip>
        <span v-if="step === 2" class="preview-title ellipsis">{{app.title.text}}</span>
        <!--步骤一-->
        <div class="phone-content" v-if="step===1">
          <div class="msg-content">
            <div class="msg-icon icon icon-my-logo"></div>
            <p class="msg-time">{{ new Date()|formatDate }}</p>
          </div>
          <div class="card" @click="step = 2"
            :style="{'width': app.width + 'px'}">
            <div class="title" ref="title" :style="titleStyle">
              {{app.title.text || $t('H5.title_hint')}}
            </div>
            <div class="row">
              <div v-if="app.cover.src" class="avatar" ref="cover" :style="avatarStyle"></div>
              <div v-else class="default-avatar"></div>
              <div class="description" ref="description">{{app.description.text || $t('H5.description_hint')}}</div>
            </div>
          </div>
          <div class="touch"></div>
        </div>
        <!--步骤二-->
        <H5Preview v-else
          :pages="pages"
          :music="music"
          :actual-width="310"
          :swiper="swiper">
        </H5Preview>
      </div>
    </div>
    <slot></slot>
  </div>
</template>
<script>
import {mapGetters} from 'vuex'
import filters from '../../libs/filters'
import H5Preview from '../../libs/views/H5Preview'

export default {
  name: 'Preview',
  components: {H5Preview},
  props: {
    closed: {
      type: Boolean,
      default: true
    }
  },
  created () {
    this.titleHeight = this.app.title.style.height
    this.appHeight = this.app.height
    this.styleTop = this.app.description.style.top
  },
  data () {
    return {
      step: 1,
      titleHeight: 0,
      appHeight: 0,
      styleTop: 0
    }
  },
  computed: {
    ...mapGetters(['app', 'pages', 'music', 'swiper']),
    titleStyle () {
      const style = this.app.title.style
      return {
        'padding-left': style.left + 'px',
        'padding-top': style.top + 'px',
        'width': style.width + 'px',
        'font-size': style.fontSize + 'px',
        'color': style.color
      }
    },
    avatarStyle () {
      return {
        'background-image': 'url("' + this.app.cover.src + '")'
      }
    }
  },
  watch: {
    closed (newValue, oldValue) {
      if (newValue) {
        this.step = 1
      }
    },
    'app.title.text' () {
      this.$nextTick(() => {
        const rect = this.$refs.title.getBoundingClientRect()
        const h = Math.round(rect.height) - this.app.title.style.top
        const diff = h - this.titleHeight
        this.app.height = this.appHeight + diff
        this.app.title.style.height = h
        this.app.description.style.top = this.styleTop + diff
        this.app.cover.style.top = this.styleTop + diff
      })
    },
    'app.description.text' () {
      this.$nextTick(() => {
        const rect = this.$refs.description.getBoundingClientRect()
        this.app.description.style.height = rect.height
      })
    }
  },
  filters
}
</script>

<style lang="less">
  @import "../../libs/assets/less/variables";

  .preview {
    height: 730px;
    .phone-preview {
      position: relative;
      padding: 60px 60px 60px 40px;
      float: left;
      border-right: 1px solid @border;
      .phone-box {
        width: 320px;
        height: 578px;
        overflow: hidden;
        background: url('../assets/img/phone.png') no-repeat;
      }
      h5, p {
        margin: 0;
        padding: 0;
        font-weight: normal;
      }
      .btn-back {
        margin-top: 22px;
        margin-left: 12px;
        .el-icon-arrow-left {
          font-size: 18px;
          line-height: 18px;
        }
      }
      .preview-title {
        display: inline-block;
        width: 260px;
        text-align: center;
        line-height: 14px;
        vertical-align: baseline;
      }
      .phone-content {
        width: 310px;
        height: 520px;
        margin-left: 8px;
        overflow-y: auto;
        overflow-x: hidden;
      }
      .msg-content {
        position: relative;
        padding-left: 40px;
        margin-top: 20px;
        font-size: 12px;
        color: #8c8c8c;
        line-height: 1.3;
        .msg-icon {
          position: absolute;
          top: 20px;
          left: 4px;
          z-index: 2;
          width: 30px;
          height: 30px;
          background: url("../assets/img/head.png") no-repeat center center;
          background-size: contain;
        }
      }
      .card {
        margin-top: 6px;
        margin-left: 38px;
        background-color: #fff;
        border: 1px solid #e5e5e5;
        border-radius: 16px;
        line-height: 1.3;
        word-break: break-all;
        cursor: pointer;
        overflow: hidden;
        .row {
          display: table;
          align-items: start;
          padding: 8px 8px 12px 12px;
          .default-avatar {
            display: table-cell;
            padding: 12px;
            border-radius: 4px;
            background: #efefef url("../../libs/assets/img/logo_icon_small.png") no-repeat 12px 12px;
            width: 36px;
            height: 36px;
          }
          .avatar {
            display: table-cell;
            width: 60px;
            height: 60px;
            border-radius: 4px;
            background-repeat: no-repeat;
            background-size: cover;
          }
          .description {
            padding-left: 8px;
            font-size: 12px;
            color: @grey-dark;
          }
        }
      }
      .touch {
        margin-top: -16px;
        margin-left: 260px;
        width: 26px;
        height: 26px;
        background: url("../../libs/assets/img/click_icon.png") no-repeat;
      }
    }
  }

</style>
