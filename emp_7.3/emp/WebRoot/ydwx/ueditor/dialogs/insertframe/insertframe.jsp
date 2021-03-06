<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort();
String inheritPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/nl"));

%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>插入框架</title>
    <script type="text/javascript" src="../internal.js"></script>
    <style type="text/css">
        * {
            color: #838383
        }

        body {
            font-size: 12px;
            width: 320px;
            height: 153px;
            overflow: hidden;
            margin: 0;
            padding: 0;
        }
        .warp {
            padding: 20px 0 0 15px;
            height: 100%;
            position: relative;
        }
        #url {
            width: 290px;
            margin-bottom: 2px
        }
        table td{
            padding:5px 0px;
        }

    </style>
</head>
<body>
<div class="warp">
        <table width="300" cellpadding="0" cellspacing="0">
            <tr>
                <td><div style="display:none;"><span>允许滚动条：</span><input type="checkbox" id="scroll"/></div></td>
                <td><div style="display:none;"><span>显示框架边框：</span><input type="checkbox" id="frameborder"/></div></td>
            </tr>

            <tr>
                <td colspan="2">
                	<input style="width:200px" id="url" type="hidden" value="<%=basePath %><%=inheritPath %>/nl/textarea.jsp"/>
           
               		<div style="display:none;"><span>宽度：</span><input style="width:200px" type="text" id="width" value="190"/> px</div>

         
              		<div style="display:none;"><span>高度：</span><input style="width:200px" type="text" id="height" value="75"/> px</div>
           
                	<span>对齐方式：</span>
                    <select id="align">
                        <option value="">默认</option>
                        <option value="left">左对齐</option>
                        <option value="right">右对齐</option>
                        <option value="middle">居中</option>
                    </select>
                </td>
            </tr>
        </table>
</div>

<script type="text/javascript">
    var iframe = editor._iframe;
    function g(id){
        return document.getElementById( id );
    }
    if(iframe){
        g("url").value = iframe.getAttribute("src") ? iframe.getAttribute("src") : "";
        g("width").value = iframe.getAttribute("width") ?  iframe.getAttribute("width") : "";
        g("height").value = iframe.getAttribute("height") ? iframe.getAttribute("height") : "";
        g("scroll").checked = (iframe.getAttribute("scrolling") == "yes") ? true : false;
        g("frameborder").checked = (iframe.getAttribute("frameborder") == "1") ? true : false;
        g("align").value = iframe.align ? iframe.align : "";
    }
    function queding(){
        var  url = g("url").value.replace(/^\s*|\s*$/ig,""),
                width = g("width").value,
                height = g("height").value,
                scroll = g("scroll"),
                frameborder = g("frameborder"),
                float = g("align").value,
                newIframe = editor.document.createElement("iframe"),
                div;
        if(!url){
            alert("请输入地址！");
            return false;
        }
        newIframe.setAttribute("src",/http:\/\/|https:\/\//ig.test(url) ? url : "http://"+url);
        /^[1-9]+[.]?\d*$/g.test( width ) ? newIframe.setAttribute("width",width) : "";
        /^[1-9]+[.]?\d*$/g.test( height ) ? newIframe.setAttribute("height",height) : "";
        scroll.checked ?  newIframe.setAttribute("scrolling","yes") : newIframe.setAttribute("scrolling","no");
        frameborder.checked ?  newIframe.setAttribute("frameborder","1",0) : newIframe.setAttribute("frameborder","0",0);
        float ? newIframe.setAttribute("align",float) :  newIframe.setAttribute("align","");
        if(iframe){
            iframe.parentNode.insertBefore(newIframe,iframe);
            parent.baidu.editor.dom.domUtils.remove(iframe);
        }else{
            div = editor.document.createElement("div");
            div.appendChild(newIframe);
            editor.execCommand("inserthtml",div.innerHTML);
        }
        editor._iframe = null;
        dialog.close();
    }
    dialog.onok = queding;
    g("url").onkeydown = function(evt){
        evt = evt || event;
        if(evt.keyCode == 13){
            queding();
        }
    }
    var ipt = g( "url" );
    var isIE = !!window.ActiveXObject;
    if ( isIE ) {
        setTimeout( function () {
            var r = ipt.createTextRange();
            r.collapse( false );
            r.select();
        } );
    }
  //  ipt.focus(); 
</script>
</body>
</html>