/**
 * 英文语言包
 */
UM.I18N['zh_HK'] = {
    'labelMap':{
        'anchor':'anchor', 'undo':'anchor', 'redo':'redo', 'bold':'bold', 'indent':'indent', 'snapscreen':'snapscreen',
        'italic':'italic', 'underline':'underline', 'strikethrough':'strikethrough', 'subscript':'subscript','fontborder':'fontborder',
        'superscript':'superscript', 'formatmatch':'formatmatch', 'source':'source', 'blockquote':'blockquote',
        'pasteplain':'pasteplain', 'selectall':'selectall', 'print':'print', 'preview':'preview',
        'horizontal':'horizontal', 'removeformat':'removeformat', 'time':'time', 'date':'date',
        'unlink':'unlink', 'insertrow':'insertrow', 'insertcol':'insertcol', 'mergeright':'mergeright', 'mergedown':'mergedown',
        'deleterow':'deleterow', 'deletecol':'deletecol', 'splittorows':'splittorows', 'splittocols':'splittocols', 'splittocells':'splittocells',
        'mergecells':'mergecells', 'deletetable':'deletetable', 'cleardoc':'cleardoc','insertparagraphbeforetable':"insertParagraphBeforeTable",'insertcode':'insertcode','fontfamily':'fontfamily', 'fontsize':'fontsize', 'paragraph':'paragraph', 'image':'image',
        'edittable': 'table attribute',
        'edittd': 'cell properties',
        'link': 'hyperlink',
        'emotion': 'expression',
          'spechars': 'special characters',
          'searchreplace': 'query replacement', 'map':'baidu map', 'gmap':'Google Map',
        'video':'video', 'help':'help', 'justifyleft':'justifyleft', 'justifyright':'justifyright', 'justifycenter':'justifycenter',
        'justifyjustify':'justifyends', 'forecolor':'forecolor', 'backcolor':'bgcolor', 'insertorderedlist':'orderedlist',
        'insertunorderedlist':'unorderedlist', 'fullscreen':'fullscreen', 'directionalityltr':'directionalityltr', 'directionalityrtl':'directionalityrtl',
        'rowspacingtop': 'section before the distance', 'rowspacingbottom': 'segment after', 'highlightcode': 'insert code', 'pagebreak': 'paging', 'insertframe': 'insert iframe', 'imagenone':'default',
        'imageleft': 'left floating', 'imageright': 'right floating', 'attachment':'attachment', 'imagecenter':'imagecenter', 'wordimage':'Imagedump',
        'lineheight':'lineheight','edittip' :'edittip','customstyle':'customstyle', 'autotypeset':'autotypeset', 'webapp':'baiduapp',
        'touppercase':'touppercase', 'tolowercase':'tolowercase','background':'background','template':'template','scrawl':'scrawl','music':'music','inserttable':'inserttable',
        'drafts': 'drafts', 'formula':'formula'


    },
    'paragraph':{'p':'p', 'h1':'h1', 'h2':'h2', 'h3':'h3', 'h4':'h4', 'h5':'h5', 'h6':'h6'},
    'fontfamily':{
        'songti':'宋体',
        'kaiti':'楷体',
        'heiti':'黑体',
        'lishu':'隶书',
        'yahei':'微软雅黑',
        'andaleMono':'andale mono',
        'arial': 'arial',
        'arialBlack':'arial black',
        'comicSansMs':'comic sans ms',
        'impact':'impact',
        'timesNewRoman':'times new roman'
    },
    'ok':"ok",
    'cancel':"cancel",
    'closeDialog':"closeDialog",
    'tableDrag': "table drag must be introduced uiUtils.js file!",
     'autofloatMsg': "Toolbar floating dependency editor UI, you first need to introduce UI file!",
     'anthorMsg': "link",
     'clearColor': 'clear color',
     'standardColor': 'standard color',
     'themeColor': 'theme color',
     'property': 'attribute',
     'default': 'default',
     'modify': 'modify',
     'justifyleft': 'left just',
     'justifyright': 'right justified',
     'justifycenter': 'center',
     'justify': 'default',
     'clear': 'clear',
     'anchorMsg': 'anchor',
     'delete': 'delete',
     'clickToUpload': "click to upload",
     'unset': 'not yet set language file',
     't_row': 'line',
     't_col': 'column',
     'more': 'more',
     'pasteOpt': 'paste option',
     'pasteSourceFormat': "Keep the source format",
     'tagFormat': 'keep only the label',
     'pasteTextFormat': 'keep only text',

    //===============dialog i18N=======================
    'image':{
    	'static': {
             'lang_tab_local': "local upload",
             'lang_tab_imgSearch': "network picture",
             'lang_input_dragTip': "support picture drag and drop upload",
             'lang_btn_add': "add",
         },
         'uploadError': 'upload error'
    },
    'emotion':{
        'static':{
    	'lang_input_choice': 'Featured',
             'lang_input_Tuzki': 'Tuzki',
             'lang_input_BOBO': 'BOBO',
             'lang_input_lvdouwa': 'lvdouwa',
             'lang_input_babyCat': 'babyCat',
             'lang_input_bubble': 'bubble',
             'lang_input_youa': 'youa'
        }
    },
    'gmap':{
    	'static': {
             'lang_input_address': 'address',
             'lang_input_search': 'search',
             'address': {'value': "Beijing"}
         },
         'searchError': 'Can not target this address!'
    },
    'link':{
    	'static': {
             'lang_input_text': 'Text content:',
             'lang_input_url': 'link address:',
             'lang_input_title': 'Title:',
             'lang_input_target': 'is opened in a new window:'
         },
         'validLink': 'only support the selection of a link to take effect',
         'httpPrompt': 'The hyperlink you entered does not contain the protocol name such as http, which will add http: // prefix for you by default'
    },
    'map':{
    	'static': {
             'lang_city': "city",
             'lang_address': "address",
             'city': {'value': "Beijing"},
             'lang_search': "search",
             'lang_dynamicmap': "insert dynamic map"
         },
         'cityMsg': "Please select city",
         'errorMsg': "sorry, can not find the location!"
    },
    'video':{
    	'static': {
            'lang_tab_insertV': "insert video",
            'lang_video_url': "video URL",
            'lang_video_size': "video size",
            'lang_videoW': "width",
            'lang_videoH': "height",
            'lang_alignment': "Alignment",
            'videoSearchTxt': {'value': "Please enter the search keyword!"},
            'videoType':{'options':["all", "hot", "entertain", "funny", "sports", "technology", "variety"]},
            'videoSearchBtn': {'value': "Baidu about"},
            'videoSearchReset': {'value': "clear the results"}
        },
        'numError': "Please enter the correct values, such as 123,400",
        'floatLeft': "left floating",
        'floatRight': "right float",
        'default': "default",
        'block': "exclusive line",
        'urlError': "Enter the video address is wrong, please check and try again!",
        'loading': "& nbsp; video loading, please wait ...",
        'clickToSelect': "click to select",
        'goToSource': 'access source video',
        'noVideo': "& nbsp; & nbsp; sorry, can not find the corresponding video, please try again!"
    },
    'formula':{
        'static':{
    		'lang_tab_common': 'common formula',
            'lang_tab_symbol':'symbol',
            'lang_tab_letter':'letter'
        }
    }
};