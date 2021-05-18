///import editor.js
///import core/utils.js
///import core/EventBase.js
///import core/browser.js
///import core/dom/dom.js
///import core/dom/domUtils.js
///import core/dom/Selection.js
///import core/dom/dtd.js
(function () {
    baidu.editor.Editor = Editor;

    var editor = baidu.editor,
        utils = editor.utils,
        EventBase = editor.EventBase,
        domUtils = editor.dom.domUtils,
        Selection = editor.dom.Selection,
        ie = editor.browser.ie,
        uid = 0,
        browser = editor.browser,
        dtd = editor.dom.dtd;

    /**
     * 编辑器类
     * @public
     * @class
     * @extends baidu.editor.EventBase
     * @name baidu.editor.Editor
     * @param {Object} options
     * @config {String}         id     将编辑器渲染到容器的id
     * @config {String}         initialStyle     编辑器内部样式
     * @config {String}         initialContent   初始化编辑器的内容
     * @config {String}         iframeCssUrl   要引入css的url
     * @config {String}         removeFormatTags   清除格式删除的标签
     * @config {String}         removeFormatAttributes   清除格式删除的属性
     * @config {String}         enterTag   编辑器回车标签。p或br
     * @config {Number}         maxUndoCount   最多可以回退的次数
     * @config {Number}         maxInputCount   当输入的字符数超过该值时，保存一次现场
     * @config {String}         selectedTdClass   设定选中td的样式名称
     * @config {Boolean}         pasteplain   是否纯文本粘贴。false为不使用纯文本粘贴，true为使用纯文本粘贴
     * @config {String}         textarea   提交表单时，服务器获取编辑器提交内容的所用的参数
     * @config {Boolean}         focus   初始化时，是否让编辑器获得焦点true或false
     * @config {String}         indentValue   初始化时，首行缩进距离
     */
    function Editor( options ) {
        var me = this;
        me.uid = uid ++;
        EventBase.call( me );
        me.commands = {};
        me.options = utils.extend( options || {}, UEDITOR_CONFIG, true );
        me.initPlugins();
    }

   
    Editor.prototype = /**@lends baidu.editor.Editor.prototype*/{

        /**
         * 渲染编辑器的DOM到指定容器，必须且只能调用一次
         * @public
         * @function
         * @param {Element|String} container
         */
        render : function ( container ) {
            if (container.constructor === String) {
                container = document.getElementById(container);
            }
            container.innerHTML = '<iframe id="' + 'baidu_editor_' + this.uid + '"' + 'width="100%" height="100%" scroll="no" frameborder="0"></iframe>';
            container.style.overflow = 'hidden';
            this._setup( container.firstChild.contentWindow.document );
        },

        _setup: function ( doc ) {
            var options = this.options,
                me = this;
            doc.open();
            var useBodyAsViewport = ie && browser.version < 9;
            doc.write( (  options.iframeDocType || (ie && browser.version < 9 ? '' : '<!DOCTYPE html>') ) +
                '<html xmlns="http://www.w3.org/1999/xhtml"' + (!useBodyAsViewport ? ' class="view"' : '')  + '><head>' +
                ( options.iframeCssUrl ? '<link rel="stylesheet" type="text/css" href="' + utils.unhtml( options.iframeCssUrl ) + '"/>' : '' ) +
                '<style type="text/css">'
                + ( options.initialStyle ||' ' ) +
                '</style></head><body' + (useBodyAsViewport ? ' class="view"' : '')  + '></body></html>' );
            doc.close();
            if ( ie ) {
                doc.body.disabled = true;
                doc.body.contentEditable = true;
                doc.body.disabled = false;
            } else {
                doc.body.contentEditable = true;
                doc.body.spellcheck = false;
            }
            this.document = doc;
            this.window = doc.defaultView || doc.parentWindow;

            this.iframe = this.window.frameElement;
            this.body = doc.body;
            if (this.options.minFrameHeight) {
                this.setHeight(this.options.minFrameHeight);
                this.body.style.height = this.options.minFrameHeight;
            }
            this.selection = new Selection( doc );
            this._initEvents();
            if(me.options.initialContent){
                if(me.options.autoClearinitialContent){
                    var oldExecCommand = me.execCommand;
                    me.execCommand = function(){
                        me.fireEvent('firstBeforeExecCommand');
                        oldExecCommand.apply(me,arguments)
                    };
                    this.setDefaultContent(this.options.initialContent);
                }else
                    this.setContent(this.options.initialContent,true);
            }
            //为form提交提供一个隐藏的textarea
            for(var form = this.iframe.parentNode;!domUtils.isBody(form);form = form.parentNode){

                if(form.tagName == 'FORM'){
                    domUtils.on(form,'submit',function(){

                        var textarea = document.getElementById('ueditor_textarea_' + me.options.textarea);

                        if(!textarea){
                            textarea = document.createElement('textarea');
                            textarea.setAttribute('name',me.options.textarea);
                            textarea.id = 'ueditor_textarea_' + me.options.textarea;
                            textarea.style.display = 'none';
                            this.appendChild(textarea);
                        }
                        textarea.value = me.getContent();

                    });
                    break;
                }
            }
            //编辑器不能为空内容
            
            if(domUtils.isEmptyNode(me.body)){
                
                this.body.innerHTML = '<p>'+(browser.ie?domUtils.fillChar:'<br/>')+'</p>';
            }
            //如果要求focus, 就把光标定位到内容开始
            if(me.options.focus){
                setTimeout(function(){
                    me.selection.getRange().setStartBefore(me.body.firstChild).setCursor(false,true);
                    //如果自动清除开着，就不需要做selectionchange;
                    !me.options.autoClearinitialContent &&  me._selectionChange()
                });


            }


            this.fireEvent( 'ready' );


        },
        /**
         * 创建textarea,同步编辑的内容到textarea,为后台获取内容做准备
         * @param formId 制定在那个form下添加
         * @public
         * @function
         */

        sync : function(formId){
            var me = this,
                form;
            function setValue(form){
                var textarea = document.getElementById('ueditor_textarea_' + me.options.textarea);

                if(!textarea){
                    textarea = document.createElement('textarea');
                    textarea.setAttribute('name',me.options.textarea);
                    textarea.id = 'ueditor_textarea_' + me.options.textarea;
                    textarea.style.display = 'none';
                    form.appendChild(textarea);
                }
                textarea.value = me.getContent();
            }
            if(formId){
                form = document.getElementById(formId);
                form && setValue(form);
            }else{
                for(form = me.iframe.parentNode;!domUtils.isBody(form);form = form.parentNode){
                    if(form.tagName == 'FORM'){
                        setValue(form);
                        break;
                    }
                }
            }

        },
        /**
         * 设置编辑器高度
         * @public
         * @function
         * @param {Number} height    高度
         */
        setHeight: function (height){
            if (height !== parseInt(this.iframe.parentNode.style.height)){
                this.iframe.parentNode.style.height = height + 'px';

            }
            //ie9下body 高度100%失效，改为手动设置
            if(browser.ie && browser.version == 9){
                this.document.body.style.height = height - 20 + 'px'
            }
        },

        /**
         * 获取编辑器内容
         * @public
         * @function
         * @returns {String}
         */
        getContent : function (cmd) {
            this.fireEvent( 'beforegetcontent',cmd );
            var reg = new RegExp( domUtils.fillChar, 'g' ),
                html = this.document.body.innerHTML.replace(reg,'');
            this.fireEvent( 'aftergetcontent',cmd );
            if (this.serialize) {
                var node = this.serialize.parseHTML(html);
                node = this.serialize.transformOutput(node);
                html = this.serialize.toHTML(node);
            }
            return html;
        },

        /**
         * 获取编辑器中的文本内容
         * @public
         * @function
         * @returns {String}
         */
        getContentTxt : function(){
            var reg = new RegExp( domUtils.fillChar,'g' );
            return this.body[browser.ie ? 'innerText':'textContent'].replace(reg,'')
        },

        /**
         * 设置编辑器内容
         * @public
         * @function
         * @param {String} html
         */
        setContent : function ( html,notFireSelectionchange) {
            var me = this;
            me.fireEvent( 'beforesetcontent' );
            var serialize = this.serialize;
            if (serialize) {
                var node = serialize.parseHTML(html);
                node = serialize.transformInput(node);
                node = serialize.filter(node);
                html = serialize.toHTML(node);
            }
            this.document.body.innerHTML = html;
            //给文本或者inline节点套p标签
            if(me.options.enterTag == 'p'){
                var child = this.body.firstChild,
                    p = me.document.createElement('p'),
                    tmpNode;
                if(!child){
                    this.body.innerHTML = '<p>'+(browser.ie ? '' :'<br/>')+'</p>'
                }else{
                     while(child){
                        if(child.nodeType ==3 || child.nodeType == 1 && dtd.p[child.tagName]){
                            tmpNode = child.nextSibling;

                            p.appendChild(child);
                            child = tmpNode;
                            if(!child){
                                me.body.appendChild(p)
                            }
                        }else{
                            if(p.firstChild){
                                me.body.insertBefore(p,child);
                                p = me.document.createElement('p')


                            }
                            child = child.nextSibling
                        }


                    }
                }


            }

            me.adjustTable && me.adjustTable(me.body);
            me.fireEvent( 'aftersetcontent' );
            !notFireSelectionchange && me._selectionChange();
        },

        /**
         * 让编辑器获得焦点
         * @public
         * @function
         */
        focus : function () {

            domUtils.getWindow( this.document ).focus();

        },

        /**
         * 加载插件
         * @private
         * @function
         * @param {Array} plugins
         */
        initPlugins : function ( plugins ) {
            var fn,originals = baidu.editor.plugins;
            if ( plugins ) {
                for ( var i = 0,pi; pi = plugins[i++]; ) {
                    if ( utils.indexOf( this.options.plugins, pi ) == -1 && (fn = baidu.editor.plugins[pi]) ) {
                        this.options.plugins.push( pi );
                        fn.call( this )
                    }
                }
            } else {

                plugins = this.options.plugins;

                if ( plugins ) {
                    for ( i = 0; pi = originals[plugins[i++]]; ) {
                        pi.call( this )
                    }
                } else {
                    this.options.plugins = [];
                    for ( pi in originals ) {
                        this.options.plugins.push( pi );
                        originals[pi].call( this )
                    }
                }
            }


        },
         /**
         * 初始化事件，绑定selectionchange
         * @private
         * @function
         */
        _initEvents : function () {
            var me = this,
                doc = me.document,
                win = me.window;
            me._proxyDomEvent = utils.bind( me._proxyDomEvent, me );
            domUtils.on( doc, ['click',  'contextmenu','mousedown','keydown', 'keyup','keypress', 'mouseup', 'mouseover', 'mouseout', 'selectstart'], me._proxyDomEvent );

            domUtils.on( win, ['focus', 'blur'], me._proxyDomEvent );

            domUtils.on( doc, ['mouseup','keydown'], function(evt){

                //特殊键不触发selectionchange
                if(evt.type == 'keydown' && (evt.ctrlKey || evt.metaKey || evt.shiftKey || evt.altKey)){
                    return;
                }
                if(evt.button == 2)return;
                me._selectionChange(250, evt );
            });


            //处理拖拽
            //ie ff不能从外边拖入
            //chrome只针对从外边拖入的内容过滤
            var innerDrag = 0,source = browser.ie ? me.body : me.document,dragoverHandler;

            domUtils.on(source,'dragstart',function(){
                innerDrag = 1;
            });

            domUtils.on(source,browser.webkit ? 'dragover' : 'drop',function(){
                return browser.webkit ?
                    function(){
                        clearTimeout( dragoverHandler );
                        dragoverHandler = setTimeout( function(){
                            if(!innerDrag){
                                var sel = me.selection,
                                    range = sel.getRange();
                                if(range){
                                    var common = range.getCommonAncestor();
                                    if(common && me.serialize){
                                        var f = me.serialize,
                                            node =
                                                f.filter(
                                                    f.transformInput(
                                                        f.parseHTML(
                                                            f.word(common.innerHTML)
                                                        )
                                                    )
                                                )
                                        common.innerHTML = f.toHTML(node)
                                    }

                                }
                            }
                            innerDrag = 0;
                        }, 200 );
                    } :
                    function(e){

                        if(!innerDrag){
                            e.preventDefault ? e.preventDefault() :(e.returnValue = false) ;

                        }
                        innerDrag = 0;
                    }

            }());

        },
        _proxyDomEvent: function ( evt ) {

            return this.fireEvent( evt.type.replace( /^on/, '' ), evt );
        },

        _selectionChange : function ( delay, evt ) {

            var me = this;
            if (evt) {
                evt = {
                    type: evt.type,
                    clientX: evt.clientX,
                    clientY: evt.clientY
                };
            }
            clearTimeout(this._selectionChangeTimer);
            this._selectionChangeTimer = setTimeout(function(){

                //修复一个IE下的bug: 鼠标点击一段已选择的文本中间时，可能在mouseup后的一段时间内取到的range是在selection的type为None下的错误值.
                //IE下如果用户是拖拽一段已选择文本，则不会触发mouseup事件，所以这里的特殊处理不会对其有影响
                var ieRange;
                if (evt && browser.ie && browser.version < 9 && evt.type == 'mouseup' && me.selection.getNative().type == 'None' ) {
                    ieRange = me.document.body.createTextRange();
                    try {
                        ieRange.moveToPoint( evt.clientX, evt.clientY );
                    } catch(ex){
                        ieRange = null;
                    }
                }
                var bakGetIERange;
                if (ieRange) {
                    bakGetIERange = me.selection.getIERange;
                    me.selection.getIERange = function (){
                        return ieRange;
                    };
                }
                me.selection.cache();
                if (bakGetIERange) {
                    me.selection.getIERange = bakGetIERange;
                }
                if ( me.selection._cachedRange && me.selection._cachedStartElement ) {
                    me.fireEvent( 'beforeselectionchange' );
                    // 第二个参数causeByUi为true代表由用户交互造成的selectionchange.
                    me.fireEvent( 'selectionchange', !!evt );
                    me.fireEvent('afterselectionchange');
                    me.selection.clear();
                }
            }, delay || 50);

        },

        _callCmdFn: function ( fnName, args ) {
            var cmdName = args[0].toLowerCase(),
                cmd, cmdFn;
            cmdFn = ( cmd = this.commands[cmdName] ) && cmd[fnName] ||
                ( cmd = baidu.editor.commands[cmdName]) && cmd[fnName];
            if ( cmd && !cmdFn && fnName == 'queryCommandState' ) {
                return false;
            } else if ( cmdFn ) {
                return cmdFn.apply( this, args );
            }
        },

        /**
         * 执行命令
         * @public
         * @function
         * @param {String} cmdName 执行的命令名
         * 
         */
        execCommand : function ( cmdName ) {
            cmdName = cmdName.toLowerCase();
            var me = this,
                result,
                cmd = me.commands[cmdName] || baidu.editor.commands[cmdName];
            if ( !cmd || !cmd.execCommand ) {
                return;
            }

            if ( !cmd.notNeedUndo && !me.__hasEnterExecCommand ) {
                me.__hasEnterExecCommand = true;
                me.fireEvent( 'beforeexeccommand', cmdName );
                result = this._callCmdFn( 'execCommand', arguments );
                me.fireEvent( 'afterexeccommand', cmdName );
                me.__hasEnterExecCommand = false;
            } else {
                result = this._callCmdFn( 'execCommand', arguments );
            }
            me._selectionChange();
            return result;
        },

        /**
         * 查询命令的状态
         * @public
         * @function
         * @param {String} cmdName 执行的命令名
         * @returns {Number|*} -1 : disabled, false : normal, true : enabled.
         * 
         */
        queryCommandState : function ( cmdName ) {
            return this._callCmdFn( 'queryCommandState', arguments );
        },

        /**
         * 查询命令的值
         * @public
         * @function
         * @param {String} cmdName 执行的命令名
         * @returns {*}
         */
        queryCommandValue : function ( cmdName ) {
            return this._callCmdFn( 'queryCommandValue', arguments );
        },
        /**
         * 检查编辑区域中是否有内容
         * @public
         * @params{Array} 自定义的标签
         * @function
         * @returns {Boolean} true 有,false 没有
         */
        hasContents : function(tags){
            if(tags){
               for(var i=0,ci;ci=tags[i++];){
                    if(this.document.getElementsByTagName(ci).length > 0)
                        return true;
               }
            }
            if(!domUtils.isEmptyBlock(this.body)){
                return true
            }

            return false;
        },
        /**
         * 从新设置
         * @public
         * @function
         */
        reset : function(){
            this.fireEvent('reset');
        },
        /**
         * 设置默认内容
         * @function
         * @param    {String}    cont     要存入的内容
         */
        setDefaultContent : function(){
             function clear(type){
                var me = this;
                
                if(me.document.getElementById('initContent')){
                    me.document.body.innerHTML = '<p>'+(baidu.editor.browser.ie ? '' : '<br/>')+'</p>';
                    var range = me.selection.getRange();

                    me.removeListener('firstBeforeExecCommand',clear);
                    me.removeListener('focus',clear);
                    setTimeout(function(){
                        range.setStart(me.document.body.firstChild,0).collapse(true).select(true);
                        me._selectionChange();
                    })


                }
            }
            return function (cont){
                var me = this;
                me.document.body.innerHTML = '<p id="initContent">'+cont+'</p>';
                me.addListener('firstBeforeExecCommand',clear);
                me.addListener('mousedown',clear);
            }


        }()

    };
    utils.inherits( Editor, EventBase );
})();
