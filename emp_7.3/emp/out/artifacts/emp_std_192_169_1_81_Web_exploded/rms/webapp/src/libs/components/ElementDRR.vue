<template>
  <vue-drr
    :x="element.x"
    :y="element.y"
    :z="element.z"
    :w="element.w"
    :h="element.h"
    :ratio="element.ratio"
    :active="element.active && !element.locked"
    :parent="false"
    :axis="axis"
    :handles="handles"
    :minw="minw"
    :resizable="resizable && !element.locked"
    :draggable="draggable && !element.locked"
    :rotatable="rotatable && !element.locked"
    v-show="element.hasOwnProperty('visible') ? element.visible : true"
    @dragstop="handleDragStop"
    @resizing="handleResizing"
    @resizestop="handleResizeStop"
    @activated="handleActivate"
    @deactivated="handleDeactivate">
    <slot></slot>
  </vue-drr>
</template>

<script>
import {mapGetters, mapMutations} from 'vuex'
import VueDrr from './vue-drr'

export default {
  name: 'ElementDRR',
  components: {VueDrr},
  computed: {
    ...mapGetters(['elements', 'content']),
    contentElement () {
      if (this.$store.getters.content) {
        return this.content.elements
      }
    }
  },
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
    // 同步更新容器高度
    updateHeight () {
      if (this.element.type === 'text') {
        const node = this.$el
        this.$nextTick(() => {
          const h = Math.ceil(node.lastChild.getBoundingClientRect().height)
          node.style.height = h + 'px'
        })
      }
    },
    // 缩放文本时更新高度
    handleResizing () {
      if (this.element.type === 'text') {
        this.updateHeight()
      }
    },
    // 处理缩放
    handleResizeStop (x, y, w, h) {
      if (this.element.x !== x || this.element.y !== y || this.element.w !== w || this.element.h !== h) {
        this.element.x = x
        this.element.y = y
        this.element.w = w
        if (this.element.type === 'text') {
          this.updateHeight()
        } else {
          this.element.h = h
        }
        this.addEleHistory()
      }
    },
    // 处理拖放
    handleDragStop (x, y) {
      if (this.element.x !== x || this.element.y !== y) {
        this.element.x = x
        this.element.y = y
        this.addEleHistory()
      }
    },
    // 处理选择
    handleActivate () {
      this.selectElement(this.element)
    },
    handleDeactivate () {
      this.element.active = false
      this.$emit('deactivated')
    },
    // 添加历史记录
    addEleHistory () {
      // 数据结构不一致
      if (this.contentElement) {
        this.addHistory(this.contentElement)
      } else {
        this.addHistory(this.elements)
      }
    }
  },
  watch: {
    'element.style.fontSize' () {
      this.$nextTick(() => {
        this.updateHeight()
      })
    }
  }
}
</script>
