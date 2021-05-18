<template>
  <div id="app">
    <H5NavBar></H5NavBar>
    <div class="stage">
      <H5Editor></H5Editor>
    </div>
  </div>
</template>

<script>
import {mapGetters, mapActions, mapMutations} from 'vuex'
import utils from '../libs/utils'
import * as types from '../libs/store/mutation-type'
import H5NavBar from './views/H5NavBar'
import H5Editor from './views/H5Editor'

export default {
  name: 'App',
  components: {H5Editor, H5NavBar},
  data () {
    return {
      route: ''
    }
  },
  created () {
    const id = utils.getUrlParameters('id', false, 'url')
    if (id !== '') {
      this.getTempDetail({
        tmId: id,
        previewType: 1
      })
    }
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
  },
  computed: mapGetters(['card', 'content']),
  methods: {
    ...mapMutations(['updateParam']),
    ...mapActions(['getTempDetail'])
  },
  watch: {
    '$route' (newValue, oldValue) {
      this.route = newValue.name
    }
  }
}
</script>
<style lang="less">
  @import "../libs/assets/less/editor-app";
</style>
