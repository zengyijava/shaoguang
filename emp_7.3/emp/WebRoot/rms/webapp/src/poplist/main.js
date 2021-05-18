import 'babel-polyfill'
import Vue from 'vue'
import App from './App'
import infiniteScroll from 'vue-infinite-scroll'
import ElementUI from 'element-ui'
import VueI18n from 'vue-i18n'
import '../libs/assets/css/normalize.css'
import '../../theme/index.css'
import '../libs/assets/less/app.less'
import utils from '../libs/utils'
import config from '../libs/config'

Vue.config.productionTip = false

Vue.use(VueI18n)
Vue.use(ElementUI)
Vue.use(infiniteScroll)

let _languageVal = function () {
  const _setLang = utils.getUrlParameters('lang', false, config.GET_URL_PARAMS.POPLIST)
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
  components: { App },
  template: '<App/>'
})
