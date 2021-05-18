<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="com.montnets.emp.entity.employee.LfEmployee"%>
<%@ page import="com.montnets.emp.employee.vo.LfEmployeeTypeVo"%>
<%@page import="com.montnets.emp.common.constant.ViewParams"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
	String path=request.getContextPath();
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));

	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("position");
	boolean addcode = false;
	if(btnMap.get(menuCode+"-1")!=null)
	{
		addcode = true;
	}
	boolean delcode = false;
	if(btnMap.get(menuCode+"-2")!=null)
	{
		delcode = true;
	}
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<title><emp:message key="employee_dxzs_title_96" defVal="职位管理" fileName="employee"/></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<%@include file="/common/common.jsp" %>
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath %>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" >
		<link href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" >
		<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
			<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
		<link rel="stylesheet" type="text/css" href="<%=iPath %>/css/epl_position.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/dxzs_YGTongXunLuGuanLi.css?V=<%=StaticValue.getJspImpVersion() %>"/>
	</head>
	<body onload="getemployeeTypeTable('<%=path %>')" id="epl_position">
	<%
		if(skin.contains("frame4.0")){
	%>
		<input id='hasBeenBind' value='1' type='hidden'/>
	<%
		}
	%>
	<input type="hidden" id="pathUrl" value="<%=path %>" />
		<%-- header开始 --%>
		<%=ViewParams.getPosition(langName,menuCode) %>
			<%-- header结束 --%>
		<div id="container" class="container">
			<%-- 内容开始 --%>
			<div id="rContent" class="rContent">
			
				<form name="pageForm" action="" method="post">
					<div class="buttons">
						 <% if(btnMap.get(menuCode+"-1")!=null) {  %>
					    <a id="add" onclick="javascript:addType()"><emp:message key="employee_dxzs_title_97" defVal="新增" fileName="employee"/></a>
					    <%} %>
						<% if(btnMap.get(menuCode+"-2")!=null) {  %>
						<a id="delete" onclick="javascript:delType()"><emp:message key="employee_dxzs_title_90" defVal="删除" fileName="employee"/></a>
					   <%} %>
					</div>
					<input type="hidden" id="servletUrl" value="<%=path %>/epl_position.htm?method=getTable"/>
					<input type="hidden" id="delUrl" value="<%=path %>/epl_position.htm?method=delete"/>
					<input type="hidden" id="depId" value="" />
					<input type="hidden" id="saveId" value="" />
					<div id="condition">
						
					</div>
					<div id="getloginUser">
					</div>
					
					<div class="user_div1"></div>	
					
				<div id="addPres"   title="<emp:message key='employee_dxzs_title_94' defVal='新增职位' fileName='employee'/>">
	              		<center>
	              		<table >
	              		    <tr><td colspan="2" height="10px;"></td></tr>
							<tr>
								<td class="dxzs_td_1">
									<emp:message key="employee_dxzs_title_14" defVal="职位：" fileName="employee"/>
								</td>
								<td >
									<input id="addN" name="AttachmentName" class="input_bd" onkeyup="this.value=this.value.replace(/[']+/img,'')"  type="text"  maxlength="32"/> 
								</td>
							</tr>
							<tr><td colspan="2" height="25px;"></td></tr>
						 <tr>
						 	<td colspan="2" align="center"><input type="button" value="<emp:message key='employee_dxzs_button_1' defVal='确定' fileName='employee'/>" class="btnClass5 mr23"  onclick="addNodeClick();"/>
						 	<input type="button" value="<emp:message key='employee_dxzs_button_5' defVal='取消' fileName='employee'/>" class="btnClass6" onclick="addcloseDiv();"/>
						 	</td>
						 </tr>
				</table>
				</center>
				</div>
		      <div id="updPres"   title="<emp:message key='employee_dxzs_title_95' defVal='修改职位' fileName='employee'/>">
		      		<input type="hidden" value="" id="updateposition" name="updateposition"/>
	              	<center>
	              	<table >
	              	        <tr><td colspan="2" height="10px;"></td></tr>
							<tr>
								<td class="dxzs_td_1">
									<emp:message key="employee_dxzs_title_14" defVal="职位：" fileName="employee"/>
								</td>
								<td >
									<input id="updN" name="AttachmentName" class="input_bd"  onkeyup="this.value=this.value.replace(/[']+/img,'')"  type="text" maxlength="32" /> 
								</td>
							</tr>
							  <tr><td colspan="2" height="20px;"></td></tr>
						 <tr>
						 	<td colspan="2" align="center"><input type="button"   class="btnClass5 mr23" value="<emp:message key='employee_dxzs_button_6' defVal='保存' fileName='employee'/>" onclick="updNodeClick();"/>
						 	<input type="button" value="<emp:message key='employee_dxzs_button_5' defVal='取消' fileName='employee'/>" class="btnClass6" onclick="updcloseDiv();"/>
						 	</td>
						 </tr>
				</table>
				</center>
				</div>
	
	
					<div id="bookInfo">
					</div>
				</form>
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
	<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script language="javascript" type="text/javascript" src="<%=iPath%>/js/booktype.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script language="javascript" type="text/javascript" src="<%=iPath%>/js/addrbook.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script language="javascript" type="text/javascript" src="<%=commonPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath%>/frame/frame4.0/js/dialogui.js"></script>
		<script type="text/javascript">
		$(document).ready(function(){
			var isIE = false;
				var isFF = false;
				var isSa = false;
				if ((navigator.userAgent.indexOf("MSIE") > 0)
						&& (parseInt(navigator.appVersion) >= 4))
					isIE = true;
				if (navigator.userAgent.indexOf("Firefox") > 0)
					isFF = true;
				if (navigator.userAgent.indexOf("Safari") > 0)
					isSa = true;
				$('#addN').keypress(function(e) {
					var iKeyCode = window.event ? e.keyCode
							: e.which;
					if (iKeyCode == 60 || iKeyCode == 62) {
						if (isIE) {
							event.returnValue = false;
						} else {
							e.preventDefault();
						}
					}
				});
				$('#updN').keypress(function(e) {
					var iKeyCode = window.event ? e.keyCode
							: e.which;
					if (iKeyCode == 60 || iKeyCode == 62) {
						if (isIE) {
							event.returnValue = false;
						} else {
							e.preventDefault();
						}
					}
				});		
				
					getLoginInfo("#getloginUser");		
		});	
		 function getemployeeTypeTable(path)  //通讯录类型
		 {
		     var time=new Date();
		     var lgguid = $('#lgguid').val();
		 	$('#bookInfo').load(path+'/epl_position.htm?method=getTable',{time:time,lgguid:lgguid});
		 }
	
		</script>
	</body>
</html>
