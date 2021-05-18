<template>
  <ul class="layer-admin">
    <li v-if="layer.tag" v-for="(layer, index) in layersList"
      class="layerBox"
      :class="{active: layersList[index].active}"
      :draggable="dragDrop && !dialog"
      :key="index"
      @contextmenu.prevent="rightClick(index)"
      @click="selectElement(layersList[index])"
      @mouseleave="hideMenu()"
      @dragstart="handleDragStart($event, index)"
      @dragover.prevent="handleDragOver($event, index)"
      @dragenter="handleDragEnter($event, index)"
      @dragend="handleDragEnd($event, index)">
      <span class="btnClick hideBtn" :class="{active: !layersList[index].visible}"
        @click="operateMethod(index, 'hide')">{{ $t('H5.layer_hidden') }}</span>
      <span class="btnClick lockBtn" :class="{active: layersList[index].locked}"
        @click="operateMethod(index, 'locked')">{{ $t('H5.layer_locked') }}</span>
      <div class="layer ellipsis" :class="{ active: element === layer}" v-text="layer.tag"></div>
      <div class="rightList" v-show="(cIndex === index) && dialog">
        <ul>
          <li></li>
          <li @click="copyLayer(element)">{{ $t('H5.layer_copy') }}</li>
          <li @click.stop="operateMethod(index, 'toTop')">{{ $t('H5.layer_to_top') }}</li>
          <li @click.stop="operateMethod(index, 'moveUp')">{{ $t('H5.layer_move_up') }}</li>
          <li @click.stop="operateMethod(index, 'moveDown')">{{ $t('H5.layer_move_down') }}</li>
          <li @click.stop="operateMethod(index, 'toBottom')">{{ $t('H5.layer_to_bottom') }}</li>
          <li @click.stop="deleteLayer(index)">{{ $t('H5.layer_dele') }}</li>
        </ul>
      </div>
    </li>
  </ul>
</template>

<script>
import {mapGetters, mapMutations} from 'vuex'

export default {
  name: 'LayerView',
  data () {
    return {
      dragDrop: true,
      dialog: false,
      cIndex: -1,
      dragging: 0
    }
  },
  mounted () {
    document.addEventListener('click', () => {
      this.dialog = false
    })
  },
  computed: {
    ...mapGetters(['element', 'elements', 'pages']),
    layersList () {
      return [...this.elements].reverse()
    }
  },
  methods: {
    ...mapMutations(['selectElement', 'copyLayer', 'removeLayer', 'sortEleByZ']),
    operateMethod (index, method) {
      const tmpElements = this.layersList
      let lastIndex = tmpElements.length - 1
      this.dialog = false
      if (index !== 0) {
        if (method === 'moveUp') {
          tmpElements.splice(index - 1, 0, ...tmpElements.splice(index, 1))
          this.sortEleByZ(tmpElements.reverse())
        }
        if (method === 'toTop') {
          tmpElements.splice(0, 0, ...tmpElements.splice(index, 1))
          this.sortEleByZ(tmpElements.reverse())
        }
      }
      if (index !== lastIndex) {
        if (method === 'moveDown') {
          tmpElements.splice(index + 1, 0, ...tmpElements.splice(index, 1))
          this.sortEleByZ(tmpElements.reverse())
        }
        if (method === 'toBottom') {
          tmpElements.splice(lastIndex, 0, ...tmpElements.splice(index, 1))
          this.sortEleByZ(tmpElements.reverse())
        }
      }
      if (method === 'hide') {
        let visible = tmpElements[index].visible
        tmpElements[index].visible = !visible
      }
      if (method === 'locked') {
        let locked = tmpElements[index].locked
        tmpElements[index].locked = !locked
      }
    },
    deleteLayer (index) {
      let _index = this.layersList.length - index - 1
      this.dialog = false
      this.removeLayer(_index)
    },
    rightClick (index) {
      this.cIndex = index
      this.dialog = !this.dialog
      this.selectElement(this.layersList[index])
    },
    hideMenu () {
      this.dialog = false
    },
    handleDragStart (e, index) {
      this.dragging = index
    },
    handleDragOver (e) {
      e.dataTransfer.dropEffect = 'move'
    },
    handleDragEnter (e, index) {
      e.dataTransfer.effectAllowed = 'move'
      if (index !== this.dragging) {
        const newEle = this.layersList
        newEle.splice(index, 0, ...newEle.splice(this.dragging, 1))
        const newLayersList = newEle.reverse()
        this.sortEleByZ(newLayersList)
      }
    },
    handleDragEnd () {
      this.dragging = 0
    }
  }
}
</script>

<style lang="less" scoped>
  @import "../../libs/assets/less/layer-list";

</style>
