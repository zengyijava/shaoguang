<template>
  <div class="back-ground-setting">
    <BackColor></BackColor>
    <ChangeImg></ChangeImg>
    <ChangeOpacity></ChangeOpacity>
    <ApplyToPages></ApplyToPages>
  </div>
</template>

<script>
import BackColor from '../../libs/components/property/BackColor'
import ChangeImg from './ChangeImg'
import ChangeOpacity from '../../libs/components/property/ChangeOpacity'
import ApplyToPages from '../../libs/components/property/ApplyToPages'

export default {
  name: 'BackGroundSetting',
  components: {BackColor, ChangeImg, ChangeOpacity, ApplyToPages}
}
</script>
