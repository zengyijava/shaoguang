<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils"%>
<%@ page import="com.montnets.emp.report.bean.RptConfInfo"%>
<%@ page import="com.montnets.emp.report.bean.RptStaticValue"%>
<%@ page import="com.montnets.emp.report.biz.ReportBiz"%>
<%@ page import="com.montnets.emp.report.biz.RptConfBiz"%>
<%@ page import="com.montnets.emp.report.vo.DepAreaRptVo"%>
<%@page import="com.montnets.emp.util.PageInfo"%>
<%@page import="org.apache.commons.beanutils.DynaBean"%>
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
	String icount = request.getAttribute("detailareaicount")==null?"0":request.getAttribute("detailareaicount").toString();
	String rsucc = request.getAttribute("detailarearsucc")==null?"0":request.getAttribute("detailarearsucc").toString();
	String rfail1 = request.getAttribute("detailarearfail1")==null?"0":request.getAttribute("detailarearfail1").toString();
	String rfail2 = request.getAttribute("detailarearfail2")==null?"0":request.getAttribute("detailarearfail2").toString();
	String rnret = request.getAttribute("detailarearnret")==null?"0":request.getAttribute("detailarearnret").toString();

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
	List<DynaBean> resultListt = (List<DynaBean>) request.getAttribute("detailareaList");
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
	DepAreaRptVo deprptvodetail=(DepAreaRptVo)request.getAttribute("deparearptvodetail");
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
	
//????????????
String gjmc = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_gjmc", request);
    if(gjmc!=null&&gjmc.length()>1){
    	gjmc = gjmc.substring(0,gjmc.length()-1);
    }	
