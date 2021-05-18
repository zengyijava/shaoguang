<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.List"%>
<%@page import="com.montnets.emp.entity.ydyw.LfDeductionsList"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
	String path = request.getContextPath();
	String iPath = request.getRequestURI().substring(0, request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0, iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0, inheritPath.lastIndexOf("/"));

	@SuppressWarnings("unchecked")
	Map<String, String> btnMap = (Map<String, String>) session.getAttribute("btnMap");//按钮权限Map

	@SuppressWarnings("unchecked")
	Map<String, String> titleMap = (Map<String, String>) session.getAttribute("titleMap");

	String menuCode = titleMap.get("sysDepReport");

	menuCode = menuCode == null ? "0-0-0" : menuCode;
	String skin = session.getAttribute("stlyeSkin") == null ? "default" : (String) session.getAttribute("stlyeSkin");
	//退费ID
	String id = request.getParameter("id");
	@SuppressWarnings("unchecked")
	//退费LIST
	LfDeductionsList lfDeductionsList = (LfDeductionsList) request.getAttribute("lfDeductionsList");
	//退费类型
	String type = request.getParameter("type");
	
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head><%@include file="/common/common.jsp"%>
		<title><%=titleMap.get(menuCode)%></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin%>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin%>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/widget/zTreeStyle/zTreeStyle.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>">
		<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
		<%if(StaticValue.ZH_HK.equals(empLangName)){%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<style type="text/css">
			body > table > tbody > tr > td:nth-child(1){
				width: 120px;
			}
		</style>
		<%}%>
		<style type="text/css">
			.bftable tbody tr
			{
				height: 30px;
			}
			.bftable tbody tr td:first-child
			{
				padding-left: 6px;
			}
		</style>
	</head>
	<body>
		<table cellpadding="0" cellspacing="0" border="0" width="100%" class="bftable" >
			<tbody style="font-size: 13px;">
		<%
		if("1".equals(type)){
			if(lfDeductionsList!=null)
			{
		%>
				<tr>
					<td><emp:message key="ydyw_ywgl_ywmbpz_text_57" defVal="姓名：" fileName="ydyw"></emp:message></td>
					<td ><%=lfDeductionsList.getCustomname()==null?"":lfDeductionsList.getCustomname() %> </td>
				</tr>
				<tr>
					<td><emp:message key="ydyw_ywgl_ywbgl_text_39" defVal="手机号码：" fileName="ydyw"></emp:message></td>
					<td><%=lfDeductionsList.getMobile()==null?"":lfDeductionsList.getMobile() %></td>
				</tr>
				<tr>
					<td><emp:message key="ydyw_ywgl_ywbgl_text_41" defVal="扣费账号：" fileName="ydyw"></emp:message></td>
					<td><%=lfDeductionsList.getDebitaccount()==null?"":lfDeductionsList.getDebitaccount() %></td>
				</tr>
				<tr>
					<td><emp:message key="ydyw_ywgl_ywbgl_text_49" defVal="签约套餐：" fileName="ydyw"></emp:message></td>
					<td><%=lfDeductionsList.getTaocanname()==null?"":lfDeductionsList.getTaocanname() %>(<%=lfDeductionsList.getTaocancode()==null?"":lfDeductionsList.getTaocancode() %>)</td>
				</tr>
				<tr>
					<td><emp:message key="ydyw_qytjbb_jgjftj_text_6_p" defVal="资费：" fileName="ydyw"></emp:message></td>
					<td><%=lfDeductionsList.getTaocanmoney()==null?"":lfDeductionsList.getTaocanmoney() %><emp:message key="ydyw_qyjfcx_khjfcx_text_8" defVal="元/月" fileName="ydyw"></emp:message></td>
					<td>
						<input type="hidden" name="taocanmoney" id="taocanmoney"  value="<%=lfDeductionsList.getTaocanmoney() %>">
					</td>
				</tr>
				<%
			}
		} 
		%>
				<tr>
					<td width="<%="zh_HK".equals(empLangName)?110:75%>px;" ><emp:message key="ydyw_qyjfcx_khtfgl_text_2_p" defVal="退费金额：" fileName="ydyw"></emp:message></td>
					<td  ><input  type="text" id="backmoeny" class="input_bd div_bd" maxlength="10" name="backmoeny" onkeyup="javascript:phoneInputCtrl($(this))"/>&nbsp;&nbsp;<emp:message key="ydyw_qyjfcx_khjfcx_text_9" defVal="元" fileName="ydyw"></emp:message></td>
				</tr>
				<tr >
				<td id="btn" colspan="2" style="text-align: center;padding:15px;">
					<input name="subBut" type="button" id="subBut" value="<emp:message key="common_confirm" defVal="确定" fileName="common"></emp:message>" onclick="save()" class="btnClass5 mr23"/>
					<input name="cancelwid" type="button" onclick="closeDiv()" value="<emp:message key="common_btn_16" defVal="取消" fileName="common"></emp:message>" class="btnClass6"/>
					<input type="hidden" value="<%=path%>" id="path" />	
					<input type="hidden" value="<%=type%>" id="type" />	
					<input type="hidden" id="lguserid" name="lguserid" value="<%=request.getParameter("lguserid") %>"/>
					<input type="hidden" id="lgcorpcode" name="lgcorpcode" value="<%=request.getParameter("lgcorpcode") %>"/>	
					<input type="hidden" name="refundId" id="refundId"  value="<%=id %>">	
				</td>
		  	</tr>
			</tbody>
		</table>
		
		<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=iPath%>/js/ydyw_crmRfMgr.js?V=<%=StaticValue.getJspImpVersion() %>" ></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=empLangName%>/ydyw_<%=empLangName%>.js"></script>
		<script>
		$(document).ready(function()
		{
			$("#backmoeny").focus();
		});
		</script>
	</body>
</html>
