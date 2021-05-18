<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.common.constant.ViewParams"%>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils" %>
<%@ page import="com.montnets.emp.samemms.vo.LfTemplateVo" %>
<%@page import="com.montnets.emp.servmodule.ydcx.constant.ServerInof"%>
<%@page import="com.montnets.emp.util.PageInfo"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String iPath = request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	PageInfo pageInfo = new PageInfo();
	pageInfo = (PageInfo) request.getAttribute("pageInfo");
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("mmsTemplate");
	menuCode = menuCode==null?"0-0-0":menuCode;
	java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	@ SuppressWarnings("unchecked")
	List<LfTemplateVo> mmsList = (List<LfTemplateVo>)request.getAttribute("mmsList");
	//@ SuppressWarnings("unchecked")
	//List<LfSysuser> sysList = (List<LfSysuser>)session.getAttribute("sysList");
	LfTemplateVo mt = (LfTemplateVo)request.getAttribute("mmsVo");
	int rState = mt.getIsPass()==null?-2:mt.getIsPass();
	long state = mt.getTmState()==null?-2:mt.getTmState();
	int auditStatus = mt.getAuditstatus()==null?-2:mt.getAuditstatus();
	int submitstatus = mt.getSubmitstatus() == null?-2:mt.getSubmitstatus();
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	//服务器名称
	String serverName = ServerInof.getServerName();
    String flowId = request.getParameter("flowId");
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
%>
<html>
	<head>
		<%@include file="/common/common.jsp" %>
		<title></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		
		<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
		<%if(StaticValue.ZH_HK.equals(langName)){%>	
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
			<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
		<link href="<%=iPath%>/css/mmsTemplate.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin%>/ydcx_applicationMms.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
	</head>
	<body id="ydcx_mmsTemplate">
		<input type="hidden" id="pathUrl" value="<%=path %>" />
	    <input type="hidden" id="ipathUrl" value="<%=inheritPath%>"/>
	    <input type="hidden" id="commonPath" value="<%=commonPath %>"/>
	    <input type="hidden" id="iPath" value="<%=iPath %>" />
		<input type="hidden" id="templType" value="2">
		<%
			if(CstlyeSkin.contains("frame4.0")){
		%>
			<input id='hasBeenBind' value='1' type='hidden'/>
		<%
			}
		 %>
		<div id="container" class="container">
			<%-- header开始 --%>
		<%--<%=ViewParams.getPosition(menuCode) %>
		--%><%=ViewParams.getPosition(langName,menuCode) %>
			<%-- header结束 --%>
		<div id="modify" title="<emp:message key="ydcx_cxyy_mbbj_shyj" defVal="审核意见" fileName="ydcx"></emp:message>" class="ydcx_modify">
				<div id="msg" class="ydcx_msg"><xmp></xmp></div>
		</div>
		<%-- 预览弹层  --%>
		<div id="tempView" title="<emp:message key="ydcx_cxyy_mbbj_cxmbyl" defVal="彩信模板预览" fileName="ydcx"></emp:message>" style="display:none;overflow: auto;">
		
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
				     <label id="pointer" class="ydcx_pointer" ></label>
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
			
			<%-- 内容开始 --%>
			<%
				if(btnMap.get(menuCode+"-0")!=null) {
			%>
			<div id="rContent" class="rContent">
				<form name="pageForm" action="tem_mmsTemplate.htm?method=find" method="post"
					id="pageForm">
					<div style="display:none" id="hiddenValueDiv"></div>
					<div class="buttons">
						<div id="toggleDiv">
						</div>
						<%
							if(btnMap.get(menuCode+"-1")!=null) {
						%>
						<a id="add" onclick="doAdd()"><font class="ydcx_addFont"><emp:message key="ydcx_cxyy_common_btn_4" defVal="新建" fileName="ydcx"></emp:message></font></a>
							<%if(StaticValue.ZH_HK.equals(langName)){%>
								<a id="upload" onclick="javascript:$('#modelTms').dialog('open')"><font class="ydcx_upLoadFont"><emp:message key="ydcx_cxyy_mbbj_dr" defVal="导入" fileName="ydcx"></emp:message></font></a>
							<%}else{%>
								<a id="upload" onclick="javascript:$('#modelTms').dialog('open')"><font class="ydcx_upLoadFont"><emp:message key="ydcx_cxyy_mbbj_dr" defVal="导入" fileName="ydcx"></emp:message></font></a>
							<%}%>
						<%
							}
						%>
						<%
							if(btnMap.get(menuCode+"-2")!=null) {
						%>
						<a id="delete" onclick="javascript:delAll('<%=path %>')"><font class="ydcx_deleteFont"><emp:message key="ydcx_cxyy_common_text_8" defVal="删除" fileName="ydcx"></emp:message></font></a>
						<%
							}
						%>
					</div>
					<div id="getloginUser" class="ydcx_getloginUser">
					</div>
					<div id="condition">
						<table>
						<tbody>
							<tr>
									<td align="left">
											<emp:message key="ydcx_cxyy_mbbj_mbbh_mh" defVal="模板编号：" fileName="ydcx"></emp:message>
											</td>
											<td >
												<input type="text" name="tmCode" 
													id="tmCode" value="<%=mt.getTmCode()==null?"":mt.getTmCode()%>" class="ydcx_tmCode"/>
											</td>
											<td align="left">
											<emp:message key="ydcx_cxyy_mbbj_mbmc_mh" defVal="模板名称：" fileName="ydcx"></emp:message>
											</td>
											<td >
												<input type="text" name="theme" 
													id="tmName" value="<%=mt.getTmName()==null?"":mt.getTmName()%>" class="ydcx_theme" />
											</td>

											<td><emp:message key="ydcx_cxyy_mbbj_mblx_mh" defVal="模板类型：" fileName="ydcx"></emp:message></td>
											<td><select id="dsFlag" name="dsFlag" class="ydcx_dsFlag" isInput="false">
											<option value=""><emp:message key="ydcx_cxyy_common_text_16" defVal="全部" fileName="ydcx"></emp:message></option>
											<option value="0" <%=mt.getDsflag()!=null && mt.getDsflag()==0?"selected":""%>><emp:message key="ydcx_cxyy_mbbj_tyjtmb" defVal="通用静态模板" fileName="ydcx"></emp:message></option>
											<option value="1" <%=mt.getDsflag()!=null && mt.getDsflag()==1?"selected":""%>><emp:message key="ydcx_cxyy_mbbj_tydtmb" defVal="通用动态模板" fileName="ydcx"></emp:message></option>
											</select></td>
													<td class="tdSer">
												<center><a id="search"></a></center>
											</td>
										</tr>
										<tr>
										
									    <td><emp:message key="ydcx_cxyy_mbbj_mbzt_mh" defVal="模板状态：" fileName="ydcx"></emp:message></td>
									    <td><select id="state" name="state" class="ydcx_state" isInput="false">
									    <option value=""><emp:message key="ydcx_cxyy_common_text_16" defVal="全部" fileName="ydcx"></emp:message></option>
											<option value="1" <%=state-1==0?"selected":""%>><emp:message key="ydcx_cxyy_mbbj_qy" defVal="启用" fileName="ydcx"></emp:message></option>
											<option value="0" <%=state+0==0?"selected":""%>><emp:message key="ydcx_cxyy_mbbj_jy" defVal="禁用" fileName="ydcx"></emp:message></option>
											<option value="2" <%=state-2==0?"selected":""%>><emp:message key="ydcx_cxyy_mbbj_cg" defVal="草稿" fileName="ydcx"></emp:message></option>
											</select></td>
										<td><emp:message key="ydcx_cxyy_mbbj_empspzt_mh" defVal="EMP审批状态：" fileName="ydcx"></emp:message></td>
											<td><select id="rState" name="rState" isInput="false">
											<option value=""><emp:message key="ydcx_cxyy_common_text_16" defVal="全部" fileName="ydcx"></emp:message></option>
											<option value="-1" <%=rState+1==0?"selected":""%>><emp:message key="ydcx_cxyy_mbbj_wsp" defVal="未审批" fileName="ydcx"></emp:message></option>
											<option value="0" <%=rState+0==0?"selected":""%>><emp:message key="ydcx_cxyy_mbbj_wxsp" defVal="无需审批" fileName="ydcx"></emp:message></option>
											<option value="1" <%=rState-1==0?"selected":""%>><emp:message key="ydcx_cxyy_mbbj_sptg" defVal="审批通过" fileName="ydcx"></emp:message></option>
											<option value="2" <%=rState-2==0?"selected":""%>><emp:message key="ydcx_cxyy_mbbj_spwtg" defVal="审批未通过" fileName="ydcx"></emp:message></option>
											</select></td>
										<td><emp:message key="ydcx_cxyy_mbbj_yysspzt_mh" defVal="运营商审批状态：" fileName="ydcx"></emp:message></td>
										<td><select id="auditStatus" name="auditStatus" isInput="false">
										<option value=""><emp:message key="ydcx_cxyy_common_text_16" defVal="全部" fileName="ydcx"></emp:message></option>
										<option value="0" <%=auditStatus==0?"selected":""%>><emp:message key="ydcx_cxyy_mbbj_wsp" defVal="未审批" fileName="ydcx"></emp:message></option>
										<%--<option value="0" <%=auditStatus==0?"selected":""%>>无需审批</option>
										--%><option value="1" <%=auditStatus==1?"selected":""%>><emp:message key="ydcx_cxyy_mbbj_sptg" defVal="审批通过" fileName="ydcx"></emp:message></option>
										<option value="2" <%=auditStatus==2?"selected":""%>><emp:message key="ydcx_cxyy_mbbj_spwtg" defVal="审批未通过" fileName="ydcx"></emp:message></option>
										</select></td>
											<td></td>
									   </tr>
									   <tr><td><emp:message key="ydcx_cxyy_mbbj_yystjzt_mh" defVal="运营商提交状态：" fileName="ydcx"></emp:message></td>
										<td><select id="submitstatus" name="submitstatus" isInput="false">
										<option value=""><emp:message key="ydcx_cxyy_common_text_16" defVal="全部" fileName="ydcx"></emp:message></option>
										<option value="0" <%=submitstatus==0?"selected":""%>><emp:message key="ydcx_cxyy_mbbj_wtj" defVal="未提交" fileName="ydcx"></emp:message></option>
										<option value="1" <%=submitstatus==1?"selected":""%>><emp:message key="ydcx_cxyy_common_text_11" defVal="成功" fileName="ydcx"></emp:message></option>
										<option value="2" <%=submitstatus==2?"selected":""%>><emp:message key="ydcx_cxyy_common_text_12" defVal="失败" fileName="ydcx"></emp:message></option>
										</select></td>
										<td >
												<emp:message key="ydcx_cxyy_mbbj_csrxm_mh" defVal="创建人姓名：" fileName="ydcx"></emp:message>
                                        </td>
                                        <td>
                                        <input type="text" name="userName"
                                                id="userName" value="<%=mt.getName()==null?"":mt.getName()%>"/>
                                        </td>
                                       <%--<td><%if(flowId != null){out.print("审核流程ID：");}%></td>
                                       --%><td><%if(flowId != null){out.print(MessageUtils.extractMessage("ydcx","ydcx_cxyy_mbbj_splcid_mh",request));}%></td>
                                       <td>
                                           <%if(flowId != null){
                                           %>
                                           <input type="text" name="flowId" class="ydcx_flowId" value="<%=flowId%>" onkeyup="javascript:numberControl($(this))" maxlength="19"/>
                                           <% }%>
                                       </td>
										<td></td>
										</tr></tbody>
						</table>
					</div>
					<table id="content">
						<thead>
					  <tr>
										<th width="1.9%">
											<input type="checkbox" name="checkall" id="checkall" onclick="checkAlls(this,'checklist')" />
										</th>
										<th width="3.4%">
											<emp:message key="ydcx_cxyy_mbbj_mbid" defVal="模板ID" fileName="ydcx"></emp:message>
										</th>
										<th width="5%">
											<emp:message key="ydcx_cxyy_mbbj_mbbh" defVal="模板编号" fileName="ydcx"></emp:message>
										</th>
										<th width="5%">
											<emp:message key="ydcx_cxyy_mbbj_mbmc" defVal="模板名称" fileName="ydcx"></emp:message>
										</th>
										<th width="7%">
										    <emp:message key="ydcx_cxyy_mbbj_mblx" defVal="模板类型" fileName="ydcx"></emp:message>
										</th>
										<th width="4.6%">
											<emp:message key="ydcx_cxyy_mbbj_mbnr" defVal="模板内容" fileName="ydcx"></emp:message>
										</th>
										<th width="5%">
											<emp:message key="ydcx_cxyy_mbbj_cjrxm" defVal="创建人姓名" fileName="ydcx"></emp:message>
										</th>
										<th width="5%">
											<emp:message key="ydcx_cxyy_mbbj_ssjg" defVal="所属机构" fileName="ydcx"></emp:message>
										</th>
										<th width="10%">
											<emp:message key="ydcx_cxyy_mbbj_cjrq" defVal="创建日期" fileName="ydcx"></emp:message>
										</th>
										<th width="8.5%">
										    <emp:message key="ydcx_cxyy_mbbj_empspzt" defVal="EMP审批状态" fileName="ydcx"></emp:message>
										</th>
										<th width="8.1%">
										    <emp:message key="ydcx_cxyy_mbbj_yystjzt" defVal="运营商提交状态" fileName="ydcx"></emp:message>
										</th>
										<th width="8.1%">
										    <emp:message key="ydcx_cxyy_mbbj_yysspzt" defVal="运营商审批状态" fileName="ydcx"></emp:message>
										</th>
										<th width="4.7%">
										    <emp:message key="ydcx_cxyy_mbbj_mbzt" defVal="模板状态" fileName="ydcx"></emp:message>
										</th>
										<th width="20%" colspan="5" >
											<emp:message key="ydcx_cxyy_common_text_14" defVal="操作" fileName="ydcx"></emp:message>
										</th>
										
									</tr>
								</thead>
								<tbody>
								<%
									if (mmsList != null && mmsList.size() > 0)
														{
															for (LfTemplateVo mv : mmsList)
															{
																boolean isEdit=false;
																if(mv.getShareType()==0){
																	isEdit=true;
																}
								%>
									<tr>
										<td>
										<% if(btnMap.get(menuCode+"-2")!=null&&isEdit){%>
											<input type="checkbox" name="checklist" value="<%=mv.getTmid() %>" />
										<% }else{%>
											<input type="checkbox" disabled="disabled""/>
										<%} %>
										</td>
										<td>
											<%
											    if(!"".equals(mv.getSptemplid()) && mv.getSptemplid() != null && (mv.getSptemplid()-0 != 0)){
											    %>
											    <a onclick="javascript:detail(this,1)" title="<%=mv.getSptemplid() %>">
											  <label style="display:none"><xmp><%=mv.getSptemplid()%></xmp></label>
														<xmp><%=mv.getSptemplid().toString().length()>5?mv.getSptemplid().toString().substring(0,5)+"...":mv.getSptemplid() %></xmp>
											</a>
											   <% }else{
											    	out.print("-");
											    }
											%>
										</td>
										<td>
											<%=mv.getTmCode()==null?"-": mv.getTmCode()%>
											
										</td>
										
										<td class="textalign">
											<a onclick="javascript:detail(this,2)" title="<%=mv.getTmName().replace("&","&amp;").replace("<","&lt;").replace(">","&gt;") %>">
											  <label style="display:none"><xmp><%=mv.getTmName()%></xmp></label>
														<xmp><%=mv.getTmName().length()>5?mv.getTmName().substring(0,5)+"...":mv.getTmName() %></xmp>
											</a> 
										</td>
										<td>
										   	<%
												if(mv.getDsflag()-1==0)
												{
													//out.print("通用动态模块");
													out.print(MessageUtils.extractMessage("ydcx","ydcx_cxyy_mbbj_tydtmk",request));
												}else if(mv.getDsflag()-0==0)
												{
													//out.print("通用静态模块");
													out.print(MessageUtils.extractMessage("ydcx","ydcx_cxyy_mbbj_tyjtmk",request));
												}else if(mv.getDsflag()-2==0)
												{
													//out.print("智能抓取模块");
													out.print(MessageUtils.extractMessage("ydcx","ydcx_cxyy_mbbj_znzqmk",request));
												}else if(mv.getDsflag()-3==0)
												{
													//out.print("移动财务模块");
													out.print(MessageUtils.extractMessage("ydcx","ydcx_cxyy_mbbj_ydcwmk",request));
												}
											%>
										</td>
										<td>
											<%
											 if (mv.getTmMsg() != null)
											 {
											%>
											<a onclick="doPreview('<%=mv.getTmMsg() %>',<%=mv.getDsflag() %>)"> <emp:message key="ydcx_cxyy_common_text_6" defVal="预览" fileName="ydcx"></emp:message></a>
											<%} %>
										</td>
										<td class="textalign">
											<%if(mv.getUserState() !=null && mv.getUserState() ==2)
											    {
											      //out.print(mv.getName()+"<font color='red'>(已注销)</font>");
											      out.print(mv.getName()+"<font color='red'>("+MessageUtils.extractMessage("ydcx","ydcx_cxyy_mbbj_yzx",request)+")</font>");
											    }
											    else
											    {
											       out.print(mv.getName());
											    }
											  %>
										</td>
										<td>
											<a onclick="javascript:detail(this,3)" title="<%=mv.getDepName().replace("&","&amp;").replace("<","&lt;").replace(">","&gt;") %>">
											  <label style="display:none"><xmp><%=mv.getDepName()%></xmp></label>
														<xmp><%=mv.getDepName().length()>5?mv.getDepName().substring(0,5)+"...":mv.getDepName() %></xmp>
											</a> 
										</td>
										<td>
											<%=df.format(mv.getAddtime()) %>
										</td>
										<td class="ztalign">
										<%
										String s = "";
										//boolean flag = false;
										int intFlag=-2;
										if(mv.getTmState()==0L||mv.getTmState()==1L){
										if (mv.getIsPass()== -1)
											 {
											     //s = "未审批";
											     s = MessageUtils.extractMessage("ydcx","ydcx_cxyy_mbbj_wsp",request);
											     //flag = true;
											     intFlag=-1;
										     }else if(mv.getIsPass()== 0)
											 {
											     //s = "无需审批";
											     s = MessageUtils.extractMessage("ydcx","ydcx_cxyy_mbbj_wxsp",request);
											     intFlag=0;
										     }else if(mv.getIsPass()== 1)
											 {
											     //s = "审批通过";
											     s = MessageUtils.extractMessage("ydcx","ydcx_cxyy_mbbj_sptg",request);
											     //flag = true;
											      intFlag=1;
										     }else if(mv.getIsPass()== 2)
										     {
										    	 //s = "审批未通过";
										    	 s = MessageUtils.extractMessage("ydcx","ydcx_cxyy_mbbj_spwtg",request);
										    	 //flag = true;
										    	 intFlag=2;
										     }
										}else{
											  s = "-";
											  intFlag=-2;
									     }
										%>
										<%
											if(intFlag==1||intFlag==2){
												%>
													<a href="javascript:opentmpAudmsg('<%=mv.getTmid() %>')"><%=s%></a>
												<%
											}else if(intFlag==-1){
												%>
													<a href="javascript:openReviewFlow('<%=mv.getTmid() %>','<%=mv.getUserId()%>','4')"><%=s%></a>
												<%
											}else{
												out.print(s);
											}
										%>
										</td>
										<td class="ztalign">
											<%
												if(mv.getSubmitstatus() == 1){
													//out.print("成功");
													out.print(MessageUtils.extractMessage("ydcx","ydcx_cxyy_common_text_11",request));
												}else if(mv.getSubmitstatus() == 2){
													//out.print("失败["+mv.getErrorcode()+"]");
													out.print(MessageUtils.extractMessage("ydcx","ydcx_cxyy_common_text_12",request)+"["+mv.getErrorcode()+"]");
											    }else if(mv.getSubmitstatus() == 0){
											    	//out.print("未提交");
											    	out.print(MessageUtils.extractMessage("ydcx","ydcx_cxyy_mbbj_wtj",request));
											    }else{
											    	out.print("-");
											    }
											%>
										
										</td>
										<td class="ztalign">
										    <%
										    if(mv.getTmState()==0L||mv.getTmState()==1L){
										    	 if(mv.getAuditstatus()-1 == 0){
													//out.print("审批通过");
													out.print(MessageUtils.extractMessage("ydcx","ydcx_cxyy_mbbj_sptg",request));
												}else if(mv.getAuditstatus()-2 == 0){
													//out.print("审批未通过");
													out.print(MessageUtils.extractMessage("ydcx","ydcx_cxyy_mbbj_spwtg",request));
												}else if(mv.getAuditstatus()- 0 == 0){
													//out.print("未审批");
													out.print(MessageUtils.extractMessage("ydcx","ydcx_cxyy_mbbj_wsp",request));
												}else{
													out.print("-");
												}
											}else{
											    out.print("-");
											}
											%>
										</td>
										<td class="ztalign">
										<%if(mv.getTmState()-1 == 0){
											//out.print("启用");
											out.print(MessageUtils.extractMessage("ydcx","ydcx_cxyy_mbbj_qy",request));
										}else if(mv.getTmState() == 0){
											//out.print("禁用");
											out.print(MessageUtils.extractMessage("ydcx","ydcx_cxyy_mbbj_jy",request));
										}else{
											//out.print("草稿");
											out.print(MessageUtils.extractMessage("ydcx","ydcx_cxyy_mbbj_cg",request));
										}
										%>
										</td>
										<%if(btnMap.get(menuCode+"-1")!=null) { %>
										<td>
											<a onclick="javascript:doCopy('<%=mv.getTmid() %>','<%=mv.getTmMsg() %>','copy')"><emp:message key="ydcx_cxyy_mbbj_fz" defVal="复制" fileName="ydcx"></emp:message></a>
										</td>
										<%}%>
										<td>
										<%if(btnMap.get(menuCode+"-1")!=null&&isEdit){%>
											<a href="javascript:showShareTmp(<%=mv.getTmid() %>,'<%=mv.getTmName() %>',<%=mv.getUserId() %>)"><emp:message key="ydcx_cxyy_mbbj_gx" defVal="共享" fileName="ydcx"></emp:message></a>
										<% } else{%>
										<emp:message key="ydcx_cxyy_mbbj_gx" defVal="共享" fileName="ydcx"></emp:message>
										<% } %>
										</td>
										<td>
										<%if(btnMap.get(menuCode+"-2")!=null&&isEdit) { %>
											<a href="javascript:del('<%=path %>',<%=mv.getTmid() %>)"><emp:message key="ydcx_cxyy_common_text_8" defVal="删除" fileName="ydcx"></emp:message></a>
										<%} else{%>
										<emp:message key="ydcx_cxyy_common_text_8" defVal="删除" fileName="ydcx"></emp:message>
										<%} %>
										</td>
										<td>
										<%
										if(isEdit){
											if(mv.getTmState()-1 == 0){
												%>
												<a onclick="javascript:chState('<%=mv.getTmid() %>','0')"><emp:message key="ydcx_cxyy_mbbj_jy" defVal="禁用" fileName="ydcx"></emp:message></a>
												<%
											}else if(mv.getTmState() == 0){
												%>
												<a onclick="javascript:chState('<%=mv.getTmid() %>','1')"><emp:message key="ydcx_cxyy_mbbj_qy" defVal="启用" fileName="ydcx"></emp:message></a>
												<%
											}else if(btnMap.get(menuCode+"-1")!=null){%>
												<a onclick="javascript:doCopy('<%=mv.getTmid() %>','<%=mv.getTmMsg() %>','edit')"><emp:message key="ydcx_cxyy_common_text_7" defVal="编辑" fileName="ydcx"></emp:message></a>
											<%}else{%>
											-
											<%}
											}else{ %>
											-
											<%} %>
										</td>
										<td>
										<%if(isEdit){
											if(mv.getTmState() == 0){ %>
										    -
										<%} else if(mv.getIsPass()== 0) { %>
										  <a onclick="javascript:doExport('<%=mv.getTmMsg() %>')"><emp:message key="ydcx_cxyy_mbbj_dcmb" defVal="导出模板" fileName="ydcx"></emp:message></a>
										<%} else if(mv.getIsPass() == 1) {%>
										      <a onclick="javascript:doExport('<%=mv.getTmMsg() %>')"><emp:message key="ydcx_cxyy_mbbj_dcmb" defVal="导出模板" fileName="ydcx"></emp:message></a>
										<%} else{ %>
										    -
										<%}}else{ %>
										-
										<%} %>
										</td>
									</tr>
									<%
									}
								}else{
									%>
									<tr><td colspan="18"><emp:message key="ydcx_cxyy_common_text_1" defVal="无记录" fileName="ydcx"></emp:message></td></tr>
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
				</form>
			</div>
			<%} %>
			<%--导入彩信文件  --%>
			<div id="modelTms" class="model ydcx_model">
					<div class="main ydcx_main">
					<div class="ydcx_main_sub">
						<div id="upTms">
							<p><span class="ydcx_upTms_p"><emp:message key="ydcx_cxyy_mbbj_mbbh_mh" defVal="模板编号：" fileName="ydcx"></emp:message></span><input type="text" name="importCode" id="importCode" class="ydcx_importCode"  maxlength="16" /><font class="ydcx_importCode_font" >*</font></p>
							<br/>
							<p>
							<span class="ydcx_choosefile"><emp:message key="ydcx_cxyy_mbbj_xzwj_mh" defVal="选择文件：" fileName="ydcx"></emp:message>
							</span>
							<%--<input id="chooseTms" name="chooseTms" type="file" />
							--%>
							<input class="ydcx_filepath" type="textfield" id="filepath" readonly>
							<span class="ydcx_filepath_span">&nbsp;&nbsp;&nbsp;&nbsp;</span>
							<span>
								<span class="ydcx_uploadfile">
									<emp:message key="ydcx_cxyy_mbbj_xzwj" defVal="上传文件" fileName="ydcx"></emp:message>
									<input class="ydcx_chooseTms" type="file" id="chooseTms" name="chooseTms" 
											onchange="document.getElementById('filepath').value=this.value;document.getElementById('filepath').title=this.value;"/>			
								</span>
							</span>
							</p>
							<br/>
							
							<br/>
							<p class="ydcx_p1"><emp:message key="ydcx_cxyy_mbbj_cxwjgsw" defVal="彩信文件格式为：tms" fileName="ydcx"></emp:message></p>
							<p class="ydcx_p2"><emp:message key="ydcx_cxyy_mbbj_mbztpgszzc" defVal="模板中图片格式只支持：jpg、jpeg、gif" fileName="ydcx"></emp:message></p>
							<p class="ydcx_p3"><emp:message key="ydcx_cxyy_mbbj_mbzypgszzc" defVal="模板中音频格式只支持：mid、midi、amr" fileName="ydcx"></emp:message></p>
							<p class="ydcx_p4">
							<input type="button" id="uploadTms" value="<emp:message key="ydcx_cxyy_mbbj_ljsc" defVal="立即上传" fileName="ydcx"></emp:message>" onclick="javascript:doUp()" class="btnClass2"/></p>
						</div>
						<div id="curTms" style="display:none">															</div>
					</div>
					</div>
					<div class="foot">
						<div class="foot-right"></div>
					</div>
					
				</div>
				<div id="detail" class="ydcx_detail">
				<table width="100%">
					<thead>
						<tr class="ydcx_detail_tr1">
							<td style='word-break: break-all;'>
								<span><label id="msgcont" style="width:100%;height:100%"><xmp style='word-break: break-all;white-space:normal;'></xmp></label></span>
								 
							</td>
							 
						</tr>
					   <tr class="ydcx_detail_tr2">
							<td>
							</td>
							</tr>
						 
					</thead>
				</table>
			</div>
			
			
		<%-- 共享模板 DIV --%>
		<div id="shareTmpDiv" title="<emp:message key="ydcx_cxyy_mbbj_mbgx" defVal="模板共享" fileName="ydcx"></emp:message>" style="display: none;">
			<center>
			<iframe id="flowFrame" name="flowFrame" src=""  attrid="" tmpname="" class="ydcx_flowFrame"  marginwidth="0" scrolling="no" frameborder="no"></iframe>
			<table>
				<tr>
					<td class="ydcx_flowFrame_tabl_td1" align="center">
						<input type="button"  value="<emp:message key="ydcx_cxyy_common_btn_7" defVal="确定" fileName="ydcx"></emp:message>" id="updateShareTemp" class="btnClass5 mr23" onclick="javascript:updateShareTemp();" />
						<input type="button"  value="<emp:message key="ydcx_cxyy_common_btn_16" defVal="取消" fileName="ydcx"></emp:message> " class="btnClass6" onclick="javascript:closeShare();" />
						<br/>
					</td>
				</tr>
			</table>
			</center>
		</div>
			
			<%-- 彩信信息详情--%>
			<div id="mmsdetailinfo" title="<emp:message key="ydcx_cxyy_mbbj_xxxx" defVal="详细信息" fileName="ydcx"></emp:message>" style="display:none;overflow: auto;">
					<div class="ydcx_recordTableDiv" id="recordTableDiv" align="center">
					<table width="95%" id="recordTable" class="ydcx_recordTable">
					</table>
					</div>
					<div class="ydcx_mmsdetailinfo_sub2" id="nextrecordmgs" align="left">
					</div>
			</div>	
			
			
		<div id="reviewflowinfo" title="<emp:message key="ydcx_cxyy_mbbj_dsp" defVal="待审批" fileName="ydcx"></emp:message>" style="display:none;overflow: auto;">
				<div class="ydcx_reviewTableDiv" id="reviewTableDiv" align="center">
				<table width="95%" id="reviewTable" class="ydcx_reviewTable" >
				</table>
				</div>
				<div class="ydcx_nextreviewmgs" id="nextreviewmgs" align="left">
				</div>
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
        <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
        <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/ajaxfileupload.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/mmsPreview.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=iPath%>/js/mmsTemplate.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
 		<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/ydcx_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=empFramePath%>/js/dialogui.js"></script>
		<script type="text/javascript">
			    $(document).ready(function() {
					var findresult="<%=request.getAttribute("findresult")%>";
				    if(findresult != null && findresult !="" && findresult=="-1")
				    {
				       //alert("加载页面失败，请检查网络是否正常！");	
				       alert(getJsLocaleMessage("ydcx","ydcx_cxyy_mbbj_jzyesb"));	
				       return;			       
				    }
				    getLoginInfo("#hiddenValueDiv");
					initPage(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>);
					$('#search').click(function(){submitForm();});
			    });
		</script>
	</body>
</html>
