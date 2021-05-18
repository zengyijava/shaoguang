<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.common.vo.LfSysuserVo" %>
<%@page import="com.montnets.emp.employee.vo.LfEmployeeTypeVo"%>
<%@page import="com.montnets.emp.employee.vo.LfEmployeeVo"%>
<%@page import="com.montnets.emp.entity.employee.LfEmployee"%>
<%@page import="com.montnets.emp.entity.sysuser.LfDep"%>
<%@page import="com.montnets.emp.entity.sysuser.LfRoles"%>
<%@page import="com.montnets.emp.entity.sysuser.LfSysuser"%>
<%@page import="com.montnets.emp.i18n.util.MessageUtils"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
	String path = request.getContextPath();
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));

	
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("employeeBook");
	LfEmployee lc = (LfEmployee)request.getAttribute("employee");
	LfEmployeeVo lco = (LfEmployeeVo)request.getAttribute("employeeVo");
	java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd");
	if (lco == null)
	{
		lco = new LfEmployeeVo();
	}
	String depName = (String) request.getAttribute("depName");
	
	@ SuppressWarnings("unchecked")
	List<LfEmployeeTypeVo> zwList = (List<LfEmployeeTypeVo>)request.getAttribute("zwList");
	boolean flag=false;
	//判断导入的职位是否在职位管理中存在
	for(LfEmployeeTypeVo vo:zwList){
		if (vo.getName().equals(lc.getDuties())) {
			flag = true;
			break;
		}
	}
	// 不存在则添加在职位选择下拉框中显示出来
	if(!flag){
		LfEmployeeTypeVo lfEmployeeTypeVo=new LfEmployeeTypeVo();
		lfEmployeeTypeVo.setName(lc.getDuties());
		zwList.add(lfEmployeeTypeVo);
	}
	LfSysuserVo sysuser = (LfSysuserVo)request.getAttribute("sysuser");
	LfSysuser user = (LfSysuser)request.getAttribute("lfsysuser");
	
	
	String employeeIds = (String)request.getAttribute("employeeIds");
   	String employeeNames = (String)request.getAttribute("employeeNames");
   	String clientIds = (String)request.getAttribute("clientIds");
   	String clientNames = (String)request.getAttribute("clientNames");
	
	@SuppressWarnings("unchecked")
	List<LfRoles> roleList = (List<LfRoles>) request.getAttribute("roleList");
	String pageI=(String)(request.getSession(false)!=null?request.getSession(false).getAttribute("pageI"):"");
	String pageS=(String)(request.getSession(false)!=null?request.getSession(false).getAttribute("pageS"):"");
	String oldDepId=(String)(request.getSession(false)!=null?request.getSession(false).getAttribute("oldDepId"):"");
		//是否存在尾号
   	Integer isExistSubno = (Integer)request.getAttribute("isExistSubno");
   	isExistSubno = isExistSubno==null?0:isExistSubno;
   	String usedSubno = (String)request.getAttribute("usedSubno");
   	Integer subnoDigit = (Integer)request.getAttribute("subnoDigit");
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	//查看是1个人权限  还是机构权限
	Integer permission = (Integer)request.getAttribute("permission");
	//默认新增操作员
	String isFlagPermission = "2";
	if((permission != null && permission == 1) || btnMap.get("1600-1200-1") == null){
		isFlagPermission = "1";
	}
	String depNam="";//操作员权限机构名称
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
	String dlyz = com.montnets.emp.i18n.util.MessageUtils.extractMessage("employee", "employee_dxzs_title_28", request);
	String xgyz = com.montnets.emp.i18n.util.MessageUtils.extractMessage("employee", "employee_dxzs_title_60", request);
	String xzjg = com.montnets.emp.i18n.util.MessageUtils.extractMessage("employee", "employee_dxzs_title_15", request);
	String djxzjs = com.montnets.emp.i18n.util.MessageUtils.extractMessage("employee", "employee_dxzs_title_34", request);
	String jigou = com.montnets.emp.i18n.util.MessageUtils.extractMessage("employee", "employee_dxzs_title_40", request);
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
		<link rel="stylesheet" href="<%=commonPath %>/common/widget/zTreeStyle/zTreeStyle.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css"/>
		<link rel="stylesheet" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>">
		<link rel="stylesheet" type="text/css" href="<%=iPath %>/css/epl_editEmployee.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/dxzs_YGTongXunLuGuanLi.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%if(StaticValue.ZH_HK.equals(langName)){%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<style type="text/css">
			#epl_editEmployee table#sysTable{
				height:100%;font-size: 12px;width: 1100px;
			}
			#epl_editEmployee td#styleTd1{
				width: 160px;
			}
			#epl_editEmployee td#styleTd3{
				width: 180px;
			}
			#epl_editEmployee td#styleTd2,td#styleTd4{
				width: 250px;
			}
		</style>
		<%}%>
	</head>
	<body onload="show()" id="epl_editEmployee">
	<input type="hidden" id="pathUrl" value="<%=path %>" />
	<input type="hidden" id="inheritPath" value="<%=inheritPath %>" />
	<input type="hidden" id="bookType"  value="employee"/>
	<input type="hidden" id="hidOpType"  value="edit"/>
	<input type="hidden" id="pageI"  value="<%=pageI %>"/>
	<input type="hidden" id="pageS"  value="<%=pageS %>"/>
	<input type="hidden" id="oldDepId"  value="<%=oldDepId %>"/>
	<input type="hidden" id="checkUrl" value="<%=path %>/epl_employeeBook.htm?method=checkBook" />
		<div id="container" class="container" style="height: 950px!important">
			<%-- header开始 --%>
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode,xgyz) %>
			<%-- header结束 --%>
			<div id="loginUser"></div>
			<%-- 内容开始 --%>
			<div id="rContent" class="rContent" >
			<%              		
				if(btnMap.get(menuCode+"-1")!=null)                       		
				{                        	
					%>
				<div class="titletop">
					<table class="titletop_table">
						<tr>
							<td class="titletop_td">
								<emp:message key="employee_dxzs_title_60" defVal="修改员工" fileName="employee"/>
							</td>
							<td align="right">
								<span class="titletop_font"  onclick="javascript:back()">&larr;&nbsp;<emp:message key="employee_dxzs_title_2" defVal="返回上一级" fileName="employee"/></span>
							</td>
						</tr>
					</table>
				</div>
					<div id="detail_Info">
						<form action="<%=path %>/epl_employeeBook.htm?method=add" method="post" id="addForm" name="addForm" >
						<input type="hidden" id="guId" name="guId"  value="<%=lc.getGuId() %>"/>
						<input type="hidden" id="bookId" name="bookId" value='<%=request.getParameter("bookId")==null?"":request.getParameter("bookId") %>'/>
						<input type="hidden" id="userId" name="userId" value="<%=sysuser!=null?sysuser.getUserId():"" %>"/>
						<%--当前操作员是否有权限处理 新增 操作员或者个人权限 --%>
						<input type="hidden" name="isFlagPermission" id="isFlagPermission" value='<%=isFlagPermission%>'/>
						<table id="sysTable" class="dxzs_sysTable"  height="100%">
						<tr align="left">
							<td colspan="4" >
								<label class="user_label"><emp:message key="employee_dxzs_title_5" defVal="员工基本信息" fileName="employee"/>：</label>
							</td>
							</tr>
						<tr >
						<td class="user_td1"><span><emp:message key="employee_dxzs_title_6" defVal="工号：" fileName="employee"/></span></td>
						<td class="user_td2">
							<label>
								<input type="text" name="employeeNo" id="employeeNo" value="<%=lc.getEmployeeNo()==null?"":lc.getEmployeeNo()%>"
								<%if(lc.getEmployeeNo()!=null&&!"".equals(lc.getEmployeeNo().trim())) {%>disabled="disabled"<%} %> maxlength="20" class="input_bd"/>
							</label>		
						</td>
						<td class="user_td1"><span><emp:message key="employee_dxzs_title_7" defVal="姓名：" fileName="employee"/></span></td>
						<td class="user_td2"><label>
											<input type="text" name="cName" id="cName"
												value='<%=lc.getName()==null?"":lc.getName() %>' maxlength="60" class="input_bd"/>
										</label><font class="user_color_red">&nbsp;*</font></td>
						</tr>
						<tr>
						<td><span><emp:message key="employee_dxzs_title_8" defVal="生日：" fileName="employee"/></span></td><td><label>
										<%
										String s = "";
										if(lc.getBirthday()!=null)
										{
											s = df.format(lc.getBirthday());
										}
										%>
											<input type="text" value='<%=s %>' id="birth" name="birth" class="Wdate" readonly="readonly" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'%yyyy-%MM-%dd'})">
										</label></td>
						<td ><span><emp:message key="employee_dxzs_title_9" defVal="性别：" fileName="employee"/></span></td><td ><label>
										<select name="sex" id="sex">
											<option value="2"><emp:message key="employee_dxzs_title_10" defVal="未知" fileName="employee"/></option>
											<option value="1" <%="1".equals(lc.getSex().toString())?"selected":"" %>><emp:message key="employee_dxzs_title_11" defVal="男" fileName="employee"/></option>
											<option value="0" <%="0".equals(lc.getSex().toString())?"selected":"" %>><emp:message key="employee_dxzs_title_12" defVal="女" fileName="employee"/></option>
										</select>
									</label></td>
					
						</tr>
						<tr>
						<td><span><emp:message key="employee_dxzs_title_13" defVal="所属机构：" fileName="employee"/></span></td><td>
										<input type="hidden" name="depId" id="depId" value='<%=lc.getDepId()==null?"":lc.getDepId() %>'/>
								        <div class="dxzs_depNam">
									    	<input id="depNam" onclick="showMenu()" name="depNam" type="text" readonly value='<%=depName==null?"":depName %>'/>
											<font class="user_color_red">&nbsp;*</font>
								         	<a id="ssdep" onclick="showMenu()"></a>
										</div>
									<div id="dropMenu">
