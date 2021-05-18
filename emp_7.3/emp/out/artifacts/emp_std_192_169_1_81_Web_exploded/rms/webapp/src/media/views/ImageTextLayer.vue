<template>
  <el-dialog
    :visible.sync="dialogTableVisible"
    :append-to-body="isAppendToBody"
    @open="openDialog"
    @closed="closePanel"
    width="1024px">
    <div id="img-editor-layer" class="img-editor-layer">
      <!-- header -->
      <div class="img-editor-hd" slot="title">
        <h2 class="title">{{ $t('media.onlineImgEdit') }}</h2>
        <div class="close-btn" @click="cancelPanel">
          <i class="close-icon"></i>
        </div>
      </div>
      <!-- body -->
      <div class="img-editor-body editor-clear" :class="{'compound': needFixed}" v-loading="loading">
        <!-- imgContent -->
        <div class="editor-show-bd editor-fl">
          <div class="img-show-cont editor-fl">
            <div class="show-box" :style="{width:imgWidth+'px',height:imgHeight+'px'}">
              <div class="edit-container" id="editorContainer">
                <ElementDRR
                  class="del-bar"
                  @tellShowText="dealShowText"
                  v-for="(item,index) in dailogElement.textEditable"
                  :deactive="false"
                  v-if="item.type==='text'"
                  :element="item"
                  :key="index"
                  :parent="true"
                  :handles="['w', 'e']"
                  :resizable="true"
                  :rotating="true"
                  :rotatable="true"
                  :draggable="true">
                  <RichImageText v-model="item.text"
                                 :element="item"
                                 @update-height="updateHeight($event, item)"
                                 @get-text="getText">
                  </RichImageText>
                  <div class="del-text-cloak">
                    <div class="cloak-text-bar" @click="delTextImg(index)">
                      <i class="el-icon-delete"></i>
                    </div>
                  </div>
                </ElementDRR>
                <ElementDRR class="del-bar"
                            v-for="(item,index) in dailogElement.textEditable"
                            @tellShowText="dealShowText"
                            :deactive="false"
                            v-if="item.type==='image'"
                            :element="item"
                            :key="item.tag"
                            :rotating="true"
                            :rotatable="true"
                            :parent="true"
                            :draggable="true"
                            @resizing="true">
                  <img :src="item.src" draggable="false" style="width: 100%; height: 100%;" />
                  <div class="del-cloak">
                    <div class="cloak-bar" @click="delTextImg(index)">
                      <i class="el-icon-delete"></i>
                    </div>
                  </div>
                </ElementDRR>
                <img :src="bkImgSrc" width="100%" height="100%" draggable="false" />
              </div>
              <ul class="container-tools">
                <!-- <li class="container-tools-btn prev-btn"></li>
                <li class="container-tools-btn next-btn"></li> -->
                <el-tooltip  effect="dark" :content="$t('media.imgEdit')" placement="right">
                  <li class="container-tools-btn bg-btn" @click="showBgImg"></li>
                </el-tooltip>
              </ul>
            </div>
          </div>
          <div class="img-edit-cont editor-fl">
            <div class="text-edit-cont" :class="{none:!showText}">
              <h4 class="title">{{ $t('media.textEdit') }}</h4>
              <div class="edit-box">
                <ul class="font-tools">
                  <li class="tools-li editor-clear">
                    <p class="desc editor-fl">{{ $t('property.mediaText') }}</p>
                    <div style="border: 1px solid #dcdfe6;
                      border-radius: 3px;
                      padding-left: 14px;
                      padding-right: 14px;
                      height: 130px;
                      float: left;
                      overflow: auto;
                      width: 180px;"
                      maxlength="180"
                      contenteditable="true"
                      v-html="changeHtml"
                      ref="writeBox"
                      @keyup.stop="handleChange"
                      @paste="handlePaste">
                    </div>
                  </li>
                  <li class="tools-li editor-clear">
                    <p class="desc editor-fl">{{ $t('property.family') }}</p>
                    <div class="select-cont editor-fl">
                      <p class="show-select-result" @click="isFontFamily=true">{{!isCurrentElement?currentElement.style.fontFamily:$t('property.family')}}</p>
                      <i class="pull-icon"></i>
                      <ol class="select-list" :style="{display:isFontFamily?'block':'none'}">
                        <li class="list-li active" data-val="黑体" @click="setElementStyle('黑体','fontFamily')" style="font-family: 黑体;">黑体</li>
                        <li class="list-li" data-val="宋体" @click="setElementStyle('宋体','fontFamily')" style="font-family: 宋体;">宋体</li>
                        <li class="list-li" data-val="仿宋" @click="setElementStyle('仿宋','fontFamily')" style="font-family: 仿宋;">仿宋</li>
                        <li class="list-li" data-val="serif" @click="setElementStyle('serif','fontFamily')" style="font-family: serif;">Serif</li>
                        <li class="list-li" data-val="微软雅黑" @click="setElementStyle('微软雅黑','fontFamily')" style="font-family: 微软雅黑;">微软雅黑</li>
                        <li class="list-li" data-val="sans-serif" @click="setElementStyle('sans-serif','fontFamily')" style="font-family: sans-serif;">Sans-serif</li>
                        <li class="list-li" data-val="Helvetica" @click="setElementStyle('Helvetica','fontFamily')" style="font-family: Helvetica;">Helvetica</li>
                      </ol>
                    </div>
                  </li>
                  <li class="tools-li editor-clear">
                    <p class="desc editor-fl">
                      <i class="desc-icon size-icon"></i>
                    </p>
                    <div class="select-cont editor-fl">
                      <p class="show-select-result" @click="isFontSize=true">{{!isCurrentElement?currentElement.style.fontSize:'12px'}}</p>
                      <i class="pull-icon"></i>
                      <ol class="select-list" :style="{display:isFontSize?'block':'none'}">
                        <li class="list-li active" data-val="12" @click="setElementStyle('12px','fontSize')" style="font-size: 12px;">12px</li>
                        <li class="list-li" data-val="14" @click="setElementStyle('14px','fontSize')" style="font-size: 14px;line-height: 30px;">14px</li>
                        <li class="list-li" data-val="16" @click="setElementStyle('16px','fontSize')" style="font-size: 16px;line-height: 30px;">16px</li>
                        <li class="list-li" data-val="18" @click="setElementStyle('18px','fontSize')" style="font-size: 18px;line-height: 30px;">18px</li>
                        <li class="list-li" data-val="24" @click="setElementStyle('24px','fontSize')" style="font-size: 24px;line-height: 30px;">24px</li>
                        <li class="list-li" data-val="32" @click="setElementStyle('32px','fontSize')" style="font-size: 32px;line-height: 40px;">32px</li>
                        <li class="list-li" data-val="48" @click="setElementStyle('48px','fontSize')" style="font-size: 48px;line-height: 52px;">48px</li>
                      </ol>
                    </div>
                    <div class="li-btn-box editor-fl">
                      <div class="icon-btn editor-fl" @click="addTextSize">
                        <i class="li-size-icons add-icon"></i>
                      </div>
                      <div class="icon-btn editor-fl" @click="reduceTextSize">
                        <i class="li-size-icons reduce-icon"></i>
                      </div>
                    </div>
                  </li>
                  <li class="tools-li editor-clear">
                    <p class="desc editor-fl">
                      <i class="desc-icon height-icon"></i>
                    </p>
                    <div class="select-cont editor-fl">
                      <p class="show-select-result" @click="isLineHeight=true">{{!isCurrentElement?currentElement.style.lineHeight:'14px'}}</p>
                      <i class="pull-icon"></i>
                      <ol class="select-list" :style="{display:isLineHeight?'block':'none'}">
                        <li class="list-li" @click="setElementStyle('14px','lineHeight')" data-val="14">14px</li>
                        <li class="list-li" @click="setElementStyle('16px','lineHeight')" data-val="16">16px</li>
                        <li class="list-li" @click="setElementStyle('18px','lineHeight')" data-val="18">18px</li>
                        <li class="list-li" @click="setElementStyle('20px','lineHeight')" data-val="20">20px</li>
                        <li class="list-li" @click="setElementStyle('28px','lineHeight')" data-val="28">28px</li>
                        <li class="list-li" @click="setElementStyle('34px','lineHeight')" data-val="34">34px</li>
                        <li class="list-li" @click="setElementStyle('40px','lineHeight')" data-val="40">40px</li>
                        <li class="list-li" @click="setElementStyle('56px','lineHeight')" data-val="56">56px</li>
                        <li class="list-li" @click="setElementStyle('68px','lineHeight')" data-val="68">68px</li>
                        <li class="list-li" @click="setElementStyle('80px','lineHeight')" data-val="80">80px</li>
                      </ol>
                    </div>
                  </li>
                </ul>
                <div class="algin-format editor-clear">
                  <span class="algin-format-btn font-weight">
                    <i class="icons"></i>
                  </span>
                  <span class="algin-format-btn font-italic">
                    <i class="icons"></i>
                  </span>
                  <span class="algin-format-btn font-line">
                    <i class="icons"></i>
                  </span>
                  <span class="algin-format-btn algin-left active">
                    <i class="icons"></i>
                  </span>
                  <span class="algin-format-btn algin-center">
                    <i class="icons"></i>
                  </span>
                  <span class="algin-format-btn algin-right">
                    <i class="icons"></i>
                  </span>
                  <span class="algin-format-btn font-default">
                    <i class="icons"></i>
                  </span>
                </div>
                <div class="font-color">
                  <ul class="color-table editor-clear">
                    <li class="color-table-li editor-fl" :class="isFontColor?'active':''" data-type="font" @click="setColorType(1)">
                      <i class="color-table-icons"></i>
                      <span>{{ $t('media.textColor') }}</span>
                    </li>
                    <li class="color-table-li editor-fl" :class="!isFontColor?'active':''" data-type="bg" @click="setColorType(0)">
                      <i class="color-table-icons"></i>
                      <span>{{ $t('media.bgColor') }}</span>
                    </li>
                  </ul>
                  <div class="color-fix-select">
                    <div class="fix-select-li editor-clear">
                      <span class="color-select none-select" data-val="transparent" @click="setColor('transparent')"></span>
                      <span class="color-select" data-val="ffffff" @click="setColor('#ffffff')" style="background-color: #ffffff;"></span>
                      <span class="color-select" data-val="d5d5d5" @click="setColor('#d5d5d5')" style="background-color: #d5d5d5;"></span>
                      <span class="color-select" data-val="45d1d7" @click="setColor('#45d1d7')" style="background-color: #45d1d7;"></span>
                      <span class="color-select" data-val="388bb6" @click="setColor('#388bb6')" style="background-color: #388bb6;"></span>
                      <span class="color-select" data-val="47b4f8" @click="setColor('#47b4f8')" style="background-color: #47b4f8;"></span>
                      <span class="color-select" data-val="79c7fa" @click="setColor('#79c7fa')" style="background-color: #79c7fa;"></span>
                      <span class="color-select" data-val="5b89cd" @click="setColor('#5b89cd')" style="background-color: #5b89cd;"></span>
                      <span class="color-select" data-val="3750c0" @click="setColor('#3750c0')" style="background-color: #3750c0;"></span>
                      <span class="color-select" data-val="7a34ff" @click="setColor('#7a34ff')" style="background-color: #7a34ff;"></span>
                      <span class="color-select" data-val="8600ff" @click="setColor('#8600ff')" style="background-color: #8600ff;"></span>
                      <span class="color-select" data-val="9900cd" @click="setColor('#9900cd')" style="background-color: #9900cd;"></span>
                      <span class="color-select" data-val="ca00bc" @click="setColor('#ca00bc')" style="background-color: #ca00bc;"></span>
                    </div>
                    <div class="fix-select-li editor-clear">
                      <span class="color-select" data-val="a7a7a7" @click="setColor('#a7a7a7')" style="background-color: #a7a7a7;"></span>
                      <span class="color-select" data-val="555555" @click="setColor('#555555')" style="background-color: #555555;"></span>
                      <span class="color-select" data-val="56e1bb" @click="setColor('#56e1bb')" style="background-color: #56e1bb;"></span>
                      <span class="color-select" data-val="50c2ae" @click="setColor('#50c2ae')" style="background-color: #50c2ae;"></span>
                      <span class="color-select" data-val="2d749c" @click="setColor('#2d749c')" style="background-color: #2d749c;"></span>
                      <span class="color-select" data-val="7f9cff" @click="setColor('#7f9cff')" style="background-color: #7f9cff;"></span>
                      <span class="color-select" data-val="4a3eff" @click="setColor('#4a3eff')" style="background-color: #4a3eff;"></span>
                      <span class="color-select" data-val="a006ff" @click="setColor('#a006ff')" style="background-color: #a006ff;"></span>
                      <span class="color-select" data-val="9200c4" @click="setColor('#9200c4')" style="background-color: #9200c4;"></span>
                      <span class="color-select" data-val="c237ff" @click="setColor('#c237ff')" style="background-color: #c237ff;"></span>
                      <span class="color-select" data-val="ec6db3" @click="setColor('#ec6db3')" style="background-color: #ec6db3;"></span>
                      <span class="color-select" data-val="eb6d77" @click="setColor('#eb6d77')" style="background-color: #eb6d77;"></span>
                      <span class="color-select" data-val="ef008e" @click="setColor('#ef008e')" style="background-color: #ef008e;"></span>
                    </div>
                    <div class="fix-select-li editor-clear">
                      <span class="color-select" data-val="1e1e1e" @click="setColor('#1e1e1e')" style="background-color: #1e1e1e;"></span>
                      <span class="color-select" data-val="1e965c" @click="setColor('#1e965c')" style="background-color: #1e965c;"></span>
                      <span class="color-select" data-val="19aa00" @click="setColor('#19aa00')" style="background-color: #19aa00;"></span>
                      <span class="color-select" data-val="77dc00" @click="setColor('#77dc00')" style="background-color: #77dc00;"></span>
                      <span class="color-select" data-val="b2df74" @click="setColor('#b2df74')" style="background-color: #b2df74;"></span>
                      <span class="color-select" data-val="c8ff00" @click="setColor('#c8ff00')" style="background-color: #c8ff00;"></span>
                      <span class="color-select" data-val="eeea00" @click="setColor('#eeea00')" style="background-color: #eeea00;"></span>
                      <span class="color-select" data-val="d5ba00" @click="setColor('#d5ba00')" style="background-color: #d5ba00;"></span>
                      <span class="color-select" data-val="c57300" @click="setColor('#c57300')" style="background-color: #c57300;"></span>
                      <span class="color-select" data-val="f28615" @click="setColor('#f28615')" style="background-color: #f28615;"></span>
                      <span class="color-select" data-val="f55b2e" @click="setColor('#f55b2e')" style="background-color: #f55b2e;"></span>
                      <span class="color-select" data-val="d92940" @click="setColor('#d92940')" style="background-color: #d92940;"></span>
                      <el-color-picker @change="setColor" color-format="hex" v-model="colorPicker">
                      </el-color-picker>
                    </div>
                  </div>
                </div>
                <div class="font-width editor-clear">
                  <p class="width-desc editor-fl">{{ $t('media.width') }}</p>
                  <div class="width-input-box editor-fl">
                    <el-input-number size="medium" v-model.number.lazy="currentElement.w" controls-position="right"></el-input-number>
                  </div>
                </div>
                <div class="font-slider-md editor-clear">
                  <p class="slider-md-desc editor-fl">{{ $t('media.rotate') }}</p>
                  <el-slider class="editor-fl editor-slider-line" :min="0" :max="360" v-model="currentElement.rotate" input-size="small"></el-slider>
                  <input class="editor-slider-input editor-fl" type="number" v-model="currentElement.rotate" placeholder="0">
                </div>
              </div>
            </div>
            <div class="crop-edit-cont" :class="{none:showText}">
              <h4 class="title">{{ $t('media.imgEdit') }}</h4>
              <div class="cut-cont editor-clear">
                <ul class="cut-tools-bar editor-fl">
                  <li class="icon-li">
                    <i class="tools-icon cut-icon" @click="startCrop"></i>
                  </li>
                  <li class="icon-li">
                    <i class="tools-icon cancel-icon" @click="clearCrop"></i>
                  </li>
                  <li class="icon-li" @click="changeScale(1)">
                    <i class="tools-icon zoom-add-icon"></i>
                  </li>
                  <li class="icon-li" @click="changeScale(-1)">
                    <i class="tools-icon zoom-reduce-icon"></i>
                  </li>
                  <li class="icon-li">
                    <i class="tools-icon rotate-left-icon" @click="rotateLeft"></i>
                  </li>
                  <li class="icon-li">
                    <i class="tools-icon rotate-right-icon" @click="rotateRight"></i>
                  </li>
                  <!-- <li class="icon-li">
                    <i class="tools-icon reset-icon"></i>
                  </li> -->
                  <!-- <li class="text-li" @click="setFixedNumber([16,9])">16:9</li>
                  <li class="text-li" @click="setFixedNumber([4,3])">4:3</li>
                  <li class="text-li" @click="setFixedNumber([1,1])">1:1</li>
                  <li class="text-li" @click="setFixedNumber([2,3])">2:3</li>
                  <li class="text-li">自定义比例</li> -->
                </ul>
                <div class="cut-cont-show editor-fl">
                  <div class="crop-container">
                    <vue-cropper ref="cropper" :img="cropperUrl" outputType="png" :autoCrop='autoCrop' :centerBox="true" :autoCropWidth="parseInt(cropperWidth)" :autoCropHeight="parseInt(cropperHeight)" :full="true"></vue-cropper>
                  </div>
                  <div class="cut-show-ft editor-clear">
                    <p class="cut-show-info editor-fl">{{ $t('media.imgSize') }}{{(compoundSize/1024).toFixed(2)+'kb'}} {{cropperWidth}} * {{cropperHeight}}</p>
                    <div class="select-box editor-fr">
                      <p class="select-result" @click="isScale=true">{{imgScaleText}}</p>
                      <i class="select-icon" @click="isScale=true"></i>
                      <ul class="select-list" :style="{display:isScale?'block':'none'}">
                        <li class="active" data-val="100" @click="setScale('100', $t('cardImgSetting.masterImg'))">{{ $t('cardImgSetting.masterImg') }}</li>
                        <li @click="setScale('98', $t('cardImgSetting.compressImg'))" data-val="98">{{ $t('cardImgSetting.compressImg') }}</li>
                      </ul>
                    </div>
                  </div>
                </div>
              </div>
              <div class="btn-cont editor-clear">
                <div class="pic-buttons">
                  <el-upload :action="url" :show-file-list="false" style="color:#fff;" :on-success="handleUploadSuccess" class="editor-fl">
                    <el-button class="img-edit-btn" type="primary" size="small" plain>{{ $t('media.replacePicture') }}</el-button>
                  </el-upload>
                  <el-button class="img-cut-btn" type="primary" size="small" @click="finish('base64')" plain>{{ $t('media.cut') }}</el-button>
                </div>
              </div>
            </div>
          </div>
        </div>
        <!-- tools -->
        <ul class="editor-tools-bar editor-fr">
          <!-- text -->
          <li class="tools-bar-li" :class="{active:showText}" @click="showTextEditor">
            <i class="tools-icons text-icon"></i>
            <span class="tools-desc">{{ $t('media.text') }}</span>
          </li>
          <!-- image -->
          <li class="tools-bar-li" :class="{active:!showText}">
            <ImagePicker @on-success="handleUploadInnerImageSuccess" style="margin-left:14px;">
              <i class="tools-icons img-icon"></i>
              <span class="tools-desc">{{ $t('media.img') }}</span>
            </ImagePicker>
          </li>
        </ul>
        <!-- saveAndCancel -->
        <div class="buttons">
          <el-button class="img-save-btn editor-fl" @click="savePanel" plain>{{ $t('property.keep') }}</el-button>
          <el-button class="img-close-btn editor-fl" @click="cancelPanel" plain>{{ $t('media.cancel') }}</el-button>
        </div>
      </div>
    </div>
  </el-dialog>
