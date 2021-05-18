<template>
  <div class="scene-li scene-border-li">
    <!-- li-preview -->
    <div class="template-preview">
      <!-- 场景预览 -->
      <scene-preview
        v-if="templateData.tmpType == 12"
        :previewData="templateData.content"
        :previewContW="234">
      </scene-preview>
      <!-- 富媒体预览 -->
      <media-preview
        v-else-if="templateData.tmpType == 11"
        :previewData="templateData.content"
        :previewFrom="'list'"
        :previewContW="234">
      </media-preview>
      <!-- 富文本预览 -->
      <rich-text-preview
        v-else-if="templateData.tmpType == 13"
        :previewData="templateData.content">
      </rich-text-preview>
      <!-- h5预览 -->
      <h5-preview-page
        v-else-if="templateData.tmpType == 15"
        :previewData="h5PreviewData(templateData.content)"
        :previewContW="234"
        :previewFrom="'list'">
      </h5-preview-page>
    </div>
    <!-- hover&moreinfo -->
    <div class="template-hover">
      <!-- h5二维码只在审核通过后才会有 -->
      <div
        v-if="templateData.tmpType == 15 && templateData.auditStatus == 1 && isShowAuthBtn('link')"
        class="scene-template-qrcode">
        <qrcode-vue :value="templateData.h5Url" :size="90" level="L"></qrcode-vue>
      </div>
      <!-- 只有我的场景下审批通过且模板为启用状态下方可设置和取消快捷场景功能 -->
      <!-- 设置快捷场景 -->
      <el-button
        v-if="(pageType === 'my' || pageType === 'rcos') && templateData.auditStatus == 1 && templateData.tmState == 1 && templateData.isShortTmp == 0 && isShowAuthBtn('shortcut')"
        @click="templateShortCutSet(1)"
        type="primary"
        size="small">
        {{ $t('sceneListTemplateLi.setShortcutText') }}
      </el-button>
      <!-- 取消快捷场景 -->
      <el-button
        v-if="(pageType === 'my' || pageType === 'rcos') && templateData.auditStatus == 1 && templateData.tmState == 1 && templateData.isShortTmp == 1 && isShowAuthBtn('shortcut')"
        @click="templateShortCutSet(0)"
        type="primary"
        class="transparency-btn"
        size="small">
        {{ $t('sceneListTemplateLi.cancelShortcutText') }}
      </el-button>
      <!-- template-tools -->
      <div class="template-tools">
        <!-- 预览 -->
        <el-tooltip
          v-if="isShowAuthBtn('preview')"
          effect="dark"
          :content="$t('moduleTips.preview')"
          placement="right">
          <div
            class="preview-icon tools-li"
            @click="showTemplatePreview">
          </div>
        </el-tooltip>
        <!-- 复制模板 -->
        <!-- 复制 -->
        <el-tooltip
          v-if="isShowAuthBtn('copy')"
          effect="dark"
          :content="$t('moduleTips.copy')"
          placement="right">
          <div
            class="copy-icon tools-li"
            @click="linkToEditor('add')">
          </div>
        </el-tooltip>
        <!-- 导出功能 -->
        <el-tooltip
          v-if="isShowAuthBtn('export')"
          effect="dark"
          :content="$t('moduleTips.export')"
          placement="right">
          <div
            class="import-icon tools-li">
            <a :href="importFileUrl(templateData.fileUrl)"></a>
          </div>
        </el-tooltip>
        <!-- 分享功能 -->
        <el-tooltip
          v-if="isShowAuthBtn('share') && !isShareTemplate && templateData.auditStatus == 1"
          effect="dark"
          :content="$t('moduleTips.share')"
          placement="right">
          <div
            class="share-icon tools-li"
            @click="shareTemplate">
          </div>
        </el-tooltip>
        <!-- 更多信息 -->
        <el-popover
          v-if="isShowAuthBtn('detail')"
          placement="right"
          width="218"
          trigger="hover">
          <div class="more-info">
            <h5 class="more-title">{{ $t('sceneListTemplateLi.moreInfoText.titleText') }}：{{ templateData.detailInfo.tmName }}</h5>
            <p>
              <span class="more-desc">ID：{{ templateData.detailInfo.sptemplid }}</span>
              <span></span>
            </p>
            <p>
              <span class="more-desc" v-html="transSize(templateData.detailInfo.degreeSize)"></span>
              <span></span>
            </p>
            <p>
              <span class="more-desc">{{ $t('sceneListTemplateLi.moreInfoText.useCountText') }}：</span>
              <span>{{ templateData.detailInfo.usecounts }}</span>
            </p>
            <p>
              <span class="more-desc">{{ $t('sceneListTemplateLi.moreInfoText.frameText') }}：</span>
              <span>{{ templateData.detailInfo.degree }}</span>
            </p>
            <p>
              <span class="more-desc">{{ $t('sceneListTemplateLi.moreInfoText.modelTypeText') }}：</span>
              <span>{{ modelTypeText }}</span>
            </p>
            <p>
              <span class="more-desc">{{ $t('sceneListTemplateLi.moreInfoText.createUserText') }}：</span>
              <span>{{ templateData.detailInfo.createUser }}</span>
            </p>
            <p>
              <span class="more-desc">{{ $t('sceneListTemplateLi.moreInfoText.companyText') }}：</span>
              <span>{{ templateData.detailInfo.depName }}</span>
            </p>
            <p v-if="templateData.detailInfo.corpCode">
              <span class="more-desc">{{ $t('sceneListTemplateLi.moreInfoText.companyCodeText') }}：</span>
              <span>{{ templateData.detailInfo.corpCode }}</span>
            </p>
            <p>
              <span class="more-desc">{{ $t('sceneListTemplateLi.moreInfoText.createTimeText') }}：</span>
              <span>{{ new Date(templateData.detailInfo.addTime)|formatDate }}</span>
            </p>
            <p>
              <span class="more-desc">{{ $t('sceneListTemplateLi.moreInfoText.tmpStateText') }}：</span>
              <span>{{ modelStateText }}</span>
            </p>
            <p>
              <span class="more-desc">{{ $t('sceneListTemplateLi.moreInfoText.operatorStateText') }}：</span>
              <span>{{ operatorStateText }}</span>
            </p>
          </div>
          <div class="more-icon tools-li" slot="reference"></div>
        </el-popover>
        <!-- 删除 -->
        <el-tooltip
          v-if="isShowAuthBtn('del')  && !isShareTemplate"
          effect="dark"
          :content="$t('moduleTips.del')"
          placement="right">
          <div
            @click="deleteTemplate"
            class="dele-icon tools-li">
          </div>
        </el-tooltip>
      </div>
    </div>
    <!-- template-info -->
    <div class="template-info">
      <p class="title">{{ templateData.tmName }}</p>
      <div class="info-edit clearfix">
        <p class="desc">ID:{{ templateData.sptemplid }}</p>
        <!-- 只有H5审核通过的模板才有复制链接功能 -->
        <el-button
          plain
          v-if="templateData.tmpType == 15 && templateData.auditStatus == 1  && isShowAuthBtn('link')"
          v-clipboard="templateData.h5Url"
          @success="copySuccess"
          @error="copyError">
          {{ $t('sceneListTemplateLi.copyLink') }}
        </el-button>
      </div>
    </div>
    <!-- template-edit -->
    <div class="template-edit clearfix">
      <!-- 审批中 -->
      <p
        v-if="(templateData.auditStatus == 3 && isShowAuthBtn('auth')) || (templateData.auditStatus == -1 && templateData.sptemplid > 0 && isShowAuthBtn('auth'))"
        class="status-btn warn">
        {{ $t('sceneListTemplateLi.checkinText') }}
      </p>
      <!-- 审批未通过 -->
      <p
        v-else-if="templateData.auditStatus == 2 && isShowAuthBtn('auth')"
        class="status-btn danger">
        {{ $t('sceneListTemplateLi.checkFailText') }}
      </p>
      <!-- 已禁用 -->
      <p
        v-else-if="templateData.auditStatus == 4 && isShowAuthBtn('auth')"
        class="status-btn forbidden">
        {{ $t('sceneListTemplateLi.forbiddenText') }}
      </p>
      <!-- 草稿 -->
      <el-button
        v-else-if="templateData.tmState == 2 && isShowAuthBtn('update')"
        size="mini"
        type="primary"
        @click="linkToEditor('update')">
        {{ $t('sceneListTemplateLi.editText') }}
      </el-button>
      <!-- 审批通过，启用状态，公共场景 -->
      <el-button
        v-else-if="templateData.auditStatus == 1 && pageType == 'common' && templateData.tmState == 1 && isShowAuthBtn('update')"
        @click="linkToEditor('add', 'common')"
        size="mini"
        type="primary">
        {{ $t('sceneListTemplateLi.editNowText') }}
      </el-button>
      <!-- 审批通过，禁用状态，公共场景 -->
      <el-button
        v-else-if="templateData.auditStatus == 1 && pageType == 'common' && templateData.tmState == 0 && isShowAuthBtn('update')"
        size="mini"
        type="info">
        {{ $t('sceneListTemplateLi.editNowText') }}
      </el-button>
      <!-- 审批通过，启用状态，我的场景 -->
      <el-button
        v-else-if="templateData.auditStatus == 1 && (pageType === 'my' || pageType === 'rcos') && templateData.tmState == 1 && isShowAuthBtn('send')"
        size="mini"
        @click="sendCurrentTemplate(templateData.tmId, templateData.detailInfo.usecounts)"
        type="primary">
        {{ $t('sceneListTemplateLi.sendNowText') }}
      </el-button>
      <p
        v-else-if="templateData.auditStatus == 1 && (pageType === 'my' || pageType === 'rcos') && templateData.tmState == 1 && !isShowAuthBtn('send')"
        class="status-btn success">
        {{ $t('sceneListTemplateLi.checkSuccess') }}
      </p>
      <!-- 审批通过，禁用状态，我的场景 -->
      <el-button
        v-else-if="templateData.auditStatus == 1 && (pageType === 'my' || pageType === 'rcos') && templateData.tmState == 0 && isShowAuthBtn('send')"
        size="mini"
        type="info">
        {{ $t('sceneListTemplateLi.sendNowText') }}
      </el-button>
      <p
        v-else-if="templateData.auditStatus == 1 && (pageType === 'my' || pageType === 'rcos') && templateData.tmState == 0 && !isShowAuthBtn('send')"
        class="status-btn success">
        {{ $t('sceneListTemplateLi.checkSuccess') }}
      </p>
      <el-switch
        v-if="isShowSwitch && !isShareTemplate"
        v-model="templateData.tmState"
        :active-value="switchActiveVal"
        :inactive-value="switchInActiveVal"
        active-color="#27B768"
        inactive-color="#dfdfdf"
        @change="currentTemplateSwitchChange">
      </el-switch>
    </div>
    <!-- previewDialog -->
    <el-dialog
      v-if="templatePreviewDialogVisible"
      class="template-preview-dialog"
      top="10vh"
      :visible.sync="templatePreviewDialogVisible">
      <p slot="title">{{ $t('sceneListTemplateLi.previewTitleText') }}</p>
      <preview :previewModule="previewModule" :previewData="templatePreviewData" :mainTmpType="templateData.tmpType"></preview>
    </el-dialog>
    <!-- deleDialog -->
    <el-dialog
      class="dele-dialog"
      top="30vh"
      :title="$t('deleHintText.titleText')"
      :visible.sync="deleTipsVisible">
      <i class="tips-icon el-icon-warning"></i>
      <span>{{ $t('deleHintText.hintText') }}</span>
      <span slot="footer" class="dialog-footer clearfix">
        <div class="btn-false" type="primary" @click="deleTipsVisible = false">{{ $t('deleHintText.cancelText') }}</div>
        <div class="btn-true" @click="deleCurrentTemplate">{{ $t('deleHintText.sureText') }}</div>
      </span>
    </el-dialog>
    <!-- 模板包含定时任务弹框 -->
    <el-dialog
      class="dele-dialog"
      top="30vh"
      :title="$t('deleHintText.titleText')"
      :visible.sync="scheduledTaskTipsVisible">
      <i class="tips-icon el-icon-warning"></i>
      <span>此模板 （ ID:{{templateData.sptemplid}} ）已设定定时任务，请先取消定时任务后，再行删除。点击“查看定时任务”跳转到“群发任务查看”页面。
                <el-link class="sptemColor" :underline="false"  @click="showHistoryTask(templateData.sptemplid)">查看定时任务
                </el-link>
          </span>

      <span slot="footer" class="dialog-footer clearfix">
          <div class="btn-false" @click="scheduledTaskTipsVisible = false">取消</div>
        </span>
    </el-dialog>
  </div>
