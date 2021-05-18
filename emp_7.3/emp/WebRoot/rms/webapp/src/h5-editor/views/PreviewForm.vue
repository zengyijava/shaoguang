<template>
  <el-form class="preview-form" ref="appForm" :model="app" label-position="top">
    <div class="title-box">
      <div class="title-box-left">
        <el-form-item :label="$t('H5.preview_title')" prop="title.text" :rules="[{ required: true, message: $t('H5.preview_title_empty')}]">
          <el-input size="small" class="small-input" :placeholder="$t('H5.title_hint')"
            v-model="app.title.text" :maxlength="32"></el-input>
          <span class="title-limit">{{app.title.text.length}}/32</span>
        </el-form-item>
        <el-form-item :label="$t('H5.preview_desc')" prop="description.text" :rules="[{ required: true, message: $t('H5.preview_desc_empty')}]">
          <el-input type="textarea" :rows="5" size="small" :placeholder="$t('H5.description_hint')"
            v-model="app.description.text" :maxlength="50">
          </el-input>
          <span class="desc-limit">{{app.description.text.length}}/50</span>
        </el-form-item>
        <el-form-item
          v-if="showCorpSelect"
          :label="$t('public.corpHint')">
          <el-select
            size="small"
            class="small-input"
            v-model="corpId"
            :remote="true"
            :filterable="true"
            :placeholder="$t('public.corpHolder')"
            @change="corpChange"
            :remote-method="remoteMethod">
            <el-option
              v-for="item in corpOptions"
              :key="item.corpCode"
              :label="item.corpName"
              :value="item.corpCode">
            </el-option>
          </el-select>
        </el-form-item>
      </div>
      <div class="title-box-right">
        <el-form-item>
          <ImagePicker class="avatar-uploader"
            :class="{'is-error': noCover}"
            @on-success="handleSuccess">
            <div v-if="app.cover.src" class="avatar" :style="avatarStyle"></div>
            <div v-else>
              <img src="../assets/img/logo_big.png" class="default-avatar" />
              <div>{{ $t('H5.up_suggest') }}</div>
              <div>{{ $t('H5.up_img_size') }}</div>
            </div>
            <div class="btn">{{ $t('H5.replace_cover') }}</div>
          </ImagePicker>
          <div v-if="noCover" class="el-form-item__error">{{ $t('H5.cover_empty') }}</div>
        </el-form-item>
      </div>
    </div>
    <template v-if="multiple">
      <el-form-item :label="$t('H5.page_effect')">
        <el-radio-group v-model="swiper.direction">
          <el-radio label="vertical">{{ $t('H5.up_and_down_effect') }}</el-radio>
          <el-radio label="horizontal">{{ $t('H5.left_and_right_effect') }}</el-radio>
        </el-radio-group>
        <el-radio-group class="show" v-model="swiper.effect" @change="handleChangeEffect">
          <el-radio-button
            v-for="item in effects"
            :label="item.value"
            :key="item.label">
            {{item.label}}
          </el-radio-button>
        </el-radio-group>
      </el-form-item>
      <el-form-item>
        <el-checkbox :checked="Boolean(swiper.pageAlign)" @change="handleChangeAlign">{{ $t('H5.page_number') }}</el-checkbox>
        <el-radio-group v-if="swiper.pageAlign" class="show" v-model="swiper.pageAlign">
          <el-radio-button label="right">{{ $t('H5.bottom_right') }}</el-radio-button>
          <el-radio-button label="left">{{ $t('H5.bottom_left') }}</el-radio-button>
          <el-radio-button label="center">{{ $t('H5.bottom_center') }}</el-radio-button>
        </el-radio-group>
      </el-form-item>
      <el-form-item>
        <div class="line-gap">
          <el-checkbox v-model="swiper.loop">{{ $t('H5.loop_play') }}</el-checkbox>
        </div>
        <div>
          <el-checkbox class="flt-l" :checked="Boolean(swiper.autoPlay)" @change="handleChangeAutoPlay">{{ $t('H5.auto_play') }}
          </el-checkbox>
          <el-slider class="slider-box flt-l" v-model="delay"
            :min="0" :max="20" input-size="small" :disabled="swiper.autoPlay===false"
            @change="handleChangeDelay">
          </el-slider>
          <el-input-number class="flt-l" v-model="delay" controls-position="right"
            :min="0" :max="20" size="small" :disabled="swiper.autoPlay === false"
            @change="handleChangeDelay">
          </el-input-number>
          <p class="flt-l tip">{{ $t('H5.page_keep_time') }}</p>
        </div>
      </el-form-item>
    </template>
    <el-form-item class="top-gap">
      <el-button type="primary" size="small"
        :loading="loading && submitType === 1"
        @click="submitForm('appForm')">
        {{ $t('richText.commitExamine') }}
      </el-button>
      <el-button size="small" @click="$emit('close')">{{ $t('richText.back') }}</el-button>
    </el-form-item>
  </el-form>
