<template>
  <div class="search-content">
    <table>
      <tr class="tableHead">
        <td v-for="(item, index) in contentHead" :key="index">
          {{item}}
        </td>
      </tr>
      <tr v-for="(item, index) in listData" :key="index">
        <td>{{index + 1}}</td>
        <td>{{item.resourceMode === 1 ? 'URL' : '标记符'}}</td>
        <td>{{item.markType === 1 ? '增强模式' : '替代模式'}}</td>
        <td>{{dateFormate(new Date(parseInt(item.updateTime)))}}</td>
        <td>{{Math.floor(Math.abs(item.effectiveEndtime -item.effectiveStarttime)/ (24 * 3600 * 1000))}}天</td>
        <td>{{dateFormate(new Date(parseInt(item.effectiveEndtime)))}}</td>
        <td>{{item.effectiveNum}}</td>
        <td>{{item.residueNum}}</td>
        <td>{{item.smsSign ? item.smsSign : '--'}}</td>
        <td v-if="item.resourceMode === 1" style="width: 18%;">{{item.smsContent ? item.smsContent : '--'}}</td>
        <td v-else style="width: 18%;" v-html="showSmsContent(item.smsContent, item.smsTag)"></td>
        <td>
          <span class="operate" v-clipboard="copyData(item)" @success="copyContent" v-if="isShowAuthBtn('link')">复制</span>
          <span class="operate" @click="preview(item)" v-if="isShowAuthBtn('preview')">预览</span>
          <span class="operate" @click="extensionDaily(parseInt(item.effectiveEndtime), item.id)" v-if="isShowAuthBtn('renewal')">续期</span>
        </td>
      </tr>
    </table>
    <el-dialog
      title="续期提醒"
      :visible.sync="centerDialogVisible"
      width="30%"
      >
      <div style="margin-bottom: 10px;">
        续期时间：
        <el-select v-model="extensionValue" placeholder="请选择">
          <el-option
            v-for="item in extensionTime"
            :key="item.extensionValue"
            :label="item.extensionLabel"
            :value="item.extensionValue"
            @click.native="chooseDaily(item.extensionLabel)">
          </el-option>
        </el-select>
        天
      </div>
      <div class="endTimeBox">
        续期后有效截止日期为：<span class="endTime">{{endEffictiveTime}}</span>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="sureExtension">确 定</el-button>
        <el-button @click="centerDialogVisible = false">取 消</el-button>
      </span>
    </el-dialog>
    <el-dialog
      v-if="templatePreviewDialogVisible"
      class="template-preview-dialog"
      top="10vh"
      :visible.sync="templatePreviewDialogVisible">
      <p slot="title">效果预览</p>
      <preview :preview-module="{hint: false, title: false}" :previewData="rxPreviewData" :markData="rxMarkData"></preview>
    </el-dialog>
  </div>
</template>

<script>
import blueIcon from '../assets/img/note_blue_icon.png'
import redIcon from '../assets/img/note_red_icon.png'
import yellowIcon from '../assets/img/note_yellow_icon.png'
import actions from '../../libs/api.js'
import Preview from '../../libs/components/Preview'
import {mapGetters} from 'vuex'

