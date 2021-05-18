<template>
  <div
    class="h5-preview-page"
    :style="transformObject">
    <!-- 背景 -->
    <BgPreview :background="previewData.background" :actual-width="320"></BgPreview>
    <!-- 元素组件定位层 -->
    <div
      class="content"
      v-for="element in previewData.elements"
      :key="element.tag"
      :style="getStyle(element)"
      style="word-wrap: break-word">
      <!-- 文本 -->
      <div
        v-if="element.type === 'text' && element.visible"
        class="rich-text"
        v-html="transText(element)">
      </div>
      <!-- 图片 -->
      <img
        v-if="element.type === 'image' && element.visible"
        :src="element.src"
        :style="{width: element.w + 'px',
        height: element.h + 'px',
        'border-radius': element.borderRadius + 'px'}" />
      <!-- 音频 -->
      <div
        v-if="element.type === 'audio' && element.visible"
        class="audio">
        <Audios :element="element" :playSize="'big-audios'" :showScrollName="showScrollName"></Audios>
      </div>
      <!-- 背景音乐 -->
      <div
        v-if="element.type === 'music' && element.visible"
        class="music">
        <i
          class="music-icon"
          :class="{'rotate': rotate}"
          @click="play">
        </i>
      </div>
      <!-- 视频 -->
      <VideoFirstFrame
        v-if="element.type === 'video' && element.visible"
        :swiperPage="swiperPage"
        :videoContent="element"
        :previewFrom="previewFrom">
      </VideoFirstFrame>
      <!-- <video
        v-if="element.type === 'video'"
        :width="element.w"
        :src="element.src">
        您的浏览器不支持Video标签。
      </video> -->
      <!-- 按钮 -->
      <div
        v-if="element.type === 'button' && element.visible"
        class="btn"
        :style="{width: element.w + 'px', lineHeight: element.h + 'px'}">
        <div class="text" v-html="element.text"></div>
      </div>
    </div>
  </div>
</template>

<script>
import VideoFirstFrame from '../VideoFirstFrame'
import BgPreview from '../BgPreview'
import Audios from './Audios'

export default {
  name: 'H5PreviewPage',
  components: {BgPreview, VideoFirstFrame, Audios},
  props: {
    previewData: {
      type: Object,
      default: () => {}
    },
    previewContW: Number,
    rotate: {
      type: Boolean,
      default: false
    },
    previewFrom: {
      type: String,
      default: 'other'
    },
    swiperPage: {
      type: Number
    },
    showScrollName: {
      type: Boolean,
      default: false
    }
  },
  data: function () {
    return {
    }
  },
  computed: {
    // 缩放样式
    transformObject: function () {
      let _widthRatio = this.previewContW / 320
      return {
        transformOrigin: 'left top',
        transform: 'scale(' + _widthRatio + ')',
        backgroundColor: this.previewData.background.color
      }
    },
    // 背景样式
    backgroundObject () {
      return {
        opacity: 1 - this.previewData.background.transparency
      }
    },
    // 组件样式
    getStyle () {
      return (ele) => {
        if (ele.type === 'audio') {
          return {
            zIndex: ele.z,
            width: ele.w + 'px',
            top: ele.y + 'px',
            left: ele.x + 'px'
          }
        } else {
          return {
            ...ele.style,
            zIndex: ele.z,
            width: ele.w + 'px',
            top: ele.y + 'px',
            left: ele.x + 'px'
          }
        }
      }
    }
  },
  methods: {
    play () {
      this.playing = !this.playing
      this.$emit('play', this.playing)
    },
    // 文字替换
    transText: function (textItem) {
      let _params = this.params
      let _paramsLength = _params ? _params.length : 0
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
    }
  }
}
</script>

<style lang="less" scoped>
@keyframes rotate {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}

.h5-preview-page{
  position: relative;
  width: 320px;
  height: 508px;
  .h5-preview-page-bg{
    position: relative;
    height: 100%;
  }
  .content{
    position: absolute;
  }
  .music{
    position: absolute;
    top: 8px;
    left: 280px;
    .music-icon {
      display: block;
      position: absolute;
      top: 0;
      height: 34px;
      width: 34px;
      cursor: pointer;
      background: url(../../assets/img/music_icon.png) no-repeat;
      &.rotate {
        transform-origin: center center;
        animation: rotate 3s linear infinite;
      }
    }
  }
  .rich-text{
    padding: 4px 8px;
  }
}
</style>
