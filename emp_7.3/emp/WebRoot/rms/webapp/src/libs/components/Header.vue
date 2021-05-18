<template>
  <header>
    <div class="timer">{{new Date()|formatDate}}</div>
    <!-- <i class="icon icon-my-logo"></i> -->
  </header>
</template>

<script>
import filters from '../filters'

export default {
  name: 'Header',
  filters
}
</script>

<style lang="less" scoped>

  header {
    height: 42px;
    .timer {
      padding-left: 30px;
      padding-top: 22px;
      font-size: 12px;
      color: #8c8c8c;
    }
  }
</style>
