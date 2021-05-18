<template>
  <div class="cont clearfix" v-loading="listLoad">
    <!-- 列表 -->
    <div class="list-cont">
      <div class="clearfix">
        <pop-model-li
          v-for="(dataItem, key) in modelLiData"
          :key="key"
          :modelData="dataItem"
          @immediateUse="immediateUse">
        </pop-model-li>
      </div>
      <!-- 加载更多 -->
      <div class="load-more"
        v-show="!busy"
        v-infinite-scroll="scrollLoadMore"
        infinite-scroll-disabled="busy"
        infinite-scroll-distance="0"
        v-loading="infiniteLoading"
        element-loading-spinner="el-icon-loading">
      </div>
      <!-- 列表为空 -->
      <div v-if="modelLiData.length == 0" class="list-none">
        <div class="list-none-img"></div>
        <p>{{ $t('public.noHaveTmp') }}</p>
      </div>
    </div>
    <!-- 搜索区 -->
    <div class="search">
      <!-- 富信类型选择 -->
      <el-select
        v-if="isShowFXType"
        size="small"
        v-model="selectFXType"
        @change="changeFxType">
        <el-option
          v-for="(item, key) in isFXSelectOption"
          :key="key"
          :label="item.label"
          :value="item.value">
        </el-option>
      </el-select>
      <!-- 场景ID -->
      <el-input
        size="small"
        v-model="sceneID"
        :placeholder="$t('public.enterId')">
      </el-input>
      <!-- 场景名称 -->
      <el-input
        size="small"
        v-model="sceneName"
        :placeholder="$t('public.enterName')">
      </el-input>
      <!-- 场景类型 -->
      <!-- h5没有场景类型选择 -->
      <el-select
        v-if="selectFXType !== 15"
        size="small"
        v-model="selectSceneType">
        <el-option
          v-for="(item, key) in isSceneTypeSelectOption"
          :key="key"
          :label="item.label"
          :value="item.value">
        </el-option>
      </el-select>
      <!-- 查询 -->
      <div
        v-if="isShowAddBtn"
        class="clearfix pop-btn-box">
        <el-button class="button-w110" type="primary" size="small" @click="queryList">{{ $t('sceneListQueryBtnText') }}</el-button>
        <el-button class="button-w76" size="small" @click="isNewBtnAction">{{ $t('public.addTmp') }}</el-button>
      </div>
      <el-button
        v-else
        class="button-full"
        type="primary"
        size="small"
        @click="queryList">
        {{ $t('sceneListQueryBtnText') }}
      </el-button>
      <!-- 行业 -->
      <div
        v-if="pageType === 'common' && isIndustryData.length > 0"
        class="filtrate-list">
        <h4 class="title">
          {{ $t('industryAndUseText.industryText') }}
        </h4>
        <p
          :class="[{active: industryActiveId === ''}, 'sub-title']"
          @click="changeIndustry('')">
          <span>{{ $t('industryAndUseText.allIndustryText') }}</span>
        </p>
        <ul class="items clearfix">
          <li
            v-for="(industryArr, key) in isIndustryData"
            :key="key"
            :class="{active: industryActiveId === industryArr.id}"
            @click="changeIndustry(industryArr.id)">
            {{ industryArr.name }}
          </li>
        </ul>
      </div>
      <!-- 用途 -->
      <div
        v-if="pageType === 'common' && isUseData.length > 0"
        class="filtrate-list">
        <h4 class="title">
          {{ $t('industryAndUseText.useText') }}
        </h4>
        <p
          :class="[{active: useActiveId === ''}, 'sub-title']"
          @click="changeUse('')">
          <span>{{ $t('industryAndUseText.allUseText') }}</span>
        </p>
        <ul class="items clearfix">
          <li
            v-for="(isUseArr, key) in isUseData"
            :key="key"
            :class="{active: useActiveId === isUseArr.id}"
            @click="changeUse(isUseArr.id)">
            {{ isUseArr.name }}
          </li>
        </ul>
      </div>
    </div>
    <!-- 新建弹出层 -->
    <el-dialog
      v-if="isShowAddArea"
      top="12vh"
      :class="['add-template-dialog', addDialogWidthClass]"
      :visible.sync="addDialogVisible"
      append-to-body>
      <p slot="title">{{ $t('sceneListAdd.addPopTitle') }}</p>
      <div class="content clearfix">
        <!-- 富文本 -->
        <div
          v-if="showEditorLinkArea(13)"
          class="add-li"
          @click="linkToEditor(13)">
          <div class="img">
            <img v-if="currentLang === 'zh_CN'" src="../assets/img/add-scene/list_preview_text_cn.png">
            <img v-else-if="currentLang === 'zh_TW'" src="../assets/img/add-scene/list_preview_text_hant.png">
            <img v-else-if="currentLang === 'zh_HK'" src="../assets/img/add-scene/list_preview_text_en.png">
          </div>
          <h4 class="add-li-title">{{ $t('sceneListAdd.addTextTitle') }}</h4>
          <p class="add-desc">{{ $t('sceneListAdd.addTextDesc') }}</p>
        </div>
        <!-- 富媒体 -->
        <div
          v-if="showEditorLinkArea(11)"
          class="add-li"
          @click="linkToEditor(11)">
          <div class="img">
            <img v-if="currentLang === 'zh_CN'" src="../assets/img/add-scene/list_preview_media_cn.png">
            <img v-else-if="currentLang === 'zh_TW'" src="../assets/img/add-scene/list_preview_media_hant.png">
            <img v-else-if="currentLang === 'zh_HK'" src="../assets/img/add-scene/list_preview_media_en.png">
          </div>
          <h4 class="add-li-title">{{ $t('sceneListAdd.addMediaTitle') }}</h4>
          <p class="add-desc">{{ $t('sceneListAdd.addMediaDesc') }}</p>
        </div>
        <!-- 场景 -->
        <div
          v-if="showEditorLinkArea(12)"
          class="add-li"
          @click="linkToEditor(12)">
          <div class="img">
            <img v-if="currentLang === 'zh_CN'" src="../assets/img/add-scene/list_preview_card_cn.png">
            <img v-else-if="currentLang === 'zh_TW'" src="../assets/img/add-scene/list_preview_card_hant.png">
            <img v-else-if="currentLang === 'zh_HK'" src="../assets/img/add-scene/list_preview_card_en.png">
          </div>
          <h4 class="add-li-title">{{ $t('sceneListAdd.addSceneTitle') }}</h4>
          <p class="add-desc">{{ $t('sceneListAdd.addSceneDesc') }}</p>
        </div>
        <!-- h5 -->
        <div
          v-if="showEditorLinkArea(15)"
          class="add-li"
          @click="linkToEditor(15)">
          <div class="img">
            <img v-if="currentLang === 'zh_CN'" src="../assets/img/add-scene/list_preview_h5_cn.png">
            <img v-else-if="currentLang === 'zh_TW'" src="../assets/img/add-scene/list_preview_h5_hant.png">
            <img v-else-if="currentLang === 'zh_HK'" src="../assets/img/add-scene/list_preview_h5_en.png">
          </div>
          <h4 class="add-li-title">{{ $t('sceneListAdd.addH5Title') }}</h4>
          <p class="add-desc">{{ $t('sceneListAdd.addH5Desc') }}</p>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import infiniteScroll from 'vue-infinite-scroll'
