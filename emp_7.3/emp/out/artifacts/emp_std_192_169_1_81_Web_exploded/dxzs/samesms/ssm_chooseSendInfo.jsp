<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Map" %>
<%@ page import="com.montnets.emp.common.constant.StaticValue"%>
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
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	String lguserid = request.getParameter("lguserid");
	//语言方面相关
String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<title><emp:message key="dxzs_xtnrqf_title_40" defVal="选择发送对象" fileName="dxzs"/></title>
		<%@include file="/common/common.jsp" %>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" />
		<link rel="stylesheet" type="text/css" href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/sendBatchSms.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/sendBatchSms.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<%}%>
		<script>
		var base_ipath = "<%=path%>";
		</script>
		<style type="text/css">
			.chooseSendInfo center {
		font-size: 14px;
	}
	.chooseSendInfo select {
		font-size: 14px;
	}
	.chooseSendInfo .dxzs_div {
		height:465px;
	}
	.chooseSendInfo .dxzs_td {
		font-size:12px;height:24px;vertical-align:top;
	}
	.chooseSendInfo .div_bd.div {
		width:80px;height:22px;background-color:#ffffff;float:left;vertical-align: middle;
	}
	.chooseSendInfo .graytext {
		font-size:12px;border:0px;width:125px;height:22px;background-color:#F3FAEE；
	}
	.chooseSendInfo .dxzs_td1 {
		font-size:12px;color:#666666;text-align:left;
	}
	.chooseSendInfo .dept.div_bd.div1 {
		width:242px;height:240px;
	}
	.chooseSendInfo .dxzs_sonFrame {
		height:240px;width:240px;
	}
	.chooseSendInfo .dept.hidden.div_bd {
		background-color:#ffffff;height:240px;width:240px;
	}
	.chooseSendInfo .div_bd.div_bg {
		border-bottom:0;margin-top:10px;height:22px;width:242px;float:left;overflow: hidden;
	}
	.chooseSendInfo .dxzs_span {
		float:left;margin-top:3px;font-size:12px;
	}
	.chooseSendInfo .dept.div_bd.div2 {
		height: 125px; width: 237px;overflow:hidden;padding-left:5px;
	}
	.chooseSendInfo .dxzs_right {
		width:204px;height: 418px;display:none; border:0;float:left;color: black;font-size: 12px;padding:4px;vertical-align:middle;margin:-6px -10px;
	}
	.chooseSendInfo .dxzs_pageDiv {
		margin-top: 3px;text-align:left;
	}
	.chooseSendInfo .dxzs_chooseType {
		line-height:22px;width:80px;border:0;padding:0;margin:0;font-size:12px;
	}
	.chooseSendInfo .dxzs_showUserName {
		display:none;height:22px;width:240px;padding-top: 5px; padding-left:10px; text-align: left;font-size: 12px;
	}
		</style>
	</head>
	<body class="chooseSendInfo">
	<input type="hidden" id="pathUrl" value="<%=path %>">
		<div id="container" class="container">
			<%-- 内容开始 --%>
			<center class="centerBack">
			<div class="dxzs_div">
			<form method="post" id="selectForm">
				<input type="hidden" id="strUser" name="strUser" value=""/>
				<input type="hidden" id="ipath" value="<%=request.getContextPath()%>"/>	
				<select class="dxzs_display_none" id='tempOptions'></select>
				<select class="dxzs_display_none" id='rightSelectTemp'></select>
				<select class="dxzs_display_none" id='rightSelectTempAll'></select>
				<input type="hidden" id="depId" name="depId" value=""/>
				<input type="hidden" id="depName" name="depName" value=""/>
				<input type="hidden" id="user" name="user" value=""/>
				<input type="hidden" id="addType" name="addType" value="3"/>	
				<input type="hidden" id="pageIndex1" name="pageIndex1" value="1"/>
				<input type="hidden" id="totalPage1" name="totalPage1" value=""/>		
				
				<table border="0"  class="chooseBox">
					<tr>
						<td class="dxzs_td"  align="left" colspan="2">
						<div class="div_bd div">
						<select name="chooseType" id="chooseType" class="dxzs_chooseType" onchange="changeChooseType()">
							<option value="1" id="chooseType1"><emp:message key="dxzs_xtnrqf_title_75" defVal="员工通讯录" fileName="dxzs"/></option>
							<option value="2" id="chooseType2"><emp:message key="dxzs_xtnrqf_title_76" defVal="群组" fileName="dxzs"/></option>
						</select>
						</div>
						<div class="div_bd search_tx_choose">
						<table border="0">
						<tr>
						<td width="125" valign="top">
						<input id="epname" name="epname" type="text" maxlength="16" class="graytext" value=""  onkeypress="if(event.keyCode==13) {zhijieSearch();event.returnValue=false;}"/>
						</td>
						<td width="28px">
					    <a onclick="zhijieSearch()"><img id="searchIcon" src="<%=skin %>/images/query.png" border="0"/></a>
						</td>
						</tr>
						</table>
					    </div>
						</td>
						<td class="dxzs_td1"><emp:message key="dxzs_xtnrqf_title_77" defVal="所选总人数" fileName="dxzs"/>：<label id="manCount">0</label></td>
					</tr>
					</table>
					<table border="0"  class="chooseBox">
					<tr>
						<td>
							<table>
								<tr>
									<td>
										<div id="etree"  class="dept div_bd div1">
											<iframe id="sonFrame" name="sonFrame" class="dxzs_sonFrame" frameborder="0" src="<%=iPath %>/ssm_addrbookDepTree1.jsp?lguserid=<%=lguserid %>"></iframe>
										</div>
										<div id="egroup"  class="dept hidden div_bd"></div>
										<div class="div_bd div_bg">
											<span class="dxzs_span">&nbsp;&nbsp;<emp:message key="dxzs_xtnrqf_title_78" defVal="成员列表" fileName="dxzs"/>：</span><span id="showUserName" class="dxzs_showUserName"><emp:message key="dxzs_xtnrqf_title_77" defVal="当前通讯录" fileName="dxzs"/>：</span>
										</div>
										<div class="dept div_bd div2">
										  
											<select  multiple name="left" id="left" size="15" onfocus="treeLoseFocus()" class="left_select_choose">
											</select>
										 
										</div>
									</td>
								</tr>
							</table>
						</td>
						<td width="60" align="center">
						<br>
							<input class="btnClass1" type="button" id="toLeft" value="<emp:message key='dxzs_xtnrqf_button_7' defVal='选择' fileName='dxzs'/>"  onclick="javascript:router();">
							<br/>
							<br/>
							<input class="btnClass1" type="button" id="toRight" value="<emp:message key='dxzs_xtnrqf_button_11' defVal='删除' fileName='dxzs'/>" onclick="javascript:moveRight();">
						</td>
						<td>
							<div id="rightDiv"   class="dept div_bd" >
								<select multiple name="right" id="right" size="20" class="dxzs_right"
									>
								</select>
								
								<ul id="getChooseMan">
								</ul>
							</div>
						</td>
					</tr>
					<tr>
						<td>
							<div id="pageDiv" class="dxzs_pageDiv">
										<input type="button" class="btnClass1" value="<emp:message key='dxzs_xtnrqf_title_80' defVal='上一页' fileName='dxzs'/>" id="lastPage1" onclick="javascript:goLastPage1()"/>
										<input type="button" class="btnClass1" value="<emp:message key='dxzs_xtnrqf_title_81' defVal='下一页' fileName='dxzs'/>" id="nextPage1" onclick="javascript:goNextPage1()"/>
										<label id="showPage1"></label>
										<br/>
							</div>
						</td>
					</tr>
				</table>
			</form>
		</div>
		</center>
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
    	<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
   		<script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/chooseInfo_new.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/jquery_Ul_Send.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=iPath%>/js/ssm_chooseSendInfo.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	</body>
</html>