<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.montnets.emp.entity.monitor.LfMonHnetwarn"%>
<%@ page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
	String path = request.getContextPath();
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
	String iPath = request.getRequestURI().substring(0, request.getRequestURI().lastIndexOf("/"));
	String inheritPath = request.getRequestURI().substring(0, request.getRequestURI().lastIndexOf("/"));
	inheritPath = inheritPath.substring(0, inheritPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0, inheritPath.lastIndexOf("/"));
	String skin = session.getAttribute("stlyeSkin") == null ? "default" : (String) session.getAttribute("stlyeSkin");
	LfMonHnetwarn hnetwarn=null;
	if(request.getAttribute("hnetwarn")!=null){
		hnetwarn= (LfMonHnetwarn)request.getAttribute("hnetwarn");
	}else{
		hnetwarn=new LfMonHnetwarn();
	}
%>
<html>
	<head>
		<title></title>
		<%@include file="/common/common.jsp" %>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
	<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
	<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
	<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion()%>" />
	<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion()%>" type="text/css" >
	<style>
		table input
		{
			width: 200px;
			height: 24px;
		}
		.xinhao
		{
			color: red;
			margin-left: 3px;
			margin-right: 3px;
		}
		.til
		{
			margin-left: 3px;
		}
		.select
		{
			width: 200px;
		}
	</style>
	<%if(StaticValue.ZH_HK.equals(langName)){%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
	<%}%>
	</head>
	<body >
		<input type="hidden" id="pathUrl" value="<%=path %>" />
		<div id="setDiv">
			<table style="width:100%;height:100%;font-size: 12px;">
			<tr>
				<td width="28%" align="left" style="padding-left: 30px;"><emp:message key="ptjk_common_gjsjh_mh" defVal="告警手机号：" fileName="ptjk"/></td>
				<td style="width: 195px;">
					<textarea name="monphone" id="monphone" class="input_bd"  style="width:180px;">
						<%=(hnetwarn!=null&&hnetwarn.getMonphone()!=null)?hnetwarn.getMonphone().trim():"" %>
					</textarea><font class="xinhao">*</font>
				</td>
					<td style="padding-right: 10px;padding-left: 2px;"><font class="til"><span style="color: #cccccc;font-size: 14px;" ><emp:message key="ptjk_wlgk_web_9" defVal="可以设置至多十个手机号,号码间用逗号(,)分隔" fileName="ptjk"/></span></font>
				</td>
				</tr>
				<tr>
					<td width="28%" align="left" style="padding-left: 30px;"><emp:message key="ptjk_common_gjyx_mh" defVal="告警邮箱：" fileName="ptjk"/></td>
					<td>
						<input type="text" name="monemail" id="monemail" maxlength="50" class="input_bd" style="width:180px;" value="<%=(hnetwarn!=null&&hnetwarn.getMonemail()!=null)?hnetwarn.getMonemail().trim():"" %>"/>
					</td>
					<td style="padding-right: 10px;padding-left: 2px;"><font class="til">
					<span style="color: #cccccc;font-size: 14px;"><emp:message key="ptjk_common_znszygyx" defVal="只能设置一个邮箱" fileName="ptjk"/></font></td>
					</tr>
					<tr>
						<td colspan="3" style="padding-left: 30px;">
						<font class="til"><span
								style="color: #cccccc;font-size: 14px;"><emp:message key="ptjk_wlgk_web_10" defVal="注：告警手机号和邮箱通用于所有WEB主机网络监控" fileName="ptjk"/></span>
						</td>
					</tr>
					
					<tr>
					<td colspan="3" style="text-align:center" id="btn">
					<input class="btnClass5 mr23" type="button" value="<emp:message key='ptjk_common_qd' defVal='确定' fileName='ptjk'/>" onclick="javascript: setHostMonNet();"/>
					<input class="btnClass6" type="button" value="<emp:message key='ptjk_common_qx' defVal='取消' fileName='ptjk'/>" onclick="javascript:doCancelEdit('#setDiv');"/>
					<%-- 为了兼容ie8下取消按钮右下角旁边能点击到确定按钮，增加这个换行 --%>
					<br/>
					</td>
					</tr>
				</table>
			</div>			
    <div class="clear"></div>
    <script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/ptjk_<%=langName%>.js"></script>
    <script language="javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript"></script>
    <script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script language="javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/js/jquery.placeholder.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script type="text/javascript" src="<%=iPath%>/js/hostMonNet.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
    <script src="<%=iPath%>/js/text.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript" ></script>
	</body>
</html>
