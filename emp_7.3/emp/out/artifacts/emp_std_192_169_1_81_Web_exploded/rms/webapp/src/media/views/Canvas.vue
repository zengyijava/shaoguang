<template>
  <section class="canvas" v-loading="loading">
    <Header></Header>
    <article>
      <div id="scrollCont" class="content">
        <div v-for="(element, key, index) in mediaContent" :key="index" style="overflow: hidden;">
          <Media :element="element" v-if="element.type!=='image'">
            <template v-if="element.type==='text'">
              <SimpleRichText :class="{'no-action': hasTemplate}" v-model="element.text" :placeholder="placeholder"
                @input="listenTextChange" @click.native="handleSelect"
                @focus="handleFocus" @blur="hideImgText(element, key)">
              </SimpleRichText>
              <div v-if="element.image.src" class="img-editor-height del-bar"
                :style="iscomputedImageHeight(element.image)">
                <div class="editor-container" :style="imgKeyFrameStyle(element.image)">
                  <ElementDRR v-for="(item, index) in element.image.textEditable" :key="index"
                    :element="item" :resizable="false" :rotatable="false" :draggable="false" :rotating="false">
                    <p v-if="item.type === 'text'" class="rich-text" :style="item.style" v-html="item.text"></p>
                    <img v-else-if="item.type === 'image'" :src="item.src" draggable="false" />
                  </ElementDRR>
                </div>
                <div class="del-cloak">
                  <div class="cloak-bar" @click="delTextImg(key, 'textImage')">
                    <i class="el-icon-delete"></i>
                  </div>
                </div>
              </div>
              <div v-if="element.src" class="del-bar">
                <img :src="element.src" draggable="false">
                <div class="del-cloak">
                  <div class="cloak-bar" @click="delTextImg(key, 'chart')">
                    <i class="el-icon-delete"></i>
                  </div>
                </div>
              </div>
            </template>
            <div v-else-if="element.type === 'chart'">
              <img :src="element.src" draggable="false">
            </div>
            <Audios v-else-if="element.type === 'audio'" :element="element"></Audios>
            <VideoFirstFrame  v-else-if="element.type === 'video'" :videoContent="element"></VideoFirstFrame>
            <!-- <video v-else-if="element.type === 'video'" controls style="width: 256px; height: 146px; background: #1C0D0D;" :src="element.src">
              {{ $t('canvas.notSupportVideo') }}
            </video> -->
          </Media>
          <Media v-else-if="element.type === 'image' && (element.src || element.text)" :element="element">
            <div v-if="element.src" class="img-editor-height del-bar" :style="iscomputedImageHeight(element)">
              <div class="editor-container" :style="imgKeyFrameStyle(element)">
                <ElementDRR v-if="element.textEditable.length > 0" v-for="(item, index) in element.textEditable"
                  :key="index" :element="item">
                  <p v-if="item.type === 'text'" class="rich-text" :style="item.style" v-html="item.text"></p>
                  <img v-else-if="item.type === 'image'" :src="item.src" draggable="false" />
                </ElementDRR>
              </div>
              <div class="del-cloak">
                <div class="cloak-bar-left" @click="showImgText(element, key)">
                  <i class="el-icon-ali-text"></i>
                </div>
                <div class="cloak-bar-right" @click="delTextImg(key, 'image')">
                  <i class="el-icon-delete"></i>
                </div>
              </div>
            </div>
            <SimpleRichText v-if="element.isShowImgText === 'show'"
              v-model="element.text" draggable="false"
              @input="handleInput" @click.native="handleSelect"
              @focus="handleFocus" @blur="hideImgText(element, key);">
            </SimpleRichText>
          </Media>
        </div>
      </div>
    </article>
    <MediaToolBar @remove-element="removeElement"></MediaToolBar>
    <footer>
      <div class="info">
        <el-tooltip v-if="mediaTemplate.degreeSize > (1.9*1024)" class="item" effect="dark" :content="$t('canvas.tipsZip')" placement="top">
          <span :style="{color:mediaTemplate.degreeSize>(1.9*1024)?'#f56c6c':''}">{{ $t('canvas.currentCapacity') }}{{mediaTemplate.degreeSize}} kb</span>
        </el-tooltip>
        <span v-else>{{ $t('canvas.currentCapacity') }}{{mediaTemplate.degreeSize}} kb</span>
        <span>{{ $t('canvas.filePosition') }}{{mediaTemplate.degree}} {{ $t('canvas.files') }}</span>
      </div>
    </footer>
  </section>
