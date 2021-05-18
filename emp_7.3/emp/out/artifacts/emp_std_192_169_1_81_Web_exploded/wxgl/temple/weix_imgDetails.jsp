<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@page import= "org.apache.commons.beanutils.DynaBean"%>
<%@page import= "java.util.LinkedHashMap"%>
<%@page import="com.montnets.emp.entity.wxgl.LfWeiRimg" %>
<%@page import="java.util.ArrayList"%>
<%@page import="com.montnets.emp.ottbase.constant.WXStaticValue"%>
<%@page import="com.montnets.emp.ottbase.util.GlobalMethods"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
    String path = request.getContextPath();
String basePath = request.getScheme() + "://"
		+ request.getServerName() + ":" + request.getServerPort()
		+ path + "/";
String iPath = request.getRequestURI().substring(0,
		request.getRequestURI().lastIndexOf("/"));
String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
//设备类型
String fromTp = (String)request.getAttribute("fromTp");
LfWeiRimg wcrimg = (LfWeiRimg)request.getAttribute("wcrimg");
if(wcrimg==null){
	wcrimg = new LfWeiRimg();
}

java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
%>
  <%!public static String turn(String str){
//下面的代码将字符串以正确方式显示（包括回车，换行，空格）
	 if(str==null) return "";       
     String html = str;
     //html = html.replaceAll("\\\"", "&quot;");
     //html.replaceAll("(<)(/?+[^aA/][^>]*)(>)","&lt$2&gt");
     //html = html.replaceAll( "\r\n", "<br/>");
     return html;
 
}%>  
<!DOCTYPE HTML>
<html lang="en-US">
<head>
	<meta charset="UTF-8">
		<%@include file="/common/common.jsp" %>
		<meta name="viewport" content="width=device-width,initial-scale=1.0, maximum-scale=1.0, user-scalable=0">
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/reset.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/chat.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<%}%>
	</head>
	<body>
		 <div id="container" style="rgba(0,0,0,0.5);">
			<%if("pc".equals(fromTp)) {%>
			<div class="chatMainPanel">
			  <div class="chatTitle">
			    <p class="chatName">
			  	  <a  style="cursor:pointer;text-decoration:none;color:#FFFFFF" href="Javascript:window.history.go(-1)"><emp:message key="wxgl_button_8" defVal="返回" fileName="wxgl"/></a>
			    </p>
			  </div>
			</div>
			<% }%>
		
			<div class="w_article">
			  <div class="article-title"><h2><%=turn(wcrimg.getTitle()) %></h2></div>
			  <div class="article-time"><%=formatter.format(wcrimg.getCreatetime())%></div>
			  <div class="imgWrap">
			  <img class="i-img" src="<%=wcrimg.getPicurl()%>" width="100%">
			  </div>
			  <div class="article-content">
			  <%=turn(wcrimg.getDescription()) %>
			  </div>
			  <%if(!"1".equals(wcrimg.getSourceUrl())&&wcrimg.getLink()!=null&&!"".equals(wcrimg.getLink())){ %>
			  	<a style="color:#2e7dc6;text-decoration:none" href="<%=wcrimg.getLink() %>"><emp:message key="wxgl_gzhgl_title_383" defVal="原文连接" fileName="wxgl"/></a>
			  <% }%>
			</div>

	   </div>
<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/wxgl_<%=langName%>.js"></script>
<script language="javascript" src="<%=commonPath%>/common/js/myjquery-c.js?V=<%=StaticValue.getJspImpVersion() %>" type="text/javascript"></script>
<script language="javascript" src="<%=iPath%>/js/jQuery.imgAutoSize.js?V=<%=StaticValue.getJspImpVersion() %>" type="text/javascript"></script>
<script type="text/javascript">
jQuery(function ($) {
	$('.imgWrap').imgAutoSize();
	var bodyW = $('body').width();
	$('body img').each(
    	function(){
        	var thisW = $(this).width();
			if(thisW > bodyW)
			{
			    $(this).css('width','100%');
			    $(this).css('height','auto');
			    //$(this).css('width',bodyW-20);
			    //$(this).css('height',((bodyW-20)/thisW)*$(this).height());
			}
	    }
  	);
});
</script>
	</body>
</html>