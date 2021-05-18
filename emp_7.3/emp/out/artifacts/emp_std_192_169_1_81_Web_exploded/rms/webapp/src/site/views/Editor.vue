<template>
  <section class="editor-page">
    <div class="top-bar">
      <div class="top">
        <img src="../../libs/assets/img/logo_icon.png" />
        <p>网页编辑</p>
        <div class="close" @click="closeWindows">关闭</div>
        <div class="save" @click="saveH5">保存</div>
      </div>
    </div>
    <div class="editor-content">
      <div class="show-bar">
        <div class="phone-bar">
          <div class="phone-top"></div>
          <div class="phone-show">
            <div class="phone-titel" v-text="editorTitle"></div>
            <div class="phone-zz" v-show="editorAuthor">
              <span class="color-b" v-text="editorAuthor"></span>
              <span class="date-time" v-text="dateTime"></span>
            </div>
            <div class="phone-nr" v-html="contentStr"></div>
          </div>
        </div>
      </div>
      <div class="editor-bar">
        <div class="editor-input-bar">
          <input type="text" maxlength="64" v-model="editorTitle" placeholder="输入标题" />
          <span>{{editorTitle.length}}/64</span>
        </div>
        <div class="editor-input-bar mt0">
          <input type="text" maxlength="8" v-model="editorAuthor" placeholder="输入作者" />
          <span>{{editorAuthor.length}}/8</span>
        </div>
        <div class="editor-nr">
          <div id="container" class="edit-bar" type="text/plain"></div>
        </div>
      </div>
    </div>
  </section>
</template>

<script>
import { mapState, mapActions } from 'vuex'
const UE = require('UE')
export default {
  name: 'editor',
  data () {
    return {
      contentStr: '',
      editorTitle: '',
      editorAuthor: '',
      h5Id: '',
      htmlHead: '<!DOCTYPE html><html><head><meta http-equiv="Content-Type" content="text/html;charset=utf-8"/><meta content="width=device-width,initial-scale=1,maximum-scale=1,user-scalable=no" name="viewport"><style>*{padding: 0;margin: 0;}body{padding: 0 20px;}.phone-titel{width: 100%;font-size: 14px;font-weight: bold;color: #1e1e1e;line-height: 22px;margin: 15px 0 5px;word-wrap: break-word;}.phone-zz {width: 100%;font-size: 11px;margin-bottom: 15px;}.date-time {color: #b3b3b3;margin-left: 5px;}.color-b {color: #4b5f8e;}img,video{max-width: 100%;}@media screen and (min-width: 1024px) { body {width: 50%;margin: 0 auto;}}</style></head><body><div class="phone-titel">',
      htmlTitle: '</div><div class="phone-zz"><span class="color-b">',
      htmlAuthor: '</span><span class="date-time">',
      htmlFoot: '</body></html>',
      textStr: '',
      ue: ''
    }
  },
  computed: {
    ...mapState(['details']),
    dateTime () {
      let date = new Date()
      let seperator1 = '-'
      let year = date.getFullYear()
      let month = date.getMonth() + 1
      let strDate = date.getDate()
      if (month >= 1 && month <= 9) {
        month = '0' + month
      }
      if (strDate >= 0 && strDate <= 9) {
        strDate = '0' + strDate
      }
      let timeStr = year + seperator1 + month + seperator1 + strDate
      return timeStr
    }
  },
  mounted () {
    // 初始化编辑器
    this.ue = UE.delEditor('container')
    this.ue = UE.getEditor('container')
    // 编辑器变化时调用事件
    this.ue.addListener('contentChange', this.getText)
    // 编辑进来时执行赋值操作
    this.h5Id = this.$route.query.id
    console.log(this.h5Id)
    if (this.h5Id) {
      this.getDetail({ hId: this.h5Id })
      setTimeout(() => {
        this.editorTitle = this.details.data.title
        this.editorAuthor = this.details.data.author
        this.setContent(this.details.data.bodyContent)
      }, 500)
    }
    // Ctrl+S事件
    document.onkeydown = event => {
      event = event || window.event
      if (event.keyCode === 83 && event.ctrlKey) {
        console.log(this)
        event.preventDefault()
        this.saveH5()
      }
    }
  },
  methods: {
    ...mapActions(['saveEditor', 'getDetail', 'editorH5']),
    closeWindows () {
      window.close()
    },
    getText () {
      this.contentStr = this.ue.getContent()
    },
    setContent (text) {
      this.ue.setContent(text)
    },
    // 保存
    saveH5 () {
      if (!this.editorTitle) {
        this.$message.error('标题不能为空！')
        return
      } else if (!this.editorAuthor) {
        this.$message.error('作者不能为空！')
        return
      } else if (!this.contentStr) {
        this.$message.error('内容不能为空！')
        return
      }
      if (this.h5Id) {
        let paramsData = {
          hId: this.h5Id,
          title: this.editorTitle,
          author: this.editorAuthor
        }
        paramsData.content = this.htmlHead + this.editorTitle + this.htmlTitle + this.editorAuthor + this.htmlAuthor + this.dateTime + '</span></div>' + this.contentStr + this.htmlFoot
        this.editorH5(paramsData)
      } else {
        let paramsData = {
          title: this.editorTitle,
          author: this.editorAuthor
        }
        paramsData.content = this.htmlHead + this.editorTitle + this.htmlTitle + this.editorAuthor + this.htmlAuthor + this.dateTime + '</span></div>' + this.contentStr + this.htmlFoot
        this.saveEditor(paramsData)
      }
    }
  }
}
</script>

<style lang="less">
@import "../assets/less/site.less";
</style>
