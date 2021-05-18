<template>
  <vue-drr
    :x="element.x"
    :y="element.y"
    :z="element.z"
    :w="element.w"
    :h="element.h"
    :active="element.active"
    :parent="false"
    :axis="axis"
    :handles="handles"
    :minw="minw"
    :can-deactive="false"
    :resizable="resizable"
    :draggable="draggable"
    :rotatable="rotatable"
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
  mounted () {
    this.handleActivate()
  },
  computed: {
    ...mapGetters(['content', 'elements']),
    contentElement () {
      return this.content.elements
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
        const h = Math.round(node.lastChild.getBoundingClientRect().height)
        node.style.height = h + 'px'
      }
    },
    // 处理缩放
    handleResizeStop (x, y, w, h) {
      this.element.x = x
      this.element.y = y
      this.element.w = w
      if (this.element.type !== 'text') {
        this.element.h = h
      } else {
        this.updateHeight()
      }
      this.addHistory(this.contentElement)
    },
    // 处理拖放
    handleDragStop (x, y) {
      this.element.x = x
      this.element.y = y
      this.addHistory(this.contentElement)
    },
    // 处理选择
    handleActivate () {
      // 设置当前激活状态
      this.elements.forEach(item => {
        if (item === this.element) {
          item.active = true
        } else {
          item.active = false
        }
      })
      this.selectElement(this.element)
    },
    handleDeactivate () {
      this.element.active = false
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
