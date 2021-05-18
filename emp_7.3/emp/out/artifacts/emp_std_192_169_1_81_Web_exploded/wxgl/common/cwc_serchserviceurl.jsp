<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
	String path = request.getContextPath();
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
	
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
  	<meta charset="utf-8">
  	<%@include file="/common/common.jsp" %>
	<meta name="viewport" content="width=device-width,height=device-height,target-densitydpi=high-dpi,initial-scale=0.7,minimum-scale=0.5, maximum-scale=1.0, user-scalable=yes"/>  
	<title><emp:message key="wxgl_gzhgl_title_25" defVal="附近服务网点查询" fileName="wxgl"/></title>
    <link href="<%=iPath %>/css/cwc_servicestation.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
    <%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<%}%>
  </head>
  
	<body style="word-break: break-all;">
	<div class="all">
	  <div class="inquiry01">
	    <div class="inquiry01_title01" style="font-size:20px;font-weight: bold;"><strong><emp:message key="wxgl_gzhgl_title_38" defVal="请选择您的产品" fileName="wxgl"/></strong></div>
	      <div class="inquiry01_list02">
	        <h1><emp:message key="wxgl_gzhgl_title_39" defVal="Lenovo产品" fileName="wxgl"/></h1>
	        <ul>
	          <li><a href="cwc_stationService.hts?method=findMap&amp;pl=101&amp;x=22.542669&amp;y=113.935265&amp;c=%E6%B7%B1%E5%9C%B3"><emp:message key="wxgl_gzhgl_title_40" defVal="笔记本" fileName="wxgl"/></a></li>
	          <li><a href="cwc_stationService.hts?method=findMap&amp;pl=101&amp;x=22.542669&amp;y=113.935265&amp;c=%E6%B7%B1%E5%9C%B3"><emp:message key="wxgl_gzhgl_title_42" defVal="台式机" fileName="wxgl"/></a></li>
	        </ul>
	        <h1><emp:message key="wxgl_gzhgl_title_41" defVal="Think产品" fileName="wxgl"/></h1>
	        <ul>
	          <li><a href="cwc_stationService.hts?method=findMap&amp;pl=101&amp;x=22.542669&amp;y=113.935265&amp;c=%E6%B7%B1%E5%9C%B3"><emp:message key="wxgl_gzhgl_title_40" defVal="笔记本" fileName="wxgl"/></a></li>
	          <li><a href="cwc_stationService.hts?method=findMap&amp;pl=101&amp;x=22.542669&amp;y=113.935265&amp;c=%E6%B7%B1%E5%9C%B3"><emp:message key="wxgl_gzhgl_title_42" defVal="台式机" fileName="wxgl"/></a></li>
	          <li><a href="cwc_stationService.hts?method=findMap&amp;pl=101&amp;x=22.542669&amp;y=113.935265&amp;c=%E6%B7%B1%E5%9C%B3"><emp:message key="wxgl_gzhgl_title_43" defVal="服务器" fileName="wxgl"/></a></li>
	          <li><a href="cwc_stationService.hts?method=findMap&amp;pl=101&amp;x=22.542669&amp;y=113.935265&amp;c=%E6%B7%B1%E5%9C%B3"><emp:message key="wxgl_gzhgl_title_44" defVal="工作站" fileName="wxgl"/></a></li>
	          <li><a href="cwc_stationService.hts?method=findMap&amp;pl=101&amp;x=22.542669&amp;y=113.935265&amp;c=%E6%B7%B1%E5%9C%B3"><emp:message key="wxgl_gzhgl_title_45" defVal="显示器" fileName="wxgl"/></a></li>
	        </ul>
	    
	 
	        <h1><emp:message key="wxgl_gzhgl_title_46" defVal="移动设备" fileName="wxgl"/></h1>
	        <ul>
	          <li><a href="cwc_stationService.hts?method=findMap&amp;pl=101&amp;x=22.542669&amp;y=113.935265&amp;c=%E6%B7%B1%E5%9C%B3">Smart TV </a></li>
	          <li><a href="cwc_stationService.hts?method=findMap&amp;pl=101&amp;x=22.542669&amp;y=113.935265&amp;c=%E6%B7%B1%E5%9C%B3" style="width:130px;"><emp:message key="wxgl_gzhgl_title_47" defVal="联想手机/Pad" fileName="wxgl"/></a></li>
	        </ul>
	        <h1 style="background-image: url(../images/20130626_title03.png);"><emp:message key="wxgl_gzhgl_title_48" defVal="打印机及数码选件" fileName="wxgl"/></h1>
	        <ul>
	          <li><a href="cwc_stationService.hts?method=findMap&amp;pl=101&amp;x=22.542669&amp;y=113.935265&amp;c=%E6%B7%B1%E5%9C%B3"> <emp:message key="wxgl_gzhgl_title_49" defVal="外设数码" fileName="wxgl"/></a></li>
	          <li><a href="cwc_stationService.hts?method=findMap&amp;pl=101&amp;x=22.542669&amp;y=113.935265&amp;c=%E6%B7%B1%E5%9C%B3"><emp:message key="wxgl_gzhgl_title_50" defVal="投影仪" fileName="wxgl"/></a></li>
	          <li><a href="cwc_stationService.hts?method=findMap&amp;pl=101&amp;x=22.542669&amp;y=113.935265&amp;c=%E6%B7%B1%E5%9C%B3"><emp:message key="wxgl_gzhgl_title_51" defVal="光墨打印机" fileName="wxgl"/></a></li>
	          <li><a href="cwc_stationService.hts?method=findMap&amp;pl=101&amp;x=22.542669&amp;y=113.935265&amp;c=%E6%B7%B1%E5%9C%B3" style="width:200px;"><emp:message key="wxgl_gzhgl_title_52" defVal="打印机/多功能一体机" fileName="wxgl"/></a></li>
	        </ul>
	
	     </div>
	  </div>
	  <div class="logo"></div>
	</div>
	</body>
</html>
