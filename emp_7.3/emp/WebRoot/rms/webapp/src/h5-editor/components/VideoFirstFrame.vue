<template>
  <div>
    <video
      ref="myVideo"
      :src='videoContent.src'
      :poster="videoContent.fistFramePath"
      @ended="endedVideo"
      :style="{'width' : videoContent.w + 'px', 'height' : videoContent.h + 'px'}"
      >
    </video>
    <img v-if="previewFrom === 'other'" class="videoBtn" @click="clickToPlay"
      :src="playIcon"
      @mouseenter="enter"
      @mouseleave="leave"
      :style="{
        'width' : w + 'px',
        'height' : h + 'px',
        'top': (videoContent.h - h) / 2 + 'px',
        'left' : (videoContent.w - w) / 2 + 'px',
        'transition' : '0.5s', 'opacity' : controlOpacity
      }"/>
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
    };
  },
  methods: {
    endedVideo () {
      let ev = event.target
      ev.nextElementSibling.src = this.playIcon
      this.controlOpacity = 1
    },
    clickToPlay () {
      let ev = event.target
      if (this.swiperPage > 1) {
        return false
      } else {
        let myVideo = this.$refs.myVideo
        if (myVideo.paused) {
          ev.src = this.stopIcon
          myVideo.play()
        } else {
          ev.src = this.playIcon
          myVideo.pause()
        }
      }
    },
    enter () {
      // 利用透明度，控制播放按钮的显示和隐藏
      this.controlOpacity = 1
      this.transitionTime = '0.5s'
    },
    leave () {
      this.controlOpacity = 0
    }
  }
};
</script>

<style lang="less">
// .controls {
//   position: absolute;
//   top: 0;
//   height: 0;
//   img {
//     position: absolute;
//     cursor: pointer;
//   }
// }
.videoBtn {
  position: absolute;
  cursor: pointer;
}
</style>
