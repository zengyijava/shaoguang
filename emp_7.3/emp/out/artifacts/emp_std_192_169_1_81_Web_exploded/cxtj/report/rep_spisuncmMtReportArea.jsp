<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.entity.passage.XtGateQueue"%>
<%@ page import="com.montnets.emp.entity.system.LfPageField"%>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils"%>
<%@ page import="com.montnets.emp.report.bean.RptConfInfo"%>
<%@ page import="com.montnets.emp.report.bean.RptStaticValue" %>
<%@page import="com.montnets.emp.report.biz.ReportBiz"%>
<%@page import="com.montnets.emp.report.biz.RptConfBiz"%>
<%@page import="com.montnets.emp.report.vo.OperatorsAreaMtDataReportVo"%>
<%@page import="com.montnets.emp.util.PageInfo"%>
<%@page import="java.net.URLDecoder"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.List" %>
<%@page import="java.util.Locale"%>
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

MessageUtils messageUtils = new MessageUtils();
String langName = (String)session.getAttribute(StaticValue.LANG_KEY);


PageInfo pageInfo = null;
if(request.getAttribute("pageInfo")!=null){
	pageInfo = (PageInfo) request.getAttribute("pageInfo");
}else{
	pageInfo=new PageInfo();
}
@ SuppressWarnings("unchecked")
Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map

@ SuppressWarnings("unchecked")
Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
String menuCode = titleMap.get("spisuncmMtReport");

menuCode = menuCode==null?"0-0-0":menuCode;
OperatorsAreaMtDataReportVo operatorsAreaMtDataReportVo = (OperatorsAreaMtDataReportVo)request.getAttribute("operatorsAreaMtDataReportVo");
@ SuppressWarnings("unchecked")
List<OperatorsAreaMtDataReportVo> reportList=(List<OperatorsAreaMtDataReportVo>)request.getAttribute("reportList");
@ SuppressWarnings("unchecked")

List<LfPageField> pagefileds=(List<LfPageField>)session.getAttribute("pagefileds");
@ SuppressWarnings("unchecked")
List<XtGateQueue> xtList = (List<XtGateQueue>)session.getAttribute("mrXtList");

long[] sumCount=(long[])request.getAttribute("sumCount");

String countTime = request.getAttribute("countTime")==null?"":request.getAttribute("countTime").toString();
String startTime = request.getAttribute("countTime")==null?"":request.getAttribute("countTime").toString();
String endTime = request.getAttribute("endTime")==null?"":request.getAttribute("endTime").toString();
String msType = request.getAttribute("msType")==null?"":request.getAttribute("msType").toString();
int reportType = Integer.parseInt(request.getAttribute("reportType")==null?"":request.getAttribute("reportType").toString());
String staffId = request.getAttribute("staffId")==null?"":request.getAttribute("staffId").toString();
String spisuncm = request.getAttribute("spisuncm")==null?"":request.getAttribute("spisuncm").toString();

String areacode = "";
String areaname = "";
if(operatorsAreaMtDataReportVo.getAreacode()!=null && !"".equals(operatorsAreaMtDataReportVo.getAreacode()))
{
	areacode = operatorsAreaMtDataReportVo.getAreacode();
}else{
	areacode = "";
}
if(operatorsAreaMtDataReportVo.getAreaname() !=null && !"".equals(operatorsAreaMtDataReportVo.getAreaname()))
{
	areaname = operatorsAreaMtDataReportVo.getAreaname();
}else{
	areaname = "";
}

//时间
String sj = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_sj", request);
//国家/地区代码
String gjdqdm = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_gjdqdm", request);
//国家名称
String gjmc = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_gjmc", request);
if(gjmc!=null&&gjmc.length()>1){
    	gjmc = gjmc.substring(0,gjmc.length()-1);
    }
//运营商账号ID
String yyszhid = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_yyszhid", request);
//通道号码
String tdhm = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_tdhm", request);
//通道名称
String tdmc = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_tdmc", request);
//发送类型
String fslx = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_fslx", request);


