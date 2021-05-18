<template>
  <nav class="nav-bar">
    <div class="logo">
      <img src="../assets/img/logo_icon.png">
      <span class="title">{{title}}</span>
    </div>
    <slot></slot>
    <div class="button-group">
      <el-button class="preview-btn" type="primary" size="small" @click="checkBeforePreview()">
        {{ $t('richText.commit') }}
      </el-button>
      <el-button type="primary" size="small" :loading="loading && submitType === 0" @click="save(0)">
        {{ $t('media.keep') }}
      </el-button>
      <el-button type="info" size="small" @click="closeConfirm">{{ $t('richText.close') }}</el-button>
    </div>
    <el-dialog :class="[singlePreviewMarLeft ? 'single-preview': '', 'preview-dialog']" top="10vh" :width="width"
      :visible.sync="previewVisible" :append-to-body="true">
      <p slot="title">{{this.hasTemplate ? $t('richText.scene') : $t('richText.richMedia')}}</p>
      <Preview :preview-data="tempArr" :preview-module="{hint: true, title: true}"
        :mainTmpType="card.tmpType"></Preview>
      <PreviewForm :card="card" element-loading-target=".preview-form" v-loading="loading && submitType === -1">
        <el-form-item>
          <el-button type="primary" size="small" :loading="loading && submitType === 1" @click="save(1)">{{
            $t('richText.commitExamine') }}
          </el-button>
          <el-button size="small" @click="previewVisible = false">{{ $t('richText.back') }}</el-button>
        </el-form-item>
      </PreviewForm>
    </el-dialog>
  </nav>
</template>

<script>
import {mapActions, mapGetters} from 'vuex'
import utils from '../../libs/utils'
import PopList from '../components/PopList'
import Preview from '../components/Preview'
import PreviewForm from '../components/PreviewForm'