</template>
<script>
import {mapActions, mapGetters, mapMutations} from 'vuex'
import 'swiper/dist/css/swiper.css'
import ImagePicker from '../../libs/components/ImagePicker'
import actions from '../../libs/api'

export default {
  name: 'PreviewForm',
  components: {ImagePicker},
  props: {
    submitType: {
      type: Number,
      default: 0
    }
  },
  created () {
    if (this.swiper.autoPlay) {
      this.delay = this.swiper.autoPlay.delay / 1000
    }
    if (this.userInfo.hasOwnProperty('data')) {
      if (Number(this.userInfo.data.type) === 1) {
        this.showCorpSelect = false
      } else {
        this.showCorpSelect = true
      }
    } else {
      this.getUserInfos()
    }
  },
  data () {
    return {
      delay: 3,
      effects: [{
        label: this.$t('H5.cube_effect'),
        value: 'cube'
      }, {
        label: this.$t('H5.slide_effect'),
        value: 'slide'
      }, {
        label: this.$t('H5.fade_effect'),
        value: 'fade'
      }, {
        label: this.$t('H5.flip_effect'),
        value: 'flip'
      }],
      noCover: false,
      // 是否显示企业选择
      showCorpSelect: false,
      corpOptions: [],
      corpId: ''
    }
  },
  computed: {
    ...mapGetters(['app', 'pages', 'swiper', 'loading', 'userInfo']),
    multiple () {
      return this.pages.length > 1
    },
    avatarStyle () {
      return {
        'background-image': 'url("' + this.app.cover.src + '")'
      }
    }
  },
  watch: {
    // 监听用户信息是否获取成功
    userInfo: function (val) {
      if (val) {
        // 只有在rcos中才有企业选择
        if (Number(this.userInfo.data.type) === 1) {
          this.showCorpSelect = false
        } else {
          this.showCorpSelect = true
        }
      }
    }
  },
  methods: {
    ...mapActions(['getUserInfos']),
    ...mapMutations(['selectCorp']),
    handleChangeEffect (value) {
      this.swiper.effect = value
    },
    handleChangeAlign (checked) {
      if (!checked) {
        this.swiper.pageAlign = ''
      } else {
        this.swiper.pageAlign = 'right'
      }
    },
    handleChangeAutoPlay (checked) {
      if (checked) {
        this.swiper.autoPlay = {
          disableOnInteraction: false,
          delay: 3000
        }
      } else {
        this.swiper.autoPlay = false
      }
    },
    handleChangeDelay (delay) {
      this.swiper.autoPlay.delay = delay * 1000
    },
    handleSuccess (event) {
      this.app.cover.src = event.url
      this.noCover = false
    },
    submitForm (formName) {
      this.noCover = this.app.cover.src === ''
      this.$refs[formName].validate(valid => {
        if (valid && !this.noCover) {
          if (!this.corpId && Number(this.userInfo.data.type) === 0) {
            this.$message.error(this.$t('richText.cropEmpty'))
            return false
          } else {
            this.$emit('save', 1)
          }
        } else {
          return false
        }
      })
    },
    getCorpOptionData (params) {
      let _self = this
      let _params = {
        corpName: params
      }
      actions.getCorpInfos(_params, response => {
        _self.corpOptions = response.data.data.corps
      }, errMsg => {
        _self.$message.error(errMsg)
      })
    },
    remoteMethod (params) {
      if (params) {
        this.getCorpOptionData(params)
      } else {
        this.corpOptions = []
      }
    },
    corpChange (val) {
      this.selectCorp(val)
    }
  }
}
</script>

