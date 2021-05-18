const getter = {
  elements: (state, getter) => state.elements,
  element: (state, getter) => state.element,
  params: (state, getter) => state.params,
  hasSelected: (state, getters) =>
    getters.elements.some(element => element.active === true)
}
export default getter
