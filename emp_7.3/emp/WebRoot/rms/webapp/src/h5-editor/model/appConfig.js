const style = {
  fontFamily: '微软雅黑',
  fontStyle: 'normal',
  fontWeight: 'normal',
  textDecoration: 'none',
  textAlign: 'left'
}

const appConfig = {
  width: 260,
  height: 120,
  title: {
    type: 'text',
    tag: 'text_1',
    text: '',
    placeholder: '我的H5标题！',
    style: {
      ...style,
      color: '#222222',
      left: 12,
      fontSize: 14,
      top: 12,
      width: 240,
      maxLine: 2,
      height: 18
    }
  },
  description: {
    type: 'text',
    tag: 'text_2',
    text: '',
    placeholder: '赶快来描述一下我的H5吧！',
    style: {
      ...style,
      color: '#333333',
      fontSize: 12,
      left: 80,
      top: 38,
      maxLine: 4,
      width: 172,
      height: 36
    }
  },
  cover: {
    type: 'image',
    tag: 'image_3',
    src: '',
    style: {
      left: 12,
      top: 28,
      width: 60,
      height: 60
    }
  }
}

export default appConfig
