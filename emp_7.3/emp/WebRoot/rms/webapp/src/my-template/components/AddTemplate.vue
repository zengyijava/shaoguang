<template>
  <div class="template-li">
    <!-- 添加按钮 -->
    <div
      class="template-add-btn"
      @click="addNewTemplate()">
      <div class="template-add-icon"></div>
      <p class="add-desc">创建RX富信</p>
    </div>
    <!-- dialog -->
    <el-dialog
      top="12vh"
      :class="['add-template-dialog', addDialogWidthClass]"
      :visible.sync="dialogVisible">
      <p slot="title">请选择RX-富信类型</p>
      <div class="clearfix">
        <!-- 富文本 -->
        <div
          v-if="showEditorAddBtn(13) && pageType === 'my'"
          class="add-li"
          @click="linkToEditor(13)">
          <div class="img">
            <img src="../../libs/assets/img/add-scene/list_preview_text_cn.png">
          </div>
          <h4 class="add-li-title">富文本消息</h4>
          <p class="add-desc">可以标红加粗的文本消息</p>
        </div>
        <!-- 富媒体 -->
        <div
          v-if="showEditorAddBtn(11)"
          class="add-li"
          @click="linkToEditor(11)">
          <div class="img">
            <img src="../../libs/assets/img/add-scene/list_preview_media_cn.png">
          </div>
          <h4 class="add-li-title">图文消息</h4>
          <p class="add-desc">支持文本、图片、音频、视频等多类型信息</p>
        </div>
        <!-- 场景 -->
        <div
          v-if="showEditorAddBtn(12)"
          class="add-li"
          @click="linkToEditor(12)">
          <div class="img">
            <img src="../../libs/assets/img/add-scene/list_preview_card_cn.png">
          </div>
          <h4 class="add-li-title">交互消息</h4>
          <p class="add-desc">支持链接跳转、打开APP等交互操作</p>
        </div>
        <!-- h5 -->
        <div
          v-if="showEditorAddBtn(15)"
          class="add-li"
          @click="linkToEditor(15)">
          <div class="img">
            <img src="../../libs/assets/img/add-scene/list_preview_h5_cn.png">
          </div>
          <h4 class="add-li-title">企业秀消息</h4>
          <p class="add-desc">丰富多彩的多页动画消息</p>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import config from '../../libs/config'

export default {
  name: 'AddTemplate',
  data () {
    return {
      dialogVisible: false
    }
  },
  props: {
    pageType: {
      type: String,
      default: 'my'
    },
    modulePer: Array
  },
  computed: {
    // 根据编辑器配置显示入口区宽度
    addDialogWidthClass () {
      let _editorConfigLen

      // 模板库不需要富文本编辑器
      if (this.pageType === 'my') {
        _editorConfigLen = this.modulePer.length
      } else {
        _editorConfigLen = this.modulePer.filter((item) => {
          return item.type !== 13
        }).length
      }

      if (_editorConfigLen === 2) {
        return 'add-dialog-w2'
      } else if (_editorConfigLen === 3) {
        return 'add-dialog-w3'
      } else if (_editorConfigLen === 4) {
        return 'add-dialog-w4'
      }
    }
  },
  methods: {
    // 显示编辑器链接入口
    showEditorAddBtn (type) {
      let _editorConfig = this.modulePer
      if (_editorConfig.find((key) => key.type === type)) {
        return true
      } else {
        return false
      }
    },

    // 跳转到编辑器
    linkToEditor (editorType) {
      let _isPublicVal = (this.pageType === 'my') ? 0 : 1
      let _attrParams = 'isPublic=' + _isPublicVal + ''
      let _locationUrl
      // 关闭弹层
      this.dialogVisible = false
      switch (editorType) {
        case 12:
          _locationUrl = config.SCENE_EDITOR_LINK
          break;
        case 11:
          _locationUrl = config.MEDIA_EDITOR_LINK
          break;
        case 13:
          _locationUrl = config.TEXT_EDITOR_LINK
          break;
        case 15:
          _locationUrl = config.H5_EDITOR_LINK
          break;
      }
      window.open(_locationUrl + '?' + _attrParams, '_blank')
    },

    // 添加新模板
    addNewTemplate () {
      let _editorConfigLen = this.modulePer.length
      if (_editorConfigLen > 1) {
        this.dialogVisible = true
      } else {
        this.linkToEditor(this.modulePer[0].type)
      }
    }
  }
}
</script>

<style lang="less">
@import "../../libs/assets/less/variables";
.template-add-btn{
  cursor: pointer;
  color: @grey;
  &:hover{
    color: @green;
    .template-add-icon{
      background: url("../assets/img/add_Choice_icon.png") no-repeat center;
    }
  }
  .template-add-icon{
    width: 38px;
    height: 38px;
    margin:  124px auto 21px;
    background: url("../assets/img/add_default_icon.png") no-repeat center;
  }
  .add-desc{
    font-size: 20px;
    text-align: center;
  }
}
.add-template-dialog {
  &.add-dialog-w2{
    .el-dialog{
      width: 692px;
    }
  }
  &.add-dialog-w3{
    .el-dialog{
      width: 1006px;
    }
  }
  &.add-dialog-w4{
    .el-dialog{
      width: 1320px;
    }
  }
  .el-dialog{
    border-radius: 4px;
    -o-border-radius: 4px;
    -ms-border-radius: 4px;
    -moz-border-radius: 4px;
    -webkit-border-radius: 4px;
    overflow: hidden;
  }
  .el-dialog__header{
    padding: 18px 24px;
    background-color: @dialog-header-bg;
    p{
      margin: 0;
      font-size: 14px;
      line-height: 1;
      color: @grey-black;
    }
    .el-dialog__headerbtn{
      top: 14px;
    }
  }
  .el-dialog__body{
    padding: 62px 45px 100px;
  }
  .add-li{
    width: 268px;
    padding: 10px;
    float: left;
    margin-right: 26px;
    font-size: 14px;
    line-height: 1;
    cursor: pointer;
    &:last-child{
      margin-right: 0;
    }
    &:hover{
      box-shadow: 1px 3px 9px 0px rgba(0, 0, 0, 0.3);
      -o-box-shadow: 1px 3px 9px 0px rgba(0, 0, 0, 0.3);
      -ms-box-shadow: 1px 3px 9px 0px rgba(0, 0, 0, 0.3);
      -moz-box-shadow: 1px 3px 9px 0px rgba(0, 0, 0, 0.3);
      -webkit-box-shadow: 1px 3px 9px 0px rgba(0, 0, 0, 0.3);
    }
    .img{
      width: 266px;
      height: 316px;
    }
    .add-li-title{
      margin: 22px 0 0;
      font-weight: normal;
      color: @grey-dark;
    }
    .add-desc{
      margin: 9px 0 0;
      color: @grey;
    }
  }
}
</style>
