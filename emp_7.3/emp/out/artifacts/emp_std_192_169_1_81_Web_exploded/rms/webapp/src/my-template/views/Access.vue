<template>
  <div class="access-wrapper clear-fix">
    <div class="access-left">
      <ul class="access-tab clear-fix">
        <router-link to="/access" tag="li">获取相同资源</router-link>
        <router-link to="/personalise" tag="li">获取个性化资源</router-link>
      </ul>
      <router-view></router-view>
    </div>
    <div class="access-right">
      <Preview :previewModule="previewModule"
               :previewData="rxPreviewData"
               :markData="rxMarkData">
      </Preview>
    </div>
  </div>
</template>

<script>
import {mapGetters} from 'vuex'
import Preview from '../../libs/components/Preview'
import actions from '../../libs/api'
export default {
  name: 'Access',
  components: {Preview},
  data () {
    return {
      previewModule: {
        title: false,
        hint: false
      },
      rxPreviewData: []
    }
  },
  created: function () {
    // 获取详细信息参数
    let _getDetailsDataParams = {
      tmId: this.$route.query.id,
      previewType: 0
    }
    let _self = this

    // 执行详细信息获取
    actions.getDetailInfo(_getDetailsDataParams, response => {
      if (response.data.code === 200 || response.data.state === '0') {
        _self.rxPreviewData = response.data.data.list
      } else {
        _self.$message.error(response.data.msg)
      }
    }, errMsg => {
      _self.$message.error(errMsg)
    })
  },
  computed: {
    ...mapGetters(['rxMarkData'])
  }
}
</script>

<style lang="less" scoped>
  @import "../assets/less/default";
  .access-wrapper{
    padding: 20px 38px;
    font-size: 14px;
    .access-left{
      float: left;
      width: 765px;
      .access-tab{
        width: 100%;
        margin-bottom: 12px;
        border-bottom: solid 2px #e4e7ed;
        li{
          float: left;
          margin-bottom: -2px;
          margin-right: 42px;
          font-size: 14px;
          line-height: 40px;
          cursor: pointer;
          &.router-link-active{
            color: #3b9cff;
            border-bottom: solid 2px #3b9cff;
          }
        }
      }
    }
    .access-right{
      float: left;
      width: 360px;
      margin-left: 48px;
    }
  }
</style>
