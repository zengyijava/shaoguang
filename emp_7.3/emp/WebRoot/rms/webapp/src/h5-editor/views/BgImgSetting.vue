<template>
  <div class="image-setting">
    <div class="media-panel">
      <div class="media-preview">
        <slot v-if="imgSrc" :imgSrc="imgSrc"></slot>
      </div>
      <div class="buttons" v-if="imgSrc">
        <el-button type="primary" size="small" @click.stop="setAddType('bgImage')">
          {{ $t('richText.replacePicture') }}
        </el-button>
        <el-button type="primary" size="small" @click.stop="removeBgImg" plain>{{ $t('richText.remove') }}</el-button>
      </div>
      <el-button v-if="!imgSrc" type="text" class="add-img-btn" icon="el-icon-plus"
        @click.stop="setAddType('bgImage')">
      </el-button>
    </div>
  </div>
</template>

<script>
import {mapGetters, mapMutations} from 'vuex'
import ImagePicker from '../../libs/components/ImagePicker'

export default {
  name: 'BgImgSetting',
  components: {ImagePicker},
  computed: {
    ...mapGetters(['background']),
    imgSrc () {
      return this.background.src
    }
  },
  methods: mapMutations(['setAddType', 'removeBgImg'])
}
</script>
<style lang="less" scoped>
  @import '../../libs/assets/less/media-panel.less';
  .media-panel {
    position: relative;
  }
  .add-img-btn {
    position: absolute;
    left: 50%;
    top: 50%;
    margin-left: -16px;
    margin-top: -16px;
    font-size: 32px;
    color: #999;
  }
</style>
