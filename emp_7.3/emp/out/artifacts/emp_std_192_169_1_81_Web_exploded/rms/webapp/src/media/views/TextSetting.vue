<template>
  <el-form class="text-setting">
    <el-form-item>
      <el-tabs type="border-card">
        <el-tab-pane :label="$t('property.params.insertArgument')" class="copy">
          <div class="form-item">
            <el-form-item v-if="hasTemplate">
              <ParamBtnGroup></ParamBtnGroup>
            </el-form-item>
            <InsertParams v-else></InsertParams>
          </div>
        </el-tab-pane>
        <el-tab-pane class="upload" :disabled="isPicClick" :label="$t('media.pImg')">
          <span class="form-item">{{ $t('media.pUpImg') }}</span>
          <ImageSetting :element="element" @cutImg="visible = true">
            <template slot-scope="{imgSrc}">
              <ImageTextLayer :image-src="imgSrc" :open="visible" @closePanel="visible = false"></ImageTextLayer>
            </template>
          </ImageSetting>
        </el-tab-pane>
      </el-tabs>
    </el-form-item>

  </el-form>
</template>

<script>
import { mapGetters, mapMutations } from 'vuex'
import InsertParams from '../../libs/components/InsertParams'
import ImageTextLayer from './ImageTextLayer'
import ImagePicker from '../../libs/components/ImagePicker'
import ImageSetting from '../../libs/views/ImageSetting'
import ParamBtnGroup from '../components/ParamBtnGroup'

export default {
  name: 'TextSetting',
  components: {ParamBtnGroup, ImageSetting, ImagePicker, InsertParams, ImageTextLayer},
  data () {
    return {
      textarea: '',
      isChange: true,
      visible: false,
      isAppendToBody: true,
      dialogTableVisible: false,
      imgContainerMaxWidth: 500,
      imgContainerMaxHeight: 505,
      imageWidth: 0,
      imageHeight: 0
    }
  },
  computed: {
    ...mapGetters('media', ['element']),
    hasTemplate () {
      return this.$store.getters.element
    },
    currentImgUrl () {
      return this.element.image.src
    },
    isPicClick () {
      let isClick
      if (this.element.src) {
        isClick = true
      } else {
        isClick = false
      }
      return isClick
    },
    isChartClick () {
      let isClick
      if (this.element.image.src) {
        isClick = true
      } else {
        isClick = false
      }
      return isClick
    }
  },
  methods: {
    ...mapMutations(['setImageSize']),
    cutImg () {
      this.isChange = false
      this.dialogTableVisible = true
      let openImg = new Image()
      openImg.src = this.currentImgUrl
      let scale = (openImg.width / openImg.height).toFixed(2)
      if (scale > 1) {
        if (openImg.width > this.imgContainerMaxWidth) {
          this.imgWidth = this.imgContainerMaxWidth
        } else {
          this.imgWidth = openImg.width
        }
        this.imgHeight = this.imgWidth / scale
      } else {
        if (openImg.height > this.imgContainerMaxHeight) {
          this.imgHeight = this.imgContainerMaxHeight
        } else {
          this.imgHeight = openImg.height
        }
        this.imgWidth = this.imgHeight * scale
      }
      this.setImageSize({
        width: parseInt(this.imgWidth),
        height: parseInt(this.imgHeight)
      })
    },
    closePanel () {
      this.dialogTableVisible = false
    }
  }
}
</script>
<style lang="less">
.text-setting {
  .upload {
    .form-item {
      padding-left: 20px;
    }
    .image-setting {
      .media-panel {
        padding-top: 0;
        .media-preview {
          width: 260px;
          height: 260px;
          .img {
            width: 260px;
            height: 260px;
          }
        }
      }
      .media-upload {
        .el-button {
          width: 150px !important;
        }
      }
      .el-button {
        &.is-plain {
          width: 100px !important;
        }
      }
    }
  }
}
</style>
