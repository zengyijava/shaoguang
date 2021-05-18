<template>
  <div class="overview">
    <div class="clearfix">
      <span class="panel" :class="{ active: viewState === 0 }" @click.stop="viewState = 0">{{ $t('H5.pages_admin') }}</span>
      <span class="panel" :class="{ active: viewState === 1 }" @click.stop="viewState = 1">{{ $t('H5.layer_admin') }}</span>
    </div>
    <PageAdmin v-show="viewState === 0"></PageAdmin>
    <LayerAdmin v-show="viewState === 1"></LayerAdmin>
  </div>
</template>
<script>

import PageAdmin from './PageAdmin'
import LayerAdmin from './LayerAdmin'

export default {
  name: 'Overview',
  components: {PageAdmin, LayerAdmin},
  data () {
    return {
      viewState: 0
    }
  }
}
</script>
<style lang="less" scoped>
  .overview {
    position: absolute;
    top: 0;
    border-right: 1px solid #d6d6d6;
    background-color: #fafafa;
    height: 100%;
    .clearfix {
      height: 40px;
      // font-size: 16px;
      border-bottom: 1px solid #d6d6d6;
    }
    .panel {
      float: left;
      line-height: 40px;
      width: 50%;
      color: #000;
      text-align: center;
      background-color: #eee;
      cursor: pointer;
      &.active {
        color: #3b9cff;
        background-color: transparent;
      }
    }
  }
</style>
