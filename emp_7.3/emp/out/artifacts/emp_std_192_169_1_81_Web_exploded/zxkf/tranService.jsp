<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://"
		+ request.getServerName() + ":" + request.getServerPort()
		+ path + "/";
String iPath = request.getRequestURI().substring(0,
		request.getRequestURI().lastIndexOf("/"));
String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
String userJson = (String)request.getAttribute("userJson");
String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
%>
<!doctype html>
<html lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<%@include file="/common/common.jsp" %>
	<title><emp:message key="zxkf_chat_title_64" defVal="转接客服页面" fileName="zxkf"/></title>
	<link rel="stylesheet" href="<%=iPath %>/static/css/base.css?V=<%=StaticValue.getJspImpVersion() %>">
	<link rel="stylesheet" href="<%=iPath %>/static/css/tranService.css?V=<%=StaticValue.getJspImpVersion() %>">
	<link rel="stylesheet" href="<%=iPath %>/static/css/nanoscroller2.css?V=<%=StaticValue.getJspImpVersion() %>">
	<%if(StaticValue.ZH_HK.equals(langName)){%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
	<%}%>
</head>
<body>
	<div id="tranService">
		<div id="selectService" class="nano">
			<div class="content">
				<ul>
					
				</ul>
			</div>
		</div>
		<div class="dest">
			<p><emp:message key="zxkf_chat_title_66" defVal="转接客服说明(最大长度500字符)" fileName="zxkf"/>：</p>
			<textarea name=""  maxlength="500" id="tranService_dest"></textarea>
		</div>
	</div>

	<script src="<%=iPath %>/static/js/myjquery-k.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=path %>/common/widget/artDialog/artDialog.js?skin=default"></script>
	<script type="text/javascript" src="<%=path %>/common/widget/artDialog/iframeTools.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script src="<%=iPath %>/static/js/jquery.nanoscroller.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script>
		var userJson = <%=userJson%>;
		var iPath="<%=iPath%>";
		art.dialog.data('service','');
		art.dialog.data('dest','');
		$(function(){
			var oSelUl = $('.content ul');
		    for(var i=0;i<userJson.length;i++)
			{
				var user = userJson[i];
				var userHtml = '<li><dl><dt><input type="radio" name="service" serviceName="'+user.name+'" value="'+user.customeId+'"></dt>'+
					'<dd><img src="'+iPath+'/static/images/kf.png" alt=""></dd>'+
					'<dd>'+user.name+'</dd></dl></li>';
				oSelUl.append(userHtml);
			}
			$('#selectService').delegate('li',{
				mouseenter:function(){
						$(this).addClass('hover');
				},
				mouseleave:function(){
					
						$(this).removeClass('hover');
				},
				click:function(){
					var oRadio=$(this).find('input[type=radio]');
					oRadio.trigger('click');
				}
			});	
			$('#selectService').delegate('input[type="radio"]','click',function(e){
				e.stopPropagation();
				art.dialog.data('service',$(this).val());
				art.dialog.data('serviceName',$(this).attr('serviceName'));
			})
			$('#tranService_dest').keyup(function(){
				art.dialog.data('dest',$(this).val());
			})

			$(".nano").nanoScroller({
				sliderMaxHeight:60
			});
			//字符限制处理问题 
			$("textarea[maxlength]").on("blur",function() {
				checkAreaLen($(this));
			});
			$("textarea[maxlength]").on("keydown",function() {
				checkAreaLen($(this));
			});
			$("textarea[maxlength]").on("keyup",function() {
				checkAreaLen($(this));
			});
		});
		function checkAreaLen(obj){
			var area = obj;//$(this);
			var max = parseInt(area.attr("maxlength"), 10); // 获取maxlength的值
			if (max > 0) {
				if (area.val().length > max) { // textarea的文本长度大于maxlength
					area.val(area.val().substr(0, max)); // 截断textarea的文本重新赋值
				}
			}
		}
	</script>
</body>
</html>