<template>
  <div>
    <label class="changeTitle">{{ $t('property.bgImg') }}</label>
    <BgImgSetting>
      <vue-cropper ref="cropper" slot-scope="{imgSrc}"
        :img="imgSrc"
        :cropLeft="crop.left"
        :cropTop="crop.top"
        :cropWidth="crop.w"
        :cropHeight="crop.h"
        :info="true"
        outputType="png"
        :autoCrop='true'
        :centerBox="true"
        :canMove="false"
        :autoCropWidth="width"
        :autoCropHeight="height"
        :fixed="true"
        :fixedNumber="[width, height]"
        @realTime="handleRealTime"
        @imgLoad="handleLoad"
        @cropMoving="startRealTime">
      </vue-cropper>
    </BgImgSetting>
  </div>
</template>

<script>
import {mapGetters, mapMutations} from 'vuex'
import BgImgSetting from './BgImgSetting'
import VueCropper from '../../libs/components/vue-cropper'
import Button from './ButtonSetting'

export default {
  name: 'ChangeImg',
  data () {
    return {
      visible: false
    }
  },
  components: {Button, BgImgSetting, VueCropper},
  computed: {
    ...mapGetters(['background', 'needRealTime']),
    crop () {
      return this.background.crop
    },
    height () {
      return this.crop.h || parseInt(this.crop.style.height) * (304 / parseInt(this.crop.style.width)) - 1
    },
    width () {
      return this.height / (508 / 320)
    }
  },
  methods: {
    ...mapMutations(['mergeCrop', 'startRealTime']),
    handleRealTime (data) {
      if (this.needRealTime) {
        this.background.crop = Object.assign(this.background.crop, data)
      }
    },
    handleLoad (result) {
      if (result === 'success') {
        this.startRealTime()
      }
    }
  }
}
</script>

<style>
  .changeTitle {
    padding-left: 20px;
  }
</style>
