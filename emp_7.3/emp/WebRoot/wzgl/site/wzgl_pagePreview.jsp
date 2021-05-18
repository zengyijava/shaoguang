<?xml version="1.0" encoding="UTF-8"?>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="org.json.simple.JSONObject"%>
<%@ page import="org.json.simple.JSONArray"%>
<%@ page import="com.montnets.emp.entity.site.LfSitPage"%>
<%@ page import="com.montnets.emp.entity.site.LfSitPlant"%>
<%@ page import="com.montnets.emp.ottbase.util.GlobalMethods"%>
<%@ page import="com.montnets.emp.util.StringUtils"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@include file="/common/common.jsp" %>
<%@ taglib prefix="emp"
	uri="http://www.montnets.com/emp/i18n/tags/simple"%>
	
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
String iPath = request.getRequestURI().substring(0, request.getRequestURI().lastIndexOf("/"));
String inheritPath = iPath.substring(0, iPath.lastIndexOf("/"));
String commonPath = inheritPath.substring(0, inheritPath.lastIndexOf("/"));
String skin = session.getAttribute("stlyeSkin") == null ? "default" : (String)session.getAttribute("stlyeSkin");

//Jsp页面中获取session中的语言设置
String langName = (String)session.getAttribute(StaticValue.LANG_KEY);	

//使用集群，文件服务器的地址
String filePath = GlobalMethods.getWeixFilePath();
String serverPath = GlobalMethods.getWeixBasePath();
LfSitPage otSitPage = (LfSitPage)request.getAttribute("otSitPage");
%> 
<!DOCTYPE html PUBLIC "-//WAPFORUM//DTD XHTML Mobile 1.0//EN" "http://www.wapforum.org/DTD/xhtml-mobile10.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<meta name='viewport' content='width=device-width, user-scalable=no, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0'/>
		<meta name='apple-mobile-web-app-status-bar-style' content='black'/>
		<meta name='apple-mobile-web-app-capable' content='yes'/>
		<link rel="stylesheet" type="text/css" href="<%=path%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" type="text/css" href="<%=path%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" type="text/css" href="<%=path%>/wxcommon/css/global.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" type="text/css" href="<%=path%>/wzgl/site/css/site.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=path%>/wzgl/site/normal/css/normal.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<%--<link rel="stylesheet" href="<%=path%>/wxcommon/css/flexslider.css" /> --%>
	    <script type="text/javascript" src="<%=path%>/common/js/myjquery-c.js?V=<%=StaticValue.getJspImpVersion() %>" ></script>
	    <script type="text/javascript" src="<%=path%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	    <%--<script type="text/javascript" src="<%=path%>/wxcommon/js/jquery.flexslider-min.js"></script> --%>
		<script type="text/javascript" src="<%=path %>/common/widget/artDialog/artDialog.js?skin=default"></script>
		<script type="text/javascript" src="<%=path %>/common/widget/artDialog/iframeTools.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		
		<link href="<%=path%>/wxcommon/skitter/css/skitter.styles.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" media="all" rel="stylesheet" />
		<link href="<%=path%>/wxcommon/skitter/css/styles.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" media="all" rel="stylesheet" />
		<link href="<%=path%>/wzgl/site/css/new.skitter.styles.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" media="all" rel="stylesheet" />
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/wzgl_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
		<script type="text/javascript" language="javascript" src="<%=path%>/wxcommon/skitter/js/jquery.easing.myjquery-p.js"></script>
		<script type="text/javascript" language="javascript" src="<%=path%>/wxcommon/skitter/js/jquery.skitter.min.js"></script>
		<script type="text/javascript" src="<%=path %>/wzgl/site/js/addSite.js"></script>				
		<script type="text/javascript">
			$(document).ready(function() {
				//图片轮询的全局变量
				/*$(function(){
					$('.flexslider').flexslider({
					    animation: "slide",
					    controlNav: false
					  });
				});*/
				
				//加载页面信息
				loadPageData();

				//手机访问微站-点击"tabs"
				$("ul.mytabs li a").live("click",function(){
					$("ul.mytabs li.active").removeClass("active");
					$(this).parent().addClass("active");
					$("#mytabs_c").next().children().hide();
					$("#mytabs_c").next().find("#"+$(this).attr('data-name')).show();
				});
			});	
	
			//加载页面数据
			function loadPageData(){
				var pageId = $("#pageId").val();
				var pageType = $("#pageType").val();
				var serverPath = "<%=GlobalMethods.getWeixBasePath()%>";
				var url =  "<%=request.getContextPath()%>/wzgl_siteManager.hts?method=getPageInfo&isPreview=1&pageId=" + pageId + "&ptype=" + pageType + "&time=" + new Date().getTime();
				 $.get(url,function(data){
					 if(data == "outOfLogin"){
				 			window.location.href = pathUrl + "/common/logoutEmp.html";
				 	 }
					 if(data=="fail"){
						 alert(getJsLocaleMessage("wzgl","wzgl_qywx_form_text_38"));
					 }else{
						 $(".page_item_view").remove();
						 $(window.document.body).prepend(data);
					 }
				 });
			}
			try{
				//隐藏微信中网页底部导航栏
				document.addEventListener('WeixinJSBridgeReady', function onBridgeReady() {
					WeixinJSBridge.call('hideToolbar');
				});
			}catch(e){
				window.console&&window.console.log(e.message);
			}
			
		</script>
		<%if(StaticValue.ZH_HK.equals(langName)){%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
	</head>
	<body>
		<div id="pageStuff" style="display:none;">
			<input type="hidden" id="basePath" value="<%=basePath%>"/>
			<input type="hidden" id="basePath" value="<%=serverPath%>"/>
			<input type="hidden" id="pageId" value="<%=otSitPage.getPageId()%>"/>
			<input type="hidden" id="pageType" value="<%=otSitPage.getPageType()%>"/>
		</div>
	</body>
</html>