</template>

<script>
import {mapGetters, mapMutations} from 'vuex'
import MediaToolBar from './MediaToolBar'
import Media from '../components/Media'
import SimpleRichText from '../../libs/components/SimpleRichText'
import Header from '../../libs/components/Header'
import ElementDRR from '../components/ElementDRR'
import utils from '../../libs/utils'
import Audios from '../../libs/components/property/Audios'
import VideoFirstFrame from '../../libs/components/VideoFirstFrame'

const CANVASWIDTH = 260

export default {
  name: 'Canvas',
  components: {ElementDRR, Header, SimpleRichText, Media, MediaToolBar, Audios, VideoFirstFrame},
  data () {
    return {
      route: 'template',
      cardSize: '',
      initDataLen: 0,
      loading: false,
      paramSelected: false,
      placeholder: this.$t('media.pEnterText'),
      func: undefined
    }
  },
  mounted () {
    // Subscriptions for mutation
    this.func = this.$store.subscribe(mutation => {
      switch (mutation.type) {
        case 'addParam':
          this.activate()
          this.addParam(mutation.payload.name)
          break;
        case 'media/addUsedParam':
          this.addParam(mutation.payload.name)
          break;
        case 'selectElement':
          this.initParam()
          this.$nextTick(() => {
            this.activate()
          })
          break
        default:
          break
      }
    })
    document.addEventListener('click', this.disSelect, false)
    document.addEventListener('keyup', this.handleDelete)

    // 容量大小计算
    this.computedContentSize(this.mediaContent)
  },
  destroyed () {
    this.$store.subscribe(this.func)
    document.removeEventListener('click', this.disSelect, false)
    document.removeEventListener('keyup', this.handleDelete)
  },
  computed: {
    ...mapGetters('media', ['element', 'usedParams']),
    ...mapGetters(['mediaTemplate', 'mediaContent', 'mediaText', 'degrees', 'isLoading', 'params', 'canInsert']),
    // 富媒体集成版本
    hasTemplate () {
      return this.$store.getters.element
    }
  },
  watch: {
    mediaContent: {
      handler (val) {
        // 判断是否滚动到底部
        if (val.length > this.initDataLen) {
          let timer = null
          let scrollEl = document.getElementById('scrollCont')
          timer = setTimeout(() => {
            scrollEl.scrollTop = scrollEl.scrollHeight
            clearTimeout(timer)
          }, 100)
        }
        this.initDataLen = val.length

        if (val.length > 15) {
          this.$message.error(this.$t('canvas.tipsFrame'))
          this.mediaContent.splice(15, 1)
        }
        // 容量大小计算
        this.computedContentSize(val)
      },
      deep: true
    },
    isLoading (newValue, oldValue) {
      this.loading = newValue
    },
    canInsert (newValue, oldValue) {
      if (!newValue && !this.paramSelected) {
        this.initParam()
        this.activate()
      }
    },
    mediaText (newValue, oldValue) {
      if (this.hasTemplate) {
        this.syncUsedParam(newValue)
      }
    },
    degrees (val) {
      // 容量大小计算
      this.computedContentSize(this.mediaContent)
    }
  },
  methods: {
    ...mapMutations('media', ['addHistory', 'updateUsedParams', 'selectElement']),
    ...mapMutations(['updateParam', 'updateCanInsert']),

    // 监听文本变化
    listenTextChange () {
      if (!this.hasTemplate) {
        this.activate()
      }
      this.addHistory(this.mediaContent)
    },
    // 监听图配文变化
    handleInput () {
      this.listenTextChange()
      this.syncUsedParam(this.mediaText)
    },
    // 初始化参数
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
    // 取得焦点
    handleFocus () {
      if (this.hasTemplate) {
        return false
      }
      this.updateCanInsert(true)
    },
    // 取消选择
    handleSelect (event) {
      if (this.hasTemplate) {
        return false
      }
      const target = event.target
      if (target.classList.contains('j-btn')) {
        // 阻止选择父级文本
        event.stopPropagation()
        // 阻止点击当前参数按Del误删所有参数
        target.blur()
        if (!this.hasTemplate) {
          // 选中当前
          this.activate(target)
          const name = utils.paramValue2Name(event.target.value)
          this.updateParam(this.params.find(item => item.name === name))
          this.updateCanInsert(false)
        }
      } else {
        this.activate()
      }
      this.paramSelected = true
    },

    // 插入参数
    addParam (name) {
      const element = this.mediaContent.find(item => item.active)
      let richText = utils.removePlaceholder(this, '.media.active .rich-text')
      // 设置文本焦点
      richText.focus()
      utils.insertHtml('<input type="button" class="j-btn" unselectable="on" readonly value="' + '{#' + name + '#}' + '" />')
      this.$nextTick(() => {
        utils.reRender(element, richText)
        this.addHistory(this.mediaContent)
      })
    },
    // 取消选择
    disSelect (event) {
      if (event.target.classList.contains('rich-text') || event.target.parentNode.classList.contains('rich-text')) {
        this.updateCanInsert(true)
      } else {
        this.updateCanInsert(false)
      }
      this.paramSelected = false
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
    },
    // 移除元素
    removeElement (index) {
      if (index !== -1) {
        this.$confirm(this.$t('canvas.trueDeleFrame'), this.$t('deleHintText.titleText'), {
          confirmButtonText: this.$t('richText.true'),
          cancelButtonText: this.$t('media.cancel'),
          type: 'warning'
        }).then(() => {
          this.mediaContent.splice(index, 1)
        }).catch(() => {
        })
      }
    },
    // 同步使用过的参数
    syncUsedParam (text) {
      if (this.params.length > 0) {
        // 交集
        const intersects = utils.getIntersectParams(text, this.params)
        // 同步参数
        this.updateUsedParams(intersects)
      }
    },
    // 显示删除文配图
    delTextImg (index, type) {
      this.$confirm(this.$t('H5.img_dele_hint'), this.$t('deleHintText.titleText'), {
        confirmButtonText: this.$t('richText.true'),
        cancelButtonText: this.$t('media.cancel'),
        type: 'warning'
      }).then(() => {
        if (type === 'chart') {
          this.mediaContent[index].src = ''
        } else if (type === 'textImage') {
          this.clean(this.mediaContent[index].image)
        } else if (type === 'image') {
          if (this.mediaContent[index].text) {
            this.clean(this.mediaContent[index])
          } else {
            this.mediaContent.splice(index, 1)
            this.element.active = false
          }
        }
        this.syncUsedParam(this.mediaText)
      }).catch(() => {
      })
    },
    // 清除文配图
    clean (currentElement) {
      currentElement.src = ''
      currentElement.size = 0
      currentElement.textEditable = []
    },
    // 隐藏图片配文
    hideImgText (element, index) {
      let timer = null
      timer = setTimeout(() => {
        if (element.text === '') {
          element.isShowImgText = 'hide'
        }
        clearTimeout(timer)
      }, 200)
    },
    // 显示图片配文
    showImgText (element, index) {
      if (element.text === '') {
        element.isShowImgText = 'show'
      }
    },
    stop (event) {
      event.stopPropagation()
    },
    // 处理删除
    handleDelete (e) {
      if (e.keyCode === 46) {
        const index = this.mediaContent.findIndex(element => element.tag === this.element.tag)
        if (index !== -1 && !this.paramSelected) {
          this.removeElement(index)
        }
      }
    },
    // 计算内容大小
    computedContentSize (data) {
      let [_contentData, _textStr, _querySize] = [data, '', 0]
      for (let [i, len] = [0, _contentData.length]; i < len; i++) {
        let _type = _contentData[i].type
        switch (_type) {
          case 'text':
            if (_contentData[i].chart && _contentData[i].chart.pictureUrl) {
              _querySize += +_contentData[i].chart.pictureSize
            } else if (_contentData[i].image && _contentData[i].image.src) {
              _querySize += (_contentData[i].image.compoundSize && _contentData[i].image.compoundSize > 0) ? +_contentData[i].image.compoundSize : +_contentData[i].image.size
            }
            _textStr += _contentData[i].text
            break
          case 'image':
            _querySize += (_contentData[i].compoundSize && _contentData[i].compoundSize > 0) ? +_contentData[i].compoundSize : +_contentData[i].size
            _textStr += _contentData[i].text
            break
          case 'audio':
          case 'video':
            _querySize += +_contentData[i].size
            break
          case 'chart':
            _querySize += +_contentData[i].pictureSize
            break
        }
      }

      let _strSize = utils.computeStringSize(_textStr)
      let _totalSize = parseInt(((_strSize + _querySize) / 1024) * 100) / 100

      this.mediaTemplate.degreeSize = (_strSize + _querySize) / 1024 <= 0.01 && (_strSize + _querySize) / 1024 > 0 ? 0.01 : _totalSize
      this.mediaTemplate.degree = this.computeDegrees(_totalSize)
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
    },

    // 计算图片缩放父容器高度
    iscomputedImageHeight (data) {
      let _scale = data.scale || ''
      if (_scale || data.width > CANVASWIDTH) {
        return {
          height: Math.floor(CANVASWIDTH / data.width * data.height) + 'px'
        }
      } else {
        return {
          height: data.height + 'px'
        }
      }
    },

    // 图片容器样式计算
    imgKeyFrameStyle (data) {
      let _scale = data.scale || ''
      if (_scale || data.width > CANVASWIDTH) {
        return {
          position: 'relative',
          transformOrigin: 'left top',
          transform: _scale ? 'scale(' + _scale + ')' : 'scale(' + CANVASWIDTH / data.width + ')',
          background: 'url("' + data.src + '")0% 0% / 100% 100% no-repeat',
          height: data.height + 'px',
          width: data.width + 'px',
          overflow: 'hidden'
        }
      } else {
        return {
          background: 'url("' + data.src + '")0% 0% / 100% 100% no-repeat',
          height: data.height + 'px',
          width: data.width + 'px',
          overflow: 'hidden'
        }
      }
    }
  }
}
</script>

