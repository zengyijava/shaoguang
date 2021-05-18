<template>
  <div
    v-loading="containerLoading"
    class="template-cont">
    <!-- searchForm -->
    <my-tpl-search-form
      v-if="pageType === 'my'"
      :modulePer="modulePer"
      ref="templateSearchForm"
      @queryCondition="queryCondition">
    </my-tpl-search-form>
    <common-tpl-search-form
      v-if="pageType === 'common'"
      :modulePer="modulePer"
      ref="templateSearchForm"
      @queryCondition="queryCondition">
    </common-tpl-search-form>
    <!-- importModule -->
    <el-upload
      v-if="showImportBtn"
      :action="importModuleUrl"
      :data="importData"
      :multiple="false"
      :show-file-list="false"
      :beforeUpload="beforeHandleImport"
      :on-success="handleImportSuccess"
      :on-error="handleImportError">
      <el-button size="small" type="primary">导入模板</el-button>
    </el-upload>
    <!-- templateList -->
    <div class="template-list clearfix">
      <add-template
        v-if="templateAddAuthority && modulePer.length > 0"
        :pageType="pageType"
        :modulePer="modulePer">
      </add-template>
      <template-li
        v-for="(items, key) in listData"
        :key="key"
        :templateData="items"
        :pageType="pageType"
        @deleCurrentSceneLi="deleCurrentSceneLi">
      </template-li>
    </div>
    <!-- pagination -->
    <div
      v-if="totalPage > pageSize"
      class="page-bar">
      <el-pagination
        background
        @current-change="handlePaginationChange"
        :current-page="currentPage"
        :page-size="pageSize"
        layout="prev, pager, next, jumper"
        :total="totalPage">
      </el-pagination>
    </div>
  </div>
</template>

<script>
import MyTplSearchForm from '../components/MyTplSearchForm'
import CommonTplSearchForm from '../components/CommonTplSearchForm'
import AddTemplate from '../components/AddTemplate'
import TemplateLi from '../components/TplLiModule'
import {mapGetters, mapActions} from 'vuex'
import utils from '../../libs/utils'
import filters from '../../libs/filters'
import actions from '../../libs/api'
import config from '../../libs/config'
import AJAXURL from '../../libs/ajax.address'

