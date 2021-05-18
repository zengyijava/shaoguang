<template>
  <div class="h5-preview">
    <audio type="audio/mpeg"
      v-if="music && music.src"
      ref="audio"
      :src="music.src"
      :autoplay="music.autoPlay"
      :loop="music.loop">
    </audio>
    <template v-if="pages.length > 1">
      <swiper v-if="visible"
        ref="h5Swiper"
        :style="{'width': actualWidth + 'px','height': actualHeight + 'px'}"
        :options="swiperOption"
        @slideChange="changeSlide">
        <swiper-slide v-for="(page, index) in pages" :key="index">
          <PageView
            :page="page"
            :actual-width="actualWidth"
            :rotate="playing"
            :showScrollName="true"
            :swiperPage="pages.length"
            @play="handlePlay">
          </PageView>
        </swiper-slide>
      </swiper>
      <div class="phone-control">
        <el-button-group>
          <el-tooltip effect="dark" :content="$t('H5.preview_prev_btn')" placement="right">
            <el-button class="btn-prev" icon="el-icon-arrow-up"></el-button>
          </el-tooltip>
          <el-tooltip effect="dark" :content="$t('H5.preview_next_btn')" placement="right">
            <el-button class="btn-next" icon="el-icon-arrow-down"></el-button>
          </el-tooltip>
        </el-button-group>
      </div>
      <img src="../assets/img/switch-guide.png" class="switch-guide" :class="swiper.direction">
    </template>
    <template v-else>
      <PageView class="inner"
        :page="pages[0]"
        :actual-width="actualWidth"
        :showScrollName="true"
        :rotate="playing"
        @play="handlePlay">
      </PageView>
    </template>
    <div v-if="swiper.pageAlign"
      class="page"
      :style="{'text-align': swiper.pageAlign}">{{activeIndex}} / {{pages.length}}
    </div>
  </div>
</template>

<script>
import 'swiper/dist/css/swiper.css'
import playIcon from '../assets/img/play_icon.png'
import stopIcon from '../assets/img/stop_icon.png'
import {swiper, swiperSlide} from 'vue-awesome-swiper'
import PageView from './PagView'
import filters from '../filters'

