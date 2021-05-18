<template>
  <div class="editor">
    <div class="editor-mask">
      <Canvas></Canvas>
    </div>
    <LayerManage></LayerManage>
    <Settings></Settings>
  </div>
</template>

<script>
import Canvas from './Canvas'
import LayerManage from './LayerManage'
import Settings from './Settings'

export default {
  name: 'CardEditor',
  components: {Settings, Canvas, LayerManage}
}
</script>

<style lang="less" scoped>
  .editor {
    display: flex;
    justify-content: space-between;
    align-items: center;
    .layer-manage {
      width: 220px;
      height: 100%;
      background: #fafafa;
      z-index: 10;
      overflow-y: auto;
    }
  }
</style>