//excel自动识别用
	Map<String,String> excelConditionMap = new java.util.LinkedHashMap<String,String>();
	//excelConditionMap.put("年份","y");
	//excelConditionMap.put("时间","imonth");
	//excelConditionMap.put("国家/地区代码","areaId");
	//excelConditionMap.put("国家名称","areaName");
	//excelConditionMap.put("运营商账号ID","spID");
	//excelConditionMap.put("通道号码","tongdId");
	//excelConditionMap.put("通道名称","tongdName");
	//excelConditionMap.put("发送类型","sendType");
	
	excelConditionMap.put(sj,"imonth");
	excelConditionMap.put(gjdqdm,"areaId");
	excelConditionMap.put(gjmc,"areaName");
	excelConditionMap.put(yyszhid,"spID");
	excelConditionMap.put(tdhm,"tongdId");
	excelConditionMap.put(tdmc,"tongdName");
	excelConditionMap.put(fslx,"sendType");
	
	
	
	
	session.setAttribute("EXCEL_MAP",excelConditionMap);
	java.util.Iterator<Map.Entry<String, String>> iter = null;
    Map.Entry<String, String> e = null;
  String findResult= (String)request.getAttribute("findresult");
  String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");

%>
<html>
	<head>
		<%@include file="/common/common.jsp" %>
		<title><%=titleMap.get(menuCode) %></title>
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<%if(StaticValue.ZH_HK.equals(langName)){%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>">
		<link rel="stylesheet" type="text/css" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>">
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link href="<%=commonPath%>/cxtj/common/css/inbox.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
	</head>

	<body class="rep_spisuncmMtReportArea">
		<div id="container" class="container">
			<%-- 当前位置 --%>
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode) %>
						
			<%-- 内容开始 --%>
			
			<%if(btnMap.get(menuCode+"-0")!=null) { %>
			<form name="pageForm" action="rep_spisuncmMtReport.htm?method=infoDetilByArea" method="post" id="pageForm">
				<div id="rContent" class="rContent">
						<div class="buttons">
							<div id="toggleDiv" >
								<%-- <img id="searchIcon" src="<%=inheritPath %>/images/toggle_collapse.png" title="展开查询条件"/> --%>
							</div>
							<span id="backgo" class="right mr5" onclick="javascript:showback()">&nbsp;<emp:message key="cxtj_sjcx_report_fh" defVal="返回" fileName="cxtj"></emp:message></span>
							<a id="exportCondition"><emp:message key="cxtj_sjcx_report_dc" defVal="导出" fileName="cxtj"></emp:message></a>
	                 	    <input type="hidden" id="menucode" value="24" name="menucode" />
	                 	    <input type="hidden" id="pageTotalRec" name="pageTotalRec" value="<%=pageInfo.getTotalRec() %>"/>
						</div>
						<div id="condition">
							<table>
								<tbody>
									<tr> 
									<td><emp:message key="cxtj_sjcx_report_gjdm" defVal="国家代码：" fileName="cxtj"></emp:message></td>
									<td><input type="text"  id="areacode" name="areacode" onblur="javascript:numberControl($(this))" onkeyup="javascript:numberControl($(this))" value="<%=areacode==null?"":areacode %>"  maxlength="11" /></td>
									<td><emp:message key="cxtj_sjcx_report_gjmc" defVal="国家名称：" fileName="cxtj"></emp:message></td>
									<td><input type="text"  id="areaname" name="areaname" value="<%=areaname==null?"":URLDecoder.decode(areaname) %>" maxlength="11" /></td>
										<td>&nbsp;
											<input type="hidden" name="countTime" value="<%=countTime %>"/>
											<input type="hidden" name="startTime" value="<%=startTime %>"/>
											<input type="hidden" name="endTime" value="<%=endTime %>"/>
											<input type="hidden" name="msType" value="<%=msType %>"/>
											<input type="hidden" name="reportType" value="<%=reportType %>"/>
											<input type="hidden" name="staffId" value="<%=staffId %>"/>
											<input type="hidden" name="spisuncm" value="<%=spisuncm %>"/>
										</td> 
										<td>&nbsp;</td> 
										<td class="tdSer">
											<center><a id="search"></a></center>
										</td>	
									</tr>
									<tr class="hidden"></tr>
								</tbody>
							</table>
						</div>
						<table id="content">
							<thead>
							    <tr>
						         <%
							 iter = excelConditionMap.entrySet().iterator();
							 while (iter.hasNext())
							{
	 							e = iter.next();
							  %>
							  <th><%=e.getKey() %></th>
						    <%
						    
						    }
						    
						     %> 
						     <%-- 读取可显示表头 --%>
						      <%
								List<RptConfInfo> rptConList = RptConfBiz.getRptConfMap().get(RptStaticValue.SPISUNCM_RPT_CONF_MENU_ID);
								for(RptConfInfo rptConf : rptConList)
								{
								%>
								<th><%=rptConf.getName() %></th>
								<%} %>
						     
						        </tr>
							</thead>
							<tbody>
								<%
								if (reportList != null && reportList.size() > 0)
								{
									SimpleDateFormat sdf = new SimpleDateFormat(MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_ymd", request));
									SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
									DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.CHINA);
									String timestr = "";
									if(reportType == 2){
										String startDate = sdf.format(df.parse(startTime));
										String endDate = sdf.format(df.parse(endTime));
										timestr = startDate+MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_z", request)+endDate;
									}
									for(OperatorsAreaMtDataReportVo report : reportList)
									{
									 // if(!report.getSpID().equals(""))
							%>
								<tr>
									<td>
										<%
											if(reportType == 2){
												out.print(timestr);
											}else if(reportType == 1){
												out.print(report.getY()+MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_y", request));
											}else{
												out.print(report.getY()+MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_y", request)+report.getImonth()+MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_m", request));
											}
										%>
									</td>
									<td><%=report.getAreacode()!=null?report.getAreacode():"" %></td><%-- 国家代码 --%>
									<td><%=report.getAreaname()!=null?report.getAreaname():"" %></td><%-- 国家名称 --%>
									<td>
										<%="".equals(report.getSpID().trim())?MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_wz", request):"******"%><%-- report.getSpID().toUpperCase() --%>
									</td>
									<td>
										<%=report.getSpgate()!=null?report.getSpgate():"" %>
									</td><%-- 通道号码 --%>
									<% 
									String tempStr="";
									String gateName=report.getGatename()==null?"":report.getGatename();
									if(!"".equals(gateName)){
										if(gateName.length()>8){
											tempStr=gateName.substring(0,8)+"...";
										}else{
											tempStr=gateName;
										}
									}
									
									%>
									<td><a onclick=modify(this)>
								<label style="display:none"><xmp><%=gateName %>
								  </xmp></label>
								  <xmp><%=tempStr%></xmp></a></td><%-- 通道名称 --%>
									<%
										String sendtypename="";
										if(report.getSpType()!=null){
											sendtypename=report.getSpType()==1?MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_empyy", request):MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_empjr", request);
											if(report.getSendType()!=null){
												if(report.getSendType()==1){
													sendtypename=sendtypename+"("+MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_empfs", request)+")";
												}else if(report.getSendType()==2){
													sendtypename=sendtypename+"("+MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_httpjr", request)+")";
												}else if(report.getSendType()==3){
													sendtypename=sendtypename+"("+MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_dbjr", request)+")";
												}else if(report.getSendType()==4){
													sendtypename=sendtypename+"("+MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_zljr", request)+")";
												}
											}
										}
									%>
									<td><%=sendtypename %></td><%-- 发送类型 --%>
									<%--//从公用方法中获取 计算结果map  --%>
									<%
										Map<String, String> map = ReportBiz.getRptNums(
															report.getIcount(), report.getRsucc(),
															report.getRfail1(), report.getRfail2(),
															report.getRnret(),
															RptStaticValue.SPISUNCM_RPT_CONF_MENU_ID);
	
										for(RptConfInfo rptConf : rptConList)
											{
												if(RptStaticValue.RPT_ICOUNT_COLUMN_ID.equals(rptConf.getColId()))
												{//提交总数
											%>
											<td><%=map.get(RptStaticValue.RPT_ICOUNT_COLUMN_ID)!=null?map.get(RptStaticValue.RPT_ICOUNT_COLUMN_ID):0 %></td>
											<%
												}
												else if(RptStaticValue.RPT_FSSUCC_COLUMN_ID.equals(rptConf.getColId()))
												{//发送成功数
											%>
											<td><%=map.get(RptStaticValue.RPT_FSSUCC_COLUMN_ID)!=null?map.get(RptStaticValue.RPT_FSSUCC_COLUMN_ID):0 %></td>
											<%
												}
												else if(RptStaticValue.RPT_RFAIL1_COLUMN_ID.equals(rptConf.getColId()))
												{//发送失败数
											%>
											<td><%=map.get(RptStaticValue.RPT_RFAIL1_COLUMN_ID)!=null?map.get(RptStaticValue.RPT_RFAIL1_COLUMN_ID):0 %></td>
											<%
												}
												else if(RptStaticValue.RPT_RFAIL2_COLUMN_ID.equals(rptConf.getColId()))
												{//接收失败数
											%>
											<td><%=map.get(RptStaticValue.RPT_RFAIL2_COLUMN_ID)!=null?map.get(RptStaticValue.RPT_RFAIL2_COLUMN_ID):0 %></td>
											<%
												}
												else if(RptStaticValue.RPT_JSSUCC_COLUMN_ID.equals(rptConf.getColId()))
												{//接收成功数
											%>
											<td><%=map.get(RptStaticValue.RPT_JSSUCC_COLUMN_ID)!=null?map.get(RptStaticValue.RPT_JSSUCC_COLUMN_ID):0 %></td>
											<%
												}
												else if(RptStaticValue.RPT_RNRET_COLUMN_ID.equals(rptConf.getColId()))
												{//未返数
											%>
											<td><%=map.get(RptStaticValue.RPT_RNRET_COLUMN_ID)!=null?map.get(RptStaticValue.RPT_RNRET_COLUMN_ID):0 %></td>
											<%
												}
												else if(RptStaticValue.RPT_FSSUCC_ICOUNT_COLUMN_ID.equals(rptConf.getColId()))
												{//发送成功率
											%>
											<td><%=map.get(RptStaticValue.RPT_FSSUCC_ICOUNT_COLUMN_ID)!=null?map.get(RptStaticValue.RPT_FSSUCC_ICOUNT_COLUMN_ID):0 %></td>
											<%
												}
												else if(RptStaticValue.RPT_RFAIL1_ICOUNT_COLUMN_ID.equals(rptConf.getColId()))
												{//发送失败率
											%>
											<td><%=map.get(RptStaticValue.RPT_RFAIL1_ICOUNT_COLUMN_ID)!=null?map.get(RptStaticValue.RPT_RFAIL1_ICOUNT_COLUMN_ID):0 %></td>
											<%
												}
												else if(RptStaticValue.RPT_JSSUCC_FSSUCC_COLUMN_ID.equals(rptConf.getColId()))
												{//接收成功率
											%>
											<td><%=map.get(RptStaticValue.RPT_JSSUCC_FSSUCC_COLUMN_ID)!=null?map.get(RptStaticValue.RPT_JSSUCC_FSSUCC_COLUMN_ID):0 %></td>
											<%
												}
												else if(RptStaticValue.RPT_RFAIL2_FSSUCC_COLUMN_ID.equals(rptConf.getColId()))
												{//接收失败率
											%>
											<td><%=map.get(RptStaticValue.RPT_RFAIL2_FSSUCC_COLUMN_ID)!=null?map.get(RptStaticValue.RPT_RFAIL2_FSSUCC_COLUMN_ID):0 %></td>
											<%
												}
												else if(RptStaticValue.RPT_RNRET_FSSUCC_COLUMN_ID.equals(rptConf.getColId()))
												{//未返率
											%>
											<td><%=map.get(RptStaticValue.RPT_RNRET_FSSUCC_COLUMN_ID)!=null?map.get(RptStaticValue.RPT_RNRET_FSSUCC_COLUMN_ID):0 %></td>
											<%}
										} %>
				
								</tr>
								<% } %>
								  <tr>
									<td colspan="7"><b><emp:message key="cxtj_sjcx_report_hj" defVal="合计：" fileName="cxtj"></emp:message></b></td>
									<%
		 							//从公用方法中获取 计算结果map
		 							long icount = sumCount[0];
		 							long rfail1 = sumCount[1];
		 							long rfail2 = sumCount[2];
		 							long rnret = sumCount[3];
		 							long rsucc = sumCount[4];
		 							Map<String,String> map=ReportBiz.getRptNums(Long.valueOf(icount),Long.valueOf(rsucc),Long.valueOf(rfail1),Long.valueOf(rfail2),Long.valueOf(rnret),RptStaticValue.SPISUNCM_RPT_CONF_MENU_ID);
									for(RptConfInfo rptConf : rptConList)
									{
										if(RptStaticValue.RPT_ICOUNT_COLUMN_ID.equals(rptConf.getColId()))
										{//提交总数
									%>
									<td><%=map.get(RptStaticValue.RPT_ICOUNT_COLUMN_ID)!=null?map.get(RptStaticValue.RPT_ICOUNT_COLUMN_ID):0 %></td>
									<%
										}
										else if(RptStaticValue.RPT_FSSUCC_COLUMN_ID.equals(rptConf.getColId()))
										{//发送成功数
									%>
									<td><%=map.get(RptStaticValue.RPT_FSSUCC_COLUMN_ID)!=null?map.get(RptStaticValue.RPT_FSSUCC_COLUMN_ID):0 %></td>
									<%
										}
										else if(RptStaticValue.RPT_RFAIL1_COLUMN_ID.equals(rptConf.getColId()))
										{//发送失败数
									%>
									<td><%=map.get(RptStaticValue.RPT_RFAIL1_COLUMN_ID)!=null?map.get(RptStaticValue.RPT_RFAIL1_COLUMN_ID):0 %></td>
									<%
										}
										else if(RptStaticValue.RPT_RFAIL2_COLUMN_ID.equals(rptConf.getColId()))
										{//接收失败数
									%>
									<td><%=map.get(RptStaticValue.RPT_RFAIL2_COLUMN_ID)!=null?map.get(RptStaticValue.RPT_RFAIL2_COLUMN_ID):0 %></td>
									<%
										}
										else if(RptStaticValue.RPT_JSSUCC_COLUMN_ID.equals(rptConf.getColId()))
										{//接收成功数
									%>
									<td><%=map.get(RptStaticValue.RPT_JSSUCC_COLUMN_ID)!=null?map.get(RptStaticValue.RPT_JSSUCC_COLUMN_ID):0 %></td>
									<%
										}
										else if(RptStaticValue.RPT_RNRET_COLUMN_ID.equals(rptConf.getColId()))
										{//未返数
									%>
									<td><%=map.get(RptStaticValue.RPT_RNRET_COLUMN_ID)!=null?map.get(RptStaticValue.RPT_RNRET_COLUMN_ID):0 %></td>
									<%
										}
										else if(RptStaticValue.RPT_FSSUCC_ICOUNT_COLUMN_ID.equals(rptConf.getColId()))
										{//发送成功率
									%>
									<td><%=map.get(RptStaticValue.RPT_FSSUCC_ICOUNT_COLUMN_ID)!=null?map.get(RptStaticValue.RPT_FSSUCC_ICOUNT_COLUMN_ID):0 %></td>
									<%
										}
										else if(RptStaticValue.RPT_RFAIL1_ICOUNT_COLUMN_ID.equals(rptConf.getColId()))
										{//发送失败率
									%>
									<td><%=map.get(RptStaticValue.RPT_RFAIL1_ICOUNT_COLUMN_ID)!=null?map.get(RptStaticValue.RPT_RFAIL1_ICOUNT_COLUMN_ID):0 %></td>
									<%
										}
										else if(RptStaticValue.RPT_JSSUCC_FSSUCC_COLUMN_ID.equals(rptConf.getColId()))
										{//接收成功率
									%>
									<td><%=map.get(RptStaticValue.RPT_JSSUCC_FSSUCC_COLUMN_ID)!=null?map.get(RptStaticValue.RPT_JSSUCC_FSSUCC_COLUMN_ID):0 %></td>
									<%
										}
										else if(RptStaticValue.RPT_RFAIL2_FSSUCC_COLUMN_ID.equals(rptConf.getColId()))
										{//接收失败率
									%>
									<td><%=map.get(RptStaticValue.RPT_RFAIL2_FSSUCC_COLUMN_ID)!=null?map.get(RptStaticValue.RPT_RFAIL2_FSSUCC_COLUMN_ID):0 %></td>
									<%
										}
										else if(RptStaticValue.RPT_RNRET_FSSUCC_COLUMN_ID.equals(rptConf.getColId()))
										{//未返率
									%>
									<td><%=map.get(RptStaticValue.RPT_RNRET_FSSUCC_COLUMN_ID)!=null?map.get(RptStaticValue.RPT_RNRET_FSSUCC_COLUMN_ID):0 %></td>
									<%}
									} %>
								</tr>
								<%}else{
							%> 
							<tr>
									<td colspan="<%=7+rptConList.size() %>">
										<emp:message key="cxtj_sjcx_report_wjl" defVal="无记录" fileName="cxtj"></emp:message>
									</td>
								</tr>
							<%} %>
							
							</tbody>
							<tfoot>
								<tr>
									<td colspan="<%=7+rptConList.size() %>">
										<div id="pageInfo"></div>
									</td>
								</tr>
							</tfoot>
						</table>
			</div>
			<%} %>
			<div id="modify" title="<%=tdmc%>">
					<table>
						<thead>
						<tr class="modify_tr1">
							<td>
								<label id="msgss"></label>
							</td>
						</tr>
						<tr class="modify_tr2">
							<td></td>
						</tr>
						</thead>
					</table>
				</div>
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
			<%-- foot结束 --%>
		</div>
    <div class="clear"></div>
    	<script type="text/javascript" src="<%=commonPath %>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=iPath %>/js/rep_spisuncmMtReportArea.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/cxtj_<%=langName%>.js"></script>
	
		<script type="text/javascript">
		
		$(document).ready(function(){
		getLoginInfo("#corpCode");
			var findresult="<%=findResult%>";
		    if(findresult=="-1")
		    {
		       //alert("加载页面失败,请检查网络是否正常!");	
		       alert(getJsLocaleMessage("cxtj","cxtj_sjcx_report_text_2"));	
		       return;			       
		    }
		    
			//$("#toggleDiv").toggle(function() {
			//	$("#condition").hide();
			//	$('#searchIcon').attr('src', '<%=inheritPath %>/images/toggle_expand.png');
			//	$('#searchIcon').attr('title', '展开查询条件');
			//}, function() {
			//	$("#condition").show();
			//	$('#searchIcon').attr('src', '<%=inheritPath %>/images/toggle_collapse.png');
			//	$('#searchIcon').attr('title', '收缩查询条件');
			//});
			
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
			
			
		//导出全部数据到excel
		$("#exportCondition").click(
			 function(){
				  //if(confirm("确定要导出数据到excel?"))
				  if(confirm(getJsLocaleMessage("cxtj","cxtj_sjcx_report_text_1")))
				   {
				   		<%if (reportList != null && reportList.size() > 0) {%>			
					   		var countTime='<%=startTime%>';
					   		var staffId = '<%=staffId%>';
					   		var reportType = '<%=reportType%>';
					   		var spisuncm = '<%=spisuncm!=null?spisuncm:""%>';
					   		if(reportType == 2)
					   		{
					   			var endTime = '<%=endTime != null?endTime:""%>';
					   		}else{
					   			var endTime = "";
					   		}
					   		var mstype=<%=msType%>;  
					   		var areacode ='<%=areacode !=null?areacode:"" %>';
					   		var areaname ='<%=areaname %>';
					   		<%if (areaname != null){%>
					   			areaname = areaname;
							<%}%>
					   		var totalIcount=<%=sumCount[0]%>;
					   		var totalRfail1=<%=sumCount[1]%>;
					   		var totalRfail2=<%=sumCount[2]%>;
					   		var totalRnret=<%=sumCount[3]%>;
					   		var totalRsucc=<%=sumCount[4]%>;
				   			$.ajax({
									type: "POST",
									url: "<%=path%>/rep_spisuncmMtReport.htm?method=r_stRptExportExcelByArea",
									data: {
										countTime:countTime,
										endTime:endTime,
										spisuncm:spisuncm,
										staffId:staffId,
										areacode:areacode,
										areaname:areaname,
										reportType:reportType,
										totalIcount:totalIcount,
										totalRfail1:totalRfail1,
										totalRfail2:totalRfail2,
										totalRnret:totalRnret,
										totalRsucc:totalRsucc,
										mstype:mstype
									},
						            beforeSend:function () {
						                page_loading();
						            },
						            complete:function () {
						           	  	page_complete()
						            },
						            success:function (result) {
						                    if (result == 'true') {
						                        download_href("<%=path%>/rep_spisuncmMtReport.htm?method=spisuncmareadownloadFile");
						                    } else {
						                       // alert('导出失败！');
						                        alert(getJsLocaleMessage("cxtj","cxtj_sjcx_report_text_3"));
						                    }
	           					}
				 			});			   		
				   			//location.href="<%=path%>/rep_spisuncmMtReport.htm?method=r_stRptExportExcelByArea&countTime="+countTime+"&endTime="+endTime+"&spisuncm="+spisuncm+"&staffId="+staffId+"&areacode="+areacode+"&areaname="+areaname+"&reportType="+reportType+"&totalIcount="+totalIcount+"&totalRfail1="+totalRfail1+"&totalRfail2="+totalRfail2+"&totalRnret="+totalRnret+"&totalRsucc="+totalRsucc+"&mstype="+mstype+"";
				   		<%} else {%>
				   		
				   		//alert("无数据可导出！");
				   		alert(getJsLocaleMessage("cxtj","cxtj_sjcx_report_text_4"));
				   		
				   		<%}%>
				   		}
			  });
			
			initPage(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>);
			$('#search').click(function(){submitForm();});

		});
		</script>
	</body>
</html>
