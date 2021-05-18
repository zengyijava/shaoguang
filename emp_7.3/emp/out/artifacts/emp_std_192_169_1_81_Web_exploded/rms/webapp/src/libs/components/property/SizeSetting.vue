<template>
  <el-form-item class="layout">
    <label class="form-item-title">{{ $t('property.sSize') }}</label>
    <div class="in-line">
      <label class="input-group fl">
        {{ $t('property.wide') }}
        <el-input-number size="small" v-model.number.lazy="element.w" controls-position="right"
          :min="1">
        </el-input-number>
      </label>
      <label v-if="element.type !== 'text'" class="input-group fr">
        {{ $t('property.height') }}
        <el-input-number
          size="small" v-model.number.lazy="element.h" controls-position="right"
          :min="1">
        </el-input-number>
      </label>
    </div>
  </el-form-item>
</template>

<script>
import {mapGetters} from 'vuex'

export default {
  name: 'SizeSetting',
  computed: mapGetters(['element']),
  props: {
    equal: {
      type: Boolean,
      default: false
    }
  },
  watch: {
    'element.w' (newValue, oldValue) {
      if (this.equal && newValue) {
        this.element.h = Math.round(this.element.w / this.element.ratio)
      }
    },
    'element.h' (newValue, oldValue) {
      if (this.equal && newValue) {
        this.element.w = Math.round(this.element.h * this.element.ratio)
      }
    }
  }
}
</script>
