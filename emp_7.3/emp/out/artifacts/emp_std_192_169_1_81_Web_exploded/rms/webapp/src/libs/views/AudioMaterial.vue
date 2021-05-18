<template>
  <div>
    <MaterialPanelHeader :isBatch="isBatch" :checkedAll="checkedAll" v-model="checkedAll"
      @delete="deleteAudio" @batch="batchAudio" @input="setCheckAll">
      <AudioMaterialPicker @on-success="handleUploadSuccess" class="fr">
        <el-tooltip effect="dark" :content="$t('H5.up_audio_hint')" placement="bottom">
          <el-button type="primary">+ {{ $t('H5.up_audio_btn') }}</el-button>
        </el-tooltip>
      </AudioMaterialPicker>
    </MaterialPanelHeader>
    <div class="infinite-scroll" v-infinite-scroll="loadMore" infinite-scroll-disabled="busy">
      <ul v-show="audioList.length > 0">
        <li v-for="(item,index) in audioList" :key="index" class="clearfix">
          <i v-show="!isBatch" @click="deleteAudio(item.id)" class="el-icon-delete check-type"></i>
          <el-checkbox v-show="isBatch" @change="dealItem(item)" v-model="item.active" :lable="item.id"
            class="check-type"></el-checkbox>
          <span class="audio-index">{{index+1}}</span>
          <span class="audio-name">{{item.original}}</span>
          <span @click="$emit('addAudio', item)" class="audio-add fr"></span>
          <span class="audio-duration fr">{{item.duration|formatTime(true)}}</span>
        </li>
      </ul>
      <div class="blank-result" v-show="audioList.length === 0">
        <img src="../assets/img/private_media_no_data.png">
        {{$t('public.noAudioUpload')}}
      </div>
      <div class="loading" v-show="!busy">
        <i class="el-icon-loading"></i>{{$t('public.loading')}}
      </div>
    </div>
  </div>
</template>
<script>
import {mapGetters, mapMutations, mapActions} from 'vuex'
import filters from '../../libs/filters'
import MaterialPanelHeader from '../components/MaterialPanelHeader'
import AudioMaterialPicker from '../components/AudioMaterialPicker'
import infiniteScroll from 'vue-infinite-scroll'

export default {
  components: {MaterialPanelHeader, AudioMaterialPicker},
  computed: mapGetters(['audioList', 'userId', 'elements', 'busy']),
  data () {
    return {
      isBatch: false,
      checkedAll: false,
      param: {
        page: 1,
        size: 15,
        type: 4
      }
    }
  },
  methods: {
    ...mapMutations(['pushAudio']),
    ...mapActions(['deleteFodder', 'getFodder']),
    handleUploadSuccess (data) {
      if (data) {
        this.checkedAll = false
        this.pushAudio(data)
      }
    },
    setCheckAll () {
      if (this.checkedAll) {
        this.audioList.map(item => {
          item.active = true
        })
      } else {
        this.audioList.map(item => {
          item.active = false
        })
      }
    },
    batchAudio () {
      this.isBatch = !this.isBatch
      this.checkedAll = true
      this.audioList.map((item, index) => {
        this.$set(this.audioList[index], 'active', true)
      })
    },
    deleteAudio (id) {
      this.$confirm(this.$t('H5.audio_dele_hint'), this.$t('H5.hint_title'), {
        confirmButtonText: this.$t('H5.hint_sure'),
        cancelButtonText: this.$t('H5.hint_cancel'),
        type: 'warning'
      }).then(() => {
        let param = {}
        if (this.isBatch) {
          let fodderIds = []
          this.audioList.map(item => {
            if (item.active) {
              fodderIds.push(item.id)
            }
          })
          param.fodderIds = fodderIds
        } else {
          param.fodderIds = []
          param.fodderIds.push(id)
        }
        this.deleteFodder(param)
      }).catch(() => {
      })
    },
    dealItem (item) {
      if (this.isBatch) {
        if (!item.active) {
          this.checkedAll = false
        } else {
          let allChecked = this.audioList.filter(item => {
            return item.active === false
          })
          if (allChecked.length === 0) {
            this.checkedAll = true
          }
        }
      } else {
        this.$emit('addAudio', item)
      }
    },
    loadMore () {
      this.param.userId = this.userId
      this.getFodder(this.param)
    }
  },
  filters,
  directives: {infiniteScroll}
}
</script>
<style lang="less" scoped>
  @import '../assets/less/audio-material';
  .infinite-scroll {
    width: 100%;
    ul {
      li {
        .check-type {
          margin-left: 18px;
          margin-right: 8px;
        }

        .el-icon-delete {
          cursor: pointer;
        }
      }
    }
  }
</style>
