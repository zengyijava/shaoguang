<template>
  <ul class="page-admin">
    <li v-for="(page, index) in pages"
      :key="index"
      draggable="true"
      @dragstart="handleDragStart($event, page)"
      @dragover.prevent="handleDragOver($event, page)"
      @dragenter="handleDragEnter($event, page)"
      @dragend="handleDragEnd($event, page)">
      <div class="list-left">
        <div class="page-num">{{index + 1}}</div>
        <div class="buttons" :class="{'actived': page === currentPage}">
          <el-tooltip effect="dark" :content="$t('H5.page_up')" placement="left">
            <el-button type="text" icon="el-icon-arrow-up" @click.stop="moveUp(index)"></el-button>
          </el-tooltip>
          <el-tooltip effect="dark" :content="$t('H5.page_copy')" placement="left">
            <el-button type="text" icon="el-icon-document" @click.stop="handleCopy(index)"></el-button>
          </el-tooltip>
          <el-tooltip effect="dark" :content="$t('H5.layer_dele')" placement="left">
            <el-button type="text" icon="el-icon-delete" @click.stop="handleRemovePage(index)"></el-button>
          </el-tooltip>
          <el-tooltip effect="dark" :content="$t('H5.page_down')" placement="left">
            <el-button type="text" icon="el-icon-arrow-down" @click.stop="moveDown(index)"></el-button>
          </el-tooltip>
        </div>
      </div>
      <div class="page-wrapper"
        :class="{'active': page === currentPage}"
        :style="{
            width: 132 + 'px',
            height: Math.ceil(132 / canvasWidth * canvasHeight)  + 'px'
          }"
        @click="selectPage(page)">
        <PageView
          :page="page"
          :actual-width="132"
          :playing="false">
        </PageView>
        <div class="icon-add" :class="{'actived': page === currentPage}">
          <el-button type="text" icon="el-icon-circle-plus-outline" @click.stop="handleAddPage(index + 1)"></el-button>
        </div>
      </div>
      <div class="clear-b"></div>
    </li>
  </ul>
</template>

<script>
import {mapGetters, mapMutations} from 'vuex'
import utils from '../../libs/utils'
import PageView from '../../libs/views/PagView';
import Page from '../model/Page'

export default {
  name: 'PagesView',
  components: {PageView},
  data () {
    return {
      viewState: 0,
      dragging: null
    }
  },
  computed: {
    ...mapGetters(['element', 'h5', 'music', 'pages', 'currentPage', 'crop']),
    canvasWidth () {
      return this.h5.width
    },
    canvasHeight () {
      return this.h5.height
    }
  },
  methods: {
    ...mapMutations(['copyPage', 'removePage', 'selectPage']),
    // 复制页
    handleCopy (index) {
      const p = utils.deepClone(this.pages[index])
      p.elements.forEach(element => {
        // 不复制参数
        if (element.type === 'text') {
          let str = element.text.replace(/<input(.+?)>/g, '')
          element.text = str
        }
        // 不复制tag和locked
        if (element.type === 'music') {
          if (element.tag) {
            delete element.tag
          }
          delete element.locked
          // 重设visible
          element.visible = this.music.reused
        }
      })
      this.pages.splice(index + 1, 0, p)
      this.selectPage(p)
    },
    // 新增页
    handleAddPage (index) {
      const page = new Page()
      const music = this.music
      if (music.src) {
        page.elements.push({
          visible: music.reused,
          active: false,
          type: 'music'
        })
      }
      this.pages.splice(index, 0, page)
      this.selectPage(page)
    },
    // 删除页
    handleRemovePage (index) {
      let len = this.pages.length
      if (len > 1) {
        this.pages.splice(index, 1)
        const page = this.pages[--index] || this.pages[++index]
        this.selectPage(page)
      } else {
        this.$message.warning(this.$t('H5.page_keep_one'))
      }
    },
    moveUp (index) {
      if (index === 0) {
        return false
      } else {
        let temPage = this.pages[index - 1]
        this.$set(this.pages, index - 1, this.pages[index])
        this.$set(this.pages, index, temPage)
        this.selectPage(temPage)
      }
    },
    moveDown (index) {
      let pageIndex = this.pages.length - 1
      if (index === pageIndex) {
        return false
      } else {
        let temPage = this.pages[index + 1]
        this.$set(this.pages, index + 1, this.pages[index])
        this.$set(this.pages, index, temPage)
        this.selectPage(temPage)
      }
    },
    handleDragStart (e, page) {
      this.dragging = page
    },
    handleDragOver (e) {
      e.dataTransfer.dropEffect = 'move'
    },
    handleDragEnter (e, page) {
      e.dataTransfer.effectAllowed = 'move'
      if (page !== this.dragging) {
        const newPages = this.pages
        const dragSrc = newPages.indexOf(this.dragging)
        const dragDst = newPages.indexOf(page)
        newPages.splice(dragDst, 0, ...newPages.splice(dragSrc, 1))
      }
    },
    handleDragEnd () {
      this.dragging = null
    }
  }
}
</script>

<style lang="less" scoped>
  @import "../../libs/assets/less/variables";

  .page-admin {
    background-color: #fafafa;
    position: absolute;
    top: 60px;
    bottom: 0;
    width: 100%;
    overflow-y: auto;
    overflow-x: hidden;
    padding-left: 0;
    margin: 0;
    li {
      &:hover,
      &:hover {
        .buttons {
          display: block;
        }
        .icon-add {
          display: block;
        }
        .page-wrapper {
          border-color: @blue;
        }
      }
    }
    .list-left {
      float: left;
      margin: 0 10px 0 20px;
      width: 30px;
      height: 210px;
      text-align: center;
    }
    .page-num {
      color: #666;
      margin-bottom: 30px;
    }
    .buttons {
      text-align: center;
      display: none;
      .el-button + .el-button {
        margin-left: 0 !important;
      }
      .el-button--text {
        font-size: 18px;
        color: #666 !important;
        &:hover {
          color: @blue !important;
        }
      }
    }
  }

  .icon-add {
    position: absolute;
    top: 93.5%;
    left: 40%;
    z-index: 2;
    display: none;
    width: 28px;
    height: 28px;
    border-radius: 50%;
    overflow: hidden;
    .el-button--text {
      font-size: 30px;
      background: #fafafa;
      padding: 0 !important;
      margin: -2px 0 0 -2px !important;
    }
  }

  .dragging:before {
    content: "";
    position: absolute;
    top: 0;
    right: 0;
    bottom: 0;
    left: 0;
    z-index: 10;
  }

  .page-wrapper {
    position: relative;
    border-color: #dadbdd;
    border-style: solid;
    border-width: 2px;
    float: left;
    &:hover,
    &.active {
      border-color: @blue;
    }
    &:before {
      content: "";
      position: absolute;
      top: 0;
      left: 0;
      bottom: 0;
      right: 0;
      z-index: 2;
    }
  }

  .clear-b {
    clear: both;
    height: 50px;
  }

  .actived {
    display: block !important;
  }
</style>
