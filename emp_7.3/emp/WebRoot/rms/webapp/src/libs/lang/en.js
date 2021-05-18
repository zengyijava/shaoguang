module.exports = {
  /**
   * 列表级预览--Listing-level preview
   */
  moduleTips: {
    preview: 'Preview',
    copy: 'Copy',
    export: 'Export',
    share: 'Share',
    del: 'Delete'
  },
  // 场景列表搜索类型下拉选择文字
  sceneListSearchTypeSelect: {
    description: 'Fuxin type',
    options: {
      all: {label: 'All'},
      scene: {label: 'Scene'},
      media: {label: 'Rich media'},
      text: {label: 'Rich text'},
      h5: {label: 'Ent Show'}
    }
  },
  // 场景列表查询按钮描述
  sceneListQueryBtnText: 'Inquire',
  // 场景列表表单输入默认占位文字
  sceneInputPlaceholderText: 'Please input',
  // 场景ID错误校验描述
  sceneIdErrorDescription: 'Please fill in the correct scene Id',
  // 权限提示信息
  authErrorHintMsg: '请联系管理员开通对应列表查看权限！',
  // 公共场景列表搜索文字
  commonSceneSearchText: {
    sceneId: 'Scene ID',
    sceneTitle: 'Scene topic',
    corpName: '企业名称'
  },
  // 我的场景列表搜索条件文字
  mySceneSearchText: {
    sceneStatusSelect: {
      description: 'Scene status',
      options: {
        all: {label: 'All'},
        start: {label: 'Enabled'},
        forbid: {label: 'Disabled'},
        draft: {label: 'Draft'}
      }
    },
    templateTypeSelect: {
      description: 'Scene type',
      options: {
        all: {label: 'All'},
        static: {label: 'Common static template'},
        dynamic: {label: 'Common dunamic template'}
      }
    },
    auditStatusSelect: {
      description: 'Approval status',
      options: {
        all: {label: 'All'},
        no: {label: 'Unapproved'},
        underway: {label: 'Pending'},
        forbid: {label: 'Disabled'},
        pass: {label: 'Approved'},
        fail: {label: 'Rejected'}
      }
    },
    createDate: 'Create time',
    startDate: 'Start date',
    endDate: 'End date'
  },
  // 场景列表行业和用途选择
  industryAndUseText: {
    industryText: 'Industry',
    allIndustryText: 'All industry',
    useText: 'Use',
    allUseText: 'All use',
    moreText: 'More'
  },
  // 场景列表添加层
  sceneListAdd: {
    addBtnDescription: 'Create new scene',
    addPopTitle: 'Select scene type',
    addSceneTitle: 'Interactive message',
    addSceneDesc: 'Support link jump, open APP and other interactive operations',
    addMediaTitle: 'Graphic message',
    addMediaDesc: 'Support text, image, audio, video and other types of information',
    addTextTitle: 'Rich text message',
    addTextDesc: 'Text messages that can be marked red and bold',
    addH5Title: 'H5',
    addH5Desc: 'Scenario messages which is closer to user usage scenarios'
  },
  // 场景列表下单个模板
  sceneListTemplateLi: {
    setShortcutText: 'Set as quick scene',
    cancelShortcutText: 'Cancel quick scene',
    copyLink: 'Copy the link',
    copyLinkSuccess: 'The link has been copied to the clipboard',
    copyLinkFail: 'Link copy failed',
    checkinText: 'Pending',
    checkFailText: 'Rejected',
    checkSuccess: '审批通过',
    forbiddenText: 'Disabled',
    editText: 'Edit',
    editNowText: 'Edit now',
    sendNowText: 'Send now',
    previewTitleText: 'Effect preview',
    moreInfoText: {
      titleText: 'Fuxin topic',
      sizeText: 'Capacity',
      useCountText: 'Number of use',
      frameText: 'Range',
      modelTypeText: 'Tempalte type',
      createUserText: 'Creator',
      companyText: 'Organizaiton',
      companyCodeText: 'Company ID',
      createTimeText: 'Create time',
      tmpStateText: 'Template status',
      operatorStateText: 'Mobile operator status'
    }
  },
  // 删除场景提示
  deleHintText: {
    titleText: 'Tips',
    hintText: 'Sure to delete the current scene?',
    sureText: 'Yes',
    cancelText: 'No'
  },
  // 预览
  previewText: {
    cardsTitleText: 'Interactive Layout',
    mediaTitleText: 'Graphic Layout',
    richTextTitleText: ' Rich Text Layout',
    supplementText: 'Complement Layout',
    supplementOneText: ' Complement layout 1',
    supplementTwoText: ' Complement layout 2',
    companyText: 'Preview',
    hintMsgText: 'Note: The practical layout will be displayed as several types, which based on the actual support of mobile terminals.'
  },
  /**
   * 场景编辑器--scene editor
   */
  // 场景tab--scene tab
  cardTabs: {
    cardStyle: 'Scene style',
    supplementStyleOne: 'Complement layout 1',
    supplementStyleTwo: 'Complement layout 2'
  },
  // 功能设置--function setting
  functionSetting: {
    funSet: 'Function setting',
    operationType: 'Operation type',
    urlJump: 'Link to jump',
    copy: 'Copy',
    selected: 'please select the content you want to copy',
    dial: 'Dial',
    dialPhone: 'Please input the number you want to dial',
    disec: 'The above number will be dial, format: 075512345678 or 4001234567'
  },
  // 样式设置
  styleSetting: {
    styleTitle: '场景样式设置',
    styleOptions: {
      all: '全部',
      standard: '标准',
      business: '商务',
      cartoon: '卡通',
      chineseStyle: '中国风'
    }
  },
  // 按钮设置--button setting
  buttonSetting: {
    buttonName: 'Name',
    selectBut: 'Please select the operation of button',
    openUrl: 'Open link',
    openApp: 'Open APP',
    openFastApp: 'Fast App',
    openMap: 'Open map',
    city: 'City',
    addr: 'Address',
    selectAddr: 'Select location',
    urlAddr: 'URL: ',
    appAddr: 'APP Link: ',
    bagName: 'Package Name',
    route: 'Route',
    params: 'Parameter',
    apkLink: 'Android Link: ',
    apkDownloadURL: 'Android Download URL: ',
    ipaLink: 'IOS Link: ',
    ipaDownloadURL: 'IOS Download URL: ',
    selection: 'Physical location selection',
    enterNr: 'Please enter content',
    cancel: 'Cancel',
    sure: 'OK'
  },
  // 画布--canvas
  canvas: {
    notSupportVideo: 'Your browser does not support Video tags.',
    notSupportAudio: 'Your browser does not support Audio tags.',
    currentCapacity: 'Current capacity ',
    filePosition: 'Range: ',
    files: 'Range',
    clearFirm: 'Clear',
    trueClear: 'Sure to clear out the content area?',
    tipsZip: 'The upper limit of the audit of scene template submission is 1.9M. Images and video can be clipped and compressed by using the editing function',
    tipsFrame: 'Up to 15 frames can be created',
    tipsDefaultInfo: 'Drag to adjust Bubble height',
    trueDeleFrame: '确定要删除当前帧？'
  },
  // 图片设置--image setting
  cardImgSetting: {
    keepProportion: 'Maintain aspect ratio',
    masterImg: '原图',
    compressImg: '压缩图'
  },
  // 图片设置--image setting
  navBar: {
    sceneEditing: 'Interactive editor',
    H5: '企业秀',
    text: 'Text',
    img: 'Image',
    audio: 'Audio',
    bgAudio: '背景音乐',
    video: 'Video',
    qrCode: 'QR code',
    button: 'Button',
    bg: '背景',
    add_qrcode_tips: 'At most add one QR code!',
    add_audio_tips: 'At most add one audio!',
    add_video_tips: 'At most add one video!',
    closeEditorHint: '确定要关闭当前编辑器？'
  },
  // 二维码设置--QR code setting
  qrCodeSetting: {
    sketchMap: 'The corrent image is for reference only, please upload your QR code image',
    fixedCode: 'Fixed QR code',
    dynamicCode: 'Dynamic QR code',
    currentParams: 'The current parameter is',
    imgUrl: 'Please upload location of image',
    upQrCode: 'Please upload QR code image',
    explain: 'Tips: you can use the mouse to drag to adjust the position,or you can use the keyboard Delete button to delete.'
  },
  // 文本设置--text setting
  textSetting: {
    setParems: 'Set the component contents as parameters',
    explain: 'Tips: After selecting the component, you can use the mouse to drag to adjust the position, or you can use the keyboard Delete button to delete.',
    paramsTip: '说明：文本显示时将根据参数实际长度进行自适应排版，建议您设置合理的参数长度并预留相应展示区域'
  },
  // 设置面板--Settings panel
  setting: {
    text: 'Textbox Settings',
    button: 'Button function setting',
    image: 'Image setting',
    qrcode: 'QR code setting',
    audio: 'Audio setting',
    video: 'Video setting',
    chart: 'Chart setting'
  },
  // 文本设置--text setting
  titleSetting: {
    titleName: 'Please input title name',
    enterName: 'Please input title'
  },
  /**
   * 公共设置模块--Public Settings Module
   */
  property: {
    borderRadius: 'Corner radius',
    color: 'Color',
    mediaText: 'Text',
    family: 'Font',
    glyph: 'Font pattern',
    size: 'Font size',
    text: 'Text ',
    bg: 'Background ',
    bgImg: 'Background Image',
    addImg: 'Superposition Image',
    opacity: 'Opacity',
    funSet: 'Funtion setting',
    click: 'Please set tap action',
    none: 'None',
    clickBigImg: 'After clicking the image, user can view full image in full screen ',
    lookBigImg: 'Full image',
    openLink: 'Open link',
    clickOpenLink: 'The user clicks on the image and opens the following link',
    explain: 'Tips: After selecting the component, you can use the mouse to drag to adjust the position, or you can use the keyboard Delete button to delete.',
    cardHintMsg: '请填写场景内容',
    mediaHintMsg: '请填写富媒体内容',
    completeHintMsgA: '请填写补充样式内容',
    completeHintMsgB: '请填写补充样式1内容',
    completeHintMsgC: '请填写补充样式2内容',
    completeLengthMsgA: '补充样式内容长度不能超过980个字符！',
    completeLengthMsgB: '补充样式2内容长度不能超过980个字符！',
    checkDegreeHint: '未检测到合适的档位，请联系管理员！',
    params: {
      argument: 'param',
      name: 'Parameter name',
      type: 'Parameter type',
      lengthRestrict: 'Limitation of length',
      variableLength: 'Variable length',
      fixedLength: 'Fixed length',
      minLength: 'Minimum length',
      maxLength: 'Maximum length',
      fixedLengthT: 'Fixed length',
      insertArgument: 'Insert parameter',
      paramSet: 'Parameter setting',
      resertArgument: 'Edit parameter',
      paramOne: 'Parameter1',
      cNumber: 'Pure digit(d)',
      zNumber: 'Letter and digit(w)',
      money: 'Amount(￥)',
      string: 'Any Character(c)',
      dateOne: 'Date(YYYY-MM-DDD)',
      dateTwo: 'Date(YYYY-MM-DD)',
      dateThree: 'Date(MM/DD)',
      dateFour: 'Date(MM/DD)',
      dateTimeOne: 'Date and time(YYYY-MM-DD hh:mm:ss)',
      dateTimeTwo: 'Date and time(YYYY-MM-DD hh:mm:ss)',
      dateTimeThree: 'Date and time(YYYY-MM-DD hh:mm:ss)',
      dateTimeFour: 'Date and time(YYYY-MM-DD hh:mm:ss)',
      time: 'Time(hh:mm:ss)',
      maxLengthTips: 'The parameter maximum length is less than the parameter minimum length!',
      tips: 'Tips',
      reset: 'Teset',
      exchange: 'Exchange',
      insert: 'Insert',
      resert: 'Change'
    },
    justUse: 'Use now',
    tSize: 'Size:',
    sSize: 'Size',
    effectPreview: 'Effect preview',
    position: 'Position:',
    wide: 'Width',
    height: 'Hight',
    nameT: 'Name:',
    nameTt: 'Name',
    pEnter: 'Please input',
    pEnterContent: 'Please input content',
    add: 'Add',
    operation: 'Operate',
    reName: 'Rename',
    keep: 'Save',
    del: 'Delete',
    complete: 'Finish',
    isDel: 'Sure to delete the current',
    yes: 'Yes',
    no: 'No'
  },
  // 公共-- public
  public: {
    imgUpTips: 'The image size must be less than or equal to 5MB!',
    mediaUpTips: 'The file size must be less than or equal to 50MB!',
    tmpKu: 'Template library',
    myScene: 'My scene',
    enterprise: '企业定制模板',
    noHaveTmp: 'No corresponding scene was found~~',
    enterId: 'Please input scene ID',
    enterName: 'Please input scene name',
    addTmp: 'Create',
    theme: 'Topic:',
    Industry: 'Industry:',
    pSelect: 'Please select',
    IndustryGm: 'Industry management',
    use: 'Use:',
    useGm: 'Use management',
    pThemeName: 'Please input topic name',
    selectTmp: 'Template',
    redo: 'Redo',
    revoke: 'Undo',
    audioHint: '您的浏览器不支持 audio 元素。',
    corpHint: '企业',
    corpHolder: '请输入企业名称'
  },
  /**
   * 富媒体编辑器--rich media editor
   */
  media: {
    richMediaEdit: 'Rich media editor',
    pEnterText: 'Please type your text here',
    richMedia: 'Rich media',
    supplementStyle: 'Complement Layout',
    user: 'Dear user',
    noCanUse: 'The current browser which you are using(IE 9 or the below the lower version) is not supported by rich media editor. For your better experience, please select brower from the below options.',
    sougoTipsA: '提示：为了您更好的编辑体验，请在使用',
    sougoTipsB: '搜狗浏览器',
    sougoTipsC: '时切换为',
    sougoTipsD: '极速模式',
    sougo: 'Sogou Browser Speedy Mode',
    tsz: '360 Browser Speedy Mode',
    onlineImgEdit: 'Online image editor',
    imgEdit: 'Edit image',
    textEdit: 'Edit text',
    textColor: 'Text color',
    bgColor: 'Background color',
    width: 'Width',
    rotate: 'Rotate',
    imgSize: 'Image size:',
    replacePicture: 'Repalce image',
    cut: 'Crop',
    text: 'Text',
    img: 'Image',
    cancel: 'Cancel',
    keep: 'Save',
    pParems: 'Please insert parameter into scene first!',
    up: 'Upward',
    down: 'Downward',
    del: 'Delete',
    total: 'Total',
    strip: 'Item',
    richMediaEditor: 'Rich media edit',
    pImg: 'Illustration',
    pUpImg: 'Please select image to upload:'
  },
  /**
   * 富文本编辑器--rich text editor
   */
  richText: {
    richTextEdit: 'Rich text editor',
    commit: 'Preview/submit',
    close: 'Close',
    richTextLook: 'Rich text preview',
    commitExamine: 'Submit to audit',
    back: 'Back',
    textLength: 'Length of text:',
    richTextEditT: 'Rich text edit',
    true: 'OK',
    richMedia: 'Graphic editor Preview',
    scene: 'Interactive editor Preview',
    dbClick: 'Double click edit text',
    changeAudio: 'Change audio',
    replacePicture: 'Replace image',
    cut: 'Edit image',
    remove: 'Remove image',
    replaceVideo: 'Replace video',
    cutVideo: 'Cut video',
    onlineVideo: 'Edit online video',
    videoSize: 'Size of video:',
    videoLength: 'Length of video:',
    delNotSlect: 'Delete unselected',
    delSlected: 'Delete selected',
    definition: 'Clarity',
    screen: '屏幕切换',
    gao: 'High',
    zhong: 'Middle',
    vertical: 'Vertical',
    horizontal: 'horizontal',
    videoHHint: '高清视频建议播放时长不超过20秒',
    videoLHint: '标清视频建议播放时长不超过30秒',
    videoVTHint: '竖屏视频建议播放时长不超过30秒',
    di: 'Low',
    setRichMedia: 'Rich information theme will be sent to users',
    contentEmpty: 'Please fill the rich text content',
    contentMaxLength: 'The text is more than 980 chars, please try again',
    cropEmpty: '请输入企业名并选择',
    audioName: '音频名称',
    recommendedColor: '推荐配色'
  },
  /*
  ** 企业秀
  */
  H5: {
    preview_prev_btn: '上一页',
    preview_next_btn: '下一页',
    apply_hint: '应用到所有页面',
    back_btn: '返回',
    title_hint: '我的H5标题！',
    description_hint: '赶快来描述一下我的H5吧！',
    select_all: '全选',
    dele: '删除',
    completed: '完成',
    batch_operation: '批量操作',
    up_audio_btn: '上传音频',
    up_audio_hint: '音频大小不超过10M，支持格式：mp3、m4a、wma、aac音频格式',
    up_audio_max_hint: '上传音频大小不能超过 10MB!',
    audio_dele_hint: '确定删除音频吗?',
    up_img_btn: '上传图片',
    up_img_hint: '图片大小不超过5M，支持格式：png、jpg、jpeg、gif、bmp',
    up_img_max_hint: '上传图片大小不能超过 5MB!',
    img_dele_hint: '确定删除图片吗?',
    hint_title: '提示',
    hint_sure: '确定',
    hint_cancel: '取消',
    audio_text: '音频',
    name_text: '名称',
    preview_and_submit: '预览提交',
    btn_name: '按钮名称',
    layer_hidden: '隐藏',
    layer_locked: '锁定',
    layer_copy: '复制',
    layer_to_top: '置于顶层',
    layer_move_up: '上移一层',
    layer_move_down: '下移一层',
    layer_to_bottom: '置于底层',
    layer_dele: '删除',
    img_material: '图片素材',
    audio_material: '音频素材',
    bg_audio_material: '背景音乐素材',
    video_material: '视频素材',
    open_bg_audio: '开启背景音乐',
    current_audio: '当前音频',
    auto_play: '自动播放',
    loop_play: '循环播放',
    show_in_all: '是否显示在所有页面',
    layer_admin: '图层管理',
    pages_admin: '页面管理',
    page_up: '上移',
    page_copy: '复制（不包含参数）',
    page_down: '下移',
    page_keep_one: '请至少保留一页',
    preview_title: '标题',
    preview_title_empty: '标题不能为空',
    preview_desc: '描述',
    preview_desc_empty: '描述不能为空',
    up_suggest: '建议上传',
    up_img_size: '200*200尺寸的图片',
    replace_cover: '更换封面',
    cover_empty: '封面不能为空',
    page_effect: '翻页效果',
    up_and_down_effect: '上下翻页',
    left_and_right_effect: '左右翻页',
    page_number: '显示页码',
    bottom_right: '底部右侧',
    bottom_left: '底部左侧',
    bottom_center: '底部居中',
    page_keep_time: '每页停留时间"秒"',
    cube_effect: '方块',
    slide_effect: '位移',
    fade_effect: '淡入',
    flip_effect: '3d翻转',
    text_param: '文本参数',
    text_setting_hint: '文本显示时将根据参数实际长度进行自适应排版，建议您设置合理的参数长度并预留相应展示区域',
    bg_setting: '背景设置',
    text_setting: '文本框设置',
    button_setting: '按钮功能设置',
    img_setting: '图片设置',
    audio_setting: '音频设置',
    video_setting: '视频设置',
    bg_audio_setting: '背景音乐设置',
    up_video_btn: '上传视频',
    up_video_hint: '视频大小不超过10M，支持格式：wmv、3gp、avi、f4v、m4v、mp4、mpg、ogv、swf、vob视频格式',
    up_video_max_hint: '上传视频大小不能超过 30MB!',
    video_dele_hint: '确定删除视频吗?'
  },
  validation: {
    beNumber: 'Please enter a number',
    beURL: 'Please enter a valid url'
  }
}
