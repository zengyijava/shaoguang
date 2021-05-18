<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.common.constant.ViewParams"%>
<%@ page import="com.montnets.emp.common.vo.LfMttaskVo2"%>
<%@ page import="com.montnets.emp.entity.sysuser.LfSysuser" %>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils" %>
<%@ page import="com.montnets.emp.servmodule.ydcx.constant.ServerInof" %>
<%@page import="com.montnets.emp.util.PageInfo"%>
<%@page import="java.util.List"%>
<%@ page import="java.util.Map"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));

	PageInfo pageInfo = new PageInfo();
	pageInfo = (PageInfo) request.getAttribute("pageInfo");
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("mmsTimerTask");
	menuCode = menuCode==null?"0-0-0":menuCode;
	@ SuppressWarnings("unchecked")
	List<LfMttaskVo2> mtList = (List<LfMttaskVo2>)request.getAttribute("mtList");
	java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	String sState = "";
	if (request.getAttribute("sstate")!=null)
	{
		sState = request.getAttribute("sstate").toString();
	}
	LfSysuser loginUser = (LfSysuser)request.getSession(false).getAttribute("loginSysuser");
	LfMttaskVo2 mtVo = (LfMttaskVo2)request.getAttribute("mtVo");
	String deptid = request.getParameter("depid");
	String depNam = request.getParameter("depNam");
	String userid = request.getParameter("userid");
	String userName = request.getParameter("userName");	
	
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	
	//不需要隐藏号码
	String isHideNumber = "1";//隐藏
	if(btnMap.get(StaticValue.PHONE_LOOK_CODE)!=null) { 
		//显示
		isHideNumber = "2";
	}
	
	//是否包含子机构
	String isContainsSun=(String)request.getAttribute("isContainsSun");
	if(isContainsSun==null||"".equals(isContainsSun)){
		isContainsSun="1";
	}
	//服务器名称
	String serverName = ServerInof.getServerName();
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
%>
<html>
	<head>
		<%@include file="/common/common.jsp" %>
		<title><emp:message key="ydcx_cxyy_dsxxck_cxdsxxcx" defVal="彩信定时信息查询" fileName="ydcx"></emp:message></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=commonPath %>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
		<link rel="stylesheet" href="<%=iPath%>/css/mmsTask.css?V=<%=StaticValue.getJspImpVersion() %>">
		<link rel="stylesheet" href="<%=commonPath %>/common/widget/zTreeStyle/zTreeStyle.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css"/>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion() %>" />

		<%if(StaticValue.ZH_HK.equals(langName)){%>	
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
			<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
		<link rel="stylesheet" href="<%=iPath%>/css/mmsTimerTask.css?V=<%=StaticValue.getJspImpVersion() %>">
		<link rel="stylesheet" href="<%=skin %>/ydcx_applicationMms.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
	</head>
	<body id="ydcx_mmsTimerTask">
	<input type="hidden" id="pathUrl" value="<%=path %>" />
	<input type="hidden" id="ipathUrl" value="<%=inheritPath %>"/>
	<input type="hidden" id="commonPath" value="<%=commonPath %>"/>
	
		<div id="container" class="container">
		
			<%-- header开始 --%>
		<%--<%=ViewParams.getPosition(menuCode) %>
		--%><%=ViewParams.getPosition(langName,menuCode) %>
			
		<div id="modify" title="<emp:message key="ydcx_cxyy_dsxxck_shyj" defVal="审核意见" fileName="ydcx"></emp:message>" class="ydcx_modify">
				<div id="msg" class="ydcx_msg"><xmp></xmp></div>
		</div>
			<%-- header结束 --%>
		<div id="tempView" title="<emp:message key="ydcx_cxyy_dsxxck_cxnr" defVal="彩信内容" fileName="ydcx"></emp:message>" class="ydcx_tempView" >
		
			<input type="hidden" id="tmmsId" value=""/>
			<div class="ydcx_tempView_sub">
				<div id="mobile" class="ydcx_mobile">
				<center>
				
				<div id="pers" class="ydcx_pers">
				
				<div id="showtime" class="ydcx_showtime"></div>
				
				<div id='chart' class="ydcx_chart">
				</div>
				</div>
				</center>
				
				<div id="screen" class="ydcx_screen">
				</div>
				
				<center>
				<table>
				<tr>
				  <td>
				     <label id="pointer" class="ydcx_pointer"></label>
				     <label id="nextpage" class="ydcx_nextpage"></label>
				  </td>
				</tr>
				<tr align="center">
					<td>
					   <label id="currentpage" class="ydcx_currentpage"></label>
					</td>
				</tr>
				</table>				
				</center>
				</div>
			</div>
			<div id="inputParamCnt1" class="ydcx_inputParamCnt1">
			</div>			
		</div>	
		
		
			<%-- 彩信信息详情--%>
				<%-- 彩信信息详情--%>
			<div id="mmsdetailinfo" title="<emp:message key="ydcx_cxyy_dsxxck_xxxx" defVal="详细信息" fileName="ydcx"></emp:message>" class="ydcx_mmsdetailinfo">
					  <table class="table01">
						<tr>
							<td class="t1 div_bd div_bg"><emp:message key="ydcx_cxyy_dsxxck_cxzt_mh" defVal="彩信主题：" fileName="ydcx"></emp:message></td>
							<td id="mms_taskname" class="div_bd"></td>
							<td class="t1 div_bd div_bg"><emp:message key="ydcx_cxyy_dsxxck_cxbt_mh" defVal="彩信标题：" fileName="ydcx"></emp:message></td>
							<td id="mms_title" class="div_bd"></td>
						</tr>
						<tr>
							<td class="t1 div_bd div_bg"><emp:message key="ydcx_cxyy_dsxxck_hmzs_mh" defVal="号码总数：" fileName="ydcx"></emp:message></td>
							<td id="mms_count" class="div_bd"></td>
							<td class="t1 div_bd div_bg"><emp:message key="ydcx_cxyy_dsxxck_yxhms_mh" defVal="有效号码数：" fileName="ydcx"></emp:message></td>
							<td id="mms_effcount" class="div_bd"></td>
						</tr>
						<tr>
							<td class="t1 div_bd div_bg"><emp:message key="ydcx_cxyy_dsxxck_cjsj_mh" defVal="创建时间：" fileName="ydcx"></emp:message></td>
							<td colspan="3" id="mms_time" class="div_bd"></td>
						</tr>
			          </table>
					<div class="ydcx_recordTableDiv" id="recordTableDiv" align="center">
						<table width="95%" id="recordTable" class="ydcx_recordTable">
						</table>
					</div>
					<div class="ydcx_nextrecordmgs" id="nextrecordmgs" align="left">
					</div>
			</div>	
		
		
			<%-- 内容开始 --%>
			<%if(btnMap.get(menuCode+"-0")!=null) { %>
			<div id="rContent" class="rContent">
			<form name="pageForm" action="mmt_mmsTimerTask.htm?method=find" method="post"
					id="pageForm">
					<div class="buttons">
						<div id="toggleDiv">
						</div>
						<%-- if(btnMap.get(menuCode+"-6")!=null) {  %>
						<a id="mmsExam" href="<%=path %>/mmt_mmsTimerTask.htm?method=infoExame"></a>
						<%} --%>
					</div>
					<div id="getloginUser" class="ydcx_getloginUser">
				</div>
					<input type="hidden" name="httpUrl" id="httpUrl" value="<%=StaticValue.getFileServerViewurl() %>">
					<input type="hidden" name="ISCLUSTER" id="isCluster" value="<%=StaticValue.getISCLUSTER() %>">
					<div id="condition">
						<table>
								<tbody><tr>
								<td>
										<emp:message key="ydcx_cxyy_dsxxck_dsjg_mh" defVal="隶属机构：" fileName="ydcx"></emp:message>
									</td>									
									<td class="condi_f_l">	
											
									  		<div class="ydcx_dep_dv">	 
									  		
									  		 <input type="hidden" id="deptid" name="depid" value="<%=deptid==null?"":deptid%>"/> 		
									  		<%--<input type="text" id="depNam" name="depNam"  class="treeInput" value="<%=depNam==null?"请选择":depNam%>"  onclick="javascript:showMenu();"  readonly style="width:160px;cursor: pointer;"/>
											--%><input type="text" id="depNam" name="depNam"  class="treeInput" value="<%=depNam==null?MessageUtils.extractMessage("ydcx","ydcx_cxyy_dsxxck_qxz",request):depNam%>"  onclick="javascript:showMenu();"  readonly />
											</div>														
											<div id="dropMenu">
											<iframe class="ydcx_iframe" frameborder="0" src="about:blank"></iframe>	
												<div align="center" class="ydcx_confirm_reset">
												    <input type="checkbox" id="isContainsSun" name="isContainsSun" <%if(isContainsSun.equals("1")){%>checked="checked" <%}%> value="1" class="ydcx_isContainsSun" /><emp:message key="ydcx_cxyy_dsxxck_bhzjg" defVal="包含子机构" fileName="ydcx"></emp:message>&nbsp;&nbsp;
													<input type="button" value="<emp:message key="ydcx_cxyy_common_btn_7" defVal="确定" fileName="ydcx"></emp:message>" class="btnClass1" onclick="javascript:zTreeOnClickOK3();" />&nbsp;&nbsp;
													<input type="button" value="<emp:message key="ydcx_cxyy_common_btn_20" defVal="清空" fileName="ydcx"></emp:message>" class="btnClass1" onclick="cleanSelect_dep3();"/>
												</div>	
												<ul id="dropdownMenu" class="tree ydcx_tree"></ul>	
											</div>			
									</td>
									<td>
										<emp:message key="ydcx_cxyy_dsxxck_czy_mh" defVal="操作员：" fileName="ydcx"></emp:message>
									</td>
									<td class="condi_f_l">	
											
											<div class="ydcx_usertree_dv">
											
											 <input type="hidden" id="userid" name="userid" value="<%=userid==null?"":userid %>"/>
											<%--<input type="text" id="userName" name="userName"  class="treeInput"  value="<%=userName==null?"请选择":userName%>" readonly onclick="javascript:showMenu2();" style="width:160px;cursor: pointer;"/>&nbsp;
											--%><input type="text" id="userName" name="userName"  class="treeInput ydcx_userName"  value="<%=userName==null?MessageUtils.extractMessage("ydcx","ydcx_cxyy_dsxxck_qxz",request):userName%>" readonly onclick="javascript:showMenu2();"/>&nbsp;
												<%--	<a onclick="javascript:showMenu2();" style="cursor: pointer;text-decoration: underline">选择</a>--%>
											</div>											
											<div id="dropMenu2">
											<iframe class="ydcx_dropMenu2_iframe" frameborder="0" src="about:blank"></iframe>	
												<div align="center" class="ydcx_dropMenu2_confirmcancle">
													<input type="button" value="<emp:message key="ydcx_cxyy_common_btn_7" defVal="确定" fileName="ydcx"></emp:message>" class="btnClass1" onclick="javascript:zTreeOnClickOK2();" />&nbsp;&nbsp;
													<input type="button" value="<emp:message key="ydcx_cxyy_common_btn_20" defVal="清空" fileName="ydcx"></emp:message>" class="btnClass1" onclick="javascript:cleanSelect_dep();" />
												</div>
												<div class="ydcx_note_dv" ><font class="zhu"><emp:message key="ydcx_cxyy_dsxxck_zqgxczyjxcx" defVal="注：请勾选操作员进行查询" fileName="ydcx"></emp:message></font></div>	
												<ul id="dropdownMenu2" class="tree"></ul>	
											</div>										   										
									</td>
								<td>
										<emp:message key="ydcx_cxyy_dsxxck_zt_mh" defVal="主题：" fileName="ydcx"></emp:message>
									</td>
									<td>
										<input type="text" value='<%=mtVo.getTaskName()==null?"":mtVo.getTaskName() %>' id="taskName" name="taskName" class="ydcx_taskName"/>
									</td>
										<td class="tdSer">
											<center><a id="search"></a></center>
										</td>
									
									</tr>
									<tr>
									
									<td>
										<emp:message key="ydcx_cxyy_dsxxck_cxbt_mh" defVal="彩信标题：" fileName="ydcx"></emp:message>
									</td>
									<td>
										<input type="text" value='<%=mtVo.getTitle()==null?"":mtVo.getTitle() %>' id="theme" name="theme" class="ydcx_theme" />
									</td>
									<td><emp:message key="ydcx_cxyy_dsxxck_tjwgzt_mh" defVal="提交网关状态：" fileName="ydcx"></emp:message></td><td><select name="sstate" id="sstate" class="ydcx_sstate" isInput="false">
									<option value=""><emp:message key="ydcx_cxyy_common_text_16" defVal="全部" fileName="ydcx"></emp:message></option>
									<option value="0" <%="0".equals(sState)?"selected=selected":"" %>><emp:message key="ydcx_cxyy_dsxxck_wtj" defVal="未提交" fileName="ydcx"></emp:message></option> 
									<option value="1" <%="1".equals(sState)?"selected=selected":"" %>><emp:message key="ydcx_cxyy_dsxxck_tjcg" defVal="提交成功" fileName="ydcx"></emp:message></option>
									<option value="2" <%="2".equals(sState)?"selected=selected":"" %>><emp:message key="ydcx_cxyy_dsxxck_tjsb" defVal="提交失败" fileName="ydcx"></emp:message></option>
									<option value="3" <%="3".equals(sState)?"selected=selected":"" %>><emp:message key="ydcx_cxyy_dsxxck_ycx" defVal="已撤销" fileName="ydcx"></emp:message></option>
									<option value="5" <%="5".equals(sState)?"selected=selected":"" %>><emp:message key="ydcx_cxyy_dsxxck_cswfs" defVal="超时未发送" fileName="ydcx"></emp:message></option>
									</select></td>
									<td colspan="3"></td>
									
									</tr>
									<tr>
									<td>
										<emp:message key="ydcx_cxyy_dsxxck_cjsj_mh" defVal="创建时间：" fileName="ydcx"></emp:message>
									</td>
									<td>
										<input type="text" value='<%=mtVo.getStartSubmitTime()==null?"":mtVo.getStartSubmitTime() %>' id="submitSartTime" name="submitSartTime" class="Wdate ydcx_submitSartTime" readonly="readonly" onclick="stime()">
									</td>
									<td align="left">
										<emp:message key="ydcx_cxyy_common_text_15" defVal="至：" fileName="ydcx"></emp:message>
									</td>
									<td>
										<input type="text" value='<%=mtVo.getEndSubmitTime()==null?"":mtVo.getEndSubmitTime() %>' id="submitEndTime" name="submitEndTime"  class="Wdate ydcx_submitEndTime" readonly="readonly" onclick="rtime()">
									</td>
									<td colspan="3"></td>
									</tr>
									
							</tbody>
						</table>
					</div>
					<table id="content">
						<thead>
							<tr>
								<th>
									<emp:message key="ydcx_cxyy_dsxxck_czy" defVal="操作员" fileName="ydcx"></emp:message>
								</th>
								<th>
									<emp:message key="ydcx_cxyy_dsxxck_dsjg" defVal="隶属机构" fileName="ydcx"></emp:message>
								</th>
								<th>
									<emp:message key="ydcx_cxyy_dsxxck_zt" defVal="主题" fileName="ydcx"></emp:message>
								</th>
								<th>
									<emp:message key="ydcx_cxyy_dsxxck_cxbt" defVal="彩信标题" fileName="ydcx"></emp:message>
								</th>
								<th>
									<emp:message key="ydcx_cxyy_dsxxck_cjsj" defVal="创建时间" fileName="ydcx"></emp:message>
								</th>
								<th>
									<emp:message key="ydcx_cxyy_dsxxck_fssj" defVal="发送时间" fileName="ydcx"></emp:message>
								</th>
								<th>
									<emp:message key="ydcx_cxyy_dsxxck_dssj" defVal="定时时间" fileName="ydcx"></emp:message>
								</th>
								<th>
									<emp:message key="ydcx_cxyy_dsxxck_tjzs" defVal="提交总数" fileName="ydcx"></emp:message>
								</th>
								<th>
									<emp:message key="ydcx_cxyy_dsxxck_tjwgzt" defVal="提交网关状态" fileName="ydcx"></emp:message>
								</th>
								<th>
									<emp:message key="ydcx_cxyy_dsxxck_fswj" defVal="发送文件" fileName="ydcx"></emp:message>
								</th>
								<th colspan="3">
									<emp:message key="ydcx_cxyy_common_text_14" defVal="操作" fileName="ydcx"></emp:message>
								</th>
							</tr>
						</thead>
						<tbody>
								<%
								if (mtList != null && mtList.size() > 0)
								{
									for (LfMttaskVo2 lm : mtList)
									{
								%>	
								<tr>
								<td class="textalign">
								<%--<%=lm.getName() %><%if(lm.getUserState()==2){out.print("<font color='red'>(已注销)</font>");} %>
								--%><%=lm.getName() %><%if(lm.getUserState()==2){out.print("<font color='red'>("+MessageUtils.extractMessage("ydcx","ydcx_cxyy_dsxxck_yzx",request)+")</font>");} %>
								</td>
								<td class="textalign">
								<a onclick="javascript:detail(this,1)" title="<%=lm.getDepName().replace("&","&amp;").replace("<","&lt;").replace(">","&gt;") %>">
								  <label style="display:none"><xmp><%=lm.getDepName()%></xmp></label>
											<xmp><%=lm.getDepName().length()>5?lm.getDepName().substring(0,5)+"...":lm.getDepName() %></xmp>
								  </a> 
								</td>
								<td class="textalign">
								<a onclick="javascript:detail(this,2)" title="<%=lm.getTaskName().replace("&","&amp;").replace("<","&lt;").replace(">","&gt;") %>">
								  <label style="display:none"><xmp><%=lm.getTaskName()%></xmp></label>
											<xmp><%=lm.getTaskName().length()>5?lm.getTaskName().substring(0,5)+"...":lm.getTaskName() %></xmp>
								  </a> 
								
								</td>
								<td class="textalign">
								 <%if(lm.getMsg()!= null){ %>
								    <a onclick="javascript:doPreview('<%=lm.getMsg() %>','<%=lm.getBmtType() %>','<%=lm.getTmplPath()%>')"><%=lm.getTitle().replace("<","&lt;").replace(">","&gt;") %></a>
								    <%}else{ %>
								<%=lm.getTitle().replace("<","&lt;").replace(">","&gt;") %> <%} %>
								</td>
								<td><%=df.format(lm.getSubmitTime()) %></td>
								<td>
							<%--发送时间:1、如果定时时间为空（没有定时），且发送状态为0（没有发送），则在等待审批，发送时间未知；
							              2、如果定时时间不为空（定时了），则发送时间为定时时间，定时时间在不断更新中，当审批完毕时也会更新定时时间。
							--%>
								<%
								String sendedTime = "-";
								if(lm.getReState()==2){//审批不通过 （发送时间为空）
									out.print("-");
								}else if(lm.getSubState()==3){//撤销任务（空）
									out.print("-");
								}else if(lm.getSendstate()==5){//超时未发送（空）
									out.print("-");
								}else if(lm.getTimerStatus()==0 && lm.getReState()==-1){//未定时未审批（待审批）（空）
									out.print("-");
								}else if(lm.getTimerStatus()==1){//定时了
									out.print(df.format(lm.getTimerTime()));
								    //if(lm.getSendstate()==0){out.print("(定时中)");}
								    if(lm.getSendstate()==0){out.print("("+MessageUtils.extractMessage("ydcx","ydcx_cxyy_dsxxck_dsz",request)+")");}
								}else if(lm.getSendstate()==1 || lm.getSendstate()==2){//发送成功或者发送失败
									out.print(df.format(lm.getTimerTime()));
								}else{
									out.print(lm.getTimerTime()==null?"-":df.format(lm.getTimerTime()));//这里面的情况就是sendstate=4(发送中)
								}
								%>
							</td>
								<td>
								    <%if (lm.getTimerTime() != null && !"".equals(lm.getTimerTime())){ %>
								    <%=df.format(lm.getTimerTime()) %>
								    <%}else{ %>
								    -
								    <%} %>
								</td>
								<td class="textalign">
								<%=lm.getEffCount() %>
								</td>
								<td class="ztalign">
								<%
									switch(lm.getSendstate())
									{
									//case 0: String st = "未提交";
									case 0: String st = MessageUtils.extractMessage("ydcx","ydcx_cxyy_dsxxck_wtj",request);
									    //if(lm.getSubState()== 2){st = "未提交";}
									    if(lm.getSubState()== 2){st = MessageUtils.extractMessage("ydcx","ydcx_cxyy_dsxxck_wtj",request);}
									    //else if(lm.getSubState()== 3){st = "已撤销";}
									    else if(lm.getSubState()== 3){st = MessageUtils.extractMessage("ydcx","ydcx_cxyy_dsxxck_ycx",request);}
										out.print(st);
										break;
									case 1:
										//out.print("提交成功");
										out.print(MessageUtils.extractMessage("ydcx","ydcx_cxyy_dsxxck_tjcg",request));
										break;
									case 2:
										//out.print("提交失败");
										out.print(MessageUtils.extractMessage("ydcx","ydcx_cxyy_dsxxck_tjsb",request));
										break;
									case 5:
										//out.print("超时未发送");
										out.print(MessageUtils.extractMessage("ydcx","ydcx_cxyy_dsxxck_cswfs",request));
										break;
									default:
										//out.print("[无效的标示]");
										out.print(MessageUtils.extractMessage("ydcx","ydcx_cxyy_dsxxck_wxdbs",request));
									}
								 %>
								</td>	
								<td>
									<% 
									if("2".equals(isHideNumber)){
										  %>
										<a href="javascript:checkFileOuter('<%=lm.getMobileUrl()%>','<%=request.getContextPath()%>')"><emp:message key="ydcx_cxyy_dsxxck_ck" defVal="查看" fileName="ydcx"></emp:message></a>
										&nbsp;
										<a href="javascript:downloadFilesOuter('<%=lm.getMobileUrl()%>','<%=request.getContextPath()%>')"><emp:message key="ydcx_cxyy_common_btn_19" defVal="下载" fileName="ydcx"></emp:message></a>
													  <% 
										}else{
											  %>
											- &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-											 
										 <% 
										}
									%>
								</td>
								<td>
								   <%
									
							    	if(lm.getIsRetry() == 1){
									    //out.print("已重发");
									    out.print(MessageUtils.extractMessage("ydcx","ydcx_cxyy_dsxxck_ycf",request));
									}else if((lm.getReState() ==1 || lm.getReState() == 0) && lm.getSendstate() == 2 && lm.getUserId().equals(loginUser.getUserId())){
									   	//out.print("<a onclick='reSend("+lm.getMtId()+");'>失败重发 </a>");
									   	out.print("<a onclick='reSend("+lm.getMtId()+");'>"+MessageUtils.extractMessage("ydcx","ydcx_cxyy_dsxxck_sbcf",request)+" </a>");
									}else{
									   out.print("&nbsp - &nbsp");
									}
								  %>
								</td>							
								<td>
								<%if(lm.getSendstate()==0 && lm.getSubState()!= 3){ %>
								<a href="javascript:doCancel(<%=lm.getMtId() %>)"><emp:message key="ydcx_cxyy_dsxxck_cxrw" defVal="撤销任务" fileName="ydcx"></emp:message></a>
								<%} else {out.print("&nbsp - &nbsp");} %>
								</td> 
								<td>
									<%-- <a href="mmt_mmsTimerTask.htm?method=getDetail&mtId=<%=lm.getMtId() %>&type=send">详细</a> --%>
									
									<a onclick="javascript:openMmsDetail('<%=lm.getMtId() %>','<%=lm.getUserId() %>');"><emp:message key="ydcx_cxyy_dsxxck_xx" defVal="详细" fileName="ydcx"></emp:message></a>
								</td>
								</tr>
								<%}
								}
								else{%>
								<tr><td colspan="13"><emp:message key="ydcx_cxyy_common_text_1" defVal="无记录" fileName="ydcx"></emp:message></td></tr>
								<%
								}
								%>
								</tbody>
								<tfoot>
							<tr>
								<td colspan="13">
									<div id="pageInfo"></div>
								</td>
							</tr>
						</tfoot>
					</table>
					</form>
			</div>
			<%} %>
			<div id="detail" class="ydcx_detail">
				<table width="100%">
					<thead>
						<tr class="ydcx_detail_tr1">
							<td class="ydcx_detail_td1">
								<span><label id="msgcont" class="ydcx_msgcont"><xmp style='word-break: break-all;white-space:normal;'></xmp></label></span>
								 
							</td>
							 
						</tr>
					   <tr class="ydcx_detail_tr2">
							<td>
							</td>
						</tr>
						 
					</thead>
				</table>
			</div>
			<%-- 内容结束 --%>
			<%-- foot开始 --%>
			<div class="bottom">
				<div id="bottom_right">
					<div id="bottom_left"></div>
					<div id="bottom_main">
					</div>
				</div>
			</div>
			<%-- foot结束 --%>
		</div>
		<div class="clear"></div>
		<script>
			var serverName = "<%=serverName%>";
		</script>
		 <script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
        <script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
        <script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
        <script type="text/javascript" src="<%=commonPath%>/common/js/jquery.ztree-myjquery-b.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/mmsPreview.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=iPath%>/js/mmsDepUserTree.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=iPath%>/js/mmsTimerTask.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/ydcx_<%=langName%>.js"></script>
		<script type="text/javascript">
		$(document).ready(function() {
			var findresult="<%=request.getAttribute("findresult")%>";
		    if(findresult != null && findresult !="" && findresult=="-1")
		    {
		       //alert("加载页面失败，请检查网络是否正常！");	
		       alert(getJsLocaleMessage("ydcx","ydcx_cxyy_dsxxck_jzymsb"));	
		       return;			       
		    }
			initPage(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>);
			$('#search').click(function(){submitForm();});
		});
	</script>
	</body>
</html>
