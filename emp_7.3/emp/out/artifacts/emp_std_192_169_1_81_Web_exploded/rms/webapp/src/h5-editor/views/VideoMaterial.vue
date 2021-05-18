<template>
  <div>
    <MaterialPanelHeader :isBatch="isBatch" :checkedAll="checkedAll" v-model="checkedAll"
      @delete="deleteVideo" @batch="batchVideo" @input="setCheckAll">
      <VideoMaterialPicker slot @on-success="handleUploadSuccess" class="fr">
        <el-tooltip effect="dark" :content="$t('H5.up_video_hint')"
          placement="bottom">
          <el-button type="primary">+ {{ $t('H5.up_video_btn') }}</el-button>
        </el-tooltip>
      </VideoMaterialPicker>
    </MaterialPanelHeader>
    <div class="video-panel-body">
      <ul>
        <li v-for="(item,index) in videoList" :key="index" :class="{active:isBatch&&item.active}">
          <div class="del-btn" v-show="!isBatch">
            <i @click="deleteVideo(item.id)" class="del-icon"></i>
          </div>
          <span v-show="isBatch" class="checked-item"></span>
          <video @click="dealItem(item,index,!item.active)" width="180" :src="item.url"></video>
        </li>
      </ul>
    </div>
  </div>
</template>
<script>
import {mapGetters, mapMutations, mapActions} from 'vuex'
import utils from '../../libs/utils'
import MaterialPanelHeader from '../components/MaterialPanelHeader'
import VideoMaterialPicker from './VideoMaterialPicker'

export default {
  components: {MaterialPanelHeader, VideoMaterialPicker},
  created () {
    if (this.userInfo === undefined) {
      this.getUserInfos()
    }
  },
  mounted () {
    let param = {}
    param.userId = this.userInfo.userId
    param.page = 0
    param.size = 100
    param.type = 3
    this.getFodder(param)
  },
  computed: mapGetters(['videoList', 'userInfo', 'element', 'elements', 'count']),
  data () {
    return {
      isBatch: false,
      checkedAll: false,
      fodderIds: []
    }
  },
  methods: {
    ...mapMutations(['pushVideo', 'selectElement', 'updateCount']),
    ...mapActions(['deleteFodder', 'getFodder', 'getUserInfos']),
    handleUploadSuccess (data) {
      if (data) {
        this.checkedAll = false
        this.pushVideo(data)
      }
    },
    setCheckAll () {
      if (this.checkedAll) {
        this.videoList.map(item => {
          item.active = true
        })
      } else {
        this.videoList.map(item => {
          item.active = false
        })
      }
    },
    batchVideo () {
      this.isBatch = !this.isBatch
      this.checkedAll = true
      this.videoList.map(item => {
        item.active = true
      })
    },
    deleteVideo (id) {
      this.$confirm(this.$t('H5.video_dele_hint'), this.$t('H5.hint_title'), {
        confirmButtonText: this.$t('H5.hint_sure'),
        cancelButtonText: this.$t('H5.hint_cancel'),
        type: 'warning'
      }).then(() => {
        let param = {}
        let fodderIds = []
        if (!this.isBatch) {
          fodderIds.push(id)
        } else {
          this.videoList.map(item => {
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
    // 添加视频
    addVideo (payload) {
      this.updateCount()
      const media = {
        locked: false,
        visible: true,
        active: true,
        x: 40,
        y: 20,
        z: this.elements.length,
        w: utils.getFitWidth(payload),
        h: utils.getFitHeight(payload),
        type: 'video',
        tag: this.$t('navBar.video') + this.count,
        src: payload.url,
        original: payload.original,
        duration: payload.duration,
        size: payload.foSize,
        fistFramePath: payload.fistFramePath
      }
      this.elements.push(media)
      this.selectElement(media)
    },
    // 替换视频
    replaceVideo (payload) {
      const media = {
        src: payload.url,
        duration: payload.duration,
        size: payload.foSize,
        fistFramePath: payload.fistFramePath
      }
      this.elements.find((item) => {
        if (item.active && item.type === 'video') {
          item.src = media.src
          item.duration = media.duration
          item.size = media.foSize
          item.fistFramePath = media.fistFramePath
        }
      })
    },
    getActiveElements () {
      return this.elements.find(item => item.active && item.type === 'video')
    },
    dealItem (item, index, active) {
      if (this.isBatch) {
        item.active = active
        let newItem = JSON.parse(JSON.stringify(item))
        this.$set(this.videoList, index, newItem)
        if (!active) {
          this.checkedAll = false
        } else {
          let allChecked = this.videoList.filter(item => {
            return item.active === false
          })
          if (allChecked.length === 0) {
            this.checkedAll = true
          }
        }
      } else {
        if (this.getActiveElements()) {
          this.replaceVideo(item)
        } else {
          this.addVideo(item)
        }
      }
    }
  }
}
</script>

<style lang="less" scoped>
  @import '../../libs/assets/less/variables';

  .video-panel-body {
    padding: 0 18px;
    width: 100%;
    ul li {
      position: relative;
      display: block;
      float: left;
      height: 120px;
      width: 182px;
      margin-right: 16px;
      text-align: center;
      margin-bottom: 16px;
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
        width: 180px;
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
      video {
        height: 100%;
      }
    }
  }
</style>
