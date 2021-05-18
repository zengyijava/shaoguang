<template>
  <ToolBar :histories="histories" @update-history="updateContent">
    <template slot>
      <el-tooltip class="item" effect="dark" :content="$t('canvas.clearFirm')" placement="right">
        <el-button type="text" icon="el-icon-ali-clean" :disabled="elements.length === 0" @click="handleClick">
        </el-button>
      </el-tooltip>
    </template>
  </ToolBar>
</template>

<script>
import { mapGetters, mapMutations } from 'vuex'
import utils from '../../libs/utils'
import VueDrr from '../../libs/components/vue-drr'
import ToolBar from '../../libs/components/ToolBar'

export default {
  name: 'CanvasToolBar',
  components: { ToolBar, VueDrr },
  data () {
    return {
      undone: []
    }
  },
  computed: {
    ...mapGetters(['content', 'element', 'elements', 'histories']),
    canRedo () {
      return this.undone.length
    },
    canUndo () {
      return this.histories.length
    }
  },
  methods: {
    ...mapMutations(['updateContent']),
    // 清空模板
    removeAllElements () {
      const elements = this.content.elements
      for (let key in elements) {
        elements[key] = []
      }
      this.element.active = false
    },
    handleClick () {
      utils.confirm(this, this.$t('canvas.trueClear')).then(() => {
        this.removeAllElements()
      }).catch(() => {
      })
    },
    stop (event) {
      event.stopPropagation()
    }
  }
}
</script>
