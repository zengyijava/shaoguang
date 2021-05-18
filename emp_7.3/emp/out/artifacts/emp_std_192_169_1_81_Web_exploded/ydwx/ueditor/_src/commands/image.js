/**
 * Created by .
 * User: zhanyi
 * for image
 */

(function (){
    var domUtils = baidu.editor.dom.domUtils,
        utils =  baidu.editor.utils,
        defaultValue = {
            left : 1,
            right : 1,
            center : 1
        },
        dtd = baidu.editor.dom.dtd;
    baidu.editor.commands['imagefloat'] = {
        execCommand : function (cmd, align){
            var range = this.selection.getRange();
            if(!range.collapsed ){
                var img = range.getClosedNode();
                if(img && img.tagName == 'IMG'){
                    switch (align){
                        case 'left':
                        case 'right':
                        case 'none':
                            var pN = img.parentNode,tmpNode,pre,next;
                            while(dtd.$inline[pN.tagName] || pN.tagName == 'A'){
                                pN = pN.parentNode;
                            }
                            tmpNode = pN;
                            if(tmpNode.tagName == 'P' && domUtils.getStyle(tmpNode,'text-align') == 'center'){
                                if(!domUtils.isBody(tmpNode) && domUtils.getChildCount(tmpNode,function(node){return !domUtils.isBr(node) && !domUtils.isWhitespace(node)}) == 1){
                                    pre = tmpNode.previousSibling;
                                    next = tmpNode.nextSibling;
                                    if(pre && next && pre.nodeType == 1 &&  next.nodeType == 1 && pre.tagName == next.tagName && domUtils.isBlockElm(pre)){
                                        pre.appendChild(tmpNode.firstChild);
                                        while(next.firstChild){
                                            pre.appendChild(next.firstChild)
                                        }
                                        domUtils.remove(tmpNode);
                                        domUtils.remove(next);
                                    }else{
                                        domUtils.setStyle(tmpNode,'text-align','')
                                    }


                                }

                                range.selectNode(img).select()
                            }
                            domUtils.setStyle(img,'float',align);
                            break;
                        case 'center':
                            if(this.queryCommandValue('imagefloat') != 'center'){
                                pN = img.parentNode;
                                domUtils.setStyle(img,'float','none');
                                tmpNode = img;
                                while(pN && domUtils.getChildCount(pN,function(node){return !domUtils.isBr(node) && !domUtils.isWhitespace(node)}) == 1
                                    && (dtd.$inline[pN.tagName] || pN.tagName == 'A')){
                                    tmpNode = pN;
                                    pN = pN.parentNode;
                                }
                                range.setStartBefore(tmpNode).setCursor(false);
                                pN = this.document.createElement('div');
                                pN.appendChild(tmpNode);
                                domUtils.setStyle(tmpNode,'float','');
                               
                                this.execCommand('insertHtml','<p id="_img_parent_tmp" style="text-align:center">'+pN.innerHTML+'</p>');

                                tmpNode = this.document.getElementById('_img_parent_tmp');
                                tmpNode.removeAttribute('id');
                                tmpNode = tmpNode.firstChild;
                                range.selectNode(tmpNode).select();
                                //去掉后边多余的元素
                                next = tmpNode.parentNode.nextSibling;
                                if(next && domUtils.isEmptyNode(next)){
                                    domUtils.remove(next)
                                }

                            }

                            break;
                    }

                }
            }
        },
        queryCommandValue : function() {
            var range = this.selection.getRange(),
                startNode,floatStyle;
            if(range.collapsed){
                return 'none';
            }
            startNode = range.getClosedNode();
            if(startNode && startNode.nodeType == 1 && startNode.tagName == 'IMG'){
                floatStyle = domUtils.getComputedStyle(startNode,'float');
                if(floatStyle == 'none'){
                    floatStyle = domUtils.getComputedStyle(startNode.parentNode,'text-align') == 'center' ? 'center' : floatStyle
                }
                return defaultValue[floatStyle] ? floatStyle : 'none'
            }
            return 'none'


        },
        queryCommandState : function(){
            if(this.highlight){
                       return -1;
                   }
            var range = this.selection.getRange(),
                startNode;
            if(range.collapsed){
                return -1;
            }
            startNode = range.getClosedNode();
            if(startNode && startNode.nodeType == 1 && startNode.tagName == 'IMG'){
                return 0;
            }
            return -1;
        }
    };

    baidu.editor.commands['insertimage'] = {
        execCommand : function (cmd, opt){
            
            opt = utils.isArray(opt) ? opt : [opt];

            var range = this.selection.getRange(),
                    img = range.getClosedNode();
            if(img && /img/i.test( img.tagName ) && img.className != "edui-faked-video" ){
                var floatStyle = opt[0]['floatStyle'];
                delete opt[0]['floatStyle'];
                domUtils.setAttributes(img,opt[0]);
                this.execCommand('imagefloat',floatStyle);
            }else{
                var html = [],str = '',ci;
                ci = opt[0];
                if(opt.length == 1){
                    str = '<img src="'+ci.src+'" '+
                            (ci.width ? 'width="'+ci.width+'" ':'') +
                            (ci.height ? ' height="'+ci.height+'" ':'') +
                            ' style="' + (ci['floatStyle']&&ci['floatStyle']!='center' ? 'float:'+ci['floatStyle']+';':'') +
                            (ci.border||'') + '" ' +
                            (ci.title?' title="'+ci.title+'"':'') + ' />';
                    if(ci['floatStyle'] == 'center'){
                            str = '<p style="text-align: center">'+str+'</p>'
                     }
                    html.push(str)
                            
                }else{
                    for(var i=0;ci=opt[i++];){
                        str =  '<p ' + (ci['floatStyle'] == 'center' ? 'style="text-align: center" ' : '') + '><img src="'+ci.src+'" '+
                            (ci.width ? 'width="'+ci.width+'" ':'') +
                            (ci.height ? ' height="'+ci.height+'" ':'') +
                            ' style="' + (ci['floatStyle']&&ci['floatStyle']!='center' ? 'float:'+ci['floatStyle']+';':'') +
                            (ci.border||'') + '" ' +
                            (ci.title?' title="'+ci.title+'"':'') + ' /></p>';
//                        if(ci['floatStyle'] == 'center'){
//                            str = '<p style="text-align: center">'+str+'</p>'
//                        }
                        html.push(str)
                    }
                }

                this.execCommand('insertHtml',html.join(''));
            }
        },
         queryCommandState : function(){
          return this.highlight ? -1 :0;
         }
    };
})();