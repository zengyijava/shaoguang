<template>
  <el-dialog :visible.sync="dialogTableVisible" width="1024px" @open="openDialog" :append-to-body="isAppendToBody">
    <div>
      <div id="img-editor-bg" class="img-editor-bg"></div>
      <div id="img-editor-layer" class="img-editor-layer">
        <div class="img-editor-hd">
          <h2 class="title">{{ $t('media.onlineImgEdit') }}</h2>
          <div class="close-btn" @click="cancelPanel">
            <i class="close-icon"></i>
          </div>
        </div>
        <div class="img-editor-body editor-clear" @keyup.delete="test">
          <div class="editor-show-bd editor-fl">
            <div class="img-show-cont editor-fl">
              <div class="show-box" :style="{width:imgWidth+'px',height:imgHeight+'px'}">
                <div class="edit-container" id="editorContainer">
                  <img :src="bkImgSrc" width="100%" height="100%" draggable="false" />
                </div>
              </div>
            </div>
            <div class="img-edit-cont editor-fl">
              <div class="crop-edit-cont">
                <h4 class="title">{{ $t('media.imgEdit') }}</h4>
                <div class="cut-cont editor-clear">
                  <ul class="cut-tools-bar editor-fl">
                    <li class="icon-li">
                      <i class="tools-icon cut-icon" @click="startCrop"></i>
                    </li>
                    <li class="icon-li">
                      <i class="tools-icon cancel-icon" @click="clearCrop"></i>
                    </li>
                    <li class="icon-li">
                      <i class="tools-icon zoom-add-icon" @click="changeScale(1)"></i>
                    </li>
                    <li class="icon-li">
                      <i class="tools-icon zoom-reduce-icon" @click="changeScale(-1)"></i>
                    </li>
                    <li class="icon-li">
                      <i class="tools-icon rotate-left-icon" @click="rotateLeft"></i>
                    </li>
                    <li class="icon-li">
                      <i class="tools-icon rotate-right-icon" @click="rotateRight"></i>
                    </li>
                  </ul>
                  <div class="cut-cont-show editor-fl">
                    <div class="crop-container">
                      <vue-cropper ref="cropper" :img="cropperUrl" outputType="png" :autoCrop='autoCrop' :centerBox="true" :autoCropWidth="parseInt(cropperWidth)" :autoCropHeight="parseInt(cropperHeight)" :full="true"></vue-cropper>
                    </div>
                    <div class="cut-show-ft editor-clear">
                      <p class="cut-show-info editor-fl">{{ $t('media.imgSize') }}{{(cropperSize/1024).toFixed(2)+'kb'}} {{cropperWidth}} * {{cropperHeight}}</p>
                      <div class="select-box editor-fr">
                        <p class="select-result" @click="isScale=true">{{imgScale}}%</p>
                        <i class="select-icon" @click="isScale=true"></i>
                        <ul class="select-list" :style="{display:isScale?'block':'none'}">
                          <li class="active" @click="setScale('30')" data-val="30">30%</li>
                          <li data-val="60" @click="setScale('60')">60%</li>
                          <li data-val="90" @click="setScale('90')">90%</li>
                          <li data-val="100" @click="setScale('100')">100%</li>
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
          <ul class="editor-tools-bar editor-fr">
            <li class="tools-bar-li active">
                <i class="tools-icons img-icon"></i>
                <span class="tools-desc">{{ $t('media.img') }}</span>
            </li>
          </ul>
          <div class="buttons">
            <input class="img-close-btn editor-fl" type="button" :value="$t('media.cancel')" @click="cancelPanel">
            <input class="img-save-btn editor-fl" type="button" :value="$t('media.keep')" @click="savePanel">
          </div>
        </div>
      </div>
    </div>
  </el-dialog>
