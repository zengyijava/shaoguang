<template>
  <div class="rich-text"
    spellcheck="false"
    :contenteditable="canEdit"
    v-html="innerText"
    @click="handleClick"
    @focus="handleFocus"
    @blur="handleBlur"
    @keydown="updateHeight"
    @keydown.delete="$event.stopPropagation()"
    @keyup="emit($event.target.innerHTML)"
    @paste="handlePaste">
  </div>
</template>

<script>
import utils from '../../libs/utils'

export default {
  name: 'EditableText',
  props: {
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
      canEdit: false,
      innerText: this.value.replace(/\n/g, '<br>') || this.$t('richText.dbClick'),
      isLocked: false
    }
  },
  created () {
    // Subscriptions for mutation
    this.$store.subscribe(mutation => {
      if (mutation.type === 'changeInput') {
        this.emit(this.$el.innerHTML)
      }
    })
  },
  watch: {
    value: function () {
      if (!this.isLocked || !this.innerText) {
        this.innerText = this.value
      }
    }
  },
  methods: {
    // 派发事件
    emit (innerText) {
      this.$emit('input', innerText)
      this.updateHeight()
    },
    // 同步更新高度
    updateHeight () {
      const h = Math.round(this.$el.getBoundingClientRect().height)
      this.$emit('update-height', h)
    },
    handleBlur (event) {
      this.isLocked = false
      // 避免点击插入参数未完成时失去焦点自动插入占位信息
      let timer = setTimeout(() => {
        if (event.target.innerHTML === '') {
          event.target.innerText = this.$t('richText.dbClick')
        }
        clearTimeout(timer)
      }, 300)
    },
    handleFocus (event) {
      this.isLocked = true
      if (event.target.innerText === this.$t('richText.dbClick')) {
        event.target.innerText = ''
      }
    },
    handleClick () {
      this.canEdit = true
      this.$el.focus()
    },
    handlePaste (event) {
      this.emit(utils.getPlainText(event))
    }
  }
}
</script>
