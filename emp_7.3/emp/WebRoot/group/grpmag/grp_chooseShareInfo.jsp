<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Map" %>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%@include file="/common/common.jsp" %>
<%
	String path = request.getContextPath();
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	//登录id
	String lguserid = request.getParameter("lguserid");
	String skin = session.getAttribute("stlyeSkin")==null?"default":
		(String)session.getAttribute("stlyeSkin");
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<title><emp:message key="group_ydbg_ygtxlgl_text_groupshare" defVal="群组共享" fileName="group"></emp:message></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath %>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=iPath %>/css/group.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/group.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath %>/common/css/table_detail.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet"
              type="text/css" />
		
		<link rel="stylesheet" type="text/css" href="<%=iPath %>/css/grp_chooseShareInfo.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<%}%>
	</head>
	<body onload="loadFn()" id="grp_chooseShareInfo">
	<input type="hidden" id="pathUrl" value="<%=path %>">
		<div id="container" class="container">
			<%-- 内容开始 --%>
			<form method="post" id="selectForm">
				<div class="hidden" id="loginUserInfo"></div>
				<input type="hidden" id="strUser" name="strUser" value=""/>
				<input type="hidden" id="isAdd" name="isAdd" value="1"/><%-- 1 新增 0 修改 --%>
				<input type="hidden" id="ipath" value="<%=request.getContextPath()%>"/>	
				<select class="group_display_none" id='tempOptions'></select>
				<select class="group_display_none" id='rightSelectTemp'></select>
				<select class="group_display_none" id='rightSelectTempAll'></select>
				<input type="hidden" id="depId" name="depId" value=""/>
				<input type="hidden" id="depName" name="depName" value=""/>
				<input type="hidden" id="user" name="user" value=""/>
				<input type="hidden"  value="<%=skin %>" id="skin">
				
				<table class="group_table1" cellspacing="0" cellpadding="0">
					<tr>
					<td  colspan="3" align="left" class="div_bg group_td1">
					<label><emp:message key="group_ydbg_ygtxlgl_text_mygroup" defVal="我的群组" fileName="group"></emp:message>：</label>
					<label id="groupName"></label>
					</td>
					</tr>
					<tr class="group_tr1"><td></td></tr>
					<tr>
						<td class="group_td2" colspan="3">
						<select name="chooseType" id="chooseType" class="input_bd" onchange="changeshow();">
							<option value="0" selected="selected"><emp:message key="group_ydbg_ygtxlgl_text_name" defVal="姓名" fileName="group"></emp:message></option>
							<option value="1"><emp:message key="group_ydbg_ygtxlgl_text_phonenum" defVal="手机号码" fileName="group"></emp:message></option>
						</select>
<%--						<input style="float:left;" type=text" name="epname" id="epname" class="input_bd" value="" maxlength="20" onkeyup = "checkText($(this))"/>--%>
<%--					    <input id="search" class="btnClass1" name="search" width="30px" type="button" value="搜索" onclick="zhijieSearch()" />--%>
					<input  id="epname" name="epname" onkeyup = "checkText($(this))" class="input_bd" type="text" maxlength="16" value='<emp:message key="group_ydbg_ygtxlgl_text_entername" defVal="请输入姓名" fileName="group"></emp:message>'
				    onkeypress="if(event.keyCode==13) {zhijieSearch();event.returnValue=false;}"/>
						<a  id="btnSearch" onclick="javascript:zhijieSearch()"></a>
					</td>	
					</tr>
					<%--<tr>
								<td align="left"><span style="padding-top: 5px; display: block; text-align: left;font-size: 12px;">操作员机构：</span></td>
								<td  width="5%" align="center"></td>
								<td width="35%" align="left"><span style="padding-top: 5px; display: block; text-align: left;font-size: 12px;">共享操作员：</span></td>
								<td></td>
							</tr>
					--%><tr>
						<td>
							<table>
								<tr>
									<td class="group_td3">
										<div id="etree"  class="dept div_bd">
											<iframe id="sonFrame" name="sonFrame" frameborder="0" src="<%=iPath %>/grp_sysuserDepTree.jsp?lguserid=<%=lguserid %>"></iframe>
										</div>
										<div id="egroup"  class="dept hidden"></div>
										<div class="div_bd group_div1">
											<span id="showUserName" class="div_bg">&nbsp;&nbsp;<emp:message key="group_ydbg_ygtxlgl_text_memberlist" defVal="成员列表" fileName="group"></emp:message>：</span>
										</div>
										<div class="dept div_bd group_div2">
											<select  multiple name="left" id="left" size="15" <%-- onfocus="depBlur()"--%>
												ondblclick="">
											</select>
										</div>
									</td>
								</tr>
							</table>
						</td>
						<td align="center" class="group_td4">
							<br>
							<br>
							<input class="btnClass2" type="button" id="selectDep" value="<emp:message key="group_common_text_choose" defVal="选择" fileName="group"></emp:message>" onclick="javascript:router();">
							<%-- 
							<input class="btnClass2" type="button" id="toLeft" value="选择" style="" onclick="javascript:moveLeft();">
							--%>
							<br>
							<br>
							<input class="btnClass2" type="button" id="toRight" value="<emp:message key="group_common_opt_delete" defVal="删除" fileName="group"></emp:message>" onclick="javascript:moveRight();">
							<br>
							<br>
						</td>
						<td style="border:0px">
							<div class="dept div_bd group_div3">
								<select multiple name="right" id="right">
								</select>
							</div>
						</td>
					</tr>
				</table>
			</form>
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
    <script language="javascript" src="<%=commonPath %>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>" type="text/javascript"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>" ></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=empLangName%>/group_<%=empLangName%>.js"></script>
		<script type="text/javascript" src="<%=iPath %>/js/sysChooseInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script>
			
			$(":text").each(function(){
				$(this).attr("holder",$(this).val()).css("color","gray");
			}).focus(function(){
				var holder = $(this).attr("holder");
				var value = $.trim($(this).val());
				if(value == holder)
					$(this).val("");
			}).blur(function(){
				var holder = $(this).attr("holder");
				var value = $.trim($(this).val());
				if(value == "")
					$(this).val(holder);
			});
		</script>
	</body>
</html>