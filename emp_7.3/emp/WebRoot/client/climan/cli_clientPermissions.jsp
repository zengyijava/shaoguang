<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page import="com.montnets.emp.entity.sysuser.LfSysuser " %>
<%@page import="com.montnets.emp.common.constant.ViewParams"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
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
	@SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	@SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = "1580-1300";
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
		
	LfSysuser curSysuser=(LfSysuser)session.getAttribute("loginSysuser");
	Integer permissionType = curSysuser.getPermissionType();
	
	
	String lguserid = String.valueOf(request.getAttribute("lguserid"));
	
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);	
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<title><%=titleMap.get(menuCode) %></title>
		<meta http-equiv="content-type" content="text/html; charset=utf-8">
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
			<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
		<link rel="stylesheet" type="text/css" href="<%=iPath %>/css/cli_clientPermissions.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/ydkf_TongXunLuGuanLi.css?V=<%=StaticValue.getJspImpVersion() %>"/>
	</head>
	<body onload="submitForm()" id="cli_clientPermissions">
		<div id="container" class="container">
			<%=ViewParams.getPosition(empLangName, menuCode) %>
			<%-- header开始 --%>
			<div id="getloginUser"></div>
			<%-- header结束 --%>
			<%-- 内容开始 --%>
			<div id="rContent" class="rContent">
					<input id="permissionType" name="permissionType" type="hidden" value="<%=permissionType %>"/>
					<input id="curDepId" type="hidden"  value="<%=request.getAttribute("connDepId")==null?"":request.getAttribute("connDepId") %>"/>
					<input id="depId" type="hidden"  value="<%=request.getAttribute("connDepId")==null?"":request.getAttribute("connDepId") %>"/>
					<input id="depName2" type="hidden" />
					<input type="hidden" id="bookType" value="client" />
					<input type="hidden" id="servletUrl" 
						value="<%=request.getContextPath()%>/cli_permissions.htm?method=toClientPm" />
					<input type="hidden" value="<%=inheritPath %>" id="inheritPath"/>
							<div class="left_dep div_bd" align="left" >
								<input type="hidden" id="id" />
								<h3 class="div_bd title_bg"><emp:message key="client_khtxlgl_txlqxgl_text_clientorg" defVal="客户机构" fileName="client"></emp:message></h3>
								<div id="depOperate" class="depOperate">
									<span id="bindCon" class="bindCon" onclick="doClientBind()"></span>
								</div>
								<div id="left_dep_list" class="list">
									<div id="DepTree">
										<iframe frameborder="0" id="ifra" src="<%=iPath %>/a_addrbookDepTree.jsp?treemethod=getClientSecondDepJson&getType=privi&lguserid=<%=lguserid%>" ></iframe>
									</div>
									 
								</div>
							</div>
							<div class="right_info">
								<form name="pageForm" action="" method="post">
								<div class="display_none" id="hiddenValueDiv"></div>
									<div class="buttons">
										<div id="toggleDiv"></div>
										<% if(btnMap.get(menuCode+"-2")!=null) {  %>
											<a id="delete" onclick="doDelCliDom()"><emp:message key="client_common_opt_delete" defVal="删除" fileName="client"></emp:message></a>
										<% } %>
										<%--
										<% if(btnMap.get(menuCode+"-1")!=null) {  %>
											<a id="bindCon" onclick="doBind()" ></a>
										<% } %>
										<a id="showAll" onclick="showAll()"></a>
										 --%>
									</div>
									<div id="condition">
										<table>
											<tbody>
												<tr>
													<td class="client_td1">
														<emp:message key="client_khtxlgl_txlqxgl_text_selectedorg" defVal="当前选中机构" fileName="client"></emp:message>：
													</td>
													<td width="200"><label id="depName"></label></td>
													<td><emp:message key="client_khtxlgl_txlqxgl_text_optname" defVal="操作员名称" fileName="client"></emp:message>：</td>
													<td><input type="text" id="username" name="username" maxlength="32"></input></td>
													<td class="tdSer">
												<center><a id="search"></a></center>
											</td>
												</tr>
											</tbody>
										</table>
									</div>
									
			 
									<div class="client_div1"></div>
									<div id="permissionsInfo">
									</div>
								</form>
							</div>
				<%--选择账户--%>
 					<div id="com_add_Dom2" title="<emp:message key="client_khtxlgl_txlqxgl_text_bindPermission" defVal="权限绑定" fileName="client"></emp:message>">
						
						<iframe id="bindUserFrame" name="bindUserFrame" marginwidth="0"  frameborder="no"></iframe>
						<table class="client_table1">
								<tr><td class="client_td2">
								<input type="button" id="btnok" value="<emp:message key="client_common_opt_confire" defVal="确定" fileName="client"></emp:message>" class="btnClass5 mr23" onclick="bindok()" />
								<input type="button"  value="<emp:message key="client_common_opt_cancel" defVal="取消" fileName="client"></emp:message>" class="btnClass6" onclick="closeBinddiv();" />
								<br/>
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
		
		<script type="text/javascript" src="<%=commonPath%>/frame/frame4.0/js/dialogui.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script src="<%=commonPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>" type="text/javascript"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=empLangName%>/common_<%=empLangName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=empLangName%>/client_<%=empLangName%>.js"></script>
		<script src="<%=iPath%>/js/addrbook.js?V=<%=StaticValue.getJspImpVersion() %>" type="text/javascript"></script>
		<script src="<%=commonPath%>/client/climan/js/permissions.js?V=<%=StaticValue.getJspImpVersion() %>" type="text/javascript"></script>
	</body>
</html>
