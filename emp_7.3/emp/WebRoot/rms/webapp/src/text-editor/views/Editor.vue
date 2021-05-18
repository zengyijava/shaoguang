<template>
  <div class="text-editor">
    <NavBar :template="{id, submitHtml, editorText, degreeSize, degree}"
      :show-supplement-editor="showSupplementEditor">
    </NavBar>
    <div class="editor">
      <!-- canvas -->
      <section class="canvas">
        <div class="timer">{{currentTime}}</div>
        <SimpleRichText class="editor-container" ref="richText" v-model="editorHtml"
          v-show="isEditor" :placeholder="placeholder"
          @input="listenTextChange" @click.native="handleSelect"
          @focus="updateCanInsert(true)">
        </SimpleRichText>
        <div class="editor-container plaint-text"
          v-show="!isEditor"
          v-html="editorText">
        </div>
        <ToolBar :histories="histories" @update-history="updateMessage" v-show="isEditor">
          <template slot>
            <el-tooltip class="item" effect="dark" :content="$t('canvas.clearFirm')" placement="right">
              <el-button type="text" icon="el-icon-ali-clean" :disabled="editorText.length === 0"
                @click.stop="clearContent">
              </el-button>
            </el-tooltip>
          </template>
        </ToolBar>
        <footer>
          <div class="info" v-show="isEditor">
            {{$t('canvas.currentCapacity')}}{{ degreeSize }} kb {{$t('canvas.filePosition')}}{{degree}}
            {{$t('canvas.files')}}
          </div>
          <div class="info" v-show="!isEditor">
            {{ $t('richText.textLength') }}{{editorText.length}}
          </div>
        </footer>
      </section>
      <!-- footer-bar -->
      <div v-if="showSupplementEditor" class="footer-nav">
        <el-radio-group v-model="editorType" @change="editorChange">
          <el-radio-button label="editor">{{ $t('richText.richTextEditT') }}</el-radio-button>
          <el-radio-button label="msg">{{ $t('media.supplementStyle') }}</el-radio-button>
        </el-radio-group>
      </div>
      <!-- font-setting -->
      <aside class="property" :class="{'show': isProperty}">
        <Panel :title="$t('setting.text')" :show-close="false">
          <el-form slot="body">
            <FontSize @setCommand="setExecCommand"></FontSize>
            <FontColor @setCommand="setExecCommand"></FontColor>
            <FontSettings @setCommand="setExecCommand"></FontSettings>
            <el-form-item>
              <label class="form-item-title">{{ $t('property.params.argument') }}</label>
            </el-form-item>
            <InsertParams></InsertParams>
          </el-form>
        </Panel>
      </aside>
    </div>
  </div>
</template>

<script>
import {mapGetters, mapActions, mapMutations} from 'vuex'
import * as types from '../../libs/store/mutation-type'
import utils from '../../libs/utils'
import FontSize from '../../libs/components/property/FontSize'
import FontColor from '../../libs/components/property/FontColor'
import FontSettings from '../../libs/components/property/FontSettings'
import InsertParams from '../../libs/components/InsertParams'
import Panel from '../../libs/components/Panel'
import Preview from '../../libs/components/Preview'
import PreviewForm from '../../libs/components/PreviewForm'
import ToolBar from '../../libs/components/ToolBar'
import NavBar from './NavBar'
import SimpleRichText from '../../libs/components/SimpleRichText'

