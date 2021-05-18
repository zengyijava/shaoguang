<template>
  <section class="canvas">
    <article class="page">
      <div class="content">
        <BgPreview :background="background" :actual-width="320"></BgPreview>
        <RichTextDRR :texts="elements.filter(item => item.type === 'text')"
          @addHistory="addHistory(elements)" @selectParam="paramSelected=$event">
        </RichTextDRR>
        <ElementDRR v-for="element in elements.filter(item => item.type === 'image')" :element="element"
          :key="element.tag" >
          <img :src="element.src" draggable="false" :style="{'border-radius': element.borderRadius + 'px'}" />
        </ElementDRR>
        <ElementDRR class="audio" v-for="element in elements.filter(item => item.type === 'audio')" :element="element"
          :key="element.tag" :resizable="false">
          <Audios :element="element" :playSize="'big-audios'"></Audios>
        </ElementDRR>
        <ElementDRR v-for="element in elements.filter(item => item.type === 'video')" :element="element"
          :key="element.tag" style="width: 240px; height: 142px;" :resizable="false">
          <VideoFirstFrame :previewFrom="'other'" :videoContent="element"></VideoFirstFrame>
        </ElementDRR>
        <ElementDRR v-for="element in elements.filter(item => item.type === 'button')" :minw="100"
          :element="element" :key="element.tag">
          <div class="btn" :style="element.style">
            <div class="text">{{element.text}}</div>
          </div>
        </ElementDRR>
        <MusicDRR class="music" v-for="(element, index) in elements.filter(item => item.type === 'music')"
          :element="element" :key="index">
        </MusicDRR>
      </div>
      <CanvasToolBar></CanvasToolBar>
    </article>
  </section>
</template>

<script>
import {mapGetters, mapMutations} from 'vuex'
import utils from '../../libs/utils'
import ElementDRR from '../../libs/components/ElementDRR'
import RichText from '../../libs/components/RichText'
import CanvasToolBar from './H5ToolBar'
import VueDrr from '../../libs/components/vue-drr'
import BgPreview from '../../libs/components/BgPreview'
import VideoFirstFrame from '../../libs/components/VideoFirstFrame'
import RichTextDRR from '../../libs/components/RichTextDRR'
import Audios from '../../libs/components/property/Audios'
import MusicDRR from '../components/MusicDRR'

export default {
  name: 'Canvas',
  components: {MusicDRR, RichTextDRR, BgPreview, VueDrr, CanvasToolBar, RichText, ElementDRR, VideoFirstFrame, Audios},
  props: {
    enabled: {
      type: Boolean,
      default: false
    },
    param: {
      type: Object,
      default: () => {
      }
    }
  },
  data () {
    return {
      loading: false,
      paramSelected: false
    }
  },
  mounted () {
    document.addEventListener('keydown', this.handleKeyDown, false)
  },
  destroyed () {
    document.removeEventListener('keydown', this.handleKeyDown, false)
  },

  computed: {
    ...mapGetters(['elements', 'background']),
    currentTime () {
      return utils.getCurrentTime()
    }
  },
  methods: {
    ...mapMutations(['addHistory']),
    handleKeyDown (e) {
      const element = this.elements.find(item => item.active)
      if (element) {
        switch (e.keyCode) {
          // 使用delete键删除
          case 46:
            const elements = this.elements
            const index = elements.findIndex(item => item === element)
            if (index !== -1 && !this.paramSelected) {
              elements[index].active = false
              elements.splice(index, 1)
            }
            break
          case 37:
            element.x--
            break
          case 38:
            element.y--
            break
          case 39:
            element.x++
            break
          case 40:
            element.y++
            break
          default:
            break
        }
      }
    }
  }
}
</script>

<style lang="less">
  @import '../../libs/assets/less/variables';

  .editor {
    .canvas {
      position: absolute;
      top: 0;
      bottom: 0;
      width: 100%;
      .page {
        position: absolute;
        width: 320px;
        height: 508px;
        left: 50%;
        top: 50%;
        margin-left: -160px;
        margin-top: -254px;
        background-color: #fff;
        .content {
          outline: none;
          width: 320px;
          height: 508px;
          overflow: hidden;
        }
        .audio, .btn, img, video {
          width: 100%;
          height: 100%;
        }
      }
      footer {
        width: 100%;
        text-align: right;
        .info {
          margin: 12px 0 12px auto;
          font-size: 12px;
          color: #888;
          span:first-child {
            margin-right: 10px;
          }
        }
      }
    }
  }
</style>
