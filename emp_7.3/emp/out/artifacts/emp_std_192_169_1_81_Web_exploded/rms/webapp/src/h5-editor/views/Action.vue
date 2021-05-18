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
          <H5LinkJump></H5LinkJump>
        </el-tab-pane>
        <el-tab-pane :label="$t('functionSetting.dial')" name="6">
          <el-form-item :label="$t('functionSetting.dialPhone')"
            prop="tel" :rules="[{pattern: /^\d*$/, message: $t('validation.beNumber')}]">
            <el-input size="small" v-model="element.tel"></el-input>
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
import H5LinkJump from './H5LinkJump'

export default {
  name: 'Action',
  components: {H5LinkJump},
  computed: mapGetters(['element']),
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
</style>
