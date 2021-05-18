<template>
  <section class="canvas">
    <Header></Header>
    <article>
      <vue-drr v-loading="isLoading" class="dragger" :w="260" :minh="160" :h="240"
        :contain="true" :parent="true" :style="{height: (content.h  + 30)+ 'px'}"
        :active="true" :draggable="false" :rotatable="false" :handles="['s']" :can-deactive="false"
        @resizing="handleResizing" @resizestop="handleResizeStop">
        <div class="content" @mousedown.stop="showDefault=true">
          <RichTextDRR v-show="contentElement.texts.length > 0" :texts="contentElement.texts"
            @addHistory="addHistory(contentElement)" @selectParam="paramSelected=$event">
          </RichTextDRR>
          <ElementDRR v-for="element in contentElement.images" :element="element"
            :key="element.tag">
            <img :src="element.src" draggable="false"
              :style="{'border-radius': element.borderRadius + 'px'}" />
          </ElementDRR>

          <ElementDRR v-for="element in contentElement.qrcodes" :element="element"
            :key="element.tag">
            <img :src="element.src" draggable="false"
              :style="{'border-radius': element.borderRadius + 'px'}" />
          </ElementDRR>

          <ElementDRR class="audio" v-for="element in contentElement.audios" :element="element"
            :key="element.tag" :resizable="false">
            <Audios :element="element"></Audios>
          </ElementDRR>

          <ElementDRR v-for="element in contentElement.videos" :element="element"
            :key="element.tag" style="width: 240px; height: 144px;">
            <VideoFirstFrame :videoContent="element"></VideoFirstFrame>
          </ElementDRR>
          <ElementDRR class="buttons" v-for="element in contentElement.buttons" :minw="100"
            :element="element" :key="element.tag">
            <div class="btn" :style="element.style">
              <div class="text">{{element.text}}</div>
            </div>
          </ElementDRR>
        </div>
      </vue-drr>
      <div class="handler" :style="{top: content.h + 'px'}">
        <i class="el-icon-caret-bottom"></i>
        <span class="text">{{info}}</span>
        <i class="el-icon-caret-top"></i>
      </div>
    </article>
    <CanvasToolBar @click="elements.find(item => item.active).active = false"></CanvasToolBar>
    <footer>
      <div class="info">
        <el-tooltip v-if="template.degreeSize > (1.9*1024)" class="item" effect="dark" :content="$t('canvas.tipsZip')" placement="top">
          <span :style="{color:template.degreeSize>(1.9*1024)?'#f56c6c':''}">{{ $t('canvas.currentCapacity') }}{{template.degreeSize}} kb</span>
        </el-tooltip>
        <span v-else>{{ $t('canvas.currentCapacity') }}{{template.degreeSize}} kb</span>
        <span>{{ $t('canvas.filePosition') }}{{template.degree}} {{ $t('canvas.files') }}</span>
      </div>
    </footer>
  </section>
</template>

<script>
import {mapGetters, mapMutations} from 'vuex'
import utils from '../../libs/utils'
import ElementDRR from '../../libs/components/ElementDRR';
import RichTextDRR from '../../libs/components/RichTextDRR'
import CanvasToolBar from './CanvasToolBar'
import VueDrr from '../../libs/components/vue-drr'
import Header from '../../libs/components/Header'
import Audios from '../../libs/components/property/Audios'
import VideoFirstFrame from '../../libs/components/VideoFirstFrame'

