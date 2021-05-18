<template>
  <div class="pop-model-li">
    <div class="pop-model-preview">
      <scene-preview
        v-if="modelData.tmpType == 12"
        :previewData="modelData.content"
        :previewContW="210">
      </scene-preview>
      <media-preview
        v-else-if="modelData.tmpType == 11"
        :previewData="modelData.content"
        :previewContW="210">
      </media-preview>
      <rich-text-preview
        v-else-if="modelData.tmpType == 13"
        :previewData="modelData.content">
      </rich-text-preview>
      <h5-preview-page
        v-else-if="modelData.tmpType == 15"
        :previewData="h5PreviewData(modelData.content)"
        :previewContW="210">
      </h5-preview-page>
    </div>
    <div class="pop-model-hover">
      <el-button type="primary" size="small" @click="showPreview">{{ $t('previewText.companyText') }}</el-button>
      <el-button type="primary" size="small" @click="immediateUse(modelData.tmId)">{{ $t('property.justUse') }}</el-button>
    </div>
    <div class="pop-model-info">
      <div class="info-hd clearfix">
        <p>ID:{{ modelData.sptemplid }}</p>
        <p>{{ modelData.tmName }}</p>
      </div>
      <div class="info-ft clearfix">
        <p>{{ $t('property.tSize') }}<span>{{ Number(modelData.detailInfo
.degreeSize) / 100 }}</span>KB</p>
        <p>{{ $t('canvas.filePosition') }}<span>{{ modelData.detailInfo
.degree }}</span>{{ $t('canvas.files') }}</p>
      </div>
    </div>
    <el-dialog class="list-dialog" top="10vh" :visible.sync="dialogTableVisible" append-to-body>
      <p slot="title">{{ $t('property.effectPreview') }}</p>
      <preview :previewModule="previewModule" :previewData="previewData" :mainTmpType="modelData.tmpType">
        <el-button class="preview-use-btn" type="primary" size="small" @click="immediateUse(modelData.tmId)">{{ $t('property.justUse') }}</el-button>
      </preview>
    </el-dialog>
  </div>
</template>

<script>
import ScenePreview from './ScenePreview'
import MediaPreview from './MediaPreview'
import RichTextPreview from './RichTextPreview'
import H5PreviewPage from './H5PreviewPage'
import Preview from '../Preview'

export default {
  name: 'PopModeLi',
  components: {
    'scene-preview': ScenePreview,
    'media-preview': MediaPreview,
    'rich-text-preview': RichTextPreview,
    'h5-preview-page': H5PreviewPage,
    'preview': Preview
  },
  data () {
    return {
      previewData: [],
      previewModule: {
        title: false,
        hint: false
      },
      dialogTableVisible: false
    }
  },
  props: {
    modelData: Object
  },
  computed: {
  },
  methods: {
    h5PreviewData: function (data) {
      let _data = JSON.parse(data)
      return _data.pages[0]
    },
    // 显示预览
    showPreview: function () {
      this.dialogTableVisible = true
      let _modelDataArr = this.modelData
      _modelDataArr.type = this.modelData.tmpType
      this.previewData = [_modelDataArr]
    },
    // 立即使用
    immediateUse: function (id) {
      this.dialogTableVisible = false
      this.$emit('immediateUse', id)
    }
  }
}
</script>

<style lang="less" scope>
@import "../../assets/less/variables";
.pop-model-li{
  position: relative;
  width: 210px;
  height: 252px;
  margin-right: 30px;
  margin-bottom: 36px;
  float: left;
  border: solid 1px @border;
  box-shadow: 0px 3px 4px 0px rgba(0, 0, 0, .08);
  cursor: pointer;
  &:nth-child(3){
    margin-right: 0;
  }
  &:hover{
    .pop-model-hover{
      display: block;
    }
  }
  .pop-model-preview{
    width: 100%;
    height: 180px;
    overflow: hidden;
  }
  .pop-model-hover{
    position: absolute;
    top: 0;
    left: 0;
    z-index: 2;
    width: 100%;
    height: 170px;
    padding-top: 82px;
    display: none;
    background: rgba(0, 0, 0, 0.3);
    .el-button{
      width: 100px;
      margin-left: auto;
      margin-right: auto;
      margin-top: 10px;
      display: block;
    }
  }
  .pop-model-info{
    font-size: 12px;
    p{
      width: 93px;
      float: left;
      overflow: hidden;
      text-overflow:ellipsis;
      white-space: nowrap;
      &:nth-child(even){
        text-align: right;
      }
      span{
        color: @danger;
      }
    }
    .info-hd,
    .info-ft{
      padding: 12px;
      line-height: 1;
    }
    .info-hd{
      border-bottom: 1px solid @border;
    }
  }
}
.list-dialog{
  h1,h2,h3,h4,h5,h6,
  p{
    margin: 0;
    padding: 0;
    line-height: 1;
    font-weight: normal
  }
  .el-dialog{
    width: 436px;
    height: auto;
    border-radius: 4px;
    overflow: hidden;
  }
  .el-dialog__body{
    padding: 20px 0;
  }
  .el-dialog__header{
    padding: 17px 20px;
    line-height: 1;
    border-bottom: 1px solid @border;
    background: @dialog-header-bg;
    .el-dialog__headerbtn{
      top: 16px;
      right: 16px;
    }
    .el-dialog__title{
      font-size: 16px;
      line-height: 1;
    }
  }
}
</style>
