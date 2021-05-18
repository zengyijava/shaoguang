<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	String skin = session.getAttribute("stlyeSkin")==null?"default":
		(String)session.getAttribute("stlyeSkin");
	String lgcorpcode = (String)request.getAttribute("lgcorpcode");
%>
<html>
	<head><%@ include file="/common/common.jsp"%>
		<title><emp:message key="common_frame2_uploadLogo_5" defVal="LOGO上传" fileName="common"></emp:message></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath %>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath %>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=commonPath %>/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
		<%if(StaticValue.ZH_HK.equals(empLangName)){%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<%}%>
        <script type="text/javascript">
		  var lgcorpcode = '<%=lgcorpcode%>';
		</script>
	</head>

	<body>
	<input type="hidden" id="pathUrl" value="<%=path %>" />
		<div id="container" class="container">
		<center>
		<div id="upImg" style="margin-top: 200px;">
			<emp:message key="common_frame2_uploadLogo_1" defVal="选择图片：" fileName="common"></emp:message><input id="chooseImg" name="chooseImg" type="file"/>
				<input type="button" id="uploadImg" value="<emp:message key="common_frame2_uploadLogo_2" defVal="立即上传" fileName="common"></emp:message>" onclick="javascript:doUp()" class="btnClass2"/>
				<p><br/><label style="color: #2970c0"><emp:message key="common_frame2_uploadLogo_3" defVal="上传图片要求【格式：png  、 长：302px 、  宽：63px】" fileName="common"></emp:message></label></p>
				<br/><a href="javascript:location.href='<%=path %>/down.htm?filepath=/common/frame/frame3.0/img/logo.png'" style="color: #2970c0"><emp:message key="common_frame2_uploadLogo_4" defVal="[LOGO样本下载]" fileName="common"></emp:message></a>
			</div>
			</center>
		</div>
			<%-- 内容结束 --%>
			<%-- foot开始 --%>
			<div class="bottom">
				<div id="bottom_right">
					<div id="bottom_left"></div>
					<div id="bottom_main"> 
					</div>
				</div>
			</div>
			<%-- foot结束 --%>
		<div class="clear"></div>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath %>/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
        <script type="text/javascript" src="<%=commonPath %>/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/js/ajaxfileupload.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=iPath %>/js/uploadLogo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	</body>
</html>