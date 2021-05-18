export default {
  state: {
    loading: false,
    code: 1
  },
  getters: {
    loading: (state, getters) => state.code === -1
  },
  mutations: {
    preload (state) {
      state.code = -1
    },
    loaded (state) {
      state.code = 1
    },
    error (state) {
      state.code = 0
    }
  }
}
