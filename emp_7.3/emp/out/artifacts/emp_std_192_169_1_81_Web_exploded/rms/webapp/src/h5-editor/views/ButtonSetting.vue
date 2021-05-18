<template>
  <div class="button-setting">
    <el-form-item>
      <label class="form-item-title">{{ $t('H5.name_text') }}</label>
      <el-input size="small" v-model="element.text" :maxlength="5"></el-input>
    </el-form-item>
    <FontColor></FontColor>
    <FontColor type="BackColor"></FontColor>
    <SizeSetting></SizeSetting>
    <PositionSetting></PositionSetting>
    <Action></Action>
  </div>
</template>

<script>
import {mapGetters} from 'vuex'
import FontSizePx from '../../libs/components/property/FontSizePx'
import FontColor from '../../libs/components/property/FontColor'
import ToolBar from '../../libs/components/property/ToolBar'
import PositionSetting from '../../libs/components/property/Position'
import Action from './Action'
import SizeSetting from '../../libs/components/property/SizeSetting'

export default {
  name: 'Button',
  components: {SizeSetting, PositionSetting, Action, ToolBar, FontColor, FontSizePx},
  computed: mapGetters(['element'])
}
</script>