</template>

<script>
import ScenePreview from '../../libs/components/property/ScenePreview'
import MediaPreview from '../../libs/components/property/MediaPreview'
import RichTextPreview from '../../libs/components/property/RichTextPreview'
import H5PreviewPage from '../../libs/components/property/H5PreviewPage'
import Preview from '../../libs/components/Preview'
import {mapGetters} from 'vuex'
import utils from '../../libs/utils'
import filters from '../../libs/filters'
import config from '../../libs/config'
import actions from '../../libs/api'
import QrcodeVue from 'qrcode.vue'

export default {
  name: 'LiTemplate',
  components: {
    'scene-preview': ScenePreview,
    'media-preview': MediaPreview,
    'rich-text-preview': RichTextPreview,
    'h5-preview-page': H5PreviewPage,
    'preview': Preview,
    'qrcode-vue': QrcodeVue
  },
  props: {
    templateData: Object,
    pageType: String
  },
  data () {
    return {
      templatePreviewDialogVisible: false,
      deleTipsVisible: false,
      switchActiveVal: 1,
      switchInActiveVal: 0,
      previewModule: {
        title: false,
        hint: false
      },
      templatePreviewData: [],
      scheduledTaskTipsVisible: false
    }
  },
  computed: {
    ...mapGetters(['userInfo']),

    // 是否为共享模板
    isShareTemplate () {
      if (this.templateData.isShare && this.templateData.isShare === 1) {
        return true
      } else {
        return false
      }
    },

    // 用户权限数据
    roleArr () {
      return this.userInfo.data.loginCorp.roleArr
    },

    // 显示启用禁用开关
    isShowSwitch: function () {
      // 草稿状态不显示启用禁用开关
      if (this.templateData.tmState === 2) {
        return false
      } else {
        // 场景列表没有复制权限则没有启用和停用权限
        if (!this.isShowAuthBtn('state')) {
          return false
        } else {
          return true
        }
      }
    },

    // 模板类型描述
    modelTypeText: function () {
      let _modelTypeText
      switch (this.templateData.detailInfo.dsFlag) {
        case 1:
          _modelTypeText = this.$t('mySceneSearchText.templateTypeSelect.options.dynamic.label')
          break
        case 0:
          _modelTypeText = this.$t('mySceneSearchText.templateTypeSelect.options.static.label')
          break
      }
      return _modelTypeText
    },

    // 模板状态描述
    modelStateText: function () {
      let _modelStateText
      switch (this.templateData.detailInfo.tmState) {
        case 0:
          _modelStateText = this.$t('mySceneSearchText.sceneStatusSelect.options.forbid.label')
          break
        case 1:
          _modelStateText = this.$t('mySceneSearchText.sceneStatusSelect.options.start.label')
          break
        case 2:
          _modelStateText = this.$t('mySceneSearchText.sceneStatusSelect.options.draft.label')
          break
      }
      return _modelStateText
    },

    // 运营商状态描述
    operatorStateText: function () {
      let _operatorStateText
      switch (this.templateData.detailInfo.auditStatus) {
        case -1:
          _operatorStateText = this.$t('mySceneSearchText.auditStatusSelect.options.no.label')
          break
        case 1:
          _operatorStateText = this.$t('mySceneSearchText.auditStatusSelect.options.pass.label')
          break
        case 2:
          _operatorStateText = this.$t('mySceneSearchText.auditStatusSelect.options.fail.label')
          break
        case 3:
          _operatorStateText = this.$t('mySceneSearchText.auditStatusSelect.options.underway.label')
          break
        case 4:
          _operatorStateText = this.$t('mySceneSearchText.auditStatusSelect.options.forbid.label')
          break
      }
      return _operatorStateText
    }
  },
  methods: {
    deleteTemplate: function () {
      if (window.parent) {
        // 如果有定时任务，显示删除提示框，否则显示不允许删除提示框
        let isScheduledTask = window.parent.document.getElementById('leftIframe').contentWindow.isScheduledTask(this.templateData.sptemplid)
        this.scheduledTaskTipsVisible = isScheduledTask === 'true'
        this.deleTipsVisible = !this.scheduledTaskTipsVisible
      }
    },
    h5PreviewData: function (data) {
      let _data = JSON.parse(data)
      return _data.pages[0]
    },

    // 根据对于权限授权显示对应按钮
    isShowAuthBtn (name) {
      let _authName = name
      // 遍历权限数据判断当前场景类型以及是否有对应权限
      for (let [i, roleArrLen] = [0, this.roleArr.length]; i < roleArrLen; i++) {
        if (this.roleArr[i].type === this.pageType) {
          if (this.roleArr[i][_authName] === '1') {
            return true
          } else {
            return false
          }
        }
      }
    },
    /*
    ** @fn templateShortCutSet模板快捷场景设置
    ** @params {Number} val快捷场景设置值1设置0取消
    */
    templateShortCutSet (val) {
      let _setShortcutParams = {
        tmId: this.templateData.tmId,
        isShortTemp: val
      }
      let _self = this
      // 调取快捷场景设置接口
      actions.setShortcut(_setShortcutParams, response => {
        _self.templateData.isShortTmp = val
        _self.$message.success(response.data.msg)
        // 同步外部使用系统的快捷菜单
        if (val === 1 && window.parent) {
          window.parent.document.getElementById('leftIframe').contentWindow.addShotCutTem(this.templateData.tmId, this.templateData.tmName)
        } else if (val === 0 && window.parent) {
          window.parent.document.getElementById('leftIframe').contentWindow.delShotCutTem(this.templateData.tmId)
        }
      }, errMsg => {
        _self.$message.error(errMsg)
      })
    },

    // 显示模板预览
    showTemplatePreview () {
      this.templatePreviewDialogVisible = true
      let _templatePreviewData = {...this.templateData}
      _templatePreviewData.type = this.templateData.tmpType
      this.templatePreviewData = [_templatePreviewData]
    },

    // 分享模板
    shareTemplate () {
      if (window.parent) {
        window.parent.document.getElementById('leftIframe').contentWindow.shareTem(this.templateData.tmId, this.templateData.tmName)
      }
    },

    /*
    ** @fn importFileUrl导出文件地址
    ** @params {string} 请求地址url
    */
    importFileUrl: function (url) {
      return config.LIST_FILE_IMPORT_URL + url
    },

    /*
    ** @fn transSize容量转换为kb
    ** @params {string} 请求地址url
    */
    transSize: function (size) {
      return this.$t('sceneListTemplateLi.moreInfoText.sizeText') + '：' + Number(size) / 100 + 'kb'
    },
    // 跳转历史查询
    showHistoryTask: function (id) {
      if (window.parent) {
        this.scheduledTaskTipsVisible = false
        let _version = window.parent.document.getElementById('leftIframe').contentWindow.turnHistoryTask(id) || ''
        if (_version) {
          window.location.href = '' + config.LIST_HISTORY_TASK_LINK + '?id=' + id + ''
        }
      }
    },
    // 删除模板
    deleCurrentTemplate: function () {
      let _deleParams = {
        tmId: this.templateData.tmId
      }
      let _self = this

      actions.deleModel(_deleParams, response => {
        _self.$emit('deleCurrentSceneLi')
        _self.$message.success(response.data.msg)
      }, errMsg => {
        _self.$message.error(errMsg)
      })
      this.deleTipsVisible = false
    },

    // 模板启用和禁用
    currentTemplateSwitchChange: function () {
      // 1为启用0为禁用
      let _setTmStateParams = {
        tmState: this.templateData.tmState,
        tmId: this.templateData.tmId
      }
      let _self = this
      actions.setModelState(_setTmStateParams, response => {
        _self.templateData.detailInfo.tmState = _self.templateData.tmState
        _self.$message.success(response.data.msg)
      }, errMsg => {
        _self.$message.error(errMsg)
        if (_self.templateData.tmState === 0) {
          _self.templateData.tmState = 1
        } else {
          _self.templateData.tmState = 0
        }
      })
    },

    // 跳转到富媒体和富文本编辑器
    linkToEditor: function (editType, sceneType = '') {
      let _lang = utils.getUrlParameters('lang', false, config.GET_URL_PARAMS.LIST) || 'zh_CN'
      let _pageType

      if (this.pageType === 'my' || this.pageType === 'rcos') {
        _pageType = 0
      } else {
        _pageType = 1
      }
      let _isPublic = sceneType ? 0 : _pageType
      let _attrParams = 'id=' + this.templateData.tmId + '&action=' + editType + '&isPublic=' + _isPublic + '&lang=' + _lang + ''
      let _locationUrl

      if (this.templateData.tmpType === 12) {
        _locationUrl = config.SCENE_EDITOR_LINK
      } else if (this.templateData.tmpType === 11) {
        _locationUrl = config.MEDIA_EDITOR_LINK
      } else if (this.templateData.tmpType === 13) {
        _locationUrl = config.TEXT_EDITOR_LINK
      } else if (this.templateData.tmpType === 15) {
        _locationUrl = config.H5_EDITOR_LINK
      }

      window.open(_locationUrl + '?' + _attrParams, '_blank')
    },

    // 立即发送
    sendCurrentTemplate: function (id, counts) {
      if (window.parent) {
        let _version = window.parent.document.getElementById('leftIframe').contentWindow.rmsSend(id, counts) || ''
        if (_version) {
          window.location.href = '' + config.LIST_SEND_NOW_LINK + '?id=' + id + ''
        }
      }
    },

    // 复制成功
    copySuccess () {
      this.$message.success(this.$t('sceneListTemplateLi.copyLinkSuccess'))
    },

    // 复制失败
    copyError () {
      this.$message.error(this.$t('sceneListTemplateLi.copyLinkFail'))
    }
  },
  filters
}
</script>

