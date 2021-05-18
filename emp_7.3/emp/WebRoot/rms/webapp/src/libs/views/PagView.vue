<template>
  <article>
    <BgPreview :background="page.background" :actual-width="actualWidth"></BgPreview>
    <div class="page-view" :style="{transform: 'scale('+ actualWidth / 320 +')'}">
      <div class="content" v-for="(element, index) in page.elements" :key="index"
        v-show="element.visible"
        :style="elementStyle(element)">
        <div v-if="element.type === 'text' && element.visible" class="rich-text"
          v-html="element.text" :style="{width: element.w + 'px'}"></div>
        <img v-if="element.type === 'image' && element.visible" :src="element.src"
          :style="{
            width: element.w + 'px',
            height: element.h + 'px',
            'border-radius': element.borderRadius + 'px'
          }" />
        <div v-if="element.type === 'audio' && element.visible" class="audio">
          <Audios :element="element" :swiperPage="swiperPage" :playSize="'big-audios'" :showScrollName="showScrollName"></Audios>
        </div>
        <div v-if="element.type === 'music' && element.visible" class="music">
          <i class="music-icon" :class="{'rotate': rotate}" @click="play"></i>
        </div>
        <VideoFirstFrame v-if="element.type === 'video' && element.visible" :swiperPage="swiperPage" :previewFrom="'other'" :videoContent="element"></VideoFirstFrame>
        <div v-if="element.type === 'button' && element.visible" class="btn" :style="{width: element.w + 'px', height: element.h + 'px'}">
          <div class="text">{{element.text}}</div>
        </div>
      </div>
    </div>
  </article>
</template>

<script>

import BgPreview from '../components/BgPreview'
import VideoFirstFrame from '../components/VideoFirstFrame'
import Audios from '../components/property/Audios'

export default {
  name: 'PageView',
  components: {BgPreview, VideoFirstFrame, Audios},
  props: {
    actualWidth: {
      type: Number,
      default: 320
    },
    page: {
      type: Object,
      default: () => {
      }
    },
    rotate: {
      type: Boolean,
      default: false
    },
    swiperPage: {
      type: Number
    },
    showScrollName: {
      type: Boolean,
      default: false
    }
  },
  computed: {
    elementStyle () {
      return (ele) => {
        if (ele.type === 'audio') {
          return {
            zIndex: ele.z,
            top: ele.y + 'px',
            left: ele.x + 'px'
          }
        } else {
          return {
            ...ele.style,
            zIndex: ele.z,
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
    }
  }
}
</script>

<style lang="less" scoped>

  .page-view {
    width: 320px;
    transform-origin: left top;
    .content {
      position: absolute;
      .rich-text {
        padding: 4px 8px;
        line-height: 1.3;
        word-break: break-all;
        vertical-align: middle;
      }
    }
  }
</style>
