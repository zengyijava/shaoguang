<template>
  <ul class="public-material-header">
    <li class="filter"
      :style="{height: industryOpen ? 'auto' : fixRowHeight + 'px', 'border-bottom': '1px solid #e2e2e2'}"
      v-if="industryArr.length > 0">
      <span class="filter-title">{{$t('public.Industry')}}</span>
      <ul class="sub-filters-list" ref="industryFilterList">
        <li
          :class="[industryActive === 0 ? 'is-active' : '']"
          @click="getIndustryData('', 0)"
        >{{$t('styleSetting.styleOptions.all')}}
        </li>
        <li
          v-for="(item, index) in industryArr"
          @click="getIndustryData(item, index)"
          :key="index"
          :class="[industryActive === index + 1 ? 'is-active' : '']"
        >{{item.name}}
        </li>
      </ul>
      <span class="fold-btn" v-show="industryRowHeight > fixRowHeight" @click.stop="showMore('industry')">
          <span class="fold-text">{{industryOpen ? $t('public.packUp') : $t('public.moreData')}}</span>
          <i :class="[industryOpen ? 'el-icon-arrow-down' : 'el-icon-arrow-up']"></i>
        </span>
    </li>
    <li
      class="filter"
      :style="{height: useOpen ? 'auto' : fixRowHeight + 'px'}"
      v-if="useIdArr.length > 0">
      <span class="filter-title">{{$t('public.use')}}</span>
      <ul class="sub-filters-list" ref="useFilterList">
        <li
          :class="[useActive === 0 ? 'is-active' : '']"
          @click="getUseData('', 0)"
        >{{$t('styleSetting.styleOptions.all')}}
        </li>
        <li
          v-for="(item, index) in useIdArr"
          :key="index"
          @click="getUseData(item, index)"
          :class="[useActive === index + 1 ? 'is-active' : '']"
        >{{item.name}}
        </li>
      </ul>
      <span class="fold-btn" v-show="useRowHeight > fixRowHeight" @click.stop="showMore('use')">
        <span class="fold-text">{{useOpen ? $t('public.packUp') : $t('public.moreData')}}</span>
        <span :class="[useOpen ? 'el-icon-arrow-down' : 'el-icon-arrow-up']"></span>
      </span>
    </li>
  </ul>
</template>

<script>
import {mapGetters, mapMutations, mapActions} from 'vuex'

export default {
  name: 'PublicMaterialHeader',
  props: {
    param: {
      type: Object,
      default: () => {}
    }
  },
  computed: {
    ...mapGetters(['useIds', 'addType']),
    // 行业数据
    industryArr () {
      let industryArr = []
      let useData = []
      if (this.useIds.length > 0) {
        useData = this.useIds
      } else {
        return industryArr
      }
      for (let i = 0; i < useData.length; i++) {
        let tmpType = useData[i].type
        if (tmpType === 0) {
          industryArr.push(useData[i])
        }
      }
      return industryArr
    },
    // 用途数据
    useIdArr () {
      let useArr = []
      let useData
      if (this.useIds.length > 0) {
        useData = this.useIds
      } else {
        return useArr
      }
      for (let i = 0; i < useData.length; i++) {
        let tmpType = useData[i].type
        if (tmpType === 1) {
          useArr.push(useData[i])
        }
      }
      return useArr
    }
  },
  data () {
    return {
      industryOpen: false,
      useOpen: false,
      industryRowHeight: 36,
      useRowHeight: 36,
      fixRowHeight: 36,
      industryActive: 0,
      useActive: 0
    }
  },
  mounted () {
    this.$nextTick(() => {
      if (this.industryArr.length > 0) {
        this.industryRowHeight = this.$refs.industryFilterList.getBoundingClientRect().height
      }
      if (this.useIdArr.length > 0) {
        this.useRowHeight = this.$refs.useFilterList.getBoundingClientRect().height
      }
      this.$emit('toggle', this.$el.getBoundingClientRect().height)
    })
  },
  methods: {
    ...mapMutations(['resetList']),
    ...mapActions(['getFodder']),
    showMore (type) {
      if (type === 'industry') {
        this.industryOpen = !this.industryOpen
      } else {
        this.useOpen = !this.useOpen
      }
      this.$nextTick(() => {
        this.$emit('toggle', this.$el.getBoundingClientRect().height)
      })
    },
    getIndustryData (val, index) {
      if (val) {
        this.industryActive = index + 1
      } else {
        this.industryActive = index
      }
      this.search(val)
    },
    getUseData (val, index) {
      if (val) {
        this.useActive = index + 1
      } else {
        this.useActive = index
      }
      this.search(val)
    },
    search (val) {
      this.resetList(this.addType)
      this.param.page = 1
      this.param.industry = val.id || ''
      this.getFodder(this.param)
    }
  }
}
</script>

<style lang="less">
  .public-material-header {
    padding: 10px 18px;

    .filter {
      height: 36px;
      line-height: 36px;
      overflow: hidden;

      .filter-title {
        display: inline-block;
        font-weight: 700;
        font-size: 12px;
        color: #222222;
      }

      .sub-filters-list {
        display: inline-block;
        vertical-align: top;
        width: 258px;
        margin-left: 40px;

        li {
          float: left;
          cursor: pointer;
          font-size: 12px;
          margin-right: 16px;
          color: #666666;

          &:hover {
            color: #2E95FF;
          }
        }
      }

      .fold-btn {
        display: inline-block;
        cursor: pointer;
        font-size: 12px;
        color: #999999;

        &:hover {
          color: #2E95FF !important;
        }
      }
    }

    .is-active {
      color: #2E95FF !important;
    }
  }
</style>
