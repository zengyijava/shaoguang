<template>
  <section class="chart-setting">
    <ul class="chart-setting-list">
      <li>
        <span>图表类型：</span>
        <el-select class="c-input" v-model="element.chartType" @change="switchCharts" placeholder="请选择" size="small">
          <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value">
          </el-option>
        </el-select>
      </li>
      <li>
        <span>图形标题：</span>
        <el-input class="c-input" v-model="element.chartTitle" placeholder="标题" size="small"
          @blur="editEchartsTitle"></el-input>
      </li>
      <li class="mt20">
        <span>图形数据：</span>
        <div class="chart-data" v-if="element.chartType === 1">
          <div class="data-list" v-for="(item,index) in element.config.pieData" :key="index">
            <div class="d-l-l" @click="editTitle(item)">
              <p v-if="item.tEdit" v-text="item.name"></p>
              <input v-else class="l-input" maxlength="10" type="text" v-model="item.name" @blur="reEditTitle(item)" />
            </div>
            <div class="d-l-m" @click="editNum(item)">
              <p v-if="item.nEdit" v-text="item.value"></p>
              <input v-else class="l-input" maxlength="10" type="text" v-model="item.value" @blur="reEditNum(item)" />
            </div>
            <div class="d-l-r">
              <b @click="addList(index)">添加</b>
              <b @click="showDeleteTips(index)">删除</b>
            </div>
          </div>
        </div>
        <div class="bar-line-data" v-else>
          <table :style="{ width: 110+55*element.config.col.length + 'px' }">
            <tr>
              <th width="55"></th>
              <th width="55" v-for="(col, index) in element.config.col" :key="index" @click="editTitle(col)">
                <p v-if="col.tEdit" v-text="col.name"></p>
                <input v-else class="l-input" maxlength="10" type="text" v-model="col.name" @blur="reEditTitle(col)" />
              </th>
              <th width="55">
                <b @click="addCol()">添加</b>
                <b @click="DelCol()">删除</b>
              </th>
            </tr>
            <tr v-for="(row, index) in element.config.row" :key="index">
              <td width="55" @click="editTitle(row)">
                <p v-if="row.tEdit" v-text="row.name"></p>
                <input v-else class="l-input" maxlength="10" type="text" v-model="row.name" @blur="reEditTitle(row)" />
              </td>
              <td width="55" v-for="(col, index) in row.children" :key="index" @click="editNum(col)">
                <p v-if="col.nEdit" v-text="col.value"></p>
                <input v-else class="l-input" maxlength="10" type="text" v-model="col.value" @blur="reEditNum(col)" />
              </td>
              <td width="55">
                <b @click="addRow(index)">添加</b>
                <b @click="delRow(index)">删除</b>
              </td>
            </tr>
          </table>
        </div>
        <div class="l-tips clear">注：图形数据中标题和数字双击可以更改内容</div>
      </li>
    </ul>
    <div class="chart-views">
      <p>预览：</p>
      <div id="showEchart" style="width:260px; height:350px"></div>
      <el-button @click="getChartImg" class="an-add-echarts" type="primary" size="small">确定</el-button>
    </div>
    <!--删除提示-->
    <el-dialog title="提示" :append-to-body="true" :visible.sync="showTips" width="25%">
      <i class="tips-icon el-icon-warning"></i>
      <span>删除当前内容？</span>
      <span slot="footer" class="dialog-footer">
        <div class="an-false" type="primary" @click="showTips = false">否</div>
        <div class="an-true" @click="deleteList">是</div>
        <div class="clear"></div>
      </span>
    </el-dialog>
  </section>
</template>

<script>
import {mapActions, mapGetters} from 'vuex'