</template>
<script>
import { mapGetters, mapActions, mapMutations } from 'vuex'
import VueCropper from '../../libs/components/vue-cropper/index'
import ImagePicker from '../../libs/components/ImagePicker'
import utils from '../../libs/utils'
import AJAXURL from '../../libs/ajax.address'
export default {
  components: {
    VueCropper, ImagePicker
  },
  props: ['imageSrc', 'open'],
  computed: {
    ...mapGetters(['background']),
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
      imgWidth: 562,
      imgHeight: 562,
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
      width: 0,
      height: 0,
      currentSize: [12, 14, 16, 18, 24, 32, 48],
      sizeIndex: 1,
      top: 0,
      cropperSize: '',
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
      colorPicker: ''
    }
  },
  watch: {
    open (val) {
      this.dialogTableVisible = val
    }
  },
  methods: {
    ...mapActions(['cutImage']),
    ...mapMutations('media', ['addImageText', 'addInnerImage', 'clearTextEitable']),
    closePanel () {
      this.dailogElement = {}
      if (!this.saveCropper) {
        this.cropperUrl = this.background.src
      }
      this.$emit('closePanel')
    },
    cancelPanel () {
      for (let item in this.initData) {
        this.background[item] = this.initData[item]
      }
      this.closePanel()
    },
    openDialog () {
      this.initData = JSON.parse(JSON.stringify(this.background))
      this.dailogElement = this.imgSrc
      this.bkImgSrc = this.background.src
      // this.cropperSize = this.element.image.size
      this.cropperWidth = '304px'
      this.cropperHeight = '304px'
      this.lastImg = this.background.src
      this.cropperUrl = this.bkImgSrc
      let openImg = new Image()
      openImg.src = this.bkImgSrc
    },
    handleUploadInnerImageSuccess (event) {
      let payload = {
        image: event,
        element: this.background.src
      }
      this.addInnerImage(payload)
    },
    handleUploadSuccess (res, file) {
      this.isChange = true
      if (res.state === 'SUCCESS') {
        this.saveCropper = false
        this.hasCropper = false
        // if (this.element.type === 'text') {
        //   this.lastImg.image = res
        // } else {
        this.lastImg = res.url
        // }
        this.cropperUrl = res.url
        this.cropperSize = res.size
        this.cropperWidth = res.width
        this.cropperHeight = res.height
      } else {
        this.$message.error(res.state)
      }
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
        this.cropperUrl = this.bkImgSrcsrc
        this.cropperWidth = this.bkImgSrcwidth
        this.cropperHeight = this.bkImgSrcheight
        // this.cropperSize = this.lastImg.image.size
      }
    },
    delTextImg (index) {
      this.dailogElement.textEditable.splice(index, 1)
    },
    setScale (scale) {
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
      if (this.sizeIndex < 6) {
        this.sizeIndex += 1
      }
      this.currentElement.style.fontSize = this.currentSize[this.sizeIndex] + 'px'
    },
    reduceTextSize () {
      if (this.sizeIndex > 0) {
        this.sizeIndex -= 1
      }
      this.currentElement.style.fontSize = this.currentSize[this.sizeIndex] + 'px'
    },
    setElementStyle (style, type) {
      this.currentElement.style[type] = style
      switch (type) {
        case 'fontFamily':
          this.isFontFamily = false
          break
        case 'fontSize':
          this.isFontSize = false
          break
        case 'lineHeight':
          this.isLineHeight = false
          this.currentElement.h = parseInt(style.replace('px', '')) + 8
          break
        default:
          break
      }
    },
    savePanel () {
      let _this = this
      let bgImageUrl
      if (!this.hasCropper) {
        // 输出
        this.saveCropper = true
        this.hasCropper = true
        this.$refs.cropper.getCropData((data) => {
          bgImageUrl = this.bkImgSrc
          let params = {
            clarity: this.imgScale,
            src: bgImageUrl,
            data: data
          }
          let formParam = utils.transformRequest(params)
          this.$post(AJAXURL.IMG_CROPPER, formParam).then(res => {
            if (res.state === '0') {
              this.cropperUrl = res.path
              this.bkImgSrc = res.path
              // this.element.src = res.path
              this.cropperSize = res.size
              this.cropperWidth = res.width
              this.cropperHeight = res.height
              // 图片真实宽度是否大于等于真实高度
              if (this.cropperWidth >= this.cropperHeight) {
                // 图片真实宽是否大于等于裁剪容器宽度
                if (this.cropperWidth >= this.imgContainerMaxWidth) {
                  this.imgWidth = this.imgContainerMaxWidth
                  this.proportion = this.previewContW / this.imgContainerMaxWidth
                  this.imgHeight = this.cropperHeight * (this.imgContainerMaxWidth / this.cropperWidth)
                } else {
                  this.imgWidth = this.cropperWidth
                  this.imgHeight = this.cropperHeight
                  this.proportion = this.previewContW / this.cropperWidth
                }
              } else {
                // 图片真实高度是否大于等于裁剪器容器高度
                if (this.cropperHeight >= this.imgContainerMaxHeight) {
                  this.imgHeight = this.imgContainerMaxHeight
                  this.imgWidth = this.cropperWidth * (this.imgContainerMaxHeight / this.cropperHeight)
                  this.proportion = this.previewContW / this.imgWidth
                } else {
                  this.imgWidth = this.cropperWidth
                  this.imgHeight = this.cropperHeight
                  this.proportion = this.previewContW / this.cropperWidth
                }
              }
              this.background.src = res.path
              _this.closePanel()
            }
          })
        })
      } else {
        this.background.src = this.bkImgSrc
        _this.closePanel()
      }
    },
    startCrop () {
      this.crap = true
      this.$refs.cropper.startCrop()
    },
    setFixedNumber (fixed) {
      this.fixedNumber = fixed
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
    finish (type) {
      // 输出
      let _this = this
      let bgImageUrl
      this.saveCropper = true
      this.hasCropper = true
      _this.$refs.cropper.getCropData((data) => {
        bgImageUrl = this.background.src
        let params = {
          clarity: this.imgScale,
          src: bgImageUrl,
          data: data
        }
        let formParam = utils.transformRequest(params)
        _this.$post(AJAXURL.IMG_CROPPER, formParam).then(res => {
          if (res.state === '0') {
            _this.cropperUrl = res.path
            _this.bkImgSrc = res.path
            // _this.element.src = res.path
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
      element.h = event
    }
  }
}
</script>
<style lang="less">
@import '../../libs/assets/less/imageLayer.less';
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
  background: url('../../libs/assets/img/pic-cut/color-table-icons.png')
    no-repeat;
}
.text-edit-cont .color-table-li.active .color-table-icons {
  width: 20px;
  height: 20px;
  margin-right: 5px;
  background: url('../../libs/assets/img/pic-cut/color-table-icons-hover.png')
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
  color: #b9c3cf;
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
