<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="com.montnets.emp.monitor.constant.MonitorStaticValue"%>
<%@ page import="com.montnets.emp.common.constant.StaticValue"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
%>

<!doctype html>
<html>
  <head>
    
    <title>My JSP 'mon_index.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<link rel="stylesheet" href="<%=iPath%>/style/jk.css">
	<!--[if IE]>
   		<script type="text/javascript" src="<%=iPath%>/js/excanvas.js"></script>
	<![endif]-->
	<!--[if IE 6]>
        <script type="text/javascript" src="<%=iPath%>/js/DD_belatedPNG.js"></script>
        <script language="javascript" type="text/javascript">
        DD_belatedPNG.fix(".headBar,.mon-body i,.png");
        </script>
    <![endif]-->
	<script>
    var ipath="<%=iPath%>";
    </script>
  </head>
  
  <body>
  	<input type="hidden" id="pathUrl" name="pathUrl" value="<%=path %>"/>
    <div id="mainMon" style="">
    </div>
  	<div id="loginInfo"></div>
	<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script src="<%=iPath%>/js/mainMon.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script src="<%=iPath%>/js/jquery.jmp3.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script src="<%=iPath%>/js/fishcomponent.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script src="<%=iPath%>/js/Monitor.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script type="text/javascript">
		$(document).ready(function() {
			getLoginInfo("#loginInfo");
			submit();
			//定时刷新时间
			var refreshTime = <%=MonitorStaticValue.getRefreshTime()%>;
			var findresult = $("#findresult").val();
			//后台出现异常
			if("-1"==findresult)
			{
				//alert("");
			}
			else
			{
				//定时刷新
				//reTimer=window.setInterval("submit()",refreshTime);
			}
		});
		function submit()
		{
			var keyword = $("#keyword").val();
			var isSoundMon = $("#isSoundMon").val();
			var time = new Date().getTime();
			var lguserid = $("#lguserid").val();
			$('#mainMon').html('');
			  $('#mainMon').load("<%=path%>/mon_mainMon.htm",{
				  method:'getMonInfo',
				  keyword:keyword,
				  isSoundMon:isSoundMon,
				  lguserid:'2',
				  isAsync:"yes"
				},function(result){
					
					if(result == "outOfLogin")
					{
						$("#logoutalert").val(1);
						location.href=$("#pathUrl").val()+"/common/logoutEmp.html?time="+time;
						return;
					}
					Monitor.init();
				});
		}
		
</script>
	
  </body>
</html>
