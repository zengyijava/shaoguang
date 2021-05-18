<template>
  <el-upload class="media-upload" :action="url" :show-file-list="false" :accept="fileType"
    :on-progress="progressFun" :beforeUpload="beforeAvatarUpload" :on-success="handleUploadSuccess">
    <slot></slot>
  </el-upload>
</template>

<script>

import config from '../config'
import AJAXURL from '../ajax.address'
import {mapGetters, mapMutations} from 'vuex'
export default {
  name: 'MediaPicker',
  props: {
    type: {
      type: String,
      default: 'audio'
    },
    single: {
      type: Boolean,
      default: false
    }
  },
  data () {
    return {
      url: AJAXURL.UPLOAD_VIDEO
    }
  },
  computed: {
    ...mapGetters(['content']),
    fileType () {
      let type = ''
      if (this.type === 'audio') {
        type = config.AUDIO_FORMAT.join(',')
      } else {
        type = config.VIDEO_FORMAT.join(',')
      }
      return type
    }
  },
  methods: {
    ...mapMutations(['setLoading']),
    beforeAvatarUpload (file) {
      const type = this.type
      if (this.single && this.content.elements[type + 's'].length > 0) {
        this.$message.warning(this.$t('navBar.add_' + type + '_tips'))
        return false
      }
      let configSize = config.MEDIA_MAX_SIZE
      if (file.size > configSize) {
        this.$message({
          message: this.$t('public.mediaUpTips'),
          type: 'warning'
        })
        return false
      } else {
        this.setLoading(true)
      }
    },
    progressFun (event, file, fileList) {
      console.log(Math.floor(event.percent))
    },
    handleUploadSuccess (res, file) {
      if (res.state === 'SUCCESS') {
        this.$emit('on-success', res)
      } else {
        this.setLoading(false)
        this.$message({
          message: res.state,
          type: 'error'
        })
      }
    }
  }
}

</script>
