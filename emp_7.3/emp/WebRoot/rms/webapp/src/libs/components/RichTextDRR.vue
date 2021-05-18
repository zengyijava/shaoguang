<template>
  <div class="rich-text-drr">
    <ElementDRR class="text-drr" v-for="element in texts"
      :element="element" :key="element.tag" :handles="['w', 'e']"
      :draggable="draggable"
      @deactivated="activate">
      <RichText v-model="element.text" :placeholder="placeholder" :ref="element.tag"
        :locked="element.locked" :style="element.style"
        @input="handleInput" @update-height="updateHeight($event, element)"
        @editing="handleEditing" @dragging="draggable = true"
        @click.stop.native="handleSelect">
      </RichText>
    </ElementDRR>
  </div>
</template>

<script>
import {mapGetters, mapMutations} from 'vuex'
import utils from '../utils'
import ElementDRR from './ElementDRR'
import RichText from './RichText'

export default {
  name: 'RichTextDRR',
  components: {RichText, ElementDRR},
  data () {
    return {
      draggable: true,
      // 阻止选择当前删除却误删所有
      paramSelected: false,
      placeholder: this.$t('richText.dbClick'),
      func: undefined
    }
  },
  mounted () {
    // Subscriptions for mutation
    this.func = this.$store.subscribe(mutation => {
      if (mutation.type === 'addParam') {
        const element = this.element
        let richText = utils.removePlaceholder(this, '.rich-text-drr .z-active .rich-text')
        // 设置文本可编辑
        richText.contentEditable = true
        // 设置文本焦点
        richText.focus()
        this.activate()
        utils.insertHtml('<input type="button" class="j-btn" unselectable="on" readonly value="' + '{#' + mutation.payload.name + '#}' + '" ondblclick="event.stopPropagation()" />')
        this.$nextTick(() => {
          utils.reRender(element, richText)
          this.$emit('addHistory')
        })
      } else if (mutation.type === 'selectElement' || mutation.type === 'selectPage') {
        this.initParam()
        this.$nextTick(() => {
          this.activate()
        })
      }
    })
    document.addEventListener('click', this.disSelect, false)
  },
  destroyed () {
    document.removeEventListener('click', this.disSelect, false)
    this.$store.subscribe(this.func)
  },
  props: {
    texts: {
      type: Array,
      default: () => []
    }
  },
  computed: mapGetters(['element', 'params', 'param', 'canInsert']),
  methods: {
    ...mapMutations(['updateParam', 'updateCanInsert']),
    // 同步更新容器高度
    updateHeight (h, element) {
      this.$nextTick(() => {
        element.h = h
      })
    },
    handleInput () {
      this.activate()
      this.$emit('addHistory')
    },
    initParam () {
      this.updateParam({
        type: 1,
        name: utils.getMaxName(this.params),
        hasLength: 1,
        lengthRestrict: 0,
        minLength: 0,
        maxLength: 32
      })
    },
    handleEditing (editing) {
      this.draggable = !editing
      if (editing) {
        this.updateCanInsert(true)
      }
    },
    handleSelect (event) {
      const target = event.target
      if (target.classList.contains('j-btn')) {
        // 阻止选择当前删除却误删所有
        target.blur()
        // 选中当前
        this.activate(target)
        const name = utils.paramValue2Name(event.target.value)
        this.updateParam(this.params.find(item => item.name === name))
        this.updateCanInsert(false)
        this.paramSelected = true
        this.$emit('selectParam', this.paramSelected)
      }
    },
    disSelect () {
      this.updateCanInsert(false)
      this.paramSelected = false
      this.$emit('selectParam', this.paramSelected)
    },
    // 设置激活状态
    activate (current = null) {
      Array.from(this.$el.querySelectorAll('.j-btn')).forEach(item => {
        if (item === current) {
          current.classList.add('active')
        } else {
          item.classList.remove('active')
        }
      })
    }
  },
  watch: {
    canInsert (newValue, oldValue) {
      if (!newValue && !this.paramSelected) {
        this.initParam()
        this.activate()
      }
    }
  }
}
</script>

<style lang="less">
  .rich-text-drr {
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
  }
</style>
