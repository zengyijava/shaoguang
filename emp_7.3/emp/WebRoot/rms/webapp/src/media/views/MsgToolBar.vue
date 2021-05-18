<template>
  <ToolBar :histories="messages" @update-history="updateMessage"></ToolBar>
</template>

<script>
import {mapGetters, mapMutations} from 'vuex'
import ToolBar from '../../libs/components/ToolBar'

export default {
  name: 'MsgToolBar',
  components: {ToolBar},
  computed: mapGetters('media', ['messages']),
  methods: mapMutations(['updateMessage'])
}
</script>
