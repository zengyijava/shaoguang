<template>
  <div v-if="markData.length > 0 && markData.markType != 2" class="mark-preview">
    <span v-if="markData.resourceMode == 2 && markData.signType == 2">【{{ markData.smsSign }}】</span>
    <span>{{ markData.smsContent }}</span>
    <span v-if="markData.resourceMode == 1" class="blue-text">{{ markData.resourceUrl }}</span>
    <span v-if="markData.resourceMode == 2 && markData.signType == 1">【{{ markData.smsSign }}】</span>
    <span></span>
  </div>
</template>

<script>
export default {
  name: 'MarkPreview',
  props: {
    markData: {
      type: Object,
      default: () => {
        return {}
      }
    }
  }
}
</script>

<style lang="less">
.mark-preview{
  width: 240px;
  padding: 4px 10px;
  margin-top: 6px;
  margin-left: 38px;
  line-height: 1.3;
  border: 1px solid #d6d6d6;
  border-radius: 16px;
  .blue-text{
    color: #409EFF;
  }
}
</style>
