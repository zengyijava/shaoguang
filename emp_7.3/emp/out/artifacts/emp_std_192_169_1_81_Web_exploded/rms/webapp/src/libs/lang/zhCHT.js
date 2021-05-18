module.exports = {
  /**
   * 列表級預覽
   */
  // 模板操作tips
  moduleTips: {
    preview: '預覽',
    copy: '複製',
    export: '匯出',
    share: '分享',
    del: '删除'
  },
  // 场景列表搜索类型下拉选择文字
  sceneListSearchTypeSelect: {
    description: '富信類型',
    options: {
      all: {label: '全部'},
      scene: {label: '交互'},
      media: {label: '圖文'},
      text: {label: '富文本'},
      h5: {label: '企業秀'}
    }
  },
  // 场景列表查询按钮描述
  sceneListQueryBtnText: '查詢',
  // 场景列表表单输入默认占位文字
  sceneInputPlaceholderText: '請輸入',
  // 场景ID错误校验描述
  sceneIdErrorDescription: '請填寫正確的場景Id',
  // 权限提示信息
  authErrorHintMsg: '請聯系管理員開通對應列表查看權限！',
  // 公共场景列表搜索文字
  commonSceneSearchText: {
    sceneId: '場景ID',
    sceneTitle: '場景主題',
    corpName: '企業名稱'
  },
  // 我的场景列表搜索条件文字
  mySceneSearchText: {
    sceneStatusSelect: {
      description: '場景狀態',
      options: {
        all: {label: '全部'},
        start: {label: '已啟用'},
        forbid: {label: '已禁用'},
        draft: {label: '草稿'}
      }
    },
    templateTypeSelect: {
      description: '場景類型',
      options: {
        all: {label: '全部'},
        static: {label: '通用靜態模板'},
        dynamic: {label: '通用動態模板'}
      }
    },
    auditStatusSelect: {
      description: '審核狀態',
      options: {
        all: {label: '全部'},
        no: {label: '未審批'},
        underway: {label: '審批中'},
        forbid: {label: '已禁用'},
        pass: {label: '審批通過'},
        fail: {label: '審批未通過'}
      }
    },
    createDate: '創建時間',
    startDate: '開始日期',
    endDate: '結束日期'
  },
  // 场景列表行业和用途选择
  industryAndUseText: {
    industryText: '行業',
    allIndustryText: '全部行業',
    useText: '用途',
    allUseText: '全部用途',
    moreText: '更多'
  },
  // 场景列表添加层
  sceneListAdd: {
    addBtnDescription: '創建新場景',
    addPopTitle: '選擇場景類型',
    addSceneTitle: '交互消息',
    addSceneDesc: '支持鏈接跳轉、打開APP等交互操作',
    addMediaTitle: '圖文消息',
    addMediaDesc: '支持文本、圖片、音頻、視頻等多類型信息',
    addTextTitle: '富文本消息',
    addTextDesc: '可以標紅加粗的文本消息',
    addH5Title: '翻頁',
    addH5Desc: '更加貼近用護使用場景的場景式消息'
  },
  // 场景列表下单个模板
  sceneListTemplateLi: {
    setShortcutText: '設為快捷場景',
    cancelShortcutText: '取消快捷場景',
    copyLink: '複製鏈接',
    copyLinkSuccess: '鏈接已複製到剪貼板',
    copyLinkFail: '鏈接複製失敗',
    checkinText: '審批中',
    checkFailText: '審批未通過',
    checkSuccess: '審批通過',
    forbiddenText: '已禁用',
    editText: '編輯',
    editNowText: '立即編輯',
    sendNowText: '立即發送',
    previewTitleText: '效果預覽',
    moreInfoText: {
      titleText: '富信主題',
      sizeText: '容量',
      useCountText: '使用次數',
      frameText: '檔位',
      modelTypeText: '模板類型',
      createUserText: '創建人',
      companyText: '所屬機構',
      companyCodeText: '企業編碼',
      createTimeText: '創建時間',
      tmpStateText: '模板狀態',
      operatorStateText: '運營商狀態'
    }
  },
  // 删除场景提示
  deleHintText: {
    titleText: '提示',
    hintText: '是否刪除當前場景？',
    sureText: '是',
    cancelText: '否'
  },
  // 預覽
  previewText: {
    cardsTitleText: '交互展示方式',
    mediaTitleText: '圖文展示方式',
    richTextTitleText: '富文本展示方式',
    supplementText: '補充展示方式',
    supplementOneText: '補充展示方式壹',
    supplementTwoText: '補充展示方式二',
    companyText: '預覽',
    hintMsgText: '註：根據手機終端的實際支持情況，將會產生以上幾種不同的展示方式，具體以手機接收情況為準。'
  },
  /**
   * 場景編輯器
   */
  // 場景tab
  cardTabs: {
    cardStyle: '場景樣式',
    supplementStyleOne: '補充樣式1',
    supplementStyleTwo: '補充樣式2'
  },
  // 功能設置
  functionSetting: {
    funSet: '功能設置',
    operationType: '操作類型：',
    urlJump: '鏈接跳轉',
    copy: '復制',
    selected: '請選擇需要復制內容：',
    dial: '撥號',
    dialPhone: '請輸入需要撥打的電話：',
    disec: '將調用系統撥號功能撥打以上填寫的號碼，格式如：075512345678或4001234567'
  },
  // 樣式設置
  styleSetting: {
    styleTitle: '場景樣式設置',
    styleOptions: {
      all: '全部',
      standard: '標準',
      business: '商務',
      cartoon: '卡通',
      chineseStyle: '中國風'
    }
  },
  // 按鈕設置
  buttonSetting: {
    buttonName: '名稱',
    selectBut: '請選擇按鈕操作',
    openUrl: '打開鏈接',
    openApp: '打開APP',
    openFastApp: '打開快應用',
    openMap: '打開地圖',
    city: '城市',
    addr: '地址',
    selectAddr: '選擇地理位置',
    urlAddr: '鏈接地址：',
    appAddr: 'APP地址：',
    bagName: '包名',
    route: '路徑',
    params: '參數',
    apkLink: 'Android地址：',
    apkDownloadURL: 'Android下載地址：',
    ipaLink: 'IOS地址：',
    ipaDownloadURL: 'IOS下載地址：',
    selection: '地理位置選擇',
    enterNr: '請輸入內容',
    cancel: '取 消',
    sure: '確 定'
  },
  // 畫布
  canvas: {
    notSupportVideo: '您的瀏覽器不支持Video標簽。',
    notSupportAudio: '您的瀏覽器不支持audio標簽。',
    currentCapacity: '當前容量：',
    filePosition: '檔位：',
    files: '檔',
    clearFirm: '清空',
    trueClear: '確定要將內容區清空嗎？',
    tipsZip: '場景模板提交審核上限為1.9M，圖片、視頻可使用編輯功能進行裁剪壓縮',
    tipsFrame: '最多只能創建15幀！',
    tipsDefaultInfo: '拖動調節氣泡高度',
    trueDeleFrame: '確定要删除當前幀？'
  },
  // 圖片設置
  cardImgSetting: {
    keepProportion: '保持寬高比例',
    masterImg: '原圖',
    compressImg: '壓縮圖'
  },
  // 圖片設置
  navBar: {
    sceneEditing: '交互编辑',
    H5: '企業秀',
    text: '文本',
    img: '圖片',
    audio: '音頻',
    bgAudio: '背景音樂',
    video: '視頻',
    qrCode: '二維碼',
    button: '按鈕',
    bg: '背景',
    add_qrcode_tips: '最多添加一個二維碼！',
    add_audio_tips: '最多添加一個音頻！',
    add_video_tips: '最多添加一個視頻！',
    closeEditorHint: '確定要關閉當前編輯器？'
  },
  // 二維碼設置
  qrCodeSetting: {
    sketchMap: '當前為示意圖，請上傳二維碼圖片',
    fixedCode: '固定二維碼',
    dynamicCode: '動態二維碼',
    currentParams: '當前參數為',
    imgUrl: '，請傳入圖片地址',
    upQrCode: '上傳二維碼圖片',
    explain: '說明：鼠標可拖動組件調整位置、大小 選中後使用鍵盤刪除'
  },
  // 文本設置
  textSetting: {
    setParems: '將組件內容設為參數',
    explain: '說明：選中組件後，可使用鼠標拖動調整位置，也可使用鍵盤Delete鍵進行刪除',
    paramsTip: '說明：文本顯示時將根據參數實際長度進行自適應排版，建議您設置合理的參數長度並預留相應展示區域'
  },
  // 設置面板
  setting: {
    text: '文本框設置',
    button: '按鈕功能設置',
    image: '圖片設置',
    qrcode: '二維碼設置',
    audio: '音頻設置',
    video: '視頻設置',
    chart: '圖表設置'
  },
  // 文本設置
  titleSetting: {
    titleName: '標題名稱',
    enterName: '請輸入標題'
  },
  /**
   * 公共設置模塊
   */
  property: {
    borderRadius: '邊框弧度',
    color: '顏色',
    mediaText: '文本',
    family: '字體',
    glyph: '字形',
    size: '字號',
    text: '文字',
    bg: '背景',
    bgImg: '背景圖片',
    addImg: '疊加圖片',
    opacity: '透明度',
    funSet: '功能設置',
    click: '請設置點擊操作',
    none: '無',
    clickBigImg: '用戶點擊該圖片後可以全屏查看大圖',
    lookBigImg: '查看大圖',
    openLink: '打開鏈接',
    clickOpenLink: '用戶點擊該圖片後打開以下鏈接',
    explain: '說明：選中組件後，可使用鼠標拖動調整位置，也可使用鍵盤Delete鍵進行刪除',
    cardHintMsg: '請填寫交互消息內容',
    mediaHintMsg: '請填寫圖文消息内容',
    completeHintMsgA: '請填寫補充樣式內容',
    completeHintMsgB: '請填寫補充樣式1內容',
    completeHintMsgC: '請填寫補充樣式2內容',
    completeLengthMsgA: '補充樣式內容長度不能超過980個字符！',
    completeLengthMsgB: '補充樣式2內容長度不能超過980個字符！',
    checkDegreeHint: '未檢測到合適的檔位，請聯系管理員！',
    params: {
      argument: '參數',
      name: '參數名稱：',
      type: '參數類型：',
      lengthRestrict: '長度限制：',
      variableLength: '可變長度',
      fixedLength: '固定長度',
      minLength: '最小長度：',
      maxLength: '最大長度：',
      fixedLengthT: '固定長度：',
      insertArgument: '插入參數',
      paramSet: '參數設置',
      resertArgument: '修改參數',
      paramOne: '參數1',
      cNumber: '純數字（d）',
      zNumber: '字母數字（w）',
      money: '金額（￥）',
      string: '任意字符（c）',
      dateOne: '日期（YYYY-MM-DD）',
      dateTwo: '日期（YYYY/MM/DD）',
      dateThree: '日期（MM-DD）',
      dateFour: '日期（MM/DD）',
      dateTimeOne: '日期時間（YYYY-MM-DD hh:mm:ss）',
      dateTimeTwo: '日期時間（YYYY/MM/DD hh:mm:ss）',
      dateTimeThree: '日期時間（MM-DD hh:mm:ss）',
      dateTimeFour: '日期時間（MM/DD hh:mm:ss）',
      time: '時間（hh:mm:ss）',
      maxLengthTips: '參數的最大長度小於最小長度！',
      tips: '提示',
      reset: '重新設置',
      exchange: '互換',
      insert: '插入',
      resert: '修改'
    },
    justUse: '立即使用',
    tSize: '大小：',
    sSize: '大小',
    effectPreview: '效果預覽',
    position: '位置',
    wide: '寬',
    height: '高',
    nameT: '名稱：',
    nameTt: '名稱',
    pEnter: '請輸入',
    pEnterContent: '請輸入內容',
    add: '添加',
    operation: '操作',
    reName: '重命名',
    keep: '保存',
    del: '刪除',
    complete: '完成',
    isDel: '是否刪除當前',
    yes: '是',
    no: '否'
  },
  // 公共
  public: {
    imgUpTips: '上傳圖片大小不能超過 5MB!',
    mediaUpTips: '上傳文件大小不能超過 50MB!',
    tmpKu: '模板庫',
    myScene: '我的場景',
    enterprise: '企業定制模板',
    noHaveTmp: '沒有找到相符合的場景~~',
    enterId: '請輸入場景ID',
    enterName: '請輸入場景名稱',
    addTmp: '新建',
    theme: '主題：',
    Industry: '行業：',
    pSelect: '請選擇',
    IndustryGm: '行業管理',
    use: '用途：',
    useGm: '用途管理',
    pThemeName: '請輸入主題名稱',
    selectTmp: '選擇模板',
    redo: '重做',
    revoke: '撤銷',
    audioHint: '您的浏覽器不支持 audio 元素。',
    corpHint: '企業',
    corpHolder: '請輸入企業名稱'
  },
  /**
   * 富媒體編輯器
   */
  media: {
    richMediaEdit: '富媒體編輯',
    pEnterText: '請在此處輸入文本',
    richMedia: '富媒體',
    supplementStyle: '補充樣式',
    user: '尊敬的用戶：',
    noCanUse: '富信編輯器不兼容當前瀏覽器（IE 9及以下版本)。為了您更好的編輯體驗，請從以下瀏覽器中選擇壹款訪問',
    sougoTipsA: '提示：爲了您更好的編輯體驗，請在使用',
    sougoTipsB: '搜狗浏覽器',
    sougoTipsC: '時切換爲',
    sougoTipsD: '極速模式',
    sougo: '搜狗瀏覽器高速模式',
    tsz: '360瀏覽器极速模式',
    onlineImgEdit: '在線圖片編輯器',
    imgEdit: '圖片編輯',
    textEdit: '文字編輯',
    textColor: '文字色',
    bgColor: '背景色',
    width: '寬度',
    rotate: '旋轉',
    imgSize: '圖片大小：',
    replacePicture: '替換圖片',
    cut: '裁剪',
    text: '文字',
    img: '圖片',
    cancel: '取消',
    keep: '保存',
    pParems: '請在場景中先插入參數！',
    up: '向上',
    down: '向下',
    del: '刪除',
    total: '共',
    strip: '條',
    richMediaEditor: '富媒體編輯',
    pImg: '配圖',
    pUpImg: '請選擇圖片上傳：'
  },
  /**
   * 富文本編輯器
   */
  richText: {
    richTextEdit: '富文本編輯',
    commit: '預覽/提交',
    close: '關閉',
    richTextLook: '富文本編輯預覽',
    commitExamine: '提交審核',
    back: '返回',
    textLength: '文本長度：',
    richTextEditT: '富文本編輯',
    true: '確定',
    richMedia: '圖文編輯預覽',
    scene: '交互編輯預覽',
    dbClick: '雙擊編輯文本',
    changeAudio: '更換音頻',
    replacePicture: '更換圖片',
    cut: '編輯圖片',
    remove: '刪除圖片',
    replaceVideo: '更換視頻',
    cutVideo: '裁剪視頻',
    onlineVideo: '在線視頻編輯',
    videoSize: '視頻大小：',
    videoLength: '視頻長度：',
    delNotSlect: '刪除未選中片段',
    delSlected: '删除選中片段',
    definition: '清晰度',
    screen: '荧幕切換',
    gao: '高清',
    zhong: '標清',
    vertical: '豎屏',
    horizontal: '寬屏',
    videoHHint: '高清視頻建議播放時長不超過20秒',
    videoLHint: '標清視頻建議播放時長不超過30秒',
    videoVTHint: '豎屏視頻建議播放時長不超過30秒',
    di: '低',
    setRichMedia: '富信主題將發送給用戶',
    contentEmpty: '請填寫富內容！',
    contentMaxLength: '文本長度超過980個字符，請重新輸入！',
    cropEmpty: '請輸入企業名並選擇',
    audioName: '音頻名稱',
    recommendedColor: '推薦配色'
  },
  /*
  ** 企业秀
  */
  H5: {
    preview_prev_btn: '上一頁',
    preview_next_btn: '下一頁',
    apply_hint: '應用到所有頁面',
    back_btn: '返回',
    title_hint: '我的H5標題！',
    description_hint: '趕快來描述一下我的H5吧！',
    select_all: '全選',
    dele: '删除',
    completed: '完成',
    batch_operation: '批量操作',
    up_audio_btn: '上傳音頻',
    up_audio_hint: '音頻大小不超過10M，支持格式：mp3、m4a、wma、aac音頻格式',
    up_audio_max_hint: '上傳音頻大小不能超過 10MB!',
    audio_dele_hint: '確定刪除音頻嗎?',
    up_img_btn: '上傳圖片',
    up_img_hint: '圖片大小不超過5M，支持格式：png、jpg、jpeg、gif、bmp',
    up_img_max_hint: '上傳圖片大小不能超過 5MB!',
    img_dele_hint: '確定刪除圖片嗎?',
    hint_title: '提示',
    hint_sure: '確定',
    hint_cancel: '取消',
    audio_text: '音頻',
    name_text: '名稱',
    preview_and_submit: '預覽提交',
    btn_name: '按鈕名稱',
    layer_hidden: '隱藏',
    layer_locked: '鎖定',
    layer_copy: '複制',
    layer_to_top: '置于頂層',
    layer_move_up: '上移一層',
    layer_move_down: '下移一層',
    layer_to_bottom: '置于底層',
    layer_dele: '删除',
    img_material: '圖片素材',
    audio_material: '音頻素材',
    bg_audio_material: '背景音樂素材',
    video_material: '視頻素材',
    open_bg_audio: '開啓背景音樂',
    current_audio: '當前音頻',
    auto_play: '自動播放',
    loop_play: '循環播放',
    show_in_all: '是否顯示在所有頁面',
    layer_admin: '圖層管理',
    pages_admin: '頁面管理',
    page_up: '上移',
    page_copy: '複制（不包含參數）',
    page_down: '下移',
    page_keep_one: '請至少保留一頁',
    preview_title: '標題',
    preview_title_empty: '標題不能爲空',
    preview_desc: '描述',
    preview_desc_empty: '描述不能爲空',
    up_suggest: '建議上傳',
    up_img_size: '200*200尺寸的圖片',
    replace_cover: '更換封面',
    cover_empty: '封面不能爲空',
    page_effect: '翻頁效果',
    up_and_down_effect: '上下翻頁',
    left_and_right_effect: '左右翻頁',
    page_number: '顯示頁碼',
    bottom_right: '底部右側',
    bottom_left: '底部左側',
    bottom_center: '底部居中',
    page_keep_time: '每頁停留時間"秒"',
    cube_effect: '方塊',
    slide_effect: '位移',
    fade_effect: '淡入',
    flip_effect: '3d翻轉',
    text_param: '文本參數',
    text_setting_hint: '文本顯示時將根據參數實際長度進行自適應排版，建議您設置合理的參數長度並預留相應展示區域',
    bg_setting: '背景設置',
    text_setting: '文本框設置',
    button_setting: '按鈕功能設置',
    img_setting: '圖片設置',
    audio_setting: '音頻設置',
    video_setting: '視頻設置',
    bg_audio_setting: '背景音樂設置',
    up_video_btn: '上傳視頻',
    up_video_hint: '視頻大小不超過10M，支持格式：wmv、3gp、avi、f4v、m4v、mp4、mpg、ogv、swf、vob視頻格式',
    up_video_max_hint: '上傳視頻大小不能超過 30MB!',
    video_dele_hint: '確定刪除視頻嗎?'
  },
  validation: {
    beNumber: '請輸入數字',
    beURL: '請輸入正確的鏈接地址'
  }
}
