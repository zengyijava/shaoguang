<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.montnets.emp.entity.lbs.LfLbsPushset"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort()+ path + "/";
	String iPath = request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	LfLbsPushset pushsetObj= (LfLbsPushset)request.getAttribute("pushsetObj");
	String pushsetResult=(String)request.getAttribute("pushsetResult");
	
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>My JSP 'weix_lbsUpload.jsp' starting page</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
  </head>
    <script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/wxgl_<%=langName%>.js"></script>
	<script type="text/javascript" src="<%=commonPath %>/wxcommon/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript">
		$(document).ready(
				function(){
					//进来设置时多图文模式1还是页面交互模式2
					<%
						if(pushsetObj != null){
						    Integer type = pushsetObj.getPushtype();
						   %>
								parent.changeinfo('<%=type%>');
						   <% 
							   	if(type == 2){
							   	   %>
							   			parent.showPushSet('<%=pushsetResult%>');
							  	 <% 
							   	}
						}else{
						    %>
				   			parent.showPushSet('<%=pushsetResult%>');
				  	 	<% 
						}
					%>


					
				});
	</script>
  <body>
    This is my JSP page. <br>
  </body>
</html>
