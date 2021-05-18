<template>
  <div class="card-cont">
    <div :style="transformObject">
      <div v-for="(imgItem, index, key) in imagesElement" :key="key" :style="imgPosObject(imgItem)">
        <img :src="imgItem.src" :style="imgInnerStyleObject(imgItem)">
      </div>
      <Audios v-for="(audioItem, index, key) in audiosElement" :key="key" :element="audioItem" :style="audioPosObject(audioItem)" :showScrollName="showScrollName"></Audios>
      <VideoFirstFrame v-for="(videoItem, index, key) in videosElement" :key="key" :videoContent="videoItem" :style="videoPosObject(videoItem)"></VideoFirstFrame>
      <!-- <video v-for="(videoItem, index, key) in videosElement" :key="key" :style="videoPosObject(videoItem)" :src="videoItem.src" controls="controls"></video> -->
      <div v-for="(qrcodeItem, index, key) in qrcodesElement" :key="key" :style="qrcodesPosObject(qrcodeItem)">
        <img :src="qrcodeItem.src" :style="qrcodeInnerStyleObject(qrcodeItem)">
      </div>
      <p v-for="(textItem, index, key) in textsElement" :key="key" :style="textPosObject(textItem)" v-html="transText(textItem)"></p>
      <el-button v-for="(buttonItem, index, key) in buttonsElement" :key="key" :style="buttonsPosObject(buttonItem)" type="text" size="small">{{ buttonItem.text }}</el-button>
    </div>
  </div>
</template>

<script>
import Audios from './Audios'
import VideoFirstFrame from '../../../libs/components/VideoFirstFrame'

export default {
  name: 'ScenePreview',
  components: { Audios, VideoFirstFrame },
  props: {
    previewData: String,
    previewContW: Number,
    params: {
      type: Array,
      default: () => {
        return []
      }
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
    },

    transformObject: function () {
      let _widthRatio = this.previewContW / this.content.w
      return {
        transformOrigin: 'left top',
        transform: 'scale(' + _widthRatio + ')',
        background: 'url("' + this.content.bgSrc + '")0% 0% / 100% 100% no-repeat',
        height: this.content.h + 'px',
        width: this.content.w + 'px',
        overflow: 'hidden'
      }
    },

    imagesElement: function () {
      if (this.content && this.content.elements && this.content.elements.images) {
        return this.content.elements.images
      } else {
        return []
      }
    },

    audiosElement: function () {
      if (this.content && this.content.elements && this.content.elements.audios) {
        return this.content.elements.audios
      } else {
        return []
      }
    },

    videosElement: function () {
      if (this.content && this.content.elements && this.content.elements.videos) {
        return this.content.elements.videos
      } else {
        return []
      }
    },

    qrcodesElement: function () {
      if (this.content && this.content.elements && this.content.elements.qrcodes) {
        return this.content.elements.qrcodes
      } else {
        return []
      }
    },

    textsElement: function () {
      if (this.content && this.content.elements && this.content.elements.texts) {
        return this.content.elements.texts
      } else {
        return []
      }
    },

    buttonsElement: function () {
      if (this.content && this.content.elements && this.content.elements.buttons) {
        return this.content.elements.buttons
      } else {
        return []
      }
    }
  },
  methods: {
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

    imgPosObject: function (data) {
      return {
        position: 'absolute',
        top: data.y + 'px',
        left: data.x + 'px',
        height: data.h + 'px',
        width: data.w + 'px',
        overflow: 'hidden',
        borderRadius: data.borderRadius + 'px'
      }
    },

    imgInnerStyleObject: function (data) {
      return {
        height: data.h + 'px',
        width: data.w + 'px'
      }
    },

    audioPosObject: function (data) {
      return {
        position: 'absolute',
        top: data.y + 'px',
        left: data.x + 'px',
        height: data.h + 'px',
        width: data.w + 'px',
        overflow: 'hidden'
      }
    },

    videoPosObject: function (data) {
      return {
        position: 'absolute',
        top: data.y + 'px',
        left: data.x + 'px',
        height: data.h + 'px',
        width: data.w + 'px'
      }
    },

    qrcodesPosObject: function (data) {
      return {
        position: 'absolute',
        top: data.y + 'px',
        left: data.x + 'px',
        height: data.h + 'px',
        width: data.w + 'px',
        overflow: 'hidden',
        borderRadius: data.borderRadius + 'px'
      }
    },

    qrcodeInnerStyleObject: function (data) {
      return {
        height: data.h + 'px',
        width: data.w + 'px'
      }
    },

    textPosObject: function (data) {
      let _tag = data.tag || ''
      let _paddingVal
      let w

      if (_tag === 'title') {
        w = data.w
        _paddingVal = '0'
      } else {
        w = data.w - 16
        _paddingVal = '4px 8px'
      }

      return {
        position: 'absolute',
        top: data.y + 'px',
        left: data.x + 'px',
        width: w + 'px',
        padding: _paddingVal,
        overflow: 'hidden',
        textAlign: data.style.textAlign,
        textDecoration: data.style['text-decoration'],
        fontSize: data.style.fontSize,
        fontFamily: data.style.fontFamily,
        fontWeight: data.style.fontWeight,
        fontStyle: data.style.fontStyle,
        color: data.style.color,
        wordBreak: 'break-all'
      }
    },

    buttonsPosObject: function (data) {
      return {
        position: 'absolute',
        top: data.y + 'px',
        left: data.x + 'px',
        height: data.h + 'px',
        width: data.w + 'px',
        borderRadius: data.style.borderRadius + 'px',
        textAlign: data.style.textAlign,
        textDecoration: data.style['text-decoration'],
        fontSize: data.style.fontSize,
        fontFamily: data.style.fontFamily,
        fontWeight: data.style.fontWeight,
        fontStyle: data.style.fontStyle,
        color: data.style.color,
        background: data.style.backgroundColor
      }
    }
  }
}
</script>

<style lang="less">
.card-cont{
  line-height: 1.4;
  overflow: hidden;
  .el-icon-ali-sound{
    width: 32px;
    font-size: 32px;
    color: #2e95ff;
    vertical-align: middle;
    font-family: iconfont!important;
  }
  .el-button + .el-button{
    margin-left: 0;
  }
  .duration{
    display: inline-block;
    font-size: 12px;
    color: #999999;
    vertical-align: middle;
  }
}
</style>
