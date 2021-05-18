<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@page import="java.util.LinkedHashMap"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.montnets.emp.entity.client.LfCustField"%>
<%@page import="com.montnets.emp.common.constant.ViewParams"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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

//按钮权限Map
@ SuppressWarnings("unchecked")
Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");
@ SuppressWarnings("unchecked")
Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
String menuCode = titleMap.get("custFieldManger");

String phone  = (String)request.getAttribute("phone");
String name = (String)request.getAttribute("name");
String udgId = (String)request.getAttribute("udgId");
@ SuppressWarnings("unchecked")
List<LfCustField> rsList = (ArrayList<LfCustField>)request.getAttribute("rsList");
String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<title><%=titleMap.get(menuCode) %></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<%@include file="/common/common.jsp" %>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" type="text/css" href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link href="<%=iPath %>/css/addrBook.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/addrBook.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
	    <link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>">
		<link rel="stylesheet" type="text/css" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>">
		<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<%}%>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/ydkf_TongXunLuGuanLi.css?V=<%=StaticValue.getJspImpVersion() %>"/>
	</head>
	<body id="cli_custFieldManage">
		<%
			if(skin.contains("frame4.0")){
		%>
			<input id='hasBeenBind' value='1' type='hidden'/>
		<%
			}
		%>
		<div id="container" class="container">
			<%=ViewParams.getPosition(empLangName, menuCode) %>
			<div id="rContent" class="rContent" >
				<div class="left_dep div_bd" align="left" >
					<h3 class="div_bd title_bg">
						<emp:message key="client_khtxlgl_khssgl_text_clientattributes" defVal="客户属性 " fileName="client"></emp:message>
					</h3>
					<div id="depOperate" class="depOperate">
						<% if(btnMap.get(menuCode+"-3")!=null) {  %>
							<span id="delDepNew" class="depOperateButton3" onclick="delGroup()"></span>
						<%} %>
						<% if(btnMap.get(menuCode+"-2")!=null) {  %>
						<span id="updateDepNew" class="depOperateButton2" onclick="editGpName()"></span>
						<%} %>
						<% if(btnMap.get(menuCode+"-1")!=null) {  %>
							<span id="addDepNew" class="depOperateButton1" onclick="addCField()"></span>
						<%} %>
					</div>
					<div class="list" id="siderList" >
					<%
					if(rsList!=null && rsList.size()>0){
					  LfCustField field;
					  for(int i=0;i<rsList.size();i++)
					  {
						  field = rsList.get(i);
						  
					%>
					 <p style="width:190px">
					 <input type="hidden" value="<%=field.getId() %>" id="r" name="hidId"/><%=field.getField_Name().replace("<","&lt;").replace(">","&gt;")
 					%>
					 </p>
					<% 
					  }
					}
					%>
 					</div>
 					</div>
 					<input type="hidden" id="field_ref" name="field_ref" value="${field_ref}"/>
 					<input type="hidden" id="udgIdtemp" name="udgIdtemp"/>
 					<input type="hidden" id="udgNametemp" name="udgNametemp"/>
 					<input type="hidden" id="udgName_old" name="udgName_old"/>
				<div class="right_info">
				<form name="pageForm" action="<%=path %>/cli_custFieldManger.htm?method=find" method="post">
				<div style="display:none" id="hiddenValueDiv"></div>
					<div class="buttons" style="height:30px">
 						<input type="hidden" id="servletUrl" value="cli_custFieldManger.htm"/>
						<% if(btnMap.get(menuCode+"-4")!=null) {  %>
						<a id="add" onclick="addFieldValue()"><emp:message key="client_khtxlgl_khssgl_opt_addattributes" defVal="添加属性值 " fileName="client"></emp:message></a>
						<%} %>
 						<% if(btnMap.get(menuCode+"-5")!=null) {  %>
						<a id="delcli" onclick="delAll()"><emp:message key="client_khtxlgl_khssgl_opt_delattributes" defVal="删除属性值 " fileName="client"></emp:message></a>
						<%} %>
					</div>
					<div id="perSearchTemp" onclick="getSearch()"></div>
				
 	                <input type="hidden" id="depCode" name="depCode"/><%-- 员工和客户通讯录  机构编码 --%>
	                <input type="hidden" id="depId" name="depId"/><%-- 自定义通讯录  机构ID --%>
					<input type="hidden" id="servletUrl" value="<%=path %>/cli_custFieldManger.htm?method=getTable"/>
					<div style="clear:right"></div>	
					<div id="bookInfo">

					</div>
				</form>
				</div>
			</div>
			</div>
			<%-- 内容结束 --%>
			<%-- foot开始 --%>
			<div class="bottom">
				<div id="bottom_right">
					<div id="bottom_left"></div>
				</div>
			</div>
			<%-- foot结束 --%>
     <div class="clear"></div>
     
		<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
 		<script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
 		<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
 		<script type="text/javascript" src="<%=iPath%>/js/addrbook.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
 		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=empLangName%>/common_<%=empLangName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=empLangName%>/client_<%=empLangName%>.js"></script>
 		<script type="text/javascript" src="<%=iPath %>/js/custField.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	</body>
</html>
