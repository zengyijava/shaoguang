<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Map" %>
<%@ page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
String path = request.getContextPath();
String iPath = request.getRequestURI().substring(0, request.getRequestURI().lastIndexOf("/"));
String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<title><emp:message key="dxkf_ydkf_xzfsdx_text_title" defVal="选择发送对象" fileName="dxkf"/></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<%@include file="/common/common.jsp" %>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" type="text/css" href="<%=skin%>/frame.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/sendBatchSms.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link href="<%=iPath %>/css/birthdaySend.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin%>/birthdaySend.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/floating.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin%>/floating.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<%}%>
		<link rel="stylesheet" type="text/css" href="<%=iPath %>/css/bir_birthSendInfo.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/ydkf_KeHuShengRiZhuFu.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		

	</head>
	<body id="bir_birthSendInfo">
	<input type="hidden" id="pathUrl" value="<%=path %>">
		<div id="container" class="container">
			<%-- 内容开始 --%>
			<center>
			<div class="dxzs_div1">
			<form method="post" id="selectForm">
				<%--存储常用数据--%>
				<div style="display:none" id="hiddenValueDiv"></div>
				<input type="hidden" id="strUser" name="strUser" value=""/>
				<input type="hidden" id="iPath" value="<%=iPath%>"/>
				<input type="hidden" id="depId" name="depId" value=""/>
				<input type="hidden" id="depName" name="depName" value=""/>
				<input type="hidden" id="user" name="user" value=""/>
				<input type ="hidden" id="pageIndex1" name="pageIndex1" value="1"/>
				<input type="hidden" id="pageIndex2" name="pageIndex2" value="1"/>
				<input type="hidden" id="totalPage1" name="totalPage1" value=""/>
				<input type="hidden" id="totalPage2" name="totalPage2" value=""/>
				<input type="hidden" id="addType" name="addType" value="3"/>
				<%--机构Ids--%>
				<input type="hidden" id="cliDepIdsStrs" value="">
				<%--客户Ids--%>
				<input type="hidden" id="clientIds" value="">

				<table class="chooseBox">
					<tr>
						<td class="dxzs_td1" align="left">
						<label id="addrType"></label>
						</td>
						<td></td>
						<td class="dxzs_td2"><emp:message key="dxkf_ydkf_xzfsdx_text_sxzrs" defVal="所选总人数：" fileName="dxkf"/><label id="manCount">0</label><div style="display:none"><label id="memberCount" ></label></div></td>
					</tr>
				</table>
				<table class="chooseBox">	
					<tr>
						<td height="350px;">
							<table>
								<tr>
									<td class="dxzs_td3">
										<div id="etree"  class="dept div_bd">
											<iframe id="sonFrame"  frameborder="0"></iframe>
										</div>
										<div class="div_bd title_bg">
											<span>&nbsp;&nbsp;<emp:message key="dxkf_ydkf_xzfsdx_text_cylb" defVal="成员列表：" fileName="dxkf"/></span><span id="showUserName"><emp:message key="common_cAddressBook" defVal="当前通讯录：" fileName="common"/><label id="addrName"></label>&nbsp;<label id="totalRec1"></label></span>
										</div>
										<div class="dept div_bd" id="etree1">
											<select multiple name="left" onfocus="treeLoseFocus()" id="left" size="15" class="left_select_choose">
											</select>
										</div>
										<div class="pt10 clear">
										<input type="button" class="btnClass1" value="<emp:message key="dxkf_common_opt_prepage" defVal="上一页" fileName="dxkf"/>" id="lastPage1" onclick="goLastPage1()"/>
										<input type="button" class="btnClass1" value="<emp:message key="dxkf_common_opt_nextpage" defVal="下一页" fileName="dxkf"/>" id="nextPage1" onclick="goNextPage1()"/>
										<label id="showPage1"></label>
										<br/>
										</div>
									</td>
								</tr>
							</table>
						</td>
						<td class="dxzs_td4">
						<br>
						
							<%--
							<input class="btnClass1" type="button" id="toLefts" value="&gt;&gt;" style="width: 60px;" onclick="moveallLeft();">
							 --%>
							<input class="btnClass1" type="button" id="toLeft" value="<emp:message key="dxkf_common_opt_xuanze" defVal="选择" fileName="dxkf"/>"  onclick="router();">
							<input class="btnClass1" type="button" id="toRight" value="<emp:message key="dxkf_common_opt_shanchu" defVal="删除" fileName="dxkf"/>" onclick="moveRight1();">
							<%--
							<input class="btnClass1" type="button" id="toRights" value="&lt;&lt;" style="width: 60px;" onclick="moveallRight()">
					        --%>
						</td>
						<td>
							<div id="rightDiv" class="div_bd" >
								<select multiple name="right" id="right" size="20">
								</select>
								<ul id="getChooseMan"></ul>
							</div>
							<div class="pt10 clear">
							<input type="button" class="btnClass1" value="<emp:message key="dxkf_common_opt_prepage" defVal="上一页" fileName="dxkf"/>" id="lastPage2" onclick="goLastPage2()"/>
							<input type="button" class="btnClass1" value="<emp:message key="dxkf_common_opt_nextpage" defVal="下一页" fileName="dxkf"/>" id="nextPage2" onclick="goNextPage2()"/>
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
    
    	<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/jquery_Ul_Send.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=empLangName%>/dxkf_<%=empLangName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/dxkf/birthwishc/js/birInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	</body>
</html>