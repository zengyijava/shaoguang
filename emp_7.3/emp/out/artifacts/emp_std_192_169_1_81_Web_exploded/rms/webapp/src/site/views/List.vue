<template>
  <section class="admin-list-page">
    <div class="list-title">
      H5页面库 > 页面库列表
    </div>
    <div class="list-nr">
      <div class="search-bar">
        <div class="search-list">
          <label>关键词：</label>
          <el-input size="small" class="key-word" v-model="keyWord" placeholder="页面ID或标题、作者"></el-input>
        </div>
        <div class="search-list">
          <label>状态：</label>
          <el-select size="small" v-model="status" placeholder="请选择">
            <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
        </div>
        <div class="search-list">
          <label>修改时间：</label>
          <el-date-picker size="small" v-model="dateRange" type="daterange" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期">
          </el-date-picker>
        </div>
        <div class="soso" @click="queryH5List">
          <i class="el-icon-search"></i>
          <span>查询</span>
        </div>
        <div class="add" @click="LinkTo('')">
          <i class="el-icon-circle-plus-outline"></i>
          <span>新建</span>
        </div>
      </div>
      <el-table :data="h5List.data.list" class="table-class">
        <el-table-column label="序号" width="60">
          <template slot-scope="scope">
            <span v-text="scope.$index+1"></span>
          </template>
        </el-table-column>
        <el-table-column label="ID" width="180">
          <template slot-scope="scope">
            <span v-text="scope.row.hId"></span>
          </template>
        </el-table-column>
        <el-table-column label="标题">
          <template slot-scope="scope">
            <span v-text="scope.row.title"></span>
          </template>
        </el-table-column>
        <el-table-column label="作者" width="100">
          <template slot-scope="scope">
            <span v-text="scope.row.author?scope.row.author:'—'"></span>
          </template>
        </el-table-column>
        <el-table-column label="H5链接">
          <template slot-scope="scope">
            <a :href="scope.row.url" target="_blank"  rel="noopener noreferrer nofollow" v-text="scope.row.url"></a>
          </template>
        </el-table-column>
        <el-table-column label="状态" width="100">
          <template slot-scope="scope">
            <span :class="{'color-g':scope.row.staus === 1, 'color-h':scope.row.staus === 2}" v-text="statusList[scope.row.staus]"></span>
          </template>
        </el-table-column>
        <el-table-column label="修改时间" width="180">
          <template slot-scope="scope">
            <span v-text="scope.row.updateTime|formatDate"></span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180">
          <template slot-scope="scope">
            <div v-if="scope.row.staus === 0 || scope.row.staus === 1">
              <el-button class="detail-color" @click="onOpenDetail(scope.row.hId)" type="text" size="small">详情</el-button>
              <el-button class="del-color vbh" type="text" size="small">删除</el-button>
            </div>
            <div v-else>
              <el-button class="detail-color" @click="LinkTo(scope.row.hId)" type="text" size="small">编辑</el-button>
              <el-button class="del-color" @click="deleteList(scope.row)" type="text" size="small">删除</el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>
      <div class="page-bar">
        <el-pagination background @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="currentPage" :pager-count="5" :page-sizes="[10, 20, 30, 40]" :page-size="pageSize" layout="prev, pager, next, sizes, jumper" :total="h5List.data.totalRecord">
        </el-pagination>
      </div>
    </div>

    <!--详情弹框-->
    <div class="detail-pop" v-show="isShowDetail">
      <div class="pop-bg"></div>
      <div class="detail-pop-nr">
        <div class="detail-title">
          <span>详情</span>
          <i class="el-icon-close" @click="onCloseDetail"></i>
        </div>
        <div class="detail-left">
          <div class="phone-bar">
            <div class="phone-top"></div>
            <div class="phone-show" v-html="details.data.content"></div>
          </div>
        </div>
        <div class="detail-right">
          <div class="detail-base-info">
            <h5>基本信息（ID：{{details.data.hId}}）</h5>
            <p>标题：{{details.data.title}}</p>
            <p>作者：{{details.data.author?details.data.author:'—'}}</p>
            <p>H5链接：
              <a :href="details.data.url" target="_blank" rel="noopener noreferrer nofollow" >{{details.data.url}}</a>
            </p>
            <p>创建时间：{{details.data.createTime|formatDate}}</p>
            <p>修改时间：{{(details.data.updateTime|formatDate)}}</p>
          </div>
          <div class="detail-base-info mt20">
            <h5>状态信息</h5>
            <p>状态：
              <span v-if="details.data.staus === 0" class="color-g">已应用</span>
              <span v-else class="color-h">应用中</span>
            </p>
            <p>应用时间：{{details.data.useTime|formatDate}}</p>
          </div>
          <div class="an-back" @click="onCloseDetail">返回</div>
        </div>
      </div>
    </div>
    <el-dialog title="提示" :visible.sync="showTips" width="25%">
      <i class="tips-icon el-icon-warning"></i>
      <span>删除当前内容？</span>
      <span slot="footer" class="dialog-footer">
        <div class="an-false" type="primary" @click="showTips = false">否</div>
        <div class="an-true" @click="trueDelete">是</div>
        <div class="clear"></div>
      </span>
    </el-dialog>
  </section>
