<%@ page language="java" import="java.util.List" pageEncoding="UTF-8"%>
<%
	String browerVer = request.getParameter("browerVer");
%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	</head>
	<body >
	<audio src="<%=request.getContextPath()+"/"+request.getParameter("voiceFile") %>" controls="controls" autoplay="true"></audio>
	</body>
</html>