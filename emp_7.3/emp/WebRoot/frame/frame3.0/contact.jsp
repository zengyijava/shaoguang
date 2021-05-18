<%@ page language="java" import="java.util.List" pageEncoding="UTF-8"%>
<%
String path = (String)request.getContextPath();
String basePath =  request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
%>
<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
	<meta charset="utf-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<title>案例</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/index1.css">
	<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/animate.css">
	<link rel="icon" href="<%=path%>/common/img/favicon.ico" mce_href="<%=path%>/common/img/favicon.ico" type="image/x-icon"/>
	<script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=ixP5eQhfhLkOkdsXoSP0hkkQIz7ahwBS"></script>
</head>
<body>
	<%-- c-auto-container --%>
	<div class="c-auto-container contact-container">
		<%-- c-header --%>
		<div class="c-header c-clear">
			<div class="c-logo c-fl">
				<a href="<%=iPath%>/index_new.jsp" title="富媒体消息">
					<img src="<%=iPath%>/img/images/logo.png">
					<span class="logo-text">富媒体消息</span>
				</a>
			</div>
			<ul class="nav-bar c-fr">
				<li class="bar-li c-fl"><a href="<%=iPath%>/index_new.jsp"><i class="border scaleNavBar"></i><span>首页</span></a></li>
				<li class="bar-li c-fl"><a href="<%=iPath%>/case.jsp"><i class="border scaleNavBar"></i><span>案例</span></a></li>
				<li class="bar-li c-fl active"><a href="<%=iPath%>/contact.jsp"><i class="border scaleNavBar"></i><span>联系</span></a></li>
				<li class="bar-li c-fl"><a onclick="toLogin1()" href="#"><i class="border scaleNavBar"></i><span>登录</span></a></li>
			</ul>
		</div>
		<%-- /c-header --%>
		<div class="contact-cont">
			<ul class="contact-items">
				<li>联系电话：0755-86017726</li>
				<li>邮箱：marketing@montnets.com</li>
				<li>地址：深圳市南山区科技园高新中四道30号龙泰利科技大厦2、4层</li>
			</ul>
			<div id="YsityMap">
				
			</div>
			<p class="copyright">版权所有：深圳市梦网科技发展有限公司 粤ICP备12012537号</p>
		</div>
	</div>
	<%-- /c-auto-container --%>
	<script type="text/javascript">
		function toLogin1(){
			window.location.href="emp_tz.hts?flag=true"; 
		}
	</script>
	<script type="text/javascript">
  	 var map = new BMap.Map("YsityMap"); 
      var point = new BMap.Point(113.941736,22.548673);  
      map.centerAndZoom(point, 19);  
      map.addControl(new BMap.MapTypeControl());
      var marker = new BMap.Marker(point); 
      map.addOverlay(marker);
      marker.setAnimation(BMAP_ANIMATION_BOUNCE);
      map.enableScrollWheelZoom(true);
  </script>
</body>
</html>
