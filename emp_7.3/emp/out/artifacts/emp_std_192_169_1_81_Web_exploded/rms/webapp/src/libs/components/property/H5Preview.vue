<template>
  <div class="h5-phone-box">
    <el-tooltip
      effect="dark"
      :content="$t('H5.back_btn')"
      placement="right-start">
      <el-button
        type="text"
        class="btn-back"
        icon="el-icon-arrow-left"
        @click="step = 1">
      </el-button>
    </el-tooltip>
    <span
      v-if="step === 2"
      class="preview-title ellipsis">
      {{step1Data.title.text}}
    </span>
    <!-- 步骤一 -->
    <div
      v-if="step === 1"
      class="phone-content">
      <!-- 时间显示 -->
      <div class="msg-content">
        <div class="msg-icon icon icon-my-logo"></div>
        <p class="msg-time">{{ new Date()|formatDate }}</p>
      </div>
      <!-- 第一步区域 -->
      <div
        class="card"
        @click="step = 2">
        <div class="title">
          {{step1Data.title.text || $t('H5.title_hint')}}
        </div>
        <div class="row">
          <!-- 图片 -->
          <div
            v-if="step1Data.cover.src"
            class="avatar"
            :style="avatarStyle">
          </div>
          <div v-else class="default-avatar"></div>
          <div class="description">
            {{step1Data.description.text || $t('H5.description_hint')}}
          </div>
        </div>
      </div>
      <!-- 小手指 -->
      <div class="touch"></div>
    </div>
    <!--步骤二-->
    <div v-else class="preview-content">
      <!-- 背景音乐 -->
      <audio
        v-if="step2Data.music"
        type="audio/mpeg"
        ref="audio"
        :src="step2Data.music.src"
        :autoplay="step2Data.music.autoPlay"
        :loop="step2Data.music.loop">
      </audio>
      <!-- 内容区 -->
      <H5PreviewPage
        v-if="step2Data.pages.length <= 1"
        :previewData="step2Data.pages[0]"
        :previewContW="308"
        :showScrollName="showScrollName"
        :rotate="playing"
        @play="handlePlay">
      </H5PreviewPage>
      <div v-if="step2Data.pages.length > 1" class="swiper-inner">
        <swiper
          v-if="visible"
          :options="swiperOption"
          ref="h5Swiper"
          @slideChange="changeSlide">
          <swiper-slide v-for="(page, index) in step2Data.pages" :key="index">
            <H5PreviewPage
              :previewData="page"
              :previewContW="308"
              :rotate="playing"
              :params="params[index]"
              :swiperPage="step2Data.pages.length"
              :showScrollName="showScrollName"
              @play="handlePlay">
            </H5PreviewPage>
          </swiper-slide>
        </swiper>
      </div>
      <!-- 按钮控制翻页区 -->
      <div v-if="step2Data.pages.length > 1" class="phone-control">
        <el-button-group>
          <el-tooltip
            effect="dark"
            :content="$t('H5.preview_prev_btn')"
            placement="right">
            <el-button
              class="btn-prev"
              icon="el-icon-arrow-up">
            </el-button>
          </el-tooltip>
          <el-tooltip
            effect="dark"
            :content="$t('H5.preview_next_btn')"
            placement="right">
            <el-button
              class="btn-next"
              icon="el-icon-arrow-down">
            </el-button>
          </el-tooltip>
        </el-button-group>
      </div>
      <img
        v-if="step2Data.pages.length > 1"
        :class="['switch-guide', step2Data.swiper.direction]"
        src="../../assets/img/switch-guide.png">
    </div>
    <div
      v-if="step2Data.swiper.pageAlign && step === 2"
      class="page"
      :style="{'text-align': step2Data.swiper.pageAlign}">
      {{ activeIndex }} / {{ step2Data.pages.length }}
    </div>
  </div>
</template>

<script>
import 'swiper/dist/css/swiper.css'
import playIcon from '../../assets/img/play_icon.png'
import stopIcon from '../../assets/img/stop_icon.png'
import {swiper, swiperSlide} from 'vue-awesome-swiper'
import H5PreviewPage from './H5PreviewPage'
import filters from '../../filters'

