<template>
  <div v-html="showRichText(previewData)" class="media-cont"></div>
</template>

<script>
export default {
  name: 'RichTextPreview',
  data: function () {
    return {
    }
  },
  props: {
    previewData: String,
    params: {
      type: Array,
      default: () => {
        return []
      }
    }
  },
  methods: {
    showRichText: function (previewData) {
      let _params = this.params
      let _paramsLength = _params.length

      if (_paramsLength > 0) {
        return _params[0].content
      } else {
        return JSON.parse(previewData).template
      }
    }
  }
}
</script>

<style scoped>
.phone-preview .media-cont {
  display: inline-block;
  max-width: 244px;
  /* min-width: 90px; */
  padding: 8px;
  /* min-height: 250px; */
  word-break: break-all;
  line-height: 1.3;
}
</style>
