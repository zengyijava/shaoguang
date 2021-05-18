<%@ page language="java" import="java.util.List" pageEncoding="UTF-8"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
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

			if(index==0)
			{
				var urls = docUrl.split("/"); 
				if(urls.length == 4)
				{
					urls[3] = "";
				}
				alert("请重新登录！");
				docUrl = urls[0] + "//" + urls[2] + "/" + urls[3];
				location.href=docUrl;
			}else
			{
				window.parent.showLogin(-1);
			}
		}
		</script>
	</head>
	<body onload='showwww()'>
	</body>
</html>