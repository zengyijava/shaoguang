<template>
  <el-form class="action" :model="element" onsubmit="event.preventDefault()">
    <el-form-item class="action-title">
      {{ $t('functionSetting.funSet') }}
    </el-form-item>
    <div class="form-item title-item">
      <div class="title">{{ $t('functionSetting.operationType') }}</div>
    </div>
    <div class="form-item">
      <el-tabs type="border-card" v-model="element.tabsAction" @tab-click="handleClick">
        <el-tab-pane :label="$t('functionSetting.urlJump')" name="2">
          <CardLinkJump></CardLinkJump>
        </el-tab-pane>
        <el-tab-pane class="copy" :label="$t('functionSetting.copy')" name="3">
          <el-form-item :label="$t('functionSetting.selected')">
            <el-radio-group v-model="element.param" class="param-group">
              <el-radio class="fl" v-for="param in intersects" :key="param.name" :label="param.name">
                {{param.name}}
              </el-radio>
            </el-radio-group>
          </el-form-item>
        </el-tab-pane>
        <el-tab-pane :label="$t('functionSetting.dial')" name="6">
          <el-form-item :label="$t('functionSetting.dialPhone')"
            prop="tel" :rules="[{pattern: /^\d*$/, message: $t('validation.beNumber')}]">
            <el-input type="tel" size="small" v-model="element.tel"></el-input>
          </el-form-item>
          <el-form-item>
            <p class="description">{{ $t('functionSetting.disec') }}</p>
          </el-form-item>
        </el-tab-pane>
      </el-tabs>
    </div>
  </el-form>
</template>

<script>
import {mapGetters, mapMutations} from 'vuex'
import CardLinkJump from './CardLinkJump'

export default {
  name: 'Action',
  components: {CardLinkJump},
  computed: mapGetters(['element', 'intersects']),
  methods: {
    ...mapMutations(['updateAction']),
    handleClick (e) {
      if (e.name !== undefined) {
        this.updateAction(e.name)
      }
    }
  }
}
</script>

<style lang="less">
  @import '../../libs/assets/less/action';
  .action {
    .el-tab-pane {
      .param-group {
        margin-bottom: 16px;
        .el-radio__label {
          font-size: 12px;
        }
      }
    }
    .copy {
      .el-radio-group {
        .el-radio {
          width: 80px;
          margin-left: 0;
          margin-bottom: 8px;
        }
      }
    }
  }
</style>
