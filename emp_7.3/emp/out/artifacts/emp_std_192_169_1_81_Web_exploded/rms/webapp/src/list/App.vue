<template>
  <div id="app" class="template-list">
    <!-- 模板库搜索条件 -->
    <common-search-form
      v-if="pageType === 'common' && modulePer.length > 0"
      ref="commonSearchForm"
      :modulePer="modulePer"
      :currentSystem="currentSystem"
      @queryCondition="queryCondition">
    </common-search-form>
    <!-- 我的场景搜索条件 -->
    <my-search-form
      v-else-if="pageType === 'my' && modulePer.length > 0"
      ref="mySearchForm"
      :modulePer="modulePer"
      :currentSystem="currentSystem"
      @queryCondition="queryCondition">
    </my-search-form>
    <!--  RCOS场景搜索条件 -->
    <rcos-search-form
      v-else-if="pageType === 'rcos' && modulePer.length > 0"
      ref="RcosSearchForm"
      :modulePer="modulePer"
      :currentSystem="currentSystem"
      @queryCondition="queryCondition">
    </rcos-search-form>
    <!-- 列表 -->
    <div class="scene-list clearfix">
      <!-- 添加按钮 -->
      <add-template
        v-if="showAddTemplateBtn && modulePer.length > 0"
        :modulePer="modulePer"
        :pageType="pageType">
      </add-template>
      <li-template
        v-for="(items, key) in sceneListData"
        :key="key"
        :templateData="items"
        :pageType="pageType"
        @deleCurrentSceneLi="deleCurrentSceneLi">
      </li-template>
    </div>
    <!-- pagination -->
    <div v-if="totalPage > pageSize" class="page-bar">
      <el-pagination
        background
        @current-change="handleCurrentChange"
        :current-page="currentPage"
        :page-size="pageSize"
        layout="prev, pager, next, jumper"
        :total="totalPage">
      </el-pagination>
    </div>
  </div>
</template>

<script>
import CommonSearchForm from './components/CommonSearchForm'
import MySearchForm from './components/MySearchForm'
import RcosSearchForm from './components/RcosSearchForm'
import AddTemplate from './components/AddTemplate'
import LiTemplate from './components/LiTemplate'
import {mapGetters, mapActions} from 'vuex'
import utils from '../libs/utils'
import filters from '../libs/filters'
import actions from '../libs/api'
import config from '../libs/config'

