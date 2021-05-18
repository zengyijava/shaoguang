<template>
  <div class="rich-text"
    :class="{editable: editable}"
    spellcheck="false"
    contenteditable="false"
    v-html="innerText"
    @dblclick="handleDbClick"
    @focus="locking = true"
    @blur="handleBlur"
    @keydown="$event.stopPropagation()"
    @keyup.stop="emitInput($event.target.innerHTML)"
    @paste="handlePaste">
  </div>
</template>

<script>
import utils from '../../libs/utils'
let range = null
export default {
  name: 'RichText',
  props: {
    locked: {
      type: Boolean,
      default: false
    },
    value: {
      type: String,
      default: ''
    },
    placeholder: {
      type: String,
      default: ''
    }
  },
  data () {
    return {
      editable: false,
      innerText: this.value.replace(/\n/g, '<br>') || this.placeholder,
      locking: false,
      editing: false
    }
  },
  watch: {
    value: function (newVal) {
      if (!this.locking || !this.innerText) {
        const reg = /<span .+>(.+)<\/span>/g
        this.innerText = newVal.replace(reg, '$1')
        // 强行删除参数文本时同步
        this.$el.innerHTML = newVal.replace(reg, '$1')
      }
    }
  },
  methods: {
    // 派发事件
    emitInput (innerText) {
      this.updateHeight()
      this.$emit('input', innerText)
    },
    // 同步更新高度
    updateHeight () {
      const h = Math.ceil(this.$el.getBoundingClientRect().height)
      this.$emit('update-height', h)
    },
    handleBlur (event) {
      this.locking = false
      this.editable = false
      this.editing = false
      const richText = event.currentTarget
      richText.contentEditable = false
      if (richText.innerHTML === '') {
        richText.innerHTML = this.placeholder
      }
      this.$emit('editing', this.editing)
    },
    handleDbClick (event) {
      if (this.locked) {
        return false
      }
      this.editable = true
      this.editing = true
      const richText = event.currentTarget
      richText.focus()
      richText.contentEditable = true
      if (document.selection) {
        range = document.body.createTextRange()
        range.moveToElementText(richText)
        range.select()
      } else if (window.getSelection) {
        range = document.createRange()
        range.selectNodeContents(richText)
        window.getSelection().removeAllRanges()
        window.getSelection().addRange(range)
      }

      this.$emit('editing', this.editing)
    },
    handlePaste (event) {
      this.emitInput(utils.getPlainText(event))
    }
  }
}
</script>

<style type="less" scoped>
  .rich-text {
    min-height: 28px;
    padding: 4px 8px;
    line-height: 1.3;
    outline: none;
    word-break: break-all;
    box-sizing: border-box;
  }

  .editable {
    cursor: text;
    user-select: text;
  }
</style>
