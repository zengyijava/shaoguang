///import core
///commands 表格
/**
 * Created by .
 * User: taoqili
 * Date: 11-5-5
 * Time: 下午2:06
 * To change this template use File | Settings | File Templates.
 */
(function() {

    /**
     * table操作插件
     */
    baidu.editor.plugins['table'] = function() {

        var editor = baidu.editor,
            browser = editor.browser,
            domUtils = editor.dom.domUtils,
            keys = domUtils.keys,
            clearSelectedTd = domUtils.clearSelectedArr;
        //框选时用到的几个全局变量
        var anchorTd,
            tableOpt,
            _isEmpty = domUtils.isEmptyNode;

        function getIndex(cell) {
            var cells = cell.parentNode.cells;
            for (var i = 0,ci; ci = cells[i]; i++) {
                if (ci === cell) {
                    return i;
                }
            }
        }

        /**
         * 判断当前单元格是否处于隐藏状态
         * @param cell 待判断的单元格
         * @return {Boolean} 隐藏时返回true，否则返回false
         */
        function _isHide(cell) {
            return cell.style.display == "none";
        }

        function getCount(arr) {
            var count = 0;
            for (var i = 0,ti; ti = arr[i++];) {
                if (!_isHide(ti)) {
                    count++
                }

            }
            return count;
        }

        var me = this;

        me.currentSelectedArr = [];
        me.addListener('mousedown', _mouseDownEvent);
        me.addListener('keydown', function(type, evt) {
            var keyCode = evt.keyCode || evt.which;
            if (!keys[keyCode] && !evt.ctrlKey && !evt.metaKey && !evt.shiftKey && !evt.altKey) {
                clearSelectedTd(me.currentSelectedArr)
            }
        });
        me.addListener('mouseup', function() {

            anchorTd = null;
            me.removeListener('mouseover', _mouseDownEvent);
            var td = me.currentSelectedArr[0];
            if (td) {
                me.document.body.style.webkitUserSelect = '';
                var range = new baidu.editor.dom.Range(me.document);
                if (_isEmpty(td)) {
                    range.setStart(me.currentSelectedArr[0], 0).setCursor();
                } else {
                    range.selectNodeContents(me.currentSelectedArr[0]).select();
                }

            } else {

                //浏览器能从table外边选到里边导致currentSelectedArr为空，清掉当前选区回到选区的最开始

                var range = me.selection.getRange().shrinkBoundary();

                if (!range.collapsed) {
                    var start = domUtils.findParentByTagName(range.startContainer, 'td', true),
                        end = domUtils.findParentByTagName(range.endContainer, 'td', true);
                    //在table里边的不能清除
                    if (start && !end || !start && end || start && end && start !== end) {
                        range.collapse(true).select(true)
                    }
                }


            }

        });

        function reset() {
            me.currentSelectedArr = [];
            anchorTd = null;

        }

        /**
         * 插入表格
         * @param numRows 行数
         * @param numCols 列数
         */
        me.commands['inserttable'] = {
            queryCommandState: function () {
               if(this.highlight || this.queryCommandState('highlightcode')){
                       return -1;
                   }
                var range = this.selection.getRange();
                
                return domUtils.findParentByTagName(range.startContainer, 'table', true)
                    || domUtils.findParentByTagName(range.endContainer, 'table', true)
                    || me.currentSelectedArr.length > 0 ? -1 : 0;
            },
            execCommand: function (cmdName, tableobj) {
                tableOpt = tableobj;
                var arr = [];

                arr.push("cellpadding='" + (tableobj.cellpadding || 0) + "'");
                arr.push("cellspacing='" + (tableobj.cellspacing || 0) + "'");
                tableobj.width ? arr.push("width='" + tableobj.width + "'") : arr.push("width='500'");
                tableobj.height ? arr.push("height='" + tableobj.height + "'") : arr.push("height='100'");
                arr.push("borderColor='" + (tableobj.bordercolor || '#000') + "'");
                arr.push("border='" + (tableobj.border || 1) + "'");
                var html,rows = [],j = tableobj.numRows;
                if (j) while (j --) {
                    var cols = [];
                    var k = tableobj.numCols;
                    while (k --) {
                        cols[k] = '<td width=' + Math.floor((tableobj.width || 500) / tableobj.numCols) + ' >' +
                            //trace: IE6下占位符
                            (browser.ie ? domUtils.fillChar : '<br/>') + '</td>';
                    }
                    rows.push('<tr ' + (tableobj.align ? 'style=text-align:' + tableobj.align + '' : '') + '>' + cols.join('') + '</tr>');
                }

                html = '<table  ' + arr.join(" ") + (tableobj.tablealign ? "align='" + tableobj.tablealign + "'" : "") + (tableobj.backgroundcolor ? ' style="background-color:' + tableobj.backgroundcolor + ';"' : '')

                    + '>' + rows.join('') + '</table>';
                html += tableobj.tablealign ? "<p class='tableclear'></p>" : "";
                this.execCommand('insertHtml', html);

                reset();
            }
        };

        function insertClearNode(node) {
            var clearnode = node.nextSibling,p;
            if (!(clearnode && clearnode.nodeType == 1 && domUtils.hasClass(clearnode, "tableclear"))) {
                p = me.document.createElement("p");
                p.className = "tableclear";
                domUtils.insertAfter(node, p);
            }
        }

        me.commands['edittable'] = {
            queryCommandState: function () {

                var range = this.selection.getRange();
               if(this.highlight || this.queryCommandState('highlightcode')){
                       return -1;
                   }

                return domUtils.findParentByTagName(range.startContainer, 'table', true)
                    || me.currentSelectedArr.length > 0 ? 0 : -1;
            },
            execCommand: function (cmdName, tableobj) {
                
                var start = this.selection.getStart();
                var table = domUtils.findParentByTagName(start, 'table', true);
                if (table) {
                    var tmp = table.getAttribute("cellpadding");
                    if(tmp != tableobj.cellpadding){
                        table.setAttribute("cellpadding", tableobj.cellpadding);
                        var tds = table.getElementsByTagName("td"),index = 0;
                        for(var i=0,ci;ci=tds[i++];){
                            ci.style.padding = tableobj.cellpadding + "px";
                        }
                    }

                    if(tableobj.cellspacing != 0){
                        table.style.borderCollapse = "separate";
                        table.setAttribute("cellspacing", tableobj.cellspacing);
                    }else{
                        table.style.borderCollapse = "collapse";
                    }
                    table.setAttribute("width", tableobj.width);
                    table.setAttribute("height", tableobj.height);
                    table.setAttribute("border", tableobj.border);
                    table.setAttribute("borderColor", tableobj.bordercolor);
                    table.setAttribute("align", tableobj.tablealign);
                    domUtils.setStyle(table, "background-color", tableobj.backgroundcolor);

                    if(me.currentSelectedArr.length > 0){
                        for(var t=0,ti;ti=me.currentSelectedArr[t++];){
                            domUtils.setStyle(ti,'text-align',tableobj.align);
                        }
                    }else{
                        var td = domUtils.findParentByTagName(start,'td',true);
                        if(td){
                            domUtils.setStyle(td,'text-align',tableobj.align);
                        }
                    }

                    if (tableobj.tablealign) {
                        insertClearNode(table);
                    }
                }

            }
        };
        /**
         * 删除表格
         */
        me.commands['deletetable'] = {
            queryCommandState:function() {
               if(this.highlight || this.queryCommandState('highlightcode')){
                       return -1;
                   }
                var range = this.selection.getRange();
                return (domUtils.findParentByTagName(range.startContainer, 'table', true)
                    && domUtils.findParentByTagName(range.endContainer, 'table', true)) || me.currentSelectedArr.length > 0 ? 0 : -1;
            },
            execCommand:function() {
                var range = this.selection.getRange(),
                    table = domUtils.findParentByTagName(me.currentSelectedArr.length > 0 ? me.currentSelectedArr[0] : range.startContainer, 'table', true);
                var p = table.ownerDocument.createElement('p'),clearnode;
                p.innerHTML = browser.ie ? '&nbsp;' : '<br/>';
                table.parentNode.insertBefore(p, table);
                clearnode = domUtils.getNextDomNode(table);
                if (domUtils.hasClass(clearnode, "tableclear")) {
                    domUtils.remove(clearnode);
                }
                domUtils.remove(table);
                range.setStart(p, 0).setCursor();

                reset();
            }
        };

        /**
         * 添加表格标题
         */
        me.commands['addcaption'] = {
            queryCommandState:function() {
               if(this.highlight || this.queryCommandState('highlightcode')){
                       return -1;
                   }
                var range = this.selection.getRange();
                return (domUtils.findParentByTagName(range.startContainer, 'table', true)
                    && domUtils.findParentByTagName(range.endContainer, 'table', true)) || me.currentSelectedArr.length > 0 ? 0 : -1;
            },
            execCommand:function(cmdName, opt) {

                var range = this.selection.getRange(),
                    table = domUtils.findParentByTagName(me.currentSelectedArr.length > 0 ? me.currentSelectedArr[0] : range.startContainer, 'table', true);

                if (opt == "on") {
                    var c = table.createCaption();
                    c.innerHTML = "请在此输入表格标题";
                } else {
                    table.removeChild(table.caption);
                }


            }
        };


        /**
         * 向右合并单元格
         */
        me.commands['mergeright'] = {
            queryCommandState : function() {
               if(this.highlight || this.queryCommandState('highlightcode')){
                       return -1;
                   }
                var range = this.selection.getRange(),
                    start = range.startContainer,
                    td = domUtils.findParentByTagName(start, ['td','th'], true);


                if (!td || this.currentSelectedArr.length > 1)return -1;

                var tr = td.parentNode;

                //最右边行不能向右合并
                var rightCellIndex = getIndex(td) + td.colSpan;
                if (rightCellIndex >= tr.cells.length) {
                    return -1;
                }
                //单元格不在同一行不能向右合并
                var rightCell = tr.cells[rightCellIndex];
                if (_isHide(rightCell)) {
                    return -1;
                }
                return td.rowSpan == rightCell.rowSpan ? 0 : -1;
            },
            execCommand : function() {

                var range = this.selection.getRange(),
                    start = range.startContainer,
                    td = domUtils.findParentByTagName(start, ['td','th'], true) || me.currentSelectedArr[0],
                    tr = td.parentNode,
                    rows = tr.parentNode.parentNode.rows;

                //找到当前单元格右边的未隐藏单元格
                var rightCellRowIndex = tr.rowIndex,
                    rightCellCellIndex = getIndex(td) + td.colSpan,
                    rightCell = rows[rightCellRowIndex].cells[rightCellCellIndex];

                //在隐藏的原生td对象上增加两个属性，分别表示当前td对应的真实td坐标
                for (var i = rightCellRowIndex; i < rightCellRowIndex + rightCell.rowSpan; i++) {
                    for (var j = rightCellCellIndex; j < rightCellCellIndex + rightCell.colSpan; j++) {
                        var tmpCell = rows[i].cells[j];
                        tmpCell.setAttribute('rootRowIndex', tr.rowIndex);
                        tmpCell.setAttribute('rootCellIndex', getIndex(td));

                    }
                }
                //合并单元格
                td.colSpan += rightCell.colSpan || 1;
                //合并内容
                _moveContent(td, rightCell);
                //删除被合并的单元格，此处用隐藏方式实现来提升性能
                rightCell.style.display = "none";
                //重新让单元格获取焦点
                range.setStart(td, 0).setCursor(true, true);
            }
        };

        /**
         * 向下合并单元格
         */
        me.commands['mergedown'] = {
            queryCommandState : function() {
              if(this.highlight || this.queryCommandState('highlightcode')){
                       return -1;
                   }
                var range = this.selection.getRange(),
                    start = range.startContainer,
                    td = domUtils.findParentByTagName(start, 'td', true);

                if (!td || getCount(me.currentSelectedArr) > 1)return -1;


                var tr = td.parentNode,
                    table = tr.parentNode.parentNode,
                    rows = table.rows;

                //已经是最底行,不能向下合并
                var downCellRowIndex = tr.rowIndex + td.rowSpan;
                if (downCellRowIndex >= rows.length) {
                    return -1;
                }

                //如果下一个单元格是隐藏的，表明他是由左边span过来的，不能向下合并
                var downCell = rows[downCellRowIndex].cells[getIndex(td)];
                if (_isHide(downCell)) {
                    return -1;
                }

                //只有列span都相等时才能合并
                return td.colSpan == downCell.colSpan ? 0 : -1;
            },
            execCommand : function() {

                var range = this.selection.getRange(),
                    start = range.startContainer,
                    td = domUtils.findParentByTagName(start, ['td','th'], true) || me.currentSelectedArr[0];


                var tr = td.parentNode,
                    rows = tr.parentNode.parentNode.rows;

                var downCellRowIndex = tr.rowIndex + td.rowSpan,
                    downCellCellIndex = getIndex(td),
                    downCell = rows[downCellRowIndex].cells[downCellCellIndex];

                //找到当前列的下一个未被隐藏的单元格
                for (var i = downCellRowIndex; i < downCellRowIndex + downCell.rowSpan; i++) {
                    for (var j = downCellCellIndex; j < downCellCellIndex + downCell.colSpan; j++) {
                        var tmpCell = rows[i].cells[j];


                        tmpCell.setAttribute('rootRowIndex', tr.rowIndex);
                        tmpCell.setAttribute('rootCellIndex', getIndex(td));
                    }
                }
                //合并单元格
                td.rowSpan += downCell.rowSpan || 1;
                //合并内容
                _moveContent(td, downCell);
                //删除被合并的单元格，此处用隐藏方式实现来提升性能
                downCell.style.display = "none";
                //重新让单元格获取焦点
                range.setStart(td, 0).setCursor();
            }
        };

        /**
         * 删除行
         */
        me.commands['deleterow'] = {
            queryCommandState : function() {
              if(this.highlight || this.queryCommandState('highlightcode')){
                       return -1;
                   }
                var range = this.selection.getRange(),
                    start = range.startContainer,
                    td = domUtils.findParentByTagName(start, ['td','th'], true);
                if (!td && me.currentSelectedArr.length == 0)return -1;

            },
            execCommand : function() {
                var range = this.selection.getRange(),
                    start = range.startContainer,
                    td = domUtils.findParentByTagName(start, ['td','th'], true),
                    tr,
                    table,
                    cells,
                    rows ,
                    rowIndex ,
                    cellIndex;

                if (td && me.currentSelectedArr.length == 0) {
                    var count = (td.rowSpan || 1) - 1;

                    me.currentSelectedArr.push(td);
                    tr = td.parentNode,
                        table = tr.parentNode.parentNode;

                    rows = table.rows,
                        rowIndex = tr.rowIndex + 1,
                        cellIndex = getIndex(td);

                    while (count) {

                        me.currentSelectedArr.push(rows[rowIndex].cells[cellIndex]);
                        count--;
                        rowIndex++
                    }
                }

                while (td = me.currentSelectedArr.pop()) {

                    if (!domUtils.findParentByTagName(td, 'table')) {//|| _isHide(td)

                        continue;
                    }
                    tr = td.parentNode,
                        table = tr.parentNode.parentNode;
                    cells = tr.cells,
                        rows = table.rows,
                        rowIndex = tr.rowIndex,
                        cellIndex = getIndex(td);
                    /*
                     * 从最左边开始扫描并隐藏当前行的所有单元格
                     * 若当前单元格的display为none,往上找到它所在的真正单元格，获取colSpan和rowSpan，
                     *  将rowspan减一，并跳转到cellIndex+colSpan列继续处理
                     * 若当前单元格的display不为none,分两种情况：
                     *  1、rowspan == 1 ，直接设置display为none，跳转到cellIndex+colSpan列继续处理
                     *  2、rowspan > 1  , 修改当前单元格的下一个单元格的display为"",
                     *    并将当前单元格的rowspan-1赋给下一个单元格的rowspan，当前单元格的colspan赋给下一个单元格的colspan，
                     *    然后隐藏当前单元格，跳转到cellIndex+colSpan列继续处理
                     */
                    for (var currentCellIndex = 0; currentCellIndex < cells.length;) {
                        var currentNode = cells[currentCellIndex];
                        if (_isHide(currentNode)) {
                            var topNode = rows[currentNode.getAttribute('rootRowIndex')].cells[currentNode.getAttribute('rootCellIndex')];
                            topNode.rowSpan--;
                            currentCellIndex += topNode.colSpan;
                        } else {
                            if (currentNode.rowSpan == 1) {
                                currentCellIndex += currentNode.colSpan;
                            } else {
                                var downNode = rows[rowIndex + 1].cells[currentCellIndex];
                                downNode.style.display = "";
                                downNode.rowSpan = currentNode.rowSpan - 1;
                                downNode.colSpan = currentNode.colSpan;
                                currentCellIndex += currentNode.colSpan;
                            }
                        }
                    }
                    //完成更新后再删除外层包裹的tr
                    domUtils.remove(tr);

                    //重新定位焦点
                    var topRowTd, focusTd, downRowTd;

                    if (rowIndex == rows.length) { //如果被删除的行是最后一行,这里之所以没有-1是因为已经删除了一行
                        //如果删除的行也是第一行，那么表格总共只有一行，删除整个表格
                        if (rowIndex == 0) {
                            var p = table.ownerDocument.createElement('p');
                            p.innerHTML = browser.ie ? '&nbsp;' : '<br/>';
                            table.parentNode.insertBefore(p, table);
                            domUtils.remove(table);
                            range.setStart(p, 0).setCursor();

                            return;
                        }
                        //如果上一单元格未隐藏，则直接定位，否则定位到最近的上一个非隐藏单元格
                        var preRowIndex = rowIndex - 1;
                        topRowTd = rows[preRowIndex].cells[ cellIndex];
                        focusTd = _isHide(topRowTd) ? rows[topRowTd.getAttribute('rootRowIndex')].cells[topRowTd.getAttribute('rootCellIndex')] : topRowTd;

                    } else { //如果被删除的不是最后一行，则光标定位到下一行,此处未加1是因为已经删除了一行

                        downRowTd = rows[rowIndex].cells[cellIndex];
                        focusTd = _isHide(downRowTd) ? rows[downRowTd.getAttribute('rootRowIndex')].cells[downRowTd.getAttribute('rootCellIndex')] : downRowTd;
                    }
                }


                range.setStart(focusTd, 0).setCursor();
                update(table)
            }
        };

        /**
         * 删除列
         */
        me.commands['deletecol'] = {
            queryCommandState:function() {
               if(this.highlight || this.queryCommandState('highlightcode')){
                       return -1;
                   }
                var range = this.selection.getRange(),
                    start = range.startContainer,
                    td = domUtils.findParentByTagName(start, ['td','th'], true);
                if (!td && me.currentSelectedArr.length == 0)return -1;
            },
            execCommand:function() {

                var range = this.selection.getRange(),
                    start = range.startContainer,
                    td = domUtils.findParentByTagName(start, ['td','th'], true);


                if (td && me.currentSelectedArr.length == 0) {

                    var count = (td.colSpan || 1) - 1;

                    me.currentSelectedArr.push(td);
                    while (count) {
                        do{
                            td = td.nextSibling
                        } while (td.nodeType == 3);
                        me.currentSelectedArr.push(td);
                        count--;
                    }
                }

                while (td = me.currentSelectedArr.pop()) {
                    if (!domUtils.findParentByTagName(td, 'table')) { //|| _isHide(td)
                        continue;
                    }

                    var tr = td.parentNode,
                        table = tr.parentNode.parentNode,
                        cellIndex = getIndex(td),
                        rows = table.rows,
                        cells = tr.cells,
                        rowIndex = tr.rowIndex;
                    /*
                     * 从第一行开始扫描并隐藏当前列的所有单元格
                     * 若当前单元格的display为none，表明它是由左边Span过来的，
                     *  将左边第一个非none单元格的colSpan减去1并删去对应的单元格后跳转到rowIndex + rowspan行继续处理；
                     * 若当前单元格的display不为none，分两种情况，
                     *  1、当前单元格的colspan == 1 ， 则直接删除该节点，跳转到rowIndex + rowspan行继续处理
                     *  2、当前单元格的colsapn >  1, 修改当前单元格右边单元格的display为"",
                     *      并将当前单元格的colspan-1赋给它的colspan，当前单元格的rolspan赋给它的rolspan，
                     *      然后删除当前单元格，跳转到rowIndex+rowSpan行继续处理
                     */
                    var rowSpan;
                    for (var currentRowIndex = 0; currentRowIndex < rows.length;) {
                        var currentNode = rows[currentRowIndex].cells[cellIndex];
                        if (_isHide(currentNode)) {
                            var leftNode = rows[currentNode.getAttribute('rootRowIndex')].cells[currentNode.getAttribute('rootCellIndex')];
                            //依次删除对应的单元格
                            rowSpan = leftNode.rowSpan;
                            for (var i = 0; i < leftNode.rowSpan; i++) {
                                var delNode = rows[currentRowIndex + i].cells[cellIndex];
                                domUtils.remove(delNode);
                            }
                            //修正被删后的单元格信息
                            leftNode.colSpan--;
                            currentRowIndex += rowSpan;
                        } else {
                            if (currentNode.colSpan == 1) {
                                rowSpan = currentNode.rowSpan;
                                for (var i = currentRowIndex,l = currentRowIndex + currentNode.rowSpan; i < l; i++) {
                                    domUtils.remove(rows[i].cells[cellIndex]);
                                }
                                currentRowIndex += rowSpan;

                            } else {
                                var rightNode = rows[currentRowIndex].cells[cellIndex + 1];
                                rightNode.style.display = "";
                                rightNode.rowSpan = currentNode.rowSpan;
                                rightNode.colSpan = currentNode.colSpan - 1;
                                currentRowIndex += currentNode.rowSpan;
                                domUtils.remove(currentNode);
                            }
                        }
                    }

                    //重新定位焦点
                    var preColTd, focusTd, nextColTd;
                    if (cellIndex == cells.length) { //如果当前列是最后一列，光标定位到当前列的前一列,同样，这里没有减去1是因为已经被删除了一列
                        //如果当前列也是第一列，则删除整个表格
                        if (cellIndex == 0) {
                            var p = table.ownerDocument.createElement('p');
                            p.innerHTML = browser.ie ? '&nbsp;' : '<br/>';
                            table.parentNode.insertBefore(p, table)
                            domUtils.remove(table);
                            range.setStart(p, 0).setCursor();
                            return;
                        }
                        //找到当前单元格前一列中和本单元格最近的一个未隐藏单元格
                        var preCellIndex = cellIndex - 1;
                        preColTd = rows[rowIndex].cells[preCellIndex];
                        focusTd = _isHide(preColTd) ? rows[preColTd.getAttribute('rootRowIndex')].cells[preColTd.getAttribute('rootCellIndex')] : preColTd;

                    } else { //如果当前列不是最后一列，则光标定位到当前列的后一列

                        nextColTd = rows[rowIndex].cells[cellIndex];
                        focusTd = _isHide(nextColTd) ? rows[nextColTd.getAttribute('rootRowIndex')].cells[nextColTd.getAttribute('rootCellIndex')] : nextColTd;
                    }
                }

                range.setStart(focusTd, 0).setCursor();
                update(table)
            }
        };

        /**
         * 完全拆分单元格
         */
        me.commands['splittocells'] = {
            queryCommandState:function() {
               if(this.highlight || this.queryCommandState('highlightcode')){
                       return -1;
                   }
                var range = this.selection.getRange(),
                    start = range.startContainer,
                    td = domUtils.findParentByTagName(start, ['td','th'], true);
                return td && ( td.rowSpan > 1 || td.colSpan > 1 ) && (!me.currentSelectedArr.length || getCount(me.currentSelectedArr) == 1) ? 0 : -1;
            },
            execCommand:function() {

                var range = this.selection.getRange(),
                    start = range.startContainer,
                    td = domUtils.findParentByTagName(start, ['td','th'], true),
                    tr = td.parentNode,
                    table = tr.parentNode.parentNode;
                var rowIndex = tr.rowIndex,
                    cellIndex = getIndex(td),
                    rowSpan = td.rowSpan,
                    colSpan = td.colSpan;


                for (var i = 0; i < rowSpan; i++) {
                    for (var j = 0; j < colSpan; j++) {
                        var cell = table.rows[rowIndex + i].cells[cellIndex + j];
                        cell.rowSpan = 1;
                        cell.colSpan = 1;
                        if (_isHide(cell)) {
                            cell.style.display = "";
                            cell.innerHTML = browser.ie ? '' : "<br/>";
                        }
                    }
                }
            }
        };


        /**
         * 将单元格拆分成行
         */
        me.commands['splittorows'] = {
            queryCommandState:function() {
             if(this.highlight || this.queryCommandState('highlightcode')){
                       return -1;
                   }
                var range = this.selection.getRange(),
                    start = range.startContainer,
                    td = domUtils.findParentByTagName(start, 'td', true) || me.currentSelectedArr[0];
                return td && ( td.rowSpan > 1) && (!me.currentSelectedArr.length || getCount(me.currentSelectedArr) == 1) ? 0 : -1;
            },
            execCommand:function() {

                var range = this.selection.getRange(),
                    start = range.startContainer,
                    td = domUtils.findParentByTagName(start, 'td', true) || me.currentSelectedArr[0],
                    tr = td.parentNode,
                    rows = tr.parentNode.parentNode.rows;

                var rowIndex = tr.rowIndex,
                    cellIndex = getIndex(td),
                    rowSpan = td.rowSpan,
                    colSpan = td.colSpan;

                for (var i = 0; i < rowSpan; i++) {
                    var cells = rows[rowIndex + i],
                        cell = cells.cells[cellIndex];
                    cell.rowSpan = 1;
                    cell.colSpan = colSpan;
                    if (_isHide(cell)) {
                        cell.style.display = "";
                        //原有的内容要清除掉
                        cell.innerHTML = browser.ie ? '' : '<br/>'
                    }
                    //修正被隐藏单元格中存储的rootRowIndex和rootCellIndex信息
                    for (var j = cellIndex + 1; j < cellIndex + colSpan; j++) {
                        cell = cells.cells[j];

                        cell.setAttribute('rootRowIndex', rowIndex + i)
                    }

                }
                clearSelectedTd(me.currentSelectedArr);
                this.selection.getRange().setStart(td, 0).setCursor();
            }
        };


        /**
         * 在表格前插入行
         */
        me.commands['insertparagraphbeforetable'] = {
            queryCommandState:function() {
              if(this.highlight || this.queryCommandState('highlightcode')){
                       return -1;
                   }
                var range = this.selection.getRange(),
                    start = range.startContainer,
                    td = domUtils.findParentByTagName(start, 'td', true) || me.currentSelectedArr[0];
                return  td && domUtils.findParentByTagName(td, 'table') ? 0 : -1;
            },
            execCommand:function() {

                var range = this.selection.getRange(),
                    start = range.startContainer,
                    table = domUtils.findParentByTagName(start, 'table', true);

                start = me.document.createElement(me.options.enterTag);
                table.parentNode.insertBefore(start, table);
                clearSelectedTd(me.currentSelectedArr);
                if (start.tagName == 'P') {
                    //trace:868 
                    start.innerHTML = browser.ie ? '' : '<br/>';
                    range.setStart(start, 0)
                } else {
                    range.setStartBefore(start)
                }
                range.setCursor();

            }
        };

        /**
         * 将单元格拆分成列
         */
        me.commands['splittocols'] = {
            queryCommandState:function() {
             if(this.highlight || this.queryCommandState('highlightcode')){
                       return -1;
                   }
                var range = this.selection.getRange(),
                    start = range.startContainer,
                    td = domUtils.findParentByTagName(start, ['td','th'], true) || me.currentSelectedArr[0];
                return td && ( td.colSpan > 1) && (!me.currentSelectedArr.length || getCount(me.currentSelectedArr) == 1) ? 0 : -1;
            },
            execCommand:function() {

                var range = this.selection.getRange(),
                    start = range.startContainer,
                    td = domUtils.findParentByTagName(start, ['td','th'], true) || me.currentSelectedArr[0],
                    tr = td.parentNode,
                    rows = tr.parentNode.parentNode.rows;

                var rowIndex = tr.rowIndex,
                    cellIndex = getIndex(td),
                    rowSpan = td.rowSpan,
                    colSpan = td.colSpan;

                for (var i = 0; i < colSpan; i++) {
                    var cell = rows[rowIndex].cells[cellIndex + i];
                    cell.rowSpan = rowSpan;
                    cell.colSpan = 1;
                    if (_isHide(cell)) {
                        cell.style.display = "";
                        cell.innerHTML = browser.ie ? '' : '<br/>'
                    }

                    for (var j = rowIndex + 1; j < rowIndex + rowSpan; j++) {
                        var tmpCell = rows[j].cells[cellIndex + i];
                        tmpCell.setAttribute('rootCellIndex', cellIndex + i);
                    }
                }
                clearSelectedTd(me.currentSelectedArr);
                this.selection.getRange().setStart(td, 0).setCursor();
            }
        };


        /**
         * 插入行
         */
        me.commands['insertrow'] = {
            queryCommandState:function() {
              if(this.highlight || this.queryCommandState('highlightcode')){
                       return -1;
                   }
                var range = this.selection.getRange();
                return domUtils.findParentByTagName(range.startContainer, 'table', true)
                    || domUtils.findParentByTagName(range.endContainer, 'table', true) || me.currentSelectedArr.length != 0 ? 0 : -1;
            },
            execCommand:function() {
                var range = this.selection.getRange(),
                    start = range.startContainer,
                    tr = domUtils.findParentByTagName(start, 'tr', true) || me.currentSelectedArr[0].parentNode,
                    table = tr.parentNode.parentNode,
                    rows = table.rows;

                //记录插入位置原来所有的单元格
                var rowIndex = tr.rowIndex,
                    cells = rows[rowIndex].cells;

                //插入新的一行
                var newRow = table.insertRow(rowIndex);

                var newCell;
                //遍历表格中待插入位置中的所有单元格，检查其状态，并据此修正新插入行的单元格状态
                for (var cellIndex = 0; cellIndex < cells.length;) {

                    var tmpCell = cells[cellIndex];

                    if (_isHide(tmpCell)) { //如果当前单元格是隐藏的，表明当前单元格由其上部span过来，找到其上部单元格

                        //找到被隐藏单元格真正所属的单元格
                        var topCell = rows[tmpCell.getAttribute('rootRowIndex')].cells[tmpCell.getAttribute('rootCellIndex')];
                        //增加一行，并将所有新插入的单元格隐藏起来
                        topCell.rowSpan++;
                        for (var i = 0; i < topCell.colSpan; i++) {
                            newCell = tmpCell.cloneNode(false);
                            newCell.rowSpan = newCell.colSpan = 1;
                            newCell.innerHTML = browser.ie ? '' : "<br/>";
                            newCell.className = '';

                            if (newRow.children[cellIndex + i]) {
                                newRow.insertBefore(newCell, newRow.children[cellIndex + i]);
                            } else {
                                newRow.appendChild(newCell)
                            }

                            newCell.style.display = "none";
                        }
                        cellIndex += topCell.colSpan;

                    } else {//若当前单元格未隐藏，则在其上行插入colspan个单元格

                        for (var j = 0; j < tmpCell.colSpan; j++) {

                            newCell = tmpCell.cloneNode(false);
                            newCell.rowSpan = newCell.colSpan = 1;
                            newCell.innerHTML = browser.ie ? '' : "<br/>";
                            newCell.className = '';
                            if (newRow.children[cellIndex + j]) {
                                newRow.insertBefore(newCell, newRow.children[cellIndex + j]);
                            } else {
                                newRow.appendChild(newCell)
                            }
                        }
                        cellIndex += tmpCell.colSpan;
                    }
                }
                update(table);
                range.setStart(newRow.cells[0], 0).setCursor();

                clearSelectedTd(me.currentSelectedArr);
            }
        };

        /**
         * 插入列
         */
        me.commands['insertcol'] = {
            queryCommandState:function() {
              if(this.highlight || this.queryCommandState('highlightcode')){
                       return -1;
                   }
                var range = this.selection.getRange();
                return domUtils.findParentByTagName(range.startContainer, 'table', true)
                    || domUtils.findParentByTagName(range.endContainer, 'table', true) || me.currentSelectedArr.length != 0 ? 0 : -1;
            },
            execCommand:function() {

                var range = this.selection.getRange(),
                    start = range.startContainer,
                    td = domUtils.findParentByTagName(start, ['td','th'], true) || me.currentSelectedArr[0],
                    table = domUtils.findParentByTagName(td, 'table'),
                    rows = table.rows;

                var cellIndex = getIndex(td),
                    newCell;

                //遍历当前列中的所有单元格，检查其状态，并据此修正新插入列的单元格状态
                for (var rowIndex = 0; rowIndex < rows.length;) {

                    var tmpCell = rows[rowIndex].cells[cellIndex],tr;

                    if (_isHide(tmpCell)) {//如果当前单元格是隐藏的，表明当前单元格由其左边span过来，找到其左边单元格

                        var leftCell = rows[tmpCell.getAttribute('rootRowIndex')].cells[tmpCell.getAttribute('rootCellIndex')];
                        leftCell.colSpan++;
                        for (var i = 0; i < leftCell.rowSpan; i++) {
                            newCell = td.cloneNode(false);
                            newCell.rowSpan = newCell.colSpan = 1;
                            newCell.innerHTML = browser.ie ? '' : "<br/>";
                            newCell.className = '';
                            tr = rows[rowIndex + i];
                            if (tr.children[cellIndex]) {
                                tr.insertBefore(newCell, tr.children[cellIndex]);
                            } else {
                                tr.appendChild(newCell)
                            }

                            newCell.style.display = "none";
                        }
                        rowIndex += leftCell.rowSpan;

                    } else { //若当前单元格未隐藏，则在其左边插入rowspan个单元格

                        for (var j = 0; j < tmpCell.rowSpan; j++) {
                            newCell = td.cloneNode(false);
                            newCell.rowSpan = newCell.colSpan = 1;
                            newCell.innerHTML = browser.ie ? '' : "<br/>";
                            newCell.className = '';
                            tr = rows[rowIndex + j];
                            if (tr.children[cellIndex]) {
                                tr.insertBefore(newCell, tr.children[cellIndex]);
                            } else {
                                tr.appendChild(newCell)
                            }

                            newCell.innerHTML = browser.ie ? '' : "<br/>";

                        }
                        rowIndex += tmpCell.rowSpan;
                    }
                }
                update(table);
                range.setStart(rows[0].cells[cellIndex], 0).setCursor();
                clearSelectedTd(me.currentSelectedArr);
            }
        };

        /**
         * 合并多个单元格，通过两个cell将当前包含的所有横纵单元格进行合并
         */
        me.commands['mergecells'] = {
            queryCommandState:function() {
               if(this.highlight || this.queryCommandState('highlightcode')){
                       return -1;
                   }
                var count = 0;
                for (var i = 0,ti; ti = this.currentSelectedArr[i++];) {
                    if (!_isHide(ti))
                        count++;
                }
                return count > 1 ? 0 : -1;
            },
            execCommand:function() {

                var start = me.currentSelectedArr[0],
                    end = me.currentSelectedArr[me.currentSelectedArr.length - 1],
                    table = domUtils.findParentByTagName(start, 'table'),
                    rows = table.rows,
                    cellsRange = {
                        beginRowIndex:start.parentNode.rowIndex,
                        beginCellIndex:getIndex(start),
                        endRowIndex:end.parentNode.rowIndex,
                        endCellIndex:getIndex(end)
                    },

                    beginRowIndex = cellsRange.beginRowIndex,
                    beginCellIndex = cellsRange.beginCellIndex,
                    rowsLength = cellsRange.endRowIndex - cellsRange.beginRowIndex + 1,
                    cellLength = cellsRange.endCellIndex - cellsRange.beginCellIndex + 1,

                    tmp = rows[beginRowIndex].cells[beginCellIndex];

                for (var i = 0, ri; (ri = rows[beginRowIndex + i++]) && i <= rowsLength;) {
                    for (var j = 0, ci; (ci = ri.cells[beginCellIndex + j++]) && j <= cellLength;) {
                        if (i == 1 && j == 1) {
                            ci.style.display = "";
                            ci.rowSpan = rowsLength;
                            ci.colSpan = cellLength;
                        } else {
                            ci.style.display = "none";
                            ci.rowSpan = 1;
                            ci.colSpan = 1;
                            ci.setAttribute('rootRowIndex', beginRowIndex);
                            ci.setAttribute('rootCellIndex', beginCellIndex);

                            //传递内容
                            _moveContent(tmp, ci);
                        }
                    }
                }
                this.selection.getRange().setStart(tmp, 0).setCursor();
                clearSelectedTd(me.currentSelectedArr);
            }
        };

        /**
         * 将cellFrom单元格中的内容移动到cellTo中
         * @param cellTo  目标单元格
         * @param cellFrom  源单元格
         */
        function _moveContent(cellTo, cellFrom) {
            if (_isEmpty(cellFrom)) return;

            if (_isEmpty(cellTo)) {
                cellTo.innerHTML = cellFrom.innerHTML;
                return;
            }
            var child = cellTo.lastChild;
            if (child.nodeType != 1 || child.tagName != 'BR') {
                cellTo.appendChild(cellTo.ownerDocument.createElement('br'))
            }

            //依次移动内容
            while (child = cellFrom.firstChild) {
                cellTo.appendChild(child);
            }
        }


        /**
         * 根据两个单元格来获取中间包含的所有单元格集合选区
         * @param cellA
         * @param cellB
         * @return {Object} 选区的左上和右下坐标
         */
        function _getCellsRange(cellA, cellB) {

            var trA = cellA.parentNode,
                trB = cellB.parentNode,
                aRowIndex = trA.rowIndex,
                bRowIndex = trB.rowIndex,
                rows = trA.parentNode.parentNode.rows,
                rowsNum = rows.length,
                cellsNum = rows[0].cells.length,
                cellAIndex = getIndex(cellA),
                cellBIndex = getIndex(cellB);

            if (cellA == cellB) {
                return {
                    beginRowIndex: aRowIndex,
                    beginCellIndex: cellAIndex,
                    endRowIndex: aRowIndex + cellA.rowSpan - 1,
                    endCellIndex: cellBIndex + cellA.colSpan - 1
                }
            }

            var
                beginRowIndex = Math.min(aRowIndex, bRowIndex),
                beginCellIndex = Math.min(cellAIndex, cellBIndex),
                endRowIndex = Math.max(aRowIndex + cellA.rowSpan - 1, bRowIndex + cellB.rowSpan - 1),
                endCellIndex = Math.max(cellAIndex + cellA.colSpan - 1, cellBIndex + cellB.colSpan - 1);

            while (1) {

                var tmpBeginRowIndex = beginRowIndex,
                    tmpBeginCellIndex = beginCellIndex,
                    tmpEndRowIndex = endRowIndex,
                    tmpEndCellIndex = endCellIndex;
                // 检查是否有超出TableRange上边界的情况
                if (beginRowIndex > 0) {
                    for (cellIndex = beginCellIndex; cellIndex <= endCellIndex;) {
                        var currentTopTd = rows[beginRowIndex].cells[cellIndex];
                        if (_isHide(currentTopTd)) {

                            //overflowRowIndex = beginRowIndex == currentTopTd.rootRowIndex ? 1:0;
                            beginRowIndex = currentTopTd.getAttribute('rootRowIndex');
                            currentTopTd = rows[currentTopTd.getAttribute('rootRowIndex')].cells[currentTopTd.getAttribute('rootCellIndex')];
                        }

                        cellIndex = getIndex(currentTopTd) + (currentTopTd.colSpan || 1);
                    }
                }

                //检查是否有超出左边界的情况
                if (beginCellIndex > 0) {
                    for (var rowIndex = beginRowIndex; rowIndex <= endRowIndex;) {
                        var currentLeftTd = rows[rowIndex].cells[beginCellIndex];
                        if (_isHide(currentLeftTd)) {
                            // overflowCellIndex = beginCellIndex== currentLeftTd.rootCellIndex ? 1:0;
                            beginCellIndex = currentLeftTd.getAttribute('rootCellIndex');
                            currentLeftTd = rows[currentLeftTd.getAttribute('rootRowIndex')].cells[currentLeftTd.getAttribute('rootCellIndex')];
                        }
                        rowIndex = currentLeftTd.parentNode.rowIndex + (currentLeftTd.rowSpan || 1);
                    }
                }

                // 检查是否有超出TableRange下边界的情况
                if (endRowIndex < rowsNum) {
                    for (var cellIndex = beginCellIndex; cellIndex <= endCellIndex;) {
                        var currentDownTd = rows[endRowIndex].cells[cellIndex];
                        if (_isHide(currentDownTd)) {
                            currentDownTd = rows[currentDownTd.getAttribute('rootRowIndex')].cells[currentDownTd.getAttribute('rootCellIndex')];
                        }
                        endRowIndex = currentDownTd.parentNode.rowIndex + currentDownTd.rowSpan - 1;
                        cellIndex = getIndex(currentDownTd) + (currentDownTd.colSpan || 1);
                    }
                }

                //检查是否有超出右边界的情况
                if (endCellIndex < cellsNum) {
                    for (rowIndex = beginRowIndex; rowIndex <= endRowIndex;) {
                        var currentRightTd = rows[rowIndex].cells[endCellIndex];
                        if (_isHide(currentRightTd)) {
                            currentRightTd = rows[currentRightTd.getAttribute('rootRowIndex')].cells[currentRightTd.getAttribute('rootCellIndex')];
                        }
                        endCellIndex = getIndex(currentRightTd) + currentRightTd.colSpan - 1;
                        rowIndex = currentRightTd.parentNode.rowIndex + (currentRightTd.rowSpan || 1);
                    }
                }

                if (tmpBeginCellIndex == beginCellIndex && tmpEndCellIndex == endCellIndex && tmpEndRowIndex == endRowIndex && tmpBeginRowIndex == beginRowIndex) {
                    break;
                }
            }

            //返回选区的起始和结束坐标
            return {
                beginRowIndex:  beginRowIndex,
                beginCellIndex: beginCellIndex,
                endRowIndex:    endRowIndex,
                endCellIndex:   endCellIndex
            }
        }


        /**
         * 鼠标按下事件
         * @param type
         * @param evt
         */
        function _mouseDownEvent(type, evt) {
            if(me.queryCommandState('highlightcode'))
                return;
            if (evt.button == 2)return;
            me.document.body.style.webkitUserSelect = '';
            anchorTd = evt.target || evt.srcElement;

            clearSelectedTd(me.currentSelectedArr);
            domUtils.clearSelectedArr(me.currentSelectedArr);
            //在td里边点击，anchorTd不是td
            if (anchorTd.tagName !== 'TD') {
                anchorTd = domUtils.findParentByTagName(anchorTd, 'td') || anchorTd;
            }

            if (anchorTd.tagName == 'TD') {


                me.addListener('mouseover', function(type, evt) {
                    var tmpTd = evt.target || evt.srcElement;
                    _mouseOverEvent.call(me, tmpTd);
                    evt.preventDefault ? evt.preventDefault() : (evt.returnValue = false);
                });

            } else {


                reset();
            }

        }

        /**
         * 鼠标移动事件
         * @param tmpTd
         */
        function _mouseOverEvent(tmpTd) {

            if (anchorTd && tmpTd.tagName == "TD") {

                me.document.body.style.webkitUserSelect = 'none';
                var table = tmpTd.parentNode.parentNode.parentNode;
                me.selection.getNative()[browser.ie ? 'empty' : 'removeAllRanges']();
                var range = _getCellsRange(anchorTd, tmpTd);
                _toggleSelect(table, range);


            }
        }

        /**
         * 切换选区状态
         * @param table
         * @param cellsRange
         */
        function _toggleSelect(table, cellsRange) {
            var rows = table.rows;
            clearSelectedTd(me.currentSelectedArr);
            for (var i = cellsRange.beginRowIndex; i <= cellsRange.endRowIndex; i++) {
                for (var j = cellsRange.beginCellIndex; j <= cellsRange.endCellIndex; j++) {
                    var td = rows[i].cells[j];
                    td.className = me.options.selectedTdClass;
                    me.currentSelectedArr.push(td);
                }
            }
        }

        //更新rootRowIndxe,rootCellIndex
        function update(table) {
            var tds = table.getElementsByTagName('td'),
                rowIndex,cellIndex,rows = table.rows;
            for (var j = 0,tj; tj = tds[j++];) {
                if (!_isHide(tj)) {
                    rowIndex = tj.parentNode.rowIndex;
                    cellIndex = getIndex(tj);
                    for (var r = 0; r < tj.rowSpan; r++) {
                        var c = r == 0 ? 1 : 0;
                        for (; c < tj.colSpan; c++) {
                            var tmp = rows[rowIndex + r].children[cellIndex + c];


                            tmp.setAttribute('rootRowIndex', rowIndex);
                            tmp.setAttribute('rootCellIndex', cellIndex);

                        }
                    }
                }
            }
        }

        me.adjustTable = function(cont) {
            var table = cont.getElementsByTagName('table');
            for (var i = 0,ti; ti = table[i++];) {
                if (ti.getAttribute('align')) {
                    insertClearNode(ti)

                }
                if (ti.getAttribute('border') == '0' || !ti.getAttribute('border')) {
                    ti.setAttribute('border', 1);
                }

                if (domUtils.getComputedStyle(ti, 'border-color') == '#ffffff') {
                    ti.setAttribute('borderColor', '#000');
                }
                var tds = domUtils.getElementsByTagName(ti, 'td'),
                    td,tmpTd;

                for (var j = 0,tj; tj = tds[j++];) {
                    if (domUtils.isEmptyNode(tj)) {
                        tj.innerHTML = browser.ie ? domUtils.fillChar : '<br/>';
                    }
                    var index = getIndex(tj),
                        rowIndex = tj.parentNode.rowIndex,
                        rows = domUtils.findParentByTagName(tj, 'table').rows;

                    for (var r = 0; r < tj.rowSpan; r++) {
                        var c = r == 0 ? 1 : 0;
                        for (; c < tj.colSpan; c++) {

                            if (!td) {
                                td = tj.cloneNode(false);

                                td.rowSpan = td.colSpan = 1;
                                td.style.display = 'none';
                                td.innerHTML = browser.ie ? '' : '<br/>';


                            } else {
                                td = td.cloneNode(true)
                            }

                            td.setAttribute('rootRowIndex', tj.parentNode.rowIndex);
                            td.setAttribute('rootCellIndex', index);
                            if (r == 0) {
                                if (tj.nextSibling) {
                                    tj.parentNode.insertBefore(td, tj.nextSibling);
                                } else {
                                    tj.parentNode.appendChild(td)
                                }
                            } else {
                                tmpTd = rows[rowIndex + r].children[index];
                                if (tmpTd) {
                                    tmpTd.parentNode.insertBefore(td, tmpTd)
                                } else {
                                    //trace:1032
                                    rows[rowIndex + r].appendChild(td)
                                }
                            }


                        }
                    }


                }

            }
        }
    };


})();
