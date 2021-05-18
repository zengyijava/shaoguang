<template>
  <div class="list-search-bar">
    <el-form
      :inline="true"
      ref="sceneSearchForm"
      :model="sceneSearchForm"
      :rules="rules"
      class="scene-search-form">
      <!-- 富信类型 -->
      <el-form-item
        v-if="isShowSceneType()"
        :label="$t('sceneListSearchTypeSelect.description') + '：'">
        <el-select
          size="small"
          v-model="sceneSearchForm.sceneType"
          @change="changeSceneType">
          <el-option
            v-for="(item, index, key) in sceneTypeOption"
            :key="key"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <!-- 企业名称 -->
      <el-form-item
        v-if="currentSystem === 0"
        :label="$t('commonSceneSearchText.corpName') + '：'">
        <el-input
          size="small"
          v-model="sceneSearchForm.corpName"
          :placeholder="$t('sceneInputPlaceholderText')"></el-input>
      </el-form-item>
      <!-- 场景ID -->
      <el-form-item
        :label="$t('commonSceneSearchText.sceneId') + '：'"
        prop="sceneId">
        <el-input
          size="small"
          v-model.number="sceneSearchForm.sceneId"
          :placeholder="$t('sceneInputPlaceholderText')"
          :maxlength="32"></el-input>
      </el-form-item>
      <!-- 场景主题 -->
      <el-form-item :label="$t('commonSceneSearchText.sceneTitle') + '：'">
        <el-input
          size="small"
          v-model="sceneSearchForm.sceneTitle"
          :placeholder="$t('sceneInputPlaceholderText')"
          :maxlength="15"></el-input>
      </el-form-item>
      <!-- 查询 -->
      <el-form-item>
        <el-button type="primary" size="small" @click="queryCondition('sceneSearchForm')">{{ $t('sceneListQueryBtnText') }}</el-button>
      </el-form-item>
    </el-form>
    <!-- 行业 -->
    <div v-if="isIndustryData.length > 0" class="table-li clearfix">
      <p class="title">{{ $t('industryAndUseText.industryText') }}</p>
      <ul :class="[{'auto-height': moreIndustry}, 'list']">
        <li
          :class="{active: sceneSearchForm.industryId === ''}"
          @click="getIndustryOrUseResult('', 'industry')">
          {{ $t('industryAndUseText.allIndustryText') }}
        </li>
        <li
          v-for="(industryArr, key) in isIndustryData"
          :key="key"
          :class="{active: sceneSearchForm.industryId === industryArr.id}" @click="getIndustryOrUseResult(industryArr.id, 'industry')">
          {{ industryArr.name }}
        </li>
      </ul>
      <div
        v-if="isIndustryData.length > 8"
        :class="[{active: moreIndustry}, 'more-btn']"
        @click="(moreIndustry) ? moreIndustry = false : moreIndustry = true">
        <p class="desc">{{ $t('industryAndUseText.moreText') }}</p>
        <i class="more-icon"></i>
      </div>
    </div>
    <!-- 用途 -->
    <div v-if="isUseData.length > 0" class="table-li clearfix">
      <p class="title">{{ $t('industryAndUseText.useText') }}</p>
      <ul :class="[{'auto-height': moreUse}, 'list']">
        <li
          :class="{active: sceneSearchForm.useId === ''}"
          @click="getIndustryOrUseResult('', 'use')">
          {{ $t('industryAndUseText.allUseText') }}
        </li>
        <li
          v-for="(isUseArr, key) in isUseData"
          :key="key"
          :class="{active: sceneSearchForm.useId === isUseArr.id}"
          @click="getIndustryOrUseResult(isUseArr.id, 'use')">
          {{ isUseArr.name }}
        </li>
      </ul>
      <div
        v-if="isUseData.length > 8"
        :class="[{active: moreUse}, 'more-btn']"
        @click="(moreUse) ? moreUse = false : moreUse = true">
        <p class="desc">{{ $t('industryAndUseText.moreText') }}</p>
        <i class="more-icon"></i>
      </div>
    </div>
  </div>
</template>

<script>
import actions from '../../libs/api'

const initData = {
  corpName: '',
  useId: '',
  sceneId: '',
  sceneType: '',
  sceneTitle: '',
  industryId: ''
}

