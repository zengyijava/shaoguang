<template>
  <div>
    <PublicMaterialHeader :param="param" @toggle="handleToggle($event)"></PublicMaterialHeader>
    <!-- 加载更多 -->
    <div class="infinite-scroll" :style="{top: scrollTop + 'px'}"
      v-infinite-scroll="loadMore" infinite-scroll-disabled="busy">
      <ul v-show="videoList.length > 0">
        <li v-for="(item,index) in videoList" :key="index" :class="{active:isBatch&&item.active}">
          <video @click="dealItem(item,index,!item.active)" width="180" :src="item.url"></video>
        </li>
      </ul>
      <div class="blank-result" v-show="videoList.length === 0">
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
import PublicMaterialHeader from '../components/PublicMaterialHeader'
import infiniteScroll from 'vue-infinite-scroll'

export default {
  name: 'PublicVideoMaterial',
  components: {PublicMaterialHeader},
  computed: mapGetters(['videoList', 'elements', 'busy']),
  data () {
    return {
      isBatch: false,
      checkedAll: false,
      scrollTop: 100,
      fodderIds: [],
      param: {
        page: 1,
        size: 15,
        type: 3,
        source: 0, // 0 公共素材 1 我的素材
        industry: '', // 行业
        use: '' // 用途
      }
    }
  },
  methods: {
    ...mapMutations(['resetList']),
    ...mapActions(['getFodder']),
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
    handleToggle (height) {
      this.scrollTop = height
    },
    loadMore () {
      this.getFodder(this.param)
    }
  },
  directives: {infiniteScroll}
}
</script>

<style lang="less" scoped>
  @import '../assets/less/video-material';

</style>
