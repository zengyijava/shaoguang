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
		<title>首页</title>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/index1.css">
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/animate.css">
		<link rel="icon" href="<%=path%>/common/img/favicon.ico" mce_href="<%=path%>/common/img/favicon.ico" type="image/x-icon"/>
	</head>
	<body>
		<%-- c-auto-container --%>
		<div class="c-auto-container index-container">
			<%-- c-header --%>
			<div class="c-header c-clear">
				<div class="c-logo c-fl">
					<a href="<%=iPath%>/index_new.jsp" title="富媒体消息">
						<img src="<%=iPath%>/img/images/logo.png">
						<span class="logo-text">富媒体消息</span>
					</a>
				</div>
				<ul class="nav-bar c-fr">
					<li class="bar-li c-fl active"><a href="<%=iPath%>/index_new.jsp"><i class="border scaleNavBar"></i><span>首页</span></a></li>
					<li class="bar-li c-fl"><a href="<%=iPath%>/case.jsp"><i class="border scaleNavBar"></i><span>案例</span></a></li>
					<li class="bar-li c-fl"><a href="<%=iPath%>/contact.jsp"><i class="border scaleNavBar"></i><span>联系</span></a></li>
					<li class="bar-li c-fl"><a onclick="toLogin()" href="#"><i class="border scaleNavBar"></i><span>登录</span></a></li>
				</ul>
			</div>
			<%-- /c-header --%>
			<%-- index-sec1 --%>
			<div class="index-sec1">
				<div class="sec-content-bg animated bounceIn"></div>
				<div class="sec1-content">
					<div class="sec1-hd c-inline-parent">
						<h2 class="title c-inline-block animated slideInDown delay1">富媒体</h2>
						<h4 class="sub-title c-inline-block animated slideInRight delay2">为云通讯赋能</h4>
					</div>
					<p class="sec1-describe animated fadeInUp delay3">RMS Empowers The Cloud Communication</p>
					<div class="sec1-trait c-inline-parent">
						<div class="trait-line c-inline-block animated slideWidth delay6"></div>
						<ul class="trait-describe c-inline-block">
							<li class="describe-li li-first c-inline-block animated fadeInRightUp delay4">内容丰富</li>
							<li class="describe-li li-last c-inline-block animated fadeInRightUp delay5">及时高效</li>
						</ul>
						<div class="trait-line c-inline-block animated slideWidth delay6"></div>
					</div>
					<ul class="items c-inline-parent">
						<li class="c-inline-block animated fadeInRightUp delay7">超大容量</li>
						<li class="c-inline-block animated fadeInRightUp delay8">实名报备</li>
						<li class="c-inline-block animated fadeInRightUp delay9">覆盖广泛</li>
						<li class="c-inline-block animated fadeInRightUp delay10">安全可靠</li>
					</ul>
				</div>
			</div>
			<%-- /index-sec1 --%>
		</div>
		<%-- /c-auto-container --%>
		<script type="text/javascript">
		function toLogin(){
			window.location.href="emp_tz.hts?flag=true"; 
		}
		/*$(function(){
			var width=$(window).width();
			var height=$(window).height();
			$('body').css({"width":width,"height":height});
		});*/
	</script>
	</body>
</html>
