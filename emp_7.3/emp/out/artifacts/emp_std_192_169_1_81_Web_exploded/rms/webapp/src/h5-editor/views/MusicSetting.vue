<template>
  <el-form ref="form" :model="music" class="audio-setting text-align-right">
    <el-form-item :label="$t('H5.open_bg_audio')">
      <el-switch v-model="music.active"></el-switch>
    </el-form-item>
    <template v-if="music.active">
      <label class="form-item-title">{{ $t('H5.current_audio') }}：<span class="ellipsis">{{music.filename}}</span></label>
      <audio :src="music.src" type="audio/mpeg" controls @durationchange="changeDuration">
        {{ $t('public.audioHint') }}
      </audio>
      <el-button v-if="tmType === 15" class="center-block full-btn" type="primary" size="small" @click="replaceMusic">{{ $t('richText.changeAudio') }}</el-button>
      <MediaPicker v-else @on-success="changeAudio($event)">
        <el-button class="center-block" type="primary" size="small">{{ $t('richText.changeAudio') }}</el-button>
      </MediaPicker>
      <el-form-item :label="$t('H5.auto_play')">
        <el-switch v-model="music.autoPlay"></el-switch>
      </el-form-item>
      <el-form-item :label="$t('H5.loop_play')">
        <el-switch v-model="music.loop"></el-switch>
      </el-form-item>
      <el-form-item :label="$t('H5.show_in_all')">
        <el-switch v-model="music.reused" @change="musicSwitch($event)"></el-switch>
      </el-form-item>
    </template>
  </el-form>
</template>

<script>
import {mapGetters} from 'vuex'
import MediaPicker from '../../libs/components/MediaPicker'

export default {
  name: 'MusicSetting',
  components: {MediaPicker},
  data () {
    return {
      active: true
    }
  },
  props: {
    isCard: {
      type: Boolean,
      default: true
    },
    element: {
      type: Object,
      default: () => {}
    },
    tmType: {
      type: Number,
      default: 0
    }
  },
  computed: mapGetters(['music', 'pages', 'currentPage']),
  methods: {
    changeAudio ($event) {
      this.$emit('upload-success', $event)
    },
    changeDuration (e) {
      this.element.duration = e.target.duration
    },
    replaceMusic () {
      this.$emit('replaceMusic')
    },
    musicSwitch ($event) {
      this.pages.forEach(page => {
        page.elements.forEach(item => {
          if (item.type === 'music') {
            if (item.tag) {
              item.visible = true
            } else {
              item.visible = $event
            }
          }
        })
      })
    }
  }
}
</script>

<style lang="less">
  @import '../../libs/assets/less/audio-setting.less';
  .text-align-right {
    .el-form-item__content {
      text-align: right;
    }
  }
  .audio-setting .full-btn{
    width: 310px;
  }
</style>
