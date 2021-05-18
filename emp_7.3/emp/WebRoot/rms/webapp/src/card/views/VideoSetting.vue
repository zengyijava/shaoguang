<template>
  <div class="video-setting">
    <div class="media-panel">
      <div class="media-preview">
        <video :src="element.src" :id="element.tag" controls="controls"></video>
      </div>
      <div class="buttons">
        <MediaPicker type="video" @on-success="changeVedio($event)">
          <el-button type="primary" size="small">更换视频</el-button>
        </MediaPicker>
        <el-button type="primary" size="small" @click.stop="showPop" plain>裁切视频</el-button>
      </div>
    </div>
    <PositionSetting v-if="isCard"></PositionSetting>
    <!--视频裁切-->
    <el-dialog :visible.sync="isShowPop" :append-to-body="true" width="10%" :before-close="handleClose">
      <div class="video-edit"
        v-loading="vedioLoading"
        element-loading-background="rgba(0, 0, 0, 0.6)">
        <div class="pop-title">
          在线视频编辑
          <div class="video-close J-video-close" @click="isShowPop=false">×</div>
        </div>
        <div class="pop-video-bar">
          <video :src="element.src" controls="controls" ref="videoTemp" element-loading-spinner="el-icon-loading" element-loading-background="rgba(0, 0, 0, 0.8)"></video>
        </div>
        <p class="video-info">
          <span>视频大小：{{(element.size/1024).toFixed(2)}}kb</span>
          <span>视频长度：{{duration}}s</span>
        </p>
        <div class="video-sld">
          <el-slider v-model="sliderArr" @change='getSliderArr' range :max="duration">
          </el-slider>
        </div>
        <div class="video-qie-bar">
          <div class="btn btn-one" :class="{active:type===1}" @click="setTimeType(1)">
            <p>删除未选中片段</p>
          </div>
          <div class="btn btn-two" :class="{active:type===0}" @click="setTimeType(0)">
            <p>删除选中片段</p>
          </div>
          <div class="btn-preview" @click="previewVideo">
            <div class="ico-play el-icon-caret-right"></div>
            <p>预览</p>
          </div>
          <div class="line"></div>
          <div class="btn-three">
            <div class="btn-nr-kk">
              <div class="kk-top">清晰度</div>
              <div class="kk-bottom" @click="showList">{{clarityType}}
                <i v-if="isShowList" class="el-icon-arrow-up"></i>
                <i v-else class="el-icon-arrow-down"></i>
              </div>
              <div class="kk-list" v-show="isShowList">
                <el-tooltip class="item" effect="dark" content="高清视频建议播放时长不超过20秒" placement="right">
                  <div @click="setClarity('h')" class="kk-btn">高清</div>
                </el-tooltip>
                <el-tooltip class="item" effect="dark" content="标清视频建议播放时长不超过30秒" placement="right">
                  <div @click="setClarity('m')" class="kk-btn no-border">标清</div>
                </el-tooltip>
              </div>
            </div>
          </div>
          <div class="btn-four" @click.stop="saveVideo">确定</div>
          <div class="clear"></div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import PositionSetting from '../../libs/components/property/Position'
import MediaPicker from '../../libs/components/MediaPicker'
import utils from '../../libs/utils'
import { mapGetters, mapActions, mapMutations } from 'vuex'

