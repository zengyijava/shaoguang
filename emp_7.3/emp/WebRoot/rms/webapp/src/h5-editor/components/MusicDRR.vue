<template>
  <vue-drr
    :x="280"
    :y="8"
    :z="1000"
    :w="32"
    :h="32"
    :active="element.active && !element.locked"
    :draggable="false"
    :resizable="false"
    :rotatable="false"
    v-show="element.hasOwnProperty('visible') ? element.visible : true"
    @activated="handleActivate"
    @deactivated="handleDeactivate">
    <i class="music-icon"></i>
  </vue-drr>
</template>

<script>
import {mapGetters, mapMutations} from 'vuex'
import VueDrr from '../../libs/components/vue-drr'

export default {
  name: 'MusicDRR',
  components: {VueDrr},
  computed: mapGetters(['elements']),
  props: {
    element: {
      type: Object,
      default: () => {
      }
    },
    musicIcon: {
      type: Object,
      default: () => {
      }
    }
  },
  methods: {
    ...mapMutations(['selectElement', 'addHistory']),
    // 处理选择
    handleActivate () {
      this.selectElement(this.element)
    },
    handleDeactivate () {
      this.element.active = false
      this.$emit('deactivated')
    }
  }
}
</script>