import PopModelLi from './property/PopModelLi'
import actions from '../api'
import config from '../config'
import utils from '../utils'

export default {
  name: 'PopListCont',
  components: {
    'pop-model-li': PopModelLi
  },
  props: {
    // 场景类型
    sceneType: {
      type: String,
      default: ''
    },
    // 弹层列表所在结构:"editor"编辑器内。"outside"外，编辑器内部不需要新建按钮，外部则需要根据权限判断
    callFrom: String,
    // 弹层类型，"my"我的场景，"common"模板库
    pageType: String,
    auditStatus: Number
  },
  data: function () {
    return {
      // 用户信息
      userInfo: {},
      // 显示富信类型
      isShowFXType: false,
      // 显示新增按钮
      isShowAddBtn: false,
      // 显示新增区域
      isShowAddArea: false,
      // 富信类型选择值
      selectFXType: '',
      // 场景ID
      sceneID: '',
      // 场景名称
      sceneName: '',
      // 场景类型
      selectSceneType: '',
      // 场景类型下拉选项
      isSceneTypeSelectOption: [
        {
          label: this.$t('mySceneSearchText.templateTypeSelect.options.all.label') + this.$t('mySceneSearchText.templateTypeSelect.description'),
          value: ''
        },
        {
          label: this.$t('mySceneSearchText.templateTypeSelect.options.static.label'),
          value: 0
        },
        {
          label: this.$t('mySceneSearchText.templateTypeSelect.options.dynamic.label'),
          value: 1
        }
      ],
      // 列表加载
      listLoad: true,
      // 列表数据
      modelLiData: [],
      // 滚动加载选项
      busy: true,
      // 无线加载动画
      infiniteLoading: true,
      // 当前页
      currentPage: 0,
      // 页面数据长度
      pageSize: 10,
      // 页面总是
      pageTotal: 1,
      // 行业和用途数据
      industryAndUseData: [],
      // 选中的行业ID
      industryActiveId: '',
      // 选中的用途ID
      useActiveId: '',
      // 新增弹层
      addDialogVisible: false
    }
  },
  created: function () {
    // 执行用户信息获取
    this.getUserInfos()
  },
  computed: {
    // 当前所属语言
    currentLang () {
      return utils.getUrlParameters('lang', false, config.GET_URL_PARAMS.POPLIST) || 'zh_CN'
    },

    // 富信类型下拉选项
    isFXSelectOption () {
      let _modulePer = this.userInfo.modulePer
      let _sceneTypeOptionLabel = this.$t('sceneListSearchTypeSelect.options')
      // 富信类型下拉选择全部选项值
      let _initSelectOption = {
        label: _sceneTypeOptionLabel.all.label,
        value: ''
      }
      let _sceneTypeOption = [_initSelectOption]

      _modulePer.forEach(item => {
        let _optionObj = {}
        switch (item.type) {
          case 11:
            _optionObj.label = _sceneTypeOptionLabel.media.label
            break
          case 12:
            _optionObj.label = _sceneTypeOptionLabel.scene.label
            break
          case 13:
            _optionObj.label = _sceneTypeOptionLabel.text.label
            break
          case 15:
            _optionObj.label = _sceneTypeOptionLabel.h5.label
            break
        }
        _optionObj.value = item.type
        _sceneTypeOption.push(_optionObj)
      })
      return _sceneTypeOption
    },

    // 行业数据
    isIndustryData: function () {
      let industryArr = []
      let useData

      if (this.industryAndUseData.length > 0) {
        useData = this.industryAndUseData
      } else {
        return industryArr
      }

      for (let i = 0; i < useData.length; i++) {
        let useType = useData[i].type

        if (useType === 0) {
          industryArr.push(useData[i])
        }
      }

      return industryArr
    },

    // 用途数据
    isUseData: function () {
      let UseArr = []
      let useData

      if (this.industryAndUseData.length > 0) {
        useData = this.industryAndUseData
      } else {
        return UseArr
      }

      for (let i = 0; i < useData.length; i++) {
        let useType = useData[i].type

        if (useType === 1) {
          UseArr.push(useData[i])
        }
      }

      return UseArr
    },

    // 根据编辑器配置显示新增弹层区宽度
    addDialogWidthClass () {
      let _modulePerLen = this.userInfo.modulePer.length
      if (_modulePerLen === 2) {
        return 'add-dialog-w2'
      } else if (_modulePerLen === 3) {
        return 'add-dialog-w3'
      } else if (_modulePerLen === 4) {
        return 'add-dialog-w4'
      }
    }
  },
  methods: {
    // 获取用户信息,包括权限和富信类型信息
    getUserInfos () {
      let _self = this
      actions.getUserInfos('', response => {
        _self.userInfo = response.data.data
        // 根据获取权限执行对应界面显示
        _self.setPanelDom()
        // 获取行业和用途数据
        _self.getIndustryAndUseData()
      }, errMsg => {
        _self.$message.error(errMsg)
      })
    },

    // 根据权限设对应界面
    setPanelDom () {
      let _self = this
      /*
      ** 内部编辑器没有富信类型选择，外部只有配置文件中配置类型为多个时才会有
      ** 新增按钮和新增区域只有外部才会有
      */
      if (this.callFrom === 'outside') {
        let _modulePer = this.userInfo.modulePer
        let _roleArr = this.userInfo.loginCorp.roleArr
        if (!_modulePer || _modulePer.length < 1) {
          this.$message.error('请联系管理员添加相应模板权限！')
          return
        }
        for (let [i, roleArrLen] = [0, _roleArr.length]; i < roleArrLen; i++) {
          if (_roleArr[i].type === _self.pageType && _roleArr[i].add === '1') {
            _self.isShowAddBtn = true
            break
          } else {
            _self.isShowAddBtn = false
          }
        }
        if (_modulePer.length > 1) {
          this.selectFXType = ''
          this.isShowAddArea = true
          this.isShowFXType = true
        } else {
          this.selectFXType = _modulePer[0].type
          this.isShowAddArea = false
          this.isShowFXType = false
        }
      } else if (this.callFrom === 'editor') {
        this.selectFXType = Number(this.sceneType)
        this.isShowAddBtn = false
        this.isShowAddArea = false
        this.isShowFXType = false
      }
      this.busy = false
    },

    // 富信类型下拉
    changeFxType () {
      this.industryActiveId = ''
      this.useActiveId = ''
      this.getIndustryAndUseData()
    },

    // 查询列表
    queryList: function () {
      if (this.checkSceneID()) {
        this.modelLiData = []
        this.searchModelList('rest')
      }
    },

    // 校验场景ID必须为数字
    checkSceneID: function () {
      let _reg = /^[0-9]*$/
      let _errMsg = this.$t('sceneIdErrorDescription')
      if (this.sceneID && !_reg.test(this.sceneID)) {
        this.$message.error(_errMsg)
        return false
      } else {
        return true
      }
    },

    // 行业用途数据获取
    getIndustryAndUseData: function () {
      let _self = this
      let _geuUseParams
      // 只有模板库才需要获取行业和用途
      if (this.pageType === 'common') {
        if (this.callFrom === 'outside') {
          let _modulePer = this.userInfo.modulePer

          if (_modulePer.length > 1) {
            _geuUseParams = {
              tmpType: this.selectFXType
            }
          } else {
            _geuUseParams = {
              tmpType: _modulePer[0].type
            }
          }
        } else if (this.callFrom === 'editor') {
          _geuUseParams = {
            tmpType: Number(this.sceneType)
          }
        }
        // 获取用途和行业
        actions.getUse(_geuUseParams, response => {
          _self.industryAndUseData = response.data.data
        }, errMsg => {
          _self.$message.error(errMsg)
        })
      }
    },

    // 类表数据获取
    // @params {String} type为add数据push，reset数据重取
    searchModelList: function (type) {
      let _getListParams = {}
      let _self = this
      let _auditStatus

      this.listLoad = true

      if (this.auditStatus === 0) {
        _auditStatus = ''
      } else {
        _auditStatus = this.auditStatus
      }

      if (type === 'add') {
        this.currentPage++
      } else {
        this.currentPage = 1
      }

      if (this.pageType === 'my') {
        _getListParams = {
          currentPage: this.currentPage,
          pageSize: this.pageSize,
          tmpType: this.selectFXType,
          tmName: this.sceneName,
          sptemplid: this.sceneID,
          isPublic: 0,
          auditStatus: _auditStatus,
          dsFlag: this.selectSceneType,
          tmState: 1,
          industryId: this.industryActiveId,
          useId: this.useActiveId,
          source: 0,
          containShare: 0,
          isMaterial: 0
        }
      } else if (this.pageType === 'rcos') {
        _getListParams = {
          currentPage: this.currentPage,
          pageSize: this.pageSize,
          tmpType: this.selectFXType,
          tmName: this.sceneName,
          sptemplid: this.sceneID,
          isPublic: 1,
          auditStatus: _auditStatus,
          dsFlag: this.selectSceneType,
          tmState: 1,
          industryId: this.industryActiveId,
          useId: this.useActiveId,
          source: 3,
          containShare: 0,
          isMaterial: 0
        }
      } else if (this.pageType === 'common') {
        _getListParams = {
          currentPage: this.currentPage,
          pageSize: this.pageSize,
          tmpType: this.selectFXType,
          tmName: this.sceneName,
          sptemplid: this.sceneID,
          isPublic: 1,
          auditStatus: _auditStatus,
          dsFlag: this.selectSceneType,
          tmState: 1,
          industryId: this.industryActiveId,
          useId: this.useActiveId,
          source: 0,
          containShare: 0,
          isMaterial: 1
        }
      }
      // 获取列表
      actions.getListData(_getListParams, response => {
        _self.pageTotal = response.data.data.totalPage

        // 判断是加载更多还是重新获取
        if (type === 'add') {
          _self.modelLiData.push(...response.data.data.list)
        } else if (type === 'rest') {
          _self.modelLiData = response.data.data.list
        }
        // 判断是否显示加载更多
        if (_self.currentPage >= _self.pageTotal) {
          _self.busy = true
        } else {
          _self.busy = false
        }
        _self.listLoad = false
      }, errMsg => {
        _self.$message.error(errMsg)
        _self.listLoad = false
      })
    },

    // 新建按钮操作
    isNewBtnAction: function () {
      let _modulePer = this.userInfo.modulePer
      if (_modulePer.length > 1) {
        this.addDialogVisible = true
      } else {
        this.linkToEditor(_modulePer[0].type)
      }
    },

    // 行业查询
    changeIndustry: function (val) {
      this.industryActiveId = val
      this.searchModelList('rest')
    },

    // 用途查询
    changeUse: function (val) {
      this.useActiveId = val
      this.searchModelList('rest')
    },

    // 滚动加更多
    scrollLoadMore () {
      this.searchModelList('add')
    },

    // 立即使用
    immediateUse: function (id) {
      this.$emit('immediateUse', id)
    },

    // 显示编辑器链接入口
    showEditorLinkArea (type) {
      let _editorConfig = this.userInfo.modulePer
      if (_editorConfig.find((key) => key.type === type)) {
        return true
      } else {
        return false
      }
    },

    // 跳转到编辑器
    linkToEditor (editorType) {
      let _lang = utils.getUrlParameters('lang', false, config.GET_URL_PARAMS.POPLIST) || 'zh_CN'
      let _isPublicVal = (this.pageType === 'my' || this.pageType === 'rcos') ? 0 : 1
      let _attrParams = 'isPublic=' + _isPublicVal + '&lang=' + _lang + ''
      let _locationUrl

      this.addDialogVisible = false
      if (editorType === 12) {
        _locationUrl = config.SCENE_EDITOR_LINK
      } else if (editorType === 11) {
        _locationUrl = config.MEDIA_EDITOR_LINK
      } else if (editorType === 13) {
        _locationUrl = config.TEXT_EDITOR_LINK
      } else if (editorType === 15) {
        _locationUrl = config.H5_EDITOR_LINK
      }
      window.open(_locationUrl + '?' + _attrParams, '_blank')
    }
  },
  directives: {infiniteScroll}
}
</script>

