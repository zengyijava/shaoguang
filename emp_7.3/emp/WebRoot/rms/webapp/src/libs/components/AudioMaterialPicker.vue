<template>
  <el-upload class="media-upload" :action="url" :show-file-list="false" :accept="audioType" :beforeUpload="beforeAvatarUpload" :on-success="handleUploadSuccess">
    <slot></slot>
  </el-upload>
</template>

<script>
import config from '../config'
import AJAXURL from '../ajax.address'

export default {
  name: 'AudioMaterialPicker',
  data () {
    return {
      url: AJAXURL.ADD_FODDER,
      audioType: config.AUDIO_FORMAT.join(',')
    }
  },
  methods: {
    beforeAvatarUpload (file) {
      let configSize = config.MEDIA_MAX_SIZE
      if (file.size > configSize) {
        this.$message({
          message: this.$t('H5.up_audio_max_hint'),
          type: 'warning'
        })
        return false
      }
    },
    handleUploadSuccess (res, file) {
      if (res.code === 200) {
        let data = res.data
        data.active = false
        this.$emit('on-success', data)
      } else {
        this.$message.error(res.msg)
      }
    }
  }
}

</script>
