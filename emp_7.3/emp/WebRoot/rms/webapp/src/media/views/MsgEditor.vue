<template>
  <div class="media-editor">
    <section class="canvas">
      <Header></Header>
      <article>
        <div class="content rich-content">
          <RichText v-model="msgTemplate.content.template"
            :support-return="false" :placeholder="placeholder"
            @input="addMsgHistory($event)">
          </RichText>
        </div>
      </article>
      <MsgToolBar></MsgToolBar>
      <footer>
        <div class="info">
          {{used}}/980, {{ $t('media.total') }}{{count}}{{ $t('media.strip') }}
        </div>
      </footer>
    </section>
    <aside class="property msg-property" :class="{'show': visible}">
      <Panel :title="$t('property.params.paramSet')" :show-close="false">
        <el-form slot="body" class="text-setting">
          <el-form-item>
            <el-button class="param-btn" type="text" size="small"
              v-for="param in intersects"
              :key="param.name" :disabled="getDisabled(param)"
              @click="handleInsert(param)">+{{param.name}}
            </el-button>
          </el-form-item>
        </el-form>
      </Panel>
    </aside>
  </div>
</template>

<script>
import {mapGetters, mapMutations} from 'vuex'
import utils from '../../libs/utils'
import NavBar from '../../libs/views/NavBar'
import Panel from '../../libs/components/Panel'
import MsgToolBar from './MsgToolBar'
import RichText from '../../libs/components/SimpleRichText'
import Header from '../../libs/components/Header'

export default {
  name: 'MsgEditor',
  components: {Header, RichText, MsgToolBar, Panel, NavBar},
  data () {
    return {
      usedParams: [],
      placeholder: this.$t('media.pEnterText')
    }
  },
  computed: {
    ...mapGetters(['msgTemplate', 'msgText', 'intersects']),
    used () {
      return this.msgText ? this.msgText.length : 0
    },
    visible () {
      return this.intersects.length > 0
    },
    count () {
      return Math.ceil(this.used / 70)
    }
  },
  methods: {
    ...mapMutations(['updateMessage']),
    ...mapMutations('media', ['addMsgHistory']),
    getDisabled (param) {
      if (param.type === 14 || this.msgText.includes(utils.param2Value(param))) {
        return true
      }
      return false
    },
    handleInsert (param) {
      if (this.usedParams.indexOf(param) === -1 && param.type !== 14) {
        this.usedParams.push(param)
        let richText = utils.removePlaceholder(this, '.media-editor .rich-text')
        // 设置文本焦点
        richText.focus()
        utils.insertHtml(utils.param2Value(param))
        this.$nextTick(() => {
          this.updateMessage(richText.innerHTML)
          // 添加历史记录
          this.addMsgHistory(richText.innerHTML)
        })
      }
    }
  },

  watch: {
    msgText (newValue, oldValue) {
      if (newValue !== oldValue) {
        this.usedParams = utils.getIntersectParams(newValue, this.intersects)
      }
    }
  }
}
</script>

<style lang="less">
  @import '../assets/less/media-editor';

  .msg-property {
    .el-button {
      width: 80px;
      background-color: #f4f5f8;
      margin-right: 20px;
      & + .el-button {
        margin-left: 0;
        margin-top: 8px;
      }
    }
  }
</style>
