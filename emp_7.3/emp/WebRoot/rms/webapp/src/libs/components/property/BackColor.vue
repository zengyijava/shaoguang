<template>
  <el-form-item>
    <label>{{suffix}}{{ $t('property.color') }}</label>
    <div class="clearfix">
      <el-color-picker v-model="style.color" :predefine="predefineColors"
        @change="setBackColor('BackColor',style.color)">
      </el-color-picker>
      <div class="color-list">
        <ul class="clearfix">
          <li v-for="(color,index) in colorType" :key="index"
            :class="color" @click="setBackColor(type, predefineColors[index])">
          </li>
        </ul>
      </div>
    </div>
  </el-form-item>
</template>

<script>
import {mapGetters} from 'vuex'

export default {
  name: 'BackColor',
  data () {
    return {
      predefineColors: [
        '#e0e0e0',
        '#ff4a37',
        '#f55b2e',
        '#ffca28',
        '#19aa00',
        '#47b4f8',
        '#79c7fa',
        '#a006ff',
        '#3750c0',
        '#1e1e1e'
      ]
    }
  },
  props: {
    type: {
      label: String,
      default: 'bgImage'
    }
  },
  computed: {
    ...mapGetters(['element', 'background', 'isBackGround']),
    style () {
      return this.background
    },
    suffix () {
      return this.$t('property.bg')
    },
    color () {
      return this.isBackGround === 'ForeColor' ? 'color' : 'background'
    },
    colorType () {
      return this.predefineColors.map((item) => {
        return 'c' + item.substring(1, 7)
      })
    }
  },
  methods: {
    setBackColor (type, style) {
      this.background.color = style
      let data = {type, 'boolean': true, style}
      this.$emit('setBackDrop', data)
    }
  }
}
</script>
<style lang="less">
  .el-color-picker,
  .color-list{
    float: left;
  }
  .color-list {
    width: 90px;
    margin-top: 4px;
    margin-left: 4px;
    ul li {
      margin-right: 4px;
      margin-bottom: 4px;
      width: 14px;
      height: 14px;
      float: left;
      border-radius: 2px;
      cursor: pointer;
    }
    .ce0e0e0 {
      background-color: #e0e0e0;
    }
    .cff4a37 {
      background-color: #ff4a37;
    }
    .cf55b2e {
      background-color: #f55b2e;
    }
    .cffca28 {
      background-color: #ffca28;
    }
    .c19aa00 {
      background-color: #19aa00;
    }
    .c47b4f8 {
      background-color: #47b4f8;
    }
    .c79c7fa {
      background-color: #79c7fa;
    }
    .ca006ff {
      background-color: #a006ff;
    }
    .c3750c0 {
      background-color: #3750c0;
    }
    .c1e1e1e {
      background-color: #1e1e1e;
    }
  }
</style>
