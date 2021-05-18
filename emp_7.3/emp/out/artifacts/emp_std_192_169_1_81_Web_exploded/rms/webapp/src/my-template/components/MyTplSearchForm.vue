<template>
  <div class="list-search-bar">
    <!-- 富信类型 -->
    <el-form
      :inline="true"
      ref="templateSearchForm"
      :model="templateSearchForm"
      :rules="formRules"
      class="tpl-search-form">
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
      <!-- 模板类型 -->
      <el-form-item label="模板类型">
        <el-select v-model="templateSearchForm.templateType">
          <el-option
            v-for="(item, index, key) in templateTypeSelect"
            :key="key"
            :label="item.label"
            :value="item.val">
          </el-option>
        </el-select>
      </el-form-item>
      <!-- 富信类型 -->
      <el-form-item
        v-if="modulePer.length > 1"
        label="富信类型">
        <el-select v-model="templateSearchForm.fuxinType">
          <el-option
            v-for="(item, index, key) in fuxinTypeOptions"
            :key="key"
            :label="item.label"
            :value="item.val">
          </el-option>
        </el-select>
      </el-form-item>
      <!-- 审核状态 -->
      <el-form-item label="审核状态">
        <el-select v-model="templateSearchForm.auditStatus">
          <el-option
            v-for="(item, index, key) in auditStatusSelect"
            :key="key"
            :label="item.label"
            :value="item.val">
          </el-option>
        </el-select>
      </el-form-item>
      <!-- 创建时间 -->
      <el-form-item label="创建时间">
        <el-date-picker
          v-model="templateSearchForm.createDate"
          type="daterange"
          start-placeholder="创建开始时间"
          end-placeholder="创建结束时间"
          :default-time="['00:00:00', '23:59:59']">
        </el-date-picker>
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
  </div>
</template>

<script>
const SELECTOPTION = {
  // 富信类型
  'fxType': [
    {
      label: '全部',
      val: ''
    },
    {
      label: '富文本',
      val: '13'
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
  ],
  // 模板类型
  'tplType': [
    {
      label: '全部',
      val: ''
    },
    {
      label: '动态模板',
      val: '1'
    },
    {
      label: '静态模板',
      val: '0'
    }
  ],
  // 审核状态
  'auditStatus': [
    {
      label: '全部',
      val: ''
    },
    {
      label: '草稿',
      val: '-1'
    },
    {
      label: '审核中',
      val: '3'
    },
    {
      label: '审核通过',
      val: '1'
    },
    {
      label: '审核未通过',
      val: '2'
    },
    {
      label: '禁用',
      val: '4'
    }
  ]
}
const formInitData = {
  templateId: '',
  templateContent: '',
  templateType: '',
  fuxinType: '',
  auditStatus: '',
  createDate: ''
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
      // 模板类型选项
      templateTypeSelect: SELECTOPTION.tplType,
      // 审核状态选项
      auditStatusSelect: SELECTOPTION.auditStatus,
      // 表单数据
      templateSearchForm: {...formInitData},
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
    // 重置
    resetForm () {
      this.templateSearchForm = {
        templateId: '',
        templateContent: '',
        templateType: '',
        fuxinType: '',
        auditStatus: '',
        createDate: ''
      }
    }
  }
}
</script>
