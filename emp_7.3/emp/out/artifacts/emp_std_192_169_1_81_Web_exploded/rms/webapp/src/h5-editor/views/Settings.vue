<template>
  <aside class="property" :class="{'show': element.active && !element.locked || isBackGround}">
    <Panel :title="title" @close="handleClose()">
      <el-form slot="body" v-model="element">
        <BackGroundSetting v-if="isBackGround"></BackGroundSetting>
        <RichTextSetting v-else-if="element.type === 'text'"></RichTextSetting>
        <ButtonSetting v-else-if="element.type === 'button'"></ButtonSetting>
        <H5ImageSetting v-else-if="element.type === 'image'"></H5ImageSetting>
        <AudioSetting
          v-else-if="element.type === 'audio'"
          :element="element"
          :tmType="15"
          @replaceAudio="setAddType('audio')">
        </AudioSetting>
        <VideoSetting
          v-else-if="element.type === 'video'"
          :element="element"
          :tmType="15"
          @replaceVideo="setAddType('video')">
        </VideoSetting>
        <MusicSetting
          v-else-if="element.type === 'music'"
          :element="element"
          :tmType="15"
          @replaceMusic="setAddType('music')">
        </MusicSetting>
      </el-form>
    </Panel>
  </aside>
</template>

<script>
import { mapGetters, mapMutations } from 'vuex'
import RichTextSetting from './RichTextSetting'
import ButtonSetting from './ButtonSetting'
import H5ImageSetting from './H5ImageSetting'
import AudioSetting from '../../libs/views/AudioSetting'
import VideoSetting from '../../libs/views/VideoSetting'
import Panel from '../../libs/components/Panel'
import MusicSetting from './MusicSetting'
import BackGroundSetting from './BackGroundSetting'

export default {
  name: 'Settings',
  components: {MusicSetting, H5ImageSetting, AudioSetting, VideoSetting, ButtonSetting, RichTextSetting, Panel, BackGroundSetting},
  data () {
    return {
      visible: true
    }
  },
  computed: {
    ...mapGetters(['element', 'addType', 'isBackGround']),
    title () {
      if (this.isBackGround) {
        return this.$t('H5.bg_setting')
      } else {
        const type = this.element.type
        switch (type) {
          case 'text':
            return this.$t('H5.text_setting')
          case 'button':
            return this.$t('H5.button_setting')
          case 'image':
            return this.$t('H5.img_setting')
          case 'audio':
            return this.$t('H5.audio_setting')
          case 'video':
            return this.$t('H5.video_setting')
          case 'music':
            return this.$t('H5.bg_audio_setting')
          case 'bgImage':
            return this.$t('H5.bg_setting')
          default:
            return ''
        }
      }
    }
  },
  methods: {
    ...mapMutations(['setAddType', 'setBgState']),
    handleClose () {
      this.element.active = false
      this.setBgState(false)
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
