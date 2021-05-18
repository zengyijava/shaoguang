<template>
  <div style="padding: 16px;">
    <RecordSearchForm @recordQueryParam="recordQueryParam"></RecordSearchForm>
    <RecordList :listData="listData" @extensionSuccess="extensionSuccess"></RecordList>
    <div class="rx-page-bar">
      <el-pagination
        background
        :current-page.sync="currentPage"
        @current-change="handleCurrentChange"
        layout="prev, pager, next, jumper"
        :total="totalNum">
      </el-pagination>
    </div>
  </div>
</template>

<script>
import RecordSearchForm from '../components/RecordSearchForm'
import RecordList from '../components/RecordList'
import {mapActions} from 'vuex'
import actions from '../../libs/api'
export default {
  name: 'AccessList',
  components: {RecordSearchForm, RecordList},
  data () {
    return {
      currentPage: 1,
      totalNum: 0,
      listData: [],
      recordParams: {},
      getTmpId: ''
    }
  },
  mounted () {
    this.getTmpId = this.$route.query.id
    this.recordQueryParam()
  },
  methods: {
    ...mapActions(['getRxRecordList']),
    recordQueryParam (params, currentPage = 1) {
      let _requestParams = {}
      _requestParams = {
        templateId: this.getTmpId ? Number(this.getTmpId) : '',
        resourceMode: params ? params.resourceMode : -1,
        markType: params ? params.markType : -1,
        smsContent: params ? params.smsContent : '',
        currentPage,
        pageSize: 10,
        effctiveStartTime1: params ? params.effctiveStartTime1 : '',
        effctiveStartTime2: params ? params.effctiveStartTime2 : '',
        effctiveEndTime1: params ? params.effctiveEndTime1 : '',
        effctiveEndTime2: params ? params.effctiveEndTime2 : ''
      }
      this.recordParams = params
      this.currentPage = currentPage
      this.getRecordList(_requestParams)
    },
    extensionSuccess () {
      this.recordQueryParam()
    },
    handleCurrentChange (val) {
      this.recordQueryParam(this.recordParams, val)
    },
    getRecordList (params) {
      let _self = this
      // 获取列表信息
      actions.getRecordList(params, response => {
        _self.totalNum = response.data.data.totalRecord
        _self.listData = response.data.data.list
      }, errMsg => {
        _self.$message.error(errMsg)
      })
    }
  }
}
</script>

<style lang="less">
@import '../assets/less/AccessList.less';
</style>
