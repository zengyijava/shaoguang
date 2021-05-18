<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String inheritPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); inheritPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
    String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));

	String iPath = request.getRequestURI().substring(0,
			request.getRequestURI().lastIndexOf("/"));
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	    <link href="<%=inheritPath%>/styles/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet"
              type="text/css" />
			
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/cha_no_dep_level_permission.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/cha_no_dep_level_permission.css?V=<%=StaticValue.getJspImpVersion() %>"/>
			
	</head>

	<body id="cha_no_dep_level_permission">
	<input type="hidden" id="pathUrl" value="<%=path %>" />
		<div id="container" class="container">
			<%-- header开始 --%>
			<div class="top">
				<div id="top_right">
					<div id="top_left"></div>
					<div id="top_main">
					</div>
				</div>
			</div>
			<%-- header结束 --%>
			<%-- 内容开始 --%>
			<div id="rContent" class="rContent rContent2">
				<br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/>
			           <h1>您所属机构的下属机构不在计费范围内，不能操作该模块，如有疑问请联系管理员！</h1> 
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
    <script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>" ></script>
    <script type="text/javascript" src="<%=commonPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	</body>
</html>