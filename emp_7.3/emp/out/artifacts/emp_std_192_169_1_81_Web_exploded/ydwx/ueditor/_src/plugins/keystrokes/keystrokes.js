/*
 *   处理特殊键的兼容性问题
 */
(function() {
    var domUtils = baidu.editor.dom.domUtils,
        browser = baidu.editor.browser,
        dtd = baidu.editor.dom.dtd,
        utils = baidu.editor.utils,
        flag = 0,
        keys = domUtils.keys,
        trans = {
            'B' : 'strong',
            'I' : 'em',
            'FONT' : 'span'
        },
        sizeMap = [0, 10, 12, 16, 18, 24, 32, 48],
        listStyle = {
            'OL':['decimal','lower-alpha','lower-roman','upper-alpha','upper-roman'],

            'UL':[ 'circle','disc','square']
        };

    baidu.editor.plugins['keystrokes'] = function() {
        var me = this;
        me.addListener('keydown', function(type, evt) {
            var keyCode = evt.keyCode || evt.which;


            //处理backspace/del
            if (keyCode == 8 || keyCode == 46) {


                var range = me.selection.getRange(),
                    tmpRange,
                    start,end;

                //当删除到body最开始的位置时，会删除到body,阻止这个动作
                if(range.collapsed){
                    start = range.startContainer;
                    //有可能是展位符号
                    if(domUtils.isWhitespace(start)){
                        start = start.parentNode;
                    }
                    if(domUtils.isEmptyNode(start) && start === me.body.firstChild){
                        if(start.tagName != 'P'){
                            p = me.document.createElement('p');
                            me.body.insertBefore(p,start);
                            p.innerHTML = browser.ie ? '' : '<br/>';
                            range.setStart(p,0).setCursor();

                        }
                        evt.preventDefault ? evt.preventDefault() : (evt.returnValue = false);
                        return;

                    }
                }

                if (range.collapsed && range.startContainer.nodeType == 3 && range.startContainer.nodeValue.replace(new RegExp(domUtils.fillChar, 'g'), '').length == 0) {
                    range.setStartBefore(range.startContainer).collapse(true)
                }
                //解决选中control元素不能删除的问题
                if (start = range.getClosedNode()) {
                    me.undoManger && me.undoManger.save();
                    range.setStartBefore(start);
                    domUtils.remove(start);
                    range.setCursor();
                    me.undoManger && me.undoManger.save();
                    evt.preventDefault ? evt.preventDefault() : (evt.returnValue = false);
                    return;
                }
                //阻止在table上的删除
                if (!browser.ie) {

                    start = domUtils.findParentByTagName(range.startContainer, 'table', true);
                    end = domUtils.findParentByTagName(range.endContainer, 'table', true);
                    if (start && !end || !start && end || start !== end) {
                        evt.preventDefault();
                        return;
                    }
                    if (browser.webkit && range.collapsed && start) {
                        tmpRange = range.cloneRange().txtToElmBoundary();
                        start = tmpRange.startContainer;

                        if (domUtils.isBlockElm(start) && start.nodeType == 1 && !dtd.$tableContent[start.tagName] && !domUtils.getChildCount(start, function(node) {
                            return node.nodeType == 1 ? node.tagName !== 'BR' : 1;
                        })) {

                            tmpRange.setStartBefore(start).setCursor();
                            domUtils.remove(start, true);
                            evt.preventDefault();
                            return;
                        }
                    }
                }
                //修中ie中li下的问题
                if (range.collapsed && !range.startOffset) {
                    tmpRange = range.cloneRange().trimBoundary();
                    var li = domUtils.findParentByTagName(range.startContainer, 'li', true),pre;


                    //要在li的最左边，才能处理
                    if (li && !tmpRange.startOffset) {

                        if (li && (pre = li.previousSibling)) {
                            if (keyCode == 46 && li.childNodes.length)
                                return;
                            me.undoManger && me.undoManger.save();
                            var first = li.firstChild;
                            if (domUtils.isBlockElm(first)) {
                                if (domUtils.isEmptyNode(first)) {
                                    range.setEnd(pre, pre.childNodes.length).shrinkBoundary().collapse().select(true);

                                } else {
                                    span = me.document.createElement('span');
                                    range.insertNode(span);
                                    if (domUtils.isBlockElm(pre.firstChild)) {

                                        pre.firstChild.appendChild(span);
                                        while (first.firstChild) {
                                            pre.firstChild.appendChild(first.firstChild);
                                        }

                                    } else {
                                        while (first.firstChild) {
                                            pre.appendChild(first.firstChild);
                                        }
                                    }

                                    range.setStartBefore(span).collapse(true).select(true);

                                    domUtils.remove(span)

                                }
                            } else {
                                if (domUtils.isEmptyNode(li)) {

                                    range.setEnd(pre, pre.childNodes.length).shrinkBoundary().collapse().select(true);
                                } else {
                                    range.setEnd(pre, pre.childNodes.length).collapse().select(true);
                                    while (li.firstChild) {
                                        pre.appendChild(li.firstChild)
                                    }


                                }
                            }

                            domUtils.remove(li);

                            me.undoManger && me.undoManger.save();
                            evt.preventDefault ? evt.preventDefault() : (evt.returnValue = false);
                            return;

                        }

//
//                            if( keyCode == 8 && (li && pre || li && domUtils.getChildCount(li,function(node){
//                                return !domUtils.isBr(node) && !domUtils.isWhitespace(node);
//                            })) ){
//                                evt.returnValue = false;
//                                return;
//                            }

                        //trace:980

                        if (li && !li.previousSibling) {
                            first = li.firstChild;
                            if (!first || domUtils.isEmptyNode(domUtils.isBlockElm(first) ? first : li)) {
                                var p = me.document.createElement('p');

                                li.parentNode.parentNode.insertBefore(p, li.parentNode);
                                p.innerHTML = browser.ie ? '' : '<br/>';
                                range.setStart(p, 0).setCursor();
                                domUtils.remove(!li.nextSibling ? li.parentNode : li);
                                me.undoManger && me.undoManger.save();
                                evt.preventDefault ? evt.preventDefault() : (evt.returnValue = false)
                                return;
                            }


                        }


                    }


                }


                if (me.undoManger) {

                    if (!range.collapsed) {
                        me.undoManger.save();
                        flag = 1;
                    }
                }

            }
            //处理tab键的逻辑
            if (keyCode == 9) {
                range = me.selection.getRange();
                me.undoManger && me.undoManger.save();
                for (var i = 0,txt = ''; i < me.options.tabSize; i++) {
                    txt += me.options.tabNode;
                }
                var span = me.document.createElement('span');
                span.innerHTML = txt;
                if (range.collapsed) {

                    li = domUtils.findParentByTagName(range.startContainer, 'li', true);

                    if (li && domUtils.isStartInblock(range)) {
                        bk = range.createBookmark();
                        var parentLi = li.parentNode,
                            list = me.document.createElement(parentLi.tagName);
                        var index = utils.indexOf(listStyle[list.tagName], domUtils.getComputedStyle(parentLi, 'list-style-type'));
                        index = index + 1 == listStyle[list.tagName].length ? 0 : index + 1;
                        domUtils.setStyle(list, 'list-style-type', listStyle[list.tagName][index]);

                        parentLi.insertBefore(list, li);
                        list.appendChild(li);
                        range.moveToBookmark(bk).select()

                    } else
                        range.insertNode(span.cloneNode(true).firstChild).setCursor(true);

                } else {
                    //处理table
                    start = domUtils.findParentByTagName(range.startContainer, 'table', true);
                    end = domUtils.findParentByTagName(range.endContainer, 'table', true);
                    if (start || end) {
                        evt.preventDefault ? evt.preventDefault() : (evt.returnValue = false);
                        return
                    }
                    //处理列表 再一个list里处理
                    start = domUtils.findParentByTagName(range.startContainer, ['ol','ul'], true);
                    end = domUtils.findParentByTagName(range.endContainer, ['ol','ul'], true);
                    if (start && end && start === end) {
                        var bk = range.createBookmark();
                        start = domUtils.findParentByTagName(range.startContainer, 'li', true);
                        end = domUtils.findParentByTagName(range.endContainer, 'li', true);
                        //在开始单独处理
                        if (start === start.parentNode.firstChild) {
                            var parentList = me.document.createElement(start.parentNode.tagName);

                            start.parentNode.parentNode.insertBefore(parentList, start.parentNode);
                            parentList.appendChild(start.parentNode);
                        } else {
                            parentLi = start.parentNode,
                                list = me.document.createElement(parentLi.tagName);

                            index = utils.indexOf(listStyle[list.tagName], domUtils.getComputedStyle(parentLi, 'list-style-type'));
                            index = index + 1 == listStyle[list.tagName].length ? 0 : index + 1;
                            domUtils.setStyle(list, 'list-style-type', listStyle[list.tagName][index]);
                            start.parentNode.insertBefore(list, start);
                            var nextLi;
                            while (start !== end) {
                                nextLi = start.nextSibling;
                                list.appendChild(start);
                                start = nextLi;
                            }
                            list.appendChild(end);

                        }
                        range.moveToBookmark(bk).select();

                    } else {
                        if (start || end) {
                            evt.preventDefault ? evt.preventDefault() : (evt.returnValue = false);
                            return
                        }
                        //普通的情况
                        start = domUtils.findParent(range.startContainer, filterFn);
                        end = domUtils.findParent(range.endContainer, filterFn);
                        if (start && end && start === end) {
                            range.deleteContents();
                            range.insertNode(span.cloneNode(true).firstChild).setCursor(true);
                        } else {
                            var bookmark = range.createBookmark(),
                                filterFn = function(node) {
                                    return domUtils.isBlockElm(node);

                                };

                            range.enlarge(true);
                            var bookmark2 = range.createBookmark(),
                                current = domUtils.getNextDomNode(bookmark2.start, false, filterFn);


                            while (current && !(domUtils.getPosition(current, bookmark2.end) & domUtils.POSITION_FOLLOWING)) {


                                current.insertBefore(span.cloneNode(true).firstChild, current.firstChild);

                                current = domUtils.getNextDomNode(current, false, filterFn);

                            }

                            range.moveToBookmark(bookmark2).moveToBookmark(bookmark).select();
                        }

                    }


                }
                me.undoManger && me.undoManger.save();
                evt.preventDefault ? evt.preventDefault() : (evt.returnValue = false);
            }
        });
        me.addListener('keyup', function(type, evt) {

            var keyCode = evt.keyCode || evt.which;
            //修复ie/chrome <strong><em>x|</em></strong> 当点退格后在输入文字后会出现  <b><i>x</i></b>
            if (!browser.gecko && !keys[keyCode] && !evt.ctrlKey && !evt.metaKey && !evt.shiftKey && !evt.altKey) {
                range = me.selection.getRange();
                if (range.collapsed) {
                    var start = range.startContainer,
                        lastNode,
                        isFixed = 0;

                    while (!domUtils.isBlockElm(start)) {
                        if (start.nodeType == 1 && utils.indexOf(['FONT','B','I'], start.tagName) != -1) {

                            var tmpNode = me.document.createElement(trans[start.tagName]);
                            if (start.tagName == 'FONT') {
                                //chrome only remember color property
                                tmpNode.style.cssText = (start.getAttribute('size') ? 'font-size:' + (sizeMap[start.getAttribute('size')] || 12) + 'px' : '')
                                    + ';' + (start.getAttribute('color') ? 'color:' + start.getAttribute('color') : '')
                                    + ';' + (start.getAttribute('face') ? 'font-family:' + start.getAttribute('face') : '')
                                    + ';' + start.style.cssText;
                            }
                            while (start.firstChild) {
                                tmpNode.appendChild(start.firstChild)
                            }
                            start.parentNode.insertBefore(tmpNode, start);
                            domUtils.remove(start);
                            if (!isFixed) {
                                range.setEnd(tmpNode, tmpNode.childNodes.length).collapse(true)

                            }
                            start = tmpNode;
                            isFixed = 1;
                        }
                        start = start.parentNode;

                    }

                    isFixed && range.select()

                }
            }

            if (keyCode == 8 || keyCode == 46) {

                var range,body,start,parent,
                    tds = this.currentSelectedArr;
                if (tds && tds.length > 0) {
                    for (var i = 0,ti; ti = tds[i++];) {
                        ti.innerHTML = browser.ie ? ( browser.version < 9 ? '&#65279' : '' ) : '<br/>';

                    }
                    range = new baidu.editor.dom.Range(this.document);
                    range.setStart(tds[0], 0).setCursor();
                    if (flag) {
                        me.undoManger.save();
                        flag = 0;
                    }
                    //阻止chrome执行默认的动作
                    if (browser.webkit) {
                        evt.preventDefault();
                    }
                    return;
                }

                range = me.selection.getRange();

                //ctrl+a 后全部删除做处理
                
                if (domUtils.isEmptyBlock(me.document.body) && !range.startOffset) {
                    
                    me.document.body.innerHTML = '<p>'+(browser.ie?'':'<br/>')+'</p>';
                    range.setStart(me.document.body.firstChild,0).setCursor();
                    me.undoManger && me.undoManger.save();
                    return;
                }

                //处理删除不干净的问题

                start = range.startContainer;
                if(domUtils.isWhitespace(start)){
                    start = start.parentNode
                }
                
                while (start.nodeType == 1 && domUtils.isEmptyNode(start) && dtd.$removeEmpty[start.tagName]) {
                   
                    parent = start.parentNode;
                    domUtils.remove(start);
                    start = parent;
                }
                if (start.nodeType == 1 && domUtils.isEmptyNode(start)) {

                    //ie下的问题，虽然没有了相应的节点但一旦你输入文字还是会自动把删除的节点加上，
                    if (browser.ie) {

                        var span = range.document.createElement('span');
                        start.appendChild(span);
                        range.setStart(span, 0).setCursor();

                    } else {
                        start.innerHTML = '<br/>';
                        range.setStart(start, 0).setCursor(false,true);
                    }

                    setTimeout(function() {
                        if (browser.ie) {
                            domUtils.remove(span);
                        }
                        //range.setStart( start, 0 ).setCursor();
                        if (flag) {
                            me.undoManger.save();
                            flag = 0;
                        }
                    }, 0)
                } else {

                    if (flag) {
                        me.undoManger.save();
                        flag = 0;
                    }

                }
            }
        })
    }
})();