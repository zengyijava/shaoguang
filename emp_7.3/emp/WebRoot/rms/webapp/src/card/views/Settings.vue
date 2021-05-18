<template>
  <aside class="property" :class="{'show': element.active}">
    <Panel :title="title" @close="handleClose()">
      <el-form slot="body" v-model="element">
        <RichTextSetting v-if="element.type === 'text'"></RichTextSetting>
        <ButtonSetting v-else-if="element.type === 'button'"></ButtonSetting>
        <CardImageSetting v-else-if="element.type === 'image'"></CardImageSetting>
        <QrCodeSetting v-else-if="element.type === 'qrcode'"></QrCodeSetting>
        <AudioSetting v-else-if="element.type === 'audio'"
          :element="element" @upload-success="updateAudio"></AudioSetting>
        <VideoSetting v-else-if="element.type === 'video'"
          :element="element" @upload-success="updateVideo"></VideoSetting>
      </el-form>
    </Panel>
  </aside>
</template>

<script>
import { mapGetters, mapMutations } from 'vuex'
import RichTextSetting from './RichTextSetting'
import ButtonSetting from './ButtonSetting'
import CardImageSetting from './CardImageSetting'
import AudioSetting from '../../libs/views/AudioSetting'
import QrCodeSetting from './QrCodeSetting'
import VideoSetting from '../../libs/views/VideoSetting'
import TitleSetting from './TitleSetting'
import Panel from '../../libs/components/Panel'

export default {
  name: 'Settings',
  components: { TitleSetting, CardImageSetting, AudioSetting, VideoSetting, ButtonSetting, RichTextSetting, Panel, QrCodeSetting },
  data () {
    return {
      visible: true
    }
  },
  computed: {
    ...mapGetters(['element']),
    title () {
      const type = this.element.type
      switch (type) {
        case 'text':
          return this.$t('setting.text')
        case 'button':
          return this.$t('setting.button')
        case 'image':
          return this.$t('setting.image')
        case 'qrcode':
          return this.$t('setting.qrcode')
        case 'audio':
          return this.$t('setting.audio')
        case 'video':
          return this.$t('setting.video')
        default:
          return ''
      }
    }
  },
  methods: {
    ...mapMutations(['updateAudio', 'updateVideo']),
    handleClose () {
      this.element.active = false
    }
  }
}
</script>

<style lang="less" scoped>
@import '../../libs/assets/less/variables';

.property {
  position: fixed;
  top: 80px;
  bottom: 20px;
  width: 350px;
  right: -350px;
  overflow-y: hidden;
  border: solid 1px @border;
  border-radius: 2px;
  background-color: @white-light;
  z-index: 2;
  transition: right 0.5s ease-in-out;
  &.show {
    right: 20px;
  }
}
</style>
