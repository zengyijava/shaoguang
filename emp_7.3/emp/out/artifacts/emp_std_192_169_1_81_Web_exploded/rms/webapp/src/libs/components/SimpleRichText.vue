<template>
  <div class="rich-text"
    spellcheck="false"
    contenteditable="true"
    v-html="innerText"
    @focus="handleFocus"
    @blur="handleBlur"
    @keydown="$event.stopPropagation()"
    @keydown.enter="preventEnter"
    @keyup.stop="emitInput($event.target.innerHTML)"
    @paste="handlePaste">
  </div>
</template>

<script>
import utils from '../utils'

export default {
  name: 'SimpleRichText',
  data () {
    return {
      innerText: this.value.replace(/\n/g, '<br>') || this.placeholder,
      isLocked: false
    }
  },
  props: {
    value: {
      type: String,
      default: ''
    },
    placeholder: {
      type: String,
      default: ''
    },
    supportReturn: {
      type: Boolean,
      default: true
    }
  },
  watch: {
    value (newValue, oldValue) {
      if (!this.isLocked || !this.innerText) {
        const reg = /<span .+>(.+)<\/span>/g
        this.innerText = newValue.replace(reg, '$1')
        // 强行删除参数文本时同步
        this.$el.innerHTML = newValue.replace(reg, '$1')
      }
    }
  },
  methods: {
    // 派发事件
    emitInput (innerText) {
      this.$emit('input', innerText)
    },
    handleFocus (event) {
      this.isLocked = true
      if (event.target.innerHTML === this.placeholder) {
        event.target.innerHTML = ''
      }
      this.$emit('focus')
    },
    handleBlur (event) {
      this.isLocked = false
      if (event.target.innerHTML === '') {
        event.target.innerHTML = this.placeholder
      }
      this.$emit('blur')
    },
    handlePaste (event) {
      this.emitInput(utils.getPlainText(event))
    },
    preventEnter (event) {
      if (!this.supportReturn) {
        event.preventDefault()
      }
    }
  }
}
</script>

<style lang="less">
  .rich-text {
    padding: 4px 8px;
    line-height: 1.3;
    outline: none;
    word-break: break-all;
    box-sizing: border-box;
    vertical-align: middle;
    border-radius: 0;
  }

  .j-btn {
    &:hover {
      color: #000099;
      cursor: pointer;
    }
    &.active {
      color: #409EFF;
      cursor: pointer;
    }
  }
</style>
