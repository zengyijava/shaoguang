<template>
  <div class="qrcode-setting">
    <el-form-item>
      <el-tabs type="border-card" class="mb20" v-model="activeName" @tab-click="addQrcodeParam">
        <el-tab-pane :label="$t('qrCodeSetting.fixedCode')" name="0">
          <div class="qrcode-static">
            <div class="qrcode-image-content">
              <img :src="element.src" :title="element.tag" />
            </div>
            <div class="qrcode-tip">{{ $t('qrCodeSetting.sketchMap') }}</div>
          </div>
        </el-tab-pane>
        <el-tab-pane :label="$t('qrCodeSetting.dynamicCode')" name="1">
          <div class="qrcode-static">
            <div class="qrcode-image-content">
              <img :src="element.src" :title="element.tag" />
            </div>
            <div class="qrcode-tip">{{ $t('qrCodeSetting.currentParams') }}
              <span class="qrcode-name">{{paramName}}</span>{{ $t('qrCodeSetting.imgUrl') }}
            </div>
          </div>
        </el-tab-pane>
      </el-tabs>
      <ImagePicker v-show="activeName==='0'" @on-success="handleSuccess">
        <el-button type="primary" size="small">{{ $t('qrCodeSetting.upQrCode') }}</el-button>
      </ImagePicker>
    </el-form-item>
    <SizeSetting :equal="equal"></SizeSetting>
    <el-form-item>
      <el-checkbox v-model="equal">{{ $t('cardImgSetting.keepProportion') }}</el-checkbox>
    </el-form-item>
    <PositionSetting class="layout bdb" style="padding-bottom:30px;"></PositionSetting>
    <el-form-item>
      <p class="description">{{ $t('qrCodeSetting.explain') }}</p>
    </el-form-item>
  </div>
</template>

<script>
import {mapGetters, mapMutations} from 'vuex'
import utils from '../../libs/utils'
import SizeSetting from '../../libs/components/property/SizeSetting'
import PositionSetting from '../../libs/components/property/Position'
import ImagePicker from '../../libs/components/ImagePicker'

export default {
  name: 'QrCodeSetting',
  components: {ImagePicker, SizeSetting, PositionSetting},
  computed: {
    ...mapGetters(['element', 'params', 'intersects'])
  },
  data () {
    return {
      activeName: '0',
      paramName: '',
      equal: false,
      param: {
        name: '',
        type: 14,
        lengthRestrict: 0,
        maxLength: 100,
        minLength: 0,
        fixLength: 1,
        index: 1,
        hasLength: 1,
        codeType: '0',
        value: ''
      }
    }
  },
  watch: {
    element (val) {
      if (val.name) {
        this.paramName = val.value
      } else {
        this.activeName = '0'
      }
    }
  },
  methods: {
    ...mapMutations(['updateQrCode', 'addParam', 'updateQrcodeParam', 'setParamCount']),
    handleSuccess (res) {
      this.activeName = '0'
      this.updateQrCode(res)
    },
    addQrcodeParam () {
      if (this.element.name) {
        this.paramName = this.element.name
      } else if (this.activeName === '1') {
        this.param.name = utils.getMaxName(this.params)
        this.paramName = '{#' + this.param.name + '#}'
        this.param.codeType = this.activeName
        // 此处不调用addParam，因为addParam有监听文本，但该监听不适用二维码
        this.params.push(this.param)
        this.intersects.push(this.param)
        this.updateQrcodeParam({
          name: this.paramName,
          codeType: this.activeName
        })
      }
    }
  }
}
</script>
<style lang="less" scoped>
  @import '../../libs/assets/less/variables';

  .qrcode-name {
    color: @blue-border;
  }

  .qrcode-setting {
    padding-top: 12px;

    .mb20 {
      margin-bottom: 20px;
    }

    .media-upload {
      text-align: center;

      .el-upload {
        .el-button {
          width: 154px;
        }
      }
    }

    .qrcode-static {
      width: 260px;
      height: 148px;
      padding: 10px 0;
      margin-left: auto;
      margin-right: auto;

      .qrcode-image-content {
        height: 88px;
        width: 88px;
        padding: 8px;
        border: solid 1px #e4e6ec;
        margin: 0 auto 10px auto;

        img {
          width: 100%;
          height: 100%;
        }
      }

      .qrcode-tip {
        text-align: center;
        color: #cbcbcb;
        line-height: normal;
        font-size: 12px;
      }
    }
  }
</style>
