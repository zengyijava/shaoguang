<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String iPath = request.getRequestURI().substring(0,
			request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	
	String result=(String)request.getAttribute("result");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
  <head>
    <title>My JSP 'wyblank.jsp' starting page</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<%--
	<link rel="stylesheet" type="text/css" href="styles.css">
	--%>
	<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=116"></script>
	<script type="text/javascript">
		
		$(document).ready(
				function(){
					var result='<%=result%>';
					if(result!=""&&result!=null)
					{
						parent.upd(result);
					}
					setTimeout(function(){window.location.href='<%=commonPath%>/common/blank.jsp';},10);
					
				});
	</script>
  </head>
  
  <body>
    This is my JSP page. <br>
  </body>
</html>
