<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.HashMap" %>
<%@page import="com.montnets.emp.entity.sysuser.LfRoles"%>
<%@page import="com.montnets.emp.entity.sysuser.LfSysuser"%>
<%@page import="com.montnets.emp.common.constant.CommonVariables"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@page import="com.montnets.emp.util.PageInfo"%>
<%@page import="com.montnets.emp.common.constant.ViewParams"%>
<%@page import="com.montnets.emp.entity.sysuser.LfDep"%>
<%@page import="com.montnets.emp.common.vo.LfSysuserVo"%>
<%@page import="com.montnets.emp.employee.vo.LfEmployeeTypeVo"%>
<%@page import="com.montnets.emp.entity.employee.LfEmployee"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.montnets.emp.common.biz.SmsBiz"%>
<%@page import="com.montnets.emp.i18n.util.MessageUtils"%>
<%@ taglib prefix="emp"
	uri="http://www.montnets.com/emp/i18n/tags/simple"%>
 <%
 	@ SuppressWarnings("unchecked")
   	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
   	String path = request.getContextPath();
     String iPath = request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
 	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
 	inheritPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
 	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
       //当前登录操作员
       LfSysuser sysuser=((LfSysuser)session.getAttribute("loginSysuser"));
        Long curId = sysuser.getUserId();
   	Long userId=Long.valueOf(request.getParameter("userId"));
   	LfSysuserVo sysVo= (LfSysuserVo) request.getAttribute("sysuser");
   	@ SuppressWarnings("unchecked")
   	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
   	String menuCode = titleMap.get("sysuser");
   	
	//Jsp页面中获取session中的语言设置
 	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
   	//是否存在尾号
   	Integer isExistSubno = (Integer)request.getAttribute("isExistSubno");
   	String usedSubno = (String)request.getAttribute("usedSubno");
   	Integer subnoDigit = (Integer)request.getAttribute("subnoDigit");
   	PageInfo pageInfo = (PageInfo) session.getAttribute("lastPageInfo");
   	@SuppressWarnings("unchecked")
   	List<LfRoles> roleList = (List<LfRoles>) session.getAttribute("roleList");
   	@ SuppressWarnings("unchecked")
	List<LfEmployeeTypeVo> zwList = (List<LfEmployeeTypeVo>)request.getAttribute("zwList");
   	@ SuppressWarnings("unchecked")
   	Map<String,String> conMap= (Map<String,String>)session.getAttribute("lastConMap");
   	String sOprId = conMap.get("sOprId")==null?"":conMap.get("sOprId");
   	String sOpName = conMap.get("sOpName")==null?"":conMap.get("sOpName");
   	String depName = conMap.get("depName")==null?"":conMap.get("depName");
   	String roleName = conMap.get("roleName")==null?"":conMap.get("roleName");
   	String subno = conMap.get("isSubno")==null?"":conMap.get("isSubno");
   	String userState = conMap.get("userState")==null?"":conMap.get("userState");
   	String depNameStr = conMap.get("depNameStr") == null?"":conMap.get("depNameStr");
   	int pageIndex = pageInfo.getPageIndex();
   	int pageSize = pageInfo.getPageSize();
       String str = "&sOprId="+sOprId+
       "&depName="+depName+"&roleName="+roleName+
       "&subno="+subno+"&userState="+userState
       +"&pageIndex="+pageIndex+"&pageSize="+pageSize;
       
   	//String loginId = ViewParams.LOGINID;
   //	String sysuserCode = ViewParams.SYSUSERCODE;
   	String sysuserCode = MessageUtils.extractMessage("user","user_xtgl_czygl_text_133",request);
	String loginId = MessageUtils.extractMessage("user","user_xtgl_czygl_text_132",request);
   	
   	LfEmployee employee = (LfEmployee)request.getAttribute("employee");
   	String employeeDep = (String)request.getAttribute("employeeDep");
   	
   	String employeeIds = (String)request.getAttribute("employeeIds");
   	String employeeNames = (String)request.getAttribute("employeeNames");
   	String clientIds = (String)request.getAttribute("clientIds");
   	String clientNames = (String)request.getAttribute("clientNames");
   	//新增 充值回收权限 pengj
   	String balancePriIds = (String)request.getAttribute("balancePriIds");
   	String balancePriNames = (String)request.getAttribute("balancePriNames");
   	//end
   	
   	String optype = (String)request.getAttribute("optype");
   	LfSysuser lfsysuser = (LfSysuser)request.getAttribute("lfsysuser");

	//   1是开启审核范围开关    2是一个都没有开启
	String switchFlag = (String)request.getAttribute("switchFlag");
   	
   	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
   	String skin = session.getAttribute("stlyeSkin")==null?"default":
   		(String)session.getAttribute("stlyeSkin");
   		 %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<title></title>
		<%@include file="/common/common.jsp" %>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=inheritPath%>/common/css/reading.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=inheritPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=inheritPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=inheritPath%>/common/css/floating.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=inheritPath%>/common/widget/zTreeStyle/zTreeStyle.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css"/>
		<link rel="stylesheet" href="<%=inheritPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>">
		<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
		<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=empBasePath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
			<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
		
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/opt_editSysuser.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/user.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		
	</head>
	<body id="opt_editSysuser" onload="show()">
	<input type="hidden" id="showZhezhao" value="false"/>
	<input type="hidden" id="pathUrl" value="<%=path%>" />
	<input type="hidden" id="guid" name="guid" value="<%=sysVo.getGuId()%>" />
		<div id="container" class="container">
			<%-- header开始 --%>
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode,"修改操作员   ") %>
			<%-- header结束 --%>
			<%-- 内容开始 --%>
			<div id="rContent" class="rContent rContent2"  >
			<%
				if(btnMap.get(menuCode+"-3")!=null)                       		
					{
			%>
			<div class="titletop">
				<table  class="xgczuy_table">
					<tr>
						<td class="titletop_td">
						<%if("update".equals(optype)){%>
							<emp:message key="user_xtgl_czygl_text_70" 
										defVal="修改操作员" fileName="user" />
						<% }else{%>
							<emp:message key="user_xtgl_czygl_text_71" 
										defVal="启用操作员" fileName="user" />
						<%} %>
						</td>
						<td align="right">
							<span class="titletop_font"  onclick="javascript:doreturn()"><emp:message key="user_xtgl_czygl_text_3" 
										defVal="返回上一级" fileName="user" /></span>
						</td>
					</tr>
				</table>
			</div>
			<div id="table_input" class="table_input">
				<form action="opt_sysuser.htm?method=update" method="post" id="Sysuser" name="Sysuser" >
					<div id="loginUser" class="hidden"></div>
					<input type="hidden" id="inheritPath" value="<%=inheritPath%>"/>
				<input type="hidden" name="optype" id="optype" value="<%=optype %>">
				<input type="hidden" name="keyId" id="keyId" value="<%=sysVo.getKeyId()%>">
				<div class="tabyys czyjbxx_div"  ><emp:message key="user_xtgl_czygl_text_4" defVal="操作员基本信息" fileName="user" /></div>
			    <div id="lyout" class="div_bd lyout2">
			  		<table  id="sysTable" width="100%" height="100%" style="width: 870px;">
			  				<tr>
								<td  class="loginId_td"><%=loginId%>：</td>
								<td  class="userNo_td">
									<label>
										<input class="input_bd" type="text" name="username" id="userNo" 
										 value="<%=sysVo.getUserName()%>" disabled="disabled" 
										title="<%=sysVo.getUserName()%>" maxlength="20"/><font class="font_red">&nbsp;&nbsp;*</font>
	 									<input type="hidden" id="userId" name="userId"  value="<%=userId%>" />
										<input type="hidden" name="username"  value="<%=sysVo.getUserName()%>" />
									</label>
								</td>
								<td  class="czymc_td"  align="left"><emp:message key="user_xtgl_czygl_text_6" 
										defVal="操作员名称：" fileName="user" /></td>
								<td >
									<label>
										<input class="input_bd" type="text" name="name" id="name" onblur="checkData(1)" 
										value='<%=sysVo.getName()==null?"":sysVo.getName()%>'  
										onkeyup="this.value=this.value.replace(/[']+/img,'')" maxlength="60"/>
										<font class="font_red">&nbsp;&nbsp;*</font>
									</label>
								</td>
							</tr>
							<tr>	
								<td><%=sysuserCode%>：</td>
								<td>
									<label>
										<input type="text" class="input_bd" name="userCode" id="userCode" 
										readonly="readonly" disabled="disabled" value='<%=sysVo.getUserCode()%>' />
										<font class="font_red">&nbsp;&nbsp;*</font>
									</label>
								</td>
								<td><emp:message key="user_xtgl_czygl_text_7" 
										defVal="所属机构：" fileName="user" /></td>
								<td class="oldDepId_td">
								<input type="hidden" name="oldDepId" id="oldDepId" value="<%=sysVo.getDepId()%>"/>
									<input type="hidden" name="depId" id="depId" value="<%=sysVo.getDepId()%>"/>
									<div  class="depNam_div">
										<input id="depNam" class="input_bd fontColor treeInput depNam" onclick="javascript:showMenu();"  
										name="depNam" type="text" readonly value='<%=sysVo.getDepName()%>'  />
										<font class="font_red">&nbsp;*</font><a id="ssdep" onclick="javascript:showMenu();" 
										class="ssdep" ></a>
									</div>
									<div id="dropMenu_udep" class="dropMenu_udep">
<%--										<div id='closeDepTreeClass' class='closeDepTreeClassHover'></div>--%>
										<ul id="dropdownMenu_udep" class="tree dropdownMenu_udep"  >
										</ul>
										
									</div>
								</td>
							</tr>
							
							<tr>
								<td><emp:message key="user_xtgl_czygl_text_9" 
										defVal="性别：" fileName="user" /></td>
								<td>
									<select name="sex" class="input_bd">
										<option value="1"><emp:message key="user_xtgl_czygl_text_10" 
										defVal="男" fileName="user" /></option>
										<option value="0"<%=sysVo.getSex()-0==0?"selected":""%>><emp:message key="user_xtgl_czygl_text_11" 
										defVal="女" fileName="user" /></option>
									</select>
								</td>
								<td><emp:message key="user_xtgl_czygl_text_12" 
										defVal="生日：" fileName="user" /></td>
							    <td>
							    	<label>
							    	<input type="text" id="birthday" name="birthday"   
							    	class="Wdate input_bd birthday" readonly="readonly" 
							    	onclick="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'%yyyy-%MM-%dd'})"
							    	 value="<%=sysVo.getBirthday()!=null&&!"".equals(sysVo.getBirthday())?sdf.format(sysVo.getBirthday()):"" %>" class="input_book">
									</label>
								</td>
							</tr>
							<tr>
								<td><emp:message key="user_xtgl_czygl_text_13" 
										defVal="手机：" fileName="user" /></td>
								<td>
									<label>	
									<%
											String mobile = sysVo.getMobile()!=null?sysVo.getMobile():"";
												if(btnMap.get(StaticValue.PHONE_LOOK_CODE) == null && !"".equals(mobile)){
													//无号码的查看权限，需替换手机号码的星号
													mobile = new CommonVariables().replacePhoneNumber(mobile);
												}
										%>
									  <input type="hidden" name="ishidephone" id="ishidephone" value="<%=mobile%>"/>
									 <input type="hidden" name="mobile" id="mobile" value="<%=sysVo.getMobile()!=null?sysVo.getMobile():""%>"/>
										<input type="text" class="input_bd" name="tempMobile" id="tempMobile" onblur="checkData(1)" value="<%=mobile%>" maxlength="21"
										onkeyup= "phoneInputCtrl($(this));" onfocus="getFocus()"/>
										 <font class="font_red">&nbsp;&nbsp;*</font>
									</label>
								</td>
								 <td><emp:message key="user_xtgl_czygl_text_14" 
										defVal="职位：" fileName="user" /></td>
								<td >
									<select  name="job" id="job" class="input_bd">
							        		<option value=""><emp:message key="user_xtgl_czygl_text_15" 
										defVal="请选择" fileName="user" /></option>
							        		<% 
							        			if(zwList != null && zwList.size()>0){
							        				LfEmployeeTypeVo zw = null;
							        				for(int i=0;i<zwList.size();i++){
							        					zw = zwList.get(i);
							        		%>
							        			<option value="<%=zw.getName() %>"
							        			<%if(zw.getName().equals(sysVo.getDuties())){%> selected="selected"<%} %>><%=zw.getName().replace("<","&lt;").replace(">","&gt;")%></option>
							        		<% 
							        				}
							        			}
							        		%>
							        </select>
								</td>
							</tr>
							<tr>
								<td><emp:message key="user_xtgl_czygl_text_16" 
										defVal="分配角色：" fileName="user" /></td>
								<td>
									<label style="">
									<%
										@ SuppressWarnings("unchecked")
										List<LfRoles> sysroleList = sysVo.getRoleList();
										String name = "";
										String showName = "";
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
											showName = MessageUtils.extractMessage("user","user_xtgl_czygl_text_72",request);
										}
									%>
									<input id="roleName"  class="input_bd roleName" onclick="javascript:openRoleChoose();"  
									name="roleName" type="text"  readonly value='<%=showName%>'  allRoleName="<%=name%>"  />
									<div id="allRoleName"  class="allRoleName allRoleName"></div>
									</label>
									<input type="hidden" id="cheRoles" name="cheRoles" value="" />
								</td>
								<td><emp:message key="user_xtgl_czygl_text_99" 
										defVal="状态：" fileName="user" /></td>
								<td><%
									if(!(curId-1 != 0 || curId-userId!=0)){
									%>
										<input type="hidden" name="userState" id="userState" value="<%=sysVo.getUserState()%>"/>
										<%out.print(sysVo.getUserState()-0==0?MessageUtils.extractMessage("user","user_xtgl_czygl_text_19",request):MessageUtils.extractMessage("user","user_xtgl_czygl_text_18",request));
									}else{%>
										<select class="input_bd" name="userState" id="userState" >
											<option value="1"><emp:message key="user_xtgl_czygl_text_18" 
										defVal="启用" fileName="user" /></option>
											<option value="0" <%=sysVo.getUserState()-0==0?"selected":""%>><emp:message key="user_xtgl_czygl_text_19" 
										defVal="禁用" fileName="user" /></option>
										</select>
									<%}%>
								</td>
							</tr>
							<tr>
								<td><emp:message key="user_xtgl_czygl_text_20" 
										defVal="设置审核流程：" fileName="user" /></td>
								<td>
									<select name="isaudited" class="input_bd"
											<%if("2".equals(switchFlag)){%>
												disabled="disabled"  title="<emp:message key="user_xtgl_czygl_text_32" 
										defVal="系统未开启审核功能" fileName="user" />"
											<%} %>
										>
										<option value="1"><emp:message key="user_xtgl_czygl_text_22" 
										defVal="必审" fileName="user" /></option>
										<option value="2"  <%if(lfsysuser != null && lfsysuser.getIsAudited() ==2){%> selected="selected"<%} %>><emp:message key="user_xtgl_czygl_text_21" 
										defVal="免审" fileName="user" /></option>
									</select>
									<%if("1".equals(switchFlag)){%>
										&nbsp;<a href="javascript:locationAudMgr('1350-1220');"><emp:message key="common_btn_21" 
										defVal="设置" fileName="common" /></a>
									<%} %>
								</td>
						
								<%
								if(sysVo.getUserState() == 1){
								%>
							
								<td><emp:message key="user_xtgl_czygl_text_23" 
										defVal="固定尾号：" fileName="user" /></td>
								<td align="left">
										&nbsp;<input type="checkbox"  class="isNeedSubno"  name="isNeedSubno" id="isNeedSubno"  <%=isExistSubno+0==1?"checked":""%> onclick="javascript:updateHaveSubno('<%=isExistSubno%>','<%=usedSubno%>');"/>
										<%
											if(isExistSubno == 1){
										%>
											  <%--当操作员分配尾号的时候   进行的INPUT 可供修改 --%>
											<input type="text" id="updateSubno" name="updateSubno" size="<%=subnoDigit%>" maxlength="<%=subnoDigit%>" value="<%=usedSubno!=null?usedSubno:""%>" onkeyup="value=value.replace(/[^\d]/g,'')"/>
											  <%--当操作员分配尾号的时候  分配的尾号 --%>
											<input type="hidden" id="subno" name="subno" value="<%=usedSubno!=null?usedSubno:""%>"/>
										<%
											}
										%>
										  <%--当操作员没有分配尾号的时候   进行的INPUT  --%>
										 <input type="text" id="addSubno" name="addSubno" size="<%=subnoDigit%>" maxlength="<%=subnoDigit%>" value="" class="addSubno" onkeyup="value=value.replace(/[^\d]/g,'')"/>
										  <%--当操作员没有分配尾号的时候  分配的尾号 --%>
										 <input type="hidden" id="subno2" name="subno2" value=""/>
										  <%--当分配过了的操作员，1是判断是否有修改     2是进行回收        当没分配过的操作员      1是进行新增和修改    2是进行回收--%>
										 <input type="hidden" id="haveSubno" name="haveSubno" value="<%=isExistSubno+0==1?"1":"2"%>"/>
										 <%--判断该操作员是否分配了操作员固定尾号 --%>
										 <input type="hidden" id="isGiveSubno" name="isGiveSubno" value="<%=isExistSubno+0==1?"1":"2"%>"/>
								</td>
							<%
								}else{
							%>
								<td colspan="2"> &nbsp;</td>
							<%
								}
							%>
						</tr>
							<tr>
								<td align="left">
										<emp:message key="user_xtgl_czygl_text_24" 
										defVal="是否接收审批提醒：" fileName="user" />
								</td>
								<% 
								String check="";
								String flag=lfsysuser.getControlFlag();
								if(flag!=null&&flag.length()>1){
								String bit=flag.substring(0,1);
									if("1".equals(bit)){
										check="true";
									}
								}
								
								%>
								<td>&nbsp;<input type="checkbox" id="msgRemind" name="msgRemind"  class="msgRemind"  <%=check.equals("true") ?"checked":""%> />&nbsp;
								<emp:message key="user_xtgl_czygl_text_25" 
										defVal="短信" fileName="user" />
								
								<% 
								String emailCheck="";
								String emailFlag=lfsysuser.getControlFlag();
								if(emailFlag!=null&&emailFlag.length()>2){
								String emailBit=emailFlag.substring(1,2);
									if("1".equals(emailBit)){
										emailCheck="true";
									}
								}
								%>
								&nbsp;&nbsp;<input type="checkbox" id="emailRemind" name="emailRemind"  class="emailRemind"  <%=emailCheck.equals("true") ?"checked":""%> />&nbsp;<emp:message key="user_xtgl_czygl_text_26" 
										defVal="邮箱" fileName="user" /></td>
								<%if(new SmsBiz().isWyModule("20")){ %>
								<td align="left"><emp:message key="user_xtgl_czygl_text_27" 
										defVal="是否为客服人员：" fileName="user" /></td>
								<td>&nbsp;<input type="checkbox" id="isCustome" name="isCustome"  class="isCustome" <%if(lfsysuser.getIsCustome()!=null&&lfsysuser.getIsCustome()==1){out.print("checked='checked'");} %> /></td>
								<%} %>
								</tr>
			  		</table>
			    </div>		
			    
			    <div  class="tabyys <%=StaticValue.ZH_HK.equals(langName)?"user_div1":"user_div2"%>"  ><emp:message key="user_xtgl_czygl_text_28" 
										defVal="数据权限" fileName="user" /><img src="<%=inheritPath%>/common/img/down.gif"></div>
			    <div id="lyout" class="div_bd lyout2"  >
			  		<table  id="sysTable" width="100%" height="100%" style="width: 870px;">	
			  			<tr>
							<td class="czysjqx_td"><emp:message key="user_xtgl_czygl_text_29" 
										defVal="操作员数据权限：" fileName="user" /></td>
							<td class="getDepName_td">
							<%
							String depNam="";
							String depId="";
							if(sysVo.getDomDepList()!=null && sysVo.getDomDepList().size()>0)
							{
								LfDep dep = (LfDep)(sysVo.getDomDepList().get(0));
								depNam = dep.getDepName();
								depId=dep.getDepId().toString();
								}
								%>
								<input type="hidden" name="domDepId" id="domDepId" value="<%=depId%>"/>
								<select id="userPerType" class="input_bd" name="userPerType" onchange="changeUserPermType();" >
								    <option value="1"><emp:message key="user_xtgl_czygl_text_124" 
										defVal="个人" fileName="user" /></option>
								<%
									LfSysuser lfSysuser = (LfSysuser)(request.getSession(false)!=null?request.getSession(false).getAttribute("loginSysuser"):null);
								    int permissionType = lfsysuser!=null?lfSysuser.getPermissionType():0;
									if(permissionType == 2){
									   out.print("<option id=\"depOption\" value=\"2\">" + MessageUtils.extractMessage("user","user_xtgl_czygl_text_30",request)+ "</option>");
									}
								%>
								</select>
								<span id="depPerm"   class="depPerm">
								<input id="domdepName" name="domdepName" class="input_bd domdepName" type="hidden" readonly 
								value='<%="".equals(depNam)?MessageUtils.extractMessage("user","user_xtgl_czygl_text_80",request):depNam%>'  />
								&nbsp;<a id="selectDepBtn_user" onclick="javascript:showUserMenu();" 
								class="selectDepBtn_user"><font color="blue"><emp:message key="user_xtgl_czygl_text_15" 
										defVal="请选择" fileName="user" /></font></a>
								</span>
								<div id="dropMenu_user" class="dropMenu_user">
