<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils"%>
<%@ page import="com.montnets.emp.report.bean.RptConfInfo"%>
<%@ page import="com.montnets.emp.report.bean.RptStaticValue"%>
<%@ page import="com.montnets.emp.report.biz.ReportBiz"%>
<%@ page import="com.montnets.emp.report.biz.RptConfBiz"%>
<%@ page import="com.montnets.emp.report.vo.DepRptVo"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="org.apache.commons.beanutils.DynaBean"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>

<%
	String path = request.getContextPath();
	String iPath = request.getRequestURI().substring(0, request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0, iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0, inheritPath.lastIndexOf("/"));
	
	MessageUtils messageUtils = new MessageUtils();
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
	
	PageInfo pageInfo = new PageInfo();
	if(request.getAttribute("pageInfo")!=null){
		pageInfo = (PageInfo) request.getAttribute("pageInfo");
	}
	String icount = request.getAttribute("detailicount")==null?"0":request.getAttribute("detailicount").toString();
	String rsucc = request.getAttribute("detailrsucc")==null?"0":request.getAttribute("detailrsucc").toString();
	String rfail1 = request.getAttribute("detailrfail1")==null?"0":request.getAttribute("detailrfail1").toString();
	String rfail2 = request.getAttribute("detailrfail2")==null?"0":request.getAttribute("detailrfail2").toString();
	String rnret = request.getAttribute("detailrnret")==null?"0":request.getAttribute("detailrnret").toString();

	SimpleDateFormat def = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	Calendar c = Calendar.getInstance();
	c.add(Calendar.DAY_OF_MONTH, -1);
	String beginTime = def.format(c.getTime()).substring(0, 8) + "01";
	String endTime = def.format(c.getTime()).substring(0, 11); //change by dj
	
	@SuppressWarnings("unchecked")
	Map<String, String> btnMap = (Map<String, String>) session.getAttribute("btnMap");//????????????Map

	@SuppressWarnings("unchecked")
	Map<String, String> titleMap = (Map<String, String>) session.getAttribute("titleMap");

	@SuppressWarnings("unchecked")
	List<DynaBean> resultListt = (List<DynaBean>) request.getAttribute("detailList");
	if(resultListt==null){
	   resultListt=new ArrayList<DynaBean>();
	}
	String menuCode = titleMap.get("sysDepReport");

	menuCode = menuCode == null ? "0-0-0" : menuCode;
	String findResult = null;
	findResult = (String) request.getAttribute("findresult");
	String skin = session.getAttribute("stlyeSkin") == null ? "default" : (String) session.getAttribute("stlyeSkin");
	int reportType =-1;
	String depusername="";
	DepRptVo deprptvodetail=(DepRptVo)request.getAttribute("deprptvodetail");
	if(deprptvodetail!=null&&deprptvodetail.getReporttype()!=null){
			reportType = deprptvodetail.getReporttype();
	}else{
		icount="0";
		rsucc="0";
		rfail1="0";
		rfail2="0";
		rnret="0";
	}
	
	String spnumtype=request.getParameter("spnumtype");
	
