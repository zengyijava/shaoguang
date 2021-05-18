<template>
  <LinkJump>
    <section slot="app" slot-scope="{element}" class="app-link">
      <el-form-item :label="$t('buttonSetting.apkLink')">
        <el-input size="small" placeholder="url scheme://..." v-model="element.apkLink"></el-input>
      </el-form-item>
      <el-form-item :label="$t('buttonSetting.apkDownloadURL')"
        prop="apkDownloadURL" :rules="[{type: 'url', message: $t('validation.beURL')}]">
        <el-input size="small" placeholder="http(s)://..."
          v-model="element.apkDownloadURL"></el-input>
      </el-form-item>
      <el-form-item :label="$t('buttonSetting.ipaLink')">
        <el-input size="small" placeholder="url scheme://..."
          v-model="element.ipaLink"></el-input>
      </el-form-item>
      <el-form-item :label="$t('buttonSetting.ipaDownloadURL')"
        prop="ipaDownloadURL" :rules="[{type: 'url', message: $t('validation.beURL')}]">
        <el-input size="small" placeholder="https://itunes.apple.com/..."
          v-model="element.ipaDownloadURL"></el-input>
      </el-form-item>
    </section>
    <el-form-item slot="quickApp" slot-scope="{element}" :label="$t('buttonSetting.appAddr')">
      <el-input size="small" placeholder="hap://app/<package>..."
        v-model="element.quickAppURL"></el-input>
    </el-form-item>
  </LinkJump>
</template>

<script>
import LinkJump from '../../libs/views/LinkJump'

export default {
  name: 'H5LinkJump',
  components: {LinkJump}
}
</script>