export default {
  name: 'App',
  components: {
    'common-search-form': CommonSearchForm,
    'my-search-form': MySearchForm,
    'rcos-search-form': RcosSearchForm,
    'add-template': AddTemplate,
    'li-template': LiTemplate
  },
  data () {
    return {
      // 显示新增模板按钮
      showAddTemplateBtn: false,
      // 场景列表数据
      sceneListData: [],
      // 角色列表对应权限
      roleArr: [],
      // 页面数据长度
      pageSize: 11,
      // 总页数
      totalPage: 0,
      // 当前页
      currentPage: 1,
      // form参数
      params: {},
      // 富信类型配置
      modulePer: [],
      // 当前所处系统，默认位1是emp如果为0是rcos
      currentSystem: 1
    }
  },
  computed: {
    ...mapGetters(['userInfo', 'loading']),
    // 页面类型，区分是从我的场景进入还是模板库中进入
    pageType () {
      // 本地测试从url上获取type，提交SVN或者线上则必须改为iframe
      let _currentPageType = utils.getUrlParameters('type', false, config.GET_URL_PARAMS.LIST) || 'my'

      return _currentPageType
    }
  },
  beforeCreate () {
    // 列表页使用在EMP或者其他系统中，需先关闭原系统中模块加载动画
    if (window.parent.complete) {
      window.isLoading = false
      window.parent.complete()
    }
  },
  created () {
    // 获取用户信息
    this.getUserInfos()
  },
  mounted () {
    // Subscriptions for mutation
    this.$store.subscribe(mutation => {
      if (mutation.payload) {
        const msg = mutation.payload.msg
        if (mutation.type === 'error') {
          this.$message.error(msg)
        }
      }
    })
    // 同步子窗口数据更新
    window.syncData = () => {
      this.queryCondition()
      this.$refs[this.pageType + 'SearchForm'].resetForm()
    }
  },
  watch: {
    // 监听用户信息是否获取成功
    userInfo (val) {
      if (val) {
        let _code = val.code
        if (_code === 200) {
          let _userData = val.data.loginCorp
          // 更新角色权限
          this.roleArr = _userData.roleArr
          // 更新富信类型
          this.modulePer = val.data.modulePer
          // 更新所处系统
          this.currentSystem = Number(val.data.type)

          if (!this.modulePer || this.modulePer.length < 1) {
            this.$message.error(this.$t('authErrorHintMsg'))
          }

          // 根据角色权限判断列表是否有添加按钮以及列表显示数据长度
          for (let [i, roleArrLen] = [0, this.roleArr.length]; i < roleArrLen; i++) {
            // 返回角色权限中取对应的页面模快权限
            if (this.roleArr[i].type === this.pageType) {
              // 判断角色权限是否有新建按钮以及页面数据显示长度
              if (this.roleArr[i].add === '1') {
                this.pageSize = 11
                this.showAddTemplateBtn = true
              } else {
                this.pageSize = 12
                this.showAddTemplateBtn = false
              }
              break
            }
          }
          // 获取用户信息成功后再加载初始列表，列表中按钮权限由用户接口返回
          this.queryCondition()
        }
      }
    }
  },
  methods: {
    ...mapActions(['getUserInfos']),

    // 分页器切换
    handleCurrentChange (val) {
      this.queryCondition(this.params, val)
    },

    // 获取当前列表初始查询条件场景类型
    getInitTmpType () {
      let _editorConfig = this.modulePer
      if (_editorConfig.length > 1) {
        return ''
      } else {
        return this.modulePer[0].type
      }
    },

    /**
     * 列表查询条件
     * @param params
     * @param currentPage
     */
    queryCondition (params, currentPage = 1) {
      let _requestParams = {}
      let _startDate = ''
      let _endDate = ''
      // 有参数且参数中有时间参数
      if (params && params.createDate) {
        _startDate = filters.formatDate(params.createDate[0])
        _endDate = filters.formatDate(params.createDate[1])
      }
      // 判断当前场景属于我的场景还是公共场景
      if (this.pageType === 'my') {
        _requestParams = {
          addtimeEnd: _endDate,
          addtimeBeg: _startDate,
          auditStatus: params ? params.auditStatus : '',
          currentPage,
          pageSize: this.pageSize,
          dsFlag: params ? params.templateType : '',
          isPublic: 0,
          sptemplid: params ? params.sceneId : '',
          tmpType: params ? params.sceneType : this.getInitTmpType(),
          tmName: params ? params.sceneTitle : '',
          tmState: params ? params.sceneStatus : '',
          corpName: params ? params.corpName : '',
          source: 0,
          containShare: 1,
          isMaterial: 0
        }
      } else if (this.pageType === 'common') {
        _requestParams = {
          currentPage,
          pageSize: this.pageSize,
          isPublic: 1,
          industryId: params ? params.industryId : '',
          sptemplid: params ? params.sceneId : '',
          tmpType: params ? params.sceneType : this.getInitTmpType(),
          tmName: params ? params.sceneTitle : '',
          useId: params ? params.useId : '',
          corpName: params ? params.corpName : '',
          source: 0,
          containShare: 1,
          isMaterial: 1
        }
      } else if (this.pageType === 'rcos') {
        _requestParams = {
          addtimeEnd: _endDate,
          addtimeBeg: _startDate,
          auditStatus: params ? params.auditStatus : '',
          currentPage,
          pageSize: this.pageSize,
          dsFlag: params ? params.templateType : '',
          isPublic: 1,
          sptemplid: params ? params.sceneId : '',
          tmpType: params ? params.sceneType : this.getInitTmpType(),
          tmName: params ? params.sceneTitle : '',
          tmState: params ? params.sceneStatus : '',
          corpName: params ? params.corpName : '',
          source: 3,
          containShare: 1,
          isMaterial: 0
        }
      }
      // 执行列表数据获取
      this.getListData(_requestParams)
      // 设置参数
      this.currentPage = currentPage
      this.params = params
    },

    // 获取列表数据
    getListData (params) {
      let _self = this
      // 获取列表信息
      actions.getListData(params, response => {
        _self.sceneListData = response.data.data.list
        _self.totalPage = response.data.data.totalRecord
      }, errMsg => {
        _self.$message.error(errMsg)
      })
    },

    // 删除模板
    deleCurrentSceneLi () {
      this.queryCondition(this.params, 1)
    }
  }
}
</script>
<style lang="less">
  @import '../libs/assets/less/template-list';
</style>
