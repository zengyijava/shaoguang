const getter = {
  element: (state, getters) => state.element,
  currentElement: (state, getters) => state.currentElement,
  usedParams: (state, getters) => state.usedParamArr,
  insertDisabled: (state, getters) => state.insertDisabled,
  textEditable: (state, getters) => state.textEditable,
  histories: (state, getters) => state.histories,
  messages: (state, getters) => state.messages,
  cropper: (state, getters) => state.cropper,
  loadState: (state, getters) => state.loadState
}
export default getter
