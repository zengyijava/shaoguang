<template>
  <div>
    <PublicMaterialHeader :param="param" @toggle="handleToggle($event)"></PublicMaterialHeader>
    <div class="infinite-scroll" :style="{top: scrollTop + 'px'}"
      v-infinite-scroll="loadMore" infinite-scroll-disabled="busy">
      <ul v-show="imageList.length > 0">
        <li v-for="(item,index) in imageList" :key="index" :class="{active:isBatch&&item.active}">
          <img @click="dealImage(item,index,!item.active)" :src="item.url.slice(0,-4)+item.url.slice((item.url.length-4),item.url.length)" style="object-fit: contain;" />
        </li>
      </ul>
      <div class="blank-result" v-show="imageList.length === 0">
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
import { mapGetters, mapActions } from 'vuex'
import PublicMaterialHeader from '../components/PublicMaterialHeader'
import infiniteScroll from 'vue-infinite-scroll'

export default {
  name: 'PublicImageMaterial',
  components: {PublicMaterialHeader},
  computed: mapGetters(['imageList', 'elements', 'addType', 'busy']),
  data () {
    return {
      isBatch: false,
      checkedAll: false,
      scrollTop: 100,
      param: {
        page: 1,
        size: 21,
        type: 2,
        source: 0, // 0 公共素材 1 我的素材
        industry: '', // 行业
        use: '' // 用途
      }
    }
  },
  methods: {
    ...mapActions(['getFodder']),
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
@import '../assets/less/image-material';

</style>
