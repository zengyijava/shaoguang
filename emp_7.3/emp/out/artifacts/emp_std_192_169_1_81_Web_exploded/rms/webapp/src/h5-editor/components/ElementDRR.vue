<template>
  <vue-drr
    :x="element.x"
    :y="element.y"
    :z="element.z"
    :w="element.w"
    :h="element.h"
    :active="element.active && !element.locked"
    :parent="false"
    :axis="axis"
    :handles="handles"
    :minw="minw"
    :resizable="resizable && !element.locked"
    :draggable="draggable && !element.locked"
    :rotatable="rotatable && !element.locked"
    v-show="element.visible"
    @dragstop="handleDragStop"
    @resizestop="handleResizeStop"
    @activated="handleActivate"
    @deactivated="handleDeactivate">
    <slot></slot>
  </vue-drr>
</template>

<script>
import {mapGetters, mapMutations} from 'vuex'
import VueDrr from '../../libs/components/vue-drr'

export default {
  name: 'ElementDRR',
  components: {VueDrr},
  computed: mapGetters(['elements']),
  props: {
    element: {
      type: Object,
      default: () => {
      }
    },
    draggable: {
      type: Boolean,
      default: true
    },
    resizable: {
      type: Boolean,
      default: true
    },
    rotatable: {
      type: Boolean,
      default: false
    },
    axis: {
      type: String,
      default: 'both'
    },
    handles: {
      type: Array,
      default: () => ['n', 'e', 's', 'w', 'nw', 'ne', 'se', 'sw']
    },
    minw: {
      type: Number,
      default: 28
    }
  },
  methods: {
    ...mapMutations(['selectElement', 'addHistory']),
    // 处理缩放
    handleResizeStop (x, y, w, h) {
      this.element.x = x
      this.element.y = y
      this.element.w = w
      if (this.element.type !== 'text') {
        this.element.h = h
      }
      this.addHistory(this.elements)
    },
    // 处理拖放
    handleDragStop (x, y) {
      this.element.x = x
      this.element.y = y
      this.addHistory(this.elements)
    },
    // 处理选择
    handleActivate () {
      this.selectElement(this.element)
    },
    handleDeactivate () {
      this.element.active = false
    }
  }
}
</script>
