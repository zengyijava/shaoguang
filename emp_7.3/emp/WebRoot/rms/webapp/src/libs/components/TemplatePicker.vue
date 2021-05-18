<template>
  <li>
    <div class="btn-card" @click="selectTemplate">
      <i class="icon-card"></i>
      <span class="text">{{ $t('public.selectTmp') }}</span>
    </div>
    <PopList :dialogStatus="visible" :sceneType="sceneType" @dialogStatusCB="handleClose"
      @immediateUse="immediateUse">
    </PopList>
  </li>
</template>

<script>
import PopList from './PopList'

export default {
  name: 'TemplatePicker',
  components: {PopList},
  props: {
    sceneType: {
      type: String,
      default: '12'
    }
  },
  data () {
    return {
      visible: false
    }
  },
  methods: {
    selectTemplate () {
      this.visible = true
    },
    // 弹层关闭回调
    handleClose: function () {
      this.visible = false
    },
    // 立即使用
    immediateUse: function (id) {
      this.$emit('getTempDetail', {
        tmId: id,
        previewType: 1
      })
      this.visible = false
    }
  }
}
</script>
