<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.common.constant.ViewParams" %>
<%@ page import="com.montnets.emp.common.vo.LfSysuserVo" %>
<%@ page import="com.montnets.emp.entity.sysuser.LfDep" %>
<%@ page import="com.montnets.emp.entity.sysuser.LfRoles" %>
<%@page import="com.montnets.emp.entity.sysuser.LfSysuser"%>
<%@page import="com.montnets.emp.i18n.util.MessageUtils"%>
<%@page import="com.montnets.emp.util.PageInfo" %>
<%@page import="java.util.LinkedHashMap" %>
<%@page import="java.util.List" %>
<%@page import="java.util.Map" %>
<%@ taglib prefix="emp"
           uri="http://www.montnets.com/emp/i18n/tags/simple" %>
<%
	String path=request.getContextPath();
	String iPath = request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	inheritPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	PageInfo pageInfo = (PageInfo) request.getAttribute("pageInfo");
	
	//Jsp页面中获取session中的语言设置
 	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
	//按钮权限
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	@ SuppressWarnings("unchecked")
	LinkedHashMap<String, String> conditionMap = (LinkedHashMap<String, String>)request.getAttribute("conditionMap");
	@ SuppressWarnings("unchecked")
	List<LfSysuserVo> sysVoList=(List<LfSysuserVo>)request.getAttribute("userList");
	String recordflag = "norecord";
	if(sysVoList != null && sysVoList.size()>0){
		recordflag = "haverecord";
	}
	String depId=conditionMap.get("depName");
	//当前登录操作员
	LfSysuser sysuser=((LfSysuser)session.getAttribute("loginSysuser"));
    Long curId = sysuser.getUserId();
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("sysuser");
	//修改时修改操作员后，返回最后一次查看的页面
	session.setAttribute("lastPageInfo",pageInfo);
	session.setAttribute("lastConMap",conditionMap);
	String findResult= (String)request.getAttribute("findresult");
	   
	//String loginId = ViewParams.LOGINID;
	String loginId = MessageUtils.extractMessage("user","user_xtgl_czygl_text_132",request);
	//String sysuserCode = ViewParams.SYSUSERCODE;
	String sysuserCode = MessageUtils.extractMessage("user","user_xtgl_czygl_text_133",request);
	@ SuppressWarnings("unchecked")
	List<LfRoles> roleList = (List<LfRoles>) request.getAttribute("roleList");
	
	String skin = session.getAttribute("stlyeSkin")==null?"default":
		(String)session.getAttribute("stlyeSkin");
	String isAll = conditionMap.get("isAll");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<title><%=titleMap.get(menuCode)%></title>
		<%@include file="/common/common.jsp" %>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=inheritPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=inheritPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=inheritPath%>/common/css/empSelect.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/empSelect.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" type="text/css" href="<%=inheritPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" href="<%=inheritPath%>/common/widget/zTreeStyle/zTreeStyle.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css"/>
		<link rel="stylesheet" href="<%=inheritPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
		
		<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=empBasePath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
			<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
		
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/opt_sysuser.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/user.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		
	</head>
	<body id="opt_sysuser">
	<input type="hidden" id="pathUrl" value="<%=path%>" />
	<input type="hidden" id="inheritPath" value="<%=inheritPath%>"/>
		<div id="container" class="container">
			<%-- header开始 --%>
			<%=ViewParams.getPosition(langName,menuCode) %>
			<div id="roleNamesdialogtemp" class="roleNamesdialogtemp">
				<div id="msg" class="msg"></div>
			</div>
			<%-- header结束 --%>
			<%-- 内容开始 --%>
			<div id="rContent" class="rContent">
			<%
				if(btnMap.get(menuCode+"-0")!=null)                       		
							{
			%>
					<form name="pageForm" action="<%=path%>/opt_sysuser.htm" method="post" id="pageForm">
						<div id="loginUser" class="hidden"></div>
						
							<div class="buttons">
								<div id="toggleDiv">
						         </div>
								<%-- <a id="flowInfo">查看审批信息</a> --%>
								<%
									if(btnMap.get(menuCode+"-1")!=null){
								%>
									<a id="add" onclick="javascript:toAdd()"><emp:message key="common_btn_4" 
										defVal="新建" fileName="common" /></a>
								<%
									}
								%>
								<%-- 
								<%
									if(btnMap.get(menuCode+"-4")!=null){
								%>
									<a id="toEmployee" onclick="javascript:toEmployee()">转员工</a>
								<%
									}
								%>
								--%>
								<a id="upload" onclick="javascript:toImport()"><emp:message key="user_xtgl_czygl_text_137" 
										defVal="导入" fileName="user" /></a>
								
								<a id="exportCondition" ><emp:message key="user_xtgl_czygl_text_77" 
										defVal="导出" fileName="user" /></a>
							</div>
							<div id="condition">
								<table>
									<tr>
									<td><%=loginId%>：</td>
										<td>
											<input type="text" name="sOprId" id="sOprId" class="sOprId" value="<%=conditionMap.get("sOprId") == null?"":conditionMap.get("sOprId")%>" maxlength="20"/>
										</td>
										<td><emp:message key="user_xtgl_czygl_text_6" 
										defVal="操作员名称：" fileName="user" /></td>
										<td>
											<input type="text" name="sOpName" id="sOpName" class="sOpName" value="<%=conditionMap.get("sOpName") == null?"":conditionMap.get("sOpName")%>" maxlength="20"/>
										</td>
										<td><emp:message key="user_xtgl_czygl_text_78" 
										defVal="机构：" fileName="user" /></td>
											<td class="condi_f_l">
												<input type="hidden" name="depName" id="depName"
												    	 value="<%=conditionMap.get("depName") == null?"":conditionMap.get("depName")%>"/>
												<div  class="sortSel_div">
												<input id="sortSel" onclick="javascript:showMenu();"  class="treeInput sortSel"  name="depNameStr" type="text" readonly value='<%=(depId == null || "".equals(depId))?MessageUtils.extractMessage("user","user_xtgl_czygl_text_80",request):conditionMap.get("depNameStr")%>'  />
												&nbsp;
													<a onclick="javascript:showMenu();" class="sortSel_a"></a> 
												</div>
												<div id="dropMenu" class="dropMenu">
													 <div class="isAll_div"><input type="checkbox" id="isAll" <%if(isAll!=null&&"1".equals(isAll)){ %>checked="checked"<%} %> name="isAll" class="isAll_input" style="position: relative; top: 4px"/><emp:message key="user_xtgl_czygl_text_134" 
										defVal="包含子机构" fileName="user" /></div>
													<ul class="call_ul">
														<li><a id="call" onclick="cAll()"><emp:message key="user_xtgl_czygl_text_80" 
										defVal="全部" fileName="user" /></a></li>
													</ul>
													<ul id="dropdownMenu" class="tree">
													</ul>
												</div>
											</td>
											<td class="tdSer">
											<center><a id="search"></a></center>
										</td>
											
										</tr>
										<tr>
											<td><emp:message key="user_xtgl_czygl_text_79" 
										defVal="角色：" fileName="user" /></td>
											<td>
												<select name="roleName" id="roleName" class="roleName" isInput="false">
													<option value=""><emp:message key="user_xtgl_czygl_text_80" 
										defVal="全部" fileName="user" /></option>
													<%
														if(roleList != null && roleList.size() > 0)
		                        						{
			                        						for(LfRoles role : roleList)
			                        						{
													%>
						                            <option value="<%=role.getRoleId()%>" 
						                            	<%if(conditionMap.get("roleName")!=null
						                            		&& conditionMap.get("roleName").toString().equals(role.getRoleId().toString()))
						                            		{
						                            			out.print("selected");
						                            		}%>>
						                            	<%=role.getRoleName().replace("<","&lt;").replace(">","&gt;")%>
						                            </option>
						                        <%
						                        	}
						                        		                        					}
						                        %>  
					                          
												</select>
											</td>
										
											<td><emp:message key="user_xtgl_czygl_text_81" 
										defVal="尾号：" fileName="user" /></td>
											<td>
												<select id="subno" name="subno" class="subno" isInput="false">
														<option value=""><emp:message key="user_xtgl_czygl_text_15" 
										defVal="请选择" fileName="user" /></option>
														<option value="1" 
														<%if(conditionMap.get("isSubno") != null && "1".equals(conditionMap.get("isSubno"))){%>
															selected="selected"
														<%}%>><emp:message key="user_xtgl_czygl_text_82" 
										defVal="有" fileName="user" /> </option>
														<option value="2"
														<%if(conditionMap.get("isSubno") != null && "2".equals(conditionMap.get("isSubno"))){%>
															selected="selected"
														<%}%>
														><emp:message key="user_xtgl_czygl_text_83" 
										defVal="无" fileName="user" /></option>
												</select>
											</td>
											<td><emp:message key="user_xtgl_czygl_text_119" 
										defVal="状态：" fileName="user" /></td>
											<td>
												<select id="userState" name="userState" class="userState" isInput="false">
														<option value=""><emp:message key="user_xtgl_czygl_text_80" 
										defVal="全部" fileName="user" /></option>
														<option value="1" <%if("1".equals(conditionMap.get("userState"))){%> selected<%}%> ><emp:message key="user_xtgl_czygl_text_18" 
										defVal="启用" fileName="user" /></option>
														<option value="0" <%if("0".equals(conditionMap.get("userState"))){%> selected<%}%>><emp:message key="user_xtgl_czygl_text_19" 
										defVal="禁用" fileName="user" /></option>
														<option value="2" <%if("2".equals(conditionMap.get("userState"))){%> selected<%}%>><emp:message key="user_xtgl_czygl_text_84" 
										defVal="注销" fileName="user" /></option>
														<option value="3" <%if("3".equals(conditionMap.get("userState"))){%> selected<%}%>><emp:message key="user_xtgl_czygl_text_85" 
										defVal="锁定" fileName="user" /></option>
												</select>
											</td>
											<td></td>
										</tr>
										
										<tr>
											<td><emp:message key="user_xtgl_czygl_text_13" 
										defVal="手机：" fileName="user" /></td>
											<td>
												<input id="conphone" name="conphone" value="<%=conditionMap.get("conphone") == null?"":conditionMap.get("conphone")%>"
												onkeyup="phoneInputCtrl($(this));"  maxlength="21" type="text" class="conphone"/>
											</td>
											
											<td><emp:message key="user_xtgl_czygl_text_86" 
										defVal="员工：" fileName="user" /></td>
											<td >
												<select name="isEmployee" id="isEmployee" class="isEmployee" isInput="false">
												<option value=""><emp:message key="user_xtgl_czygl_text_80" 
										defVal="全部" fileName="user" /></option>
												<option value="2" <%if("2".equals(conditionMap.get("isEmployee"))){ %>selected="selected"<%} %>><emp:message key="user_xtgl_czygl_text_34" 
										defVal="是" fileName="user" /></option>
												<option value="0" <%if("0".equals(conditionMap.get("isEmployee"))){ %>selected="selected"<%} %>><emp:message key="user_xtgl_czygl_text_33" 
										defVal="否" fileName="user" /></option>
												</select>
											</td>
											<td><emp:message key="user_xtgl_czygl_text_87" 
										defVal="开户人：" fileName="user" /></td>
											<td>
												<input id="conname"  name="conname" value="<%=conditionMap.get("conname") == null?"":conditionMap.get("conname")%>"  
												type="text" maxlength="32" class="conname"/>
											</td>
											<td>
											</td>
										</tr>
										
										<tr>
											<td><emp:message key="user_xtgl_czygl_text_88" 
										defVal="创建时间：" fileName="user" /></td>
											<td>
												<input type="text" value='<%=conditionMap.get("submitSartTime") == null?"":conditionMap.get("submitSartTime")%>' 
												id="submitSartTime" name="submitSartTime" 
												class="Wdate submitSartTime" readonly="readonly" onclick="stime()">
											</td>
											<td><emp:message key="user_xtgl_czygl_text_89" 
										defVal="至：" fileName="user" /></td>
											<td>
												<input type="text" value='<%=conditionMap.get("submitEndTime") == null?"":conditionMap.get("submitEndTime")%>' 
												id="submitEndTime" name="submitEndTime" 
												class="Wdate submitEndTime" readonly="readonly" onclick="rtime()">
											</td>
											<td colspan="3"></td>
										</tr>
										
								</table>
							</div>
							<table id="content">
								<thead>
									<tr>
										<th>
											<%=loginId%>
										</th>
										<th>
											<emp:message key="user_xtgl_czygl_text_90" 
										defVal="操作员名称" fileName="user" />
										</th>
										<th>
											<emp:message key="user_xtgl_czygl_text_91" 
										defVal="手机" fileName="user" />
										</th>
										<th>
											<%=sysuserCode%>
										</th>
										<th>
											<emp:message key="user_xtgl_czygl_text_92" 
										defVal="所属机构" fileName="user" />
										</th>
										<th>
											<emp:message key="user_xtgl_czygl_text_93" 
										defVal="数据权限" fileName="user" />
										</th>
										<th>
											<emp:message key="user_xtgl_czygl_text_94" 
										defVal="员工" fileName="user" />
										</th>
										<th class="cjsj_th">
											<emp:message key="user_xtgl_czygl_text_95" 
										defVal="创建时间" fileName="user" />
										</th>
										<%-- <th>
											业务管辖范围
										</th> --%>
										<th>
											<emp:message key="user_xtgl_czygl_text_96" 
										defVal="开户人" fileName="user" />
										</th>
										<th>
										   	<emp:message key="user_xtgl_czygl_text_97" 
										defVal="尾号" fileName="user" />
										</th>
										<th>
											<emp:message key="user_xtgl_czygl_text_98" 
										defVal="角色" fileName="user" />
										</th>
										<th>
											<emp:message key="user_xtgl_czygl_text_99" 
										defVal="状态 " fileName="user" />
										</th>
										<%
											int colspan = 0;
											if(btnMap.get(menuCode+"-3") != null)
											{
												colspan ++;
												colspan ++;
											}
											if(btnMap.get(menuCode+"-6") != null)
											{
												colspan ++;											
											}
											if(btnMap.get(menuCode+"-7") != null)
											{
												colspan ++;
											}
											if(colspan > 0)                       		
											{
										%>
										<th  <%out.print(" colspan="+String.valueOf(colspan));%>>
											<emp:message key="user_xtgl_czygl_text_100" 
										defVal="操作" fileName="user" />
										</th>
										<%
											}
										%>
									</tr>
								</thead>
								<tbody>
									<%
										if(sysVoList != null && sysVoList.size()>0){
																			
																				for(int v=0;v<sysVoList.size();v++)
																				{
																					LfSysuserVo sysVo=sysVoList.get(v);
									%>
									<tr>
										<td class="textalign">
											<%=sysVo.getUserName()%>
										</td>
										<td class="textalign">
											<label><xmp><%=sysVo.getName()%></xmp></label>
										</td>
										<td>
											<%
												if(sysVo.getMobile() != null && !"".equals(sysVo.getMobile())){
													out.print(sysVo.getMobile());
												}else{
													out.print("-");
												}
											%>
										</td>
										<td>
											<%=sysVo.getUserCode()%>
										</td>
										<td class="textalign">
											<%String depname = sysVo.getDepName();%>
											<a onclick="javascript:detail(this,1)" title="<%=depname %>">
										  		<label class="depname_label"><xmp><%=depname%></xmp></label>
												<xmp><%=depname.length()>5?depname.substring(0,5)+"...":depname %></xmp>
										  </a> 
										</td>
										<!-- 数据权限 -->
										<td class="textalign">
										<%-- 
											<%=sysVo.getPermissionType() == 1?"个人权限":"机构权限" %>--%>
											<%
												if(sysVo.getPermissionType() == 1){
											%><emp:message key="user_xtgl_czygl_text_101" 
										defVal="个人权限" fileName="user" />
											<%}else{
											%><%if(sysVo.getDomDepList() != null && sysVo.getDomDepList().size()>0){
													LfDep dep =(LfDep)sysVo.getDomDepList().get(0);
											%>
											<a onclick="javascript:detail(this,2)" title="<%=dep.getDepName() %>">
											  <label class="getDepName_label"><xmp><%=dep.getDepName()%></xmp></label>
											  <xmp><%=dep.getDepName().length()>5?dep.getDepName().substring(0,5)+"...":dep.getDepName() %></xmp>
											</a> 
											<%}
											%>
											<%
											}
											%>
										</td>
										<td>
										<%if(sysVo.getUserType() == 2){%>
										<emp:message key="user_xtgl_czygl_text_34" 
										defVal="是" fileName="user" />
										<%} else{%>
										<emp:message key="user_xtgl_czygl_text_33" 
										defVal="否" fileName="user" />
										<%}%>
										</td>
										<td>
										  <%
										  	java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
										  %>
										  <%
										  	if(sysVo.getRegTime()==null||"".equals(sysVo.getRegTime())){
										  %>
										 - <%
										  	}else{
										  %>
										 <%=df.format(sysVo.getRegTime())%><%
										 	}
										 %>
										</td>
										<%--
										<td>
										<div style="min-width:150px;height:0px;overflow: hidden"></div>
										<%
										String aaa=null;
										if ( sysVo.getDomDepList() != null && sysVo.getDomDepList().size() == 0)
										{
										
										   <div style="padding-top:1px">原机构已被调整或删除</div>
										<%
										}
										else
										{
										%>
											 
										 	<%=((LfDep)sysVo.getDomDepList().get(0)).getDepName() %>
											<%} %>
											<div style="min-width:150px;height:0px;overflow: hidden"><%=aaa %></div>
										</td>--%>
										<td class="textalign">
											<%=null==sysVo.getHolder()?"":sysVo.getHolder()%>
										</td>
										
										
										<%
											if(sysVo.getIsExistSubno() != null && sysVo.getIsExistSubno() == 1){
										%>
											<td>
												<%=sysVo.getUsedSubno() != null?sysVo.getUsedSubno():""%>
											</td>
										<%
											}else{
										%>
											<td>
												<emp:message key="user_xtgl_czygl_text_83" 
										defVal="无" fileName="user" />
											</td>
										<%
											}
										%>
										<!--角色显示  -->
										<td class="textalign">
											 
											<%
											String name = "";
											String roleName ="";
											String roleNameShow ="";
											String ids="";
											//当前选中的角色ID
											String currentRoleId="";//conditionMap.get("roleName")
											if(conditionMap.containsKey("roleName")){
												currentRoleId=conditionMap.get("roleName");
											}
											for(int r=0;r<sysVo.getRoleList().size();r++)
											{
												LfRoles rol2=(LfRoles)sysVo.getRoleList().get(r);
												//显示的角色名称为当前查询条件设置的角色名称 
												if(currentRoleId.equals(rol2.getRoleId().toString())){
													name=rol2.getRoleName();
												}else{
													if(r == 0){
														name = rol2.getRoleName();//默认显示的是第一个角色
													}
												}
												roleName+= rol2.getRoleName() +",";
												roleNameShow+= rol2.getRoleName() +"MWHS]#";
												ids+=rol2.getRoleId()+",";
												
											}
											if(roleName.contains(",")){
													roleName = roleName.substring(0,roleName.lastIndexOf(","));
												    //判断是否有多个角色
												    if(roleName.contains(",")){
												    	name+="...";
												    }
											}
											
											//--------修改权限----------------------
											if(btnMap.get(menuCode+"-3")!=null)                       		
											{     
												boolean cEd = true;
											    if("admin".equals(sysVo.getUserName()) || "sysadmin".equals(sysVo.getUserName()))
											    {
											    	if(!("admin".equals(sysuser.getUserName())||"sysadmin".equals(sysuser.getUserName())))
											    	{
											    		cEd = false;
											    	}
											    }
												if(cEd&&!sysuser.getUserId().equals(sysVo.getUserId()) &&  sysVo.getUserState()-2 !=0){
						 											%>
	                                            <%--<a onclick="javascript:updateRole('<%=ids%>',<%=sysVo.getUserId()%>,'<%=sysVo.getName()%>','<%=sysVo.getKeyId() %>')" title="<%=roleName%>">--%>
	                                            <%--<label class="roleName_label"><xmp><%=roleName%></xmp>--%>
	                                            <%--</label>--%>
	                                            <%--<xmp><%=name%></xmp>--%>
	                                            <%--</a>--%>
	                                            <!-- 该操作员拥有的角色 -->
	                                            <a onclick="javascript:detail(this,3)" title="<%=roleName %>">
												  <label class="roleName_label"><xmp><%=roleName%></xmp></label>
												  <xmp><%=name %></xmp>
												</a>
												<%
												}else{
												%>
	                                            <%--<a onclick="javascript:updateRoleNo(this);" title="<%=roleName%>">--%>
	                                            <%--<label class="roleNameShow_label"><xmp><%=roleNameShow%></xmp>--%>
	                                            <%--</label>--%>
	                                            <%--<xmp><%=name%></xmp>--%>
	                                            <%--</a>--%>
	                                            <a onclick="javascript:detail(this,3)" title="<%=roleName %>">
												  <label class="roleName_label"><xmp><%=roleName%></xmp></label>
												  <xmp><%=name %></xmp>
												</a>
										    <%
												}
											}else{
											%>
                                            <%--<a onclick="javascript:updateRoleNo(this)" title="<%=roleName%>">--%>
                                            <%--<label class="roleNameShow_label"><xmp><%=roleNameShow%></xmp>--%>
                                            <%--</label>--%>
                                            <%--<xmp><%=name%></xmp>--%>
                                            <%--</a>--%>
                                            <label class="roleName_label">
                                                <xmp><%=roleName%>
                                                </xmp>
                                            </label>
                                            <xmp><%=name%>
                                            </xmp>
											<%
											}
											%>
										</td>
										<td class="getUserId_td">
										<center>
											<%
												boolean zhuxiao=false;
												if(btnMap.get(menuCode+"-3")!=null)                       		
												{  
													if(!sysuser.getUserId().equals(sysVo.getUserId()) 
															&& !"admin".equals(sysVo.getUserName()) 
															&& !"sysadmin".equals(sysVo.getUserName()) 
															)
													{ 
														zhuxiao=true;
													}
												}
												if(!"sysadmin".equals(sysVo.getUserName())&&btnMap.get(menuCode+"-3")!=null){
												boolean cc = true;
												if("admin".equals(sysVo.getUserName()))
												{
													cc = false;
												}
												if("sysadmin".equals(sysuser.getUserName()))
												{
													cc = true;
												}
											if(sysVo.getUserId()-curId!=0 && cc && sysVo.getUserState()-2 !=0){
											%>
												<select id="lb<%=sysVo.getUserId()%>" onchange="javascript:checkdel(this.value,<%=sysVo.getUserId()%>,'<%=sysVo.getName()%>','<%=sysVo.getUserState()%>','<%=sysVo.getKeyId() %>')" >
													<%
													if(sysVo.getUserState()-1==0)
													{%>
														<option value="0"><emp:message key="user_xtgl_czygl_text_102" 
										defVal="已启用" fileName="user" /></option>
														<option value="1"><emp:message key="user_xtgl_czygl_text_19" 
										defVal="禁用" fileName="user" /></option>
														<%if(zhuxiao){%>
															<option value="2"><emp:message key="user_xtgl_czygl_text_84" 
										defVal="注销" fileName="user" /></option>
														<%}
													}else if(sysVo.getUserState()-0==0){
													%>
														<option value="1" selected><emp:message key="user_xtgl_czygl_text_103" 
										defVal="已禁用" fileName="user" /></option>
														<option value="0"><emp:message key="user_xtgl_czygl_text_18" 
										defVal="启用" fileName="user" /></option>
														<%if(zhuxiao){
														%>
															<option value="2"><emp:message key="user_xtgl_czygl_text_84" 
										defVal="注销" fileName="user" /></option>
														<%}
													}else if(sysVo.getUserState()-3==0){%>
													<option value="-1"><emp:message key="user_xtgl_czygl_text_104" 
										defVal="已锁定" fileName="user" /></option>
													<option value="1"><emp:message key="user_xtgl_czygl_text_19" 
										defVal="禁用" fileName="user" /></option>
														<%if(zhuxiao){%>
															<option value="2"><emp:message key="user_xtgl_czygl_text_84" 
										defVal="注销" fileName="user" /></option>
														<%}
													} %>
												</select>
											<%
												}else{
														if(sysVo.getUserState()-1==0)
														{
															out.print("<div class='setControl getUserState'  > " + MessageUtils.extractMessage("user","user_xtgl_czygl_text_102",request) +"</div>");
															//out.print("已启用");
														}else if(sysVo.getUserState()-0==0)
														{
															out.print("<div class='setControl getUserState'  > " + MessageUtils.extractMessage("user","user_xtgl_czygl_text_103",request) +"</div>");
															//out.print("已禁用");
														}else if(sysVo.getUserState()-2==0)
														{
															if(zhuxiao)
															{%>	
																<select id="lb<%=sysVo.getUserId()%>" onchange="javascript:checkdel(this.value,<%=sysVo.getUserId()%>,'<%=sysVo.getName()%>','<%=sysVo.getUserState()%>','<%=sysVo.getKeyId() %>')" >
																	<option value="2"><emp:message key="user_xtgl_czygl_text_105" 
										defVal="已注销" fileName="user" /></option>
																	<option value="3"><emp:message key="user_xtgl_czygl_text_106" 
										defVal="恢复" fileName="user" /></option>
																</select>
														<%}else{
																	out.print("<div class='setControl getUserState'  > " + MessageUtils.extractMessage("user","user_xtgl_czygl_text_105",request) +"</div>");
															    	//out.print("已注销");
																}
														}else if(sysVo.getUserState()-3==0){
															out.print("<div class='setControl getUserState'  > " + MessageUtils.extractMessage("user","user_xtgl_czygl_text_104",request) +"</div>");
														}
													}
												}else
													{
													if(sysVo.getUserState()-1==0)
													{
														out.print("<div class='setControl getUserState'  >" + MessageUtils.extractMessage("user","user_xtgl_czygl_text_102",request) +"</div>");
														//out.print("已启用");
													}else if(sysVo.getUserState()-0==0)
													{
														out.print("<div class='setControl getUserState'  >" + MessageUtils.extractMessage("user","user_xtgl_czygl_text_103",request) +"</div>");
														//out.print("已禁用");
													}else if(sysVo.getUserState()-2==0)
													{
													if(zhuxiao)
													{
												%>	
													<select id="lb<%=sysVo.getUserId()%>" onchange="javascript:checkdel(this.value,<%=sysVo.getUserId()%>,'<%=sysVo.getName()%>','<%=sysVo.getUserState()%>',,'<%=sysVo.getKeyId() %>')" >
														<option value="2"><emp:message key="user_xtgl_czygl_text_105" 
										defVal="已注销" fileName="user" /></option>
														<option value="3"><emp:message key="user_xtgl_czygl_text_106" 
										defVal="恢复" fileName="user" /></option>
													</select>
											<%
												}
												else
												{
													out.print("<div class='setControl getUserState'  >" + MessageUtils.extractMessage("user","user_xtgl_czygl_text_105",request) +"</div>");
											    	//out.print("已注销");
												}
												}else if(sysVo.getUserState()-3==0){
													out.print("<div class='setControl getUserState'  >" + MessageUtils.extractMessage("user","user_xtgl_czygl_text_104",request) +"</div>");
												}
												}
											%>
										</center>
										</td>
										<%
											if(btnMap.get(menuCode+"-3")!=null)                       		
											{     
												boolean cEd = true;
										    if("admin".equals(sysVo.getUserName()) || "sysadmin".equals(sysVo.getUserName()))
										    {
										    	if(!("admin".equals(sysuser.getUserName())||"sysadmin".equals(sysuser.getUserName())))
										    	{
										    		cEd = false;
										    	}
										    }
											if( cEd)                       		
											{
										%>
										<td>
										    <%
										    	if(!sysuser.getUserId().equals(sysVo.getUserId()) &&  sysVo.getUserState()-2 !=0){
										    %>
									           <a onclick="javascript:toEdit(<%=sysVo.getUserId()%>,'<%=sysVo.getKeyId()%>');"><emp:message key="common_text_9" 
										defVal="修改" fileName="common" /></a>
									           <%
									           	}else
           								       {
           								     		out.print("-");
           								       }
									           %>
										</td>
										<%
											}else{
										%>
										  <td>-</td>
										<%
											}
										%>
										<%
											}
												if(btnMap.get(menuCode+"-6")!=null)                       		
												{
										%>
										<td>
											    <%
											    	if(!sysuser.getUserId().equals(sysVo.getUserId()) && !"admin".equals(sysVo.getUserName()) && !"sysadmin".equals(sysVo.getUserName()) && sysVo.getUserState()-2 !=0){
											    %>
										           <a href="javascript:InitPass('<%=sysVo.getGuId()%>','<%=sysVo.getUserName()%>','<%=sysVo.getName()%>','<%=sysVo.getKeyId() %>')">
										          	 <emp:message key="user_xtgl_czygl_text_128" 
															defVal="初始化密码" fileName="user" /></a>
									           <%
									           	}else{
									         		out.print("-");
									           	}
									           %>
										</td>
										<%
											}
										%>
										</tr>
									<%
										} 
																}else{
									%>
										<tr>
											<td colspan="15" align="center">
												<emp:message key="user_xtgl_czygl_text_59" 
										defVal="无记录" fileName="user" />
											</td>
										</tr>
									<%
										}
									%>
								</tbody>
								<tfoot>
							<tr>
							<td colspan="15">
								<div id="pageInfo"></div>
							</td>
						</tr>
					</tfoot>
				</table>
			</form>
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
    <div id="rolesDialog" title="<emp:message key="user_xtgl_czygl_text_107" 
										defVal="角色管理" fileName="user" />" class="rolesDialog">
    	<%-- 存放操作員的id --%>
    	<input type="hidden" id="userIdRole" value="">
    	<%-- 存放角色id的字符串拼接 --%>
    	<input type="hidden" id="roleIds" value="">
    	<input type="hidden" id="userNameRole" value="">
    	<input type="hidden" id="keyId" value="">
    	<div class="getRoleName ">
    	<%
    		if(roleList!=null&&roleList.size()>0)
    	    	{
    	    		for(int i=0;i<roleList.size();i++)
    	    		{
    	    			LfRoles lfRole=roleList.get(i);
    	%>
    			<label ><input type="checkbox" value="<%=lfRole.getRoleId() %>" >&nbsp;<xmp><%=lfRole.getRoleName() %></xmp></label>
    			<br/>
    	<%
    		}
    	}
    	%>
    	</div>
    	<center>
    			<div class="qved_div">
    				<input type="button" class="btnClass5 mr23" value="<emp:message key="common_btn_7" 
										defVal="确定" fileName="common" />" onclick="updateRo()"/>
    				<input type="button" class="btnClass6" value="<emp:message key="common_btn_16" 
										defVal="取消" fileName="common" />" onclick="$('#rolesDialog').dialog('close')"/>
    				<br/>
    			</div>
    	</center>
    </div>
    
     <div id="rolesDialog2" title="<emp:message key="user_xtgl_czygl_text_73" 
										defVal="角色列表  " fileName="user" />"  class="rolesDialog2">
    	<div class="msgRole_div ">
    		<label id="msgRole"></label>
    	</div>
    	<center>
    			<div class="qvex2_dov">
    				<input type="button" class="btnClass6" value="<emp:message key="common_btn_16" 
										defVal="取消" fileName="common" />" onclick="$('#rolesDialog2').dialog('close')"/>
    			</div>
    	</center>
    </div>
    <div id="detail" class="detail">
		<table class="msgcont_table">
			<thead>
				<tr class="msgcont_tr">
					<td class='msgcont_td'>
						<span><label id="msgcont" class="msgcont"><xmp class="msgcont_xmp"></xmp></label></span>
						 
					</td>
					 
				</tr>
			</thead>
		</table>
	</div>
	<script type="text/javascript" src="<%=empBasePath%>/common/i18n/i18nUtil.js"></script>
	<script type="text/javascript" src="<%=empBasePath%>/common/i18n/<%=langName%>/user_<%=langName%>.js"></script>
	<script type="text/javascript" src="<%=empBasePath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
	<script type="text/javascript" src="<%=inheritPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=inheritPath%>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=inheritPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=inheritPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=inheritPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=inheritPath%>/common/js/jquery.ztree-myjquery-b.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=inheritPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=inheritPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=inheritPath%>/common/js/empSelect.js?V=<%=StaticValue.getJspImpVersion() %>" ></script>
	<script language="javascript" src="<%=iPath%>/js/optsysuser.js?V=<%=StaticValue.getJspImpVersion() %>" type="text/javascript"></script>
	<script language="javascript" src="<%=iPath%>/js/sysuser.js?V=<%=StaticValue.getJspImpVersion() %>" type="text/javascript"></script>
	<script type="text/javascript">
		var zTree1;
		var setting;
		var zNodes =[];
		var findresult="<%=findResult%>";
		var totalPage = <%=pageInfo.getTotalPage()%>;
		var pageIndex = <%=pageInfo.getPageIndex()%>;
		var pageSize = <%=pageInfo.getPageSize()%>;
		var totalRec = <%=pageInfo.getTotalRec()%>;
		var sysvoFlag =  "<%=recordflag%>";
		</script>	
	</body>
</html>
