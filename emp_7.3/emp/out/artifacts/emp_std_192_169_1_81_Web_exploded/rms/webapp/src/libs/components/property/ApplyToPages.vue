<template>
  <el-form-item>
    <el-button class="applyBox" type="primary" size="medium" @click="applyAction">
      {{ $t('H5.apply_hint') }}
    </el-button>
  </el-form-item>
</template>

<script>
import {mapGetters} from 'vuex'
import utils from '../../utils'
export default {
  name: 'ApplyToPages',
  data () {
    return {
      equal: false,
      visible: false
    }
  },
  computed: {
    ...mapGetters(['background', 'pages'])
  },
  methods: {
    applyAction () {
      this.pages.map(item => {
        item.background = utils.deepClone(this.background)
      })
    }
  }
}
</script>

<style>
  .applyBox{
    width: 100%;
  }
</style>
