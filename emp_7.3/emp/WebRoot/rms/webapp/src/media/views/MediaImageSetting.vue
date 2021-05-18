<template>
  <ImageSetting class="media-image-setting" :element="element" @cutImg="visible = true">
    <template slot-scope="{imgSrc}">
      <el-form class="action">
        <div class="action-title">
          {{ $t('functionSetting.funSet') }}
        </div>
        <div class="form-item">
          <el-tabs type="border-card">
            <el-tab-pane :label="$t('property.params.insertArgument')" class="copy">
              <el-form-item v-if="hasTemplate">
                <ParamBtnGroup></ParamBtnGroup>
              </el-form-item>
              <InsertParams v-else>
              </InsertParams>
            </el-tab-pane>
          </el-tabs>
        </div>
      </el-form>
      <ImageTextLayer :image-src="imgSrc" :open="visible" @closePanel="visible = false"></ImageTextLayer>
    </template>
  </ImageSetting>
</template>

<script>
import {mapGetters, mapMutations} from 'vuex'
import ImageSetting from '../../libs/views/ImageSetting'
import InsertParams from '../../libs/components/InsertParams'
import ImageTextLayer from './ImageTextLayer'
import ParamBtnGroup from '../components/ParamBtnGroup'

export default {
  name: 'MediaImageSetting',
  components: {ParamBtnGroup, ImageTextLayer, InsertParams, ImageSetting},
  data () {
    return {
      visible: false
    }
  },
  computed: {
    ...mapGetters(['mediaTemplate', 'card', 'mediaContent']),
    ...mapGetters('media', ['element']),
    hasTemplate () {
      return this.$store.getters.element
    }
  },
  methods: mapMutations('media', ['addUsedParam'])
}
</script>

<style lang="less">
  .media-image-setting {
    .action-title {
      padding-left: 20px;
      padding-bottom: 8px;
    }
  }

  .pd {
    .el-textarea__inner {
      padding: 5px 8px !important;
    }
  }
</style>
