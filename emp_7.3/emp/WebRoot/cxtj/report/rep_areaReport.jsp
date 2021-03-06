<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.entity.report.AprovinceCity"%>
<%@ page import="com.montnets.emp.entity.system.LfPageField"%>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils"%>
<%@page import="com.montnets.emp.report.bean.RptConfInfo"%>
<%@page import="com.montnets.emp.report.bean.RptStaticValue"%>
<%@page import="com.montnets.emp.report.biz.ReportBiz"%>
<%@page import="com.montnets.emp.report.biz.RptConfBiz"%>
<%@page import="com.montnets.emp.report.vo.AreaReportVo"%>
<%@page import="com.montnets.emp.util.PageInfo"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%@include file="/common/common.jsp" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
String inheritPath = iPath.substring(0,	iPath.lastIndexOf("/"));
String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
PageInfo pageInfo = null;
if(request.getAttribute("pageInfo")!=null){
	pageInfo = (PageInfo) request.getAttribute("pageInfo");
}else{
	pageInfo=new PageInfo();
}
MessageUtils messageUtils = new MessageUtils();

	String icount = request.getAttribute("icount")==null?"0":request.getAttribute("icount").toString();
	String rsucc = request.getAttribute("rsucc")==null?"0":request.getAttribute("rsucc").toString();
	String rfail1 = request.getAttribute("rfail1")==null?"0":request.getAttribute("rfail1").toString();
	String rfail2 = request.getAttribute("rfail2")==null?"0":request.getAttribute("rfail2").toString();
	String rnret = request.getAttribute("rnret")==null?"0":request.getAttribute("rnret").toString();

@ SuppressWarnings("unchecked")
Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//????????????Map
@ SuppressWarnings("unchecked")
Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
String menuCode = titleMap.get("areaReport");
menuCode = menuCode==null?"0-0-0":menuCode;
AreaReportVo areaReportVo = areaReportVo=(AreaReportVo)request.getAttribute("areaReportVo");;
@ SuppressWarnings("unchecked")
List<AprovinceCity> acitys=(List<AprovinceCity>)request.getAttribute("acitys");
@ SuppressWarnings("unchecked")
List<AreaReportVo> reportList=(List<AreaReportVo>)request.getAttribute("reportList");
@ SuppressWarnings("unchecked")
List<LfPageField> pagefileds=(List<LfPageField>)session.getAttribute("pagefileds");
String counTime = request.getAttribute("countTime")==null?"":request.getAttribute("countTime").toString();
Boolean isFirstEnter = (Boolean) request.getAttribute("isFirstEnter");
   //String strshow="?????????";
   String strshow=MessageUtils.extractMessage("cxtj", "cxtj_sjcx_yystjbb_wjj", request);
if(isFirstEnter!=null&&isFirstEnter){
   		//strshow="??????????????????????????? ";
   		strshow=MessageUtils.extractMessage("cxtj", "cxtj_sjcx_yystjbb_qdjcxhqsj", request);
}
//excel???????????????
	//??????
	String sj = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_yystjbb_sj", request);
    
	//??????
	String qy = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_qytjbb_qy", request);
    if(qy!=null&&qy.length()>1){
    	qy = qy.substring(0,qy.length()-1);
    }
	//???????????????
	String fscgs = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_yystjbb_fscgs", request);
	//???????????????
	String jssbs = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_yystjbb_jssbs", request);
	
	Map<String,String> excelConditionMap = new java.util.LinkedHashMap<String,String>();
	//excelConditionMap.put("??????","imonth");
	//excelConditionMap.put("??????","area");
	//excelConditionMap.put("???????????????","icount&-&rfail");
	//excelConditionMap.put("???????????????","rfail");
	
	excelConditionMap.put(sj,"imonth");
	excelConditionMap.put(qy,"area");
	excelConditionMap.put(fscgs,"icount&-&rfail");
	excelConditionMap.put(jssbs,"rfail");
	
	session.setAttribute("EXCEL_MAP",excelConditionMap);
	java.util.Iterator<Map.Entry<String, String>> iter = null;
    Map.Entry<String, String> e = null;
  String findResult= (String)request.getAttribute("findresult");
  String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	//????????????
	int reportType = 2;
	String startTime = "";
	String endTime = "";
	int msType = areaReportVo!=null&&areaReportVo.getMstype()!=null?areaReportVo.getMstype():0;
	String province = areaReportVo!=null&&areaReportVo.getProvince()!=null?areaReportVo.getProvince() : "";
	if(areaReportVo!=null&&areaReportVo.getReporttype()!=null){
		reportType = areaReportVo.getReporttype();
		startTime = areaReportVo.getStartdate()==null?"":areaReportVo.getStartdate();
		endTime = areaReportVo.getEnddate()==null?"":areaReportVo.getEnddate();
	}
	boolean isDes = "1".equals(request.getParameter("isDes"));//??????????????????
