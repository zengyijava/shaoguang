import Vue from 'vue'
import Vuex from 'vuex'
import media from './media'
import user from '../../libs/store/user'
import editor from './editor/index'

Vue.use(Vuex)

export default new Vuex.Store({
  modules: {
    media,
    user,
    editor
  }
})
