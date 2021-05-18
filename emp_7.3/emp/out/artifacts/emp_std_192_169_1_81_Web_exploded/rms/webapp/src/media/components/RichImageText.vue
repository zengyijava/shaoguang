<template>
  <p class="rich-text"
    :contenteditable="canEdit"
    @dblclick="canEdit=false"
    @focus="handleFocus"
    @blur="handleBlur"
    @keydown.enter="preventEnter"
    @keyup="handleChange"
    @paste="handlePaste"
    @click.prevent="getText(value)"
    ref="myMediaText"
    v-html="innerText"
    :style="element.style">
  </p>
</template>

<script>
import utils from '../../libs/utils'
import { mapMutations } from 'vuex'
export default {
  name: 'EditableText',
  props: {
    value: {
      type: String,
      default: ''
    },
    element: {
      type: Object,
      default: () => {
        return {}
      }
    },
    supportReturn: {
      type: Boolean,
      default: true
    }
  },
  data () {
    return {
      innerText: this.value,
      canEdit: false,
      isLocked: false
    }
  },
  watch: {
    value () {
      if (!this.isLocked || !this.innerText) {
        this.innerText = this.value
      }
    },
    innerText () {
      const h = this.$refs.myMediaText.getBoundingClientRect().height
      this.$emit('update-height', h)
    }
  },
  methods: {
    ...mapMutations('media', ['updateElement']),
    handleBlur (event) {
      this.isLocked = false
      this.canEdit = false
      const h = event.target.getBoundingClientRect().height
      this.$emit('update-height', h)
    },
    handleFocus (event) {
      this.isLocked = true
      const h = event.target.getBoundingClientRect().height
      this.$emit('update-height', h)
    },
    handleChange (event) {
      // 阻止冒泡删除
      if (event.keyCode === 46) {
        event.stopPropagation()
      }
      let _text = event.target.innerText
      if (_text.length > 180) {
        event.target.innerText = _text.slice(0, 180)
        this.cursorMoveEnd(event.target)
      }
      this.$emit('input', event.target.innerHTML)
      const h = event.target.getBoundingClientRect().height
      this.$emit('update-height', h)
    },
    handlePaste (event) {
      let _text = event.target.innerText
      if (_text.length > 180) {
        event.target.innerText = _text.slice(0, 180)
        this.cursorMoveEnd(event.target)
      }
      this.$emit('input', utils.getPlainText(event))
    },
    preventEnter (event) {
      if (!this.supportReturn) {
        event.preventDefault()
      }
    },
    cursorMoveEnd (obj) {
      if (window.getSelection) {
        let range = window.getSelection()
        range.selectAllChildren(obj)
        range.collapseToEnd()
      } else if (document.selection) {
        let range = document.selection.createRange()
        range.moveToElementText(obj)
        range.collapse(false)
        range.select();
      }
    },
    getText (text) {
      this.$emit('get-text', text)
    }
  }
}
</script>

<style lang="less" scoped>
  .rich-text {
    padding: 4px 8px;
    line-height: 1.3;
    outline: none;
    word-break: break-all;
    -webkit-box-sizing: border-box;
    box-sizing: border-box;
  }
</style>
