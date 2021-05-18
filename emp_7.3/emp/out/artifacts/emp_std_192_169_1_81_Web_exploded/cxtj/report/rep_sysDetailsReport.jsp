<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils"%>
<%@ page import="com.montnets.emp.report.bean.RptConfInfo"%>
<%@ page import="com.montnets.emp.report.bean.RptStaticValue"%>
<%@ page import="com.montnets.emp.report.biz.ReportBiz"%>
<%@ page import= "com.montnets.emp.report.biz.RptConfBiz"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="org.apache.commons.beanutils.DynaBean"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
	String path = request.getContextPath();
    String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
    String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
    String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	
	MessageUtils messageUtils = new MessageUtils();
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
    
	PageInfo pageInfo = new PageInfo();
	if (request.getAttribute("pageInfo")!=null){
		pageInfo = (PageInfo) request.getAttribute("pageInfo");
	}
	
	SimpleDateFormat def = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	Calendar c = Calendar.getInstance();
	c.add(Calendar.DAY_OF_MONTH, -1);
	String beginTime = def.format(c.getTime()).substring(0, 8) + "01";
	String endTime = def.format(c.getTime()).substring(0, 11); //change by dj
	String icount = request.getAttribute("userdetailicount")==null?"0":request.getAttribute("userdetailicount").toString();
	String rsucc = request.getAttribute("userdetailrsucc")==null?"0":request.getAttribute("userdetailrsucc").toString();
	String rfail1 = request.getAttribute("userdetailrfail1")==null?"0":request.getAttribute("userdetailrfail1").toString();
	String rfail2 = request.getAttribute("userdetailrfail2")==null?"0":request.getAttribute("userdetailrfail2").toString();
	String rnret = request.getAttribute("userdetailrnret")==null?"0":request.getAttribute("userdetailrnret").toString();
	@SuppressWarnings("unchecked")
	Map<String, String> btnMap = (Map<String, String>) session.getAttribute("btnMap");//按钮权限Map
	@SuppressWarnings("unchecked")	
	Map<String, String> titleMap = (Map<String, String>) session.getAttribute("titleMap");
	@SuppressWarnings("unchecked")	
	List<DynaBean> userdetaillist = (List<DynaBean>) request.getAttribute("userdetaillist");
	String menuCode = titleMap.get("sysUserReport");
	menuCode = menuCode == null ? "0-0-0" : menuCode;
	String msType =request.getParameter("mstype");
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	String reportType=request.getParameter("reporttype");
	String spnumtype=request.getParameter("spnumtype");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
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
		<link href="<%=commonPath %>/common/widget/zTreeStyle/zTreeStyle.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=commonPath %>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>">
		<link rel="stylesheet" href="<%=commonPath %>/common/skin/<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
		<link href="<%=commonPath%>/cxtj/common/css/inbox.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
	</head>
	<body class="rep_sysDetailsReport">
		<div id="container" class="container">
			<%-- 当前位置 --%>
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode) %>
			<%-- 内容开始 --%>
			<%
				if (btnMap.get(menuCode + "-0") != null)
				{
			%>
			<div id="rContent" class="rContent">
				<input type="hidden" value="<%=inheritPath %>" id="inheritPath"/>
				<form name="pageForm" action="rep_sysUserReport.htm?method=findInfoById"  method="post" id="pageForm">
					<input type="hidden" id="reporttype" name="reporttype" value="<%=request.getParameter("reporttype") == null ? "" : request.getParameter("reporttype") %>" />
					<input type="hidden" id="sendtime" name="sendtime" value="<%=request.getParameter("sendtime") == null ? "" : request.getParameter("sendtime") %>" />
					<input type="hidden" id="endtime" name="endtime" value="<%=request.getParameter("endtime") == null ? "" : request.getParameter("endtime") %>" />
					<input type="hidden" id="userid" name="userid" value="<%=request.getParameter("userid") == null ? "" : request.getParameter("userid") %>" />
					<input type="hidden" id="mstype" name="mstype" value="<%=request.getParameter("mstype") == null ? "" : request.getParameter("mstype") %>" />
					<input type="hidden" id="spnumtype" name="spnumtype" value="<%=request.getParameter("spnumtype") == null ? "" : request.getParameter("spnumtype") %>" />
					<input type="hidden" id="deptString" name="deptString" value="<%=request.getParameter("deptString") == null ? "" : request.getParameter("deptString")%>" />
					<input type="hidden" id="depNam" name="depNam" value="<%=session.getAttribute("userrptdname") == null ? MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_qxz", request) : session.getAttribute("userrptdname").toString().replace("<", "&lt;").replace(">", "&gt;")%>" />
					<input type="hidden" id="userString" name="userString" value="<%=request.getParameter("userString") == null ? "" : request.getParameter("userString")%>" />
					<input type="hidden" id="userName" name="userName" value="<%=session.getAttribute("userrptuname")==null?MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_qxz", request):session.getAttribute("userrptuname").toString().replace("<","&lt;").replace(">","&gt;") %>" />
					<input type="hidden" id="lgpageIndex" name="lgpageIndex" value="<%=request.getParameter("lgpageIndex")==null?"1":request.getParameter("lgpageIndex") %>" />
					<input type="hidden" id="lgpageSize" name="lgpageSize" value="<%=request.getParameter("lgpageSize")==null?"15":request.getParameter("lgpageSize") %>" />
					<div class="buttons">
						<%
							if (btnMap.get(menuCode + "-5") != null)
							{
						%>
						<a id="exportCurrent"></a>
						<a id="exportCondition" ></a>
						
						<input type="hidden" name="menucode" id="menucode" value="17"/>
						<%
							}
						%>
						
				 	   <a id="exportCondition" ><emp:message key="cxtj_sjcx_report_dc" defVal="导出" fileName="cxtj"></emp:message></a>
				 	   <span id="backgo" class="right" onclick="javascript:back()"><emp:message key="cxtj_sjcx_report_fh" defVal="返回" fileName="cxtj"></emp:message></span>
					</div>
					<table id="content">
						<thead>
							<tr>
							   <th><emp:message key="cxtj_sjcx_report_sj" defVal="时间" fileName="cxtj"></emp:message></th>
							   <th><emp:message key="cxtj_sjcx_report_czy" defVal="操作员" fileName="cxtj"></emp:message></th>
							   <th><emp:message key="cxtj_sjcx_report_jg" defVal="机构" fileName="cxtj"></emp:message></th>
							   <th><emp:message key="cxtj_sjcx_report_yys" defVal="运营商" fileName="cxtj"></emp:message></th>
							    <%
								List<RptConfInfo> rptConList = RptConfBiz.getRptConfMap().get(RptStaticValue.USER_RPT_CONF_MENU_ID);
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
							<%if(userdetaillist!=null&& userdetaillist.size()>0){
								for(int i=0;i<userdetaillist.size();i++){
									DynaBean mdreportVo = userdetaillist.get(i);
								
							%>							
							<tr>
								<td>
								<%
									//SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月d日");
									SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
									//SimpleDateFormat sdf = new SimpleDateFormat("yyyy"+MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_y", request)+"M"+MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_m", request)+"d"+MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_d", request));
									SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
									if("2".equals(reportType)){
											//out.print((mdreportVo.get("y")!=null?mdreportVo.get("y").toString():"2015")+MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_y", request)+(mdreportVo.get("imonth")!=null?mdreportVo.get("imonth").toString():"01")+MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_m", request));
											out.print((mdreportVo.get("y")!=null?mdreportVo.get("y").toString():"2015")+"-"+(mdreportVo.get("imonth")!=null?mdreportVo.get("imonth").toString():"01"));
									}else{
										out.print(sdf.format(sdf1.parse(mdreportVo.get("iymd")!=null?mdreportVo.get("iymd").toString():"20150101")));
									}
								%>
								</td>
								<td class="textalign">
									<%=session.getAttribute("username")!=null?session.getAttribute("username"):"--" %>	
								</td>
								<td class="textalign">
									<%=session.getAttribute("depname")!=null?session.getAttribute("depname"):"--" %>
								</td>		
								<td>
									<%
									if(spnumtype!=null&&"0".equals(spnumtype)){
										//out.print("国内");
										out.print(MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_gn", request));
									}else if(spnumtype!=null&&"1".equals(spnumtype)){
										//out.print("国外");
										out.print(MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_gw", request));
									}else if(spnumtype!=null&&"-1".equals(spnumtype)){
										//out.print("全部");
										out.print(MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_qb", request));
									}else{
										out.print("--");
									}
									%>
								</td>				
								<%
								long icountl=mdreportVo.get("icount")!=null?Long.valueOf(mdreportVo.get("icount").toString()):0;
								long rsuccl=mdreportVo.get("rsucc")!=null?Long.valueOf(mdreportVo.get("rsucc").toString()):0;
								long rfail1l=mdreportVo.get("rfail1")!=null?Long.valueOf(mdreportVo.get("rfail1").toString()):0;
								long rfail2l=mdreportVo.get("rfail2")!=null?Long.valueOf(mdreportVo.get("rfail2").toString()):0;
								long rnretl=mdreportVo.get("rnret")!=null?Long.valueOf(mdreportVo.get("rnret").toString()):0;
	 							//从公用方法中获取 计算结果map
	 							Map<String,String> map=ReportBiz.getRptNums(icountl,rsuccl,rfail1l,rfail2l,rnretl,RptStaticValue.USER_RPT_CONF_MENU_ID);
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
						<%} %>
							<tr>
						    <td colspan="4"><b><emp:message key="cxtj_sjcx_report_hj" defVal="合计：" fileName="cxtj"></emp:message></b></td>
						    <%
								
	 							//从公用方法中获取 计算结果map
	 							Map<String,String> map=ReportBiz.getRptNums(Long.valueOf(icount),Long.valueOf(rsucc),Long.valueOf(rfail1),Long.valueOf(rfail2),Long.valueOf(rnret),RptStaticValue.USER_RPT_CONF_MENU_ID);
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
						<%}else{						%>
						<tr>
							<td colspan="<%=4+rptConList.size() %>" align="center"><emp:message key="cxtj_sjcx_report_wjl" defVal="无记录" fileName="cxtj"></emp:message></td>
							
						</tr>
						<%} %>
												
						</tbody>
						
						<tfoot>
							<tr>
								<td colspan="<%=4+rptConList.size() %>">
								    <input type="hidden" name="pageTotalRec" id="pageTotalRec" value="<%=pageInfo.getTotalRec() %>"/>
								    <input type="hidden" name="queryTime" id="queryTime" value="<%=request.getParameter("begintime") == null ? "" : request.getParameter("begintime")%>至<%=request.getParameter("endtime") == null ? "" : request.getParameter("endtime")%>"/>
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
			<%-- 内容结束 --%>
			<%-- foot开始 --%>
			<div class="bottom">
				<div id="bottom_right">
					<div id="bottom_left"></div>
					<div id="bottom_main">
					</div>
				</div>
			</div>
		</div>
		<%-- foot结束 --%>
		<div class="clear"></div>
		<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript"	src="<%=commonPath %>/common/js/jquery.ztree-myjquery-b.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/cxtj_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>

		<script type="text/javascript">
		function back(){
			var pageIndex='<%=request.getParameter("lgpageIndex")==null?"1":request.getParameter("lgpageIndex")%>';
	   	    var pageSize='<%=request.getParameter("lgpageSize")==null?"15":request.getParameter("lgpageSize")%>';
	   	    var mstype = '<%=request.getParameter("mstype") == null ? "" : request.getParameter("mstype") %>';
	   	    var reportType='<%=request.getParameter("reporttype") == null ? "" : request.getParameter("reporttype") %>';
	   	    var begintime='<%=request.getParameter("sendtime") == null ? "" : request.getParameter("sendtime") %>';
	   	    $("#pageForm").attr("action","rep_sysUserReport.htm?method=find&inFlag=1&lguserid=<%=request.getParameter("lguserid")%>&lgcorpcode=<%=request.getParameter("lgcorpcode")%>&pageIndex="+pageIndex+"&pageSize="+pageSize+"&msType="+mstype+"&reportType="+reportType+"&begintime="+begintime);
			submitForm();
		}
		
		$(document).ready(function(){
			getLoginInfo("#corpCode");
			$("#content tbody tr").hover(function() {
					$(this).addClass("hoverColor");
			}, function() {
					$(this).removeClass("hoverColor");
			});
			initPage(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>);
			//导出全部数据到excel
			$("#exportCondition").click(
			 function()
			 {
				  //if(confirm("确定要导出数据到excel?"))
				  if(confirm(getJsLocaleMessage("cxtj","cxtj_sjcx_report_text_1")))
				   {
				   		<%
				   			List<DynaBean> list=userdetaillist;
				   			if(list!=null && list.size()>0){
				   		%>			
						   		var reporttype='<%=request.getParameter("reporttype")!=null?request.getParameter("reporttype"):"" %>'
								var sendtime = '<%=request.getParameter("sendtime")!=null?request.getParameter("sendtime"):beginTime %>';
							   	var recvtime = '<%=request.getParameter("endtime")!=null?request.getParameter("endtime"):endTime%>';
				   	    		var mstype = '<%=request.getParameter("mstype") == null ? "" : request.getParameter("mstype") %>';
								var userid='<%=request.getParameter("userid")==null?"":request.getParameter("userid") %>';
							   	var spnumtype='<%=spnumtype == null ? "" : spnumtype%>';
							   	$.ajax({
									type: "POST",
									url: "<%=path%>/rep_sysUserReport.htm?method=r_suRptDataExportExcel",
									data: {
										lgcorpcode:'<%=request.getParameter("lgcorpcode")%>',
										lguserid:'<%=request.getParameter("lguserid")%>',
										sendtime:sendtime,
										endtime:recvtime,
										mstype:mstype,
										reporttype:reporttype,
										userid:userid,
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
						                      download_href("<%=path%>/rep_sysUserReport.htm?method=detaildownloadFile");
						                } else {
						                      //alert('导出失败！');
						                      alert(getJsLocaleMessage("cxtj","cxtj_sjcx_report_text_3"));
						                }
			           				}
				 				});
				   				//location.href="<%=path%>/rep_sysUserReport.htm?method=r_suRptDataExportExcel&lgcorpcode=<%=request.getParameter("lgcorpcode")%>&lguserid=<%=request.getParameter("lguserid")%>&sendtime="+sendtime+"&endtime="+recvtime+"&mstype="+mstype+"&reporttype="+reporttype+"&userid="+userid+"&spnumtype="+spnumtype;
				   		<%	
				   			}else{
				   		%>
				   			//alert("无数据可导出！");
				   			alert(getJsLocaleMessage("cxtj","cxtj_sjcx_report_text_4"));
				   		<%
				   			}
				   		%>
				   }				 
			  });
		});

		</script>
	</body>
</html>

