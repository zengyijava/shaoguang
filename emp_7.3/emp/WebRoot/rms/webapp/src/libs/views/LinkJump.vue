<template>
  <div class="link-jump">
    <el-form-item :label="$t('buttonSetting.selectBut')">
      <el-radio-group size="small" v-model="element.action">
        <el-radio-button label="2">{{ $t('buttonSetting.openUrl') }}</el-radio-button>
        <el-radio-button label="4">{{ $t('buttonSetting.openApp') }}</el-radio-button>
        <el-radio-button label="7">{{ $t('buttonSetting.openFastApp') }}</el-radio-button>
        <el-radio-button label="5">{{ $t('buttonSetting.openMap') }}</el-radio-button>
      </el-radio-group>
    </el-form-item>
    <el-form v-if="element.action==='5'" label-position="left">
      <el-form-item class="city-show" v-if="addr" :label="$t('buttonSetting.city')" label-width="40px">
        <span>{{ currentCity }}</span>
      </el-form-item>
      <el-form-item class="city-show" v-if="addr" :label="$t('buttonSetting.addr')" label-width="40px">
        <span>{{ addr + " " + title }}</span>
      </el-form-item>
      <el-form-item align="right">
        <el-button type="primary" size="small" @click="dialogTableVisible = true">{{ $t('buttonSetting.selection') }}
        </el-button>
      </el-form-item>
    </el-form>
    <template v-else>
      <el-form-item v-if="element.action === '2'" :label="$t('buttonSetting.urlAddr')"
        prop="url" :rules="[{type: 'url', message: $t('validation.beURL')}]">
        <el-input size="small" placeholder="http(s)://..." v-model="element.url"></el-input>
      </el-form-item>
      <slot v-else-if="element.action === '4'" name="app" :element="element"></slot>
      <slot v-else-if="element.action === '7'" name="quickApp" :element="element"></slot>
    </template>
    <el-dialog class="map-dialog" :title="$t('buttonSetting.selection')" top="10vh" :visible.sync="dialogTableVisible"
      @open="initBaiduMap" append-to-body>
      <div class="dialog-cont clearfix">
        <div class="search-cont">
          <el-input :placeholder="$t('buttonSetting.enterNr')" v-model="addressVal" size="small">
            <el-button slot="append" type="primary" icon="el-icon-search" @click="searchAddress"></el-button>
          </el-input>
          <div id="search-result" class="search-result">
            <div v-for="(item, index, key) in searchAddressArr" :key="key"
              :class="[{'active': index === activeIndex},'search-li', 'clearfix']"
              @click="getCurrentPoint(item, index)">
              <p class="number">{{ index + 1 }}</p>
              <div class="info">
                <p class="title">{{ item.title }}</p>
                <p class="desc">{{ item.address }}</p>
              </div>
            </div>
          </div>
        </div>
        <div id="allmap" class="mapContainer"></div>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button size="small" @click="dialogTableVisible = false">{{ $t('buttonSetting.cancel') }}</el-button>
        <el-button type="primary" size="small" @click="confirmAddress">{{ $t('buttonSetting.sure') }}</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {mapGetters} from 'vuex'
// 可能会内网使用，不使用import以免造成程序阻断
let BMap = null
try {
  BMap = window.BMap
} catch (e) {
}

