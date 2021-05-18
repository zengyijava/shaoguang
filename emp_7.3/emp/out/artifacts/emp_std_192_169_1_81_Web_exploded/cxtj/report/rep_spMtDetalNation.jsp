<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.entity.passage.XtGateQueue"%>
<%@ page import="com.montnets.emp.entity.system.LfPageField"%>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils"%>
<%@ page import="com.montnets.emp.report.bean.RptConfInfo"%>
<%@ page import="com.montnets.emp.report.bean.RptStaticValue"%>
<%@ page import="com.montnets.emp.report.biz.ReportBiz" %>
<%@page import="com.montnets.emp.report.biz.RptConfBiz"%>
<%@page import="com.montnets.emp.report.vo.SpMtDataNationVo"%>
<%@page import="com.montnets.emp.util.PageInfo"%>
<%@page import="java.net.URLDecoder"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.Calendar"%>
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

MessageUtils messageUtils = new MessageUtils();
String langName = (String)session.getAttribute(StaticValue.LANG_KEY);

PageInfo pageInfo = new PageInfo();
pageInfo = (PageInfo) request.getAttribute("pageInfo");
@ SuppressWarnings("unchecked")
 Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map

@ SuppressWarnings("unchecked")
Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
String menuCode = titleMap.get("spMtReport");
menuCode = menuCode==null?"0-0-0":menuCode;
@ SuppressWarnings("unchecked")
List<SpMtDataNationVo> reportList=(List<SpMtDataNationVo>)session.getAttribute("reportList");
@ SuppressWarnings("unchecked")
List<String> userList = (List<String>)request.getAttribute("spUserList");
List<String> mmsUserList = (List<String>)request.getAttribute("mmsUserList");
List<LfPageField> pagefileds=(List<LfPageField>)session.getAttribute("pagefiledsps");
String msType =(String)request.getAttribute("msType");
@ SuppressWarnings("unchecked")
List<XtGateQueue> xtList = (List<XtGateQueue>)session.getAttribute("mrXtList");
long[] sumCount=(long[])session.getAttribute("sumCount");
String counTime = "";
if(request.getAttribute("countTime")!=null){
	counTime=request.getAttribute("countTime").toString();
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
//SP账号
String spzh = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_spzh", request);
//通道号码
String tdhm = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_tdhm", request);
//通道名称
String tdmc = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_tdmc", request);
//发送类型
String fslx = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_fslx", request);

//excel自动识别用
	Map<String,String> excelConditionMap = new java.util.LinkedHashMap<String,String>();
	//excelConditionMap.put("时间","imonth");
	//excelConditionMap.put("国家/地区代码","statuscode");
	//excelConditionMap.put("国家名称","statuname");
	//excelConditionMap.put("SP账号","spusertype");
	//excelConditionMap.put("通道号码","sendtype");
	//excelConditionMap.put("通道名称","sendtype");
	//excelConditionMap.put("发送类型","sendtype");
	
	excelConditionMap.put(sj,"imonth");
	excelConditionMap.put(gjdqdm,"statuscode");
	excelConditionMap.put(gjmc,"statuname");
	excelConditionMap.put(spzh,"spusertype");
	excelConditionMap.put(tdhm,"sendtype");
	excelConditionMap.put(tdmc,"sendtype");
	excelConditionMap.put(fslx,"sendtype");
	
	
	session.setAttribute("EXCEL_MAP",excelConditionMap);
	java.util.Iterator<Map.Entry<String, String>> iter = null;
    Map.Entry<String, String> e = null;
    
    String findResult= (String)request.getAttribute("findresult");
    String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
    
    String corpcode = (String)request.getAttribute("corpcode");
    Boolean isFirstEnter=(Boolean)request.getAttribute("isFirstEnter");
    //String strshow="无记录";
    String strshow=MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_wjl", request);
   if(isFirstEnter!=null&&isFirstEnter){
   		reportList=null;
   		//strshow="请点击查询获取数据 ";
   		strshow=MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_qdjcxhqsj", request);
   }
 String reportType="2";//默认为日报表   
   if(request.getAttribute("reportType")!=null){
	   reportType=request.getAttribute("reportType").toString();
   }
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	Calendar c = Calendar.getInstance();
	c.add(Calendar.DAY_OF_MONTH, -1);
	String beginTime = df.format(c.getTime()).substring(0, 8) + "01";
	String endTime = df.format(c.getTime()).substring(0, 11); //change by dj
 
	String excelURL="";
	if(request.getAttribute("excelURL")!=null){
		excelURL=request.getAttribute("excelURL").toString();
	  }
%>
<html>
	<head>
		<%@include file="/common/common.jsp" %>
		<title><%=titleMap.get(menuCode) %></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<%if(StaticValue.ZH_HK.equals(langName)){%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
		<link href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
		<link href="<%=commonPath%>/cxtj/common/css/inbox.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
	</head>
	<body class="rep_spMtDetalNation">
		<div id="container" class="container">
			<%-- 当前位置 --%>
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode) %>
			
			<%-- 内容开始 --%>
			<%if(btnMap.get(menuCode+"-0")!=null) { %>
			<form name="pageForm" action="rep_spMtReport.htm?method=nationSendInfo" method="post" id="pageForm">
				<div id="rContent" class="rContent">
						<div class="buttons">
						<div id="toggleDiv" >
							</div>
						<input type="hidden" id="sptype" name="sptype" value="<%=request.getParameter("sptype")==null?"":request.getParameter("sptype")%>" />
					    <input type="hidden" id="userId" name="userId" value="<%=request.getParameter("userId")==null?"":request.getParameter("userId")%>"/>
						<input type="hidden" id="staffname" name="staffname" value="<%=request.getParameter("staffname")==null?"":request.getParameter("staffname")%>" />
						<input type="hidden" id="sendtype" name="sendtype" value="<%=request.getParameter("sendtype")==null?"":request.getParameter("sendtype")%>" />
						<input  type="hidden" name="msType" id="msType" value="<%=request.getParameter("msType")==null?"":request.getParameter("msType")%>">
						<input type="hidden" name="begintime" id="begintime" value="<%=request.getParameter("begintime")==null?"":request.getParameter("begintime")%>"/>
						<input type="hidden" name="endtime" id="endtime" value="<%=request.getParameter("endtime")==null?"":request.getParameter("endtime")%>"/>
						<input type="hidden" name="spisuncm" id="spisuncm" value="<%=request.getParameter("spisuncm")==null?"":request.getParameter("spisuncm")%>"/>
						<input type="hidden" name="reportType" id="reportType" value="<%=request.getParameter("reportType")==null?"":request.getParameter("reportType")%>"/>
						<input type="hidden" name="countTime" id="countTime" value="<%=counTime%>"/>
							<span id="backgo" class="right mr5" onclick="javascript:showback()">&nbsp;<emp:message key="cxtj_sjcx_report_fh" defVal="返回" fileName="cxtj"></emp:message></span>
							 <a id="exportCondition" ><emp:message key="cxtj_sjcx_report_dc" defVal="导出" fileName="cxtj"></emp:message></a>
	                 	    <input type="hidden" id="menucode" value="24" name="menucode" />
	                 	    <input type="hidden" id="pageTotalRec" name="pageTotalRec" value="<%=pageInfo.getTotalRec() %>"/>
						</div>
						<div id="condition">
							<table>
								<tbody>
									<tr>
									<td><emp:message key="cxtj_sjcx_report_gjdm" defVal="国家代码：" fileName="cxtj"></emp:message></td>
									<td><input type="text"  id="nationCode" name="nationCode" onblur="javascript:numberControl($(this))" onkeyup="javascript:numberControl($(this))" value='<%=request.getParameter("nationCode")==null?"":request.getParameter("nationCode") %>'maxlength="11" /></td>
									<td><emp:message key="cxtj_sjcx_report_gjmc" defVal="国家名称：" fileName="cxtj"></emp:message></td>
									<td><input type="text"  id="nationName" name="nationName" value='<%=request.getParameter("nationName")==null?"":URLDecoder.decode(request.getParameter("nationName"), "UTF-8") %>'maxlength="11" /></td>
									<td class="tdSer"><center><a id="search"></a></center></td>
									</tr>
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
						     <%
								List<RptConfInfo> rptConList = RptConfBiz.getRptConfMap().get(RptStaticValue.SPUSER_RPT_CONF_MENU_ID);
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
								if (reportList != null && reportList.size() > 0)
								{
									for(SpMtDataNationVo report : reportList)
									{
							%>
								<tr>								    
									<td>
									<% 
									String showTime = "";
									String begintime="";
									String endtime="";
									if("2".equals(reportType)){
									String btime = request.getParameter("begintime") != null ? request.getParameter("begintime") : beginTime;
									 begintime=btime;
									String etime = request.getParameter("endtime") != null ? request.getParameter("endtime") : endTime;
									endtime=etime;
									if(!"".equals(btime) && null != btime && 0 != btime.length())
									{
										String btemp[] = btime.split("-");
										//btime = btemp[0] + "年" + btemp[1] + "月" + btemp[2] + "日";
										btime = btemp[0] + MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_y", request) + btemp[1] + MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_m", request) + btemp[2] + MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_d", request);
									}

									if(!"".equals(etime) && null != etime && 0 != etime.length())
									{
										String etemp[] = etime.split("-");
										//etime = etemp[0] + "年" + etemp[1] + "月" + etemp[2] + "日";
										etime = etemp[0] + MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_y", request) + etemp[1] + MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_m", request) + etemp[2] + MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_d", request);
									}
									showTime = btime + MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_z", request) + etime;
									}else if("0".equals(reportType)){
										showTime=report.getY()+MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_y", request)+report.getImonth()+MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_m", request)	;
									}else {
										showTime=report.getY()+MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_y", request);
									}
									%>
											<%=showTime%>
									
									</td>	
																	
									<td>
										<xmp><%=report.getNationcode()==null?MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_wz", request):report.getNationcode()%></xmp>
									</td>
									<td><xmp><%=report.getNationname()!=null?MessageUtils.extractMessage("cxtj","cxtj_country_"+report.getNationcode(),request) : MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_wz", request) %></xmp></td>
									<td>
										<xmp><%=report.getUserid()==null?MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_wz", request):report.getUserid()%></xmp>
									</td>
									<td>
										<xmp><%=report.getSpgatecode()==null?MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_wz", request):report.getSpgatecode()%></xmp>
									</td>
									<td>
										<xmp><%=report.getSpgatename()==null?MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_wz", request):report.getSpgatename()%></xmp>
									</td>
									<%
									String sendtypename="";
									if(report.getSptype()!=null){
										sendtypename=report.getSptype()==1?MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_empyy", request):MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_empjr", request);
										if(report.getSendtype()!=null){
											if(report.getSendtype()==1){
												sendtypename=sendtypename+"("+MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_empfs", request)+")";
											}else if(report.getSendtype()==2){
												sendtypename=sendtypename+"("+MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_httpjr", request)+")";
											}else if(report.getSendtype()==3){
												sendtypename=sendtypename+"("+MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_dbjr", request)+")";
											}else if(report.getSendtype()==4){
												sendtypename=sendtypename+"("+MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_zljr", request)+")";
											}
										}
									}
									
									%>
									<td><xmp><%=sendtypename %></xmp></td>
									<%
	 							//从公用方法中获取 计算结果map
	 							Map<String,String> map=ReportBiz.getRptNums(report.getIcount(),report.getRsucc(),report.getRfail1(),
	 									report.getRfail2(),report.getRnret(),RptStaticValue.SPUSER_RPT_CONF_MENU_ID);
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
								<%
								}
								%>
								<tr>
									<td colspan="7"><b><emp:message key="cxtj_sjcx_report_hj" defVal="合计：" fileName="cxtj"></emp:message></b></td>
								    <%
	 							//从公用方法中获取 计算结果map
	 							Map<String,String> mapsum=ReportBiz.getRptNums(sumCount[0],sumCount[1],sumCount[2],
	 									sumCount[3],sumCount[4],RptStaticValue.SPUSER_RPT_CONF_MENU_ID);
								for(RptConfInfo rptConf : rptConList)
								{
									if(RptStaticValue.RPT_ICOUNT_COLUMN_ID.equals(rptConf.getColId()))
									{//提交总数
								%>
								<td><%=mapsum.get(RptStaticValue.RPT_ICOUNT_COLUMN_ID)!=null?mapsum.get(RptStaticValue.RPT_ICOUNT_COLUMN_ID):0 %></td>
								<%
									}
									else if(RptStaticValue.RPT_FSSUCC_COLUMN_ID.equals(rptConf.getColId()))
									{//发送成功数
								%>
								<td><%=mapsum.get(RptStaticValue.RPT_FSSUCC_COLUMN_ID)!=null?mapsum.get(RptStaticValue.RPT_FSSUCC_COLUMN_ID):0 %></td>
								<%
									}
									else if(RptStaticValue.RPT_RFAIL1_COLUMN_ID.equals(rptConf.getColId()))
									{//发送失败数
								%>
								<td><%=mapsum.get(RptStaticValue.RPT_RFAIL1_COLUMN_ID)!=null?mapsum.get(RptStaticValue.RPT_RFAIL1_COLUMN_ID):0 %></td>
								<%
									}
									else if(RptStaticValue.RPT_RFAIL2_COLUMN_ID.equals(rptConf.getColId()))
									{//接收失败数
								%>
								<td><%=mapsum.get(RptStaticValue.RPT_RFAIL2_COLUMN_ID)!=null?mapsum.get(RptStaticValue.RPT_RFAIL2_COLUMN_ID):0 %></td>
								<%
									}
									else if(RptStaticValue.RPT_JSSUCC_COLUMN_ID.equals(rptConf.getColId()))
									{//接收成功数
								%>
								<td><%=mapsum.get(RptStaticValue.RPT_JSSUCC_COLUMN_ID)!=null?mapsum.get(RptStaticValue.RPT_JSSUCC_COLUMN_ID):0 %></td>
								<%
									}
									else if(RptStaticValue.RPT_RNRET_COLUMN_ID.equals(rptConf.getColId()))
									{//未返数
								%>
								<td><%=mapsum.get(RptStaticValue.RPT_RNRET_COLUMN_ID)!=null?mapsum.get(RptStaticValue.RPT_RNRET_COLUMN_ID):0 %></td>
								<%
									}
									else if(RptStaticValue.RPT_FSSUCC_ICOUNT_COLUMN_ID.equals(rptConf.getColId()))
									{//发送成功率
								%>
								<td><%=mapsum.get(RptStaticValue.RPT_FSSUCC_ICOUNT_COLUMN_ID)!=null?mapsum.get(RptStaticValue.RPT_FSSUCC_ICOUNT_COLUMN_ID):0 %></td>
								<%
									}
									else if(RptStaticValue.RPT_RFAIL1_ICOUNT_COLUMN_ID.equals(rptConf.getColId()))
									{//发送失败率
								%>
								<td><%=mapsum.get(RptStaticValue.RPT_RFAIL1_ICOUNT_COLUMN_ID)!=null?mapsum.get(RptStaticValue.RPT_RFAIL1_ICOUNT_COLUMN_ID):0 %></td>
								<%
									}
									else if(RptStaticValue.RPT_JSSUCC_FSSUCC_COLUMN_ID.equals(rptConf.getColId()))
									{//接收成功率
								%>
								<td><%=mapsum.get(RptStaticValue.RPT_JSSUCC_FSSUCC_COLUMN_ID)!=null?mapsum.get(RptStaticValue.RPT_JSSUCC_FSSUCC_COLUMN_ID):0 %></td>
								<%
									}
									else if(RptStaticValue.RPT_RFAIL2_FSSUCC_COLUMN_ID.equals(rptConf.getColId()))
									{//接收失败率
								%>
								<td><%=mapsum.get(RptStaticValue.RPT_RFAIL2_FSSUCC_COLUMN_ID)!=null?mapsum.get(RptStaticValue.RPT_RFAIL2_FSSUCC_COLUMN_ID):0 %></td>
								<%
									}
									else if(RptStaticValue.RPT_RNRET_FSSUCC_COLUMN_ID.equals(rptConf.getColId()))
									{//未返率
								%>
								<td><%=mapsum.get(RptStaticValue.RPT_RNRET_FSSUCC_COLUMN_ID)!=null?mapsum.get(RptStaticValue.RPT_RNRET_FSSUCC_COLUMN_ID):0 %></td>
								<%}
								} %>
								</tr>
								<%}else{
							%> 
							<tr>
									<td colspan="<%=7+rptConList.size() %>" align="center"><%=strshow %></td>
									
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
			<%-- 内容结束 --%>
			<%-- foot开始 --%>
			<%-- 内容结束 --%>
			<%-- foot开始 --%>
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

<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=commonPath %>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=commonPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
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
		       //window.history.go(-1);
		       return;			       
		    }
		var excelURL="<%=excelURL%>";
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
			initPage(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>);
			$('#search').click(function(){
				var nationCode=$("#nationCode").val();
				var nationName=$("#nationName").val();
				nationName=encodeURI(encodeURI(nationName));
				location.href="<%=path%>/rep_spMtReport.htm?method=nationSendInfo&web=1&nationCode="+nationCode+"&nationName="+nationName+"&"+excelURL;
		});
			//导出全部数据到excel
			$("#exportCondition").click(
			 function()
			 {

				  //if(confirm("确定要导出数据到excel?"))
				  if(confirm(getJsLocaleMessage("cxtj","cxtj_sjcx_report_text_1")))
				   {
				   		
				   		if(excelURL==""){
						       //alert("加载页面失败,请检查网络是否正常!");	
						       alert(getJsLocaleMessage("cxtj","cxtj_sjcx_report_text_2"));
						       return;		
					   	}
				   		<%
				   			
				   		if(reportList!=null && reportList.size()>0){
				   		%>
						var nationCode=$("#nationCode").val();
						var nationName=$("#nationName").val();
						nationName=nationName;
						$.ajax({
								type: "POST",
								url: "<%=path%>/rep_spMtReport.htm?method=nationExportExcel&"+excelURL,
								data: {
										nationCode:nationCode,
									   	nationName:nationName
									  },
					            beforeSend:function () {
					                    page_loading();
					               	  },
					            complete:function () {
					           	  		page_complete()
					                  },
					            success:function (result) {
					                    if (result == 'true') {
					                        download_href("<%=path%>/rep_spMtReport.htm?method=nationdownloadFile");
					                    } else {
					                       // alert('导出失败！');
					                       alert(getJsLocaleMessage("cxtj","cxtj_sjcx_report_text_3")); 
					                    }
	               					}
						 });			
				   		//location.href="<%=path%>/rep_spMtReport.htm?method=nationExportExcel&"+excelURL+"&nationCode="+nationCode+"&nationName="+nationName;
				   		
				   		<%	
				   		}else{
				   		%>
				   		//alert("无数据可导出！");
				   		alert(getJsLocaleMessage("cxtj","cxtj_sjcx_report_text_4"));
				   		<%
				   		}%>
				   }				 
			  });		
			
			
		});
		
		function showback(){
			$("#pageForm").attr("action","rep_spMtReport.htm?method=find&isback=1");
			submitForm();
			//location.href="<%=path%>/rep_spMtReport.htm?method=find&lguserid=<%=request.getParameter("lguserid")%>&lgcorpcode=<%=request.getParameter("lgcorpcode")%>";
		}
</script>
	</body>
</html>
