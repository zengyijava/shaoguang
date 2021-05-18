const getter = {
  styleVisible: (state, getters) => state.styleVisible,
  index: (state, getters) => state.index,
  element: (state, getters) => state.element,
  style: (state, getters) => getters.element.style,
  histories: (state, getters) => state.histories
}
export default getter
