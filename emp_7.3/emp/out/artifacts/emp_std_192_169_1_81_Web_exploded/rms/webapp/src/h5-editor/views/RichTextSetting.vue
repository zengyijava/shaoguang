<template>
  <div class="rich-text-setting">
    <FontSizePx></FontSizePx>
    <FontColor></FontColor>
    <ToolBar></ToolBar>
    <SizeSetting></SizeSetting>
    <PositionSetting></PositionSetting>
    <el-form-item class="bdb">
      <label class="form-item-title">{{ $t('H5.text_param') }}</label>
    </el-form-item>
    <InsertParams></InsertParams>
    <el-form-item>
      <p class="description">{{ $t('H5.text_setting_hint') }}</p>
    </el-form-item>
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
  components: {InsertParams, ToolBar, FontColor, FontSizePx, SizeSetting, PositionSetting}
}
</script>
