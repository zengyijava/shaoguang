<template>
  <div class="media-cont" v-html="showMsg(previewData)"></div>
</template>

<script>
export default {
  name: 'MsgPreview',
  data: function () {
    return {
    }
  },
  props: {
    previewData: String
  },
  methods: {
    showMsg: function (msg) {
      return JSON.parse(msg).template
    }
  }
}
</script>

<style scoped>
.phone-preview .media-cont{
  display: inline-block;
  max-width: 244px;
  /* min-width: 90px; */
  padding: 8px;
  /* min-height: 250px; */
  line-height: 1.3;
  word-break: break-all;
}
</style>
