<template>
  <div class="outcome clear-fix">
    <div class="outcome-left">
      <div class="title" v-if="this.$route.query.type === '1'">URL已复制到剪贴板、您可直接粘贴到短信内容中；<br/>如浏览器不支持，您可以手动复制以下URL地址</div>
      <div class="title" v-if="this.$route.query.type === '2'">内容已复制到剪贴板、您可直接粘贴到短信内容中；<br/>如浏览器不支持，您可以手动复制以下内容</div>
      <el-form label-width="130px" label-position="left">
        <el-form-item v-if="this.$route.query.type === '1'" label="您复制的URL为：">
          <div>{{shorturl}}</div>
        </el-form-item>
        <el-form-item v-if="this.$route.query.type === '2'" label="您复制的内容为：">
          <div class="textarea-w">
            {{rxMarkData.smsContent}}
            <span class="tag-box" v-if="rxMarkData.resourceMode == 2 && rxMarkData.smsTag == 0">
              <img v-if="rxMarkData.tagMarkType == 2" src="../assets/img/note_blue_icon.png">
              <img src="../assets/img/note_blue_icon.png">
            </span>
            <span class="tag-box" v-if="rxMarkData.resourceMode == 2 && rxMarkData.smsTag == 1">
              <img v-if="rxMarkData.tagMarkType == 2" src="../assets/img/note_yellow_icon.png">
              <img src="../assets/img/note_yellow_icon.png">
            </span>
            <span class="tag-box" v-if="rxMarkData.resourceMode == 2 && rxMarkData.smsTag == 2">
              <img v-if="rxMarkData.tagMarkType == 2" src="../assets/img/note_red_icon.png">
              <img src="../assets/img/note_red_icon.png">
            </span>
          </div>
        </el-form-item>
        <el-form-item label="URL有效期：">
          <div>{{effectivetime}}</div>
        </el-form-item>
        <el-form-item label="有效次数：">
          <div>{{effectivenum}}</div>
        </el-form-item>
        <el-form-item>
          <el-button @click="$router.push({path: 'access-list', query: {id: id}})">查看获取记录</el-button>
          <el-button @click="$router.push('/')">返回列表</el-button>
        </el-form-item>
      </el-form>
    </div>
    <div class="outcome-right">
      <Preview :previewModule="previewModule"
               :previewData="rxPreviewData"
               :markData="rxMarkData">
      </Preview>
    </div>
  </div>
</template>

<script>
import {mapGetters} from 'vuex'
import Preview from '../../libs/components/Preview'
import actions from '../../libs/api'
export default {
  name: 'Outcome',
  components: {Preview},
  data () {
    return {
      shorturl: this.$route.query.shorturl,
      effectivetime: this.$route.query.effectivetime,
      effectivenum: this.$route.query.effectivenum,
      previewModule: {
        title: false,
        hint: false
      },
      rxPreviewData: [],
      id: this.$route.query.id
    }
  },
  created: function () {
    // 获取详细信息参数
    let _getDetailsDataParams = {
      tmId: this.$route.query.id,
      previewType: 0
    }
    let _self = this

    // 执行详细信息获取
    actions.getDetailInfo(_getDetailsDataParams, response => {
      if (response.data.code === 200 || response.data.state === '0') {
        _self.rxPreviewData = response.data.data.list
      } else {
        _self.$message.error(response.data.msg)
      }
    }, errMsg => {
      _self.$message.error(errMsg)
    })
  },
  computed: {
    ...mapGetters(['rxMarkData'])
  }
}
</script>

<style lang="less" scoped>
  @import "../assets/less/default";
  .outcome{
    padding: 20px;
    .outcome-left{
      float: left;
      width: 765px;
      .title{
        width: 745px;
        background-color: #e6f7ff;
        border-radius: 2px;
        padding: 10px 20px;
        line-height: 25px;
        margin-bottom: 20px;
      }
      .tag-box img{
        vertical-align: top!important;
      }
    }
    .outcome-right{
      float: left;
      width: 360px;
      margin-left: 48px;
    }
    & /deep/ .el-form-item--small.el-form-item {
      margin-bottom: 2px;
    }
  }
</style>
