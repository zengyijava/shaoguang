<template>
  <div class="access-form">
    <div class="common">
      <div class="tip-box">
        <div class="title">发送相同内容给用户</div>
        <ol>
          <li>生成固定的链接，可复制此链接群发短信</li>
          <li>统计报表只能按下载次数查询</li>
        </ol>
      </div>
      <el-form
        class="form-wrapper"
        :model="resForm"
        ref="submitForm"
        :rules="formRules"
        label-width="110px"
        label-position="left">
        <el-form-item label="选择资源类型：">
          <el-radio-group v-model="rxMarkData.resourceMode" @change="initPeriod">
            <el-radio label="1">
              URL标记<span class="color-8">（系统会生成一个短链接，可直接复制此链接到短信内容中即可）</span>
            </el-radio><br>
            <el-radio label="2">
              标记符模式<span class="color-8">（选择系统设定的标记符后，可直接复制标记符+短信内容即可）</span>
            </el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item
          label="URL 地址："
          v-show="rxMarkData.resourceMode === '1'">
          <div>{{rxMarkData.resourceUrl}}</div>
          <div class="tip">提示“此链接不可用、仅作为占位展示，点击提交后会生成长度大概为20位的正式链接地址”</div>
        </el-form-item>
        <el-form-item
          label="原短信内容："
          v-show="rxMarkData.resourceMode === '2'">
          <el-input
            class="textarea-w"
            type="textarea"
            v-model="rxMarkData.smsContent"
            :autosize="{ minRows: 5}"></el-input>
          <div class="input-warn">原短信内容中不允许带参数，否则会匹配失败</div>
        </el-form-item>
        <el-form-item
          label="选择标记符："
          v-show="rxMarkData.resourceMode === '2'">
          <el-select
            class="select-w"
            v-model="rxMarkData.tagMarkType"
            placeholder="请选择标记模式">
            <el-option
              v-for="item in markOptions"
              :label="item.label"
              :value="item.value"
              :key="item.value">
            </el-option>
          </el-select>
          <el-popover prop="tag" placement="bottom" trigger="click">
            <el-radio-group v-model="rxMarkData.smsTag">
              <el-radio label="0">
                <img src="../assets/img/note_blue_icon.png" alt="">
                <img v-if="rxMarkData.tagMarkType === '2'" src="../assets/img/note_blue_icon.png" alt="">
              </el-radio>
              <el-radio label="1">
                <img src="../assets/img/note_yellow_icon.png" alt="">
                <img v-if="rxMarkData.tagMarkType === '2'" src="../assets/img/note_yellow_icon.png" alt="">
              </el-radio>
              <el-radio label="2">
                <img src="../assets/img/note_red_icon.png" alt="">
                <img v-if="rxMarkData.tagMarkType === '2'" src="../assets/img/note_red_icon.png" alt="">
              </el-radio>
            </el-radio-group>
            <el-button class="enhance input-w" slot="reference">请选择增强模式标记符</el-button>
          </el-popover>
        </el-form-item>
        <el-form-item
          label="签名："
          v-show="rxMarkData.resourceMode === '2'">
          <el-input
            v-model="rxMarkData.smsSign"
            placeholder="请输入签名内容，包括签名格式，如签名【签名】">
          </el-input>
          <el-select
            v-model="rxMarkData.signType"
            class="input-w">
            <el-option
              v-for="item in siteOptions"
              :label="item.label"
              :value="item.value"
              :key="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item
          prop="effective"
          :label="(rxMarkData.resourceMode === '1' ? 'URL' : '') + '有效期：'">
          <el-select
            class="select-s"
            v-model="resForm.effective"
            :placeholder="'请选择有效期，超过有效期后' + (rxMarkData.resourceMode === '1' ? 'URL无法访问' : '将不再识别标记符')">
            <el-option
              v-for="item in periodOptions"
              :label="item.lable"
              :value="item.value"
              :key="item.value">
            </el-option>
          </el-select>
          <span>&nbsp;天</span>
        </el-form-item>
        <el-form-item
          prop="frequency"
          :label="(rxMarkData.resourceMode === '1' ? 'URL' : '') + '有效次数：'">
          <el-input
            v-model="resForm.frequency"
            style="width: 655px;"
            :placeholder="'请输入有效次数，超过此次数' + (rxMarkData.resourceMode === '1' ? 'URL无法访问' : '将不再识别标记符')">
          </el-input>
        </el-form-item>
        <el-form-item
          label="标记模式："
          v-show="rxMarkData.resourceMode === '1'">
          <el-radio-group v-model="rxMarkData.markType">
            <el-radio label="1">
              增强模式<span class="color-8">（原短信内容和富信都会展示）</span>
            </el-radio><br>
            <el-radio label="2">
              替代模式<span class="color-8">（原短信内容不展示）</span>
            </el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item>
          <el-button @click="submitForm('submitForm')" type="primary" v-clipboard="copyData">提交</el-button>
          <el-button @click="$router.push('/')">取消</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import {mapGetters} from 'vuex'
