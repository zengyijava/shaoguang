<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String inheritPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
//	inheritPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
    String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
%>
<html>
	<head><%@ include file="/common/common.jsp"%>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	    <link href="<%=inheritPath%>/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<%if(StaticValue.ZH_HK.equals(empLangName)){%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<%}%>
	</head>

	<body>
	<input type="hidden" id="pathUrl" value="<%=path %>" />
		<div id="container" class="container">
			<%-- header开始 --%>
			<%-- header结束 --%>
			<%-- 内容开始 --%>
			<div id="rContent" class="rContent" style="padding:5px;text-align:center;vertical-align: middle;">
				<br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/>
			           <h1><emp:message key="common_permission_1" defVal="您没有机构权限，不能操作该模块，如有疑问请联系管理员！" fileName="common"></emp:message></h1>
			</div>
			<%-- 内容结束 --%>
			<%-- foot开始 --%>
			<div class="bottom">
				<div id="bottom_right">
					<div id="bottom_left"></div>
					<div id="bottom_main"> 
					</div>
				</div>
			</div>
			<%-- foot结束 --%>
		</div>
		<div class="clear"></div>
    <script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>" ></script>
    <script type="text/javascript" src="<%=commonPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
	</body>
</html>