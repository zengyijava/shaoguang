<template>
  <el-form-item class="tool-bar">
    <div class="checkbox-group">
      <el-checkbox-button
        v-model="style.fontWeight" false-label="normal" true-label="bold">
        <i class="el-icon-ali-jiacu"></i>
      </el-checkbox-button>
    </div>

    <el-radio-group fill="#e9f8f3" text-color="#8f9daf"
      v-model="style.textAlign">
      <el-radio-button label="left">
        <i class="el-icon-ali-zuoduiqi"></i>
      </el-radio-button>
      <el-radio-button label="center">
        <i class="el-icon-ali-juzhong"></i>
      </el-radio-button>
      <el-radio-button label="right">
        <i class="el-icon-ali-youduiqi"></i>
      </el-radio-button>
    </el-radio-group>
  </el-form-item>
</template>

<script>
import {mapGetters} from 'vuex'

export default {
  name: 'ToolBar',
  computed: mapGetters(['style'])
}
</script>
<style lang="less">
  @import '../../../libs/assets/less/variables';
  @bg-color: #e8f3ff;
  @text-color: #8f9daf;
  .tool-bar {
    .el-form-item__content {
      line-height: normal;
    }
    .checkbox-group {
      display: inline-block;
      .el-checkbox-button {
        border: 1px solid transparent;
        .el-checkbox-button__inner {
          padding: 2px;
          color: @text-color;
          background-color: @white-light;
          border: 2px solid @white-light;
        }
        &.is-checked {
          padding: 0;
          background-color: @blue-hover;
          border: 1px solid @blue-border;
          border-radius: 2px;
          .el-checkbox-button__inner{
            background-color: @blue-hover;
            border: 2px solid @blue-hover;
          }
        }
        & + .el-checkbox-button {
          margin-left: 8px;
        }
      }
    }
    .el-radio-group {
      // margin-left: 10px;
      border-left: 1px solid @border;
      .el-radio-button {
        border: 1px solid transparent;
        margin-left: 8px;
        .el-radio-button__inner {
          padding: 2px;
          color: @text-color;
          background-color: @white-light;
          border: 2px solid @white-light;
        }
        &.is-active {
          padding: 0;
          background-color: @blue-hover;
          border: 1px solid @blue-border;
          border-radius: 2px;
          .el-radio-button__inner{
            background-color: @blue-hover !important;
            border: 2px solid @blue-hover !important;
            box-shadow: rgb(233, 248, 243) 0px 0px 0px !important;
          }
        }
      }
    }
  }
</style>
