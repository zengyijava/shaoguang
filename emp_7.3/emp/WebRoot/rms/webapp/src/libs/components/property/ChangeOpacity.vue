<template>
  <el-form-item class="changOpacityBox">
    <label class="form-item-title">{{ $t('property.opacity') }}</label>
    <el-slider :min="0" :max="1" v-model.lazy="background.transparency" :step="0.01"></el-slider>
    <div class="opacityNum">
      {{Math.round((1 - background.transparency) * 100)}}%
    </div>
  </el-form-item>
</template>

<script>
import {mapGetters} from 'vuex'

export default {
  name: 'Opacity',
  computed: {
    ...mapGetters(['background'])
  }
}
</script>

<style lang="less">
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
