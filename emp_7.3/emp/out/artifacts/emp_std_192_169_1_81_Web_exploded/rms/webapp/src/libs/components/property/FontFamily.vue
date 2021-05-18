<template>
  <el-form-item :label="$t('property.family')">
    <label class="form-item-title"></label>
    <el-select size="medium" v-model="style.fontFamily">
      <el-option :label="option" :value="option"
        v-for="option in options" :key="option">
      </el-option>
    </el-select>
  </el-form-item>
</template>

<script>
import {mapGetters} from 'vuex'
export default {
  name: 'FontSize',
  data () {
    return {
      options: [
        '宋体',
        '微软雅黑',
        'Hiragino Sans GB',
        'PingFang SC',
        'Helvetica Neue',
        'Helvetica',
        'Arial'
      ]
    }
  },
  computed: mapGetters(['style'])
}
</script>
<style>
  .el-select {
    width: 100%
  }
</style>
