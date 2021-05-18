(function(win, $){
	var LANGUAGE,
		TEMPLATE,
		IMGEDITORTEMPLATE,
		languageConfig = {
			"zh_CN": {
				"previewHint": "手机预览效仅供参考，具体以手机显示为准。",
				"flashHint": "您的浏览器未安装Flash,或者Flash版本低于15！",
				"textMax": "当前帧已存在文本，请直接在文本后编辑或换行编辑！",
				"mainTools": {
					"text": "文本",
					"img": "图片",
					"chart": "图表",
					"audio": "音频",
					"video": "视频",
					"module": "引入场景"
				},
				"keyTools": {
					"params": "参数",
					"text": "配文",
					"img": "配图",
					"chart": "图表",
					"edit": "编辑"
				},
				"keyEditHint": {
					"top": "已到达最顶部",
					"bottom": "已位于最底部",
					"max": "最多可新增15页",
					"min": "只可配一张图片或一张图表"
				},
				"audioHint": {
					"compatible": "您的浏览器不支持音频播放，请升级到ie9以上或使用其他最新版浏览器"
				},
				"videoHint": {
					"compatible": "您的浏览器不支持视频播放，请升级到ie9以上或使用其他最新版浏览器",
					"cutSuccess": "视频裁剪成功！",
					"cutFail": "视频裁剪失败！"
				},
				"imgHint": {
					"cutArea": "请选择裁剪范围！",
					"cutSuccess": "图片裁剪成功！",
					"cutFail": "图片裁剪失败！",
					"max": "图片文件过大！"
				},
				"paramsText": {
					"desc": "参数",
					"add": "新增参数",
					"inset": "插入参数",
					"hint": "请先配文再选择参数",
					"confirm": "确定",
					"cancel": "取消"
				},
				"fileUpHint": {
					"fail": "文件上传失败！",
					"type": "文件类型错误！",
					"max": "文件过大！"
				},
				"audioEdit": {
					"title": "在线视频编辑",
					"size": "视频大小",
					"time": "视频时长",
					"delSelect": "删除选中片段",
					"delNSelect": "删除未选中片段",
					"definition": "清晰度",
					"preview": "预览",
					"high": "高",
					"normal": "中",
					"low": "低",
					"confirm": "确定"
				},
				"imgEdit": {
					"title": "在线图片编辑器",
					"confirm": "保存",
					"cancel": "取消",
					"text": "文字",
					"imgArea":{
						"title": "图片编辑",
						"clip": "裁剪",
						"confirm": "确定",
						"cancel": "取消",
						"zoomA": "放大",
						"zoomB": "缩小",
						"leftRot": "向左旋转",
						"rightRot": "向右旋转",
						"reset": "重置",
						"diy": "自定义比例",
						"replace": "替换图片",
						"size": "图片大小"
					},
					"textArea": {
						"title": "文字",
						"placeholder": "请双击输入文字",
						"ffm": "字体",
						"color": "文字色",
						"bgColor": "背景色",
						"w": "宽度",
						"rotate": "旋转",
						"insetParams": "插入参数",
						"addParams": "参数",
						"imgParams": "图参",
						"inset": "插入",
						"insetPH": "请选择您要插入参数位置！"
					}
				}
			},
			"zh_TW": {
				"previewHint": "手機預覽效僅供參考，具體以手機顯示為準。",
				"flashHint": "您的瀏覽器未安裝Flash,或者Flash版本低於15！",
				"textMax": "當前幀已存在文本，請直接在文本後編輯或換行編輯！",
				"mainTools": {
					"text": "文本",
					"img": "圖片",
					"chart": "圖表",
					"audio": "音頻",
					"video": "視頻",
					"module": "引入場景"
				},
				"keyTools": {
					"params": "參數",
					"text": "配文",
					"img": "配圖",
					"chart": "圖表",
					"edit": "編輯"
				},
				"keyEditHint": {
					"top": "已到達最頂部",
					"bottom": "已位於最底部",
					"max": "最多可新增15頁",
					"min": "只可配一張圖片或一張圖表"
				},
				"audioHint": {
					"compatible": "您的瀏覽器不支持音頻播放，請升級到ie9以上或使用其他最新版瀏覽器"
				},
				"videoHint": {
					"compatible": "您的瀏覽器不支持視頻播放，請升級到ie9以上或使用其他最新版瀏覽器",
					"cutSuccess": "視頻裁剪成功！",
					"cutFail": "視頻裁剪失敗！"
				},
				"imgHint": {
					"cutArea": "請選擇裁剪範圍！",
					"cutSuccess": "圖片裁剪成功！",
					"cutFail": "圖片裁剪失敗！",
					"max": "圖片文件過大！"
				},
				"paramsText": {
					"desc": "參數",
					"add": "新增參數",
					"inset": "插入參數",
					"hint": "請先配文再選擇參數",
					"confirm": "確定",
					"cancel": "取消"
				},
				"fileUpHint": {
					"fail": "文件上傳失敗！",
					"type": "文件類型錯誤！",
					"max": "文件過大！"
				},
				"audioEdit": {
					"title": "在線視頻編輯",
					"size": "視頻大小",
					"time": "視頻時長",
					"delSelect": "刪除選中片段",
					"delNSelect": "刪除未選中片段",
					"definition": "清晰度",
					"preview": "預覽",
					"high": "高",
					"normal": "中",
					"low": "低",
					"confirm": "確定"
				},
				"imgEdit": {
					"title": "在線圖片編輯器",
					"confirm": "保存",
					"cancel": "取消",
					"text": "文字",
					"imgArea":{
						"title": "圖片編輯",
						"clip": "裁剪",
						"confirm": "確定",
						"cancel": "取消",
						"zoomA": "放大",
						"zoomB": "縮小",
						"leftRot": "向左旋轉",
						"rightRot": "向右旋轉",
						"reset": "重置",
						"diy": "自定義比例",
						"replace": "替換圖片",
						"size": "圖片大小"
					},
					"textArea": {
						"title": "文字",
						"placeholder": "請雙擊輸入文字",
						"ffm": "字體",
						"color": "文字色",
						"bgColor": "背景色",
						"w": "寬度",
						"rotate": "旋轉",
						"insetParams": "插入參數",
						"addParams": "參數",
						"imgParams": "圖參",
						"inset": "插入",
						"insetPH": "請選擇您要插入參數位置！"
					}
				}
			},
			"zh_HK": {
				"previewHint": "Preview effect is for reference only, the specific mobile phone display shall prevail",
				"flashHint": "Your browser does not have Flash installed, or Flash version under 15!",
				"textMax": "The current frame already exists the text, please edit directly after the text or line - feed editing!",
				"mainTools": {
					"text": "text",
					"img": "image",
					"chart": "chart",
					"audio": "audio",
					"video": "video",
					"module": "scene"
				},
				"keyTools": {
					"params": "parameter",
					"text": "caption",
					"img": "photo",
					"chart": "chart",
					"edit": "edit"
				},
				"keyEditHint": {
					"top": "It's at the top",
					"bottom": "It's at the bottom",
					"max": "Up to 15 pages can be added",
					"min": "one picture or one chart only"
				},
				"audioHint": {
					"compatible": "Your browser does not support audio playback, please upgrade to ie9(or above) or use other latest browsers"
				},
				"videoHint": {
					"compatible": "Your browser does not support video playback, please upgrade to ie9(or above) or use other latest browsers",
					"cutSuccess": "Video was clipped successfully!",
					"cutFail": "Video clipping failed!"
				},
				"imgHint": {
					"cutArea": "Please select the clipping range!",
					"cutSuccess": "Image was clipped successfully!",
					"cutFail": "Image clipping failed!",
					"max": "Image file is too large!"
				},
				"paramsText": {
					"desc": "parm",
					"add": "add parameter",
					"inset": "insert parameter",
					"hint": "Please enter the text before selecting the parameters",
					"confirm": "OK",
					"cancel": "Cancel"
				},
				"fileUpHint": {
					"fail": "File upload failed!",
					"type": "File type error!",
					"max": "file is too large!"
				},
				"audioEdit": {
					"title": "online photo editting",
					"size": "size of video",
					"time": "duration",
					"delSelect": "Delete selected",
					"delNSelect": "Delete unselected",
					"definition": "Resolution",
					"preview": "preview",
					"high": "high",
					"normal": "medium",
					"low": "low",
					"confirm": "OK"
				},
				"imgEdit": {
					"title": "online photo editor",
					"confirm": "save",
					"cancel": "cancel",
					"text": "text",
					"imgArea":{
						"title": "edit image",
						"clip": "clip",
						"confirm": "OK",
						"cancel": "cancel",
						"zoomA": "zoom in",
						"zoomB": "zoom out",
						"leftRot": "rotate left",
						"rightRot": "rotate right",
						"reset": "reset",
						"diy": "Custom scaling",
						"replace": "replace image",
						"size": "size of image"
					},
					"textArea": {
						"title": "text",
						"placeholder": "Please double-click to input text",
						"ffm": "font",
						"color": "color",
						"bgColor": "background",
						"w": "width",
						"rotate": "rotate",
						"insetParams": "insert parameter",
						"addParams": "parameter",
						"imgParams": "parm",
						"inset": "insert",
						"insetPH": "Please select the location where you want to insert the parameter！"
					}
				}
			}
		};
	
    // 配置项
    win.FXEDITOR_CONFIG = {
    	HOME_URL: "http://192.169.1.196:8080/p_ydcx/",
        editTools: ["text", "image", "chart", "audio", "video", "module"],    // 工具栏显示工具类
        // 图片配置
        imageConfig: {
            size: 5242880,      // 图片默认大小为5M转换为字节
            format: ["png", "jpg", "jpeg", "gif", "bmp"], // 图片格式
            uploadUrl: "rms/ueditor/jsp/controller.jsp?action=uploadimage&encode=utf-8",      // 图片上传地址
            tailorUrl: "fx_imgCropperUploder"       // 图片裁剪上传地址
        },
        // 音频配置
        audioConfig:{
            size: 10485760,      // 音频默认大小为10M转换为字节
            format: ["aac","m4a","wma","mp3"], // 音频格式
            uploadUrl: "rms/ueditor/jsp/controller.jsp?action=uploadvideo&encode=utf-8",      // 音频上传地址
            tailorUrl: ""       // 音频裁剪上传地址
        },
        // 视频配置
        videoConfig: {
            size: 10485760,      // 视频默认大小为10M转换为字节
            format: ["wmv","3gp","avi","f4v","flv","m4v","mkv","mov","mp4","mpg","ogv","swf","vob"], // 视频格式
            uploadUrl: "rms/ueditor/jsp/controller.jsp?action=uploadvideo&encode=utf-8",      // 视频上传地址
            tailorUrl: "fx_videoCropperUploader"       // 视频裁剪上传地址
        }
    };
    
    win.SetLanguage = function(type){
    	LANGUAGE = languageConfig[type];
    	// 模板结构
        TEMPLATE = {
        	// 编辑器加载层
        	editorLoadLayer: '<div id="fxeditor-laod" class="fxeditor-load"><p class="load-hint"></p></div>',
            // 整体编辑器容器
            containerTemplate: '<div class="fxeditor-page"><div class="editor-container J-editor-container"></div><div class="editor-preview J-editor-preview"></div></div>',
            // 编辑器工具栏
            toolsContainerTemplate : '<div class="editor-tools"></div>',
            // 编辑器工具模板
            toolsTemplate : '<div class="editor-tools-li J-tools-li"><i class="editor-tools-icons J-tools-icon"></i><span class="J-tool-name"></span></div>',
            // 编辑器内容容器
            editorContentTemplate: '<div class="editor-content J-editor-content"></div>',
            // 关键帧容器
            keyframeContentTemplate : '<div class="editor-keyframe J-keyframe"><div class="keyframe-content J-keyframe-content"></div></div>',
            // 关键帧内文字模板
            editTextTemplate : '<div class="editor-text J-edit-text"></div>',
            // 关键帧内图片模板
            editImgTemplate : '<div class="editor-img J-editor-img"></div>',
            // 关键帧内视频模板
            editVideoTemplate : '<video class="J-video" src="" controls="controls" preload>'+LANGUAGE.videoHint.compatible+'</video>',
            // 关键帧内音频模板
            editAudioTemplate : '<audio class="J-audio" src="" controls="controls" preload>'+LANGUAGE.audioHint.compatible+'</audio>',
            // 关键帧内图片编辑模板
            imgEditTemplate : '<div class="img-edit-layer J-img-edit-layer"><div class="edit-bg"></div><div class="eidt-content">'
                                +'<div class="btn edit-btn J-img-edit"></div><div class="btn dele-btn J-img-dele"></div></div>',
            // 关键帧编辑容器
            keyFrameEditContentTemplate: '<div class="keyframe-edit-content J-keyframe-edit-content"></div>',
            // 关键帧ICON按钮
            keyFrameEditIconTemplate: '<div class="edit-btn J-edit-keyframe-btn"><p class="edit-icon J-edit-icon"></p></div>',
            // 关键帧带文字按钮
            keyFrameEditTextTemplate: '<div class="edit-btn J-edit-keyframe-btn"></div>',
            // 预览容器
            previewContentTemplate: '<div class="preview-container">'
                                +'<div class="preview-scroll-box"><div class="preview-scroll J-preview-content"></div></div></div>'
                                +'<p class="preview-hint">'+LANGUAGE.previewHint+'</p>',
           // 错误提示信息层
           errorHintLayerTemplate: '<div class="editor-error-hint J-editor-error"><p></p></div>',
           // 插入参数弹层模板
           editParamsPopLayerTemplate: '<div class="editor-pop-layer J-editor-pop"><div class="pop-layer-bg"></div><div class="pop-layer-panel">'
        	   					+'<div class="panel-header"><h2 class="title">'+LANGUAGE.paramsText.inset+'</h2></div><div class="panel-content">'
        	   					+'<div class="content"><ul class="check-box J-check-box"></ul>'
        	   					+'<input class="blue-btn J-add-params" type="button" value="'+LANGUAGE.paramsText.add+'"></div>'
        	   					+'<div class="btn-area"><input class="pop-btn J-editor-params-confirm" type="button" value="'+LANGUAGE.paramsText.confirm+'">'
        	   					+'<input class="pop-btn J-editor-params-cancel" type="button" value="'+LANGUAGE.paramsText.cancel+'">'
        	   					+'</div></div></div></div>',
        	// 视频裁剪器模板
        	videoCutTemplate: '<div class="editor-video-bg J-video-cut-bg"></div><div id="editor-video" class="editor-video"><div class="editor-video-hd">'+LANGUAGE.audioEdit.title+'<div class="video-close J-video-close">×</div></div>'
        					+'<div class="editor-video-content"><div class="video-content J-video-content"><video id="my-video" class="video-js vjs-big-play-centered" controls preload="auto" data-setup="{}"><source class="J-video-cut-resource" src="" type=""></video>'
        					+'</div></div><div class="editor-video-ft"><div class="editor-info">'
        					+'<p class="info-desc"><span class="J-video-size"></span></p><p class="info-desc"><span class="J-video-time"></span></p></div>'
        					+'<div class="editor-range"><input type="text" id="video-range" value="" /></div><div class="video-edit-tools"><div class="tools-left"><div class="tools-btn dele-select-no J-vedio-cut-btn" data-type="1">'
        					+'<p>'+LANGUAGE.audioEdit.delNSelect+'</p></div><div class="tools-btn dele-select J-vedio-cut-btn" data-type="0"><p>'+LANGUAGE.audioEdit.delSelect+'</p></div><div class="tools-btn preview-btn J-vedio-preivew-btn"><i class="preview-icon"></i>'
        					+'<p>'+LANGUAGE.audioEdit.preview+'</p></div></div><div class="tools-right"><div class="tools-btn ratio-select J-vedio-ratio-btn"><div class="ratio-content"><p class="desc">'+LANGUAGE.audioEdit.definition+'</p>'
        					+'<div class="ratio-list"><p class="list-show J-vedio-ratio-show" data-val="300">'+LANGUAGE.audioEdit.low+'</p><ul class="list J-vedio-ratio-list"><li class="list-li J-vedio-ratio-li" data-val="300">'+LANGUAGE.audioEdit.low+'</li><li class="list-li J-vedio-ratio-li" data-val="500">'+LANGUAGE.audioEdit.normal+'</li>'
        					+'<li class="list-li J-vedio-ratio-li" data-val="1000">'+LANGUAGE.audioEdit.high+'</li></ul></div></div></div><div class="tools-btn video-confirm J-vedio-confirm"><p>'+LANGUAGE.audioEdit.confirm+'</p></div></div></div></div></div>'
        };
        
        // 图片编辑器DOM结构模板文件，谨慎修改
        IMGEDITORTEMPLATE = {
            // 图片编辑器背景
            imgEditorBgTPL: '<div id="img-editor-bg" class="img-editor-bg"></div>',
            // 图片编辑器
            imgEditorContTPL: '<div id="img-editor-layer" class="img-editor-layer"></div>',
            // 图片编辑器头
            imgEditorContHdTPL: '<div class="img-editor-hd">' +
                            '<div class="btn-box editor-clear">'+
                            '<button class="editor-btn gray-btn editor-fl J-imgeditor-cancel" type="button">'+LANGUAGE.imgEdit.cancel+'</button>'+
                            '<button class="editor-btn green-btn editor-fl J-imgeditor-save" type="button">'+LANGUAGE.imgEdit.confirm+'</button></div>'+
                            '<h2 class="title">'+LANGUAGE.imgEdit.title+'</h2>'+
                            '<div class="close-btn J-imgeditor-cancel"><i class="close-icon"></i></div></div>',
            // 图片编辑器容器
            imgEditorContainerTPL: '<div class="img-editor-body editor-clear">'+
                            '<div class="editor-show-bd editor-fl">'+
                            '<div class="img-show-cont editor-fl">'+
                            '<div class="show-box">'+
                            '<div class="edit-container J-main-container"></div>'+
                            '<ul class="container-tools">'+
                            '<li class="container-tools-btn bg-btn J-bg-btn"></li></ul></div></div>'+
                            '<div class="img-edit-cont editor-fl J-clip-tools"></div></div>'+
                            '<ul class="editor-tools-bar editor-fr">'+
                            '<li class="tools-bar-li J-main-tool" data-type="text">'+
                            '<i class="tools-icons text-icon"></i>'+
                            '<span class="tools-desc">'+LANGUAGE.imgEdit.text+'</span></li></ul></div>',
            // 文字编辑工具容器
            imgEditorTextToolsTPL: '<div class="text-edit-cont editor-hidden J-text-tools"><h4 class="title">'+LANGUAGE.imgEdit.textArea.title+'</h4>'+
                        '<div class="edit-box"><ul class="font-tools"><li class="tools-li editor-clear">'+
                        '<p class="desc editor-fl">'+LANGUAGE.imgEdit.textArea.ffm+'</p><div class="select-cont editor-fl J-pull-select" data-type="fontFamily">'+
                        '<p class="show-select-result J-select-val"></p><i class="pull-icon"></i>'+
                        '<ol class="select-list J-select-list J-font-family"></ol></div></li>'+
                        '<li class="tools-li editor-clear"><p class="desc editor-fl">'+
                        '<i class="desc-icon size-icon"></i></p><div class="select-cont editor-fl J-pull-select" data-type="fontSize">'+
                        '<p class="show-select-result J-select-val"></p><i class="pull-icon"></i>'+
                        '<ol class="select-list J-select-list J-font-size"></ol></div><div class="li-btn-box editor-fl">'+
                        '<div class="icon-btn editor-fl J-fontsize-edit" data-type="add"><i class="li-size-icons add-icon"></i></div>'+
                        '<div class="icon-btn editor-fl J-fontsize-edit" data-type="reduce"><i class="li-size-icons reduce-icon"></i>'+
                        '</div></div></li><li class="tools-li editor-clear">'+
                        '<p class="desc editor-fl"><i class="desc-icon height-icon"></i></p>'+
                        '<div class="select-cont editor-fl J-pull-select" data-type="lineHeight"><p class="show-select-result J-select-val"></p>'+
                        '<i class="pull-icon"></i><ol class="select-list J-select-list J-font-height"></ol></div></li></ul>'+
                        '<div class="align-format editor-clear">'+
                        '<span class="align-format-btn font-weight J-font-style" data-type="weight"><i class="icons"></i></span>'+
                        '<span class="align-format-btn font-italic J-font-style" data-type="italic"><i class="icons"></i></span>'+
                        '<span class="align-format-btn font-line J-font-style" data-type="line"><i class="icons"></i></span>'+
                        '<span class="align-format-btn align-left active J-align-style" data-type="left"><i class="icons"></i></span>'+
                        '<span class="align-format-btn align-center J-align-style" data-type="center"><i class="icons"></i></span>'+
                        '<span class="align-format-btn font-default J-font-default"><i class="icons"></i></span></div>'+
                        '<div class="font-color"><ul class="color-table editor-clear">'+
                        '<li class="color-table-li editor-fl active J-color-table" data-type="font">'+
                        '<i class="color-table-icons"></i><span>'+LANGUAGE.imgEdit.textArea.color+'</span></li>'+
                        '<li class="color-table-li editor-fl J-color-table" data-type="bg">'+
                        '<i class="color-table-icons"></i><span>'+LANGUAGE.imgEdit.textArea.bgColor+'</span></li></ul>'+
                        '<div class="color-fix-select">'+
                        '<div class="fix-select-li editor-clear">'+
                        '<span class="color-select J-color-select none-select" data-val="transparent"></span>'+
                        '<span class="color-select J-color-select" data-val="#ffffff" style="background-color: #ffffff;"></span>'+
                        '<span class="color-select J-color-select" data-val="#d5d5d5" style="background-color: #d5d5d5;"></span>'+
                        '<span class="color-select J-color-select" data-val="#45d1d7" style="background-color: #45d1d7;"></span>'+
                        '<span class="color-select J-color-select" data-val="#388bb6" style="background-color: #388bb6;"></span>'+
                        '<span class="color-select J-color-select" data-val="#47b4f8" style="background-color: #47b4f8;"></span>'+
                        '<span class="color-select J-color-select" data-val="#79c7fa" style="background-color: #79c7fa;"></span>'+
                        '<span class="color-select J-color-select" data-val="#5b89cd" style="background-color: #5b89cd;"></span>'+
                        '<span class="color-select J-color-select" data-val="#3750c0" style="background-color: #3750c0;"></span>'+
                        '<span class="color-select J-color-select" data-val="#7a34ff" style="background-color: #7a34ff;"></span>'+
                        '<span class="color-select J-color-select" data-val="#8600ff" style="background-color: #8600ff;"></span>'+
                        '<span class="color-select J-color-select" data-val="#9900cd" style="background-color: #9900cd;"></span>'+
                        '<span class="color-select J-color-select" data-val="#ca00bc" style="background-color: #ca00bc;"></span></div>'+
                        '<div class="fix-select-li editor-clear">'+
                        '<span class="color-select J-color-select" data-val="#a7a7a7" style="background-color: #a7a7a7;"></span>'+
                        '<span class="color-select J-color-select" data-val="#555555" style="background-color: #555555;"></span>'+
                        '<span class="color-select J-color-select" data-val="#56e1bb" style="background-color: #56e1bb;"></span>'+
                        '<span class="color-select J-color-select" data-val="#50c2ae" style="background-color: #50c2ae;"></span>'+
                        '<span class="color-select J-color-select" data-val="#2d749c" style="background-color: #2d749c;"></span>'+
                        '<span class="color-select J-color-select" data-val="#7f9cff" style="background-color: #7f9cff;"></span>'+
                        '<span class="color-select J-color-select" data-val="#4a3eff" style="background-color: #4a3eff;"></span>'+
                        '<span class="color-select J-color-select" data-val="#a006ff" style="background-color: #a006ff;"></span>'+
                        '<span class="color-select J-color-select" data-val="#9200c4" style="background-color: #9200c4;"></span>'+
                        '<span class="color-select J-color-select" data-val="#c237ff" style="background-color: #c237ff;"></span>'+
                        '<span class="color-select J-color-select" data-val="#ec6db3" style="background-color: #ec6db3;"></span>'+
                        '<span class="color-select J-color-select" data-val="#eb6d77" style="background-color: #eb6d77;"></span>'+
                        '<span class="color-select J-color-select" data-val="#ef008e" style="background-color: #ef008e;"></span></div>'+
                        '<div class="fix-select-li editor-clear">'+
                        '<span class="color-select J-color-select" data-val="#1e1e1e" style="background-color: #1e1e1e;"></span>'+
                        '<span class="color-select J-color-select" data-val="#1e965c" style="background-color: #1e965c;"></span>'+
                        '<span class="color-select J-color-select" data-val="#19aa00" style="background-color: #19aa00;"></span>'+
                        '<span class="color-select J-color-select" data-val="#77dc00" style="background-color: #77dc00;"></span>'+
                        '<span class="color-select J-color-select" data-val="#b2df74" style="background-color: #b2df74;"></span>'+
                        '<span class="color-select J-color-select" data-val="#c8ff00" style="background-color: #c8ff00;"></span>'+
                        '<span class="color-select J-color-select" data-val="#eeea00" style="background-color: #eeea00;"></span>'+
                        '<span class="color-select J-color-select" data-val="#d5ba00" style="background-color: #d5ba00;"></span>'+
                        '<span class="color-select J-color-select" data-val="#c57300" style="background-color: #c57300;"></span>'+
                        '<span class="color-select J-color-select" data-val="#f28615" style="background-color: #f28615;"></span>'+
                        '<span class="color-select J-color-select" data-val="#f55b2e" style="background-color: #f55b2e;"></span>'+
                        '<span class="color-select J-color-select" data-val="#d92940" style="background-color: #d92940;"></span>'+
                        '<span class="color-select other-select" data-val="#ffffff" id="colorSelector"></span>'+
                        '</div></div></div><div class="font-width editor-clear">'+
                        '<p class="width-desc editor-fl">'+LANGUAGE.imgEdit.textArea.w+'</p>'+
                        '<div class="width-input-box editor-fl">'+
                        '<input class="width-input editor-fl J-font-input" data-type="width" type="text" value="180px">'+
                        '<ul class="width-edit-box editor-fr">'+
                        '<li class="width-add-btn J-width-edit" data-type="add"></li>'+
                        '<li class="width-reduce-btn J-width-edit" data-type="reduce"></li></ul></div></div>'+
                        '<div class="font-slider-md editor-clear">'+
                        '<p class="slider-md-desc editor-fl">'+LANGUAGE.imgEdit.textArea.rotate+'</p>'+
                        '<div class="editor-slider-cont editor-fl J-slider-container" data-type="rotate">'+
                        '<div class="slider-bar J-slider-bar"></div>'+
                        '<div class="slider-pointer J-slider-pointer"></div></div>'+
                        '<input class="editor-slider-input editor-fl J-font-input" data-type="rotate" type="text" value="0°">'+
                        '</div><div class="font-params-panel J-font-params"><h4 class="params-panel-title">'+LANGUAGE.imgEdit.textArea.insetParams+'</h4>'+
                        '<div class="font-params-edit editor-clear J-font-params-edit"><div class="params-edit-btn editor-fl J-params-edit" data-type="add">'+
                        '<i class="params-edit-icon"></i><span>'+LANGUAGE.imgEdit.textArea.addParams+'</span></div>'+
                        '<div class="params-edit-btn gray-btn editor-fr J-params-edit" data-type="confirm"><span>'+LANGUAGE.imgEdit.textArea.inset+'</span>'+
                        '</div></div></div></div></div>',
            // 图片编辑器容器
            imgEditorImgToolsTPL: '<div class="crop-edit-cont J-imgEdit-tools">' +
                        '<h4 class="title">'+LANGUAGE.imgEdit.imgArea.title+'</h4>' +
                        '<div class="cut-cont editor-clear">' +
                            '<ul class="cut-tools-bar editor-fl">' +
                                '<li class="icon-li J-cropper-edit" data-method="setDragMode" data-option="crop" title="'+LANGUAGE.imgEdit.imgArea.clip+'">' +
                                    '<i class="tools-icon cut-icon"></i>' +
                                '</li>' +
                                '<li class="icon-li J-cropper-edit" data-method="clear" title="'+LANGUAGE.imgEdit.imgArea.cancel+'">' +
                                    '<i class="tools-icon cancel-icon"></i>' +
                                '</li>' +
                                '<li class="icon-li J-cropper-edit" data-method="zoom" data-option="0.1" title="'+LANGUAGE.imgEdit.imgArea.zoomA+'">' +
                                    '<i class="tools-icon zoom-add-icon"></i>' +
                                '</li>' +
                                '<li class="icon-li J-cropper-edit" data-method="zoom" data-option="-0.1" title="'+LANGUAGE.imgEdit.imgArea.zoomB+'">' +
                                    '<i class="tools-icon zoom-reduce-icon"></i>' +
                                '</li>' +
                                '<li class="icon-li J-cropper-edit" data-method="rotate" data-option="-45" title="'+LANGUAGE.imgEdit.imgArea.leftRot+'">' +
                                    '<i class="tools-icon rotate-left-icon"></i>' +
                                '</li>' +
                                '<li class="icon-li J-cropper-edit" data-method="rotate" data-option="45" title="'+LANGUAGE.imgEdit.imgArea.rightRot+'">' +
                                    '<i class="tools-icon rotate-right-icon"></i>' +
                                '</li>' +
                                '<li class="icon-li J-cropper-edit" data-method="reset" title="'+LANGUAGE.imgEdit.imgArea.reset+'">' +
                                    '<i class="tools-icon reset-icon"></i>' +
                                '</li>' +
                                '<li class="text-li J-cropper-edit" data-method="setAspectRatio" data-option="1.7777777777777777">16:9</li>' +
                                '<li class="text-li J-cropper-edit" data-method="setAspectRatio" data-option="1.3333333333333333">4:3</li>' +
                                '<li class="text-li J-cropper-edit" data-method="setAspectRatio" data-option="1">1:1</li>' +
                                '<li class="text-li J-cropper-edit" data-method="setAspectRatio" data-option="0.6666666666666666">2:3</li>' +
                                '<li class="text-li J-cropper-edit" data-method="setAspectRatio" data-option="NaN">'+LANGUAGE.imgEdit.imgArea.diy+'</li>' +
                            '</ul>' +
                            '<div class="cut-cont-show editor-fl">' +
                                '<div class="crop-container J-crop-container"></div>' +
                                '<div class="cut-show-ft editor-clear">' +
                                    '<p class="cut-show-info editor-fl J-cropper-size"></p>' +
                                    '<div class="select-box editor-fr J-cropper-ratio">' +
                                        '<p class="select-result J-ratio-show" data-val="100">100%</p>' +
                                        '<i class="select-icon"></i>' +
                                        '<ul class="select-list J-ratio-list">' +
                                            '<li class="J-ratio-li active" data-val="100">100%</li>' +
                                            '<li class="J-ratio-li" data-val="90">90%</li>' +
                                            '<li class="J-ratio-li" data-val="60">60%</li>' +
                                            '<li class="J-ratio-li" data-val="30">30%</li>' +
                                        '</ul>' +
                                    '</div>' +
                                '</div>' +
                            '</div>' +
                        '</div>' +
                        '<div class="btn-cont editor-clear">' +
                            '<div id="cropper-replace-img" class="img-edit-btn editor-fl">'+LANGUAGE.imgEdit.imgArea.replace+'</div>' +
                            '<div class="btn-cont-r editor-fr">' +
                                '<input class="img-edit-btn editor-fl J-cropper-cancel" type="button" value="'+LANGUAGE.imgEdit.imgArea.cancel+'">' +
                                '<input class="img-edit-btn editor-fl J-cropper-edit" type="button" data-method="getCroppedCanvas" value="'+LANGUAGE.imgEdit.imgArea.confirm+'">' +
                            '</div>' +
                        '</div>' +
                    '</div>',
            // 图片编辑器内拖拽工具模板
            imgEditorDragTPL: '<div class="editor-resize-layer editor-hidden J-drag-box" data-type="drag">'+
                        '<div id="resize-text-holder"></div>'+
                        '<div class="editor-resize-pointer p-t-l J-drag-pointer" data-type="t-l"></div>' +
                        '<div class="editor-resize-pointer p-t-c J-drag-pointer" data-type="t-c"></div>' +
                        '<div class="editor-resize-pointer p-t-r J-drag-pointer" data-type="t-r"></div>' +
                        '<div class="editor-resize-pointer p-l-c J-drag-pointer" data-type="c-l"></div>' +
                        '<div class="editor-resize-pointer p-r-c J-drag-pointer" data-type="c-r"></div>' +
                        '<div class="editor-resize-pointer p-b-l J-drag-pointer" data-type="b-l"></div>' +
                        '<div class="editor-resize-pointer p-b-c J-drag-pointer" data-type="b-c"></div>' +
                        '<div class="editor-resize-pointer p-b-r J-drag-pointer" data-type="b-r"></div></div>'
        }
    };
	SetLanguage("zh_CN");
    
    // 全局对编辑器对象
    win.FxEditor = {
        /*
        ** @fn getEditor获取编辑器
        ** @params {string} appendEl插入节点ID
        ** @params {element} intVal初始值
        */
        getEditor: function(appendEl, intVal){
        	this.appendEl = appendEl;
        	this.timer = null;
        	this.fxEditorRenderObj = new fxEditorRender(appendEl);
        	
            var _initVal = intVal || "";
            
            this.fxEditorRenderObj.renderInitTemplate(_initVal);
        },

        /*
        ** @fn getContentHtml获取所有内容结构
        ** @return {string} 返回HTML结构字符串
        */
        getContentHtml: function(){
        	var _allHtml = $("#"+this.appendEl+"").find(".J-editor-content").html();
        	_allHtml.replace(/<scirpt>/g,"&lt;scirpt&gt;");
        	_allHtml.replace(/<\/scirpt>/g,"&lt;/scirpt&gt;");
        	
        	return _allHtml;
        },
        
        // 监听编辑器变化
        listenFxEditorChange: function(CB){
        	var _self = this;
        	
        	$("#"+this.appendEl+"").on("DOMSubtreeModified", ".J-editor-content", function(){
                 clearTimeout(_self.timer);
                 _self.timer = setTimeout(function(){
                	 CB && CB(_self.getContentSize());
                 }, 100);
             });
        },
        
        // 设置标题
        setEditorTitle: function(title){
        	this.fxEditorRenderObj.setPreviewTitle(title);
        },

        /*
        ** @fn getContentSize获取内容字节大小,单位为字节
        ** @return {object} object.allSize:所有内容大小
        **                  object.textSize:所有文本内容大小
        **                  object.imageSize:所有图片文件大小
        **                  object.audioSize:所有音频文件大小
        **                  object.videoSize:所有视频文件大小
        */
        getContentSize: function(){
        	var _textEl = $("body").find(".J-editor-content").find(".J-edit-text"),
        		_imageEl = $("body").find(".J-editor-content").find(".J-editor-img"),
        		_videoEl = $("body").find(".J-editor-content").find(".J-video"),
        		_audioEl = $("body").find(".J-editor-content").find(".J-audio"),
        		_textSize = 0,
        		_imageSize = 0,
        		_videoSize = 0,
        		_audioSize = 0;
        	
        	$.each(_textEl, function(key){
        		_textSize += mbStringLength(_textEl.eq(key).text());
        	});
        	$.each(_imageEl, function(key){
        		_imageSize += parseInt(_imageEl.eq(key).attr("data-size"));
        	});
        	$.each(_videoEl, function(key){
        		_videoSize += parseInt(_videoEl.eq(key).attr("data-size"));
        	});
        	$.each(_audioEl, function(key){
        		_audioSize += parseInt(_audioEl.eq(key).attr("data-size"));
        	});
        	
            return {
                allSize: _textSize+_imageSize+_audioSize+_videoSize,
                textSize: _textSize,
                imageSize: _imageSize,
                audioSize: _audioSize,
                videoSize: _videoSize
            }
        }
    }
    
    // 文字大小统计，字节
    function mbStringLength(s) { 
    	var totalLength = 0,
    		i,
    		charCode; 
    	
    	for (i = 0; i < s.length; i++) { 
	    	charCode = s.charCodeAt(i); 
	    	if (charCode < 0x007f) { 
	    		totalLength = totalLength + 1; 
	    	} else if ((0x0080 <= charCode) && (charCode <= 0x07ff)) { 
	    		totalLength += 2; 
	    	} else if ((0x0800 <= charCode) && (charCode <= 0xffff)) { 
	    		totalLength += 3; 
	    	} else if ((00010000 <= charCode) && (charCode <= 0x1fffff)) { 
	    		totalLength += 4; 
	    	} 
    	} 
    	return totalLength; 
    }
    
    // 浏览器flash检查，依赖webUploader浏览器检查
    function getFlashSuport(){
    	var _userAgent = window.navigator.userAgent;
        if(!WebUploader.Uploader.support('flash') && WebUploader.browser.ie){
        	var _reIE = new RegExp("MSIE (\\d+\\.\\d+);"),
	   	 		_fIEVersion;
        	
	    	_reIE.test(_userAgent);
	        _fIEVersion = parseFloat(RegExp["$1"]);
	       
	        if (_fIEVersion < 10) {
	        	return false;
	        }else{
	        	return true;
	        }
        }else if(WebUploader.Uploader.support('flash') && WebUploader.browser.ie){
        	var _reIE = new RegExp("MSIE (\\d+\\.\\d+);"),
        		_swf = new ActiveXObject('ShockwaveFlash.ShockwaveFlash'),
       	 		_swfVersion = _swf.GetVariable("$version").split("WIN"),
       	 		_fIEVersion;
        	
        	_swfVersion = parseInt(_swfVersion[1]);
        	_reIE.test(_userAgent);
            _fIEVersion = parseFloat(RegExp["$1"]);
           
            if (_fIEVersion < 10) {
            	if(_swfVersion > 14){
            		return true;
            	}else{
            		return false;
            	}
            }else{
            	return true;
            }
        }else{
        	return true;
        }
    }
    
    // 编辑器渲染与事件绑定
    function fxEditorRender(appendEl) {
        var _self = this,
            _parentEl = $("#"+appendEl+""),
            _timer;
        
        this.previewTitle = "";	// 预览界面标题
        this.selection = null;	// 光标select对象
        this.range = null;		// 光标range对象
        this.cursorSelectEl = "";	// 光标选中节点
        this.paramsAppendEl = "";	// 参数插入节点
        this.keyCount = 0;		// 关键帧数量统计
        this.uploader = null;
        this.imgCropperObj = null;	// 图片裁剪对象
        this.editImgEL = "";		// 编辑操作的图片元素
        this.videCutObj = null;		// 视频裁剪对象
        this.videoEditObjData = {};	// 当前裁剪视频数据
        this.editVideogEL = "";		// 编辑操作的视频元素
        this.editorAppendEl = appendEl;
        this.upFileType = "";	// 当前上传文件类型

        this.currentEditChartType = "addKey";		// 当前编辑图表的类型，addKey新增一个图表关键帧，add文字帧内新增一个图表，edit表示需要编辑的图表
        this.currentEditChartKeyFrameEl = "";		// 当前操作图表所属的关键帧
        
        // 关键帧内文本粘贴            
        $(document).on('paste', ".J-edit-text,.editor-resize-text", function(e) {
            e.preventDefault();
            var text = null,
            	textRange,
            	sel;
            
            // 干掉IE http之类地址自动加链接
            try {
                document.execCommand("AutoUrlDetect", false, false);
            } catch (e) {}
        
            if(window.clipboardData && clipboardData.setData) {
                // IE
                text = window.clipboardData.getData('text');
            } else {
                text = (e.originalEvent || e).clipboardData.getData('text/plain');
            }
            if (document.body.createTextRange) {    
                if (document.selection) {
                    textRange = document.selection.createRange();
                } else if (window.getSelection) {
                    sel = window.getSelection();
                    var range = sel.getRangeAt(0);
                    
                    // 创建临时元素，使得TextRange可以移动到正确的位置
                    var tempEl = document.createElement("span");
                    tempEl.innerHTML = "&#FEFF;";
                    range.deleteContents();
                    range.insertNode(tempEl);
                    textRange = document.body.createTextRange();
                    textRange.moveToElementText(tempEl);
                    tempEl.parentNode.removeChild(tempEl);
                }
                textRange.text = text;
                textRange.collapse(false);
                textRange.select();
            } else {
                // Chrome之类浏览器
                document.execCommand("insertText", false, text);
            }
        });
        // 去除Crtl+b/Ctrl+i/Ctrl+u等快捷键
        $(document).on('keydown', ".J-edit-text,.editor-resize-text", function(e) {
            // e.metaKey for mac
            if (e.ctrlKey || e.metaKey) {
                switch(e.keyCode){
                    case 66: //ctrl+B or ctrl+b	
                    case 98: 
                    case 73: //ctrl+I or ctrl+i
                    case 105: 
                    case 85: //ctrl+U or ctrl+u
                    case 117: {
                        e.preventDefault();    
                        break;
                    }
                }
            }    
        });
        
        // 监听关键帧内容变化
        _parentEl.on("DOMSubtreeModified", ".J-editor-content", function(){
             clearTimeout(_timer);
             _timer = setTimeout(function(){
				if(navigator.appName == "Microsoft Internet Explorer" && parseInt(navigator.appVersion.split(";")[1].replace(/[ ]/g, "").replace("MSIE","")) < 9){
	            	 _self.renderPreviewContent();
				}else{
					_self.renderPreviewContentScale();
				}
             }, 100);
         });

        // 工具栏绑定事件
        _parentEl.on("click", ".J-tools-li", function(){
            var _type = $(this).data("type");
            // 文本工具直接渲染关键帧，上传工具监听input的change事件
            if(_type === "text"){
            	_self.renderTextKeyframe(_type);
            }else if(_type === "chart"){
            	// 显示图表编辑层，当前图表所属关键帧为空，类型为新增一个图表关键帧
            	_self.currentEditChartType = "addKey";
            	_self.currentEditChartKeyFrameEl = "";
            	_self.showChartEidtLayer();
            }else if(_type === "module"){
            	quoteRmsTemplate();
            }
        });
        
        // 触发渲染关键帧编辑工具条
        _parentEl.delegate(".J-keyframe", "mousedown", function(e){
        	var _event = e || window.event;
        	// 执行渲染关键帧编辑工具条
        	_self.renderKeyframeEditTools($(this));
        	_event.stopPropagation();
        });
        
        // 关键帧图片绑定事件
        _parentEl.on("mouseenter", ".J-editor-img", function(){
        	_self.keyframeImageHover($(this));
        }).on("mouseleave", ".J-editor-img", function(){
        	$(this).find(".J-img-edit-layer").remove();
        });
        
        // 删除关键帧内图片
        _parentEl.on("click", ".J-img-dele", function(){
        	var _currentKeyFrame = $(this).closest(".J-keyframe-content"),
        		_keyframePNodeLength = _currentKeyFrame.find(".J-edit-text").length;
        	
        	// 判断图片帧内是否还有文本信息，没有则删除帧
        	if(_keyframePNodeLength > 0){
            	$(this).closest(".J-editor-img").remove();
        	}else{
        		_currentKeyFrame.closest(".J-keyframe").remove();
        		_self.keyCount--;
        	}
        	// 移除关键帧操作区域
        	$(".J-keyframe-edit-content").remove();
        });
        
        // 编辑关键帧内图片
        _parentEl.on("click", ".J-img-edit", function(){
        	var _editImgEl = $(this).closest(".J-keyframe-content").find(".J-editor-img"),
        		_editImgElType = _editImgEl.data("type"),
        		_editImgParams;
        	
        	if(_editImgElType === "image"){        		
            	_self.editImgEL = _editImgEl;
            	// 渲染图片裁剪器
            	FXImgEditor.init(_editImgEl);
        	}else if(_editImgElType === "chart"){
        		// 显示图表编辑层，当前图表所属帧已存在，图表新增类型为在当前帧内编辑
            	_self.currentEditChartType = "edit";
            	_self.currentEditChartKeyFrameEl = $(this).closest(".J-keyframe");
            	_editImgParams = $(this).closest(".J-keyframe").find(".J-chart-data").text();
            	isShowChart(_editImgParams);
        	}
        });
        
        // 视频裁剪器删除片段操作
        _parentEl.on("click", ".J-vedio-cut-btn", function(){
        	$(this).toggleClass("active").siblings(".J-vedio-cut-btn").removeClass("active");
        });
        // 视频裁剪器清晰度调整
        _parentEl.on("click", ".J-vedio-ratio-btn", function(){
        	var _showList = $(this).find(".J-vedio-ratio-list");
        	_showList.slideToggle("fast");
        }).on("click", ".J-vedio-ratio-li", function(){
        	var _showValEl = $(".J-vedio-ratio-show"),
        		_selectVal = $(this).data("val");
        	
        	_showValEl.text($(this).text()).data("val", _selectVal);
        });
        
        // 视频裁剪器预览
        _parentEl.on("click", ".J-vedio-preivew-btn", function(){
        	var _deleBtnActive = $(".J-vedio-cut-btn.active"),
        		_deleBtnType = _deleBtnActive.data("type"),
        		_minTime = 0,
        		_maxTimeStr = $(".irs-max").text(),
        		_maxTime = _maxTimeStr.substring(0, _maxTimeStr.indexOf("″")),
        		_startTimeStr = $(".irs-from").text(),
        		_startTime = _startTimeStr.substring(0, _startTimeStr.indexOf("″")),
        		_endTimeStr = $(".irs-to").text(),
        		_endTime =  _endTimeStr.substring(0, _endTimeStr.indexOf("″"));
        	
        	if(_deleBtnActive.length > 0 && _deleBtnType == 0){
        		_self.videCutObj.currentTime(_minTime);
        		_self.videCutObj.play();

        	  	setTimeout(function(){
        	  		_self.videCutObj.currentTime(_endTime);
            		_self.videCutObj.play();
        		}, (_startTime-_minTime+1)*1000);
        	}else{
        		_self.videCutObj.currentTime(_startTime);
        		_self.videCutObj.play();

        	  	setTimeout(function(){
        	  		_self.videCutObj.pause();
        		}, (_endTime-_startTime+1)*1000);
        	}
        });
        
        // 确认裁剪当前视频
        _parentEl.on("click", ".J-vedio-confirm", function(){
        	var _deleBtnActive = $(".J-vedio-cut-btn.active"),
        		_deleBtnType = _deleBtnActive.data("type"),
        		_minTime = 0,
        		_maxTimeStr = $(".irs-max").text(),
        		_maxTime = _maxTimeStr.substring(0, _maxTimeStr.indexOf("″")),
        		_startTimeStr = $(".irs-from").text(),
        		_startTime = _startTimeStr.substring(0, _startTimeStr.indexOf("″")),
        		_endTimeStr = $(".irs-to").text(),
        		_endTime =  _endTimeStr.substring(0, _endTimeStr.indexOf("″")),
        		_ratio = $(".J-vedio-ratio-show").data("val"),
        		_type = 1,
        		_postData = {};
        	
        	// type0为删除选中片段
        	if(_deleBtnActive.length > 0){
        		_type = _deleBtnType;
        	}
        	// 数据请求发送数据
        	_postData = {
        		"src": _self.videoEditObjData.videoSrc,
        		"startTime": _startTime,
        		"endTime": _endTime,
        		"clarity": _ratio,
        		"width": _self.videoEditObjData.videoW,
        		"height": _self.videoEditObjData.videoH,
        		"type": _type 
        	};
        	
        	_self.listenVideoCutEdit(_postData);
        });
        
        // 关闭视频裁剪器
        _parentEl.on("click", ".J-video-close", function(){
        	$("#editor-video, .J-video-cut-bg").remove();
        	_self.videCutObj.dispose();
        	_self.videoEditObjData = {};
        });
        
        // 关键帧操作按钮事件绑定
        _parentEl.on("click", ".J-edit-keyframe-btn", function(){
            var _type = $(this).data("type"),
                _keyframeEl = $(this).closest(".J-keyframe");
            // 根据相应的类型判断相应的操作
            switch(_type){
                case "dele":
                	_self.keyCount--;
                    _keyframeEl.remove();
                    break;
                case "up":
                    _self.keyframeUp(_keyframeEl);
                    break;
                case "down":
                    _self.keyframeDown(_keyframeEl);
                    break;
                case "params":
                	_self.paramsAppendEl = _keyframeEl.find(".J-edit-text");
                	_self.renderParamsContent();
                    break;
                case "text":
                	_self.keyframeInsertText(_keyframeEl);
                    break;
                case "edit":
                	_self.renderVideoCut(_keyframeEl);
                    break;
                case "chart":
                	// 显示图表编辑层，当前图表所属帧已存在，图表新增类型为在当前帧内新增
                	_self.currentEditChartType = "add";
                	_self.currentEditChartKeyFrameEl = _keyframeEl;
                	_self.showChartEidtLayer();
                	break;
            }
        });
        
        // 新增参数
        _parentEl.on("click", ".J-add-params", function(){
        	var _hasLength = $(".J-check-box").find("li").length;
        	for(var i = 1; i < 5; i++){
        		var showLength = i+_hasLength;
        		var _checkboxLi = $('<li class="check-box-li"><input id="params-'+showLength+'" value="{#'+LANGUAGE.paramsText.desc+''+showLength+'#}" type="checkbox"><label for="params-'+showLength+'">'+LANGUAGE.paramsText.desc+''+showLength+'</label></li>');
        		$(".J-check-box").append(_checkboxLi);
        	}
        });
        // 关闭参数列表
        _parentEl.on("click", ".J-editor-params-cancel", function(){
        	$(".J-editor-pop").remove();
        });
        // 获取插入参数插入点
        _parentEl.on("click", ".J-edit-text", function(){
        	_self.getInitCursor();
        });
        $(document).keydown(function(event){
        	_self.getInitCursor();
        });
        _parentEl.on("DOMSubtreeModified", ".J-edit-text", function(){
        	_self.getInitCursor();
        });
        
        // 确认参数列表
        _parentEl.on("click", ".J-editor-params-confirm", function(){
        	var _checkedEl = $(".J-editor-pop").find("input:checked"),
        		_checkeStr = "";
        	
        	$.each(_checkedEl, function(key){
        		_checkeStr += _checkedEl.eq(key).val();
        	});

        	$(".J-editor-pop").remove();
        	_self.renderParamsText( _checkeStr);
        });
    };
    
    /*
    ** @fn fxEditorRender.renderVideoCut渲染视频裁剪器
    ** @params {element} editVideoEl视频节点
    */
    fxEditorRender.prototype.renderVideoCut = function(editVideoEl){
    	var _videoEl = editVideoEl.find("video"),
    		_videoSize = (_videoEl.attr("data-size")/1024).toFixed(2)+"KB",
    		_videoSrc = _videoEl.attr("src"),
    		_videoType = _videoEl.attr("src").split("."),
    		_videoTime = parseInt(_videoEl[0].duration)+"s",
    		_videoCutTemplate = $(TEMPLATE.videoCutTemplate);		// 视频裁剪器模板
    	
    	_videoType = _videoType[_videoType.length-1];
    	
    	_videoCutTemplate.find(".J-video-cut-resource").attr("src", _videoSrc).attr("type","video/"+_videoType+"");
    	_videoCutTemplate.find(".J-video-size").text(""+LANGUAGE.audioEdit.size+"："+_videoSize);
    	_videoCutTemplate.find(".J-video-time").text(""+LANGUAGE.audioEdit.time+"："+_videoTime);
    	
    	$("#"+this.editorAppendEl+"").find(".fxeditor-page").append(_videoCutTemplate);
    	
    	this.editVideogEL = _videoEl;
    	this.videoEditObjData = {
    		videoSrc: _videoSrc,
    		videoW: _videoEl[0].videoWidth,
    		videoH: _videoEl[0].videoHeight
    	}
    	
    	// 视频播放器初始化
    	this.initVideoCutResource(parseInt(_videoEl[0].duration));
    }
    
    // 初始化视频裁剪器中视频加载
    fxEditorRender.prototype.initVideoCutResource = function(videoTime){
    	var _self = this;
    	
    	_self.videCutObj = videojs("my-video");
    	
    	_self.videCutObj.on("loadeddata", function(){
    		$("#video-range").ionRangeSlider({
    			min: 0,            //Range最小值
    			max: videoTime, 	//Range最大值
    			from: 0,           //Range默认开始位置
    			to: videoTime,  //Range默认结束位置
    			type: 'double',    //设置游标个数
    			step: 1,           //移动范围  
    			prefix: "",        //设置数值前缀
    			postfix: "″",      //设置数值后缀
    			hasGrid: true,    //底部是否出现刻度
    			prettify: true
    		});
    	});
    }
    
    /*
    ** @fn listenVideoCutEdit监听视频裁剪器确定
    ** @params {string} postData发送数据
    */
    fxEditorRender.prototype.listenVideoCutEdit = function(postData){
     	var _self = this,
     		_url = FXEDITOR_CONFIG.HOME_URL+FXEDITOR_CONFIG.videoConfig.tailorUrl;

     		_self.renderEditorLoad(true);
			// 向服务器发送裁剪后图片数据
			_self.IO(_url, "POST", postData, function(rst){
				if(rst.state == 0){
					var _newVideoSize = rst.size,
						_newVideoSrc = rst.path;
					
					if(_newVideoSrc.indexOf("http") < 0){
						_newVideoSrc = FXEDITOR_CONFIG.HOME_URL+_newVideoSrc;
					}
					
					_self.editVideogEL.attr("src", _newVideoSrc)
					_self.editVideogEL.attr("data-size", _newVideoSize);
					$("#editor-video, .J-video-cut-bg").remove();
					
					_self.editVideogEL.append("<p class='J-change'></p>");
					_self.editVideogEL.find(".J-change").remove();
					
		        	_self.videCutObj.dispose();
		        	_self.videoEditObjData = {};
			    	_self.renderErrorMsg(LANGUAGE.videoHint.cutSuccess);
				}else{
					$("#editor-video, .J-video-cut-bg").remove();
		        	_self.videCutObj.dispose();
		        	_self.videoEditObjData = {};
			    	_self.renderErrorMsg(LANGUAGE.videoHint.cutFail);
				}
				_self.renderEditorLoad(false);
			}, function(){
				_self.renderEditorLoad(false);
				$("#editor-video, .J-video-cut-bg").remove();
	        	_self.videCutObj.dispose();
	        	_self.videoEditObjData = {};
		    	_self.renderErrorMsg(LANGUAGE.videoHint.cutFail);
			});
    }    

	/*
    ** @fn IO向服务器发送数据方法
    ** @{string} sendURL数据发送路径
    ** @{string} method数据请求方法"POPST"&"GET"
    ** @{JSONArray} params发送的数据
    ** @{function} successCB成功后回调函数
    ** @{function} errorCB失败后回调函数
    */
    fxEditorRender.prototype.IO = function(sendURL, method, params, successCB, errorCB){
        // 请求数据
        $.ajax({
            url: sendURL,		// 请求地址
            type: method,		// 方法
            data: params,		// 数据
            dataType: "JSON",	// 数据类型
            success: function(rst) {
                successCB && successCB(rst);
            },
            error: function(XMLHttpRequest, textStatus) {
            	errorCB && errorCB()
            }
        });
    }
    
    // 创建文件上传
    fxEditorRender.prototype.fileUpload = function(uploadUrl, uploadEl, fileSize, acceptObj, uploadBeforeCB, uploadSuccessCB, uploadErrorCB){
    	 var _self = this;
    	 _self.uploader = WebUploader.create({
         	 auto: true, // 自动上传
             swf: 'rms/plugin/resource/webuploader-0.1.5/Uploader.swf',
             // 文件接收服务端。
             server: uploadUrl,
             compress: false,
             // 选择文件的按钮。可选。
             // 内部根据当前运行是创建，可能是input元素，也可能是flash.
             pick: {
            	 id: uploadEl,
            	 multiple: false
             },
             // 不压缩image, 默认如果是jpeg，文件上传前会压缩一把再上传！
             resize: false,
             // 去重
             duplicate: true,
             // 文件大小B为单位
             fileSingleSizeLimit: fileSize,
             // 选择文件类型
             accept: acceptObj
    	 }).on("beforeFileQueued", function(file){
        	 _self.renderEditorLoad(true);
        	 uploadBeforeCB && uploadBeforeCB(file);
         }).on( 'uploadError', function(file, reason) {
        	 _self.renderErrorMsg(LANGUAGE.fileUpHint.fail);
        	 _self.renderEditorLoad(false);
         }).on( 'uploadSuccess', function(file, response) {
        	 uploadSuccessCB && uploadSuccessCB(file, response);
        	 _self.renderEditorLoad(false);
         }).on("error", function(type ){
        	 _self.renderEditorLoad(false);
        	 uploadErrorCB && uploadErrorCB(type);
         });
    }
    
    /* 
    ** @fn renderInitTemplate渲染初始模板
    ** @params {element} initVal模板内容初始值
    */
    fxEditorRender.prototype.renderInitTemplate = function(initVal){
        var _editorAppendEl = $("#"+this.editorAppendEl+""),        // 整个编辑器插入节点
            _containerTemplate = $(TEMPLATE.containerTemplate),     // 编辑器容器模板
            _previewContentTemplate = $(TEMPLATE.previewContentTemplate),   // 编辑器预览模板
            _toolsBarContentTemplate = $(TEMPLATE.toolsContainerTemplate),  // 编辑器工具栏模板
            _editorContentTemplate = $(TEMPLATE.editorContentTemplate);     // 编辑器内容容器模板
        
        // 渲染整个编辑器
        _editorAppendEl.append(_containerTemplate);
        // 渲染工具栏容器
        _containerTemplate.find(".J-editor-container").append(_toolsBarContentTemplate);
        // 渲染编辑内容容器
        _containerTemplate.find(".J-editor-container").append(_editorContentTemplate);
        // 渲染预览区域
        _containerTemplate.find(".J-editor-preview").append(_previewContentTemplate);

        // 渲染工具条内详细工具
        this.renderToolsBar(_toolsBarContentTemplate);
        // 显示初始值
        _containerTemplate.find(".J-editor-content").html(initVal);
    };

    // 渲染工具条
    fxEditorRender.prototype.renderToolsBar = function(appendEl){
        var _self = this,
        	_editorToolsArr = FXEDITOR_CONFIG.editTools,        // 工具配置文件
            _editorToolsTemplate,								// 工具模板
            _inputAccept,				// 上文件限制
            _iconClassName,				// 工具图标
            _toolsId,					// 工具ID
            _toolsName,					// 工具名称
            _uploadUrl,					// 上传地址
            _uploadFileMaxSize;			// 上传文件最大尺寸
            
        // 根据参数配置渲染工具条
        $.each(_editorToolsArr, function(key){
            _editorToolsTemplate = $(TEMPLATE.toolsTemplate);   // 工具条内详细工具模板
            // 根据配置判断渲染工具条类型
            switch (_editorToolsArr[key]){
                case "text":
                    _iconClassName = "text-icon";
                    _toolsName = LANGUAGE.mainTools.text;
                    break;
                case "image":
                    _iconClassName = "image-icon";
                    _toolsName = LANGUAGE.mainTools.img;
                    _toolsId = "imageFile";
                	_inputAccept = {
            		    title: 'Images',
            		    extensions: FXEDITOR_CONFIG.imageConfig.format.join(","),
            		    mimeTypes: "image/*"
            		};
                	_uploadUrl = FXEDITOR_CONFIG.HOME_URL+FXEDITOR_CONFIG.imageConfig.uploadUrl;
                	_uploadFileMaxSize = FXEDITOR_CONFIG.imageConfig.size;
                    break;
                case "chart":
                	_toolsId = "";
                    _iconClassName = "chart-icon";
                    _toolsName = LANGUAGE.mainTools.chart;
                    break;
                case "audio":
                    _iconClassName = "audio-icon";
                    _toolsName = LANGUAGE.mainTools.audio;
                	_inputAccept = {
            		    title: 'Audio',
            		    extensions: FXEDITOR_CONFIG.audioConfig.format.join(","),
            		    mimeTypes: "audio/*"
            		};
                    _toolsId = "audioFile";
                	_uploadUrl = FXEDITOR_CONFIG.HOME_URL+FXEDITOR_CONFIG.audioConfig.uploadUrl;
                	_uploadFileMaxSize = FXEDITOR_CONFIG.audioConfig.size;
                    break;
                case "video":
                    _iconClassName = "video-icon";
                    _toolsName = LANGUAGE.mainTools.video;
                	_inputAccept = {
            		    title: 'Video',
            		    extensions: FXEDITOR_CONFIG.videoConfig.format.join(","),
            		    mimeTypes: "video/*"
            		};
                    _toolsId = "videoFile";
                	_uploadUrl = FXEDITOR_CONFIG.HOME_URL+FXEDITOR_CONFIG.videoConfig.uploadUrl;
                	_uploadFileMaxSize = FXEDITOR_CONFIG.videoConfig.size;
                    break;
                case "module":
                	_toolsId = "";
                    _iconClassName = "module-icon";
                    _toolsName = LANGUAGE.mainTools.module;
                    break;
            }

            if(_editorToolsArr[key] === "module"){
            	_editorToolsTemplate.addClass("width-80");
            }
            	
            _editorToolsTemplate.attr({"id":_toolsId});
            _editorToolsTemplate.attr("data-type", _editorToolsArr[key]);
            _editorToolsTemplate.find(".J-tools-icon").addClass(_iconClassName);
            _editorToolsTemplate.find(".J-tool-name").text(_toolsName);
            appendEl.append(_editorToolsTemplate);
            
            if(_editorToolsArr[key] != "text" && 
            		_editorToolsArr[key] != "chart" &&
            		_editorToolsArr[key] != "module" &&
            		getFlashSuport()
            ){
                _self.fileUpload(_uploadUrl, '#'+_toolsId+'', _uploadFileMaxSize, _inputAccept, function(file){
                	var _fileType = file.type.split("/")[0];
                	
                	switch(_fileType){
	                	case "image":
	                    	_self.upFileType = LANGUAGE.mainTools.img;
	                		break;
	                	case "video":
	                    	_self.upFileType = LANGUAGE.mainTools.video;
	                		break;
	                	case "audio":
	                    	_self.upFileType = LANGUAGE.mainTools.audio;
	                		break;
                	}
                }, function(file, response){
                	if(response.state === "SUCCESS"){
                		switch(_editorToolsArr[key]){
	                		case "image":
	                			_self.renderImageKeyframe(response, _editorToolsArr[key]);
	                            break;
	                        case "audio":
	                        	_self.renderAudioKeyframe(response, _editorToolsArr[key]);
	                            break;
	                        case "video":
	                        	_self.renderVideoKeyframe(response, _editorToolsArr[key]);
	                            break;
                		}
                	}else{
                		_self.renderErrorMsg(LANGUAGE.fileUpHint.fail);
                	}
                }, function(type){
                	switch (type) {
	                	case "Q_EXCEED_SIZE_LIMIT":
	                	case "F_EXCEED_SIZE":
	                		_self.renderErrorMsg(""+_self.upFileType+""+LANGUAGE.fileUpHint.max+"");
	                		break;
	                	case "Q_TYPE_DENIED":
	                		_self.renderErrorMsg(LANGUAGE.fileUpHint.type);
	                		break;
                	}
                });
            }
        });
        
        if(!getFlashSuport()){
        	$("#imageFile, #audioFile, #videoFile").addClass("disable");
        	_self.renderErrorMsg(LANGUAGE.flashHint, 6000);
        }
    }
    
    /*
    ** @fn fxEditorRender.renderKeyframeEditTools渲染关键帧内编辑器工具条
    ** @params {element} editKeyframe当前编辑的关键帧
    */
    fxEditorRender.prototype.renderKeyframeEditTools = function(editKeyframe){
    	var _self = this,
    		_type,
	        _keyFrameEditContentTemplate,
	        _imgBtn;
    	
	    // 判断是否有关键帧编辑区
	    if(editKeyframe.find(".J-keyframe-edit-content").length === 0){
	        _type = editKeyframe.data("type");	// 当前关键帧类型
	        _keyFrameEditContentTemplate = _self.renderKeyframeEdit(_type);	// 当前帧编辑器容器
	        _imgBtn = _keyFrameEditContentTemplate.find("#keyAddImage");	// 当前帧编辑器内图片上传按钮
	        // 移除其他帧编辑器
	        $(".J-keyframe-edit-content").remove();
	        editKeyframe.addClass("active").siblings(".J-keyframe").removeClass("active");	// 当前帧聚焦
	        editKeyframe.append(_keyFrameEditContentTemplate);		// 为当前帧插入关键帧内容编辑器
	        
	        // 判断当前帧如果是文本帧则准备图片上传事件绑定
	        if(_type === "text"){
	        	var _inputAccept = {
	        		    title: 'Images',
	        		    extensions: FXEDITOR_CONFIG.imageConfig.format.join(","),
	        		    mimeTypes: "image/*"
	        		},
	        		_url = FXEDITOR_CONFIG.HOME_URL+FXEDITOR_CONFIG.imageConfig.uploadUrl;
	        	
	        	if(getFlashSuport()){
		        	// 初始化文件上传插件
		        	_self.fileUpload(_url, _imgBtn, FXEDITOR_CONFIG.imageConfig.size, _inputAccept, function(file){
		        		// 当前上传关键帧内图片数量
		            	var _imgEl = $("#rt_"+file.source.ruid+"").closest(".J-keyframe").find(".J-editor-img"),
		            		_imgLength = _imgEl.length;
		            	
		            	if(_imgLength > 0){
		            		_self.renderErrorMsg(LANGUAGE.keyEditHint.min);
		            		_self.renderEditorLoad(false);
		            		_self.uploader.cancelFile(file);
		            	}
		            }, function(file, response){
		            	if(response.state === "SUCCESS"){
		            		_self.renderImage(response, $("#rt_"+file.source.ruid+"").closest(".J-keyframe").find(".J-keyframe-content"));
		            	}else{
		            		_self.renderErrorMsg(LANGUAGE.fileUpHint.fail);
		            	}
		            }, function(type){
		            	switch (type) {
		                	case "Q_EXCEED_SIZE_LIMIT":
		                	case "F_EXCEED_SIZE":
		                		_self.renderErrorMsg(LANGUAGE.imgHint.max);
		                		break;
		                	case "Q_TYPE_DENIED":
		                		_self.renderErrorMsg(LANGUAGE.fileUpHint.type);
		                		break;
		            	}
		            });
	            }else{
	            	$("#keyAddImage").addClass("disable");
	            }
	        }
	    }
    }
    
    /*
	** @fn fxEditorRender.renderTextKeyframe渲染文字关键帧
	** @params {string} keyType渲染关键帧类型
	*/
    fxEditorRender.prototype.renderTextKeyframe = function(keyType){
        if(this.keyCount >= 15){
        	this.renderErrorMsg(LANGUAGE.keyEditHint.max);
        	return false;
        }
        this.keyCount++;
        
        var _keyframeContentTemplate = $(TEMPLATE.keyframeContentTemplate),
            _keyframeFistContentTemplate = $(TEMPLATE.editTextTemplate);
        
        _keyframeContentTemplate.attr("data-type", keyType);
        _keyframeFistContentTemplate.attr("contenteditable", true);
        _keyframeContentTemplate.find(".J-keyframe-content").append(_keyframeFistContentTemplate);
        $(".J-editor-content").append(_keyframeContentTemplate);
        _keyframeContentTemplate.find(".J-edit-text").focus();
        
        // 执行渲染关键帧编辑工具条
        this.renderKeyframeEditTools(_keyframeContentTemplate);
    }

    /*
	** @fn fxEditorRender.renderImageKeyframe渲染图片关键帧
	** @params {string} keyType渲染关键帧类型
	*/
    fxEditorRender.prototype.renderImageKeyframe = function(filesData, keyType){
    	if(this.keyCount >= 15){
        	this.renderErrorMsg(LANGUAGE.keyEditHint.max);
        	return false;
        }
        this.keyCount++;
        var _keyframeContentTemplate = $(TEMPLATE.keyframeContentTemplate),		// 关键帧容器模板
            _keyframeFistContentTemplate = $(TEMPLATE.editImgTemplate),			// 关键帧图片容器
            _imageSize = filesData.size,	// 图片大小
            _imageSrc = filesData.url;		// 图片路径
        
        if(_imageSrc.indexOf("http") < 0){
        	_imageSrc = FXEDITOR_CONFIG.HOME_URL+_imageSrc;
        }
        
        _keyframeFistContentTemplate.attr({"data-size": _imageSize, "data-type": "image"}).html('<img class="J-main-img" src="'+_imageSrc+'">');
        _keyframeContentTemplate.attr("data-type", keyType);
        _keyframeContentTemplate.find(".J-keyframe-content").append(_keyframeFistContentTemplate);
        $(".J-editor-content").append(_keyframeContentTemplate);
        
        // 执行渲染关键帧编辑工具条
        this.renderKeyframeEditTools(_keyframeContentTemplate);
    }
    
    /*
	** @fn fxEditorRender.renderImage渲染图片模板
	** @params {objectEl} appendKeyEl插入的关键帧
	*/
    fxEditorRender.prototype.renderImage = function(filesData, appendKeyEl){
        var _keyframeFistContentTemplate = $(TEMPLATE.editImgTemplate),			// 关键帧图片容器
            _imageSize = filesData.size,	// 图片大小
            _imageSrc = filesData.url;		// 图片路径
        
        if(_imageSrc.indexOf("http") < 0){
        	_imageSrc = FXEDITOR_CONFIG.HOME_URL+_imageSrc;
        }
        
        _keyframeFistContentTemplate.attr({"data-size": _imageSize, "data-type": "image"}).html('<img class="J-main-img" src="'+_imageSrc+'">');
        appendKeyEl.append(_keyframeFistContentTemplate);
    }

    /*
	** @fn fxEditorRender.renderVideoKeyframe渲染视频关键帧
	** @params {string} keyType渲染关键帧类型
	*/
    fxEditorRender.prototype.renderVideoKeyframe = function(filesData, keyType){
    	if(this.keyCount >= 15){
        	this.renderErrorMsg(LANGUAGE.keyEditHint.max);
        	return false;
        }
        this.keyCount++;
        
        var _keyframeContentTemplate = $(TEMPLATE.keyframeContentTemplate),		// 关键帧容器模板
            _keyframeFistContentTemplate = $(TEMPLATE.editVideoTemplate),			// 关键帧视频容器
            _vedioSize = filesData.size,	// 视频大小
            _vedioSrc = filesData.url;		// 视频路径
        
        if(_vedioSrc.indexOf("http") < 0){
        	_vedioSrc = FXEDITOR_CONFIG.HOME_URL+_vedioSrc;
        }
        
        _keyframeFistContentTemplate.attr({"data-size": _vedioSize, "src": _vedioSrc});
        _keyframeContentTemplate.attr("data-type", keyType);
        _keyframeContentTemplate.find(".J-keyframe-content").append(_keyframeFistContentTemplate);
        $(".J-editor-content").append(_keyframeContentTemplate);
        
        // 执行渲染关键帧编辑工具条
        this.renderKeyframeEditTools(_keyframeContentTemplate);
    }

    /*
	** @fn fxEditorRender.renderAudioframe渲染音频关键帧
	** @params {string} keyType渲染关键帧类型
	*/
    fxEditorRender.prototype.renderAudioKeyframe = function(filesData, keyType){
    	if(this.keyCount >= 15){
        	this.renderErrorMsg(LANGUAGE.keyEditHint.max);
        	return false;
        }
        this.keyCount++;
        
        var _keyframeContentTemplate = $(TEMPLATE.keyframeContentTemplate),		// 关键帧容器模板
            _keyframeFistContentTemplate = $(TEMPLATE.editAudioTemplate),			// 关键帧音频容器
            _audioSize = filesData.size,	// 音频大小
            _audioSrc = filesData.url;		// 音频路径
        
        if(_audioSrc.indexOf("http") < 0){
        	_audioSrc = FXEDITOR_CONFIG.HOME_URL+_audioSrc;
        }
        
        _keyframeFistContentTemplate.attr({"data-size": _audioSize, "src": _audioSrc});
        _keyframeContentTemplate.attr("data-type", keyType);
        _keyframeContentTemplate.find(".J-keyframe-content").append(_keyframeFistContentTemplate);
        $(".J-editor-content").append(_keyframeContentTemplate);
        
        // 执行渲染关键帧编辑工具条
        this.renderKeyframeEditTools(_keyframeContentTemplate);
    }
    
    /*
    ** @fn fxEditorRender.renderKeyframeEdit渲染关键帧操作容器
    ** @params {string} keyType当前关键帧类型
    */
    fxEditorRender.prototype.renderKeyframeEdit = function(keyType) {
        var _keyFrameEditContentTemplate = $(TEMPLATE.keyFrameEditContentTemplate),
            _keyFrameEditDeleTemplate = $(TEMPLATE.keyFrameEditIconTemplate),
            _keyFrameEditUpTemplate = $(TEMPLATE.keyFrameEditIconTemplate),
            _keyFrameEditDownTemplate = $(TEMPLATE.keyFrameEditIconTemplate),
            _keyFrameEditParamsTemplate = $(TEMPLATE.keyFrameEditIconTemplate),
            _keyFrameEditImgTemplate = $(TEMPLATE.keyFrameEditTextTemplate),
            _keyFrameEditTextTemplate = $(TEMPLATE.keyFrameEditIconTemplate),
            _keyFrameEditChartTemplate = $(TEMPLATE.keyFrameEditIconTemplate);

        _keyFrameEditDeleTemplate.attr("data-type", "dele");
        _keyFrameEditDeleTemplate.find(".J-edit-icon").addClass("dele");
        _keyFrameEditUpTemplate.attr("data-type", "up");
        _keyFrameEditUpTemplate.find(".J-edit-icon").addClass("up");
        _keyFrameEditDownTemplate.attr("data-type", "down");
        _keyFrameEditDownTemplate.find(".J-edit-icon").addClass("down");
        _keyFrameEditParamsTemplate.attr("data-type", "params");
        _keyFrameEditParamsTemplate.find(".J-edit-icon").addClass("parm").attr("title", LANGUAGE.keyTools.params);

        switch(keyType){
            case "text":
            	_keyFrameEditImgTemplate.attr("id", "keyAddImage");
            	_keyFrameEditImgTemplate.addClass("img").attr("title", LANGUAGE.keyTools.img);
                _keyFrameEditChartTemplate.attr("data-type", "chart");
                _keyFrameEditChartTemplate.find(".J-edit-icon").addClass("chart").attr("title", LANGUAGE.keyTools.chart);
                _keyFrameEditContentTemplate.append(_keyFrameEditDeleTemplate);
                _keyFrameEditContentTemplate.append(_keyFrameEditUpTemplate);
                _keyFrameEditContentTemplate.append(_keyFrameEditDownTemplate);
                _keyFrameEditContentTemplate.append(_keyFrameEditParamsTemplate);
                _keyFrameEditContentTemplate.append(_keyFrameEditImgTemplate);
                _keyFrameEditContentTemplate.append(_keyFrameEditChartTemplate);
                break;
            case "chart":
            case "image":
                _keyFrameEditTextTemplate.attr("data-type", "text");
                _keyFrameEditTextTemplate.find(".J-edit-icon").addClass("text").attr("title", LANGUAGE.keyTools.text);
                _keyFrameEditContentTemplate.append(_keyFrameEditDeleTemplate);
                _keyFrameEditContentTemplate.append(_keyFrameEditUpTemplate);
                _keyFrameEditContentTemplate.append(_keyFrameEditDownTemplate);
                _keyFrameEditContentTemplate.append(_keyFrameEditParamsTemplate);
                _keyFrameEditContentTemplate.append(_keyFrameEditTextTemplate);
                break;
            case "audio":
                _keyFrameEditContentTemplate.append(_keyFrameEditDeleTemplate);
                _keyFrameEditContentTemplate.append(_keyFrameEditUpTemplate);
                _keyFrameEditContentTemplate.append(_keyFrameEditDownTemplate);
                break;
            case "video":
                _keyFrameEditTextTemplate.attr("data-type", "edit");
                _keyFrameEditTextTemplate.text(LANGUAGE.keyTools.edit);
                _keyFrameEditContentTemplate.append(_keyFrameEditDeleTemplate);
                _keyFrameEditContentTemplate.append(_keyFrameEditUpTemplate);
                _keyFrameEditContentTemplate.append(_keyFrameEditDownTemplate);
                _keyFrameEditContentTemplate.append(_keyFrameEditTextTemplate);
                break;
        }

        return _keyFrameEditContentTemplate;
    }
    
    // 设置预览区标题
    fxEditorRender.prototype.setPreviewTitle = function(title){
    	this.previewTitle = title;
    }
    
    // 渲染预览内容
    fxEditorRender.prototype.renderPreviewContent = function(){
    	var _keyframeContent = $(".J-keyframe-content"),
    	 	_title = this.previewTitle || "",
    	 	_widthProportion = (256/$(".J-editor-img").eq(0).width()).toFixed(2),
    	 	_addModuleEL,
    	 	_titleTemplate,
    	 	_appendHtml;
    	 
    	$(".J-preview-content").html("");
    	if(_title){
    		_titleTemplate = $("<h4 class=\"fxeditor-preview-title\">"+_title+"</h4>");
        	$(".J-preview-content").append(_titleTemplate);
    	}
    	$.each(_keyframeContent, function(key){
    		 _appendHtml = _keyframeContent.eq(key).clone();
    		 _addModuleEL = _appendHtml.find(".J-add-module");
    		 
    		 $.each(_addModuleEL, function(key2){
    			 var __addModuleELTop = _addModuleEL.eq(key2).css("top"),
	    			 __addModuleELLeft = _addModuleEL.eq(key2).css("left"),
	    			 __addModuleELWidth = _addModuleEL.eq(key2).find(".editor-resize-text").width(),
	    			 __addModuleELFontsize = _addModuleEL.eq(key2).find(".editor-resize-text").css("font-size"),
	    			 __addModuleELLineHeight = _addModuleEL.eq(key2).find(".editor-resize-text").css("line-height");
    			 
    			 __addModuleELTop = parseInt(__addModuleELTop);
    			 __addModuleELLeft = parseInt(__addModuleELLeft);
    			 __addModuleELFontsize = parseInt(__addModuleELFontsize);
    			 __addModuleELLineHeight = parseInt(__addModuleELLineHeight);
    			 
    			 _addModuleEL.eq(key2).css({
    				 "top": parseInt(__addModuleELTop*_widthProportion)+1+"px",
    				 "left": parseInt(__addModuleELLeft*_widthProportion)+1+"px"
    			 });
    			 
    			 _addModuleEL.eq(key2).find(".editor-resize-text").css({
    				 "width": parseInt(__addModuleELWidth*_widthProportion)+1+"px",
    				 "font-size": parseInt(__addModuleELFontsize*_widthProportion)+1+"px",
    				 "line-height": parseInt(__addModuleELLineHeight*_widthProportion)+1+"px"
    			 });
    		 });
    		 
    		 _appendHtml.find(".J-add-module").attr("id", "");
    		 _appendHtml.find(".J-add-module div").attr("id", "");
    		 _appendHtml.find(".J-img-edit-layer").remove();
    		 _appendHtml.find(".J-editor-img").removeClass("J-editor-img");
    		 _appendHtml.find(".J-edit-text").attr("contenteditable", false);
    		 
    		 $(".J-preview-content").append(_appendHtml.html());
    	});
    }
    
    // 渲染预览缩放内容
    fxEditorRender.prototype.renderPreviewContentScale = function(){
    	var _keyframeContent = $(".J-keyframe-content"),
    	 	_title = this.previewTitle || "",
    	 	_widthProportion = (256/$(".J-editor-img").eq(0).width()).toFixed(2),
    	 	_prevAddModuleEL,
    	 	_addModuleEL,
    	 	_titleTemplate,
    	 	_appendHtml;
    	 
    	$(".J-preview-content").html("");
    	if(_title){
    		_titleTemplate = $("<h4 class=\"fxeditor-preview-title\">"+_title+"</h4>");
        	$(".J-preview-content").append(_titleTemplate);
    	}
    	$.each(_keyframeContent, function(key){
    		 _appendHtml = _keyframeContent.eq(key).clone();
    		 _addModuleEL = _appendHtml.find(".J-add-module");
    		 _prevAddModuleEL = _keyframeContent.eq(key).find(".J-add-module");

    		 $.each(_addModuleEL, function(key2){
    			 var __addModuleELTop = _addModuleEL.eq(key2).css("top"),
	    			 __addModuleELLeft = _addModuleEL.eq(key2).css("left"),
	    			 __rotateMatrix = _prevAddModuleEL.eq(key2).css("transform").split('(')[1].split(')')[0].split(','),   // 获取旋转参数值
	    			 __rotateVal = FXImgEditor.getmatrix(__rotateMatrix[0], __rotateMatrix[1], __rotateMatrix[2], __rotateMatrix[3]);    // 旋转角度换算;
				 
				 __addModuleELTop = parseInt(__addModuleELTop);
				 __addModuleELLeft = parseInt(__addModuleELLeft);
				 
    			 _addModuleEL.eq(key2).css({
    				 "top": parseInt(__addModuleELTop*_widthProportion)+1+"px",
    				 "left": parseInt(__addModuleELLeft*_widthProportion)+1+"px",
    				 "transform-origin": "center top",
    				 "-ms-transform-origin": "center top",
    				 "-moz-transform-origin": "center top",
    				 "-webkit-transform-origin": "center top",
    				 "-o-transform-origin": "center top",
    				 "transform": "rotate("+__rotateVal+"deg) scale("+_widthProportion+")",
	    			 "-ms-transform": "rotate("+__rotateVal+"deg) scale("+_widthProportion+")",
	    			 "-moz-transform": "rotate("+__rotateVal+"deg) scale("+_widthProportion+")",
	    			 "-webkit-transform": "rotate("+__rotateVal+"deg) scale("+_widthProportion+")",
	    			 "-o-transform": "rotate("+__rotateVal+"deg) scale("+_widthProportion+")"
    			 });
    		 });
    		 
    		 _appendHtml.find(".J-add-module").attr("id", "");
    		 _appendHtml.find(".J-add-module div").attr("id", "");
    		 _appendHtml.find(".J-img-edit-layer").remove();
    		 _appendHtml.find(".J-editor-img").removeClass("J-editor-img");
    		 _appendHtml.find(".J-edit-text").attr("contenteditable", false);
    		 
    		 $(".J-preview-content").append(_appendHtml.html());
    	});
    }
    
    /*
    ** @fn fxEditorRender.renderErrorMsg渲染错误提示信息
    ** @params {string} errorMsg错误信息
    */ 
    fxEditorRender.prototype.renderErrorMsg = function(errorMsg, time){
    	var _self = this,
    		_time = time || 2000,
    		_timer,
    		_errorMsgTemplate = $(TEMPLATE.errorHintLayerTemplate),
    		_appendEl = $("#"+this.editorAppendEl+"").find(".fxeditor-page");
    	
    	if("" != errorMsg){
    		_errorMsgTemplate.find("p").text(errorMsg);
    		_appendEl.append(_errorMsgTemplate);
    		clearTimeout(_timer);
    		_timer = setTimeout(function(){
    			_appendEl.find(".J-editor-error").remove();
    		}, _time);
    	}else{
    		_appendEl.find(".J-editor-error").remove();
    	}
    }

    /*
    ** @fn fxEditorRender.keyframeUp上移关键帧
    ** @params {DOMObject} keyframeEl关键帧对象
    */
    fxEditorRender.prototype.keyframeUp = function(keyframeEl) {
        var _currentIndex = keyframeEl.index(),
            _currentEl = keyframeEl;

        if(_currentIndex != 0){
            keyframeEl.remove();
            $(".J-keyframe").eq(_currentIndex-1).before(_currentEl);
        }else{
        	this.renderErrorMsg(LANGUAGE.keyEditHint.top);
        }
    }

    /*
    ** @fn fxEditorRender.keyframeDown下移关键帧
    ** @params {DOMObject} keyframeEl关键帧对象
    */
    fxEditorRender.prototype.keyframeDown = function(keyframeEl) {
        
        var _keyframeMaxLength = $(".J-keyframe").length,
            _currentIndex = keyframeEl.index(),
            _currentEl = keyframeEl;

        if(_currentIndex != _keyframeMaxLength-1){
            keyframeEl.remove();
            $(".J-keyframe").eq(_currentIndex).after(_currentEl);
        }else{
        	this.renderErrorMsg(LANGUAGE.keyEditHint.bottom);
        }
    }
    
    /*
    ** @fn fxEditorRender.renderParamsContent渲染参数容器
    ** @params {DOMObject} keyframeEl关键帧对象
    */
    fxEditorRender.prototype.renderParamsContent = function() {
         var _editParamsPopLayerTemplate = $(TEMPLATE.editParamsPopLayerTemplate),
         	_checkEl;
         
         for(var i = 1; i < 9; i++){
        	 _checkEl = $('<li class="check-box-li"><input id="params-'+i+'" value="{#'+LANGUAGE.paramsText.desc+''+i+'#}" type="checkbox"><label for="params-'+i+'">'+LANGUAGE.paramsText.desc+''+i+'</label></li>');
        	 _editParamsPopLayerTemplate.find(".J-check-box").append(_checkEl);
         }
         
         $("#"+this.editorAppendEl+"").append(_editParamsPopLayerTemplate);
    }
    
    /*
    ** @fn fxEditorRender.renderParamsText渲染参数描述
    ** @params {string} val插入文本
    */
    fxEditorRender.prototype.renderParamsText = function(val) {
    	var _cursorSelectIndex = $(this.cursorSelectEl).closest(".J-keyframe").index(),
    		_paramsAppendElIndex = this.paramsAppendEl.closest(".J-keyframe").index();
    	
    	if(_paramsAppendElIndex < 0){
    		this.renderErrorMsg(LANGUAGE.paramsText.hint);
    		return false;
    	}
    	
    	 if(_cursorSelectIndex === _paramsAppendElIndex && $(this.cursorSelectEl).closest(".J-keyframe-content").length > 0){
         	this.range.collapse(false);                       
 	        this.range.insertNode(document.createTextNode(val));
 	        this.selection.removeAllRanges();
 	        this.selection.addRange(this.range);
    	}else{
    		this.paramsAppendEl.text(this.paramsAppendEl.text()+val);
    	}
    }
    
    /*
    ** @fn fxEditorRender.getInitCursor获取光标位置
    */
    fxEditorRender.prototype.getInitCursor = function(){
    	this.selection = window.getSelection ? window.getSelection() : document.selection;
    	this.range = this.selection.createRange ? this.selection.createRange() : this.selection.getRangeAt(0);
    	this.cursorSelectEl = this.range.startContainer;
    }
    
    /*
    ** @fn fxEditorRender.keyframeInsertText插入描述文本
    ** @params {DOMObject} keyframeEl关键帧对象
    */
    fxEditorRender.prototype.keyframeInsertText = function(keyframeEl){
    	var _currentEl = keyframeEl,
    		_editTextTemplate = $(TEMPLATE.editTextTemplate),
    		_editTextLength = keyframeEl.find(".J-keyframe-content").find(".J-edit-text").length;
	
    	if(_editTextLength > 0){
    		this.renderErrorMsg(LANGUAGE.textMax);
    	}else{
    		_editTextTemplate.attr("contenteditable", true);
        	keyframeEl.find(".J-keyframe-content").append(_editTextTemplate);
        	_editTextTemplate.focus();
    	}
    }
    
    /*
    ** @fn fxEditorRender.keyframeImageHover关键帧图片操作
    ** @params {DOMObject} imageEl图片对象
    */
    fxEditorRender.prototype.keyframeImageHover = function(imageEl){
     	var _currentEl = imageEl,
     		_imgEditTemplate = $(TEMPLATE.imgEditTemplate);
 	
     	_currentEl.append(_imgEditTemplate);
    }
    
    /*
    ** @fn fxEditorRender.renderEditorLoad渲染编辑器加载页
    ** @params {string} laodVal加载描述，有描述显示加载，无描述移除加载
    */
    fxEditorRender.prototype.renderEditorLoad = function(laodVal){
    	var _loadTemplate = $(TEMPLATE.editorLoadLayer),
    		_appendEl = $("#"+this.editorAppendEl+"").find(".fxeditor-page");
    	
    	if(laodVal){
    		_appendEl.append(_loadTemplate);
    	}else{
    		$("#fxeditor-laod").remove();
    	}
    }
    
    // 显示图表关键帧编辑层
    fxEditorRender.prototype.showChartEidtLayer = function(){
    	// 判断是否可以显示图表编辑层
		if(this.currentEditChartType === "addKey" && this.keyCount >= 15){
			this.renderErrorMsg(LANGUAGE.keyEditHint.max);
        	return false;
		}
		if(this.currentEditChartType === "add"){
			var _keyHasImgEl = this.currentEditChartKeyFrameEl.find(".J-editor-img");
			if(_keyHasImgEl.length > 0){
				this.renderErrorMsg(LANGUAGE.keyEditHint.min);
				return false;
			}
		}
		// 不要在这个js里面找这个方法，这个是图表编辑器弹出层的调用，至于在哪不要问我，我也不知道，人少任性
        isShowChart();
    }
    
    // 渲染图表操作，这个方法的执行不在这个js里面，这个是给图表调用的，在哪里调用我也不知道····，要问我为什么这么骚操作，人少任性
    fxEditorRender.prototype.renderChartKeyFrame = function(chartData, keyType){
    	var _self = this,
    		_chartData,
    		_chartDataStr;
    	
    	// 判断调用执行给的参数是json字符串还是json数据
    	if(typeof (chartData) === "string"){
    		_chartDataStr = chartData;
    		_chartData = JSON.parse(chartData);
    	}else{
    		_chartData = chartData;
    		_chartDataStr = JSON.stringify(chartData);
    	}
    	
    	// 判断图表编辑类型 ，addKey新增一个图表关键帧，add文字帧内新增一个图表，edit表示需要编辑的图表
    	switch (this.currentEditChartType){
	    	case "addKey":
	    		_self.addChartKeyFrame(_chartData, _chartDataStr, keyType);
	    		break;
	    	case "add":
	    		_self.keyFrameAddChartImg(_chartData, _chartDataStr, keyType);
	    		break;
	    	case "edit":
	    		_self.keyFrameEditChartImg(_chartData, _chartDataStr);
	    		break;
    	}
    }
    
    // 新增图表关键帧
    fxEditorRender.prototype.addChartKeyFrame = function(chartData, _chartDataStr, keyType){    	
        this.keyCount++; 
        var _keyframeContentTemplate = $(TEMPLATE.keyframeContentTemplate),		// 关键帧容器模板
            _keyframeImgTemplate = $(TEMPLATE.editImgTemplate),			// 关键帧图片容器
            _imageSize = chartData.pictureSize,		// 图片大小
            _imageSrc = chartData.pictureUrl;		// 图片路径
        
        if(_imageSrc.indexOf("http") < 0){
        	_imageSrc = FXEDITOR_CONFIG.HOME_URL+_imageSrc;
        }
        
        // 为什么要保存json数据到隐藏dom结构，因为数据里面没有唯一id标示，我想保存到变量里面也不敢，怕最后谁也不认识谁
        _keyframeImgTemplate.attr({"data-size": _imageSize, "data-type": keyType})
        							.html('<img src="'+_imageSrc+'"><div class="J-chart-data" style="display:none;">'+_chartDataStr+'</div>');
        _keyframeContentTemplate.attr("data-type", keyType);
        _keyframeContentTemplate.find(".J-keyframe-content").append(_keyframeImgTemplate);
        $(".J-editor-content").append(_keyframeContentTemplate);
        
        // 执行渲染关键帧编辑工具条
        this.renderKeyframeEditTools(_keyframeContentTemplate);
    }
    
    // 帧内新增图表图片
    fxEditorRender.prototype.keyFrameAddChartImg = function(chartData, _chartDataStr, keyType){
    	var _keyframeImgTemplate = $(TEMPLATE.editImgTemplate),			// 关键帧图片容器
	        _imageSize = chartData.pictureSize,		// 图片大小
	        _imageSrc = chartData.pictureUrl;		// 图片路径
    	
    	if(_imageSrc.indexOf("http") < 0){
        	_imageSrc = FXEDITOR_CONFIG.HOME_URL+_imageSrc;
        }
	    
    	 // 为什么要保存json数据到隐藏dom结构，因为数据里面没有唯一id标示，我想保存到变量里面也不敢，怕最后谁也不认识谁
        _keyframeImgTemplate.attr({"data-size": _imageSize, "data-type": keyType})
        							.html('<img src="'+_imageSrc+'"><div class="J-chart-data" style="display:none;">'+_chartDataStr+'</div>');
	    this.currentEditChartKeyFrameEl.find(".J-keyframe-content").append(_keyframeImgTemplate);
    }
    
    // 帧内图表图片编辑
    fxEditorRender.prototype.keyFrameEditChartImg = function(chartData, _chartDataStr){
    	var _imgParentEL = this.currentEditChartKeyFrameEl.find(".J-editor-img"),
    		_imgEl = _imgParentEL.find("img"),
    		_chartDataEl = _imgParentEL.find(".J-chart-data"),
    		_imageSize = chartData.pictureSize,		// 图片大小
	        _imageSrc = chartData.pictureUrl;		// 图片路径
    	
    	if(_imageSrc.indexOf("http") < 0){
        	_imageSrc = FXEDITOR_CONFIG.HOME_URL+_imageSrc;
        }
    	
    	_imgParentEL.data("size",_imageSize);
    	_imgEl.attr("src", _imageSrc);
    	_chartDataEl.text(_chartDataStr);
    }

    function debouce(func,delay,immediate){
        var timer = null;
        return function(){
            var context = this;
            var args = arguments;
            if(timer) clearTimeout(timer);
            if(immediate){
                //根据距离上次触发操作的时间是否到达delay来决定是否要现在执行函数
                var doNow = !timer;
                //每一次都重新设置timer，就是要保证每一次执行的至少delay秒后才可以执行
                timer = setTimeout(function(){
                    timer = null;
                },delay);
                //立即执行
                if(doNow){
                    func.apply(context,args);
                }
            }else{
                timer = setTimeout(function(){
                    func.apply(context,args);
                },delay);
            }
        }
    }
    
    // 整个图片编辑器对象
    var FXImgEditor = {
	        createModuleCountNum: 0,            // 新创建模板统计
	        currentEditModuleELId: "",          // 当前编辑模块节点ID
	        selection: null,                    // 光标select对象
	        range: null,                        // 光标range对象
	        cursorSelectEl: "",                 // 光标选中节点
	        imgContainerMaxWidth: 538,			// 图片显示区最大宽度
	        imgContainerMaxHeight: 594,			// 图片显示区最大高度
	        imgCropperObj: null,                // 图片裁剪器对象
	        ImgCropperCurrentEditImgSrc: "",	// 图片裁剪器当前编辑图片初始地址
	        ImgCropperCurrentEditImgSize: 0,
	        imgCropperReplaceImgSrc: "",		// 图片裁剪器替换后的图片
	        imgCropperReplaceImgSize: 0,
	        currentDelModuleElId: "",           // 当前可删除模块节点
	        clickTimes: 0,                      // 文本，图片，拖拽层点击时保存的时间戳，为了计算双击时间间隔
	        _textToolsTplObj: null,             // 文本编辑工具栏模板对象
	        // 文字模板默认创建配置
	        fontModuleDefaultOpts: {
	            "fontFamily": "黑体",
	            "fontSize": "18px",
	            "lineHeight": "18px",
	            "fontWeight": "400",
	            "fontStyle": "normal",
	            "textDecoration": "none",
	            "textAlign": "left",
	            "color": "#555555",
	            "backgroundColor": "transparent",
	            "width": "180px",
	            "rotate": 0
        },
        /*
        ** @fn init初始化
        ** @params {json} opts初始化参数
        ** @params {elemtn} editImgEl编辑图片
        */
        init: function(editImgEl){
        	this.initKeyFrameEditImgEl = editImgEl;					// 编辑的帧图片元素
        	this.initImgSize = editImgEl.attr("data-size");  // 初始图片大小，来源调用图片编辑器传入
            this.initImgSrc = editImgEl.find(".J-main-img").attr("src");    // 初始图片地址，来源调用图片编辑器传入
            this.initHtmlNode = editImgEl.find(".J-module-html");    // 初始图片编辑层附加的html结构,记录图片上的文字和小图

            // 如果没有初始图片地址，退出方法调用
            if(!this.initImgSrc){
                console.log("COULD NOT FIND FILE");
                return false;
            }
            // 执行编辑器主结构渲染
            this.renderImgEditorMainStructure();
        },

        // DOM事件绑定
        bindUI: function(){
            var _self = this;
            
            // 显示背景图片裁剪
            $("#img-editor-layer").on("click", ".J-bg-btn", function(){
            	var _imgSrc = $(".J-bg-img").attr("src"),
            		_imgSize = $(".J-bg-img").attr("data-size");
            	
            	$(".J-text-tools").addClass("editor-hidden");
            	$(".J-main-tool").removeClass("active");
            	$(".J-imgEdit-tools").removeClass("editor-hidden");
            	
            	_self.ImgCropperCurrentEditImgSrc = _imgSrc;
            	_self.imgCropperReplaceImgSrc = _imgSrc;
            	_self.ImgCropperCurrentEditImgSize = _imgSize;
            	_self.imgCropperReplaceImgSize = _imgSize;
            	_self.imgCropperObj.cropper('replace', _imgSrc);
            });
            
            // 裁剪器事件绑定
            $("#img-editor-layer").on("click", ".J-cropper-edit", function(){
            	_self.listenImageCropperEdit($(this).data());
            });
            
            // 取消裁剪器内图片编辑
            $("#img-editor-layer").on("click", ".J-cropper-cancel", function(){
            	_self.imgCropperReplaceImgSrc = "";
            	_self.imgCropperReplaceImgSize = 0;
            	$(".J-cropper-size").text(""+LANGUAGE.imgEdit.imgArea.size+"："+(_self.ImgCropperCurrentEditImgSize/1024).toFixed(2)+"KB")
            	_self.imgCropperObj.cropper('replace', _self.ImgCropperCurrentEditImgSrc);
            });

            // 让拖拽层失去焦点
            $(document).on("click", function(e){
                var _event = e || window.event,
                    _eventTarget = _event.target,
                    _pullSelectEl = $(_eventTarget).closest(".J-pull-select"),
                    _dragEl = $(_eventTarget).closest(".J-drag-box"),
                    _colorPickerEL = $(_eventTarget).closest(".colorpicker"),
                    _clipToolsEl = $(_eventTarget).closest(".J-clip-tools");

                // 点击在非拖拽层和子工具栏中,且当前要有选中拖动层
                if(_dragEl.length <= 0 && _clipToolsEl.length <= 0 && _colorPickerEL.length <= 0){
                    // 执行文字模块编辑后显示和样式继承
                    _self.isTextDragModuleClone();
                    // 隐藏拖拽编辑层
                    _self.isToggleDrag(false, false);
                    // 当前选中模块
                    _self.currentEditModuleELId = "";
                    // 当前可删除模块id
                    _self.currentDelModuleElId = "";
                    // 当前获取光标元素
                    _self.cursorSelectEl = "";
                    // 文本工具栏设置默认参数
                    _self._textToolsTplObj.showToolsDefaultVal(_self.fontModuleDefaultOpts);
                }

                // 下拉层隐藏
                if(_pullSelectEl.length <= 0){
                    $(".J-pull-select").find(".J-select-list").hide();
                }
            });

            // 文本或图片模板获得拖动框聚焦
            $("#img-editor-layer").on("click", ".J-add-module", function(e){
                var _event = e || window.event,
                    _elId = $(this).attr("id");
                
                $(".J-text-tools").removeClass("editor-hidden");
            	$(".J-main-tool[data-type='text']").addClass("active");
            	$(".J-imgEdit-tools").addClass("editor-hidden");

                // 保存当前时间到时间戳
                _self.clickTimes = new Date().getTime();
                // 执行文字模块编辑后显示和样式继承
                _self.isTextDragModuleClone();
                // 当前模块
                _self.currentEditModuleELId = _elId;
                // 当前可删除模块id
                _self.currentDelModuleElId = _elId;
                // 执行当前模板样式获取,用于对文字或图片编辑工具栏反选
                _self.getCurrentModuleStyle();
                // 为当前选中模板显示拖拽层
                _self.isToggleDrag(true, false);
                // 阻止事件冒泡
                _event.stopPropagation();
            });

            // 拖动层点击事件绑定
            $("#img-editor-layer").on("click", ".J-drag-box", function(){
                var _times = new Date().getTime();
                
                // 判断是否为双击
                if((_times - _self.clickTimes) < 300){
                    // 当前可删除模块id
                    _self.currentDelModuleElId = "";
                    // 执行拖拽层双击显示
                    _self.showDragDoubleClickResult($(this));
                }
                // 保存当前时间到时间戳
                _self.clickTimes = new Date().getTime();
            });
            
            // 获取插入参数插入点
            $("#img-editor-layer").on("click", "#resize-text-holder", function(){
            	_self.getInitCursor();
            }).on("DOMSubtreeModified", "#resize-text-holder", debouce(function(){
            	var _containerHeight = $(".J-main-container").height(),
            		_containerTop = $(".J-main-container").offset().top,
            		_editElHeight,
            		_editElTop = $(".J-drag-box").offset().top;
            	
            	_self.getInitCursor();

            	$(".J-drag-box").find(".editor-resize-text").height("auto");
            	_editElHeight = $(".J-drag-box").height();
            	if(_editElHeight >= (_containerHeight - (_editElTop - _containerTop))){
            		$(".J-drag-box").find(".editor-resize-text").height(_containerHeight - (_editElTop - _containerTop)).css("overflow", "hidden");
            	}
            }, 10, true));
            
            $(document).keydown(function(event){
            	var _event = event || window.event,
            		_target = _event.target;
            	if(_target.getAttribute("class") === "editor-resize-text"){
                	_self.getInitCursor();
            	}
            });

            // 删除模板层
            $(document).keydown(function(event){
                var _event = event || window.event,
                    _keyCode = _event.keyCode,
                    _delEl = $("#"+_self.currentDelModuleElId+"");

                if(_keyCode == 46 && _delEl.length > 0){
                    _delEl.remove();
                    _self.currentDelModuleElId = "";
                    _self.currentEditModuleELId = "";
                    // 执行拖拽层失去焦点操作
                    $(".J-drag-box").addClass("editor-hidden");
                }
            });

            // 下拉选择框操作
            $("#img-editor-layer").on("click", ".J-pull-select", function(){
                // 执行下拉展示
                _self.showPullSelect($(this));
            });

            // 文字字体、大小，行高选择
            $("#img-editor-layer").on("click", ".J-list-li", function(event){
                var _event = event || window.event,
                    _selectVal = $(this).data("val"),
                    _selectType = $(this).closest(".J-pull-select").data("type");

                $(this).parent(".J-select-list").hide();
                $(this).parent(".J-select-list").siblings(".J-select-val").text(_selectVal);

                if(_selectType === "fontSize"){
                    $(".J-font-height").siblings(".J-select-val").text(_selectVal);
                }

                // 执行文字样式操作设置
                _self.showTextFontStyleSet(_selectType, _selectVal);
                // 阻止事件冒泡
                _event.stopPropagation();
            }).on("click", ".J-fontsize-edit", function(event){
                var _event = event || window.event,
                    _elType = $(this).data("type"),
                    _selectVal = $(".J-font-size").siblings(".J-select-val").text(),
                    _showVal;

                if(_elType === "add"){
                    _showVal = (parseInt(_selectVal)+2)+"px";
                }else if(_elType === "reduce"){
                    if((parseInt(_selectVal)-2) <= 12){
                        _showVal = "12px";
                    }else{
                        _showVal = (parseInt(_selectVal)-2)+"px";
                    }
                }
                $(".J-font-size").siblings(".J-select-val").text(_showVal);
                $(".J-font-height").siblings(".J-select-val").text(_showVal);
                // 执行文字样式操作设置
                _self.showTextFontStyleSet("fontSize", _showVal);
                // 阻止事件冒泡
                _event.stopPropagation();
            });

            // 文字样式选择
            $("#img-editor-layer").on("click", ".J-font-style", function(event){
                var _event = event || window.event,
                    _type = $(this).data("type"),
                    _selectVal;

                $(this).toggleClass("active");

                if(_type === "weight" && $(this).hasClass("active")){
                    _selectVal = "700";
                }else if(_type === "weight" && !$(this).hasClass("active")){
                    _selectVal = "400";
                }else if(_type === "italic" && $(this).hasClass("active")){
                    _selectVal = "italic";
                }else if(_type === "italic" && !$(this).hasClass("active")){
                    _selectVal = "normal";
                }else if(_type === "line" && $(this).hasClass("active")){
                    _selectVal = "underline";
                }else if(_type === "line" && !$(this).hasClass("active")){
                    _selectVal = "none";
                }
                // 执行文字样式操作设置
                _self.showTextFontStyleSet(_type, _selectVal);
                // 阻止事件冒泡
                _event.stopPropagation();
            });

            // 文字对齐方式切换
            $("#img-editor-layer").on("click", ".J-align-style", function(event){
                var _event = event || window.event,
                    _selectType = $(this).data("type");

                $(this).addClass("active").siblings(".J-align-style").removeClass("active");

                // 执行文字样式操作设置
                _self.showTextFontStyleSet(_selectType, _selectType);
                // 阻止事件冒泡
                _event.stopPropagation();
            });

            // 文字格式清除
            $("#img-editor-layer").on("click", ".J-font-default", function(event){
                var _event = event || window.event;
                // 执行文字样式清除
                _self.clearTextModuleStyle();
                // 阻止事件冒泡
                _event.stopPropagation();
            });

            // 文字颜色选择切换
            $("#img-editor-layer").on("click", ".J-color-table", function(){
                $(this).addClass("active").siblings().removeClass("active");
            });

            // 文字颜色选择
            $("#img-editor-layer").on("click", ".J-color-select", function(event){
                var _event = event || window.event,
                    _selectVal = $(this).data("val"),
                    _selectType = $(".J-color-table.active").data("type");

                // 执行文字样式操作设置
                _self.showTextFontStyleSet(_selectType, _selectVal);
                // 阻止事件冒泡
                _event.stopPropagation();
            });

            // 输入框文字处理
            $("#img-editor-layer").on("focus input propertychange", ".J-font-input", function(){
                // 执行文字输入处理结果显示
                _self.showFontInputResult($(this));
            }).on("blur", ".J-font-input", function(){
                var _type = $(this).data("type"),
                    _val = parseInt($(this).val());

                switch (_type){
                    case "width":
                        $(this).val(_val+"px");
                        break;
                    case "rotate":
                        $(this).val(_val+"°");
                        break;
                }
            });

            // 文字块宽度处理
            $("#img-editor-layer").on("click", ".J-width-edit", function(){
                // 执行文字区块宽度编辑结果显示
                _self.showFontModuleWidth($(this));
            });

            // 参数操作
            $("#img-editor-layer").on("click", ".J-params-edit", function(){
                var _type = $(this).data("type");
                // 判断是添加参数还是确认插入参数
                if(_type === "add"){
                    _self.isAddFontParams();
                }else if(_type === "confirm"){
                    _self.isAppendFontParams();
                    _self.isRestParams();
                }
            });
        },

        // 插入参数
        isAppendFontParams: function(){
            var _currentEditModuleELId = this.currentEditModuleELId,
                _dragType = $(".J-drag-box").attr("data-type"),
                _checkedEl = $(".J-font-params").find("input:checked"),
                _checkeStr = "";
            
            if(!_currentEditModuleELId ||
            	_currentEditModuleELId.split("_")[0] !== "TEXT" ||
            	_dragType === "drag" ||
            	$(this.cursorSelectEl).closest(".J-drag-box").length <= 0){
            	this.renderErrorMsg(LANGUAGE.imgEdit.textArea.insetPH,"");
                return;
            }

            $.each(_checkedEl, function(key){
        		_checkeStr += _checkedEl.eq(key).val();
        	});
            
	    	this.range.collapse(false);
	        this.range.insertNode(document.createTextNode(_checkeStr));
	        this.selection.removeAllRanges();
	        this.selection.addRange(this.range);
            $("#"+_currentEditModuleELId+"_inner").text($("#DRAG_"+_currentEditModuleELId+"").text());
        },

        // 显示文字字体设置
        showTextFontStyleSet: function(type, val){
            var _dragElType = $(".J-drag-box").attr("data-type"),
                _currentEditModuleELId = this.currentEditModuleELId,
                _fontStyleType = type,
                _fontStyleVal = val,
                _currentEditELType;
            // 让当前没有选中模块
            if(!_currentEditModuleELId){
                return;
            }
            _currentEditELType = _currentEditModuleELId.split("_")[0];
            // 当前不是文本模块
            if(_currentEditELType !== "TEXT"){
                return;
            }

            switch (_fontStyleType){
                case "lineHeight":
                    $("#"+_currentEditModuleELId+"_inner").css("line-height", _fontStyleVal);
                    $("#DRAG_"+_currentEditModuleELId+"").css("line-height", _fontStyleVal);
                    this.resetDragElHeight();
                    break;
                case "left":
                    $("#"+_currentEditModuleELId+"_inner").css("text-align", _fontStyleVal);
                    $("#DRAG_"+_currentEditModuleELId+"").css("text-align", _fontStyleVal);
                    break;
                case "center":
                    $("#"+_currentEditModuleELId+"_inner").css("text-align", _fontStyleVal);
                    $("#DRAG_"+_currentEditModuleELId+"").css("text-align", _fontStyleVal);
                    break;
                case "right":
                    $("#"+_currentEditModuleELId+"_inner").css("text-align", _fontStyleVal);
                    $("#DRAG_"+_currentEditModuleELId+"").css("text-align", _fontStyleVal);
                    break;
                case "width":
                    $("#"+_currentEditModuleELId+"_inner").width(_fontStyleVal);
                    $("#DRAG_"+_currentEditModuleELId+"").width(_fontStyleVal);
                    break;
                case "rotate":
                    $("#"+_currentEditModuleELId+"").css("transform", " rotate("+_fontStyleVal+"deg)");
                    $(".J-drag-box").css("transform", " rotate("+_fontStyleVal+"deg)");
                    break;
            }

            // 是否处于编辑状态
            if(_dragElType === "drag"){
                switch (_fontStyleType){
                    case "fontFamily":
                        $("#"+_currentEditModuleELId+"_inner").css("font-family", _fontStyleVal);
                        this.resetDragElHeight();
                        break;
                    case "fontSize":
                        $("#"+_currentEditModuleELId+"_inner").css({"font-size": _fontStyleVal, "line-height": _fontStyleVal});
                        this.resetDragElHeight();
                        break;
                    case "weight":
                        $("#"+_currentEditModuleELId+"_inner").css("font-weight", _fontStyleVal);
                        this.resetDragElHeight();
                        break;
                    case "italic":
                        $("#"+_currentEditModuleELId+"_inner").css("font-style", _fontStyleVal);
                        break;
                    case "line":
                        $("#"+_currentEditModuleELId+"_inner").css("text-decoration", _fontStyleVal);
                        break;
                    case "font":
                        $("#"+_currentEditModuleELId+"_inner").css("color", _fontStyleVal);
                        break;
                    case "bg":
                        $("#"+_currentEditModuleELId+"_inner").css("background-color", _fontStyleVal);
                        break;
                }
            }else if(_dragElType === "edit"){
                switch (_fontStyleType){
                    case "fontFamily":
                        $("#"+_currentEditModuleELId+"_inner").css("font-family", _fontStyleVal);
                        $("#DRAG_"+_currentEditModuleELId+"").css("font-family", _fontStyleVal);
                        this.resetDragElHeight();
                        break;
                    case "fontSize":
                        $("#"+_currentEditModuleELId+"_inner").css({"font-size": _fontStyleVal, "line-height": _fontStyleVal});
                        $("#DRAG_"+_currentEditModuleELId+"").css({"font-size": _fontStyleVal, "line-height": _fontStyleVal});
                        this.resetDragElHeight();
                        break;
                    case "weight":
                        $("#"+_currentEditModuleELId+"_inner").css("font-weight", _fontStyleVal);
                        $("#DRAG_"+_currentEditModuleELId+"").css("font-weight", _fontStyleVal);
                        this.resetDragElHeight();
                        break;
                    case "italic":
                        $("#"+_currentEditModuleELId+"_inner").css("font-style", _fontStyleVal);
                        $("#DRAG_"+_currentEditModuleELId+"").css("font-style", _fontStyleVal);
                        break;
                    case "line":
                        $("#"+_currentEditModuleELId+"_inner").css("text-decoration", _fontStyleVal);
                        $("#DRAG_"+_currentEditModuleELId+"").css("text-decoration", _fontStyleVal);
                        break;
                    case "font":
                        $("#"+_currentEditModuleELId+"_inner").css("color", _fontStyleVal);
                        $("#DRAG_"+_currentEditModuleELId+"").css("color", _fontStyleVal);
                        break;
                    case "bg":
                        $("#"+_currentEditModuleELId+"_inner").css("background-color", _fontStyleVal);
                        $("#DRAG_"+_currentEditModuleELId+"").css("background-color", _fontStyleVal);
                        break;
                }
            }
        },

        // 重置拖拽框高度
        resetDragElHeight: function(){
            var _currentEditModuleELId = this.currentEditModuleELId,
                _dragElType = $(".J-drag-box").attr("data-type"),
                _editModuleHeight;
            if(_dragElType === "drag"){
                _editModuleHeight = $("#"+_currentEditModuleELId+"").height();
            }else if(_dragElType === "edit"){
                _editModuleHeight = $("#DRAG_"+_currentEditModuleELId+"").height();
            }
            $(".J-drag-box").css("height", _editModuleHeight);
        },

        // 清除文字样式设置
        clearTextModuleStyle: function(){
            var _dragElType = $(".J-drag-box").attr("data-type"),
                _currentEditModuleELId = this.currentEditModuleELId,
                _currentEditModuleText,     // 当前编辑的文字
                _currentEditElWidth,
                _textValObj,
                _currentEditELType;
            // 让当前没有选中模块
            if(!_currentEditModuleELId){
                return;
            }
            _currentEditELType = _currentEditModuleELId.split("_")[0];
            // 当前不是文本模块
            if(_currentEditELType !== "TEXT"){
                return;
            }

            if(_dragElType === "drag"){
                _currentEditModuleText = $("#"+_currentEditModuleELId+"_inner").text();
                $("#"+_currentEditModuleELId+"_inner").css({
                    "font-family": this.fontModuleDefaultOpts.fontFamily,
                    "font-size": this.fontModuleDefaultOpts.fontSize,
                    "line-height": this.fontModuleDefaultOpts.lineHeight,
                    "font-weight": this.fontModuleDefaultOpts.fontWeight,
                    "font-style": this.fontModuleDefaultOpts.fontStyle,
                    "text-decoration": this.fontModuleDefaultOpts.textDecoration,
                    "text-align": this.fontModuleDefaultOpts.textAlign,
                    "color": this.fontModuleDefaultOpts.color,
                    "background-color": this.fontModuleDefaultOpts.backgroundColor
                }).html(_currentEditModuleText);
                _currentEditElWidth = $("#"+_currentEditModuleELId+"_inner").width();
            }else if(_dragElType === "edit"){
                _currentEditModuleText = $("#DRAG_"+_currentEditModuleELId+"").text();
                $("#DRAG_"+_currentEditModuleELId+"").css({
                    "font-family": this.fontModuleDefaultOpts.fontFamily,
                    "font-size": this.fontModuleDefaultOpts.fontSize,
                    "line-height": this.fontModuleDefaultOpts.lineHeight,
                    "font-weight": this.fontModuleDefaultOpts.fontWeight,
                    "font-style": this.fontModuleDefaultOpts.fontStyle,
                    "text-decoration": this.fontModuleDefaultOpts.textDecoration,
                    "text-align": this.fontModuleDefaultOpts.textAlign,
                    "color": this.fontModuleDefaultOpts.color,
                    "background-color": this.fontModuleDefaultOpts.backgroundColor
                }).html(_currentEditModuleText);

                _currentEditElWidth = $("#DRAG_"+_currentEditModuleELId+"").width();
            };

            _textValObj = {
                "fontFamily": this.fontModuleDefaultOpts.fontFamily,
                "fontSize": this.fontModuleDefaultOpts.fontSize,
                "lineHeight": this.fontModuleDefaultOpts.lineHeight,
                "fontWeight": this.fontModuleDefaultOpts.fontWeight,
                "fontStyle": this.fontModuleDefaultOpts.fontStyle,
                "textDecoration": this.fontModuleDefaultOpts.textDecoration,
                "textAlign": this.fontModuleDefaultOpts.textAlign,
                "width": _currentEditElWidth,
                "rotate": 0
            };

            this._textToolsTplObj.showToolsDefaultVal(_textValObj);
            this.resetDragElHeight();
        },

        // 获取光标位置
        getInitCursor: function(){
            this.selection = window.getSelection ? window.getSelection() : document.selection;
        	this.range = this.selection.createRange ? this.selection.createRange() : this.selection.getRangeAt(0);
            this.cursorSelectEl = this.range.startContainer;
        },

        // 获取当前聚焦模板样式
        getCurrentModuleStyle: function(){
            var _moduleType = this.currentDelModuleElId.split("_")[0];

            if(_moduleType === "TEXT"){
                // 执行当前文字模板样式获取
                this.getCurrentTextModuleStyle();
                // 执行文字工具参数复原
                this.isRestParams();
            }else if(_moduleType === "IMG"){
                // 图片模板后期迭代
            }
        },

        // 获取当前文本模板样式属性
        getCurrentTextModuleStyle: function(){
            var _currentEl = $("#"+this.currentEditModuleELId+""),
                _currentInnerEL = $("#"+this.currentEditModuleELId+"_inner"),
                _fontFamilyVal = _currentInnerEL.css("font-family"),
                _fontSizeVal = _currentInnerEL.css("font-size"),
                _lineHeightVal = _currentInnerEL.css("line-height"),
                _fontWeightVal = _currentInnerEL.css("font-weight"),
                _fontStyleVal = _currentInnerEL.css("font-style"),
                _textDecorationVal = _currentInnerEL.css("text-decoration"),
                _textAlignVal = _currentInnerEL.css("text-align"),
                _widthVal = _currentInnerEL.css("width"),
                _rotateMatrix = _currentEl.css("transform").split('(')[1].split(')')[0].split(','),
                _rotateVal = this.getmatrix(_rotateMatrix[0], _rotateMatrix[1], _rotateMatrix[2], _rotateMatrix[3]),
                _textValObj = {
                    "fontFamily": _fontFamilyVal,
                    "fontSize": _fontSizeVal,
                    "lineHeight": _lineHeightVal,
                    "fontWeight": _fontWeightVal,
                    "fontStyle": _fontStyleVal,
                    "textDecoration": _textDecorationVal,
                    "textAlign": _textAlignVal,
                    "width": _widthVal,
                    "rotate": _rotateVal
                };

            this._textToolsTplObj.showToolsDefaultVal(_textValObj);
        },

        // 新增参数
        isAddFontParams: function(){
            var _paramsEl = $(".J-font-params"),
                _paramsListEl = _paramsEl.find(".font-params-list"),
                _paramsLiEl = _paramsListEl.eq(0).find(".prams-list-li"),
                _paramsLiNum = _paramsLiEl.length,
                _listNum = _paramsListEl.length+1,
                _singleParamsNum,
                _paramsTplStr = "",
                _paramsTpl;

            for(var i = 0; i < _paramsLiNum; i++){
                if(_listNum <= 1){
                    _singleParamsNum = i+1;
                }else{
                    _singleParamsNum = (i+1)+((_listNum-1)*_paramsLiNum);
                }

                _paramsTplStr += '<p class="prams-list-li editor-fl"><input id="font-params-'+_singleParamsNum+'" value="{#'+LANGUAGE.imgEdit.textArea.imgParams+''+_singleParamsNum+'#}"  type="checkbox"><label for="font-params-'+_singleParamsNum+'">'+LANGUAGE.imgEdit.textArea.imgParams+''+_singleParamsNum+'</label></p>';
            }
            _paramsTpl = $('<div class="font-params-list editor-clear J-addParams-list">'+_paramsTplStr+'</div>');
            _paramsEl.find(".J-font-params-edit").before(_paramsTpl);
        },

        // 参数复原
        isRestParams: function(){
            $(".J-font-params").find(".J-addParams-list").remove();
            $(".J-font-params").find("input").attr("checked", false);
        },

        /*
        ** @fn showFontModuleWidth显文字区块宽度
        ** @params {element} editEl编辑区块宽度元素节点
        */
        showFontModuleWidth: function(eidtEL){
            var _intVal = eidtEL.parent().siblings("input").val(),
                _maxVal = $(".J-main-container").width(),
                _showVal = parseInt(_intVal) || 0,
                _eidtType = eidtEL.data("type");

            if(_eidtType === "add"){
                _showVal++;
                if(_showVal >= _maxVal){
                    _showVal = _maxVal;
                }
            }else if(_eidtType === "reduce"){
                _showVal--;
                if(_showVal <= 0){
                    _showVal = 0;
                }
            }
            $("#"+this.currentEditModuleELId+"_inner").width(_showVal);
            $("#DRAG_"+this.currentEditModuleELId+"").width(_showVal);
            $(".J-drag-box").width(_showVal);
            // this.resetDragElHeight();
            eidtEL.parent().siblings("input").val(_showVal+"px");
        },

        /*
        ** @fn showFontInputResult显示文字编辑区输入框输入处理
        ** @params {element} inputEl输入元素节点
        */
        showFontInputResult: function(inputEl){
            var _inputVal = inputEl.val(),
                _inputType = inputEl.data("type"),  // 表单类型
                _rotateMax = 360,
                _widthMax = $(".J-main-container").width(),
                _maxInputVal,       // 最大输入值
                _showVal = parseInt(_inputVal) || 0;   // 输入框值只能为整数

            // 如果是旋转角度，最大值不能超过360，宽度不能超过容器宽度
            if(_inputType === "width"){
                _maxInputVal = _widthMax;
            }else if(_inputType === "rotate"){
                _maxInputVal = _rotateMax;
            }
            // 值范围控制
            if(_showVal <= 0){
                _showVal = 0;
            }else if(_showVal >= _maxInputVal){
                _showVal = _maxInputVal;
            }

            if(_inputType === "width"){
                $("#"+this.currentEditModuleELId+"_inner").width(_showVal);
                $("#DRAG_"+this.currentEditModuleELId+"").width(_showVal);
                $(".J-drag-box").width(_showVal);
            }else if(_inputType === "rotate"){
                $("#"+this.currentEditModuleELId+"").css({"transform":"rotate("+_showVal+"deg)"});
                $(".J-drag-box").css({"transform":"rotate("+_showVal+"deg)"});
                EditorSlider.setSliderBarInitOffset(inputEl);
            }
            this.resetDragElHeight();
            inputEl.val(_showVal);
        },

        /*
        ** @fn showPullSelect展示下拉选择
        ** @params {element} pullEl下拉选元素
        */
        showPullSelect: function(pullEl){
            var _currentVal = pullEl.find(".J-select-val").text(),
                _selectList = pullEl.find(".J-select-list"),
                _selectLi = pullEl.find(".J-list-li");

            _selectLi.removeClass("active");
            _selectList.find(".J-list-li[data-val='"+_currentVal+"']").addClass("active");
            $(".J-pull-select").find(".J-select-list").hide();
            _selectList.toggle();
        },

        /*
        ** @fn showDragDoubleClickResult拖拽层双击显示
        ** @params {element} el拖拽元素
        */
        showDragDoubleClickResult: function(el){
            var _textTpl,
                _elStatus = el.attr("data-type"),
                _currentEditModuleELId = this.currentEditModuleELId,
                _dragType = _currentEditModuleELId.split("_")[0];

            if(_dragType === "TEXT"){
                if(_elStatus === "drag"){
                    $(".J-drag-box").attr("data-type", "edit").css({"width": "auto","height": "auto"});
                    $("#"+_currentEditModuleELId+"_inner").addClass("editor-hidden");
                    _textTpl =$($("#"+_currentEditModuleELId+"").html());
                    _textTpl.removeClass("editor-hidden").attr({
                        "id": "DRAG_"+_currentEditModuleELId+"",
                        "contenteditable": true});
                    $("#resize-text-holder").html("").removeClass("editor-hidden").append(_textTpl);
                }
                // 执行文本全选
                this.isTextSelectAll("DRAG_"+_currentEditModuleELId+"");
            }else if(_dragType === "IMG"){

            }
        },

        // 文字模块编辑后显示和样式继承
        isTextDragModuleClone: function(){
            var _currentDragModuleEl,           // 当前处于拖动的模块
                _newTextModuleInnerDom,         //  文本克隆内容
                _dragEl = $(".J-drag-box"),     // 拖动模块节点
                _dragModuleType,                //  拖动模块的类型
                _dragElType = _dragEl.attr("data-type"),    // 拖动层类型
                _currentEditModuleELId = this.currentEditModuleELId;    // 当前处于拖拽或编辑层模板id

            // 如果当前没有有模块处于拖拽或编辑状态，则返回
            if(!_currentEditModuleELId){
                return;
            }
            _currentDragModuleEl =  $("#"+_currentEditModuleELId+"");
            _dragModuleType = _currentEditModuleELId.split("_")[0];

            // 如果是文字拖拽且处于编辑状态层则要当前编辑的文字层显示出来，并继承拖拽层中文本编辑样式
            if(_dragModuleType === "TEXT" && _dragElType === "edit"){
                _newTextModuleInnerDom = $("#DRAG_"+_currentEditModuleELId+"").clone();

                // 清除复制节点的编辑属性和更改复制节点ID
                _newTextModuleInnerDom.removeAttr("contenteditable").attr("id",""+_currentEditModuleELId+"_inner");
                 // 更新当前编辑的文本模板
                _currentDragModuleEl.html("").append(_newTextModuleInnerDom);
            }
        },

        // 渲染编辑器主结构
        renderImgEditorMainStructure: function(){
            var _self = this,
            	_imgEditorBgTemplate = $(IMGEDITORTEMPLATE.imgEditorBgTPL),                  // 图片编辑器背景模板
                _imgEditorContTemplate = $(IMGEDITORTEMPLATE.imgEditorContTPL),              // 图片编辑器主容器模板
                _imgEditorContHdTemplate = $(IMGEDITORTEMPLATE.imgEditorContHdTPL),          // 图片编辑主容器头部模板
                _imgEditorContainerTemplate = $(IMGEDITORTEMPLATE.imgEditorContainerTPL),    // 编辑器拖拽显示容器模板
                _moduleDragTemplate = $(IMGEDITORTEMPLATE.imgEditorDragTPL),                 // 模块拖拽模板
                _toolsAppendEl = undefined;                                         // 详细工具插入节点

            // 拖拽模板插入到主容器中
            _imgEditorContainerTemplate.find(".J-main-container").append(_moduleDragTemplate);
            // 主容器头部和显示容器插入主容器中
            _imgEditorContTemplate.append(_imgEditorContHdTemplate);
            _imgEditorContTemplate.append(_imgEditorContainerTemplate);
            // 整个图片编辑器插入body中
            $("body").append(_imgEditorBgTemplate);
            $("body").append(_imgEditorContTemplate);
            _toolsAppendEl = _imgEditorContainerTemplate.find(".J-clip-tools");
            // 文本编辑器工具栏渲染
            this._textToolsTplObj = new TextModuleEditTools();
            // 文字工具栏初始化，传入工具栏插入节点，和文字默认参数
            this._textToolsTplObj.init(_toolsAppendEl, this.fontModuleDefaultOpts);
            // 执行编辑器显示区渲染
            this.renderEditorShowArea();
            // 渲染图片裁剪器
            this.renderImgCropper();
            // 对拖拽模块进行事件绑定
            _moduleDragTemplate.Drag(FXImgEditor.isDragCB);
            // 拉伸点事件绑定
            this.isDragEventBind();
            // 执行主工具栏事件绑定
            this.mainToolsBindEvents();
            // 执行事件绑定
            this.bindUI();
            
            // 取消图片编辑
            $(".J-imgeditor-cancel").on("click", function(){
            	$("#img-editor-bg,#img-editor-layer,.colorpicker").remove();
            	 // 当前选中模块
                _self.currentEditModuleELId = "";
                // 当前可删除模块id
                _self.currentDelModuleElId = "";
                // 当前裁剪图片初始地址清空
                _self.ImgCropperCurrentEditImgSrc = "";
                _self.imgCropperReplaceImgSrc = "";
                _self.ImgCropperCurrentEditImgSize = 0;
                _self.imgCropperReplaceImgSize = 0;
            });
            
            // 保存图片
            $(".J-imgeditor-save").on("click", function(){
            	_self.isSaveEditorImg();
            });
        },
        
        // 拖动点时间绑定
        isDragEventBind: function(){
            var self = this,
                dragVal = false,                                // 拖动锁定
                dragEl = $(".J-drag-box"),                      // 拖动元素
                targetEl = $(".J-drag-pointer"),                // 拖动容器
                dragContainerEL = $(".J-main-container"),       // 拉伸点
                targetType,             // 拉伸点类型
                dragElWidth,            // 拖动层宽度
                dragElHeight,           // 拖动层高度
                dragElLeft,             // 拖动层左边距
                dragElTop,              // 通道层顶边距
                dragContainerELWidth,   // 容器宽度
                dragContainerELHeight,  // 容器高度
                dragContainerELLeft,    // 容器左边距
                dragContainerELTop,     // 容器顶边距
                dragElProportion,       // 拖拽层宽高比
                dragHolderEl,           // 拖拽占位元素
                holderElOffsetLeft,    // 占位层左边距
                holderElOffsetTop,     // 占位层顶边距
                holderElWidth,         // 占位层宽
                holderElHeight,        // 占位层高度
                startX,                 // 鼠标起点clientX位置
                startY;                 // 鼠标起点clientY位置

            // 拖拽点事件绑定
            targetEl.on("mousedown", function(event){
                var _event = event || window.event;

                dragVal = true;
                startX = _event.clientX;
                startY = _event.clientY;
                targetType = $(this).data("type");
                dragElWidth = dragEl.outerWidth();
                dragElHeight = dragEl.outerHeight();
                dragElLeft = dragEl.offset().left;
                dragElTop = dragEl.offset().top;
                dragContainerELWidth = dragContainerEL.outerWidth();
                dragContainerELHeight = dragContainerEL.outerHeight();
                dragContainerELLeft = dragContainerEL.offset().left;
                dragContainerELTop = dragContainerEL.offset().top;
                dragElProportion = parseInt(dragElWidth/dragElHeight);

                // 阻止默认事件
                _event.preventDefault();
                // 阻止事件冒泡
                _event.stopPropagation();
            });

            // 拖拽点移动
            $(document).on("mousemove", function(event){
                var _event = event || window.event,
                    _endX = _event.clientX,     // 鼠标移动点clientX位置
                    _endY = _event.clientY;     // 鼠标移动点clientY位置

                // 判断是否为拖动点移动
                if(!dragVal){
                    return;
                }
                // 创建拉伸占位层
                if(!dragHolderEl){
                    dragHolderEl = createMoveTemplate(dragEl);
                    dragContainerEL.append(dragHolderEl);
                }

                // 判断拖拽类型
                switch (targetType){
                    case "t-l":
                        var __moveX = _endX-startX,
                            __offsetLeft = dragElLeft + __moveX;

                        (__moveX <= 0) ? __moveX = Math.abs(__moveX): __moveX = -__moveX;

                        holderElWidth = dragElWidth + __moveX;
                        holderElHeight = parseInt(holderElWidth/dragElProportion);
                        holderElOffsetLeft = __offsetLeft;
                        holderElOffsetTop = (dragElTop + dragElHeight) - holderElHeight;

                        // 判断是否拉伸到达左边界
                        if(holderElWidth >= (dragElWidth + (dragElLeft - dragContainerELLeft))){
                            holderElWidth = dragElWidth + (dragElLeft - dragContainerELLeft);
                            holderElHeight = parseInt(holderElWidth/dragElProportion);
                            holderElOffsetLeft = dragContainerELLeft;
                            holderElOffsetTop = (dragElTop + dragElHeight) - holderElHeight;
                        }
                        break;
                    case "t-c":
                        var __addHeight = _endY - startY,
                            __offsetTop = dragElTop + __addHeight;

                        (__addHeight <= 0) ? __addHeight = Math.abs(__addHeight): __addHeight = - __addHeight;

                        holderElWidth = dragElWidth;
                        holderElHeight = dragElHeight + __addHeight;
                        holderElOffsetLeft = dragElLeft;
                        holderElOffsetTop = __offsetTop;

                        // 判断是否拉伸到达上边界
                        if(holderElHeight >= ((dragElTop - dragContainerELTop) + dragElHeight)){
                            holderElHeight = (dragElTop - dragContainerELTop) + dragElHeight;
                            holderElOffsetTop = dragContainerELTop;
                        }
                        break;
                    case "t-r":
                        var __moveX = _endX - startX;

                        holderElWidth = dragElWidth + __moveX;
                        holderElHeight = parseInt(holderElWidth/dragElProportion);
                        holderElOffsetLeft = dragElLeft;
                        holderElOffsetTop = (dragElTop + dragElHeight) - holderElHeight;

                        // 判断是否拉伸到达右边界
                        if(holderElWidth >= (dragContainerELWidth - (dragElLeft - dragContainerELLeft))){
                            holderElWidth = dragContainerELWidth - (dragElLeft - dragContainerELLeft);
                            holderElHeight = parseInt(holderElWidth/dragElProportion);
                            holderElOffsetLeft = dragElLeft;
                            holderElOffsetTop = (dragElTop + dragElHeight) - holderElHeight;
                        }
                        break;
                    case "c-l":
                        var __addWidth = _endX - startX,
                            __offsetLeft = dragElLeft + __addWidth;

                        (__addWidth <= 0) ? __addWidth = Math.abs(__addWidth): __addWidth = -__addWidth;

                        holderElOffsetLeft = __offsetLeft;
                        holderElOffsetTop = dragElTop;
                        holderElWidth = dragElWidth + __addWidth;
                        holderElHeight = dragElHeight;

                        // 拉伸左边界限定
                        if(holderElWidth >= (dragElWidth + (dragElLeft - dragContainerELLeft))){
                            holderElWidth = dragElWidth + (dragElLeft - dragContainerELLeft);
                            holderElOffsetLeft = dragContainerELLeft;
                        }
                        break;
                    case "c-r":
                        var __addWidth = _endX - startX;

                        holderElOffsetLeft = dragElLeft;
                        holderElOffsetTop = dragElTop;
                        holderElWidth = dragElWidth + __addWidth;
                        holderElHeight = dragElHeight;

                        // 拉伸右边界限定
                        if(holderElWidth >= (dragContainerELWidth - (dragElLeft - dragContainerELLeft))){
                            holderElWidth = dragContainerELWidth - (dragElLeft - dragContainerELLeft);
                        }
                        break;
                    case "b-l":
                        var __moveX = _endX - startX,
                            __offsetLeft = dragElLeft + __moveX;

                        (__moveX <= 0) ? __moveX = Math.abs(__moveX): __moveX = -__moveX;

                        holderElOffsetLeft = __offsetLeft;
                        holderElOffsetTop = dragElTop;
                        holderElWidth = dragElWidth + __moveX;
                        holderElHeight = holderElWidth/dragElProportion;

                        // 拉伸左边界限定
                        if(holderElWidth >= (dragElWidth + (dragElLeft - dragContainerELLeft))){
                            holderElWidth = dragElWidth + (dragElLeft - dragContainerELLeft);
                            holderElHeight = parseInt(holderElWidth/dragElProportion);
                            holderElOffsetLeft = dragContainerELLeft;
                        }
                        break;
                    case "b-c":
                        var __addHeight = _endY - startY;

                        holderElOffsetLeft = dragElLeft;
                        holderElOffsetTop = dragElTop;
                        holderElWidth = dragElWidth;
                        holderElHeight = dragElHeight + __addHeight;

                        // 拉伸下边界限定
                        if(holderElHeight >= (dragContainerELHeight - (dragElTop - dragContainerELTop))){
                            holderElHeight = dragContainerELHeight - (dragElTop - dragContainerELTop);
                        }
                        break;
                    case "b-r":
                        var __moveX = _endX - startX;

                        holderElOffsetLeft = dragElLeft;
                        holderElOffsetTop = dragElTop;
                        holderElWidth = dragElWidth + __moveX;
                        holderElHeight = (dragElWidth + __moveX)/dragElProportion;

                        // 判断是否拉伸到达右边界
                        if(holderElWidth >= (dragContainerELWidth - (dragElLeft - dragContainerELLeft))){
                            holderElWidth = dragContainerELWidth - (dragElLeft - dragContainerELLeft);
                            holderElHeight = parseInt(holderElWidth/dragElProportion);
                        }
                        break;
                }

                // 实时显示拖动变化
                dragHolderEl.offset({"top": holderElOffsetTop, "left": holderElOffsetLeft});
                dragHolderEl.height(holderElHeight).width(holderElWidth);

                // 阻止默认事件
                _event.preventDefault();
                _event.stopPropagation();
            });

            // 拖拽结束
            $(document).on("mouseup", function(){
                var _currentEditModuleTyoe = self.currentEditModuleELId.split("_")[0];
                dragVal = false;

                if(dragHolderEl){
                    dragHolderEl.remove();
                    dragHolderEl = "";
                    $("#"+self.currentEditModuleELId+"").offset({"left": holderElOffsetLeft, "top":holderElOffsetTop});
                    // 实时计算移动位置距离
                    dragEl.offset({"left": holderElOffsetLeft, "top":holderElOffsetTop});
                    // 文本拉伸不改变高度，只改变宽度
                    if(_currentEditModuleTyoe === "TEXT"){
                        dragEl.outerWidth(holderElWidth);
                        $("#"+self.currentEditModuleELId+"").width(holderElWidth);
                        $(".J-font-input[data-type='width']").val(holderElWidth).trigger("focus").trigger("blur");
                        // 执行文字模块编辑后显示和样式继承
                        self.isTextDragModuleClone();
                        // 为当前选中模板显示拖拽层
                        self.isToggleDrag(true, false);
                    }
                }
            });
        },
        
        // 图片保存
        isSaveEditorImg: function(){
        	var _editImgWidth = $(".J-bg-img").width(),
        		_imgSrc = $(".J-bg-img").attr("src"),
        		_imgSize = $(".J-bg-img").attr("data-size"),
        		_imgWidth = $(".J-bg-img").width(),
        		_imgHieght = $(".J-bg-img").height(),
	        	_keyFrameImgWidth = this.initKeyFrameEditImgEl.width(),
	        	_widthProportion = _keyFrameImgWidth/_editImgWidth,
	        	_newAddModule = $("<div class='J-module-html'></div>"),
	        	_addModuleEl = $("#img-editor-layer").find(".J-add-module"),
	        	_dragElType = $(".J-drag-box").attr("data-type"),
	        	_addModuleWidth,
	        	_addModuleTop,
	        	_addModuleLeft,
	        	_addModuleFontSize,
	        	_addModuleLineHeight,
	        	_cloneModuleEL,
	        	_cloneModuleId;
        	
        	$.each(_addModuleEl, function(key){
            	_cloneModuleId = _addModuleEl.eq(key).attr("id");
        		_addModuleWidth = _addModuleEl.eq(key).find("#"+_cloneModuleId+"_inner").width();
            	_addModuleTop = _addModuleEl.eq(key).css("top");
            	_addModuleLeft = _addModuleEl.eq(key).css("left");
            	_addModuleFontSize = _addModuleEl.eq(key).find("#"+_cloneModuleId+"_inner").css("font-size");
            	_addModuleLineHeight = _addModuleEl.eq(key).find("#"+_cloneModuleId+"_inner").css("line-height");
            	_cloneModuleEL = _addModuleEl.eq(key).clone();
            	
            	_addModuleTop = parseInt(_addModuleTop);
            	_addModuleLeft = parseInt(_addModuleLeft);
            	_addModuleFontSize = parseInt(_addModuleFontSize);
            	_addModuleLineHeight = parseInt(_addModuleLineHeight);
            	
            	_cloneModuleEL.css({
            		"top": parseInt(_addModuleTop*_widthProportion)+"px",
            		"left": parseInt(_addModuleLeft*_widthProportion)+"px"
            	}).attr("id", _cloneModuleId+"_show");
            	_cloneModuleEL.find("#"+_cloneModuleId+"_inner").css({
            		"width": parseInt(_addModuleWidth*_widthProportion)+"px",
            		"font-size": parseInt(_addModuleFontSize*_widthProportion)+"px",
            		"line-height": parseInt(_addModuleLineHeight*_widthProportion)+"px"
            	}).attr("id", _cloneModuleId+"_inner_show");
            	_cloneModuleEL.attr({
            		"data-val": "top:"+_addModuleTop+";left:"+_addModuleLeft+";width:"+_addModuleWidth+";line-height:"+_addModuleLineHeight+";font-size:"+_addModuleFontSize+";"
            	});
            	_newAddModule.append(_cloneModuleEL);
        	});
        	
        	if(_dragElType === "edit"){
        		var _dragTextId = $(".J-drag-box").find(".editor-resize-text").attr("id"),
        			_dragTextHtml = $("#"+_dragTextId+"").html(),
        			_changeTextModuleId = _dragTextId.split("DRAG_")[1];
        		
        		_newAddModule.find("#"+_changeTextModuleId+"_inner_show").html(_dragTextHtml);
        	}
        	_newAddModule.find(".editor-hidden").removeClass("editor-hidden");
        	this.initKeyFrameEditImgEl.find(".J-module-html").remove();
        	this.initKeyFrameEditImgEl.append(_newAddModule);
        	this.initKeyFrameEditImgEl.attr("data-size", _imgSize);
        	this.initKeyFrameEditImgEl.find(".J-main-img").attr("src", _imgSrc).attr("data-val","width:"+_imgWidth+";height:"+_imgHieght+"");
        	$(".J-imgeditor-cancel").trigger("click");
        },

        // 渲染图片裁剪器
        renderImgCropper: function(){
            var _self = this,
            	_imgCropperTemplate = $(IMGEDITORTEMPLATE.imgEditorImgToolsTPL),
                _cropContainer = _imgCropperTemplate.find(".J-crop-container"),
                _inputAccept = {
        		    title: 'Images',
        		    extensions: FXEDITOR_CONFIG.imageConfig.format.join(","),
        		    mimeTypes: "image/*"
        		},
            	_uploadUrl = FXEDITOR_CONFIG.HOME_URL+FXEDITOR_CONFIG.imageConfig.uploadUrl,
            	_uploadFileMaxSize = FXEDITOR_CONFIG.imageConfig.size,
            	_imgSizeText = (this.initImgSize/1024).toFixed(2)+"KB";

            _cropContainer.html("<img id=\"cropper-container\" src=\""+this.initImgSrc+"\"/>");
            _imgCropperTemplate.find(".J-cropper-size").text(""+LANGUAGE.imgEdit.imgArea.size+"："+_imgSizeText);
            $(".J-clip-tools").append(_imgCropperTemplate);
            this.ImgCropperCurrentEditImgSrc = this.initImgSrc;
            this.ImgCropperCurrentEditImgSize = this.initImgSize;
            this.imgCropperReplaceImgSrc = this.initImgSrc;
            this.imgCropperReplaceImgSize = this.initImgSize;
        	this.imgCropperObj = $("#cropper-container").cropper({
        		autoCropArea: 1
        	});
        	
        	// 图片裁剪器下拉选择事件绑定
        	$(".J-ratio-show").on("click", function(){
        		$(".J-ratio-list").toggle();
        	});
        	// 图片分辨率选择
        	$(".J-ratio-li").on("click", function(){
        		$(this).addClass("active").siblings().removeClass("active");
        		$(".J-ratio-show").attr("data-val", $(this).attr("data-val")).text($(this).text());
        		$(".J-ratio-list").toggle();
        	});
        	
        	// 初始化文件上传插件
        	FxEditor.fxEditorRenderObj.fileUpload(_uploadUrl, '#cropper-replace-img', _uploadFileMaxSize, _inputAccept, function(file){}, function(file, response){
            	if(response.state === "SUCCESS"){
                    var _imageSize =( response.size/1024).toFixed(2)+"KB",		// 图片大小
                   		_imageSrc = response.url;		// 图片路径

	                if(_imageSrc.indexOf("http") < 0){
	                	_imageSrc = FXEDITOR_CONFIG.HOME_URL+_imageSrc;
	                }
	                
                    _self.imgCropperReplaceImgSrc = _imageSrc;
                    _self.imgCropperReplaceImgSize = _imageSize;
                    $(".J-cropper-size").text(""+LANGUAGE.imgEdit.imgArea.size+"："+_imageSize);
	                _self.imgCropperObj.cropper('replace', _imageSrc);
            	}else{
            		_self.renderErrorMsg(LANGUAGE.fileUpHint.fail,"");
            	}
            }, function(type){
            	switch (type) {
                	case "Q_EXCEED_SIZE_LIMIT":
                	case "F_EXCEED_SIZE":
                		_self.renderErrorMsg(LANGUAGE.imgHint.max,"");
                		break;
                	case "Q_TYPE_DENIED":
                		_self.renderErrorMsg(LANGUAGE.fileUpHint.type,"");
                		break;
            	}
            });
        },
        
        /*
        ** @fn listenImageCropperEdit监听图片裁剪器操作
        ** @params {string} editData操作方法data属性
        */
        listenImageCropperEdit: function(editData){
         	var _self = this;
         	
     		if(editData.method){
     			// 执行图片裁剪器
     			_self.imgCropperObj.cropper(editData.method, editData.option);
     			
     			if (editData.method === 'getCroppedCanvas') {
    				if(_self.imgCropperObj.cropper("getCropBoxData").left === undefined){
    					_self.renderErrorMsg(LANGUAGE.imgHint.cutArea);
    					return false;
    				}
    				
    				var _url = FXEDITOR_CONFIG.HOME_URL+FXEDITOR_CONFIG.imageConfig.tailorUrl,
    					_ratio = $(".J-ratio-show").attr("data-val"),
    					_imgSrc = _self.imgCropperReplaceImgSrc,
    					_imgData = _self.imgCropperObj.cropper(editData.method, editData.option).toDataURL(),
    					_params = {
    	    				"clarity": _ratio,
    	    				"src": _imgSrc,
    	    				"data": _imgData
    	    			};
    				
    				_self.renderEditorLoad(true);
    				
    				// 请求数据
    		        $.ajax({
    		            url: _url,		// 请求地址
    		            type: "POST",		// 方法
    		            data: _params,		// 数据
    		            dataType: "JSON",	// 数据类型
    		            success: function(rst) {
    		            	if(rst.state == 0){
        						var _newImgSize = rst.size,
        							_newImgSrc = rst.path;
        						
        						if(_newImgSrc.indexOf("http") < 0){
        							_newImgSrc = FXEDITOR_CONFIG.HOME_URL+_newImgSrc;
        						}        						
        						// 替换已有图片尺寸和路径        		            	
        		            	_self.ImgCropperCurrentEditImgSrc = _newImgSrc;
        		            	_self.imgCropperReplaceImgSrc = _newImgSrc;
        		            	_self.ImgCropperCurrentEditImgSize = _newImgSize;
        		            	_self.imgCropperReplaceImgSize = _newImgSize;
        		            	_self.imgCropperObj.cropper('replace', _newImgSrc);
        		            	$(".J-bg-img").attr({"src": _newImgSrc, "data-size": _newImgSize});
        		            	$(".J-cropper-size").text(""+LANGUAGE.imgEdit.imgArea.size+"："+(_newImgSize/1024).toFixed(2)+"KB");
        		            	
        		            	// 延时执行，确认新图片地址赋值成功
        	    		    	var _timer = setTimeout(function(){
        	    		    		var _mianBgImgTpl = $("<img src=\""+_newImgSrc+"\">"),
		        		            	_imgNaturalSize = _self.getImgNaturalSize(_mianBgImgTpl),
		        		            	_setImgWidth = _imgNaturalSize.width,
		        		            	_setImgHeight = _imgNaturalSize.height,
		        		            	_showAreaHeight = $(".J-main-container").closest(".img-show-cont").height(),
		        		            	_setImgMarginTop = (_showAreaHeight-_setImgHeight)/2;
		        		            	
	        		            	$(".J-main-container").parent(".show-box").css({
	        		            		"width": _setImgWidth + "px",
	        		            		"height": _setImgHeight + "px",
	        		            		"margin-top": _setImgMarginTop-20 + "px"
	        		            	});	        		            	
	        	    		    	_self.renderErrorMsg(LANGUAGE.imgHint.cutSuccess);
	        	    		    	
									clearTimeout(_timer);
								}, 200);
        					}else{
        	    		    	_self.renderErrorMsg(LANGUAGE.imgHint.cutFail);
        					}
        					_self.renderEditorLoad(false);
    		            },
    		            error: function(XMLHttpRequest, textStatus) {
    		            	_self.renderEditorLoad(false);
        			    	_self.renderErrorMsg(LANGUAGE.imgHint.cutFail);
    		            }
    		        });
    	        }
     		}
        },

        // 拖拽完成回调
        isDragCB: function(X,Y){
            $("#"+FXImgEditor.currentEditModuleELId+"").offset({"left": X, "top":Y});
        },

        // 主工具栏事件绑定
        mainToolsBindEvents: function(){
            var _self = this,
                _event,
                _toggleElType,                  // 当前执行事件工具类型
                _bindEl = $(".J-main-tool");    // 需要绑定事件的工具节点

            _bindEl.on("click", function(e){
                _event = e || window.event;
                _toggleElType = $(this).data("type");
                $(".J-imgEdit-tools").addClass("editor-hidden");
                // 执行文字模块编辑后显示和样式继承
                _self.isTextDragModuleClone();
                // 根据主工具栏类型判断插入模板类型和工具栏显示类型
                if(_toggleElType === "text"){
                	// 当前裁剪图片初始地址清空
                    _self.ImgCropperCurrentEditImgSrc = "";
                    _self.imgCropperReplaceImgSrc = "";
                    // 展示文本工具栏
                    $(this).addClass("active");
                    $(".J-text-tools").removeClass("editor-hidden");
                    // 重置参数列表
                    _self.isRestParams();
                    // 执行文本展示模块创建
                    _self.createTextShowModule();
                }else if(_toggleElType === "img"){
                    // 小图模板后期迭代
                }
                // 阻止事件冒泡
                e.stopPropagation();
            });
        },

        // 渲染编辑器显示区
        renderEditorShowArea: function(){
            var _proportion = (this.imgContainerMaxWidth/this.imgContainerMaxHeight).toFixed(2),
            	_keyFrameImgEl = this.initKeyFrameEditImgEl.find(".J-main-img"),
            	_widthProportion,
            	_keyFrameProportion = (_keyFrameImgEl.width()/_keyFrameImgEl.height()).toFixed(2),
            	_addModuleEL = this.initHtmlNode.find(".J-add-module"),
            	_cloneAddModuleEL,
            	_mianBgImgTpl = $("<img class=\"J-bg-img\" data-size=\""+this.initImgSize+"\" src=\""+this.initImgSrc+"\">"),
            	_imgNaturalSize = this.getImgNaturalSize(_mianBgImgTpl),
            	_showAreaHeight = $(".J-main-container").closest(".img-show-cont").height(),
            	_setImgWidth,
            	_setImgHeight,
            	_setImgMarginTop;
            
            if(_keyFrameProportion >= _proportion){
            	
            	if(_imgNaturalSize.width > this.imgContainerMaxWidth){
            		_setImgWidth = this.imgContainerMaxWidth;
            	}else{
            		_setImgWidth = _imgNaturalSize.width;
            	}
            	_setImgHeight = _setImgWidth/(_imgNaturalSize.width/_imgNaturalSize.height);
            	_setImgMarginTop = (_showAreaHeight-_setImgHeight)/2;
            	
            	$(".J-main-container").parent(".show-box").css({
            		"width": _setImgWidth + "px",
            		"height": _setImgHeight + "px",
            		"margin-top": _setImgMarginTop-20 + "px"
            	});
            	_mianBgImgTpl.css({
            		"width": "100%"
            	});
            }else{            	
            	if(_imgNaturalSize.height > this.imgContainerMaxHeight){
            		_setImgHeight = this.imgContainerMaxHeight;
            	}else{
            		_setImgHeight = _imgNaturalSize.height;
            	}
            	_setImgWidth = _setImgHeight*(_imgNaturalSize.width/_imgNaturalSize.height);
            	_setImgMarginTop = (_showAreaHeight-_setImgHeight)/2;
            	
            	$(".J-main-container").parent(".show-box").css({
            		"height": _setImgHeight+"px",
            		"width": _setImgWidth+"px",
            		"margin-top": _setImgMarginTop-20 + "px"
            	});
            	_mianBgImgTpl.css({
            		"width": "100%"
            	});
            }
            
            _widthProportion = (_setImgWidth/_keyFrameImgEl.width()).toFixed(2)
            
            $.each(_addModuleEL, function(key){
            	var _addModuleId = _addModuleEL.eq(key).attr("id").split("_show")[0],
            		_addModuleTop = _addModuleEL.eq(key).css("top"),
	            	_addModuleLft = _addModuleEL.eq(key).css("left"),
	            	_addModuleWidth = _addModuleEL.eq(key).find(".editor-resize-text").width(),
	            	_addModuleFontsize = _addModuleEL.eq(key).find(".editor-resize-text").css("font-size"),
	            	_addModuleLineHeight = _addModuleEL.eq(key).find(".editor-resize-text").css("line-height");
            	
            	_addModuleTop = parseInt(_addModuleTop);
            	_addModuleLft = parseInt(_addModuleLft);
            	_addModuleFontsize = parseInt(_addModuleFontsize);
            	_addModuleLineHeight = parseInt(_addModuleLineHeight);
            	_cloneAddModuleEL = _addModuleEL.eq(key).clone();
            	_cloneAddModuleEL.css({
            		"top": parseInt(_addModuleTop*_widthProportion)+1+"px",
            		"left": parseInt(_addModuleLft*_widthProportion)+1+"px"
            	}).attr("id", _addModuleId);
            	_cloneAddModuleEL.find(".editor-resize-text").css({
            		"width": parseInt(_addModuleWidth*_widthProportion)+1+"px",
            		"font-size": parseInt(_addModuleFontsize*_widthProportion)+1+"px",
            		"line-height": parseInt(_addModuleLineHeight*_widthProportion)+1+"px"
            	}).attr("id", _addModuleId+"_inner");
            	
                $(".J-main-container").append(_cloneAddModuleEL);
            });
            
            $(".J-main-container").append(_mianBgImgTpl);
        },

        // 创建文本展示模块
        createTextShowModule: function(){
            var _textContTpl = $("<div id=\"TEXT_MODULE_"+this.createModuleCountNum+"\" class=\"J-add-module\"></div>"),
                _textInnerTpl = $("<div id=\"TEXT_MODULE_"+this.createModuleCountNum+"_inner\" class=\"editor-resize-text editor-hidden\"></div>");

            _textContTpl.css({
                "position": "absolute",
                "top": 20+(this.createModuleCountNum*10)+"px",
                "left": 20+(this.createModuleCountNum*10)+"px",
                "z-index": "39",
                "transform-origin": "center center",
                "cursor": "normal",
                "transform": "rotate("+this.fontModuleDefaultOpts.rotate+"deg)"
            });
            _textInnerTpl.html(LANGUAGE.imgEdit.textArea.placeholder).css({
                "font-family": this.fontModuleDefaultOpts.fontFamily,
                "font-size": this.fontModuleDefaultOpts.fontSize,
                "line-height": this.fontModuleDefaultOpts.lineHeight,
                "font-weight": this.fontModuleDefaultOpts.fontWeight,
                "font-style": this.fontModuleDefaultOpts.fontStyle,
                "text-decoration": this.fontModuleDefaultOpts.textDecoration,
                "text-align": this.fontModuleDefaultOpts.textAlign,
                "color": this.fontModuleDefaultOpts.color,
                "background-color": this.fontModuleDefaultOpts.backgroundColor,
                "width": this.fontModuleDefaultOpts.width
            });

            this.currentEditModuleELId = "TEXT_MODULE_"+this.createModuleCountNum+"";
            this.createModuleCountNum++;
            _textContTpl.append(_textInnerTpl);
            $(".J-main-container").append(_textContTpl);
            // 显示拖拽层
            this.isToggleDrag(true, true);
        },

        /*
        ** @fn isToggleDrag拖拽层显示和隐藏
        ** @params {Boolean} boolean是否需要显示拖拽层
        ** @params {Boolean} editVal拖拽层内文本模块是否需要编辑
        */
        isToggleDrag: function(boolean, editVal){
            var _currentEditModuleELId = this.currentEditModuleELId,        // 当前置于拖拽或编辑状态的模块ID
                _currentEditEl,
                _dragTop,
                _dragLeft,
                _dragWidth,
                _dragHieght,
                _rotateMatrix,
                _rotateVal,
                _currentEditType,
                _textTpl;       // 文本拖拽层时，文本模板

            // 当前编辑模块处于拖拽或者编辑状态才去获取属性
            if(_currentEditModuleELId){
                _currentEditEl = $("#"+_currentEditModuleELId+"");
                _dragTop = _currentEditEl.css("top");
                _dragLeft = _currentEditEl.css("left");
                _dragWidth = _currentEditEl.outerWidth();
                _dragHieght = _currentEditEl.outerHeight();
                _rotateMatrix = _currentEditEl.css("transform").split('(')[1].split(')')[0].split(',');
                _rotateVal = this.getmatrix(_rotateMatrix[0], _rotateMatrix[1], _rotateMatrix[2], _rotateMatrix[3]);
                _currentEditType = _currentEditModuleELId.split("_")[0];
            }

            // 判断是否显示拖拽层
            if(boolean){
                // 先移除拖拽层的隐藏和先定位是为了文本选中后可以立即编辑
                $(".J-drag-box").css({
                    "top": _dragTop,
                    "left": _dragLeft,
                    "width": "auto",
                    "height": "auto",
                    "transform": "rotate("+_rotateVal+"deg)"
                }).removeClass("editor-hidden");

                // 当前需要拖拽的层类型
                if(_currentEditType === "TEXT" && editVal){
                    $(".J-drag-box").attr("data-type", "edit");
                    _textTpl =$($("#"+_currentEditModuleELId+"").html());
                    _textTpl.removeClass("editor-hidden").attr({
                        "id": "DRAG_"+_currentEditModuleELId+"",
                        "contenteditable": true});
                    $("#resize-text-holder").html("").removeClass("editor-hidden").append(_textTpl);
                    // 执行文本全选
                    this.isTextSelectAll("DRAG_"+_currentEditModuleELId+"");
                }else{
                    $(".J-drag-box").css({
                        "width": _dragWidth,
                        "height": _dragHieght
                    }).attr("data-type", "drag");
                    $("#resize-text-holder").addClass("editor-hidden").html("");
                }
            }else{
                $("#resize-text-holder").addClass("editor-hidden").html("");
                $(".J-drag-box").addClass("editor-hidden").attr("data-type", "drag");
            }
        },

        // 角度计算
        getmatrix: function(a,b,c,d){
            var aa=Math.round(180*Math.asin(a)/ Math.PI),
                bb=Math.round(180*Math.acos(b)/ Math.PI),
                cc=Math.round(180*Math.asin(c)/ Math.PI),
                dd=Math.round(180*Math.acos(d)/ Math.PI),
                deg=0;

            if(aa==bb||-aa==bb){
                deg=dd;
            }else if(-aa+bb==180){
                deg=180+cc;
            }else if(aa+bb==180){
                deg=360-cc||360-dd;
            }

            return deg >= 360 ? 0 : parseInt(deg);
        },

        // 文本全选
        isTextSelectAll: function(selectElID){
            var _range,
                _selectEl = document.getElementById(selectElID);

            if (document.selection) {
                _range = document.body.createTextRange();
                _range.moveToElementText(_selectEl);
                _range.select();
            } else if (window.getSelection) {
                _range = document.createRange();
                _range.selectNodeContents(_selectEl);
                window.getSelection().removeAllRanges();
                window.getSelection().addRange(_range);
            }
        },
        
        /*
        ** @fn renderErrorMsg渲染错误提示信息
        ** @params {string} errorMsg错误信息
        */ 
        renderErrorMsg: function(errorMsg, time){
         	var _time = time || 2000,
         		_timer,
         		_errorMsgTemplate = $(TEMPLATE.errorHintLayerTemplate),
         		_appendEl = $("#img-editor-layer");
         	
         	if("" != errorMsg){
         		_errorMsgTemplate.find("p").text(errorMsg);
         		_appendEl.append(_errorMsgTemplate);
         		clearTimeout(_timer);
         		_timer = setTimeout(function(){
         			_appendEl.find(".J-editor-error").remove();
         		}, _time);
         	}else{
         		_appendEl.find(".J-editor-error").remove();
         	}
         },
         /*
         ** @fn renderEditorLoad渲染编辑器加载页
         ** @params {string} laodVal加载描述，有描述显示加载，无描述移除加载
         */
         renderEditorLoad: function(laodVal){
			var _loadTemplate = $(TEMPLATE.editorLoadLayer),
				_appendEl = $("#img-editor-layer");
			
			if(laodVal){
				_appendEl.append(_loadTemplate);
			}else{
				$("#fxeditor-laod").remove();
			}
         },
         
         // 获取图片实际尺寸信息
         getImgNaturalSize: function(Domlement) {
     		var _naturalSize = {};
     		if(window.naturalWidth && window.naturalHeight) {
     			_naturalSize.width = Domlement.naturalWidth;
     			_naturalSize.height = Domlement.naturalHeight;
     		} else {
     			var _img = new Image();
     			_img.src = Domlement[0].src;
     			_naturalSize.width = _img.width;
     			_naturalSize.height = _img.height;
     		}
     		return _naturalSize;
         }
         
    };

    // 文本模块编辑工具栏对象
    var TextModuleEditTools = function(){
        this.textToolsTemplate = $(IMGEDITORTEMPLATE.imgEditorTextToolsTPL);           // 文字工具栏主结构模板
        this.fontFamilyArr = ["黑体", "宋体", "仿宋", "serif", "微软雅黑", "sans-serif", "Helvetica"];                // 字体默认下拉列表数据
        this.fontSizeArr = ["12px", "14px", "16px", "18px", "24px", "32px", "48px"];                                // 字体大小默认下拉列表数据
        this.lineHeightArr = ["14px", "16px", "18px", "20px", "28px", "34px", "40px", "56px", "68px", "80px"];      // 文字2行高默认下拉列表数据
        this.textParamsLength = 4;          // 文本参数每行显示参数量
        this.textParamsStartList = 1;       // 文本参数起始列
    }

    /*
    ** @fn TextModuleEditTools.init()文本模块工具栏初始化方法
    ** @params {element} appendEL文本模块工具栏插入节点
    ** @prams {JSON} textDefaultData文字默认参数，为部分工具提供默认参数值
    */
    TextModuleEditTools.prototype.init = function(appendEL, textDefaultData){
        var _fontFamilyTpl = $(this.getFontFamilyListTpl()),    // 字体列表模板
            _fontSizeTpl = $(this.getFontSizeListTpl()),        // 字体大小列表模板
            _lineHeightTpl = $(this.getLineHeightListTpl()),    // 字体行高列表模板
            _colorVal = textDefaultData.color,                  // 颜色默认值
            _textParamsListA,
            _selectType,
            _textParamsListB,
            _colorVal;

        // 字体列表，文字大小，行高等列表插入父级节点
        this.textToolsTemplate.find(".J-font-family").append(_fontFamilyTpl);
        this.textToolsTemplate.find(".J-font-size").append(_fontSizeTpl);
        this.textToolsTemplate.find(".J-font-height").append(_lineHeightTpl);
        // 执行两次参数列表获取和插入，默认渲染8条参数
        _textParamsListA = $(this.getTextParamsListTpl());
        this.textToolsTemplate.find(".J-font-params-edit").before(_textParamsListA);
        this.textParamsStartList++;
        _textParamsListB = $(this.getTextParamsListTpl());
        this.textToolsTemplate.find(".J-font-params-edit").before(_textParamsListB);

        // 工具插入到工具栏中
        appendEL.append(this.textToolsTemplate);
        // 滑动条初始化
        EditorSlider.init();
        // 默认参数渲染
        this.showToolsDefaultVal(textDefaultData)
        // 颜色选择器插件调用渲染
        $('#colorSelector').ColorPicker({
            color: _colorVal,
            onChange: function (hsb, hex, rgb) {
                _selectType = $(".J-color-table.active").data("type");
                // 执行文字样式操作设置
                FXImgEditor.showTextFontStyleSet(_selectType, "#"+hex+"");
            },
            onSubmit: function(hsb, hex, rgb, el){
                $(el).ColorPickerHide();
            }
        });
    }

    /*
    ** @fn showToolsDefaultVal显示工具栏中默认值
    ** @params {JSON} toolsDefaultVal默认参数数据
    */
    TextModuleEditTools.prototype.showToolsDefaultVal = function(toolsDefaultVal){
        var _fontFamilyVal = toolsDefaultVal.fontFamily,        // 字体默认值
            _fontSizeVal = toolsDefaultVal.fontSize,            // 字体大小默认值
            _lineHeightVal = toolsDefaultVal.lineHeight,        // 行高默认值
            _fontWeightVal = toolsDefaultVal.fontWeight,        // 字体加粗默认值
            _fontStyleVal = toolsDefaultVal.fontStyle,          // 字体斜体默认值
            _textDecoration = toolsDefaultVal.textDecoration.split(" ")[0],   // 文字下划线
            _textAlignVal = toolsDefaultVal.textAlign,          // 对齐方式默认值
            _widthVal = toolsDefaultVal.width,                  // 模块宽度默认值
            _rotateVal = parseInt(toolsDefaultVal.rotate);      // 模块旋转角度默认值

        $(".J-text-tools").find(".J-font-family").siblings(".J-select-val").text(_fontFamilyVal);
        $(".J-text-tools").find(".J-font-size").siblings(".J-select-val").text(_fontSizeVal);
        $(".J-text-tools").find(".J-font-height").siblings(".J-select-val").text(_lineHeightVal);

        _fontWeightVal === "400" ? $(".J-font-style[data-type='weight']").removeClass("active") : $(".J-font-style[data-type='weight']").addClass("active");
        _fontStyleVal === "normal" ? $(".J-font-style[data-type='italic']").removeClass("active") : $(".J-font-style[data-type='italic']").addClass("active");
        _textDecoration === "none" ? $(".J-font-style[data-type='line']").removeClass("active") : $(".J-font-style[data-type='line']").addClass("active");
        $(".J-align-style[data-type='"+_textAlignVal+"']").addClass("active").siblings(".J-align-style").removeClass("active");
        $(".J-font-input[data-type='width']").val(_widthVal);
        $(".J-font-input[data-type='rotate']").val(_rotateVal+"°").trigger("focus").blur();
    }

    // 获取字体列表模板
    TextModuleEditTools.prototype.getFontFamilyListTpl = function(){
        var _data = this.fontFamilyArr,
            _dataTplStr = "",
            _dataLength = _data.length;

        for(var i = 0; i < _dataLength; i++){
            _dataTplStr += "<li class=\"list-li J-list-li\" data-val=\""+_data[i]+"\" style=\"font-family: "+_data[i]+";\">"+_data[i]+"</li>"
        }
        // 返回数据模板
        return _dataTplStr;
    }

    // 获取文字大小列表模板
    TextModuleEditTools.prototype.getFontSizeListTpl = function(){
        var _data = this.fontSizeArr,
            _dataTplStr = "",
            _dataLength = _data.length,
            _lineHeight;

        for(var i = 0; i < _dataLength; i++){
            if(parseInt(_data[i]) < 32){
                _lineHeight = "30px";
            }else{
                _lineHeight = 1.5;
            }

            _dataTplStr += '<li class="list-li J-list-li" data-val="'+_data[i]+'" style="font-size:'+_data[i]+';line-height:'+_lineHeight+';">'+_data[i]+'</li>'
        }
        return _dataTplStr;
    }

    // 获取文字行高列表模板
    TextModuleEditTools.prototype.getLineHeightListTpl = function(){
        var _data = this.lineHeightArr,
            _dataTplStr = "",
            _dataLength = _data.length;

        for(var i = 0; i < _dataLength; i++){
            _dataTplStr += '<li class="list-li J-list-li" data-val="'+_data[i]+'">'+_data[i]+'</li>'
        }
        return _dataTplStr;
    }

    // 获取文本参数列表模板
    TextModuleEditTools.prototype.getTextParamsListTpl = function(){
        var _paramsLiNum = this.textParamsStartList,
            _paramsLength = this.textParamsLength,
            _singleParamsNum,
            _paramsTplStr = "";

        for(var i = 0; i < _paramsLength; i++){
            if(_paramsLiNum <= 1){
                _singleParamsNum = i+1;
            }else{
                _singleParamsNum = (i+1)+((_paramsLiNum-1)*_paramsLength);
            }

            _paramsTplStr += '<p class="prams-list-li editor-fl"><input id="font-params-'+_singleParamsNum+'" value="{#'+LANGUAGE.imgEdit.textArea.imgParams+''+_singleParamsNum+'#}"  type="checkbox"><label for="font-params-'+_singleParamsNum+'">'+LANGUAGE.imgEdit.textArea.imgParams+''+_singleParamsNum+'</label></p>';
        }
        return '<div class="font-params-list editor-clear">'+_paramsTplStr+'</div>';
    }

    // 编辑器中滑动条方法
    var EditorSlider = {
        dragLock: false,        // 是否可以拖动锁
        startX: 0,              // 起始点
        endX: 0,                // 结束点
        pointerLeft: 0,         // 拖动点左边距
        init: function(){
            this.targetEl = "";     // 当前拖动的元素节点
            this.bindUI();
        },

        bindUI: function(){
            var _self = this,
                _event;

            // 拖动开始
            $(".J-slider-pointer").on("mousedown", function(event){
                _event = event || window.event;

                _self.targetEl = $(this);
                _self.dragLock = true;
                _self.startX = _event.clientX;
                _self.pointerLeft = $(this).offset().left;
                // 阻止默认事件
                _event.preventDefault();
            });

            // 拖动
            $(document).on("mousemove", function(event){
                _event = event || window.event;
                if(!_self.dragLock){
                    return;
                }
                _self.endX = _event.clientX;
                // 执行拖动计算
                _self.isComputeSliderResult()
                // 阻止默认事件
                _event.preventDefault();
            });

            // 拖动结束
            $(document).on("mouseup", function(){
                _self.dragLock = false;
            });
        },

        // 计算拖动结果
        isComputeSliderResult: function(){
            var _sliderContainerEl = this.targetEl.closest(".J-slider-container"),
                _sliderBarEl = this.targetEl.siblings(".J-slider-bar"),
                _sliderContainerElLeft = _sliderContainerEl.offset().left,
                _sliderPointerWidth = this.targetEl.width(),
                _sliderContainerWidth = _sliderContainerEl.width(),
                _sliderBarShowWidth,
                _showNum,
                _moveX;

            _moveX = this.pointerLeft + (this.endX - this.startX);

            if(_moveX <= _sliderContainerElLeft){
                _moveX = _sliderContainerElLeft
            }else if(_moveX >= _sliderContainerElLeft+(_sliderContainerWidth-_sliderPointerWidth)){
                _moveX = _sliderContainerElLeft+(_sliderContainerWidth-_sliderPointerWidth);
            }
            _sliderBarShowWidth = _moveX-_sliderContainerElLeft+(_sliderPointerWidth/2);

            _showNum = parseInt(this.isComputeNum(_moveX-_sliderContainerElLeft));

            _sliderBarEl.width(_sliderBarShowWidth);
            this.targetEl.offset({"left": _moveX});
            _sliderContainerEl.siblings(".J-font-input").val(_showNum+"°");
            _sliderContainerEl.siblings(".J-font-input").trigger("focus")
        },

        // 计算关联数据显示
        isComputeNum: function(sliderWidth){
            var _sliderContainerEl = this.targetEl.closest(".J-slider-container"),
                _sliderContainerWidth = _sliderContainerEl.width(),
                _sliderPointerWidth = this.targetEl.width(),
                _dataType = _sliderContainerEl.data("type"),
                _maxVal,
                _showNum;

            if(_dataType === "rotate"){
                _maxVal = 360;
            }

            _showNum = sliderWidth/(_sliderContainerWidth-_sliderPointerWidth)*_maxVal;

            return _showNum;
        },

        // 设置滑动条起始位置
        setSliderBarInitOffset: function(inputEl){
            var _inputVal = parseInt(inputEl.val()),
                _inputType = inputEl.data("type"),
                _sliderContainerEl = inputEl.siblings(".J-slider-container"),
                _sliderContainerWidth = _sliderContainerEl.width(),
                _sliderBarEL = _sliderContainerEl.find(".J-slider-bar"),
                _sliderPointerEL = _sliderContainerEl.find(".J-slider-pointer"),
                _sliderPointerWidth = _sliderPointerEL.width(),
                _offsetLeft,
                _maxVal;

            if(_inputType === "rotate"){
                _maxVal = 360;
            }
            if(_inputVal <= 0){
                _inputVal = 0;
            }else if(_inputVal >= _maxVal){
                _inputVal = _maxVal;
            }

            _offsetLeft = (_inputVal/_maxVal)*(_sliderContainerWidth-_sliderPointerWidth);
            _sliderPointerEL.css("left", _offsetLeft+"px");
            _sliderBarEL.css("width", _offsetLeft+(_sliderPointerWidth/2)+"px");
        }
    }
    
    // jq拓展拖拽方法
    $.fn.Drag = function(mouseupCB){
        var DNode = this,
            dragLock = false,
            dragBoxEL,         // 拖拽的限定容器
            dragHolderEl,      // 拖拽占位元素
            tagEl,             // 当前拖动对象
            startX,            // 鼠标起点clientX位置
            startY,            // 鼠标起点clientY位置
            endX,              // 鼠标结束点clientX位置
            endY,              // 鼠标结束点clientY位置
            moveX,             // X轴移动距离
            moveY,             // Y轴移动距离
            ELOffsetTop,       // 元素距离父元素顶部偏移距离
            ELOffsetLeft;      // 元素距离父元素左边偏移距离

        DNode.on("mousedown", function(e){
            var _event = e || window.event,
                _elType = $(this).attr("data-type");

            // 鼠标左键拖拽
            if(_event.button !== 0 || _elType !== "drag"){
                return;
            }

            tagEl = $(this);
            startX = _event.clientX;
            startY = _event.clientY;
            dragBoxEL = tagEl.closest(".J-main-container");
            ELOffsetTop = tagEl.offset().top;
            ELOffsetLeft = tagEl.offset().left;
            dragLock = true;
            _event.preventDefault();
        });

        $(document).on("mousemove", function(e){
            var _event = e || window.event,
                _dragBoxElLeft,         // 拖拽限定容器左边距
                _dragBoxElTop,          // 拖拽限定容器顶部距离
                _dragBoxElWidth,        // 拖拽限定容器宽度
                _dragBoxElHeight,       // 拖拽限定容器高度
                _dragElWidth,           // 拖拽对象的宽度
                _dragElHeight;          // 拖拽对象的高度

            // 判断是否已经松开鼠标按键
            if(!dragLock){
                return;
            }
            endX = _event.clientX;
            endY = _event.clientY;
            _dragBoxElLeft = dragBoxEL.offset().left;
            _dragBoxElTop = dragBoxEL.offset().top;
            _dragBoxElWidth = dragBoxEL.width();
            _dragBoxElHeight = dragBoxEL.height();
            _dragElWidth = tagEl.outerWidth();
            _dragElHeight = tagEl.outerHeight();
            moveX = ELOffsetLeft+(endX - startX);
            moveY = ELOffsetTop+(endY - startY);

            // 左右边界限定
            if(moveX <= _dragBoxElLeft){
                moveX = _dragBoxElLeft;
            }else if(moveX >= _dragBoxElLeft+(_dragBoxElWidth-_dragElWidth)){
                moveX = _dragBoxElLeft+(_dragBoxElWidth-_dragElWidth)
            }else{
                moveX = moveX;
            }

            // 上下边界限定
            if(moveY <= _dragBoxElTop){
                moveY = _dragBoxElTop;
            }else if(moveY >= _dragBoxElTop+(_dragBoxElHeight-_dragElHeight)){
                moveY = _dragBoxElTop+(_dragBoxElHeight-_dragElHeight)
            }else{
                moveY = moveY;
            }

            if(!dragHolderEl){
                dragHolderEl = createMoveTemplate(tagEl);
                dragBoxEL.append(dragHolderEl);
            }
            dragHolderEl.offset({"left": moveX, "top":moveY});
            _event.preventDefault();
        });

        $(document).on("mouseup", function(){
            dragLock = false;
            if(dragHolderEl){
                dragHolderEl.remove();
                dragHolderEl = "";
                mouseupCB && mouseupCB(moveX, moveY);
                // 实时计算移动位置距离
                tagEl.offset({"left": moveX, "top":moveY});
            }
        });
    };

    // 创建拉伸占位层
    function createMoveTemplate(targetEl){
        var _elTemplate = $("<div></div>"),     // 占位层模板
            _elWidth = targetEl.outerWidth(),      // 占位层初始宽度
            _elHeight = targetEl.outerHeight(),     // 占位层初始高度
            _rotateMatrix = targetEl.css("transform").split('(')[1].split(')')[0].split(','),   // 获取旋转参数值
            _rotateVal = FXImgEditor.getmatrix(_rotateMatrix[0], _rotateMatrix[1], _rotateMatrix[2], _rotateMatrix[3]);    // 旋转角度换算

        // 模板样式
        _elTemplate.css({
            "position": "absolute",
            "top": 0,
            "left": 0,
            "z-index": "199",
            "width": _elWidth+"px",
            "height": _elHeight+"px",
            "background": "rgba(0,0,0,.3)",
            "transform": "rotate("+_rotateVal+"deg)"
        });
        // 返回拉伸占位模板
        return _elTemplate;
    }
}(window, window.jQuery));