<%--									<div id='closeDepTreeClass' class='closeDepTreeClassHover'></div>--%>
									<ul id="dropdownMenu_user" class="tree dropdownMenu_user"  >
									</ul>
								</div>
							</td>
							<td class="jgshy_td"><emp:message key="user_xtgl_czygl_text_31" 
										defVal="机构审核员：" fileName="user" /></td>
							<td>
								<select name="isReviewer" id="isReviewer" class="input_bd" 
									<%if("2".equals(switchFlag)){%>
											disabled="disabled"  title="<emp:message key="user_xtgl_czygl_text_32" 
										defVal="系统未开启审核功能" fileName="user" />"
									<%} %>
								>
									<option value="2"  <%if(sysVo.getIsReviewer() ==2){%> selected="selected"<%} %>><emp:message key="user_xtgl_czygl_text_33" 
										defVal="否" fileName="user" /></option>
									<option value="1" <%if(sysVo.getIsReviewer() ==1){%> selected="selected"<%} %>><emp:message key="user_xtgl_czygl_text_34"
										defVal="是" fileName="user" /></option>
									
								</select>
							</td>
						</tr>
						<tr>
							<td class="ygtxlqx_td">	<emp:message key="user_xtgl_czygl_text_35" 
										defVal="员工通讯录权限：" fileName="user" /></td>
							<td class="domDepId_employ_td">
								<input type="hidden" name="domDepId_employ" id="domDepId_employ"/>
								<select class="input_bd" id="employPerType" name="employPerType" 
									onchange="changeEmployPermType();"  >
						   		 	<option value="1"><emp:message key="user_xtgl_czygl_text_124" 
										defVal="个人" fileName="user" /></option>
						    		<option id="depOption_employ" value="2"><emp:message key="user_xtgl_czygl_text_30" 
										defVal="机构" fileName="user" /></option>
								</select>
								<span id="depPerm_employ"  class="depPerm_employ">
									<input id="domdepName_employ" name="domdepName_employ" type="hidden" value='' 
									class="domdepName_employ"/>
									&nbsp;<a id="selectDepBtn_employ" onclick="javascript:showEmployMenu();" 
									class="selectDepBtn_employ" ><font color="blue"><emp:message key="user_xtgl_czygl_text_15" 
										defVal="请选择" fileName="user" /></font></a>
								</span>
								<div id="dropMenu_employ" class="dropMenu_employ">
									<div class="dropMenu_employ_down_div">
										<input type="button" value="<emp:message key="common_btn_7" 
										defVal="确定" fileName="common" />" class="btnClass1" onclick="javascript:zTreeOnClickOK_employ();"  />&nbsp;&nbsp;
										<input type="button" value="<emp:message key="common_btn_20" 
										defVal="清空" fileName="common" />" class="btnClass1" onclick="javascript:cleanSelect_employ();"  />
									</div>	
									<ul  id="dropdownMenu_employ" class="tree dropdownMenu_employ"  ></ul>	
								</div>		
							</td>
							<td class="khtxlqx_td"><emp:message key="user_xtgl_czygl_text_38" 
										defVal="客户通讯录权限：" fileName="user" /></td>
							<td  class="domDepId_client_td">
								<input type="hidden" name="domDepId_client" id="domDepId_client"/>
								<select class="input_bd" id="clientPerType" name="clientPerType" onchange="changeClientPermType();" >
						   		 	<option value="1"><emp:message key="user_xtgl_czygl_text_124" 
										defVal="个人" fileName="user" /></option>
						    		<option id="depOption_client" value="2"><emp:message key="user_xtgl_czygl_text_30" 
										defVal="机构" fileName="user" /></option>
								</select>
								<span id="depPerm_client"  class="depPerm_client">
									<input id="domdepName_client" name="domdepName_client" type="hidden" value='' 
									class="domdepName_client"/>
									&nbsp;<a id="selectDepBtn_client" onclick="javascript:showClientMenu();" 
									class="selectDepBtn_client" ><font color="blue"><emp:message key="user_xtgl_czygl_text_15" 
										defVal="请选择" fileName="user" /></font></a>
								</span>
								<div id="dropMenu_client" class="dropMenu_client">
									<div class="dropMenu_client_down_div">
										<input type="button" value="<emp:message key="common_btn_7" 
										defVal="确定" fileName="common" />" class="btnClass1" onclick="javascript:zTreeOnClickOK_client();"  />&nbsp;&nbsp;
										<input type="button" value="<emp:message key="common_btn_20" 
										defVal="清空" fileName="common" />" class="btnClass1" onclick="javascript:cleanSelect_client();"  />
									</div>	
									<ul id="dropdownMenu_client" class="tree dropdownMenu_client"  >
									</ul>
								</div>
							</td>
						</tr>
						
						<%-- EMP5.7新需求：增加对操作员充值和回收权限   by pengj --%>
								<tr>
									<td>
										<emp:message key="user_xtgl_czygl_text_39" 
										defVal="充值回收权限：" fileName="user" />
									</td>
									<td class="domDepId_dep_td">
											<input type="hidden" name="domDepId_dep" id="domDepId_dep"/>
											<select class="input_bd" id="depPerType" name="depPerType"
												onchange="changeDepPermType();"  >
												 
									   		 	<option value="1"><emp:message key="user_xtgl_czygl_text_40" 
										defVal="操作员所属机构" fileName="user" /></option>
									   		 	 
									    		<option id="depOption_dep" value="2"><emp:message key="user_xtgl_czygl_text_30" 
										defVal="机构" fileName="user" /></option>
									    		
											</select>
											<span id="depPerm_dep"  class="depPerm_dep">
												<input id="domdepName_dep" name="domdepName_dep" type="hidden" value='' 
													class="domdepName_dep"/>
												&nbsp;<a id="selectDepBtn_dep" onclick="javascript:showDepMenu();" 
												class="selectDepBtn_dep" >
												<font color="blue"><emp:message key="user_xtgl_czygl_text_15" 
										defVal="请选择" fileName="user" /></font></a>
											</span>
											<div id="dropMenu_dep" class="dropMenu_dep">
												<div class="dropMenu_dep_down_div">
													<input type="button" value="<emp:message key="common_btn_7" 
										defVal="确定" fileName="common" />" class="btnClass1" onclick="javascript:zTreeOnClickOK_dep();"  />&nbsp;&nbsp;
													<input type="button" value="<emp:message key="common_btn_20" 
										defVal="清空" fileName="common" />" class="btnClass1" onclick="javascript:cleanSelect_dep();"  />
												</div>	
												<ul  id="dropdownMenu_dep" class="tree dropdownMenu_dep" ></ul>	
											</div>		
									</td>
									<td>
										<emp:message key="user_xtgl_czygl_text_136"
													 defVal="下行内容查看权限：" fileName="user"/>
									</td>
									<td class="domDepId_dep_td">
										<input name="showNum" type="radio" value="0" <%=sysVo.getShowNum()-0==0?"checked":""%> />全部可见
										&nbsp;&nbsp;&nbsp;&nbsp;
										<input name="showNum" type="radio" value="1" <%=sysVo.getShowNum()-0==1?"checked":""%> />数字不可见
									</td>
									
								</tr>
								<%-- end --%>
						
			  		</table>
			    </div>		
			    
			    
			    <div  class="tabyys <%=StaticValue.ZH_HK.equals(langName)?"qtlxfs_div1":"qtlxfs_div2"%>" ><emp:message key="user_xtgl_czygl_text_41" 
										defVal="其他联系方式" fileName="user" /><img src="<%=inheritPath%>/common/img/down.gif"></div>
			    <div id="lyout" class="div_bd lyout2" >
			  		<table  id="sysTable" width="100%" height="100%" style="width: 870px;">	
			  			<tr>
							<td class="zj_td"><emp:message key="user_xtgl_czygl_text_42" 
										defVal="座机：" fileName="user" /></td>
							<td class="oph_td">
								<label>
								 	<input type="text" class="input_bd" name="oph" id="oph" onblur="checkData(1)" value="<%=sysVo.getOph()!=null?sysVo.getOph():""%>"   maxlength="18"/>
								</label>
							</td>
							<td  class="mail_td">E-mail：</td>
							<td>
								<label>
									<input type="text" class="input_bd" name="EMail" id="EMail" onblur="checkData(1)" value="<%=sysVo.getEMail()!=null?sysVo.getEMail():""%>"   maxlength="32"/>
								</label><font id="editEmailCheck" class="editEmailCheck">&nbsp;*</font>
							</td>
						</tr>
						
						<tr>
						     <td class="QQmh_td">QQ：</td>
						     <td class="qq_td">
						     	<label>
									<input type="text" class="input_bd" name="qq" id="qq" onblur="checkData(1)" 	value="<%=sysVo.getQq()!=null?sysVo.getQq():""%>"   maxlength="16"/>
								</label>
							</td>
							<td  class="MSNmh_td" >MSN：</td>
						    <td>
						    	<label>
						    		<input type="text" class="input_bd" name="msn" id="msn" onblur="checkData(1)" value='<%=sysVo.getMsn()!=null?sysVo.getMsn():"" %>'   maxlength="32"/>
								</label>
							</td>
						</tr>
						<tr > 
						    <td  class="chuanz_td"><emp:message key="user_xtgl_czygl_text_43" 
										defVal="传真：" fileName="user" /></td>
						    <td  class="fax_td">
						    	<label>
						    		<input type="text" class="input_bd" name="fax" id="fax" onblur="checkData(1)" value='<%=sysVo.getFax()!=null?sysVo.getFax():"" %>'   maxlength="18"/>
								</label>
							</td>
						</tr>
			  		</table>
			    </div>		
			    
			    <div  class="tabyys <%=StaticValue.ZH_HK.equals(langName)?"jrygtxl_div1":"jrygtxl_div2"%>"  ><emp:message key="user_xtgl_czygl_text_44" 
										defVal="加入员工通讯录" fileName="user" /><img src="<%=inheritPath%>/common/img/down.gif" style="position: absolute;padding-top: 9px;"> </div>
			    <div id="lyout" class="div_bd lyout2"   >
			  		<table  id="sysTable" width="100%" height="100%" style="width: 870px;">	
			  			<tr>
			  				<td colspan="4">&nbsp;
								<input name="isEmploy" type="radio" value="0"  checked="checked" 
									onclick="javascript:$('#isJoinEmploy').hide()"  /> <emp:message key="user_xtgl_czygl_text_45" 
										defVal="不加入" fileName="user" />
								  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								 <input name="isEmploy"  onclick="javascript:$('#isJoinEmploy').show()" type="radio" value="1" />
								<emp:message key="user_xtgl_czygl_text_46" 
										defVal="加入" fileName="user" /> &nbsp;
							</td>
			  			</tr>
			  			<tr id="isJoinEmploy" class="isJoinEmploy">
							<td  class="gongh_td"><emp:message key="user_xtgl_czygl_text_47" 
										defVal="工号：" fileName="user" /></td>
							<td  class="employeeNo_td">
							<label>
									<input  type="text" name="employeeNo" id="employeeNo" 
									value="<%=employee==null?"":employee.getEmployeeNo() %>"
									size="10" <%if(StaticValue.getCORPTYPE() ==1){out.print("class='input_bd employeeNo'");}else{out.print("class='input_bd'");}%>
									<%if(employee!=null&&employee.getEmployeeNo()!=null&&!"".equals(employee.getEmployeeNo().trim())){%> disabled="disabled" <%} %>
									 maxlength="20" />
									 <input type="hidden" id="oremployeeNo" value="<%=employee==null?"":employee.getEmployeeNo() %>"/>
							</label>
							</td>
							<td class="ygjg_td"><emp:message key="user_xtgl_czygl_text_48" 
										defVal="员工机构：" fileName="user" /></td>
							<td>
								<input type="hidden" name="employDepId" id="employDepId" value="<%=employee!=null?employee.getDepId():"" %>"/>
								<div  class="employDepNam_div">
									<input id="employDepNam" class="input_bd fontColor treeInput employDepNam" onclick="javascript:showMenuEdep();"  name="employDepNam" type="text" readonly value='<%=employeeDep!=null && !"".equals(employeeDep)?employeeDep:"点击选择机构" %>'  />
									<a id="ssdep" onclick="javascript:showMenuEdep();" class="ssdep" ></a>
								</div>
								<div id="dropMenu_edep" class="dropMenu_edep">