<%--									<div id='closeDepTreeClass' class='closeDepTreeClassHover'></div>--%>
									<ul id="dropdownMenu" class="tree">
									</ul>
									</div>
									</td>
						<td><span><emp:message key="employee_dxzs_title_14" defVal="职位：" fileName="employee"/></span></td><td><label>
										<select  name="job" id="job" class="input_bd" >
													<option value=""><emp:message key="employee_dxzs_title_16" defVal="请选择" fileName="employee"/></option>
									        		<% 
									        			if(zwList != null && zwList.size()>0){
									        				LfEmployeeTypeVo zw = null;
									        				for(int i=0;i<zwList.size();i++){
									        					zw = zwList.get(i);
									        		%>
									        			<option value="<%=zw.getName()%>" <% if(lc.getDuties()!=null && !"".equals(lc.getDuties()) && lc.getDuties().equals(String.valueOf(zw.getName()))){%>selected="selected"<%}%>><%=zw.getName().replace("<","&lt;").replace(">","&gt;")%></option>
									        		<% 
									        				}
									        			}
									        		%>
										</select>
									        	<input type="hidden" id="oldType"  value='<%=lc.getDuties()==null?"":lc.getDuties() %>'/>
										</label></td>
						</tr>
						<tr align="left">
						<td colspan="4" >
							<label class="user_label"><emp:message key="employee_dxzs_title_17" defVal="联系方式 ：" fileName="employee"/></label>
						</td>
						</tr>
						<tr>			
						<td><span><emp:message key="employee_dxzs_title_18" defVal="手机：" fileName="employee"/></span></td><td><label>	
						<%
									String mobile = lc.getMobile()!=null?lc.getMobile():"";
									if(btnMap.get(StaticValue.PHONE_LOOK_CODE) == null && !"".equals(mobile)){
										//无号码的查看权限，需替换手机号码的星号
										mobile = mobile.substring(0,3)+"*****"+mobile.substring(8,11);
									}
								 %>
								 <input type="hidden" name="ishidephone" id="ishidephone" value="<%=mobile%>"/>
								 <input type="hidden" name="mobile" id="mobile" value="<%=lc.getMobile()!=null?lc.getMobile():"" %>"/>
											<input type="text" name="tempMobile" id="tempMobile" onkeyup="phoneInputCtrl($(this))"
												  value='<%=mobile %>'  class="input_bd" />
										</label><font class="user_color_red">&nbsp;*</font></td>
					
					<td ><span><emp:message key="employee_dxzs_title_19" defVal="QQ：" fileName="employee"/></span></td><td><label>
											<input type="text" name="qq" id="qq"
												 onkeyup="numberControl($(this))" value='<%=lc.getQq()==null?"":lc.getQq() %>' maxlength="12" class="input_bd"/>
										</label></td>
						</tr>
						<tr>
						<td><span><emp:message key="employee_dxzs_title_20" defVal="E-mail：" fileName="employee"/></span></td><td><label>
											<input type="text" name="EMail" id="EMail" value='<%=lc.getEmail()==null?"":lc.getEmail() %>' class="input_bd"/>
										</label></td>
						<td><span><emp:message key="employee_dxzs_title_21" defVal="MSN：" fileName="employee"/></span></td><td><label>
											<input type="text" name="msn" id="msn" value='<%=lc.getMsn()==null?"":lc.getMsn() %>' class="input_bd"/>
										</label></td>
						</tr>
						<tr>				
						<td><span><emp:message key="employee_dxzs_title_22" defVal="座机：" fileName="employee"/></span></td><td><label>
											<input type="text" name="oph" id="oph" onkeyup="numberControl($(this))" value='<%=lc.getOph()==null?"":lc.getOph() %>' class="input_bd input_book"/>
										</label></td>
							<td><span><emp:message key="employee_dxzs_title_23" defVal="传真：" fileName="employee"/></span></td><td>
							<label>
							<input type="text" name="chuanz" id="chuanz" onkeyup="numberControl($(this))" value='<%=lc.getFax()==null?"":lc.getFax() %>' maxlength="30" class="input_bd input_book"/>
							</label>
							</td>
						</tr>
					
						
						<% 
							if("2".equals(isFlagPermission)){
						%>		
							<tr align="left">
								<td colspan="2"><label class="user_label"><emp:message key="employee_dxzs_title_24" defVal="登录系统权限：" fileName="employee"/></label></td>
								<td colspan="2">&nbsp;
									<input name="isUser" class="dxzs_input"  type="radio" value="0"  checked="checked" onclick="$('#isJoinUser').hide();$('.rContent').css('padding','10px 25px 190px 25px');"/> <emp:message key="employee_dxzs_title_25" defVal="无" fileName="employee"/>
									  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									 <input name="isUser" class="dxzs_input"  onclick="$('#isJoinUser').show();$('.rContent').css('padding','10px 25px 340px 25px');" type="radio" value="1" />
									 <emp:message key="employee_dxzs_title_26" defVal="有" fileName="employee"/>&nbsp;
								</td>
							</tr>
							<% 
							}else{
								%>
									<tr align="left" class="dxkf_display_none">
									<td>
										<input name="isUser"  class="dxzs_input"  type="radio" value="0"  checked="checked" /><emp:message key="employee_dxzs_title_25" defVal="无" fileName="employee"/>
									</td>
									</tr>
								<%
							}
						%>	
								
								<tr id="isJoinUser" class="dxkf_display_none"><td colspan="4">
								<table class="user_table">
									<tr  align="left" >
									<%-- <%=ViewParams.LOGINID %> --%>
									<td id="styleTd1"><%=MessageUtils.extractMessage("employee","employee_dxzs_title_110",request)%></td>
									<td id="styleTd2">
									<label>
											<input class="graytext input_bd" type="text" name="userName" 
											id="userName" value='<%=sysuser!=null?sysuser.getUserName(): dlyz%>' maxlength="15"
											<%if(sysuser!=null){%> disabled="disabled" <%} %>
											/>
													<font class="user_color_red">&nbsp;*</font>
											</label>
											</td>
											<td id="styleTd3"><emp:message key="employee_dxzs_title_27" defVal="操作员编码：" fileName="employee"/></td>
											<td id="styleTd4">
												<label>
												<input class="input_bd" type="text" name="userCode" id="userCode" size="10" value='<%=sysuser!=null?sysuser.getUserCode():"" %>' 
												<%if(sysuser!=null){%> disabled="disabled" <%} %> onkeyup= "if(value != value.replace(/[\n\s*\r]/g,'')) value=value.replace(/[\n\s*\r]/g,'')"  maxlength="20" />
												</label>
												<font class="user_color_red">&nbsp;*</font>
											</td>
										</tr>
										<tr align="left">
											<td><emp:message key="employee_dxzs_title_29" defVal="操作员机构：" fileName="employee"/></td>
											<td>
												<input type="hidden" id="_userDepId" value='<%=sysuser!=null?sysuser.getDepId():"" %>'/>
												<input type="hidden" name="userDepId" id="userDepId" value='<%=sysuser!=null?sysuser.getDepId():"" %>'/>
												<div  class="user_td2">
													<input id="userDepName" class="input_bd fontColor" onclick="showUdepMenu();" name="userDepName" type="text" readonly value='<%=sysuser!=null?sysuser.getDepName():xzjg %>'/>
													<font class="user_color_red">&nbsp;*</font>
													<a id="ssdep" onclick="showUdepMenu()"></a>
												</div>
												<div id="dropMenu_udep" >
		<%--											<div id='closeDepTreeClass' class='closeDepTreeClassHover'></div>--%>
													<ul id="dropdownMenu_udep" class="tree">
													</ul>
												</div>
											</td>
											<td><emp:message key="employee_dxzs_title_30" defVal="账号状态：" fileName="employee"/></td>
											<td><select name="userState" id="userState" disabled="disabled">
													<option value="1" <%if(user!=null && user.getUserState() ==1){%> selected="selected"<%} %>><emp:message key="employee_dxzs_title_31" defVal="启用" fileName="employee"/></option>
													<option value="0" <%if(user!=null && user.getUserState() ==0){%> selected="selected"<%} %>><emp:message key="employee_dxzs_title_32" defVal="禁用" fileName="employee"/></option>
													<option value="2" <%if(user!=null && user.getUserState() ==2){%> selected="selected"<%} %>><emp:message key="employee_dxzs_title_61" defVal="注销" fileName="employee"/></option>
												</select>
											</td>
										</tr>
										<tr align="left">
										<td ><emp:message key="employee_dxzs_title_33" defVal="分配角色：" fileName="employee"/></td>
											<td>
												<label>
												<%
													String name = "";
													String showName = "";
													String clas = "";
													if(sysuser != null){
														@ SuppressWarnings("unchecked")
														List<LfRoles> sysroleList = sysuser.getRoleList();
														if(sysroleList != null && sysroleList.size()>0){
														    if(sysroleList.size() == 1){
														    	showName = sysroleList.get(0).getRoleName();
														    }else{
														    	showName = sysroleList.get(0).getRoleName()+"...";
														    }
															for(LfRoles role:sysroleList){
																name += role.getRoleName()+",";
															}
															name = name.substring(0,name.lastIndexOf(","));
														}else{
															showName = djxzjs;
														}
													}else {
														showName = djxzjs;
													}
													clas = djxzjs.equals(showName) ? "fontColor":"";
												%>
												<input id="roleName"  class="input_bd <%=clas%>" onclick="openRoleChoose();"  name="roleName" type="text"
												readonly value='<%=showName%>' allRoleName="<%=name%>"
												/><font class="user_color_red">&nbsp;*</font>
												<div id="allRoleName"  class="allRoleName"></div>
												</label>
												<input type="hidden" id="cheRoles" name="cheRoles" value="" />
											</td>
											<td>
												<emp:message key="employee_dxzs_title_35" defVal="设置审核流程：" fileName="employee"/>
											</td>
											<td>
												<select name="isaudited" class="input_bd dxzs_isaudited">
													<option value="2"  <%if(user!=null && user.getIsAudited() ==2){%> selected="selected"<%} %>><emp:message key="employee_dxzs_title_36" defVal="免审" fileName="employee"/></option>
													<option value="1"  <%if(user!=null && user.getIsAudited() ==1){%> selected="selected"<%} %>><emp:message key="employee_dxzs_title_37" defVal="必审" fileName="employee"/></option>
												</select>
											</td>
								
										</tr>
									
										<tr align="left">
											<td><emp:message key="employee_dxzs_title_38" defVal="操作员数据权限：" fileName="employee"/></td>
											<td>
											<%
											String depId="";
											if(sysuser != null && sysuser.getDomDepList()!=null && sysuser.getDomDepList().size()>0)
											{
												LfDep dep = (LfDep)(sysuser.getDomDepList().get(0));
												depNam = dep.getDepName();
												depId=dep.getDepId().toString();
											}
											%>
												<input type="hidden" id="_domDepId" value="<%=depId%>"/>
												<input type="hidden" name="domDepId" id="domDepId" value="<%=depId%>"/>
												<select id="userPerType" class="input_bd" name="userPerType" onchange="changeUserPermType();" >
												    <option value="1"><emp:message key="user_xtgl_czygl_text_124" defVal="个人" fileName="user" /></option>
												<%
													LfSysuser lfSysuser = (LfSysuser)(request.getSession(false)!=null?request.getSession(false).getAttribute("loginSysuser"):new LfSysuser());
												    int permissionType = lfSysuser.getPermissionType();
													if(permissionType == 2){
													   out.print("<option id=\"depOption\" value=\"2\">"+jigou+"</option>");
													}
												%>
												</select>
												<span id="depPerm">
												<input id="domdepName" name="domdepName" class="input_bd" type="hidden" readonly value='<%="".equals(depNam)?"":depNam%>'/>
												&nbsp;<a id="selectDepBtn_user" onclick="javascript:showUserMenu();" ><font class="user_color_blue"><emp:message key="common_pleaseSelect" defVal="请选择" fileName="common"></emp:message></font></a>
												</span>
												<div id="dropMenu_user" >
													<ul id="dropdownMenu_user" class="tree" >
													</ul>
												</div>
											</td>
											<td><emp:message key="employee_dxzs_title_41" defVal="机构审核人：" fileName="employee"/></td>
											<td><select name="isReviewer" class="input_bd user_select">
													<option value="2"  <%if(sysuser!=null && sysuser.getIsReviewer() ==2){%> selected="selected"<%} %>><emp:message key="employee_dxzs_title_42" defVal="否" fileName="employee"/></option>
													<option value="1" <%if(sysuser!=null && sysuser.getIsReviewer() ==1){%> selected="selected"<%} %>><emp:message key="employee_dxzs_title_43" defVal="是" fileName="employee"/></option>
												</select><font class="user_color_red">&nbsp;*</font>
											</td>
										</tr>
										<tr align="left">
											<td><emp:message key="employee_dxzs_title_44" defVal="员工通讯录权限：" fileName="employee"/></td>
											<td>
												<input type="hidden" name="domDepId_employ" id="domDepId_employ"/>
												<select class="input_bd user_select" id="employPerType" name="employPerType" onchange="changeEmployPermType();"  >
										   		 	<option value="1"><emp:message key="employee_dxzs_title_39" defVal="个人" fileName="employee"/></option>
										    		<option id="depOption_employ" value="2"><emp:message key="employee_dxzs_title_40" defVal="机构" fileName="employee"/></option>
												</select>
												<span id="depPerm_employ">
													<input id="domdepName_employ" name="domdepName_employ" type="hidden" value='' />
													&nbsp;<a id="selectDepBtn_employ" onclick="javascript:showEmployMenu();"  ><font class="user_color_blue"><emp:message key="employee_dxzs_title_16" defVal="请选择" fileName="employee"/></font></a>
												</span>
												<div id="dropMenu_employ">
													<div class="user_div2">
														<input type="button" value="<emp:message key='employee_dxzs_button_1' defVal='确定' fileName='employee'/>" class="btnClass1" onclick="javascript:zTreeOnClickOK_employ();" />&nbsp;&nbsp;
														<input type="button" value="<emp:message key='employee_dxzs_button_2' defVal='清空' fileName='employee'/>" class="btnClass1" onclick="javascript:cleanSelect_employ();" />
													</div>	
													<ul  id="dropdownMenu_employ" class="tree" ></ul>	
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
													<input id="domdepName_client" name="domdepName_client" type="hidden" value='' />
													&nbsp;<a id="selectDepBtn_client" onclick="javascript:showClientMenu();"  ><font class="user_color_blue"><emp:message key="employee_dxzs_title_16" defVal="请选择" fileName="employee"/></font></a>
												</span>
												<div id="dropMenu_client">
													<div class="user_div2">
														<input type="button" value="<emp:message key='employee_dxzs_button_1' defVal='确定' fileName='employee'/>" class="btnClass1" onclick="javascript:zTreeOnClickOK_client();"/>&nbsp;&nbsp;
														<input type="button" value="<emp:message key='employee_dxzs_button_2' defVal='清空' fileName='employee'/>" class="btnClass1" onclick="javascript:cleanSelect_client();"/>
													</div>	
													<ul id="dropdownMenu_client" class="tree" >
													</ul>
												</div>
											</td>
										</tr>
											<tr align="left"> 
											 <td ><emp:message key="employee_dxzs_title_45" defVal="固定尾号：" fileName="employee"/></td>
											<td align="left">
													<input type="checkbox" name="isNeedSubno" id="isNeedSubno"  <%=isExistSubno+0==1?"checked":""%> onclick="javascript:updateHaveSubno('<%=isExistSubno%>','<%=usedSubno%>');"/>
													  <%--当操作员分配尾号的时候   进行的INPUT 可供修改 --%>
													<input type="text" id="addSubno" name="addSubno" size="<%=subnoDigit%>" maxlength="<%=subnoDigit%>" value="<%=usedSubno!=null?usedSubno:""%>" onkeyup="value=value.replace(/[^\d]/g,'')"/>
													  <%--当操作员分配尾号的时候  分配的尾号 --%>
													<input type="hidden" id="subno2" name="subno2" value="<%=usedSubno!=null?usedSubno:""%>"/>
													<%--
													  <%--当操作员没有分配尾号的时候   进行的INPUT  --%>
													 <input type="text" id="addSubno" name="addSubno" size="<%=subnoDigit%>" maxlength="<%=subnoDigit%>" value="" class="dxkf_display_none" onkeyup="value=value.replace(/[^\d]/g,'')"/>
													  <%--当操作员没有分配尾号的时候  分配的尾号 --%>
													 <input type="hidden" id="subno2" name="subno2" value=""/>
													  <%--当分配过了的操作员，1是判断是否有修改     2是进行回收        当没分配过的操作员      1是进行新增和修改    2是进行回收--%>
													 <input type="hidden" id="haveSubno" name="haveSubno" value="<%=isExistSubno+0==1?"1":"2"%>"/>
													 <%--判断该操作员是否分配了操作员固定尾号 --%>
													 <input type="hidden" id="isGiveSubno" name="isGiveSubno" value="<%=isExistSubno+0==1?"1":"2"%>"/>
											</td>
										</tr>
										</table>
									</td>
								</tr>
								<tr align="left">
						<td><emp:message key="employee_dxzs_title_46" defVal="备注：" fileName="employee"/></td>
						<td colspan="3"><textarea rows="4" name="comments" id="comments"><%=lc.getCommnets()==null?"":lc.getCommnets() %></textarea> </td>
						</tr>

						</table>
						<div class="user_div3" align="right">
												<input type="button" name="button" id="button" value="<emp:message key='employee_dxzs_button_1' defVal='确定' fileName='employee'/>" class="btnClass5 mr23" onclick="javascript:doSub()" />
										<input type="button" onclick="javascript:back()"	name="button2" id="button2"  class="btnClass6" value="<emp:message key='employee_dxzs_button_3' defVal='返回' fileName='employee'/>" /></div>
								</form>
												</div>
							</div>
							<div class="clear"></div>
				<div id="roDiv" class="dxkf_display_none">
							<div class="user_div4">
								<%
									if (roleList != null && sysuser != null){
									Map<Long,Object> roleMap=new HashMap<Long,Object>();
									for(int r=0;r<sysuser.getRoleList().size();r++){
										LfRoles role=(LfRoles)sysuser.getRoleList().get(r);
										roleMap.put(role.getRoleId() ,role);
									}
								%>
								<div id="roleDiv">
								<%
									for (int i = 0; i < roleList.size(); i++){
										LfRoles role = roleList.get(i);
								%>
									<input type="checkbox" name="roleId" id="roleId:<%=role.getRoleId()%>" value="<%=role.getRoleId()%>" <%=roleMap.get(role.getRoleId())!=null?"checked":""%> />
									<label for="roleId:<%=role.getRoleId()%>"><%=role.getRoleName().replace("<","&lt;").replace(">","&gt;")%></label><br/>
								<%
									}
									roleMap.clear();
									for(LfRoles role:roleList){
										roleMap.put(role.getRoleId() ,role);
									}
									for (int r=0;r<sysuser.getRoleList().size();r++){
										LfRoles role=(LfRoles)sysuser.getRoleList().get(r);
										if(roleMap.get(role.getRoleId()) == null){
								%>
									<input type="checkbox" disabled="disabled" name="roleId" id="roleId:<%=role.getRoleId() %>" value="<%=role.getRoleId() %>" checked />
									<label for="roleId:<%=role.getRoleId() %>"><%=role.getRoleName().replace("<","&lt;").replace(">","&gt;")%></label><br/>
								<%}
								}
								%>
								</div>
								<% } else if(roleList != null){%>
								<div id="roleDiv">
								<%
									for (int i = 0; i < roleList.size(); i++) {
									 LfRoles role = roleList.get(i);
								%>
							<input type="checkbox" name="roleId" id="roleId:<%=role.getRoleId() %>" value="<%=role.getRoleId() %>" />
							<label for="roleId:<%=role.getRoleId() %>"><%=role.getRoleName().replace("<","&lt;").replace(">","&gt;")%></label><br/>
						<% }%>
						 </div>
								<%}%>
							</div>
							<center>
									<div id="" class="roleBut">
										<input type="button" class="btnClass5" value="<emp:message key='employee_dxzs_button_1' defVal='确定' fileName='employee'/>" onclick="dorole()"/>
										</div>
									</center>
						</div>
			</div>
				<%
							}
						%>
			<%-- 内容结束 --%>
			<%-- foot开始 --%>
			<div class="bottom">
				<div id="bottom_right">
					<div id="bottom_left"></div>
				</div>
			</div>
			<%-- foot结束 --%>
    <div class="clear"></div>
    <script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/employee_<%=langName%>.js"></script>
    <script language="javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>" type="text/javascript"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.ztree-myjquery-b.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script language="javascript" src="<%=iPath%>/js/book.js?V=<%=StaticValue.getJspImpVersion() %>" type="text/javascript"></script>
	<script language="javascript" src="<%=iPath%>/js/dataPerm.js?V=<%=StaticValue.getJspImpVersion() %>" type="text/javascript"></script>
	<script language="javascript" src="<%=iPath%>/js/editEmployee.js?V=<%=StaticValue.getJspImpVersion() %>" type="text/javascript"></script>
	<script type="text/javascript">
		function show(){
			<% String result=(String)request.getAttribute("result");
				if(result!=null && result.equals("true")){%>
				alert(getJsLocaleMessage('employee','employee_alert_144'));
				//location.href = "<%=path %>/epl_employeeBook.htm";
				back();
			<%}else if(result!=null && result.equals("false")){%>
				alert(getJsLocaleMessage('employee','employee_alert_76'));
			<%}else if(result!=null && result.equals("maxemp")){%>
				alert(getJsLocaleMessage('employee','employee_alert_136')+"<%=StaticValue.MAX_PEOPLE_COUNT%>" +getJsLocaleMessage('employee','employee_alert_137'));
			<%}%>
		}

		
		$(document).ready(function(){
			if($('#isNeedSubno').attr('checked')){
					$('#addSubno').show();
			}else{
					$('#addSubno').hide();
				}
		    setting.expandSpeed = ($.browser.msie && parseInt($.browser.version)<=7)?"":"fast";
			reloadTree();
			<% if("2".equals(isFlagPermission)){%>
				closeTreeFunOnSpc(["dropMenu_employ","dropMenu_client","dropMenu_udep","dropMenu","dropMenu_user"]);
				if(<%=sysuser!=null && sysuser.getUserType()==2%>){
					$("input[name='isUser']").get(1).checked = true; 
					$('#isJoinUser').show();
				}
				if(<%=sysuser!=null%>){
					var permissionType = <%=sysuser!=null? sysuser.getPermissionType():1%>;
					if(permissionType == 2){
					    var depEle = $("#userPerType option[value='"+permissionType+"']");
					    depEle.attr("text",getJsLocaleMessage('employee','employee_alert_79')+"<%=sysuser!=null? depNam:""%>");
					    depEle.attr("selected",true);
					}else{
						$("#selectDepBtn_user").hide();
					}
					var employeeIds = '<%=employeeIds==null?"":employeeIds%>';
				if(employeeIds!=""){
					var depEle = $("#employPerType option[value='2']");
				    depEle.attr("text",getJsLocaleMessage('employee','employee_alert_79')+"<%=employeeNames%>");
				    depEle.attr("selected",true);
				    $("#domDepId_employ").val(employeeIds);
				    $("#selectDepBtn_employ").show();
				} 
				var clientIds = '<%=clientIds==null?"":clientIds%>';
				if(clientIds!=""){
					var depEle = $("#clientPerType option[value='2']");
				    depEle.attr("text",getJsLocaleMessage('employee','employee_alert_79')+"<%=clientNames%>");
				    depEle.attr("selected",true);
                    $("#selectDepBtn_client").parent().show();
				    $("#selectDepBtn_client").show();
				    $("#domDepId_client").val(clientIds);
				} 
				}
			<% 
				}
			%>
			$("#dropMenu").blur(function(){hideMenu();})
			
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
				$('#cName').keypress(function(e) {
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
				$('#cName').blur(function(e) {
					$(this).val($(this).val().replaceAll("<","").replaceAll(">",""));
				});
				
				CloseTreePlugIN();
				initzTreeuser();
		});
	
		</script>
	</body>
</html>
