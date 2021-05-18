<template>
  <div>
    <el-form-item>
      <label class="form-item-title">{{ $t('property.params.argument') }}</label>
    </el-form-item>
    <div class="param">
      <el-form-item :label="$t('property.params.name')">
        {{$t('property.params.argument')+ paramNum}}
      </el-form-item>
      <el-form-item :label="$t('property.params.type')">
        <el-select size="small" v-model="param.type" @change="setParamType">
          <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('property.params.lengthRestrict')" v-show="hasLenth">
        <el-radio-group v-model="param.lengthRestrict">
          <el-radio size="small" :label="0">{{ $t('property.params.variableLength') }}</el-radio>
          <el-radio size="small" :label="1">{{ $t('property.params.fixedLength') }}</el-radio>
        </el-radio-group>
      </el-form-item>
      <div v-show="param.lengthRestrict===0&&hasLenth">
        <el-form-item :label="$t('property.params.minLength')">
          <el-input-number size="small" :min="0" :max="maxLenth" v-model.number="param.minLength" controls-position="right">
          </el-input-number>
        </el-form-item>
        <el-form-item :label="$t('property.params.maxLength')" v-show="hasLenth">
          <el-input-number size="small" :min="0" :max="maxLenth" v-model.number="param.maxLength" controls-position="right">
          </el-input-number>
        </el-form-item>
      </div>
      <div v-show="param.lengthRestrict===1&&hasLenth">
        <el-form-item :label="$t('property.params.fixedLengthT')">
          <el-input-number size="small" :min="0" :max="maxLenth" v-model.number="param.fixLength" controls-position="right">
          </el-input-number>
        </el-form-item>
      </div>
      <el-form-item align="left">
        <el-button size="small" type="primary" @click="handleAddParam" style="margin-left:82px;">{{ $t('property.params.insert') }}</el-button>
      </el-form-item>
    </div>
  </div>
</template>
<script>
import { mapGetters, mapMutations } from 'vuex'
export default {
  computed: {
    ...mapGetters(['element', 'paramList', 'paramLen', 'editorContent']),
    paramNum () {
      // if (this.paramList && this.paramList.name) {
      //   return parseInt(this.paramList[this.paramList.length - 1].name.match(/\d+/)[0]) + 1
      // }
      return this.paramList.length ? parseInt(this.paramList[this.paramList.length - 1].name.match(/\d+/)[0]) + 1 : 1
    }
  },
  props: {
    insertAbility: {
      type: Boolean
    }
  },
  data () {
    return {
      options: [{
        label: this.$t('property.params.cNumber'),
        value: 1
      }, {
        label: this.$t('property.params.zNumber'),
        value: 2
      }, {
        label: this.$t('property.params.money'),
        value: 3
      }, {
        label: this.$t('property.params.string'),
        value: 4
      }, {
        label: this.$t('property.params.dateOne'),
        value: 5
      }, {
        label: this.$t('property.params.dateTwo'),
        value: 6
      }, {
        label: this.$t('property.params.dateThree'),
        value: 7
      }, {
        label: this.$t('property.params.dateFour'),
        value: 8
      }, {
        label: this.$t('property.params.dateTimeOne'),
        value: 9
      }, {
        label: this.$t('property.params.dateTimeTwo'),
        value: 10
      }, {
        label: this.$t('property.params.dateTimeThree'),
        value: 11
      }, {
        label: this.$t('property.params.dateTimeFour'),
        value: 12
      }, {
        label: this.$t('property.params.time'),
        value: 13
      }],
      radio: '1',
      param: {
        name: '参数',
        lengthRestrict: 0,
        maxLength: 32,
        minLength: 0,
        type: 1,
        index: 1,
        hasLength: 1,
        style: {
          font: {
            color: '#000',
            size: '2'
          },
          isBolder: false
        }
      },
      count: 0,
      maxLenth: 32,
      hasLenth: true,
      content: ''
    }
  },
  watch: {
    param (newV, oldV) {
      this.setParamType(newV.type)
    },
    editorContent (val) {
      this.content = val
    }
  },
  methods: {
    ...mapMutations(['pushParam']),
    handleAddParam () {
      if (this.insertAbility) {
        const param = { ...this.param }
        if (param.lengthRestrict === 0) {
          delete param.fixLength
        } else if (param.lengthRestrict === 1) {
          delete param.minLength
          delete param.maxLength
        }
        param.name = '参数' + this.paramNum
        param.index = this.paramNum
        this.pushParam(param)
        this.$emit('addParam', param)
      }
    },
    // handleResetParam () {
    //   this.$emit('resetParam', this.param)
    // },
    setParamType (type) {
      switch (type) {
        case 1:
        case 2:
          this.hasLenth = true
          this.maxLenth = 32
          this.param.maxLength = 32
          break
        case 3:
          this.hasLenth = true
          this.maxLenth = 15
          this.param.maxLength = 15
          break
        case 4:
          this.hasLenth = true
          this.maxLenth = 5
          this.param.maxLength = 5
          break
        case 5:
        case 6:
        case 7:
        case 8:
        case 9:
        case 10:
        case 11:
        case 12:
        case 13:
          this.hasLenth = false
          break
        default:
          break
      }
    }
  }
}
</script>

<style lang="less">
@import '../../libs/assets/less/variables';
.param {
  height: 260px;
  background-color: #fff;
  .el-radio + .el-radio {
    margin-left: 10px;
  }
  .el-form-item {
    margin-bottom: 0;
    .el-select,
    .el-input-number--small {
      width: 180px;
    }
    &.btn-form-item {
      padding-top: 8px;
      padding-bottom: 16px;
      text-align: left;
      margin-left: 82px;
      .el-button:first-child {
        margin-right: 10px;
      }
      .el-button + .el-button {
        margin-left: 0;
      }
    }
  }
}
</style>
