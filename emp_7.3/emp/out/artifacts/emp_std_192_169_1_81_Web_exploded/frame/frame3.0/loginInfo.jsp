<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
String tablesb = (String)request.getAttribute("tableStr");
%>
<html>
	<head><%@ include file="/common/common.jsp"%>
		<title><emp:message key="common_frame2_login_87" defVal="登录信息页面" fileName="common"></emp:message></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<script type="text/javascript" src="<%=path%>/common/widget/jqueryui/myjquery-b.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<link href="<%=path%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=path%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=iPath%>/skin/default/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<%if(StaticValue.ZH_HK.equals(empLangName)){%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<%}%>
		<script>
		function reloadpage()
		{
			location.href=location.href;
		}
		function setxh()
		{
			$("table tr ").eq(0).prepend("<th>"+getJsLocaleMessage("common","common_serialNumber")+"</th>");
			var ind = 0;
			$("table tr ").each(
				function(){
					if(ind > 0)
					{
						$(this).prepend("<td>"+ind+"</td>");
					}
					ind = ind + 1;
					
				}
			);
			$('#content tr').hover(function() {
				$(this).css('background-color', '#c1ebff');
			}, function() {
				$(this).css('background-color', '#FFFFFF');
			});
		}
		</script>
	</head>
	<body onload="setxh()">
	&nbsp;&nbsp;<button onclick="reloadpage()" style="padding:5px;"><emp:message key="common_refresh" defVal="刷新" fileName="common"></emp:message></button>
	<%=tablesb %>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
	</body>
</html>