export default {
  name: 'H5Preview',
  components: {PageView, swiper, swiperSlide},
  created () {
    if (this.music) {
      this.playing = this.music.autoPlay
    }
  },
  props: {
    actualWidth: {
      type: Number,
      default: 310
    },
    pages: {
      type: Array,
      default: () => []
    },
    music: {
      type: Object,
      default: () => {
      }
    },
    swiper: {
      type: Object,
      default: () => {
      }
    }
  },
  data () {
    return {
      visible: true,
      playing: true,
      playIcon: playIcon,
      stopIcon: stopIcon,
      activeIndex: 1,
      swiperOption: {
        autoplay: this.swiper.autoPlay,
        direction: this.swiper.direction,
        slidesPerView: 1,
        spaceBetween: 0,
        loop: this.swiper.loop,
        mousewheel: true,
        navigation: {
          nextEl: '.btn-next',
          prevEl: '.btn-prev'
        },
        speed: 600,
        effect: this.swiper.effect,
        grabCursor: true,
        fadeEffect: {
          crossFade: true
        },
        cubeEffect: {
          shadow: false,
          slideShadows: false
        },
        flipEffect: {
          slideShadows: false,
          limitRotation: false
        },
        on: {
          slideChangeTransitionEnd: () => {
            let videos = Array.from(document.querySelectorAll('.swiper-container video'))
            let audios = Array.from(document.querySelectorAll('.swiper-container .j-audio'))
            pauseMedia(videos, 'video')
            pauseMedia(audios, 'audio')

            function pauseMedia (media, type) {
              media.forEach(element => {
                element.pause()
                if (type === 'audio') {
                  let audioBtn = element.previousElementSibling.previousElementSibling;
                  audioBtn.classList.add('paused');
                  audioBtn.classList.remove('play');
                }
              })
            }
          }
        }
      }
    }
  },
  mounted () {
    this.$nextTick(function () {
      if (this.pages.length > 1) {
        let swiper = this.$refs.h5Swiper.$el
        swiper.addEventListener('click', event => {
          let ev = event.target
          if (ev.classList.contains('videoBtn')) {
            let video = ev.previousElementSibling
            if (video.paused) {
              video.play()
            }
            video.addEventListener('timeupdate', () => {
              video.setAttribute('controls', true)
              ev.style.display = 'none'
            })
          }
          if (ev.classList.contains('player-btn')) {
            let music = ev.parentNode.querySelector('audio')
            if (music.paused) {
              music.play()
              ev.classList.add('play')
              ev.classList.remove('paused')
            } else {
              music.pause()
              ev.classList.add('paused')
              ev.classList.remove('play')
            }
            music.addEventListener('timeupdate', () => {
              let parentElement = music.previousElementSibling
              let _progressBar = parentElement.querySelector('.progress-bg')
              let _progressBarW = _progressBar.offsetWidth
              let _showTime = filters.formatTime(music.currentTime)
              let _showBarW = _progressBarW / (music.duration / music.currentTime)
              parentElement.querySelector('.slider-bar').style.left = _showBarW + 'px'
              parentElement.querySelector('.progress-bar').style.width = _showBarW + 'px'
              parentElement.querySelector('.show-time').innerHTML = _showTime
            })
            music.addEventListener('ended', () => {
              ev.classList.add('paused')
              ev.classList.remove('play')
            })
          }
        })
      }
    })
  },
  computed: {
    actualHeight () {
      return Math.floor(this.actualWidth / 320 * 508)
    }
  },
  watch: {
    'swiper.direction' (newValue) {
      this.swiperOption.direction = newValue
      this.reInit()
    },
    'swiper.effect' (newValue) {
      this.swiperOption.effect = newValue
      this.reInit()
    },
    'swiper.loop' (newValue) {
      this.swiperOption.loop = newValue
      this.reInit()
    },
    'swiper.autoPlay' (newValue) {
      this.swiperOption.autoplay = newValue
      this.reInit()
    },
    'swiper.autoPlay.delay' (newValue) {
      this.swiperOption.autoplay.delay = newValue
      this.getSwiper().params.autoplay.delay = newValue
    }
  },
  methods: {
    getSwiper () {
      return this.$refs.h5Swiper.swiper
    },
    handlePlay (playing) {
      this.playing = playing
      let audio = this.$refs.audio
      if (playing) {
        audio.play()
      } else {
        audio.pause()
      }
    },
    changeSlide () {
      this.activeIndex = this.getSwiper().realIndex + 1
    },
    // 强行重新初始化
    reInit () {
      this.visible = false
      this.$nextTick(() => {
        this.visible = true
        // 页码重置
        this.activeIndex = 1
      })
    }
  }
}
</script>

<style lang="less" scoped>
  .h5-preview {
    .inner {
      margin-left: 5px;
      height: 492px;
    }

    .phone-control {
      position: absolute;
      top: 250px;
      right: 0;

      .el-button-group {
        .el-button {
          display: block;
          padding: 25px 20px;
          float: none;
          border: none;
          border-radius: 0;
          background-color: unset;
          color: #777;
          font-size: 20px;

          &:focus {
            background-color: #fff;
          }
        }
      }
    }

    .switch-guide {
      position: absolute;
      width: 24px;
      height: 18px;
      z-index: 10;

      &.vertical {
        left: 0;
        right: 20px;
        bottom: 80px;
        margin-left: auto;
        margin-right: auto;
        animation: swipe-tip-v 1.5s infinite ease-in-out;
      }

      &.horizontal {
        top: 0;
        bottom: 0;
        right: 64px;
        margin-top: auto;
        margin-bottom: auto;
        animation: swipe-tip-h 1.5s infinite ease-in-out;
      }
    }

    .page {
      font-size: 12px;
      line-height: 14px;
      padding-left: 32px;
      padding-right: 32px;
    }

    @keyframes swipe-tip-v {
      0% {
        opacity: 1
      }
      to {
        transform: translate3d(0, -100%, 0);
        opacity: 0
      }
    }
    @keyframes swipe-tip-h {
      0% {
        opacity: 1;
        transform: rotate(270deg);
      }
      to {
        transform: rotate(270deg) translate3d(0, -100%, 0);
        opacity: 0
      }
    }
  }
</style>
