<template>
  <div class="use-pop">
    <div class="use-hd clearfix">
      <p class="desc">{{ title }}{{ $t('property.nameT') }}</p>
      <el-input size="small" maxlength="6" v-model="useName" :placeholder="$t('property.pEnter')"></el-input>
      <el-button type="text" size="small" @click="addUse">{{ $t('property.add') }}</el-button>
    </div>
    <el-table
      :data="itemData"
      height="200">
      <el-table-column
        prop="name"
        :label="title + $t('property.nameTt')">
        <template slot-scope="scope">
          <span v-if="currentIndex !== scope.$index">{{ scope.row.name }}</span>
          <el-input v-else v-model="currentName" maxlength="6" size="mini" :placeholder="$t('property.pEnterContent')"></el-input>
        </template>
      </el-table-column>
      <el-table-column
        :label="$t('property.operation')"
        width="100"
        align="center">
        <template slot-scope="scope">
          <el-button v-if="currentIndex !== scope.$index" type="text" size="small" @click="showRenameEdit(scope.$index, scope.row.name)">{{ $t('property.reName') }}</el-button>
          <el-button v-else type="text" size="small" @click="updateUse(scope.row.id)">{{ $t('property.keep') }}</el-button>
          <el-button type="text" size="small" @click="showDeleHint(scope.row.id)">{{ $t('property.del') }}</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-button class="right-btn" type="primary" size="small" @click="closePanel">{{ $t('property.complete') }}</el-button>
    <el-dialog class="hint-dialog" title="提示" :visible.sync="deleTipsVisible" append-to-body>
      <i class="tips-icon el-icon-warning"></i>
      <span> {{ $t('property.isDel') }}{{ title }}？</span>
      <span slot="footer" class="dialog-footer clearfix">
        <div class="an-false" type="primary" @click="deleTipsVisible = false">{{ $t('property.no') }}</div>
        <div class="an-true" @click="deleUse">{{ $t('property.yes') }}</div>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import actions from '../../api'
import {mapGetters} from 'vuex'
export default {
  name: 'UseManage',
  data () {
    return {
      useName: '',
      deleId: 0,
      currentName: '',
      currentIndex: '',
      deleTipsVisible: false
    }
  },
  props: {
    itemData: Array,
    itemType: String,
    tmType: Number
  },
  computed: {
    ...mapGetters(['useIds']),

    // 标题
    title: function () {
      let _title

      if (this.itemType === 'industry') {
        _title = this.$t('industryAndUseText.industryText')
      } else if (this.itemType === 'use') {
        _title = this.$t('industryAndUseText.useText')
      }
      return _title
    },

    // 行业或者用途类型
    useType: function () {
      let _id

      if (this.itemType === 'industry') {
        _id = 0
      } else if (this.itemType === 'use') {
        _id = 1
      }
      return _id
    }
  },
  methods: {
    // 重命名
    showRenameEdit: function (index, name) {
      this.currentName = name
      this.currentIndex = index
    },

    // 添加行业和用途
    addUse: function () {
      let _useName = this.useName.replace(/\s+/g, '')
      let _addParam
      let _self = this
      this.currentIndex = ''
      this.currentName = ''

      if (_useName.length > 0) {
        _addParam = {
          name: this.useName,
          type: this.useType,
          tmpType: this.tmType
        }

        actions.addIndustryAndUse(_addParam, response => {
          _self.useIds.push(response.data.data)
          _self.useName = ''
          _self.$message.success(response.data.msg)
        }, errMsg => {
          _self.$message.error(errMsg)
        })
      }
    },

    // 显示删除提示
    showDeleHint: function (deleID) {
      this.deleId = deleID
      this.deleTipsVisible = true
    },

    // 删除行业和用途
    deleUse: function () {
      let _deleParam
      let _self = this
      _deleParam = {
        id: this.deleId
      }
      this.currentIndex = ''
      this.currentName = ''

      actions.deleIndustryAndUse(_deleParam, response => {
        let _userIdsIndex

        _self.useIds.map(function (val, index) {
          if (val.id === _self.deleId) {
            _userIdsIndex = index
          }
        })

        _self.useIds.splice(_userIdsIndex, 1)
        _self.$message.success(response.data.msg)
        _self.deleTipsVisible = false
      }, errMsg => {
        _self.$message.error(errMsg)
      })
    },

    // 更新行业和用途
    updateUse: function (id) {
      let _updateParam
      let _self = this
      _updateParam = {
        name: this.currentName,
        id: id,
        type: this.useType
      }

      actions.updateIndustryAndUse(_updateParam, response => {
        let _useIdsIndex

        _self.useIds.map(function (val, index) {
          if (val.id === id) {
            _useIdsIndex = index
          }
        })
        _self.useIds[_useIdsIndex].name = _self.currentName

        this.currentIndex = ''
        this.currentName = ''
      }, errMsg => {
        _self.$message.error(errMsg)
      })
    },

    // 关闭面板
    closePanel: function () {
      this.currentIndex = ''
      this.currentName = ''
      this.$emit('hidenUsePanel', this.itemType)
    }
  }
}
</script>

<style lang="less">
@import '../../assets/less/variables';
.use-pop{
  position: absolute;
  top: 45px;
  left: 0;
  z-index: 9999;
  padding: 14px 12px;
  width: 237px;
  background-color: #ffffff;
  border: 1px solid @border-light;
  border-radius: 2px;
  p{
    margin: 0;
    padding: 0;
  }
  .el-table{
    margin-top: 18px;
    font-size: 12px;
    td,
    th{
      padding: 0;
      font-weight: normal;
    }
    th{
      line-height: 30px;
      color: @grey-black;
      background-color: @dialog-header-bg;
    }
    td{
      color: @grey-dark;
    }
    .el-input{
      width: auto;
    }
  }
  .right-btn{
    float: right;
    margin-top: 10px;
  }
}
.hint-dialog{
  .el-dialog{
    width: 426px;
  }
}
.an-false{
  width: 55px;
  height: 30px;
  border: solid 1px @green;
  background-color: @green;
  border-radius: 4px;
  line-height: 30px;
  text-align: center;
  font-size: 12px;
  color: #fff;
  float: right;
  margin-left: 14px;
  cursor: pointer;
  &:hover{
    border: solid 1px @green-hover;
    background-color: @green-hover;
  }
}
.an-true{
  width: 55px;
  height: 30px;
  border: solid 1px #dcdfe6;
  background-color: #ffffff;
  border-radius: 4px;
  line-height: 30px;
  text-align: center;
  font-size: 12px;
  color: #666;
  float: right;
  cursor: pointer;
  &:hover{
    color: @green;
    border-color: #c6e2ff;
    background-color: #ecf5ff;
  }
}
.tips-icon{
  color: #e6a23c;
  margin-right: 8px;
  font-size: 20px;
}
.el-dialog__body{
  padding: 20px;
}
.use-hd{
  .desc,
  .el-input,
  .el-button{
    float: left;
    font-size: 12px;
  }
  .desc{
    line-height: 32px;
  }
  .el-input{
    width: 140px;
    line-height: 32px;
  }
  .el-button{
    margin-top: 0 !important;
    margin-left: 10px;
  }
}
</style>
