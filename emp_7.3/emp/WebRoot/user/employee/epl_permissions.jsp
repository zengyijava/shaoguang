<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page import="com.montnets.emp.entity.sysuser.LfSysuser" %>
<%@page import="com.montnets.emp.common.constant.ViewParams"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));

	@SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	@SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	//String bookType = (String)request.getAttribute("bookType");
	String bookType = "employee";
	String menuCode = titleMap.get("permissions");
	LfSysuser curSysuser=(LfSysuser)session.getAttribute("loginSysuser");
	Integer permissionType = curSysuser.getPermissionType();
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<title><%=titleMap.get(menuCode) %></title>
		<meta http-equiv="content-type" content="text/html; charset=utf-8">
		<%@include file="/common/common.jsp" %>
 		<link href="<%=commonPath%>/common/css/addrBook.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/addrBook.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath %>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" >
		<link href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" >
		<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
			<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
		<link rel="stylesheet" type="text/css" href="<%=iPath %>/css/epl_permissions.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/dxzs_YGTongXunLuGuanLi.css?V=<%=StaticValue.getJspImpVersion() %>"/>
	</head>
	<body onload="submitForm()" id="epl_permissions">
		<div id="container" class="container">
			<%-- header开始 --%>
			<%=ViewParams.getPosition(langName,menuCode) %>
			<%-- header结束 --%>
			<%-- 内容开始 --%>
			<div id="rContent" class="rContent">
					<input id="permissionType" name="permissionType" type="hidden" value="<%=permissionType %>"/>
					<input id="curDepId" type="hidden"  value="<%=request.getAttribute("connDepId")==null?"":request.getAttribute("connDepId") %>"/>
					<input id="depId" type="hidden"  value="<%=request.getAttribute("connDepId")==null?"":request.getAttribute("connDepId") %>"/>
					<input id="depName2" type="hidden" />
					<input type="hidden" id="bookType" value="<%=bookType %>" />
					<input type="hidden" id="servletUrl" 
						value="<%=request.getContextPath()%>/epl_permissions.htm?method=toEmployeePm"/>
					<input type="hidden" value="<%=inheritPath %>" id="inheritPath"/>
					<input type="hidden" value="<%=iPath %>" id="iPath"/>
					
							<div class="left_dep div_bd" align="left" >
								<input type="hidden" id="id" />
								<h3 class="div_bd title_bg">
									<emp:message key="employee_dxzs_title_64" defVal="员工机构" fileName="employee"/>
								</h3>
								<div id="depOperate" class="depOperate">
									<span id="bindCon" class="bindCon" onclick="doBind()"></span>
								</div>
								<div id="left_dep_list" class="list">
									<div id="DepTree">
										<iframe frameborder="0" id="ifra" src="<%=iPath %>/epl_permissionsTree.jsp?treemethod=getEmpSecondDepJson">
										</iframe>
									</div>
								</div>
							</div>
							<div class="right_info">
								<form name="pageForm" action="" method="post">
								<div class="user_display_none" id="hiddenValueDiv"></div>
									<div class="buttons">
										<div id="toggleDiv"></div>
										<% if(btnMap.get(menuCode+"-2")!=null) {  %>
											<a id="delete" onclick="doDelQuanXian()"><emp:message key="employee_dxzs_title_90" defVal="删除" fileName="employee"/></a>
										<% } %>
									</div>
									<div id="condition">
										<table>
											<tbody>
												<tr>
													<td class="user_td1">
														<emp:message key="employee_dxzs_title_86" defVal="当前机构：" fileName="employee"/>
													</td>
													<td class="user_td2"><label id="depName"></label></td>
													<td><emp:message key="employee_dxzs_title_88" defVal="操作员姓名：" fileName="employee"/></td>
													<td><input type="text" id="username" name="username" maxlength="32"></input></td>
													<td class="tdSer">
												<center><a id="search"></a></center>
											</td>
												</tr>
											</tbody>
										</table>
									</div>
									<div class="user_div1"></div>
									<div id="permissionsInfo">
									</div>
								</form>
							</div>
				<%--选择账户--%>
 					<div id="com_add_Dom2" title="<emp:message key='employee_dxzs_title_93' defVal='权限绑定' fileName='employee'/>" class="user_display_none">
						<iframe id="bindUserFrame" name="bindUserFrame" marginwidth="0"  frameborder="no"></iframe>
						<table class="user_table1">
								<input type="hidden"  value="<%=skin %>" id="skin">
								<tr><td class="user_td3">
								<input type="button" id="btnok" value="<emp:message key='employee_dxzs_button_1' defVal='确定' fileName='employee'/>" class="btnClass5 mr23" onclick="bindok()" />
								<input type="button"  value="<emp:message key='employee_dxzs_button_5' defVal='取消' fileName='employee'/>" class="btnClass6" onclick="closeBinddiv();" /><br/>
								</td>
								</tr>
						</table>
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
		</div>
		<div class="clear"></div>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/employee_<%=langName%>.js"></script>
		 <script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>" ></script>
        <script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/user/employee/js/permissions.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	</body>
</html>
