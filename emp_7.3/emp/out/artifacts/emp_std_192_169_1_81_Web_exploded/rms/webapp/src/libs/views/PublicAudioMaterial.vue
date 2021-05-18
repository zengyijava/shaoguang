<template>
  <div>
    <PublicMaterialHeader :param="param" @toggle="handleToggle($event)"></PublicMaterialHeader>
    <div class="infinite-scroll" :style="{top: scrollTop + 'px'}"
      v-infinite-scroll="loadMore" infinite-scroll-disabled="busy">
      <ul v-show="audioList.length > 0">
        <li v-for="(item,index) in audioList" :key="index" class="clearfix">
          <span class="audio-index">{{index+1}}</span>
          <span class="audio-name">{{item.original}}</span>
          <span @click="$emit('addAudio', item)" class="audio-add fr"></span>
          <span class="audio-duration fr">{{item.duration|formatTime(true)}}</span>
        </li>
      </ul>
      <div class="blank-result" v-show="audioList.length === 0">
        <img src="../assets/img/no_relate_result.png">
        {{$t('public.noRelateData')}}
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
import PublicMaterialHeader from '../components/PublicMaterialHeader'
import infiniteScroll from 'vue-infinite-scroll'

export default {
  name: 'PublicAudioMaterial',
  components: {PublicMaterialHeader},
  computed: mapGetters(['audioList', 'elements', 'busy']),
  data () {
    return {
      isBatch: false,
      checkedAll: false,
      scrollTop: 100,
      param: {
        page: 1,
        size: 18,
        type: 4,
        source: 0, // 0 公共素材 1 我的素材
        industry: '', // 行业
        use: '' // 用途
      }
    }
  },
  methods: {
    ...mapMutations(['resetList']),
    ...mapActions(['getFodder']),
    handleToggle (height) {
      this.scrollTop = height
    },
    loadMore () {
      this.getFodder(this.param)
    }
  },
  filters,
  directives: {infiniteScroll}
}
</script>
<style lang="less" scoped>
  @import '../assets/less/audio-material';
</style>