export default {
  name: 'NavBar',
  components: {PreviewForm, Preview, PopList},
  props: {
    title: {
      type: String,
      default: ''
    },
    isCard: {
      type: Boolean,
      default: false
    }
  },
  data () {
    return {
      dialogStatus: false,
      previewVisible: false,
      submitType: -1,
      mediaHasSubEditor: true,
      sceneHasMediaSubEditor: true,
      sceneHasMsgSubEditor: true
    }
  },
  computed: {
    ...mapGetters(['card', 'template', 'mediaTemplate', 'msgTemplate', 'elements', 'loading', 'degrees', 'userInfo', 'intersects']),
    // 有值则为场景，否则为富媒体
    hasTemplate () {
      return this.$store.getters.element
    },
    // 预览区宽度，由权限配置项决定是否有二级补充样式
    width () {
      if (this.hasTemplate) {
        if (this.sceneHasMediaSubEditor && this.sceneHasMsgSubEditor) {
          return '1510px'
        } else if (this.sceneHasMediaSubEditor || this.sceneHasMsgSubEditor) {
          return '1160px'
        } else {
          return '860px'
        }
      } else {
        if (this.mediaHasSubEditor) {
          return '1160px'
        } else {
          return '860px'
        }
      }
    },
    // 单个预约是左边距处理
    singlePreviewMarLeft () {
      if (!this.mediaHasSubEditor || (!this.sceneHasMediaSubEditor && !this.sceneHasMsgSubEditor)) {
        return true
      } else {
        return false
      }
    },
    // 预览数据
    tempArr () {
      let arr = [this.mediaTemplate, this.msgTemplate]
      if (this.hasTemplate) {
        arr.unshift(this.$store.getters.template)
        if (!this.sceneHasMediaSubEditor && !this.sceneHasMsgSubEditor) {
          arr = arr.filter((item) => {
            return item.tmpType === 12
          })
        } else if (!this.sceneHasMediaSubEditor && this.sceneHasMsgSubEditor) {
          arr = arr.filter((item) => {
            return item.tmpType !== 11
          })
        } else if (this.sceneHasMediaSubEditor && !this.sceneHasMsgSubEditor) {
          arr = arr.filter((item) => {
            return item.tmpType !== 14
          })
        }
      } else {
        arr = this.mediaHasSubEditor ? [this.mediaTemplate, this.msgTemplate] : [this.mediaTemplate]
      }
      return arr.map(item => {
        return {
          tmpType: item.tmpType,
          content: JSON.stringify(item.content)
        }
      })
    }
  },
  created () {
    if (!this.userInfo.hasOwnProperty('data')) {
      this.getUserInfos()
    }
  },
  watch: {
    userInfo (val) {
      const _modulePer = this.userInfo.data.modulePer
      const _mediaModulePer = _modulePer.find((item) => {
        return item.type === 11
      })
      const _sceneModulePer = _modulePer.find((item) => {
        return item.type === 12
      })
      const _sceneSub = _sceneModulePer ? _sceneModulePer.suppStyle : []
      const _mediaSub = _mediaModulePer ? _mediaModulePer.suppStyle : []
      // 判断富媒体编辑器是否有补充编辑器器
      _mediaSub.length > 0 ? this.mediaHasSubEditor = true : this.mediaHasSubEditor = false
      // 判断场景编辑器是否有富媒体补充编辑器和短信补充编辑器
      if (_sceneSub.length > 0) {
        _sceneSub.indexOf(11) > -1 ? this.sceneHasMediaSubEditor = true : this.sceneHasMediaSubEditor = false
        _sceneSub.indexOf(14) > -1 ? this.sceneHasMsgSubEditor = true : this.sceneHasMsgSubEditor = false
      } else {
        this.sceneHasMediaSubEditor = false
        this.sceneHasMsgSubEditor = false
      }
    }
  },
  methods: {
    ...mapActions(['addTemplate', 'getTempDetail', 'getUserInfos']),

    // 校验编辑器内容是否有输入
    isCheckEditorContent () {
      // 判断是场景还是富媒体
      if (this.hasTemplate) {
        if (this.elements.length < 1) {
          this.$router.push('editor')
          this.$message.warning(this.$t('property.cardHintMsg'))
          return false
        } else {
          const _modulePer = this.userInfo.data.modulePer
          const _sceneModulePer = _modulePer.find((item) => {
            return item.type === 12
          })
          const _sceneSub = _sceneModulePer ? _sceneModulePer.suppStyle : []
          // 判断场景编辑器是否有富媒体补充编辑器和短信补充编辑器
          if (_sceneSub.length > 0) {
            _sceneSub.indexOf(11) > -1 ? this.sceneHasMediaSubEditor = true : this.sceneHasMediaSubEditor = false
            _sceneSub.indexOf(14) > -1 ? this.sceneHasMsgSubEditor = true : this.sceneHasMsgSubEditor = false
          } else {
            this.sceneHasMediaSubEditor = false
            this.sceneHasMsgSubEditor = false
          }

          if (this.sceneHasMediaSubEditor && this.sceneHasMsgSubEditor) {
            if (this.mediaTemplate.degreeSize === 0) {
              this.$router.push('media')
              this.$message.warning(this.$t('property.completeHintMsgB'))
              return false
            } else if (this.msgTemplate.content.template === '') {
              this.$router.push('msg')
              this.$message.warning(this.$t('property.completeHintMsgC'))
              return false
            } else {
              return true
            }
          } else if (this.sceneHasMediaSubEditor && !this.sceneHasMsgSubEditor) {
            if (this.mediaTemplate.degreeSize === 0) {
              this.$router.push('media')
              this.$message.warning(this.$t('property.completeHintMsgA'))
              return false
            } else {
              return true
            }
          } else if (!this.sceneHasMediaSubEditor && this.sceneHasMsgSubEditor) {
            if (this.msgTemplate.content.template === '') {
              this.$router.push('msg')
              this.$message.warning(this.$t('property.completeHintMsgA'))
              return false
            } else {
              return true
            }
          } else {
            return true
          }
        }
      } else {
        if (this.mediaTemplate.degreeSize === 0) {
          this.$router.push('media')
          this.$message.warning(this.$t('property.mediaHintMsg'))
          return false
        } else if (this.mediaHasSubEditor && this.msgTemplate.content.template === '') {
          this.$router.push('msg')
          this.$message.warning(this.$t('property.completeHintMsgA'))
          return false
        } else {
          return true
        }
      }
    },

    // 校验文本输入长度
    isCheckMsgLength () {
      const msgContentLength = this.msgTemplate.content.template.length

      if (this.hasTemplate) {
        if (this.sceneHasMsgSubEditor && this.sceneHasMediaSubEditor && msgContentLength > 980) {
          this.$message.warning(this.$t('property.completeHintMsgC'))
          this.$router.push('msg')
          return false
        } else if (this.sceneHasMsgSubEditor && !this.sceneHasMediaSubEditor && msgContentLength > 980) {
          this.$message.warning(this.$t('property.completeLengthMsgA'))
          this.$router.push('msg')
          return false
        } else {
          return true
        }
      } else {
        if (this.mediaHasSubEditor && msgContentLength > 980) {
          this.$message.warning(this.$t('property.completeLengthMsgA'))
          this.$router.push('msg')
          return false
        } else {
          return true
        }
      }
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
    // 预览提交前校验
    checkBeforePreview () {
      let degrees
      if (this.hasTemplate) {
        degrees = this.computeDegrees(this.template.degreeSize)
      } else {
        degrees = this.computeDegrees(this.mediaTemplate.degreeSize)
      }
      // 校验档位
      if (!degrees) {
        this.$alert(this.$t('property.checkDegreeHint'), {
          confirmButtonText: this.$t('richText.true')
        }).then(() => {
        })
        return false
      }
      // 校验文本短信输入是否超过
      if (!this.isCheckMsgLength()) {
        return false
      }
      // 校验内容是否有输入和总档位是否超出
      if (!this.isCheckEditorContent()) {
        return false
      } else if (this.mediaTemplate.degreeSize > (1.9 * 1024)) {
        this.$alert(this.$t('canvas.tipsZip'), {
          confirmButtonText: this.$t('richText.true')
        }).then(() => {
        })
        return false
      } else {
        this.previewVisible = true
      }
    },

    // 保存
    save (type) {
      // 分开加载状态
      this.submitType = type
      const action = utils.getUrlParameters('action', false, 'url')
      const id = utils.getUrlParameters('id', false, 'url')
      const isPublic = utils.getUrlParameters('isPublic', false, 'url')

      let param = {
        ...this.card,
        subType: type
      }
      // 校验短信输入长度
      if (!this.isCheckMsgLength()) {
        return false
      }
      if (action === 'add') {
        if (type === 1) { // 预览提交
          param.tmid = this.card.tmid
        } else {
          param.tmid = 0 // 保存
        }
      } else if (id !== '') { // 修改
        param.tmid = id
      }
      param.paramArr = this.intersects
      if (isPublic !== '') {
        param.isPublic = isPublic
      }
      if (!this.isCheckEditorContent()) {
        return false
      }
      if (!param.tmName && type === 1) {
        this.$message.error(this.$t('public.pThemeName'))
        return false
      }
      if (!param.corpId && type === 1 && Number(this.userInfo.data.type) === 0) {
        this.$message.error(this.$t('richText.cropEmpty'))
        return false
      }
      // 富媒体时增加editorWidth
      if (!this.hasTemplate) {
        param.editorWidth = 260
      }

      // 删除paramText使用最新方式
      this.mediaTemplate.content.forEach(item => {
        if (item.hasOwnProperty('paramText')) {
          delete item.paramText
        }
      })

      param.tempArr = [this.mediaTemplate, this.msgTemplate]
      if (this.hasTemplate) {
        this.template.content.elements.texts.forEach(item => {
          if (item.hasOwnProperty('paramText')) {
            delete item.paramText
          }
        })
        // 设置非激活状态
        this.elements.forEach(element => {
          // 移除active属性
          delete element.active
        })
        param.tempArr.unshift(this.template)
      }

      // 保存出去的所有容量要乘以100，数据库不能存带小数点容量
      let _cloneParam = utils.deepClone(param)
      _cloneParam.tempArr.filter(item => item.tmpType !== 14).forEach(temp => {
        temp.degreeSize = Number(temp.degreeSize) * 100
      })
      console.log(JSON.stringify(_cloneParam))
      this.addTemplate(_cloneParam)
    },
    // 弹层关闭回调
    dialogStatusCB () {
      this.dialogStatus = false
    },
    // 立即使用
    immediateUse (id) {
      this.getTempDetail({
        tmId: id,
        previewType: 1
      })
      this.dialogStatus = false
    },
    back () {
      if (window.opener && !window.opener.closed) {
        window.close()
      }
    },

    closeConfirm () {
      this.$confirm(this.$t('navBar.closeEditorHint'), this.$t('deleHintText.titleText'), {
        confirmButtonText: this.$t('richText.true'),
        cancelButtonText: this.$t('media.cancel'),
        type: 'warning'
      }).then(() => {
        this.back()
      }).catch(() => {
      })
    }
  }
}
</script>

<style lang="less">
  @import '../../libs/assets/less/nav-bar';
</style>
