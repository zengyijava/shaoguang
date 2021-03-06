<%@page import="com.montnets.emp.entity.passage.XtGateQueue"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="com.montnets.emp.wyquery.bean.RptWyConfInfo"%>
<%@ page import="com.montnets.emp.wyquery.bean.RptWyStaticValue"%>
<%@ page import="com.montnets.emp.wyquery.biz.ReportWyBiz" %>
<%@page import="com.montnets.emp.wyquery.biz.RptWyConfBiz"%>
<%@page import="com.montnets.emp.wyquery.vo.WyReportVo"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<%

String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
 String inheritPath = iPath.substring(0,
		iPath.lastIndexOf("/"));
String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
PageInfo pageInfo = new PageInfo();
pageInfo = (PageInfo) request.getAttribute("pageInfo");

	String icount = request.getAttribute("icount")==null?"0":request.getAttribute("icount").toString();
	String rsucc = request.getAttribute("rsucc")==null?"0":request.getAttribute("rsucc").toString();
	String rfail1 = request.getAttribute("rfail1")==null?"0":request.getAttribute("rfail1").toString();
	String rfail2 = request.getAttribute("rfail2")==null?"0":request.getAttribute("rfail2").toString();
	String rnret = request.getAttribute("rnret")==null?"0":request.getAttribute("rnret").toString();
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	//Date date = new Date();
	Calendar c=Calendar.getInstance();
	c.add(Calendar.DAY_OF_MONTH,-1);
	String  beginTime= df.format(c.getTime()).substring(0, 8)+"01";
	//String  endTime =df.format(date).substring(0, 8)+"07" ;
	
	String  endTime =df.format(c.getTime()).substring(0, 11) ;  //change by dj

@ SuppressWarnings("unchecked")
 Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//????????????Map
@ SuppressWarnings("unchecked")
Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
String menuCode = titleMap.get("statisticalReport");
menuCode = menuCode==null?"0-0-0":menuCode;
@ SuppressWarnings("unchecked")
List<WyReportVo> reportList=(List<WyReportVo>)session.getAttribute("wyreportList");
@ SuppressWarnings("unchecked")
List<XtGateQueue> xtGateQueueList=(List<XtGateQueue>)request.getAttribute("xtGateQueueList");
long[] sumCount=(long[])session.getAttribute("sumCount");
//excel???????????????
	Map<String,String> excelConditionMap = new java.util.LinkedHashMap<String,String>();
	excelConditionMap.put(MessageUtils.extractMessage("wygl", "wygl_common_text49", request),"imonth");
	excelConditionMap.put(MessageUtils.extractMessage("wygl", "wygl_common_text47", request),"spagte");
	excelConditionMap.put(MessageUtils.extractMessage("wygl", "wygl_common_text48", request),"gatename");
	session.setAttribute("EXCEL_MAP",excelConditionMap);
	java.util.Iterator<Map.Entry<String, String>> iter = null;
    Map.Entry<String, String> e = null;
    String reportType=request.getParameter("reportType");
    String findResult= (String)request.getAttribute("findresult");
    String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
    Boolean isFirstEnter=(Boolean)request.getAttribute("isFirstEnter");
    String strshow=MessageUtils.extractMessage("common", "common_norecord", request);
   if(isFirstEnter!=null&&isFirstEnter){
   		reportList=null;
   		strshow=MessageUtils.extractMessage("wygl", "wygl_common_text29", request);
   }
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
    