export default {
  name: 'RecordList',
  components: {Preview},
  props: {
    listData: Array
  },
  data () {
    return {
      extensionTime: [{
        extensionValue: 0,
        extensionLabel: 30
      }, {
        extensionValue: 1,
        extensionLabel: 20
      }, {
        extensionValue: 2,
        extensionLabel: 15
      }, {
        extensionValue: 3,
        extensionLabel: 10
      }, {
        extensionValue: 4,
        extensionLabel: 7
      }, {
        extensionValue: 5,
        extensionLabel: 3
      }],
      clickId: '', // 获取点击列表项的id
      extensionValue: 0, // 默认续期时间为 30天
      extensionDate: 30, // 具体续期天数 默认30天
      activeEndTime: '', // 有效截止时间
      centerDialogVisible: false, // 是否展示续期弹框
      templatePreviewDialogVisible: false, // 是否展示预览弹框
      endEffictiveTime: '', // 续期后的有效截至日期
      rxPreviewData: {},
      blueIcon: blueIcon,
      redIcon: redIcon,
      yellowIcon: yellowIcon,
      rxMarkData: {},
      contentHead: ['序号', '获取资源类型', '标记模式', '获取时间', '有效期', '有效截止时间', '访问次数', '剩余访问次数', '签名', '资源内容', '操作']
    }
  },
  computed: {
    ...mapGetters(['userInfo']),
    // 用户权限数据
    roleArr () {
      return this.userInfo.data.loginCorp.roleArr
    }
  },
  methods: {
    copyContent () {
      this.$message.success('复制资源内容成功')
    },
    copyData (data) {
      // resourceMode，1为url,2为标记符
      if (+data.resourceMode === 1) {
        return data.smsContent
      } else {
        return data.smsContent + data.smsTag
      }
    },
    preview (val) {
      let _self = this
      let params = {
        id: val.id,
        templateId: val.templateId,
        previewType: 0
      }
      actions.getRecordPreview(params, response => {
        this.templatePreviewDialogVisible = true
        this.rxPreviewData = response.data.data.list
        this.rxMarkData = response.data.data.rxResourceData[0]
      }, errMsg => {
        _self.$message.error(errMsg)
      })
    },
    showSmsContent (mySmsContent = '--', smsTag) {
      let smsContent = smsTag
      if (/(\\u2709\\ufe0f)/ig.test(smsContent)) {
        smsContent = smsContent.replace(/(\\u2709\\ufe0f)/ig, '<img class="blueIcon" src="' + this.blueIcon + '" />')
      }
      if (/(\\ud83d\\udce9)/ig.test(smsContent)) {
        smsContent = smsContent.replace(/(\\ud83d\\udce9)/ig, '<img class="yellowIcon" src="' + this.yellowIcon + '" />')
      }
      if (/(\\ud83d\\udce8)/ig.test(smsContent)) {
        smsContent = smsContent.replace(/(\\ud83d\\udce8)/ig, '<img class="redIcon" src="' + this.redIcon + '" />')
      }
      return mySmsContent + smsContent
    },
    // 根据对于权限授权显示对应按钮
    isShowAuthBtn (name) {
      let _authName = name
      // 遍历权限数据判断当前场景类型以及是否有对应权限
      for (let [i, roleArrLen] = [0, this.roleArr.length]; i < roleArrLen; i++) {
        if (this.roleArr[i].type === 'resource') {
          if (this.roleArr[i][_authName] === '1') {
            return true
          } else {
            return false
          }
        }
      }
    },
    extensionDaily (time, getId) {
      this.centerDialogVisible = true
      this.extensionValue = 0
      this.activeEndTime = time
      this.clickId = getId
      this.extensionDate = 30
      this.renewalDate(time, 30)
    },
    chooseDaily (val) {
      this.extensionDate = val
      this.renewalDate(this.activeEndTime, val)
    },
    renewalDate (time, day) {
      let getTime = new Date(time)
      let changeTime = new Date(getTime.setDate(getTime.getDate() + day))
      this.endEffictiveTime = this.dateFormate(changeTime)
    },
    sureExtension () {
      let _self = this
      let params = {
        id: this.clickId,
        rday: this.extensionDate
      }
      actions.renewalTime(params, response => {
        this.$emit('extensionSuccess', true)
        this.$message.success('续期成功')
      }, errMsg => {
        _self.$message.error(errMsg)
      })
      this.centerDialogVisible = false
    },
    dateFormate (val) {
      let changeYear = val.getFullYear()
      let changeMonth = (val.getMonth() + 1) >= 10 ? (val.getMonth() + 1) : '0' + (val.getMonth() + 1)
      let changeDay = val.getDate() >= 10 ? val.getDate() : '0' + val.getDate()
      let changeHour = val.getHours() >= 10 ? val.getHours() : '0' + val.getHours()
      let changeMinutes = val.getMinutes() >= 10 ? val.getMinutes() : '0' + val.getMinutes()
      let changSeconds = val.getSeconds() >= 10 ? val.getSeconds() : '0' + val.getSeconds()
      return changeYear + '-' + changeMonth + '-' + changeDay + ' ' + changeHour + ':' + changeMinutes + ':' + changSeconds
    }
  }
}
</script>

<style>
  .blueIcon {
    display: inline-block;
    position: relative;
    top: 4px;
    margin-right: 6px
  }
  .redIcon, .yellowIcon {
    display: inline-block;
    vertical-align: text-bottom;
    margin-right: 6px
  }
</style>
