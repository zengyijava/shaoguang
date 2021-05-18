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
                        <p class="select-result" @click="isScale=true">{{scaleText}}</p>
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
          <ul class="editor-tools-bar editor-fr">
            <li class="tools-bar-li active">
              <i class="tools-icons img-icon"></i>
              <span class="tools-desc">{{ $t('media.img') }}</span>
            </li>
          </ul>
          <div class="buttons">
            <el-button class="img-close-btn editor-fl" @click="cancelPanel" plain>{{ $t('media.cancel') }}</el-button>
            <el-button class="img-save-btn editor-fl" @click="savePanel" :loading="saveLoading" plain>{{ $t('media.keep') }}</el-button>
          </div>
        </div>
      </div>
    </div>
  </el-dialog>
</template>
<script>
import { mapGetters, mapActions, mapMutations } from 'vuex'
import VueCropper from '../components/vue-cropper'
import ImagePicker from '../components/ImagePicker'
import utils from '../utils'
import AJAXURL from '../ajax.address'
export default {
  components: {
    VueCropper, ImagePicker
  },
  props: ['imageSrc', 'open'],
  computed: {
    ...mapGetters(['element']),
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
      saveLoading: false,
      saveCropper: false,
      imgWidth: 0,
      imgHeight: 0,
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
      scaleText: this.$t('cardImgSetting.masterImg'),
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
      previewContW: 240,
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
      this.hasCropper = false
      this.initData = JSON.parse(JSON.stringify(this.element))
      if (this.element.type === 'text') {
        this.dailogElement = this.element.image
        this.bkImgSrc = this.element.image.src
        this.cropperSize = this.element.image.size
        this.cropperWidth = this.element.image.width
        this.cropperHeight = this.element.image.height
        this.lastImg = this.element.image
      } else {
        this.dailogElement = this.element
        this.bkImgSrc = this.element.src
        this.cropperSize = this.element.size
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
          } else {
            this.imgWidth = openImg.width
          }
          this.imgHeight = this.imgWidth / scale
        } else {
          if (openImg.height > this.imgContainerMaxHeight) {
            this.imgHeight = this.imgContainerMaxHeight
          } else {
            this.imgHeight = openImg.height
          }
          this.imgWidth = this.imgHeight * scale
        }
        this.$refs.cropper.checkedImg()
      }, 200)
    },
    handleUploadInnerImageSuccess (event) {
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
        this.saveCropper = false
        this.hasCropper = false
        if (this.element.type === 'text') {
          this.lastImg.image = res
        } else {
          this.lastImg = res
        }
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
    },
    setScale (scale, scaleTet) {
      this.scaleText = scaleTet
      this.imgScale = scale
      this.isScale = false
    },
    savePanel () {
      let _this = this
      let bgImageUrl
      let [_elementW, _elementH] = [0, 0]

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
          this.$post(AJAXURL.IMG_CROPPER, formParam).then(res => {
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

              // 判断裁剪后的图片是否大于编码面板宽度

              if (_this.cropperWidth >= _this.previewContW && _this.cropperWidth >= _this.cropperHeight) {
                _this.proportion = _this.previewContW / _this.cropperWidth
                _elementW = _this.previewContW
                _elementH = _this.cropperHeight * _this.proportion
              } else if (_this.cropperWidth < _this.cropperHeight && _this.cropperHeight >= _this.previewContW) {
                _this.proportion = _this.previewContW / _this.cropperHeight
                _elementW = _this.cropperWidth * _this.proportion
                _elementH = _this.previewContW
              } else {
                _this.proportion = 0
                _elementW = _this.cropperWidth
                _elementH = _this.cropperHeight
              }

              if (_this.element.type === 'text') {
                _this.element.image.w = _elementW
                _this.element.image.h = _elementH
                _this.element.image.size = _this.cropperSize
                _this.element.image.src = _this.bkImgSrc
                _this.element.image.width = _this.cropperWidth
                _this.element.image.height = _this.cropperHeight
                _this.element.image.scale = _this.proportion
              } else {
                _this.element.w = _elementW
                _this.element.h = _elementH
                _this.element.size = _this.cropperSize
                _this.element.src = _this.bkImgSrc
                _this.element.width = _this.cropperWidth
                _this.element.height = _this.cropperHeight
                _this.element.scale = _this.proportion
              }
              _this.closePanel()
            }
          })
        })
      } else {
        // 判断裁剪后的图片是否大于编码面板宽度
        if (this.cropperWidth >= this.previewContW && this.cropperWidth >= this.cropperHeight) {
          this.proportion = this.previewContW / this.cropperWidth
          _elementW = this.previewContW
          _elementH = this.cropperHeight * this.proportion
        } else if (this.cropperWidth < this.cropperHeight && this.cropperHeight >= this.previewContW) {
          this.proportion = this.previewContW / this.cropperHeight
          _elementW = this.cropperWidth * this.proportion
          _elementH = this.previewContW
        } else {
          this.proportion = 0
          _elementW = this.cropperWidth
          _elementH = this.cropperHeight
        }

        if (this.element.type === 'text') {
          this.element.image.w = _elementW
          this.element.image.h = _elementH
          this.element.image.size = this.cropperSize
          this.element.image.src = this.bkImgSrc
          this.element.image.width = this.cropperWidth
          this.element.image.height = this.cropperHeight
          this.element.image.scale = this.proportion
        } else {
          // 图片真实高度是否大于等于裁剪器容器高度
          this.element.w = _elementW
          this.element.h = _elementH
          this.element.size = this.cropperSize
          this.element.src = this.bkImgSrc
          this.element.width = this.cropperWidth
          this.element.height = this.cropperHeight
          this.element.scale = this.proportion
        }
        this.closePanel()
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
      this.saveLoading = true
      _this.$refs.cropper.getCropData((data) => {
        if (_this.element.type === 'text') {
          bgImageUrl = _this.element.image.src
        } else {
          bgImageUrl = _this.element.src
        }
        let params = {
          clarity: _this.imgScale,
          src: bgImageUrl,
          data: data
        }
        let formParam = utils.transformRequest(params)
        _this.$post(AJAXURL.IMG_CROPPER, formParam).then(res => {
          if (res.state === '0') {
            _this.saveLoading = false
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
.img-editor-hd .close-btn .close-icon:hover {
  background: url('../../libs/assets/img/pic-cut/close-btn-hover.png') no-repeat;
}
</style>
