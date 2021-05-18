<template>
  <div class="video-setting">
    <div class="media-panel">
      <div class="media-preview">
        <video :src="element.src" :id="element.tag" controls="controls"></video>
      </div>
      <div class="buttons">
        <el-button v-if="tmType === 15" type="primary" size="small" @click="replaceVideo">{{ $t('richText.replaceVideo') }}
        </el-button>
        <MediaPicker v-else type="video" @on-success="changeVideo">
          <el-button type="primary" size="small">{{ $t('richText.replaceVideo') }}</el-button>
        </MediaPicker>
        <el-button type="primary" size="small" @click.stop="showPop" plain>{{ $t('richText.cutVideo') }}</el-button>
      </div>
    </div>
    <PositionSetting v-if="isCard"></PositionSetting>
    <!--视频裁切-->
    <el-dialog :visible.sync="isShowPop" :append-to-body="true" width="10%" :before-close="handleClose">
      <div class="video-edit"
        v-loading="loading"
        element-loading-background="rgba(0, 0, 0, 0.6)">
        <div class="pop-title">
          {{ $t('richText.onlineVideo') }}
          <div class="video-close J-video-close" @click="isShowPop=false">×</div>
        </div>
        <div class="pop-video-bar">
          <video :src="element.src" controls="controls" ref="videoTemp" element-loading-spinner="el-icon-loading"
            element-loading-background="rgba(0, 0, 0, 0.8)"></video>
        </div>
        <p class="video-info">
          <span>{{ $t('richText.videoSize') }}{{element.size ? (element.size/1024).toFixed(2) : 0}}kb</span>
          <span>{{ $t('richText.videoLength') }}{{duration ? parseInt(duration) : 0}}s</span>
        </p>
        <div class="video-sld">
          <el-slider v-model="sliderArr" @change='getSliderArr' range :max="duration">
          </el-slider>
        </div>
        <div class="video-qie-bar">
          <div class="btn btn-one" :class="{active:type===1}" @click="setTimeType(1)">
            <p>{{ $t('richText.delNotSlect') }}</p>
          </div>
          <div class="btn btn-two" :class="{active:type===0}" @click="setTimeType(0)">
            <p>{{ $t('richText.delSlected') }}</p>
          </div>
          <div class="line"></div>
          <div class="btn-three">
            <div class="btn-nr-kk">
              <div class="kk-top">{{ $t('richText.screen') }}</div>
              <div class="kk-bottom" @click="showScreenList">{{screenTypeText}}
                <i v-if="isShowScreenList" class="el-icon-arrow-up"></i>
                <i v-else class="el-icon-arrow-down"></i>
              </div>
              <div class="kk-list" v-show="isShowScreenList">
                <div @click="setScreen('h')" class="kk-btn">{{ $t('richText.horizontal') }}</div>
                <div @click="setScreen('v')" class="kk-btn no-border">{{ $t('richText.vertical') }}</div>
              </div>
            </div>
          </div>
          <div class="btn-three">
            <div class="btn-nr-kk">
              <div class="kk-top">{{ $t('richText.definition') }}</div>
              <div class="kk-bottom" @click="showList">{{clarityType}}
                <i v-if="isShowList" class="el-icon-arrow-up"></i>
                <i v-else class="el-icon-arrow-down"></i>
              </div>
              <div class="kk-list" v-show="isShowList">
                <el-tooltip class="item" effect="dark" :content="$t('richText.videoHHint')" placement="right">
                  <div @click="setClarity('h')" class="kk-btn">{{ $t('richText.gao') }}</div>
                </el-tooltip>
                <el-tooltip class="item" effect="dark" :content="$t('richText.videoLHint')" placement="right">
                  <div @click="setClarity('m')" class="kk-btn no-border">{{ $t('richText.zhong') }}</div>
                </el-tooltip>
              </div>
            </div>
          </div>
          <div class="line"></div>
          <div class="btn-preview" @click="previewVideo">
            <div class="ico-play el-icon-caret-right"></div>
            <p>{{ $t('previewText.companyText') }}</p>
          </div>
          <div class="btn-four" @click.stop="saveVideo">{{ $t('richText.true') }}</div>
          <div class="clear"></div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import axios from '../axios'
import AJAXURL from '../ajax.address'
import utils from '../../libs/utils'
import PositionSetting from '../../libs/components/property/Position'
import MediaPicker from '../../libs/components/MediaPicker'
import {mapGetters, mapMutations} from 'vuex'

