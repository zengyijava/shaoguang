<template>
  <div style="position: relative;">
    <!-- <video
      ref="myVideo"
      :src='videoContent.src'
      :poster="videoContent.fistFramePath"
      style="width: 240px; height: 140px; background: #1C0D0D;"
      >
    </video> -->
    <!-- <video
      ref="myVideo"
      :src='videoContent.src'
      :poster="videoContent.fistFramePath"
      :style="{'width' : videoContent.w + 'px', 'height' : videoContent.h + 'px', 'background' : '#1C0D0D'}"
      >
    </video> -->
    <video
      ref="myVideo"
      :src='videoContent.src'
      :poster="videoContent.fistFramePath"
      :style="{'width' : (videoContent.w > 241 ? videoContent.w : 240) + 'px', 'height' : 142 + 'px', 'background' : '#1C0D0D', 'object-fit': 'contain'}"
      >
    </video>
    <img class="videoBtn" @click="clickToPlay"
      :src="playIcon"
      :style="{
        'width' : w + 'px',
        'height' : h + 'px',
        'top': 48 + 'px',
        'left' : (videoContent.w > 241 ? (videoContent.w - w) / 2 : 97) + 'px'
      }"/>
    <!-- <img class="videoBtn" @click="clickToPlay"
      :src="playIcon"
      :style="{
        'width' : w + 'px',
        'height' : h + 'px',
        'top': '47px',
        'left' : '97px'
      }"/> -->
    <!-- (videoContent.w > 241 ? (videoContent.h - h) / 2 : 47) -->
    <!-- <img class="videoBtn" @click="clickToPlay"
      :src="playIcon"
      :style="{
        'width' : w + 'px',
        'height' : h + 'px',
        'top': (videoContent.h - h) / 2 + 'px',
        'left' : (videoContent.w - w) / 2 + 'px'
      }"/> -->
  </div>
</template>

<script>
import playIcon from '../assets/img/play_icon.png';
import stopIcon from '../assets/img/stop_icon.png';

export default {
  props: {
    previewFrom: String,
    videoContent: Object,
    swiperPage: Number
  },
  data () {
    return {
      playing: true,
      w: 46,
      h: 46,
      // 播放按钮透明度
      controlOpacity: 1,
      // 过渡时间
      transitionTime: '0s',
      playIcon: playIcon,
      stopIcon: stopIcon
    }
  },
  methods: {
    clickToPlay () {
      if (this.swiperPage > 1) {
        return false
      } else {
        let myVideo = this.$refs.myVideo
        let ev = event.target
        if (myVideo.paused) {
          myVideo.play()
          ev.style.display = 'none'
          myVideo.setAttribute('controls', true)
        }
      }
    }
  }
}
</script>

<style lang="less">
.videoBtn {
  position: absolute;
  cursor: pointer;
}
</style>
