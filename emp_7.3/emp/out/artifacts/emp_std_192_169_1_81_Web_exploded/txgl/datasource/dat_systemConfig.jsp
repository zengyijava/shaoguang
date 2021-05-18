<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String iPath = request.getRequestURI().substring(0,
			request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");

%>


		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/dat_systemConfig.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/dat_systemConfig.css?V=<%=StaticValue.getJspImpVersion() %>"/>

<html>
	<body id="dat_systemConfig">
	<div class="left"></div>
	<div class="right">
			<iframe id="editWyGateFrame" name="editWyGateFrame" class="editWyGateFrame" marginwidth="0"  frameborder="no"  ></iframe>
	</div>
	<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript">
	setInterval(function(){
		$('.right').width($('body').width()-190);
	},50)
		
		$.ajax({
			url: 'systemManage.htm?method=toMenu&time=' + new Date().getTime(),
			type: 'GET',
			timeout: 60000,
			async: false,
			dataType: 'html',
			success: function(data){
				var _data = $(data);
				$('.left').html(_data);
			}
		});
		$('.contleft li').click(function(){
			var url = $(this).attr('url');
			$("#editWyGateFrame").attr("src","<%=path %>/"+url);
			$(this).addClass('on').siblings().removeClass('on');
		});

		$("#editWyGateFrame").attr("src","<%=path %>/systemManage.htm");
		
	</script>
	</body>
</html>
