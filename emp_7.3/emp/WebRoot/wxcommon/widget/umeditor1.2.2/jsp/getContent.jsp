<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<script src="../third-party/jquery.min.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script src="../third-party/mathquill/mathquill.min.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<link rel="stylesheet" href="../third-party/mathquill/mathquill.css?V=<%=StaticValue.getJspImpVersion() %>"/>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
<%
request.setCharacterEncoding("utf-8");
response.setCharacterEncoding("utf-8");
String content = request.getParameter("myEditor");



response.getWriter().print("<div class='content'>"+content+"</div>");

%>