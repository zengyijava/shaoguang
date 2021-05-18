<template>
  <header>
    <div class="timer">{{currentTime}}</div>
    <!-- <i class="icon icon-my-logo"></i> -->
  </header>
</template>

<script>
import utils from '../../libs/utils'

export default {
  name: 'Header',
  computed: {
    currentTime () {
      return utils.getCurrentTime()
    }
  }
}
</script>

<style lang="less" scoped>
  header {
    height: 44px;
    .timer {
      position: absolute;
      font-size: 12px;
      top: 24px;
      color: #8c8c8c;
      left: 16px;
    }
    .icon-my-logo {
      position: absolute;
      top: 42px;
      left: 12px;
      height: 30px;
      width: 30px;
      background: url(../../libs/assets/img/qylogo.png) no-repeat;
    }
  }
</style>
