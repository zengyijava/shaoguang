<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Map" %>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@page import="com.montnets.emp.common.constant.ViewParams"%>

<%@ taglib prefix="emp"
	uri="http://www.montnets.com/emp/i18n/tags/simple"%>

<%
	String path = request.getContextPath();
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	Integer noticeCount = StaticValue.MAX_PERNOTIC_COUNT;
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	
	//Jsp页面中获取session中的语言设置
 	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<title>选择发送对象</title>
		<%@include file="/common/common.jsp" %>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=inheritPath%>/common/css/reading.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=inheritPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=inheritPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=inheritPath%>/common/css/floating.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=inheritPath%>/common/widget/zTreeStyle/zTreeStyle.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css"/>
		<link rel="stylesheet" href="<%=inheritPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>">
		<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
		<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=empBasePath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
			<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
		
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/opt_depPassUserInfo.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/user.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		
	</head>
	<body id="opt_depPassUserInfo">

		<div id="container" class="container">
			<%-- 内容开始 --%>
			<center>
			<div  class="selectForm">
			<form method="post" id="selectForm">
				<input type="hidden" id="ipath" value="<%=request.getContextPath()%>"/>	
				<input type="hidden" id="pathUrl" value="<%=path %>">
				<input type="hidden" id="iPathUrl" value="<%=iPath %>">
				<input type="hidden" value="" id="addUserId"/>
				<table>
					<tr>
						<td class="showdepName_td" align="left">
							<span id="showdepName"></span>
						</td>
					</tr>
					<tr>
						<td>
							<div id="etree"  class="div_bd">
								<iframe id="sonFrame"  class="sonFrame" frameborder="0" src="<%=inheritPath %>/operator/opt_depPassUserTree1.jsp"></iframe>
							</div>
						</td>
					</tr>
				</table>
			</form>
		</div>
		</center>
			<%-- 内容结束 --%>
			<%-- foot开始 --%>
			<div class="bottom">
				<div id="bottom_right">
					<div id="bottom_left"></div>
				</div>
			</div>
			<%-- foot结束 --%>
		</div>
    <div class="clear"></div>
	</body>
</html>