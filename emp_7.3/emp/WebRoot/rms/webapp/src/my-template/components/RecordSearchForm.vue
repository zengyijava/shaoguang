<template>
  <div class="search-option">
    <table style="width: 100%;">
      <tr>
        <td>
          获取资源类型：
          <el-select v-model="sourceValue" placeholder="请选择">
            <el-option
              v-for="item in sourceType"
              :key="item.sourceValue"
              :label="item.sourceLabel"
              :value="item.sourceValue">
            </el-option>
          </el-select>
        </td>
        <td style="padding-left: 28px;">
          标记模式：
          <el-select v-model="markValue" placeholder="请选择">
            <el-option
              v-for="item in markModel"
              :key="item.markValue"
              :label="item.markLabel"
              :value="item.markValue">
            </el-option>
          </el-select>
        </td>
        <td>
          资源内容：
          <el-input v-model="resourceInput" placeholder="请输入资源内容" size="small" style="width: auto;"></el-input>
        </td>
      </tr>
      <tr>
        <td style="padding-left: 28px;">
          获取时间：
          <el-date-picker
            v-model="achieveTime"
            type="datetimerange"
            size="small"
            style="width: 350px;"
            value-format="yyyy-MM-dd HH:mm:ss"
            start-placeholder="开始日期"
            end-placeholder="结束日期">
          </el-date-picker>
        </td>
        <td>
          有效截止时间：
          <el-date-picker
            v-model="endTime"
            type="datetimerange"
            size="small"
            style="width: 350px;"
            value-format="yyyy-MM-dd HH:mm:ss"
            start-placeholder="开始日期"
            end-placeholder="结束日期">
          </el-date-picker>
        </td>
        <td>
          <el-button type="primary" size="small" @click="searchList">查询</el-button>
        </td>
      </tr>
    </table>
  </div>
</template>

<script>
export default {
  name: 'RecordSearchForm',
  data () {
    return {
      sourceType: [{
        sourceValue: -1,
        sourceLabel: '全部'
      }, {
        sourceValue: 1,
        sourceLabel: 'URL'
      }, {
        sourceValue: 2,
        sourceLabel: '标记符'
      }],
      sourceValue: -1, // 默认获取资源类型为 全部
      markModel: [{
        markValue: -1,
        markLabel: '全部'
      }, {
        markValue: 1,
        markLabel: '增强模式'
      }, {
        markValue: 2,
        markLabel: '替代模式'
      }],
      markValue: -1, // 默认标记模式为 全部
      resourceInput: '', // 资源内容
      achieveTime: '', // 获取时间
      endTime: '' // 有效截止时间
    }
  },
  methods: {
    searchList () {
      let recordQueryParam = {
        resourceMode: this.sourceValue,
        markType: this.markValue,
        smsContent: this.resourceInput,
        effctiveStartTime1: this.achieveTime ? this.achieveTime[0] : '',
        effctiveStartTime2: this.achieveTime ? this.achieveTime[1] : '',
        effctiveEndTime1: this.endTime ? this.endTime[0] : '',
        effctiveEndTime2: this.endTime ? this.endTime[1] : ''
      }
      this.$emit('recordQueryParam', recordQueryParam)
    }
  }
}
</script>

<style>

</style>
