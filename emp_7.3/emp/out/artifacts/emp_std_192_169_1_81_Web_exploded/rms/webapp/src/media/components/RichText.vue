<template>
  <div class="rich-text"
    spellcheck="false"
    contenteditable="true"
    v-html="innerText"
    @focus="handleFocus"
    @blur="handleBlur"
    @keydown.enter="preventEnter"
    @keyup="handleChange"
    @paste="handlePaste">
  </div>
</template>

<script>
import utils from '../../libs/utils'

export default {
  name: 'EditableText',
  data () {
    return {
      tips: this.$t('media.pEnterText'),
      innerText: this.value.replace(/\n/g, '<br>') || this.$t('media.pEnterText'),
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
  created () {
    // Subscriptions for mutation
    this.$store.subscribe(mutation => {
      if (mutation.type === 'changeInput') {
        this.$emit('input', this.$el.innerHTML)
      }
    })
  },
  mounted () {
    this.$el.focus()
  },
  watch: {
    value: function () {
      if (!this.isLocked || !this.innerText) {
        this.innerText = this.value
      }
    }
  },
  methods: {
    handleFocus (event) {
      this.isLocked = true
      if (event.target.innerHTML === this.$t('media.pEnterText')) {
        event.target.innerText = ''
      }
    },
    handleBlur (event) {
      this.isLocked = false
      let _pEnterText = this.$t('media.pEnterText')
      // 避免点击插入参数未完成时失去焦点自动插入占位信息
      let timer = setTimeout(() => {
        if (event.target.innerHTML === '') {
          event.target.innerText = _pEnterText
        }
        clearTimeout(timer)
      }, 300)
      this.$emit('blur')
    },
    handleChange (event) {
      // 阻止冒泡删除
      if (event.keyCode === 46) {
        event.stopPropagation()
      }
      this.$emit('input', event.target.innerHTML)
    },
    handlePaste (event) {
      this.$emit('input', utils.getPlainText(event))
    },
    preventEnter (event) {
      if (!this.supportReturn) {
        event.preventDefault()
      }
    }
  }
}
</script>