<style lang="less">
@import "../../libs/assets/less/variables";

.template-preview{
  position: relative;
  width: 100%;
  height: 230px;
  overflow: hidden;
  border-bottom: 1px solid @border-light;
  cursor: pointer;
  &:hover + .template-hover{
    display: block;
  }
  p{
    margin: 0;
  }
}
.template-hover{
  position: absolute;
  top: 0;
  left: 0;
  z-index: 2;
  width: 100%;
  height: 230px;
  display: none;
  background: rgba(0,0,0, .2);
  &:hover{
    display: block;
  }
  .el-button{
    position: absolute;
    top: 128px;
    left: 58px;
    z-index: 3;
    &.transparency-btn{
      background: rgba(0, 0, 0, .48);
      border-color: transparent;
      color: #fff;
      &:focus{
        background: rgba(0, 0, 0, .48);
        border-color: transparent;
        color: #fff;
      }
    }
  }
  .template-tools{
    position: absolute;
    top: 0;
    right: 10px;
    z-index: 3;
    .tools-li{
      position: relative;
      width: 24px;
      height: 24px;
      margin-top: 10px;
      cursor: pointer;
      background: url('../assets/img/card_edit_icon.png') no-repeat;
      a{
        width: 24px;
        height: 24px;
        display: block;
      }
      &.preview-icon{
        background-position: 0 0;
      }
      &.copy-icon{
        background-position: 0 -33px;
      }
      &.import-icon{
        background-position: 0 -66px;
      }
      &.share-icon{
        background-position: 0 -99px;
      }
      &.more-icon{
        background-position: 0 -132px;
      }
      &.dele-icon{
        background-position: 0 -165px;
      }
    }
  }
  .scene-template-qrcode{
    position: absolute;
    top: 48px;
    left: 60px;
    width: 90px;
    height: 90px;
    padding: 5px;
    background: #fff;
  }
  .scene-template-qrcode + .el-button{
    top: 160px;
  }
}
.el-popover{
  background: rgba(0, 0, 0, .8);
  color: #fff;
}
.el-popper[x-placement^=right] .popper__arrow::after,
.el-popper[x-placement^=left] .popper__arrow::after,
.popper__arrow::after{
  border-right-color: rgba(0, 0, 0, .8);
  border-left-color: rgba(0, 0, 0, .8);
}
.more-info{
  padding: 15px;
  .more-title{
    margin-top: 0;
    margin-bottom: 10px;
    font-size: 12px;
    font-weight: normal;
    word-break: break-all;
    &::after{
      content: "";
      display: block;
      width: 100%;
      height: 1px;
      margin-top: 12px;
      background: rgba(255, 255, 255, .2);
    }
  }
  p{
    margin: 0;
    font-size: 12px;
    line-height: 20px;
  }
}
.template-info{
  height: 28px;
  padding: 12px 14px;
  border-bottom: 1px solid @border-light;
  font-size: 12px;
  color: @grey-dark;
  .info-edit{
    .desc{
      float: left;
    }
    .el-button{
      float: right;
      padding: 0;
      border: 0;
      background: transparent;
      font-size: 12px;
      color: @old-green;
      &:hover{
        color: @old-green-hover;
      }
    }
  }
  .title,
  .desc{
    margin: 0;
    word-break: keep-all;
    text-overflow: ellipsis;
    overflow: hidden;
  }
  .title{
    margin-bottom: 4px;
    line-height: 16px;
  }
}
.template-edit{
  padding: 7px 12px;
  .status-btn{
    padding: 6px 5px;
    margin: 0;
    float: left;
    font-size: 12px;
    line-height: 1;
    border-radius: 2px;
    &.warn{
      color: #fca53f;
      border: 1px solid #fca53f;
    }
    &.success{
      color: @old-green;
      border: 1px solid @old-green;
    }
    &.danger{
      color: #fc7a77;
      border: 1px solid #fc7a77;
    }
    &.forbidden{
      color: #a1a1a1;
      border: 1px solid #a1a1a1;
    }
  }
  .el-switch{
    margin-top: 3px;
    float: right;
  }
  .el-button--info{
    cursor: not-allowed;
  }
}
.template-preview-dialog{
  .el-dialog{
    width: 436px;
    overflow: hidden;
    border-radius: 4px;
  }
  .el-dialog__body{
    padding-left: 0;
    padding-right: 0;
  }
  .el-dialog__header{
    padding: 17px 20px;
    border-bottom: 1px solid @border;
    background: @dialog-header-bg;
    .el-dialog__title{
      font-size: 16px;
      line-height: 1;
    }
    .el-dialog__headerbtn{
      top: 16px;
      right: 16px;
    }
    p{
      margin: 0;
      line-height: 1;
    }
  }
}
.dele-dialog{
  .el-dialog{
    width: 426px;
  }
  .btn-false{
    width: 55px;
    height: 30px;
    border: solid 1px @old-green;
    background-color: @old-green;
    border-radius: 4px;
    line-height: 30px;
    text-align: center;
    font-size: 12px;
    color: #fff;
    float: right;
    margin-left: 14px;
    cursor: pointer;
    &:hover{
      border: solid 1px @old-green-hover;
      background-color: @old-green-hover;
    }
  }
  .btn-true{
    width: 55px;
    height: 30px;
    border: solid 1px @border;
    background-color: #ffffff;
    border-radius: 4px;
    line-height: 30px;
    text-align: center;
    font-size: 12px;
    color: #666;
    float: right;
    cursor: pointer;
    &:hover{
      color: @old-green;
      border-color: #96dbb5;
      background-color: #f0f9eb;
    }
  }
  .tips-icon{
    color: #e6a23c;
    margin-right: 8px;
    font-size: 20px;
  }
  .el-dialog__body{
    padding: 20px;
  }
  .sptemColor{
    color:red;
  }
}
</style>
