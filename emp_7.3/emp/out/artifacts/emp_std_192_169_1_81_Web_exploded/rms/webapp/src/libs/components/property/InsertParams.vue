<template>
  <div class="insert-param">
    <el-form-item :label="$t('property.params.name')">
      {{ paramName }}
    </el-form-item>
    <el-form-item :label="$t('property.params.type')">
      <el-select size="small" v-model="param.type" @change="setParamType" :popper-append-to-body="false">
        <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value">
        </el-option>
      </el-select>
    </el-form-item>
    <el-form-item :label="$t('property.params.lengthRestrict')" v-show="param.hasLength">
      <el-radio-group v-model="param.lengthRestrict">
        <el-radio size="small" :label="0">{{ $t('property.params.variableLength') }}</el-radio>
        <el-radio size="small" :label="1">{{ $t('property.params.fixedLength') }}</el-radio>
      </el-radio-group>
    </el-form-item>
    <el-form-item :label="$t('property.params.minLength')" v-show="param.lengthRestrict===0&&param.hasLength">
      <el-input-number size="small" :min="0" :max="maxLength" v-model.number="param.minLength" controls-position="right">
      </el-input-number>
    </el-form-item>
    <el-form-item :label="$t('property.params.maxLength')" v-show="param.lengthRestrict===0&&param.hasLength">
      <el-input-number size="small" :min="0" :max="maxLength" v-model.number="param.maxLength" controls-position="right">
      </el-input-number>
    </el-form-item>
    <el-form-item :label="$t('property.params.fixedLengthT')" v-show="param.lengthRestrict===1&&param.hasLength">
      <el-input-number size="small" :min="0" :max="maxLength" v-model.number="param.fixLength" controls-position="right">
      </el-input-number>
    </el-form-item>
    <el-form-item class="save-param">
      <el-button type="primary" size="small" @click="handleAddParam" @blur="$event.stopPropagation()">{{ $t('property.params.insertArgument') }}</el-button>
    </el-form-item>
    <el-form-item>
      <p class="description">{{ $t('textSetting.paramsTip') }}</p>
    </el-form-item>
    <el-dialog :visible.sync="dialogTableVisible" width="500px" :append-to-body="isAppendToBody" :title="$t('property.params.paramSet')">
      <el-form-item :label="$t('property.params.name')">
        {{ currentParam.name }}
      </el-form-item>
      <el-form-item :label="$t('property.params.type')">
        <el-select size="small" v-model="currentParam.type" @change="setParamType">
          <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('property.params.lengthRestrict')" v-show="currentParam.hasLength">
        <el-radio-group v-model="currentParam.lengthRestrict">
          <el-radio size="small" :label="0">{{ $t('property.params.variableLength') }}</el-radio>
          <el-radio size="small" :label="1">{{ $t('property.params.fixedLength') }}</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item :label="$t('property.params.minLength')" v-show="currentParam.lengthRestrict===0&&currentParam.hasLength">
        <el-input-number size="small" :min="0" :max="maxLength" v-model.number="currentParam.minLength" controls-position="right">
        </el-input-number>
      </el-form-item>
      <el-form-item :label="$t('property.params.maxLength')" v-show="currentParam.lengthRestrict===0&&currentParam.hasLength">
        <el-input-number size="small" :min="0" :max="maxLength" v-model.number="currentParam.maxLength" controls-position="right">
        </el-input-number>
      </el-form-item>
      <el-form-item :label="$t('property.params.fixedLengthT')" v-show="currentParam.lengthRestrict===1&&currentParam.hasLength">
        <el-input-number size="small" :min="0" :max="maxLength" v-model.number="currentParam.fixLength" controls-position="right">
        </el-input-number>
      </el-form-item>
      <el-form-item class="save-param">
        <el-button type="primary" size="small" @click="saveChange">{{ $t('property.params.resertArgument') }}</el-button>
      </el-form-item>
    </el-dialog>
  </div>
