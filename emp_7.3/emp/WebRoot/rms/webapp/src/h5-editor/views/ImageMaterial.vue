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
    <div class="image-panel-body">
      <ul class="clearfix" style="padding-bottom: 50px">
        <li v-for="(item,index) in imageList" :key="index" :class="{active:isBatch&&item.active}">
          <div class="del-btn" v-show="!isBatch">
            <i @click="deleteImg(item.id)" class="del-icon"></i>
          </div>
          <span class="checked-item"></span>
          <img @click="dealImage(item,index,!item.active)" :src="item.url.slice(0,-4)+item.url.slice((item.url.length-4),item.url.length)" style="object-fit: contain;" />
          <!-- <img :src="item.url" /> -->
        </li>
      </ul>
      <!-- 加载更多 -->
      <div class="load-more" v-infinite-scroll="loadMore" infinite-scroll-disabled="busy" infinite-scroll-distance="0">
      </div>
    </div>
  </div>
</template>
<script>
import { mapGetters, mapMutations, mapActions } from 'vuex'
import MaterialPanelHeader from '../components/MaterialPanelHeader'
import ImageMaterialPicker from './ImageMaterialPicker'
import utils from '../../libs/utils'
import actions from '../../libs/api'
import infiniteScroll from 'vue-infinite-scroll'

export default {
  components: {ImageMaterialPicker, MaterialPanelHeader},
  created () {
    if (this.userInfo === undefined) {
      this.getUserInfos()
    }
  },
  mounted () {
    let param = {}
    param.userId = this.userInfo.userId
    param.page = 0
    param.size = 18
    param.type = 2
    this.getFodder(param)
  },
  computed: mapGetters(['imageList', 'userInfo', 'element', 'elements', 'addType', 'background', 'count']),
  data () {
    return {
      isBatch: false,
      checkedAll: false,
      busy: false,
      currentPage: 1,
      pageSize: 16,
      loading: true
    }
  },
  methods: {
    ...mapMutations(['pushImage', 'selectElement', 'updateCount']),
    ...mapActions(['deleteFodder', 'getFodder', 'getUserInfos']),
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
    // 添加图片
    addImage (payload) {
      if (this.addType === 'bgImage') {
        this.background.src = payload.url
        this.background.crop.style.width = payload.width
        this.background.crop.style.height = payload.height
      } else {
        this.updateCount()
        const image = {
          locked: false,
          visible: true,
          active: true,
          x: 10,
          y: 20,
          z: this.elements.length,
          type: 'image',
          tag: this.$t('navBar.img') + this.count,
          src: payload.url,
          ratio: payload.radio,
          size: payload.foSize,
          width: payload.width,
          height: payload.height,
          w: utils.getFitWidth(payload),
          h: utils.getFitHeight(payload),
          action: '0',
          url: '',
          borderRadius: 0
        }
        this.elements.push(image)
        this.selectElement(image)
      }
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
      // this.$emit('closePanel')
    },
    getActiveElements () {
      return this.elements.find(item => item.active && item.type === 'image')
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
        if (this.getActiveElements()) {
          this.replaceImage(item)
        } else {
          this.addImage(item)
        }
      }
    },
    loadMore () {
      this.currentPage++
      let _self = this
      let _getListParams = {
        userId: this.userInfo.userId,
        page: this.currentPage,
        size: 0,
        type: 2
      }
      // 获取列表
      actions.getFodder(_getListParams, response => {
        _self.pageTotal = response.data.data.totalPage
        // 判断是否显示加载更多
        if (_self.currentPage > _self.pageTotal) {
          _self.busy = true
          _self.loading = false
        } else {
          _self.busy = false
          _self.loading = true
        }
        if (_self.loading) {
          _self.imageList.push(...response.data.data.list)
        }
      }, errMsg => {
        _self.$message.error(errMsg)
        _self.listLoad = false
      })
    }
  },
  directives: {infiniteScroll}
}
</script>

<style lang="less" scoped>
@import '../../libs/assets/less/variables';
.image-panel-body {
  padding: 0 18px;
  width: 100%;
  ul li {
    position: relative;
    display: block;
    float: left;
    height: 120px;
    width: 120px;
    margin-right: 10px;
    margin-bottom: 10px;
    background-color: #e5e6e8;
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
        background: url('../../libs/assets/img/pic-cut/close-btn.png') no-repeat;
        background-position: 0 0;
        display: block;
        cursor: pointer;
      }
      .del-icon:hover {
        background: url('../../libs/assets/img/pic-cut/close-btn-hover.png') no-repeat;
      }
    }
    &:hover {
      .del-btn {
        display: block;
      }
    }
    &.active {
      border: solid 1px #3b9cff;
      width: 118px;
      height: 118px;
      .checked-item {
        display: block;
        position: absolute;
        height: 26px;
        width: 26px;
        top: 0;
        left: 0;
        background: url('../assets/img/btn_choice.png') no-repeat;
      }
    }
    img {
      width: 100%;
      height: 100%;
    }
  }
}
</style>
