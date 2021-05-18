<template>
  <ToolBar :use-keymap="false" :histories="histories" @update-history="updateMediaContent">
    <template slot>
      <el-tooltip class="item" effect="dark" :content="$t('media.up')" placement="right">
        <el-button type="text" icon="el-icon-ali-up"
          :disabled="currentIndex < 1" @click="moveKeyFrame('up')"></el-button>
      </el-tooltip>
      <el-tooltip class="item" effect="dark" :content="$t('media.down')" placement="right">
        <el-button type="text" icon="el-icon-ali-down"
          :disabled="currentIndex >= mediaContent.length - 1"
          @click="moveKeyFrame('down')"></el-button>
      </el-tooltip>
      <el-tooltip class="item" effect="dark" :content="$t('media.del')" placement="right">
        <el-button type="text" icon="el-icon-ali-close" :disabled="deleteDisabled"
          @click="$emit('remove-element', currentIndex)">
        </el-button>
      </el-tooltip>
    </template>
  </ToolBar>
</template>

<script>
import {mapGetters, mapMutations} from 'vuex'
import VueDrr from '../../libs/components/vue-drr'
import ToolBar from '../../libs/components/ToolBar'

export default {
  name: 'MediaToolBar',
  components: {ToolBar, VueDrr},
  props: {
    isMedia: {
      type: Boolean,
      default: true
    }
  },
  data () {
    return {
      popover: false,
      cardSize: '',
      undone: []
    }
  },
  computed: {
    ...mapGetters('media', ['histories']),
    ...mapGetters(['mediaContent']),
    currentIndex () {
      return this.mediaContent.findIndex(element => element.active)
    },
    // 禁用删除按钮
    deleteDisabled () {
      return this.mediaContent.length === 0 || this.currentIndex === -1
    }
  },
  methods: {
    ...mapMutations(['updateMediaContent']),
    // 上移下移
    moveKeyFrame: function (dir) {
      const elements = this.mediaContent
      const index = elements.findIndex(item => item.active)
      if (index === -1) {
        return
      }
      let tempElement = null
      if (dir === 'up') {
        tempElement = elements[index - 1]
        this.$set(elements, index - 1, elements[index])
        this.$set(elements, index, tempElement)
      } else if (dir === 'down') {
        tempElement = elements[index + 1]
        this.$set(elements, index + 1, elements[index])
        this.$set(elements, index, tempElement)
        this.updateMediaContent(elements)
      }
    }
  }
}
</script>
