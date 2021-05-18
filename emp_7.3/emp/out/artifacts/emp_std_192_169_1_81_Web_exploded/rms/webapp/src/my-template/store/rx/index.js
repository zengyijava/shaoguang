import getters from './getters'

const state = {
  resourceList: [],
  rxMarkData: {
    resourceMode: '1',
    signType: '1',
    smsSign: '',
    smsTag: '0',
    tagMarkType: '1',
    smsContent: '示例短信示例短信示例短信示例短信示例短信示例短信。',
    resourceUrl: 'http://www.montnets.com/',
    markType: '1'
  }
}

export default {
  state,
  getters
}
