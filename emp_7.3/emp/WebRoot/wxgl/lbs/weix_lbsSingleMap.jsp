<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.montnets.emp.ottbase.constant.WXStaticValue"%>
<%@page import="com.montnets.emp.entity.lbs.LfLbsPios"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()+ path + "/";
	String iPath = request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String shortInheritPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	//百度地图密钥
	String ak = WXStaticValue.BAIDU_MAP_AK;
	//用户LNG 经度
	String userlng = (String)request.getParameter("userlng");
	//用户lat 纬度
	String userlat = (String)request.getParameter("userlat");
	//公众帐号
	String aid = (String)request.getParameter("aid");
	//公众帐号
	String corpcode = (String)request.getParameter("corpcode");
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<title><emp:message key="wxgl_gzhgl_title_140" defVal="获取" fileName="wxgl"/></title>
		<meta name="viewport" content="initial-scale=1.0, user-scalable=no" /> 
		<meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0"/>
    	<meta name="apple-mobile-web-app-status-bar-style" content="black"/>
    	<meta name="apple-mobile-web-app-capable" content="yes"/> 
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<%@include file="/common/common.jsp" %>  
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
		<link href="<%=iPath %>/css/location.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<style type="text/css">
		body,html,#allmap {
			width: 100%;
			height: 95%;
			overflow: auto;
			margin: 0;
		}
		</style>
		<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
			<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
	</head>
	<body>
		<div style="height: 35px;padding-top: 10px;display: block;">
		<%-- 用户纬度   经度 --%>
		<input name="userlat" id="userlat" type="hidden" value="<%=userlat %>" />
		<input name="userlng" id="userlng" type="hidden" value="<%=userlng %>" />
		<%-- 所选择目标纬度   经度 --%>
		<input name="pioslat" id="pioslat" type="hidden" value="" />
		<input name="pioslng" id="pioslng" type="hidden" value="" />
		<input name="aid" id="aid" type="hidden" value="<%=aid %>" />
		<input name="corpcode" id="corpcode" type="hidden" value="<%=corpcode %>" />
		
		<input id="pathUrl" value="<%=path %>" type="hidden" />
		<emp:message key="wxgl_gzhgl_title_153" defVal="城市:" fileName="wxgl"/><input type="text" name="address" id="address" value="" class="input_bd" style="width:160px;" 
		size="128" maxlength="128"/>
		<input name="seacrchbtn" id="seacrchbtn" value="<emp:message key='wxgl_button_16' defVal='查询' fileName='wxgl'/>" type="button" onclick="searchPios();"/>
		</div>
		<%-- 搜索地图结束  --%>
		<div id="allmap" style="display:block;"></div>
		<%-- 搜索地图结束 --%>
		
		<%-- 乘车方案开始  --%>
		<div class="all" id="busplandiv" style="display:none;height: 100%;">
		  <div class="route01">
		    <div class="route01_titlr01">
		      <div class="bus"><a href="#1" class="route01_titlr01_a01"><emp:message key="wxgl_gzhgl_title_30" defVal="公交" fileName="wxgl"/></a></div>
		      <div class="car"><a href="#1" onclick="changeDiv('car');"><emp:message key="wxgl_gzhgl_title_31" defVal="驾车" fileName="wxgl"/></a></div>
		    </div>
		    <div class="route01_list01">
		      <ul id="busplandiv_ul"></ul>
		    </div>
		  </div>
		</div>
		<%-- 乘车方案结束  --%>
		<%-- 自驾方案开始 --%>
		<div class="all" id="carplandiv" style="display:none;height: 100%;">
		  <div class="route01">
		    <div class="route01_titlr01">
		      <div class="bus"><a href="#1" onclick="changeDiv('bus');"><emp:message key="wxgl_gzhgl_title_30" defVal="公交" fileName="wxgl"/></a></div>
		      <div class="car"><a href="#1" class="route01_titlr01_a01"><emp:message key="wxgl_gzhgl_title_31" defVal="驾车" fileName="wxgl"/></a></div>
		    </div>
		    <div class="route01_list03" id="zijiafangan">
		      <div class="route01_list03_title01">
		        <p id = "carinfo"></p>
		      </div>
		      <ul id="carplan">
		      </ul>
		    </div>
		    <div class="return-map01" id="carlinkmap"></div>
		  </div>
		</div>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/wxgl_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/myjquery-b.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	    <script type="text/javascript" src="<%=commonPath %>/wxcommon/widget/artDialog/artDialog.js?skin=default"></script>
	  	<script type="text/javascript" src="<%=commonPath %>/wxcommon/widget/artDialog/iframeTools.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/common.js?v=<%=WXStaticValue.OTT_VERSION %>"></script>
		<script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=<%=ak %>"></script>
		<script type="text/javascript" src="http://developer.baidu.com/map/jsdemo/demo/convertor.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=iPath %>/js/lbsSingleMap.js?v=<%=WXStaticValue.OTT_VERSION %>"></script>
	</body>
</html>


