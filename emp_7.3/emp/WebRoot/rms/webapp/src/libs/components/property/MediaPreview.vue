<template>
  <div class="media-cont">
    <div v-for="(item, key, index) in content" :key="index" :style="audioPlayerTransform(item)">
      <div class="cont-keyframe" v-if="item.type === 'text'">
        <p v-html="transText(item)"></p>
        <!-- <img v-if="item.chart && item.chart.pictureUrl.length > 0" :src="item.chart.pictureUrl"> -->
        <div v-if="item.image && item.image.src.length > 0" class="img-cont" :style="imgContHeight(item.image)">
          <div class="img-transform-cont" :style="imgTransformObj(item.image)">
            <p v-for="(imgItem, key2, index2) in item.image.textEditable" :key="index2" v-if="imgItem.type === 'text' && imgItem.text.length > 0" :style="imgTextTransformObj(imgItem)" v-html="imgItem.text"></p>
            <img v-for="(imgItem, key2, index2) in item.image.textEditable" :key="index2" :style="imgInnerImgTransformObj(imgItem)" v-if="imgItem.type === 'image'" :src="imgItem.src">
          </div>
        </div>
      </div>
      <div class="cont-keyframe" v-else-if="item.type === 'image'">
        <div v-if="item.src" class="img-cont" :style="imgContHeight(item)">
          <div class="img-transform-cont" :style="imgTransformObj(item)">
            <p v-for="(imgItem, key2, index2) in item.textEditable" :key="index2" :style="imgTextTransformObj(imgItem)" v-if="imgItem.type === 'text' && imgItem.text.length > 0" v-html="imgItem.text"></p>
            <img v-for="(imgItem, key2, index2) in item.textEditable" :key="index2" :style="imgInnerImgTransformObj(imgItem)" v-if="imgItem.type === 'image'" :src="imgItem.src">
          </div>
        </div>
        <p v-if="item.text" v-html="transText(item)"></p>
      </div>
      <!-- <div class="cont-keyframe" v-else-if="item.type === 'chart'">
        <img :src="item.pictureUrl">
      </div> -->
      <!-- <video class="cont-keyframe" v-else-if="item.type === 'video'" :src="item.src" controls="controls"></video> -->
      <VideoFirstFrame :previewFrom="previewFrom" :videoContent="item" v-else-if="item.type === 'video'"></VideoFirstFrame>
      <Audios v-else-if="item.type === 'audio'" class="cont-keyframe" :element="item" :showScrollName="showScrollName"></Audios>
      <!-- <audio class="cont-keyframe"  :src="item.src" controls="controls"></audio> -->
    </div>
  </div>
</template>

<script>
import VideoFirstFrame from '../VideoFirstFrame'
import Audios from './Audios'
export default {
  name: 'MediaPreview',
  data: function () {
    return {
      cutContHeight: 505,
      cutContWidth: 500
    }
  },
  components: { VideoFirstFrame, Audios },
  props: {
    previewData: String,
    previewContW: Number,
    params: {
      type: Array,
      default: () => {
        return []
      }
    },
    previewFrom: {
      type: String,
      default: 'other'
    },
    showScrollName: {
      type: Boolean,
      default: false
    }
  },
  computed: {
    content: function () {
      if (this.previewData) {
        return JSON.parse(this.previewData)
      } else {
        return []
      }
    }
  },
  methods: {
    // 文字替换
    transText: function (textItem) {
      let _params = this.params
      let _paramsLength = _params.length
      let _showText

      if (_paramsLength > 0) {
        let i = 0

        for (i; i < _paramsLength; i++) {
          if (_params[i].tag === textItem.tag) {
            _showText = _params[i].content.replace(/\n/g, '<br/>')
            break
          } else {
            _showText = textItem.text.replace(/\n/g, '<br/>')
          }
        }
      } else {
        _showText = textItem.text.replace(/\n/g, '<br/>')
      }

      return _showText
    },
    // 图片容器高度计算
    imgContHeight: function (imgData) {
      let _scale = imgData.scale || ''
      if (_scale || imgData.width > this.previewContW) {
        return {
          height: Math.floor(this.previewContW / imgData.width * imgData.height) + 'px'
        }
      } else {
        return {
          height: imgData.height + 'px'
        }
      }
    },
    // 图片缩放
    imgTransformObj: function (imgData) {
      if (imgData.width > this.previewContW) {
        return {
          position: 'relative',
          transformOrigin: 'left top',
          transform: 'scale(' + this.previewContW / imgData.width + ')',
          background: 'url("' + imgData.src + '")0% 0% / 100% 100% no-repeat',
          height: imgData.height + 'px',
          width: imgData.width + 'px',
          overflow: 'hidden'
        }
      } else {
        return {
          background: 'url("' + imgData.src + '")0% 0% / 100% 100% no-repeat',
          height: imgData.height + 'px',
          width: imgData.width + 'px',
          overflow: 'hidden'
        }
      }
    },

    // 音频播放器缩放
    audioPlayerTransform: function (audioData) {
      if (audioData.type === 'audio' && audioData.w > this.previewContW) {
        let _scale = this.previewContW / 260
        return {
          position: 'relative',
          transformOrigin: 'center top',
          transform: 'scale(' + _scale + ')',
          height: audioData.h + 'px',
          width: audioData.w + 'px',
          overflow: 'hidden',
          zIndex: '1'
        }
      } else {
        return {
          position: 'relative',
          zIndex: '1'
        }
      }
    },

    // 图片内文字定位
    imgTextTransformObj: function (imgTextData) {
      return {
        position: 'absolute',
        top: imgTextData.y + 'px',
        left: imgTextData.x + 'px',
        height: imgTextData.h + 'px',
        width: imgTextData.w + 'px',
        overflow: 'hidden',
        textAlign: imgTextData.style.textAlign,
        fontSize: imgTextData.style.fontSize,
        lineHeight: imgTextData.style.lineHeight,
        fontFamily: imgTextData.style.fontFamily,
        fontWeight: imgTextData.style.fontWeight,
        fontStyle: imgTextData.style.fontStyle,
        color: imgTextData.style.color,
        backgroundColor: imgTextData.style.backgroundColor,
        transformOrigin: 'center center',
        transform: 'rotate(' + imgTextData.rotate + 'deg)',
        minHeight: '28px',
        boxSizing: 'border-box'
      }
    },
    // 图片内图片定位
    imgInnerImgTransformObj: function (imgTextData) {
      return {
        position: 'absolute',
        top: imgTextData.y + 'px',
        left: imgTextData.x + 'px',
        height: imgTextData.h + 'px',
        width: imgTextData.w + 'px',
        transformOrigin: 'center center',
        transform: 'rotate(' + imgTextData.rotate + 'deg)'
      }
    }
  }
}
</script>

<style scoped lang="less">
.media-cont{
  min-height: 26px;
  img,
  video,
  audio{
    width: 100%
  }
  p{
    padding: 4px 8px;
    word-break: break-all;
  }
  .cont-keyframe{
    margin-bottom: 2px;
    line-height: 1.3;
    &.audios-player{
      margin-left: auto;
      margin-right: auto;
    }
  }
  .img-transform-cont{
    position: relative;
  }
  .img-cont{
    overflow: hidden;
  }
}
</style>