%>
<!doctype html>
<html lang="en">
	<head>
		<%--??????????????????--%>
		<title><%=titleMap.get(menuCode) %></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<%if(StaticValue.ZH_HK.equals(langName)){%>
		<link href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css"/>
		<link href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
		<%}%>
		<link href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
		<link href="<%=commonPath%>/cxtj/common/css/inbox.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
	</head>

	<body class="rep_areaReport">
		<div id="container" class="container">
			<%-- ???????????? --%>
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode) %>
						
			<%-- ???????????? --%>
			<%if(btnMap.get(menuCode+"-0")!=null) { %>
			<div id="rContent" class="rContent">
			<form name="pageForm" action="rep_areaReport.htm?method=find" method="post" id="pageForm">
						<div class="buttons">
							<%if(isDes){%> 
								<span id="backgo" class="right mr5" onclick="showback()">&nbsp;<emp:message key="cxtj_sjcx_yystjbb_fh" defVal="??????" fileName="cxtj"></emp:message></span>
							<%}else{%>
							<div id="toggleDiv" >
								<%-- <img id="searchIcon" src="<%=inheritPath %>/images/toggle_collapse.png" title="??????????????????"/> --%>
							</div>
							<%} %>
							<a id="exportCondition"><emp:message key="cxtj_sjcx_yystjbb_dc" defVal="??????" fileName="cxtj"></emp:message></a>
	                 	    <input type="hidden" id="menucode" value="24" name="menucode" />
	                 	    <input type="hidden" id="pageTotalRec" name="pageTotalRec" value="<%=pageInfo.getTotalRec() %>"/>
						</div>
						<div id="condition">
							<table>
								<tr>
									<td><emp:message key="cxtj_sjcx_qytjbb_qy" defVal="?????? ???" fileName="cxtj"></emp:message></td>
									<td>
										
											<select name="provinces" id="provinces" isInput="false">
												<option value=""><emp:message key="cxtj_sjcx_yystjbb_qb" defVal="??????" fileName="cxtj"></emp:message></option>
													<% for(AprovinceCity a: acitys){ %>
													<option <%if(areaReportVo!=null&&areaReportVo.getProvince()!=null&&areaReportVo.getProvince().equals(a.getProvince())){ %> selected="selected" <%} %> value="<%=a.getProvince() %>"><%=a.getProvince() %></option>
													<%
														}
													 %>
												</select>
												<input type="hidden" id="province" name="province" value='<%=province %>' />
										
									</td>
									<%
									String typename="";
									if(pagefileds!=null&&pagefileds.size()>0){
										LfPageField first=pagefileds.get(0);
										typename=first.getField()+"???";
									} 
									
								    %>
									<td><emp:message key="cxtj_sjcx_yystjbb_xxlx" defVal="????????????" fileName="cxtj"/></td>
									<td><%--
										<select name="msType" id="msType" style="width:180px">
										<%
										if(pagefileds!=null&&pagefileds.size()>1){
											for(int i=1;i<pagefileds.size();i++){
											LfPageField pagefid=pagefileds.get(i);
											
										%>
										     <option value="<%=pagefid.getSubFieldValue() %>" <%=pagefid.getSubFieldValue().equals(areaReportVo!=null&&areaReportVo.getMstype()!=null?areaReportVo.getMstype().toString():"")?"selected":"" %>><%=pagefid.getSubFieldName() %></option>
										<% 
											}
										}
										%>
										</select> --%>
										<select name="msType" id="msType" isInput="false">
											<option value="0" <%="0".equals(areaReportVo!=null&&areaReportVo.getMstype()!=null?areaReportVo.getMstype().toString():"")?"selected":"" %>><emp:message key="cxtj_sjcx_yystjbb_dx" defVal="??????" fileName="cxtj"/></option>
											<option value="1" <%="1".equals(areaReportVo!=null&&areaReportVo.getMstype()!=null?areaReportVo.getMstype().toString():"")?"selected":"" %>><emp:message key="cxtj_sjcx_yystjbb_cx" defVal="??????" fileName="cxtj"/></option>
										</select>
										</td>
										<td colspan="2"></td>
									<td class="tdSer">
										<center><a id="search"></a></center>
									</td> 
								</tr>
								<tr>
									<td><emp:message key="cxtj_sjcx_zhtjbb_bblx" defVal="???????????? ???" fileName="cxtj"></emp:message></td>
									<td id="timeSelect">
										<select name="reportType" id="reportType">
										      <option value="0"><emp:message key="cxtj_sjcx_yystjbb_ybb" defVal="?????????" fileName="cxtj"></emp:message></option>
										     <option value="1" ><emp:message key="cxtj_sjcx_yystjbb_nbb" defVal="?????????" fileName="cxtj"></emp:message></option>
										     <option value="2" ><emp:message key="cxtj_sjcx_yystjbb_rbb" defVal="?????????" fileName="cxtj"></emp:message></option>
										</select>
									</td> 
									<td>
										<input type="hidden" value="month" name="yearOrMonth" />
										<input type="hidden" value="2" name="count" />
										<span id="countSpan"><emp:message key="cxtj_sjcx_zhtjbb_tjny" defVal="???????????????" fileName="cxtj"></emp:message></span>???
									</td>
									<td>
										<input type="text" name="countTime" id="countTime" value="" readonly="readonly" class="Wdate startdate"/>
										<input type="hidden" id="tTime" value="<%=counTime %>"/>
										<input type="hidden" id="startTime" value="<%=startTime %>"/>
									</td>
									<td><span class="novisible"><emp:message key="cxtj_sjcx_yystjbb_z" defVal="??????" fileName="cxtj"></emp:message></span></td>
									<td>
										<input type="text" name="endTime" value="" readonly="readonly" class="Wdate novisible enddate"/>
										<input type="hidden" id="endTime" value="<%=endTime %>"/>
									</td>
									<td><input type="hidden" name="isDes" id="isDes" value="<%=isDes?1:0 %>"/></td>
								</tr>
								<tr class="hidden"></tr>
							</table>
						</div>
						<table id="content">
							<thead>
							    <tr>
								    <th><emp:message key="cxtj_sjcx_yystjbb_sj" defVal="??????" fileName="cxtj"></emp:message></th>
								   	<th><emp:message key="cxtj_rpt_qy" defVal="??????" fileName="cxtj"></emp:message></th>
							 	   <%
								List<RptConfInfo> rptConList = RptConfBiz.getRptConfMap().get(RptStaticValue.AREA_RPT_CONF_MENU_ID);
								String temp = null;
								for(RptConfInfo rptConf : rptConList)
								{
									temp = rptConf.getName();
								%>
								<th><%=MessageUtils.extractMessage("cxtj",temp,request) %></th>
								<%} %>
						     <%if(!isDes){%><th><emp:message key="cxtj_sjcx_yystjbb_xq" defVal="??????" fileName="cxtj"></emp:message></th><%} %>
						        </tr>
							</thead>
							<tbody>
								<%
								if (reportList != null && reportList.size() > 0)
								{
									//SimpleDateFormat sdf = new SimpleDateFormat("yyyy???M???d???");
									SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
									//SimpleDateFormat sdf = new SimpleDateFormat("yyyy"+MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_year", request)+"M"+MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_month", request)+"d"+MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_day", request));
									SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
									//DateFormat df = DateFormat.getDateInstance();
									String timestr = "";
									if(reportType == 2){
										//String startDate = sdf.format(df.parse(startTime));
										//String endDate = sdf.format(df.parse(endTime));
										//timestr = startDate+"???"+endDate;
										String startDate="";
										String endDate="";
										if(!"".equals(startTime) && null != startTime && 0 != startTime.length())
										{
											String btemp[] = startTime.split("-");
											//startDate = btemp[0] + "???" + btemp[1] + "???" + btemp[2] + "???";
											startDate = startTime;
											//startDate = btemp[0] + MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_year", request) + btemp[1] + MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_month", request) + btemp[2] + MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_day", request);
										}

										if(!"".equals(endTime) && null != endTime && 0 != endTime.length())
										{
											String etemp[] = endTime.split("-");
											//endDate = etemp[0] + "???" + etemp[1] + "???" + etemp[2] + "???";
											endDate = etemp[0] + "-" + etemp[1] + "-" + etemp[2];
											//endDate = etemp[0] + MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_year", request) + etemp[1] + MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_month", request) + etemp[2] + MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_day", request);
										}
										//timestr = startDate + " ??? " + endDate;
										timestr = startDate + "  "+  MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_z", request)+"  " + endDate;
										//timestr = startDate +  MessageUtils.extractMessage("cxtj", "cxtj_sjcx_yystjbb_z", request).substring(0,MessageUtils.extractMessage("cxtj", "cxtj_sjcx_yystjbb_z", request).length()-1) + endDate;
									}
									for(AreaReportVo report : reportList)
									{
									 // if(!report.getSpID().equals(""))
							%>
								<tr>
									<td>
										<%
											if(!isDes){
												if(reportType == 2){
													out.print(timestr);
												}else if(reportType == 1){
													//out.print(report.getY()+"???");
													out.print(report.getY());
													//out.print(report.getY()+MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_year", request));
												}else{
													//out.print(report.getY()+"???"+report.getImonth()+"???");
													out.print(report.getY()+"-"+report.getImonth());
													//out.print(report.getY()+MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_year", request)+report.getImonth()+MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_month", request));
												}
											}else{
												if(reportType == 2){
													out.print(sdf.format(sdf1.parse(report.getIymd())));
												}else if(reportType == 1){
													//out.print(report.getY()+"???"+report.getImonth()+"???");
													out.print(report.getY()+"-"+report.getImonth());
													//out.print(report.getY()+MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_year", request)+report.getImonth()+MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_month", request));
												}else{
													out.print(sdf.format(sdf1.parse(report.getIymd())));
												}
											}
										%>
									</td>
									<td>
										<%=report.getProvince()!=null&&!("").equals(report.getProvince())?report.getProvince().trim():MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_wz", request)%>
									</td>
									
										<%
	 							//???????????????????????? ????????????map
	 							Map<String,String> map=ReportBiz.getRptNums(report.getIcount(),report.getRsucc(),report.getRfail1(),
	 							report.getRfail2(),report.getRnret(),RptStaticValue.AREA_RPT_CONF_MENU_ID);
								for(RptConfInfo rptConf : rptConList)
								{
									if(RptStaticValue.RPT_ICOUNT_COLUMN_ID.equals(rptConf.getColId()))
									{//????????????
								%>
								<td><%=report.getIcount() %></td>
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
								<td><%=report.getRfail1() %></td>
								<%
									}
									else if(RptStaticValue.RPT_RFAIL2_COLUMN_ID.equals(rptConf.getColId()))
									{//???????????????
								%>
								<td><%=report.getRfail2() %></td>
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
								<td><%=report.getRnret() %></td>
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
									<%if(!isDes){%>
									<td>
										<a class="des"><emp:message key="cxtj_sjcx_yystjbb_ck" defVal="??????" fileName="cxtj"></emp:message></a>
									</td>
									<%}%>
								</tr>
								<%
								     
								  }%>
								  <tr>
									<td colspan="2"><b><emp:message key="cxtj_sjcx_yystjbb_hj" defVal="?????????" fileName="cxtj"></emp:message></b></td>
							       
								<%
	 							//???????????????????????? ????????????map
	 							Map<String,String> mapsum=ReportBiz.getRptNums(Long.valueOf(icount),Long.valueOf(rsucc),Long.valueOf(rfail1),
	 							Long.valueOf(rfail2),Long.valueOf(rnret),RptStaticValue.AREA_RPT_CONF_MENU_ID);
								for(RptConfInfo rptConf : rptConList)
								{
									if(RptStaticValue.RPT_ICOUNT_COLUMN_ID.equals(rptConf.getColId()))
									{//????????????
								%>
								<td><%=icount %></td>
								<%
									}
									else if(RptStaticValue.RPT_FSSUCC_COLUMN_ID.equals(rptConf.getColId()))
									{//???????????????
								%>
								<td><%=mapsum.get(RptStaticValue.RPT_FSSUCC_COLUMN_ID)!=null?mapsum.get(RptStaticValue.RPT_FSSUCC_COLUMN_ID):0 %></td>
								<%
									}
									else if(RptStaticValue.RPT_RFAIL1_COLUMN_ID.equals(rptConf.getColId()))
									{//???????????????
								%>
								<td><%=rfail1 %></td>
								<%
									}
									else if(RptStaticValue.RPT_RFAIL2_COLUMN_ID.equals(rptConf.getColId()))
									{//???????????????
								%>
								<td><%=rfail2 %></td>
								<%
									}
									else if(RptStaticValue.RPT_JSSUCC_COLUMN_ID.equals(rptConf.getColId()))
									{//???????????????
								%>
								<td><%=mapsum.get(RptStaticValue.RPT_JSSUCC_COLUMN_ID)!=null?mapsum.get(RptStaticValue.RPT_JSSUCC_COLUMN_ID):0 %></td>
								<%
									}
									else if(RptStaticValue.RPT_RNRET_COLUMN_ID.equals(rptConf.getColId()))
									{//?????????
								%>
								<td><%=rnret %></td>
								<%
									}
									else if(RptStaticValue.RPT_FSSUCC_ICOUNT_COLUMN_ID.equals(rptConf.getColId()))
									{//???????????????
								%>
								<td><%=mapsum.get(RptStaticValue.RPT_FSSUCC_ICOUNT_COLUMN_ID)!=null?mapsum.get(RptStaticValue.RPT_FSSUCC_ICOUNT_COLUMN_ID):0 %></td>
								<%
									}
									else if(RptStaticValue.RPT_RFAIL1_ICOUNT_COLUMN_ID.equals(rptConf.getColId()))
									{//???????????????
								%>
								<td><%=mapsum.get(RptStaticValue.RPT_RFAIL1_ICOUNT_COLUMN_ID)!=null?mapsum.get(RptStaticValue.RPT_RFAIL1_ICOUNT_COLUMN_ID):0 %></td>
								<%
									}
									else if(RptStaticValue.RPT_JSSUCC_FSSUCC_COLUMN_ID.equals(rptConf.getColId()))
									{//???????????????
								%>
								<td><%=mapsum.get(RptStaticValue.RPT_JSSUCC_FSSUCC_COLUMN_ID)!=null?mapsum.get(RptStaticValue.RPT_JSSUCC_FSSUCC_COLUMN_ID):0 %></td>
								<%
									}
									else if(RptStaticValue.RPT_RFAIL2_FSSUCC_COLUMN_ID.equals(rptConf.getColId()))
									{//???????????????
								%>
								<td><%=mapsum.get(RptStaticValue.RPT_RFAIL2_FSSUCC_COLUMN_ID)!=null?mapsum.get(RptStaticValue.RPT_RFAIL2_FSSUCC_COLUMN_ID):0 %></td>
								<%
									}
									else if(RptStaticValue.RPT_RNRET_FSSUCC_COLUMN_ID.equals(rptConf.getColId()))
									{//?????????
								%>
								<td><%=mapsum.get(RptStaticValue.RPT_RNRET_FSSUCC_COLUMN_ID)!=null?mapsum.get(RptStaticValue.RPT_RNRET_FSSUCC_COLUMN_ID):0 %></td>
								<%}
								} %>
									<%if(!isDes){%><td>-</td><%} %>
								</tr>
								<%}else{
							%> 
							<tr>
									<td colspan="<%=(isDes?2:3)+rptConList.size() %>">
									<%=strshow %>
									</td>
								</tr>
							<%}

							 %>
							
							</tbody>
							<tfoot>
								<tr>
									<td colspan="<%=(isDes?2:3)+rptConList.size() %>">
										<div id="pageInfo"></div>
									</td>
								</tr>
							</tfoot>
						</table>
				
			<%-- ???????????? --%>
			<%-- foot?????? --%>
			<%-- ???????????? --%>
			<%-- foot?????? --%>
			<div class="bottom">
				<div id="bottom_right">
					<div id="bottom_left"></div>
					<div id="bottom_main">
					</div>
				</div>
			</div>
			<div id="corpCode" class="hidden"></div>
			 </form>
			 </div>
			 <%} %>
			<%-- foot?????? --%>
		</div>
    <div class="clear"></div>
		<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=iPath %>/js/rep_spisuncmMtReport.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/cxtj_<%=langName%>.js"></script>
		
		<script type="text/javascript">
		//?????????????????????
		var _province = "<%=province%>";
		var _msType = "<%=msType%>";
		var _reportType = "<%=reportType%>";
		var _startTime = "<%=startTime%>";
		var _endTime = "<%=endTime%>";
		$(document).ready(function(){
		var isDes = $("#isDes").val();
		if(isDes === 1 || isDes === "1"){
			$("#condition").hide();
		}
		getLoginInfo("#corpCode");
			var findresult="<%=findResult%>";
			$('#reportType option[value="<%=reportType%>"]').attr('selected',true);
		    if(findresult=="-1")
		    {
		       //alert("??????????????????,???????????????????????????!");	
		       alert(getJsLocaleMessage("cxtj","cxtj_sjcx_xtsxjl_text_1"));	
		       return;			       
		    }
		    
			
			$("#toggleDiv").toggle(function() {
					$("#condition").hide();
					$(this).addClass("collapse");
				}, function() {
					$("#condition").show();
					$(this).removeClass("collapse");
				});
				

			$("#content tbody tr").hover(function() {
					$(this).addClass("hoverColor");
				}, function() {
					$(this).removeClass("hoverColor");
				});

		//?????????????????????excel
			$("#exportCondition").click(
			 function(){
				  //if(confirm("????????????????????????excel?"))
				  if(confirm(getJsLocaleMessage("cxtj","cxtj_sjcx_xtsxjl_text_2")))
				   {

				   		var countTime=_startTime;
				   		var endTime=_endTime;
				   		var reportType = _reportType;
				   		var province=_province;
				   		<%if (reportList != null && reportList.size() > 0) {%>			
				   			var mstype=_msType;
				   			var isDes = $('#isDes').val();
				   			$.ajax({
								type: "POST",
								url: "<%=path%>/rep_areaReport.htm?method=r_areaRptExportExcel",
								data: {
										lgcorpcode:$('#lgcorpcode').val(),
									   	countTime:countTime,
									   	reportType:reportType,
									   	mstype:mstype,
									   	province:province,
									   	isDes:isDes,
									   	endTime:endTime
									  },
				                beforeSend:function () {
				                    page_loading();
				                },
				                complete:function () {
				               	  	page_complete()
				                },
				                success:function (result) {
				                    if (result == 'true') {
				                        download_href("<%=path%>/rep_areaReport.htm?method=downloadFile");
				                    } else {
				                        //alert('???????????????');
				                        alert(getJsLocaleMessage("cxtj","cxtj_sjcx_xtsxjl_text_3"));
				                    }
               					}
							});
				   		//location.href="<%=path%>/rep_areaReport.htm?method=r_areaRptExportExcel&lgcorpcode="+$('#lgcorpcode').val()+"&countTime="+countTime+"&reportType="+reportType+"&totalC="+totalC+"&totalF="+totalF+"&mstype="+mstype+"&province="+province+"&isDes="+isDes+"&endTime="+endTime;
				   		
				   		<%} else {%>
				   		
				   		//alert("?????????????????????");
				   		alert(getJsLocaleMessage("cxtj","cxtj_sjcx_xtsxjl_text_4"));
				   		<%}%>
				   		}
			  });
			
			initPage(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>);
			$('#search').click(function(){submitForm();});
			$('#provinces').isSearchSelect({'width':'180','zindex':1},function(data){
				//keyup click????????????
					$("#province").val(data.value);
			},function(data){
				//???????????????
				var val=$("#province").val();
				if(val){
					data.box.input.val(val);
				}
			});
			$('#msType').isSearchSelect({'width':'180','zindex':1,'isInput':false});
            $('#reportType').isSearchSelectNew(
                {'width':'180','zindex':0,'isInput':false},
                function(){$("#reportType").change();
                }
            );
            $("#reportType").change();
            $("#timeSelect div:nth-child(3)").hide();
		});
	
		</script>
		
	</body>
</html>