export default {
  components: {
    SimpleRichText,
    NavBar,
    ToolBar,
    FontSize,
    FontColor,
    FontSettings,
    InsertParams,
    Panel,
    Preview,
    PreviewForm
  },
  computed: {
    ...mapGetters(['histories', 'params', 'canInsert', 'degrees', 'userInfo']),
    // 系统所属EMP还是RCOS，ROCS的预览保存需要选择制定企业
    sysType () {
      if (this.userInfo.hasOwnProperty('data')) {
        return Number(this.userInfo.data.type)
      } else {
        return 1
      }
    },
    showSupplementEditor () {
      // 能够明确获取到用户信息设置的富文本没有补充样式才返回false，否则都返回true
      if (this.userInfo.hasOwnProperty('data')) {
        const _modulePer = this.userInfo.data.modulePer
        const _textModulePer = _modulePer.find((item) => {
          return item.type === 13
        })
        const _textSubLen = _textModulePer.suppStyle.length
        if (_textSubLen > 0) {
          return true
        } else {
          return false
        }
      } else {
        return true
      }
    }
  },
  data () {
    return {
      editorType: 'editor',
      currentTime: '',
      richText: null,
      id: '',
      editorHtml: '',
      submitHtml: '',
      editorText: '',
      degreeSize: 0,
      degree: '0',
      template: {},
      isEditor: true,
      isProperty: true,
      paramSelected: false,
      placeholder: this.$t('media.pEnterText'),
      func: undefined
    }
  },
  created () {
    if (!this.userInfo.hasOwnProperty('data')) {
      this.getUserInfos()
    }
    this.currentTime = utils.getCurrentTime()
    this.getDegree()
    this.id = utils.getUrlParameters('id', false, 'url')
    if (this.id) {
      this.getTempDetail({
        tmId: this.id,
        previewType: 1
      })
    }
  },
  mounted () {
    this.richText = this.$refs.richText.$el
    // Subscriptions for mutation
    this.func = this.$store.subscribe(mutation => {
      if (mutation.payload) {
        switch (mutation.type) {
          case types.GET_TEMP_DETAIL_SUCCESS:
            const detail = mutation.payload.data
            detail.list.forEach(item => {
              if (item.tmpType === 13) {
                this.editorHtml = JSON.parse(item.content).template
              } else {
                this.editorText = JSON.parse(item.content).template
              }
            })
            if (this.editorHtml) {
              this.updateCanInsert(false)
              this.initParam()
            }
            break
          case 'addParam':
            let richText = this.richText
            // 设置焦点
            richText.focus()
            this.activate()
            // 动态生成button节点
            const btn = document.createElement('input')
            btn.type = 'button'
            btn.className = 'j-btn active'
            btn.setAttribute('unselectable', 'on')
            btn.setAttribute('readonly', '')
            btn.value = '{#' + mutation.payload.name + '#}'
            utils.insertNodeOverSelection(btn, richText)
            this.editorHtml = richText.innerHTML
            this.syncEditorText()
            this.addHistory(this.editorHtml)
            break
          default:
            break
        }
      }
    })
    document.addEventListener('click', this.disSelect, false)
  },
  destroyed () {
    this.$store.subscribe(this.func)
    document.removeEventListener('click', this.disSelect, false)
  },
  methods: {
    ...mapActions(['addTemplate', 'getUserInfos', 'getTempDetail', 'getDegree']),
    ...mapMutations(['removeAllElements', 'addHistory', 'updateParam', 'updateCanInsert']),
    editorChange () {
      if (this.editorType === 'editor') {
        this.isEditor = true
        this.isProperty = true
      } else {
        this.isEditor = false
        this.isProperty = false
      }
    },
    // 处理选择
    handleSelect (event) {
      const target = event.target
      if (target.classList.contains('j-btn')) {
        // 阻止选择父级文本
        event.stopPropagation()
        // 阻止点击当前参数按Del误删所有参数
        target.blur()
        // 选中当前
        this.activate(target)
        const name = utils.paramValue2Name(event.target.value)
        this.updateParam(this.params.find(item => item.name === name))
        this.updateCanInsert(false)
      } else {
        this.activate()
      }
      this.paramSelected = true
    },
    // 处理文本变化
    listenTextChange (html) {
      this.syncEditorText()
      this.activate()
      this.addHistory(html)
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
    // 取消选择
    disSelect (event) {
      if (utils.isOrContainsNode(this.richText, event.target)) {
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
    // 执行命令
    setExecCommand (data) {
      if (data.type) {
        document.execCommand(data.type, false, data.style)
      } else {
        document.execCommand(data.style)
      }
      this.submitHtml = this.richText.innerHTML
    },

    // 清空
    clearContent () {
      this.$confirm(this.$t('canvas.trueClear'), this.$t('deleHintText.titleText'), {
        confirmButtonText: this.$t('richText.true'),
        cancelButtonText: this.$t('media.cancel'),
        type: 'warning'
      }).then(() => {
        this.editorHtml = ''
      }).catch(() => {
      })
    },
    // 同步editor text
    syncEditorText () {
      const reg = /<input type="button" class="j-btn.+? value="({#参数\d+#})">/g
      this.editorText = this.editorHtml.replace(reg, '$1').replace(/<\/?.+?>/g, '')
    },
    // 撤销重做
    updateMessage (html) {
      this.editorHtml = html || this.placeholder
    }
  },
  watch: {
    editorHtml (newValue, oldValue) {
      // 提交的html去掉active样式类
      this.submitHtml = newValue.replace(/class="j-btn active"/, 'class="j-btn"')
      let _textSize = newValue ? utils.computeStringSize(newValue) / 1024 : utils.computeStringSize(this.editorText) / 1024
      this.degreeSize = (_textSize > 0 && _textSize < 0.01) ? 0.01 : Math.ceil(_textSize * 100) / 100
      this.degree = this.computeDegrees(this.degreeSize)
    },
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
  @import '../../libs/assets/less/editor-app.less';
  @import '../assets/text-editor';
</style>
