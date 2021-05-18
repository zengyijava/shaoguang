import getters from './getters'
import mutations from './mutations'
import actions from './actions'

export default {
  state: {
    degrees: [],
    useIds: [],
    card: {
      tmid: 0,
      tmName: '',
      tmpType: 12,
      industryId: '',
      useId: '',
      corpId: '',
      isPublic: 0
    },
    template: {
      tmpType: 12,
      content: {
        w: 260,
        h: 300,
        bgSrc: '',
        elements: {
          texts: [],
          images: [],
          audios: [],
          videos: [],
          buttons: [],
          qrcodes: []
        }
      },
      cardHtml: '',
      degree: 0,
      degreeSize: 0
    },
    mediaTemplate: {
      tmpType: 11,
      content: [],
      cardHtml: '',
      degree: 0,
      degreeSize: 0
    },
    msgTemplate: {
      tmpType: 14,
      content: {
        template: ''
      }
    },
    imageSize: {},
    // 参数数组，只增不减，用来做参数对比
    paramArr: [],
    // 参数数组，可增可减，用来生成按钮组并保存到后端
    intersects: [],
    param: {
      type: 1,
      name: '参数1',
      hasLength: 1,
      lengthRestrict: 0,
      minLength: 0,
      maxLength: 32
    },
    canInsert: false,
    isLoading: false
  },
  getters,
  mutations,
  actions
}
