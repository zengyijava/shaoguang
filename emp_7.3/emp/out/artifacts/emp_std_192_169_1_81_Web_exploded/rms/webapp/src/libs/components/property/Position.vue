<template>
  <el-form-item>
    <label class="form-item-title">{{ $t('property.position') }}</label>
    <div class="in-line">
      <label class="input-group fl">
        X
        <el-input-number size="small" v-model.number.lazy="element.x" controls-position="right"></el-input-number>
      </label>
      <label class="input-group fr">
        Y
        <el-input-number size="small" v-model.number.lazy="element.y" controls-position="right"></el-input-number>
      </label>
    </div>
  </el-form-item>
</template>

<script>
import {mapGetters} from 'vuex'

export default {
  name: 'Position',
  computed: mapGetters(['element'])
}
</script>
