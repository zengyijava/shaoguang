<template>
  <div v-if="hasBgImg || background.color" class="bg-preview" :style="getStyle">
    <img v-if="hasBgImg" :src="background.src" :style="crop.style">
  </div>
</template>

<script>
export default {
  name: 'BgPreview',
  props: {
    actualWidth: {
      type: Number,
      default: 320
    },
    background: {
      type: Object,
      default: () => {
      }
    }
  },
  computed: {
    crop () {
      return this.background.crop;
    },
    hasBgImg () {
      return this.background.src
    },
    opacity () {
      return 1 - this.background.transparency
    },
    getStyle () {
      const crop = this.crop
      const background = this.background
      if (this.hasBgImg) {
        return {
          width: crop.w + 'px',
          height: crop.h + 'px',
          transform: 'scale(' + this.actualWidth / this.crop.w + ')',
          opacity: this.opacity
        }
      } else if (background.color) {
        return {
          backgroundColor: background.color,
          transform: 'scale(' + this.actualWidth / 320 + ')',
          opacity: this.opacity
        }
      }
    }
  }
}
</script>

<style scoped>
  .bg-preview {
    position: absolute;
    width: 320px;
    height: 508px;
    overflow: hidden;
    transform-origin: left top;
  }
</style>
