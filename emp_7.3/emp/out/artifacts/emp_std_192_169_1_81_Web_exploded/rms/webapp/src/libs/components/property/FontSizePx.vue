<template>
  <el-form-item>
    <label class="form-item-title">{{ $t('property.size') }}</label>
    <div class="in-line">
      <el-select size="small" v-model="style.fontSize">
        <el-option v-for="option in options"
          :label="option" :value="option" :key="option">
        </el-option>
      </el-select>
      <el-button size="small" @click="addSize()">A+</el-button>
      <el-button size="small" @click="lessSize()">A-</el-button>
    </div>
  </el-form-item>
</template>

<script>
import {mapGetters} from 'vuex'

export default {
  name: 'FontSize',
  computed: mapGetters(['style']),
  data () {
    return {
      options: ['12px', '14px', '16px', '18px', '24px', '32px', '42px', '48px', '64px', '72px', '88px']
    }
  },
  methods: {
    addSize () {
      let size = parseInt(this.style.fontSize)
      if (size < 120) {
        this.style.fontSize = size + 2 + 'px'
      }
    },
    lessSize () {
      let size = parseInt(this.style.fontSize)
      if (size > 12) {
        this.style.fontSize = size - 2 + 'px'
      }
    }
  }
}
</script>
