<template>
  <div class="list-search-bar">
    <!-- 富信类型 -->
    <el-form
      :inline="true"
      ref="sceneSearchForm"
      :model="sceneSearchForm"
      :rules="rules"
      class="scene-search-form">
      <el-form-item
        v-if="isShowSceneType()"
        :label="$t('sceneListSearchTypeSelect.description') + '：'">
        <el-select size="small"
          v-model="sceneSearchForm.sceneType">
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
          v-model="sceneSearchForm.sceneId"
          :placeholder="$t('sceneInputPlaceholderText')"></el-input>
      </el-form-item>
      <!-- 场景主题 -->
      <el-form-item :label="$t('commonSceneSearchText.sceneTitle') + '：'">
        <el-input
          size="small"
          v-model="sceneSearchForm.sceneTitle"
          :placeholder="$t('sceneInputPlaceholderText')"></el-input>
      </el-form-item>
      <!-- 场景状态 -->
      <el-form-item
        :label="$t('mySceneSearchText.sceneStatusSelect.description') + '：'">
        <el-select size="small" v-model="sceneSearchForm.sceneStatus">
          <el-option
            v-for="(item, index, key) in sceneStatusOption"
            :key="key"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <!-- 场景类型 -->
      <el-form-item
        v-if="isShowTemplateType()"
        :label="$t('mySceneSearchText.templateTypeSelect.description') + '：'">
        <el-select size="small" v-model="sceneSearchForm.templateType">
          <el-option
            v-for="(item, index, key) in templateTypeOption"
            :key="key"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <!-- 审核状态 -->
      <el-form-item :label="$t('mySceneSearchText.auditStatusSelect.description') + '：'">
        <el-select size="small" v-model="sceneSearchForm.auditStatus">
          <el-option
            v-for="(item, index, key) in auditStatusOption"
            :key="key"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <!-- 创建时间 -->
      <el-form-item :label="$t('mySceneSearchText.createDate') + '：'">
        <el-date-picker
          v-model="sceneSearchForm.createDate"
          type="daterange"
          size="small"
          :start-placeholder="$t('mySceneSearchText.startDate')"
          :end-placeholder="$t('mySceneSearchText.endDate')"
          :default-time="['00:00:00', '23:59:59']">
        </el-date-picker>
      </el-form-item>
      <!-- 查询 -->
      <el-form-item>
        <el-button
          type="primary"
          size="small"
          @click="queryCondition('sceneSearchForm')">
          {{ $t('sceneListQueryBtnText') }}
        </el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
const initData = {
  corpName: '',
  sceneId: '',
  sceneType: '',
  createDate: '',
  sceneTitle: '',
  sceneStatus: '',
  auditStatus: '',
  templateType: ''
}

export default {
  name: 'RcosSearchForm',
  props: {
    modulePer: Array,
    currentSystem: Number
  },
  data () {
    let _sceneIdErrorMsg = this.$t('sceneIdErrorDescription')
    return {
      sceneSearchForm: {...initData},
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
      let _editorConfig = this.modulePer
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

    // 场景状态下拉选项
    sceneStatusOption () {
      let _sceneStatusOptionLabel = this.$t('mySceneSearchText.sceneStatusSelect.options')
      let _sceneStatusOptionVal = {
        all: {value: ''},
        start: {value: '1'},
        forbid: {value: '0'},
        draft: {value: '2'}
      }
      // 为对应的option添加value值
      for (let key in _sceneStatusOptionLabel) {
        this.$set(_sceneStatusOptionLabel[key], 'value', _sceneStatusOptionVal[key].value)
      }

      return _sceneStatusOptionLabel
    },

    // 场景类型下拉选项
    templateTypeOption () {
      let _templateTypeOptionLabel = this.$t('mySceneSearchText.templateTypeSelect.options')
      let _templateTypeOptionVal = {
        all: {value: ''},
        static: {value: '0'},
        dynamic: {value: '1'}
      }
      // 为对应的option添加value值
      for (let key in _templateTypeOptionLabel) {
        this.$set(_templateTypeOptionLabel[key], 'value', _templateTypeOptionVal[key].value)
      }

      return _templateTypeOptionLabel
    },

    // 审核状态下拉选项
    auditStatusOption () {
      return {
        all: {
          label: this.$t('mySceneSearchText.auditStatusSelect.options.all.label'),
          value: ''
        },
        forbid: {
          label: this.$t('mySceneSearchText.auditStatusSelect.options.forbid.label'),
          value: '4'
        }
      }
    }
  },
  methods: {
    // 是否显示富信类型筛选
    isShowSceneType () {
      let _editorConfig = this.modulePer
      if (_editorConfig.length > 1) {
        return true
      } else {
        this.sceneSearchForm.sceneType = this.sceneTypeOption[1].value
        return false
      }
    },

    // 是否显示场景下拉选项
    isShowTemplateType () {
      let _sceneType = this.sceneSearchForm.sceneType

      if (+_sceneType === 15) {
        this.sceneSearchForm.templateType = ''
        return false
      } else {
        return true
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
    // 重置
    resetForm () {
      this.sceneSearchForm = {
        corpName: '',
        sceneId: '',
        sceneType: '',
        createDate: '',
        sceneTitle: '',
        sceneStatus: '',
        auditStatus: '',
        templateType: ''
      }
    }
  }
}
</script>
