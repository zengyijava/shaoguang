<%@page import="com.montnets.emp.i18n.util.MessageUtils"%>
<%@page import="com.montnets.emp.i18n.util.MessageResource"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@page import="com.montnets.emp.entity.biztype.LfBusManager"%>
<%@page import="com.montnets.emp.wyquery.vo.LfMttaskVo"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@page import="com.montnets.emp.common.constant.CommonVariables"%>
<%@page import="com.montnets.emp.common.constant.SystemGlobals"%>
<%@page import="com.montnets.emp.common.context.EmpExecutionContext"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>

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
	List<String> userList = (List<String>)request.getAttribute("sendUserList");
	
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
	if(taskID==null || taskID.trim().length() == 0){
		taskID="";
	}else{
		try{
			Long.parseLong(taskID.trim());
			taskID=taskID.trim();
		}catch(Exception e){
			taskID="";
			EmpExecutionContext.error(e, "网优历史记录taskid转型异常。");
		}
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
	
	String mtSendState = request.getParameter("mtSendState");
	
	int corpType = StaticValue.getCORPTYPE();
	boolean isFirstEnter = (Boolean) request.getAttribute("isFirstEnter");
	String zNodes3 = (String)request.getAttribute("departmentTree");
	//String zNodes2 = (String)request.getAttribute("deptUserTree");
	
	String findResult= (String)request.getAttribute("findresult");
	CommonVariables  CV = new CommonVariables();
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	
	String httpUrl = StaticValue.getFileServerViewurl();
	String langName = (String) session.getAttribute(StaticValue.LANG_KEY);
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<title>historyRecord.html</title>
		<%@include file="/common/common.jsp" %>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=commonPath %>/common/widget/jqueryui/css/jquery.ui.dialog.new.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
	    <link href="<%=commonPath%>/common/css/reading.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/widget/zTreeStyle/zTreeStyle.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
		<link href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
	    <%--<link href="<%=inheritPath %>/styles/easyui.css?V=<%=StaticValue.JSP_IMP_VERSION %>" rel="stylesheet" type="text/css" />--%>
		
		<style type="text/css">
			body div#tooltip { position:absolute; z-index:1000; max-width:435px; _width:expression(this.scrollWidth > 435 ? "435px" : "auto");  width:auto; background:#A8CFF6; border:#FEFFD4 solid 1px; text-align:left; padding:6px;}
			body div#tooltip p { margin:0;padding:6; color:#FFFFFE;font:12px verdana,arial,sans-serif;}
			body div#tooltip p em { display:block;margin-top:3px; color:#f60;font-style:normal; font-weight:bold;}

		</style>
		
		<%if(StaticValue.ZH_HK.equals(langName)){%>	
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
			<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
	</head>

	<body>
		<div id="container" class="container">
			<%-- 当前位置 --%>
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(empLangName,menuCode) %>
			
			<%-- 内容开始 --%>
			<div id="rContent" class="rContent">
			<input type="hidden" name="httpUrl" id="httpUrl" value="<%=httpUrl %>">
			<input type="hidden" name="ISCLUSTER" id="isCluster" value="<%=StaticValue.getISCLUSTER() %>">
			<%-- 表示请求的名称 --%>
			<input type="hidden" name="htmName" id="htmName" value="wyq_historyRecord.htm">
			<%-- 短信账号--%>
			<input type="hidden" name="smsAccount" id="smsAccount" value="<%=StaticValue.SMSACCOUNT %>">
			<form name="pageForm" action="wyq_historyRecord.htm" method="post"
					id="pageForm">
					<div class="buttons">
						<div id="toggleDiv">							
						</div>
						<a id="exportCondition"><emp:message key="common_export" defVal="导出" fileName="common"></emp:message></a>
					</div>
					<div id="condition">
						<table>
						   
							<tbody>
								<tr>
									<td>
										<emp:message key="wygl_common_text1" defVal="隶属机构：" fileName="wygl"></emp:message>
									</td>									
									<td class="condi_f_l">											
									  		<div style="width:180px;">	
									  		
									  		 <input type="hidden" id="deptid" name="depid" value="<%=deptid==null?"":deptid%>"/> 		
									  		<input type="text" class="treeInput" id="depNam" name="depNam" value="<%if(depNam==null){%><emp:message key="common_pleaseSelect" defVal="请选择" fileName="common"></emp:message><%}else{%><%=depNam %><%}%>" 
									  			onclick="javascript:showMenu();"  readonly style="width:138px;cursor: pointer;"/>&nbsp;
											</div>														
											
											<div id="dropMenu" >
												<iframe style="position: absolute; z-index: -1; width: 100%; height: 100%; top: 0; left: 0; scrolling: no;" frameborder="0" src="about:blank"></iframe>	
												<div style="margin-top: 3px;margin-right:10px;text-align:center">
												 <input type="checkbox" id="isContainsSun" name="isContainsSun" <%if(isContainsSun.equals("1")){%>checked="checked" <%}%> value="1" style="width:15px;height:15px;"/><emp:message key="wygl_common_text2" defVal="包含子机构" fileName="wygl"></emp:message>&nbsp;&nbsp;
													<input type="button" value='<emp:message key="common_confirm" defVal="确定" fileName="common"></emp:message>' class="btnClass1" onclick="javascript:zTreeOnClickOK3();" style=""/>&nbsp;&nbsp;
													<input type="button" value='<emp:message key="common_clean" defVal="清空" fileName="common"></emp:message>' class="btnClass1" onclick="javascript:cleanSelect_dep3();" style=""/>
													<br/>
												</div>	
												<ul  id="dropdownMenu" class="tree"></ul>	
											</div>	
									</td>
									<td>
										<emp:message key="wygl_common_text3" defVal="操作员：" fileName="wygl"></emp:message>
									</td>
									<td class="condi_f_l">											
											<div style="width:180px;">											
											 <input type="hidden" id="userid" name="userid" value="<%=userid==null?"":userid %>"/>
											<input type="text" id="userName" class="treeInput" name="userName" value="<%if(userName==null){%><emp:message key="common_pleaseSelect" defVal="请选择" fileName="common"></emp:message><%}else{%><%=userName %><%}%>" onclick="javascript:showMenu2();" readonly 
											style="width:138px;cursor: pointer;"/>&nbsp;
											</div>											
											<div id="dropMenu2">
												<iframe style="position: absolute; z-index: -1; width: 100%; height: 100%; top: 0; left: 0; scrolling: no;" frameborder="0" src="about:blank"></iframe>	
												<div style="margin-top: 3px;margin-right:10px;text-align:right">
													<input type="button" value="<emp:message key="common_confirm" defVal="确定" fileName="common"></emp:message>" class="btnClass1" onclick="javascript:zTreeOnClickOK2();" style=""/>&nbsp;&nbsp;
													<input type="button" value="<emp:message key="common_clean" defVal="清空" fileName="common"></emp:message>" class="btnClass1" onclick="javascript:cleanSelect_dep();" style=""/>
												</div>	
												<div style="margin-top:3px;padding-left:4px;"><font class="zhu"><emp:message key="wygl_common_text4" defVal="注：请勾选操作员进行查询" fileName="wygl"></emp:message></font></div>
												<ul  id="dropdownMenu2" class="tree"></ul>	
											</div>										   										
									</td>
									<td><emp:message key="wygl_common_text5" defVal="源SP账号：" fileName="wygl"></emp:message></td>
									  
									<td>	
  		                                 	<label>
											<select name="spUser" id="spUser"  style="width: 162px">
												<option value="">
													<emp:message key="common_whole" defVal="全部" fileName="common"></emp:message>
												</option>
												<%
													  if(userList!=null && userList.size()>0){
													     for(String userdata:userList){
													    	 if(userdata!=null){
																	String spuser=userdata==null?"":userdata;
													 %>
													<option value="<%=spuser %>" 
														<%=spuser.equals(spUser)?"selected":""%>>
														<%=spuser %>
													</option>
												<%}
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
								       <emp:message key="wygl_common_text6" defVal="发送类型：" fileName="wygl"></emp:message>
								   </td>
								   <td>
								    	<select name="taskType" id="taskType" style="width: 162px;">
								    		<option value="0"><emp:message key="common_whole" defVal="全部" fileName="common"></emp:message></option>
								    		<option value="1" <%="1".equals(taskType)?"selected":"" %>><emp:message key="wygl_common_text7" defVal="EMP发送" fileName="wygl"></emp:message></option>
								    		<option value="2" <%="2".equals(taskType)?"selected":"" %>><emp:message key="wygl_common_text8" defVal="HTTP接入" fileName="wygl"></emp:message></option>
								    	</select>
								   </td>
								   <td><emp:message key="wygl_common_text9" defVal="任务批次：" fileName="wygl"></emp:message></td>
								   <td>
								      <input type="text" id="taskID" name ="taskID" style="width: 158px" maxlength="19" onkeyup="javascript:numberControl($(this))" value="<%="".equals(taskID)?"":taskID%>">	
								    </td>
								    <td><emp:message key="wygl_common_text10" defVal="发送主题：" fileName="wygl"></emp:message></td>
								    <td><input type="text" id="title" name ="title" style="width: 158px" value="<%=title== null?"":title %>"></td>
								    <td>&nbsp;</td>
								</tr>
								<tr>
								<td>
								       <emp:message key="wygl_common_text11" defVal=" 发送时间：" fileName="wygl"></emp:message>
								   </td>
								   <td>
								    	<input type="text"
											style="cursor: pointer; width: 158px; background-color: white;"
											readonly="readonly" class="Wdate" onclick="stime()" 
											value="<%= sendtime== null?"":sendtime %>"  id="sendtime" name="sendtime"
												 <%--onchange="onblourSendTime('end')" --%>>
								   </td>
								   <td><emp:message key="common_to" defVal="至：" fileName="common"></emp:message></td>
								   <td>
								      	<input type="text"
											style="cursor: pointer; width: 158px; background-color: white;"
											readonly="readonly" class="Wdate"  onclick="rtime()"
											value="<%=recvtime==null?"":recvtime %>" 
											 id="recvtime" name="recvtime" <%--onchange="onblourSendTime('end')" --%>>
								    </td>
								  
								    <td><emp:message key="wygl_common_text12" defVal="发送状态：" fileName="wygl"></emp:message></td>
								    <td>
										<select name="mtSendState" id="mtSendState" style="width: 162px" >
											<option value="" ><emp:message key="common_whole" defVal="全部" fileName="common"></emp:message></option>
								    		<option value="1" <%="1".equals(mtSendState)?"selected":"" %> ><emp:message key="wygl_common_text13" defVal="网关接收成功" fileName="wygl"></emp:message></option>
								    		<option value="2" <%="2".equals(mtSendState)?"selected":"" %> ><emp:message key="common_fail" defVal="失败" fileName="common"></emp:message></option>
								    		<option value="3" <%="3".equals(mtSendState)?"selected":"" %> ><emp:message key="wygl_common_text14" defVal="网关处理完成" fileName="wygl"></emp:message></option>
								    		<%--  <option value="0" <%="0".equals(mtSendState)?"selected":"" %> >未发送</option>
											<option value="4" <%="4".equals(mtSendState)?"selected":"" %> >EMP发送中</option>
											<option value="6" <%="6".equals(mtSendState)?"selected":"" %> >终止发送</option>--%>
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
									<emp:message key="wygl_common_text15" defVal="任务批次" fileName="wygl"></emp:message>
								</th>
								<th>
									<emp:message key="wygl_common_text16" defVal="操作员" fileName="wygl"></emp:message>
								</th>
								<th>
									<emp:message key="wygl_common_text17" defVal="隶属机构" fileName="wygl"></emp:message>
								</th>
								<th>
									<emp:message key="wygl_common_text18" defVal="源SP账号" fileName="wygl"></emp:message>
								</th>
								<th>
									<emp:message key="wygl_common_text19" defVal="发送类型" fileName="wygl"></emp:message>
								</th>
								<th>
									<emp:message key="wygl_common_text20" defVal="发送主题" fileName="wygl"></emp:message>
								</th>
								<th>
								    <emp:message key="wygl_common_text21" defVal="信息内容" fileName="wygl"></emp:message>
								</th>
								<th>
									<emp:message key="wygl_common_text22" defVal="发送时间" fileName="wygl"></emp:message>
								</th>
								<th>
								    <emp:message key="wygl_common_text23" defVal="发送状态" fileName="wygl"></emp:message>
								</th>
								<%-- <th>
									号码个数
								</th> --%>
								<th>
									<emp:message key="wygl_common_text24" defVal="提交信息数" fileName="wygl"></emp:message>
								</th>
								<th>
									<emp:message key="wygl_common_text25" defVal="发送成功数" fileName="wygl"></emp:message>
								</th>
								<th>
									<emp:message key="wygl_common_text26" defVal="提交失败数" fileName="wygl"></emp:message>
								</th>
								<th>
									<emp:message key="wygl_common_text27" defVal="接收失败数" fileName="wygl"></emp:message>
								</th>
								<th>
								    <emp:message key="wygl_common_text28" defVal="详情" fileName="wygl"></emp:message>
								</th>
							</tr>
						</thead>
						<tbody>
							<%
							if(isFirstEnter)
							{
							%>
							<tr><td colspan="18" align="center"><emp:message key="wygl_common_text29" defVal="请点击查询获取数据" fileName="wygl"></emp:message></td></tr>
							<%
							} else if(mtList != null && mtList.size()>0)
							{
								for(LfMttaskVo mt : mtList)
								{
						%>
						<tr align="center">
							<td>
								<%=mt.getTaskId()==0?"-":mt.getTaskId()%>
							</td>
							<td class="textalign">
								<%=mt.getName() %><%if(mt.getUserState()==2){out.print("<font color='red'>("+MessageUtils.extractMessage("wygl", "wygl_common_text30", request)+")</font>");} %>
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
												out.print(MessageUtils.extractMessage("wygl", "wygl_common_text7", request));
											}else{
												out.print(MessageUtils.extractMessage("wygl", "wygl_common_text8", request));
											}
											%>
							</td>
							<td class="textalign">
								<%=mt.getTitle()==null?"":(mt.getTitle().replaceAll("<","&lt;").replaceAll(">","&gt;")) %>
							</td>
							<td  class="textalign">
							<%if(mt.getMsg()==null || "".equals(mt.getMsg())){
								if(mt.getTaskType()==1){
									out.print(MessageUtils.extractMessage("wygl", "wygl_common_text31", request));
								}else{
									out.print("-");
								}
							}else{ %>
									<a onclick="javascript:modify(this)">
											<xmp>
											<%
												String st = "";
												String temp = mt.getMsg().replaceAll("#[pP]_(\\d+)#","{#"+MessageUtils.extractMessage("common", "common_parameter", request)+"$1#}");
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
								  <label style="display:none"><xmp><%=temp%></xmp></label>
								  </a> 
							<%} %>
							</td>
							<td>
								<%=mt.getTimerTime()==null?"":df.format(mt.getTimerTime()) %><%--发送时间 --%>
							</td>
							<td class="ztalign">
						 	<%
								    String sendState = mt.getSendstate().toString();
						            String error = "";
								    if(sendState.equals("0")){
								       sendState = MessageUtils.extractMessage("wygl", "wygl_common_text32", request);
								    }else if(sendState.equals("1")){
								       sendState = MessageUtils.extractMessage("wygl", "wygl_common_text13", request);
								    }else if(sendState.equals("2")){
								       sendState = MessageUtils.extractMessage("common", "common_text_12", request);
								       if(mt.getErrorCodes()!=null && !"".equals(mt.getErrorCodes())){
								    	   error="["+mt.getErrorCodes()+"]";
								       }
								    }else if(sendState.equals("4")){
								       sendState = MessageUtils.extractMessage("wygl", "wygl_common_text33", request);
								    }else if(sendState.equals("6")){
								    	sendState = MessageUtils.extractMessage("wygl", "wygl_common_text34", request);
								    }else if(sendState.equals("3"))
								    	{
								    	sendState = MessageUtils.extractMessage("wygl", "wygl_common_text35", request);
								    	}else{
								       sendState = MessageUtils.extractMessage("wygl", "wygl_common_text36", request);
								    }
								 %>
								 <%=sendState %><%=error %>	
							 </td>
							 <%
								String[] wysendinfoArray = mt.getWySendInfo().split("/");
							 	//长度少于4，则数据有问题
							 	if(wysendinfoArray.length < 4){
							 		//号码个数
									mt.setEffCount("0");
									//提交信息数，预览填
									mt.setIcount("0");
									//提交信息数，网关填
									mt.setIcount2("0");
									//发送成功数
									mt.setSucCount("0");
									//提交失败数/接收失败数
									mt.setFaiCount("0");
									//接收失败数
									mt.setRFail2(0L);
							 	}else{
									//号码个数
									mt.setEffCount("0");
									//提交信息数，预览填
									mt.setIcount(wysendinfoArray[0].trim());
									//提交信息数，网关填
									mt.setIcount2(wysendinfoArray[0].trim());
									
									//提交信息数
									long iCount = Long.parseLong(wysendinfoArray[0].trim());
									//提交失败数
									long faiCount = Long.parseLong(wysendinfoArray[2].trim());
									//发送成功数
									long sucCount = iCount - faiCount;
									//发送成功数
									mt.setSucCount(String.valueOf(sucCount));
									//mt.setSucCount(wysendinfoArray[1].trim());
									
									//提交失败数
									mt.setFaiCount(wysendinfoArray[2].trim());
									//接收失败数
									mt.setRFail2(Long.parseLong(wysendinfoArray[3].trim()));
							 	}
							%>
							<%-- <td>
								<%
									if(mt.getTaskType()==1){
										out.print(mt.getEffCount());
									}else{
										out.print("-");
									}
							 	%>
							</td> --%>
							<td>
								<%-- 提交信息数 --%>
								<%=mt.getIcount2()==null?"-":mt.getIcount2() %>
							</td>
							<td>
								<%-- 发送成功数 --%>
								<%=mt.getSucCount()==null?"-":mt.getSucCount() %>
								
							</td>
							<td>
								<%-- 提交失败数 --%>
								<%=mt.getFaiCount()==null?"-":mt.getFaiCount() %>
							</td>
							<td>
								<%-- 接收失败数 --%>
								<%=mt.getRFail2()==null?"-":mt.getRFail2() %>
							</td>
							


							<td>
							  <%if(mt.getSendstate().toString().equals("2")){ %>
							     -
							  <%} else{%>
							  <a onclick="javascript:location.href='wyq_historyRecord.htm?method=findAllSendInfo&mtid=<%=mt.getMtId() %>&type=0'"><emp:message key="wygl_common_text37" defVal="查看" fileName="wygl"></emp:message></a>
							  <%} %>
							</td>			
						</tr>
						<%
								}
							}else{
						%>
						<tr><td align="center" colspan="18"><emp:message key="common_norecord" defVal="无记录" fileName="common"></emp:message></td></tr>
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
			<div id="modify" title="<emp:message key="wygl_common_text21" defVal="信息内容" fileName="wygl"></emp:message>"  style="padding:5px;width:300px;height:160px;display:none">
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
    <script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/wygl_<%=langName%>.js"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.ztree-myjquery-b.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>" type="text/javascript" ></script>
	<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/checkFile.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=iPath %>/js/wyq_historyRecordForTree.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=iPath %>/js/wyq_historyRecord.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript">
		$(document).ready(function() {
		    // getLoginInfo("#smssendparams");
		    //参数是要隐藏的下拉框的div的id数组，
		    closeTreeFun(["dropMenu2","dropMenu"]);
			var findresult="<%=findResult%>";
		    if(findresult=="-1")
		    {
		       alert(getJsLocaleMessage('wygl', 'wygl_common_text1'));	
		       return;			       
		    }
			
			initPage(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>);
			
			//导出全部数据到excel
			$("#exportCondition").click(
			function()
			{
				
				if(confirm(getJsLocaleMessage('wygl', 'wygl_common_text2')))
				 {
			   		<%
			   		if(mtList!=null && mtList.size()>0){
			   		  if(pageInfo.getTotalRec()<=500000){
			   		%>	
			   			  var langName ='<%=langName %>';	
			   			  var mTitle = '<%=title!=null?title:""%>';
				   	      var spuser = '<%=spUser!=null?spUser:""%>';			   	    
				   	   	  var lgcorpcode =$("#lgcorpcode").val();
						  var lguserid =$("#lguserid").val(); 	
						  var taskType='<%=taskType%>';	
						  var taskID='<%=taskID%>';	
						  var isContainsSun='<%=isContainsSun%>';	
			   		      $.ajax({
							type: "POST",
							url: "<%=path%>/wyq_historyRecord.htm?method=ReportCurrPageExcel",
							data: {
								langName:langName,
								spuser:spuser,
								mTitle:mTitle,
								pageIndex:'<%=pageInfo.getPageIndex()%>',
								pageSize:'<%=pageInfo.getPageSize()%>',
								lguserid:lguserid,
								lgcorpcode:lgcorpcode,
								taskType:taskType,
								taskID:taskID,
								isContainsSun:isContainsSun
							},
				            beforeSend:function () {
				                page_loading();
				            },
				            complete:function () {
				           	  	page_complete();
				            },
				            success:function (result) {
				                    if (result == 'true') {
				                        download_href("<%=path%>/wyq_historyRecord.htm?method=downloadFile");
				                    } else {
				                        alert(getJsLocaleMessage('wygl', 'wygl_common_text5'));
				                    }
	           				}
				 	});				
			   		      //location.href="<%=path%>/wyq_historyRecord.htm?method=ReportCurrPageExcel&spuser="+
			   		      //spuser+"&mTitle="+mTitle+"&pageIndex=<%=pageInfo.getPageIndex()%>&pageSize=<%=pageInfo.getPageSize()%>&lguserid="+
			   		      //lguserid+"&lgcorpcode="+lgcorpcode+"&taskType="+taskType+"&taskID="+taskID+"&isContainsSun="+isContainsSun;
			   		
			   		<%	
			   		}else{%>
			   		    alert(getJsLocaleMessage('wygl', 'wygl_common_text3'));
			   		<%}
			   		}else{
			   		%>
			   		    alert(getJsLocaleMessage('wygl', 'wygl_common_text4'));
			   		<%
			   		}%>
				};				 
			});
			
			$("#title").live('keyup blur',function(){
				var value=$(this).val();
				if(value!=filterString(value)){
					$(this).val(filterString(value));
				};
			});
			
		});
	</script>
	</body>
</html>
