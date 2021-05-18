<template>
  <aside class="left-property" :class="{'show':(addType.length>0)}">
    <section class="left-panel">
      <el-tabs v-model="activeName" @tab-click="handleClick">
        <el-tab-pane :label="pubTitle" name="public">
          <PublicImageMaterial v-if="activeName === 'public' && (addType==='image'||addType==='bgImage')"
            @closePanel="handleClose"
            @addImage="$emit('addImage', $event)">
          </PublicImageMaterial>
          <PublicAudioMaterial v-if="activeName === 'public' && (addType==='audio'||addType==='music')"
            @closePanel="handleClose"
            @addAudio="$emit('addAudio', $event)">
          </PublicAudioMaterial>
          <PublicVideoMaterial v-else-if="activeName === 'public' && addType==='video'"
            @closePanel="handleClose"
            @addVideo="$emit('addVideo', $event)">
          </PublicVideoMaterial>
        </el-tab-pane>
        <el-tab-pane :label="privateTitle" name="private">
          <ImageMaterial v-if="activeName === 'private' && (addType==='image' || addType==='bgImage')"
            @closePanel="handleClose"
            @addImage="$emit('addImage', $event)">
          </ImageMaterial>
          <AudioMaterial v-else-if="activeName === 'private' && (addType==='audio'||addType==='music')"
            @closePanel="handleClose"
            @addAudio="$emit('addAudio', $event)">
          </AudioMaterial>
          <VideoMaterial v-else-if="activeName === 'private' && addType==='video'"
            @closePanel="handleClose"
            @addVideo="$emit('addVideo', $event)">
          </VideoMaterial>
        </el-tab-pane>
      </el-tabs>
      <a class="el-icon-close close-btn" @click="handleClose"></a>
    </section>
  </aside>
</template>
<script>
import { mapGetters, mapMutations, mapActions } from 'vuex'
import ImageMaterial from './ImageMaterial'
import AudioMaterial from './AudioMaterial'
import VideoMaterial from './VideoMaterial'
import PublicImageMaterial from './PublicImageMaterial'
import PublicAudioMaterial from './PublicAudioMaterial'
import PublicVideoMaterial from './PublicVideoMaterial'

export default {
  name: 'Material',
  components: {PublicImageMaterial, PublicAudioMaterial, PublicVideoMaterial, ImageMaterial, AudioMaterial, VideoMaterial},
  props: {
    sourceType: {
      type: Number
    }
  },
  data () {
    return {
      activeName: 'public'
    }
  },
  computed: {
    ...mapGetters(['addType']),
    type () {
      if (this.addType === 'bgImage') {
        return 'image'
      }
      return this.addType
    },
    pubTitle () {
      const type = this.addType
      if (type) {
        return this.$t(type + '_material')
      }
    },
    privateTitle () {
      const type = this.addType
      if (type) {
        return this.$t('my_' + type + '_material')
      }
    }
  },
  created () {
    this.getIndustryUses({
      name: '',
      type: '',
      tmpType: this.sourceType,
      belongType: 1 // 0 模版 1 素材
    })
  },
  methods: {
    ...mapMutations(['setAddType', 'resetList']),
    ...mapActions(['getIndustryUses']),
    handleClick (tab, event) {
      this.resetList(this.addType)
    },
    handleClose () {
      this.setAddType('')
    }
  },
  watch: {
    addType (newValue, oldValue) {
      this.activeName = 'public'
    }
  }
}
</script>
<style lang="less">
@import '../assets/less/variables';

.left-property {
  position: fixed;
  display: none;
  top: 60px;
  bottom: 20px;
  height: 100%;
  left: 0;
  overflow-y: hidden;
  border: solid 1px @border;
  border-radius: 2px;
  background-color: @white-light;
  z-index: 20;
  transition: right 0.5s ease-in-out;
}
.show {
  display: block;
}
.left-panel {
  position: relative;
  width: 418px;
  height: 100%;
  box-sizing: border-box;
  z-index: 2;
  overflow: hidden;

  .el-tabs__nav-scroll {
    height: 50px;
  }

  .el-tabs__nav-wrap::after {
    height: 1px;
  }

  .el-tabs__nav {
    margin-left: 18px;
    .el-tabs__item {
      padding-top: 5px;
      padding-bottom: 5px;
    }
  }

  .el-tabs__content {
    position: absolute;
    width: 100%;
    height: 100%;
  }

  .close-btn {
    position: absolute;
    top: 12px;
    right: 18px;
    padding: 0;
    background: transparent;
    border: none;
    outline: none;
    cursor: pointer;
    color: @grey;
    font-weight: bold;

    &:hover {
      color: @green;
    }
  }

  .infinite-scroll {
    width: 100%;
    position: absolute;
    top: 50px;
    bottom: 80px;
    overflow-y: auto;
    overflow-x: hidden;
    margin-bottom: 50px;
    .loading {
      margin: 8px auto;
      text-align: center;
    }
  }
}
</style>
