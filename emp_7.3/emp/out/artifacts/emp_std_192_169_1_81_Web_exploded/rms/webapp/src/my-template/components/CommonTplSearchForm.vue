<template>
  <div class="list-search-bar">
    <!-- 富信类型 -->
    <el-form
      :inline="true"
      ref="templateSearchForm"
      :model="templateSearchForm"
      :rules="formRules"
      class="tpl-search-form">
      <!-- 富信类型 -->
      <el-form-item
        v-if="modulePer.length > 1"
        label="富信类型">
        <el-select
          v-model="templateSearchForm.fuxinType"
          @change="changeSceneType">
          <el-option
            v-for="(item, index, key) in fuxinTypeOptions"
            :key="key"
            :label="item.label"
            :value="item.val">
          </el-option>
        </el-select>
      </el-form-item>
      <!-- 模板ID -->
      <el-form-item
        label="模板ID："
        prop="templateId">
        <el-input
          placeholder="请输入模板ID"
          v-model="templateSearchForm.templateId"
          :maxlength="32">
        </el-input>
      </el-form-item>
      <!-- 模板内容 -->
      <el-form-item
        label="模板内容：">
        <el-input
          placeholder="请输入模板内容"
          v-model="templateSearchForm.templateContent"
          :maxlength="15">
        </el-input>
      </el-form-item>
      <!-- 查询按钮 -->
      <el-form-item>
        <el-button
          type="primary"
          @click="queryCondition('templateSearchForm')">
          查询
        </el-button>
      </el-form-item>
    </el-form>
    <!-- 行业 -->
    <div
      v-if="isIndustryData.length > 0"
      class="table-li clearfix">
      <p class="title">行业</p>
      <ul :class="[{'auto-height': moreIndustry}, 'list']">
        <li
          :class="{active: templateSearchForm.industryId === ''}"
          @click="getIndustryOrUseResult('', 'industry')">
          全部行业
        </li>
        <li
          v-for="(industryArr, key) in isIndustryData"
          :key="key"
          :class="{active: templateSearchForm.industryId === industryArr.id}" @click="getIndustryOrUseResult(industryArr.id, 'industry')">
          {{ industryArr.name }}
        </li>
      </ul>
      <div
        v-if="isIndustryData.length > 8"
        :class="[{active: moreIndustry}, 'more-btn']"
        @click="(moreIndustry) ? moreIndustry = false : moreIndustry = true">
        <p class="desc">更多</p>
        <i class="more-icon"></i>
      </div>
    </div>
    <!-- 用途 -->
    <div
      v-if="isUseData.length > 0"
      class="table-li clearfix">
      <p class="title">用途</p>
      <ul :class="[{'auto-height': moreUse}, 'list']">
        <li
          :class="{active: templateSearchForm.useId === ''}"
          @click="getIndustryOrUseResult('', 'use')">
          全部用途
        </li>
        <li
          v-for="(isUseArr, key) in isUseData"
          :key="key"
          :class="{active: templateSearchForm.useId === isUseArr.id}"
          @click="getIndustryOrUseResult(isUseArr.id, 'use')">
          {{ isUseArr.name }}
        </li>
      </ul>
      <div
        v-if="isUseData.length > 8"
        :class="[{active: moreUse}, 'more-btn']"
        @click="(moreUse) ? moreUse = false : moreUse = true">
        <p class="desc">更多</p>
        <i class="more-icon"></i>
      </div>
    </div>
  </div>
</template>

<script>
import actions from '../../libs/api'

const SELECTOPTION = {
  // 富信类型
  'fxType': [
    {
      label: '全部',
      val: ''
    },
    {
      label: '图文',
      val: '11'
    },
    {
      label: '交互',
      val: '12'
    },
    {
      label: '企业秀',
      val: '15'
    }
  ]
}
const formInitData = {
  templateId: '',
  templateContent: '',
  fuxinType: '',
  useId: '',
  industryId: ''
}

export default {
  name: 'MyTplSearchForm',
  props: {
    modulePer: Array
  },
  data () {
    return {
      // 富信类型选项
      fxSelect: SELECTOPTION.fxType,
      // 表单数据
      templateSearchForm: {...formInitData},
      // 更多用途
      moreUse: false,
      // 更多行业
      moreIndustry: false,
      // 用途及行业
      useAndIndustry: [],
      // 表单校验
      formRules: {
        // 模板ID校验
        templateId: [
          {
            trigger: 'blur',
            validator: (rule, value, callback) => {
              if (value !== '') {
                // 模板ID不为空的请况下必须为正整数
                let _reg = /^[0-9]*$/
                if (!_reg.test(value)) {
                  callback(new Error('请填写正确的模板ID'))
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
    fuxinTypeOptions () {
      let _self = this
      let _optionArr = []
      _optionArr.push(_self.fxSelect[0])
      // 从权限配置中读取对应配置
      this.modulePer.forEach((key) => {
        _self.fxSelect.forEach((index) => {
          if (+key.type === +index.val) {
            _optionArr.push(index)
          }
        })
      })
      return _optionArr
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
      this.templateSearchForm.fuxinType = this.modulePer[0].type
      // 获取当前单个富信类型的行业和用途
      this.changeSceneType(this.modulePer[0].type)
    } else {
      // 获取当前单个富信类型的行业和用途
      this.changeSceneType('')
    }
  },
  methods: {
    // 搜索结果
    queryCondition (searchForm) {
      let _self = this
      // 校验是否通过
      this.$refs[searchForm].validate((valid) => {
        if (valid) {
          // 单个编辑器时富信类型取配置的值
          if (this.modulePer.length === 1) {
            this.templateSearchForm.fuxinType = this.modulePer[0].type
          }
          _self.$emit('queryCondition', this.templateSearchForm)
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
      this.templateSearchForm.useId = ''
      this.templateSearchForm.industryId = ''
      this.templateSearchForm.templateId = ''
      this.templateSearchForm.templateContent = ''
      this.$emit('queryCondition', this.templateSearchForm)

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
      this.$refs['templateSearchForm'].validate((valid) => {
        if (valid) {
          if (type === 'industry') {
            _self.templateSearchForm.industryId = id
          } else if (type === 'use') {
            _self.templateSearchForm.useId = id
          }
          _self.$emit('queryCondition', _self.templateSearchForm)
        } else {
          return false
        }
      })
    },

    // 重置
    resetForm () {
      this.templateSearchForm = {
        templateId: '',
        templateContent: '',
        fuxinType: '',
        useId: '',
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
      color: @blue-border;
    }
    &.active{
      padding-bottom: 5px;
      border-bottom: 2px solid @blue-border;
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
      color: @blue-border;
    }
    &.active{
      color: @blue-border;
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
