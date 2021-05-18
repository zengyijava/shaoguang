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
    <div class="audio-panel-body">
      <ul>
        <li v-for="(item,index) in audioList" :key="index" class="clearfix">
          <i v-show="!isBatch" @click="deleteAudio(item.id)" class="el-icon-delete check-type"></i>
          <el-checkbox v-show="isBatch" @change="dealItem(item)" v-model="item.active" :lable="item.id"
            class="check-type"></el-checkbox>
          <span class="audio-index">{{index+1}}</span>
          <span class="audio-name">{{item.original}}</span>
          <span @click="addAudio(item)" class="audio-add fr"></span>
          <span class="audio-duration fr">{{item.duration|formatTime(true)}}</span>
        </li>
      </ul>
    </div>
  </div>
</template>
<script>
import {mapGetters, mapMutations, mapActions} from 'vuex'
import MaterialPanelHeader from '../components/MaterialPanelHeader'
import AudioMaterialPicker from './AudioMaterialPicker'
import filters from '../../libs/utils'

export default {
  components: {MaterialPanelHeader, AudioMaterialPicker},
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
    param.type = 4
    this.getFodder(param)
  },
  computed: mapGetters(['audioList', 'userInfo', 'pages', 'currentPage', 'elements', 'music', 'element', 'addType', 'count']),
  data () {
    return {
      isBatch: false,
      checkedAll: false
    }
  },
  methods: {
    ...mapMutations(['pushAudio', 'selectElement', 'updateMusic', 'updateCount']),
    ...mapActions(['deleteFodder', 'getFodder', 'getUserInfos']),
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
    // 添加音频
    addAudio (payload) {
      if (this.addType === 'audio') {
        this.updateCount()
        let media = {
          locked: false,
          visible: true,
          active: true,
          x: 10,
          h: 90,
          w: 290,
          z: this.elements.length,
          type: 'audio',
          tag: this.$t('navBar.audio') + this.count,
          src: payload.url,
          duration: payload.duration,
          filename: payload.original,
          title: payload.original.slice(0, 20),
          size: payload.size,
          style: {
            color: '#333333',
            backgroundColor: '#ffffff',
            transparency: 0
          }
        }
        // 替换音频
        if (this.elements.find(item => item.active && item.type === 'audio')) {
          this.elements.find((item) => {
            if (item.active && item.type === 'audio') {
              item.src = media.src
              item.duration = media.duration
              item.filename = media.original
              item.title = media.title
              item.size = media.size
            }
          })
        } else {
          this.elements.push(media)
          this.selectElement(media)
        }
      } else if (this.addType === 'music') {
        // 更新背景音乐
        const music = this.music
        let hasMusic = false
        music.src = payload.url
        music.filename = payload.original
        this.currentPage.elements.map(item => {
          if (item.type === 'music') {
            hasMusic = true
          }
        })
        if (!hasMusic) {
          const musicElement = {
            visible: true,
            active: true,
            type: 'music'
          }
          this.pages.forEach(item => {
            if (item === this.currentPage) {
              item.elements.push({
                locked: false,
                ...musicElement,
                tag: this.$t('navBar.bgAudio')
              })
            } else {
              item.elements.push(musicElement)
            }
          })
        }
      }
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
        this.addAudio(item)
      }
    }
  },
  filters
}
</script>
<style lang="less" scoped>
  @import '../../libs/assets/less/variables';

  .audio-add {
    margin-right: 20px;
    margin-top: 16px;
    width: 15px;
    height: 15px;
    cursor: pointer;
    background: url('../assets/img/btn_add_one.png') no-repeat;
  }

  .audio-panel-body {
    width: 100%;
    background-color: #fafafa;
    ul li {
      height: 50px;
      line-height: 50px;
      width: 100%;
      color: #7f7f7f;
      .check-type {
        margin-left: 18px;
        margin-right: 8px;
      }
      .el-icon-delete {
        cursor: pointer;
      }
      .audio-name {
        margin-left: 32px;
        margin-right: 44px;
        width: 200px;
        /* for internet explorer */
      }
      .audio-duration {
        margin-right: 14px;
      }
      &:hover {
        border-top: solid 1px #f1f1f1;
        border-bottom: solid 1px #f1f1f1;
        // box-shadow: 5px 0 5px 5px #f1f1f1;
        background-color: #fff;
        color: #3b9cff;
        height: 48px;
        line-height: 48px;
      }
      &:first-child:hover {
        height: 49px;
        line-height: 49px;
        border-top: none;
      }
    }
  }
</style>
