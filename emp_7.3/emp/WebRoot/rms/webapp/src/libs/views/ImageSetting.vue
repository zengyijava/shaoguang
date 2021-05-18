<template>
  <div class="image-setting">
    <div class="media-panel">
      <div class="media-preview">
        <a v-if="element.action !== '0'" :href="toUrl" target="_blank" rel="noopener noreferrer nofollow" >
          <div class="img" :style="{'background-image': 'url(' + imgSrc + ')'}"></div>
        </a>
        <div v-else class="img" :style="{'background-image': 'url(' + imgSrc + ')'}"></div>
      </div>
      <div class="buttons" v-if="imgSrc">
        <el-button v-if="tmType === 15" type="primary" size="small" @click.stop="$emit('replaceImage')">{{
          $t('richText.replacePicture') }}
        </el-button>
        <image-picker v-else @on-success="handleUploadSuccess">
          <el-button type="primary" size="small">{{ $t('richText.replacePicture') }}</el-button>
        </image-picker>
        <el-button type="primary" size="small" @click.stop="$emit('cutImg')" plain>{{ $t('richText.cut') }}</el-button>
      </div>
      <image-picker v-else @on-success="handleUploadSuccess">
        <i class="el-icon-ali-add"></i>
      </image-picker>
    </div>
    <slot :imgSrc="imgSrc"></slot>
  </div>
</template>

<script>
import {mapMutations} from 'vuex'
import ImagePicker from '../components/ImagePicker'

export default {
  name: 'ImageSetting',
  components: {ImagePicker},
  props: {
    element: {
      type: Object,
      default: () => {
      }
    },
    tmType: {
      type: Number,
      default: 0
    }
  },
  computed: {
    toUrl () {
      return this.element.action === '1' ? this.element.src : this.element.url
    },
    currentImgUrl () {
      return this.element.src
    },
    imgSrc () {
      return this.element.src || (this.element.image && this.element.image.src) || ''
    }
  },
  methods: {
    ...mapMutations('media', ['selectCurrentElement']),
    handleUploadSuccess (res) {
      if (this.element.type === 'text') {
        this.element.image.src = res.url
        this.element.image.size = res.size
        this.element.image.compoundSize = 0
        this.element.image.ratio = res.ratio
        this.element.image.width = res.width
        this.element.image.height = res.height
        this.element.image.h = Math.floor(260 / res.width * res.height)
        this.element.image.scale = ''
      } else {
        this.element.src = res.url
        this.element.size = res.size
        this.element.compoundSize = 0
        this.element.ratio = res.ratio
        this.element.width = res.width
        this.element.height = res.height
        this.element.h = Math.floor(260 / res.width * res.height)
        this.element.scale = ''
      }
    }
  }
}
</script>
<style lang="less" scoped>
  @import '../assets/less/media-panel.less';

  .media-panel {
    position: relative;
  }

  .el-icon-ali-add {
    position: absolute;
    left: 50%;
    top: 50%;
    margin-left: -12px;
    margin-top: -24px;
    font-size: 24px;
    color: #999;
  }
</style>
