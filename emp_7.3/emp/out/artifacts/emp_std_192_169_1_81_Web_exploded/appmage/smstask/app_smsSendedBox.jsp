<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@page import="com.montnets.emp.entity.pasroute.LfSpDepBind"%>
<%@page import="com.montnets.emp.entity.biztype.LfBusManager"%>
<%@page import="com.montnets.emp.common.vo.LfMttaskVo"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@page import="com.montnets.emp.common.constant.SystemGlobals"%>
<%@page import="com.montnets.emp.common.context.EmpExecutionContext"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%@page import="com.montnets.emp.i18n.util.MessageUtils"%>
<%@include file="/common/common.jsp" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	
	java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	
	PageInfo pageInfo = new PageInfo();
	pageInfo=(PageInfo)request.getAttribute("pageInfo");
	
	@ SuppressWarnings("unchecked")
	List<LfSpDepBind> userList = (List<LfSpDepBind>)request.getAttribute("sendUserList");
	
	@ SuppressWarnings("unchecked")
	List<LfBusManager> busList = (List<LfBusManager>)request.getAttribute("busList");
	
	@ SuppressWarnings("unchecked")
	List<LfMttaskVo> mtList =(List<LfMttaskVo>) request.getAttribute("mtList");
	
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String titlePath = (String)request.getAttribute("titlePath");
	
	String menuCode = titleMap.get(titlePath);
	menuCode = menuCode==null?"0-0-0":menuCode;
	
	String recvtime = request.getParameter("recvtime");
	String sendtime = request.getParameter("sendtime");
	String deptid = request.getParameter("depid");
	String busCode = request.getParameter("busCode");
	String userid = request.getParameter("userid");
	String userName = request.getParameter("userName");
	String depNam = request.getParameter("depNam");
	String spUser = request.getParameter("spUser");
	String taskState= request.getParameter("taskState");
	if(taskState==null || "".equals(taskState))
	{
		taskState="0";
	}
	
	String taskType= request.getParameter("taskType");
	if(taskType==null || "".equals(taskType))
	{
		taskType="0";
	}
	
	//是否包含子机构
	String isContainsSun=(String)request.getAttribute("isContainsSun");
	if(isContainsSun==null||"".equals(isContainsSun)){
		isContainsSun="1";
	}
	
	//任务批次
	String taskID= request.getParameter("taskID");
	if(taskID==null||"".equals(taskID.trim())){
		taskID="";
	}else{
		try{
			Long.parseLong(taskID.trim());
			taskID=taskID.trim();
		}catch(Exception e){
			EmpExecutionContext.error(e, "任务批次号转换异常!taskID:" + taskID);
			taskID="";
		}
	}
	
	int corpType = StaticValue.getCORPTYPE();
	boolean isFirstEnter = (Boolean) request.getAttribute("isFirstEnter");
	String zNodes3 = (String)request.getAttribute("departmentTree");
	//String zNodes2 = (String)request.getAttribute("deptUserTree");
	
	String findResult= (String)request.getAttribute("findresult");
	String skin = session.getAttribute("stlyeSkin")==null?
			"default":(String)session.getAttribute("stlyeSkin");
			
	String httpUrl = StaticValue.getFileServerViewurl();
	String taskid = (String)request.getParameter("taskid");
	
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<title>sendSMS.html</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
	    <link href="<%=commonPath%>/common/css/reading.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/widget/zTreeStyle/zTreeStyle.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
	    <%--<link href="<%=inheritPath %>/styles/easyui.css" rel="stylesheet" type="text/css" />--%>
				
		
		<style type="text/css">
			body div#tooltip { position:absolute; z-index:1000; max-width:435px; _width:expression(this.scrollWidth > 435 ? "435px" : "auto"); width:auto; background:#A8CFF6; border:#FEFFD4 solid 1px; text-align:left; padding:6px;}
			body div#tooltip p { margin:0;padding:6; color:#FFFFFE;font:12px verdana,arial,sans-serif;}
			body div#tooltip p em { display:block;margin-top:3px; color:#f60;font-style:normal; font-weight:bold;}
			#content tr.peachpuff,#content tr.peachpuff td{background:#FFDAB9;}
		</style>
		<%if(StaticValue.ZH_HK.equals(empLangName)){%>
		<link rel="stylesheet" type="text/css" href="<%=empBasePath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
	</head>

	<body>
		<div id="container" class="container">
			<%-- 当前位置 --%>
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(empLangName, menuCode) %>
			
			<%-- 内容开始 --%>
			<div id="rContent" class="rContent">
			<%-- 表示请求的名称 --%>
			<input type="hidden" name="httpUrl" id="httpUrl" value="<%=httpUrl %>">
			<input type="hidden" name="ISCLUSTER" id="isCluster" value="<%=StaticValue.getISCLUSTER() %>">
			<input type="hidden" name="htmName" id="htmName" value="app_<%=titlePath %>.htm">
			<form name="pageForm" action="app_<%=titlePath %>.htm" method="post"
					id="pageForm">
					<div class="buttons">
						<div id="toggleDiv">							
						</div>
						<a id="exportCondition"><emp:message key="appmage_common_opt_daochu" defVal="导出" fileName="appmage"></emp:message></a>						
					</div>
					<div id="condition">
						<table>
						   
							<tbody>
								<tr>
									<td>
										<emp:message key="appmage_xxfb_dxqf_text_dept" defVal="隶属机构" fileName="appmage"></emp:message>：
									</td>									
									<td class="condi_f_l">	
											
									  		<div style="width:220px;">	 
									  		 <input type="hidden" id="deptid" name="depid" value="<%=deptid==null?"":deptid%>"/> 		
									  		<input type="text" id="depNam" class="treeInput" name="depNam" value="<%=depNam==null?MessageUtils.extractMessage("appmage","appmage_xxfb_appfsrw_text_qingxuanze",request):depNam%>"  onclick="javascript:showMenu();"  readonly 
									  		style="width:159px;cursor: pointer;"/>
											</div>														
											<div id="dropMenu" >
											<iframe style="position: absolute; z-index: -1; width: 100%; height: 100%; top: 0; left: 0; scrolling: no;" frameborder="0" src="about:blank"></iframe>	
												<div style="margin-top: 3px;margin-right:10px;text-align:right;">
												     <input type="checkbox" id="isContainsSun" name="isContainsSun" <%if(isContainsSun.equals("1")){%>checked="checked" <%}%> value="1" style="width:15px;height:15px;vertical-align:middle;margin-right:3px;"/><emp:message key="appmage_xxfb_dxqf_text_childdept" defVal="包含子机构" fileName="appmage"></emp:message>&nbsp;&nbsp;
													<input type="button" value="<emp:message key="appmage_common_opt_queding" defVal="确定" fileName="appmage"></emp:message>" class="btnClass1" onclick="javascript:zTreeOnClickOK3();" style=""/>&nbsp;&nbsp;
													<input type="button" value="<emp:message key="appmage_common_opt_qingkong" defVal="清空" fileName="appmage"></emp:message>" class="btnClass1" onclick="cleanSelect_dep3();" style=""/>
												</div>	
												<ul id="dropdownMenu"  class="tree"></ul>	
											</div>			
									</td>
									<td>
										<emp:message key="appmage_xxfb_appfsrw_text_caozuoyuan" defVal="操作员" fileName="appmage"></emp:message>：
									</td>
									<td class="condi_f_l">	
											
											<div style="width:220px;">
											
											 <input type="hidden" id="userid" name="userid" value="<%=userid==null?"":userid %>"/>
											<input type="text" class="treeInput" id="userName" name="userName" value="<%=userName==null?MessageUtils.extractMessage("appmage","appmage_xxfb_appfsrw_text_qingxuanze",request):userName%>" readonly onclick="javascript:showMenu2();" style="width:159px;cursor: pointer;"/>&nbsp;
<%--											<a onclick="javascript:showMenu2();" style="cursor: pointer;text-decoration: underline">选择</a>--%>
											</div>											
											<div id="dropMenu2">
											<iframe style="position: absolute; z-index: -1; width: 100%; height: 100%; top: 0; left: 0; scrolling: no;" frameborder="0" src="about:blank"></iframe>	
												<div style="margin-top: 3px;margin-right:10px;text-align:right">
													<input type="button" value="<emp:message key="appmage_common_opt_queding" defVal="确定" fileName="appmage"></emp:message>" class="btnClass1" onclick="javascript:zTreeOnClickOK2();" style=""/>&nbsp;&nbsp;
													<input type="button" value="<emp:message key="appmage_common_opt_qingkong" defVal="清空" fileName="appmage"></emp:message>" class="btnClass1" onclick="javascript:cleanSelect_dep();" style=""/>
												</div>
												<div style="margin-top:3px;padding-left:4px;"><font class="zhu"><emp:message key="appmage_xxfb_appfsrw_text_tips" defVal="注：请勾选操作员进行查询" fileName="appmage"></emp:message></font></div>	
												<ul  id="dropdownMenu2" class="tree"></ul>	
											</div>										   										
									</td>
									<td><%=MessageUtils.extractMessage("txgl","txgl_mwgateway_text_3",request) %></td>
									  
									<td>	
  		                                 	<label>
											<select name="spUser"  style="width: 180px">
												<option value="">
													<emp:message key="appmage_common_text_all" defVal="全部" fileName="appmage"></emp:message>
												</option>
												<%
													  if(userList!=null && userList.size()>0){
													     for(LfSpDepBind userdata:userList){
													      
													 %>
												<option value="<%=userdata.getSpUser() %>"
													<%=userdata.getSpUser().equals(spUser)?"selected":"" %>>
													<%=userdata.getSpUser()%>
												</option>
												<%
													   }
													  }
													 %>
											</select>
										</label>		
									</td>	
									<td class="tdSer">
									       <center><a id="search"></a></center>
								    </td>		
								</tr>
								<tr>
								
								<%--	<td>
										业务类型：
									</td>
									<td>
									   <label>
											<select name="busCode" id="busCode" value="<%=busCode%>" style="width: 180px">
												<option value="">
													全部
												</option>
												<%
														if(busList != null && busList.size()>0){													
														  for(LfBusManager lfbusManager:busList){												    
													 %>
												<option value="<%=lfbusManager.getBusCode() %>"
													<%=lfbusManager.getBusCode().equals(busCode)?"selected":"" %>>
													<%=lfbusManager.getBusName() %>
												</option>
												<%
											            }
											           }
											          %>
											</select>
										</label>
									</td> --%>
							   	   
								    <td><emp:message key="appmage_xxfb_dxqf_text_taskstatus" defVal="任务状态" fileName="appmage"></emp:message>：</td>
								    <td>
								    	<select name="taskState" id="taskState" style="width: 180px;">
								    		<option value="0"><emp:message key="appmage_common_text_all" defVal="全部" fileName="appmage"></emp:message></option>
								    		<option value="1" <%="1".equals(taskState)?"selected":"" %>><emp:message key="appmage_xxfb_dxqf_text_audit" defVal="待审批" fileName="appmage"></emp:message></option>
								    		<option value="2" <%="2".equals(taskState)?"selected":"" %>><emp:message key="appmage_xxfb_dxqf_text_auditfalse" defVal="审批不通过" fileName="appmage"></emp:message></option>
								    		<option value="3" <%="3".equals(taskState)?"selected":"" %>><emp:message key="appmage_xxfb_dxqf_text_waitsend" defVal="待发送" fileName="appmage"></emp:message></option>
								    		<option value="4" <%="4".equals(taskState)?"selected":"" %>><emp:message key="appmage_xxfb_dxqf_text_revoked" defVal="已撤销" fileName="appmage"></emp:message></option>
								    		<option value="5" <%="5".equals(taskState)?"selected":"" %>><emp:message key="appmage_xxfb_dxqf_text_sent" defVal="已发送" fileName="appmage"></emp:message></option>
								    		<option value="6" <%="6".equals(taskState)?"selected":"" %>><emp:message key="appmage_xxfb_dxqf_text_forzen" defVal="已冻结" fileName="appmage"></emp:message></option>
								    		<option value="7" <%="7".equals(taskState)?"selected":"" %>><emp:message key="appmage_xxfb_dxqf_text_timeoutnotsent" defVal="超时未发送" fileName="appmage"></emp:message></option>
								    	</select>
								    </td>
								    <td>
								       <emp:message key="appmage_xxfb_dxqf_text_sendtype" defVal="发送类型" fileName="appmage"></emp:message>：
								   </td>
								   <td>
								    	<select name="taskType" id="taskType" style="width: 180px;">
								    		<option value="0"><emp:message key="appmage_common_text_all" defVal="全部" fileName="appmage"></emp:message></option>
								    		<option value="1" <%="1".equals(taskType)?"selected":"" %>><emp:message key="appmage_xxfb_dxqf_text_EMPsend" defVal="EMP发送" fileName="appmage"></emp:message></option>
								    		<option value="2" <%="2".equals(taskType)?"selected":"" %>><emp:message key="appmage_xxfb_dxqf_text_HTTPsend" defVal="HTTP接入" fileName="appmage"></emp:message></option>
								    	</select>
								   </td>
								   <td><emp:message key="appmage_xxfb_dxqf_text_tasknum" defVal="任务批次" fileName="appmage"></emp:message>：</td>
								   <td>
								      <input type="text" id="taskID" name ="taskID" style="width: 180px" onkeyup="javascript:numberControl($(this))" value="<%="".equals(taskID)?"":taskID%>">		
								    </td>
								     <td></td>
								</tr>
								<tr>
								 
								    <td>
								        <emp:message key="appmage_xxfb_dxqf_text_createtime" defVal="创建时间" fileName="appmage"></emp:message>：
								   </td>
								   <td>
								    	<input type="text"
											style="cursor: pointer; width: 178px; background-color: white;"
											readonly="readonly" class="Wdate" onclick="stime()" 
											value="<%= sendtime== null?"":sendtime %>"  id="sendtime" name="sendtime"
												 <%--onchange="onblourSendTime('end')" --%>>
								   </td>
								   <td><emp:message key="appmage_common_text_zhi" defVal="至：" fileName="appmage"></emp:message></td>
								   <td>
								      	<input type="text"
											style="cursor: pointer; width: 178px; background-color: white;"
											readonly="readonly" class="Wdate"  onclick="rtime()"
											value="<%=recvtime==null?"":recvtime %>" 
											 id="recvtime" name="recvtime" <%--onchange="onblourSendTime('end')" --%>>
								    </td>
								    <td></td>
								    <td>
								    
								    </td>
								     <td></td>
								</tr>
							</tbody>
						</table>
					</div>
					<table id="content">
						<thead>
							<tr>
								<th>
									<emp:message key="appmage_xxfb_appfsrw_text_caozuoyuan" defVal="操作员" fileName="appmage"></emp:message>
								</th>
								<th>
									<emp:message key="appmage_xxfb_dxqf_text_dept" defVal="隶属机构" fileName="appmage"></emp:message>
								</th>
								<th>
									<%=MessageUtils.extractMessage("txgl","txgl_mwgateway_text_1",request) %>
								</th>
								<th>
									<emp:message key="appmage_xxfb_dxqf_text_sendtype" defVal="发送类型" fileName="appmage"></emp:message>
								</th>
								<th>
									<emp:message key="appmage_xxfb_appfsrw_text_subject" defVal="发送主题" fileName="appmage"></emp:message>
								</th>
								<th>
									<emp:message key="appmage_xxfb_dxqf_text_tasknum" defVal="任务批次" fileName="appmage"></emp:message>
								</th>
								<th>
									<emp:message key="appmage_xxfb_dxqf_text_createtime" defVal="创建时间" fileName="appmage"></emp:message>
								</th>
								<th>
									<emp:message key="appmage_xxfb_appfsrw_text_sendtime" defVal="发送时间" fileName="appmage"></emp:message>
								</th>
								<th>
									<emp:message key="appmage_xxfb_dxqf_text_validnumbers" defVal="有效号码数" fileName="appmage"></emp:message>  
								</th>
								 <th>
								    <emp:message key="appmage_xxfb_appfsrw_text_smscontent" defVal="信息内容" fileName="appmage"></emp:message>
								 </th>
								<th>
									<emp:message key="appmage_xxfb_dxqf_text_sendfile" defVal="发送文件" fileName="appmage"></emp:message>
								</th>
								<%--
								<th>
									审批详情
								</th>
								--%>
							    <th>
								<emp:message key="appmage_xxfb_dxqf_text_taskstatus" defVal="任务状态" fileName="appmage"></emp:message>
								</th>
								<th>
								<emp:message key="appmage_common_opt_caozuo" defVal="操作" fileName="appmage"></emp:message>  
								</th>
								
							</tr>
						</thead>
						<tbody>
							<%
							if(isFirstEnter)
							{
							%>
							<tr><td colspan="13" align="center"><emp:message key="appmage_xxfb_appfsrw_text_chilkdata" defVal="请点击查询获取数据" fileName="appmage"></emp:message></td></tr>
							<%
							} else if(mtList != null && mtList.size()>0)
							{
								for(LfMttaskVo mt : mtList)
								{
									if(taskid != null && !"".equals(taskid) && Long.valueOf(taskid) - mt.getTaskId() == 0)
								{
									%>
										<tr class="peachpuff">
									<%
									}
									else
									{
									%>
										<tr>
									<%} %>
											<td class="textalign">
												<%=mt.getName() %><%if(mt.getUserState()==2){out.print("<font color='red'>(" + MessageUtils.extractMessage("appmage","appmage_xxfb_appfsrw_text_becanceled",request) + ")</font>");} %>
											</td>
											<td class="textalign">
												<xmp><%=mt.getDepName() %></xmp>
											</td>
											<td>
												<%=mt.getSpUser() %>
											</td>
											<td  class="textalign">
											<% 
											if(mt.getTaskType()==1){
												out.print(MessageUtils.extractMessage("appmage","appmage_xxfb_dxqf_text_EMPsend",request));
											}else{
												out.print(MessageUtils.extractMessage("appmage","appmage_xxfb_dxqf_text_HTTPsend",request));
											}
											%>
											</td>
											<td   class="textalign">
												<%=mt.getTitle()==null?"-":(mt.getTitle().replaceAll("<","&lt;").replace(">","&gt;")) %>
											</td>
											<td>
												<%=mt.getTaskId()==0?"-":mt.getTaskId()%>
											</td>
											<td><%-- 创建时间--%>
												<%=mt.getSubmitTime()==null?"-":df.format(mt.getSubmitTime()) %>
											</td>
											<td>
											<%--发送时间:1、如果定时时间为空（没有定时），且发送状态为0（没有发送），则在等待审批，发送时间未知；
											              2、如果定时时间不为空（定时了），则发送时间为定时时间，定时时间在不断更新中，当审批完毕时也会更新定时时间。
											--%>
												<%
												String sendedTime = "-";
												if(mt.getReState()==2){//审批不通过 （发送时间为空）
													out.print("-");
												}else if(mt.getSubState()==3){//撤销任务（空）
													out.print("-");
												}else if(mt.getSendstate()==5){//超时未发送（空）
													out.print("-");
												}else if(mt.getTimerStatus()==0 && mt.getReState()==-1){//未定时未审批（待审批）（空）
													out.print("-");
												}else if(mt.getTimerStatus()==1){//定时了
													out.print(df.format(mt.getTimerTime()));
												    if(mt.getSendstate()==0){out.print(MessageUtils.extractMessage("appmage","appmage_xxfb_dxqf_text_timed",request));}
												}else if(mt.getSendstate()==1 || mt.getSendstate()==2){//发送成功或者发送失败
													out.print(df.format(mt.getTimerTime()));
												}else{
													out.print(mt.getTimerTime()==null?"-":df.format(mt.getTimerTime()));//这里面的情况就是sendstate=4(发送中)
												}
												%>
											</td>
											<td>
													<%
														if(mt.getTaskType()==1){
															out.print(mt.getEffCount());
														}else{
															out.print("-");
														}
							 						%>
											</td>
							<td style="text-align:left;">
							<%
							if(mt.getMsg()==null || "".equals(mt.getMsg())){
								if(mt.getTaskType()==1){
									out.print(MessageUtils.extractMessage("appmage","appmage_xxfb_dxqf_text_seefile",request));
								}else{
									out.print("-");
								}
							}else{ %>
									 <a onclick=javascript:modify(this,1)>
									<xmp>
									<%
									String st = "";
									String temp = mt.getMsg().replaceAll("#[pP]_(\\d+)#","{#" + MessageUtils.extractMessage("appmage","appmage_xxfb_dxqf_text_param",request) + "$1#}");
									if(temp.length()>7)
									{
										st = temp.substring(0,7)+"...";
									}else
									{
										st = temp;
									}
									out.print(st);
									%>
									</xmp>
								 	 <label style="display:none"><xmp><%=temp %></xmp></label>
								  </a> 
							<%} %>
							</td>
							<td>
								<%if(btnMap.get(StaticValue.PHONE_LOOK_CODE) == null||mt.getTaskType()==2 ){ %>	
								 <label>-</label>
								<%}else{ %>
								<a href="javascript:checkFileOuter('<%=mt.getMobileUrl()%>','<%=path%>')"><emp:message key="appmage_common_opt_chakan" defVal="查看" fileName="appmage"></emp:message></a>
								&nbsp;
								<a href="javascript:downloadFilesOuter('<%=mt.getMobileUrl()%>','<%=path%>')"><emp:message key="appmage_xxfb_dxqf_text_dwonload" defVal="下载" fileName="appmage"></emp:message></a>
								<%} %>
							</td>
							
							<td>
								 <%
								    boolean flag = false;
								    Integer sendState = mt.getSendstate();
								    Integer subState = mt.getSubState();
								    String state=new String();
								    if(subState==2&&mt.getReState()==-1){
									    state = "<font color='#FE3E4D'>" + MessageUtils.extractMessage("appmage","appmage_xxfb_dxqf_text_audit",request) + "</font>";
									    flag =true;
									}
								    else if(subState==2&&mt.getReState()==2){
									    state = "<font color='#FE3E4D'>" + MessageUtils.extractMessage("appmage","appmage_xxfb_dxqf_text_auditfalse",request) + "</font>";
									    flag =true;
									}
									else if(subState ==4)
									{
									   state = "<font color='#FE3E4D'>" + MessageUtils.extractMessage("appmage","appmage_xxfb_dxqf_text_forzen",request) + "</font>";
									}
								    else if(subState==3){
								    	state = "<font color='#FE3E4D'>" + MessageUtils.extractMessage("appmage","appmage_xxfb_dxqf_text_revoked",request) + "</font>";
									}
								    else if(sendState==5){
								    	state="<font color='#FE3E4D'>" + MessageUtils.extractMessage("appmage","appmage_xxfb_dxqf_text_timeoutnotsent",request) + "</font>";
								    }
								    else if(sendState!=0){
								    	state = "<font color='#088dd2'>" + MessageUtils.extractMessage("appmage","appmage_xxfb_dxqf_text_sent",request) + "</font>";
								    	 flag =true;
								    }
								    else if(subState==2){
									      if(mt.getReState()==-1){
									    	  state="<font color='#000000'>" + MessageUtils.extractMessage("appmage","appmage_xxfb_dxqf_text_audit",request) + "</font>";
									    	  flag =true;
									      }else if(mt.getReState()==2){ 
									    	  state = "<font color='#FE3E4D'>" + MessageUtils.extractMessage("appmage","appmage_xxfb_dxqf_text_auditfalse",request) + "</font>";
									    	  flag =true;
									      }else{ 
									    	  state = "<font color='#048e79'>" + MessageUtils.extractMessage("appmage","appmage_xxfb_dxqf_text_waitsend",request) + "</font>";
									    	  flag =true;
									      }
									 }
								 %>	
								 <span id="s<%=mt.getMtId()%>">
									 <% 
									 	//如果类型为EMP界面发送，则可以看审核详情
									 	if(mt.getTaskType()==1){
									 		if(flag){
									 			if(mt.getReState()-0 != 0){
										 		    %>
												     <%-- javascript:verify('<mt.getMtId()') --%>
													 <a onclick="javascript:openSmsDetail('<%=mt.getMtId() %>','<%=mt.getUserId() %>');" name="querySP">
													 	<%=state %>
													 </a>
													<%
									 			}else{
									 				out.print(state);
									 			}
									 		}else{
									 			out.print(state);
									 		}
								 		}else{
								 			out.print(state);
								 		}
									 %>	
								 </span>					
							</td>
							<%-- 
							<td>
							    <%
							   	 if(mt.getReState()-0 != 0){
							    %>
							     <!-- javascript:verify('<mt.getMtId()') -->
								 <a onclick="javascript:openSmsDetail('<%=mt.getMtId() %>','<%=mt.getUserId() %>');" name="querySP">查看</a>
								<%
							    }else{
								out.print("-");
								}
								%>
							</td> 
							--%>
							<td>
							  <span id="cz<%=mt.getMtId()%>">
							    <%
							      if(mt.getTaskType()==1){
										   if(mt.getSendstate()!=0 || mt.getSubState()==3 || mt.getReState()==2){
											   out.print("-");
										   }
										   else{
										 	%>																			  									 
											<a title="<emp:message key="appmage_xxfb_dxqf_text_revoktask" defVal="撤销任务" fileName="appmage"></emp:message>" href="javascript:cancelTimer(<%=mt.getMtId() %>)"><emp:message key="appmage_xxfb_dxqf_text_revok" defVal="撤销" fileName="appmage"></emp:message></a>
											<%
										   }
								   }else{
								   			out.print("-");
								   }
								 %>		
								 </span>
							 </td>
						</tr>
						<%
								}
							}else{
						%>
						<tr><td align="center" colspan="13"><emp:message key="appmage_common_text_nodata" defVal="无记录" fileName="appmage"></emp:message></td></tr>
						<%} %>
						</tbody>
						<tfoot>
							<tr>
								<td colspan="13">
									<div id="pageInfo"></div>
								</td>
							</tr>
						</tfoot>
					</table>
					<div id="smssendparams" class="hidden"></div>
				</form>
			</div>
			
			<%-- 审批详情--%>
			<div id="shenhe" title="<emp:message key="appmage_xxfb_dxqf_text_auditdetail" defVal="审批详情" fileName="appmage"></emp:message>"  style="padding:5px;width:300px;height:160px;display:none">
			
			</div>
			<%-- 内容结束 --%>
			
			<div id="modify" title="<emp:message key="appmage_xxfb_appfsrw_text_smscontent" defVal="信息内容" fileName="appmage"></emp:message>"  style="padding:5px;width:300px;height:160px;display:none">
				<table width="100%">
					<thead>
						<tr style="padding-top:2px;margin-bottom: 2px;">
							<td style='word-break: break-all;'>
								<span><label id="msgcont" style="width:100%;height:100%"><xmp style='word-break: break-all;white-space:normal;'></xmp></label></span>
								 
							</td>
							 
						</tr>
					   <tr style="padding-top:2px;">
							<td>
							</td>
							</tr>
						 
					</thead>
				</table>
			</div>
			
			<div id="smsdetailinfo" title="<emp:message key="appmage_xxfb_dxqf_text_auditdetail" defVal="审批详情" fileName="appmage"></emp:message>" style="display:none;overflow: auto;">
				<div style="width: 100%;padding:15px 0;display:none;" id="recordTableDiv" align="center">
						<table width="95%" id="recordTable"  style='text-align:center'>
						</table>
				</div>
				<div style="width: 95%;padding-bottom:15px;padding-top:10px;display:none;font-size: 12px;color: blue;padding-left: 25px;" id="nextrecordmgs" align="left">
				</div>
			</div>	
			<%-- foot开始 --%>
			<div class="bottom">
				<div id="bottom_right">
					<div id="bottom_left"></div>
					<div id="bottom_main">
					</div>
				</div>
				</div>
		</div>
    <div class="clear"></div>
    <script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
        <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
        <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.ztree-myjquery-b.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>" type="text/javascript" ></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		 <script type="text/javascript" src="<%=commonPath%>/common/js/checkFile.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		 <script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=empLangName%>/appmage_<%=empLangName%>.js"></script>
		<script type="text/javascript" src="<%=iPath %>/js/app_smsSendedBox.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=iPath %>/js/smsSendedBox.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript">
			$(document).ready(function() {
			    //getLoginInfo("#smssendparams");
			    var findresult="<%=findResult%>";
			    closeTreeFun(["dropMenu2","dropMenu"]);
			    if(findresult=="-1")
			    {
			       //alert("加载页面失败,请检查网络是否正常!");
			       alert(getJsLocaleMessage('appmage','appmage_page_mttaskselect_loadpagefalse'));		
			       return;			       
			    }
				initPage(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>);
				//导出全部数据到excel
				$("#exportCondition").click(
				    function()
				    {
					
						if(confirm(getJsLocaleMessage('appmage','appmage_page_mttaskselect_exportexcel')))
						{
							var sendtime = '<%=sendtime!=null?sendtime:""%>';
							var recvtime = '<%=recvtime!=null?recvtime:""%>';
					   		var userName = '<%=userid!=null?userid:""%>';
					   	    var depNam = '<%=deptid!=null?deptid:""%>';	
					   	    var spuser = '<%=spUser!=null?spUser:""%>';	
					   	    
					   	    var lgcorpcode =$("#lgcorpcode").val();
					        var lguserid =$("#lguserid").val();		   	    
					   	    var taskState='<%=taskState%>';
					   	    var taskType='<%=taskType%>';
					   	    var taskID='<%=taskID%>';
					   	    var isContainsSun='<%=isContainsSun%>';
					   	    var pageIndex = <%=pageInfo.getPageIndex()%>
					   	    var pageSize = <%=pageInfo.getPageSize()%>
					   		<%
					   		if(mtList!=null && mtList.size()>0 && pageInfo.getTotalRec()<=500000){
					   		%>
					   			$.ajax({
									type:"POST",
									url: "<%=path%>/app_<%=titlePath %>.htm",
									data: {method: "ReportAllPageExcel",
										sendtime:sendtime,
					   		      		recvtime:recvtime,
					   		      		userid:userName,
					   		      		depid:depNam,
					   		      		spuser:spuser,
					   		      		pageIndex:pageIndex,
					   		      		pageSize:pageSize,
					   		      		lguserid:lguserid,
					   		      		lgcorpcode:lgcorpcode,
					   		      		taskState:taskState,
					   		      		taskType:taskType,
					   		      		taskID:taskID,
					   		      		isContainsSun:isContainsSun
									},
					   				beforeSend: function(){
					   					page_loading();
									},
									complete:function(){
										page_complete();
									},
									success: function(result){
										if(result)
										{
											window.parent.complete();
											download_href("<%=path%>/app_<%=titlePath %>.htm?method=downloadFile");
										}else
										{
											//alert("导出失败！");
											alert(getJsLocaleMessage('appmage','appmage_page_mttaskselect_exportfalse'));
										}
									}
								});					   		
					   		<%	
					   		}
					   		else if(mtList!=null && pageInfo.getTotalRec()>500000)
					   		{
					   		%>
					   		  // alert("数据量超过导出的范围50万，请从数据库中导出！");
					   		   alert(getJsLocaleMessage('appmage','appmage_page_mttaskselect_datatoobig'));
					   		<%}	else{
					   		%>
					   		    //alert("无数据可导出！");
					   		    alert(getJsLocaleMessage('appmage','appmage_page_mttaskselect_nodatatoexport'));
					   		<%
					   		}%>
					   }				 
				});
			});
		</script>
	</body>
</html>
