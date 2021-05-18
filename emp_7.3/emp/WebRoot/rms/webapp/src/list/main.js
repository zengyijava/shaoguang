import 'babel-polyfill'
import Vue from 'vue'

import '../../theme-green/index.css'
import '../libs/assets/css/normalize.css'
import '../libs/assets/less/app.less'
import '../libs/assets/css/iconfont.css'
import axios from '../libs/axios'
import store from './store/index'
import utils from '../libs/utils'
import config from '../libs/config'
import ElementUI from 'element-ui'
import VueI18n from 'vue-i18n'
import VueClipboards from 'vue-clipboards'
import App from './App'

Vue.use(ElementUI)
Vue.use(VueClipboards)
Vue.use(VueI18n)

axios.setResponse(store)

// 多语言配置，由调用者在iframe中设置data-param属性和值构成方式类url带参
let _languageVal = function () {
  const _setLang = utils.getUrlParameters('lang', false, config.GET_URL_PARAMS.LIST)
  let _setVal
  switch (_setLang) {
    case 'zh_CN':
      _setVal = 'zh'
      break
    case 'zh_TW':
      _setVal = 'zhCHT'
      break
    case 'zh_HK':
      _setVal = 'en'
      break
    default:
      _setVal = 'zh'
  }
  return _setVal
}

const messages = {
  zh: require('../libs/lang/zh'),
  en: require('../libs/lang/en'),
  zhCHT: require('../libs/lang/zhCHT')
}

const i18n = new VueI18n({
  locale: _languageVal(), // 语言标识
  messages
})

Vue.config.productionTip = false

/* eslint-disable no-new */
new Vue({
  el: '#app',
  i18n,
  store,
  components: { App },
  template: '<App/>'
})