//???
String z = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_z", request);

	
	
	
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<%@include file="/common/common.jsp" %>
		<title><%=titleMap.get(menuCode)%></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin%>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin%>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<%if(StaticValue.ZH_HK.equals(langName)){%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
		<link href="<%=commonPath%>/common/widget/zTreeStyle/zTreeStyle.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/cxtj/common/css/inbox.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
	</head>
	<body class="rep_sysDepDetail">
		<div id="container" class="container">
			<%-- ???????????? --%>
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode)%>

			<%-- ???????????? --%>
			<%
				if(btnMap.get(menuCode + "-0") != null)
				{
			%>
			<div id="rContent" class="rContent">
				<input type="hidden" value="<%=inheritPath%>" id="inheritPath" />
				<form name="pageForm" action="rep_sysDepReport.htm?method=findInfoById&lguserid=<%=request.getParameter("lguserid") %>&lgcorpcode=<%=request.getParameter("lgcorpcode") %>" method="post"
					id="pageForm">
					<input type="hidden" id="reportType" name="reportType" value="<%=request.getParameter("reportType") == null ? "" : request.getParameter("reportType") %>" />
					<input type="hidden" id="idtype" name="idtype" value="<%=request.getParameter("idtype") == null ? "" : request.getParameter("idtype") %>" />
					<input type="hidden" id="begintime" name="begintime" value="<%=request.getParameter("begintime") == null ? "" : request.getParameter("begintime") %>" />
					<input type="hidden" id="endtime" name="endtime" value="<%=request.getParameter("endtime") == null ? "" : request.getParameter("endtime") %>" />
					<input type="hidden" id="id" name="id" value="<%=request.getParameter("id") == null ? "" : request.getParameter("id") %>" />
					<input type="hidden" id="mstype" name="mstype" value="<%=request.getParameter("mstype") == null ? "" : request.getParameter("mstype") %>" />
					<input type="hidden" id="datasourcetype" name="datasourcetype" value="<%=request.getParameter("datasourcetype") == null ? "" : request.getParameter("datasourcetype") %>" />
					<input type="hidden" id="spnumtype" name="spnumtype" value="<%=request.getParameter("spnumtype") == null ? "" : request.getParameter("spnumtype") %>" />
					<input type="hidden" id="deptString" name="deptString" value="<%=request.getParameter("deptString") == null ? "" : request.getParameter("deptString")%>" />
					<input type="hidden" id="depNam" name="depNam" value="<%=session.getAttribute(RptStaticValue.PREFIX_RPT_DEP + "depNam") == null ? "?????????" : session.getAttribute(RptStaticValue.PREFIX_RPT_DEP + "depNam").toString().replace("<", "&lt;").replace(">", "&gt;")%>" />
					<input type="hidden" id="lgpageIndex" name="lgpageIndex" value="<%=request.getParameter("lgpageIndex")==null?"1":request.getParameter("lgpageIndex") %>" />
					<input type="hidden" id="lgpageSize" name="lgpageSize" value="<%=request.getParameter("lgpageSize")==null?"15":request.getParameter("lgpageSize") %>" />
					<div class="buttons">
						<%
							if(btnMap.get(menuCode + "-5") != null){
						%>
						<a id="exportCurrent"></a>
						<a id="exportCondition"></a>
						<input type="hidden" name="menucode" id="menucode" value="17" />
						<%
							}
						%>
						<a id="exportCondition"><emp:message key="cxtj_sjcx_report_dc" defVal="??????" fileName="cxtj"></emp:message></a>
					<span id="backgo" class="right" onclick="javascript:back()"><emp:message key="cxtj_sjcx_report_fh" defVal="??????" fileName="cxtj"></emp:message></span>
					</div>
					<table id="content">
						<thead>
							<tr>
								<th>
									<emp:message key="cxtj_sjcx_report_sj" defVal="??????" fileName="cxtj"></emp:message>
								</th>
								<th>
									<emp:message key="cxtj_sjcx_report_jgczy" defVal="??????/?????????" fileName="cxtj"></emp:message>
								</th>
								<th>
									<emp:message key="cxtj_sjcx_report_fslx" defVal="????????????" fileName="cxtj"></emp:message>
								</th>
								<th>
									<emp:message key="cxtj_sjcx_report_yys" defVal="?????????" fileName="cxtj"></emp:message>
								</th>
								<%
								List<RptConfInfo> rptConList = RptConfBiz.getRptConfMap().get(RptStaticValue.DEP_RPT_CONF_MENU_ID);
								String temp = null;
								for(RptConfInfo rptConf : rptConList)
								{
								temp = rptConf.getName();
								%>
								<th><%=MessageUtils.extractMessage("cxtj",temp,request) %></th>
								<%} %>
							</tr>
						</thead>
						<tbody>
							<%
							if(resultListt==null||resultListt.size()==0){
							%>
							<tr>
								<td colspan="<%=4+rptConList.size() %>" align="center">
									<emp:message key="cxtj_sjcx_report_wjl" defVal="?????????" fileName="cxtj"></emp:message>
								</td>
							</tr>
							<%
							}else{
								if(findResult != null){
							%>
							<tr>
								<td colspan="<%=4+rptConList.size() %>" align="center">
									<emp:message key="cxtj_sjcx_report_wjl" defVal="?????????" fileName="cxtj"></emp:message>
								</td>
							</tr>
							<%
								}else{
							%>
							<%
								for (int i = 0; i < resultListt.size(); i++){
									DynaBean dynrptvo = resultListt.get(i);
							%>
							<tr>
								<td>
								<%
								//SimpleDateFormat sdf = new SimpleDateFormat("yyyy???M???d???");
								SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
								//SimpleDateFormat sdf = new SimpleDateFormat("yyyy"+MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_y", request)+"M"+MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_m", request)+"d"+MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_d", request));
								SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
								if(reportType == 2){
										//out.print((dynrptvo.get("y")!=null?dynrptvo.get("y").toString():"2015")+"???"+(dynrptvo.get("imonth")!=null?dynrptvo.get("imonth").toString():"01")+"???");
										out.print((dynrptvo.get("y")!=null?dynrptvo.get("y").toString():"2015")+"-"+(dynrptvo.get("imonth")!=null?dynrptvo.get("imonth").toString():"01"));
										//out.print((dynrptvo.get("y")!=null?dynrptvo.get("y").toString():"2015")+MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_y", request)+(dynrptvo.get("imonth")!=null?dynrptvo.get("imonth").toString():"01")+MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_m", request));
										
								}else{
									out.print(sdf.format(sdf1.parse(dynrptvo.get("iymd")!=null?dynrptvo.get("iymd").toString():"20150101")));
								}
								%>
								</td>
								<td class="textalign">
								<%=session.getAttribute("userdepname")!=null?session.getAttribute("userdepname"):"--" %>	
								</td>
								<td>
									<%
											if("0".equals(request.getParameter("datasourcetype"))){ 
													//out.print("??????");
													out.print(MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_qb", request));
											}else if("1".equals(request.getParameter("datasourcetype"))){
													//out.print("EMP??????");
													out.print(MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_empfs", request));
											}else if("2".equals(request.getParameter("datasourcetype"))){
													//out.print("HTTP??????");
													out.print(MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_httpjr", request));
											}else if("3".equals(request.getParameter("datasourcetype"))){
													//out.print("DB??????");
													out.print(MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_dbjr", request));
											}else if("4".equals(request.getParameter("datasourcetype"))){
													//out.print("????????????");
													out.print(MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_zljr", request));
											}else{
													out.print("--");
											}
										//session.getAttribute("datasourcename")!=null?session.getAttribute("datasourcename"):"--" 
									//=session.getAttribute("datasourcenamedetail")!=null?session.getAttribute("datasourcenamedetail"):"--" 
									%>		
								</td>
								<td>
									<%
									if(spnumtype!=null&&"0".equals(spnumtype)){
										//out.print("??????");
										out.print(MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_gn", request));
									}else if(spnumtype!=null&&"1".equals(spnumtype)){
										//out.print("??????");
										out.print(MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_gw", request));
									}else if(spnumtype!=null&&"-1".equals(spnumtype)){
										//out.print("??????");
										out.print(MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_qb", request));
									}else{
										out.print("--");
									}
									%>
								</td>
								<%
								long icountl=dynrptvo.get("icount")!=null?Long.valueOf(dynrptvo.get("icount").toString()):0;
								long rsuccl=dynrptvo.get("rsucc")!=null?Long.valueOf(dynrptvo.get("rsucc").toString()):0;
								long rfail1l=dynrptvo.get("rfail1")!=null?Long.valueOf(dynrptvo.get("rfail1").toString()):0;
								long rfail2l=dynrptvo.get("rfail2")!=null?Long.valueOf(dynrptvo.get("rfail2").toString()):0;
								long rnretl=dynrptvo.get("rnret")!=null?Long.valueOf(dynrptvo.get("rnret").toString()):0;
	 							//???????????????????????? ????????????map
	 							Map<String,String> map=ReportBiz.getRptNums(icountl,rsuccl,rfail1l,rfail2l,rnretl,RptStaticValue.DEP_RPT_CONF_MENU_ID);
								for(RptConfInfo rptConf : rptConList)
								{
									if(RptStaticValue.RPT_ICOUNT_COLUMN_ID.equals(rptConf.getColId()))
									{//????????????
								%>
								<td><%=map.get(RptStaticValue.RPT_ICOUNT_COLUMN_ID)!=null?map.get(RptStaticValue.RPT_ICOUNT_COLUMN_ID):0 %></td>
								<%
									}
									else if(RptStaticValue.RPT_FSSUCC_COLUMN_ID.equals(rptConf.getColId()))
									{//???????????????
								%>
								<td><%=map.get(RptStaticValue.RPT_FSSUCC_COLUMN_ID)!=null?map.get(RptStaticValue.RPT_FSSUCC_COLUMN_ID):0 %></td>
								<%
									}
									else if(RptStaticValue.RPT_RFAIL1_COLUMN_ID.equals(rptConf.getColId()))
									{//???????????????
								%>
								<td><%=map.get(RptStaticValue.RPT_RFAIL1_COLUMN_ID)!=null?map.get(RptStaticValue.RPT_RFAIL1_COLUMN_ID):0 %></td>
								<%
									}
									else if(RptStaticValue.RPT_RFAIL2_COLUMN_ID.equals(rptConf.getColId()))
									{//???????????????
								%>
								<td><%=map.get(RptStaticValue.RPT_RFAIL2_COLUMN_ID)!=null?map.get(RptStaticValue.RPT_RFAIL2_COLUMN_ID):0 %></td>
								<%
									}
									else if(RptStaticValue.RPT_JSSUCC_COLUMN_ID.equals(rptConf.getColId()))
									{//???????????????
								%>
								<td><%=map.get(RptStaticValue.RPT_JSSUCC_COLUMN_ID)!=null?map.get(RptStaticValue.RPT_JSSUCC_COLUMN_ID):0 %></td>
								<%
									}
									else if(RptStaticValue.RPT_RNRET_COLUMN_ID.equals(rptConf.getColId()))
									{//?????????
								%>
								<td><%=map.get(RptStaticValue.RPT_RNRET_COLUMN_ID)!=null?map.get(RptStaticValue.RPT_RNRET_COLUMN_ID):0 %></td>
								<%
									}
									else if(RptStaticValue.RPT_FSSUCC_ICOUNT_COLUMN_ID.equals(rptConf.getColId()))
									{//???????????????
								%>
								<td><%=map.get(RptStaticValue.RPT_FSSUCC_ICOUNT_COLUMN_ID)!=null?map.get(RptStaticValue.RPT_FSSUCC_ICOUNT_COLUMN_ID):0 %></td>
								<%
									}
									else if(RptStaticValue.RPT_RFAIL1_ICOUNT_COLUMN_ID.equals(rptConf.getColId()))
									{//???????????????
								%>
								<td><%=map.get(RptStaticValue.RPT_RFAIL1_ICOUNT_COLUMN_ID)!=null?map.get(RptStaticValue.RPT_RFAIL1_ICOUNT_COLUMN_ID):0 %></td>
								<%
									}
									else if(RptStaticValue.RPT_JSSUCC_FSSUCC_COLUMN_ID.equals(rptConf.getColId()))
									{//???????????????
								%>
								<td><%=map.get(RptStaticValue.RPT_JSSUCC_FSSUCC_COLUMN_ID)!=null?map.get(RptStaticValue.RPT_JSSUCC_FSSUCC_COLUMN_ID):0 %></td>
								<%
									}
									else if(RptStaticValue.RPT_RFAIL2_FSSUCC_COLUMN_ID.equals(rptConf.getColId()))
									{//???????????????
								%>
								<td><%=map.get(RptStaticValue.RPT_RFAIL2_FSSUCC_COLUMN_ID)!=null?map.get(RptStaticValue.RPT_RFAIL2_FSSUCC_COLUMN_ID):0 %></td>
								<%
									}
									else if(RptStaticValue.RPT_RNRET_FSSUCC_COLUMN_ID.equals(rptConf.getColId()))
									{//?????????
								%>
								<td><%=map.get(RptStaticValue.RPT_RNRET_FSSUCC_COLUMN_ID)!=null?map.get(RptStaticValue.RPT_RNRET_FSSUCC_COLUMN_ID):0 %></td>
								<%}
								} %>

							</tr>
							<%
								}
							%>
							<tr>
								<td colspan="4">
									<b><emp:message key="cxtj_sjcx_report_hj" defVal="?????????" fileName="cxtj"></emp:message></b>
								</td>
								 <%
	 							//???????????????????????? ????????????map
	 							Map<String,String> map=ReportBiz.getRptNums(Long.valueOf(icount),Long.valueOf(rsucc),Long.valueOf(rfail1),Long.valueOf(rfail2),Long.valueOf(rnret),RptStaticValue.DEP_RPT_CONF_MENU_ID);
								for(RptConfInfo rptConf : rptConList)
								{
									if(RptStaticValue.RPT_ICOUNT_COLUMN_ID.equals(rptConf.getColId()))
									{//????????????
								%>
								<td><%=map.get(RptStaticValue.RPT_ICOUNT_COLUMN_ID)!=null?map.get(RptStaticValue.RPT_ICOUNT_COLUMN_ID):0 %></td>
								<%
									}
									else if(RptStaticValue.RPT_FSSUCC_COLUMN_ID.equals(rptConf.getColId()))
									{//???????????????
								%>
								<td><%=map.get(RptStaticValue.RPT_FSSUCC_COLUMN_ID)!=null?map.get(RptStaticValue.RPT_FSSUCC_COLUMN_ID):0 %></td>
								<%
									}
									else if(RptStaticValue.RPT_RFAIL1_COLUMN_ID.equals(rptConf.getColId()))
									{//???????????????
								%>
								<td><%=map.get(RptStaticValue.RPT_RFAIL1_COLUMN_ID)!=null?map.get(RptStaticValue.RPT_RFAIL1_COLUMN_ID):0 %></td>
								<%
									}
									else if(RptStaticValue.RPT_RFAIL2_COLUMN_ID.equals(rptConf.getColId()))
									{//???????????????
								%>
								<td><%=map.get(RptStaticValue.RPT_RFAIL2_COLUMN_ID)!=null?map.get(RptStaticValue.RPT_RFAIL2_COLUMN_ID):0 %></td>
								<%
									}
									else if(RptStaticValue.RPT_JSSUCC_COLUMN_ID.equals(rptConf.getColId()))
									{//???????????????
								%>
								<td><%=map.get(RptStaticValue.RPT_JSSUCC_COLUMN_ID)!=null?map.get(RptStaticValue.RPT_JSSUCC_COLUMN_ID):0 %></td>
								<%
									}
									else if(RptStaticValue.RPT_RNRET_COLUMN_ID.equals(rptConf.getColId()))
									{//?????????
								%>
								<td><%=map.get(RptStaticValue.RPT_RNRET_COLUMN_ID)!=null?map.get(RptStaticValue.RPT_RNRET_COLUMN_ID):0 %></td>
								<%
									}
									else if(RptStaticValue.RPT_FSSUCC_ICOUNT_COLUMN_ID.equals(rptConf.getColId()))
									{//???????????????
								%>
								<td><%=map.get(RptStaticValue.RPT_FSSUCC_ICOUNT_COLUMN_ID)!=null?map.get(RptStaticValue.RPT_FSSUCC_ICOUNT_COLUMN_ID):0 %></td>
								<%
									}
									else if(RptStaticValue.RPT_RFAIL1_ICOUNT_COLUMN_ID.equals(rptConf.getColId()))
									{//???????????????
								%>
								<td><%=map.get(RptStaticValue.RPT_RFAIL1_ICOUNT_COLUMN_ID)!=null?map.get(RptStaticValue.RPT_RFAIL1_ICOUNT_COLUMN_ID):0 %></td>
								<%
									}
									else if(RptStaticValue.RPT_JSSUCC_FSSUCC_COLUMN_ID.equals(rptConf.getColId()))
									{//???????????????
								%>
								<td><%=map.get(RptStaticValue.RPT_JSSUCC_FSSUCC_COLUMN_ID)!=null?map.get(RptStaticValue.RPT_JSSUCC_FSSUCC_COLUMN_ID):0 %></td>
								<%
									}
									else if(RptStaticValue.RPT_RFAIL2_FSSUCC_COLUMN_ID.equals(rptConf.getColId()))
									{//???????????????
								%>
								<td><%=map.get(RptStaticValue.RPT_RFAIL2_FSSUCC_COLUMN_ID)!=null?map.get(RptStaticValue.RPT_RFAIL2_FSSUCC_COLUMN_ID):0 %></td>
								<%
									}
									else if(RptStaticValue.RPT_RNRET_FSSUCC_COLUMN_ID.equals(rptConf.getColId()))
									{//?????????
								%>
								<td><%=map.get(RptStaticValue.RPT_RNRET_FSSUCC_COLUMN_ID)!=null?map.get(RptStaticValue.RPT_RNRET_FSSUCC_COLUMN_ID):0 %></td>
								<%}
								} %>
							</tr>
							<%
								}
									}
							%>
						</tbody>

						<tfoot>
							<tr>
								<td colspan="<%=4+rptConList.size() %>">
									<input type="hidden" name="pageTotalRec" id="pageTotalRec"
										value="<%=pageInfo.getTotalRec()%>" />
									<input type="hidden" name="queryTime" id="queryTime"
										value="<%=request.getParameter("begintime") == null ? "" : request.getParameter("begintime")%><%=z%><%=request.getParameter("endtime") == null ? "" : request.getParameter("endtime")%>" />
									<div id="pageInfo"></div>
								</td>
							</tr>

						</tfoot>
					</table>
					<div id="corpCode" class="hidden"></div>
				</form>
			</div>
			<%
				}
			%>
			<%-- ???????????? --%>
			<%-- foot?????? --%>
			<div class="bottom">
				<div id="bottom_right">
					<div id="bottom_left"></div>
					<div id="bottom_main">
					</div>
				</div>
			</div>
		</div>
		<%-- foot?????? --%>
		<div class="clear"></div>
		<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.ztree-myjquery-b.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/cxtj_<%=langName%>.js"></script>

		<script type="text/javascript">
		
		function back(){
			var pageIndex='<%=request.getParameter("lgpageIndex")==null?"1":request.getParameter("lgpageIndex")%>';
	   	    var pageSize='<%=request.getParameter("lgpageSize")==null?"15":request.getParameter("lgpageSize")%>';
	   	    var mstype = '<%=request.getParameter("mstype") == null ? "" : request.getParameter("mstype") %>';
	   	    $("#pageForm").attr("action","rep_sysDepReport.htm?method=find&inFlag=1&lguserid=<%=request.getParameter("lguserid")%>&lgcorpcode=<%=request.getParameter("lgcorpcode")%>&pageIndex="+pageIndex+"&pageSize="+pageSize+"&msType="+mstype);
			submitForm();
		}
		
		$(document).ready(function(){
			getLoginInfo("#corpCode");
			var findresult="<%=findResult%>";
		    if(findresult=="-1")
		    {
		       //alert("??????????????????,???????????????????????????!");	
		       alert(getJsLocaleMessage("cxtj","cxtj_sjcx_report_text_2"));	
		       return;			       
		    }
		    
			$("#content tbody tr").hover(function() {
					$(this).addClass("hoverColor");
				}, function() {
					$(this).removeClass("hoverColor");
				});

			initPage(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>);
			
			//?????????????????????excel
			$("#exportCondition").click(
			 function()
			 {
				
				  //if(confirm("????????????????????????excel?"))
				  if(confirm(getJsLocaleMessage("cxtj","cxtj_sjcx_report_text_1")))
				   {
					   	
					   	<%if(resultListt==null||resultListt.size()==0){%>
					   			//alert("?????????????????????");
					   			alert(getJsLocaleMessage("cxtj","cxtj_sjcx_report_text_4"));
					   	<%}else{
							if(resultListt != null && resultListt.size() > 0){
						%>			
								var reportType='<%=deprptvodetail.getReporttype()!=null?deprptvodetail.getReporttype():"" %>';
								var sendtime = '<%=deprptvodetail.getSendTime()!=null?deprptvodetail.getSendTime():beginTime %>';
							   	var recvtime = '<%=deprptvodetail.getEndTime()!=null?deprptvodetail.getEndTime():endTime%>';
						   	    var datasourcetype='<%=deprptvodetail.getDatasourcetype() == null ? "" : deprptvodetail.getDatasourcetype()%>';
				   	    		var mstype = '<%=deprptvodetail.getMstype() == null ? "" : deprptvodetail.getMstype()%>';
								var depid='<%=request.getParameter("id")==null?"":request.getParameter("id") %>';
							   	var idtype = '<%=deprptvodetail.getIdtype() == null ? "" : deprptvodetail.getIdtype() %>';
							   	var spnumtype='<%=spnumtype == null ? "" : spnumtype%>';
							   	$.ajax({
									type: "POST",
									url: "<%=path%>/rep_sysDepReport.htm?method=r_sdRptDetailExportExcel",
									data: {
										lguserid:'<%=request.getParameter("lguserid")%>',
										lgcorpcode:'<%=request.getParameter("lgcorpcode")%>',
										reportType:reportType,
										idtype:idtype,
										begintime:sendtime,
										endtime:recvtime,
										id:depid,
										mstype:mstype,
										datasourcetype:datasourcetype,
										spnumtype:spnumtype
									},
						            beforeSend:function () {
						                page_loading();
						            },
						            complete:function () {
						           	  	page_complete()
						            },
						            success:function (result) {
						                    if (result == 'true') {
						                        download_href("<%=path%>/rep_sysDepReport.htm?method=depdetaildownloadFile");
						                    } else {
						                        //alert('???????????????');
						                        alert(getJsLocaleMessage("cxtj","cxtj_sjcx_report_text_3"));
						                    }
	           					}
				 			});			
							   	
						 		//location.href="<%=path%>/rep_sysDepReport.htm?method=r_sdRptDetailExportExcel&&lguserid=<%=request.getParameter("lguserid")%>&lgcorpcode=<%=request.getParameter("lgcorpcode")%>&reportType="+reportType+"&idtype="+idtype+"&begintime="+sendtime+"&endtime="+recvtime+"&id="+depid+"&mstype="+mstype+"&datasourcetype="+datasourcetype+"&spnumtype="+spnumtype;
						<%}else{%>
						   		//alert("?????????????????????");
						   		alert(getJsLocaleMessage("cxtj","cxtj_sjcx_report_text_4"));
						<%
						}
				   }
				   		%>
				   }				 
			  });
		
		   
			
		});
		
		function numberControl(va)
		{
			var pat=/^\d*$/;
			if(!pat.test(va.val()))
			{
				va.val(va.val().replace(/[^\d]/g,''));
			}
		}


		function cleanSelect_dep()
		{

			$('#depNam').attr('value', '');
			//$('#depNam').attr('value', '?????????');
			$('#depNam').attr('value', getJsLocaleMessage("cxtj","cxtj_sjcx_report_text_5"));
			$('#deptString').attr('value', '');
		}
		
		  

		</script>
	</body>
</html>
