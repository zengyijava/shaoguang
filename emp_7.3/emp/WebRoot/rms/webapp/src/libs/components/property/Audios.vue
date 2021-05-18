<template>
  <div
    :class="['audios-player', playSize]">
    <div class="audios-player-bg" :style="{backgroundColor: element.style.backgroundColor, opacity: (1 - element.style.transparency)}"></div>
    <div class="audios-player-cont clearfix" :style="{color: element.style.color}">
      <div :class="[play ? 'play' : 'paused', 'player-btn']" @click="playAudios"></div>
      <div class="player-control">
        <h4 v-if="showScrollName" class="scroll-audios-name" ref="audioNameBox">
          <span ref="audioNameScroll" class="scroll-inner">{{ element.title }}</span>
        </h4>
        <h4 v-else class="audios-name">{{ element.title }}</h4>
        <div class="progress-bg">
          <div
            class="progress-bar"
            :style="{width: progressBarW + 'px'}">
          </div>
          <div
            class="slider-bar"
            :style="{left: progressBarW + 'px'}"
            @mousedown="sliderBarMove">
          </div>
        </div>
        <div class="player-times">
          <span class="show-time">{{ showTime }}</span>
          <span>/{{ element.duration|formatTime }}</span>
        </div>
      </div>
      <audio
        class="j-audio"
        ref="audio"
        :src="element.src"
        @timeupdate="showAudioProgress"
        @durationchange="handleChange"
        @ended="endAudioPlay">
      </audio>
    </div>
  </div>
</template>
<script>
import filters from '../../filters'

export default {
  name: 'Audios',
  data () {
    return {
      play: false,
      showTime: '00:00',
      progressBarW: 0
    }
  },
  props: {
    element: {
      type: Object,
      default: () => {
        return {}
      }
    },
    playSize: {
      type: String,
      default: 'small-audios'
    },
    showScrollName: {
      type: Boolean,
      default: false
    },
    swiperPage: {
      type: Number,
      default: 1
    }
  },
  computed: {
    // 进度条长度
    progressAllWidth () {
      if (this.playSize === 'small-audios') {
        return 164
      } else {
        return 190
      }
    }
  },
  mounted () {
    this.$nextTick(function () {
      if (!this.$refs.audioNameBox) {
        return
      }
      let _nameBoxW = this.$refs.audioNameBox.offsetWidth
      let _scrollW = this.$refs.audioNameScroll.offsetWidth
      if (_scrollW > _nameBoxW) {
        this.$refs.audioNameScroll.className += ' scroll-left';
      } else {
        this.$refs.audioNameScroll.className = 'scroll-inner'
      }
    })
  },
  methods: {
    // 时长变化
    handleChange (e) {
      this.element.duration = e.target.duration
    },
    // 播放和暂停音频
    playAudios (event) {
      event.stopPropagation()
      if (this.swiperPage === 1) {
        let myAudio = this.$refs.audio
        if (myAudio.paused) {
          this.play = true
          myAudio.play()
        } else {
          this.play = false
          myAudio.pause()
        }
      }
    },
    // 播放进度变化
    showAudioProgress () {
      let _audioEl = this.$refs.audio
      // 进度条宽度计算
      this.progressBarW = this.progressAllWidth / (this.element.duration / _audioEl.currentTime)
      this.showTime = filters.formatTime(_audioEl.currentTime)
    },
    // 播放结束
    endAudioPlay () {
      this.play = false
    },
    // 拖动进度条
    sliderBarMove (event) {
      let _moveX = event.clientX - event.target.offsetLeft
      document.onmousemove = (event) => {
        let _barW = event.clientX - _moveX
        if (_barW <= 0) {
          _barW = 0
        } else if (_barW >= this.progressAllWidth) {
          _barW = this.progressAllWidth
        }
        let _moveTime = this.element.duration / (this.progressAllWidth / _barW)

        this.progressBarW = _barW
        this.showTime = filters.formatTime(_moveTime)
        this.$refs.audio.currentTime = _moveTime
      }
      document.onmouseup = (e) => {
        document.onmousemove = null;
        document.onmouseup = null;
      }
      event.stopPropagation()
    }
  },
  filters
}
</script>

