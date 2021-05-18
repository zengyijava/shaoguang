<template>
  <div class="media-editor">
    <Canvas></Canvas>
    <Settings></Settings>
  </div>
</template>

<script>
import Canvas from './Canvas'
import Settings from './Settings'

export default {
  name: 'MediaEditor',
  components: {Settings, Canvas}
}
</script>
