(function (){
    var utils = baidu.editor.utils,
        uiUtils = baidu.editor.ui.uiUtils,
        UIBase = baidu.editor.ui.UIBase;

    function EditorUI(options){
        this.initOptions(options);
        this.initEditorUI();
    }
    EditorUI.prototype = {
        uiName: 'editor',
        initEditorUI: function (){
            this.editor.ui = this;
            this.initUIBase();
            this._initToolbars();
            var editor = this.editor;
            editor.addListener('ready', function (){
                baidu.editor.dom.domUtils.on(editor.window, 'scroll', function (){
                    baidu.editor.ui.Popup.postHide();
                });

                //display bottom-bar label based on config
                if(editor.options.elementPathEnabled){
                    editor.ui.getDom('elementpath').innerHTML = '<div class="edui-editor-breadcrumb">path:</div>';
                }
                if(editor.options.wordCount){
                    /*字数统计*/
                    editor.ui.getDom('wordcount').innerHTML = getJsLocaleMessage("ydwx","ydwx_common_wordCount");
                }

                editor.fireEvent('selectionchange', false, true);
            });
            editor.addListener('mousedown', function (t, evt){
                var el = evt.target || evt.srcElement;
                baidu.editor.ui.Popup.postHide(el);
            });
            editor.addListener('contextmenu', function (t, evt){
                baidu.editor.ui.Popup.postHide();
            });
            var me = this;
            editor.addListener('selectionchange', function (){
                me._updateElementPath();
                //字数统计
                me._wordCount();
            });
            editor.addListener('sourcemodechanged', function (t, mode){
                if(editor.options.elementPathEnabled){
                    if (mode) {
                        me.disableElementPath();
                    } else {
                        me.enableElementPath();
                    }
                }
                if(editor.options.wordCount){
                     if (mode) {
                        me.disableWordCount();
                    } else {
                        me.enableWordCount();
                    }
                }


            });
            // 超链接的编辑器浮层
            var linkDialog = new baidu.editor.ui.Dialog({
                iframeUrl: editor.ui.mapUrl(me.editor.options.iframeUrlMap.link),
                autoReset: true,
                draggable: true,
                editor: editor,
                className: 'edui-for-user-link',
                /*超链接*/
                title: getJsLocaleMessage("common","common_hyperlink"),
                buttons: [{
                    className: 'edui-okbutton',
                    /*确定*/
                    label: getJsLocaleMessage("common","common_confirm"),
                    onclick: function (){
                        linkDialog.close(true);
                    }
                }, {
                    className: 'edui-cancelbutton',
                    /*取消*/
                    label: getJsLocaleMessage("common","common_cancel"),
                    onclick: function (){
                        linkDialog.close(false);
                    }
                }],
                onok: function (){},
                oncancel: function (){},
                onclose: function (t,ok){
                    if (ok) {
                        return this.onok();
                    } else {
                        return this.oncancel();
                    }
                }

            });
            linkDialog.render();
            // 图片的编辑器浮层
            var imgDialog = new baidu.editor.ui.Dialog({
                iframeUrl: editor.ui.mapUrl(me.editor.options.iframeUrlMap.insertimage),
                autoReset: true,
                draggable: true,
                editor: editor,
                className: 'edui-for-user-insertimage',
                /*图片*/
                title: getJsLocaleMessage("ydwx","ydwx_wxbj_74"),
                buttons: [{
                    className: 'edui-okbutton',
                    /*确定*/
                    label: getJsLocaleMessage("common","common_confirm"),
                    onclick: function (){
                        imgDialog.close(true);
                    }
                }, {
                    className: 'edui-cancelbutton',
                    /*取消*/
                    label: getJsLocaleMessage("common","common_cancel"),
                    onclick: function (){
                        imgDialog.close(false);
                    }
                }],
                onok: function (){},
                oncancel: function (){},
                onclose: function (t,ok){
                    if (ok) {
                        return this.onok();
                    } else {
                        return this.oncancel();
                    }
                }

            });
            imgDialog.render();
            
             // 互动的编辑器浮层
            var interactionDialog = new baidu.editor.ui.Dialog({
                iframeUrl: editor.ui.mapUrl(me.editor.options.iframeUrlMap.insertinteraction),
                autoReset: true,
                draggable: true,
                editor: editor,
                className: 'edui-for-user-insertinteraction',
                /*图片*/
                title: getJsLocaleMessage("ydwx","ydwx_wxbj_74"),
                buttons: [{
                    className: 'edui-okbutton',
                    /*确定*/
                    label: getJsLocaleMessage("common","common_confirm"),
                    onclick: function (){
                        interactionDialog.close(true);
                    }
                }, {
                    className: 'edui-cancelbutton',
                    /*取消*/
                    label: getJsLocaleMessage("common","common_cancel"),
                    onclick: function (){
                        interactionDialog.close(false);
                    }
                }],
                onok: function (){},
                oncancel: function (){},
                onclose: function (t,ok){
                    if (ok) {
                        return this.onok();
                    } else {
                        return this.oncancel();
                    }
                }

            });
            interactionDialog.render();
            
            
            
              // 互动的编辑器浮层
            var downfileDialog = new baidu.editor.ui.Dialog({
                iframeUrl: editor.ui.mapUrl(me.editor.options.iframeUrlMap.insertdownfile),
                autoReset: true,
                draggable: true,
                editor: editor,
                className: 'edui-for-user-insertdownfile',
                /*插入下载文件*/
                title: getJsLocaleMessage("ydwx","ydwx_wxbj_91"),
                buttons: [{
                    className: 'edui-okbutton',
                    /*确定*/
                    label: getJsLocaleMessage("common","common_confirm"),
                    onclick: function (){
                        downfileDialog.close(true);
                    }
                }, {
                    className: 'edui-cancelbutton',
                    /*取消*/
                    label: getJsLocaleMessage("common","common_cancel"),
                    onclick: function (){
                        downfileDialog.close(false);
                    }
                }],
                onok: function (){},
                oncancel: function (){},
                onclose: function (t,ok){
                    if (ok) {
                        return this.onok();
                    } else {
                        return this.oncancel();
                    }
                }

            });
            downfileDialog.render();
            
            //锚点
            var anchorDialog = new baidu.editor.ui.Dialog({
                iframeUrl: editor.ui.mapUrl(me.editor.options.iframeUrlMap.anchor),
                autoReset: true,
                draggable: true,
                editor: editor,
                className: 'edui-for-anchor',
                /*锚点*/
                title: getJsLocaleMessage("ydwx","ydwx_common_anchor"),
                buttons: [{
                    className: 'edui-okbutton',
                    /*确定*/
                    label: getJsLocaleMessage("common","common_confirm"),
                    onclick: function (){
                        anchorDialog.close(true);
                    }
                }, {
                    className: 'edui-cancelbutton',
                   /*取消*/
                    label: getJsLocaleMessage("common","common_cancel"),
                    onclick: function (){
                        anchorDialog.close(false);
                    }
                }],
                onok: function (){},
                oncancel: function (){},
                onclose: function (t,ok){
                    if (ok) {
                        return this.onok();
                    } else {
                        return this.oncancel();
                    }
                }

            });
            anchorDialog.render();
            // video
            var videoDialog = new baidu.editor.ui.Dialog({
                iframeUrl: editor.ui.mapUrl(me.editor.options.iframeUrlMap.insertvideo),
                autoReset: true,
                draggable: true,
                editor: editor,
                className: 'edui-for-video',
                /*视频*/
                title: getJsLocaleMessage("ydwx","ydwx_wxsc_55"),
                buttons: [{
                    className: 'edui-okbutton',
                    /*确定*/
                    label: getJsLocaleMessage("common","common_confirm"),
                    onclick: function (){
                        videoDialog.close(true);
                    }
                }, {
                    className: 'edui-cancelbutton',
                    /*取消*/
                    label: getJsLocaleMessage("common","common_cancel"),
                    onclick: function (){
                        videoDialog.close(false);
                    }
                }],
                onok: function (){},
                oncancel: function (){},
                onclose: function (t,ok){
                    if (ok) {
                        return this.onok();
                    } else {
                        return this.oncancel();
                    }
                }

            });
            videoDialog.render();
            // map
            var mapDialog = new baidu.editor.ui.Dialog({
                iframeUrl: editor.ui.mapUrl(me.editor.options.iframeUrlMap.map),
                autoReset: true,
                draggable: true,
                editor: editor,
                className: 'edui-for-map',
                /*地图*/
                title: getJsLocaleMessage("ydwx","ydwx_common_map"),
                buttons: [{
                    className: 'edui-okbutton',
                    /*确定*/
                    label: getJsLocaleMessage("common","common_confirm"),
                    onclick: function (){
                        mapDialog.close(true);
                    }
                }, {
                    className: 'edui-cancelbutton',
                    /*取消*/
                    label: getJsLocaleMessage("common","common_cancel"),
                    onclick: function (){
                        mapDialog.close(false);
                    }
                }],
                onok: function (){},
                oncancel: function (){},
                onclose: function (t,ok){
                    if (ok) {
                        return this.onok();
                    } else {
                        return this.oncancel();
                    }
                }

            });
            mapDialog.render();
            // map
            var gmapDialog = new baidu.editor.ui.Dialog({
                iframeUrl: editor.ui.mapUrl(me.editor.options.iframeUrlMap.gmap),
                autoReset: true,
                draggable: true,
                editor: editor,
                className: 'edui-for-gmap',
                /*地图*/
                title: 'Google '+getJsLocaleMessage("ydwx","ydwx_common_map"),
                buttons: [{
                    className: 'edui-okbutton',
                    /*确定*/
                    label: getJsLocaleMessage("common","common_confirm"),
                    onclick: function (){
                        gmapDialog.close(true);
                    }
                }, {
                    className: 'edui-cancelbutton',
                    /*取消*/
                    label: getJsLocaleMessage("common","common_cancel"),
                    onclick: function (){
                        gmapDialog.close(false);
                    }
                }],
                onok: function (){},
                oncancel: function (){},
                onclose: function (t,ok){
                    if (ok) {
                        return this.onok();
                    } else {
                        return this.oncancel();
                    }
                }

            });
            gmapDialog.render();
            var popup = new baidu.editor.ui.Popup({
                content: '',
                className: 'edui-bubble',
                _onEditButtonClick: function (){
                    this.hide();
                    linkDialog.open();
                },
                _onImgEditButtonClick: function (){
                    this.hide();
                    var nodeStart = editor.selection.getRange().getClosedNode();
                    var img = baidu.editor.dom.domUtils.findParentByTagName(nodeStart,"img",true);
                    if(img && img.className.indexOf("edui-faked-video") != -1){
                        videoDialog.open();
                    }else if(img && img.src.indexOf("http://api.map.baidu.com")!=-1){
                        mapDialog.open();
                    }else if(img && img.src.indexOf("http://maps.google.com/maps/api/staticmap")!=-1){
                        gmapDialog.open();
                    }else if(img && img.getAttribute("anchorname")){
                        anchorDialog.open();
                    }else{
                        imgDialog.open();
                    }

                },
                _getImg: function (){
                    var img = editor.selection.getRange().getClosedNode();
                    if (img && (img.nodeName == 'img' || img.nodeName == 'IMG')) {
                        return img;
                    }
                    return null;
                },
                _onImgSetFloat: function(value){
                    if (this._getImg()) {
                        editor.execCommand("imagefloat",value);
                        var img = this._getImg();
                        if (img) {
                            this.showAnchor(img);
                        }
                    }
                },
                _setIframeAlign: function(value){
                    var frame = popup.anchorEl;
                    var newFrame = frame.cloneNode(true);
                    switch(value){
                        case -2:
                            newFrame.setAttribute("align","");
                            break;
                        case -1:
                            newFrame.setAttribute("align","left");
                            break;
                        case 1:
                            newFrame.setAttribute("align","right");
                            break;
                        case 2:
                            newFrame.setAttribute("align","middle");
                            break;
                    }
                    frame.parentNode.insertBefore(newFrame,frame);
                    baidu.editor.dom.domUtils.remove(frame);
                    popup.anchorEl = newFrame;
                    popup.showAnchor(popup.anchorEl);
                },
                _updateIframe: function(){
                    editor._iframe = popup.anchorEl;
                    insertframe.open();
                    popup.hide();
                },
                _onRemoveButtonClick: function (){
                    var nodeStart = editor.selection.getRange().getClosedNode();
                    var img = baidu.editor.dom.domUtils.findParentByTagName(nodeStart,"img",true);
                    if(img && img.getAttribute("anchorname")){
                        editor.execCommand("anchor");
                    }else{
                        editor.execCommand('unlink');
                    }
                    this.hide();
                },
                queryAutoHide: function (el){
                    if (el && el.ownerDocument == editor.document) {
                        if (el.tagName.toLowerCase() == 'img' || baidu.editor.dom.domUtils.findParentByTagName(el, 'a', true)) {
                            return el !== popup.anchorEl;
                        }
                    }
                    return baidu.editor.ui.Popup.prototype.queryAutoHide.call(this, el);
                }
            });
            popup.render();
           
              //actividata
            var actividata = new baidu.editor.ui.Dialog({
                iframeUrl: editor.ui.mapUrl(me.editor.options.iframeUrlMap.actividata),
                autoReset: true,
                draggable: true,
                editor: editor,
                className: 'edui-for-actividata',
                /*插入*/
                title: getJsLocaleMessage("ydwx","ydwx_common_insert")+' iframe',
                buttons: [{
                    className: 'edui-okbutton',
                    /*确定*/
                    label: getJsLocaleMessage("common","common_confirm"),
                    onclick: function (){
                        insertframe.close(true);
                    }
                }, {
                    className: 'edui-cancelbutton',
                    /*取消*/
                    label: getJsLocaleMessage("common","common_cancel"),
                    onclick: function (){
                        insertframe.close(false);
                    }
                }],
                onok: function (){},
                oncancel: function (){},
                onclose: function (t,ok){
                    if (ok) {
                        return this.onok();
                    } else {
                        return this.oncancel();
                    }
                }

            });
            actividata.render();
            editor.addListener('mouseover',function(t,evt){
                evt = evt || window.event;
                var el = evt.target || evt.srcElement;
                if(/iframe/ig.test(el.tagName)){
                    var html = popup.formatHtml(
                        /*属性: 默认 左对齐  右对齐  居中 修改*/
                        '<nobr>'+getJsLocaleMessage("ydwx","ydwx_common_attribution")+'<span onclick=$$._setIframeAlign(-2) class="edui-clickable">'+getJsLocaleMessage("ydwx","ydwx_wxbj_86")+'</span>&nbsp;&nbsp;<span onclick=$$._setIframeAlign(-1) class="edui-clickable">'+getJsLocaleMessage("ydwx","ydwx_wxbj_78")+'</span>&nbsp;&nbsp;<span onclick=$$._setIframeAlign(1) class="edui-clickable">'+getJsLocaleMessage("ydwx","ydwx_wxbj_79")+'</span>&nbsp;&nbsp;'+
                            '<span onclick=$$._setIframeAlign(2) class="edui-clickable">'+getJsLocaleMessage("ydwx","ydwx_wxbj_89")+'</span>' +
                            ' <span onclick="$$._updateIframe( this);" class="edui-clickable">'+getJsLocaleMessage("common","common_modify")+'</span></nobr>');
                    if (html) {
                        popup.getDom('content').innerHTML = html;
                        popup.anchorEl = el;
                        popup.showAnchor(popup.anchorEl);
                    } else {
                        popup.hide();
                    }
                }
            });
            editor.addListener('selectionchange', function (t, causeByUi){
                if (!causeByUi) return;
                var html = '';
                var img = editor.selection.getRange().getClosedNode();
                if(img != null && img.tagName.toLowerCase() == 'img'){
                    if(img.getAttribute('anchorname')){
                        //锚点处理
                        html += popup.formatHtml(
                            /*属性:  修改  刪除*/
                        '<nobr>'+getJsLocaleMessage("ydwx","ydwx_common_attribution")+'<span onclick=$$._onImgEditButtonClick(event) class="edui-clickable">'+getJsLocaleMessage("common","common_modify")+'</span>&nbsp;&nbsp;<span onclick=$$._onRemoveButtonClick(event) class="edui-clickable">'+getJsLocaleMessage("common","common_delete")+'</span></nobr>');
                    }else{
                        html += popup.formatHtml(
                            /*属性: 默认 居左 居右 居中 修改*/
                            '<nobr>'+getJsLocaleMessage("ydwx","ydwx_common_attribution")+'<span onclick=$$._onImgSetFloat("none") class="edui-clickable">'+getJsLocaleMessage("ydwx","ydwx_wxbj_86")+'</span>&nbsp;&nbsp;<span onclick=$$._onImgSetFloat("left") class="edui-clickable">'+getJsLocaleMessage("ydwx","ydwx_wxbj_87")+'</span>&nbsp;&nbsp;<span onclick=$$._onImgSetFloat("right") class="edui-clickable">'+getJsLocaleMessage("ydwx","ydwx_wxbj_88")+'</span>&nbsp;&nbsp;'+
                            '<span onclick=$$._onImgSetFloat("center") class="edui-clickable">'+getJsLocaleMessage("ydwx","ydwx_wxbj_89")+'</span>' +
                            ' <span onclick="$$._onImgEditButtonClick(event, this);" class="edui-clickable">'+getJsLocaleMessage("common","common_modify")+'</span></nobr>');
                    }
                }
                var link;
                if(editor.selection.getRange().collapsed){
                    link = editor.queryCommandValue("link");
                }else{
                    link = editor.selection.getStart();
                }
                link = baidu.editor.dom.domUtils.findParentByTagName(link,"a",true);
                var url;
                if (link != null && (url = link.getAttribute('href', 2)) != null) {
                    var txt = url;
                    if(url.length>30){
                        txt = url.substring(0,20)+"...";
                    }
                    if (html) {
                        html += '<div style="height:5px;"></div>'
                    }
                    html += popup.formatHtml(
                        /*链接:  修改  清除*/
                        '<nobr>'+getJsLocaleMessage("ydwx","ydwx_common_link")+'<a target="_blank" href="'+ url +'" title="'+url+'" >' + txt + '</a>' +
                        ' <span class="edui-clickable" onclick="$$._onEditButtonClick(event, this);">'+getJsLocaleMessage("common","common_modify")+'</span>' +
                        ' <span class="edui-clickable" onclick="$$._onRemoveButtonClick(event, this);"> '+getJsLocaleMessage("ydwx","ydwx_common_clean")+'</span></nobr>');
                    popup.showAnchor(link);
                }
                if (html) {
                    popup.getDom('content').innerHTML = html;
                    popup.anchorEl = img || link;
                    popup.showAnchor(popup.anchorEl);
                } else {
                    popup.hide();
                }
            });
        },
        _initToolbars: function (){
            var editor = this.editor;
            var toolbars = this.toolbars || [];
            var toolbarUis = [];
            for (var i=0; i<toolbars.length; i++) {
                var toolbar = toolbars[i];
                var toolbarUi = new baidu.editor.ui.Toolbar();
                for (var j=0; j<toolbar.length; j++) {
                    var toolbarItem = toolbar[j];
                    var toolbarItemUi = null;
                    if (typeof toolbarItem == 'string') {
                        if (toolbarItem == '|') {
                            toolbarItem = 'Separator';
                        }
                        if (baidu.editor.ui[toolbarItem]) {
                            toolbarItemUi = new baidu.editor.ui[toolbarItem](editor);
                        }
                    } else {
                        toolbarItemUi = toolbarItem;
                    }
                    if (toolbarItemUi) {
                        toolbarUi.add(toolbarItemUi);
                    }
                }
                toolbarUis[i] = toolbarUi;
            }
            this.toolbars = toolbarUis;
        },
        getHtmlTpl: function (){
            return '<div id="##" class="%%">' +
                '<div id="##_toolbarbox" class="%%-toolbarbox">' +
                 '<div id="##_toolbarboxouter" class="%%-toolbarboxouter"><div class="%%-toolbarboxinner">' +
                  this.renderToolbarBoxHtml() +
                 '</div></div>' +
                 '<div id="##_toolbarmsg" class="%%-toolbarmsg" style="display:none;">' +
                  '<div class="%%-toolbarmsg-close" onclick="$$.hideToolbarMsg();">x</div>' +
                  '<div id="##_toolbarmsg_label" class="%%-toolbarmsg-label"></div>' +
                  '<div style="height:0;overflow:hidden;clear:both;"></div>' +
                 '</div>' +
                '</div>' +
                '<div id="##_iframeholder" class="%%-iframeholder"></div>' +
                //wordCount
//                '<div class="%%-bottomContainer">'+
//                    '<div id="##_bottombar" class="%%-bottombar"></div>' +
//                    '<div id="##_wordcount" class="%%-wordcount"></div>' +
//                    '<div style="height:0;overflow:hidden;clear:both;"></div>' +
//                '</div>'+
                //modify wdcount by matao
                '<div id="##_bottombar" class="%%-bottomContainer"><table><tr>' +
                '<td id="##_elementpath" class="%%-bottombar"></td>' +
                '<td id="##_wordcount" class="%%-wordcount"></td>' +
                '</tr></table></div>' +
                '</div>';
        },
        renderToolbarBoxHtml: function (){
            var buff = [];
            for (var i=0; i<this.toolbars.length; i++) {
                buff.push(this.toolbars[i].renderHtml());
            }
            return buff.join('');
        },
        setFullScreen: function (fullscreen){
            function fixGecko(editor){
                editor.body.contentEditable = false;
                setTimeout(function(){
                      editor.body.contentEditable = true;
                },200) 
            }
            if (this._fullscreen != fullscreen) {
                this._fullscreen = fullscreen;
                this.editor.fireEvent('beforefullscreenchange', fullscreen);
                var editor = this.editor;
                
                if(baidu.editor.browser.gecko){
                    var bk = editor.selection.getRange().createBookmark();
                }

                if (fullscreen) {
                    this._bakHtmlOverflow = document.documentElement.style.overflow;
                    this._bakBodyOverflow = document.body.style.overflow;
                    this._bakAutoHeight = this.editor.autoHeightEnabled;
                    this._bakScrollTop = Math.max(document.documentElement.scrollTop, document.body.scrollTop);
                    if (this._bakAutoHeight) {
                        this.editor.disableAutoHeight();
                    }
                    document.documentElement.style.overflow = 'hidden';
                    document.body.style.overflow = 'hidden';
                    this._bakCssText = this.getDom().style.cssText;
                    this._bakCssText1 = this.getDom('iframeholder').style.cssText;
                    this._updateFullScreen();

                } else {
                    document.documentElement.style.overflow = this._bakHtmlOverflow;
                    document.body.style.overflow = this._bakBodyOverflow;
                    this.getDom().style.cssText = this._bakCssText;
                    this.getDom('iframeholder').style.cssText = this._bakCssText1;
                    if (this._bakAutoHeight) {
                        this.editor.enableAutoHeight();
                    }
                    window.scrollTo(0, this._bakScrollTop);

                }
                if(baidu.editor.browser.gecko){
                    
                    var input = document.createElement('input');
                       
                    document.body.appendChild(input);

                    editor.body.contentEditable = false;
                    setTimeout(function(){

                        input.focus();
                        setTimeout(function(){
                            editor.body.contentEditable = true;
                            editor.selection.getRange().moveToBookmark(bk).select();
                            baidu.editor.dom.domUtils.remove(input)
                        })

                    })
                }
                    
                this.editor.fireEvent('fullscreenchanged', fullscreen);
                this.triggerLayout();
            }
        },
        _wordCount:function(){
            var wdcount = this.getDom('wordcount');
            if(!this.editor.options.wordCount) {
                wdcount.style.display="none";
                return;
            }
            wdcount.innerHTML = editor.queryCommandValue("wordcount");
        },
        disableWordCount: function (){
            var w = this.getDom('wordcount');
            w.innerHTML = '';
            w.style.display = 'none';
            this.wordcount = false;

        },
        enableWordCount: function (){
            var w = this.getDom('wordcount');
            w.style.display = '';
            this.wordcount = true;
            this._wordCount();
        },
        _updateFullScreen: function (){
            if (this._fullscreen) {
                var vpRect = uiUtils.getViewportRect();
                this.getDom().style.cssText = 'border:0;position:absolute;left:0;top:0;width:'+vpRect.width+'px;height:'+vpRect.height+'px;';
                uiUtils.setViewportOffset(this.getDom(), { left: 0, top: 0 });
                this.editor.setHeight(vpRect.height - this.getDom('toolbarbox').offsetHeight - this.getDom('bottombar').offsetHeight);
            }
        },
        _updateElementPath: function (){
            var bottom = this.getDom('elementpath');
            if (this.elementPathEnabled) {
                var list = this.editor.queryCommandValue('elementpath');
                var buff = [];
                for(var i=0,ci;ci=list[i];i++){
                    buff[i] = this.formatHtml('<span unselectable="on" onclick="$$.editor.execCommand(&quot;elementpath&quot;, &quot;'+ i +'&quot;);">' + ci + '</span>');
                }
                bottom.innerHTML = '<div class="edui-editor-breadcrumb" onmousedown="return false;">path: ' + buff.join(' &gt; ') + '</div>';
                
            }else{
                bottom.style.display = 'none'
            }
        },
        disableElementPath: function (){
            var bottom = this.getDom('elementpath');
            bottom.innerHTML = '';
            bottom.style.display = 'none';
            this.elementPathEnabled = false;

        },
        enableElementPath: function (){
            var bottom = this.getDom('elementpath');
            bottom.style.display = '';
            this.elementPathEnabled = true;
            this._updateElementPath();
        },
        isFullScreen: function (){
            return this._fullscreen;
        },
        postRender: function (){
            UIBase.prototype.postRender.call(this);
            for (var i=0; i<this.toolbars.length; i++) {
                this.toolbars[i].postRender();
            }
            var me = this;
            var timerId;
            baidu.editor.dom.domUtils.on(window, 'resize', function (){
                clearTimeout(timerId);
                timerId = setTimeout(function (){
                    me._updateFullScreen();
                });
            });
        },
        showToolbarMsg: function (msg){
            this.getDom('toolbarmsg_label').innerHTML = msg;
            this.getDom('toolbarmsg').style.display = '';
        },
        hideToolbarMsg: function (){
            this.getDom('toolbarmsg').style.display = 'none';
        },
        mapUrl: function (url){
            return url.replace('~/', this.editor.options.UEDITOR_HOME_URL || '');
        },
        triggerLayout: function (){
            var dom = this.getDom();
            if (dom.style.zoom == '1') {
                dom.style.zoom = '100%';
            } else {
                dom.style.zoom = '1';
            }
        }
    };
    utils.inherits(EditorUI, baidu.editor.ui.UIBase);

    baidu.editor.ui.Editor = function (options){
        
        var editor = new baidu.editor.Editor(options);
        editor.options.editor = editor;
        new EditorUI(editor.options);
        
        var oldRender = editor.render;
        editor.render = function (holder){
            if(holder){
                if (holder.constructor === String) {
                    holder = document.getElementById(holder);
                }

                if(holder && holder.tagName.toLowerCase() == 'textarea'){
                    var newDiv = document.createElement('div');
                    holder.parentNode.insertBefore(newDiv,holder);
                    if(holder.value){
                        editor.options.initialContent = holder.value;
                    }
                    newDiv.id = holder.id;
                    holder.parentNode.removeChild(holder);
                    holder = newDiv;
                    holder.innerHTML = '';
                }
            }


            editor.ui.render(holder);
            var iframeholder = editor.ui.getDom('iframeholder');
            return oldRender.call(this, iframeholder);
        };
        return editor;
    };
})();