import Vue from 'vue'
import Vuex from 'vuex'
import load from '../../libs/store/load'
import user from '../../libs/store/user'

Vue.use(Vuex)

export default new Vuex.Store({
  modules: {
    load,
    user
  }
})