export default {
  name: 'MyTemplateList',
  components: {
    'my-tpl-search-form': MyTplSearchForm,
    'common-tpl-search-form': CommonTplSearchForm,
    'add-template': AddTemplate,
    'template-li': TemplateLi
  },
  data () {
    return {
      // 角色列表对应权限
      roleArr: [],
      // 场景列表数据
      listData: [],
      // 总页数
      totalPage: 0,
      // 页面数据长度
      pageSize: 11,
      // 当前页
      currentPage: 1,
      // 富信类型配置
      modulePer: [],
      // 模板新增权限
      templateAddAuthority: true,
      // 表单查询条件
      formCondition: {},
      // 页面加载
      containerLoading: false,
      // 导入地址
      importModuleUrl: AJAXURL.IMPORT_MODULE,
      // 导入附加信息
      importData: {
        corpCode: '',
        userId: ''
      }
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
      this.$refs['templateSearchForm'].resetForm()
    }
  },
  computed: {
    ...mapGetters(['userInfo']),
    // 页面类型，区分是从我的场景进入还是模板库中进入
    pageType () {
      // 本地测试从url上获取type，提交SVN或者线上则必须改为iframe
      let _currentPageType = utils.getUrlParameters('type', false, config.GET_URL_PARAMS.LIST) || 'my'

      return _currentPageType
    },
    // 导入按钮权限
    showImportBtn () {
      let _importVal = false
      // 根据角色权限判断列表是否有导入按钮
      for (let [i, roleArrLen] = [0, this.roleArr.length]; i < roleArrLen; i++) {
        // 返回角色权限中取对应的页面模快权限
        if (this.roleArr[i].type === this.pageType) {
          if (this.roleArr[i].upload === '1') {
            _importVal = true
            break
          }
        }
      }
      return _importVal
    }
  },
  watch: {
    userInfo (val) {
      if (val) {
        let _code = val.code
        if (_code === 200) {
          let _userData = val.data.loginCorp
          this.importData.corpCode = _userData.corpCode
          this.importData.userId = _userData.userId
          // 更新角色权限
          this.roleArr = _userData.roleArr
          // 更新富信类型
          this.modulePer = val.data.modulePer
          // 未配置任何富信编辑器则返回错误信息
          if (!this.modulePer || this.modulePer.length < 1) {
            this.$message.error('请联系管理员开通对应列表查看权限！')
          }

          // 根据角色权限判断列表是否有添加按钮以及列表显示数据长度
          for (let [i, roleArrLen] = [0, this.roleArr.length]; i < roleArrLen; i++) {
            // 返回角色权限中取对应的页面模快权限
            if (this.roleArr[i].type === this.pageType) {
              // 判断角色权限是否有新建按钮以及页面数据显示长度
              if (this.roleArr[i].add === '1') {
                this.pageSize = 11
                this.templateAddAuthority = true
              } else {
                this.pageSize = 12
                this.templateAddAuthority = false
              }
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
    /**
     * 列表查询条件
     * @param params
     * @param currentPage
     */
    queryCondition (params, currentPage = 1) {
      let _startDate = ''
      let _endDate = ''
      // 有参数且参数中有时间参数
      if (params && params.createDate) {
        _startDate = filters.formatDate(params.createDate[0])
        _endDate = filters.formatDate(params.createDate[1])
      }

      let _requestParams = {
        sptemplid: params ? params.templateId : '', // 场景ID
        tmName: params ? params.templateContent : '', // 模板内容
        dsFlag: params ? params.templateType : '', // 模板类型(静态或动态)
        tmpType: params ? params.fuxinType : this.getInitTmpType(), // 富信类型
        auditStatus: params ? params.auditStatus : '', // 审核状态
        addtimeEnd: _endDate, // 创建筛选起始时间
        addtimeBeg: _startDate, // 创建筛选结束时间
        currentPage: currentPage, // 当前页
        pageSize: this.pageSize // 页面数据长度
      }

      // 判断当前场景属于我的场景还是公共场景
      if (this.pageType === 'my') {
        _requestParams = {
          isPublic: 0, // 模板库类型值，0我的场景，1模板库
          sptemplid: params ? params.templateId : '', // 场景ID
          tmName: params ? params.templateContent : '', // 模板内容
          dsFlag: params ? params.templateType : '', // 模板类型(静态或动态)
          tmpType: params ? params.fuxinType : this.getInitTmpType(), // 富信类型
          auditStatus: params ? params.auditStatus : '', // 审核状态
          addtimeEnd: _endDate, // 创建筛选起始时间
          addtimeBeg: _startDate, // 创建筛选结束时间
          currentPage: currentPage, // 当前页
          pageSize: this.pageSize // 页面数据长度
        }
      } else if (this.pageType === 'common') {
        _requestParams = {
          isPublic: 1, // 模板库类型值，0我的场景，1模板库
          sptemplid: params ? params.templateId : '', // 场景ID
          tmName: params ? params.templateContent : '', // 模板内容
          tmpType: params ? params.fuxinType : this.getInitTmpType(), // 富信类型
          currentPage: currentPage, // 当前页
          pageSize: this.pageSize, // 页面数据长度
          useId: params ? params.useId : '', // 用途
          industryId: params ? params.industryId : '' // 行业
        }
      }
      // 执行列表数据获取
      this.getListData(_requestParams)
      // 设置参数
      this.currentPage = currentPage
      this.formCondition = params
    },

    // 获取列表数据
    getListData (params) {
      let _self = this
      // 获取列表信息
      actions.getListData(params, response => {
        _self.listData = response.data.data.list
        _self.totalPage = response.data.data.totalRecord
      }, errMsg => {
        _self.$message.error(errMsg)
      })
    },

    // 分页器切换
    handlePaginationChange (currentPage) {
      this.queryCondition(this.formCondition, currentPage)
    },

    // 初始化富信类型
    getInitTmpType () {
      let _editorConfig = this.modulePer
      if (_editorConfig.length > 1) {
        return ''
      } else {
        return this.modulePer[0].type
      }
    },

    // 删除模板
    deleCurrentSceneLi () {
      this.queryCondition(this.formCondition, 1)
    },

    // 文件导入前
    beforeHandleImport (file) {
      let _fileName = file.name
      if (!/\.(mrcsl|rms)$/.test(_fileName)) {
        this.$message.error('请导入格式为“.mrcsl”和“.rms”的文件！')
        return false
      } else {
        this.containerLoading = true
      }
    },

    // 文件导入失败
    handleImportError () {
      this.$message.error('文件导入失败！')
      this.containerLoading = false
    },

    // 文件导入成功
    handleImportSuccess (res, file) {
      this.containerLoading = false
      if (res.code === 200) {
        this.queryCondition()
        this.$message.success('文件导入成功！')
      } else {
        this.$message.error(res.msg)
      }
    }
  }
}
</script>

<style lang="less">
@import '../../libs/assets/less/variables';

/*分页样式*/
.page-bar {
  width: 100%;
  margin-top: 20px;
  text-align: right;
  .el-pagination.is-background .el-pager li:not(.disabled):hover {
    color: @green !important;
  }
  .number {
    font-weight: normal !important;
    background: #fff !important;
    border: 1px solid @border-dark !important;
  }
  .btn-prev,
  .btn-next {
    background: #fff !important;
    border: 1px solid @border-dark !important;
  }
  .more {
    font-weight: normal !important;
    background: #fff !important;
    border: 1px solid @border-dark !important;
  }
  .number:hover {
    color: @green !important;
  }
  .active {
    background: #fff !important;
    color: @green !important;
    border: 1px solid @green !important;
  }
  .el-pagination__sizes .el-input .el-input__inner:hover {
    border-color: @green !important;
  }
  .el-select .el-input.is-focus .el-input__inner {
    border-color: @green !important;
  }
  .el-select .el-input__inner:focus {
    border-color: @green !important;
  }
  .el-input.is-active .el-input__inner,
  .el-input__inner:focus {
    border-color: @green !important;
    outline: 0;
  }
}
.el-scrollbar {
  .selected {
    color: @green !important;
    font-weight: normal !important;
  }
}

@media screen and (max-width: 1500px) {
  .template-cont {
    width: 1100px;
  }
  .template-li{
    margin-right: 50px;
    margin-bottom: 24px;
    &:nth-child(4n) {
      margin-right: 0;
    }
  }
  .list-search-bar {
    .el-form--inline {
      .el-form-item {
        &:nth-child(4) {
          margin-right: 0;
        }
      }
    }
  }
}
@media screen and (min-width: 1500px) {
  .template-cont {
    width: 1560px;
  }
  .template-li{
    margin-right: 28px;
    margin-bottom: 24px;
    &:nth-child(6n) {
      margin-right: 0;
    }
  }
}
.template-cont{
  margin: 24px auto 40px;
  .template-list {
    margin-top: 20px;
  }
  .template-li {
    position: relative;
    float: left;
    &.template-border-li,
    .template-add-btn {
      width: 234px;
      height: 326px;
      border: 1px solid @border;
      box-shadow: 1px 2px 4px 0px rgba(0, 0, 0, .08);
      -o-box-shadow: 1px 2px 4px 0px rgba(0, 0, 0, .08);
      -ms-box-shadow: 1px 2px 4px 0px rgba(0, 0, 0, .08);
      -moz-box-shadow: 1px 2px 4px 0px rgba(0, 0, 0, .08);
      -webkit-box-shadow: 1px 2px 4px 0px rgba(0, 0, 0, .08);
    }
  }
}
</style>
