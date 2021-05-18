<template>
  <el-form-item>
    <label class="form-item-title">{{suffix}}{{ $t('property.color') }}</label>
    <div class="clearfix">
      <el-color-picker v-model="style[color]" :predefine="predefineColors"
        @change="setCommandColor('ForeColor',style.color)" @mousedown.native="openPicker">
      </el-color-picker>
      <div class="color-list">
        <ul class="clearfix">
          <li v-for="(color,index) in colorType" :key="index"
            :class="color" @mousedown.prevent="setCommandColor(type, predefineColors[index])">
          </li>
        </ul>
      </div>
    </div>
  </el-form-item>
</template>

<script>

export default {
  name: 'FontColor',
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
      default: 'ForeColor'
    }
  },
  computed: {
    style () {
      if (this.$store.getters.element) {
        return this.$store.getters.element.style
      } else if (this.$store.getters['media/element']) {
        return this.$store.getters['media/element'].style
      }
      return {
        color: '#1e1e1e',
        backgroundColor: '#fff'
      }
    },
    suffix () {
      return this.type === 'ForeColor' ? this.$t('property.text') : this.$t('property.bg')
    },
    color () {
      return this.type === 'ForeColor' ? 'color' : 'backgroundColor'
    },
    colorType () {
      return this.predefineColors.map((item) => {
        return 'c' + item.substring(1, 7)
      })
    }
  },
  mounted () {
    let colorDropdown = document.querySelector('.el-color-dropdown__main-wrapper')
    let colorPredefine = document.querySelector('.el-color-predefine')
    colorDropdown.onmousedown = colorPredefine.onmousedown = function (event) {
      event.preventDefault()
    }
    // let plckerPanel = document.querySelector('.el-color-picker__panel')
    // plckerPanel.onmousedown = function (event) {
    //   event.preventDefault()
    // }
  },
  methods: {
    setCommandColor (type, style) {
      if (type === 'ForeColor' || this.type === 'ForeColor') {
        this.style.color = style
      } else {
        this.style.backgroundColor = style
      }
      let data = {type, 'boolean': true, style}
      this.$emit('setCommand', data)
    },
    openPicker (event) {
      let ev = event || window.event
      if (ev.preventDefault) {
        ev.preventDefault()
      }
      ev.returnValue = false
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