export default {
  name: 'CommonSearchForm',
  props: {
    modulePer: Array,
    currentSystem: Number
  },
  data () {
    let _sceneIdErrorMsg = this.$t('sceneIdErrorDescription')
    return {
      // 输入表单
      sceneSearchForm: {...initData},
      // 更多用途
      moreUse: false,
      // 更多行业
      moreIndustry: false,
      // 用途及行业
      useAndIndustry: [],
      // 表单校验规则
      rules: {
        sceneId: [
          {
            trigger: 'blur',
            validator: (rule, value, callback) => {
              if (value !== '') {
                let _reg = /^[0-9]*$/
                if (!_reg.test(value)) {
                  callback(new Error(_sceneIdErrorMsg))
                } else {
                  callback()
                }
              } else {
                callback()
              }
            }
          }
        ]
      }
    }
  },
  computed: {
    // 富信类型配置
    sceneTypeOption () {
      // 模板库不需要富文本
      let _editorConfig = this.modulePer.filter((item) => {
        return item.type !== 13
      })
      let _sceneTypeOptionLabel = this.$t('sceneListSearchTypeSelect.options')

      // 富信类型下拉选择全部选项值
      let _initSelectOption = {
        label: _sceneTypeOptionLabel.all.label,
        value: ''
      }
      let _sceneTypeOption = [_initSelectOption]

      for (let [i, len] = [0, _editorConfig.length]; i < len; i++) {
        let _optionObj = {}
        switch (_editorConfig[i].type) {
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
        _optionObj.value = _editorConfig[i].type
        _sceneTypeOption.push(_optionObj)
      }
      return _sceneTypeOption
    },

    // 用途数据
    isUseData: function () {
      let _UseArr = []
      let _useAndIndustryData

      if (this.useAndIndustry.length > 0) {
        _useAndIndustryData = this.useAndIndustry
      } else {
        return _UseArr
      }

      for (let i = 0; i < _useAndIndustryData.length; i++) {
        let _type = _useAndIndustryData[i].type

        if (_type === 1) {
          _UseArr.push(_useAndIndustryData[i])
        }
      }

      return _UseArr
    },

    // 行业数据
    isIndustryData: function () {
      let _industryArr = []
      let _useAndIndustryData

      if (this.useAndIndustry.length > 0) {
        _useAndIndustryData = this.useAndIndustry
      } else {
        return _industryArr
      }

      for (let i = 0; i < _useAndIndustryData.length; i++) {
        let _type = _useAndIndustryData[i].type

        if (_type === 0) {
          _industryArr.push(_useAndIndustryData[i])
        }
      }

      return _industryArr
    }
  },
  created () {
    let _editorConfig = this.modulePer
    if (_editorConfig.length === 1) {
      this.sceneSearchForm.sceneType = this.sceneTypeOption[1].value
      // 获取当前单个富信类型的行业和用途
      this.changeSceneType(this.sceneTypeOption[1].value)
    }
  },
  methods: {
    // 是否显示富信类型筛选
    isShowSceneType () {
      let _editorConfig = this.modulePer
      if (_editorConfig.length > 1) {
        return true
      } else {
        return false
      }
    },

    // 搜索结果
    queryCondition (sceneSearchForm) {
      let _self = this
      // 校验是否通过
      this.$refs[sceneSearchForm].validate((valid) => {
        if (valid) {
          _self.$emit('queryCondition', this.sceneSearchForm)
        } else {
          return false
        }
      })
    },

    // 切换场景类型
    changeSceneType (val) {
      let _getUseAndIndustryParams = {
        tmpType: val
      }
      let _self = this
      // 切换场景类型时会重新请求列表数据，故清空上一次表单输入结果以免造成数据混乱
      this.sceneSearchForm.corpName = ''
      this.sceneSearchForm.useId = ''
      this.sceneSearchForm.industryId = ''
      this.sceneSearchForm.sceneId = ''
      this.sceneSearchForm.sceneTitle = ''
      this.$emit('queryCondition', this.sceneSearchForm)

      actions.getUse(_getUseAndIndustryParams, response => {
        _self.useAndIndustry = response.data.data
      }, errMsg => {
        _self.useAndIndustry = []
        _self.$message.error(errMsg)
      })
    },

    // 获取行业或用途切换结果
    getIndustryOrUseResult: function (id, type) {
      let _self = this
      // 行业和用途切换会重新请求列表数据，所以需要校验相关表单输入是否人为修改
      this.$refs['sceneSearchForm'].validate((valid) => {
        if (valid) {
          if (type === 'industry') {
            _self.sceneSearchForm.industryId = id
          } else if (type === 'use') {
            _self.sceneSearchForm.useId = id
          }
          _self.$emit('queryCondition', _self.sceneSearchForm)
        } else {
          return false
        }
      })
    },

    // 重置
    resetForm () {
      this.sceneSearchForm = {
        corpName: '',
        useId: '',
        sceneId: '',
        sceneType: '',
        sceneTitle: '',
        industryId: ''
      }
    }
  }
}
</script>

<style lang="less" scoped>
@import "../../libs/assets/less/variables";
.table-li{
  width: 1100px;
  font-size: 12px;
  .title{
    width: 58px;
    margin-top: 7px;
    border-right: 1px solid @border;
  }
  .list{
    width: 990px;
    height: 31px;
    overflow: hidden;
    &.auto-height{
      height: auto;
    }
  }
  .list li{
    width: 72px;
    padding: 7px;
    margin-bottom: 7px;
    margin-left: 20px;
    text-align: center;
    cursor: pointer;
    &:hover{
      color: @old-green;
    }
    &.active{
      padding-bottom: 5px;
      border-bottom: 2px solid @old-green;
    }
  }
  .title,
  .list,
  .list li{
    float: left;
  }
  .more-btn{
    position: relative;
    width: 50px;
    margin-top: 7px;
    float: right;
    cursor: pointer;
    .desc{
      margin-top: 0;
      margin-bottom: 0;
    }
    &:hover{
      color: @old-green;
    }
    &.active{
      color: @old-green;
      .more-icon{
        background: url("../assets/img/arrow_two.png")no-repeat center center;
      }
    }
    .more-icon{
      position: absolute;
      top: 2px;
      right: 0;
      z-index: 2;
      width: 10px;
      height: 10px;
      display: block;
      background: url("../assets/img/arrow_one.png")no-repeat center center;
    }
  }
}
</style>
