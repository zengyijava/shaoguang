<template>
  <section class="left-panel">
    <div class="left-panel-header">
      <slot name="leftHeader">
        <div class="left-header-title">{{title}}</div>
        <a v-if="showClose" class="el-icon-close close-btn" @click="handleClose"></a>
      </slot>
    </div>
    <div class="left-panel-body">
      <slot name="leftBody"></slot>
    </div>
  </section>
</template>

<script>
export default {
  name: 'Panel',
  props: {
    title: {
      type: String,
      default: ' '
    },
    showClose: {
      type: Boolean,
      default: true
    }
  },

  methods: {
    handleClose () {
      if (this.showClose) {
        this.$emit('close')
      }
    }
  }
}
</script>

<style lang="less">
  @import "../assets/less/variables";

  .left-panel {
    position: relative;
    width: 100%;
    height: 100%;
    box-sizing: border-box;
    z-index: 2;
    overflow: hidden;
    .left-panel-header {
      position: relative;
      height: 50px;
      line-height: 50px;
      padding-left: 18px;
      padding-right: 18px;
      vertical-align: middle;
      background: #fff;
      border-bottom: solid 1px @border;
      z-index: 10;
      .header-title {
        font-size: 16px;
        color: @grey-black;
      }
      .close-btn {
        position: absolute;
        top: 20px;
        right: 18px;
        padding: 0;
        background: transparent;
        border: none;
        outline: none;
        cursor: pointer;
        color: @grey;
        font-weight: bold;
        &:hover {
          color: @green;
        }
      }
    }
    .left-panel-body {
      position: absolute;
      overflow-y: auto;
      overflow-x: hidden;
      width: 100%;
      top: 50px;
      bottom: 0;
      .el-form > {
        padding-top: 8px;
        padding-bottom: 8px;
        .el-form-item {
          padding-left: 20px;
          padding-right: 20px;
        }
      }
    }
  }
</style>
