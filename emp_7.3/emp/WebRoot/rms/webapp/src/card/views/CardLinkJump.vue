<template>
  <LinkJump>
    <el-form-item slot="app" slot-scope="{element}" :label="$t('buttonSetting.appAddr')">
      <el-input size="small" placeholder="url scheme://..." v-model="element.pkg"></el-input>
    </el-form-item>
    <section slot="quickApp" slot-scope="{element}" class="app-link">
      <el-form-item :label="$t('buttonSetting.bagName')">
        <el-input size="small" placeholder="<package>" v-model="element.pkg"></el-input>
      </el-form-item>
      <el-form-item :label="$t('buttonSetting.route')">
        <el-input size="small" placeholder="[path]" v-model="element.path"></el-input>
      </el-form-item>
      <el-form-item :label="$t('buttonSetting.params')">
        <el-input size="small" placeholder="[?key=value]" v-model="element.param"></el-input>
      </el-form-item>
    </section>
  </LinkJump>
</template>

<script>
import LinkJump from '../../libs/views/LinkJump'
export default {
  name: 'CardLinkJump',
  components: {LinkJump}
}
</script>
