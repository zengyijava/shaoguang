<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.Iterator" %>
<%@page import="java.util.List"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@page import="com.montnets.emp.netnews.entity.LfWXDataType"%>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
String path = request.getContextPath();
String iPath = request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String langName = (String)session.getAttribute("emp_lang");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
        "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head><%@include file="/common/common.jsp" %>
    <title></title>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
    <script type="text/javascript" src="../internal.js"></script>
    <script type="text/javascript" src="../myjquery-f.js"></script>
    <script type="text/javascript" src="../jquery.form.js"></script>
    <style type="text/css">
        *{color: #838383} 
        body { 
            font-size: 12px;
            width:382px;
            overflow: hidden;
            margin:0px;padding:0px;
        }
         span.txt{width: 60px;height:30px;line-height: 30px;display: block;float:left}
         span.txt1{width: 60px;height:30px;line-height: 30px;display: block;float:left}
        .content{
            padding: 9px 0px 0px 15px;
            height:100%;
        }
        #link_href,
        #title{width:210px;height:21px;background: #FFF;border:1px solid #d7d7d7;padding: 0px; margin: 0px; line-height:21px}
        #link_href{width:210px}
        .content table{padding:0px;margin:0px}
        .content table tr{padding:0px;margin:0; list-style: none;height: 25px; line-height: 25px;}
        .download_tit{width:65px;display:block;float:left;text-align:right;}
        .red{color:red;}
    </style>
    <%if(StaticValue.ZH_HK.equals(empLangName)){%>
    <link rel="stylesheet" type="text/css" href="<%=path%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
    <style type="text/css">
        .download_tit{width:100px;display:block;float:left;text-align:left;}
        table {width: 450px;}
        #title{width:190px;}
        #link_href{width:190px}
    </style>
    <%}%>
</head>
<body onload="javascript:loadfile('1');">
<div class="content">
    <table>
		<tr>
            <td>
            	<span class="download_tit"><emp:message key="ydwx_wxbj_add_3" defVal="下载文件：" fileName="ydwx"></emp:message></span><select style="width:215px;" id="downfile" onclick="chang(this)"></select>
            	<br/> <span id="msgfile" class="red"></span>
            </td>
        </tr>
        <tr>
            <td><span class="download_tit"><emp:message key="ydwx_wxbj_add_4" defVal="下载地址：" fileName="ydwx"></emp:message></span>
                <input id="link_href" type="text" disabled="disabled"/>&nbsp;&nbsp;<emp:message key="ydwx_wxbj_add_5" defVal="*不能输入" fileName="ydwx"></emp:message> <br/>
            <span id="msg2" class="red"></span></td>
        </tr>
        <tr>
            <td>
                <span class="download_tit"><emp:message key="ydwx_wxbj_add_6" defVal="标 题：" fileName="ydwx"></emp:message></span>
            	<input id="title" type="text" size="10"/>
            </td>
        </tr>

    </table>
</div>
<script type="text/javascript" src="<%=basePath%>/common/i18n/i18nUtil.js"></script>
<script type="text/javascript" src="<%=basePath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>	
<script type="text/javascript" src="<%=basePath%>/common/i18n/<%=langName%>/ydwx_<%=langName%>.js"></script>	
<script type="text/javascript">
	/**
	初始化素材库
**/ 
		function loadfile(pagesize){
		var str ="<table>";
			//发送请求，并将在onComplete选项中调用回调函数
			var lguserid = $(window.parent.document).find("#lguserid").val();
			var lgcorpcode = $(window.parent.document).find("#lgcorpcode").val();
			$.post('<%=path %>/wx_ueditor.htm?method=getVideoUeditor',{Action:"post",pagesize:pagesize,filetype:"2",lguserid:lguserid,lgcorpcode:lgcorpcode},function(result){
				//	alert(result.success);
					if(result.success){
							var dataObj=result.msg;//转换为json对象  
							if(dataObj){
								$("<option>").appendTo($("#downfile")).html(getJsLocaleMessage("ydwx","ydwx_common_qxz")).val("");
								$.each(dataObj.root, function(idx,item){
									$("<option>").appendTo($("#downfile")).html(item.file_name).val(item.file_url); 
								}); 
							}else{
								document.getElementById("msgfile").innerHTML=getJsLocaleMessage("ydwx","ydwx_wxbj_132");
							} 
					}else{
					//	alert(result.success);
							alert(getJsLocaleMessage("ydwx","ydwx_common_czshb"));
						//	document.getElementById("tab1").innerHTML="正在加载素材......";
					}
				},"json");			
	
		}
		
	function chang(da){
		document.getElementById("msg2").innerHTML="";
		if($("#downfile ").val()){
			G("link_href").value=$("#downfile ").val();
			G("title").value=$("#downfile").find("option:selected").text(); 
		}
		
	}
	
    function G(id){
        return document.getElementById(id);
    }
    
    dialog.onok = function (){
        var href = G('link_href').value || '';
        var obj = {},td,link;
        obj.href = href.replace(/^\s+|\s+$/g, '');
 		 	
        if(G("link_href").value==""){
            G("msg2").innerHTML = getJsLocaleMessage("ydwx","ydwx_wxbj_133");
            return false;
        }
        obj.href = G("link_href").value;
        if(G("title").value.replace(/^\s+|\s+$/g, '')){
            obj["title"] = G("title").value.replace(/^\s+|\s+$/g, '');
        }
        var url=obj.href;
        //判断下该文件是否存在
       $.post('<%=path %>/wx_ueditor.htm?method=isExist',{url:url},function(result){

		if(result=="true"){
			url="<%=basePath %>ydwx/wap/404.jsp";
		}
		editor.execCommand('downfile', "<p><a href=\""+url+"\" title=\""+obj.title+"\" style=\"float: none;text-decoration: none;\"><img style=\"FLOAT: none;border:0px;\" src=\"<%=path%>/ydwx/images/downfile.jpg\"/> </a></p>");
	       });
        dialog.close();
        
    }
    var keyHandle = function(evt){
        evt = evt || window.event;
        if (evt.keyCode == 13) {
            handleDialogOk();
            return false;
        }
    }
    
</script>
</body>
</html>
