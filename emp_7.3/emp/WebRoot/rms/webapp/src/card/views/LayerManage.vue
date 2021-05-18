<template>
  <div class="layer-manage" v-if="layersList.length > 0">
    <div class="layer-title">{{ $t('H5.layer_admin') }}</div>
    <ul class="layer-admin">
      <li v-for="(layer, index) in layersList"
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
            @click="operate(index, 'hide')">{{ $t('H5.layer_hidden') }}</span>
        <span class="btnClick lockBtn" :class="{active: layersList[index].locked}"
              @click="operate(index, 'lock')">{{ $t('H5.layer_locked') }}</span>
        <div class="layer ellipsis" :class="{ active: element === layer}" v-text="layer.tag"></div>
        <div class="rightList" v-show="(cIndex === index) && dialog">
          <ul>
            <li></li>
            <li @click="element.type !== 'qrcode' && copyLayer(element)">{{ $t('H5.layer_copy') }}</li>
            <li @click.stop="moveAct(index, 'top')">{{ $t('H5.layer_to_top') }}</li>
            <li @click.stop="moveAct(index, 'up')">{{ $t('H5.layer_move_up') }}</li>
            <li @click.stop="moveAct(index, 'down')">{{ $t('H5.layer_move_down') }}</li>
            <li @click.stop="moveAct(index, 'bottom')">{{ $t('H5.layer_to_bottom') }}</li>
            <li @click.stop="removeLayer(element)">{{ $t('H5.layer_dele') }}</li>
          </ul>
        </div>
      </li>
    </ul>
  </div>
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
    ...mapGetters(['element', 'elements']),
    layersList () {
      let layersArr = [...this.elements]
      return layersArr.reverse()
    }
  },
  methods: {
    ...mapMutations(['selectElement', 'moveUp', 'moveDown', 'moveTop', 'moveBottom',
      'layerHide', 'layerLocked', 'removeLayer', 'copyLayer']),
    commonAct (index) {
      let _index = this.layersList.length - index - 1
      this.dialog = false
      return _index
    },
    moveAct (index, direction) {
      let _index = this.commonAct(index)
      let pageIndex = this.elements.length - 1
      if (_index !== pageIndex) {
        if (direction === 'up') {
          this.moveUp(_index)
        }
        if (direction === 'top') {
          this.moveTop(_index)
        }
      }
      if (_index !== 0) {
        if (direction === 'down') {
          this.moveDown(_index)
        }
        if (direction === 'bottom') {
          this.moveBottom(_index)
        }
      }
    },
    operate (index, mode) {
      let _index = this.commonAct(index)
      if (mode === 'hide') {
        this.layerHide(_index)
      }
      if (mode === 'lock') {
        this.layerLocked(_index)
      }
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
        newLayersList.forEach((item, i) => {
          item.z = i
        })
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

  .layer-manage {
    position: absolute;
    top: 0;
    border-right: 1px solid #d6d6d6;
    background-color: #fafafa;
    height: 100%;

    .layer-title {
      width: 100%;
      height: 42px;
      line-height: 42px;
      font-size: 16px;
      color: #000;
      text-align: center;
      background-color: #fff;
      border-bottom: 1px solid #d6d6d6;
    }

  }
</style>