</template>
<script>
import { mapGetters, mapMutations } from 'vuex'
import VueCropper from '../../libs/components/vue-cropper'
import ElementDRR from '../components/ElementDRR'
import RichImageText from '../components/RichImageText'
import ImagePicker from '../../libs/components/ImagePicker'
import utils from '../../libs/utils'
import AJAXURL from '../../libs/ajax.address'

export default {
  name: 'ImageTextLayer',
  components: {ImagePicker, VueCropper, ElementDRR, RichImageText},
  props: ['imageSrc', 'open'],
  computed: {
    ...mapGetters('media', ['element', 'currentElement']),
    isCurrentElement () {
      if (this.currentElement.style === undefined) {
        return true
      } else {
        return false
      }
    }
  },
  data () {
    return {
      fixedNumber: [1, 1],
      autoCrop: true,
      outputSize: 1,
      fixed: false,
      showText: false,
      saveCropper: false,
      loading: false,
      imgWidth: 0,
      imgHeight: 0,
      changeHtml: this.$t('media.pEnterText'),
      hasCropper: false,
      bkImgSrc: this.imageSrc,
      url: AJAXURL.UPLOAD_IMAGE,
      lastImg: {},
      cropperUrl: this.imageSrc,
      isFontFamily: false,
      isFontSize: false,
      isLineHeight: false,
      isFontColor: 1,
      isScale: false,
      imgScale: 100,
      imgScaleText: this.$t('cardImgSetting.masterImg'),
      width: 0,
      height: 0,
      currentSize: [12, 14, 16, 18, 24, 32, 48],
      sizeIndex: 1,
      top: 0,
      cropperSize: '',
      compoundSize: 0,
      proportion: 0,
      cropperWidth: 200,
      cropperHeight: 200,
      imgContainerMaxWidth: 500,
      previewContW: 260,
      imgContainerMaxHeight: 505,
      isAppendToBody: true,
      dialogTableVisible: false,
      dailogElement: {},
      initData: '',
      colorPicker: '',
      needFixed: true
    }
  },
  watch: {
    open (val) {
      this.dialogTableVisible = val
    }
  },
  methods: {
    ...mapMutations('media', ['addImageText', 'addInnerImage', 'clearTextEitable']),
    closePanel () {
      this.dailogElement = {}
      this.showText = false
      if (!this.saveCropper) {
        this.cropperUrl = this.bkImgSrc
      }
      this.$emit('closePanel')
    },
    cancelPanel () {
      for (let item in this.initData) {
        this.element[item] = this.initData[item]
      }
      this.closePanel()
    },
    openDialog () {
      this.initData = utils.deepClone(this.element)

      if (this.element.type === 'text') {
        this.dailogElement = this.element.image
        this.bkImgSrc = this.element.image.src
        this.cropperSize = this.element.image.size
        this.compoundSize = (this.element.image.compoundSize && this.element.image.compoundSize > 0) ? this.element.image.compoundSize : this.element.image.size
        this.cropperWidth = this.element.image.width
        this.cropperHeight = this.element.image.height
        this.lastImg = this.element.image
      } else {
        this.dailogElement = this.element
        this.bkImgSrc = this.element.src
        this.cropperSize = this.element.size
        this.compoundSize = (this.element.compoundSize && this.element.compoundSize > 0) ? this.element.compoundSize : this.element.size
        this.cropperWidth = this.element.width
        this.cropperHeight = this.element.height
        this.lastImg = this.element
      }
      if (this.bkImgSrc) {
        this.cropperUrl = this.bkImgSrc
      }
      let openImg = new Image()
      openImg.src = this.bkImgSrc
      setTimeout(() => {
        let scale = (openImg.width / openImg.height).toFixed(2)
        if (scale > 1) {
          if (openImg.width > this.imgContainerMaxWidth) {
            this.imgWidth = this.imgContainerMaxWidth
            this.hasCropper = false
          } else {
            this.imgWidth = openImg.width
          }
          this.imgHeight = this.imgWidth / scale
        } else {
          if (openImg.height > this.imgContainerMaxHeight) {
            this.imgHeight = this.imgContainerMaxHeight
            this.hasCropper = false
          } else {
            this.imgHeight = openImg.height
          }
          this.imgWidth = this.imgHeight * scale
        }
        this.$refs.cropper.checkedImg()
      }, 200)
      this.showText = false
    },
    handleUploadInnerImageSuccess (event) {
      this.showText = false
      let payload = {
        image: event,
        element: this.element
      }
      this.addInnerImage(payload)
      this.$refs.cropper.checkedImg()
    },
    handleUploadSuccess (res, file) {
      this.isChange = true
      if (res.state === 'SUCCESS') {
        this.showText = false
        this.saveCropper = false
        this.hasCropper = false
        if (this.element.type === 'text') {
          this.lastImg.image = res
        } else {
          this.lastImg = res
        }
        this.cropperUrl = res.url
        this.cropperSize = res.size
        this.compoundSize = res.size
        this.cropperWidth = res.width
        this.cropperHeight = res.height
      } else {
        this.$message.error(res.state)
      }
    },
    getText (text) {
      this.changeHtml = text
    },
    handleChange (event) {
      // 阻止冒泡删除
      if (event.keyCode === 46) {
        event.stopPropagation()
      }
      let _text = event.target.innerText
      if (_text.length > 180) {
        event.target.innerText = _text.slice(0, 180)
      }
      this.currentElement.text = event.target.innerHTML
    },
    handlePaste (event) {
      let _text = event.target.innerText
      if (_text.length > 180) {
        event.target.innerText = _text.slice(0, 180)
      }
      this.changeHtml = (event.originalEvent || event).clipboardData.getData('text/plain').replace(/\r\n/g, '')
      this.currentElement.text = event.target.innerHTML
    },
    dealShowText (deal) {
      this.showText = deal
    },
    showTextEditor () {
      this.addImageText(this.element)
      this.changeHtml = this.$t('media.pEnterText')
      this.$refs.writeBox.innerHTML = this.$t('media.pEnterText')
      this.showText = true
    },
    changeElementW (type) {
      let num
      let width = parseInt(this.currentElement.w)
      if (type === 'add') {
        num = 1
      } else {
        num = -1
      }
      this.currentElement.w = width + num
    },
    resetLastImg () {
      if (!this.saveCropper) {
        if (this.element.type === 'text') {
          this.cropperUrl = this.lastImg.image.src
          this.cropperWidth = this.lastImg.image.width
          this.cropperHeight = this.lastImg.image.height
          this.cropperSize = this.lastImg.image.size
        } else {
          this.cropperUrl = this.lastImg.src
          this.cropperWidth = this.lastImg.width
          this.cropperHeight = this.lastImg.height
          this.cropperSize = this.lastImg.size
        }
      }
    },
    delTextImg (index) {
      this.dailogElement.textEditable.splice(index, 1)
      this.changeHtml = this.$t('media.pEnterText')
      this.$refs.writeBox.innerHTML = this.$t('media.pEnterText')
    },
    setScale (scale, scaleText) {
      this.imgScaleText = scaleText
      this.imgScale = scale
      this.isScale = false
    },
    setColorType (type) {
      this.isFontColor = type
    },
    setColor (color) {
      if (this.isFontColor && color) {
        this.currentElement.style.color = color
      } else if (color) {
        this.currentElement.style.backgroundColor = color
      }
    },
    addTextSize () {
      let _currentFontSize = parseInt(this.currentElement.style.fontSize)
      if (_currentFontSize >= 46) {
        _currentFontSize = 46
      }
      this.currentElement.style.fontSize = (_currentFontSize + 2) + 'px'
      this.currentElement.style.lineHeight = (_currentFontSize + 2) + 'px'
    },
    reduceTextSize () {
      let _currentFontSize = parseInt(this.currentElement.style.fontSize)
      if (_currentFontSize <= 14) {
        _currentFontSize = 14
      }
      this.currentElement.style.fontSize = (_currentFontSize - 2) + 'px'
      this.currentElement.style.lineHeight = (_currentFontSize - 2) + 'px'
    },
    setElementStyle (style, type) {
      this.currentElement.style[type] = style
      switch (type) {
        case 'fontFamily':
          this.isFontFamily = false
          break
        case 'fontSize':
          this.isFontSize = false
          this.currentElement.style.lineHeight = style
          break
        case 'lineHeight':
          this.isLineHeight = false
          this.currentElement.h = this.$el.getBoundingClientRect().height
          this.currentElement.style.lineHeight = style
          break
        default:
          break
      }
    },
    savePanel () {
      this.needFixed = true
      let _this = this
      let bgImageUrl
      if (!this.hasCropper) {
        // 输出
        this.saveCropper = true
        this.hasCropper = true
        this.$refs.cropper.getCropData((data) => {
          if (this.element.type === 'text') {
            bgImageUrl = this.element.image.src
          } else {
            bgImageUrl = this.element.src
          }
          let params = {
            clarity: this.imgScale,
            src: bgImageUrl,
            data: data
          }
          let formParam = utils.transformRequest(params)
          this.loading = true
          this.$post(AJAXURL.IMG_CROPPER, formParam).then(res => {
            if (res.state === '0') {
              _this.bkImgSrc = res.path
              // this.element.src = res.path
              _this.cropperSize = res.size
              _this.cropperWidth = res.width
              _this.cropperHeight = res.height
              // 图片真实宽度是否大于等于真实高度
              if (_this.cropperWidth >= _this.cropperHeight) {
                // 图片真实宽是否大于等于裁剪容器宽度
                if (_this.cropperWidth >= _this.imgContainerMaxWidth) {
                  _this.imgWidth = _this.imgContainerMaxWidth
                  _this.proportion = _this.previewContW / _this.imgContainerMaxWidth
                  _this.imgHeight = _this.cropperHeight * (_this.imgContainerMaxWidth / _this.cropperWidth)
                } else {
                  _this.imgWidth = _this.cropperWidth
                  _this.imgHeight = _this.cropperHeight
                  _this.proportion = _this.previewContW / _this.cropperWidth
                }
              } else {
                // 图片真实高度是否大于等于裁剪器容器高度
                if (_this.cropperHeight >= _this.imgContainerMaxHeight) {
                  _this.imgHeight = _this.imgContainerMaxHeight
                  _this.imgWidth = _this.cropperWidth * (_this.imgContainerMaxHeight / _this.cropperHeight)
                  _this.proportion = _this.previewContW / _this.imgWidth
                } else {
                  _this.imgWidth = _this.cropperWidth
                  _this.imgHeight = _this.cropperHeight
                  _this.proportion = _this.previewContW / _this.cropperWidth
                }
              }

              // 判断裁剪后的图片是否大于编码面板宽度
              if (_this.cropperWidth >= _this.previewContW) {
                _this.proportion = _this.previewContW / _this.cropperWidth
              } else {
                _this.proportion = 0
              }

              // 判断是图配文中的图片还是文配图中的图片
              if (_this.element.type === 'text') {
                _this.element.image.w = _this.proportion ? _this.previewContW : _this.cropperWidth
                _this.element.image.h = _this.proportion ? parseInt(_this.cropperHeight * _this.proportion) : _this.cropperHeight
                _this.element.image.size = _this.cropperSize
                _this.element.image.src = _this.bkImgSrc
                _this.element.image.width = _this.cropperWidth
                _this.element.image.height = _this.cropperHeight
                _this.element.image.scale = _this.proportion
              } else {
                _this.element.w = _this.proportion ? _this.previewContW : _this.cropperWidth
                _this.element.h = _this.proportion ? parseInt(_this.cropperHeight * _this.proportion) : _this.cropperHeight
                _this.element.src = _this.bkImgSrc
                _this.element.width = _this.cropperWidth
                _this.element.height = _this.cropperHeight
                _this.element.size = _this.cropperSize
                _this.element.scale = _this.proportion
              }
              _this.compoundImgSize()
            }
          }).catch(e => {
            console.log(e)
            this.compoundImgSize()
          })
        })
      } else {
        if (this.element.type === 'text') {
          this.element.image.w = this.proportion ? this.previewContW : this.cropperWidth
          this.element.image.h = this.proportion ? parseInt(this.cropperHeight * this.proportion) : this.cropperHeight
          this.element.image.size = this.cropperSize
          this.element.image.src = this.bkImgSrc
          this.element.image.width = this.cropperWidth
          this.element.image.height = this.cropperHeight
          this.element.image.scale = this.proportion
        } else {
          this.element.w = this.proportion ? this.previewContW : this.cropperWidth
          this.element.h = this.proportion ? parseInt(this.cropperHeight * this.proportion) : this.cropperHeight
          this.element.src = this.bkImgSrc
          this.element.width = this.cropperWidth
          this.element.height = this.cropperHeight
          this.element.size = this.cropperSize
          this.element.scale = this.proportion
        }
        _this.compoundImgSize()
      }
    },
    compoundImgSize () {
      let editTextEditable = []
      if (this.element.type === 'text') {
        editTextEditable = [...this.element.image.textEditable]
      } else {
        editTextEditable = [...this.element.textEditable]
      }

      if (editTextEditable.length > 0) {
        let compoundParam = {
          src: this.bkImgSrc,
          width: this.cropperWidth,
          height: this.cropperHeight,
          textEditable: editTextEditable
        }
        let _self = this
        this.$post(AJAXURL.IMG_COMPOUND, compoundParam).then(res => {
          if (res.code === 200) {
            if (_self.element.type === 'text') {
              _self.element.image.compoundSize = res.data.compoundSize
            } else {
              _self.element.compoundSize = res.data.compoundSize
            }
          }
          _self.loading = false
          _self.closePanel()
        })
      } else {
        this.loading = false
        this.closePanel()
      }
    },
    startCrop () {
      this.crap = true
      this.$refs.cropper.startCrop()
    },
    setFixedNumber (fixed) {
      this.fixedNumber = fixed
      console.log(this.fixedNumber)
    },
    clearCrop () {
      // clear
      this.$refs.cropper.clearCrop()
    },
    changeScale (num) {
      num = num || 1
      this.$refs.cropper.changeScale(num)
    },
    rotateLeft () {
      this.$refs.cropper.rotateLeft()
      if (this.$refs.cropper.rotate === 0 || this.$refs.cropper.rotate === -2 || this.$refs.cropper.rotate === 2) {
        this.$refs.cropper.changeCrop(this.$refs.cropper.cropW, this.$refs.cropper.cropH)
      } else {
        this.$refs.cropper.changeCrop(this.$refs.cropper.cropH, this.$refs.cropper.cropW)
      }
    },
    rotateRight () {
      this.$refs.cropper.rotateRight()
      if (this.$refs.cropper.rotate === 0 || this.$refs.cropper.rotate === -2 || this.$refs.cropper.rotate === 2) {
        this.$refs.cropper.changeCrop(this.$refs.cropper.cropW, this.$refs.cropper.cropH)
      } else {
        this.$refs.cropper.changeCrop(this.$refs.cropper.cropH, this.$refs.cropper.cropW)
      }
    },
    finish () {
      this.needFixed = false
      // 输出
      let _this = this
      let bgImageUrl
      this.saveCropper = true
      this.hasCropper = true
      _this.$refs.cropper.getCropData((data) => {
        if (this.element.type === 'text') {
          bgImageUrl = this.element.image.src
        } else {
          bgImageUrl = this.element.src
        }
        let params = {
          clarity: this.imgScale,
          src: bgImageUrl,
          data: data
        }
        let formParam = utils.transformRequest(params)
        this.loading = true
        _this.$post(AJAXURL.IMG_CROPPER, formParam).then(res => {
          this.loading = false
          if (res.state === '0') {
            _this.cropperUrl = res.path
            _this.bkImgSrc = res.path
            // _this.element.src = res.path
            _this.cropperSize = res.size
            _this.compoundSize = res.size
            _this.cropperWidth = res.width
            _this.cropperHeight = res.height
            // 图片真实宽度是否大于等于真实高度
            if (_this.cropperWidth >= _this.cropperHeight) {
              // 图片真实宽是否大于等于裁剪容器宽度
              if (_this.cropperWidth >= _this.imgContainerMaxWidth) {
                _this.imgWidth = _this.imgContainerMaxWidth
                _this.proportion = _this.previewContW / _this.imgContainerMaxWidth
                _this.imgHeight = _this.cropperHeight * (_this.imgContainerMaxWidth / _this.cropperWidth)
              } else {
                _this.imgWidth = _this.cropperWidth
                _this.imgHeight = _this.cropperHeight
                _this.proportion = _this.previewContW / _this.cropperWidth
              }
            } else {
              // 图片真实高度是否大于等于裁剪器容器高度
              if (_this.cropperHeight >= _this.imgContainerMaxHeight) {
                _this.imgHeight = _this.imgContainerMaxHeight
                _this.imgWidth = _this.cropperWidth * (_this.imgContainerMaxHeight / _this.cropperHeight)
                _this.proportion = _this.previewContW / _this.imgWidth
              } else {
                _this.imgWidth = _this.cropperWidth
                _this.imgHeight = _this.cropperHeight
                _this.proportion = _this.previewContW / _this.cropperWidth
              }
            }

            // 判断裁剪后的图片是否大于编码面板宽度
            if (_this.cropperWidth >= _this.previewContW) {
              _this.proportion = _this.previewContW / _this.cropperWidth
            } else {
              _this.proportion = 0
            }
          }
        })
      })
    },
    updateHeight (event, element) {
      if (event + element.y >= this.imgHeight) {
        element.overflowY = 'scroll'
      } else {
        element.overflowY = ''
      }
      this.$nextTick(() => {
        element.h = event
      })
    },
    showBgImg () {
      this.showText = false
      this.$refs.cropper.checkedImg()
    }
  }
}
</script>
<style lang="less">
@import '../../libs/assets/less/imageLayer.less';
.compound {
  .el-loading-mask {
    position: fixed;
  }
}
.img-editor-body .el-color-picker {
  .el-color-picker__trigger {
    border: none;
    width: 19px;
    height: 19px;
    margin-right: 1px;
    margin-bottom: 2px;
    padding: 0;
    background: url('../../libs/assets/img/pic-cut/other-select.png') no-repeat;
    .el-icon-arrow-down {
      display: none;
    }
  }
}
.font-slider-md .el-slider__runway {
  margin: 14px 0;
}
.del-bar {
  position: relative;
  width: 100%;
  height: 100%;
  &:hover {
    .del-cloak,
    .del-text-cloak {
      display: block;
    }
  }
  .del-text-cloak {
    position: absolute;
    top: -2px;
    right: -20px;
    width: 20px;
    height: 20px;
    display: none;
    .cloak-text-bar {
      position: absolute;
      z-index: 2;
      cursor: pointer;
      i {
        font-size: 20px;
        color: #fff;
      }
      &:hover {
        i {
          color: #2e95ff;
        }
      }
    }
  }
  .del-cloak {
    position: absolute;
    top: 0;
    left: 0;
    z-index: 4;
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
.editor-slider-line {
  width: 134px;
  margin-right: 10px;
  .el-slider__runway {
    background-color: #e4e7ed;
  }
}
.img-edit-cont {
  .none {
    display: none;
  }
}

.editor-show-bd .container-tools .container-tools-btn {
  width: 34px;
  height: 34px;
}

.editor-show-bd .container-tools .container-tools-btn.prev-btn {
  background: url('../../libs/assets/img/pic-cut/prev-btn.png');
}
.editor-show-bd .container-tools .container-tools-btn.next-btn {
  background: url('../../libs/assets/img/pic-cut/next-btn.png');
}
.editor-show-bd .container-tools .container-tools-btn.bg-btn {
  background: url('../../libs/assets/img/pic-cut/bg-btn.png');
  box-shadow: 0px 1px 10px 0px rgba(0, 0, 0, 0.1);
}
.editor-show-bd .container-tools .container-tools-btn:hover.prev-btn {
  background: url('../../libs/assets/img/pic-cut/prev-btn-hover.png');
  background-color: #3b9cff;
}
.editor-show-bd .container-tools .container-tools-btn:hover.next-btn {
  background: url('../../libs/assets/img/pic-cut/next-btn-hover.png');
  background-color: #3b9cff;
}
.editor-show-bd .container-tools .container-tools-btn:hover.bg-btn {
  background: url('../../libs/assets/img/pic-cut/bg-btn-hover.png');
  background-color: #3b9cff;
}

.text-edit-cont .font-tools .li-size-icons {
  width: 20px;
  height: 20px;
  margin: 5px auto;
  display: block;
}

.text-edit-cont .font-tools .li-size-icons.add-icon {
  background: url('../../libs/assets/img/pic-cut/add-icon.png');
}
.text-edit-cont .font-tools .li-size-icons.reduce-icon {
  background: url('../../libs/assets/img/pic-cut/reduce-icon.png');
}

.text-edit-cont .font-tools .size-icon {
  width: 20px;
  height: 20px;
  margin-top: 5px;
  display: block;
  background: url('../../libs/assets/img/pic-cut/size-icon.png');
}

.text-edit-cont .font-tools .height-icon {
  width: 20px;
  height: 20px;
  margin-top: 5px;
  display: block;
  background: url('../../libs/assets/img/pic-cut/height-icon.png');
}
.text-edit-cont .font-tools .pull-icon {
  position: absolute;
  top: 10px;
  right: 5px;
  z-index: 2;
  display: block;
  width: 20px;
  height: 20px;
  background: url('../../libs/assets/img/pic-cut/select-icon.png') no-repeat;
}

.text-edit-cont .align-format .icons {
  width: 20px;
  height: 20px;
  margin: 5px auto;
  display: block;
}

.text-edit-cont .align-format-btn.font-weight .icons {
  background: url('../../libs/assets/img/pic-cut/prev-btn.png');
}
.text-edit-cont .align-format-btn.font-italic .icons {
  background-position: 0 -20px;
}
.text-edit-cont .align-format-btn.font-line .icons {
  background-position: 0 -40px;
}
.text-edit-cont .align-format-btn.align-left .icons {
  background-position: 0 -60px;
}
.text-edit-cont .align-format-btn.align-center .icons {
  background-position: 0 -80px;
}
.text-edit-cont .align-format-btn.align-right .icons {
  background-position: 0 -100px;
}
.text-edit-cont .align-format-btn.font-default .icons {
  background-position: 0 -120px;
}

.text-edit-cont .color-table-li .color-table-icons {
  width: 20px;
  height: 20px;
  margin-right: 5px;
  background: url('../../libs/assets/img/pic-cut/color-table-icons-hover.png')
    no-repeat;
}
.text-edit-cont .color-table-li.active .color-table-icons {
  width: 20px;
  height: 20px;
  margin-right: 5px;
  background: url('../../libs/assets/img/pic-cut/color-table-icons.png')
    no-repeat;
}

.text-edit-cont .fix-select-li .color-select.none-select {
  background: url('../../libs/assets/img/pic-cut/none-select.png') no-repeat;
}
.text-edit-cont .fix-select-li .color-select.other-select {
  background: url('../../libs/assets/img/pic-cut/other-select.png') no-repeat;
}

.text-edit-cont .width-edit-box li.width-add-btn {
  border-left: solid 1px #dcdfe6;
  background: url('../../libs/assets/img/pic-cut/width-add-btn.png') no-repeat;
  background-position: center center;
}
.text-edit-cont .width-edit-box li.width-reduce-btn {
  border-left: solid 1px #dcdfe6;
  border-top: solid 1px #dcdfe6;
  background: url('../../libs/assets/img/pic-cut/width-reduce-btn.png')
    no-repeat;
  background-position: center center;
}

.crop-edit-cont .icon-li .tools-icon {
  width: 22px;
  height: 22px;
  margin-top: 4px;
  margin-left: auto;
  margin-right: auto;
  display: block;
}

.crop-edit-cont .icon-li .tools-icon.cut-icon {
  background: url('../../libs/assets/img/pic-cut/cut-icon.png');
}
.crop-edit-cont .icon-li .tools-icon.cancel-icon {
  background: url('../../libs/assets/img/pic-cut/cancel-icon.png');
}
.crop-edit-cont .icon-li .tools-icon.zoom-add-icon {
  background: url('../../libs/assets/img/pic-cut/zoom-add-icon.png');
}
.crop-edit-cont .icon-li .tools-icon.zoom-reduce-icon {
  background: url('../../libs/assets/img/pic-cut/zoom-reduce-icon.png');
}
.crop-edit-cont .icon-li .tools-icon.rotate-left-icon {
  background: url('../../libs/assets/img/pic-cut/rotate-left-icon.png');
}
.crop-edit-cont .icon-li .tools-icon.rotate-right-icon {
  background: url('../../libs/assets/img/pic-cut/rotate-right-icon.png');
}
.crop-edit-cont .icon-li .tools-icon.reset-icon {
  background: url('../../libs/assets/img/pic-cut/reset-icon.png');
}
.crop-edit-cont .icon-li.active .tools-icon.cut-icon,
.crop-edit-cont .icon-li:hover .tools-icon.cut-icon {
  background: url('../../libs/assets/img/pic-cut/cut-icon-hover.png');
}
.crop-edit-cont .icon-li.active .tools-icon.cancel-icon,
.crop-edit-cont .icon-li:hover .tools-icon.cancel-icon {
  background: url('../../libs/assets/img/pic-cut/cancel-icon-hover.png');
}
.crop-edit-cont .icon-li.active .tools-icon.zoom-add-icon,
.crop-edit-cont .icon-li:hover .tools-icon.zoom-add-icon {
  background: url('../../libs/assets/img/pic-cut/zoom-add-icon-hover.png');
}
.crop-edit-cont .icon-li.active .tools-icon.zoom-reduce-icon,
.crop-edit-cont .icon-li:hover .tools-icon.zoom-reduce-icon {
  background: url('../../libs/assets/img/pic-cut/zoom-reduce-icon-hover.png');
}
.crop-edit-cont .icon-li.active .tools-icon.rotate-left-icon,
.crop-edit-cont .icon-li:hover .tools-icon.rotate-left-icon {
  background: url('../../libs/assets/img/pic-cut/rotate-left-icon-hover.png');
}
.crop-edit-cont .icon-li.active .tools-icon.rotate-right-icon,
.crop-edit-cont .icon-li:hover .tools-icon.rotate-right-icon {
  background: url('../../libs/assets/img/pic-cut/rotate-right-icon-hover.png');
}
.crop-edit-cont .icon-li.active .tools-icon.reset-icon,
.crop-edit-cont .icon-li:hover .tools-icon.reset-icon {
  background: url('../../libs/assets/img/pic-cut/reset-icon-hover.png');
}

.crop-edit-cont .select-box .select-icon {
  position: absolute;
  top: 9px;
  right: 2px;
  z-index: 2;
  width: 15px;
  height: 8px;
  display: block;
  background: url('../../libs/assets/img/pic-cut/select-icon.png') no-repeat
    center center;
}

.img-editor-body .tools-bar-li .tools-icons {
  width: 22px;
  height: 22px;
  display: block;
  margin: 4px auto 2px;
}

.img-editor-body .tools-bar-li .tools-icons.text-icon {
  background: url('../../libs/assets/img/pic-cut/text-icon.png') no-repeat;
}
.img-editor-body .tools-bar-li .tools-icons.img-icon {
  background: url('../../libs/assets/img/pic-cut/img-icon.png') no-repeat;
}
.img-editor-body .tools-bar-li .tools-desc {
  display: block;
  text-align: center;
  font-size: 12px;
  color: #7f8b99;
}
.img-editor-body .tools-bar-li.active {
  background-color: #3b9cff;
}
.img-editor-body .tools-bar-li.active .tools-icons.text-icon,
.img-editor-body .tools-bar-li:hover .tools-icons.text-icon {
  background-color: #3b9cff;
  background: url('../../libs/assets/img/pic-cut/text-icon-hover.png') no-repeat;
}
.img-editor-body .tools-bar-li.active .tools-icons.img-icon,
.img-editor-body .tools-bar-li:hover .tools-icons.img-icon {
  background-color: #3b9cff;
  background: url('../../libs/assets/img/pic-cut/img-icon-hover.png') no-repeat;
}
.img-editor-body .tools-bar-li.active .tools-desc,
.img-editor-body .tools-bar-li:hover .tools-desc {
  background: #3b9cff;
  color: #fff;
}
.img-editor-hd .close-btn .close-icon {
  width: 14px;
  height: 14px;
  margin-top: 13px;
  margin-left: auto;
  margin-right: auto;
  display: block;
  background: url('../../libs/assets/img/pic-cut/close-btn.png') no-repeat;
  background-position: 0 0;
}
.img-editor-hd .close-btn .close-icon:hover{
  background: url('../../libs/assets/img/pic-cut/close-btn-hover.png') no-repeat;
}
</style>