export default {
  name: 'H5Preview',
  components: {H5PreviewPage, swiper, swiperSlide},
  data () {
    return {
      step: 1,
      visible: true,
      playing: true,
      playIcon: playIcon,
      stopIcon: stopIcon,
      activeIndex: 1,
      swiperOption: {
        autoplay: JSON.parse(this.previewData).swiper.autoPlay,
        direction: JSON.parse(this.previewData).swiper.direction,
        width: 308,
        height: 489,
        slidesPerView: 1,
        spaceBetween: 0,
        loop: JSON.parse(this.previewData).swiper.loop,
        mousewheel: true,
        navigation: {
          nextEl: '.btn-next',
          prevEl: '.btn-prev'
        },
        speed: 600,
        effect: JSON.parse(this.previewData).swiper.effect,
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
      if (this.step2Data.pages.length > 1) {
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
  props: {
    previewData: String,
    appData: String,
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
    step1Data () {
      return JSON.parse(this.appData)
    },
    step2Data () {
      return JSON.parse(this.previewData)
    },
    avatarStyle () {
      return {
        'background-image': 'url("' + this.step1Data.cover.src + '")'
      }
    }
  },
  created () {
    this.playing = this.step2Data.music ? this.step2Data.music.autoPlay : false
  },
  watch: {
    'step2Data.swiper.direction' (newValue) {
      this.swiperOption.direction = newValue
      this.reInit()
    },
    'step2Data.swiper.effect' (newValue) {
      this.visible = false
      this.swiperOption.effect = newValue.value
      this.$nextTick(() => {
        this.visible = true
      })
    },
    'step2Data.swiper.loop' (newValue) {
      this.swiperOption.loop = newValue
      this.reInit()
    },
    'step2Data.swiper.autoPlay' (newValue) {
      const autoplay = this.getSwiper().autoplay
      if (newValue) {
        autoplay.start()
      } else {
        autoplay.stop()
      }
    },
    'step2Data.swiper.autoPlay.delay' (newValue) {
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
    // 强行更新初始化
    reInit () {
      this.visible = false
      this.$nextTick(() => {
        this.visible = true
      })
      this.getSwiper().realIndex = 1
    }
  },
  filters
}
</script>

<style lang="less">
  @import "../../assets/less/variables";

  .h5-phone-box {
    position: relative;
    width: 320px;
    height: 578px;
    background: url('../../assets/img/h5_phone.png') no-repeat;

    h5, p {
      margin: 0;
      padding: 0;
      font-weight: normal;
    }

    .swiper-inner {
      position: relative;
      overflow: hidden;
      width: 308px;
      height: 489px;
      margin-left: auto;
      margin-right: auto;
    }

    .swiper-container {
      height: 100%;
    }

    .swiper-slide {
      background-position: center;
      background-size: cover;

      &.swiper-slide-prev {
        visibility: hidden;
      }

      &.swiper-slide-next {
        visibility: hidden;
      }
    }

    .switch-guide {
      position: absolute;
      width: 24px;
      height: 18px;
      z-index: 10;

      &.vertical {
        left: 0;
        right: 0;
        bottom: 24px;
        margin-left: auto;
        margin-right: auto;
        animation: swipe-tip-v 1.5s infinite ease-in-out;
      }

      &.horizontal {
        top: 0;
        bottom: 0;
        right: 4px;
        margin-top: auto;
        margin-bottom: auto;
        animation: swipe-tip-h 1.5s infinite ease-in-out;
      }
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

    .btn-back {
      margin-top: 22px;
      margin-left: 12px;

      .el-icon-arrow-left {
        font-size: 18px;
        line-height: 18px;
      }
    }

    .preview-title {
      display: inline-block;
      width: 260px;
      margin: 0;
      text-align: center;
      line-height: 14px;
      vertical-align: baseline;
    }

    .phone-content {
      width: 310px;
      height: 520px;
      margin-left: 8px;
      overflow-y: auto;
      overflow-x: hidden;
    }

    .msg-content {
      position: relative;
      padding-left: 40px;
      margin-top: 20px;
      font-size: 12px;
      color: #8c8c8c;
      line-height: 1.3;

      .msg-icon {
        position: absolute;
        top: 20px;
        left: 4px;
        z-index: 2;
        width: 30px;
        height: 30px;
        background: url("../../assets/img/qylogo.png") no-repeat center center;
        background-size: contain;
      }
    }

    .card {
      width: 260px;
      margin-top: 6px;
      margin-left: 38px;
      background-color: #fff;
      border: 1px solid @border;
      border-radius: 16px;
      line-height: 1.3;
      word-break: break-all;
      cursor: pointer;
      overflow: hidden;
      color: @grey-black;

      .title {
        padding: 12px 8px 0 12px;
      }

      .row {
        display: flex;
        align-items: start;
        padding: 8px 8px 12px 12px;

        .avatar {
          width: 60px;
          height: 60px;
          margin-right: 8px;
          align-self: start;
          vertical-align: middle;
          background-repeat: no-repeat;
          background-size: cover;
          border-radius: 4px;
        }

        .default-avatar {
          width: 36px;
          height: 36px;
          padding: 12px;
          margin-right: 8px;
          align-self: start;
          vertical-align: middle;
          background: #efefef url("../../assets/img/logo_icon_small.png") no-repeat 12px 12px;
          border-radius: 4px;
        }

        .description {
          flex: 1;
          vertical-align: middle;
          font-size: 12px;
          color: @grey-dark;
        }
      }
    }

    .preview-content {
      width: 308px;
      height: 489px;
      overflow: hidden;
      margin-left: auto;
      margin-right: auto;
    }

    .phone-control {
      position: absolute;
      top: 200px;
      right: -60px;

      .el-button-group {
        .el-button {
          display: block;
          padding: 28px 23px;
          float: none;
          border: none;
          border-radius: 0;
          background-color: unset;

          &:focus {
            background-color: #fff;
          }
        }
      }
    }

    .touch {
      margin-top: -16px;
      margin-left: 260px;
      width: 26px;
      height: 26px;
      background: url("../../assets/img/click_icon.png") no-repeat;
    }

    .page {
      padding-right: 32px;
      padding-left: 32px;
      font-size: 12px;
      line-height: 14px;
    }
  }
</style>
