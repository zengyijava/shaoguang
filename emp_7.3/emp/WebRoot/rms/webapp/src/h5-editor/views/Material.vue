<template>
  <aside class="left-property" :class="{'show':(addType.length>0)}">
    <LeftPanel :title="title" @close="handleClose()">
      <el-form slot="leftBody">
        <ImageMaterial v-if="addType==='image' || addType==='bgImage'" @closePanel="handleClose"></ImageMaterial>
        <AudioMaterial v-else-if="addType==='audio'||addType==='music'" @closePanel="handleClose"></AudioMaterial>
        <VideoMaterial v-else-if="addType==='video'" @closePanel="handleClose"></VideoMaterial>
      </el-form>
    </LeftPanel>
  </aside>
</template>
<script>
import ImageMaterial from './ImageMaterial'
import AudioMaterial from './AudioMaterial'
import VideoMaterial from './VideoMaterial'
import LeftPanel from '../../libs/components/LeftPanel'
import { mapGetters, mapMutations } from 'vuex'
export default {
  name: 'Material',
  components: { ImageMaterial, AudioMaterial, VideoMaterial, LeftPanel },
  computed: {
    ...mapGetters(['addType']),
    title () {
      const type = this.addType
      switch (type) {
        case 'image':
          return this.$t('H5.img_material')
        case 'bgImage':
          return this.$t('H5.img_material')
        case 'audio':
          return this.$t('H5.audio_material')
        case 'music':
          return this.$t('H5.bg_audio_material')
        case 'video':
          return this.$t('H5.video_material')
        default:
          return ''
      }
    }
  },
  methods: {
    ...mapMutations(['setAddType']),
    handleClose () {
      this.setAddType('')
    }
  }
}
</script>
<style lang="less" scoped>
@import '../../libs/assets/less/variables';

.left-property {
  position: fixed;
  display: none;
  top: 60px;
  bottom: 20px;
  height: 100%;
  width: 418px;
  left: 0;
  overflow-y: hidden;
  border: solid 1px @border;
  border-radius: 2px;
  background-color: @white-light;
  z-index: 20;
  transition: right 0.5s ease-in-out;
}
.show {
  display: block;
}
</style>
