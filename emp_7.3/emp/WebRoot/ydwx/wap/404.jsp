<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%@include file="/common/common.jsp" %>
<% String path = request.getContextPath();%>

<head>
	<meta http-equiv="Content-Type" content="text/html;charset=UTF-8" />
	<title></title>
	<style type="text/css">
	 *{margin:0;padding:0;}
	 .wrap_404{width:178px;padding:0 10px;}
	 .ico404{width:99px;margin:0 auto 10px;}
	 body{font-size:12px;}
	 body{background:#f3f3f3;}
	 p{line-height:24px;}
	 .submit{width:150px;margin:10px auto 0;}
	 .submit a{background:#10a501;width:140px;height:29px;line-height:29px;color:#fff;display:block;text-align:center;text-decoration:none;}
	</style>
</head>
<body>
	<div class="wrap_404">
	  <div class="ico404">
	    <img src="<%=path %>/ydwx/images/404_ico<%="zh_HK".equals(empLangName)?"_zh_HK":""%>.jpg" alt=""/>
	  </div>
	  <p><emp:message key='ydwx_wap_404Page_1' defVal='可能出现的问题' fileName='ydwx'></emp:message></p>
	  <p><emp:message key='ydwx_wap_404Page_2' defVal='1、页面已经移除了' fileName='ydwx'></emp:message></p>
	  <p><emp:message key='ydwx_wap_404Page_3' defVal='2、网络错误无法加载' fileName='ydwx'></emp:message></p>
	  <p><emp:message key='ydwx_wap_404Page_4' defVal='3、系统繁忙' fileName='ydwx'></emp:message></p>
	</div>
</body>
</html>