<template>
  <div class="rich-text-setting">
    <FontSizePx></FontSizePx>
    <FontColor></FontColor>
    <ToolBar></ToolBar>
    <SizeSetting></SizeSetting>
    <PositionSetting></PositionSetting>
    <el-form-item style="margin-bottom: 0;">
      <p class="description">{{ $t('textSetting.explain') }}</p>
    </el-form-item>
    <el-form-item class="bdb">
      <label class="form-item-title">{{ $t('H5.text_param') }}</label>
    </el-form-item>
    <InsertParams></InsertParams>
  </div>
</template>

<script>
import FontSizePx from '../../libs/components/property/FontSizePx'
import FontColor from '../../libs/components/property/FontColor'
import ToolBar from '../../libs/components/property/ToolBar'
import SizeSetting from '../../libs/components/property/SizeSetting'
import PositionSetting from '../../libs/components/property/Position'
import InsertParams from '../../libs/components/InsertParams'

export default {
  name: 'RichTextSetting',
  data () {
    return {
      useParam: false
    }
  },
  components: {InsertParams, ToolBar, FontColor, FontSizePx, SizeSetting, PositionSetting}
}
</script>
