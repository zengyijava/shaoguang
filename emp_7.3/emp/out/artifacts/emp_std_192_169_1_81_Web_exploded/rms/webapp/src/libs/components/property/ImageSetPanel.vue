<template>
  <div class="action">
    <el-form-item class="action-title">
      {{ $t('property.funSet') }}
    </el-form-item>
    <div class="form-item">
      <div class="title">{{ $t('property.click') }}</div>
    </div>
    <div class="form-item">
      <el-radio-group v-model="element.action">
        <el-radio label="0">{{ $t('property.none') }}</el-radio>
        <el-tooltip effect="dark" :content="$t('property.clickBigImg')" placement="top-start">
          <el-radio label="1">{{ $t('property.lookBigImg') }}</el-radio>
        </el-tooltip>
        <el-radio label="2">{{ $t('property.openLink') }}</el-radio>
      </el-radio-group>
    </div>
    <el-form-item v-if="element.action === '2'" :label="$t('property.clickOpenLink')">
      <el-input size="small" v-model.trim="element.url"></el-input>
    </el-form-item>
    <el-form-item>
      <p class="description">
        {{ $t('property.explain') }}
      </p>
    </el-form-item>
  </div>
</template>

<script>
import {mapGetters} from 'vuex'

export default {
  name: 'Action',
  data () {
    return {
      action: {}
    }
  },
  computed: mapGetters(['element'])
}
</script>

<style lang="less">
  .action {
    .action-title {
      padding-left: 40px;
      background-color: #f1f1f1;
    }
    .form-item {
      padding: 10px 20px;
      .action-title {
        line-height: 18px;
      }
    }
    .el-tab-pane {
      .param-group {
        margin-bottom: 16px;
        .el-radio__label {
          font-size: 12px;
        }
      }
    }
    .el-tabs {
      .el-tabs__item {
        padding-left: 16px;
      }
    }
    .copy {
      .el-radio-group {
        .el-radio {
          width: 110px;
          margin-left: 0;
          margin-top: 12px;
        }
      }
    }
  }
</style>
