<template>
  <aside class="canvas-tool-bar">
    <vue-drr drag-handle=".handler-wrapper" :w="40" h="auto" :minw="40" :minh="96" :resizable="false"
      :rotatable="false">
      <div class="handler-wrapper">
        <div class="handler">=</div>
      </div>
      <div class="button-group">
        <el-tooltip :disabled="!canRedo" effect="dark" :content="$t('public.redo')" placement="right">
          <el-button type="text" icon="el-icon-ali-redo"
            :disabled="!canRedo" @click.stop="handleRedo">
          </el-button>
        </el-tooltip>
        <el-tooltip :disabled="!canUndo" effect="dark" :content="$t('public.revoke')" placement="right">
          <el-button type="text" icon="el-icon-ali-undo"
            :disabled="!canUndo" @click.stop="handleUndo">
          </el-button>
        </el-tooltip>
        <slot></slot>
      </div>
    </vue-drr>
  </aside>
</template>

<script>
import VueDrr from './vue-drr'

export default {
  name: 'CanvasToolBar',
  components: {VueDrr},
  mounted () {
    if (this.useKeymap) {
      document.addEventListener('keydown', this.handleKeyDown)
    }
  },
  destroyed () {
    if (this.useKeymap) {
      document.removeEventListener('keydown', this.handleKeyDown)
    }
  },
  props: {
    histories: {
      type: Array,
      default: () => []
    },
    useKeymap: {
      type: Boolean,
      default: true
    }
  },
  data () {
    return {
      undone: []
    }
  },
  computed: {
    canRedo () {
      return this.undone.length
    },
    canUndo () {
      if (this.histories === []) {
        return false
      }
      return this.histories.length
    }
  },
  methods: {
    handleKeyDown (e) {
      if (e.ctrlKey && e.keyCode === 89) { // CTRL + Y
        this.handleRedo()
      } else if (e.ctrlKey && e.keyCode === 90) { // CTRL + Z
        this.handleUndo()
      }
    },
    handleRedo () {
      let last = this.undone.pop()
      this.histories.push(last)
      this.$emit('update-history', last)
    },
    handleUndo () {
      this.undone.push(this.histories.pop())
      const lastIndex = this.histories.length - 1
      let last = this.histories[lastIndex]
      this.$emit('update-history', last)
    }
  }
}
</script>

<style lang="less">
  @import '../../libs/assets/less/variables';

  .canvas-tool-bar {
    position: absolute;
    top: 124px;
    left: 340px;
    border-radius: 2px;
    z-index: 5;
    .z-active {
      border: none !important;
    }
    .handler-wrapper {
      height: 16px;
      line-height: 16px;
      background-color: #fff;
      .handler {
        background-color: #f3f3f3;
        vertical-align: middle;
        color: #d3d3d3;
        text-align: center;
      }
    }
    .button-group {
      width: 40px;
      background-color: #fff;
      box-shadow: 0px 1px 10px 0px rgba(0, 0, 0, 0.1);
      .el-button {
        display: block;
        width: 40px;
        height: 40px;
        border-radius: 0;
        color: @grey;
        &:hover {
          background: @green;
          color: #fff;
        }
        &.is-disabled {
          cursor: default;
          background-color: transparent;
          color: #ddd;
        }
      }
    }
  }
</style>
