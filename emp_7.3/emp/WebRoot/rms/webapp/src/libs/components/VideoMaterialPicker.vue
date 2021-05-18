<template>
  <el-upload class="media-upload" :action="url" :show-file-list="false" :accept="videoType" :beforeUpload="beforeAvatarUpload" :on-progress="uploadVideoProcess" :on-success="handleUploadSuccess">
    <slot></slot>
    <el-progress v-if="videoFlag" type="circle" :width="36" :stroke-width="2" :percentage="videoUploadPercent" :status="status" style="vertical-align: top;margin-left: 5px"></el-progress>
  </el-upload>
</template>

<script>
import config from '../config'
import AJAXURL from '../ajax.address'

export default {
  name: 'VideoMaterialPicker',
  data () {
    return {
      url: AJAXURL.ADD_FODDER,
      videoType: config.VIDEO_FORMAT.join(','),
      videoFlag: false,
      status: ''
    }
  },
  methods: {
    beforeAvatarUpload (file) {
      let configSize = 31457280
      if (file.size > configSize) {
        this.$message({
          message: this.$t('H5.up_video_max_hint'),
          type: 'warning'
        })
        return false
      }
    },
    uploadVideoProcess (event, file, fileList) {
      let processPer = Math.floor(event.loaded / event.total * 100)
      this.videoFlag = true
      this.videoUploadPercent = processPer
      if (processPer === 100) {
        this.status = 'success'
      }
    },
    handleUploadSuccess (res, file) {
      if (res.code === 200) {
        this.videoFlag = false
        this.videoUploadPercent = 0
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