<style lang="less">
  @import '../assets/less/media-editor';

  .media-editor .canvas article .content .img-editor-height img {
    width: 100%;
    height: 100%;
    margin-top: 0;
  }

  .media-editor .canvas .audios-player {
    margin-left: auto;
    margin-right: auto;
  }

  .img-editor-height {
    overflow: hidden;

    .z-drr-container img {
      height: 100%;
    }
    .z-drr-container .rich-text{
      padding: 4px 8px;
    }
  }

  .del-bar {
    position: relative;

    &:hover {
      .del-cloak {
        display: block;
      }
    }

    .del-cloak {
      position: absolute;
      top: 0;
      left: 0;
      z-index: 1;
      width: 100%;
      height: 100%;
      background: rgba(0, 0, 0, 0.5);
      display: none;

      .cloak-bar,
      .cloak-bar-left,
      .cloak-bar-right {
        position: absolute;
        top: 50%;
        left: 50%;
        z-index: 2;
        width: 50px;
        height: 50px;
        margin-left: -25px;
        margin-top: -25px;
        cursor: pointer;

        i {
          margin-top: 12px;
          margin-left: 12px;
          font-size: 26px;
          color: #fff;
        }

        &:hover {
          background: rgba(255, 255, 255, 0.86);

          i {
            color: #2e95ff;
          }
        }
      }

      .cloak-bar-left {
        left: 40%;
      }

      .cloak-bar-right {
        left: 60%;
      }
    }
  }
</style>
