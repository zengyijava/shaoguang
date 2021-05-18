import Vue from 'vue'
import Vuex from 'vuex'

import load from '../../libs/store/load'
import user from '../../libs/store/user'
import editor from './editor/index'

Vue.use(Vuex)

export default new Vuex.Store({
  modules: {
    load,
    user,
    editor
  }
})
