<template>
  <div class="material-panel-header clearfix">
    <el-checkbox v-show="isBatch" v-model="checkedAll" @change="$emit('input' , checkedAll)" class="check-all fl">{{ $t('H5.select_all') }}
    </el-checkbox>
    <el-button v-show="isBatch" @click="$emit('delete')">{{ $t('H5.dele') }}</el-button>
    <el-button class="batch-btn" @click="handleBatch">{{isBatch ? $t('H5.completed') : $t('H5.batch_operation')}}</el-button>
    <slot></slot>
  </div>
</template>
<script>
export default {
  name: 'MaterialPanelHeader',
  props: {
    isBatch: {
      type: Boolean,
      default: false
    },
    checkedAll: {
      type: Boolean,
      default: false
    }
  },
  methods: {
    handleBatch () {
      this.$emit('batch', true)
    }
  }
}
</script>

<style lang="less">
  .material-panel-header {
    padding: 10px 18px;
    height: 32px;
    text-align: right;
    .el-button {
      height: 32px;
      padding: 8px 16px;
      &.batch-btn {
        margin-right: 14px;
      }
    }
    .check-all {
      line-height: 32px;
    }
  }
</style>
