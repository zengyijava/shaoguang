<template>
  <div id="app">
    <div class="content">
      <el-breadcrumb separator-class="el-icon-arrow-right" class="bdb">
        <el-breadcrumb-item to="/">{{ breadcrumbText }}</el-breadcrumb-item>
        <el-breadcrumb-item>{{$route.meta.path}}</el-breadcrumb-item>
      </el-breadcrumb>
      <router-view class="main-view"></router-view>
    </div>
  </div>
</template>

<script>
import utils from '../libs/utils'
import config from '../libs/config'
export default {
  name: 'App',
  mounted () {
    // 同步子窗口数据更新
    window.syncData = () => {
      this.queryCondition()
      this.$refs['templateSearchForm'].resetForm()
    }
  },
  computed: {
    breadcrumbText () {
      // 本地测试从url上获取type，提交SVN或者线上则必须改为iframe
      let _currentPageType = utils.getUrlParameters('type', false, config.GET_URL_PARAMS.LIST) || 'my'
      if (_currentPageType === 'my') {
        return '我的RX富信'
      } else {
        return 'RX富信模板库'
      }
    }
  }
}
</script>
<style lang="less">
  .admin-menu {
    width: 240px;
    padding: 8px;
  }
  .content {
    position: absolute;
    min-width: 1280px;
    box-sizing: border-box;
    top: 0;
    bottom: 0;
    left: 0;
    right: 0;
    overflow-y: auto;
    border-left: 2px solid rgb(230, 237, 240);
    .el-breadcrumb {
      height: 48px;
      line-height: 48px;
      vertical-align: middle;
      padding-left: 16px;
      box-sizing: border-box;
      .el-breadcrumb__inner a,
      .el-breadcrumb__inner.is-link{
        font-weight: normal
      }
    }
    .main-view {
      background-color: #fff;
    }
  }
</style>
