<template>
  <el-form-item class="font-setting">
    <label class="form-item-title">{{ $t('property.glyph') }}</label>
    <span :class="{active:jiacuIsActive}" @mousedown.prevent="setTextElement(0,'bold',!jiacuIsActive)">
      <i class="el-icon-ali-jiacu" style="color:#79899d;"></i>
    </span>
  </el-form-item>
</template>

<script>
// import {mapGetters} from 'vuex'

export default {
  name: 'FontSettings',
  computed: {
    element () {
      if (this.$store.getters.element) {
        return this.$store.getters.element.style
      } else if (this.$store.getters['media/element']) {
        return this.$store.getters['media/element'].style
      }
      return {
        fontWeight: 'normal'
      }
    }
  },
  data () {
    return {
      jiacuIsActive: false,
      xietiIsActive: false,
      underlineIsActive: false,
      current: 100
    }
  },
  methods: {
    setTextElement (type, style, isActive) {
      if (typeof (isActive) !== 'boolean') {
        this.current = isActive
      }
      if (style === 'bold') {
        this.jiacuIsActive = isActive
      }
      if (isActive) {
        this.element.fontWeight = style
      }
      let data = {type, 'boolean': false, style}
      this.$emit('setCommand', data)
    }
  }
}
</script>
<style lang="less">
  @import '../../../libs/assets/less/variables';

  .font-setting {
    span {
      display: inline-block;
      height: 24px;
      width: 24px;
      text-align: center;
      line-height: 26px;
      margin-right: 4px;
      border: solid 1px @white-light;
      color: #8f9daf;
      cursor: pointer;
      &:hover,
      &.active {
        border: solid 1px @blue-border;
        background-color: @blue-hover;
        border-radius: 2px;
      }
    }
  }
</style>
