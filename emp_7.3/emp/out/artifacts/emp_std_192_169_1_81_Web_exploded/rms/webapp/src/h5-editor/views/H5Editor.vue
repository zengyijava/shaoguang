<template>
  <div class="editor">
    <div class="editor-mask">
      <Canvas></Canvas>
    </div>
    <Overview></Overview>
    <Material></Material>
    <Settings></Settings>
  </div>
</template>

<script>
import Canvas from './Canvas'
import Settings from './Settings'
import Overview from './Overview'
import Material from './Material'

export default {
  name: 'H5Editor',
  components: {Overview, Settings, Canvas, Material}
}
</script>
<style lang="less" scoped>
  .editor {
    display: flex;
    justify-content: space-between;
    align-items: center;
    .overview {
      width: 220px;
      height: 100%;
      background: #fafafa;
      z-index: 10;
      overflow-y: auto;
    }
  }
</style>
