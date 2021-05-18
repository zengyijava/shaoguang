<template>
  <NavBar :title="$t('media.richMediaEdit')">
    <ul class="menu-bar" slot>
      <TemplatePicker v-if="!hasTemplate" scene-type="11" @getTempDetail="getTempDetail"></TemplatePicker>
      <li @click.stop="addText">
        <i class="iconfont icon-text"></i>{{ $t('navBar.text') }}
      </li>
      <li>
        <ImagePicker @on-success="handleSuccess">
          <i class="iconfont icon-picture"></i>{{ $t('navBar.img') }}
        </ImagePicker>
      </li>
      <!-- <li @click="addChart">
        <i class="iconfont icon-chart"></i>图表
      </li> -->
      <li>
        <MediaPicker @on-success="addAudio">
          <i class="iconfont icon-music"></i>{{ $t('navBar.audio') }}
        </MediaPicker>
      </li>
      <li>
        <MediaPicker type="video" @on-success="addVideo">
          <i class="iconfont icon-video"></i>{{ $t('navBar.video') }}
        </MediaPicker>
      </li>
    </ul>
  </NavBar>
</template>

<script>
import { mapGetters, mapActions, mapMutations } from 'vuex'
import picUrl from '../assets/img/chart.png'
import NavBar from '../../libs/views/NavBar'
import ImagePicker from '../../libs/components/ImagePicker'
import MediaPicker from '../../libs/components/MediaPicker'
import TemplatePicker from '../../libs/components/TemplatePicker'

let count = 0
let chart = {}
const CANVASWIDTH = 260

export default {
  name: 'MediaNavBar',
  components: { TemplatePicker, ImagePicker, MediaPicker, NavBar },
  computed: {
    ...mapGetters(['mediaContent']),
    hasTemplate () {
      return this.$store.getters.element
    }
  },
  methods: {
    ...mapActions(['getTempDetail']),
    ...mapMutations('media', ['addHistory']),
    ...mapMutations(['setLoading']),
    // 添加元素
    addText (payload) {
      count++
      const text = {
        active: true,
        type: 'text',
        text: this.$t('media.pEnterText'),
        tag: 'text_' + count,
        image: {
          src: '',
          width: payload.width,
          height: payload.height,
          ratio: payload.ratio,
          w: CANVASWIDTH,
          h: 0,
          size: 0,
          compoundSize: 0,
          textEditable: []
        },
        src: '',
        chart: {}
      }
      this.mediaContent.push(text)
      this.addHistory(this.mediaContent)
    },

    // 添加图表
    addChart () {
      count++
      const ele = {
        active: true,
        type: 'chart',
        tag: 'chart_' + count,
        src: picUrl,
        w: -1,
        h: 0,
        ...chart
      }
      this.mediaContent.push(ele)
      this.addHistory()
    },

    // 添加图片
    addImage (payload) {
      count++
      let _showW = payload.width >= CANVASWIDTH ? CANVASWIDTH : payload.width
      let _showH = payload.width >= CANVASWIDTH ? Math.floor(CANVASWIDTH / payload.width * payload.height) : payload.height
      const image = {
        active: true,
        type: 'image',
        tag: 'image_' + count,
        src: payload.url,
        size: payload.size,
        compoundSize: 0,
        width: payload.width,
        height: payload.height,
        w: _showW,
        h: _showH,
        borderRadius: 0,
        text: '',
        isShowImgText: 'hide',
        textEditable: []
      }
      this.mediaContent.push(image)
      this.addHistory(this.mediaContent)
    },

    // 添加音频
    addAudio (payload) {
      this.setLoading(false)
      count++
      const media = {
        active: true,
        type: 'audio',
        tag: 'audio_' + count,
        src: payload.url,
        duration: 0,
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
      this.mediaContent.push(media)
      this.addHistory(this.mediaContent)
    },

    // 添加视频
    addVideo (payload) {
      this.setLoading(false)
      count++
      const media = {
        active: true,
        type: 'video',
        tag: 'video_' + count,
        w: CANVASWIDTH,
        h: Math.floor(CANVASWIDTH / payload.width * payload.height),
        src: payload.url,
        width: payload.width,
        height: payload.height,
        duration: payload.duration,
        size: payload.size
      }
      this.mediaContent.push(media)
      this.addHistory(this.mediaContent)
    },

    handleSuccess (event) {
      this.addImage(event)
    }
  }
}
</script>

<style lang="less">
@import '../../libs/assets/less/menu-bar';
</style>