const echarts = require('echarts')
export default {
  name: 'ChartSetting',
  data () {
    return {
      myChart: '',
      showTips: false,
      delIndex: '',
      color: ['#37a2da', '#ff9f7e', '#66e1e3', '#ffdb5c', '#fb7293', '#97bfff', '#e162af', '#9fe7b9', '#e791d1', '#e7bdf3', '#32c5e9', '#9d97f5', '#8378eb'],
      options: [{
        value: 1,
        label: '饼状图'
      }, {
        value: 2,
        label: '柱状图'
      }, {
        value: 3,
        label: '折线图'
      }]
    }
  },
  props: {
    element: {
      type: Object,
      default: () => {}
    }
  },
  computed: {
    ...mapGetters(['chartInfo']),
    // 饼状图第二列数值
    rowValue () {
      let data = []
      for (let i = 0; i < this.element.config.pieData.length; i++) {
        data.push(this.element.config.pieData[i].value)
      }
      return data
    },
    // 饼状图数据源
    lData () {
      let data = []
      for (let i = 0; i < this.element.config.pieData.length; i++) {
        data.push(this.element.config.pieData[i].name)
      }
      return data
    },
    // 列数据源
    rowData () {
      let data = []
      for (let i = 0; i < this.element.config.col.length; i++) {
        data.push(this.element.config.col[i].name)
      }
      return data
    },
    // 柱状图数据源
    colData () {
      let _Lidata = []

      for (let i = 0; i < this.element.config.col.length; i++) {
        let _singleObj = {
          name: this.element.config.col[i].name,
          type: 'bar',
          data: []
        }

        for (let j = 0; j < this.element.config.row.length; j++) {
          let _val = this.element.config.row[j].children[i].value

          _singleObj.data.push(_val)
        }
        _Lidata.push(_singleObj)
      }

      return _Lidata
    },
    // 折线图数据源
    lineData () {
      let _Lidata = []

      for (let i = 0; i < this.element.config.col.length; i++) {
        let _singleObj = {
          name: this.element.config.col[i].name,
          type: 'line',
          data: []
        }

        for (let j = 0; j < this.element.config.row.length; j++) {
          let _val = this.element.config.row[j].children[i].value

          _singleObj.data.push(_val)
        }
        _Lidata.push(_singleObj)
      }

      return _Lidata
    },
    // 行标题
    colTitel () {
      let data = []
      for (let i = 0; i < this.element.config.row.length; i++) {
        data.push(this.element.config.row[i].name)
      }
      return data
    },
    // 行标题
    barValue () {
      let str = ''
      for (let i = 0; i < this.lineData.length; i++) {
        str += ('@' + this.lineData[i].data.join(','))
      }
      return str.slice(1)
    },
    // 结构
    barTableVal () {
      let str = ''
      str += ',' + this.rowData.join(',')
      for (let i = 0; i < this.element.config.row.length; i++) {
        let _data = ''
        for (let j = 0; j < this.lineData.length; j++) {
          if (j === 0) {
            _data = this.lineData[j].data[i]
          } else {
            _data = _data + ',' + this.lineData[j].data[i]
          }
        }
        str += ('@' + this.element.config.row[i].name + ',' + _data)
      }
      return str
    }
  },
  mounted () {
    if (this.element.chartType === 1) {
      this.pieInit()
    } else if (this.element.chartType === 2) {
      this.barInit()
    } else if (this.element.chartType === 3) {
      this.lineInit()
    }
  },
  watch: {
    element () {
      if (this.element.chartType === 1) {
        this.pieInit()
      } else if (this.element.chartType === 2) {
        this.barInit()
      } else if (this.element.chartType === 3) {
        this.lineInit()
      }
    }
  },
  methods: {
    ...mapActions('media', ['createChartImg']),
    // 切换图表
    switchCharts () {
      if (this.element.chartType === 1) {
        this.pieInit()
      } else if (this.element.chartType === 2) {
        this.barInit()
      } else if (this.element.chartType === 3) {
        this.lineInit()
      }
    },
    // 饼状图初始化
    pieInit () {
      // 基于准备好的dom，初始化echarts实例
      this.myChart = echarts.init(document.getElementById('showEchart'))
      this.myChart.clear()
      // 绘制图表
      this.myChart.setOption({
        title: {
          text: this.element.chartTitle,
          left: 'center'
        },
        tooltip: {
          trigger: 'item',
          formatter: '{b} :<br> {c} ({d}%)'
        },
        // toolbox: {
        //   show: true,
        //   feature: {
        //     saveAsImage: { show: true }
        //   }
        // },
        color: this.color,
        legend: {
          bottom: 0,
          left: 'center',
          data: this.lData
        },
        series: [
          {
            type: 'pie',
            radius: '50%',
            center: ['50%', '40%'],
            label: {
              show: true,
              formatter: '{d}%'
            },
            labelLine: {show: true},
            data: this.element.config.pieData,
            itemStyle: {
              // normal: {
              //   label: {
              //     show: true,
              //     formatter: '{b} : {c} ({d}%)'
              //   },
              //   labelLine: { show: true }
              // },
              emphasis: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              }
            }
          }
        ]
      })
    },
    // 柱状图初始化
    barInit () {
      // 基于准备好的dom，初始化echarts实例
      this.myChart = echarts.init(document.getElementById('showEchart'))
      this.myChart.clear()
      this.myChart.setOption({
        title: {
          text: this.element.chartTitle,
          left: 'center'
        },
        color: this.color,
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          bottom: 0,
          left: 'center',
          data: this.element.config.col
        },
        // toolbox: {
        //   show: true,
        //   feature: {
        //     saveAsImage: { show: true }
        //   }
        // },
        calculable: true,
        xAxis: [
          {
            type: 'category',
            data: this.colTitel
          }
        ],
        yAxis: [
          {
            type: 'value'
          }
        ],
        series: this.colData
      })
    },
    // 折线图初始化
    lineInit () {
      // 基于准备好的dom，初始化echarts实例
      this.myChart = echarts.init(document.getElementById('showEchart'))
      this.myChart.clear()
      this.myChart.setOption({
        title: {
          text: this.element.chartTitle,
          left: 'center'
        },
        tooltip: {
          trigger: 'axis'
        },
        color: this.color,
        legend: {
          bottom: 0,
          left: 'center',
          data: this.element.config.col
        },
        // toolbox: {
        //   show: true,
        //   feature: {
        //     saveAsImage: { show: true }
        //   }
        // },
        calculable: true,
        xAxis: [
          {
            type: 'category',
            data: this.colTitel
          }
        ],
        yAxis: [
          {
            type: 'value'
          }
        ],
        series: this.lineData
      })
    },
    // 修改图标大标题
    editEchartsTitle () {
      if (this.element.chartType === 1) {
        this.pieInit()
      } else if (this.element.chartType === 2) {
        this.barInit()
      } else if (this.element.chartType === 3) {
        this.lineInit()
      }
    },
    // 编辑标题
    editTitle (item) {
      item.tEdit = false
    },
    // 放弃标题编辑
    reEditTitle (item) {
      item.tEdit = true
      if (this.element.chartType === 1) {
        this.pieInit()
      } else if (this.element.chartType === 2) {
        this.barInit()
      } else if (this.element.chartType === 3) {
        this.lineInit()
      }
    },
    // 编辑数字
    editNum (item) {
      item.nEdit = false
    },
    // 放弃数字编辑
    reEditNum (item) {
      item.nEdit = true
      if (this.element.chartType === 1) {
        this.pieInit()
      } else if (this.element.chartType === 2) {
        this.barInit()
      } else if (this.element.chartType === 3) {
        this.lineInit()
      }
    },
    // 新增
    addList (i) {
      let list = {
        name: '行标题' + (this.element.config.pieData.length + 1),
        value: '800',
        tEdit: true,
        nEdit: true
      }
      if (this.element.config.pieData.length >= 12) {
        this.$message.success('最多设置12行！')
      } else {
        this.element.config.pieData.splice(i + 1, 0, list)
        this.pieInit()
      }
    },
    // 删除
    showDeleteTips (i) {
      this.delIndex = i
      this.showTips = true
    },
    deleteList () {
      this.showTips = false
      if (this.element.config.pieData.length <= 2) {
        this.$message.success('至少保留两行数据！')
      } else {
        this.element.config.pieData.splice(this.delIndex, 1)
        this.pieInit()
      }
    },
    // 新增列标题
    addCol (i) {
      if (this.element.config.col.length >= 6) {
        this.$message.success('最多设置6列！')
      } else {
        this.element.config.col.push({
          name: '列标题' + (this.element.config.col.length + 1),
          tEdit: true
        })
        for (let i = 0; i < this.element.config.row.length; i++) {
          this.element.config.row[i].children.push({
            value: '0',
            nEdit: true
          })
        }
        if (this.element.chartType === 2) {
          this.barInit()
        } else if (this.element.chartType === 3) {
          this.lineInit()
        }
      }
    },
    // 删除列标题
    DelCol () {
      if (this.element.config.col.length <= 1) {
        this.$message.success('至少保留1列数据！')
      } else {
        this.element.config.col.pop()
        for (let i = 0; i < this.element.config.row.length; i++) {
          this.element.config.row[i].children.pop()
        }
        if (this.element.chartType === 2) {
          this.barInit()
        } else if (this.element.chartType === 3) {
          this.lineInit()
        }
      }
    },
    // 新增行
    addRow (i) {
      let list = {
        name: '行标题' + (this.element.config.row.length + 1),
        tEdit: true,
        children: []
      }
      for (let i = 0; i < this.element.config.row[0].children.length; i++) {
        list.children.push({
          value: '5',
          nEdit: true
        })
      }
      if (this.element.config.row.length >= 12) {
        this.$message.success('最多设置12行！')
      } else {
        this.element.config.row.splice(i + 1, 0, list)
        if (this.element.chartType === 2) {
          this.barInit()
        } else if (this.element.chartType === 3) {
          this.lineInit()
        }
      }
    },
    // 删除行
    delRow (i) {
      if (this.element.config.row.length <= 2) {
        this.$message.success('至少保留两行数据！')
      } else {
        this.element.config.row.splice(i, 1)
        if (this.element.chartType === 2) {
          this.barInit()
        } else if (this.element.chartType === 3) {
          this.lineInit()
        }
      }
    },
    // 生成图片
    getChartImg () {
      // let imgUrl = this.myChart.getDataURL()
      // console.log(imgUrl)
      let params = {
        chartType: this.element.chartType,
        chartTitle: this.element.chartTitle,
        ptType: 1,
        parmValue: '',
        rowNum: 0,
        colNum: 0
      }
      if (this.element.chartType === 1) {
        params.rowValue = this.rowValue.join(',')
        params.barRowName = this.lData.join(',')
        params.color = this.color.slice(0, this.rowValue.length).join(',')
      } else {
        params.barColName = this.rowData.join(',')
        params.barRowName = this.colTitel.join(',')
        params.barValue = this.barValue
        params.barTableVal = this.barTableVal
      }
      this.createChartImg(params)
      this.element.barRowName = this.lData.join(',')
      this.element.barColName = this.rowData.join(',')
      this.element.barValue = this.barValue
      this.element.barTableVal = this.barTableVal
    }
  }
}
</script>
<style lang="less">
  @import "../../libs/assets/less/variables";

  p {
    margin: 0;
    padding: 0;
  }

  .clear {
    clear: both;
  }

  .tips-icon {
    color: #e6a23c;
    margin-right: 8px;
    font-size: 20px;
  }

  .mt20 {
    margin-top: 20px;
  }

  .an-add-echarts {
    display: block;
    margin: 10px auto 0;
  }

  .an-false {
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
    &:hover {
      border: solid 1px @green-hover;
      background-color: @green-hover;
    }
  }

  .an-true {
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
    &:hover {
      color: @green;
      border-color: #c6e2ff;
      background-color: #ecf5ff;
    }
  }

  .chart-setting {
    .chart-setting-list {
      padding: 10px 20px;
      box-sizing: border-box;
      li {
        width: 100%;
        min-height: 30px;
        margin-bottom: 11px;
        .l-tips {
          border-top: 1px solid #e5e9ed;
          font-size: 12px;
          color: #afafaf;
          padding-top: 10px;
          line-height: 16px;
        }
        > span {
          font-size: 12px;
          color: #222;
          float: left;
          width: 60px;
          line-height: 30px;
        }
        .c-input {
          width: 210px;
          float: left;
        }
        .bar-line-data {
          width: 278px;
          overflow-x: auto;
          position: relative;
          font-size: 12px;
          th,
          td {
            font-weight: normal;
            height: 40px;
            line-height: 40px;
            border-top: 1px solid #e5e9ed;
          }
          b {
            color: @green;
            font-weight: normal;
            cursor: pointer;
          }
          p {
            color: #333;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
            text-align: center;
          }
          .l-input {
            width: 53px;
            height: 38px;
            text-align: center;
            border: none;
            outline: none;
            border-left: 1px solid #e5e9ed;
            border-right: 1px solid #e5e9ed;
          }
        }
        .chart-data {
          width: 278px;
          .data-list {
            width: 100%;
            height: 40px;
            clear: both;
            line-height: 40px;
            font-size: 12px;
            cursor: pointer;
            border-top: 1px solid #e5e9ed;
            &:hover {
              background: #f8faff;
            }
            .d-l-l {
              width: 105px;
              height: 40px;
              float: left;
            }
            .d-l-m {
              width: 105px;
              height: 40px;
              float: left;
            }
            .d-l-r {
              width: 68px;
              text-indent: 3px;
              height: 40px;
              float: right;
            }
            p {
              text-indent: 10px;
              color: #333;
              overflow: hidden;
              text-overflow: ellipsis;
              white-space: nowrap;
            }
            .l-input {
              width: 103px;
              height: 38px;
              border: none;
              text-indent: 9px;
              outline: none;
              border-left: 1px solid #e5e9ed;
              border-right: 1px solid #e5e9ed;
            }
            b {
              font-weight: normal;
              color: @green;
              &:hover {
                text-decoration: underline;
              }
            }
          }
        }
      }
    }
    .chart-views {
      clear: both;
      padding: 0 20px 20px;
      box-sizing: border-box;
    }
  }
</style>
