<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String langName = (String)session.getAttribute("emp_lang");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<%@include file="/common/common.jsp" %>
	    <title></title>
	    <meta content="text/html; charset=utf-8" http-equiv="Content-Type"/>
	    <script type="text/javascript" src="../internal.js"></script>
	    <script type="text/javascript" src="<%=basePath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=basePath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>	
		<script type="text/javascript" src="<%=basePath%>/common/i18n/<%=langName%>/ydwx_<%=langName%>.js"></script>	
	    <style type="text/css">
	        *{color: #838383}
	        body {
	            font-size: 12px;
	            width:550px;
	            height: 225px;
	            overflow: hidden;
	            margin:0px;padding:0px;
	        }
	        .wrap{
	            padding: 9px 0px 0px 15px;
	            height:100%;
	        }
	        .wrap table{padding:0;margin:0;font-size: 12px;}
	        .wrap table tr{padding:0;margin:0px; list-style: none;height: 30px; line-height: 30px;}
	        .wrap input{ width:110px;height:21px;line-height:21px;background: #FFF;border:1px solid #d7d7d7;padding: 0px; margin: 0px;font-size: 12px; }
			table{
				width: 550px;
			}
			body > div > table > tbody > tr > td:nth-child(1),body > div > table > tbody > tr > td:nth-child(3){
				width: 105px;
			}
			body > div > table > tbody > tr span{
				width: 105px;
			}

	    </style>
	</head>
	<body>
	<div class="wrap">
	    <table>
	        <tr>
				<td><span><emp:message key="ydwx_wxbj_add_31" defVal="行数：" fileName="ydwx"></emp:message></span></td>
				<td><input type="text" id="rowCount" value="2"/></td>
				<td><span><emp:message key="ydwx_wxbj_add_24" defVal="宽度：" fileName="ydwx"></emp:message></span></td>
				<td><input type="text" id="tableWidth" value="300"/> px</td>
	        </tr>
	        <tr>
	            <td><span><emp:message key="ydwx_wxbj_add_32" defVal="列数：" fileName="ydwx"></emp:message></span></td>
				<td><input type="text" id="colCount"  value="5"/></td>
	            <td><span><emp:message key="ydwx_wxbj_add_25" defVal="高度：" fileName="ydwx"></emp:message></span></td>
				<td><input type="text" id="tableHeight"  value="100"/> px</td>
	        </tr>
	        <tr>
	            <td><span><emp:message key="ydwx_wxbj_add_33" defVal="表格边框：" fileName="ydwx"></emp:message></span></td>
				<td><input type="text" id="border" value="1"/> px</td>
	            <td><span><emp:message key="ydwx_wxbj_add_34" defVal="间距：" fileName="ydwx"></emp:message></span></td>
				<td><input type="text" id="cellspacing" value="0"/> px</td>
	        </tr>
	        <tr>
	            <td><span><emp:message key="ydwx_wxbj_add_35" defVal="表格边框颜色：" fileName="ydwx"></emp:message></span></td>
				<td><input type="text" style="width: 68px!important;width: 65px;" id="border-color" onclick="show(this)" /></td>
	            <td><span><emp:message key="ydwx_wxbj_add_36" defVal="边距：" fileName="ydwx"></emp:message></span></td>
				<td><input type="text" id="cellpadding" value="1"/> px</td>
	        </tr>
	        <tr>
	            <td><span><emp:message key="ydwx_wxbj_add_37" defVal="表格对齐：" fileName="ydwx"></emp:message></span></td>
				<td>
					<select id="tablealign" >
	                    <option value=""><emp:message key="ydwx_wxbj_add_19" defVal="默认" fileName="ydwx"></emp:message></option>
	                    <option value="left"><emp:message key="ydwx_wxbj_add_38" defVal="居左" fileName="ydwx"></emp:message></option>
	                    <option value="right"><emp:message key="ydwx_wxbj_add_39" defVal="居右" fileName="ydwx"></emp:message></option>
	                    <option value="center"><emp:message key="ydwx_wxbj_add_22" defVal="居中" fileName="ydwx"></emp:message></option>
	                </select>
				</td>
	            <td><span><emp:message key="ydwx_wxbj_add_40" defVal="单元格对齐：" fileName="ydwx"></emp:message></span></td>
				<td>
					<select id="alignment" style="">
	                    <option value="left"><emp:message key="ydwx_wxbj_add_41" defVal="左对齐" fileName="ydwx"></emp:message></option>
	                    <option value="center"><emp:message key="ydwx_wxbj_add_22" defVal="居中" fileName="ydwx"></emp:message></option>
	                    <option value="right"><emp:message key="ydwx_wxbj_add_42" defVal="右对齐" fileName="ydwx"></emp:message></option>
	                </select>
	            </td>
	        </tr>
	        <tr>
	            <td><span><emp:message key="ydwx_wxbj_add_43" defVal="表格背景颜色：" fileName="ydwx"></emp:message></span></td>
				<td><input type="text" style="width: 68px!important;width: 65px;" id="background-color" onclick="show(this)" /></td>
	        </tr>
	    </table>
	</div>
	<script type="text/javascript">
	    var activeEl;
	    var colorpicker = new parent.baidu.editor.ui.Popup({
	            content: new parent.baidu.editor.ui.ColorPicker({
	                noColorText: getJsLocaleMessage("ydwx","ydwx_wxbj_153")
	            })
	    });
	    colorpicker.render();
	    colorpicker.content.onpickcolor = function(t, color){
	        activeEl.value = color.toUpperCase();
	        colorpicker.hide();
	    };
	    colorpicker.content.onpicknocolor = function(t, color){
	        activeEl.value = '';
	        colorpicker.hide();
	    };
	    parent.baidu.editor.dom.domUtils.on(document, 'mousedown', function (evt){
	        parent.baidu.editor.ui.Popup.postHide(evt.target || evt.srcElement);
	    });
	    var state = editor.queryCommandState("edittable"),oldtable;
	    if(state == 0){
	        var range = editor.selection.getRange(),tmptd;
	            tmptd = parent.baidu.editor.dom.domUtils.findParentByTagName(range.startContainer, 'td', true);
	            oldtable = tmptd.parentNode.parentNode.parentNode;
	        if(oldtable){
	            var rowlength = 0,
	                    celllength = 0,
	                    alignmode = parent.baidu.editor.dom.domUtils.getComputedStyle(tmptd,"text-align");
	            for(var r=0,row;row = oldtable.rows[r++];){
	                var tmpcells = 0;
	                for(var c = 0,cell;cell = row.cells[c++];){
	                    if(cell.style.display != "none"){
	                        tmpcells++;
	                    }
	                }
	                rowlength += tmpcells>0 ? 1 : 0;
	                celllength = tmpcells > celllength ? tmpcells : celllength;
	            }
	            g("rowCount").value = rowlength;
	            g("rowCount").disabled = true;
	            g("colCount").value = celllength;
	            g("colCount").disabled = true;
	            g("cellpadding").value = oldtable.getAttribute("cellpadding") || "";
	            g("cellspacing").value = oldtable.getAttribute("cellspacing") || "";
	            g("tableWidth").value = oldtable.getAttribute("width") || "";
	            g("tableHeight").value = oldtable.getAttribute("height") || "";
	            g("border-color").value = oldtable.getAttribute("bordercolor").toUpperCase() || "";
	            g("border").value = oldtable.getAttribute("border") || 1;
	            g("background-color").value = parent.baidu.editor.dom.domUtils.getComputedStyle(oldtable,"background-color")!= "transparent" ? parent.baidu.editor.dom.domUtils.getComputedStyle(oldtable,"background-color"): "";
	            g("alignment").value = alignmode;
	            g("tablealign").value = oldtable.getAttribute("align") || "";
	        }
	    }else{
	        var ipt = g("rowCount");
	        var isIE = !!window.ActiveXObject;
	        if ( isIE ) {
	            setTimeout( function () {
	                var r = ipt.createTextRange();
	                r.collapse( false );
	                r.select();
	            } );
	        }
	        ipt.focus();
	    }
	    function show(el){
	        activeEl = el;
	        colorpicker.showAnchor(el);
	    }
	    function g(id){
	        return document.getElementById(id);
	    }
	    dialog.onok = function(){
	        var rows = g("rowCount").value,
	            cols = g("colCount").value,
	            width = g("tableWidth").value,
	            height = g("tableHeight").value,
	            border = g("border").value,
	            cellspacing = g("cellspacing").value,
	            cellpadding = g("cellpadding").value,
	            align = g("alignment").value,
	            bordercolor = g("border-color").value,
	            backgroundcolor = g("background-color").value,
	            tablealign = g("tablealign").value;
	        if(!/^[1-9]+[0-9]*$/.test(rows)){
	            alert(getJsLocaleMessage("ydwx","ydwx_wxbj_154"));
	            return false;
	        }
	        if(!/^[1-9]+[0-9]*$/.test(cols)){
	            alert(getJsLocaleMessage("ydwx","ydwx_wxbj_155"));
	            return false;
	        }
	        if(width && !/^[1-9]+[0-9]*$/.test(width)){
	            alert(getJsLocaleMessage("ydwx","ydwx_wxbj_156"));
	            return false;
	        }
	        if(height && !/^[1-9]+[0-9]*$/.test(height)){
	            alert(getJsLocaleMessage("ydwx","ydwx_wxbj_157"));
	            return false;
	        }
	        if(border && !/^[0-9]*$/.test(border)){
	            alert(getJsLocaleMessage("ydwx","ydwx_wxbj_158"));
	            return false;
	        }
	        if(cellspacing && !/^[0-9]*$/.test(cellspacing)){
	            alert(getJsLocaleMessage("ydwx","ydwx_wxbj_159"));
	            return false;
	        }
	        if(cellpadding && !/^[0-9]*$/.test(cellpadding)){
	            alert(getJsLocaleMessage("ydwx","ydwx_wxbj_160"));
	            return false;
	        }
	        if(bordercolor && !/^#(?:[0-9a-fA-F]{3}|[0-9a-fA-F]{6})$/.test(bordercolor)){
	            alert(getJsLocaleMessage("ydwx","ydwx_wxbj_161"));
	            return false;
	        }
	        if(backgroundcolor && !/^#(?:[0-9a-fA-F]{3}|[0-9a-fA-F]{6})$/.test(backgroundcolor)){
	            alert(getJsLocaleMessage("ydwx","ydwx_wxbj_162"));
	            return false;
	        }
	        var table = {
	            numRows : rows,
	            numCols : cols,
	            border : border,
	            cellpadding : cellpadding,
	            cellspacing : cellspacing,
	            width : width,
	            height : height,
	            align : align,
	            bordercolor:bordercolor,
	            backgroundcolor:backgroundcolor,
	            tablealign : tablealign
	        };
	        if(oldtable){
	            editor.execCommand('edittable',table );
	        }else{
	            editor.execCommand('inserttable',table );
	        }
	    };
	    
	</script>
	</body>
</html>