export default {
  name: 'LinkJump',
  data () {
    return {
      dialogTableVisible: false,
      addressVal: '',
      currentCity: '',
      activeIndex: '',
      activeData: '',
      searchAddressArr: [],
      lat: '',
      map: '',
      lon: '',
      title: '',
      addr: ''
    }
  },

  computed: mapGetters(['element']),

  watch: {
    dialogTableVisible (val) {
      if (!val) {
        this.clearSearchResult()
      }
    }
  },

  mounted () {
    const _self = this
    // 根据ip获取所在城市
    try {
      new BMap.LocalCity().get((result) => {
        _self.currentCity = result.name
      })
    } catch (err) {
      return false
    }
  },

  methods: {
    // 地图初始化
    initBaiduMap () {
      const _self = this
      this.$nextTick(() => {
        try {
          this.map = new BMap.Map('allmap')
        } catch (err) {
          return
        }
        if (this.lat && this.lon) {
          let _point = new BMap.Point(this.lon, this.lat)
          let marker = new BMap.Marker(_point)
          let _labelText = this.addr + '&nbsp&nbsp' + this.title

          this.setLableText(_point, _labelText)
          this.map.addOverlay(marker)
          this.map.centerAndZoom(_point, 18)
        } else {
          this.map.centerAndZoom(_self.currentCity, 12)
        }
        this.map.addControl(new BMap.NavigationControl())
        this.map.enableScrollWheelZoom(true)
        this.map.enableDoubleClickZoom(true)
      })
    },
    // 设置地图标点文字显示
    setLableText (point, text) {
      let _labelOpts = {
        position: point,
        offset: new BMap.Size(-60, -53)
      }
      let _label = new BMap.Label(text, _labelOpts)
      _label.setStyle({
        color: '#333',
        fontSize: '12px',
        height: '20px',
        lineHeight: '20px',
        padding: '2px 6px',
        fontFamily: '微软雅黑'
      })
      this.map.addOverlay(_label)
    },
    // 地址搜索
    searchAddress () {
      let local = new BMap.LocalSearch(this.currentCity, {
        onSearchComplete: this.searchComplete,
        pageCapacity: 10
      })

      local.search(this.addressVal)
    },
    // 地址搜索完成
    searchComplete (res) {
      let _result = res ? res.Ar : []

      this.activeIndex = ''
      this.searchAddressArr = _result
    },
    // 获取当前点击搜索结果的经纬度
    getCurrentPoint (data, index) {
      this.activeIndex = index
      this.activeData = data
      let _point = new BMap.Point(data.point.lng, data.point.lat)
      let marker = new BMap.Marker(_point)
      let _labelText = data.address + '&nbsp&nbsp' + data.title

      this.map.clearOverlays()
      this.setLableText(_point, _labelText)
      this.map.addOverlay(marker)
      this.map.centerAndZoom(_point, 18)
    },
    // 清除搜索结果
    clearSearchResult () {
      this.addressVal = ''
      this.activeIndex = ''
      this.searchAddressArr = []
    },
    // 确定使用当前地址
    confirmAddress () {
      let _activeAddressData = this.activeData ? this.activeData : ''
      let _lat = _activeAddressData ? _activeAddressData.point.lat : ''
      let _lng = _activeAddressData ? _activeAddressData.point.lng : ''
      let _title = _activeAddressData ? _activeAddressData.title : ''
      let _address = _activeAddressData ? _activeAddressData.address : ''

      this.lat = _lat
      this.lon = _lng
      this.title = _title
      this.addr = _address
      this.dialogTableVisible = false
      this.element.lat = _lat
      this.element.lon = _lng
      this.element.title = _title
      this.element.addr = _address
    }
  }
}
</script>

<style lang="less">
  @import "../assets/less/variables";

  .link-jump {
    .el-form-item {
      padding-left: 16px !important;
      padding-right: 16px !important;
    }

    .app-link {
      .el-form-item__content {
        line-height: normal;
      }
    }

    .el-radio-group {
      .el-radio-button__inner {
        padding-left: 6px;
        padding-right: 6px;
        border-radius: 0;
      }
    }

    .city-show {
      .el-form-item__content,
      .el-form-item__label {
        line-height: 1.2;
      }
    }
  }

  .map-dialog {
    .dialog-cont {
      width: 100%;
      height: auto;

      .mapContainer,
      .search-cont {
        float: left;
      }

      .mapContainer {
        width: 400px;
        height: 400px;
      }

      .search-cont {
        width: 200px;
      }

      .search-result {
        height: 347px;
        padding: 10px 4px;
        margin-top: -1px;
        border: 1px solid @border;
        overflow-y: auto;

        p {
          margin: 0;
          padding: 0;
        }

        .search-li {
          margin-bottom: 10px;
          cursor: pointer;

          &:hover {
            color: @green;
          }

          &.active {
            color: @green;

            .desc {
              color: @green;
            }
          }
        }

        .number,
        .info {
          float: left;
        }

        .number {
          width: 26px;
          text-align: center;
          font-size: 14px;
        }

        .info {
          font-size: 12px;
          width: 158px;

          .title {
            font-size: 14px;
          }

          .desc {
            margin-top: 2px;
            color: @grey;
          }
        }
      }

      .el-input-group--append .el-input__inner,
      .el-input-group__append {
        border-radius: 0;
      }
    }

    .el-dialog {
      width: 640px;
      overflow: hidden;
      border-radius: 4px;
    }

    .el-dialog__body {
      padding: 20px;
    }

    .el-dialog__header {
      padding: 17px 20px;
      border-bottom: 1px solid @border;
      background: @dialog-header-bg;
      line-height: 1;

      .el-dialog__title {
        font-size: 14px;
        line-height: 1;
      }

      .el-dialog__headerbtn {
        top: 16px;
        right: 16px;
      }
    }

    .el-dialog__footer {
      padding-top: 0;
    }
  }
</style>
