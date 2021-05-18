import Vue from 'vue'
import Vuex from 'vuex'

import load from '../../libs/store/load'
import user from '../../libs/store/user'
import api from '../../libs/store/index'
import media from './editor/index'

Vue.use(Vuex)

export default new Vuex.Store({
  modules: {
    load,
    user,
    api,
    media
  }
})
