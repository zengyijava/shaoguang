export default {
  // 所有数据请求的基础地址
  // API_BASE_URL: '/emp/',
  API_BASE_URL: '',
  // 版本控制
  VERSION: '3.0',
  // iframe Url
  IFRAME_URL: 'rms/webapp/dist/static/Editor.html',
  /*
  ** 下面三条‘TEXT_EDITOR_LINK’，‘MEDIA_EDITOR_LINK’，‘CARD_EDITOR_LINK’为打包给EMP系统访问编辑器地址，与本地三个访问地址不能共存
  ** 别问为什么，任性
  */
  TEXT_EDITOR_LINK: 'textEditor.meditorPage',
  MEDIA_EDITOR_LINK: 'mediaEditor.meditorPage',
  SCENE_EDITOR_LINK: 'cardEditor.meditorPage',
  H5_EDITOR_LINK: 'h5Editor.meditorPage',

  /*
  ** 下面三条‘TEXT_EDITOR_LINK’，‘MEDIA_EDITOR_LINK’，‘CARD_EDITOR_LINK’为前端本地测试访问编辑器地址，与EMP三个访问地址不能共存
  ** 别问为什么，任性
  */
  // TEXT_EDITOR_LINK: 'text-editor',
  // MEDIA_EDITOR_LINK: 'media',
  // SCENE_EDITOR_LINK: 'card',
  // H5_EDITOR_LINK: 'h5-editor',

  // 获取地址栏参数，本地测试从url中获取，上线环境需改为iframe
  GET_URL_PARAMS: {
    LIST: 'iframe',
    PREVIEW: 'iframe',
    POPLIST: 'iframe'
  },

  /*
  ** 列表可以进入的编辑器配置，不能为空至少保留一个，别只能为'card'场景/'media'富媒体/'text'富文本
  ** 版本修改，此配置只能为['media']和['card', 'media', 'text']两种配置，其余配置不支持
  */
  EDITOR_CONFIG_ARRAY: ['scene', 'media', 'text', 'h5'],
  // EDITOR_CONFIG_ARRAY: ['scene', 'media', 'text'],
  // EDITOR_CONFIG_ARRAY: ['media'],
  // 列表页中立即发送配置地址
  LIST_SEND_NOW_LINK: 'rms_rmsSameMms.htm',
  // 列表页中模板导出公共地址
  LIST_FILE_IMPORT_URL: 'meditor/exportFile?fileUrl=',
  // 动态二维码占位图地址
  QRCODE_IMG_PLACEHOLDER_URL: 'rms/meditor/image/qrcode.png',
  // 图片音视频文件格式限制
  IMAGE_FORMAT: ['.png', '.jpg', '.jpeg', '.gif', '.bmp'],
  AUDIO_FORMAT: ['.aac', '.m4a', '.wma', '.mp3'],
  VIDEO_FORMAT: ['.wmv', '.3gp', '.avi', '.f4v', '.m4v', '.mp4', '.mpg', '.ogv', '.swf', '.vob'],
  // 音视频上传最大限制50M
  MEDIA_MAX_SIZE: 52428800
}