</template>

<script>
import { mapState, mapActions } from 'vuex'
import filters from '../../libs/filters'
export default {
  name: 'List',
  data () {
    return {
      keyWord: '',
      status: '',
      dateRange: [],
      item: {},
      showTips: false,
      isShowDetail: false,
      currentPage: 1,
      pageSize: 10,
      statusList: ['已应用', '应用中', '草稿'],
      options: [{
        value: '',
        label: '全部'
      }, {
        value: 0,
        label: '已应用'
      }, {
        value: 1,
        label: '应用中'
      }, {
        value: 2,
        label: '草稿'
      }]
    }
  },
  computed: {
    ...mapState(['h5List', 'details']),
    dateTime () {
      let date = new Date()
      let seperator1 = '-'
      let year = date.getFullYear()
      let month = date.getMonth() + 1
      let strDate = date.getDate()
      if (month >= 1 && month <= 9) {
        month = '0' + month
      }
      if (strDate >= 0 && strDate <= 9) {
        strDate = '0' + strDate
      }
      let timeStr = year + seperator1 + month + seperator1 + strDate
      return timeStr
    }
  },
  beforeCreate: function () {
    // 关闭原来EMP系统LOAD
    if (window.parent.complete) {
      window.isLoading = false
      window.parent.complete()
    }
  },
  mounted () {
    this.queryH5List()
    // 同步子窗口数据更新
    window.syncData = () => {
      this.queryH5List()
    }
  },
  methods: {
    ...mapActions(['getH5List', 'getDetail', 'deleteEditor']),
    // 获取列表数据
    queryH5List () {
      let paramDat = {
        pageFuzzyMatch: this.keyWord,
        status: this.status,
        currentPage: this.currentPage,
        pageSize: this.pageSize
      }
      if (this.dateRange[0]) {
        paramDat.updateTimeBeg = filters.formatDate(this.dateRange[0])
      } else {
        paramDat.updateTimeBeg = ''
      }
      if (this.dateRange[1]) {
        paramDat.updateTimeEnd = filters.formatDate(this.dateRange[1])
      } else {
        paramDat.updateTimeEnd = ''
      }
      this.getH5List(paramDat)
    },
    // 修改每页显示条数
    handleSizeChange (val) {
      console.log(`每页 ${val} 条`)
      this.pageSize = val
      this.queryH5List()
    },
    // 切换页码
    handleCurrentChange (val) {
      console.log(`当前页: ${val}`)
      this.currentPage = val
      this.queryH5List()
    },
    // 打开详情预览
    onOpenDetail (id) {
      let params = {
        hId: id
      }
      this.getDetail(params)
      this.isShowDetail = true
      document.body.style.cssText = 'height: 100%;overflow:hidden;'
      document.documentElement.style.cssText = 'height: 100%;overflow:hidden;'
    },
    // 关闭详情预览
    onCloseDetail () {
      this.isShowDetail = false
      document.body.style.cssText = 'height: auto;overflow:auto;'
      document.documentElement.style.cssText = 'height: auto;overflow:auto;'
    },
    // 获取详情预览
    queryDetail (id) {
      this.getDetail(id)
    },
    // 删除列表
    deleteList (item) {
      this.item = item
      this.showTips = true
    },
    trueDelete () {
      this.deleteEditor(this.item)
      this.showTips = false
    },
    // 跳转去编辑器——新建
    LinkTo (id) {
      let tId = id || ''
      // this.$router.push({ path: '/Editor', query: { id: tId } })
      let routeData = this.$router.resolve({ path: '/Editor' })
      window.open(routeData.href + '?id=' + tId, '_blank')
    }
  },
  filters
}
</script>

<style lang="less">
@import "../assets/less/site.less";
@import "../../libs/assets/less/pagenation";
</style>