%>
<html>
	<head>
		<title><%=titleMap.get(menuCode) %></title>
		<%@include file="/common/common.jsp" %>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion() %>" />
		
		<%if(StaticValue.ZH_HK.equals(langName)){%>	
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
			<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
	</head>

	<body>
		<div id="container" class="container">
			<%-- ???????????? --%>
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(empLangName,menuCode) %>
			
			<%-- ???????????? --%>
			<%if(btnMap.get(menuCode+"-0")!=null) { %>
			<form name="pageForm" action="wyq_statisticalReport.htm?method=find" method="post" id="pageForm">
				<div id="rContent" class="rContent">
						<div class="buttons">
							<div id="toggleDiv" >
							</div>
							 <a id="exportCondition" ><emp:message key="common_export" defVal="??????" fileName="common"></emp:message></a>
	                 	    <input type="hidden" id="menucode" value="24" name="menucode" />
	                 	    <input type="hidden" id="pageTotalRec" name="pageTotalRec" value="<%=pageInfo.getTotalRec() %>"/>
						</div>
						<div id="condition">
							<table>
								<tbody>
									<tr>
									<td><emp:message key="wygl_common_text50" defVal="???????????????" fileName="wygl"></emp:message></td>
									<td>
										<input type="text"  id="gateName" name="gateName"  style="width: 177px;" value='<%=request.getParameter("gateName")==null?"":request.getParameter("gateName") %>'maxlength="11" />
									</td>
									<td><emp:message key="wygl_common_text43" defVal="???????????????" fileName="wygl"></emp:message></td>
										<td>
										<label>
										<select style="width: 180px;" name="tdhm" id="tdhm">
											<option value=""><emp:message key="common_whole" defVal="??????" fileName="common"></emp:message></option>
											<%
												String spgate=request.getParameter("spgate");
												if (xtGateQueueList != null && xtGateQueueList.size() > 0){
													for(int k=0;k<xtGateQueueList.size();k++){
											%>
														<option value="<%=xtGateQueueList.get(k).getSpgate() %>"
															<%= xtGateQueueList.get(k).getSpgate().equals(spgate)?"selected":"" %>><%=xtGateQueueList.get(k).getSpgate()%>
														</option>
											<%		
													}
												} 
											%>
											</select>
											 <input type="hidden" id="spgate" value="<%=spgate!=null?spgate:"" %>" name="spgate">
											</label> 
											
										</td> 
										<td>&nbsp;</td>
										<td>&nbsp;</td>									
										<td class="tdSer">
												<center><a id="search"></a></center>
										</td>
									</tr>
									<tr>
									<td><emp:message key="wygl_common_text51" defVal="???????????????" fileName="wygl"></emp:message></td>
									<td id="timeSelect">
										<select name="reportType" id="reportType" style="width: 182px">
										<%if(reportType!=null) {
											String Strtype=reportType.toString();
										%>
									     <option value="0" <%if("0".equals(Strtype)){out.print("selected"); } %>><emp:message key="wygl_common_text52" defVal="?????????" fileName="wygl"></emp:message></option>
									     <option value="1" <%if("1".equals(Strtype)){out.print("selected"); } %>><emp:message key="wygl_common_text53" defVal="?????????" fileName="wygl"></emp:message></option>
									     <option value="2" <%if("2".equals(Strtype)){out.print("selected"); } %>><emp:message key="wygl_common_text54" defVal="?????????" fileName="wygl"></emp:message></option>
								     <%} else{%>
										<option value="0"><emp:message key="wygl_common_text52" defVal="?????????" fileName="wygl"></emp:message></option>
										<option value="1"><emp:message key="wygl_common_text53" defVal="?????????" fileName="wygl"></emp:message></option>
										<option value="2"><emp:message key="wygl_common_text54" defVal="?????????" fileName="wygl"></emp:message></option>
								     <%} %>
										</select>
									</td>  
									<td>
										<label id="sjlabel"><emp:message key="wygl_common_text55" defVal="????????????" fileName="wygl"></emp:message></label>???
									</td>
									<td class="tableTime">

										<input type="text"
											style="cursor: pointer; width: 178px; background-color: white;"
											class="Wdate" readonly="readonly" onclick="setime()"
											value="<%=request.getParameter("begintime") != null ? request.getParameter("begintime") : beginTime%>" id="sendtime"
											name="begintime">
											<input type="hidden" id="sTime" value="<%=beginTime %>"/>
									</td>
									<td>
										<span id="zhilabel"><emp:message key="common_to" defVal="??????" fileName="common"></emp:message></span>
									</td>
									<td >
										<input type="text"
											style="cursor: pointer; width: 178px; background-color: white;"
											class="Wdate" readonly="readonly" onclick="retime()"
											value="<%=request.getParameter("endtime") != null ? request.getParameter("endtime") : endTime%>" id="recvtime"
											name="endtime">
											<input type="hidden" id="eTime" value="<%=endTime %>"/>
									</td>
									<td>&nbsp;</td>
								</tr>
								</tbody>
							</table>
						</div>
						<table id="content">
							<thead>
							    <tr>
								<th><emp:message key="wygl_common_text49" defVal="??????" fileName="wygl"></emp:message></th>
								<th><emp:message key="wygl_common_text47" defVal="????????????" fileName="wygl"></emp:message></th>
								<th><emp:message key="wygl_common_text48" defVal="????????????" fileName="wygl"></emp:message></th>
							     <%
							     	List<RptWyConfInfo> rptConList = RptWyConfBiz.getRptConfMap().get(RptWyStaticValue.WY_RPT_CONF_MENU_ID);
							     	for(RptWyConfInfo rptConf : rptConList)
							     	{
							     %>
									<%--<th><%=rptConf.getName()%></th>--%>
									
									<%if("???????????????".equals(rptConf.getName())){%>
										<th><emp:message key="wygl_common_text25" defVal="???????????????" fileName="wygl"></emp:message>
									<%}else if("???????????????".equals(rptConf.getName())){%>
										<th><emp:message key="wygl_common_text27" defVal="???????????????" fileName="wygl"></emp:message>
									<%}else if("????????????".equals(rptConf.getName())){%>
										<th><emp:message key="wygl_common_text101" defVal="????????????" fileName="wygl"></emp:message>
									<%}else if("???????????????".equals(rptConf.getName())){%>
										<th><emp:message key="wygl_common_text102" defVal="???????????????" fileName="wygl"></emp:message>
									<%}else if("???????????????".equals(rptConf.getName())){%>
										<th><emp:message key="wygl_common_text103" defVal="???????????????" fileName="wygl"></emp:message>
									<%}else if("?????????".equals(rptConf.getName())){%>
										<th><emp:message key="wygl_common_text104" defVal="?????????" fileName="wygl"></emp:message>
									<%}else if("???????????????".equals(rptConf.getName())){%>
										<th><emp:message key="wygl_common_text105" defVal="???????????????" fileName="wygl"></emp:message>
									<%}else if("???????????????".equals(rptConf.getName())){%>
										<th><emp:message key="wygl_common_text106" defVal="???????????????" fileName="wygl"></emp:message>
									<%}else if("???????????????".equals(rptConf.getName())){%>
										<th><emp:message key="wygl_common_text107" defVal="???????????????" fileName="wygl"></emp:message>
									<%}else if("???????????????".equals(rptConf.getName())){%>
										<th><emp:message key="wygl_common_text108" defVal="???????????????" fileName="wygl"></emp:message>
									<%}else if("?????????".equals(rptConf.getName())){%>
										<th><emp:message key="wygl_common_text109" defVal="?????????" fileName="wygl"></emp:message>
									<%}else{%>
										<th><%=rptConf.getName()%></th>
									<%}%>
									
									<%
									}
									%>
							     <th><emp:message key="wygl_common_text28" defVal="??????" fileName="wygl"></emp:message></th>
							     </tr>
								</thead>
								<tbody>
									<%
										//????????????????????????  eg???2012???12???12??? 
										String btime = request.getParameter("begintime") != null ? request.getParameter("begintime") : beginTime;
										String etime = request.getParameter("endtime") != null ? request.getParameter("endtime") : endTime;
										String showTime = "";
										if( !"".equals(btime) && null != btime && 0 != btime.length())
										{
										String btemp[] = btime.split("-");
										if(btemp.length==1){
											btime = btemp[0] + MessageUtils.extractMessage("common", "text_year", request);
										}else if(btemp.length==2){
											btime = btemp[0] + MessageUtils.extractMessage("common", "text_year", request) + btemp[1] + MessageUtils.extractMessage("common", "text_month", request);
										}else{
											btime = btemp[0] + MessageUtils.extractMessage("common", "text_year", request) + btemp[1] + MessageUtils.extractMessage("common", "text_month", request) + btemp[2] + MessageUtils.extractMessage("common", "text_day", request);
										}
																				 	 		 
										}
																					
										if( !"".equals(etime) && null != etime && 0 != etime.length() )
										{
											String etemp[] = etime.split("-");
											if(etemp.length==1){
											etime = "";
										}else if(etemp.length==2){
											etime = "";
										}else{
											etime = " - " + etemp[0] + MessageUtils.extractMessage("common", "text_year", request) + etemp[1] + MessageUtils.extractMessage("common", "text_month", request) + etemp[2].trim() + MessageUtils.extractMessage("common", "text_day", request);
										}
										}
																					
										showTime = btime +  etime;
										session.setAttribute("wyshowTime",showTime);
																						
																						
										if (reportList != null && reportList.size() > 0)
										{
											for(WyReportVo report : reportList)
												{
									%>
								<tr>								    
									<td>
										<%=showTime%>
									</td>	
																	
									<td>
										<%=report.getSpgate()==null?MessageUtils.extractMessage("wygl", "wygl_common_text56", request):report.getSpgate().toUpperCase()%>
									</td>
									<td><%=report.getGateName()!=null?report.getGateName():"--"%></td>
									
								<%
									//???????????????????????? ????????????map
									Map<String,String> map=ReportWyBiz.getRptNums(report.getIcount(),report.getRsucc(),report.getRfail1(),report.getRfail2(),report.getRnret(),RptWyStaticValue.WY_RPT_CONF_MENU_ID);
									for(RptWyConfInfo rptConf : rptConList)
									{
									if(RptWyStaticValue.RPT_ICOUNT_COLUMN_ID.equals(rptConf.getColId()))
																																									{//????????????
								%>
									<td><%=report.getIcount()%></td>
								<%
									}else if(RptWyStaticValue.RPT_FSSUCC_COLUMN_ID.equals(rptConf.getColId())){//???????????????
								%>
								<td><%=map.get(RptWyStaticValue.RPT_FSSUCC_COLUMN_ID)!=null?map.get(RptWyStaticValue.RPT_FSSUCC_COLUMN_ID):0%></td>
								<%
									}else if(RptWyStaticValue.RPT_RFAIL1_COLUMN_ID.equals(rptConf.getColId())){//???????????????
								%>
								<td><%=report.getRfail1()%></td>
								<%
									}else if(RptWyStaticValue.RPT_RFAIL2_COLUMN_ID.equals(rptConf.getColId())){//???????????????
								%>
								<td><%=report.getRfail2()%></td>
								<%
									}else if(RptWyStaticValue.RPT_JSSUCC_COLUMN_ID.equals(rptConf.getColId())){//???????????????
								%>
								<td><%=map.get(RptWyStaticValue.RPT_JSSUCC_COLUMN_ID)!=null?map.get(RptWyStaticValue.RPT_JSSUCC_COLUMN_ID):0%></td>
								<%
									}else if(RptWyStaticValue.RPT_RNRET_COLUMN_ID.equals(rptConf.getColId())){//?????????
								%>
								<td><%=report.getRnret()%></td>
								<%
									}else if(RptWyStaticValue.RPT_FSSUCC_ICOUNT_COLUMN_ID.equals(rptConf.getColId())){//???????????????
								%>
								<td><%=map.get(RptWyStaticValue.RPT_FSSUCC_ICOUNT_COLUMN_ID)!=null?map.get(RptWyStaticValue.RPT_FSSUCC_ICOUNT_COLUMN_ID):0%></td>
								<%
									}else if(RptWyStaticValue.RPT_RFAIL1_ICOUNT_COLUMN_ID.equals(rptConf.getColId())){//???????????????
								%>
								<td><%=map.get(RptWyStaticValue.RPT_RFAIL1_ICOUNT_COLUMN_ID)!=null?map.get(RptWyStaticValue.RPT_RFAIL1_ICOUNT_COLUMN_ID):0%></td>
								<%
									}else if(RptWyStaticValue.RPT_JSSUCC_FSSUCC_COLUMN_ID.equals(rptConf.getColId())){//???????????????
								%>
								<td><%=map.get(RptWyStaticValue.RPT_JSSUCC_FSSUCC_COLUMN_ID)!=null?map.get(RptWyStaticValue.RPT_JSSUCC_FSSUCC_COLUMN_ID):0%></td>
								<%
									}else if(RptWyStaticValue.RPT_RFAIL2_FSSUCC_COLUMN_ID.equals(rptConf.getColId())){//???????????????
								%>
								<td><%=map.get(RptWyStaticValue.RPT_RFAIL2_FSSUCC_COLUMN_ID)!=null?map.get(RptWyStaticValue.RPT_RFAIL2_FSSUCC_COLUMN_ID):0%></td>
								<%
									}else if(RptWyStaticValue.RPT_RNRET_FSSUCC_COLUMN_ID.equals(rptConf.getColId())){//?????????
								%>
								<td><%=map.get(RptWyStaticValue.RPT_RNRET_FSSUCC_COLUMN_ID)!=null?map.get(RptWyStaticValue.RPT_RNRET_FSSUCC_COLUMN_ID):0%></td>
								<%
									}
								}
								%>
									<td>
									<a id="details"  onclick="dataReportfind('<%=report.getSpgate()%>')"><emp:message key="wygl_common_text37" defVal="??????" fileName="wygl"></emp:message></a>
									</td>
								</tr>
								<%
									}
								%>
								<tr>
									<td colspan="3"><b><emp:message key="wygl_common_text57" defVal="?????????" fileName="wygl"></emp:message></b></td>
								<%
									//???????????????????????? ????????????map
									Map<String,String> mapsum=ReportWyBiz.getRptNums(Long.valueOf(icount),Long.valueOf(rsucc),Long.valueOf(rfail1),
									Long.valueOf(rfail2),Long.valueOf(rnret),RptWyStaticValue.WY_RPT_CONF_MENU_ID);
									for(RptWyConfInfo rptConf : rptConList)
									{
										if(RptWyStaticValue.RPT_ICOUNT_COLUMN_ID.equals(rptConf.getColId()))
										{//????????????
								%>
								<td><%=icount%></td>
								<%
									}
									else if(RptWyStaticValue.RPT_FSSUCC_COLUMN_ID.equals(rptConf.getColId()))
									{//???????????????
								%>
								<td><%=mapsum.get(RptWyStaticValue.RPT_FSSUCC_COLUMN_ID)!=null?mapsum.get(RptWyStaticValue.RPT_FSSUCC_COLUMN_ID):0%></td>
								<%
									}
									else if(RptWyStaticValue.RPT_RFAIL1_COLUMN_ID.equals(rptConf.getColId()))
									{//???????????????
								%>
								<td><%=rfail1%></td>
								<%
									}
									else if(RptWyStaticValue.RPT_RFAIL2_COLUMN_ID.equals(rptConf.getColId()))
									{//???????????????
								%>
								<td><%=rfail2%></td>
								<%
									}
									else if(RptWyStaticValue.RPT_JSSUCC_COLUMN_ID.equals(rptConf.getColId()))
									{//???????????????
								%>
								<td><%=mapsum.get(RptWyStaticValue.RPT_JSSUCC_COLUMN_ID)!=null?mapsum.get(RptWyStaticValue.RPT_JSSUCC_COLUMN_ID):0%></td>
								<%
									}
									else if(RptWyStaticValue.RPT_RNRET_COLUMN_ID.equals(rptConf.getColId()))
									{//?????????
								%>
								<td><%=rnret%></td>
								<%
									}else if(RptWyStaticValue.RPT_FSSUCC_ICOUNT_COLUMN_ID.equals(rptConf.getColId()))
									{//???????????????
								%>
								<td><%=mapsum.get(RptWyStaticValue.RPT_FSSUCC_ICOUNT_COLUMN_ID)!=null?mapsum.get(RptWyStaticValue.RPT_FSSUCC_ICOUNT_COLUMN_ID):0%></td>
								<%
									}
									else if(RptWyStaticValue.RPT_RFAIL1_ICOUNT_COLUMN_ID.equals(rptConf.getColId()))
									{//???????????????
								%>
								<td><%=mapsum.get(RptWyStaticValue.RPT_RFAIL1_ICOUNT_COLUMN_ID)!=null?mapsum.get(RptWyStaticValue.RPT_RFAIL1_ICOUNT_COLUMN_ID):0%></td>
								<%
									}
									else if(RptWyStaticValue.RPT_JSSUCC_FSSUCC_COLUMN_ID.equals(rptConf.getColId()))
									{//???????????????
								%>
								<td><%=mapsum.get(RptWyStaticValue.RPT_JSSUCC_FSSUCC_COLUMN_ID)!=null?mapsum.get(RptWyStaticValue.RPT_JSSUCC_FSSUCC_COLUMN_ID):0%></td>
								<%
									}
									else if(RptWyStaticValue.RPT_RFAIL2_FSSUCC_COLUMN_ID.equals(rptConf.getColId()))
									{//???????????????
								%>
								<td><%=mapsum.get(RptWyStaticValue.RPT_RFAIL2_FSSUCC_COLUMN_ID)!=null?mapsum.get(RptWyStaticValue.RPT_RFAIL2_FSSUCC_COLUMN_ID):0%></td>
								<%
									}
									else if(RptWyStaticValue.RPT_RNRET_FSSUCC_COLUMN_ID.equals(rptConf.getColId()))
									{//?????????
								%>
								<td><%=mapsum.get(RptWyStaticValue.RPT_RNRET_FSSUCC_COLUMN_ID)!=null?mapsum.get(RptWyStaticValue.RPT_RNRET_FSSUCC_COLUMN_ID):0%></td>
								<%}
								} %> 
									<td>-</td>
								</tr>
								<%}else{
							%> 
							<tr>
									<td colspan="<%=6+rptConList.size() %>" align="center"><%=strshow %></td>
							</tr>
							<%} %>
							
							</tbody>
							<tfoot>
								<tr>
									<td colspan="<%=8+rptConList.size() %>">
										<div id="pageInfo"></div>
									</td>
								</tr>
							</tfoot>
						</table>
			</div>
			<%} %>
			<%-- ???????????? --%>
			<%-- foot?????? --%>
			<%-- ???????????? --%>
			<%-- foot?????? --%>
			<div class="bottom">
				<div id="bottom_right">
					<div id="bottom_left"></div>
					<div id="bottom_main">
						<div id="pageInfo"></div>
					</div>
				</div>
			</div>
			<div id="corpCode" class="hidden"></div>
			 </form>
			<%-- foot?????? --%>
		</div>
    <div class="clear"></div>
<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=empLangName%>/common_<%=empLangName%>.js"></script>
<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=empLangName%>/wygl_<%=empLangName%>.js"></script>
<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=commonPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=commonPath %>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript">


	//????????????
		function setime(){
			var r = $("#reportType").attr("value");
			if (r == 0)
			{
				var max = "2099-12-31";
		    	var v = $("#recvtime").attr("value");
		    	var min = "1900-01-01";
				WdatePicker({dateFmt:'yyyy-MM-dd',minDate:min,maxDate:v,isShowClear:false});
			}else if (r == 1)
			{
				WdatePicker({dateFmt:'yyyy-MM',isShowClear:false});
			}
			else
			{
				WdatePicker({dateFmt:'yyyy',isShowClear:false});
			}
			

		}
		//????????????????????????
		function retime(){
		    var r = $("#reportType").attr("value");
			if (r == 0)
			{
				var max = "2099-12-31";
		    	var v = $("#sendtime").attr("value");
				WdatePicker({dateFmt:'yyyy-MM-dd',minDate:v,maxDate:max,isShowClear:false});
			}else if (r == 1)
			{
				WdatePicker({dateFmt:'yyyy-MM',isShowClear:false});
			}
			else
			{
				WdatePicker({dateFmt:'yyyy',isShowClear:false});
			}
			

		}

		$(document).ready(function(){
            $('#reportType').isSearchSelectNew(
                {'width':'180','zindex':0,'isInput':false},
                function(){$("#reportType").change();
                }
            );
            $("#reportType").change();
            $("#timeSelect div:nth-child(3)").hide();

			getLoginInfo("#corpCode");
			$('#tdhm').isSearchSelect({'width':'180','zindex':0},function(data){
				//keyup click????????????
					if(data.value!=getJsLocaleMessage('common', 'common_whole')){
						$("#spgate").val(data.value);
					}else{
						$("#spgate").val('');
					}
			},function(data){
				//???????????????
				var val=$("#spgate").val();
				if(val){
					data.box.input.val(val);
				}
				
			});
			var findresult="<%=findResult%>";
		    if(findresult=="-1")
		    {
		       alert(getJsLocaleMessage('wygl', 'wygl_common_text1'));	
		       //window.history.go(-1);
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
			
 			if ($("#reportType").attr("value") == "1"){
					$("#recvtime").hide();
					$("#zhilabel").hide();
					$("#sjlabel").text(getJsLocaleMessage('wygl', 'wygl_common_text6'));
			}else if ($("#reportType").attr("value") == "2"){
					$("#recvtime").hide();
					$("#zhilabel").hide();
					$("#sjlabel").text(getJsLocaleMessage('wygl', 'wygl_common_text6'));
			}else{
					$("#recvtime").show();
					$("#zhilabel").show();
					$("#sjlabel").text(getJsLocaleMessage('wygl', 'wygl_common_text7'));
			}

			$("#reportType").change(function(){
				var t = $("#sTime").attr("value");
				var r = $("#eTime").val();//????????????
				if ($("#reportType").attr("value") == "1")
				{
					$("#sendtime").attr("value",t.toString().substring(0,7));
					$("#recvtime").attr("value",r.toString().substring(0,7));
					$("#recvtime").hide();
					$("#zhilabel").hide();
					$("#sjlabel").text(getJsLocaleMessage('wygl', 'wygl_common_text6'));
				}else if ($("#reportType").attr("value") == "2")
				{
					$("#sendtime").attr("value",t.toString().substring(0,4));
					$("#recvtime").attr("value",r.toString().substring(0,4));
					$("#recvtime").hide();
					$("#zhilabel").hide();
					$("#sjlabel").text(getJsLocaleMessage('wygl', 'wygl_common_text6'));
				}
				else
				{
					$("#sendtime").attr("value",t);
					$("#recvtime").attr("value",r);
					$("#recvtime").show();
					$("#zhilabel").show();
					$("#sjlabel").text(getJsLocaleMessage('wygl', 'wygl_common_text7'));
				}
				
			});
			
			$("#gateName").live("keyup blur",function(){
					var value=$(this).val();
					if(value!=filterString(value)){
						$(this).val(filterString(value));
					}
			});
			
			initPage(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>);
			$('#search').click(function(){
				submitForm();
			});
			
			
			//?????????????????????excel
			$("#exportCondition").click(
			 function()
			 {

				  if(confirm(getJsLocaleMessage('wygl', 'wygl_common_text2')))
				   {
						var langName ='<%=langName %>';	
				   		var gatename='<%=request.getParameter("gateName")==null?"":request.getParameter("gateName") %>';
				   		var spgate='<%=request.getParameter("spgate")==null?"":request.getParameter("spgate") %>';
				   		var sendtime = '<%=request.getParameter("begintime") != null ? request.getParameter("begintime") : beginTime%>';
				   		var recvtime = '<%=request.getParameter("endtime") != null ? request.getParameter("endtime") : endTime%>';
				   		var reportType ='<%=reportType!=null?reportType.toString():"" %>';
				   		var lgcorpcode='<%=request.getParameter("lgcorpcode")!=null?request.getParameter("lgcorpcode"):"" %>'
				   		<%
				   		if(reportList!=null && reportList.size()>0){
				   		%>
				   				$.ajax({
									type: "POST",
									url: "<%=path%>/wyq_statisticalReport.htm?method=r_smRptExportExcel",
									data: {
										langName:langName,
										gatename:gatename,
										spgate:spgate,
										reportType:reportType,
										sendtime:sendtime,
										endtime:recvtime,
										lgcorpcode:lgcorpcode
									},
						            beforeSend:function () {
						                page_loading();
						            },
						            complete:function () {
						           	  	page_complete();
						            },
						            success:function (result) {
						                    if (result == 'true') {
						                        download_href("<%=path%>/wyq_statisticalReport.htm?method=downloadFile");
						                    } else {
						                        alert(getJsLocaleMessage('wygl', 'wygl_common_text5'));
						                    }
			           				}
				 				});			
				   		//location.href="<%=path%>/wyq_statisticalReport.htm?method=r_smRptExportExcel&gatename="+gatename+"&spgate="+spgate+"&reportType="+reportType+"&sendtime="+sendtime+"&endtime="+recvtime+"&lgcorpcode="+lgcorpcode;
				   		<%	
				   		}else{
				   		%>
				   		alert(getJsLocaleMessage('wygl', 'wygl_common_text4'));
				   		<%
				   		}%>
				   }				 
			  });		
			
			
		});
	
		function dataReportfind(id){
			var begintime = '<%=request.getParameter("begintime") != null ? request.getParameter("begintime") : beginTime%>';
			var recvtime = '<%=request.getParameter("endtime") != null ? request.getParameter("endtime") : endTime%>';
			var reportType ='<%=reportType!=null?reportType.toString():"" %>';
			var lgcorpcode='<%=request.getParameter("lgcorpcode")!=null?request.getParameter("lgcorpcode"):"" %>';
			var lgspgate='<%=request.getParameter("spgate")!=null?request.getParameter("spgate"):"" %>';
			var lggatename='<%=request.getParameter("gateName")!=null?request.getParameter("gateName"):"" %>';
			var pageIndex='<%=request.getParameter("pageIndex")==null?"1":request.getParameter("pageIndex")%>';
	   	    var pageSize='<%=request.getParameter("pageSize")==null?"15":request.getParameter("pageSize")%>';
			location.href="<%=path%>/wyq_statisticalReport.htm?method=findInfoById&lgpageindex="+pageIndex+"&lgpagesize="+pageSize+"&lgspgate="+lgspgate+"&lggatename="+lggatename+"&spgate="+id+"&reportType="+reportType+"&begintime="+begintime+"&endtime="+recvtime+"&lgcorpcode="+lgcorpcode;
		}  
	
</script>
<script type="text/javascript" src="<%=iPath %>/js/wyq_report.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	</body>
</html>