<%--									<div id='closeDepTreeClass' class='closeDepTreeClassHover'></div>--%>
									<ul id="dropdownMenu_edep" class="tree dropdownMenu_edep"  >
									</ul>
								</div>
							</td>
						</tr>
						<tr class="beiz_tr">
							<td><emp:message key="user_xtgl_czygl_text_50" 
										defVal="备注：" fileName="user" /></td>
							<td><textarea name="comments"></textarea> 
							</td>
						</tr>
			  		</table>
			    </div>		
				<table id="" class="tr_table">
						<tr id="hint-tr"  >
							<td id="hint" ><p id="zhu"></p></td>
						</tr>
						<tr>
						   
							<td align="center">
								<div class="buttonDiv hint-tr_div"  >
								<%
									if(userId - curId != 0 && userId - 1 != 0 && !(StaticValue.getCORPTYPE() ==0 && sysVo.getUserName().equals("admin"))) {
								%>
									<input type="button" value="<emp:message key="common_btn_7" 
										defVal="确定" fileName="common" />" id="button" onclick="checkData(2)" class="btnClass5 mr23"/>
									<input type="button" name="button" id="button" value="<emp:message key="common_btn_10" 
										defVal="返回" fileName="common" />" onclick="javascript:doreturn()" class="btnClass6"/>
									<br/>
									<%
										}
									%>
								</div>
							</td>
						</tr>
				</table>
					<iframe id="tif"></iframe>
					<%-- 弹出添加角色 overflow-y:auto;border: 1px solid #cccccc;--%>
						<div id="roDiv" class="roDiv">
							<div class="roDiv_down_div">
								<%
									Map<Long,Object> roleMap=new HashMap<Long,Object>();
									for(int r=0;r<sysVo.getRoleList().size();r++){
										LfRoles role=(LfRoles)sysVo.getRoleList().get(r);
										roleMap.put(role.getRoleId() ,role);
									}
									if (roleList != null){
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
									for (int r=0;r<sysVo.getRoleList().size();r++){
										LfRoles role=(LfRoles)sysVo.getRoleList().get(r);
										if(roleMap.get(role.getRoleId()) == null){
								%>
									<input type="checkbox" disabled="disabled" name="roleId" id="roleId:<%=role.getRoleId() %>" value="<%=role.getRoleId() %>" checked />
									<label for="roleId:<%=role.getRoleId() %>"><%=role.getRoleName().replace("<","&lt;").replace(">","&gt;")%></label><br/>
								<%}
								}
								%>
								</div>
								<%
									}
								%>
							</div>
							<center>
								<div style="" id="" class="roleBut">
									<input type="button" class="btnClass5 mr23" value="<emp:message key="common_btn_7" 
										defVal="确定" fileName="common" />" onclick="dorole()"/>
									<%--<input type="button" class="btnClass6" value="返回" onclick="javascript:doreturn()"/>  --%>
								</div>
							</center>
						</div>
				</form>
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
		</div>
		<iframe id="ifr" class="ifr"></iframe>
    <div class="clear"></div>
    <script language="javascript" src="<%=inheritPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"
			type="text/javascript"></script>
		<script type="text/javascript" src="<%=empBasePath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=empBasePath%>/common/i18n/<%=langName%>/user_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=empBasePath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
        <script type="text/javascript" src="<%=inheritPath%>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=inheritPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=inheritPath%>/common/js/jquery.ztree-myjquery-b.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=inheritPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=inheritPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script language="javascript" src="<%=iPath%>/js/sysuser.js?V=<%=StaticValue.getJspImpVersion() %>" type="text/javascript"></script>
		<script language="javascript" src="<%=iPath%>/js/dataPerm.js?V=<%=StaticValue.getJspImpVersion() %>" type="text/javascript"></script>
		<script src="<%=inheritPath%>/common/js/floating.js?V=<%=StaticValue.getJspImpVersion() %>" type="text/javascript"></script>
    
    <script>
     $(function(){
      $('.tabyys').each(function(i){
       
       $(this).bind('click',function(){
       if(i==0){return false;}
        var _src=$(this).find('img').attr('src');
        if($(this).next().is(":visible")){
          _src=_src.replace('down.gif','up.gif');
          $(this).addClass('mt10').find('img').attr('src',_src);
          
        }else{
        _src=_src.replace('up.gif','down.gif');
        $(this).removeClass('mt10').find('img').attr('src',_src);
        }
        $(this).next().toggle();
        
      })
     })
     });
		$(document).ready(function(){
			getLoginInfo("#loginUser");
			reloadTree();
			//修复下拉框双箭头的问题;
            $(".input_bd").find("div").hide();
			var permissionType = <%=sysVo.getPermissionType()%>;
			if(permissionType == 2){
			    var depEle = $("#userPerType option[value='"+<%=sysVo.getPermissionType()%>+"']");
			    depEle.attr("text",getJsLocaleMessage("user","user_xtgl_czygl_text_9")+$("#domdepName").val());
			    depEle.attr("selected",true);
                $("#selectDepBtn_user").parent().show();
                // $("#selectDepBtn_user").show();
			}else{
				$("#selectDepBtn_user").hide();
			}
			var employeeIds = '<%=employeeIds==null?"":employeeIds%>';
			if(employeeIds!=""){
				var depEle = $("#employPerType option[value='2']");
			    depEle.attr("text",getJsLocaleMessage("user","user_xtgl_czygl_text_9")+"<%=employeeNames%>");
			    depEle.attr("selected",true);
			    $("#domDepId_employ").val(employeeIds);
			    $("#selectDepBtn_employ").show();
			} 
			var clientIds = '<%=clientIds==null?"":clientIds%>';
			if(clientIds!=""){
				var depEle = $("#clientPerType option[value='2']");
			    depEle.attr("text",getJsLocaleMessage("user","user_xtgl_czygl_text_9")+"<%=clientNames%>");
			    depEle.attr("selected",true);
			    $("#selectDepBtn_client").parent().show();
			    $("#selectDepBtn_client").show();
			    $("#domDepId_client").val(clientIds);
			} 
//新增 充值回收权限 pengj
			var balancePriIds = '<%=balancePriIds==null?"":balancePriIds%>';
			if(balancePriIds!=""){
				var depEle = $("#depPerType option[value='2']");
			    depEle.attr("text",getJsLocaleMessage("user","user_xtgl_czygl_text_9")+"<%=balancePriNames%>");
			    depEle.attr("selected",true);
			    $("#selectDepBtn_dep").show();
			    $("#domDepId_dep").val(balancePriIds);
			} 
//end
			
			var userType = <%=sysVo.getUserType() %>;
			if(userType == 2 ){
				$("input[name='isEmploy']").get(1).checked = true; 
				$('#isJoinEmploy').show();
			}
			
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
			$('#name').keypress(function(e) {
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
			$('#name').blur(function(e) {
				
				$(this).val($(this).val().replaceAll("<","").replaceAll(">",""));
			});
			closeTreeFunSel(["dropMenu_udep","dropMenu_edep"]);
			closeTreeFunOnSpc(["dropMenu_employ","dropMenu_client","dropMenu_user"]);

			$("#roleName").myFloating({
				divId: "allRoleName",
				align: "right",
				paddingSize: 7,
				beforeShow: function(){
					var roleName = $("#roleName").attr("allRoleName");
				    if(roleName == ""){
				    	<%String rolename = "";
						if (roleList != null){
							for (int i = 0; i < roleList.size(); i++){
								LfRoles role = roleList.get(i);
								rolename += role.getRoleName().replaceAll("<","&lt;").replaceAll(">","&gt;")+"<br/>";
							}
						}%>
						$("#allRoleName").html(getJsLocaleMessage("user","user_xtgl_czygl_text_145"));
				    }else{
				        var name = roleName.split(",");
				        var str = "";
				    	for(var i=0;i<name.length;i++){
				    		str += name[i].replaceAll("<","&lt;").replaceAll(">","&gt;")+"<br/>";
				    	}
				    	$("#allRoleName").html(str);
				    }
				}
			});
		});

		
		function show()
		{
			<%String result=(String)request.getAttribute("result");
				if(result!=null && result.equals("true")){%>
					alert(getJsLocaleMessage("user","user_xtgl_czygl_text_25"));
				<%}else if(result!=null && result.equals("false")){%>
					alert(getJsLocaleMessage("user","user_xtgl_czygl_text_26"));
				<%}else if(result!=null && result.equals("subnoRepeat")){%>
					alert(getJsLocaleMessage("user","user_xtgl_czygl_text_157"));
				<%}else if(result!=null && result.equals("errer")){%>
					alert(getJsLocaleMessage("user","user_xtgl_czygl_text_26"));
				<%}
				if(result !=null && !result.equals("subnoRepeat"))
				{%>
					location.href="<%=path%>/opt_sysuser.htm?method=find&returnBySubPage=true&lguserid="+$("#lguserid").val()+
						"&lgcorpcode="+$("#lgcorpcode").val()+"<%=str%>"+"&sOpName="+encodeURI(encodeURI("<%=sOpName%>"))
						+"&depNameStr="+encodeURI(encodeURI("<%=depNameStr%>"));
					<%}%>
		}
			//这里是点击CHECKBOX 是否需要固定尾号
			//isHave  是判断它是否分配尾号   1 是有   2是无
			//oldSubno  分配的尾号
			function updateHaveSubno(isHave,oldSubno){
				 var isneedSubno = "2";		
				 var guid = $("#guid").val();
			  	//如果点击了选中 
				if(document.getElementsByName('isNeedSubno')[0].checked==true){	
					isneedSubno = "1";
				}
				//这里是判断下  在该操作员有绑定固定尾号情况下，选择是否需要回收还是修改尾号
			 	if(isHave == "1"){
			 		if(isneedSubno == "1"){
			 			$("#updateSubno").val(oldSubno);
			 			$("#updateSubno").css("visibility","visible");
			 		}else if(isneedSubno == "2"){
			 			$("#updateSubno").val("");
			 			$("#updateSubno").css("visibility","hidden");
			 		}
			 		$("#haveSubno").val(isneedSubno);
			 	}else{					// if(isHave == "2" || isHave == "0" )
			 		//这里是判断下  在该操作员没有绑定固定尾号情况下，选择是否需要绑定尾号
			 		if(isneedSubno == "1"){
			 			$.post("opt_sysuser.htm?method=getSingleSubno",{guid:guid,lgcorpcode:$("#lgcorpcode").val()},function(msg){
							if("" == msg){
			 					alert(getJsLocaleMessage("user","user_xtgl_czygl_text_11"));
			 					document.getElementsByName('isNeedSubno')[0].checked = false;
			 					return;
			 				}else if("notsubno" == msg){
			 					alert(getJsLocaleMessage("user","user_xtgl_czygl_text_11"));
			 					document.getElementsByName('isNeedSubno')[0].checked = false;
			 					return;
			 				}else if("enough" == msg){
			 					alert(getJsLocaleMessage("user","user_xtgl_czygl_text_12"));
			 					document.getElementsByName('isNeedSubno')[0].checked = false;
			 					return;
			 				}else{
			 					$("#addSubno").val(msg);
								$("#subno2").val(msg);
								$("#addSubno").show();
			 				}
						});
			 		}else if(isneedSubno == "2"){
			 			$.post("opt_sysuser.htm?method=delSingleSubno",{guid:guid,lgcorpcode:$("#lgcorpcode").val()},function(msg){
							if(msg == "1"){
								$("#addSubno").val("");
								$("#addSubno").hide();
							}else{
								$("#zhu").html(getJsLocaleMessage("user","user_xtgl_czygl_text_13"));
							}
						});
			 		}
			 		$("#haveSubno").val(isneedSubno);
			 	}
			}
			function back(){
					location.href="<%=path%>/opt_sysuser.htm?method=find&lguserid="+$("#lguserid").val()+
						"&lgcorpcode="+$("#lgcorpcode").val()+"<%=str%>"+"&sOpName="+encodeURI(encodeURI("<%=sOpName%>"))
						+"&depNameStr="+encodeURI(encodeURI("<%=depNameStr%>"));
					
					
		}
		<%-- 处理获得焦点事件--%>
		function getFocus(){
			$("#tempMobile").val("");
		}


		<%--跳转审核管理界面--%>
		function locationAudMgr(menuCode){
			var lguserid = $("#lguserid").val();
			<%
				if(StaticValue.getInniMenuMap().containsKey("/aud_auditpro.htm"))
				{
				
			%>
					window.parent.openNewTab(menuCode,"<%=path%>/aud_auditpro.htm?method=find&lguserid="+lguserid);
			<%
				}else{
			%>
					alert(getJsLocaleMessage("user","user_xtgl_czygl_text_150"));
					return;
			<%
				}
			%>
		}
		
		</script>
	</body>
</html>