export default {
  name: 'Canvas',
  components: {ElementDRR, RichTextDRR, Header, VueDrr, CanvasToolBar, Audios, VideoFirstFrame},
  data () {
    return {
      paramSelected: false,
      info: this.$t('canvas.tipsDefaultInfo'),
      showDefault: true
    }
  },
  mounted () {
    // Subscriptions for mutation
    this.func = this.$store.subscribe(mutation => {
      if (mutation.payload) {
        switch (mutation.type) {
          case 'updateContent':
            break
        }
      }
    })
    document.addEventListener('keydown', this.handleKeyDown)
  },
  destroyed () {
    this.$store.subscribe(this.func)
    document.removeEventListener('keydown', this.handleKeyDown)
  },
  computed: {
    ...mapGetters(['template', 'elements', 'content', 'msgText', 'mediaContent', 'degrees', 'isLoading', 'params']),
    contentElement () {
      return this.content.elements
    },
    cardText () {
      return this.contentElement.texts.map(item => item.text).join('')
    }
  },
  watch: {
    content: {
      handler (val) {
        this.computedContentSize(val.elements)
      },
      deep: true
    },
    degrees (val) {
      this.computedContentSize(this.content.elements)
    },
    cardText (newValue, oldValue) {
      if (this.params.length > 0) {
        // 交集
        const intersects = utils.getIntersectParams(newValue, this.params)
        // 同步参数
        this.updateParams(intersects)
      }
      // 删除时处理
      if (oldValue && oldValue.length > newValue.length) {
        // 差集
        const difference = utils.getDifferentParams(newValue, oldValue)
        // 遍历短信参数数组，对比已有参数数组，删除短信中无用参数
        utils.removeMsgParam(difference, this)

        // 遍历富媒体参数数组，对比已有参数数组，删除富媒体中无用参数
        this.mediaContent.forEach(item => {
          if (item.type === 'text' || item.type === 'image') {
            difference.forEach(value => {
              if (item.text.includes(value)) {
                const re = new RegExp('<input type="button" class="j-btn" unselectable="on" readonly="" value="' + value + '">')
                const str = item.text.replace(re, '')
                item.text = str
              }
            })
          }
        })

        // 遍历按钮，删除复制参数功能中不存在的参数
        this.contentElement.buttons.forEach(item => {
          if (!newValue.includes(item.param)) {
            item.param = ''
          }
        })
      }
    }
  },
  methods: {
    ...mapMutations(['addHistory', 'updateMessage', 'updateParams']),
    handleKeyDown (e) {
      const element = this.elements.find(item => item.active)
      if (element && element.active) {
        switch (e.keyCode) {
          // 使用delete键删除
          case 46:
            const elements = this.contentElement[element.type + 's']
            const index = elements.findIndex(item => item === element)
            if (index !== -1 && !this.paramSelected) {
              elements[index].active = false
              elements.splice(index, 1)
            }
            break
          // left key
          case 37:
            element.x--
            break
          // top key
          case 38:
            element.y--
            break
          // right key
          case 39:
            element.x++
            break
          // bottom key
          case 40:
            element.y++
            break
          default:
            break
        }
      }
    },
    handleResizing (x, y, w, h) {
      if (h <= 500) {
        this.content.h = h
        if (!this.showDefault) {
          this.info = w.toString() + ' * ' + h.toString()
        } else {
          this.showDefault = false
        }
      }
    },
    handleResizeStop () {
      this.info = this.$t('canvas.tipsDefaultInfo')
    },

    // 计算内容大小
    computedContentSize (data) {
      let [_contentData, _textStr, _querySize] = [data, '', 0]

      for (let key in _contentData) {
        switch (key) {
          case 'audios':
            for (let [i, dataLen] = [0, _contentData[key].length]; i < dataLen; i++) {
              _querySize += +_contentData[key][i].size
            }
            break
          case 'buttons':
            for (let [i, dataLen] = [0, _contentData[key].length]; i < dataLen; i++) {
              _textStr += _contentData[key][i].text
            }
            break
          case 'images':
            for (let [i, dataLen] = [0, _contentData[key].length]; i < dataLen; i++) {
              _querySize += +_contentData[key][i].size
            }
            break
          case 'qrcodes':
            for (let [i, dataLen] = [0, _contentData[key].length]; i < dataLen; i++) {
              _querySize += +_contentData[key][i].size
            }
            break
          case 'texts':
            for (let [i, dataLen] = [0, _contentData[key].length]; i < dataLen; i++) {
              _textStr += _contentData[key][i].text
            }
            break
          case 'videos':
            for (let [i, dataLen] = [0, _contentData[key].length]; i < dataLen; i++) {
              _querySize += +_contentData[key][i].size
            }
            break
        }
      }

      let _strSize = utils.computeStringSize(_textStr)
      let _totalSize = parseInt(((_strSize + _querySize) / 1024) * 100) / 100
      this.template.degreeSize = (_strSize + _querySize) / 1024 <= 0.01 && (_strSize + _querySize) / 1024 > 0 ? 0.01 : _totalSize
      this.template.degree = this.computeDegrees(_totalSize)
    },

    // 计算档位
    computeDegrees (size) {
      let _degrees = this.degrees
      for (let key in _degrees) {
        let _degreesArr = _degrees[key].split('-')
        if (size >= _degreesArr[0] && size < _degreesArr[1]) {
          return key
        }
      }
    }
  }
}
</script>

<style lang="less">
  @import '../../libs/assets/less/variables';

  .editor {
    min-height: 692px;

    .canvas {
      position: relative;
      width: 320px;
      margin: 50px auto 0 auto;
      background: url('../../libs/assets/img/phone.png') no-repeat;

      article {
        position: relative;
        width: 260px;
        height: 560px;
        margin: 0 auto;
        box-sizing: border-box;
        border-bottom: solid 20px transparent;

        .dragger {
          height: 300px;

          &.z-active {
            border: none;
          }

          > .z-handle-s {
            display: none;
          }

          > .z-handle-s:after {
            content: '';
            position: absolute;
            width: calc(100% - 2px);
            height: 27px;
            top: -24px;
            left: 1px;
            margin-left: 0;
            border: solid 1px transparent;
            border-radius: 0 0 16px 16px;
            background: #f2f4f8;
            z-index: 100;
          }
        }

        .content {
          margin-left: auto;
          margin-right: auto;
          width: 100%;
          height: 100%;
          border: solid 1px #d6d6d6;
          border-radius: 16px;
          outline: none;

          .audio {
            .el-icon-ali-sound {
              width: 32px;
              font-size: 32px;
              color: @green;
              vertical-align: middle;
            }

            .duration {
              display: inline-block;
              font-size: 12px;
              color: @grey;
              vertical-align: middle;
            }
          }

          .btn {
            display: table;

            .text {
              display: table-cell;
              height: 100%;
              vertical-align: middle;
              white-space: nowrap;
              box-sizing: border-box;
              cursor: pointer;
            }
          }

          .audio, .btn, img, video {
            width: 100%;
            height: 100%;
          }
        }

        .handler {
          position: absolute;
          width: 100%;
          top: 272px;
          margin-left: auto;
          margin-right: auto;
          text-align: center;
          z-index: 99;
          pointer-events: none;

          i {
            line-height: 28px;
            vertical-align: middle;
            font-size: 18px;
            color: #c6c8cd;
          }

          .text {
            line-height: 28px;
            font-size: 12px;
          }
        }
      }

      footer {
        width: 100%;
        text-align: right;

        .info {
          margin: 12px 0 12px auto;
          font-size: 12px;
          color: #888;

          span:first-child {
            margin-right: 10px;
          }
        }
      }
    }
  }

</style>