<style lang="less">
  @import '../../assets/less/variables';
  @keyframes moveLeft {
    from {
      transform:translate3d(100%, 0, 0);
    }
    to {
      transform:translate3d(-100%, 0, 0);
    }
  }
  @-moz-keyframes moveLeft {
    from {
      transform:translate3d(100%, 0, 0);
    }
    to {
      transform:translate3d(-100%, 0, 0);
    }
  }
  @-webkit-keyframes moveLeft {
    from {
      transform:translate3d(100%, 0, 0);
    }
    to {
      transform:translate3d(-100%, 0, 0);
    }
  }
  .audios-player{
    position: relative;
    border-radius: 4px;
    overflow: hidden;
    .audios-player-bg{
      position: absolute;
      top: 0;
      left: 0;
      width: 100%;
      height: 100%;
      z-index: 2;
    }
    .audios-player-cont{
      position: absolute;
      top: 0;
      left: 0;
      width: 100%;
      height: 100%;
      z-index: 3;
    }
    .player-btn,
    .player-control{
      float: left;
      line-height: 1;
    }
    .player-btn{
      cursor: pointer;
    }
    .audios-name{
      width: 100%;
      margin: 0;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
      font-weight: normal;
    }
    .scroll-audios-name{
      position: relative;
      width: 100%;
      margin: 0;
      overflow: hidden;
      font-weight: normal;
      .scroll-inner{
        position: absolute;
        white-space: nowrap;
        &.scroll-left{
          animation: moveLeft 5s linear infinite;
          -moz-animation: moveLeft 5s linear infinite;
          -webkit-animation: moveLeft 5s linear infinite;
        }
      }
    }
    .progress-bg{
      position: relative;
      width: 100%;
      height: 2px;
      background-color: @grey-light;
    }
    .progress-bar{
      position: absolute;
      top: 0;
      left: 0;
      z-index: 2;
      height: 2px;
      background-color: @grey-dark;
    }
    .slider-bar{
      position: absolute;
      top: -5px;
      z-index: 3;
      width: 12px;
      height: 12px;
      margin-left: -6px;
      cursor: pointer;
      background: url("../../assets/img/audio/plan_icon.png") no-repeat center center;
    }
    .player-times{
      font-size: 12px;
    }
    &.small-audios{
      width: 238px;
      height: 68px;
      .player-btn{
        width: 44px;
        height: 44px;
        padding: 12px 6px 12px 14px;
        &.play{
          background: url("../../assets/img/audio/stop_btn_icon.png") no-repeat center center;
        }
        &.paused{
          background: url("../../assets/img/audio/play_btn_icon.png") no-repeat center center;
        }
      }
      .player-control{
        width: 164px;
        padding-top: 12px;
        padding-bottom: 10px;
      }
      .audios-name{
        font-size: 14px;
      }
      .scroll-audios-name{
        height: 16px;
        .scroll-inner{
          height: 14px;
          display: block;
          font-size: 14px;
          line-height: 14px;
        }
      }
      .progress-bg{
        margin-top: 8px;
        margin-bottom: 8px;
      }
    }
    &.big-audios{
      width: 290px;
      height: 90px;
      .player-btn{
        width: 56px;
        height: 56px;
        padding: 17px 8px 17px 18px;
        &.play{
          background: url("../../assets/img/audio/big_stop_btn_icon.png") no-repeat center center;
        }
        &.paused{
          background: url("../../assets/img/audio/big_play_btn_icon.png") no-repeat center center;
        }
      }
      .player-control{
        width: 190px;
        padding-top: 18px;
        padding-bottom: 13px;
      }
      .audios-name{
        font-size: 18px;
      }
      .scroll-audios-name{
        height: 20px;
        .scroll-inner{
          height: 18px;
          display: block;
          font-size: 18px;
          line-height: 18px;
        }
      }
      .progress-bg{
        margin-top: 13px;
        margin-bottom: 13px;
      }
    }
  }
</style>
