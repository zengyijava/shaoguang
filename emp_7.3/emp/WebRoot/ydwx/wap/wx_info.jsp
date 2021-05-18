<%@page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//WAPFORUM//DTD XHTML Mobile 1.0//EN" "http://www.wapforum.org/DTD/xhtml-mobile10.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
//	 style="margin:0 0px;background-image:url('/images/head.jpg');background-repeat:repeat-y;background-repeat:repeat-x;"
%>
<head>
<title> 网讯 - 梦网科技</title>
<meta name="viewport" content="width=device-width; initial-scale=1.0; minimum-scale=1.0; maximum-scale=1.0"/>
<meta name="MobileOptimized" content="240"/>
<meta name="apple-mobile-web-app-capable" content="yes" /> 
<meta name="apple-mobile-web-app-status-bar-style" content="black" /> 
<meta name="format-detection" content="telephone=no" />  
<style>
 A {
	COLOR: #3d64ef; text-decoration: none;font-size:12px;
}
A:hover {
	COLOR: red;
}
</style> 

</head>
  
	<body>
		<center>
		 <br/><br/>
		 <p>
			 
			 <font  color="red" style="color:red;text-align:left;"> 
				  ${msg }
			 </font>
		</p>
		 <br/><br/>
		 <a href="<%=path %>/wx.nms?w=${w}" style="TEXT-DECORATION:none;">返回网讯</a>
		 <br/><br/>
		</center>
	</body>
</html>
						 