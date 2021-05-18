<%@ page language="java" import="java.util.List" pageEncoding="UTF-8"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head><%@ include file="/common/common.jsp"%>
    <base href="<%=basePath%>">
    
    <title><emp:message key="common_errors_1" defVal="错误页面" fileName="common"></emp:message></title>
    <%if(StaticValue.ZH_HK.equals(empLangName)){%>
    <link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
    <%}%>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<style type="text/css">
	body{background:url(/emp/website/sglcorpFrame/images/indexbg.png) top center #e5fcff no-repeat;}
	</style>
  </head>
  
  <body>
         <div><center><font color="red"><emp:message key="common_errors_2" defVal="该功能不能使用，请联系管理员！" fileName="common"></emp:message></font> </center></div>
  <script type="text/javascript">
			var paddd=window.parent;
			var docUrl = window.location.href;
			if(paddd.location.href==docUrl)
			{
				var index=0;
				while(paddd.location.href!=docUrl && index<6)
				{
					docUrl=paddd.location.href;
					paddd=paddd.parent;
					index++;
				}
				docUrl = paddd.location.href;
				var urls = docUrl.split("/"); 
				docUrl = urls[0] + "//" + urls[2] + "/" + urls[3];
				paddd.location.href=docUrl;
			}

	</script>
     <script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
  </body>
</html>
