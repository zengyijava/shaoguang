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
    <!-- 加载更多 -->
    <div class="infinite-scroll" v-infinite-scroll="loadMore" infinite-scroll-disabled="busy">
      <ul v-show="videoList.length > 0">
        <li v-for="(item,index) in videoList" :key="index" :class="{active:isBatch&&item.active}">
          <div class="del-btn" v-show="!isBatch">
            <i @click="deleteVideo(item.id)" class="del-icon"></i>
          </div>
          <span v-show="isBatch" class="checked-item"></span>
          <video @click="dealItem(item,index,!item.active)" width="180" :src="item.url"></video>
        </li>
      </ul>
      <div class="blank-result" v-show="videoList.length === 0">
        <img src="../assets/img/private_media_no_data.png">
        {{$t('public.noVideoUpload')}}
      </div>
      <div class="loading" v-show="!busy">
        <i class="el-icon-loading"></i>{{$t('public.loading')}}
      </div>
    </div>
  </div>
</template>
<script>
import {mapGetters, mapMutations, mapActions} from 'vuex'
import MaterialPanelHeader from '../components/MaterialPanelHeader'
import VideoMaterialPicker from '../components/VideoMaterialPicker'
import infiniteScroll from 'vue-infinite-scroll'

export default {
  components: {MaterialPanelHeader, VideoMaterialPicker},
  computed: mapGetters(['videoList', 'userId', 'elements', 'busy']),
  data () {
    return {
      isBatch: false,
      checkedAll: false,
      fodderIds: [],
      param: {
        page: 1,
        size: 12,
        type: 3
      }
    }
  },
  methods: {
    ...mapMutations(['pushVideo']),
    ...mapActions(['deleteFodder', 'getFodder']),
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
        this.$emit('addVideo', item)
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
  @import '../assets/less/video-material';

</style>
