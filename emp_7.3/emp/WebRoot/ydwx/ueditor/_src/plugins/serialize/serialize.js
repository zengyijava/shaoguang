(function (){




baidu.editor.plugins['serialize'] = function () {
    var editor = this;
    var domUtils = baidu.editor.dom.domUtils;
    var dtd = baidu.editor.dom.dtd;
    var EMPTY_TAG = dtd.$empty;
    var browser = baidu.editor.browser;

    var parseHTML = function (){

    var RE_PART = /<(?:(?:\/([^>]+)>[ \t\r\n]*)|(?:!--([\S|\s]*?)-->)|(?:([^\s\/>]+)\s*((?:(?:"[^"]*")|(?:'[^']*')|[^"'<>])*)\/?>[ \t\r\n]*))/g;
    var RE_ATTR = /([\w\-:.]+)(?:(?:\s*=\s*(?:(?:"([^"]*)")|(?:'([^']*)')|([^\s>]+)))|(?=\s|$))/g;

    var EMPTY_ATTR = {checked:1,compact:1,declare:1,defer:1,disabled:1,ismap:1,multiple:1,nohref:1,noresize:1,noshade:1,nowrap:1,readonly:1,selected:1};
    var CDATA_TAG = {script:1,style: 1};
    var NEED_PARENT_TAG = {
        "li": { "$": 'ul', "ul": 1, "ol": 1 },
        "dd": { "$": "dl", "dl": 1 },
            "dt": { "$": "dl", "dl": 1 },
            "option": { "$": "select", "select": 1 },
            "td": { "$": "tr", "tr": 1 },
            "th": { "$": "tr", "tr": 1 },
            "tr": { "$": "tbody", "tbody": 1, "thead": 1, "tfoot": 1, "table": 1 },
            "tbody": { "$": "table", 'table':1,"colgroup": 1 },
            "thead": { "$": "table", "table": 1 },
            "tfoot": { "$": "table", "table": 1 },
            "col": { "$": "colgroup","colgroup":1 }
        };
        var NEED_CHILD_TAG = {
            "table": "td", "tbody": "td", "thead": "td", "tfoot": "td", "tr": "td",
            "colgroup": "col",
            "ul": "li", "ol": "li",
            "dl": "dd",
            "select": "option"
        };

        function parse(html, callbacks){

            var match,
                nextIndex = 0,
                tagName,
                cdata;
            RE_PART.exec("");
            while ((match = RE_PART.exec(html))) {
                var tagIndex = match.index;
                if (tagIndex > nextIndex) {
                    var text = html.slice(nextIndex, tagIndex);
                    if (cdata) {
                        cdata.push(text);
                    } else {
                        callbacks.onText(text);
                    }
                }
                nextIndex = RE_PART.lastIndex;
                if ((tagName = match[1])) {
                    tagName = tagName.toLowerCase();
                    if (cdata && tagName == cdata._tag_name) {
                        callbacks.onCDATA(cdata.join(''));
                        cdata = null;
                    }
                    if (!cdata) {
                        callbacks.onTagClose(tagName);
                        continue;
                    }
                }
                if (cdata) {
                    cdata.push(match[0]);
                    continue;
                }
                if ((tagName = match[3])) {
                    if (/="/.test(tagName)) {
                        continue;
                    }
                    tagName = tagName.toLowerCase();
                    var attrPart = match[4],
                        attrMatch,
                        attrMap = {},
                        selfClosing = attrPart && attrPart.slice(-1) == '/';
                    if (attrPart) {
                        RE_ATTR.exec("");
                        while ((attrMatch = RE_ATTR.exec(attrPart))) {
                            var attrName = attrMatch[1].toLowerCase(),
                                attrValue = attrMatch[2] || attrMatch[3] || attrMatch[4] || '';
                            if (!attrValue && EMPTY_ATTR[attrName]) {
                                attrValue = attrName;
                            }
                            if (attrName == 'style') {
                                if (browser.ie && browser.version <= 6) {
                                    attrValue = attrValue.replace(/(?!;)\s*([\w-]+):/g, function (m, p1){
                                        return p1.toLowerCase() + ':';
                                    });
                                }
                            }
                            attrMap[attrName] = attrValue;
                        }
                    }
                    callbacks.onTagOpen(tagName, attrMap, selfClosing);
                    if (!cdata && CDATA_TAG[tagName]) {
                        cdata = [];
                        cdata._tag_name = tagName;
                    }
                    continue;
                }
                if ((tagName = match[2])) {
                    callbacks.onComment(tagName);
                }
            }
            if (html.length > nextIndex) {
                callbacks.onText(html.slice(nextIndex, html.length));
            }
        }
        return function (html, forceDtd){
            var fragment = {
                type: 'fragment',
                parent: null,
                children: []
            };
            var currentNode = fragment;
            function addChild(node){
                node.parent = currentNode;
                currentNode.children.push(node);
            }
            function addElement(element, open){
                var node = element;
                // ??????????????????????????????
                if (NEED_PARENT_TAG[node.tag]) {
                    // ???????????????????????????, ?????????????????????
                    // e.g. <table><tr><td>12312`<tr>`4566
                    while (NEED_PARENT_TAG[currentNode.tag] && NEED_PARENT_TAG[currentNode.tag][node.tag]){
                        currentNode = currentNode.parent;
                    }
                    // ????????????????????????????????????????????????, ?????????????????????
                    // e.g. <ul><li>123<li>
                    if (currentNode.tag == node.tag) {
                        currentNode = currentNode.parent;
                    }
                    // ?????????????????????
                    while (NEED_PARENT_TAG[node.tag]) {
                        if (NEED_PARENT_TAG[node.tag][currentNode.tag]) break;
                        node = node.parent = {
                            type: 'element',
                            tag: NEED_PARENT_TAG[node.tag]['$'],
                            attributes: {},
                            children: [node]
                        };
                    }
                }
                if (forceDtd) {

                    // ????????????????????????????????????????????????????????????????????????????????????,span????????????
                    while (dtd[node.tag] && !(currentNode.tag == 'span' ? baidu.editor.utils.extend(dtd['strong'],{'a':1,'A':1}) : (dtd[currentNode.tag] || dtd['div']))[node.tag]) {
                        if (tagEnd(currentNode)) continue;
                        if (!currentNode.parent) break;
                        currentNode = currentNode.parent;
                    }
                }
                node.parent = currentNode;
                currentNode.children.push(node);
                if (open) {
                    currentNode = element;
                }
                return element;
            }
            // ????????????????????????????????????????????????????????????????????????
            // e.g. <table></table>
            function tagEnd(node){
                var needTag;
                if (!node.children.length && (needTag = NEED_CHILD_TAG[node.tag])) {
                    addElement({
                        type: 'element',
                        tag: needTag,
                        attributes: {},
                        children: []
                    }, true);
                    return true;
                }
                return false;
            }
            parse(html, {
                onText: function (text){
                    while (!(dtd[currentNode.tag] || dtd['div'])['#']) {
                        if (tagEnd(currentNode)) continue;
                        currentNode = currentNode.parent;
                    }

                    // TODO: ?????????????????????????????????
                    if (/[^ \t\r\n]/.test(text)) {
                        addChild({
                            type: 'text',
                            data: text
                        });
                    }
                },
                onComment: function (text){
                    addChild({
                        type: 'comment',
                        data: text
                    });
                },
                onCDATA: function (text){
                    while (!(dtd[currentNode.tag] || dtd['div'])['#']) {
                        if (tagEnd(currentNode)) continue;
                        currentNode = currentNode.parent;
                    }
                    addChild({
                        type: 'cdata',
                        data: text
                    });
                },
                onTagOpen: function (tag, attrs, closed){
                    closed = closed || EMPTY_TAG[tag];
                    addElement({
                        type: 'element',
                        tag: tag,
                        attributes: attrs,
                        closed: closed,
                        children: []
                    }, !closed);
                },
                onTagClose: function (tag){
                    var node = currentNode;
                    // ????????????????????????, ???????????????dtd??????????????????tagOpen???????????????????????????, ??????????????????
                    while (node && tag != node.tag) {
                        node = node.parent;
                    }
                    if (node) {
                        // ?????????????????????
                        for (var tnode=currentNode; tnode!==node.parent; tnode=tnode.parent) {
                            tagEnd(tnode);
                        }
                        //???????????????inline??????
                        //?????????????????????
                        if(!node.children.length && (dtd.$removeEmpty[node.tag] || dtd.$removeEmptyBlock[node.tag]) && !node.attributes.anchorname && node.attributes['class'] != 'pagebreak'){

                            node.parent.children.pop();
                        }
                        currentNode = node.parent;
                    } else {
                        // ??????????????????????????????, ??????????????????
                        // eg. </div> => <div></div>
                        if(!(dtd.$removeEmpty[tag]|| dtd.$removeEmptyBlock[tag])){
                            node = {
                                type: 'element',
                                tag: tag,
                                attributes: {},
                                children: []
                            };
                            addElement(node, true);
                            tagEnd(node);
                            currentNode = node.parent;
                        }


                    }
                }
            });
            // ??????????????????, ?????????????????????????????????????????????, ????????????????????????
            // eg. <table>
            while (currentNode !== fragment) {
                tagEnd(currentNode);
                currentNode = currentNode.parent;
            }

            return fragment;
        };
    }();
    var unhtml1 = function (){
        var map = { '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#39;' };
        function rep( m ){ return map[m]; }
        return function ( str ) {
            str = str + '';
            return str ? str.replace( /[<>"']/g, rep ) : '';
        };
    }();
    var toHTML = function (){
        function printChildren(node){
            var children = node.children;

            var buff = [];
            for (var i=0,ci; ci = children[i]; i++) {

                buff.push(toHTML(ci));
            }
            return buff.join('');
        }
        function printAttrs(attrs){
            var buff = [];
            for (var k in attrs) {
                buff.push(k + '="' + unhtml1(attrs[k]) + '"');
            }
            return buff.join(' ');
        }
        function printData(node,notTrans){

            return notTrans ? node.data : unhtml1(node.data);
        }
        function printElement(node){
            var tag = node.tag;
            var attrs = printAttrs(node.attributes);
            var html = '<' + tag + (attrs ? ' ' + attrs : '') + (EMPTY_TAG[tag] ? ' />' : '>');
            if (!EMPTY_TAG[tag]) {
                html += printChildren(node);
                html += '</' + tag + '>';
            }
            return html;
        }

        return function (node){
            if (node.type == 'fragment') {
                return printChildren(node);
            } else if (node.type == 'element') {
                return printElement(node);
            } else if (node.type == 'text' || node.type == 'cdata') {
                return printData(node,dtd.$notTransContent[node.parent.tag]);
            } else if (node.type == 'comment') {
                return '<!--' + node.data + '-->';
            }
            return '';
        };
    }();

    /////////////////
    // WORD /////////
    /////////////////
    var transformWordHtml = function (){

        function isWordDocument( strValue ) {
            var re = new RegExp( /(class="?Mso|style="[^"]*\bmso\-|w:WordDocument)/ig );
            return re.test( strValue );
        }

        function ensureUnits( v ) {
            v = v.replace(/([\d.]+)([\w]+)?/g, function (m, p1, p2){
                return (Math.round(parseFloat(p1)) || 1) + (p2 || 'px');
            });
            return v;
        }

        function filterPasteWord( str ) {
            str = str.replace( /<!--\s*EndFragment\s*-->[\s\S]*$/, '' );
            //remove link break
            str = str.replace( /\r\n|\n|\r/ig, "" );
            //remove &nbsp; entities at the start of contents
            str = str.replace( /^\s*(&nbsp;)+/ig, "" );
            //remove &nbsp; entities at the end of contents
            str = str.replace( /(&nbsp;|<br[^>]*>)+\s*$/ig, "" );
            // Word comments like conditional comments etc
            str = str.replace( /<!--[\s\S]*?-->/ig, "" );
            // Remove comments, scripts (e.g., msoShowComment), XML tag, VML content, MS Office namespaced tags, and a few other tags
            str = str.replace( /<(!|script[^>]*>.*?<\/script(?=[>\s])|\/?(\?xml(:\w+)?|xml|meta|link|style|\w+:\w+)(?=[\s\/>]))[^>]*>/gi, "" );

            //convert word headers to strong
            str = str.replace( /<p [^>]*class="?MsoHeading"?[^>]*>(.*?)<\/p>/gi, "<p><strong>$1</strong></p>" );
            //remove lang attribute
            str = str.replace( /(lang)\s*=\s*([\'\"]?)[\w-]+\2/ig, "" );
            // Examine all styles: delete junk, transform some, and keep the rest
            str = str.replace( /(<[a-z][^>]*)\sstyle="([^"]*)"/gi, function( str, tag, style ) {
                var n = [],
                        i = 0,
                        s = style.replace( /^\s+|\s+$/, '' ).replace( /&quot;/gi, "'" ).split( /;\s*/g );

                // Examine each style definition within the tag's style attribute
                for ( var i = 0; i < s.length; i++ ) {
                    var v = s[i];
                    var name, value,
                            parts = v.split( ":" );

                    if ( parts.length == 2 ) {
                        name = parts[0].toLowerCase();
                        value = parts[1].toLowerCase();
                        // Translate certain MS Office styles into their CSS equivalents
                        switch ( name ) {
                            case "mso-padding-alt":
                            case "mso-padding-top-alt":
                            case "mso-padding-right-alt":
                            case "mso-padding-bottom-alt":
                            case "mso-padding-left-alt":
                            case "mso-margin-alt":
                            case "mso-margin-top-alt":
                            case "mso-margin-right-alt":
                            case "mso-margin-bottom-alt":
                            case "mso-margin-left-alt":
        //                        case "mso-border-alt":
        //                        case "mso-border-top-alt":
        //                        case "mso-border-right-alt":
        //                        case "mso-border-bottom-alt":
        //                        case "mso-border-left-alt":
                            case "mso-table-layout-alt":
                            case "mso-height":
                            case "mso-width":
                            case "mso-vertical-align-alt":
                                n[i++] = name.replace( /^mso-|-alt$/g, "" ) + ":" + ensureUnits( value );
                                continue;

                            case "horiz-align":
                                n[i++] = "text-align:" + value;
                                continue;

                            case "vert-align":
                                n[i++] = "vertical-align:" + value;
                                continue;

                            case "font-color":
                            case "mso-foreground":
                                n[i++] = "color:" + value;
                                continue;

                            case "mso-background":
                            case "mso-highlight":
                                n[i++] = "background:" + value;
                                continue;

                            case "mso-default-height":
                                n[i++] = "min-height:" + ensureUnits( value );
                                continue;

                            case "mso-default-width":
                                n[i++] = "min-width:" + ensureUnits( value );
                                continue;

                            case "mso-padding-between-alt":
                                n[i++] = "border-collapse:separate;border-spacing:" + ensureUnits( value );
                                continue;

                            case "text-line-through":
                                if ( (value == "single") || (value == "double") ) {
                                    n[i++] = "text-decoration:line-through";
                                }
                                continue;

                            case "mso-zero-height":
                                if ( value == "yes" ) {
                                    n[i++] = "display:none";
                                }
                                continue;
                        }

                        if ( /^(mso|column|font-emph|lang|layout|line-break|list-image|nav|panose|punct|row|ruby|sep|size|src|tab-|table-border|text-(?:align|decor|indent|trans)|top-bar|version|vnd|word-break)/.test( name ) ) {
                                if(!/mso\-list/.test(name))
                                continue;
                        }
                        // If it reached this point, it must be a valid CSS style
                        n[i] = name + ":" + parts[1];        // Lower-case name, but keep value case
                    }
                }
                // If style attribute contained any valid styles the re-write it; otherwise delete style attribute.
                if ( i > 0 ) {
                    return tag + ' style="' + n.join( ';' ) + '"';
                } else {
                    return tag;
                }
            } );
            str = str.replace( /([ ]+)<\/span>/ig, function ( m, p ) {
                return new Array( p.length + 1 ).join( '&nbsp;' ) + '</span>';
            } );
            
            return str;
        }
        return function (html){

            //??????word,?????????p->li
            first = null;
            parentTag = '',liStyle = '',firstTag = '';
            if (isWordDocument(html)) {
                html = filterPasteWord(html);
            }
            return html.replace(/>[ \t\r\n]*</g,'><');
        };
    }();
    var NODE_NAME_MAP = {
        'text': '#text',
        'comment': '#comment',
        'cdata': '#cdata-section',
        'fragment': '#document-fragment'
    };

    function _likeLi(node){
        var a;
        if (node && node.tag == 'p') {
        //office 2011?????????
            if(node.attributes['class'] == 'MsoListParagraph' || /mso-list/.test(node.attributes.style)) {
                a = 1;
            } else {
                var firstChild = node.children[0];
                if (firstChild && firstChild.tag == 'span' && /Wingdings/i.test(firstChild.attributes.style)) {
                    a = 1;
                }
            }
        }
        return a;
    }
    //???p==>li ????????????
    var first,
        orderStyle = {
            'decimal' : /\d+/,
            'lower-roman': /^m{0,4}(cm|cd|d?c{0,3})(xc|xl|l?x{0,3})(ix|iv|v?i{0,3})$/,
            'upper-roman': /^M{0,4}(CM|CD|D?C{0,3})(XC|XL|L?X{0,3})(IX|IV|V?I{0,3})$/,
            'lower-alpha' : /^\(?[a-z]+\)?$/,
            'upper-alpha': /^\(?[A-Z]+\)?$/
        },
        unorderStyle = { 'disc' : /^[l\u00B7\u2002]/, 'circle' : /^[\u006F\u00D8]/,'square' : /^[\u006E\u25C6]/},
        parentTag = '',liStyle = '',firstTag;


    //????????????????????????????????????????????????
    function transNode(node){
        if(node.type == 'element' && !node.children.length && (dtd.$removeEmpty[node.tag]||dtd.$removeEmptyBlock[node.tag]) && node.tag != 'a'){// ????????????


            return {
                type : 'fragment',
                children:[]
            }
        }
        var sizeMap = [0, 10, 12, 16, 18, 24, 32, 48],
            attr,
            indexOf = baidu.editor.utils.indexOf;

        switch(node.tag){
            case 'li':
                var child = node.children[0];
                
                if(!child || child.type != 'element' || child.tag != 'p'){

                    node.children = [{
                        type: 'element',
                        tag: 'p',
                        attributes: {},
                        children: child ? node.children : [{
                            type : 'element',
                            tag : 'br',
                            attributes:{},
                            closed: true,
                            children: []
                        }],
                        parent : node
                    }];
                }
                break;
            case 'table':
            case 'td':
                optStyle(node);
                break;
            case 'a'://?????????a==>img
                if(node.attributes['anchorname']){
                    node.tag = 'img';
                    node.attributes = {
                        'class' : 'anchorclass',
                        'anchorname':node.attributes['name']
                    };
                    node.closed = 1;
                }
                break;
            case 'b':
                node.tag = node.name = 'strong';
                break;
            case 'i':
                node.tag = node.name = 'em';
                break;
            case 'u':
                node.tag = node.name = 'span';
                node.attributes.style = (node.attributes.style || '') + ';text-decoration:underline;';
                break;
            case 's':
            case 'del':
                node.tag = node.name = 'span';
                node.attributes.style = (node.attributes.style || '') + ';text-decoration:line-through;';
                if(node.children.length == 1 ){
                    child = node.children[0];
                    if(child.tag == node.tag){
                        node.attributes.style += ";" + child.attributes.style;
                        node.children = child.children;
                        
                    }
                }
                break;
            case 'span':
                if(/mso-list/.test(node.attributes.style)){
                    
                    //?????????????????????????????????
                   if(firstTag != 'end'){

                        var ci = node.children[0],p;
                        while(ci.type == 'element'){
                            ci = ci.children[0];
                        }
                        for(p in unorderStyle){
                            if(unorderStyle[p].test(ci.data)){
                               // ci.data = ci.data.replace(unorderStyle[p],'');
                                parentTag = 'ul';
                                liStyle = p;
                                break;
                            }
                        }


                        if(!parentTag){
                           for( p in orderStyle){
                                if(orderStyle[p].test(ci.data.replace(/\.$/,''))){
                                  //   ci.data = ci.data.replace(orderStyle[p],'');
                                    parentTag = 'ol';
                                    liStyle = p;
                                    break;
                                }
                            }
                        }
                        if(firstTag){
                            if(ci.data == firstTag){
                                if(parentTag != 'ul'){
                                    liStyle = '';
                                }
                                parentTag = 'ul'
                            }else{
                                 if(parentTag != 'ol'){
                                    liStyle = '';
                                }
                                parentTag = 'ol'
                            }
                            firstTag = 'end'
                        }else{
                            firstTag = ci.data
                        }
                        if(parentTag){
                            var tmpNode = node;
                            while(tmpNode.tag != 'ul' && tmpNode.tag != 'ol'){
                                tmpNode = tmpNode.parent;
                            }
                            tmpNode.tag = parentTag;
                            tmpNode.attributes.style = 'list-style-type:' + liStyle;


                        }

                   }

                    node = {
                        type : 'fragment',
                        children : []
                    };
                    break;



                }
                var style = node.attributes.style;
                if(style){
                    style = style.replace(/background(?!-)/g, 'background-color');
                    style = style.match(/(?:\b(?:color|font-size|background-color|font-size|font-family|text-decoration)\b\s*:\s*(&[^;]+;|[^;])+(?=;)?)/gi);
                    if(style){
                        node.attributes.style = style.join(';');
                        if(!node.attributes.style){
                            delete node.attributes.style;
                        }
                    }
                }
            
                break;
            case 'font':
                node.tag = node.name = 'span';
                attr = node.attributes;
                node.attributes = {
                    'style': (attr.size ? 'font-size:' + (sizeMap[attr.size] || 12) + 'px' : '')
                    + ';' + (attr.color ? 'color:'+ attr.color : '')
                    + ';' + (attr.face ? 'font-family:'+ attr.face : '')
                    + ';' + (attr.style||'')
                };

                while(node.parent.tag == node.tag && node.parent.children.length == 1){
                    node.attributes.style && (node.parent.attributes.style ? (node.parent.attributes.style += ";" + node.attributes.style) : (node.parent.attributes.style = node.attributes.style));
                    node.parent.children = node.children;
                    node = node.parent;
                    
                }
                break;
            case 'p':
                if (node.attributes.align) {
                    node.attributes.style = (node.attributes.style || '') + ';text-align:' +
                        node.attributes.align + ';';
                    delete node.attributes.align;
                }
                if(_likeLi(node)){
                    
                    if(!first){

                        var ulNode  = {
                            type: 'element',
                            tag: 'ul',
                            attributes: {},
                            children: []
                        },
                        index = indexOf(node.parent.children,node);
                        node.parent.children[index] = ulNode;
                        ulNode.parent = node.parent;
                        ulNode.children[0] = node;
                        node.parent = ulNode;
                        
                        while(1){
                            node = ulNode.parent.children[index+1];
                            if( _likeLi(node)){
                                ulNode.children[ulNode.children.length] = node;
                                node.parent = ulNode;
                                ulNode.parent.children.splice(index+1,1);
                               
                            }else{
                                break;
                            }
                        }

                        return ulNode;
                    }
                    node.tag = node.name = 'li';
                    //???chrome???????????????????????????
                    var span = node.children[0];

                    while(span && span.type == 'element'){
                        span = span.children[0]
                    }
                    span.parent.attributes.style = (span.parent.attributes.style || '') + 'mso-list:10';
                        
                    
                    delete node.attributes['class'];
                    delete node.attributes.style;

                   
                }
        }
        return node;
    }
    function optStyle(node){
        if(browser.ie && node.attributes.style){

            var border = node.attributes.style.match(/border[^:]*:([^;]*)/i);
            if(border){
                border = border[1];
                if(border){
                    node.attributes.style = node.attributes.style.replace(/border[^;]*?(;|$)/ig,'').replace(/^\s*|\s*$/,'');
                    
                    if(!/^\s*#\w+\s*$/.test(border)){
                        node.attributes.style = (/;$/.test(node.attributes.style)||node.attributes.style.length == 0?'':';') + 'border:' + border;
                    }

                }
            }

            node.attributes.style = node.attributes.style.replace(/^\s*|\s*$/,'')
        }
    }
    function transOutNode(node){
        if(node.type == 'text'){
            
            node.data = node.data.replace(/ /g,'&nbsp;')
        }
        switch (node.tag){
//            case 'pre':
//                preStyle = node.attributes.style;
//                delete node.attributes.style;
//                break;
            case 'table':
//                tableStyle = node.attributes.style;
                !node.attributes.style && delete node.attributes.style;
                if(browser.ie && node.attributes.style){

                    optStyle(node);
                }
                break;
            case 'td':
            case 'th':
                if (/display\s*:\s*none/i.test(node.attributes.style)) {
                    return {
                        type: 'fragment',
                        children: []
                    };
                }
                if(browser.ie && !node.children.length ){
                     var txtNode  = {
                            type: 'text',
                            data:domUtils.fillChar,
                            parent : node
                        };
                        node.children[0] = txtNode;
                }
                if(browser.ie && node.attributes.style){
                    optStyle(node);

                }
                break;
            case 'img'://?????????img==>a
                if(node.attributes.anchorname){
                    node.tag = 'a';
                    node.attributes = {
                        name : node.attributes.anchorname,
                        anchorname : 1
                    }
                    node.closed = null;
                }
                



        }

        return node;
    }
    function childrenAccept(node, visit, ctx){
        
        if (!node.children || !node.children.length) {
            return node;
        }
        var children = node.children;
        for (var i=0; i<children.length; i++) {
            var newNode = visit(children[i], ctx);
            if (newNode.type == 'fragment') {
                var args = [i, 1];
                args.push.apply(args, newNode.children);
                children.splice.apply(children, args);
                //??????????????????????????????????????????????????????????????????????????????
                if(!children.length){
                    node = {
                        type: 'fragment',
                        children: []
                    }
                }
                i --;
            } else {
                children[i] = newNode;
            }
        }
        return node;
    }
    function Serialize(rules){
        this.rules = rules;
    }
    Serialize.prototype = {
        // NOTE: selector???????????????tagName
        rules: null,
        // NOTE: node?????????fragment
        filter: function (node, rules,modify){
            rules = rules || this.rules;
            var whiteList = rules && rules.whiteList;
            var blackList = rules && rules.blackList;
            function visitNode(node, parent){
                node.name = node.type == 'element' ?
                        node.tag : NODE_NAME_MAP[node.type];
                if (parent == null) {
                    return childrenAccept(node, visitNode, node);
                }
                if (blackList && blackList[node.name]) {
                    modify && (modify.flag = 1);
                    return {
                        type: 'fragment',
                        children: []
                    };
                }
                if (whiteList) {
                    if (node.type == 'element') {
                        if (parent.type == 'fragment' ? whiteList[node.name] : whiteList[node.name] && whiteList[parent.name][node.name]) {
                            var props;
                            if ((props = whiteList[node.name].$)) {
                                var oldAttrs = node.attributes;
                                var newAttrs = {};
                                for (var k in props) {
                                    if (oldAttrs[k]) {
                                        newAttrs[k] = oldAttrs[k];
                                    }
                                }
                                node.attributes = newAttrs;
                            }
                        } else {
                            modify && (modify.flag = 1);
                            node.type = 'fragment';
                            // NOTE: ??????????????????hack
                            node.name = parent.name;
                        }
                    } else {
                        // NOTE: ??????????????????
                    }
                }
                if (blackList || whiteList) {
                    childrenAccept(node, visitNode, node);
                }
                return node;
            }
            return visitNode(node, null);
        },
        transformInput: function (node, wrapInline){
            function visitNode(node){
                node = transNode(node);
                if(node.tag == 'ol' || node.tag == 'ul'){
                    first = 1;
                }
                node = childrenAccept(node, visitNode, node);
                if(node.tag == 'ol' || node.tag == 'ul'){
                    first = 0;
                    parentTag = '',liStyle = '',firstTag = '';
                }
                if(node.type == 'text' && node.data.replace(/\s/g,'') == editor.options.pageBreakTag){

                    node.type = 'element';
                    node.name = node.tag = 'div';

                    delete node.data;
                    node.attributes = {
                        'class' : 'pagebreak',
                        'unselectable' : 'on',
                        'style' : 'moz-user-select:none;-khtml-user-select: none;'
                    };
                    
                    node.children = [];

                }

                return node;
            }
            return visitNode(node);
        },
        transformOutput: function (node){
            function visitNode(node){

                if(node.tag == 'div' && node.attributes['class'] == 'pagebreak'){
                    delete node.tag;
                    node.type = 'text';
                    node.data = editor.options.pageBreakTag;
                    delete node.children;

                }
                
                node = transOutNode(node);
                if(node.tag == 'ol' || node.tag == 'ul'){
                    first = 1;
                }
                node = childrenAccept(node, visitNode, node);
                if(node.tag == 'ol' || node.tag == 'ul'){
                    first = 0;
                }
                return node;
            }
            return visitNode(node);
        },
        toHTML: toHTML,
        parseHTML: parseHTML,
        word: transformWordHtml
    };
    editor.serialize = new Serialize(editor.options.serialize);
    baidu.editor.serialize = new Serialize({});
};

})();
