<template>
  <div id="app" style="height: 100%;">
    <router-view/>
  </div>
</template>

<script>
import * as types from './store/mutation-type'
export default {
  name: 'App',
  created () {
    // Subscriptions for mutation
    this.$store.subscribe(mutation => {
      if (mutation.payload) {
        const msg = mutation.payload.msg
        switch (mutation.type) {
          case types.ADD_H5_SUCCESS:
            this.$message.success(msg)
            if (window.opener && !window.opener.closed) {
              window.opener.syncData()
            }
            break
          case types.EDIT_H5_SUCCESS:
            this.$message.success(msg)
            if (window.opener && !window.opener.closed) {
              window.opener.syncData()
            }
            break
          case 'error':
            this.$message.error(msg || '请求错误，请稍后重试')
            break
          default:
            break
        }
      }
    })
  }
}
</script>
