<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Map" %>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
String path = request.getContextPath();
String iPath = request.getRequestURI().substring(0, request.getRequestURI().lastIndexOf("/"));
String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
//语言方面相关
String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<title><emp:message key="dxzs_xtnrqf_title_40" defVal="选择发送对象" fileName="dxzs"/></title>
		<%@include file="/common/common.jsp" %>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" type="text/css" href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/sendBatchSms.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link href="<%=iPath %>/css/birthdaySend.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/birthdaySend.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/floating.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/floating.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<%}%>
		<style type="text/css">
			#birthSendInfoEMP center {
				font-size: 14px;
			}
			#birthSendInfoEMP select {
				font-size: 14px;
			}
			#birthSendInfoEMP .dept {
			    float: left;
			    text-align: left;
			    width: 240px;
			    margin-bottom:10px;
			}
			#birthSendInfoEMP #rightDiv{
				margin-bottom:10px;
			}
			#birthSendInfoEMP .dxzs_div1{
				height:470px;
			}
			#birthSendInfoEMP #tempOptions,#rightSelectTemp,#rightSelectTempAll{
				display:none;
			}
			#birthSendInfoEMP .dxzs_td1{
				font-size:12px;
				width:302px;
			}
			#birthSendInfoEMP .dxzs_td2{
				font-size:12px;
				color:#666666;
				text-align:left;
			}
			#birthSendInfoEMP .dxzs_td3{
				padding-top:5px;
			}
			#birthSendInfoEMP .dxzs_td3 td{
				border:1;
			}
			#birthSendInfoEMP #etree{
				width:242px;
				height:240px;
			}
			#birthSendInfoEMP #sonFrame{
				width:240px;
				height:240px;
			}
			#birthSendInfoEMP .div_bd.title_bg{
				border-bottom:0;
				margin-top:10px;
				height:22px;
				width:242px;
				float:left;
				overflow: hidden
			}
			#birthSendInfoEMP .div_bd.title_bg span{
				float:left;
				margin-top:3px;
				font-size:12px
			}
			#birthSendInfoEMP #showUserName{
				height:22px;
				width:240px;
				padding-top: 5px;
				padding-left:10px;
				display: none; 
				text-align: left;
				font-size: 12px;
			}
			#birthSendInfoEMP #totalRec1{
				color:blue;
				display:none
			}
			#birthSendInfoEMP #etree1{
				height: 120px; 
				width: 237px;
				overflow:hidden;
				display:block;
				padding-left:5px;
			}
			#birthSendInfoEMP #left{
				padding:4px 0 6px 8px;
			}
			#birthSendInfoEMP .dxzs_td4{
				width:5%;
				padding: 5px;
			}
			#birthSendInfoEMP #toRight{
				margin-top:10px;
			}
			#birthSendInfoEMP #right{
				width:204px; 
				display:none;
				height: 395px; 
				border:0;
				float:left;
				color: black;
				font-size: 12px;
				padding:4px;
				vertical-align:middle;
				margin:-6px -10px;
			}
		</style>
		
	</head>
	<body id="birthSendInfoEMP">
	<input type="hidden" id="pathUrl" value="<%=path %>">
		<div id="container" class="container">
			<%-- 内容开始 --%>
			<div class="dxzs_div1">
			<form method="post" id="selectForm">
				<%--存储常用数据--%>
				<div style="display:none" id="hiddenValueDiv"></div>
                <%--机构Ids--%>
                <input type="hidden" id="empDepIdsStrs" value="">
                <%--员工Ids--%>
                <input type="hidden" id="employeeIds" value="">
                <input type="hidden" id="depId" value="">
                <input type="hidden" id="depName" value="">
				<input type="hidden" id="strUser" name="strUser" value=""/>
				<input type="hidden" id="ipath" value="<%=iPath%>"/>
				<input type ="hidden" id="pageIndex1" name="pageIndex1" value="1"/>
				<input type="hidden" id="pageIndex2" name="pageIndex2" value="1"/>
				<input type="hidden" id="depIdTempStr" name="depIdTempStr" value=""/>
				<input type="hidden" id="totalPage1" name="totalPage1" value=""/>
				<input type="hidden" id="totalPage2" name="totalPage2" value=""/>
				<input type="hidden" id="addType" name="addType" value="3"/>		
				<table class="chooseBox">
					<tr>
						<td class="dxzs_td1" align="left">
						<label id="addrType"></label>
						</td>
						<td></td>
						<td class="dxzs_td2"><emp:message key="dxzs_xtnrqf_title_77" defVal="所选总人数" fileName="dxzs"/>：<label id="manCount">0</label><div style="display:none"><label id="memberCount" ></label></div></td>
					</tr>
				</table>
				<table class="chooseBox">	
					<tr >
						<td class="dxzs_td3">
							<table>
								<tr>
									<td>
										<div id="etree"  class="dept div_bd">
											<iframe id="sonFrame"  frameborder="0"></iframe>
										</div>
										<div class="div_bd title_bg">
											<span >&nbsp;&nbsp;<emp:message key="dxzs_xtnrqf_title_77" defVal="成员列表" fileName="dxzs"/>：</span><span id="showUserName"><emp:message key="common_cAddressBook" defVal="当前通讯录：" fileName="common"></emp:message><label id="addrName"></label>&nbsp;<label id="totalRec1"></label></span>
										</div>
										<div id="etree1" class="dept div_bd">
											<select multiple name="left" onfocus="treeLoseFocus()" id="left" size="15" class="left_select_choose">
											</select>
										</div>
										<div class="gotoPage">
										<input type="button" class="btnClass1" value="<emp:message key='dxzs_xtnrqf_title_80' defVal='上一页' fileName='dxzs'/>" id="lastPage1" onclick="goLastPage1()"/>
										<input type="button" class="btnClass1" value="<emp:message key='dxzs_xtnrqf_title_81' defVal='下一页' fileName='dxzs'/>" id="nextPage1" onclick="goNextPage1()"/>
										<label id="showPage1"></label>
										<br/>
										</div>
									</td>
								</tr>
							</table>
						</td>
						<td class="dxzs_td4" align="center"> 
						<br>
							<input class="btnClass1" type="button" id="toLeft" value="<emp:message key='dxzs_xtnrqf_button_7' defVal='选择' fileName='dxzs'/>"  onclick="router();">
							<input class="btnClass1" type="button" id="toRight" value="<emp:message key='dxzs_xtnrqf_button_11' defVal='删除' fileName='dxzs'/>" onclick="moveRight1();">
						</td>
						<td>
							<div id="rightDiv"  class="div_bd" >
								<select multiple name="right" id="right" size="20" >
								</select>
								<ul id="getChooseMan"></ul>
							</div>
							<div class="gotoPage">
							<input type="button" class="btnClass1" value="<emp:message key='dxzs_xtnrqf_title_80' defVal='上一页' fileName='dxzs'/>" id="lastPage2" onclick="goLastPage2()"/>
							<input type="button" class="btnClass1" value="<emp:message key='dxzs_xtnrqf_title_81' defVal='下一页' fileName='dxzs'/>" id="nextPage2" onclick="goNextPage2()"/>
							<label id="showPage2"></label>
							<br/>
							</div>
						</td>
						<td width="20%">
						</td>
					</tr>
				</table>
			</form>
		</div>
		<iframe id="ifr" class="ifr"></iframe>
			<div id="id2" class="remindMessage"></div>
		
			<%-- 内容结束 --%>
			<%-- foot开始 --%>
			<div class="bottom">
				<div id="bottom_right">
					<div id="bottom_left"></div>
				</div>
			</div>
			<%-- foot结束 --%>
		</div>
    <div class="clear"></div>
    	<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/dxzs_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
    	<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
    	<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/jquery_Ul_Send.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/dxzs/birthwish/js/birInfoE.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	</body>
</html>