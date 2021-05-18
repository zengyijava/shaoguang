<template>
  <el-dialog
    class="pop-list-dialog"
    top="10vh"
    :visible.sync="dialogVisible"
    @closed="hideDialog">
    <el-tabs v-model="activeName">
      <!-- 我的场景 -->
      <el-tab-pane :label="$t('public.myScene')" name="first">
        <pop-list-cont
          :pageType="'my'"
          :callFrom="'editor'"
          :auditStatus="0"
          :sceneType="sceneType"
          @immediateUse="immediateUse">
        </pop-list-cont>
      </el-tab-pane>
      <!-- 模板库 -->
      <el-tab-pane :label="$t('public.tmpKu')" name="second">
        <pop-list-cont
          :pageType="'common'"
          :callFrom="'editor'"
          :auditStatus="1"
          :sceneType="sceneType"
          @immediateUse="immediateUse">
        </pop-list-cont>
      </el-tab-pane>
      <!-- 企业定制模板 -->
      <el-tab-pane :label="$t('public.enterprise')" name="three">
        <pop-list-cont
          :pageType="'rcos'"
          :callFrom="'editor'"
          :auditStatus="0"
          :sceneType="sceneType"
          @immediateUse="immediateUse">
        </pop-list-cont>
      </el-tab-pane>
    </el-tabs>
  </el-dialog>
</template>

<script>
import PopListCont from './PopListCont'
export default {
  name: 'PopList',
  components: {
    'pop-list-cont': PopListCont
  },
  props: {
    sceneType: String,
    dialogStatus: Boolean
  },
  data: function () {
    return {
      activeName: 'first',
      dialogVisible: false
    }
  },
  watch: {
    // 监听内部弹层调用
    dialogStatus: function (newVal, oldVal) {
      this.dialogVisible = newVal
    }
  },
  methods: {
    // 隐藏弹出层
    hideDialog: function () {
      this.activeName = 'first'
      this.$emit('dialogStatusCB')
    },
    // 立即使用
    immediateUse: function (id) {
      this.$emit('immediateUse', id)
    }
  }
}
</script>

<style lang="less" scope>
@import '../../libs/assets/less/variables';
.el-dialog__wrapper.pop-list-dialog .el-dialog .el-dialog__header{
  border-bottom: none;
}
.pop-list-dialog{
  .el-dialog{
    width: 1035px;
    height: 770px;
    border-radius: 4px;
    overflow: hidden;
  }
  .el-dialog__header{
    position: absolute;
    top: -5px;
    right: 0;
    z-index: 3;
    .el-dialog__headerbtn{
      width: 20px;
      top: 22px;
      right: 17px;
    }
  }
  .el-tabs{
    position: absolute;
    top: 0;
    left: 0;
    z-index: 2;
    width: 100%;
    background: #fff;
    .el-tabs__nav-wrap{
      padding-left: 20px;
      padding-right: 20px;
      padding-top: 10px;
    }
    .el-tabs__item{
      padding: 0 15px;
      font-size: 12px;
    }
  }
  .el-tabs__nav-wrap::after{
    height: 1px;
  }
  .el-tabs__header{
    background: @dialog-header-bg;
    margin-bottom: 0;
  }
}
</style>
