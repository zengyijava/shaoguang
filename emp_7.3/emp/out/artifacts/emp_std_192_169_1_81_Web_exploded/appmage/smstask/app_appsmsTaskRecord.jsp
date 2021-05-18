<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@page import="com.montnets.emp.entity.pasroute.LfSpDepBind"%>
<%@page import="com.montnets.emp.entity.biztype.LfBusManager"%>
<%@page import="com.montnets.emp.common.vo.LfMttaskVo"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@page import="com.montnets.emp.common.constant.CommonVariables"%>
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
	
	//@ SuppressWarnings("unchecked")
	//LfSysuser sysuser=((LfSysuser)session.getAttribute("loginSysuser"));
	
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
	String title = request.getParameter("title");
	String currUserid = request.getParameter("lguserid");
	//任务批次
	String taskID= request.getParameter("taskID");
	if(taskID==null||"".equals(taskID.trim())){
		taskID="";
	}else{
		try{
			Long.parseLong(taskID.trim());
			taskID=taskID.trim();
		}catch(Exception e){
			EmpExecutionContext.error(e, MessageUtils.extractMessage("appmage","appmage_xxfb_dxqf_text_taskerror",request)  + "taskID:" + taskID);
			taskID="";
		}
	}
	
	String sendstate=request.getParameter("sendstate");
	if(sendstate==null || "".equals(sendstate))
	{
		sendstate="0";
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
	
	int corpType = StaticValue.getCORPTYPE();
	boolean isFirstEnter = (Boolean) request.getAttribute("isFirstEnter");
	String zNodes3 = (String)request.getAttribute("departmentTree");
	//String zNodes2 = (String)request.getAttribute("deptUserTree");
	
	String findResult= (String)request.getAttribute("findresult");
	CommonVariables  CV = new CommonVariables();
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	
	String httpUrl = StaticValue.getFileServerViewurl();
	
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
        <link href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
	    <%--<link href="<%=inheritPath %>/styles/easyui.css" rel="stylesheet" type="text/css" />--%>
		
		<style type="text/css">
			body div#tooltip { position:absolute; z-index:1000; max-width:435px; _width:expression(this.scrollWidth > 435 ? "435px" : "auto");  width:auto; background:#A8CFF6; border:#FEFFD4 solid 1px; text-align:left; padding:6px;}
			body div#tooltip p { margin:0;padding:6; color:#FFFFFE;font:12px verdana,arial,sans-serif;}
			body div#tooltip p em { display:block;margin-top:3px; color:#f60;font-style:normal; font-weight:bold;}

		</style>
		<%if(StaticValue.ZH_HK.equals(empLangName)){%>
		<link rel="stylesheet" type="text/css" href="<%=empBasePath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<%--<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.JSP_IMP_VERSION %>"/>--%>
		<%}%>
        <style type="text/css">
            #condition table tr td > input, #condition table tr td > div > input, #condition table tr td > label > input {
                height: 33px !important;
                padding: 0px;
                padding-left: 10px;
                width: 208px !important;
            }
            #condition .condi_f_l>div .treeInput{
                width:180px;height: 34px;
            }
        </style>
	</head>

	<body>
		<div id="container" class="container">
			<%-- 当前位置 --%>
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(empLangName, menuCode) %>
			
			<%-- 内容开始 --%>
			<div id="rContent" class="rContent">
			<input type="hidden" name="httpUrl" id="httpUrl" value="<%=httpUrl %>">
			<input type="hidden" name="ISCLUSTER" id="isCluster" value="<%=StaticValue.getISCLUSTER() %>">
			<%-- 表示请求的名称 --%>
			<input type="hidden" name="htmName" id="htmName" value="app_<%=titlePath %>.htm">
			<%-- 短信账号--%>
			<input type="hidden" name="smsAccount" id="smsAccount" value="<%=MessageUtils.extractMessage("txgl","txgl_mwgateway_text_1",request) %>">
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
									  		<div <%--style="width:180px;height: 34px;"--%>>
									  		
									  		 <input type="hidden" id="deptid" name="depid" value="<%=deptid==null?"":deptid%>"/> 		
									  		<input type="text" class="treeInput" id="depNam" name="depNam" value="<%=depNam==null?MessageUtils.extractMessage("appmage","appmage_xxfb_appfsrw_text_qingxuanze",request):depNam%>" 
									  			onclick="javascript:showMenu();"  readonly style="width:138px;cursor: pointer;"/>&nbsp;
											</div>														
											
											<div id="dropMenu" >
												<iframe style="position: absolute; z-index: -1; width: 100%; height: 100%; top: 0; left: 0; scrolling: no;" frameborder="0" src="about:blank"></iframe>	
												<div style="margin-top: 3px;margin-right:10px;text-align:center">
												 <input type="checkbox" id="isContainsSun" name="isContainsSun" <%if(isContainsSun.equals("1")){%>checked="checked" <%}%> value="1" style="width:15px;height:15px;"/><emp:message key="appmage_xxfb_dxqf_text_childdept" defVal="包含子机构" fileName="appmage"></emp:message>&nbsp;&nbsp;
													<input type="button" value="<emp:message key="appmage_common_opt_queding" defVal="确定" fileName="appmage"></emp:message>" class="btnClass1" onclick="javascript:zTreeOnClickOK3();" style=""/>&nbsp;&nbsp;
													<input type="button" value="<emp:message key="appmage_common_opt_qingkong" defVal="清空" fileName="appmage"></emp:message>" class="btnClass1" onclick="javascript:cleanSelect_dep3();" style=""/>
												</div>	
												<ul  id="dropdownMenu" class="tree"></ul>	
											</div>	
									</td>
									<td>
										<emp:message key="appmage_xxfb_appfsrw_text_caozuoyuan" defVal="操作员" fileName="appmage"></emp:message>：
									</td>
									<td class="condi_f_l">											
											<div <%--style="width:180px;height: 34px;"--%>>
											 <input type="hidden" id="userid" name="userid" value="<%=userid==null?"":userid %>"/>
											<input type="text" id="userName" class="treeInput" name="userName" value="<%=userName==null?MessageUtils.extractMessage("appmage","appmage_xxfb_appfsrw_text_qingxuanze",request):userName%>" onclick="javascript:showMenu2();" readonly 
											style="width:138px;cursor: pointer;"/>&nbsp;
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
											<select name="spUser" id="spUser" style="width: 162px">
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
								
									<%--<td>
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
							   	   <td>
								       <emp:message key="appmage_xxfb_dxqf_text_sendtype" defVal="发送类型" fileName="appmage"></emp:message>：
								   </td>
								   <td>
								    	<select name="taskType" id="taskType" style="width: 162px;" isInput="false">
								    		<option value="0"><emp:message key="appmage_common_text_all" defVal="全部" fileName="appmage"></emp:message></option>
								    		<option value="1" <%="1".equals(taskType)?"selected":"" %>><emp:message key="appmage_xxfb_dxqf_text_EMPsend" defVal="EMP发送" fileName="appmage"></emp:message></option>
								    		<option value="2" <%="2".equals(taskType)?"selected":"" %>><emp:message key="appmage_xxfb_dxqf_text_HTTPsend" defVal="HTTP接入" fileName="appmage"></emp:message></option>
								    	</select>
								   </td>
								   <td><emp:message key="appmage_xxfb_dxqf_text_tasknum" defVal="任务批次" fileName="appmage"></emp:message>：</td>
								   <td>
								      <input type="text" id="taskID" name ="taskID" style="width: 158px" onkeyup="javascript:numberControl($(this))" value="<%="".equals(taskID)?"":taskID%>">	
								    </td>
								    <td><emp:message key="appmage_xxfb_appfsrw_text_subject" defVal="发送主题" fileName="appmage"></emp:message>：</td>
								    <td><input type="text" id="title" name ="title" style="width: 158px" value="<%=title== null?"":title %>"></td>
								    <td>&nbsp;</td>
								</tr>
								<tr>
								<td>
								   <emp:message key="appmage_xxfb_appfsrw_text_sendtime" defVal="发送时间" fileName="appmage"></emp:message>：
								   </td>
								   <td>
								    	<input type="text"
											style="cursor: pointer; width: 158px; background-color: white;"
											readonly="readonly" class="Wdate" onclick="stime()" 
											value="<%= sendtime== null?"":sendtime %>"  id="sendtime" name="sendtime"
												 <%--onchange="onblourSendTime('end')" --%>>
								   </td>
								   <td><emp:message key="appmage_common_text_zhi" defVal="至：" fileName="appmage"></emp:message></td>
								   <td>
								      	<input type="text"
											style="cursor: pointer; width: 158px; background-color: white;"
											readonly="readonly" class="Wdate"  onclick="rtime()"
											value="<%=recvtime==null?"":recvtime %>" 
											 id="recvtime" name="recvtime" <%--onchange="onblourSendTime('end')" --%>>
								    </td>
								  
								    <td><emp:message key="appmage_xxfb_appfsjl_text_sendstatus" defVal="发送状态" fileName="appmage"></emp:message>：</td>
								    <td>
								    	<select name="sendstate" id="sendstate" style="width: 162px;" isInput="false">
								    		<option value="0"><emp:message key="appmage_common_text_all" defVal="全部" fileName="appmage"></emp:message></option>
								    		<option value="3" <%="3".equals(sendstate)?"selected":"" %>><emp:message key="appmage_xxfb_dxqf_text_wgclwc" defVal="网关处理完成" fileName="appmage"></emp:message></option>
								    		<option value="1" <%="1".equals(sendstate)?"selected":"" %>><emp:message key="appmage_xxfb_dxqf_text_wgjscg" defVal="网关接收成功" fileName="appmage"></emp:message></option>
								    		<option value="2" <%="2".equals(sendstate)?"selected":"" %>><emp:message key="appmage_xxfb_appfsrw_text_false" defVal="失败" fileName="appmage"></emp:message></option>
								    	</select>
								   </td>
								    <td>&nbsp;</td>
								</tr>
							</tbody>
						</table>
					</div>
					<table id="content">
						<thead>
							<tr align="center">
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
									<emp:message key="appmage_xxfb_appfsrw_text_sendtime" defVal="发送时间" fileName="appmage"></emp:message>
								</th>
								<th>
								    <emp:message key="appmage_xxfb_appfsjl_text_sendstatus" defVal="发送状态" fileName="appmage"></emp:message>
								</th>
								<th>
									<emp:message key="appmage_xxfb_dxqf_text_phonenum" defVal="号码个数" fileName="appmage"></emp:message>
								</th>
								<th>
									<emp:message key="appmage_xxfb_dxqf_text_submitnum" defVal="提交信息数" fileName="appmage"></emp:message>
								</th>
								<th>
									<emp:message key="appmage_xxfb_dxqf_text_sendsucnum" defVal="发送成功数" fileName="appmage"></emp:message>
								</th>
								<th>
									<emp:message key="appmage_xxfb_dxqf_text_submitfalsenum" defVal="提交失败数" fileName="appmage"></emp:message>
								</th>
								<th>
									<emp:message key="appmage_xxfb_dxqf_text_recvefalsenum" defVal="接收失败数" fileName="appmage"></emp:message>
								</th>
								<th>
								    <emp:message key="appmage_xxfb_appfsrw_text_smscontent" defVal="信息内容" fileName="appmage"></emp:message>
								</th>
								<th>
								    &nbsp;&nbsp;<emp:message key="appmage_xxfb_dxqf_text_sendfile" defVal="发送文件" fileName="appmage"></emp:message>&nbsp;&nbsp;
								</th>
								<th>
								    <emp:message key="appmage_common_opt_caozuo" defVal="操作" fileName="appmage"></emp:message>  
								</th>
								<th>
								  <emp:message key="appmage_xxfb_dxqf_text_hfxq" defVal="回复详情" fileName="appmage"></emp:message> 
								</th>
								<th>
								  <emp:message key="appmage_xxfb_dxqf_text_fsxq" defVal="发送详情" fileName="appmage"></emp:message> 
								</th>
							</tr>
						</thead>
						<tbody>
							<%
							if(isFirstEnter)
							{
							%>
							<tr><td colspan="18" align="center"><emp:message key="appmage_xxfb_appfsrw_text_chilkdata" defVal="请点击查询获取数据" fileName="appmage"></emp:message></td></tr>
							<%
							} else if(mtList != null && mtList.size()>0)
							{
								for(LfMttaskVo mt : mtList)
								{
						%>
						<tr align="center">
							<td class="textalign">
								<%=mt.getName() %><%if(mt.getUserState()==2){out.print("<font color='red'>(" + MessageUtils.extractMessage("appmage","appmage_xxfb_appfsrw_text_becanceled",request) + ")</font>");} %>
							</td>
							<td class="textalign">
								<xmp><%=mt.getDepName() %></xmp>
							</td>
							<td>
								<%=mt.getSpUser() %>
							</td>
							<td class="textalign">
										  <% 
											if(mt.getTaskType()==1){
												out.print(MessageUtils.extractMessage("appmage","appmage_xxfb_dxqf_text_EMPsend",request));
											}else{
												out.print(MessageUtils.extractMessage("appmage","appmage_xxfb_dxqf_text_HTTPsend",request));
											}
											%>
							</td>
							<td class="textalign">
								<%=mt.getTitle()==null?"":(mt.getTitle().replaceAll("<","&lt;").replaceAll(">","&gt;")) %>
							</td>
							<td>
								<%=mt.getTaskId()==0?"-":mt.getTaskId()%>
							</td>
							<td>
								<%=mt.getTimerTime()==null?"":df.format(mt.getTimerTime()) %><%--发送时间 --%>
							</td>
							<td class="ztalign">
						 	<%
								    String sendState = mt.getSendstate().toString();
						            String error = "";
								    if(sendState.equals("0")){
								       sendState = MessageUtils.extractMessage("appmage","appmage_xxfb_dxqf_text_nosend",request) ; //"未发送";
								    }else if(sendState.equals("1")){
								       sendState = MessageUtils.extractMessage("appmage","appmage_xxfb_dxqf_text_wgjscg",request) ; //"网关接收成功";
								    }else if(sendState.equals("2")){
								       sendState = MessageUtils.extractMessage("appmage","appmage_xxfb_appfsrw_text_false",request) ; //"失败";
								       if(mt.getErrorCodes()!=null && !"".equals(mt.getErrorCodes())){
								    	   error="["+mt.getErrorCodes()+"]";
								       }
								    }else if(sendState.equals("4")){
								       sendState = MessageUtils.extractMessage("appmage","appmage_xxfb_dxqf_text_EMPsending",request) ; //"EMP发送中";
								    }else if(sendState.equals("6")){
								    	sendState = MessageUtils.extractMessage("appmage","appmage_xxfb_dxqf_text_stopsend",request) ; //"终止发送";
								    }else if(sendState.equals("3"))
								    	{
								    	sendState = MessageUtils.extractMessage("appmage","appmage_xxfb_dxqf_text_wgclwc",request) ; //"网关处理完成";
								    	}else{
								       sendState = MessageUtils.extractMessage("appmage","appmage_xxfb_dxqf_text_nouse",request) ; //"未使用";
								    }
								 %>
								 <%=sendState %><%=error %>	
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
							<td>
								<%=mt.getSendstate()==2?(mt.getIcount()==null?"0":mt.getIcount()):(mt.getIcount2()==null?"-":mt.getIcount2()) %>
							</td>
							<td>
								<%
								String fail_count=(mt.getFaiCount()==null?"0":mt.getFaiCount());
								String icount=(mt.getIcount2()==null?"0":mt.getIcount2());
								
								//提交总数
								Long icount1=Long.parseLong(icount);
								//提交失败总数
								Long fail=Long.parseLong(fail_count);
								long suc=icount1-fail;
								if(mt.getSendstate()==2)
								{
									suc=0;
									out.print(suc);
								}
								else if(mt.getIcount2()==null)
								{
									out.print("-");
								}
								else
								{
									out.print(suc);
								}
								%>
							</td>
							<td>
								<%=mt.getSendstate()==2?(mt.getIcount()==null?0:mt.getIcount()):(mt.getIcount2()==null?"-":mt.getFaiCount()) %>
							</td>
							<td>
								<%=mt.getSendstate()==2?"0":(mt.getIcount2()==null?"-":(mt.getRFail2()==null?"-":mt.getRFail2()))%>
							</td>
							<td  class="textalign">
							<%if(mt.getMsg()==null || "".equals(mt.getMsg())){
								if(mt.getTaskType()==1){
									out.print(MessageUtils.extractMessage("appmage","appmage_xxfb_dxqf_text_seefile",request)) ; //"详见文件");
								}else{
									out.print("-");
								}
							}else{ %>
								  	<a onclick=javascript:modify(this)>
									<xmp>
									<%
									String st = "";
									String temp = mt.getMsg().replaceAll("#[pP]_(\\d+)#","{#" + MessageUtils.extractMessage("appmage","appmage_xxfb_dxqf_text_param",request) + "$1#}");
									if(temp.length()>5)
									{
										st = temp.substring(0,5)+"...";
									}else
									{
										st = temp;
									}
									out.print(st);
									%>
									</xmp>
								 	 <label style="display:none"><xmp><%=temp%></xmp></label>
								  </a> 
							<%} %>
							</td>
							<td>
								<%if(btnMap.get(StaticValue.PHONE_LOOK_CODE) == null||mt.getTaskType()==2 ){ %>	
								 <label>-</label>
								<%}else{ %>									
								 <a href="javascript:checkFileOuter('<%=mt.getMobileUrl()%>','<%=request.getContextPath()%>')"><emp:message key="appmage_common_opt_chakan" defVal="查看" fileName="appmage"></emp:message></a>
									&nbsp;
								  <a href="javascript:downloadFilesOuter('<%=mt.getMobileUrl()%>','<%=request.getContextPath()%>')"><emp:message key="appmage_xxfb_dxqf_text_dwonload" defVal="下载" fileName="appmage"></emp:message></a>
								<%} %>								
							</td>

							<td>
							   <%
							   if(mt.getTaskType()==1){
									   if(mt.getIsRetry() == 1){ %>
									      <label><emp:message key="appmage_xxfb_dxqf_text_resend" defVal="已重发" fileName="appmage"></emp:message></label>
									   <%}else if(mt.getSendstate().toString().equals("2") && currUserid.toString().equals(mt.getUserId().toString())){%>
									     <a name="rsend"  href="javascript:reSend('<%=mt.getMtId() %>',<%=mt.getMsType() %>)"><emp:message key="appmage_xxfb_dxqf_text_falseresend" defVal="失败重发" fileName="appmage"></emp:message></a>
									     <%}else{ %>
									     <label>-</label>
							    		<%}
							    }else{ %>
							      		<label>-</label>
							    <%} %>
							</td>					

							<td>
							<%
							if(mt.getTaskType()==1){
									if(mt.getIsReply()==1&&"3".equals( mt.getSendstate().toString())){ %>
													<a href="javascript:searchNoticeDetail(1,<%=mt.getMtId()%>)"><emp:message key="appmage_common_opt_chakan" defVal="查看" fileName="appmage"></emp:message></a>
									<%}else {%>
									-
									<% }
							}else{%>
									-
							<%} %>
							</td>
							<td>
							  <%if(mt.getSendstate().toString().equals("2")){ %>
							     -
							  <%} else{%>
							  <a onclick="javascript:location.href='app_appsmsTaskRecord.htm?method=findAllSendInfo&mtid=<%=mt.getMtId() %>&type=0'"><emp:message key="appmage_common_opt_chakan" defVal="查看" fileName="appmage"></emp:message></a>
							  <%} %>
							</td>			
						</tr>
						<%
								}
							}else{
						%>
						<tr><td align="center" colspan="18"><emp:message key="appmage_common_text_nodata" defVal="无记录" fileName="appmage"></emp:message></td></tr>
						<%} %>
						</tbody>
						<tfoot>
							<tr>
								<td colspan="18">
									<div id="pageInfo"></div>
								</td>
							</tr>
						</tfoot>
					</table>
					<div id="smssendparams" class="hidden"></div>
				</form>
			</div>
			<div id="detailDialog" style="padding: 0px; display: none; width: 550px;">
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
	<script type="text/javascript" src="<%=iPath %>/js/app_smsTaskRecord.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript">
		$(document).ready(function() {
		    // getLoginInfo("#smssendparams");
		    //参数是要隐藏的下拉框的div的id数组，
		    closeTreeFun(["dropMenu2","dropMenu"]);
			var findresult="<%=findResult%>";
		    if(findresult=="-1")
		    {
		       //alert("加载页面失败,请检查网络是否正常!");
		       alert(getJsLocaleMessage('appmage','appmage_page_mttaskselect_loadpagefalse'));	
		       return;			       
		    }
			
			$("#title").live("keyup blur",function(){
					var value=$(this).val();
					if(value!=filterString(value)){
						$(this).val(filterString(value));
					}
			});
			
			initPage(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>);
            $('#spUser').isSearchSelect({'width':'160','isInput':true,'zindex':0});
			//导出全部数据到excel
			$("#exportCondition").click(
			function()
			{
				
				if(confirm(getJsLocaleMessage('appmage','appmage_page_mttaskselect_exportexcel')))
				 {
			   	    var mTitle = '<%=title!=null?title:""%>';
			   	    var spuser = '<%=spUser!=null?spUser:""%>';			   	    
			   	   	var lgcorpcode =$("#lgcorpcode").val();
					var lguserid =$("#lguserid").val(); 	
					 var taskType='<%=taskType%>';	
					 var taskID='<%=taskID%>';	
					 var isContainsSun='<%=isContainsSun%>';
					 var sendstate='<%=sendstate%>';
			   		<%
			   		if(mtList!=null && mtList.size()>0){
			   		  if(pageInfo.getTotalRec()<=500000){
			   		%>	
			   			$.ajax({
							type:"POST",
							url: "<%=path%>/app_appsmsTaskRecord.htm",
							data: {method: "ReportCurrPageExcel",
								spuser:spuser,
								mTitle:mTitle,
								pageIndex:pageIndex,
								pageSize:pageSize,
								lguserid:lguserid,
								lgcorpcode:lgcorpcode,
								taskType:taskType,
								taskID:taskID,
								isContainsSun:isContainsSun,
								sendstate:sendstate
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
									download_href("<%=path%>/app_appsmsTaskRecord.htm?method=downloadFile");
								}else
								{
									//alert("导出失败！");
									alert(getJsLocaleMessage('appmage','appmage_page_mttaskselect_exportfalse'));
								}
							}
						});	
			   		<%	
			   		}else{%>
			   		    //alert("数据量超过导出的范围50万，请从数据库中导出！");
			   		     alert(getJsLocaleMessage('appmage','appmage_page_mttaskselect_datatoobig'));
			   		<%}
			   		}else{
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
