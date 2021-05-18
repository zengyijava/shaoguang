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
	</head>
	<body>
		<%-- c-auto-container --%>
		<div class="c-auto-container list-container">
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
					<li class="bar-li c-fl active"><a href="<%=iPath%>/case.jsp"><i class="border scaleNavBar"></i><span>案例</span></a></li>
					<li class="bar-li c-fl"><a href="<%=iPath%>/contact.jsp"><i class="border scaleNavBar"></i><span>联系</span></a></li>
					<li class="bar-li c-fl"><a onclick="toLogin2()" href="#"><i class="border scaleNavBar"></i><span>登录</span></a></li>
				</ul>
			</div>
			<%-- /c-header --%>
			<%-- list-sec1 --%>
			<div class="list-sec1">
				<div class="list-info-cont">
					<div class="list-cont-bg animated bounceIn"></div>
					<div class="info-cont">
						<h2 class="list-title animated slideInDown delay11">
							<span class="hint-text">富媒体</span>
							<i>—</i>
							<span>成为企业“连接”客户的新利器</span>
						</h2>
						<ul id="list-table" class="list-table c-clear">
							<li class="active animated fadeInRightDown delay1">全部场景</li>
							<li class="animated fadeInRightDown delay13">企业品牌</li>
							<li class="animated fadeInRightDown delay2">交通出行</li>
							<li class="animated fadeInRightDown delay15">银行金融</li>
							<li class="animated fadeInRightDown delay3">旅游酒店</li>
							<li class="animated fadeInRightDown delay17">餐饮休闲</li>
							<li class="animated fadeInRightDown delay4">电影娱乐</li>
							<li class="mar-right0 animated fadeInRightDown delay19">新闻报刊</li>
						</ul>
						<div id="list-cont" class="list-content">
							<ul class="list-items current animated bounceInRight delay5">
							</ul>
							<ul class="list-items">
								<li class="items-li">
									<div class="img-cont">
										<img src="<%=iPath%>/img/images/pingpai_1.png" >
									</div>
									<p class="items-desc">互联网</p>
								</li>
								<li class="items-li">
									<div class="img-cont">
										<img src="<%=iPath%>/img/images/pingpai_2.png" >
									</div>
									<p class="items-desc">汽车</p>
								</li>
								<li class="items-li active">
									<div class="img-cont">
										<img src="<%=iPath%>/img/images/pingpai_3.png" >
									</div>
									<p class="items-desc">商场</p>
								</li>
								<li class="items-li">
									<div class="img-cont">
										<img src="<%=iPath%>/img/images/pingpai_4.png" >
									</div>
									<p class="items-desc">房地产</p>
								</li>
								<li class="items-li mar-right0">
									<div class="img-cont">
										<img src="<%=iPath%>/img/images/pingpai_5.png" >
									</div>
									<p class="items-desc">手机</p>
								</li>
							</ul>
							<ul class="list-items">
								<li class="items-li">
									<div class="img-cont">
										<img src="<%=iPath%>/img/images/traffic_1.png" >
									</div>
									<p class="items-desc">电子机票</p>
								</li>
								<li class="items-li active">
									<div class="img-cont">
										<img src="<%=iPath%>/img/images/traffic_2.png" >
									</div>
									<p class="items-desc">共享单车</p>
								</li>
								<li class="items-li mar-right0">
									<div class="img-cont">
										<img src="<%=iPath%>/img/images/traffic_3.png" >
									</div>
									<p class="items-desc">违章通知</p>
								</li>
							</ul>
							<ul class="list-items">
								<li class="items-li">
									<div class="img-cont">
										<img src="<%=iPath%>/img/images/Bank_1.png" >
									</div>
									<p class="items-desc">国际金融</p>
								</li>
								<li class="items-li active">
									<div class="img-cont">
										<img src="<%=iPath%>/img/images/Bank_2.png" >
									</div>
									<p class="items-desc">消息通知</p>
								</li>
								<li class="items-li mar-right0">
									<div class="img-cont">
										<img src="<%=iPath%>/img/images/Bank_3.png" >
									</div>
									<p class="items-desc">信用卡</p>
								</li>
							</ul>
							<ul class="list-items">
								<li class="items-li">
									<div class="img-cont">
										<img src="<%=iPath%>/img/images/Tourism_1.png" >
									</div>
									<p class="items-desc">旅游景点</p>
								</li>
								<li class="items-li active">
									<div class="img-cont">
										<img src="<%=iPath%>/img/images/Tourism_2.png" >
									</div>
									<p class="items-desc">旅游路线</p>
								</li>
								<li class="items-li mar-right0">
									<div class="img-cont">
										<img src="<%=iPath%>/img/images/Tourism_3.png" >
									</div>
									<p class="items-desc">酒店</p>
								</li>
							</ul>
							<ul class="list-items">
								<li class="items-li">
									<div class="img-cont">
										<img src="<%=iPath%>/img/images/Restaurant_1.png" >
									</div>
									<p class="items-desc">餐饮</p>
								</li>
								<li class="items-li active">
									<div class="img-cont">
										<img src="<%=iPath%>/img/images/Restaurant_2.png" >
									</div>
									<p class="items-desc">美食</p>
								</li>
								<li class="items-li mar-right0">
									<div class="img-cont">
										<img src="<%=iPath%>/img/images/Restaurant_3.png" >
									</div>
									<p class="items-desc">菜谱</p>
								</li>
							</ul>
							<ul class="list-items">
								<li class="items-li">
									<div class="img-cont">
										<img src="<%=iPath%>/img/images/Film_1.png" >
									</div>
									<p class="items-desc">电影海报</p>
								</li>
								<li class="items-li active">
									<div class="img-cont">
										<img src="<%=iPath%>/img/images/Film_2.png" >
									</div>
									<p class="items-desc">电影预报</p>
								</li>
								<li class="items-li mar-right0">
									<div class="img-cont">
										<img src="<%=iPath%>/img/images/Film_3.png" >
									</div>
									<p class="items-desc">获奖</p>
								</li>
							</ul>
							<ul class="list-items">
								<li class="items-li">
									<div class="img-cont">
										<img src="<%=iPath%>/img/images/Journalism_1.png" >
									</div>
									<p class="items-desc">科技新闻</p>
								</li>
								<li class="items-li active">
									<div class="img-cont">
										<img src="<%=iPath%>/img/images/Journalism_2.png" >
									</div>
									<p class="items-desc">时尚杂志</p>
								</li>
								<li class="items-li mar-right0">
									<div class="img-cont">
										<img src="<%=iPath%>/img/images/Journalism_3.png" >
									</div>
									<p class="items-desc">军事新闻</p>
								</li>
							</ul>
						</div>
					</div>
				</div>
				<div class="list-check-ft c-clear">
					<div class="list-check-icon list-check-prev c-fl J-list-check disable animated scaleNavBar" data-type="prev"><img src="./img/images/left_arrow_pre .png"></div>
					<div class="list-check-icon list-check-next c-fr J-list-check animated scaleNavBar" data-type="next"><img src="./img/images/right_arrow_nor.png"></div>
				</div>
			</div>
			<%-- /list-sec1 --%>
		</div>
		<%-- /c-auto-container --%>
		<script src="<%=iPath%>/js/myjquery-r.js"></script>
		<script src="<%=iPath%>/js/list.js"></script>
				<script type="text/javascript">
			function toLogin2(){
				window.location.href="emp_tz.hts?flag=true"; 
					}
		</script>
	</body>
</html>
