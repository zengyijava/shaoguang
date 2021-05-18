<template>
  <div class="template-li template-border-li">
    <!-- template-preview -->
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
      <div class="template-tools">
        <!-- 预览 -->
        <el-tooltip
          v-if="isShowAuthBtn('preview')"
          effect="dark"
          content="预览"
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
          content="复制"
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
          content="导出"
          placement="right">
          <div
            class="import-icon tools-li"
            @click="importCheckFile(templateData.fileUrl)">
          </div>
        </el-tooltip>
        <!-- 更多信息 -->
        <el-popover
          v-if="isShowAuthBtn('detail')"
          placement="right"
          width="218"
          trigger="hover">
          <div class="more-info">
            <h5 class="more-title">
              富信主题：{{ templateData.detailInfo.tmName }}
            </h5>
            <p>ID：{{ templateData.detailInfo.sptemplid }}</p>
            <p v-html="transSize(templateData.detailInfo.degreeSize)"></p>
            <p>使用次数：{{ templateData.detailInfo.usecounts }}</p>
            <p>档位：{{ templateData.detailInfo.degree }}</p>
            <p>模板类型：{{ modelTypeText }}</p>
            <p>创建人：{{ templateData.detailInfo.createUser }}</p>
            <p>所属机构：{{ templateData.detailInfo.depName }}</p>
            <p v-if="templateData.detailInfo.corpCode">企业编码：{{ templateData.detailInfo.corpCode }}</p>
            <p>创建时间：{{ new Date(templateData.detailInfo.addTime)|formatDate }}</p>
            <p>运营商状态：{{ operatorStateText }}</p>
          </div>
          <div class="more-icon tools-li" slot="reference"></div>
        </el-popover>
        <!-- 删除 -->
        <el-tooltip
          v-if="isShowAuthBtn('del')"
          effect="dark"
          content="删除"
          placement="right">
          <div
            @click="deleTipsVisible = true"
            class="dele-icon tools-li">
          </div>
        </el-tooltip>
      </div>
    </div>
    <!-- template-info -->
    <div class="template-info">
      <p class="title">{{ templateData.tmName }}</p>
      <p class="desc">ID：{{ templateData.sptemplid }}</p>
    </div>
    <!-- template-edit -->
    <div class="template-edit clearfix">
      <p
        v-if="templateData.auditStatus == 3"
        class="status-btn warn">
        审核中
      </p>
      <p
        v-if="templateData.auditStatus == 4"
        class="status-btn forbidden">
        禁用
      </p>
      <p
        v-if="templateData.auditStatus == 2"
        class="status-btn danger">
        审核不通过
      </p>
      <el-button
        v-if="templateData.auditStatus == -1 && isShowAuthBtn('update')"
        size="mini"
        type="primary"
        @click="linkToEditor('update')">
        编辑
      </el-button>
      <el-button
        v-if="templateData.auditStatus == 1 && pageType === 'my' && isShowAuthBtn('gResource')"
        size="mini"
        type="primary"
        @click="getTemplateResource">
        获取资源
      </el-button>
      <el-button
        v-if="(templateData.auditStatus == 1 && pageType === 'my' && isShowAuthBtn('gHis')) || (templateData.auditStatus == 4 && pageType === 'my' && isShowAuthBtn('gHis'))"
        size="mini"
        type="primary"
        @click="$router.push({path: 'access-list', query: {id: templateData.tmId}})">
        获取记录
      </el-button>
      <el-button
        v-if="templateData.auditStatus == 1 && pageType === 'common' && isShowAuthBtn('quote')"
        size="mini"
        type="primary"
        @click="linkToEditor('add', 'common')">
        引用
      </el-button>
      <el-button
        v-if="templateData.auditStatus == 4 && pageType === 'common' && isShowAuthBtn('quote')"
        size="mini"
        type="info">
        引用
      </el-button>
    </div>
    <!-- previewDialog -->
    <el-dialog
      v-if="templatePreviewDialogVisible"
      class="template-preview-dialog"
      top="10vh"
      :visible.sync="templatePreviewDialogVisible">
      <p slot="title">效果预览</p>
      <preview
        :previewModule="previewModule"
        :previewData="templatePreviewData">
      </preview>
    </el-dialog>
    <!-- deleDialog -->
    <el-dialog
      class="dele-dialog"
      top="30vh"
      title="删除提醒"
      :visible.sync="deleTipsVisible">
      <i class="tips-icon el-icon-warning"></i>
      <span>确定删除吗？删除后此模板对应的URL或短信标记都将清除，删除后不可撤回</span>
      <span slot="footer" class="dialog-footer clearfix">
        <div
          class="btn-false"
          type="primary"
          @click="deleTipsVisible = false">
          否
        </div>
        <div class="btn-true" @click="deleCurrentTemplate">是</div>
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
import filters from '../../libs/filters'
import config from '../../libs/config'
import actions from '../../libs/api'

