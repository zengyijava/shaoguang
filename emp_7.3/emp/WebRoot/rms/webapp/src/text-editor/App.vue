<template>
  <div id="app">
    <Editor></Editor>
  </div>
</template>

<script>
import * as types from './../libs/store/mutation-type'
import Editor from './views/Editor'

export default {
  name: 'App',
  components: {Editor},
  mounted () {
    // Subscriptions for mutation
    this.$store.subscribe(mutation => {
      if (mutation.payload) {
        const msg = mutation.payload.msg
        switch (mutation.type) {
          case types.ADD_TEMPLATE_SUCCESS:
            this.$message.success(msg)
            if (window.opener && !window.opener.closed) {
              window.opener.syncData()
            }
            break
          case 'error':
            this.$message.error(msg)
            break
          default:
            break
        }
      }
    })
  }
}
</script>
<style scoped>
  body {
    margin: 0 auto;
    overflow: hidden;
  }
</style>
