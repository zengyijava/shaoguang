<template>
  <div class="media" :class="{'active': element.active}" @mousedown="handleActivate">
    <slot></slot>
  </div>
</template>

<script>
import { mapGetters, mapMutations } from 'vuex'
export default {
  name: 'Media',
  mounted () {
    this.handleActivate()
  },
  computed: mapGetters(['mediaContent']),
  props: {
    element: {
      type: Object,
      default: () => { }
    }
  },
  methods: {
    ...mapMutations('media', ['selectElement']),
    /**
     * 处理选择
     */
    handleActivate () {
      // 设置当前激活状态
      this.mediaContent.forEach(item => {
        if (item === this.element) {
          item.active = true
        } else {
          item.active = false
        }
      })
      this.selectElement(this.element)
    }
  }
}
</script>

<style lang="less" scoped>
@import '../../libs/assets/less/variables';
.media {
  margin-bottom: 1px;
  border: 1px solid transparent;
  &.active {
    border: 1px dashed #2e95ff;
  }
}
</style>
