///import core
///commands 格式
/**
 * 段落样式
 * @function
 * @name baidu.editor.execCommand
 * @param   {String}   cmdName     paragraph插入段落执行命令
 * @param   {String}   style               标签值为：'p', 'h1', 'h2', 'h3', 'h4', 'h5', 'h6'
 * @param   {String}   attrs               标签的属性
 * @author zhanyi
 */
(function() {
    var domUtils = baidu.editor.dom.domUtils,
        block = domUtils.isBlockElm,
        notExchange = ['TD','LI','PRE'],
        utils = baidu.editor.utils,
        browser = baidu.editor.browser,
        
        doParagraph = function(range,style,attrs){
            var bookmark = range.createBookmark(),
                filterFn = function( node ) {
                    return   node.nodeType == 1 ? node.tagName.toLowerCase() != 'br' &&  !domUtils.isBookmarkNode(node) : !domUtils.isWhitespace( node )
                },
                para;

            range.enlarge( true );
            var bookmark2 = range.createBookmark(),
                current = domUtils.getNextDomNode( bookmark2.start, false, filterFn ),
                tmpRange = range.cloneRange(),
                tmpNode;
            while ( current && !(domUtils.getPosition( current, bookmark2.end ) & domUtils.POSITION_FOLLOWING) ) {
                if ( current.nodeType == 3 || !block( current ) ) {
                    tmpRange.setStartBefore( current );
                    while ( current && current !== bookmark2.end && !block( current ) ) {
                        tmpNode = current;
                        current = domUtils.getNextDomNode( current, false, null, function( node ) {
                            return !block( node )
                        } );
                    }
                    tmpRange.setEndAfter( tmpNode );
                    
                    para = range.document.createElement( style );
                    if(attrs){
                        for(var pro in attrs){
                            para.style[pro] = attrs[pro];
                        }
                    }
                    para.appendChild( tmpRange.extractContents() );

                    tmpRange.insertNode( para );

                    var parent = para.parentNode;
                    //如果para上一级是一个block元素且不是body,td就删除它
                    if ( block( parent ) && !domUtils.isBody( para.parentNode ) && utils.indexOf(notExchange,parent.tagName)==-1) {
                        //存储dir,style

                        parent.getAttribute('dir') && para.setAttribute('dir',parent.getAttribute('dir'));
                        //trace:1070
                        parent.style.cssText && (para.style.cssText = parent.style.cssText + ';' + para.style.cssText);
                        //trace:1030
                        parent.style.textAlign && !para.style.textAlign && (para.style.textAlign = parent.style.textAlign);
                        parent.style.textIndent && !para.style.textIndent && (para.style.textIndent = parent.style.textIndent);
                        parent.style.padding && !para.style.padding && (para.style.padding = parent.style.padding);
                        if(attrs && /h\d/i.test(parent.tagName)){
                           for(var pro in attrs){
                                parent.style[pro] = attrs[pro];
                           }
                            domUtils.remove(para,true);
                            para = parent;
                        }else
                            domUtils.remove( para.parentNode, true );

                    }
                    if(  utils.indexOf(notExchange,parent.tagName)!=-1){
                        current = parent;
                    }else{
                       current = para;
                    }


                    current = domUtils.getNextDomNode( current, false, filterFn );
                } else {
                    current = domUtils.getNextDomNode( current, true, filterFn );
                }
            }
            return range.moveToBookmark( bookmark2 ).moveToBookmark( bookmark );
        };

    baidu.editor.commands['paragraph'] = {
        execCommand : function( cmdName, style,attrs ) {
            var range = new baidu.editor.dom.Range(this.document);
            if(this.currentSelectedArr && this.currentSelectedArr.length > 0){
                for(var i=0,ti;ti=this.currentSelectedArr[i++];){
                    //trace:1079 不显示的不处理，插入文本，空的td也能加上相应的标签
                    if(ti.style.display == 'none') continue;
                    if(domUtils.isEmptyNode(ti)){
                      
                        var tmpTxt = this.document.createTextNode('paragraph');
                        ti.innerHTML = '';
                        ti.appendChild(tmpTxt);
                    }
                    doParagraph(range.selectNodeContents(ti),style,attrs);
                    if(tmpTxt){
                        var pN = tmpTxt.parentNode;
                        domUtils.remove(tmpTxt);
                        //ie下空的block 不显示
                        if(browser.ie && !pN.firstChild){
                            pN.innerHTML = domUtils.fillChar;
                        }    
                    }


                }
                range.selectNode(this.currentSelectedArr[0]).select()
            }else{
                range = this.selection.getRange();
                 //闭合时单独处理
                if(range.collapsed){
                    var txt = this.document.createTextNode('p');
                    range.insertNode(txt);
                    //去掉冗余的fillchar
                    if(browser.ie){
                        var node = txt.previousSibling;
                        if(node && domUtils.isWhitespace(node)){
                            domUtils.remove(node)
                        }
                        node = txt.nextSibling;
                        if(node && domUtils.isWhitespace(node)){
                            domUtils.remove(node)
                        } 
                    }

                }
                range = doParagraph(range,style,attrs);
                if(txt){
                    range.setStartBefore(txt).collapse(true);
                    domUtils.remove(txt);
                }

                if(browser.gecko && range.collapsed && range.startContainer.nodeType == 1){
                    var child = range.startContainer.childNodes[range.startOffset];
                    if(child && child.nodeType == 1 && child.tagName.toLowerCase() == style){
                        range.setStart(child,0).collapse(true)
                    }
                }
                //trace:1097 原来有true，原因忘了，但去了就不能清除多余的占位符了
                range.select()

            }
            return true;
        },
        queryCommandValue : function() {
            var startNode = this.selection.getStart(),
                parent =  domUtils.findParentByTagName( startNode, ['p','h1','h2','h3','h4','h5','h6'], true );

            return  parent && parent.tagName.toLowerCase();
        },
        queryCommandState : function(){
            return this.highlight ? -1 :0;
        }
    }


})();
