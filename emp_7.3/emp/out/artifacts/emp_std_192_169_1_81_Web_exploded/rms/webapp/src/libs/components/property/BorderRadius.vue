<template>
  <el-form-item class="border-radius">
    <label class="form-item-title">{{ $t('property.borderRadius') }}</label>
    <el-slider :min="0" :max="50" v-model.lazy="element.borderRadius" show-input input-size="small"></el-slider>
  </el-form-item>
</template>

<script>
import {mapGetters} from 'vuex'

export default {
  name: 'Position',
  computed: mapGetters(['element'])
}
</script>

<style lang="less">
  .border-radius {
    .el-slider__runway {
      &.show-input {
        margin-right: 120px;
      }
    }
    .el-slider__input {
      width: 100px !important;
    }
  }
</style>
