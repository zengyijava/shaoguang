<%@ page language="java" contentType="text/html" import="java.util.*" pageEncoding="utf-8"%>
<%@ page import="com.montnets.emp.common.constant.StaticValue"%>
<%
	String path = request.getContextPath();
	String empLangName = (String) session.getAttribute(StaticValue.LANG_KEY);
%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<script type="text/javascript" src="<%=path%>/common/i18n/<%=empLangName%>/common_<%=empLangName%>.js"></script>
		<script type="text/javascript" src="<%=path%>/common/i18n/i18nUtil.js"></script>
		<script>
		function showwww()
		{
			var paddd=window.parent;
			var docUrl = window.location.href;
			var index=0;
			while(paddd.location.href!=docUrl && index<6)
			{
				docUrl=paddd.location.href;
				paddd=paddd.parent;
				index++;
			}
			if(paddd.document.getElementById("logoutalert")!=null)
			{
				paddd.document.getElementById("logoutalert").value = 1;
			}
			docUrl = paddd.location.href;
			var urls = docUrl.split("/");
			if(urls.length == 5)
			{
				if(urls[4]!="systemManage.htm?method=toSkip")
				{
					urls[3] = "";
				}
			}
			docUrl = urls[0] + "//" + urls[2] + "/" + urls[3];
			alert(getJsLocaleMessage("common","common_logoutEmp_1"));
			paddd.location.href=docUrl;
		}
		</script>
	</head>
	<body onload='showwww()'>
	</body>
</html>