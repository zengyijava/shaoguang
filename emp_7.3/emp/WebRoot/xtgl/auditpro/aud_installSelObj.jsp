<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Map" %>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@page import="com.montnets.emp.common.constant.ViewParams"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
	String path = request.getContextPath();
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	String lguserid = request.getParameter("lguserid");
	//判断是 1 创建流程界面操作下来的还是  2 完成界面中上一步操作  3是从列表页面弹出
	String pathtype = (String)request.getParameter("pathtype");
	String lgcorpcode = (String)request.getParameter("lgcorpcode");
	String flowid = request.getParameter("flowid");
	
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<title><emp:message key="xtgl_spgl_shlcgl_xzbshdx" defVal="选择被审核对象" fileName="xtgl"/></title>
		<%@include file="/common/common.jsp" %>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=iPath%>/css/installsel.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
			<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
		
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/aud_installSelObj.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/aud_installSelObj.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		
	</head>
	<body id="aud_installSelObj">
		<div id="container" class="container">
			<%-- 内容开始 --%>
			<center>
			<div  class="container_div">
			<form method="post" id="selectForm">
				<input type="hidden" id="pathUrl" value="<%=path %>">
				<input type="hidden" id="lguserid" value="<%=lguserid %>">
				<input type="hidden" id="lgcorpcode" value="<%=lgcorpcode %>">
				<%--选择设置的机构ID --%>
				<input type="hidden" id="auditdepid" value="" name="auditdepid"/>
				<%--选择设置的机构名称 --%>
				<input type="hidden" id="auditdepname" value="" name="auditdepname"/>
				<%-- bindattr 绑定流程1   没有  2 isexistAud 1存在该流程    2不存在该流程--%>
				<input type="hidden" id="auditbindattr" value="" name="auditbindattr"/>
				<input type="hidden" id="auditisexist" value="" name="auditisexist"/>
				<%--流程ID --%>
				<input type="hidden" id="flowid" value="<%=flowid %>" name="flowid"/>			
				<table  class="selectForm_table">
					<tr class="selectForm_table_tr1">
						<td class="selectForm_table_tr1_td" align="left" colspan="2">
								<div class="div_bd selectForm_table_tr1_td_div" >
									<table border="0">
										<tr>
											<td class="searchname_td">
												<input id="searchname" name="searchname" type="text" maxlength="16" class="graytext searchname" value=""  
												onkeypress="if(event.keyCode==13) {searchbyname();event.returnValue=false;}"/>
											</td>
											<td class="searchIcon_td">
										   		 <a onclick="searchbyname()"><img id="searchIcon" src="<%=skin %>/images/query.png" border="0"/></a>
											</td>
										</tr>
									</table>
							    </div>
						</td>
					</tr>
					<tr>
						<td class="wszdbshdx_td" align="left" colspan="2">
							<emp:message key="xtgl_spgl_shlcgl_wszdbshdx" defVal="未设置的被审核对象" fileName="xtgl"/>
						</td>
						<td class="yszdx_tb" align="left">
							&nbsp;&nbsp;<emp:message key="xtgl_spgl_shlcgl_yszdx" defVal="已设置对象" fileName="xtgl"/><span id="usercount"></span>
						</td>
					</tr>
					<tr>
						<td class="last_tr_td">
							<table>
								<tr>
									<td>
										<div id="etree"  class="dept div_bd">
											<iframe id="sonFrame"  class="sonFrame" frameborder="0" src="<%=iPath %>/aud_installtree.jsp?lguserid=<%=lguserid %>&flowid=<%=flowid %>"></iframe>
										</div>
										<div class="shownameDiv div_bd showUserName_div" >
											<span id="showUserName" class="title_bg showUserName" ><emp:message key="xtgl_spgl_shlcgl_cylb_mh" defVal="成员列表：" fileName="xtgl"/>
											</span>
										</div>
										<div class="dept div_bd left_div" >
											<select  multiple name="left" id="left" size="16" onfocus="treeLoseFocus()" class="left"
												ondblclick="">
											</select>
										</div>
									</td>
								</tr>
							</table>
						</td>
						<td  align="center"  class="toLeft_td">
							<input class="btnClass1" type="button" id="toLeft" value="<emp:message key="xtgl_spgl_shlcgl_xz" defVal="选择" fileName="xtgl"/>"  onclick="javascript:router();">
							<br/>
							<br/>
							<input class="btnClass1" type="button" id="toRight" value="<emp:message key="xtgl_spgl_shlcgl_sc" defVal="删除" fileName="xtgl"/>" onclick="javascript:moveOut();">
							<br/>
							<br/>
							<br/>
							<br/>
							<br/>
							<br/>
							<br/>
							<br/>
						</td>
						<td  class="right_td">
							<div class="showchoiceDiv div_bd">
								<select multiple name="right" id="right" class="right">
								</select>
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
    <script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/xtgl_<%=langName%>.js"></script>
    <script language="javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>" type="text/javascript"></script>
	<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=iPath%>/js/auditpro.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript">
			$(document).ready(function(){
				<%
					if(pathtype != null && "2".equals(pathtype)){
						%>
							var str = $(window.parent.document).find("#optionstr").children("xmp").text();
							$("#right").empty();
							$("#right").html(str);
						<%
					}else if(pathtype != null && "3".equals(pathtype)){
						%>
							$.post("<%=path%>/aud_auditpro.htm",{
								method:"getAuditObj",
								flowid:<%=flowid%>
				   			},function(returnmsg){
					   			if(returnmsg != ""){
					   				$("#right").empty();
					   				$("#right").html(returnmsg);
						   		}
					   		});
				   		<%
					}
				%>
			});
		</script>
	</body>
</html>