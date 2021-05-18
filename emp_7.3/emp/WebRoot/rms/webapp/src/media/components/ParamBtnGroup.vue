<template>
  <div class="param-btn-group">
    <p v-if="intersects.length === 0">{{ $t('media.pParems') }}</p>
    <el-button class="param-btn" type="text" size="small"
      v-for="param in intersects"
      :key="param.name" :disabled="getDisabled(param)"
      @click="handleInsert(param)">+{{param.name}}
    </el-button>
  </div>
</template>

<script>
import {mapGetters, mapMutations} from 'vuex'

export default {
  name: 'ParamBtnGroup',
  computed: {
    ...mapGetters(['mediaContent', 'intersects']),
    ...mapGetters('media', ['usedParams'])
  },
  methods: {
    ...mapMutations('media', ['addUsedParam']),
    getDisabled (param) {
      let disabled = false
      if (param.type === 14) {
        disabled = true
      }
      let value = '{#' + param.name + '#}'
      this.mediaContent.forEach(item => {
        if (item.type === 'text' || item.type === 'image') {
          if (item.text.includes(value)) {
            disabled = true
          }
        }
      })
      return disabled
    },
    handleInsert (param) {
      if (this.usedParams.indexOf(param) === -1 && param.type !== 14) {
        this.addUsedParam(param)
      }
    }
  }
}
</script>

<style lang="less">
  .param-btn-group {
    .el-button {
      width: 80px;
      background-color: #f4f5f8;
      margin-right: 12px;

      & + .el-button {
        margin-left: 0;
        margin-top: 8px;
      }

      &:nth-child(3n + 0) {
        margin-right: 0;
      }
    }
  }
</style>
