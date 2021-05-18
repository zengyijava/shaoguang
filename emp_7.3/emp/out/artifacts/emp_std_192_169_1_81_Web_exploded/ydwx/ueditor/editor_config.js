/**
 *  ueditor完整配置项
 *  可以在这里配置整个编辑器的特性
 */
var path = "ydwx/";
var UEDITOR_CONFIG = {
    UEDITOR_HOME_URL: '/ydwx/ueditor/', //这里你可以配置成ueditor目录在您网站的绝对路径
    toolbars: [
        ['Undo','Redo','|',  //'Source','|',
         'Bold','Underline','RemoveFormat','|',
         'ForeColor','BackColor','InsertOrderedList','InsertUnorderedList','|',
         'Horizontal','InsertTable','DeleteTable','MergeCells','SplittoCells','SelectAll','ClearDoc','|',
         'FontFamily','FontSize','|',
         'ImageNone','ImageLeft','ImageRight','ImageCenter','|',
         'InsertInteraction','InsertDownfile','Actividata','InsertImage','InsertVideo','Link']
    ],
    labelMap: {
     //   'anchor':'锚点',
        'undo': getJsLocaleMessage("ydwx","ydwx_wxbj_61"),
        'redo': getJsLocaleMessage("ydwx","ydwx_wxbj_62"),
        'bold': getJsLocaleMessage("ydwx","ydwx_wxbj_63"),
  //      'indent':'首行缩进',
 //       'outdent':'取消缩进',
//        'italic': '斜体',
        'underline': getJsLocaleMessage("ydwx","ydwx_wxbj_64"),
 //       'strikethrough': '删除线',
 //       'subscript': '下标',
 //       'superscript': '上标',
 //       'formatmatch': '格式刷',
 //       'source': '源代码',
 //       'blockquote': '引用',
 //       'pasteplain': '纯文本粘贴模式',
        'selectall': getJsLocaleMessage("ydwx","ydwx_wxbj_65"),
   //     'preview': '预览',
        'horizontal': getJsLocaleMessage("ydwx","ydwx_wxbj_66"),
        'removeformat': getJsLocaleMessage("ydwx","ydwx_wxbj_67"),
    //    'time': '时间',
    //    'date': '日期',
    //    'unlink': '取消链接',
   //     'insertrow': '前插入行',
   //     'insertcol': '前插入列',
   //     'mergeright': '右合并单元格',
  //      'mergedown': '下合并单元格',
  //      'deleterow': '删除行',
  //      'deletecol': '删除列',
  //      'splittorows': '拆分成行',
 //       'splittocols': '拆分成列',
          'splittocells': getJsLocaleMessage("ydwx","ydwx_wxbj_68"),
          'mergecells': getJsLocaleMessage("ydwx","ydwx_wxbj_69"),
          'deletetable': getJsLocaleMessage("ydwx","ydwx_wxbj_70"),
 //       'insertparagraphbeforetable': '表格前插行',
        'cleardoc': getJsLocaleMessage("ydwx","ydwx_wxbj_71"),
        'fontfamily': getJsLocaleMessage("ydwx","ydwx_wxbj_72"),
        'fontsize': getJsLocaleMessage("ydwx","ydwx_wxbj_73"),
  //      'paragraph': '段落格式',
        'insertimage': getJsLocaleMessage("ydwx","ydwx_wxbj_74"),
        'inserttable': getJsLocaleMessage("ydwx","ydwx_wxbj_75"),
        'link': getJsLocaleMessage("ydwx","ydwx_wxbj_76"),
 
   //     'spechars': '特殊字符',
  //      'searchreplace': '查询替换',
 
        'insertvideo': getJsLocaleMessage("ydwx","ydwx_wxbj_77"),
    //    'help': '帮助',
        'justifyleft':getJsLocaleMessage("ydwx","ydwx_wxbj_78"),
        'justifyright':getJsLocaleMessage("ydwx","ydwx_wxbj_79"),
        'justifycenter':getJsLocaleMessage("ydwx","ydwx_wxbj_80"),
        'justifyjustify':getJsLocaleMessage("ydwx","ydwx_wxbj_81"),  
        'forecolor':getJsLocaleMessage("ydwx","ydwx_wxbj_82"), 
        'backcolor':getJsLocaleMessage("ydwx","ydwx_wxbj_83"),
        'insertorderedlist' : getJsLocaleMessage("ydwx","ydwx_wxbj_84"),
        'insertunorderedlist' : getJsLocaleMessage("ydwx","ydwx_wxbj_85"),
    //    'fullscreen' : '全屏',
   //     'directionalityltr' : '从左向右输入',
  //      'directionalityrtl' : '从右向左输入',
   //     'rowspacing' : '行距',
  //      'pagebreak':'分页',
         'imagenone':getJsLocaleMessage("ydwx","ydwx_wxbj_86"),
         'imageleft':getJsLocaleMessage("ydwx","ydwx_wxbj_87"),          
         'imageright':getJsLocaleMessage("ydwx","ydwx_wxbj_88"),
         'imagecenter':getJsLocaleMessage("ydwx","ydwx_wxbj_89"), 
         'insertinteraction':getJsLocaleMessage("ydwx","ydwx_wxbj_90"),
         'insertdownfile':getJsLocaleMessage("ydwx","ydwx_wxbj_91"),
         'actividata':getJsLocaleMessage("ydwx","ydwx_wxbj_92")
    },
    iframeUrlMap: {
     	'insertinteraction': path+'ueditor/dialogs/Interaction/Interaction.jsp',
        'anchor': path+'ueditor/dialogs/anchor/anchor.html',
        'insertdownfile':path+'ueditor/dialogs/downfile/downfile.jsp',
        'insertimage': path+'ueditor/dialogs/image/image.jsp',
        'inserttable': path+'ueditor/dialogs/table/table.jsp',
        'link': path+'ueditor/dialogs/link/link.jsp',
        'spechars': path+'ueditor/dialogs/spechars/spechars.html',
        'searchreplace': path+'ueditor/dialogs/searchreplace/searchreplace.html',
        'map': path+'ueditor/dialogs/map/map.html',
        'gmap': path+'ueditor/dialogs/gmap/gmap.html',
        'insertvideo': path+'ueditor/dialogs/video/video.jsp',
        'help': path+'ueditor/dialogs/help/help.html',
        'highlightcode' : path+'ueditor/dialogs/code/code.html',
        'emotion': path+'ueditor/dialogs/emotion/emotion.html',
         
        'actividata': path+'ueditor/dialogs/actividata/activiData.jsp'
    },
    listMap: {
        'fontfamily': [getJsLocaleMessage("ydwx","ydwx_wxbj_93"), getJsLocaleMessage("ydwx","ydwx_wxbj_94"), getJsLocaleMessage("ydwx","ydwx_wxbj_95"), getJsLocaleMessage("ydwx","ydwx_wxbj_96"),'andale mono','arial','arial black','comic sans ms','impact','times new roman'],
        'fontsize': [10, 11, 12, 14, 16, 18, 20, 24, 36],
        'underline':['none','overline','line-through','underline'],
        'paragraph': ['p:Paragraph', 'h1:Heading 1', 'h2:Heading 2', 'h3:Heading 3', 'h4:Heading 4', 'h5:Heading 5', 'h6:Heading 6'],
        'rowspacing' : ['1.0:0','1.5:15','2.0:20','2.5:25','3.0:30']
    },
    fontMap: {
    	'宋体': [getJsLocaleMessage("ydwx","ydwx_wxbj_93"), 'SimSun'],
    	'楷体': [getJsLocaleMessage("ydwx","ydwx_wxbj_94"), getJsLocaleMessage("ydwx","ydwx_wxbj_97"), 'SimKai'],
    	'黑体': [getJsLocaleMessage("ydwx","ydwx_wxbj_96"), 'SimHei'],
    	'隶书': [getJsLocaleMessage("ydwx","ydwx_wxbj_95"), 'SimLi'],
        'andale mono' : ['andale mono'],
        'arial' : ['arial','helvetica','sans-serif'],
        'arial black' : ['arial black','avant garde'],
        'comic sans ms' : ['comic sans ms'],
        'impact' : ['impact','chicago'],
        'times new roman' : ['times new roman']
    },
    contextMenu: [
        {
            label : getJsLocaleMessage("ydwx","ydwx_common_shanchu"),
            cmdName : 'delete'

        },
        {
            label : getJsLocaleMessage("ydwx","ydwx_wxbj_98"),
            cmdName : 'selectall'

        },{
            label : getJsLocaleMessage("ydwx","ydwx_wxbj_99"),
            cmdName : 'highlightcode',
            icon : 'deletehighlightcode'

        },{
             label : getJsLocaleMessage("ydwx","ydwx_wxbj_100"),
             cmdName : 'cleardoc',
            exec : function(){
                
                if(confirm(getJsLocaleMessage("ydwx","ydwx_wxbj_101"))){

                    this.execCommand('cleardoc');
                }
            }
        },'-',{
             label : getJsLocaleMessage("ydwx","ydwx_wxbj_102"),
             cmdName : 'unlink'
        },'-',{
            group : getJsLocaleMessage("ydwx","ydwx_wxbj_103"),
            icon : 'justifyjustify',
           
            subMenu : [
                {
                    label: getJsLocaleMessage("ydwx","ydwx_wxbj_78"),
                    cmdName : 'justify',
                    value : 'left'
                },
               {
                    label: getJsLocaleMessage("ydwx","ydwx_wxbj_79"),
                    cmdName : 'justify',
                    value : 'right'
                },{
                    label: getJsLocaleMessage("ydwx","ydwx_wxbj_80"),
                    cmdName : 'justify',
                    value : 'center'
                },{
                    label: getJsLocaleMessage("ydwx","ydwx_wxbj_81"),
                    cmdName : 'justify',
                    value : 'justify'
                }
            ]
        },'-',{
                label:getJsLocaleMessage("ydwx","ydwx_wxbj_104"),
                cmdName:'edittable',
                exec : function(){
                    this.tableDialog.open();
                }
            },{
            group : getJsLocaleMessage("ydwx","ydwx_wxbj_105"),
            icon : 'table',

            subMenu : [
                {
                    label: getJsLocaleMessage("ydwx","ydwx_wxbj_106"),
                    cmdName : 'deletetable'
                },
                {
                    label: getJsLocaleMessage("ydwx","ydwx_wxbj_107"),
                    cmdName : 'insertparagraphbeforetable'
                },
                '-',
                {
                    label: getJsLocaleMessage("ydwx","ydwx_wxbj_108"),
                    cmdName : 'deleterow'
                },
                {
                    label: getJsLocaleMessage("ydwx","ydwx_wxbj_109"),
                    cmdName : 'deletecol'
                },
                '-',
                 {
                    label: getJsLocaleMessage("ydwx","ydwx_wxbj_110"),
                    cmdName : 'insertrow'
                },
                {
                    label: getJsLocaleMessage("ydwx","ydwx_wxbj_111"),
                    cmdName : 'insertcol'
                },
                '-',
                 {
                    label: getJsLocaleMessage("ydwx","ydwx_wxbj_112"),
                    cmdName : 'mergeright'
                },
                {
                    label: getJsLocaleMessage("ydwx","ydwx_wxbj_113"),
                    cmdName : 'mergedown'
                },
                '-',
                 {
                    label: getJsLocaleMessage("ydwx","ydwx_wxbj_114"),
                    cmdName : 'splittorows'
                },
                {
                    label: getJsLocaleMessage("ydwx","ydwx_wxbj_115"),
                    cmdName : 'splittocols'
                },
                 {
                    label: getJsLocaleMessage("ydwx","ydwx_wxbj_116"),
                    cmdName : 'mergecells'
                },
                {
                    label: getJsLocaleMessage("ydwx","ydwx_wxbj_117"),
                    cmdName : 'splittocells'
                }
            ]
        }
    ],
    initialStyle:                                       //编辑器内部样式
        //选中的td上的样式
        '.selectTdClass{background-color:#3399FF !important}'+
        //插入代码的外框样式
        'pre{background-color:#F8F8F8;border:1px solid #CCCCCC;padding:10px 10px;margin:5px;word-wrap:normal;}'+
         //插入的表格的默认样式
        'table{margin-bottom:10px;border-collapse:collapse;}td{vertical-align:top;padding:2px;height:20px;}'+
        //分页符的样式
        '.pagebreak{border-bottom:1px dotted #999999 !important;border-top:1px dotted #999999 !important;clear:both !important;cursor:default !important;height: 5px !important;padding: 0 !important;width: 100% !important;margin-bottom:10px;height:5px !important;overflow: hidden;}'+
        //锚点的样式,注意这里背景图的路径
        '.anchorclass{background: url("../themes/default/images/anchor.gif") no-repeat scroll left center transparent;border: 1px dotted #0000FF;cursor: auto;display: inline-block;height: 16px;width: 15px;}' +
        //设置四周的留边
        '.view{padding:0;word-wrap:break-word;word-break:break-all;cursor:text;height:100%;}\n' +
        'body{margin:8px;font-family:"'+getJsLocaleMessage("ydwx","ydwx_wxbj_93")+'";font-size:16px;}' +

        //设置段落间距
        'p{margin:.5em 0}'+
        //清除表格浮动状态
        '.tableclear{clear:both;margin:0;padding:0;}'
    ,
    initialContent: '<span></span>',  //初始化编辑器的内容
    autoClearinitialContent:false,                       //是否自动清除编辑器初始内容
    iframeCssUrl :'../themes/default/iframe.css',        //要引入css的url
    removeFormatTags : 'b,big,code,del,dfn,em,font,i,ins,kbd,q,samp,small,span,strike,strong,sub,sup,tt,u,var',    //清除格式删除的标签
    removeFormatAttributes : 'class,style,lang,width,height,align,hspace,valign',        //清除格式删除的属性
    enterTag : 'p',                                      //编辑器回车标签。p或br
    maxUndoCount : 20,                                   //最多可以回退的次数
    maxInputCount : 20,                                  //当输入的字符数超过该值时，保存一次现场
    selectedTdClass : 'selectTdClass',                   //设定选中td的样式名称
    pasteplain : 0,                                      //是否纯文本粘贴。false为不使用纯文本粘贴，true为使用纯文本粘贴
    textarea : 'editorValue',                            //提交表单时，服务器获取编辑器提交内容的所用的参数，多实例时请给每个实例赋予不同的名字
    focus : false,                                       //初始化时，是否让编辑器获得焦点true或false
    indentValue : '2em',                                 //初始化时，首行缩进距离
    pageBreakTag : '_baidu_page_break_tag_',             //分页符
    minFrameHeight: 345,                                 //最小高度
    autoHeightEnabled:true,                             //是否自动长高
    autoFloatEnabled: true,                              //是否保持toolbar的位置不动
    elementPathEnabled : false,                           //是否启用elementPath
    wordCount:false,                                      //是否开启字数统计
    maximumWords:20000,                                  //允许的最大字符数
    tabSize : 4,                                           //tab的宽度
    tabNode : '&nbsp;',                                      //tab时的单一字符
    emotionPath:"../dialogs/emotion/",
    messages:{
        pasteMsg:getJsLocaleMessage("ydwx","ydwx_wxbj_118"),//粘贴提示
        wordCountMsg:getJsLocaleMessage("ydwx","ydwx_wxbj_119")+'{#count}'+getJsLocaleMessage("ydwx","ydwx_wxbj_120")+'{#leave}'+getJsLocaleMessage("ydwx","ydwx_wxbj_121"),   //字数统计提示，{#count}代表当前字数，{#leave}代表还可以输入多少字符数。
        wordOverFlowMsg:getJsLocaleMessage("ydwx","ydwx_wxbj_122") //超出闲置
    },
    serialize : function(){                              //配置过滤标签

        return {
            blackList: {}
        };
    }(),
    ComboxInitial: {
        FONT_FAMILY: getJsLocaleMessage("ydwx","ydwx_wxbj_123"),
        FONT_SIZE: getJsLocaleMessage("ydwx","ydwx_wxbj_124"),
        ROW_SPACING: getJsLocaleMessage("ydwx","ydwx_wxbj_125"),
        PARAGRAPH: getJsLocaleMessage("ydwx","ydwx_wxbj_126")
    },
    //源码的查看方式，codermirror 是代码高亮，textarea是文本框
    sourceEditor: 'codemirror',
    codemirrorOptions : {
        mode: "text/html",
        tabMode: "indent",
        lineNumbers: true
    }
};