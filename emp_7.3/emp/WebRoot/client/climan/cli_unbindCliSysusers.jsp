<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="com.montnets.emp.common.constant.ViewParams" %>
<%@ page import="com.montnets.emp.entity.sysuser.LfSysuser" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://"
		+ request.getServerName() + ":" + request.getServerPort()
		+ path + "/";
String iPath = request.getRequestURI().substring(0,
		request.getRequestURI().lastIndexOf("/"));
String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("permissions");
	menuCode = menuCode==null?"0-0-0":menuCode;
	PageInfo pageInfo = new PageInfo();
	pageInfo = (PageInfo) request.getAttribute("pageInfo");
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	
	String depid = (String)request.getAttribute("depid");
	String sysName = (String)request.getAttribute("sysName");
	String depName = (String)request.getAttribute("depName");
	String lgcorpcode = (String)request.getAttribute("lgcorpcode");
	String lguserid = (String)request.getAttribute("lguserid");
	
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
		
%>
<html>
	<head>
		<title></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<%@include file="/common/common.jsp" %>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" type="text/css" href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link href="<%=commonPath %>/common/widget/zTreeStyle/zTreeStyle.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>">
		<link rel="stylesheet" type="text/css" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>">
		<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<%}%>
		<link rel="stylesheet" type="text/css" href="<%=iPath %>/css/cli_unbindCliSysusers.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/ydkf_TongXunLuGuanLi.css?V=<%=StaticValue.getJspImpVersion() %>"/>
	</head>
	<body id="cli_unbindCliSysusers">
		<div id="container" class="container">
			<%-- header开始 --%>
			<%-- header结束 --%>
			<%-- 内容开始 --%>
			<%if(btnMap.get(menuCode+"-0")!=null) { %>
			<form name="pageForm" action="cli_permissions.htm?method=getSysuserList" method="post" id="pageForm">
			<div class="display_none" id="hiddenValueDiv"></div>
			<input id="lguserid" name="lguserid" type="hidden" value="<%=lguserid %>" />
			<input id="lgcorpcode" name="lgcorpcode" type="hidden" value="<%=lgcorpcode %>" />
			<input id="selDepId" name="selDepId" type="hidden" value="<%=request.getAttribute("selDepId") %>" />
			<input id="selDepName" name="selDepName" type="hidden" value="<%=request.getAttribute("selDepName") %>" />
			<input id="bookType" name="bookType" type="hidden" value="client" />
			<div id="rContent" class="rContent">
					<div class="buttons">
						<div id="toggleDiv">
						</div>
						
					</div>
					<div id="condition">
						<table>
							<tr>
								<td><emp:message key="client_khtxlgl_txlqxgl_text_curorg" defVal="当前机构" fileName="client"></emp:message>：</td>
								<td class="client_td1" colspan="4"><label id="selDepName"><%=request.getAttribute("selDepName") %></label></td>
							</tr>
							<tr>
								
								<td><span><emp:message key="client_common_text_org" defVal="机构" fileName="client"></emp:message>：</span></td>
								<td class="condi_f_l">
								<input readonly onclick="javascript:dropSysMenu()" class="treeInput"  type="text" name="depName" id="depName" value="<%=null==depName?"":depName %>"/>
								<input type="hidden" name="depid" id="depid" value="<%=depid==null?"":depid %>"/> 						
								<div id="dropMenu" >
									<iframe class="client_iframe" frameborder="0" src="about:blank"></iframe>
									<div class="client_div1">
													<input type="button" value="<emp:message key="client_common_opt_confire" defVal="确定" fileName="client"></emp:message>" class="btnClass1" onclick="javascript:sure();" />&nbsp;&nbsp;
													<input type="button" value="<emp:message key="client_common_opt_emptiy" defVal="清空" fileName="client"></emp:message>" class="btnClass1" onclick="clearTreeValue()" />
												</div>	
									<ul id="ztree" class="tree"></ul>
								</div>
								</td>
								<td><span><emp:message key="client_khtxlgl_txlqxgl_text_optname" defVal="操作员姓名" fileName="client"></emp:message>：</span></td>
								<td>
									<input type="text" name="sysName" id="sysName" value="<%=null==sysName?"":sysName %>" maxlength="11"/>
								</td>
								<td class="tdSer">
									<center><a id="search"></a></center>
								</td>
							</tr>
						</table>
					</div>
					<table id="content">
					<thead>
					<tr>
						<th>
							<input type="checkbox" name=checkall2 id="checkall2" onclick="checkAlls(this,'sysUserName')"/>						</th>
						<th>
							<emp:message key="employee_dxzs_title_77" defVal="登录账号" fileName="employee"/>
						</th>
						<th>
							<emp:message key="client_khtxlgl_txlqxgl_text_optname" defVal="操作员姓名" fileName="client"></emp:message>
						</th>
					</tr>
					</thead>
					<tbody>
					<%
						@ SuppressWarnings("unchecked")
									List<LfSysuser> sysuserList=(List<LfSysuser>)request.getAttribute("sysuserList");
									for(int g=0;g<sysuserList.size();g++)
									{
										LfSysuser lfSysuser=sysuserList.get(g);
					%>
					<tr >
						<td>
							<input type="checkbox" name="sysUserName" value="<%=lfSysuser.getUserId() %>"/>
						</td>
						<td><%=lfSysuser.getUserName()==null?"":lfSysuser.getUserName().replace("<","&lt;").replace(">","&gt;") %></td>
						<td><%=lfSysuser.getName()==null?"":lfSysuser.getName() %></td>
					</tr>
			 		<%
			 			} 
			  			if(sysuserList.size()==0)
			  			{
			  		%>
					<tr><td colspan="3"><emp:message key="client_common_text_norecord" defVal="无记录" fileName="client"></emp:message></td></tr>
					<%
						}
					%>
					</tbody>
						<tfoot>
							<tr>
								<td colspan="3">
									<div id="pageInfo"></div>
								</td>
							</tr>
						</tfoot>
					</table>
					
			</div>
			<%} %>
			<%-- 内容结束 --%>
			<%-- foot开始 --%>
			<div class="bottom">
				<div id="bottom_right">
					<div id="bottom_left"></div>
					<div id="bottom_main">
					</div>
				</div>
			</div>
			</form>
			<%-- foot结束 --%>
		</div>
		
		<div class="clear"></div>
		<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script src="<%=commonPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>" type="text/javascript"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=empLangName%>/common_<%=empLangName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=empLangName%>/client_<%=empLangName%>.js"></script>
		<script src="<%=iPath %>/js/unbindCli.js?V=<%=StaticValue.getJspImpVersion() %>" type="text/javascript"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/jquery.ztree-myjquery-b.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript">
		$(document).ready(function() {
			getLoginInfo("#hiddenValueDiv");
			$("#toggleDiv").toggle(function() {
				$("#condition").hide();
				$(this).addClass("collapse");
			}, function() {
				$("#condition").show();
				$(this).removeClass("collapse");
			});
			$("#content tbody tr").hover(function() {
				$(this).addClass("hoverColor");
				}, function() {
				$(this).removeClass("hoverColor");
			});
			
			showPageInfo2(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>,[10]);
			$('#search').click(function(){submitForm();});
			closeTreeFun(["dropMenu"]);
		});
		</script>
	</body>
</html>
