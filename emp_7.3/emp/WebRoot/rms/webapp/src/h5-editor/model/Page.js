export default class Page {
  constructor () {
    this.background = {
      color: '',
      src: '',
      crop: {
        w: 0,
        h: 0,
        left: 0,
        top: 0,
        url: '',
        style: {}
      },
      transparency: 0
    }
    this.elements = []
    this.histories = []
    this.paramArr = []
    this.param = {}
    this.canInsert = false
  }
}
