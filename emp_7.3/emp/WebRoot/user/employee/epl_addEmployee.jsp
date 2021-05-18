<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.employee.vo.LfEmployeeTypeVo" %>
<%@page import="com.montnets.emp.entity.sysuser.LfRoles"%>
<%@page import="com.montnets.emp.i18n.util.MessageUtils"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
	String path = request.getContextPath();
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));

	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("employeeBook");
	
	@ SuppressWarnings("unchecked")
	List<LfEmployeeTypeVo> zwList = (List<LfEmployeeTypeVo>)request.getAttribute("zwList");
	@SuppressWarnings("unchecked")
	List<LfRoles> roleList = (List<LfRoles>) request.getAttribute("roleList");
	boolean phoneRepeat = request.getAttribute("phoneRepeat") == null ? false :
		(Boolean)request.getAttribute("phoneRepeat");
	Long guid = (Long)request.getAttribute("guid");	
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	//查看是1个人权限  还是机构权限
	Integer permission = (Integer)request.getAttribute("permission");
	//默认新增操作员
	String isFlagPermission = "2";
	if((permission != null && permission == 1) || btnMap.get("1600-1200-1") == null){
		isFlagPermission = "1";
	}
	
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
	String addemployee = com.montnets.emp.i18n.util.MessageUtils.extractMessage("employee", "employee_dxzs_title_1", request);
 %>
 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<%@include file="/common/common.jsp" %>
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/reading.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/floating.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=commonPath %>/common/widget/zTreeStyle/zTreeStyle.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css"/>
		<link rel="stylesheet" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>">
		<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css">
		<link href="<%=commonPath %>/user/employee/css/uploadFile.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" type="text/css" href="<%=iPath %>/css/epl_addEmployee.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/dxzs_YGTongXunLuGuanLi.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
			<style type="text/css">
				table#sysTable{height:100%;font-size: 12px;width: 1100px;}
				td#styleTd1{width: 160px;}
				td#styleTd3{width: 180px;}
				td#styleTd2,td#styleTd4{width: 250px;}
				td#organ{width: 15%;}
			</style>
			<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
	</head>
	<body id="epl_addEmployee">
	<input type="hidden" id="pathUrl" value="<%=path %>" />
	<input type="hidden" id="inheritPath" value="<%=inheritPath %>" />
	<input type="hidden" id="bookType"  value="employee"/>
	<input type="hidden" id="checkUrl" value="<%=path %>/epl_employeeBook.htm?method=checkBook&add=a" />
		<div id="container" class="container">
			<%-- header开始 --%>
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode,addemployee) %>
			<%-- header结束 --%>
			<%-- 内容开始 --%>
			<div id="loginUser">
			</div>
			<div id="rContent" class="rContent">
			<%              		
				if(btnMap.get(menuCode+"-1")!=null)                       		
				{                        	
			%>
				<div class="titletop">
					<table class="titletop_table">
						<tr>
							<td class="titletop_td">
								<emp:message key="employee_dxzs_title_1" defVal="添加员工" fileName="employee"/>
							</td>
							<td align="right">
								<span class="titletop_font" onclick="javascript:back()">&larr;&nbsp;<emp:message key="employee_dxzs_title_2" defVal="返回上一级" fileName="employee"/></span>
							</td>
						</tr>
					</table>
				</div>
			
				<div id="tab">
				    <table class="user_table1" align="center">  
				      <tr align="center">
				      <td id="addone"  class="infotd1" onclick="javascript:changeinfo(1)"><emp:message key="employee_dxzs_title_3" defVal="单个添加" fileName="employee"/></td>
				      <td id="addall" class="infotd2" onclick="javascript:changeinfo(2)"><emp:message key="employee_dxzs_title_4" defVal="批量导入" fileName="employee"/></td>
				      </tr>
				    </table> 
	                <div id="addoneDiv" class="block">
	                   <center>
						<form action="<%=path %>/epl_employeeBook.htm?method=add" method="post" id="addForm" name="addForm" >
						<input type="hidden" id="bookId" name="bookId" value=''/>
						<input type="hidden" id="eguid" name="eguid" value="<%=guid %>" />
						<%--当前操作员是否有权限处理 新增 操作员或者个人权限 --%>
						<input type="hidden" name="isFlagPermission" id="isFlagPermission" value='<%=isFlagPermission%>'/>
							<table id="sysTable" class="dxzs_sysTable">
							<tr align="left">
							<td colspan="4" >
								<label class="user_label1"><emp:message key="employee_dxzs_title_5" defVal="员工基本信息" fileName="employee"/>：</label>
							</td>
							</tr>	
									<tr align="left">
										<td width="110px;"><emp:message key="employee_dxzs_title_6" defVal="工号：" fileName="employee"/></td>
										<td width="300px;">
											<label>
											<input type="text" name="employeeNo" id="employeeNo" value='' maxlength="20" class="input_bd" />
											</label>
											<%--  <font style="color: red;">&nbsp;*</font>--%>
										</td>
										<td  width="110px"><span><emp:message key="employee_dxzs_title_7" defVal="姓名：" fileName="employee"/></span></td>
										<td  width="300px;">
											<label>
											<input type="text" name="cName" id="cName" onkeyup="this.value=this.value.replace(/[']+/img,'')" value='' maxlength="60" class="input_bd" />
											</label>
											<font class="user_color_red">&nbsp;*</font>
										</td>
									</tr>
									<tr align="left">
										<td ><span><emp:message key="employee_dxzs_title_8" defVal="生日：" fileName="employee"/></span></td>
										<td ><label>
											<input type="text" value='' id="birth" name="birth" class="Wdate input_bd" readonly="readonly" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'%yyyy-%MM-%dd'})" class="input_book">
										</label></td>
										<td ><span><emp:message key="employee_dxzs_title_9" defVal="性别：" fileName="employee"/></span></td>
										<td >
											<label>
											<select name="sex" id="sex" >
												<option value="2"><emp:message key="employee_dxzs_title_10" defVal="未知" fileName="employee"/></option>
												<option value="1" ><emp:message key="employee_dxzs_title_11" defVal="男" fileName="employee"/></option>
												<option value="0" ><emp:message key="employee_dxzs_title_12" defVal="女" fileName="employee"/></option>
											</select>
											</label>
										</td>
									</tr>
									<tr align="left">
										<td ><span><emp:message key="employee_dxzs_title_13" defVal="所属机构：" fileName="employee"/></span></td>
										<td >
											<input type="hidden" name="depId" id="depId" value=''/>
									        <div class="user_div1">
											    <input id="depNam"  onclick="javascript:showMenu('addForm');"  name="depNam" type="text" readonly value='<emp:message key='employee_dxzs_title_15' defVal='点击选择机构' fileName='employee'/>' class="treeInput"/>
									            <a id="ssdep" onclick="javascript:showMenu('addForm');"></a><font class="user_color_red">&nbsp;*</font>
											</div>
											<div id="dropMenu">
 												<ul id="dropdownMenu" class="tree">
												</ul>
											</div>
										</td>
										<td ><span><emp:message key="employee_dxzs_title_14" defVal="职位：" fileName="employee"/></span></td>
										<td ><label>
									        <select  name="job" id="job" class="input_bd">
									        		<option value=""><emp:message key="employee_dxzs_title_16" defVal="请选择" fileName="employee"/></option>
									        		<% 
									        			if(zwList != null && zwList.size()>0){
									        				LfEmployeeTypeVo zw = null;
									        				for(int i=0;i<zwList.size();i++){
									        					zw = zwList.get(i);
									        		%>
									        			<option value="<%=zw.getName() %>"><%=zw.getName().replace("<","&lt;").replace(">","&gt;")%></option>
									        		<% 
									        				}
									        			}
									        		%>
									        </select>
											</label>
										</td>
									</tr>
									<tr align="left">
									<td colspan="4" >
										<label class="user_label1"><emp:message key="employee_dxzs_title_17" defVal="联系方式 ：" fileName="employee"/></label>
									</td>
								</tr>
									<tr align="left">	
										<td ><span><emp:message key="employee_dxzs_title_18" defVal="手机：" fileName="employee"/></span></td>
										<td >
											<label>	
											<input type="text" name="mobile" id="mobile" onkeyup="phoneInputCtrl($(this))" value='' class="input_bd input_book"/>
											</label><font class="user_color_red">&nbsp;*</font>
										</td>
										<td ><span><emp:message key="employee_dxzs_title_19" defVal="QQ：" fileName="employee"/></span></td>
										<td ><label>
											<input type="text" name="qq" id="qq"
												 onkeyup="numberControl($(this))" value='' maxlength="12" class="input_bd input_book"/>
											</label>
										</td>
									</tr>
									<tr align="left">
										<td ><span><emp:message key="employee_dxzs_title_20" defVal="E-mail：" fileName="employee"/></span></td>
										<td ><label>
											<input type="text" name="EMail" id="EMail" value='' class="input_bd input_book"/>
											</label>
										</td>
										<td ><span><emp:message key="employee_dxzs_title_21" defVal="MSN：" fileName="employee"/></span></td>
										<td ><label>
											<input type="text" name="msn" id="msn" value='' maxlength="64" class="input_bd input_book"/>
										</label></td>
								    </tr>

									<tr align="left">	
										<td ><span><emp:message key="employee_dxzs_title_22" defVal="座机：" fileName="employee"/></span></td>
										<td ><label>
											<input type="text" name="oph" id="oph" onkeyup="numberControl($(this))" value='' maxlength="64" class="input_bd input_book"/>
										</label></td>
										<td ><span><emp:message key="employee_dxzs_title_23" defVal="传真：" fileName="employee"/></span></td>
										<td >
											<label>
											<input type="text" name="fax" id="fax" onkeyup="numberControl($(this))" value='' maxlength="30" class="input_bd input_book"/>
											</label>
										</td>
									</tr>
							<% 
								if("2".equals(isFlagPermission)){
							%>
							<tr align="left"  id="isshowjoinuser">
								<td colspan="2" class="user_label1"><emp:message key="employee_dxzs_title_24" defVal="登录系统权限：" fileName="employee"/></td>
								<td colspan="2">&nbsp;
									<input name="isUser" class="dxzs_radio"  type="radio" value="0"  checked="checked" onclick="$('#isJoinUser').hide();$('.rContent').css('padding','10px 25px 190px 25px');"/> <span><emp:message key="employee_dxzs_title_25" defVal="无" fileName="employee"/></span>
									  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									 <input name="isUser" class="dxzs_radio"  onclick="$('#isJoinUser').show();$('.rContent').css('padding','10px 25px 335px 25px');" type="radio" value="1"/>
									 <emp:message key="employee_dxzs_title_26" defVal="有" fileName="employee"/>&nbsp;
								</td>
							</tr>
							<% 
								}else{
									%>
										<tr align="left" class="dxkf_display_none">
                                            <td class="user_label1"><emp:message key="employee_dxzs_title_24" defVal="登录系统权限：" fileName="employee"/></td>
                                            <td colspan="2">&nbsp;
                                                <input name="isUser"  class="dxzs_radio" type="radio" value="0"  checked="checked" /><emp:message key="employee_dxzs_title_25" defVal="无" fileName="employee"/>
                                            </td>
										</tr>
									<%
								}
							%>
							
							
							
							<tr id="isJoinUser" class="dxkf_display_none"><td colspan="4">
							<table  class="user_table2">
									<tr  align="left" >
									<%-- <%=ViewParams.LOGINID %>：--%>
									<td id="styleTd1"><%=MessageUtils.extractMessage("employee","employee_dxzs_title_110",request)%></td>
									<td id="styleTd2">
									<label>
											<input class="graytext input_bd" type="text" name="userName" id="userName" value="<emp:message key='employee_dxzs_title_28' defVal='登录密码和账号一致' fileName='employee'/>" maxlength="15"/>
											<font class="user_color_red">&nbsp;*</font>
									</label>
									</td>
									<td id="styleTd3"><emp:message key="employee_dxzs_title_27" defVal="操作员编码：" fileName="employee"/></td>
									<td id="styleTd4">
										<label>
										<input class="input_bd" type="text" name="userCode" id="userCode" size="10" value='' onkeyup= "if(value != value.replace(/[\n\s*\r]/g,'')) value=value.replace(/[\n\s*\r]/g,'')"  maxlength="20" />
										</label>
										<font class="user_color_red">&nbsp;*</font>
									</td>
								</tr>
								<tr align="left">
									<td><emp:message key="employee_dxzs_title_29" defVal="操作员机构：" fileName="employee"/></td>
									<td>
										<input type="hidden" name="userDepId" id="userDepId" value=""/>
										<div  class="user_div2">
											<input id="userDepName" class="input_bd fontColor" onclick="javascript:showUdepMenu();"  name="userDepName" type="text" readonly value='<emp:message key='employee_dxzs_title_15' defVal='点击选择机构' fileName='employee'/>'/>
											<font class="user_color_red">&nbsp;*</font>
											<a id="ssdep" onclick="javascript:showUdepMenu();"></a>
										</div>
										<div id="dropMenu_udep">
											<ul id="dropdownMenu_udep" class="tree">
											</ul>
										</div>
									</td>
									<td><emp:message key="employee_dxzs_title_30" defVal="账号状态：" fileName="employee"/></td>
									<td><select name="userState" id="userState" disabled="disabled">
											<option value="1"><emp:message key="employee_dxzs_title_31" defVal="启用" fileName="employee"/></option>
											<option value="0"><emp:message key="employee_dxzs_title_32" defVal="禁用" fileName="employee"/></option>
										</select>
									</td>
								</tr>
								<tr align="left">
								<td ><emp:message key="employee_dxzs_title_33" defVal="分配角色：" fileName="employee"/></td>
									<td>
										<label>
										<input id="roleName" onclick="openRoleChoose();"  name="roleName" type="text"
										readonly value='<emp:message key='employee_dxzs_title_34' defVal='点击选择角色' fileName='employee'/>'  allRoleName="" class="input_bd fontColor" />
										<font class="user_color_red">&nbsp;*</font>
										<div id="allRoleName" class="dxkf_display_none" class="allRoleName"></div>
										</label><input type="hidden" id="cheRoles" name="cheRoles" value="" />
								    </td>
						    		<td>
										<emp:message key="employee_dxzs_title_35" defVal="设置审核流程：" fileName="employee"/>
									</td>
									<td>
										<select name="isaudited" class="input_bd">
											<option value="2" ><emp:message key="employee_dxzs_title_36" defVal="免审" fileName="employee"/></option>
											<option value="1" ><emp:message key="employee_dxzs_title_37" defVal="必审" fileName="employee"/></option>
										</select>
									</td>
								
								</tr>
								
								<tr align="left">
									<td><emp:message key="employee_dxzs_title_38" defVal="操作员数据权限：" fileName="employee"/></td>
									<td>
										<input type="hidden" name="domDepId" id="domDepId"/>
										<select class="input_bd" id="userPerType" name="userPerType" onchange="changeUserPermType();" disabled="disabled">
								   		 	<option value="1"><emp:message key="employee_dxzs_title_39" defVal="个人" fileName="employee"/></option>
								    		<option id="depOption" value="2"><emp:message key="employee_dxzs_title_40" defVal="机构" fileName="employee"/></option>
										</select>
										<span id="depPerm">
											<input id="domdepName" name="domdepName" type="hidden" value=''/>
											&nbsp;<a id="selectDepBtn_user" onclick="javascript:showUserMenu();"><font color="blue"><emp:message key="employee_dxzs_title_16" defVal="请选择" fileName="employee"/></font></a>
										</span>
										<div id="dropMenu_user">
											<ul id="dropdownMenu_user" class="tree">
											</ul>
										</div>
									</td>
									 <td><emp:message key="employee_dxzs_title_41" defVal="机构审核人：" fileName="employee"/></td>
									<td><select name="isReviewer" class="input_bd">
											<option value="2" ><emp:message key="employee_dxzs_title_42" defVal="否" fileName="employee"/></option>
											<option value="1" ><emp:message key="employee_dxzs_title_43" defVal="是" fileName="employee"/></option>
										</select><font class="user_color_red">&nbsp;*</font>
									</td>
									
								</tr>
								<tr align="left">
									<td><emp:message key="employee_dxzs_title_44" defVal="员工通讯录权限：" fileName="employee"/></td>
									<td>
										<input type="hidden" name="domDepId_employ" id="domDepId_employ"/>
										<select class="input_bd" id="employPerType" name="employPerType" onchange="changeEmployPermType();"  >
								   		 	<option value="1"><emp:message key="employee_dxzs_title_39" defVal="个人" fileName="employee"/></option>
								    		<option id="depOption_employ" value="2"><emp:message key="employee_dxzs_title_40" defVal="机构" fileName="employee"/></option>
										</select>
										<span id="depPerm_employ" >
											<input id="domdepName_employ" name="domdepName_employ" type="hidden" value='' />
											&nbsp;<a id="selectDepBtn_employ" onclick="javascript:showEmployMenu();" ><font color="blue"><emp:message key="employee_dxzs_title_16" defVal="请选择" fileName="employee"/></font></a>
										</span>
										<div id="dropMenu_employ">
											<div class="user_div3">
												<input type="button" value="<emp:message key='employee_dxzs_button_1' defVal='确定' fileName='employee'/>" class="btnClass1" onclick="javascript:zTreeOnClickOK_employ();"/>&nbsp;&nbsp;
												<input type="button" value="<emp:message key='employee_dxzs_button_2' defVal='清空' fileName='employee'/>" class="btnClass1" onclick="javascript:cleanSelect_employ();"/>
											</div>	
											<ul  id="dropdownMenu_employ" class="tree"></ul>	
										</div>
									</td>
									<td><emp:message key="employee_dxzs_title_62" defVal="客户通讯录权限：" fileName="employee"/></td>
									<td>
										<input type="hidden" name="domDepId_client" id="domDepId_client"/>
										<select class="input_bd" id="clientPerType" name="clientPerType" onchange="changeClientPermType();" >
								   		 	<option value="1"><emp:message key="employee_dxzs_title_39" defVal="个人" fileName="employee"/></option>
								    		<option id="depOption_client" value="2"><emp:message key="employee_dxzs_title_40" defVal="机构" fileName="employee"/></option>
										</select>
										<span id="depPerm_client">
											<input id="domdepName_client" name="domdepName_client" type="hidden" value=''/>
											&nbsp;<a id="selectDepBtn_client" onclick="javascript:showClientMenu();"><font color="blue"><emp:message key="employee_dxzs_title_16" defVal="请选择" fileName="employee"/></font></a>
										</span>
										<div id="dropMenu_client">
											<div class="user_div3">
												<input type="button" value="<emp:message key='employee_dxzs_button_1' defVal='确定' fileName='employee'/>" class="btnClass1" onclick="javascript:zTreeOnClickOK_client();" />&nbsp;&nbsp;
												<input type="button" value="<emp:message key='employee_dxzs_button_2' defVal='清空' fileName='employee'/>" class="btnClass1" onclick="javascript:cleanSelect_client();" />
											</div>	
											<ul id="dropdownMenu_client" class="tree">
											</ul>
										</div>
									</td>
							
								</tr>
								<tr>
									<td align="left"><emp:message key="employee_dxzs_title_45" defVal="固定尾号：" fileName="employee"/></td>
									<td align="left">
										&nbsp;<input type="checkbox" id="isNeedSubno" name="isNeedSubno" onclick="javascript:clickSubno('<%=guid %>')"/>
										<input type="text" id="addSubno" name="addSubno" size="" maxlength="" value="" class="dxkf_display_none" onkeyup="value=value.replace(/[^\d]/g,'')"/>
										<input type="hidden" id="subno2" name="subno2" value=""/>
										<input type="hidden" id="haveSubno" name="haveSubno" value="2" />
										<input type="hidden" id="isGiveSubno" name="isGiveSubno" value="2" />
									</td>
									<td colspan="2">
									 &nbsp;
									</td>
								</tr>
								</table>
								</td></tr>
								<tr align="left">
									<td><emp:message key="employee_dxzs_title_46" defVal="备注：" fileName="employee"/></td>
									<td colspan="3"><textarea rows="4" name="comments" id="comments"></textarea>
									<font class="user_font2"><emp:message key="employee_dxzs_title_47" defVal="备注不能超过150个字" fileName="employee"/></font></td>
									</tr>
									<tr class="dxzs_tr1"><td colspan="4" id="hint" ><p id="zhu"></p></td></tr>
								</table>
								<div class="user_div4" align="right">
									<input type="button" name="button" id="button" value="<emp:message key='employee_dxzs_button_1' defVal='确定' fileName='employee'/>" class="btnClass5 mr23"  onclick="javascript:doSub()" />
									<input type="button" name="button2"  class="btnClass6" id="button2"  value="<emp:message key='employee_dxzs_button_3' defVal='返回' fileName='employee'/>" onclick="javascript:back()"/>
								</div>
						</form>
						</center>
					</div>
					<div id="addallDiv">
					  <center>
							<form action="<%=path %>/epl_employeeBook.htm?method=uploadBook" method="post" enctype="multipart/form-data" id="uploadForm" name="uploadForm" target="hidden_iframe">
								<iframe name="hidden_iframe" id="hidden_iframe"
								class="dxkf_display_none"></iframe>
								<input type="hidden" id="bookId" name="bookId" value=''/>
								<table id="sysTable" class="user_table2" >
									<thead>
										<tr align="left">
											<td id="organ"><span><emp:message key="employee_dxzs_title_13" defVal="所属机构：" fileName="employee"/></span></td>
											<td>
												<input type="hidden" name="depId" id="depId" value=''/>
										        <div  class="user_div5">
												    <input id="depNam"  onclick="javascript:showMenu('uploadForm');"  name="depNam" type="text" readonly value='<emp:message key='employee_dxzs_title_15' defVal='点击选择机构' fileName='employee'/>' class="treeInput depNam-inp"/>
									                <a id="ssdep" onclick="javascript:showMenu('uploadForm');" ></a><font class="user_color_red">&nbsp;*</font>
												</div>
												<div id="dropMenu">
						                        <div class="user_div3">
												<input type="button" value="<emp:message key='employee_dxzs_button_4' defVal='关闭' fileName='employee'/>" class="btnClass1 closeTree" >
												</div>
												<ul id="dropdownMenu" class="tree">
												</ul>
												</div>
											</td>
										</tr>
										<tr align="left">
											<td><span><emp:message key="employee_dxzs_title_48" defVal="选择上传文件：" fileName="employee"/></span></td>
											<td>
												<a href="javascript:" class="a-upload">
													<input type="file" name="uploadFile" id="uploadFile" onkeyup="numberControl($(this))" value='' maxlength="11"<%-- onchange="showFileName()--%>"/>
													<emp:message key="ydwx_wxfs_dtwxfs_schwj" defVal="上传文件" fileName="ydwx"></emp:message>
												</a>
												<span class="showFileName"></span>
												<font class="user_color_red">&nbsp;*</font>
											</td>
										</tr>
										<tr align="left">
										<td></td>
										
										<%--备份 p
										<td><a id="file" href="javascript:location.href='<%=path %>/down.htm?filepath=user/employee/file/temp/employeeTem.xls'" >支持Excel文件的上传。点击下载上传模板</a></td>
										 --%>
										 <%-- 新增 下载时不处理等待滚动 p --%>
										<td><a id="file" href="javascript:download_href('<%=path %>/down.htm?filepath=user/employee/file/temp/employeeTem_<%=empLangName%>.xls')" ><emp:message key='employee_dxzs_title_49' defVal='支持Excel文件的上传。点击下载上传模板' fileName='employee'/></a></td>
										
										
										
										</tr>
										<tr align="left">
											<td><span><emp:message key='employee_dxzs_title_50' defVal='过滤重号：' fileName='employee'/></span></td>
											<td>	
												<input type="radio" value="1" name="checkFlag" checked/><emp:message key='employee_dxzs_title_51' defVal='过滤' fileName='employee'/>
												&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
											 	<input type="radio" value="2" name="checkFlag" ><emp:message key='employee_dxzs_title_52' defVal='不过滤' fileName='employee'/>
												<font class="user_color_red">&nbsp;*</font><font class="zhu">
												<emp:message key='employee_dxzs_title_53' defVal='选择过滤时，则电话号码相同的记录会被过滤掉' fileName='employee'/></font>
											</td>
										</tr>
										
										<tr align="left">
										<td colspan="2" class="user_td1"><font class="zhu"><emp:message key='employee_dxzs_title_54' defVal='注：员工号重复的记录将不被添加；电话号码和姓名都重复的记录将不被添加！' fileName='employee'/><br><emp:message key='employee_dxzs_title_55' defVal='员工通讯录适用于企业管理内部员工资料，上限为5万个员工。' fileName='employee'/></font></td>
										</tr>
										<tr align="right">
											<td colspan="2" >
												
	                         				</td>
										</tr>
									</thead>
								</table>
								<div class="user_div6" align="right"><input type="button" name="button1" id="button1"  class="btnClass5 mr23" value="<emp:message key='employee_dxzs_button_1' defVal='确定' fileName='employee'/>" onclick="javascript:doUpload()"  />
												<input type="button" onclick="javascript:back()"	name="button2" id="button2"  class="btnClass6" value="<emp:message key='employee_dxzs_button_3' defVal='返回' fileName='employee'/>" /></div>
							</form>
							</center>
					</div>
				</div>
				<%-- 弹出添加角色 overflow-y:auto;border: 1px solid #cccccc;--%>

				<div id="roDiv" class="dxkf_display_none">
					<div class="user_div7">
						<%
							if (roleList != null) {
						%>
						<div id="roleDiv">
								<%
									for (int i = 0; i < roleList.size(); i++) {
									 LfRoles role = roleList.get(i);
								%>
							<input type="checkbox" name="roleId" id="roleId:<%=role.getRoleId() %>" value="<%=role.getRoleId() %>" />
							<label for="roleId:<%=role.getRoleId() %>"><%=role.getRoleName().replace("<","&lt;").replace(">","&gt;")%></label><br/>
						<% }%>
						 </div>
						 <%  } %>
					</div>
					<center>
					<div id="" class="roleBut">
						<input type="button" class="btnClass5" value="<emp:message key='employee_dxzs_button_1' defVal='确定' fileName='employee'/>" onclick="dorole()"/>
						</div>
					</center>
				</div>
				<div class="clear"></div>
			<%
				}
			
			%>
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
    <script language="javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>" type="text/javascript"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.ztree-myjquery-b.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath %>/common/js/checkFile.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script language="javascript" src="<%=iPath%>/js/book.js?V=<%=StaticValue.getJspImpVersion() %>" type="text/javascript"></script>
	<script language="javascript" src="<%=iPath%>/js/dataPerm.js?V=<%=StaticValue.getJspImpVersion() %>" type="text/javascript"></script>
	<script language="javascript" src="<%=iPath%>/js/addEmployee.js?V=<%=StaticValue.getJspImpVersion() %>" type="text/javascript"></script>
	<script type="text/javascript">

	$(document).ready(function(){
		// 解决 IE9 及以下 onChange 事件失效问题
		$("#uploadFile").change(function(){
            alert(getJsLocaleMessage('common','common_uploadSucceed'));
			showFileName();
		});
	});
	function showFileName(){
		var file=$("#uploadFile").val();
		var arr=file.split('\\');
		var fileName=arr[arr.length-1];
		$(".showFileName").text(fileName);
		$("#uploadFile").attr("title",fileName);
	}
	var userAgent = navigator.userAgent;
	var isChrome = 0;
	if (userAgent.indexOf("Chrome") > -1)
	{
   			isChrome = 1;
  	}
  	
    function closeIt()
	{   
		if($("#addall").hasClass("infotd1")){
	        $.post('common.htm?method=frontLog',{
				info:getJsLocaleMessage('employee','employee_alert_131')+$("#lguserid").val()+',corpcode='+$("#lgcorpcode").val()
			});
		}
    }
    function openIt()
	{   
		if($("#addall").hasClass("infotd1")){	
	        $.post('common.htm?method=frontLog',{
				info:getJsLocaleMessage('employee','employee_alert_131')+$("#lguserid").val()+',corpcode='+$("#lgcorpcode").val()
			});
		}
    }   
    //判断如果是谷歌浏览器则启用关闭方法
    if(isChrome==1){
   		window.onbeforeunload = closeIt;  
   	}else{
   		window.onunload = openIt;  
   	} 
	
	    
 
	//window.onbeforeunload = function(){ 
		//alert("ssd");
    	//$.post('common.htm?method=frontLog',{
		//			info:'员工通讯录添加，刷新页面'
		//});

	//}
	
	function show(){
		var $but = $(window.parent.document).find("#button1");
		$but.attr("disabled","");
		$but.prev().remove();
		<% String result=(String)request.getAttribute("result");
			String repeatPath = (String)request.getAttribute("path");
			if(result!=null && result.equals("true")){%>
			alert(getJsLocaleMessage('employee','employee_alert_132'));
			back();
		<%}else if (result!=null && (result.startsWith("upload")|| result.equals("noRecord")))
			{
			if(repeatPath!=null)
			{
			%>
				var url = '<%=repeatPath%>';
				var src = '<%=request.getContextPath()%>';
				var srcpath='<%=StaticValue.BASEURL %>';
				$.post(src+"/common.htm?method=checkFile", {url : url,upload:0 },function(returnmsg) {
						if(returnmsg == "true" ) {
							//window.showModalDialog(src + "/" + url +"?Rnd="+ Math.random());
							window.showModalDialog(srcpath + url +"?Rnd="+ Math.random());
						}else if (returnmsg == "false"){
							alert(getJsLocaleMessage('employee','employee_alert_133'));
						}else{
							alert(getJsLocaleMessage('employee','employee_alert_134'));
						}
						window.parent.back();
				});
			<%	
			}else{
				%>
					alert(getJsLocaleMessage('employee','employee_alert_135'));
					window.parent.back();
				<%
			}
			%>
		<%}else if(result!=null && result.equals("false")){%>
			alert(getJsLocaleMessage('employee','employee_alert_76'));
			
		<%}else if(result!=null && result.equals("maxemp")){%>
			alert(getJsLocaleMessage('employee','employee_alert_136')+<%=StaticValue.MAX_PEOPLE_COUNT%>+getJsLocaleMessage('employee','employee_alert_137'));
			
		<%}%>
	}

	</script>
	</body>
</html>
