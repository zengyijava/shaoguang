<template>
  <nav class="nav-bar">
    <div class="logo">
      <img src="../../libs/assets/img/logo_icon.png">
      <span class="title">{{ $t('navBar.H5') }}</span>
    </div>
    <ul class="menu-bar" slot>
      <TemplatePicker :sceneType="'15'" @getTempDetail="getTempDetail"></TemplatePicker>
      <li @click="addText">
        <i class="iconfont icon-text"></i>{{ $t('navBar.text') }}
      </li>
      <li @click="setAddType('image')">
        <i class="iconfont icon-picture"></i>{{ $t('navBar.img') }}
      </li>
      <li>
        <el-dropdown @command="setAddType">
          <span class="el-dropdown-link">
            <i class="iconfont icon-music"></i>{{ $t('navBar.audio') }}
          </span>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item command="music">{{ $t('navBar.bgAudio') }}</el-dropdown-item>
            <el-dropdown-item command="audio">{{ $t('navBar.audio') }}</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </li>
      <li @click="setAddType('video')">
        <i class="iconfont icon-video"></i>{{ $t('navBar.video') }}
      </li>
      <li @click="addButton">
        <i class="iconfont icon-button"></i>{{ $t('navBar.button') }}
      </li>
      <li @click="setBgState(true)">
        <i class="iconfont icon-bg"></i>{{ $t('navBar.bg') }}
      </li>
    </ul>
    <div class="button-group">
      <el-button class="preview-btn" type="primary" size="small" @click="previewVisible=true">{{ $t('richText.commit') }}</el-button>
      <el-button type="primary" size="small" :loading="loading && submitType === 0" @click="save(0)">{{ $t('property.keep') }}</el-button>
      <el-button type="info" size="small" @click="closeConfirm">{{ $t('richText.close') }}</el-button>
    </div>
    <el-dialog class="preview-dialog" top="10vh" width="1089px" :visible.sync="previewVisible">
      <p slot="title">{{ $t('H5.preview_and_submit') }}</p>
      <Preview :closed="previewVisible">
        <PreviewForm slot :submit-type="1" @save="save(1)" @close="close"></PreviewForm>
      </Preview>
    </el-dialog>
  </nav>
</template>

<script>
import {mapActions, mapGetters, mapMutations} from 'vuex'
import utils from '../../libs/utils'
import ImagePicker from '../../libs/components/ImagePicker'
import MediaPicker from '../../libs/components/MediaPicker'
import TemplatePicker from '../../libs/components/TemplatePicker'
import NavBar from '../../libs/views/NavBar'
import Preview from './Preview'
import PreviewForm from './PreviewForm'

const element = {
  locked: false,
  visible: true,
  active: true
}

export default {
  name: 'H5NavBar',
  components: {PreviewForm, Preview, TemplatePicker, NavBar, MediaPicker, ImagePicker},
  data () {
    return {
      dialogStatus: false,
      previewVisible: false,
      visibleAudio: false,
      submitType: -1
    }
  },
  computed: {
    ...mapGetters(['h5', 'app', 'params', 'currentPage', 'pages', 'element', 'music', 'swiper', 'loading', 'corpId', 'count']),
    paramPages () {
      return this.pages.map(({background, elements}) => {
        return {
          background,
          elements
        }
      })
    },
    length () {
      return this.currentPage.elements.length
    }
  },
  methods: {
    ...mapActions(['addTemplate', 'getTempDetail', 'setLoading']),
    ...mapMutations(['setLoading', 'addHistory', 'setAddType', 'selectElement', 'setBgState', 'updateCount']),
    addText () {
      this.updateCount()
      const text = {
        ...element,
        x: 10,
        y: 20,
        z: this.length,
        w: 300,
        type: 'text',
        tag: this.$t('navBar.text') + this.count,
        text: this.$t('richText.dbClick'),
        style: {
          fontSize: '14px',
          fontWeight: 'normal',
          textAlign: 'left',
          color: '#333333'
        }
      }
      this.currentPage.elements.push(text)
      this.selectElement(text)
      this.addHistory(this.currentPage.elements)
    },
    // 添加按钮
    addButton () {
      this.updateCount()
      const btn = {
        ...element,
        x: 110,
        y: 260,
        z: this.length,
        w: 100,
        h: 32,
        type: 'button',
        tag: this.$t('navBar.button') + this.count,
        text: this.$t('H5.btn_name'),
        action: '2',
        tabsAction: '2',
        style: {
          fontSize: '14px',
          textAlign: 'center',
          color: '#ffffff',
          backgroundColor: '#2e95ff',
          borderRadius: 0
        }
      }
      this.currentPage.elements.push(btn)
      this.selectElement(btn)
      this.addHistory(this.currentPage.elements)
    },
    save (type) {
      const action = utils.getUrlParameters('action', false, 'url')
      const id = utils.getUrlParameters('id', false, 'url')
      const isPublic = utils.getUrlParameters('isPublic', false, 'url')

      const names = Array.from(document.querySelectorAll('.page-admin .j-btn')).map(item => {
        return utils.paramValue2Name(item.value)
      })
      this.submitType = type
      const additional = {
        tmid: this.h5.tmid,
        tmName: this.app.title.text,
        subType: type,
        tmpType: 15,
        industryId: '',
        useId: '',
        corpId: this.corpId,
        paramArr: [...this.params].filter(param => names.find(name => param.name === name)),
        isPublic: 0
      }
      const content = {
        pages: this.paramPages,
        swiper: this.swiper
      }
      if (this.music && this.music.src) {
        content.music = this.music
      }
      const param = {
        ...additional,
        app: this.app,
        tempArr: [{
          content,
          tmpType: 15,
          h5Type: 1,
          cardHtml: '',
          degree: 0,
          degreeSize: 0
        }]
      }
      if (id !== '') {
        param.tmid = id
      }
      if (isPublic !== '') {
        param.isPublic = isPublic
      }
      if (action === 'add') {
        param.tmid = 0
      }
      // 设置非激活状态
      this.pages.forEach(page => {
        page.elements.forEach(element => {
          // 移除active属性
          delete element.active
          if (element.type === 'text') {
            const str = element.text
            // 从text中移除active类
            element.text = str.replace(/j-btn active/, 'j-btn')
          }
        })
        if (page !== this.currentPage) {

        }
      })
      console.log(JSON.stringify(param))
      this.addTemplate(param)
    },
    // 弹层关闭回调
    dialogStatusCB () {
      this.dialogStatus = false
    },
    // 立即使用
    immediateUse (id) {
      this.getTempDetail({
        tmId: id,
        previewType: 1
      })
      this.dialogStatus = false
    },
    close () {
      this.previewVisible = false
    },
    back () {
      if (window.opener && !window.opener.closed) {
        window.close()
      }
    },

    closeConfirm () {
      this.$confirm(this.$t('navBar.closeEditorHint'), this.$t('deleHintText.titleText'), {
        confirmButtonText: this.$t('richText.true'),
        cancelButtonText: this.$t('media.cancel'),
        type: 'warning'
      }).then(() => {
        this.back()
      }).catch(() => {
      })
    }
  }
}
</script>

<style lang="less">
  @import '../../libs/assets/less/nav-bar';
  @import '../../libs/assets/less/menu-bar';

  .el-dropdown {
    color: inherit;
  }
</style>