//???
String y = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_y", request);
//???
String m = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_m", request);
//???
String d = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_d", request);
//??????
String qb = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_qb", request);
//EMP??????
String empfs = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_empfs", request);
//HTTP??????
String httpjr = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_httpjr", request);
//DB??????
String dbjr = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_dbjr", request);
//????????????
String zljr = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_zljr", request);
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
		<link href="<%=commonPath%>/common/widget/zTreeStyle/zTreeStyle.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
		<link href="<%=commonPath%>/cxtj/common/css/inbox.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
	</head>
	<body class="rep_sysDepAreaDetail">
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
				<form name="pageForm" action="rep_sysDepReport.htm?method=findAreaInfoById&lguserid=<%=request.getParameter("lguserid") %>&lgcorpcode=<%=request.getParameter("lgcorpcode") %>" method="post"
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
					<input type="hidden" id="depNam" name="depNam" value="<%=session.getAttribute("deprptdname") == null ? "?????????" : session.getAttribute("deprptdname").toString().replace("<", "&lt;").replace(">", "&gt;")%>" />
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
					<div id="condition">
						<table>
							<tbody>
								<tr>
									<td>
										<emp:message key="cxtj_sjcx_report_gjdm" defVal="???????????????" fileName="cxtj"></emp:message>
									</td>
									<td>
										<input type="text"  id="areacode" name="areacode" onblur="javascript:numberControl($(this))" onkeyup="javascript:numberControl($(this))"
										value='<%=request.getParameter("areacode")!=null?request.getParameter("areacode"):"" %>' maxlength="21" />
									</td>
									<td>	
										<emp:message key="cxtj_sjcx_report_gjmc" defVal="???????????????" fileName="cxtj"></emp:message>
									</td>
									<td>
										<input type="text" name="areaname" id="areaname" class="input_bd" value="<%=request.getParameter("areaname")!=null?request.getParameter("areaname"):"" %>" maxlength="50" />
									</td> 
									<td class="tdSer">
										<center>
											<a id="search" name="research"></a>
										</center>
									</td>
								</tr>
							</tbody>
						</table>
					</div>
					<table id="content">
						<thead>
							<tr>
								<th>
									<emp:message key="cxtj_sjcx_report_sj" defVal="??????" fileName="cxtj"></emp:message>
								</th>
								<th>
									<emp:message key="cxtj_sjcx_report_gjdqdm" defVal="??????/????????????" fileName="cxtj"></emp:message>
								</th>
								<th>
									<%=gjmc %>
								</th>
								<th>
									<emp:message key="cxtj_sjcx_report_jgczy" defVal="??????/?????????" fileName="cxtj"></emp:message>
								</th>
								<th>
									<emp:message key="cxtj_sjcx_report_tdhm" defVal="????????????" fileName="cxtj"></emp:message>
								</th>
								<th>
									<emp:message key="cxtj_sjcx_report_tdmc" defVal="????????????" fileName="cxtj"></emp:message>
								</th>
								<th>
									<emp:message key="cxtj_sjcx_report_fslx" defVal="????????????" fileName="cxtj"></emp:message>
								</th>
								<%
								List<RptConfInfo> rptConList = RptConfBiz.getRptConfMap().get(RptStaticValue.DEP_RPT_CONF_MENU_ID);
								for(RptConfInfo rptConf : rptConList)
								{
								%>
								<%--<th><%=rptConf.getName() %></th>--%>
								<th><%=MessageUtils.extractMessage("cxtj",rptConf.getName(),request)%></th>
								
								<%} %>
							</tr>
						</thead>
						<tbody>
							<%
							if(resultListt==null||resultListt.size()==0){
							%>
							<tr>
								<td colspan="<%=7+rptConList.size() %>" align="center">
									<emp:message key="cxtj_sjcx_report_wjl" defVal="?????????" fileName="cxtj"></emp:message>
								</td>
							</tr>
							<%
							}else{
								if(findResult != null){
							%>
							<tr>
								<td colspan="<%=7+rptConList.size() %>" align="center">
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
								<%
								//????????????????????????  eg???2012???12???12??? 
								String btime = request.getParameter("begintime") != null ? request.getParameter("begintime") : beginTime;
								String etime = request.getParameter("endtime") != null ? request.getParameter("endtime") : endTime;
								String showTime = "";
								if(!"".equals(btime) && null != btime && 0 != btime.length()){
									String btemp[] = btime.split("-");
									if(btemp.length==1){
											//btime = btemp[0] + "???";
											btime = btemp[0] + MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_y", request);
									}else if(btemp.length==2){
											//btime = btemp[0] + "???" + btemp[1] + "???";
											btime = btemp[0] + MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_y", request) + btemp[1] + MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_m", request);
									}else{
											//btime = btemp[0] + "???" + btemp[1] + "???" + btemp[2] + "???";
											btime = btemp[0] + MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_y", request) + btemp[1] + MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_m", request) + btemp[2] + MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_d", request);
									}
								}
								if(!"".equals(etime) && null != etime && 0 != etime.length()){
									String etemp[] = etime.split("-");
									if(etemp.length==1){
											etime = "";
									}else if(etemp.length==2){
											etime = "";
									}else{
											//etime = " - " +etemp[0] + "???" + etemp[1] + "???" + etemp[2] + "???";
											etime = " - " +etemp[0] + MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_y", request) + etemp[1] + MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_m", request) + etemp[2] + MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_d", request);
									}
								}
								showTime = btime +  etime;
								session.setAttribute("showareatime", showTime);
								%>
								<td><%=showTime%></td>
								<td><%=dynrptvo.get("areacode")!=null?dynrptvo.get("areacode").toString():"" %></td>
								<td><%=dynrptvo.get("areaname")!=null?dynrptvo.get("areaname").toString():"" %></td>
								<td class="textalign">
								<%=session.getAttribute("userareadepname")!=null?session.getAttribute("userareadepname"):"--" %>	
								</td>
								<td><%=dynrptvo.get("spgate")!=null?dynrptvo.get("spgate").toString():"" %></td>
								<td><%=dynrptvo.get("gatename")!=null?dynrptvo.get("gatename").toString():"" %></td>
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
								<td colspan="7">
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
								<td colspan="<%=7+rptConList.size() %>">
									<input type="hidden" name="pageTotalRec" id="pageTotalRec"	value="<%=pageInfo.getTotalRec()%>" />
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
		<link rel="stylesheet" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>">
		<script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.ztree-myjquery-b.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
				<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/cxtj_<%=langName%>.js"></script>

		<script type="text/javascript">

		
		function back(){
		    var mstype = '<%=deprptvodetail == null || deprptvodetail.getMstype()==null ? "" : deprptvodetail.getMstype()%>';
		  	var pageIndex='<%=request.getParameter("lgpageIndex")==null?"1":request.getParameter("lgpageIndex")%>';
	   	    var pageSize='<%=request.getParameter("lgpageSize")==null?"15":request.getParameter("lgpageSize")%>';
	   	    
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
			
			$('#search').click(function(){
				//????????????????????????????????????
			    submitForm(); 		        
			});			
			
			
			
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
								var reportType='<%=deprptvodetail.getReporttype()!=null?deprptvodetail.getReporttype():"" %>'
								var sendtime = '<%=deprptvodetail.getSendtime()!=null?deprptvodetail.getSendtime():beginTime %>';
							   	var recvtime = '<%=deprptvodetail.getEndtime()!=null?deprptvodetail.getEndtime():endTime%>';
						   	    var datasourcetype='<%=deprptvodetail.getDatasourcetype() == null ? "" : deprptvodetail.getDatasourcetype()%>'
				   	    		var mstype = '<%=deprptvodetail.getMstype() == null ? "" : deprptvodetail.getMstype()%>';
								var depid='<%=request.getParameter("id")==null?"":request.getParameter("id") %>';
							   	var idtype = '<%=deprptvodetail.getIdtype() == null ? "" : deprptvodetail.getIdtype() %>';
							   	var spnumtype='<%=spnumtype == null ? "" : spnumtype%>';
							   	var areacode='<%=request.getParameter("areacode")!=null?request.getParameter("areacode"):"" %>';
							   	var areaname='<%=request.getParameter("areaname")!=null?request.getParameter("areaname"):"" %>';
							   	var reportType = $("#reportType").val();
						   		$.ajax({
									type: "POST",
									url: "<%=path%>/rep_sysDepReport.htm?method=r_sdRptAreaDetailExportExcel",
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
										spnumtype:spnumtype,
										areacode:areacode,
										areaname:areaname
									},
						            beforeSend:function () {
						                page_loading();
						            },
						            complete:function () {
						           	  	page_complete()
						            },
						            success:function (result) {
						                    if (result == 'true') {
						                        download_href("<%=path%>/rep_sysDepReport.htm?method=depNationdownloadFile");
						                    } else {
						                        //alert('???????????????');
						                        alert(getJsLocaleMessage("cxtj","cxtj_sjcx_report_text_3"));
						                    }
			           					}
							 	});
							   	
						 		//location.href="<%=path%>/rep_sysDepReport.htm?method=r_sdRptAreaDetailExportExcel&lguserid=<%=request.getParameter("lguserid")%>&lgcorpcode=<%=request.getParameter("lgcorpcode")%>&reportType="+reportType+"&idtype="+idtype+"&begintime="+sendtime+"&endtime="+recvtime+"&id="+depid+"&mstype="+mstype+"&datasourcetype="+datasourcetype+"&spnumtype="+spnumtype+"&areacode="+areacode+"&areaname="+areaname;
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
