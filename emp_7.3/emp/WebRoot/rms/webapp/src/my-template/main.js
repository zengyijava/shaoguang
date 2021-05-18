import 'babel-polyfill'
import Vue from 'vue'

import '../../theme/index.css'
import '../libs/assets/css/normalize.css'
import '../libs/assets/less/app.less'
import axios from '../libs/axios'
import router from './router/index'
import store from './store'
import VueI18n from 'vue-i18n'
import ElementUI from 'element-ui'
import VueClipboards from 'vue-clipboards'
import App from './App'

Vue.use(ElementUI, {size: 'small'});
Vue.use(VueClipboards)
Vue.use(VueI18n)

axios.setResponse(store)

const messages = {
  zh: require('../libs/lang/zh')
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
  store,
  i18n,
  components: { App },
  template: '<App/>'
})
