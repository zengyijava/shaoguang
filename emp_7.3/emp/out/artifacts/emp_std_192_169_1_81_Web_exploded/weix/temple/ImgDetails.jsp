<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@page import= "org.apache.commons.beanutils.DynaBean"%>
<%@page import= "java.util.LinkedHashMap"%>
<%@page import="com.montnets.emp.entity.weix.LfWcRimg" %>
<%@page import="java.util.ArrayList"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@page import="com.montnets.emp.weix.common.util.GlobalMethods"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@include file="/common/common.jsp" %>
<%@ taglib prefix="emp"
	uri="http://www.montnets.com/emp/i18n/tags/simple"%>
	
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String iPath = request.getRequestURI().substring(0,
			request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	String tempId = (String)request.getAttribute("tempId");
	
	//Jsp页面中获取session中的语言设置
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
	
	//设备类型
	String fromTp = (String)request.getAttribute("fromTp");
	LfWcRimg wcrimg = (LfWcRimg)request.getAttribute("wcrimg");
	if(wcrimg==null){
		wcrimg = new LfWcRimg();
	}
	
	java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	//使用集群，文件服务器的地址
	String filePath = "";
	if("pc".equals(fromTp)){
		filePath = GlobalMethods.getWeixFilePath();
	}else{
		filePath = GlobalMethods.getWeixBasePath();
	}
%>
  <%!
public static String turn(String str){
//下面的代码将字符串以正确方式显示（包括回车，换行，空格）
	 if(str==null) return "";       
     String html = str;
     html = html.replaceAll("\\\"", "&quot;");
     html.replaceAll("(<)(/?+[^aA/][^>]*)(>)","&lt$2&gt");
     html = html.replaceAll( "\r\n", "<br/>");
     return html;
 
}
%>  
<!DOCTYPE HTML>
<html lang="en-US">
<head>
	<meta charset="UTF-8">
		<meta name="viewport" content="width=device-width,initial-scale=1.0, maximum-scale=1.0, user-scalable=0">
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/reset.css"/>
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/chat.css"/>
		<script language="javascript" src="<%=commonPath%>/common/js/myjquery-a.js" type="text/javascript"></script>
	</head>
	<body>
		 <div id="container" style="rgba(0,0,0,0.5);">
			<div class="chatMainPanel">
			  <div class="chatTitle">
			    <p class="chatName">
			    <%if("pc".equals(fromTp)) {%>
			  	  <a  style="cursor:pointer;text-decoration:none;color:#FFFFFF" href="Javascript:window.history.go(-1)"><emp:message key="common_btn_10" defVal="返回"
											fileName="common"></emp:message></a>
			  	<% }%>
			    </p>
			  </div>
			</div>
		
			<div class="w_article">
			  <div class="article-title"><h2><%=turn(wcrimg.getTitle()) %></h2></div>
			  <div class="article-time"><%=formatter.format(wcrimg.getCreatetime())%></div>
			  <div class="imgWrap">
			  <img class="i-img" src="<%= filePath+ wcrimg.getPicurl()%>" width="170">
			  </div>
			  <div class="article-content">
			  <%=turn(wcrimg.getDescription()) %>
			  
			  </div>
			</div>

	   </div>
  		 <script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/weix_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
		<script language="javascript" src="<%=iPath%>/js/jQuery.imgAutoSize.js" type="text/javascript"></script>
<script type="text/javascript">
jQuery(function ($) {
	$('.imgWrap').imgAutoSize();
});
</script>
	</body>
</html>