export default {
  name: 'VideoSetting',
  components: {PositionSetting, MediaPicker},
  props: {
    isCard: {
      type: Boolean,
      default: true
    },
    element: {
      type: Object,
      default: () => {
      }
    },
    tmType: {
      type: Number,
      default: 0
    }
  },
  data () {
    return {
      sliderArr: [0, 0],
      isShowPop: false,
      isShowList: false,
      isShowScreenList: false,
      update: 'update',
      videoDom: null,
      videoPreviewDom: null,
      duration: 10,
      timer: null,
      type: 1,
      clarityType: this.$t('richText.zhong'),
      screenTypeText: this.$t('richText.horizontal'),
      clarity: 453,
      screenType: 0, // 1竖屏，0其他
      startTime: 0,
      endTime: 0
    }
  },
  computed: mapGetters(['loading']),
  methods: {
    ...mapMutations(['setLoading', 'preload', 'loaded', 'error']),
    getSliderArr () {
      this.startTime = Math.floor(this.sliderArr[0])
      this.endTime = Math.floor(this.sliderArr[1])
    },
    changeVideo ($event) {
      this.setLoading(false)
      this.$emit('upload-success', $event)
    },
    showPop () {
      this.isShowPop = true
      let videoDom = document.getElementById(this.element.tag)
      this.duration = videoDom.duration
      this.sliderArr = [0, this.duration]
    },
    setTimeType (type) {
      this.type = type
    },
    setClarity (type) {
      this.isShowList = false
      switch (type) {
        case 'h':
          this.clarityType = this.$t('richText.gao')
          this.clarity = 648
          break
        case 'm':
          this.clarityType = this.$t('richText.zhong')
          this.clarity = 453
          break
        default:
          this.clarityType = this.$t('richText.zhong')
          this.clarity = 453
          break
      }
    },
    setScreen (type) {
      this.isShowScreenList = false
      switch (type) {
        case 'h':
          this.screenTypeText = this.$t('richText.horizontal')
          this.screenType = 0
          break
        case 'v':
          this.screenTypeText = this.$t('richText.vertical')
          this.screenType = 1
          break
        default:
          this.screenTypeText = this.$t('richText.horizontal')
          this.screenType = 0
          break
      }
    },
    previewVideo () {
      let timeDifference = (this.endTime - this.startTime)
      this.videoPreviewDom = this.$refs.videoTemp
      if (this.type === 0) {
        this.videoPreviewDom.currentTime = this.startTime
        this.videoPreviewDom.play()
        this.timer = setTimeout(() => {
          this.videoPreviewDom.pause()
        }, (timeDifference + 1) * 1000)
      } else {
        this.timeupdate(() => {
          this.videoPreviewDom.currentTime = this.endTime
          this.videoPreviewDom.play()
        }, timeDifference)
      }
    },
    timeupdate (callback, time) {
      let temp = false
      this.videoPreviewDom.currentTime = 0
      this.videoPreviewDom.play()
      setTimeout(() => {
        temp = true
      }, time * 1000)
      let timer = setInterval(() => {
        if (temp) {
          clearInterval(timer)
          callback()
        }
      }, 100)
    },
    saveVideo () {
      this.getSliderArr()

      let [_w, _h] = [0, 0]

      if (this.screenType === 1) {
        _w = 562
        _h = 1080
      } else {
        if (this.clarity === 648) {
          _w = 720
          _h = 480
        } else {
          _w = 480
          _h = 360
        }
      }

      let params = {
        startTime: this.startTime,
        endTime: this.endTime,
        clarity: this.clarity,
        width: _w,
        height: _h,
        type: this.type,
        src: this.element.src
      }
      let formParam = utils.transformRequest(params)
      this.preload()
      axios
        .post(AJAXURL.VIDEO_CROPPER, formParam)
        .then(res => {
          this.loaded()
          const payload = res.data
          this.element.size = payload.size
          this.element.duration = payload.duration
          this.element.src = payload.path
          this.isShowPop = false
        }).catch(e => {
        })
    },
    showList () {
      this.isShowList = !this.isShowList
    },
    showScreenList () {
      this.isShowScreenList = !this.isShowScreenList
    },
    handleClose (done) {
      done()
    },
    replaceVideo () {
      this.$emit('replaceVideo')
    }
  }
}
</script>
<style lang="less">
  @import '../assets/less/media-panel.less';

  @video-grey: #474c58;
  .video-setting {
    .media-preview {
      video {
        width: 100%;
        height: 100%;
        background-color: #ececec;
        box-sizing: border-box;
      }
    }
  }

  // 裁切弹框
  .video-edit {
    width: 580px;
    height: 500px;
    background-color: #2b2f38;
    border-radius: 6px;
    position: absolute;
    left: -200px;
    top: 0px;

    .pop-title {
      width: 100%;
      height: 36px;
      text-align: center;
      line-height: 36px;
      color: #dbdada;
      font-size: 14px;

      .video-close {
        position: absolute;
        top: 0;
        right: 6px;
        z-index: 2;
        width: 38px;
        height: 38px;
        line-height: 38px;
        font-size: 28px;
        color: #888;
        cursor: pointer;
      }
    }

    .pop-video-bar {
      width: 570px;
      height: 307px;
      margin: 0 5px;

      video {
        width: 570px;
        height: 307px;
      }
    }

    .video-info {
      margin: 10px 0 0 15px;
      font-size: 12px;
      color: #999;

      span {
        margin-right: 35px;
      }
    }

    .video-sld {
      width: 540px;
      margin: 5px 20px;
    }

    .video-qie-bar {
      width: 540px;
      margin: 0 20px;

      .btn {
        width: 94px;
        height: 68px;
        font-size: 12px;
        color: #969b9b;
        float: left;
        text-align: center;
        cursor: pointer;

        &:hover,
        &.active {
          color: #fff;
        }
      }

      p {
        margin-top: 40px;
      }

      .btn-one {
        background: url('../assets/img/one_default_icon.png') no-repeat center center;
      }

      .btn-one:hover {
        background: url('../assets/img/one_after_icon.png') no-repeat center center;
      }

      .btn-one.active {
        background: url('../assets/img/one_checked_icon.png') no-repeat center center;
      }

      .btn-two {
        background: url('../assets/img/two_default_icon.png') no-repeat center center;
      }

      .btn-two:hover {
        background: url('../assets/img/two_after_icon.png') no-repeat center center;
      }

      .btn-two.active {
        background: url('../assets/img/two_checked_icon.png') no-repeat center center;
      }

      .hvr {
        border: 1px solid #409eff !important;
      }

      .btn-preview {
        width: 64px;
        height: 62px;
        border: 1px solid @video-grey;
        background: @video-grey;
        float: left;
        cursor: pointer;
        border-radius: 4px;

        .ico-play {
          font-size: 28px;
          color: #c5c8ca;
          margin: 8px 0 0 18px;
        }

        p {
          font-size: 12px;
          color: #cdcdcd;
          text-align: center;
          height: 12px;
          margin-top: 0;
        }

        &:hover {
          div,
          p {
            color: #fff;
          }
        }
      }

      .line {
        width: 1px;
        height: 64px;
        background: #3d4351;
        float: left;
        margin: 0 6px;
      }

      .btn-three {
        width: 90px;
        height: 62px;
        border: 1px solid @video-grey;
        background: @video-grey;
        float: left;
        cursor: pointer;
        border-radius: 4px;
        margin-right: 2px;

        .btn-nr-kk {
          width: 58px;
          height: 42px;
          border: 1px solid #757c8a;
          border-radius: 6px;
          margin: 9px 15px;
          position: relative;

          .kk-top {
            width: 100%;
            height: 20px;
            font-size: 12px;
            line-height: 20px;
            text-align: center;
            color: #cdcdcd;
            border-bottom: 1px solid #757c8a;
          }

          .kk-bottom {
            width: 100%;
            height: 21px;
            font-size: 12px;
            line-height: 21px;
            text-align: center;
            color: #cdcdcd;

            i {
              margin-left: 10px;
            }
          }

          .kk-list {
            width: 60px;
            height: 50px;
            position: absolute;
            top: 55px;
            background: @video-grey;
            border-radius: 5px;

            .kk-btn {
              line-height: 24px;
              text-align: center;
              color: #cdcdcd;
              font-size: 12px;
              height: 24px;
              border-bottom: 1px solid #3d4350;

              &:hover {
                color: #fff;
              }
            }

            .no-border {
              border: none !important;
            }
          }
        }
      }

      .btn-four {
        width: 64px;
        height: 62px;
        text-align: center;
        line-height: 62px;
        color: #cdcdcd;
        font-size: 18px;
        border: 1px solid @video-grey;
        background: @video-grey;
        float: right;
        cursor: pointer;
        border-radius: 4px;

        &:hover {
          color: #fff;
        }
      }
    }
  }
</style>
