<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@page import="com.montnets.emp.entity.sysuser.LfSysuser"%>
<%@page import="com.montnets.emp.entity.sysuser.LfReviewer2level"%>
<%@page import="com.montnets.emp.common.biz.SysuserBiz"%>
<%@page import="com.montnets.emp.entity.sysuser.LfDep"%>
<%@page import="com.montnets.emp.entity.sysuser.LfRoles"%>
<%@page import="com.montnets.emp.common.constant.ViewParams"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@page import="com.montnets.emp.common.constant.CommonVariables"%>
<%@page import="com.montnets.emp.entity.pasroute.LfSpDepBind"%>
<%@page import="com.montnets.emp.entity.pasgroup.Userdata"%>
<%@page import="com.montnets.emp.common.vo.LfSysuserVo"%>
<%@page import="com.montnets.emp.i18n.util.MessageUtils"%>
<%@ taglib prefix="emp"
	uri="http://www.montnets.com/emp/i18n/tags/simple"%>
 <%
	String path = request.getContextPath();
    String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
    String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
    String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
    
	//Jsp页面中获取session中的语言设置
 	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
    //LfSysuser sysuser=((LfSysuser)session.getAttribute("loginSysuser"));
    // Long curId = sysuser.getUserId();
    LfSysuser currUser = (LfSysuser)request.getAttribute("currUser");
    Long curId = currUser.getUserId();
	Long userId=Long.valueOf(request.getParameter("userId"));
	LfSysuserVo sysVo= (LfSysuserVo) request.getAttribute("sysuser");
	@SuppressWarnings("unchecked")
	List<LfReviewer2level> lrl = (List<LfReviewer2level>) request.getAttribute("lrl");
	SysuserBiz sysBiz=new SysuserBiz();
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("sysuser");
	Integer isExistSubno = (Integer)request.getAttribute("isExistSubno");
	String usedSubno = (String)request.getAttribute("usedSubno");
	Integer subnoDigit = (Integer)request.getAttribute("subnoDigit");
	Integer temp = isExistSubno;
	
	LfDep lfdep = (LfDep)request.getAttribute("lfdep");
	String depname = "";
	if(lfdep!=null)
	{
	    depname= lfdep.getDepName();
	}
	PageInfo pageInfo = (PageInfo) session.getAttribute("lastPageInfo");
	@SuppressWarnings("unchecked")
	List<LfRoles> roleList = (List<LfRoles>) session.getAttribute("roleList");
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
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
	//String sysuserCode = ViewParams.SYSUSERCODE;
	String sysuserCode = MessageUtils.extractMessage("user","user_xtgl_czygl_text_133",request);
	String loginId = MessageUtils.extractMessage("user","user_xtgl_czygl_text_132",request);
	
    String skin = session.getAttribute("stlyeSkin")==null?"default":
		(String)session.getAttribute("stlyeSkin");
 %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<title></title>
		<%@include file="/common/common.jsp" %>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=empBasePath %>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=empBasePath %>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=empBasePath %>/common/css/reading.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=empBasePath %>/common/css/floating.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=empBasePath %>/common/widget/zTreeStyle/zTreeStyle.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css"/>
		<link rel="stylesheet" href="<%=empBasePath %>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>">
		<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
		<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=empBasePath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
			<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
		
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/opt_useSysuser.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/user.css?V=<%=StaticValue.getJspImpVersion() %>"/>

	</head>
	<body id="opt_useSysuser" onload="show()">
	<input type="hidden" id="showZhezhao" value="false"/>
	<input type="hidden" id="pathUrl" value="<%=path %>" />
	<input type="hidden" id="guid" name="guid" value="<%=sysVo.getGuId() %>" />
		<div id="container" class="container">
			<%-- header开始 --%>
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode,"启用操作员   ") %>
			<%-- header结束 --%>
			<%-- 内容开始 --%>
			<div id="rContent" class="rContent rContent2"  >
			<%              		
						if(btnMap.get(menuCode+"-3")!=null)                       		
						{                        	
					%>
					<div class="titletop">
						<table class="titletop_table">
							<tr>
								<td class="titletop_td">
									<emp:message key="user_xtgl_czygl_text_71" 
										defVal="启用操作员" fileName="user" />
								</td>
								<td align="right">
									<span class="titletop_font fhsyj_span"  onclick="javascript:back()">&larr;&nbsp;<emp:message key="user_xtgl_czygl_text_3" 
										defVal="返回上一级" fileName="user" /></span>
								</td>
							</tr>
						</table>
					</div>
					<div id="table_input" class="table_input">
						<form action="<%=path %>/opt_sysuser.htm?method=useUser" method="post" id="Sysuser" name="Sysuser" >
						<div id="loginUser" class="hidden"></div>
							<input type="hidden" id="inheritPath" value="<%=inheritPath %>"/>
						<table  id="sysTable" class="sysTable">
						<tr>
							<td class="widthTD"><%=loginId %>：</td>
							<td>
								<label>
									<input type="text" class="input_bd" name="username" id="userNo" onblur="checkUser(1,1)" value="<%=sysVo.getUserName()%>" disabled="disabled" title="<%=sysVo.getUserName()%>" maxlength="20"/><font class="font_red">&nbsp;*</font>
 									<input type="hidden" id="userId" name="userId"  value="<%=userId %>" />
									<input type="hidden" name="username"  value="<%=sysVo.getUserName() %>" />
								</label>
							</td>
						</tr>
						<tr>
							<td><%=sysuserCode %>：</td>
							<td>
								<label>
									<input type="text" class="input_bd" name="userCode" id="userCode" readonly="readonly" disabled="disabled" value='<%=sysVo.getUserCode()%>' /><font class="font_red">&nbsp;*</font>
								</label>
							</td>
						</tr>
						<tr>
							<%--<td height="29px" align="right">登录密码：</td>
							<td>
								<label>
									<input type="password" name="newPwd" id="newPwd" readonly="readonly"  style="background-color:#E8E8E8;"/><font style="color: red;">&nbsp;*</font>
									<%if(btnMap.get(menuCode+"-5")!=null)                       		
										{ %>
										<input type="button" id="rePwd" value="重置密码" style="width:55px;cursor:pointer" onclick="javascript:resetPwd($(this).val());"/>
										<%} %> 				
									
								</label>
							</td>
							--%>
							<td><emp:message key="user_xtgl_czygl_text_90" 
										defVal="操作员名称" fileName="user" /></td>
							<td>
								<label>
									<input type="text" class="input_bd" name="name" id="name" onblur="checkData(1)"  onkeyup="this.value=this.value.replace(/[']+/img,'')" value="<%=sysVo.getName() %>" maxlength="12"/><font class="font_red">&nbsp;*</font>
								</label>
							</td>
						</tr>
						
						<tr>
							<td><emp:message key="user_xtgl_czygl_text_9" 
										defVal="性别：" fileName="user" /></td>
							<td>
								<select name="sex" class="input_bd">
									<option value="1"><emp:message key="user_xtgl_czygl_text_10" 
										defVal="男" fileName="user" /></option>
									<option value="0"<%=sysVo.getSex()-0==0?"selected":"" %>><emp:message key="user_xtgl_czygl_text_11" 
										defVal="女" fileName="user" /></option>
								</select><font class="font_red">&nbsp;*</font>
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
								 <input type="hidden" name="mobile" id="mobile" value="<%=sysVo.getMobile()!=null?sysVo.getMobile():"" %>"/>
									<input type="text" class="input_bd" name="tempMobile" id="tempMobile" onblur="checkData(1)" value="<%=mobile %>" maxlength="11"
									onkeyup= "if(value != value.replace(/[^0-9]+/g,'')) value = value.replace(/[^0-9]+/g,'')"
									 onpaste="return !clipboardData.getData('text').match(/[^0-9]+/g)"/><font class="font_red">&nbsp;*</font>
								</label>
							</td>
						</tr>
						
						<tr>
							<td>	<emp:message key="user_xtgl_czygl_text_42" 
										defVal="座机：" fileName="user" /></td>
							<td>
								<label>
								 	<input type="text" class="input_bd" name="oph" id="oph" onblur="checkData(1)" value="<%=sysVo.getOph()!=null?sysVo.getOph():"" %>"/>
								</label>
							</td>
						</tr>
						
						
						<tr>
							<td >E-mail：</td>
							<td>
								<label>
									<input type="text" class="input_bd" name="EMail" id="EMail" onblur="checkData(1)" value="<%=sysVo.getEMail()!=null?sysVo.getEMail():"" %>"/>
								</label>
							</td>
						</tr>
						
						<tr>
						<%--
						    <td height="29px" align="right">机构负责人：</td>
						    <td>
						   
						    是
						   
						    否
						    </td> --%>
						     <td>QQ：</td>
						     <td>
						     	<label>
									<input type="text" class="input_bd" name="qq" id="qq" onblur="checkData(1)" 	value="<%=sysVo.getQq()!=null?sysVo.getQq():"" %>" />
								</label>
							</td>
						</tr>
						
						<tr>
							<td colspan="2" class="fengexian">
								<div class="leftDiv"></div>
								<div class="conCent"><emp:message key="user_xtgl_czygl_text_129" 
										defVal="以下选项必填" fileName="user" />  </div>
								<div class="rightDiv"></div>
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
								<input id="roleName" class="input_bd roleName" onclick="javascript:openRoleChoose();"  name="roleName" type="text" 
								readonly value='<%=showName %>'  allRoleName="<%=name %>"  />
								<div id="allRoleName"  class="allRoleName allRoleName2"></div>
<%--									<input type="button" value="角色" class="button" onclick="javascript:openRoleChoose()" />--%>
								</label>
								<input type="hidden" id="cheRoles" name="cheRoles" value="" />
							</td>
						</tr>
						
						<tr>
							<td ><emp:message key="user_xtgl_czygl_text_92" 
										defVal="所属机构：" fileName="user" /></td>
							<td>
								
								<div  class="lfdep_div">
									<label>
								   <%if(lfdep != null){ %>
								     <input type="hidden" name="oldDepId" id="oldDepId" value="<%=sysVo.getDepId() %>"/>
							    	<input type="hidden" name="depId" id="depId" value="0"/>
								     <input id="depNam" class="input_bd treeInput depNam" onclick="javascript:showMenu();"  name="depNam" type="text" readonly value='<emp:message key="user_xtgl_czygl_text_130" 
										defVal="请重新选择机构" fileName="user" />'   />
								   <%}else{ %>
								     <input type="hidden" name="oldDepId" id="oldDepId" value="<%=sysVo.getDepId() %>"/>
								     <input type="hidden" name="depId" id="depId" value="<%=sysVo.getDepId() %>"/>
									<input id="depNam" class="input_bd treeInput depNam" onclick="javascript:showMenu();"  name="depNam" type="text" readonly value='<%=sysVo.getDepName() %>'  />
									<%} %>
									</label>
									<a id="ssdep" onclick="javascript:showMenu();" class="ssdep"></a>
								</div>
								<div id="dropMenu" class="dropMenu">
<%--									<div id='closeDepTreeClass' class='closeDepTreeClassHover'></div>--%>
									<ul id="dropdownMenu" class="tree dropdownMenu"  >
									</ul>
								</div>
							</td>
						</tr>
						
						<tr>
							<td><emp:message key="user_xtgl_czygl_text_116" 
										defVal="管辖范围：" fileName="user" /></td>
							<td>
								<%String depNam="";
												String depId="";
												if(sysVo.getDomDepList()!=null && sysVo.getDomDepList().size()>0)
												{
													LfDep dep = (LfDep)(sysVo.getDomDepList().get(0));
													depNam = dep.getDepName();
													depId=dep.getDepId().toString();
													}
													%>
								<input type="hidden" name="domDepId" id="domString" value="<%=depId %>"/>
								<select id="permissionType" class="input_bd" name="permissionType" onchange="changePermType();" >
								    <option value="1"><emp:message key="user_xtgl_czygl_text_129" 
										defVal="个人" fileName="user" /></option>
								<%
								    LfSysuser lfSysuser = (LfSysuser)(request.getSession(false)!=null?request.getSession(false).getAttribute("loginSysuser"):null);
								    int permissionType =lfSysuser!=null?lfSysuser.getPermissionType():0;
									if(permissionType == 2){
									   out.print("<option id=\"depOption\" value=\"2\">" + MessageUtils.extractMessage("user","user_xtgl_czygl_text_30",request)+ "</option>");
									}
								 %>
								   
								</select>
								<span id="depPerm"   class="depPerm">
										<input id="domdepName" name="domdepName" type="hidden" readonly value='<%="".equals(depNam)? MessageUtils.extractMessage("user","user_xtgl_czygl_text_80",request):depNam %>' class="domdepName"/>
										&nbsp;<a id="selectDepBtn" onclick="javascript:showMenu2();" class="selectDepBtn">
										<font color="blue"><emp:message key="user_xtgl_czygl_text_15" 
										defVal="请选择" fileName="user" /></font></a>
										</span>
										<div id="dropMenu2" class="dropMenu2">
<%--											<div id='closeDepTreeClass' class='closeDepTreeClassHover'></div>--%>
											<ul id="dropdownMenu2" class="tree dropdownMenu2"  >
											</ul>
										</div>
							</td>
						</tr>
						
						<tr>
								<%
								    boolean isSetFlow = true;
								    if (lrl == null || lrl.size() == 0)
								    {
								    	isSetFlow = false;
								    }
								%>
								<%
								    String flowType = StaticValue.FLOW_TYPE;
								    if ("1".equals(flowType))
								    {
								%>
										<td><emp:message key="user_xtgl_czygl_text_117" 
										defVal="设置审批流：" fileName="user" /></td>
										<td><input name="isCheckFlow" id="isCheckFlowY" <%=isSetFlow?"checked='checked'":"" %> onclick="javascript:$('#isFlow').show()" type="radio" value="y" />
												   <emp:message key="user_xtgl_czygl_text_34" 
										defVal="是" fileName="user" />    &nbsp;&nbsp;&nbsp;&nbsp; <input name="isCheckFlow" id="isCheckFlowN" type="radio" <%=isSetFlow?"":"checked='checked'" %> value="n" onclick="javascript:$('#isFlow').hide()"/>
											             <emp:message key="user_xtgl_czygl_text_33" 
										defVal="否" fileName="user" /> &nbsp;<input type="button" id="isFlow" value="<emp:message key="user_xtgl_czygl_text_131" 
										defVal="审批流信息：" fileName="user" />" onclick="javascript:shFlow()"  class="btnClass3 isFlow"/>
															<input type="hidden" name="bindOpeNum" id="bindOpeNum" value=""/>
										</td>
								<%} %>
							</tr>
							
						
						<tr>
							<td><emp:message key="user_xtgl_czygl_text_118" 
										defVal="是否分配固定尾号：" fileName="user" /></td>
							<td>
									<input type="checkbox"  class="isNeedSubno"  name="isNeedSubno" id="isNeedSubno"  <%=isExistSubno+0==1?"checked":"" %> onclick="javascript:updateHaveSubno('<%=isExistSubno%>','<%=usedSubno%>');"/>
									<%--<input type="text" id="updateSubno" name="updateSubno" value="<%=usedSubno%>"  <%=isExistSubno+0==1?"style='display: block;width:80px;'":"style='display: none;width:80px;'" %>/> --%>
									<% 
										if(isExistSubno == 1){
									%>
										  <%--当操作员分配尾号的时候   进行的INPUT 可供修改 --%>
										<input type="text" id="updateSubno" name="updateSubno" size="<%=subnoDigit %>" maxlength="<%=subnoDigit %>" value="<%=usedSubno!=null?usedSubno:"" %>" onkeyup="value=value.replace(/[^\d]/g,'')"/>
										  <%--当操作员分配尾号的时候  分配的尾号 --%>
										<input type="hidden" id="subno" name="subno" value="<%=usedSubno!=null?usedSubno:"" %>"/>
									<%
										}
									 %>
									  <%--当操作员没有分配尾号的时候   进行的INPUT  --%>
									 <input type="text" id="addSubno" name="addSubno" size="<%=subnoDigit %>" maxlength="<%=subnoDigit%>" value="" class="addSubno" onkeyup="value=value.replace(/[^\d]/g,'')"/>
									  <%--当操作员没有分配尾号的时候  分配的尾号 --%>
									 <input type="hidden" id="subno2" name="subno2" value=""/>
									  <%--当分配过了的操作员，1是判断是否有修改     2是进行回收        当没分配过的操作员      1是进行新增和修改    2是进行回收--%>
									 <input type="hidden" id="haveSubno" name="haveSubno" value="<%=isExistSubno+0==1?"1":"2" %>"/>
									 <%--判断该操作员是否分配了操作员固定尾号 --%>
									 <input type="hidden" id="isGiveSubno" name="isGiveSubno" value="<%=isExistSubno+0==1?"1":"2" %>"/>
							</td>
						</tr>
						
						<tr>
							<%--<td height="29px" align="right">确定密码：</td>
							<td>
								<label>
									<input type="password" id="newPwd2" readonly="readonly"  style="background-color:#E8E8E8;"/><font style="color: red;">&nbsp;*</font>
								</label>
							</td>
							--%>
							<td><emp:message key="user_xtgl_czygl_text_119" 
										defVal="状态：" fileName="user" /></td>
							<td><%
									if(!(curId-1 != 0 || curId-userId!=0))
									{
									%>
										<input type="hidden" name="userState" id="userState" value="<%=sysVo.getUserState() %>"/>
									<%out.print(sysVo.getUserState()-0==0?MessageUtils.extractMessage("user","user_xtgl_czygl_text_18",request):MessageUtils.extractMessage("user","user_xtgl_czygl_text_19",request));
									}else
									{
									%>
										<select class="input_bd" name="userState" id="userState" style="">
											<option value="1"><emp:message key="user_xtgl_czygl_text_18" 
										defVal="启用" fileName="user" /></option>
											<option value="0" <%=sysVo.getUserState()-0==0?"selected":"" %>><emp:message key="user_xtgl_czygl_text_19" 
										defVal="禁用" fileName="user" /></option>
										</select>
									<%
									} 
									%>
							</td>
						</tr>
						   
						<tr>
							<td colspan="4" height="29px" align="right"><p id="zhu"></p></td>
						</tr>
						<tr>
							<td colspan="4" align="right">
								<div class="buttonDiv">
								<%if(userId - curId != 0 && userId - 1 != 0 && !(StaticValue.getCORPTYPE() ==0 && sysVo.getUserName().equals("admin"))) { %>
									<input type="button" value="<emp:message key="common_btn_7" 
										defVal="确定" fileName="common" />" id="button" onclick="checkData(2)" class="btnClass5 mr23"/>
									<%} %>
<%--									<input type="button" value="返回"  onclick="javascript:back();" id="button2" class="btnClass1"/>--%>
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
												<div id="roleDiv" class="roleDiv">
												<%
												for (int i = 0; i < roleList.size(); i++){
													LfRoles role = roleList.get(i);
												%>
													<input type="checkbox" name="roleId" id="roleId:<%=role.getRoleId() %>" value="<%=role.getRoleId() %>" <%=roleMap.get(role.getRoleId())!=null?"checked":"" %> />
													<label for="roleId:<%=role.getRoleId() %>"><%=role.getRoleName().replace("<","&lt;").replace(">","&gt;")%></label><br/>
												<%}
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
															<input type="button" name="button2" id="button2" value="<emp:message key="common_btn_10" 
										defVal="返回" fileName="common" />" 
																onclick="javascript:back()"/>
														</div>
											</center>
										</div>
								</form>
							</div>
							<%--选择账户--%>
							<div
								class="accDiv"
								id="accDiv">
								<div id="acc_choose">
								<div id="dTitle">
									<h6 id="c_a_ph3">
										<emp:message key="user_xtgl_czygl_text_120" 
										defVal="选择账户" fileName="user" />
									</h6>
									</div>
									<div><table>
										<thead>
										<tr>
											<th class="bangd_th"><emp:message key="user_xtgl_czygl_text_121" 
										defVal="绑定" fileName="user" /></th>
											<th class="fszh_th"><emp:message key="user_xtgl_czygl_text_122" 
										defVal="发送账号" fileName="user" /></th>
											<th><emp:message key="user_xtgl_czygl_text_123" 
										defVal="账户名称" fileName="user" /></th>
										</tr>
										</thead>
										</table>
									</div>
									<div class="isBind_div">
									<table>
										<%
										@SuppressWarnings("unchecked")
										List<Userdata> userList = (List<Userdata>) session.getAttribute("routeUserdatas");
					           			@SuppressWarnings("unchecked")
										List<LfSpDepBind> lsdList = (List<LfSpDepBind>) session.getAttribute("lsdList");
										if (userList != null)
										{
											for (Userdata ud : userList)
											{
												boolean isBind = false;
												for (LfSpDepBind lsdb : lsdList)
												{
													if (ud.getUserId().equals(lsdb.getSpUser()))
													{
														isBind = true;
													}
												}
										%>
										<tr>
											<td class="isAccNum_td">
											<input type="checkbox" name="isAccNum" value="<%=ud.getUserId() %>" <%=isBind?"checked='checked'":"" %>/></td>
											<td class="getUserId_td"><%=ud.getUserId() %></td>
											<td><%=ud.getStaffName() %></td>
										</tr>
										<%
											}
										}
										%>
									</table>
									</div>
									<div id="btnclick">
										<input type="button" value="<emp:message key="common_btn_7" 
											defVal="确定" fileName="common" />" id="btnTrue2"  onclick="javascript:accOk();" class="btnClass1" />
										<input type="button" value="<emp:message key="common_btn_10" 
											defVal="返回" fileName="common" />"	onclick="javascript:closeShow('accDiv');" id="btnExti" class="btnClass1"/> 
									</div>
								</div>
							</div>
							<%--审批流程--%>
 								<div id="flowDiv">
								<div id="flow_choose">
									<div>
									<%
									    String infos = "";
									    String infostr = "";
									    if (lrl != null && lrl.size() > 0)
									    {
									    	for (LfReviewer2level lr : lrl)
									    	{
									    		infos = infos + "<option value=\'"+lr.getUserId()+"\'>"+sysBiz.getSysuserVoByUserId(lr.getUserId()).getName()+"</option>";
									    	    infostr = infostr + lr.getUserId() +",";
									    	}
									    }
									%>
									<input type="hidden" id="inits" value="<%=infos %>"/>
									<input type="hidden" id="infostr" value="<%=infostr %>"/>
									<jsp:include page="opt_sysuserTree.jsp"/>
									</div>
								</div>
							</div>
							<div class="clear"></div>

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
    <div class="clear"></div>
    	<script language="javascript" src="<%=empBasePath %>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"
			type="text/javascript"></script>
		<script type="text/javascript" src="<%=empBasePath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=empBasePath%>/common/i18n/<%=langName%>/user_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=empBasePath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>	
        <script type="text/javascript" src="<%=empBasePath %>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=empBasePath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=empBasePath %>/common/js/jquery.ztree-myjquery-b.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=empBasePath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script language="javascript" src="<%=iPath %>/js/sysuser.js?V=<%=StaticValue.getJspImpVersion() %>" type="text/javascript"></script>
		<script src="<%=empBasePath%>/common/js/floating.js?V=<%=StaticValue.getJspImpVersion() %>" type="text/javascript"></script>
		<script language="javascript" src="<%=iPath %>/js/useSysuser.js?V=<%=StaticValue.getJspImpVersion() %>" type="text/javascript"></script>
		
		<script>
	
		$(document).ready(function(){	
			var permissionType = <%=sysVo.getPermissionType()%>;
			if(permissionType == 2){
			    var depEle = $("#permissionType option[value='"+<%=sysVo.getPermissionType()%>+"']");
			    depEle.attr("text",getJsLocaleMessage("user","user_xtgl_czygl_text_9")+$("#domdepName").val());
			    depEle.attr("selected",true);
			}else{
				$("#selectDepBtn").hide();
			}
			$("#roleName").myFloating({
				divId: "allRoleName",
				align: "right",
				paddingSize: 7,
				beforeShow: function(){
					var roleName = $("#roleName").attr("allRoleName");
				    if(roleName == ""){
				    	<%
				    	String rolename = "";
						if (roleList != null){
							for (int i = 0; i < roleList.size(); i++){
								LfRoles role = roleList.get(i);
								rolename += role.getRoleName().replaceAll("<","&lt;").replaceAll(">","&gt;")+"<br/>";
							}
						}
						%>
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
			<% String result=(String)request.getAttribute("result");
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
				{
					%>
					location.href="<%=path%>/opt_sysuser.htm?method=find&lguserid="+$("#lguserid").val()+
						"&lgcorpcode="+$("#lgcorpcode").val()+"<%=str%>"+"&sOpName="+encodeURI(encodeURI("<%=sOpName%>"))
						+"&depNameStr="+encodeURI(encodeURI("<%=depNameStr%>"));
					<%
				}
			%>
		}
		
			
		function checkDep()
		{
		   var depnames ='<%=depname%>';

		    if(depnames !=null && depnames !="" && depnames.length>0)
		    {
		       alert(depnames+getJsLocaleMessage("user","user_xtgl_czygl_text_153"));
		    }			  
		}

		 function back(){
		    var url = '<%=path%>/opt_sysuser.htm';
			var conditionUrl = "";
			if(url.indexOf("?")>-1)
			{
				conditionUrl="&";
			}else
			{
				conditionUrl="?";
			}
			conditionUrl = conditionUrl +backfind("#loginUser");
			location.href=url+conditionUrl+"<%=str%>"+"&sOpName="+encodeURI(encodeURI("<%=sOpName%>"))
						+"&depNameStr="+encodeURI(encodeURI("<%=depNameStr%>"));
		}
	
		</script>	
	</body>
</html>
