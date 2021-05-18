<template>
  <ImageSetting
    :element="element"
    @cutImg="visible = true"
    :tmType="15"
    @replaceImage="setAddType('image')">
    <template slot-scope="{imgSrc}">
      <SizeSetting :equal="equal"></SizeSetting>
      <el-form-item>
        <el-checkbox v-model="equal">{{ $t('cardImgSetting.keepProportion') }}</el-checkbox>
      </el-form-item>
      <PositionSetting></PositionSetting>
      <BorderRadiusSetting></BorderRadiusSetting>
      <ImageSetPanel></ImageSetPanel>
      <ImageLayer :image-src="imgSrc" :open="visible" @closePanel="visible = false"></ImageLayer>
    </template>
  </ImageSetting>
</template>

<script>
import {mapGetters, mapMutations} from 'vuex'
import SizeSetting from '../../libs/components/property/SizeSetting'
import BorderRadiusSetting from '../../libs/components/property/BorderRadius'
import PositionSetting from '../../libs/components/property/Position'
import ImageSetPanel from '../../libs/components/property/ImageSetPanel'
import ImageSetting from '../../libs/views/ImageSetting'
import ImageLayer from '../../libs/views/ImageLayer'

export default {
  name: 'H5ImageSetting',
  components: {ImageLayer, ImageSetting, SizeSetting, PositionSetting, BorderRadiusSetting, ImageSetPanel},
  data () {
    return {
      equal: false,
      visible: false
    }
  },
  computed: mapGetters(['element']),
  methods: {
    ...mapMutations(['setAddType'])
  }
}
</script>
