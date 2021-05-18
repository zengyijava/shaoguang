<template>
  <div>
    <MaterialPanelHeader :isBatch="isBatch" :checkedAll="checkedAll" v-model="checkedAll"
      @delete="deleteImg" @batch="batchImg" @input="setCheckAll">
      <ImageMaterialPicker slot @on-success="handleUploadSuccess" class="fr">
        <el-tooltip effect="dark" :content="$t('H5.up_img_hint')">
          <el-button type="primary">+ {{ $t('H5.up_img_btn') }}</el-button>
        </el-tooltip>
      </ImageMaterialPicker>
    </MaterialPanelHeader>
    <div class="infinite-scroll" v-infinite-scroll="loadMore" infinite-scroll-disabled="busy">
      <ul v-show="imageList.length > 0">
        <li v-for="(item,index) in imageList" :key="index" :class="{active:isBatch&&item.active}">
          <div class="del-btn" v-show="!isBatch">
            <i @click="deleteImg(item.id)" class="del-icon"></i>
          </div>
          <span class="checked-item"></span>
          <img @click="dealImage(item,index,!item.active)" :src="item.url.slice(0,-4)+item.url.slice((item.url.length-4),item.url.length)" style="object-fit: contain;" />
        </li>
      </ul>
      <div class="blank-result" v-show="imageList.length === 0">
        <img src="../assets/img/private_media_no_data.png">
        {{$t('public.noPictureUpload')}}
      </div>
      <div class="loading" v-show="!busy">
        <i class="el-icon-loading"></i>{{$t('public.loading')}}
      </div>
    </div>
  </div>
</template>
<script>
import { mapGetters, mapMutations, mapActions } from 'vuex'
import MaterialPanelHeader from '../components/MaterialPanelHeader'
import ImageMaterialPicker from '../components/ImageMaterialPicker'
import utils from '../utils'
import infiniteScroll from 'vue-infinite-scroll'

export default {
  components: {ImageMaterialPicker, MaterialPanelHeader},
  computed: mapGetters(['imageList', 'userId', 'elements', 'addType', 'busy']),
  data () {
    return {
      isBatch: false,
      checkedAll: false,
      param: {
        page: 1,
        size: 18,
        type: 2
      }
    }
  },
  methods: {
    ...mapMutations(['pushImage']),
    ...mapActions(['deleteFodder', 'getFodder']),
    handleUploadSuccess (data) {
      if (data) {
        this.checkedAll = false
        this.pushImage(data)
      }
    },
    setCheckAll () {
      if (this.checkedAll) {
        this.imageList.map(item => {
          item.active = true
        })
      } else {
        this.imageList.map(item => {
          item.active = false
        })
      }
    },
    batchImg () {
      this.isBatch = !this.isBatch
      this.checkedAll = true
      this.imageList.map(item => {
        item.active = true
      })
    },
    deleteImg (id) {
      this.$confirm(this.$t('H5.img_dele_hint'), this.$t('H5.hint_title'), {
        confirmButtonText: this.$t('H5.hint_sure'),
        cancelButtonText: this.$t('H5.hint_cancel'),
        type: 'warning'
      }).then(() => {
        let param = {}
        let fodderIds = []
        if (!this.isBatch) {
          fodderIds.push(id)
        } else {
          this.imageList.map(item => {
            if (item.active) {
              fodderIds.push(item.id)
            }
          })
        }
        param.fodderIds = fodderIds
        this.deleteFodder(param)
      }).catch(() => {
      })
    },

    // 替换图片
    replaceImage (payload) {
      if (this.addType === 'bgImage') {
        this.background.src = payload.url
      } else {
        const image = {
          src: payload.url,
          ratio: payload.ratio,
          size: payload.foSize,
          width: payload.width,
          height: payload.height,
          w: utils.getFitWidth({ width: payload.width, height: payload.height }),
          h: utils.getFitHeight({ width: payload.width, height: payload.height })
        }
        this.elements.find((item) => {
          if (item.active && item.type === 'image') {
            item.src = image.src
            item.ratio = image.ratio
            item.size = image.size
            item.width = image.width
            item.height = image.height
            item.w = image.w
            item.h = image.h
          }
        })
      }
    },
    dealImage (item, index, active) {
      if (this.isBatch) {
        item.active = active
        let newItem = JSON.parse(JSON.stringify(item))
        this.$set(this.imageList, index, newItem)
        if (!active) {
          this.checkedAll = false
        } else {
          let allChecked = this.imageList.filter(item => {
            return item.active === false
          })
          if (allChecked.length === 0) {
            this.checkedAll = true
          }
        }
      } else {
        this.$emit('addImage', item)
      }
    },
    loadMore () {
      this.param.userId = this.userId
      this.getFodder(this.param)
    }
  },
  directives: {infiniteScroll}
}
</script>

<style lang="less" scoped>
  @import '../assets/less/image-material';
ul {
  li {
    .del-btn {
      position: absolute;
      top: 0;
      left: 0;
      width: 100%;
      height: 30px;
      z-index: 10;
      display: none;
      i.del-icon {
        width: 14px;
        height: 14px;
        float: right;
        margin-top: 8px;
        margin-right: 8px;
        background: url('../assets/img/pic-cut/close-btn.png') no-repeat;
        background-position: 0 0;
        display: block;
        cursor: pointer;
      }
      .del-icon:hover {
        background: url('../assets/img/pic-cut/close-btn-hover.png') no-repeat;
      }
    }
    &:hover {
      .del-btn {
        display: block;
      }
    }
  }
}
</style>
