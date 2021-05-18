<template>
  <el-form-item>
    <label class="form-item-title">{{ $t('property.size') }}</label>
    <div class="in-line">
      <el-dropdown trigger="click" size="small">
        <span class="el-dropdown-link">
          <el-button size="medium">{{fontNumber}}
            <i class="el-icon-arrow-down el-icon--right"></i>
          </el-button>
        </span>
        <el-dropdown-menu class="font-size-menu" slot="dropdown">
          <el-dropdown-item
            v-for="(option,key) in fontType"
            :key="key"
            @mousedown.native="selectSize(option.label, option.value)">
            {{option.label}}
          </el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
      <el-button size="small" @click="addSize('FontSize',fontSize)">A+</el-button>
      <el-button size="small" @click="lessSize('FontSize',fontSize)">A-</el-button>
    </div>
  </el-form-item>
</template>

<script>
export default {
  name: 'FontSize',
  data () {
    return {
      fontNumber: '14px',
      fontSize: 2,
      fontType: [{
        label: '12px',
        value: 1
      }, {
        label: '14px',
        value: 2
      }, {
        label: '16px',
        value: 3
      }, {
        label: '18px',
        value: 4
      }, {
        label: '24px',
        value: 5
      }, {
        label: '32px',
        value: 6
      }, {
        label: '48px',
        value: 7
      }]
    }
  },
  mounted () {
    var suffix = document.querySelector('.el-input--suffix')
    suffix.onmousedown = function (event) {
      event.preventDefault()
    }
  },
  methods: {
    setCommandSize (type, style) {
      let data = { type, style }
      this.$emit('setCommand', data)
    },
    addSize (type, style) {
      if (this.fontSize < 7) {
        this.fontSize = ++this.fontSize
        this.matchSize()
        let data = { type, 'style': this.fontSize }
        this.$emit('setCommand', data)
      }
    },
    lessSize (type, style) {
      if (this.fontSize > 1) {
        this.fontSize = --this.fontSize
        this.matchSize()
        let data = { type, 'style': this.fontSize }
        this.$emit('setCommand', data)
      }
    },
    selectSize (fontNum, fontValue) {
      let ev = event || window.event
      if (ev.preventDefault) {
        ev.preventDefault()
      }
      ev.returnValue = false
      this.setCommandSize('FontSize', fontValue)
      this.fontSize = fontValue
      this.fontNumber = fontNum
    },
    matchSize () {
      for (var i = 0; i < this.fontType.length; i++) {
        if (this.fontSize === this.fontType[i].value) {
          this.fontNumber = this.fontType[i].label
        }
      }
    }
  }
}
</script>
