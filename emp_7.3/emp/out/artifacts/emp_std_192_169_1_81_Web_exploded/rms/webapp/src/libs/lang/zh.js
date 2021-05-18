module.exports = {
  /**
   * 列表级预览
   */
  // 模板操作tips
  moduleTips: {
    preview: '预览',
    copy: '复制',
    export: '导出',
    share: '分享',
    del: '删除'
  },
  // 场景列表搜索类型下拉选择文字
  sceneListSearchTypeSelect: {
    description: '富信类型',
    options: {
      all: {label: '全部'},
      scene: {label: '交互'},
      media: {label: '图文'},
      text: {label: '富文本'},
      h5: {label: '企业秀'}
    }
  },
  // 场景列表查询按钮描述
  sceneListQueryBtnText: '查询',
  // 场景列表表单输入默认占位文字
  sceneInputPlaceholderText: '请输入',
  // 场景ID错误校验描述
  sceneIdErrorDescription: '请填写正确的场景Id',
  // 权限提示信息
  authErrorHintMsg: '请联系管理员开通对应列表查看权限！',
  // 公共场景列表搜索文字
  commonSceneSearchText: {
    sceneId: '场景ID',
    sceneTitle: '场景主题',
    corpName: '企业名称'
  },
  // 我的场景列表搜索条件文字
  mySceneSearchText: {
    sceneStatusSelect: {
      description: '场景状态',
      options: {
        all: {label: '全部'},
        start: {label: '已启用'},
        forbid: {label: '已禁用'},
        draft: {label: '草稿'}
      }
    },
    templateTypeSelect: {
      description: '场景类型',
      options: {
        all: {label: '全部'},
        static: {label: '通用静态模板'},
        dynamic: {label: '通用动态模板'}
      }
    },
    auditStatusSelect: {
      description: '审核状态',
      options: {
        all: {label: '全部'},
        no: {label: '未审批'},
        underway: {label: '审批中'},
        forbid: {label: '已禁用'},
        pass: {label: '审批通过'},
        fail: {label: '审批未通过'}
      }
    },
    createDate: '创建时间',
    startDate: '开始日期',
    endDate: '结束日期'
  },
  // 场景列表行业和用途选择
  industryAndUseText: {
    industryText: '行业',
    allIndustryText: '全部行业',
    useText: '用途',
    allUseText: '全部用途',
    moreText: '更多'
  },
  // 场景列表添加层
  sceneListAdd: {
    addBtnDescription: '创建新场景',
    addPopTitle: '选择场景类型',
    addSceneTitle: '交互消息',
    addSceneDesc: '支持链接跳转、打开APP等交互操作',
    addMediaTitle: '图文消息',
    addMediaDesc: '支持文本、图片、音频、视频等多类型信息',
    addTextTitle: '富文本消息',
    addTextDesc: '可以标红加粗的文本消息',
    addH5Title: '企业秀消息',
    addH5Desc: '丰富多彩的多页动画消息'
  },
  // 场景列表下单个模板
  sceneListTemplateLi: {
    setShortcutText: '设为快捷场景',
    cancelShortcutText: '取消快捷场景',
    copyLink: '复制链接',
    copyLinkSuccess: '链接已复制到剪贴板',
    copyLinkFail: '链接复制失败',
    checkinText: '审批中',
    checkFailText: '审批未通过',
    checkSuccess: '审批通过',
    forbiddenText: '已禁用',
    editText: '编辑',
    editNowText: '立即编辑',
    sendNowText: '立即发送',
    previewTitleText: '效果预览',
    moreInfoText: {
      titleText: '富信主题',
      sizeText: '容量',
      useCountText: '使用次数',
      frameText: '档位',
      modelTypeText: '模板类型',
      createUserText: '创建人',
      companyText: '所属机构',
      companyCodeText: '企业编码',
      createTimeText: '创建时间',
      tmpStateText: '模板状态',
      operatorStateText: '运营商状态'
    }
  },
  // 删除场景提示
  deleHintText: {
    titleText: '提示',
    hintText: '是否删除当前场景？',
    sureText: '是',
    cancelText: '否'
  },
  // 预览
  previewText: {
    cardsTitleText: '交互展示方式',
    mediaTitleText: '图文展示方式',
    richTextTitleText: '富文本展示方式',
    supplementText: '补充展示方式',
    supplementOneText: '补充展示方式一',
    supplementTwoText: '补充展示方式二',
    companyText: '预览',
    hintMsgText: '注：根据手机终端的实际支持情况，将会产生以上几种不同的展示方式，具体以手机接收情况为准。'
  },
  /**
   * 场景编辑器
   */
  // 场景tab
  cardTabs: {
    cardStyle: '场景样式',
    supplementStyleOne: '补充样式1',
    supplementStyleTwo: '补充样式2'
  },
  // 功能设置
  functionSetting: {
    funSet: '功能设置',
    operationType: '操作类型：',
    urlJump: '链接跳转',
    copy: '复制',
    selected: '请选择需要复制内容：',
    dial: '拨号',
    dialPhone: '请输入需要拨打的电话：',
    disec: '将调用系统拨号功能拨打以上填写的号码，格式如：075512345678或4001234567'
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
  // 按钮设置
  buttonSetting: {
    buttonName: '名称',
    selectBut: '请选择按钮操作',
    openUrl: '打开链接',
    openApp: '打开APP',
    openFastApp: '打开快应用',
    openMap: '打开地图',
    city: '城市',
    addr: '地址',
    selectAddr: '选择地理位置',
    urlAddr: '链接地址：',
    appAddr: 'APP地址：',
    bagName: '包名',
    route: '路径',
    params: '参数',
    apkLink: 'Android地址：',
    apkDownloadURL: 'Android下载地址：',
    ipaLink: 'IOS地址：',
    ipaDownloadURL: 'IOS下载地址：',
    selection: '地理位置选择',
    enterNr: '请输入内容',
    cancel: '取 消',
    sure: '确 定'
  },
  // 画布
  canvas: {
    notSupportVideo: '您的浏览器不支持Video标签。',
    notSupportAudio: '您的浏览器不支持audio标签。',
    currentCapacity: '当前容量：',
    filePosition: '档位：',
    files: '档',
    clearFirm: '清空',
    trueClear: '确定要将内容区清空吗？',
    tipsZip: '场景模板容量总和已超上限2M，图片，视频可使用编辑功能进行裁剪压缩',
    tipsFrame: '最多只能创建15帧！',
    tipsDefaultInfo: '拖动调节气泡高度',
    trueDeleFrame: '确定要删除当前帧？'
  },
  // 图片设置
  cardImgSetting: {
    keepProportion: '保持宽高比例',
    masterImg: '原图',
    compressImg: '压缩图'
  },
  // nav设置
  navBar: {
    sceneEditing: '交互编辑',
    H5: '企业秀',
    text: '文本',
    img: '图片',
    audio: '音频',
    bgAudio: '背景音乐',
    video: '视频',
    qrCode: '二维码',
    button: '按钮',
    bg: '背景',
    add_qrcode_tips: '最多添加一个二维码！',
    add_audio_tips: '最多添加一个音频！',
    add_video_tips: '最多添加一个视频！',
    closeEditorHint: '确定要关闭当前编辑器？'
  },
  // 二维码设置
  qrCodeSetting: {
    sketchMap: '当前为示意图，请上传二维码图片',
    fixedCode: '固定二维码',
    dynamicCode: '动态二维码',
    currentParams: '当前参数为',
    imgUrl: '，请传入图片地址',
    upQrCode: '上传二维码图片',
    explain: '说明：鼠标可拖动组件调整位置、大小 选中后使用键盘删除'
  },
  // 文本设置
  textSetting: {
    setParems: '将组件内容设为参数',
    explain: '说明：选中组件后，可使用鼠标拖动调整位置，也可使用键盘Delete键进行删除',
    paramsTip: '说明：文本显示时将根据参数实际长度进行自适应排版，建议您设置合理的参数长度并预留相应展示区域'
  },
  // 设置面板
  setting: {
    text: '文本框设置',
    button: '按钮功能设置',
    image: '图片设置',
    qrcode: '二维码设置',
    audio: '音频设置',
    video: '视频设置',
    chart: '图表设置'
  },
  // 文本设置
  titleSetting: {
    titleName: '标题名称',
    enterName: '请输入标题'
  },
  /**
   * 公共设置模块
   */
  property: {
    borderRadius: '边框弧度',
    color: '颜色',
    mediaText: '文本',
    family: '字体',
    glyph: '字形',
    size: '字号',
    text: '文字',
    bg: '背景',
    bgImg: '背景图片',
    addImg: '叠加图片',
    opacity: '透明度',
    funSet: '功能设置',
    click: '请设置点击操作',
    none: '无',
    clickBigImg: '用户点击该图片后可以全屏查看大图',
    lookBigImg: '查看大图',
    openLink: '打开链接',
    clickOpenLink: '用户点击该图片后打开以下链接',
    explain: '说明：选中组件后，可使用鼠标拖动调整位置，也可使用键盘Delete键进行删除',
    cardHintMsg: '请填写交互消息内容',
    mediaHintMsg: '请填写图文消息内容',
    completeHintMsgA: '请填写补充样式内容',
    completeHintMsgB: '请填写补充样式1内容',
    completeHintMsgC: '请填写补充样式2内容',
    completeLengthMsgA: '补充样式内容长度不能超过980个字符！',
    completeLengthMsgB: '补充样式2内容长度不能超过980个字符！',
    checkDegreeHint: '未检测到合适的档位，请联系管理员！',
    params: {
      argument: '参数',
      name: '参数名称：',
      type: '参数类型：',
      lengthRestrict: '长度限制：',
      variableLength: '可变长度',
      fixedLength: '固定长度',
      minLength: '最小长度：',
      maxLength: '最大长度：',
      fixedLengthT: '固定长度：',
      insertArgument: '插入参数',
      paramSet: '参数设置',
      resertArgument: '修改参数',
      paramOne: '参数1',
      cNumber: '纯数字（d）',
      zNumber: '字母数字（w）',
      money: '金额（￥）',
      string: '任意字符（c）',
      dateOne: '日期（YYYY-MM-DD）',
      dateTwo: '日期（YYYY/MM/DD）',
      dateThree: '日期（MM-DD）',
      dateFour: '日期（MM/DD）',
      dateTimeOne: '日期时间（YYYY-MM-DD hh:mm:ss）',
      dateTimeTwo: '日期时间（YYYY/MM/DD hh:mm:ss）',
      dateTimeThree: '日期时间（MM-DD hh:mm:ss）',
      dateTimeFour: '日期时间（MM/DD hh:mm:ss）',
      time: '时间（hh:mm:ss）',
      maxLengthTips: '参数的最大长度小于最小长度！',
      tips: '提示',
      reset: '重新设置',
      exchange: '互换',
      insert: '插入',
      resert: '修改'
    },
    justUse: '立即使用',
    tSize: '大小：',
    sSize: '大小',
    effectPreview: '效果预览',
    position: '位置',
    wide: '宽',
    height: '高',
    nameT: '名称：',
    nameTt: '名称',
    pEnter: '请输入',
    pEnterContent: '请输入内容',
    add: '添加',
    operation: '操作',
    reName: '重命名',
    keep: '保存',
    del: '删除',
    complete: '完成',
    isDel: '是否删除当前',
    yes: '是',
    no: '否'
  },
  // 公共
  public: {
    imgUpTips: '上传图片大小不能超过 5MB!',
    mediaUpTips: '上传文件大小不能超过 50MB!',
    tmpKu: '模板库',
    myScene: '我的场景',
    enterprise: '企业定制模板',
    noHaveTmp: '没有找到相符合的场景~~',
    enterId: '请输入场景ID',
    enterName: '请输入场景名称',
    addTmp: '新建',
    theme: '主题：',
    Industry: '行业：',
    pSelect: '请选择',
    IndustryGm: '行业管理',
    use: '用途：',
    useGm: '用途管理',
    pThemeName: '请输入主题名称',
    selectTmp: '选择模板',
    redo: '重做',
    revoke: '撤销',
    audioHint: '您的浏览器不支持 audio 元素。',
    corpHint: '企业',
    corpHolder: '请输入企业名称'
  },
  /**
   * 富媒体编辑器（图文编辑）
   */
  media: {
    richMediaEdit: '图文编辑',
    pEnterText: '请输入文本',
    richMedia: '图文',
    supplementStyle: '补充样式',
    user: '尊敬的用户：',
    noCanUse: '富信编辑器不兼容当前浏览器（IE 9及以下版本)。为了您更好的编辑体验，请从以下浏览器中选择一款访问',
    sougoTipsA: '提示：为了您更好的编辑体验，请在使用',
    sougoTipsB: '搜狗浏览器',
    sougoTipsC: '时切换为',
    sougoTipsD: '极速模式',
    sougo: '搜狗浏览器高速模式',
    tsz: '360浏览器极速模式',
    onlineImgEdit: '在线图片编辑器',
    imgEdit: '图片编辑',
    textEdit: '文字编辑',
    textColor: '文字色',
    bgColor: '背景色',
    width: '宽度',
    rotate: '旋转',
    imgSize: '图片大小：',
    replacePicture: '替换图片',
    cut: '裁剪',
    text: '文字',
    img: '图片',
    cancel: '取消',
    keep: '保存',
    pParems: '请在场景中先插入参数！',
    up: '向上',
    down: '向下',
    del: '删除',
    total: '共',
    strip: '条',
    richMediaEditor: '图文编辑',
    pImg: '配图',
    pUpImg: '请选择图片上传：'
  },
  /**
   * 富文本编辑器
   */
  richText: {
    richTextEdit: '富文本编辑',
    commit: '预览/提交',
    close: '关闭',
    richTextLook: '富文本编辑预览',
    commitExamine: '提交审核',
    back: '返回',
    textLength: '文本长度：',
    richTextEditT: '富文本编辑',
    true: '确定', //
    richMedia: '图文编辑预览',
    scene: '交互编辑预览',
    dbClick: '双击编辑文本',
    changeAudio: '更换音频',
    replacePicture: '更换图片',
    cut: '编辑图片',
    remove: '删除图片',
    replaceVideo: '更换视频',
    cutVideo: '裁剪视频',
    onlineVideo: '在线视频编辑',
    videoSize: '视频大小：',
    videoLength: '视频长度：',
    delNotSlect: '删除未选中片段',
    delSlected: '删除选中片段',
    definition: '清晰度',
    screen: '屏幕切换',
    gao: '高清',
    zhong: '标清',
    vertical: '竖屏',
    horizontal: '宽屏',
    videoHHint: '高清视频建议播放时长不超过20秒',
    videoLHint: '标清视频建议播放时长不超过30秒',
    videoVTHint: '竖屏视频建议播放时长不超过30秒',
    di: '低',
    setRichMedia: '富信主题将发送给用户',
    contentEmpty: '请输入富文本内容',
    contentMaxLength: '文本长度超过980个字符，请重新输入！',
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
    beNumber: '请输入数字',
    beURL: '请输入正确的链接地址'
  }
}