<style lang="less" scope>
@import '../../libs/assets/less/variables';
.el-select-dropdown__item{
  font-size: 12px;
}
.cont{
  width: 100%;
  text-align: left;
  h1,h2,h3,h4,h5,h6,
  p{
    margin: 0;
    padding: 0;
    font-weight: normal
  }
  .list-cont,
  .search{
    float: left;
  }
  .list-cont{
    width: 746px;
    padding: 25px 26px 0 38px;
    height: 695px;
    overflow-y: auto;
    .list-none{
      width: 100%;
      height: 100%;
      overflow: hidden;
      text-align: center;
      p{
        font-size: 16px;
      }
      .list-none-img{
        width: 214px;
        height: 190px;
        margin: 120px auto 26px;
        background: url('../assets/img/list_none_icon.png') no-repeat;
      }
    }
  }
  .search{
    width: 197px;
    height: 681px;
    padding: 20px 14px;
    background: @white;
    overflow-y: auto;
    .button-full{
      width: 100%;
      margin-top: 8px;
      font-size: 12px;
    }
  }
  .pop-btn-box{
    margin-top: 14px;
    .button-w110{
      width: 100px;
      float: left;
    }
    .button-w76{
      width: 76px;
      float: right;
    }
    .el-button--default{
      color: @green;
      border: 1px solid @green;
    }
  }
  .el-input{
    margin-top: 8px;
    font-size: 12px;
  }
  .el-select{
    width: 100%;
  }
  .filtrate-list{
    text-align: left;
    margin-top: 20px;
    .title{
      padding: 10px 13px;
      font-size: 14px;
      border-bottom: 1px solid @border;
    }
    .sub-title{
      font-size: 12px;
      padding: 10px 13px 0;
      color: @grey-black;
      &.active,
      &:hover{
        span{
          color: @green;
        }
      }
      span{
        cursor: pointer;
      }
    }
    .items{
      li{
        width: 70px;
        padding-left: 13px;
        padding-right: 13px;
        margin: 16px 0 0;
        float: left;
        font-size: 12px;
        text-align: left;
        color: @grey-black;
        cursor: pointer;
        &:hover,
        &.active{
          color: @green;
        }
      }
    }
  }
  .load-more{
    height: 20px;
    .el-icon-loading{
      display: inline-block;
    }
  }
}
.add-template-dialog {
  &.add-dialog-w2{
    .el-dialog{
      width: 692px;
    }
  }
  &.add-dialog-w3{
    .el-dialog{
      width: 1006px;
    }
  }
  &.add-dialog-w4{
    .el-dialog{
      width: 1320px;
    }
  }
  .el-dialog{
    border-radius: 4px;
    -o-border-radius: 4px;
    -ms-border-radius: 4px;
    -moz-border-radius: 4px;
    -webkit-border-radius: 4px;
    overflow: hidden;
  }
  .el-dialog__header{
    padding: 18px 24px;
    background-color: @dialog-header-bg;
    p{
      margin: 0;
      font-size: 14px;
      line-height: 1;
      color: @grey-black;
    }
    .el-dialog__headerbtn{
      top: 14px;
    }
  }
  .el-dialog__body{
    padding: 72px 45px 110px;
  }
  .add-li{
    width: 268px;
    padding: 10px;
    float: left;
    margin-right: 26px;
    font-size: 14px;
    line-height: 1;
    cursor: pointer;
    &:last-child{
      margin-right: 0;
    }
    &:hover{
      box-shadow: 1px 3px 9px 0px rgba(0, 0, 0, 0.3);
      -o-box-shadow: 1px 3px 9px 0px rgba(0, 0, 0, 0.3);
      -ms-box-shadow: 1px 3px 9px 0px rgba(0, 0, 0, 0.3);
      -moz-box-shadow: 1px 3px 9px 0px rgba(0, 0, 0, 0.3);
      -webkit-box-shadow: 1px 3px 9px 0px rgba(0, 0, 0, 0.3);
    }
    .img{
      width: 266px;
      height: 316px;
    }
    .add-li-title{
      margin: 22px 0 0;
      font-weight: normal;
      color: @grey-dark;
    }
    .add-desc{
      margin: 9px 0 0;
      color: @grey;
    }
  }
}
</style>
