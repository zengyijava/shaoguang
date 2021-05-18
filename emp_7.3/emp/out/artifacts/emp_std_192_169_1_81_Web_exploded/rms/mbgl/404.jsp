<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String iPath = request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));

%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <body>
  		<center>
	    	<div class="notFoundFile" style="margin-top: 65px;">
	    		<img alt="资源文件不存在" src="rms/mbgl/image/404.png">
	    		<h4 style="font-size: 12px;color: #666666;margin-top: 15px;">资源文件不存在</h4>
	    	</div>
    	</center>
  </body>
</html>