import actions from '../../libs/api'
const SELECTOPTION = {
  // 有效期时间
  'validity': [
    {
      label: '30',
      value: 30
    },
    {
      label: '20',
      value: 20
    },
    {
      label: '15',
      value: 15
    },
    {
      label: '10',
      value: 10
    },
    {
      label: '7',
      value: 7
    },
    {
      label: '3',
      value: 3
    }
  ],
  // 签名位置
  'location': [
    {
      label: '运营商后置',
      value: '1'
    },
    {
      label: '运营商前置',
      value: '2'
    },
    {
      label: '运营商免签',
      value: '3'
    }
  ],
  // 标记符
  'mark': [
    {
      label: '增强模式',
      value: '1'
    },
    {
      label: '替代模式',
      value: '2'
    }
  ]
}
const formDefault = {
  effective: '',
  frequency: ''
}
export default {
  name: 'AccessForm',
  data () {
    return {
      // 复制的url
      copyUrl: '',
      // 表单数据
      resForm: {...formDefault},
      // 有效期时间
      periodOptions: SELECTOPTION.validity,
      // 签名位置
      siteOptions: SELECTOPTION.location,
      // 标记符
      markOptions: SELECTOPTION.mark,
      // 输入验证
      formRules: {
        effective: [
          {required: true, message: '请选择有效期', trigger: 'change'}
        ],
        frequency: [
          {
            required: true,
            trigger: 'blur',
            validator: (rule, value, callback) => {
              if (value !== '') {
                // 模板ID不为空的请况下必须为正整数
                let _reg = /^[1-9][0-9]*$/
                if (!_reg.test(value)) {
                  callback(new Error('有效次数必须为正整数，请重新输入'))
                } else {
                  callback()
                }
              } else {
                callback(new Error('请输入有效次数'))
              }
            }
          }
        ]
      }
    }
  },
  computed: {
    ...mapGetters(['rxMarkData']),
    copyData () {
      if (this.rxMarkData.resourceMode === '1') {
        return this.copyUrl
      } else {
        let smsTag = ''
        if (this.rxMarkData.smsTag === '0') {
          if (this.rxMarkData.tagMarkType === '1') {
            smsTag = '\u2709\ufe0f'
          } else {
            smsTag = '\u2709\ufe0f\u2709\ufe0f'
          }
        }
        if (this.rxMarkData.smsTag === '1') {
          if (this.rxMarkData.tagMarkType === '1') {
            smsTag = '\ud83d\udce9'
          } else {
            smsTag = '\ud83d\udce9\ud83d\udce9'
          }
        }
        if (this.rxMarkData.smsTag === '2') {
          if (this.rxMarkData.tagMarkType === '1') {
            smsTag = '\ud83d\udce8'
          } else {
            smsTag = '\ud83d\udce8\ud83d\udce8'
          }
        }
        return this.rxMarkData.smsContent + smsTag
      }
    }
  },
  methods: {
    initPeriod () {
      this.resForm.effective = ''
      this.resForm.frequency = ''
      this.rxMarkData.smsSign = ''
    },
    commitFormParams () {
      let _self = this
      let formParams = {
        templateid: this.$route.query.id,
        marktype: this.rxMarkData.resourceMode === '1' ? parseInt(this.rxMarkData.markType) : '',
        tagmarktype: this.rxMarkData.resourceMode === '2' ? parseInt(this.rxMarkData.tagMarkType) : '',
        effectivetime: this.resForm.effective,
        effectivenum: parseInt(this.resForm.frequency),
        resourcemode: parseInt(this.rxMarkData.resourceMode),
        smstag: this.rxMarkData.resourceMode === '2' ? parseInt(this.rxMarkData.smsTag) : '',
        smscontent: this.rxMarkData.resourceMode === '2' ? this.rxMarkData.smsContent : '',
        smssign: this.rxMarkData.resourceMode === '2' ? this.rxMarkData.smsSign : '',
        signtype: this.rxMarkData.resourceMode === '2' ? parseInt(this.rxMarkData.signType) : '',
        longurl: this.rxMarkData.resourceMode === '1' ? this.rxMarkData.resourceUrl : ''
      }
      actions.commitResourceInfo(formParams, response => {
        if (response.data.code === 200) {
          this.copyUrl = response.data.data.shorturl
          this.$router.push(
            {
              path: 'outcome',
              query: {
                type: this.rxMarkData.resourceMode,
                id: response.data.data.resourceid,
                shorturl: response.data.data.shorturl,
                effectivetime: this.resForm.effective,
                effectivenum: this.resForm.frequency
              }
            })
        }
      }, errMsg => {
        _self.$message.error(errMsg)
      })
    },
    submitForm (formName) {
      let _self = this
      this.$refs[formName].validate((valid) => {
        if (valid) {
          _self.commitFormParams()
        } else {
          console.log('error submit!!')
          return false
        }
      });
    }
  }
}
</script>

<style lang="less" scoped>
  @import '../assets/less/default';
  .access-form{
    padding-top: 6px;
    .form-wrapper{
      padding: 24px 0;
      .color-8{
        color: @lightGray;
      }
      .el-radio{
        line-height: 26px;
      }
      .el-radio+.el-radio{
        margin-left: 0;
      }
      .el-input--small, .select-w{
        width: 445px;
      }
      .select-s{
        width: 630px;
      }
      .input-w{
        width: 205px;
      }
      .textarea-w{
        width: 655px;
      }
      .enhance{
        color: #c0c4cc;
        text-align: left;
      }
      .input-warn{
        font-size: 12px;
        color: #ff0000;
      }
      .tip{
        line-height: 16px;
        color: #888;
      }
      & /deep/ .el-form-item__label:before{
        display: none;
      }
      & /deep/ .el-form-item__error{
        color: #f56c6c;
      }
      & /deep/ .el-button--primary span{
        color: #fff;
      }
    }
  }
</style>