</template>
<script>
import { mapGetters, mapMutations } from 'vuex'
export default {
  name: 'InsertParams',
  data () {
    return {
      paramName: this.$t('property.params.paramOne'),
      isAppendToBody: true,
      dialogTableVisible: false,
      maxLength: 32,
      currentParam: {},
      targetName: '',
      count: 1,
      isBlur: false,
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
      radio: '1'
    }
  },
  computed: {
    ...mapGetters(['card', 'param', 'params', 'paramCount'])
  },
  watch: {
    count (val) {
      this.paramName = this.$t('property.params.argument') + val
      this.setParamCount(val)
    }
  },
  mounted () {
    this.$nextTick(() => {
      let params = [...this.card.paramArr]
      if (this.card.paramArr.length > 0) {
        let lastParam = parseInt((params.pop().name).substring(2))
        this.count = (lastParam + 1)
      } else {
        this.count = this.paramCount
      }
      this.initMousedown()
    })
  },
  methods: {
    ...mapMutations(['addParam', 'setParamCount', 'changeInput']),
    handleAddParam () {
      if (this.param.maxLength < this.param.minLength) {
        this.$confirm(this.$t('property.params.maxLengthTips'), this.$t('property.params.tips'), {
          confirmButtonText: this.$t('property.params.reset'),
          cancelButtonText: this.$t('property.params.exchange'),
          type: 'warning'
        }).then(() => {

        }).catch(() => {
          let tmp = this.param.maxLength
          this.param.maxLength = this.param.minLength
          this.param.minLength = tmp
          this.addInputParam()
        })
      } else {
        this.addInputParam()
      }
    },
    initMousedown () {
      document.querySelector('article').addEventListener('mousedown', e => {
        e.stopPropagation()
        let target = e.target
        if (target.classList.contains('J-add-param')) {
          this.currentParam = this.$store.getters.card.paramArr.find(item => {
            if (item) {
              return target.defaultValue.match(/\d+/)[0] === item.name.match(/\d+/)[0]
            }
          })
          if (this.currentParam) {
            if (this.currentParam.type === 1 || this.currentParam.type === 2 || this.currentParam.type === 3 || this.currentParam.type === 4) {
              this.currentParam.hasLength = 1
            } else {
              this.currentParam.hasLength = 0
            }
            this.openSetParamWindow()
          }
        }
      }, false)
    },
    getHtml () {
      const ref = {...this.param}
      this.param.index = this.count
      ref.name = this.$t('property.params.argument') + this.count
      ref.value = '{#' + this.$t('property.params.argument') + this.count + '#}'
      ref.index = this.count
      this.count++
      this.paramName = this.$t('property.params.argument') + this.count
      this.addParam(ref)
      const name = ref.name
      let htmlW = name.length > 3 ? name.length > 4 ? 80 : 72 : 64
      const html = '<input class="param-input J-add-param" style="width: ' + htmlW + 'px;" ' +
        'type="text" readonly="readonly" value="{#' + name + '#}">'
      return html
    },
    addInputParam () {
      // 火狐、谷歌
      if (window.getSelection) {
        let sel, range
        sel = window.getSelection()
        if (sel.anchorNode === null) {
          return false
        }
        if (sel.anchorNode) {
          if (sel.anchorNode.classList) {
            if (sel.anchorNode.classList[0] === 'el-input' || sel.anchorNode.classList[0] === 'content') {
              return false
            }
          }
        }
        if (sel.getRangeAt && sel.rangeCount) {
          range = sel.getRangeAt(0)
          range.deleteContents()
          let el = document.createElement('div')
          el.innerHTML = this.getHtml()
          let frag = document.createDocumentFragment()
          let node, lastNode
          while ((node = el.firstChild)) {
            lastNode = frag.appendChild(node)
          }
          range.insertNode(frag)
          if (lastNode) {
            range = range.cloneRange()
            range.setStartAfter(lastNode)
            range.collapse(true)
            sel.removeAllRanges()
            sel.addRange(range)
          }
        }
        // IE
      } else if (document.selection && document.type !== 'Control') {
        return document.selection.createRange().parseHtml(this.getHtml())
      }
      this.changeInput()
    },
    openSetParamWindow () {
      this.dialogTableVisible = true
    },
    saveChange () {
      if (this.currentParam.maxLength < this.currentParam.minLength) {
        this.$confirm(this.$t('property.params.maxLengthTips'), this.$t('property.params.tips'), {
          confirmButtonText: this.$t('property.params.reset'),
          cancelButtonText: this.$t('property.params.exchange'),
          type: 'warning'
        }).then(() => {

        }).catch(() => {
          let tmp = this.currentParam.maxLength
          this.currentParam.maxLength = this.currentParam.minLength
          this.currentParam.minLength = tmp
          this.dialogTableVisible = false
        })
      } else {
        this.dialogTableVisible = false
      }
    },
    setParamType (type) {
      let editedParam
      if (this.dialogTableVisible) {
        editedParam = this.currentParam
      } else {
        editedParam = this.param
      }
      switch (type) {
        case 1:
        case 2:
          editedParam.hasLength = 1
          this.maxLength = 32
          editedParam.maxLength = editedParam.maxLength ? editedParam.maxLength : 32
          break
        case 3:
          editedParam.hasLength = 1
          this.maxLength = 15
          editedParam.maxLength = editedParam.maxLength ? editedParam.maxLength : 15
          break
        case 4:
          editedParam.hasLength = 1
          this.maxLength = 5
          editedParam.maxLength = editedParam.maxLength ? editedParam.maxLength : 5
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
          editedParam.hasLength = 0
          break
        default:
          break
      }
    }
  }
}
</script>

<style lang="less">
  @import '../../../libs/assets/less/variables';

  .el-dialog__wrapper .el-dialog .el-dialog__header {
    border-bottom: solid 1px #dcdcdc;
  }

  .save-param {
    padding-top: 8px;
    padding-bottom: 16px;
    margin-left: 80px;
  }

  .insert-param {
    background-color: #fff;
    .el-radio + .el-radio {
      margin-left: 10px;
    }
    .el-form-item {
      margin-bottom: 0;
      .el-select,
      .el-input-number--small {
        width: 175px;
      }
      &:last-child {
        padding-top: 8px;
        padding-bottom: 16px;
        margin-left: 80px;
      }
    }
  }
</style>
