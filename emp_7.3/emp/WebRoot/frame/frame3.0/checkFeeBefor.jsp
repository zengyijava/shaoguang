<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
String ye = request.getParameter("ye"); 
String mmsBanalce = request.getParameter("mmsBanalce"); 
String time = request.getParameter("time");
String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
String lguserid = request.getParameter("lguserid"); 
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
  <head><%@include file="/common/common.jsp"%>
    <title><emp:message key="common_frame2_checkFee_1" defVal="运营商余额查看" fileName="common"></emp:message></title>
    <link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
	<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
	<link href="<%=skin%>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
	<link href="<%=skin%>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
    <%if(StaticValue.ZH_HK.equals(empLangName)){%>
    <link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
    <%}%>
	
  </head>
  
  <body onload="loadFee()"><br/><center><font size="+1" color="red"><emp:message key="common_frame2_checkFee_20" defVal="加载中，请稍候" fileName="common"></emp:message></font></center>
  <script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
	<script>
  function loadFee()
  {
  	$("body").load("<%=path%>/checkFee.htm?ye=<%=ye%>&mmsBanalce=<%=mmsBanalce%>&time=<%=time%>&lguserid=<%=lguserid%>");
  }
  function reLoadFee()
  {
	  $("body").html("<br/><center><font size='+1' color='red'>"+getJsLocaleMessage("common","common_frame2_checkFee_20")+"</font></center>");
	  setTimeout("getFee()",1000);
  }
  function getFee()
  {
  	$("body").load("<%=path%>/checkFee.htm?ye=<%=ye%>&mmsBanalce=<%=mmsBanalce%>&isreload=1&time=<%=time%>&lguserid=<%=lguserid%>");
  }
  </script>
  </body>
</html>
