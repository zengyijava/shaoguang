<template>
  <el-upload class="media-upload"
    :action="url" :show-file-list="false"
    :accept="imgType" :multiple="true"
    :data="{tmpType: templateType}"
    :beforeUpload="beforeAvatarUpload"
    :on-preview="handlePreview"
    :on-success="handleUploadSuccess">
    <slot></slot>
  </el-upload>
</template>

<script>
import { mapGetters } from 'vuex'
import config from '../config'
import AJAXURL from '../ajax.address'

export default {
  name: 'ImageMetarialPicker',
  data () {
    return {
      url: AJAXURL.ADD_FODDER,
      imgType: config.IMAGE_FORMAT.join(',')
    }
  },
  computed: mapGetters(['templateType']),
  methods: {
    beforeAvatarUpload (file) {
      let configSize = 5242880
      if (file.size > configSize) {
        this.$message({
          message: this.$t('H5.up_img_max_hint'),
          type: 'warning'
        })
        return false
      }
    },
    handlePreview (file) {
      console.log(file)
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
