<template>
  <NavBar :is-card="true" :title="$t('navBar.sceneEditing')">
    <ul class="menu-bar" slot>
      <TemplatePicker @getTempDetail="getTempDetail"></TemplatePicker>
      <li @click="addText">
        <i class="iconfont icon-text"></i>{{ $t('navBar.text') }}
      </li>
      <li>
        <ImagePicker @on-success="handleSuccess">
          <i class="iconfont icon-picture"></i>{{ $t('navBar.img') }}
        </ImagePicker>
      </li>
      <li>
        <MediaPicker :single="true" @on-success="addAudio">
          <i class="iconfont icon-music"></i>{{ $t('navBar.audio') }}
        </MediaPicker>
      </li>
      <li>
        <MediaPicker type="video" :single="true" @on-success="addVideo">
          <i class="iconfont icon-video"></i>{{ $t('navBar.video') }}
        </MediaPicker>
      </li>
      <li @click="addQrCode">
        <i class="iconfont icon-code"></i>{{ $t('navBar.qrCode') }}
      </li>
      <li @click="addButton"><i class="iconfont icon-button"></i>{{ $t('navBar.button') }}</li>
      <!--<li @click="showStyle"><i class="iconfont icon-card"></i>卡片样式</li>-->
    </ul>
  </NavBar>
</template>

<script>
import {mapGetters, mapActions, mapMutations} from 'vuex'
import utils from '../../libs/utils'
import ImagePicker from '../../libs/components/ImagePicker'
import MediaPicker from '../../libs/components/MediaPicker'
import TemplatePicker from '../../libs/components/TemplatePicker'
import NavBar from '../../libs/views/NavBar'
import config from '../../libs/config'

let count = 0
const element = {
  locked: false,
  visible: true,
  active: true,
  x: 10,
  y: 20
}

export default {
  name: 'CardNavBar',
  components: {TemplatePicker, NavBar, MediaPicker, ImagePicker},
  computed: {
    ...mapGetters(['content', 'elements']),
    length () {
      return this.elements.length
    }
  },
  methods: {
    ...mapActions(['addTemplate', 'getTempDetail', 'setLoading']),
    ...mapMutations(['setLoading', 'addHistory', 'selectElement']),
    addText () {
      count++
      const text = {
        ...element,
        w: 240,
        z: this.length,
        type: 'text',
        tag: this.$t('navBar.text') + count,
        text: this.$t('richText.dbClick'),
        style: {
          fontSize: '14px',
          fontWeight: 'normal',
          textAlign: 'left',
          color: '#333333',
          backgroundColor: ''
        }
      }
      this.content.elements.texts.push(text)
      this.selectElement(text)
      this.addHistory(this.content.elements)
    },
    // 添加按钮
    addButton () {
      count++
      const buttons = this.content.elements.buttons
      if (buttons.length < 3) {
        const btn = {
          ...element,
          x: 90,
          y: 220,
          w: 100,
          h: 32,
          z: this.length,
          type: 'button',
          tag: this.$t('navBar.button') + count,
          text: this.$t('buttonSetting.buttonName'),
          action: '2',
          tabsAction: '2',
          style: {
            fontSize: '14px',
            textAlign: 'center',
            color: '#ffffff',
            backgroundColor: '#2e95ff',
            borderRadius: 0
          }
        }
        buttons.push(btn)
        this.selectElement(btn)
        this.addHistory(this.content.elements)
      }
    },

    // 添加图片
    addImage (payload) {
      count++
      const image = {
        ...element,
        z: this.length,
        type: 'image',
        tag: this.$t('navBar.img') + count,
        src: payload.url,
        ratio: payload.ratio,
        width: payload.width,
        height: payload.height,
        size: payload.size,
        w: utils.getFitWidth(payload),
        h: utils.getFitHeight(payload),
        action: '0',
        url: '',
        borderRadius: 0
      }
      this.content.elements.images.push(image)
      this.selectElement(image)
      this.addHistory(this.content.elements)
    },

    // 添加二维码
    addQrCode () {
      let QrCodes = this.content.elements.qrcodes
      if (QrCodes.length > 0) {
        this.$message.warning(this.$t('navBar.add_qrcode_tips'))
        return false
      }
      let QrCode = {
        ...element,
        z: this.length,
        type: 'qrcode',
        tag: this.$t('navBar.qrCode'),
        src: config.QRCODE_IMG_PLACEHOLDER_URL,
        w: 88,
        h: 88,
        ratio: 1,
        size: 1.34 * 1024,
        borderRadius: 0
      }
      QrCodes.push(QrCode)
      this.selectElement(QrCode)
      this.addHistory(this.content.elements)
    },

    // 添加音频
    addAudio (payload) {
      count++
      this.setLoading(false)
      const media = {
        ...element,
        z: this.length,
        type: 'audio',
        tag: this.$t('navBar.audio') + count,
        src: payload.url,
        duration: 0,
        filename: payload.original,
        title: payload.original.slice(0, 20),
        size: payload.size,
        w: 240,
        h: 70,
        style: {
          color: '#333333',
          backgroundColor: '#ffffff',
          transparency: 0
        }
      }
      this.content.elements.audios.push(media)
      this.selectElement(media)
      this.addHistory(this.content.elements)
    },

    // 添加视频
    addVideo (payload) {
      count++
      this.setLoading(false)
      const media = {
        ...element,
        z: this.length,
        type: 'video',
        tag: this.$t('navBar.video') + count,
        src: payload.url,
        duration: 0,
        size: payload.size,
        w: utils.getFitWidth(payload),
        h: utils.getFitHeight(payload)
      }
      this.content.elements.videos.push(media)
      this.selectElement(media)
      this.addHistory(this.content.elements)
    },
    handleSuccess (event) {
      this.addImage(event)
    }
  }
}
</script>

<style lang="less">
  @import '../../libs/assets/less/menu-bar';
  .editor {
    .nav-bar {
      .logo {
        display: table-cell;
        width: 120px;
      }
    }
  }
</style>
