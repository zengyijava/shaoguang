<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="com.montnets.emp.entity.employee.LfEmployee "%>
<%@ page import="com.montnets.emp.entity.sysuser.LfSysuser "%>
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
	String menuCode = titleMap.get("empDep");
	
	@ SuppressWarnings("unchecked")
	List<LfEmployeeTypeVo> optionList = (List<LfEmployeeTypeVo>)request.getAttribute("optionList");
	

	
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
	String username = request.getParameter("lgusername");
	
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
	
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
	<%@include file="/common/common.jsp" %>
		<title><emp:message key="employee_dxzs_title_63" defVal="员工通讯录" fileName="employee"/></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
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
		<style type="text/css">
			.dxkf_display_none{
				display:none;
			}
			#epl_empDep #etree{
				height:285px;WIDTH: 200px;OVERFLOW:auto;
			}
			#epl_empDep .buttons{
				margin-bottom:5px;
			}
			#epl_empDep .user_div1{
				clear:right
			}
			#epl_empDep #changeDep{
				padding:5px;display:none;
			}
			#epl_empDep .user_div2{
				height: 20px;
			}
			#epl_empDep #infoDiv,#infoEdit{
				display: none;overflow:hidden;
			}
			#epl_empDep .user_span1{
				background-color: #ccffcc;
			}
			#epl_empDep #flowFrameadd,#flowFrameedit{
				width:100%;border: 0;height:500px;
			}
		</style>
	</head>
	<body onload="submitForm()" id="epl_empDep">
	<input type="hidden" id="pathUrl" value="<%=path %>" />
	<input type="hidden" id="iPath" value="<%=iPath %>" />
			
			<div id="container" class="container">
			<%-- header开始 --%>
			<%=ViewParams.getPosition(langName,menuCode) %>
			<%-- header结束 --%>
			<div id="container" class="container">
			<%-- 内容开始 --%>
			<div id="rContent" class="rContent" >
				<div class="left_dep div_bd" align="left" >
					<input type="hidden" id="id" />
					<h3 class="div_bd title_bg">
		         	<emp:message key="employee_dxzs_title_64" defVal="员工机构" fileName="employee"/>
					</h3>
					<div id="depOperate" class="depOperate">
						<%if (delcode){%>
							<span id="delDepNew" class="depOperateButton3" onclick="doDel('','')"></span>
						<%} %>
						<span id="updateDepNew" class="depOperateButton2" onclick="updateDepFun()"></span>
						<%if (addcode){%>
							<span id="addDepNew" class="depOperateButton1" onclick="addDepFun()"></span>
						<%} %>
					</div>
					<div id="etree" class="list">
					</div>
				</div>
				
				<div class="right_info">
				<form name="pageForm" action="" method="post">
				<div id="getloginUser" class="dxkf_display_none"></div>
					<div class="buttons">
						
					   <% if(btnMap.get(menuCode+"-9")!=null) {  %>
					  		<a id="addeployee" href="javascript:addUpdep()"> <emp:message key="employee_dxzs_title_65" defVal="合并机构" fileName="employee"/></a>
					    <%} %>
					   <% if(btnMap.get(menuCode+"-8")!=null) {  %>
						<a id="toSysuer" onclick="javascript:adjustDep();"> <emp:message key="employee_dxzs_title_66" defVal="调整机构" fileName="employee"/> </a>
						<% } %>
					</div>
					<input type="hidden" id="servletUrl" value="<%=path %>/epl_empDep.htm?method=getTable"/>
					<input type="hidden" id="delUrl" value="<%=path %>/epl_empDep.htm?method=delete"/>
					<input type="hidden" id="depId" value="<%=request.getAttribute("connDepId")==null?"":request.getAttribute("connDepId") %>" />
					<input type="hidden" id="depName2" value="" />
					<div class="user_div1"></div>	
					<div id="bookInfo">
					</div>
				</form>
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
		</div>
    <div class="clear"></div>
    
    <div id="changeDep">
			 <input type="hidden" id="changeDepId" >
			<div id="depDiv">
				<ul id="dropdownMenu" class="tree"></ul>
			</div>
			<div class="user_div2"></div>
			<center>
				<div>
					<input type="button" value="<emp:message key='employee_dxzs_button_1' defVal='确定' fileName='employee'/>" onclick="doSubmit()" class="btnClass5 mr23"/>
					<input type="button" value="<emp:message key='employee_dxzs_button_5' defVal='取消' fileName='employee'/>" onclick="depTreeCancel()" class="btnClass6"/>
					<br/>
				</div>
			</center>
			  
	</div>
	<%-- 合并机构弹出框 --%>
				<div id="infoDiv" title="<emp:message key='employee_dxzs_title_65' defVal='合并机构' fileName='employee'/>">
						<span class="user_span1">&minus;&minus;<emp:message key="employee_dxzs_title_67" defVal="请选择要合并到的机构" fileName="employee"/>&minus;&minus;</span>
						<iframe id="flowFrameadd" name="flowFrame" src="" marginwidth="0" frameborder="no"></iframe>

				</div>
	<%-- 调整机构弹出框 --%>
				<div id="infoEdit" title="<emp:message key='employee_dxzs_title_66' defVal='调整机构' fileName='employee'/>">
						<span class="user_span1">&minus;&minus;<emp:message key="employee_dxzs_title_68" defVal="请选择要调整到的机构" fileName="employee"/>&minus;&minus;</span>
						<iframe id="flowFrameedit" name="flowFrame" src="" marginwidth="0" frameborder="no"></iframe>
				</div>
    	<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/employee_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>" ></script>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.ztree-myjquery-b.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=iPath%>/js/addremp.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=iPath%>/js/empDep.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript">
			$(document).ready(function() {
				$("#search").click(function(){submitForm();});
				$("#etree").load("<%=iPath %>/epl_empDepManageTree.jsp?treemethod=getEmpSecondDepJson&ac="+<%=addcode%>+"&dc="+<%=delcode%>);
				reloadTree();
				setLeftHeight();
			});
		</script>			
	</body>
	
</html>