export default {
  name: 'LiTemplate',
  components: {
    'scene-preview': ScenePreview,
    'media-preview': MediaPreview,
    'rich-text-preview': RichTextPreview,
    'h5-preview-page': H5PreviewPage,
    'preview': Preview
  },
  props: {
    templateData: Object,
    pageType: String
  },
  data () {
    return {
      templatePreviewDialogVisible: false,
      deleTipsVisible: false,
      previewModule: {
        title: false,
        hint: false
      },
      templatePreviewData: []
    }
  },
  computed: {
    ...mapGetters(['userInfo']),

    // 用户权限数据
    roleArr () {
      return this.userInfo.data.loginCorp.roleArr
    },

    // 模板类型描述
    modelTypeText: function () {
      let _modelTypeText
      switch (this.templateData.detailInfo.dsFlag) {
        case 1:
          _modelTypeText = '通用动态模板'
          break
        case 0:
          _modelTypeText = '通用静态模板'
          break
      }
      return _modelTypeText
    },

    // 运营商状态描述
    operatorStateText: function () {
      let _operatorStateText
      switch (this.templateData.detailInfo.auditStatus) {
        case -1:
          _operatorStateText = '草稿'
          break
        case 1:
          _operatorStateText = '审核通过'
          break
        case 2:
          _operatorStateText = '审核未通过'
          break
        case 3:
          _operatorStateText = '审核中'
          break
        case 4:
          _operatorStateText = '禁用'
          break
      }
      return _operatorStateText
    }
  },
  methods: {
    // 获取资源
    getTemplateResource () {
      if (this.templateData.detailInfo.dsFlag === 1) {
        this.$alert('您编辑的富信模板中包含了参数，个性化资源只支持调用接口获取 ，您可通过调取 “生成RX富信（URL模式）”接口来获取资源', '选择需要获取的资源类型', {
          confirmButtonText: '关闭',
          callback: action => {
          }
        })
      } else {
        this.$router.push({path: 'access', query: {id: this.templateData.tmId}})
      }
    },

    // 导出前校验
    importCheckFile (url) {
      let _importParams = {
        fileUrl: url
      }
      let _self = this
      actions.exportCheckFile(_importParams, response => {
        window.location.href = config.LIST_FILE_IMPORT_URL + url
      }, errMsg => {
        _self.$message.error(errMsg)
      })
    },

    // 企业秀预览数据
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

    // 显示模板预览
    showTemplatePreview () {
      this.templatePreviewDialogVisible = true
      let _templatePreviewData = {...this.templateData}
      _templatePreviewData.type = this.templateData.tmpType
      this.templatePreviewData = [_templatePreviewData]
    },

    /*
    ** @fn transSize容量转换为kb
    ** @params {string} 请求地址url
    */
    transSize: function (size) {
      return '容量：' + Number(size) / 100 + 'kb'
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

    // 跳转到对应编辑器
    linkToEditor: function (editType, sceneType = '') {
      let _pageType
      this.pageType === 'my' ? _pageType = 0 : _pageType = 1

      if (this.pageType === 'my' || this.pageType === 'rcos') {
        _pageType = 0
      } else {
        _pageType = 1
      }
      let _isPublic = sceneType ? 0 : _pageType

      let _attrParams = 'id=' + this.templateData.tmId + '&action=' + editType + '&isPublic=' + _isPublic + ''
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
  height: 50px;
  padding-left: 14px;
  padding-right: 14px;
  border-bottom: 1px solid @border-light;
  overflow: hidden;
  font-size: 12px;
  color: @grey-dark;
  .title,
  .desc{
    margin: 0;
    line-height: 1;
    word-break: keep-all;
    text-overflow: ellipsis;
    overflow: hidden;
  }
  .title{
    margin-top: 10px;
    margin-bottom: 4px;
    line-height: 16px;
  }
}
.template-edit{
  padding: 7px 12px;
  .status-btn{
    padding: 7px 14px;
    margin: 0 10px 0 0;
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
    border: solid 1px @blue-border;
    background-color: @green;
    border-radius: 4px;
    line-height: 30px;
    text-align: center;
    font-size: 12px;
    color: #fff;
    float: right;
    margin-left: 14px;
    cursor: pointer;
    &:hover{
      border: solid 1px @green-hover;
      background-color: @green-hover;
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
      color: @green;
      border-color: @blue-border;
      background-color: @blue-hover;
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
}
</style>
