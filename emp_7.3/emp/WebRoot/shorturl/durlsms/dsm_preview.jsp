<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ page import="com.montnets.emp.common.constant.StaticValue"%>

<%
	String path = request.getContextPath();
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.indexOf("/",1));
	
	String result=(String)request.getAttribute("result");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    
    <title>短信预览</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<script language="javascript"
			src="<%=commonPath %>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"
			type="text/javascript"></script>
	
	<script type="text/javascript">
		
		$(document).ready(
				function(){
					
					var result='<%=result%>';
					if(result!=""&&result!=null)
					{
						parent.preSend(result);
					}
					parent.checkError();
					setTimeout(function(){window.location.href='<%=commonPath%>/common/blank.jsp';},10);
					
				});
	</script>
  </head>
  
  <body>
   
  </body>
</html>
