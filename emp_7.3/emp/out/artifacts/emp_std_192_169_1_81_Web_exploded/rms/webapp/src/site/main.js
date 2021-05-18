// 全局插件
import 'babel-polyfill'
import Vue from 'vue'
import Vuex from 'vuex'
import ElementUI from 'element-ui'
import router from './router/index'

import '../libs/assets/css/normalize.css'
import 'element-ui/lib/theme-chalk/index.css'
import '../libs/assets/less/app.less'
import VueI18n from 'vue-i18n'
import App from './App'
import Store from './store/index'
// ueditor
import '../../static/UE/ueditor.config.js'
import '../../static/UE/ueditor.all.js'
import '../../static/UE/lang/zh-cn/zh-cn.js'
import '../../static/UE/ueditor.parse.min.js'

Vue.use(Vuex)
Vue.use(ElementUI)
Vue.use(VueI18n)

const messages = {
  zh: require('../libs/lang/zh'),
  en: require('../libs/lang/en'),
  zhCHT: require('../libs/lang/zhCHT')
}

const i18n = new VueI18n({
  locale: 'zh', // 语言标识
  messages
})

Vue.config.productionTip = false

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  i18n,
  store: new Vuex.Store(Store),
  components: { App },
  template: '<App/>'
})
