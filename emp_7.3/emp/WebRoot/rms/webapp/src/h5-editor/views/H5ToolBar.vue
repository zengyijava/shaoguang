<template>
  <ToolBar :histories="currentPage.histories" @update-history="updateElements">
    <template slot>
      <el-tooltip class="item" effect="dark" :content="$t('canvas.clearFirm')" placement="right">
        <el-button type="text" icon="el-icon-ali-clean"
          :disabled="elements.length === 0" @click="handleClick">
        </el-button>
      </el-tooltip>
    </template>
  </ToolBar>
</template>

<script>
import {mapGetters, mapMutations} from 'vuex'
import utils from '../../libs/utils'
import ToolBar from '../../libs/components/ToolBar'

export default {
  name: 'CanvasToolBar',
  components: {ToolBar},
  computed: mapGetters(['currentPage', 'element', 'elements']),
  methods: {
    ...mapMutations(['updateElements']),
    // 清空模板
    removeAllElements () {
      this.currentPage.elements = []
      this.element.active = false
    },
    handleClick () {
      utils.confirm(this, '确定要将内容区清空吗？').then(() => {
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
