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
	String menuCode = titleMap.get("mmsTask");
	menuCode = menuCode==null?"0-0-0":menuCode;
	@ SuppressWarnings("unchecked")
	List<LfMttaskVo2> mtList = (List<LfMttaskVo2>)request.getAttribute("mtList");
	//@ SuppressWarnings("unchecked")
	//LinkedHashMap<String, String> conditionMap = (LinkedHashMap<String, String>)request.getAttribute("conditionMap");
	java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	//String reState = conditionMap.get("reState")==null?"":conditionMap.get("reState");
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
	String skin = session.getAttribute("stlyeSkin")==null?"default":
		(String)session.getAttribute("stlyeSkin");
	String taskid = (String)request.getParameter("taskid");
		
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
    String flowId = request.getParameter("flowId");
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
%>
<html>
	<head>
		<%@include file="/common/common.jsp" %>
		<title><emp:message key="ydcx_cxyy_fsxxck_cxrwcx" defVal="彩信任务查询" fileName="ydcx"></emp:message></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=commonPath %>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
		<link rel="stylesheet" href="<%=commonPath %>/common/widget/zTreeStyle/zTreeStyle.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css"/>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" href="<%=iPath%>/css/mmsTask.css?V=<%=StaticValue.getJspImpVersion() %>">
		<style type="text/css">
			#content tr.peachpuff,#content tr.peachpuff td{background:#FFDAB9;}
		</style>
		
		<%if(StaticValue.ZH_HK.equals(langName)){%>	
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
			<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
		<link rel="stylesheet" href="<%=skin %>/ydcx_applicationMms.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
	</head>
	<body id="ydcx_mmsTask">
	<input type="hidden" id="pathUrl" value="<%=path %>" />
	<input type="hidden" id="ipathUrl" value="<%=inheritPath %>"/>
	<input type="hidden" id="commonPath" value="<%=commonPath %>"/>
	
		<div id="container" class="container">
			<%-- header开始 --%>
		<%--<%=ViewParams.getPosition(menuCode) %>
		--%><%=ViewParams.getPosition(langName,menuCode) %>
			
		<div id="modify" title="<emp:message key="ydcx_cxyy_fsxxck_shyj" defVal="审核意见" fileName="ydcx"></emp:message>"  class="ydcx_modify">
				<div id="msg" class="ydcx_msg"><xmp></xmp></div>
		</div>
			<%-- header结束 --%>
		<div id="tempView" title="<emp:message key="ydcx_cxyy_fsxxck_cxnr" defVal="彩信内容" fileName="ydcx"></emp:message>" class="ydcx_tempView">
		
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
			<div id="mmsdetailinfo" title="<emp:message key="ydcx_cxyy_fsxxck_xxxx" defVal="详细信息" fileName="ydcx"></emp:message>" class="ydcx_mmsdetailinfo">
<%--			  <table class="table01">--%>
<%--				<tr>--%>
<%--					<td class="t1 div_bd div_bg">彩信主题：</td>--%>
<%--					<td id="mms_taskname" class="div_bd"></td>--%>
<%--					<td class="t1 div_bd div_bg">彩信标题：</td>--%>
<%--					<td id="mms_title" class="div_bd"></td>--%>
<%--				</tr>--%>
<%--				<tr>--%>
<%--					<td class="t1 div_bd div_bg">号码总数：</td>--%>
<%--					<td id="mms_count" class="div_bd"></td>--%>
<%--					<td class="t1 div_bd div_bg">有效号码数：</td>--%>
<%--					<td id="mms_effcount" class="div_bd"></td>--%>
<%--				</tr>--%>
<%--				<tr>--%>
<%--					<td class="t1 div_bd div_bg">创建时间：</td>--%>
<%--					<td colspan="3" id="mms_time" class="div_bd"></td>--%>
<%--				</tr>--%>
<%--	          </table>--%>
				<div class="ydcx_record_dv" id="recordTableDiv" align="center">
					<table width="95%" id="recordTable"  class="ydcx_recordTable">
					</table>
				</div>
				<div class="ydcx_nextrecordmgs" id="nextrecordmgs" align="left">
				</div>
			</div>	
		
			<div id="reviewflowinfo" title="<emp:message key="ydcx_cxyy_fsxxck_dsp" defVal="待审批" fileName="ydcx"></emp:message>" class="ydcx_reviewflowinfo">
				<div class="ydcx_reviewTableDiv" id="reviewTableDiv" align="center">
					<table width="95%" id="reviewTable" class="ydcx_reviewTable">
					</table>
				</div>
				<div class="ydcx_nextreviewmgs" id="nextreviewmgs" align="left">
				</div>
			</div>	
		
		
		
		
			<%-- 内容开始 --%>
			<%if(btnMap.get(menuCode+"-0")!=null) { %>
			<div id="rContent" class="rContent">
			<form name="pageForm" action="mmt_mmsTask.htm?method=find" method="post"
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
								<tbody>
									<tr>
										<td>
											<emp:message key="ydcx_cxyy_fsxxck_dsjg_mh" defVal="隶属机构：" fileName="ydcx"></emp:message>
										</td>									
										<td class="condi_f_l">	
												
										  		<div class="ydcx_dep_dv">	 
										  		
										  		 <input type="hidden" id="deptid" name="depid" value="<%=deptid==null?"":deptid%>"/> 		
										  		<%--<input type="text" id="depNam" name="depNam" class="treeInput" value="<%=depNam==null?"请选择":depNam%>"  onclick="javascript:showMenu();"  readonly style="width:160px;height:20px;border: 1px solid #7F9DB9;vertical-align: bottom;cursor: pointer;"/>
												--%><input type="text" id="depNam" name="depNam" class="treeInput ydcx_depNam" value="<%=depNam==null?MessageUtils.extractMessage("ydcx","ydcx_cxyy_common_text_13",request):depNam%>"  onclick="javascript:showMenu();"  readonly />
												</div>														
												<div id="dropMenu" class="ydcx_dropMenu">
												<iframe class="ydcx_iframe" frameborder="0" src="about:blank"></iframe>	
													<div align="center" class="ydcx_confirm_reset">
														<input type="checkbox" id="isContainsSun" name="isContainsSun" <%if(isContainsSun.equals("1")){%>checked="checked" <%}%> value="1" class="ydcx_isContainsSun"/><emp:message key="ydcx_cxyy_fsxxck_bhzjg" defVal="包含子机构" fileName="ydcx"></emp:message>&nbsp;&nbsp;
														<input type="button" value="<emp:message key="ydcx_cxyy_common_btn_7" defVal="确定" fileName="ydcx"></emp:message>" class="btnClass1" onclick="javascript:zTreeOnClickOK3();" />&nbsp;&nbsp;
														<input type="button" value="<emp:message key="ydcx_cxyy_common_btn_20" defVal="清空" fileName="ydcx"></emp:message>" class="btnClass1" onclick="cleanSelect_dep3();" />
													</div>	
													<ul id="dropdownMenu" class="tree ydcx_tree"></ul>	
												</div>			
										</td>
										<td>
											<emp:message key="ydcx_cxyy_fsxxck_czy_mh" defVal="操作员：" fileName="ydcx"></emp:message>
										</td>
										<td class="condi_f_l">	
												<div class="ydcx_usertree_dv">
												
												 <input type="hidden" id="userid" name="userid" value="<%=userid==null?"":userid %>"/>
												<%--<input type="text" id="userName" name="userName" class="treeInput" value="<%=userName==null?"请选择":userName%>" readonly onclick="javascript:showMenu2();" style="width:160px;cursor: pointer;"/>&nbsp;
												--%><input type="text" id="userName" name="userName" class="treeInput" value="<%=userName==null?MessageUtils.extractMessage("ydcx","ydcx_cxyy_common_text_13",request):userName%>" readonly onclick="javascript:showMenu2();"/>&nbsp;
												<%--<a onclick="javascript:showMenu2();" style="cursor: pointer;text-decoration: underline">选择</a>--%>
												</div>											
												<div id="dropMenu2">
												<iframe class="ydcx_dropMenu2_iframe" frameborder="0" src="about:blank"></iframe>	
													<div align="center" class="ydcx_dropMenu2_confirmcancle">
														<input type="button" value="<emp:message key="ydcx_cxyy_common_btn_7" defVal="确定" fileName="ydcx"></emp:message>" class="btnClass1" onclick="javascript:zTreeOnClickOK2();" />&nbsp;&nbsp;
														<input type="button" value="<emp:message key="ydcx_cxyy_common_btn_20" defVal="清空" fileName="ydcx"></emp:message>" class="btnClass1" onclick="javascript:cleanSelect_dep();" />
													</div>
													<div class="ydcx_note_dv"><font class="zhu"><emp:message key="ydcx_cxyy_fsxxck_zqgxcxyjxcx" defVal="注：请勾选操作员进行查询" fileName="ydcx"></emp:message></font></div>	
													<ul  id="dropdownMenu2" class="tree"></ul>	
												</div>										   										
										</td>
										<td><emp:message key="ydcx_cxyy_fsxxck_tjwgzt_mh" defVal="提交网关状态：" fileName="ydcx"></emp:message></td><td><select id="sstate" name="sstate" class="ydcx_sstate" isInput="false">
										<option value=""><emp:message key="ydcx_cxyy_common_text_16" defVal="全部" fileName="ydcx"></emp:message></option>
										<option value="0" <%="0".equals(sState)?"selected=selected":"" %>><emp:message key="ydcx_cxyy_fsxxck_wtj" defVal="未提交" fileName="ydcx"></emp:message></option> 
										<option value="1" <%="1".equals(sState)?"selected=selected":"" %>><emp:message key="ydcx_cxyy_fsxxck_tjcg" defVal="提交成功" fileName="ydcx"></emp:message></option>
										<option value="2" <%="2".equals(sState)?"selected=selected":"" %>><emp:message key="ydcx_cxyy_fsxxck_tjsb" defVal="提交失败" fileName="ydcx"></emp:message></option>
										<option value="3" <%="3".equals(sState)?"selected=selected":"" %>><emp:message key="ydcx_cxyy_fsxxck_ycx" defVal="已撤销" fileName="ydcx"></emp:message></option>
										<option value="5" <%="5".equals(sState)?"selected=selected":"" %>><emp:message key="ydcx_cxyy_fsxxck_cswfs" defVal="超时未发送" fileName="ydcx"></emp:message></option>
										</select></td>
										<td class="tdSer">
													<center><a id="search"></a></center>
										</td>
									</tr>
									<tr>
										<td>
											<emp:message key="ydcx_cxyy_fsxxck_zt_mh" defVal="主题：" fileName="ydcx"></emp:message>
										</td>
										<td>
											<input type="text" value='<%=mtVo.getTaskName()==null?"":mtVo.getTaskName() %>' id="taskName" name="taskName" class="ydcx_taskName" />
										</td>
										<td>
											<emp:message key="ydcx_cxyy_fsxxck_cxbt_mh" defVal="彩信标题：" fileName="ydcx"></emp:message>
										</td>
										<td>
											<input type="text" value='<%=mtVo.getTitle()==null?"":mtVo.getTitle() %>' id="theme" name="theme" class="ydcx_theme"/>
										</td>
										<td><emp:message key="ydcx_cxyy_fsxxck_spzt_mh" defVal="审批状态：" fileName="ydcx"></emp:message></td>
										<td>
										<select id="state" name="state" class="ydcx_state" isInput="false">
											<option value=""><emp:message key="ydcx_cxyy_common_text_16" defVal="全部" fileName="ydcx"></emp:message></option>
											<option value="0" <%="0".equals(mtVo.getReState()+"")?"selected=selected":"" %>><emp:message key="ydcx_cxyy_fsxxck_wxsp" defVal="无需审批" fileName="ydcx"></emp:message></option> 
											<option value="-1" <%="-1".equals(mtVo.getReState()+"")?"selected=selected":"" %>><emp:message key="ydcx_cxyy_fsxxck_wsp" defVal="未审批" fileName="ydcx"></emp:message></option>
											<option value="1" <%="1".equals(mtVo.getReState()+"")?"selected=selected":"" %>><emp:message key="ydcx_cxyy_fsxxck_tg" defVal="通过" fileName="ydcx"></emp:message></option>
											<option value="2" <%="2".equals(mtVo.getReState()+"")?"selected=selected":"" %>><emp:message key="ydcx_cxyy_fsxxck_btg" defVal="不通过" fileName="ydcx"></emp:message></option>
										</select>
										</td>
											<td>
										</td>	
									</tr>
									<tr>
										<td>
											<emp:message key="ydcx_cxyy_fsxxck_cjsj_mh" defVal="创建时间：" fileName="ydcx"></emp:message>
										</td>
										<td>
											<input type="text" value='<%=mtVo.getStartSubmitTime()==null?"":mtVo.getStartSubmitTime() %>' id="submitSartTime" name="submitSartTime" class="Wdate ydcx_submitSartTime" readonly="readonly" onclick="stime()">
										</td>
										<td align="left">
											<emp:message key="ydcx_cxyy_common_text_15" defVal="至：" fileName="ydcx"></emp:message>
										</td>
										<td>
											<input type="text" value='<%=mtVo.getEndSubmitTime()==null?"":mtVo.getEndSubmitTime() %>' id="submitEndTime" name="submitEndTime" class="Wdate ydcx_submitEndTime" readonly="readonly" onclick="rtime()">
										</td>
	
	                                    <%--<td><%if(flowId != null){out.print("审核流程ID：");}%></td>
	                                    --%><td><%if(flowId != null){out.print(MessageUtils.extractMessage("ydcx","ydcx_cxyy_fsxxck_splcid_mh",request));}%></td>
	                                    <td>
	                                        <%if(flowId != null){
	                                        %>
	                                        <input type="text" name="flowId" class="ydcx_flowId" value="<%=flowId%>" onkeyup="javascript:numberControl($(this))" maxlength="19"/>
	                                        <% }%>
	                                    </td>
										<td></td>
									</tr>
							</tbody>
						</table>
					</div>
					<table id="content">
						<thead>
							<tr>
								<th width="4.5%">
									<emp:message key="ydcx_cxyy_fsxxck_czy" defVal="操作员" fileName="ydcx"></emp:message>
								</th>
								<th width="5.8%">
									<emp:message key="ydcx_cxyy_fsxxck_dsjg" defVal="隶属机构" fileName="ydcx"></emp:message>
								</th>
								<th width="4.2%">
									<emp:message key="ydcx_cxyy_fsxxck_zt" defVal="主题" fileName="ydcx"></emp:message>
								</th>
								<th width="5.4%">
									<emp:message key="ydcx_cxyy_fsxxck_cxbt" defVal="彩信标题" fileName="ydcx"></emp:message>
								</th>
								<th width="11%">
									<emp:message key="ydcx_cxyy_fsxxck_cjsj" defVal="创建时间" fileName="ydcx"></emp:message>
								</th>
								<th width="14%">
									<emp:message key="ydcx_cxyy_fsxxck_fssj" defVal="发送时间" fileName="ydcx"></emp:message>
								</th>
								<th width="7.4%">
									<emp:message key="ydcx_cxyy_fsxxck_tjzs" defVal="提交总数" fileName="ydcx"></emp:message>
								</th>
								<th width="6.8%">
									<emp:message key="ydcx_cxyy_fsxxck_fscgs" defVal="发送成功数" fileName="ydcx"></emp:message>
								</th>
								<th width="6.8%">
									<emp:message key="ydcx_cxyy_fsxxck_tjsbs" defVal="提交失败数" fileName="ydcx"></emp:message>
								</th>
								<%-- 
								<th>
									定时时间
								</th>--%>	
								<th width="8.6%">
									<emp:message key="ydcx_cxyy_fsxxck_spzt" defVal="审批状态" fileName="ydcx"></emp:message>
								</th>
								<th width="10.2%">
									<emp:message key="ydcx_cxyy_fsxxck_tjwgzt" defVal="提交网关状态" fileName="ydcx"></emp:message>
								</th>
								<th width="8.5%">
									<emp:message key="ydcx_cxyy_fsxxck_fswj" defVal="发送文件" fileName="ydcx"></emp:message>
								</th>
								<th width="6.7%" colspan="2">
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
									   if(taskid != null && !"".equals(taskid) && Long.valueOf(taskid) - lm.getTaskId() == 0)
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
											<%--<%=lm.getName() %><%if(lm.getUserState()==2){out.print("<font color='red'>(已注销)</font>");} %>
											--%><%=lm.getName() %><%if(lm.getUserState()==2){out.print("<font color='red'>("+MessageUtils.extractMessage("ydcx","ydcx_cxyy_fsxxck_yzx",request)+")</font>");} %>
											</td>
											<td class="textalign">
											<a onclick="javascript:detail(this,1)" title="<%=lm.getDepName().replace("&","&amp;").replace("<","&lt;").replace(">","&gt;") %>">
											  <label style="display:none"><xmp><%=lm.getDepName()%></xmp></label>
														<xmp><%=lm.getDepName().length()>5?lm.getDepName().substring(0,5)+"...":lm.getDepName() %></xmp>
											  </a> 
											</td>
											<td class="textalign">
											<a onclick="javascript:detail(this,2)" title="<%=lm.getTaskName()!=null?lm.getTaskName().replace("&","&amp;").replace("<","&lt;").replace(">","&gt;"):"" %>">
											  <label style="display:none"><xmp><%=lm.getTaskName()%></xmp></label>
														<xmp><%=lm.getTaskName()!=null?lm.getTaskName().length()>5?lm.getTaskName().substring(0,5)+"...":lm.getTaskName():"" %></xmp>
											  </a> 
											
											</td>
											<td class="textalign">
											<%if(lm.getMsg()!= null){ %>
											    <a onclick="javascript:doPreview('<%=lm.getMsg() %>','<%=lm.getBmtType() %>','<%=lm.getTmplPath()%>')"><%=lm.getTitle()!=null?lm.getTitle().replace("<","&lt;").replace(">","&gt;"):"-" %></a>
											    <%}else{ %>
											<%=lm.getTitle()!=null?lm.getTitle().replace("<","&lt;").replace(">","&gt;"):"-" %> <%} %>
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
											    if(lm.getSendstate()==0){out.print("("+MessageUtils.extractMessage("ydcx","ydcx_cxyy_fsxxck_dsz",request)+")");}
											}else if(lm.getSendstate()==1 || lm.getSendstate()==2){//发送成功或者发送失败
												out.print(df.format(lm.getTimerTime()));
											}else{
												out.print(lm.getTimerTime()==null?"-":df.format(lm.getTimerTime()));//这里面的情况就是sendstate=4(发送中)
											}
											%>
										</td>
											<td class="textalign">
											<%=lm.getEffCount() %>
											</td>
											<td class="ztalign">
												<% 
													String fail_count=(lm.getFaiCount()==null?"0":lm.getFaiCount());
													String icount=(lm.getIcount2()==null?"0":lm.getIcount2());
													//提交总数
													Long icount1=Long.parseLong(icount);
													//提交失败总数
													Long fail=Long.parseLong(fail_count);
													long suc=icount1-fail;
													if(lm.getSendstate()==2)
													{
														suc=0;
														out.print(suc);
													}
													else if(lm.getIcount2()==null)
													{
														out.print("-");
													}
													else
													{
														out.print(suc);
													}
												
												%>
											</td>
											<td class="ztalign">
												<%=lm.getSendstate()==2?(lm.getIcount()==null?0:lm.getIcount()):(lm.getIcount2()==null?"-":lm.getFaiCount()) %>
											</td>
											<td  class="ztalign"><%
											    String s="";
												switch(lm.getReState())
												{
												case 0:
													//s="无需审批";
													s=MessageUtils.extractMessage("ydcx","ydcx_cxyy_fsxxck_wxsp",request);
													//out.print("无需审批");
													break;
												case -1:
												    //s="未审批";
												    s=MessageUtils.extractMessage("ydcx","ydcx_cxyy_fsxxck_wsp",request);
													//out.print("未审批");
													break;
												case 1:
												    //s="通过";
												    s=MessageUtils.extractMessage("ydcx","ydcx_cxyy_fsxxck_tg",request);
													//out.print("通过");
													break;
												case 2:
												    //s="不通过";
												    s=MessageUtils.extractMessage("ydcx","ydcx_cxyy_fsxxck_btg",request);
													//out.print("不通过");
													break;
												default:
												    //s="[无效的标示]";
												    s=MessageUtils.extractMessage("ydcx","ydcx_cxyy_fsxxck_wxdbs",request);
													//out.print("[无效的标示]");
												}
											%>
											<%if(lm.getReState()==1||lm.getReState()==2){ %>
												<a onclick="javascript:openMmsDetail('<%=lm.getMtId()%>','<%=lm.getUserId()%>');"><%=s%></a>
											<%}else if(lm.getReState()==-1){ %>
												<a onclick="javascript:openReviewFlow('<%=lm.getMtId()%>','<%=lm.getUserId()%>','2');"  name="querySP"><%=s%></a>
											<% }else{%>
												<%=s%>
											<%} %>
											</td>
											<td class="ztalign">
											<%
												switch(lm.getSendstate())
												{
												//case 0: String st = "未提交";
												case 0: String st = MessageUtils.extractMessage("ydcx","ydcx_cxyy_fsxxck_wtj",request);
												    //if(lm.getSubState()== 2){st = "未提交";}
												    if(lm.getSubState()== 2){st = MessageUtils.extractMessage("ydcx","ydcx_cxyy_fsxxck_wtj",request);}
												    //else if(lm.getSubState()== 3){st = "已撤销";}
												    else if(lm.getSubState()== 3){st = MessageUtils.extractMessage("ydcx","ydcx_cxyy_fsxxck_ycx",request);}
													out.print(st);
													break;
												case 1:
													//out.print("提交成功");
													out.print(MessageUtils.extractMessage("ydcx","ydcx_cxyy_fsxxck_tjcg",request));
													break;
												case 2:
													//out.print("提交失败");
													out.print(MessageUtils.extractMessage("ydcx","ydcx_cxyy_fsxxck_tjsb",request));
													break;
												case 5:
													//out.print("超时未发送");
													out.print(MessageUtils.extractMessage("ydcx","ydcx_cxyy_fsxxck_cswfs",request));
													break;
												default:
													//out.print("[无效的标示]");
													out.print(MessageUtils.extractMessage("ydcx","ydcx_cxyy_fsxxck_wxdbs",request));
												}
											 %>
											</td>
											<td>
											<% 
												if("2".equals(isHideNumber)){
													  %>
											  			<a href="javascript:checkFileOuter('<%=lm.getMobileUrl()%>','<%=request.getContextPath()%>')"><emp:message key="ydcx_cxyy_fsxxck_ck" defVal="查看" fileName="ydcx"></emp:message></a>
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
											    out.print(MessageUtils.extractMessage("ydcx","ydcx_cxyy_fsxxck_ycf",request));
											}else if((lm.getReState() ==1 || lm.getReState() == 0) && lm.getSendstate() == 2 && lm.getUserId().equals(loginUser.getUserId())){
											   	//out.print("<a onclick='reSend("+lm.getMtId()+");'>失败重发</a>");
											   	out.print("<a onclick='reSend("+lm.getMtId()+");'>"+MessageUtils.extractMessage("ydcx","ydcx_cxyy_fsxxck_sbcf",request)+"</a>");
											}else{
											   out.print("&nbsp - &nbsp");
											}
										 %>
										</td>
										<td>
										<%
										    if(lm.getReState()==2){
										       //out.print("无需撤销");
										       out.print(MessageUtils.extractMessage("ydcx","ydcx_cxyy_fsxxck_wxcx",request));
										    }else if(lm.getSubState()==2) {
										       if(lm.getReState()==1||lm.getReState()==0 || lm.getIsRetry()==1){
										    %> &nbsp - &nbsp <%
										       }else{%>
										       <a href="javascript:doCancel(<%=lm.getMtId() %>)"><emp:message key="ydcx_cxyy_fsxxck_cxrw" defVal="撤销任务" fileName="ydcx"></emp:message></a>
									         <%}
									        }else if(lm.getSubState()==3){
									           out.print("&nbsp - &nbsp");
									        } %>
										</td> 
<%--										<td>--%>
<%--											<%--	<a href="mmt_mmsTask.htm?method=getDetail&mtId=<%=lm.getMtId() %>&type=send&userId=<%=lm.getUserId() %>">详细</a> --%>
<%--												<%--<a onclick="javascript:openMmsDetail('<%=//lm.getMtId() %>','<%=//lm.getUserId() %>');">详细</a>--%>
<%--										</td>--%>
										</tr>
										<%}
										}
										else{%>
										<tr><td colspan="14"><emp:message key="ydcx_cxyy_common_text_1" defVal="无记录" fileName="ydcx"></emp:message></td></tr>
										<%
										}
										%>
								</tbody>
								<tfoot>
							<tr>
								<td colspan="14">
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
		<script type="text/javascript" src="<%=iPath%>/js/mmsTask.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=iPath%>/js/mmsDepUserTree.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/ydcx_<%=langName%>.js"></script>
		<script type="text/javascript">
		$(document).ready(function() {
			var findresult="<%=request.getAttribute("findresult")%>";
		    if(findresult != null && findresult !="" && findresult=="-1")
		    {
		       //alert("加载页面失败，请检查网络是否正常！");
		       alert(getJsLocaleMessage("ydcx","ydcx_cxyy_fsxxck_jzymsb"));		
		       return;			       
		    }
			initPage(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>);
			$('#search').click(function(){submitForm();});
		});
	</script>
	</body>
</html>
