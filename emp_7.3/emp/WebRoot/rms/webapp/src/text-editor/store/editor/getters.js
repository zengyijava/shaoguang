const getter = {
  editor: (state, getters) => state.editor,
  degrees: (state, getters) => state.degrees,
  useIds: (state, getters) => state.useIds,
  params: (state, getters) => state.paramArr,
  param: (state, getters) => state.param,
  canInsert: (state, getters) => state.canInsert,
  histories: (state, getters) => state.histories
}
export default getter
