<template>
  <el-form-item :label="$t('titleSetting.titleName')">
    <el-input size="small" :placeholder="$t('titleSetting.enterName')" v-model="element.text" :maxlength="15"></el-input>
  </el-form-item>
</template>

<script>
import {mapGetters} from 'vuex'

export default {
  name: 'TitleSetting',
  computed: mapGetters(['element'])
}
</script>
