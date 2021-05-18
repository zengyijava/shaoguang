<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="com.montnets.emp.employee.vo.LfEmployeeTypeVo"%>
<%@page import="com.montnets.emp.common.constant.ViewParams"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="com.montnets.emp.employee.vo.LfEmployeeVo"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@page import="com.montnets.emp.i18n.util.MessageUtils"%>
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
	String menuCode = titleMap.get("employeeBook");
	
	PageInfo pageInfo = new PageInfo();
	pageInfo = (PageInfo) request.getAttribute("pageInfo");
	
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
	
	String skin = session.getAttribute("stlyeSkin")==null?"default":
		(String)session.getAttribute("stlyeSkin");
	boolean isDes = "1".equals(request.getParameter("isDes"));
	LfEmployeeVo bookInfo = null;
	String name = "";
	String moible = "";
	String sex = "";
	String dutyName = "";
	String userName = "";
	String startTime = "";
	String endTime = "";
	if(isDes){
		bookInfo = (LfEmployeeVo) session.getAttribute("employeeInfo");
		if(bookInfo!=null){
			name = StringUtils.defaultIfEmpty(bookInfo.getName(),"");
			moible = StringUtils.defaultIfEmpty(bookInfo.getMobile(),"");
			sex = StringUtils.defaultIfEmpty(bookInfo.getSex()+"","");
			dutyName = StringUtils.defaultIfEmpty(bookInfo.getDutyName(),"");
			userName = StringUtils.defaultIfEmpty(bookInfo.getUserName(),"");
			if(bookInfo.getBeginbir()!=null){
				startTime = bookInfo.getBeginbir();
				if(startTime.length()>10){
					startTime = startTime.substring(0,10);
				}
			}
			if(bookInfo.getEndbir()!=null){
				endTime = bookInfo.getEndbir();
				if(endTime.length()>10){
					endTime = endTime.substring(0,10);
				}
			}
		}
	}
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<%@include file="/common/common.jsp" %>
		<title><emp:message key="employee_dxzs_title_63" defVal="员工通讯录" fileName="employee"/></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/addrBook.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath %>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/addrBook.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=iPath %>/css/epl_employeeBook.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
		<link href="<%=skin %>/dxzs_YGTongXunLuGuanLi.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
        <%if(StaticValue.ZH_HK.equals(langName)){%>
            <link href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
            <link href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
        <%}%>


	</head>
	<body onload="submitForm()" id="epl_employeeBook">
	<input type="hidden" id="pathUrl" value="<%=path %>" />
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
						<div id="toggleDiv"></div>
					   <% if(btnMap.get(menuCode+"-1")!=null) {  %>
					  		<a id="addeployee" href="javascript:toAddEmployee('<%=path %>')"><emp:message key="employee_dxzs_title_1" defVal="添加员工" fileName="employee"/></a>
					    <%} %>
					    <%-- 
					    <% if(btnMap.get(menuCode+"-4")!=null) {  %>
						<a id="toSysuer" onclick="javascript:tochangeSysuser();">转操作员</a>
						<% } %>
						--%>
						<% if(btnMap.get(menuCode+"-a")!=null) {  %>
						<% } %>
						<% if(btnMap.get(menuCode+"-8")!=null) {  %>
						<% } %>
						<% if(btnMap.get(menuCode+"-2")!=null) {  %>
						<a id="changedep" onclick="showDepTree()"><emp:message key="employee_dxzs_title_70" defVal="转机构" fileName="employee"/></a>
					   <%} %>
						<%--<a id="showAll"onclick="showAll()">显示全部</a> --%>
						<%
							if(btnMap.get(menuCode+"-5")!=null)
							{
						%>
							<a id="exportCondition" onclick="importExcel()"><emp:message key="employee_dxzs_title_71" defVal="导出" fileName="employee"/></a>
						<%
							}
						%>																		
						<% if(btnMap.get(menuCode+"-2")!=null) {  %>
						<a id="delepl" onclick="delBk()"><emp:message key="employee_dxzs_title_72" defVal="删除员工" fileName="employee"/></a>
					   <%} %>
					</div>
					<input type="hidden" id="servletUrl" value="<%=path %>/epl_employeeBook.htm?method=getTable"/>
					<input type="hidden" id="delUrl" value="<%=path %>/epl_employeeBook.htm?method=delete"/>
					<input type="hidden" id="depId" value="<%=request.getAttribute("connDepId")==null?"":request.getAttribute("connDepId") %>" />
					<input type="hidden" id="depName2" value="" />
					<input type="hidden" id="isDes" value="<%=StringUtils.defaultIfEmpty(request.getParameter("isDes"),"") %>"/>
					<div id="condition">
						<table>
							<tbody>
								<tr>
									<td><emp:message key="employee_dxzs_title_7" defVal="姓名：" fileName="employee"/></td>
									<td><input id="name" type="text" maxlength="32" value="<%=name %>"/></td>
									<td><emp:message key="employee_dxzs_title_9" defVal="性别：" fileName="employee"/></td>
									<td>
										<select id="sexy" name="sexy">
												<option value=""><emp:message key="employee_dxzs_title_73" defVal="全部" fileName="employee"/></option>
												<option value="1" <%if("1".equals(sex)){out.print("selected");} %>><emp:message key="employee_dxzs_title_11" defVal="男" fileName="employee"/></option>
												<option value="0" <%if("0".equals(sex)){out.print("selected");} %>><emp:message key="employee_dxzs_title_12" defVal="女" fileName="employee"/></option>
												<option value="2" <%if("2".equals(sex)){out.print("selected");} %>><emp:message key="employee_dxzs_title_10" defVal="未知" fileName="employee"/></option>
										</select>
									</td>
									
									<td><emp:message key="employee_dxzs_title_14" defVal="职位：" fileName="employee"/></td>
									<td>
										<select id="option" name="option">
												<option value=""><emp:message key="employee_dxzs_title_73" defVal="全部" fileName="employee"/></option>
												<%
													if(optionList != null && optionList.size()>0){
														LfEmployeeTypeVo temp = null;
														for(int i=0;i<optionList.size();i++){
															temp = optionList.get(i);
												%>
															<option value="<%=temp.getName()%>" <%if(temp.getName().equals(dutyName)){out.print("selected");} %>><%=temp.getName().replaceAll("<","&lt;").replaceAll(">","&gt;")%></option>
												<%
															temp = null;
														}
													}
												%>
												<option value=">"><emp:message key="employee_dxzs_title_10" defVal="未知" fileName="employee"/></option>
										</select>
									</td>
									<td class="tdSer">
										<a id="search"></a>
									</td>
								</tr>
								<tr>
									<td><emp:message key="employee_dxzs_title_18" defVal="手机：" fileName="employee"/></td>
									<td><input id="phone" onkeyup="phoneInputCtrl($(this))" value="<%=moible %>" type="text"/></td>
									<td><emp:message key="employee_dxzs_title_8" defVal="生日：" fileName="employee"/></td>
									<td>
										<input type="text" value='<%=startTime %>' id="submitSartTime" name="submitSartTime" 
										class="Wdate" readonly="readonly" onclick="stime()">
									</td>
									<td><emp:message key="employee_dxzs_title_74" defVal="至：" fileName="employee"/></td>
									<td>
										<input type="text" value='<%=endTime %>' id="submitEndTime" name="submitEndTime" 
										class="Wdate" readonly="readonly" onclick="rtime()">
									</td>
									<td colspan="1">&nbsp;</td>
								</tr>
								<tr>
								<td><%=MessageUtils.extractMessage("employee","employee_dxzs_title_110",request)%></td>
										<td>
											<input type="text" name="userName" id="userName" value="<%=userName %>" maxlength="20"/>
										</td>
								<td colspan="5"></td>
								</tr>
							</tbody>
						</table>
					</div>
					
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
	<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>" ></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/employee_<%=langName%>.js"></script>
	<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.ztree-myjquery-b.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=iPath%>/js/addrbook.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath%>/user/employee/js/employeeBook.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#etree").load("<%=iPath %>/epl_employeeDepTree.jsp?treemethod=getEmpSecondDepJson&ac="+"<%=addcode%>"+"&dc="+"<%=delcode%>");
		});
	</script>
	</body>
	
</html>
