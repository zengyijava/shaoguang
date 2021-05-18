<template>
  <el-upload class="media-upload" :action="url" :show-file-list="false" :accept="imgType" :beforeUpload="beforeAvatarUpload"
    :on-success="handleUploadSuccess">
    <slot></slot>
  </el-upload>
</template>

<script>
import config from '../config'
import AJAXURL from '../ajax.address'

export default {
  name: 'ImagePicker',
  data () {
    return {
      url: AJAXURL.UPLOAD_IMAGE,
      imgType: config.IMAGE_FORMAT.join(',')
    }
  },
  methods: {
    beforeAvatarUpload (file) {
      let configSize = 5242880
      if (file.size > configSize) {
        this.$message({
          message: this.$t('public.imgUpTips'),
          type: 'warning'
        })
        return false
      }
    },
    handleUploadSuccess (res, file) {
      if (res.state === 'SUCCESS') {
        this.$emit('on-success', res)
      } else {
        this.$message.error(res.state)
      }
    }
  }
}

</script>
