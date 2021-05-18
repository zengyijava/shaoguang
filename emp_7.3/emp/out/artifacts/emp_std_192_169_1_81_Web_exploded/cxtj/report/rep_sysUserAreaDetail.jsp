<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils"%>
<%@ page import="com.montnets.emp.report.bean.RptConfInfo"%>
<%@ page import="com.montnets.emp.report.bean.RptStaticValue"%>
<%@ page import="com.montnets.emp.report.biz.ReportBiz"%>
<%@ page import="com.montnets.emp.report.biz.RptConfBiz"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
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
	String icount = request.getAttribute("userareadetailicount")==null?"0":request.getAttribute("userareadetailicount").toString();
	String rsucc = request.getAttribute("userareadetailrsucc")==null?"0":request.getAttribute("userareadetailrsucc").toString();
	String rfail1 = request.getAttribute("userareadetailrfail1")==null?"0":request.getAttribute("userareadetailrfail1").toString();
	String rfail2 = request.getAttribute("userareadetailrfail2")==null?"0":request.getAttribute("userareadetailrfail2").toString();
	String rnret = request.getAttribute("userareadetailrnret")==null?"0":request.getAttribute("userareadetailrnret").toString();

	SimpleDateFormat def = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	Calendar c = Calendar.getInstance();
	c.add(Calendar.DAY_OF_MONTH, -1);
	String beginTime = def.format(c.getTime()).substring(0, 8) + "01";
	String endTime = def.format(c.getTime()).substring(0, 11); //change by dj
	
	@SuppressWarnings("unchecked")
	Map<String, String> btnMap = (Map<String, String>) session.getAttribute("btnMap");//按钮权限Map

	@SuppressWarnings("unchecked")
	Map<String, String> titleMap = (Map<String, String>) session.getAttribute("titleMap");

	@SuppressWarnings("unchecked")
	List<DynaBean> resultListt = (List<DynaBean>) request.getAttribute("userareadetaillist");
	if(resultListt==null){
	   resultListt=new ArrayList<DynaBean>();
	}
	String menuCode = titleMap.get("sysUserReport");

	menuCode = menuCode == null ? "0-0-0" : menuCode;
	String findResult = null;
	findResult = (String) request.getAttribute("findresult");
	String skin = session.getAttribute("stlyeSkin") == null ? "default" : (String) session.getAttribute("stlyeSkin");
	
	String spnumtype=request.getParameter("spnumtype");
	
	//国家名称
	String gjmc = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_gjmc", request);
    if(gjmc!=null&&gjmc.length()>1){
    	gjmc = gjmc.substring(0,gjmc.length()-1);
    }
    //至
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
		<link href="<%=commonPath%>/cxtj/common/css/inbox.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
	</head>
	<body class="rep_sysUserAreaDetail">
		<div id="container" class="container">
			<%-- 当前位置 --%>
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode)%>

			<%-- 内容开始 --%>
			<%
				if(btnMap.get(menuCode + "-0") != null)
				{
			%>
			<div id="rContent" class="rContent">
				<input type="hidden" value="<%=inheritPath%>" id="inheritPath" />
				<form name="pageForm" action="rep_sysUserReport.htm?method=findAreaInfoById&lguserid=<%=request.getParameter("lguserid") %>&lgcorpcode=<%=request.getParameter("lgcorpcode") %>" method="post"
					id="pageForm">
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
							if(btnMap.get(menuCode + "-5") != null){
						%>
						<a id="exportCurrent"></a>
						<a id="exportCondition"></a>
						<input type="hidden" name="menucode" id="menucode" value="17" />
						<%
							}
						%>
						<a id="exportCondition"><emp:message key="cxtj_sjcx_report_dc" defVal="导出" fileName="cxtj"></emp:message></a>
					<span id="backgo" class="right" onclick="back()"><emp:message key="cxtj_sjcx_report_fh" defVal="返回" fileName="cxtj"></emp:message></span>
					</div>
					<div id="condition">
						<table>
							<tbody>
								<tr>
									<td>
										<emp:message key="cxtj_sjcx_report_gjdm" defVal="国家代码：" fileName="cxtj"></emp:message>
									</td>
									<td>
										<input type="text"  id="areacode" name="areacode"
										onblur="javascript:numberControl($(this))" onkeyup="javascript:numberControl($(this))" 
										value='<%=request.getParameter("areacode")!=null?request.getParameter("areacode"):"" %>' maxlength="21" />
									</td>
									<td>	
										<emp:message key="cxtj_sjcx_report_gjmc" defVal="国家名称：" fileName="cxtj"></emp:message>
									</td>
									<td>
										<input type="text" name="areaname" id="areaname" class="input_bd"
										value="<%=request.getParameter("areaname")!=null?request.getParameter("areaname"):"" %>" maxlength="50" />
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
									<emp:message key="cxtj_sjcx_report_sj" defVal="时间" fileName="cxtj"></emp:message>
								</th>
								<th>
									<emp:message key="cxtj_sjcx_report_gjdqdm" defVal="国家/地区代码" fileName="cxtj"></emp:message>
								</th>
								<th>
									<%=gjmc %>
								</th>
								<th>
									<emp:message key="cxtj_sjcx_report_czy" defVal="操作员" fileName="cxtj"></emp:message>
								</th>
								<th>
									<emp:message key="cxtj_sjcx_report_jg" defVal="机构" fileName="cxtj"></emp:message>
								</th>
								<th>
									<emp:message key="cxtj_sjcx_report_tdhm" defVal="通道号码" fileName="cxtj"></emp:message>
								</th>
								<th>
									<emp:message key="cxtj_sjcx_report_tdmc" defVal="通道名称" fileName="cxtj"></emp:message>
								</th>
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
							<%
								if(resultListt!=null&& resultListt.size()>0){
								for (int i = 0; i < resultListt.size(); i++){
									DynaBean dynrptvo = resultListt.get(i);
							%>
							<tr>
								<%
								//时间格式显示处理  eg：2012年12月12日 
								String btime = request.getParameter("sendtime") != null ? request.getParameter("sendtime") : beginTime;
								String etime = request.getParameter("endtime") != null ? request.getParameter("endtime") : endTime;
								String showTime = "";
								if(!"".equals(btime) && null != btime && 0 != btime.length()){
									String btemp[] = btime.split("-");
									if(btemp.length==1){
											//btime = btemp[0] + "年";
											btime = btemp[0] ;
											//btime = btemp[0] + MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_y", request);
									}else if(btemp.length==2){
											//btime = btemp[0] + "年" + btemp[1] + "月";
											btime = btemp[0] + "-" + btemp[1] ;
											//btime = btemp[0] + MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_y", request) + btemp[1] + MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_m", request);
									}else{
											//btime = btemp[0] + "年" + btemp[1] + "月" + btemp[2] + "日";
											btime = btemp[0] + "-" + btemp[1] + "-" + btemp[2] ;
											//btime = btemp[0] + MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_y", request) + btemp[1] + MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_m", request) + btemp[2] + MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_d", request);
									}
								}
								if(!"".equals(etime) && null != etime && 0 != etime.length()){
									String etemp[] = etime.split("-");
									if(etemp.length==1){
											etime = "";
									}else if(etemp.length==2){
											etime = "";
									}else{
											//etime = " - " +etemp[0] + "年" + etemp[1] + "月" + etemp[2] + "日";
											etime = "  "+MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_z", request)+"  "+etemp[0] + "-" + etemp[1] + "-" + etemp[2];
											//etime = " - " +etemp[0] + MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_y", request) + etemp[1] + MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_m", request) + etemp[2] + MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_d", request);
									}
								}
								showTime = btime +  etime;
								session.setAttribute("showuserareatime", showTime);
								%>
								<td><%=showTime%></td>
								<td><%=dynrptvo.get("areacode")!=null?dynrptvo.get("areacode").toString():"" %></td>
								<td><%=dynrptvo.get("areaname")!=null?dynrptvo.get("areaname").toString():"" %></td>
								<td class="textalign">
								<%=session.getAttribute("userareaname")!=null?session.getAttribute("userareaname"):"--" %>	
								</td>
								<td class="textalign">
								<%=session.getAttribute("userareadepname")!=null?session.getAttribute("userareadepname"):"--" %>	
								</td>
								<td><%=dynrptvo.get("spgate")!=null?dynrptvo.get("spgate").toString():"" %></td>
								<td><%=dynrptvo.get("gatename")!=null?dynrptvo.get("gatename").toString():"" %></td>
								<%
								long icountl=dynrptvo.get("icount")!=null?Long.valueOf(dynrptvo.get("icount").toString()):0;
								long rsuccl=dynrptvo.get("rsucc")!=null?Long.valueOf(dynrptvo.get("rsucc").toString()):0;
								long rfail1l=dynrptvo.get("rfail1")!=null?Long.valueOf(dynrptvo.get("rfail1").toString()):0;
								long rfail2l=dynrptvo.get("rfail2")!=null?Long.valueOf(dynrptvo.get("rfail2").toString()):0;
								long rnretl=dynrptvo.get("rnret")!=null?Long.valueOf(dynrptvo.get("rnret").toString()):0;
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
							<%
								}
							%>
							<tr>
								<td colspan="7">
									<b><emp:message key="cxtj_sjcx_report_hj" defVal="合计：" fileName="cxtj"></emp:message></b>
								</td>
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
							<%
								}else{
							%>
							<tr>
							<td colspan="<%=7+rptConList.size() %>" align="center"><emp:message key="cxtj_sjcx_report_wjl" defVal="无记录" fileName="cxtj"></emp:message></td>
							</tr>
							<%} %>
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
		<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<link rel="stylesheet" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>">
		<script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.ztree-myjquery-b.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/cxtj_<%=langName%>.js"></script>

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
			var findresult="<%=findResult%>";
		    if(findresult=="-1")
		    {
		       //alert("加载页面失败,请检查网络是否正常!");	
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
				//选择好时间段，才允许查询
			    submitForm(); 		        
			});			
			
			
			
			//导出全部数据到excel
			$("#exportCondition").click(
			 function()
			 {
				
				  //if(confirm("确定要导出数据到excel?"))
				  if(confirm(getJsLocaleMessage("cxtj","cxtj_sjcx_report_text_1")))
				   {
					   	<%
							if(resultListt != null && resultListt.size() > 0){
						%>			
						 		var reporttype='<%=request.getParameter("reporttype")!=null?request.getParameter("reporttype"):"" %>'
								var sendtime = '<%=request.getParameter("sendtime")!=null?request.getParameter("sendtime"):beginTime %>';
						   		var recvtime = '<%=request.getParameter("endtime")!=null?request.getParameter("endtime"):endTime%>';
			   	    			var mstype = '<%=request.getParameter("mstype") == null ? "" : request.getParameter("mstype") %>';
								var userid='<%=request.getParameter("userid")==null?"":request.getParameter("userid") %>';
						   		var spnumtype='<%=spnumtype == null ? "" : spnumtype%>';
						   		var areacode='<%=request.getParameter("areacode")!=null?request.getParameter("areacode"):"" %>';
						   		var areaname='<%=request.getParameter("areaname")!=null?request.getParameter("areaname"):"" %>';
						   		$.ajax({
									type: "POST",
									url: "<%=path%>/rep_sysUserReport.htm?method=r_suRptDataAreaExportExcel",
									data: {
										lgcorpcode:'<%=request.getParameter("lgcorpcode")%>',
										lguserid:'<%=request.getParameter("lguserid")%>',
										sendtime:sendtime,
										endtime:recvtime,
										mstype:mstype,
										reporttype:reporttype,
										userid:userid,
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
						                      download_href("<%=path%>/rep_sysUserReport.htm?method=areadownloadFile");
						                      
						                } else {
						                      //alert('导出失败！');
						                      alert(getJsLocaleMessage("cxtj","cxtj_sjcx_report_text_3"));
						                }
			           				}
				 				});
						   		
						 		//location.href="<%=path%>/rep_sysUserReport.htm?method=r_suRptDataAreaExportExcel&lgcorpcode=<%=request.getParameter("lgcorpcode")%>&lguserid=<%=request.getParameter("lguserid")%>&sendtime="+sendtime+"&endtime="+recvtime+"&mstype="+mstype+"&reporttype="+reporttype+"&userid="+userid+"&spnumtype="+spnumtype+"&areacode="+areacode+"&areaname="+areaname;
						<%}else{%>
						   		//alert("无数据可导出！");
						   		alert(getJsLocaleMessage("cxtj","cxtj_sjcx_report_text_4"));
						<%
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
			//$('#depNam').attr('value', '请选择');
			$('#depNam').attr('value', getJsLocaleMessage("cxtj","cxtj_sjcx_report_text_5"));
			$('#deptString').attr('value', '');
		}
</script>
	</body>
</html>