export default {
  name: 'VideoSetting',
  components: { PositionSetting, MediaPicker },
  props: {
    isCard: {
      type: Boolean,
      default: true
    },
    element: {
      type: Object,
      default: () => {
      }
    }
  },
  data () {
    return {
      sliderArr: [0, 0],
      isShowPop: false,
      isShowList: false,
      update: 'update',
      videoDom: null,
      videoPreviewDom: null,
      duration: 10,
      timer: null,
      type: 1,
      loading: false,
      vedioLoading: false,
      clarityType: '标清',
      clarity: 453,
      startTime: 0,
      endTime: 0
    }
  },
  computed: mapGetters(['videosCutLoading']),
  watch: {
    videosCutLoading: {
      handler: function (val) {
        this.vedioLoading = val
        if (!val) {
          this.isShowPop = false
        }
      },
      deep: true
    }
  },
  methods: {
    ...mapActions(['updateVideo']),
    ...mapMutations(['setLoading', 'setVideoCutLoading']),
    getSliderArr () {
      let _this = this
      _this.startTime = Math.floor(_this.sliderArr[0])
      _this.endTime = Math.floor(_this.sliderArr[1])
    },
    changeVedio ($event) {
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
          this.clarityType = '高清'
          this.clarity = 648
          break
        case 'm':
          this.clarityType = '标清'
          this.clarity = 453
          break
        default:
          this.clarityType = '标清'
          this.clarity = 453
          break
      }
    },
    previewVideo () {
      let _this = this
      let timeDifference = (_this.endTime - _this.startTime)
      _this.videoPreviewDom = _this.$refs.videoTemp
      if (_this.type === 0) {
        this.timeupdate(function () {
          _this.videoPreviewDom.currentTime = _this.endTime
          _this.loading = false
          _this.videoPreviewDom.play()
        }, timeDifference)
      } else {
        _this.videoPreviewDom.currentTime = _this.startTime
        _this.loading = false
        _this.videoPreviewDom.play()
        this.timer = setTimeout(function () {
          _this.videoPreviewDom.pause()
          _this.loading = true
        }, (timeDifference + 1) * 1000)
      }
    },
    timeupdate (callback, time) {
      let _this = this
      let temp = false
      _this.videoPreviewDom.currentTime = 0
      _this.loading = false
      _this.videoPreviewDom.play()
      setTimeout(function () {
        temp = true
        _this.loading = true
      }, time * 1000)
      let timer = setInterval(function () {
        if (temp) {
          clearInterval(timer)
          callback()
        }
      }, 100)
    },
    saveVideo () {
      this.setVideoCutLoading(true)
      this.getSliderArr()
      let params = {
        startTime: this.startTime,
        endTime: this.endTime,
        clarity: this.clarity,
        width: (this.clarity === 648) ? 854 : 640,
        height: (this.clarity === 648) ? 480 : 360,
        type: this.type,
        src: this.element.src
      }
      let formParam = utils.transformRequest(params)
      this.updateVideo(formParam)
    },
    hidePop () {
      this.isShowPop = false
    },
    showList () {
      this.isShowList = !this.isShowList
    },
    handleClose (done) {
      done()
    }
  }
}
</script>
<style lang="less">
@import '../../libs/assets/less/media-panel.less';

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
    // .el-slider__stop {
    //   height: 20px !important;
    //   width: 3px !important;
    //   border-radius: 0 !important;
    //   background-color: #333 !important;
    // }
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
      background: url('../../libs/assets/img/one_default_icon.png') no-repeat center
        center;
    }
    .btn-one:hover {
      background: url('../../libs/assets/img/one_after_icon.png') no-repeat center
        center;
    }
    .btn-one.active {
      background: url('../../libs/assets/img/one_Checked_icon.png') no-repeat center
        center;
    }
    .btn-two {
      background: url('../../libs/assets/img/two_default_icon.png') no-repeat center
        center;
    }
    .btn-two:hover {
      background: url('../../libs/assets/img/two_after_icon.png') no-repeat center
        center;
    }
    .btn-two.active {
      background: url('../../libs/assets/img/two_Checked_icon.png') no-repeat center
        center;
    }
    .hvr {
      border: 1px solid #409eff !important;
    }
    .btn-preview {
      width: 90px;
      height: 62px;
      border: 1px solid @video-grey;
      background: @video-grey;
      float: left;
      cursor: pointer;
      border-radius: 4px;
      margin-left: 15px;
      .ico-play {
        font-size: 28px;
        color: #c5c8ca;
        margin: 8px 0 0 30px;
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
      margin: 0 22px;
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
      width: 90px;
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