<style lang="less">
  @import "../../libs/assets/less/variables";

  .preview-form {
    display: inline-block;
    padding: 45px 16px 60px 40px;
    vertical-align: top;
    .title-box {
      width: 570px;
      .title-box-left, .title-box-right {
        display: inline-block;
        vertical-align: top;
      }
      .title-limit{
        position: absolute;
        top: 0;
        right: 8px;
        line-height: 32px;
        color: #a3afb7;
        font-size: 12px;
      }
      .desc-limit{
        position: absolute;
        top: 76px;
        right: 8px;
        line-height: 32px;
        color: #a3afb7;
        font-size: 12px;
      }
      .title-box-right {
        margin-top: 32px;
        margin-left: 12px;
        .el-form-item {
          margin-bottom: 0;
          .el-form-item__content {
            line-height: normal !important;
          }
        }
      }
    }
    .avatar-uploader {
      &.is-error {
        .el-upload {
          border-color: #f56c6c;
        }
      }
      .el-upload {
        width: 180px;
        height: 180px;
        border: 1px solid #d9d9d9;
        background-color: #efefef;
        cursor: pointer;
        position: relative;
        overflow: hidden;
        line-height: 20px;
      }
    }
    .btn {
      position: absolute;
      width: 100%;
      height: 32px;
      line-height: 32px;
      bottom: 0;
      color: #fff;
      background-color: rgba(0, 0, 0, 0.6);
    }
    .avatar {
      display: inline-block;
      width: 180px;
      height: 180px;
      background-repeat: no-repeat;
      background-size: cover;
    }
    .default-avatar {
      display: inline-block;
      width: 54px;
      height: 54px;
      padding-top: 32px;
    }
    .small-input {
      width: 341px;
      .el-input__inner{
        padding-right: 42px;
      }
    }
    .el-input--small {
      .el-textarea__inner {
        width: 341px;
      }
    }
    textarea {
      resize: none;
    }
    .el-button.el-button--small {
      width: 80px;
    }
    .el-form-item {
      &.is-required {
        margin-bottom: 13px;
      }
    }
    .el-radio-button {
      margin-right: 8px;
      .el-radio-button__inner {
        padding: 6px 12px;
        background: #e3e3e3;
        border-radius: 2px;
        border: none;
      }
      :checked + .el-radio-button__inner {
        background-color: #409EFF;
      }
    }
    .line-gap {
      margin-top: 8px;
      line-height: 20px;
    }
    .slider-box {
      width: 180px;
      border-radius: 2px;
      margin-left: 6px;
      margin-right: 10px;
    }
    .flt-l {
      display: inline-block;
      vertical-align: middle;
    }
    .el-input-number--small {
      width: 54px;
      .el-input-number__decrease, .el-input-number__increase {
        width: 28px;
      }
    }
    .el-input-number.is-controls-right {
      .el-input__inner {
        padding-left: 5px;
        padding-right: 0;
      }
    }
    .tip {
      margin: 0 0 0 6px;
      font-size: 12px;
      color: #999999;
    }
    .top-gap {
      margin-top: 32px;
    }
    .el-icon-loading {
      display: none;
    }
    .el-button [class*=el-icon-] + span {
      margin-left: 0;
    }
  }

</style>
