<template>
  <vue-drr :angle="element.rotate"
    :x="element.x"
    :y="element.y"
    :w="element.w"
    :h="element.h"
    :overflowY="element.overflowY"
    :active="element.active"
    :parent="parent"
    :axis="axis"
    :handles="handles"
    :resizable="resizable"
    :draggable="draggable"
    :rotatable="false"
    @dragstop="handleDragStop"
    @resizestop="handleResizeStop"
    @activated="handleActivate"
    @deactivated="handleDeactivate">
    <slot></slot>
  </vue-drr>
</template>

<script>
import { mapGetters, mapMutations } from 'vuex'
import VueDrr from '../../libs/components/vue-drr'

export default {
  name: 'ElementDRR',
  components: { VueDrr },
  mounted () {
    this.handleActivate()
  },
  computed: mapGetters('media', ['textEditable']),
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
      default: true
    },
    axis: {
      type: String,
      default: 'both'
    },
    handles: {
      type: Array,
      default: () => ['n', 'e', 's', 'w', 'nw', 'ne', 'se', 'sw']
    },
    parent: {
      type: Boolean,
      default: false
    }
  },
  methods: {
    ...mapMutations('media', ['selectCurrentElement']),
    // 同步更新容器高度
    updateHeight () {
      if (this.element.type === 'text') {
        let _self = this
        const node = this.$el
        this.$nextTick(() => {
          const h = Math.ceil(node.querySelector('.rich-text').getBoundingClientRect().height)
          node.style.height = h + 'px'
          _self.element.h = h
        })
      }
    },
    // 缩放文本时更新高度
    handleResizing () {
      if (this.element.type === 'text') {
        this.updateHeight()
      }
    },
    /**
     * 处理缩放
     */
    handleResizeStop (x, y, w, h) {
      this.element.x = x
      this.element.y = y
      this.element.w = w
      this.element.width = w
      if (this.element.type === 'text') {
        this.updateHeight()
      } else {
        this.element.h = h
      }
    },
    /**
     * 处理拖放
     */
    handleDragStop (x, y) {
      this.element.x = x
      this.element.y = y
    },
    /**
     * 处理选择
     */
    handleActivate () {
      this.textEditable.forEach(item => {
        if (this.element.tag === item.tag) {
          item.active = true
        } else {
          item.active = false
        }
      })
      this.selectCurrentElement(this.element)
      if (this.element.type === 'text') {
        this.$emit('tellShowText', true)
      } else {
        this.$emit('tellShowText', false)
      }
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
