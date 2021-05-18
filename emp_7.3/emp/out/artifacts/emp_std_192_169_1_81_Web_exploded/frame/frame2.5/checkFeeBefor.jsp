<%@ page language="java" import="com.montnets.emp.common.constant.ViewParams" pageEncoding="UTF-8"%>
<%@page import="com.montnets.emp.i18n.util.MessageUtils"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
String lguserid = request.getParameter("lguserid"); 
String time = request.getParameter("time");
String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");

String isshowww = request.getParameter("isshowww");
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
  
  <body onload="loadFee()">
    <%if(!"0".equals(isshowww)){
        String location = ViewParams.getPositionWhitIn(MessageUtils.extractMessage("common","common_index",request), MessageUtils.extractMessage("common","common_balanceQuery",request));
        location = location.replace("当前位置：",MessageUtils.extractMessage("common","common_location",request));
    %>
    <%=location%>
  	<%}%>
	<div id="feecontent">
  <br/><center><font size="+1" class="zhu"><emp:message key="common_frame2_checkFee_20" defVal="加载中，请稍候" fileName="common"></emp:message></font></center>
  </div>
  <script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
	<script>
  function loadFee()
  {
	  var ttt= Date.parse(new Date());
  	$("#feecontent").load("<%=path%>/checkFee.htm?method=findSpfee&lguserid=<%=lguserid%>&time="+ttt);
  }
  function reLoadFee()
  {
	  $("#feecontent").html("<br/><center><font size='+1' class='zhu'>"+getJsLocaleMessage("common","common_frame2_checkFee_20")+"</font></center>");
	  setTimeout("getFee()",1000);
  }
  function getFee()
  {
	  var ttt= Date.parse(new Date());
  	$("#feecontent").load("<%=path%>/checkFee.htm?method=findSpfee&isreload=1&lguserid=<%=lguserid%>&time="+ttt);
  }
  </script>
  </body>
</html>
