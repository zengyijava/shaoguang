<template>
  <aside class="property" :class="{'show': element.active}">
    <Panel :title="title" @close="handleClose()">
      <el-form slot="body" v-model="element">
        <TextSetting v-if="element.type === 'text'"></TextSetting>
        <MediaImageSetting v-else-if="element.type === 'image'"></MediaImageSetting>
        <AudioSetting
          v-else-if="element.type === 'audio'"
          :showColorSet="false"
          :is-card="false"
          :element="element"
          @upload-success="updateAudio">
        </AudioSetting>
        <VideoSetting v-else-if="element.type === 'video'"
          :is-card="false" :element="element" @upload-success="updateVideo"></VideoSetting>
      </el-form>
    </Panel>
  </aside>
</template>

<script>
import {mapGetters, mapMutations} from 'vuex'
import TextSetting from './TextSetting'
import MediaImageSetting from './MediaImageSetting'
import AudioSetting from '../../libs/views/AudioSetting'
import VideoSetting from '../../libs/views/VideoSetting'
import Panel from '../../libs/components/Panel'

export default {
  name: 'Settings',
  components: {TextSetting, MediaImageSetting, AudioSetting, VideoSetting, Panel},
  data () {
    return {
      visible: true
    }
  },
  computed: {
    ...mapGetters(['mediaTemplate']),
    ...mapGetters('media', ['element']),
    title () {
      const type = this.element.type
      switch (type) {
        case 'text':
          return this.$t('setting.text')
        case 'chart':
          return this.$t('setting.chart')
        case 'image':
          return this.$t('setting.image')
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
    ...mapMutations('media', ['updateAudio', 'updateVideo']),
    handleClose () {
      this.element.active = false
    }
  }
}
</script>
