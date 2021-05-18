<template>
  <!-- nav-bar -->
  <div class="nav-bar">
    <div class="logo">
      <img src="../../libs/assets/img/logo_icon.png">
      <p class="title">{{ $t('richText.richTextEdit') }}</p>
    </div>
    <div class="button-group">
      <el-button class="preview-btn" type="primary" size="small" @click="preview">
        {{ $t('richText.commit') }}
      </el-button>
      <el-button type="primary" size="small" :loading="loading && submitType === 0" @click="save(0)">
        {{ $t('media.keep') }}
      </el-button>
      <el-button type="info" size="small" @click="closeConfirm">{{ $t('richText.close') }}</el-button>
    </div>
    <!-- preview-dialog -->
    <el-dialog :visible.sync="previewVisible" :append-to-body="true" class="preview-dialog" top="10vh"
      :width="showSupplementEditor? '1160px' : '860px'">
      <p slot="title">{{ $t('richText.richTextLook') }}</p>
      <Preview :previewData="previewContent" :mainTmpType="13" :preview-module="{hint: true, title: true}"></Preview>
      <PreviewForm :card="editor" element-loading-target=".preview-form" v-loading="loading && submitType === -1">
        <el-form-item>
          <el-button type="primary" size="small" :loading="loading && submitType === 1" @click="save(1)">
            {{ $t('richText.commitExamine') }}
          </el-button>
          <el-button size="small" @click="previewVisible = false">{{ $t('richText.back') }}</el-button>
        </el-form-item>
      </PreviewForm>
    </el-dialog>
  </div>
</template>

<script>
import {mapActions, mapGetters} from 'vuex'
import Preview from '../../libs/components/Preview'
import PreviewForm from '../../libs/components/PreviewForm'
import utils from '../../libs/utils'

export default {
  name: 'NavBar',
  components: {PreviewForm, Preview},
  props: {
    template: {
      type: Object,
      default: () => {}
    },
    showSupplementEditor: {
      type: Boolean,
      default: true
    }
  },
  data () {
    return {
      previewVisible: false,
      submitType: -1,
      previewContent: []
    }
  },
  computed: {
    ...mapGetters(['editor', 'params', 'loading']),
    tempId () {
      return this.template.id
    },
    editorHtml () {
      return this.template.submitHtml
    },
    editorText () {
      return this.template.editorText
    },
    degree () {
      return this.template.degree
    },
    degreeSize () {
      return this.template.degreeSize
    }
  },
  methods: {
    ...mapActions(['addTemplate']),
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
    },

    save (type) {
      // 分开加载状态
      this.submitType = type
      const reg = /<input type="button" class="j-btn.+? value="({#参数\d+#})">/g
      const TEXTLEN = this.editorHtml.replace(reg, '$1').replace(/<\/?.+?>/g, '').length
      const acitonType = utils.getUrlParameters('action', false, 'url')
      const isPublic = utils.getUrlParameters('isPublic', false, 'url')

      if (TEXTLEN < 1) {
        this.$message({
          type: 'warning',
          message: this.$t('richText.contentEmpty')
        })
        return false
      } else if (TEXTLEN > 980) {
        this.$message({
          type: 'warning',
          message: this.$t('richText.contentMaxLength')
        })
        return false
      } else {
        if (acitonType === 'add') {
          this.editor.tmid = 0
        } else if (this.tempId) {
          this.editor.tmid = this.tempId
        }
        const tempModel = {
          cardHtml: '',
          id: this.tempId,
          w: 260,
          h: 254,
          x: 0,
          y: 0
        }

        const names = this.editorHtml.match(/参数\d+/g) || []
        const param = {
          ...this.editor,
          paramArr: [...this.params].filter(param => names.find(name => param.name === name)),
          subType: type,
          tempArr: [
            {
              ...tempModel,
              tmpType: 13,
              content: {template: this.editorHtml},
              degree: this.degree,
              degreeSize: this.degreeSize
            },
            {
              ...tempModel,
              tmpType: 14,
              content: {template: this.showSupplementEditor ? this.editorText : ''},
              degree: '0',
              degreeSize: 0
            }
          ],
          isPublic
        }
        if (!param.tmName && type === 1) {
          this.$message.error(this.$t('public.pThemeName'))
          return false
        }
        if (!param.corpId && type === 1 && this.sysType === 0) {
          this.$message.error(this.$t('richText.cropEmpty'))
          return false
        }
        if (type) {
          this.previewVisible = false
          setTimeout(() => {
            window.close()
          }, 3000)
        }
        console.log(JSON.stringify(param))
        this.addTemplate(param)
      }
    },
    preview () {
      const reg = /<input type="button" class="j-btn.+? value="({#参数\d+#})">/g
      const TEXTLEN = this.editorHtml.replace(reg, '$1').replace(/<\/?.+?>/g, '').length

      if (TEXTLEN < 1) {
        this.$message({
          type: 'warning',
          message: this.$t('richText.contentEmpty')
        })
        return false
      }
      this.previewVisible = true
      const previewContentModel = {
        'id': '',
        'frontJson': '',
        'status': 1,
        'addTime': '',
        'endJson': '',
        'priority': 0,
        'fileUrl': '',
        'tmId': '',
        'cardHtml': ''
      }

      const editorContent = {
        ...previewContentModel,
        'tmpType': 13,
        'content': JSON.stringify({template: this.editorHtml})
      }

      // 权限配置如果不配补充样式则不显示补充样式预览数据
      if (this.showSupplementEditor) {
        this.previewContent = [editorContent,
          {
            ...previewContentModel,
            'tmpType': 14,
            'content': JSON.stringify({template: this.editorText})
          }
        ]
      } else {
        this.previewContent = [editorContent]
      }
    }
  }
}
</script>

<style lang="less">
  @import '../../libs/assets/less/nav-bar';
</style>
