<template>
  <div class="preview-page">
    <div class="preview-list clearfix">
      <div
        v-for="(items, key) in previewData"
        :key="key"
        class='phone-preview'>
        <h4
          v-if="items.tmpType == 12 && previewModule.title"
          class='preview-title'>
          {{ $t('previewText.cardsTitleText') }}
        </h4>
        <h4
          v-else-if="items.tmpType == 11 && previewModule.title && mainTmpType == 11"
          class='preview-title'>
          {{ $t('previewText.mediaTitleText') }}
        </h4>
        <h4
          v-else-if="items.tmpType == 11 && previewModule.title && mainTmpType == 12"
          class='preview-title'>
          {{ $t('previewText.supplementOneText') }}
        </h4>
        <h4
          v-else-if="items.tmpType == 13 && previewModule.title && mainTmpType == 13"
          class='preview-title'>
          {{ $t('previewText.richTextTitleText') }}
        </h4>
        <h4
          v-else-if="items.tmpType == 14 && previewModule.title && mainTmpType == 12"
          class='preview-title'>
          {{ $t('previewText.supplementTwoText') }}
        </h4>
        <h4
          v-else-if="items.tmpType == 14 && previewModule.title"
          class='preview-title'>
          {{ $t('previewText.supplementText') }}
        </h4>
        <div
          v-if="items.tmpType !== 15"
          class="phone-box">
          <h5 class="msg-title"></h5>
          <div class="phone-cont">
            <div class="msg-cont">
              <div class="msg-icon icon icon-my-logo"></div>
              <p class="msg-time">{{ new Date()|formatDate }}</p>
            </div>
            <mark-preview :markData="markData"></mark-preview>
            <scene-preview
              v-if="items.tmpType == 12"
              :previewData="items.content"
              :previewContW="260"
              :showScrollName="true"
              :params="params">
            </scene-preview>
            <media-preview
              v-else-if="items.tmpType == 11"
              :previewData="items.content"
              :previewContW="260"
              :showScrollName="true"
              :params="params">
            </media-preview>
            <rich-text-preview
              v-else-if="items.tmpType == 13"
              :previewData="items.content"
              :params="params">
            </rich-text-preview>
            <msg-preview
              v-else-if="items.tmpType == 14"
              :previewData="items.content">
            </msg-preview>
          </div>
        </div>
        <h5-preview
          v-else
          :previewData="items.content"
          :appData="items.app"
          :showScrollName="true"
          :params="params">
        </h5-preview>
      </div>
    </div>
    <p
      v-if="previewModule.hint"
      class="preview-hint">
      {{ $t('previewText.hintMsgText') }}
    </p>
    <slot></slot>
  </div>
</template>

<script>
import ScenePreview from './property/ScenePreview'
import MediaPreview from './property/MediaPreview'
import RichTextPreview from './property/RichTextPreview'
import MsgPreview from './property/MsgPreview'
import H5Preview from './property/H5Preview'
import MarkPreview from './property/MarkPreview'
import filters from '../filters'

export default {
  name: 'Preview',
  components: {
    'scene-preview': ScenePreview,
    'media-preview': MediaPreview,
    'rich-text-preview': RichTextPreview,
    'msg-preview': MsgPreview,
    'h5-preview': H5Preview,
    'mark-preview': MarkPreview
  },
  props: {
    previewData: Array,
    previewModule: Object,
    mainTmpType: Number,
    markData: {
      type: Object,
      default: () => {
        return {}
      }
    },
    params: {
      type: Array,
      default: () => {
        return []
      }
    }
  },
  filters
}
</script>

<style lang="less">
@import '../../libs/assets/less/variables';

.preview-page{
  display: inline-block;
  .preview-list {
    padding: 20px 34px;
  }
  .preview-hint{
    font-size: 12px;
    text-align: center;
    color: @grey;
  }
  .preview-use-btn{
    width: 130px;
    height: 34px;
    display: block;
    margin-left: auto;
    margin-right: auto;
    text-align: center
  }
}

.phone-preview{
  .phone-box{
    width: 322px;
    height: 613px;
    overflow: hidden;
    background: url('../assets/img/preview_phone.png') no-repeat center center;
  }
  .preview-title{
    margin-top: 12px;
    margin-bottom: 12px;
    font-size: 14px;
    text-align: center;
  }
  h4,h5,h6,p{
    margin: 0;
    padding: 0;
    font-weight: normal;
  }
  padding-left: 23px;
  padding-right: 23px;
  float: left;
  .msg-title{
    height: 14px;
    margin-top: 38px;
    font-size: 14px;
    text-align: center;
  }
  .phone-cont{
    width: 310px;
    height: 520px;
    margin-top: 13px;
    margin-left: 7px;
    overflow-y: auto;
    overflow-x: hidden;
  }
  .msg-cont{
    position: relative;
    padding-left: 38px;
    margin-top: 20px;
    font-size: 12px;
    color: #8c8c8c;
    line-height: 1.3;
    &.card-msg-cont{
      padding-left: 12px;
    }
    .msg-icon{
      position: absolute;
      top: 20px;
      left: 4px;
      z-index: 2;
      width: 28px;
      height: 28px;
      background: url("../assets/img/qylogo.png") no-repeat center center;
      background-size: contain;
    }
  }
  .card-cont{
    position: relative;
    width: 260px;
    margin-top: 6px;
    margin-left: 38px;
    line-height: 1.3;
    border: 1px solid #d6d6d6;
    border-radius: 16px;
  }
  .media-cont{
    max-width: 260px;
    margin-top: 6px;
    margin-left: 38px;
    overflow: hidden;
    border: 1px solid #d6d6d6;
    border-radius: 16px;
    line-height: 1.3;
  }
}
</style>
