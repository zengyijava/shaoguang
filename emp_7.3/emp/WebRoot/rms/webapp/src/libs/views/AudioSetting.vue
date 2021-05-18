<template>
  <div class="audio-setting">
    <!-- 文件命名 -->
    <el-form-item>
      <label class="form-item-title">{{ $t('richText.audioName') }}</label>
      <div class="audio-name-set">
        <el-input
          v-model="element.title"
          size="small"
          maxlength="20"
          :placeholder="$t('property.pEnterContent')">
        </el-input>
        <span class="input-hint">{{ element.title.length }}/20</span>
      </div>
    </el-form-item>
    <el-button v-if="tmType === 15" class="center-block full-btn" type="primary" size="small" @click="replaceAudio">{{ $t('richText.changeAudio') }}</el-button>
    <MediaPicker v-else @on-success="changeAudio($event)">
      <el-button class="center-block" type="primary" size="small">{{ $t('richText.changeAudio') }}</el-button>
    </MediaPicker>
    <!-- 推荐配色 -->
    <el-form-item v-if="showColorSet">
      <label class="form-item-title">{{ $t('richText.recommendedColor') }}</label>
      <ul class="clearfix audio-color">
        <li class="default" @click="changeColor('default')"></li>
        <li class="blue" @click="changeColor('blue')"></li>
        <li class="green" @click="changeColor('green')"></li>
        <li class="red" @click="changeColor('red')"></li>
      </ul>
    </el-form-item>
    <FontColor v-if="showColorSet"></FontColor>
    <FontColor v-if="showColorSet" type="BackColor"></FontColor>
    <el-form-item v-if="showColorSet" class="changOpacityBox">
      <label class="form-item-title">{{ $t('property.opacity') }}</label>
      <el-slider :min="0" :max="1" v-model.lazy="element.style.transparency" :step="0.01"></el-slider>
      <div class="opacityNum">
        {{Math.round((1 - element.style.transparency) * 100)}}%
      </div>
    </el-form-item>
    <Position v-if="isCard && showColorSet"></Position>
  </div>
</template>

<script>
import Position from '../components/property/Position'
import FontColor from '../components/property/FontColor'
import MediaPicker from '../components/MediaPicker'
import { mapMutations } from 'vuex'

export default {
  name: 'AudioSetting',
  components: {MediaPicker, Position, FontColor},
  props: {
    showColorSet: {
      type: Boolean,
      default: true
    },
    isCard: {
      type: Boolean,
      default: true
    },
    element: {
      type: Object,
      default: () => {}
    },
    tmType: {
      type: Number,
      default: 0
    }
  },
  methods: {
    ...mapMutations(['setLoading']),
    changeAudio ($event) {
      this.setLoading(false)
      this.$emit('upload-success', $event)
    },
    replaceAudio () {
      this.$emit('replaceAudio')
    },
    changeColor (type) {
      switch (type) {
        case 'default':
          this.element.style.color = '#333333'
          this.element.style.backgroundColor = '#ffffff'
          break;
        case 'blue':
          this.element.style.color = '#ffffff'
          this.element.style.backgroundColor = '#2196f3'
          break;
        case 'green':
          this.element.style.color = '#ffffff'
          this.element.style.backgroundColor = '#4caf50'
          break;
        case 'red':
          this.element.style.color = '#ffffff'
          this.element.style.backgroundColor = '#f44336'
          break;
      }
    }
  }
}
</script>

<style lang="less">
  @import '../assets/less/audio-setting.less';
  .audio-setting .full-btn{
    width: 310px;
  }
  .audio-color{
    margin-top: 4px;
    li{
      width: 66px;
      height: 30px;
      margin-right: 8px;
      float: left;
      border-radius: 2px;
      cursor: pointer;
      &.default{
        background-color: #515151;
      }
      &.blue{
        background-color: #2196f3;
      }
      &.green{
        background-color: #4caf50;
      }
      &.red{
        background-color: #f44336;
      }
    }
  }
  .audio-name-set{
    position: relative;
    .el-input--small .el-input__inner{
      padding-right: 44px;
    }
    .input-hint{
      position: absolute;
      top: 0;
      right: 8px;
      z-index: 2;
      font-size: 12px;
      color: #999999;
    }
  }
  .changOpacityBox {
    width: 300px !important;
    .el-slider {
      width: 200px !important;
      display: inline-block !important;
    }
    .el-slider__runway {
      display: inline-block !important;
    }
    .opacityNum {
      display: inline-block;
      width: 60px;
      height: 34px;
      text-align: center;
      line-height: 34px;
      border: 1px solid #cccccc;
      margin-left: 6px;
    }
  }
</style>
