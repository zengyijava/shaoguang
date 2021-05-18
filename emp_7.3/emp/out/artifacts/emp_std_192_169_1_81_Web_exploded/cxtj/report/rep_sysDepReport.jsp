<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.entity.system.LfPageField"%>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils"%>
<%@ page import="com.montnets.emp.report.bean.RptConfInfo"%>
<%@ page import="com.montnets.emp.report.bean.RptStaticValue"%>
<%@ page import="com.montnets.emp.report.biz.ReportBiz"%>
<%@ page import="com.montnets.emp.report.biz.RptConfBiz"%>
<%@ page import="com.montnets.emp.report.vo.DepRptVo"%>
<%@ page import="com.montnets.emp.report.vo.MtDataReportVo"%>
<%@page import="com.montnets.emp.util.PageInfo"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%@include file="/common/common.jsp" %>
<%
	String path = request.getContextPath();
	String iPath = request.getRequestURI().substring(0, request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0, iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0, inheritPath.lastIndexOf("/"));
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
	
	PageInfo pageInfo = new PageInfo();
	if(request.getAttribute("pageInfo")!=null){
		pageInfo = (PageInfo) request.getAttribute("pageInfo");
	}

	String icount = request.getAttribute("icount")==null?"0":request.getAttribute("icount").toString();
	String rsucc = request.getAttribute("rsucc")==null?"0":request.getAttribute("rsucc").toString();
	String rfail1 = request.getAttribute("rfail1")==null?"0":request.getAttribute("rfail1").toString();
	String rfail2 = request.getAttribute("rfail2")==null?"0":request.getAttribute("rfail2").toString();
	String rnret = request.getAttribute("rnret")==null?"0":request.getAttribute("rnret").toString();
	
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	//Date date = new Date();
	Calendar c = Calendar.getInstance();
	c.add(Calendar.DAY_OF_MONTH, -1);
	String beginTime = df.format(c.getTime()).substring(0, 8) + "01";
	String endTime = df.format(c.getTime()).substring(0, 10); //change by dj

	@SuppressWarnings("unchecked")
	Map<String, String> btnMap = (Map<String, String>) session.getAttribute("btnMap");//按钮权限Map

	@SuppressWarnings("unchecked")
	Map<String, String> titleMap = (Map<String, String>) session.getAttribute("titleMap");

	@SuppressWarnings("unchecked")
	List<DepRptVo> resultListt = (List<DepRptVo>) request.getAttribute("resultList");
	List<LfPageField> pagefileds = (List<LfPageField>) session.getAttribute("pagefileddeps");
	String menuCode = titleMap.get("sysDepReport");

	menuCode = menuCode == null ? "0-0-0" : menuCode;
	String findResult = null;
	findResult = (String) request.getAttribute("findresult");
	String skin = session.getAttribute("stlyeSkin") == null ? "default" : (String) session.getAttribute("stlyeSkin");
	String msType = request.getParameter("msType");
	String spnumtype=request.getParameter("spnumtype");
	boolean isFirstEnter = (Boolean) request.getAttribute("isFirstEnter");
	
	String reportType = request.getParameter("reportType");
	String depName = request.getParameter("depNam");
	
	MessageUtils messageUtils = new MessageUtils();
	//给属性赋值  确定
	String qd = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_jgtjbb_qd", request);
	//清空
	String qk = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_jgtjbb_qk", request);
	//机构
	String jg = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_jgtjbb_jg", request);
	if(jg!=null&&jg.length()>1){
    	jg = jg.substring(0,jg.length()-1);
    }
	//操作员
	String czy = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_jgtjbb_czy", request);
	if(czy!=null&&czy.length()>1){
    	czy = czy.substring(0,czy.length()-1);
    }
	//发送类型
	String fslx = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_fslx", request);
	if(fslx!=null&&fslx.length()>1){
    	fslx = fslx.substring(0,fslx.length()-1);
    }
	//运营商
	String yys = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_yystjbb_yys", request);
	if(yys!=null&&yys.length()>1){
    	yys = yys.substring(0,yys.length()-1);
    }						
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<title><%=titleMap.get(menuCode)%></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin%>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin%>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<%if(StaticValue.ZH_HK.equals(langName)){%>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<%}%>
		<link href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
		<link href="<%=commonPath%>/common/widget/zTreeStyle/zTreeStyle.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/cxtj/common/css/inbox.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
	</head>
	<body class="rep_sysDepReport">
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
				<input type="hidden" value="<%=skin%>" id="skin" name="skin"/>
				<form name="pageForm" action="rep_sysDepReport.htm" method="post" id="pageForm">
					<input type="hidden" id="deptString" name="deptString" value="<%=request.getParameter("deptString") == null ? "" : request.getParameter("deptString")%>" />
					<input type="hidden" id="sp" name="sp" value="<%=request.getAttribute("sp") == null ? "" : request.getAttribute("sp")%>" />
					<div class="buttons">
						<div id="toggleDiv">
						</div>
						<%
							if(btnMap.get(menuCode + "-5") != null)
								{
						%>
						<a id="exportCurrent"></a>
						<a id="exportCondition"></a>

						<input type="hidden" name="menucode" id="menucode" value="17" />
						<%
							}
						%>

						<a id="exportCondition"><emp:message key="cxtj_sjcx_yystjbb_dc" defVal="导出" fileName="cxtj"/></a>
						
					</div>
					<div id="condition">
						<table>
							<tbody>
								<tr>
									<td>
										<emp:message key="cxtj_sjcx_jgtjbb_jg" defVal="机构：" fileName="cxtj"/>
									</td>
									<td class="condi_f_l">
										<div class="depNamWrapper">
											<%-- <input type="text" id="depNam" name="depNam" value="<%=depName == null ? "请选择" : depName.replace("<", "&lt;").replace(">", "&gt;")%>"--%>
											<input type="text" id="depNam" name="depNam" value="<%=depName == null ? MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_qxz", request) : depName.replace("<", "&lt;").replace(">", "&gt;")%>"
												onclick="showMenu();" readonly class="treeInput"/>
											&nbsp;
										</div>
										<div id="dropMenu">
											<div>
												<input type="button" value="<%=qd %>" class="btnClass1 dropMenu_btn" style="width: 50px" onclick="zTreeOnClickOK3();"/>&nbsp;&nbsp;
												<input type="button" value="<%=qk %>" class="btnClass1 dropMenu_btn" style="width: 50px" onclick="cleanSelect_dep();"/>
											</div>
											<ul id="dropdownMenu" class="tree"></ul>
										</div>
									</td>
									<td>
										<emp:message key="cxtj_sjcx_zhtjbb_fslx" defVal="发送类型：" fileName="cxtj"/>
									</td>
									<td>
										<select name="datasourcetype" id="datasourcetype">
											<%
											if(btnMap.get(menuCode + "-1") != null&&btnMap.get(menuCode + "-2") != null){
											 %>
												<option value="0" <%if("0".equals(request.getParameter("datasourcetype"))){ %> selected="selected" <%} %>><emp:message key="cxtj_sjcx_yystjbb_qb" defVal="全部" fileName="cxtj"/></option>
											<%
											}
											if(btnMap.get(menuCode + "-1") != null){
											 %>
												<option value="1" <%if("1".equals(request.getParameter("datasourcetype"))){ %> selected="selected" <%} %>><emp:message key="cxtj_sjcx_zhtjbb_fs" defVal="EMP发送" fileName="cxtj"/></option>
											<%
											}
											if(btnMap.get(menuCode + "-2") != null){
											 %>
												<option value="2" <%if("2".equals(request.getParameter("datasourcetype"))){ %> selected="selected" <%} %>><emp:message key="cxtj_sjcx_zhtjbb_httpjr" defVal="HTTP接入" fileName="cxtj"/></option>
											<%
											}
											if(btnMap.get(menuCode + "-3") != null){
											 %>
												<option value="3" <%if("3".equals(request.getParameter("datasourcetype"))){ %> selected="selected" <%} %>><emp:message key="cxtj_sjcx_zhtjbb_dbjr" defVal="DB接入" fileName="cxtj"/></option>
											<%
											}
											if(btnMap.get(menuCode + "-4") != null){
											 %>
												<option value="4" <%if("4".equals(request.getParameter("datasourcetype"))){ %> selected="selected" <%} %>><emp:message key="cxtj_sjcx_zhtjbb_zljr" defVal="直连接入" fileName="cxtj"/></option>
											<%
											}
											 %>
											 

										</select>
									</td>
									<%--<td>
									短信类型：
									</td>
									<td>
										<select name="msType" id="msType" style="width:183px">
										     <option value="0" <%="0".equals(msType) ? "selected" : ""%>>短信</option>
										     
										     <option value="1" <%="1".equals(msType) ? "selected" : ""%>>彩信</option>
										     
										     </select>
									</td> 
									
								--%>
									<%
										String typename = "";
											if(pagefileds != null && pagefileds.size() > 0)
											{
												LfPageField first = pagefileds.get(0);
												typename = first.getField() + "：";
											}
									%>
									<td>
										<emp:message key="cxtj_sjcx_yystjbb_xxlx" defVal="信息类型" fileName="cxtj"/>
									</td>

									<td><%--
										<select name="msType" id="msType" style="width: 182px">
											<%
												if(pagefileds != null && pagefileds.size() > 1)
													{
														for (int i = 1; i < pagefileds.size(); i++)
														{
															LfPageField pagefid = pagefileds.get(i);
											%>
											<option value="<%=pagefid.getSubFieldValue()%>"
												<%=pagefid.getSubFieldValue().equals(msType == null ? "" : msType) ? "selected" : ""%>><%=pagefid.getSubFieldName()%></option>
											<%
												}
													}
											%>
											
										</select> --%>
										<select name="msType" id="msType">
											<option value="0" <%="0".equals(msType == null ? "" : msType)?"selected":"" %>><emp:message key="cxtj_sjcx_yystjbb_dx" defVal="短信" fileName="cxtj"/></option>
											<option value="1" <%="1".equals(msType == null ? "" : msType)?"selected":"" %>><emp:message key="cxtj_sjcx_yystjbb_cx" defVal="彩信" fileName="cxtj"/></option>
										</select>
									</td>
									<td class="tdSer">
										<center>
											<a id="search" name="research"></a>
										</center>
									</td>
								</tr>
								<tr>
									<td>
										<emp:message key="cxtj_sjcx_yystjbb_yys" defVal="运营商：" fileName="cxtj"/>
									</td>
									<td>
										<select name="spnumtype" id="spnumtype">
									     	<option value="0" <%if("0".equals(spnumtype)){out.print("selected"); } %>><emp:message key="cxtj_sjcx_zhtjbb_gn" defVal="国内" fileName="cxtj"/></option>
									     	<option value="1" <%if("1".equals(spnumtype)){out.print("selected"); } %>><emp:message key="cxtj_sjcx_zhtjbb_gw" defVal="国外" fileName="cxtj"/></option>
									     	<option value="-1" <%if("-1".equals(spnumtype)){out.print("selected"); } %>><emp:message key="cxtj_sjcx_yystjbb_qb" defVal="全部" fileName="cxtj"/></option>
										</select>
									</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
								</tr>
								<tr>
									<td>
										<emp:message key="cxtj_sjcx_zhtjbb_bblx" defVal="报表类型：" fileName="cxtj"/>
									</td>
									<td>
										<select name="reportType" id="reportType">
											<option value="0" <%="0".equals(reportType)?"selected":""%>><emp:message key="cxtj_sjcx_yystjbb_rbb" defVal="日报表" fileName="cxtj"/></option>
											<option value="1" <%="1".equals(reportType)?"selected":""%>><emp:message key="cxtj_sjcx_yystjbb_ybb" defVal="月报表" fileName="cxtj"/></option>
											<option value="2" <%="21".equals(reportType)?"selected":""%>><emp:message key="cxtj_sjcx_yystjbb_nbb" defVal="年报表" fileName="cxtj"/></option>
										</select>
									</td>
								
									<td>
										<label id="sjlabel"><emp:message key="cxtj_sjcx_yystjbb_tjsj" defVal="统计时间：" fileName="cxtj"/></label>：
									</td>
									<td class="tableTime">
										<input type="text" class="Wdate" readonly="readonly" onclick="setime()"
											value="<%=request.getParameter("begintime") != null ? request.getParameter("begintime") : beginTime%>" 
											id="sendtime" name="begintime">
											<input type="hidden" id="sTime" value="<%=beginTime%>"/>
									</td>
									<td>
										<label id="zhilabel"><emp:message key="cxtj_sjcx_yystjbb_z" defVal="至：" fileName="cxtj"/></label>
									</td>
									<td>
										<input type="text" class="Wdate" readonly="readonly" onclick="retime()"
											value="<%=request.getParameter("endtime") != null ? request.getParameter("endtime") : endTime%>"
											id="recvtime" name="endtime">
											<input type="hidden" id="eTime" value="<%=endTime%>"/>
									</td>
									<td>
										&nbsp;
									</td>
								</tr>
								
							</tbody>
						</table>
					</div>
					<table id="content">
						<thead>
							<tr>
								<th>
									<emp:message key="cxtj_sjcx_yystjbb_sj" defVal="时间" fileName="cxtj"/>
								</th>
								<th>
									<%=jg %>/<%=czy %>
								</th>
								<th>
									<%=fslx %>
								</th>
								<th>
									<%=yys %>
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
								<th>
									<emp:message key="cxtj_sjcx_yystjbb_xq" defVal="详情" fileName="cxtj"/>
								</th>
							</tr>
						</thead>
						<tbody>
							<%
								if(isFirstEnter)
									{
							%>
							<tr>
								<td colspan="<%=5+rptConList.size() %>" align="center">
									<emp:message key="cxtj_sjcx_yystjbb_qdjcxhqsj" defVal="请点击查询获取数据" fileName="cxtj"/>
								</td>
							</tr>
							<%
								}
									else if(resultListt==null||resultListt.size()==0)
									{
							%>
							<tr>
								<td colspan="<%=5+rptConList.size() %>" align="center">
									<emp:message key="cxtj_sjcx_yystjbb_wjj" defVal="无记录" fileName="cxtj"/>
								</td>
							</tr>
							<%
								}
									else
									{
							%>

							<%
								if(findResult != null)
										{
							%>

							<tr>
								<td colspan="<%=5+rptConList.size() %>" align="center">
									<emp:message key="cxtj_sjcx_yystjbb_wjj" defVal="无记录" fileName="cxtj"/>
								</td>
							</tr>
							<%
								}else{
							%>
							<%
								for (int i = 0; i < resultListt.size(); i++)
											{
												DepRptVo mdreportVo = resultListt.get(i);
							%>
							<tr>
								<%
									//时间格式显示处理  eg：2012年12月12日 
													String btime = request.getParameter("begintime") != null ? request.getParameter("begintime") : beginTime;
													String etime = request.getParameter("endtime") != null ? request.getParameter("endtime") : endTime;
													String showTime = "";
													if(!"".equals(btime) && null != btime && 0 != btime.length())
													{
														String btemp[] = btime.split("-");
														if(btemp.length==1){
															//btime = btemp[0] + MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_year", request);
															btime = btemp[0];
														}else if(btemp.length==2){
															//btime = btemp[0] + MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_year", request) + btemp[1] + MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_month", request);
															btime = btemp[0] + "-" + btemp[1];
														}else{
															//btime = btemp[0] + MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_year", request) + btemp[1] + MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_month", request) + btemp[2] + MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_day", request);
															btime = btemp[0] + "-" + btemp[1] + "-" + btemp[2];
														}
													}

													if(!"".equals(etime) && null != etime && 0 != etime.length())
													{
														String etemp[] = etime.split("-");
														if(etemp.length==1){
															etime = "";
														}else if(etemp.length==2){
															etime = "";
														}else{
															//etime = " - " +etemp[0] + MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_year", request) + etemp[1] + MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_month", request) + etemp[2] + MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_day", request);
															etime = "  "+MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_z", request)+"  " +etemp[0] + "-" + etemp[1] + "-" + etemp[2];
														}
													}
													showTime = btime +  etime;
													session.setAttribute("showTime", showTime);
								%>
								<td><%=showTime%></td>
								<td class="textalign"><%=mdreportVo.getDepName() == null ? MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_wz", request) : mdreportVo.getDepName().toString().replace("<", "&lt;").replace(">", "&gt;")%>
									<%
										if(mdreportVo.getDepName() != null && mdreportVo.getUserState() != null && mdreportVo.getUserState() == 2)
														{
															out.print("<font color='red'>("+MessageUtils.extractMessage("cxtj", "cxtj_sjcx_jgtjbb_yzx", request)+")</font>");
														}
									%>
								</td>
								<td>
									<%
											if("0".equals(request.getParameter("datasourcetype"))){ 
													out.print(MessageUtils.extractMessage("cxtj", "cxtj_sjcx_yystjbb_qb", request));
											}else if("1".equals(request.getParameter("datasourcetype"))){
													//out.print("EMP发送");
													out.print(MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_fs", request));
											}else if("2".equals(request.getParameter("datasourcetype"))){
													//out.print("HTTP接入");
													out.print(MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_httpjr", request));
											}else if("3".equals(request.getParameter("datasourcetype"))){
													//out.print("DB接入");
													out.print(MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_dbjr", request));
											}else if("4".equals(request.getParameter("datasourcetype"))){
													//out.print("直连接入");
													out.print(MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_zljr", request));
											}else{
													out.print("--");
											}
									%>
								</td>
								<td>
									<%
									if(spnumtype!=null&&"0".equals(spnumtype)){
										//out.print("国内");
										out.print(MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_gn", request));
									}else if(spnumtype!=null&&"1".equals(spnumtype)){
										//out.print("国外");
										out.print(MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_gw", request));
									}else if(spnumtype!=null&&"-1".equals(spnumtype)){
										//out.print("全部");
										out.print(MessageUtils.extractMessage("cxtj", "cxtj_sjcx_yystjbb_qb", request));
									}else{
										out.print("--");
									}
									%>
								</td>
								<%
	 							//从公用方法中获取 计算结果map
	 							Map<String,String> map=ReportBiz.getRptNums(mdreportVo.getIcount(),mdreportVo.getRsucc(),mdreportVo.getRfail1(),
	 							mdreportVo.getRfail2(),mdreportVo.getRnret(),RptStaticValue.DEP_RPT_CONF_MENU_ID);
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
								<td>
									<a onclick="detail('<%=mdreportVo.getSecretId() %>','<%=mdreportVo.getIdtype() %>',1)"><emp:message key="cxtj_sjcx_yystjbb_ck" defVal="查看" fileName="cxtj"/></a>
									&nbsp;
									<%if(spnumtype!=null&&"1".equals(spnumtype)){%>
									<a onclick="detail('<%=mdreportVo.getSecretId() %>','<%=mdreportVo.getIdtype() %>',2)"><emp:message key="cxtj_sjcx_jgtjbb_ggxq" defVal="各国详情" fileName="cxtj"/></a>
									<%}
									%>
								</td>
							</tr>
							<%
								}
							%>
							<tr>
								<td colspan="4">
									<b><emp:message key="cxtj_sjcx_yystjbb_hj" defVal="合计：" fileName="cxtj"/></b>
								</td>
								
								<%
	 							//从公用方法中获取 计算结果map
	 							Map<String,String> mapsum=ReportBiz.getRptNums(Long.valueOf(icount),Long.valueOf(rsucc),Long.valueOf(rfail1),
	 							Long.valueOf(rfail2),Long.valueOf(rnret),RptStaticValue.DEP_RPT_CONF_MENU_ID);
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
								
								<td>
									-
								</td>
							</tr>
							<%
								}
									}
							%>
						</tbody>

						<tfoot>
							<tr>
								<td colspan="<%=5+rptConList.size() %>">
									<input type="hidden" name="pageTotalRec" id="pageTotalRec"
										value="<%=pageInfo.getTotalRec()%>" />
									<input type="hidden" name="queryTime" id="queryTime"
										value="<%=request.getParameter("begintime") == null ? "" : request.getParameter("begintime")%><%=MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_z", request) %><%=request.getParameter("endtime") == null ? "" : request.getParameter("endtime")%>" />
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
		<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-c.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.ztree-myjquery-b.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/cxtj_<%=langName%>.js"></script>
		<script type="text/javascript">
		
		var zTree3;
		var setting3;
		var zNodes3 =[];

		//获取机构代码
		setting3 = {									
				async : true,				
				asyncUrl : "<%=path%>/rep_sysDepReport.htm?method=createDeptTree", //获取节点数据的URL地址
			    isSimpleData : true,
				rootPID : 0,
				treeNodeKey : "id",
				treeNodeParentKey : "pId",
				asyncParam: ["depId"],	
				callback: {
					click: zTreeOnClick3,					
					asyncSuccess:function(event, treeId, treeNode, msg){
						if(!treeNode){
						   var rootNode = zTree3.getNodeByParam("level", 0);
						   zTree3.expandNode(rootNode, true, false);
						}
					}
				}
		};


		//隐藏机构树形控件
		function showMenu() {
			hideMenu();
			$("#dropMenu").toggle();
		}
		
		//隐藏机构树形控件
		function hideMenu() {
			$("#dropMenu").hide();
		}
		
		//选中的机构显示文本框
		function zTreeOnClick3(event, treeId, treeNode) {
			if (treeNode) {
				$("#depNam").attr("value", treeNode.name); //设置机构属性
				$("#deptString").attr("value", treeNode.id); //设置机构代码	
				
			}
			
		}		
		
		//选中的机构显示文本框
		function zTreeOnClickOK3() {
				hideMenu();
		}

		// 加载机构树形控件
		function reloadTree() {
			hideMenu();
			zTree3 = $("#dropdownMenu").zTree(setting3, zNodes3);
		}
   		
		//开始时间
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
		//发送起止时间控制
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

		function detail(depid,idtype,type){
			var reportType='<%=reportType!=null?reportType:"" %>';
			var sendtime = '<%=request.getParameter("begintime") != null ? request.getParameter("begintime") : beginTime%>';
	   		var recvtime = '<%=request.getParameter("endtime") != null ? request.getParameter("endtime") : endTime%>';
	   	    var datasourcetype='<%=request.getParameter("datasourcetype") == null ? "" : request.getParameter("datasourcetype")%>'
	   	    var mstype = '<%=msType == null ? "" : msType%>';
	   	    var spnumtype='<%=spnumtype==null?"":spnumtype%>';
	   	    var pageIndex='<%=request.getParameter("pageIndex")==null?"1":request.getParameter("pageIndex")%>';
	   	    var pageSize='<%=request.getParameter("pageSize")==null?"15":request.getParameter("pageSize")%>';
	   	    var deptString = '<%=request.getParameter("deptString") == null ? "" : request.getParameter("deptString")%>';
	   	    var method="";
	   	    if(type==1){
	   	    	method="findInfoById";
	   	    }else{
	   	    	method="findAreaInfoById";
	   	    }
			location.href="<%=path%>/rep_sysDepReport.htm?method="+method+"&lguserid=<%=request.getParameter("lguserid")%>&lgcorpcode=<%=request.getParameter("lgcorpcode")%>&lgpageIndex="+pageIndex+"&deptString="+deptString+"&lgpageSize="+pageSize+"&reportType="+reportType+"&idtype="+idtype+"&begintime="+sendtime+"&endtime="+recvtime+"&id="+depid+"&mstype="+mstype+"&datasourcetype="+datasourcetype+"&spnumtype="+spnumtype;
		}
		
		$(document).ready(function(){
			getLoginInfo("#corpCode");
			var findresult="<%=findResult%>";
			closeTreeFun(["dropMenu"],[""]);
		    if(findresult=="-1") {
		       //alert("加载页面失败,请检查网络是否正常!");	
		       alert(getJsLocaleMessage("cxtj","cxtj_sjcx_xtsxjl_text_1"));
		       return;			       
		    }
		    if ($("#reportType").val() == "1"){
					$("#recvtime").hide();
					$("#zhilabel").hide();
					//$("#sjlabel").text("统计年月");
					$("#sjlabel").text(getJsLocaleMessage("cxtj","cxtj_sjcx_jgtjbb_text_1"));
					
			}else if ($("#reportType").val() == "2"){
					$("#recvtime").hide();
					$("#zhilabel").hide();
					//$("#sjlabel").text("统计年月");
					$("#sjlabel").text(getJsLocaleMessage("cxtj","cxtj_sjcx_jgtjbb_text_1"));
					
			}else{
					$("#recvtime").show();
					$("#zhilabel").show();
					//$("#sjlabel").text("统计时间");
					$("#sjlabel").text(getJsLocaleMessage("cxtj","cxtj_sjcx_jgtjbb_text_2"));
					
			}
			//4.0下手动实现
            var skin = $("#skin").val();
            if(skin.indexOf("frame4.0") > -1){
				$("#reportType").next(".c_selectBox ").find(".c_result li").each(function () {
                    $(this).click(function () {
                        var t = $("#sTime").attr("value");
                        var r = $("#eTime").val();//结束时间
                        if($(this).text() === getJsLocaleMessage("cxtj","cxtj_sjcx_yystjbb_rbb")){
                            $("#sendtime").attr("value",t);
                            $("#recvtime").attr("value",r);
                            $("#recvtime").show();
                            $("#zhilabel").show();
                            //$("#sjlabel").text("统计时间");
                            $("#sjlabel").text(getJsLocaleMessage("cxtj","cxtj_sjcx_jgtjbb_text_2"));
                        }else if($(this).text() === getJsLocaleMessage("cxtj","cxtj_sjcx_yystjbb_ybb")){
                            $("#sendtime").attr("value",t.toString().substring(0,7));
                            $("#recvtime").attr("value",r.toString().substring(0,7));
                            $("#recvtime").hide();
                            $("#zhilabel").hide();
                            //$("#sjlabel").text("统计年月");
                            $("#sjlabel").text(getJsLocaleMessage("cxtj","cxtj_sjcx_jgtjbb_text_1"));
                        }else if($(this).text() === getJsLocaleMessage("cxtj","cxtj_sjcx_yystjbb_nbb")){
                            $("#sendtime").attr("value",t.toString().substring(0,4));
                            $("#recvtime").attr("value",r.toString().substring(0,4));
                            $("#recvtime").hide();
                            $("#zhilabel").hide();
                            //$("#sjlabel").text("统计年月");
                            $("#sjlabel").text(getJsLocaleMessage("cxtj","cxtj_sjcx_jgtjbb_text_1"));
                        }
                    });
                });
			}

            $('#spnumtype,#datasourcetype,#msType').isSearchSelect({'width':180,'isInput':false,'zindex':0});
            $('#reportType').isSearchSelect({'width':180,'isInput':false,'zindex':0},function(o){
                var t = $("#sTime").attr("value");
                var r = $("#eTime").val();//结束时间
                if ($("#reportType").attr("value") == "1") {
                    $("#sendtime").attr("value",t.toString().substring(0,7));
                    $("#recvtime").attr("value",r.toString().substring(0,7));
                    $("#recvtime").hide();
                    $("#zhilabel").hide();
                    //$("#sjlabel").text("统计年月");
                    $("#sjlabel").text(getJsLocaleMessage("cxtj","cxtj_sjcx_jgtjbb_text_1"));
                }else if ($("#reportType").attr("value") == "2") {
                    $("#sendtime").attr("value",t.toString().substring(0,4));
                    $("#recvtime").attr("value",r.toString().substring(0,4));
                    $("#recvtime").hide();
                    $("#zhilabel").hide();
                    //$("#sjlabel").text("统计年月");
                    $("#sjlabel").text(getJsLocaleMessage("cxtj","cxtj_sjcx_jgtjbb_text_1"));
                } else {
                    $("#sendtime").attr("value",t);
                    $("#recvtime").attr("value",r);
                    $("#recvtime").show();
                    $("#zhilabel").show();
                    //$("#sjlabel").text("统计时间");
                    $("#sjlabel").text(getJsLocaleMessage("cxtj","cxtj_sjcx_jgtjbb_text_2"));
                }
            });

			reloadTree();
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
			
			var checkSubmitFlag = true;
			$('#search').click(function(){
			
				var judgeSendtime = $("#sendtime").val();//开始时间
				
				var judgeendtime = $("#recvtime").val();//结束时间
				
				var datasourcetype = $("#datasourcetype").val();//数据源
				
				if(datasourcetype==null||""==datasourcetype)
				{
					//alert("您当前的角色未赋予查看[EMP界面发送、接口发送]的数据权限");	
					alert(getJsLocaleMessage("cxtj","cxtj_sjcx_jgtjbb_text_3"));	
					return;
				}
				
				if("" == $.trim(judgeSendtime) || "" == $.trim(judgeendtime) )
				{
					//alert("请选择开始时间和结束时间！");	
					alert(getJsLocaleMessage("cxtj","cxtj_sjcx_jgtjbb_text_4"));	
				}else
				{
				   checkSubmit();
				}
			
			});			
			
			function checkSubmit(){
			    if(checkSubmitFlag ==true){
			        // $("a[name='research']").attr("title","您已经点过了查询按钮，请稍等!");
			         $("a[name='research']").attr("title",getJsLocaleMessage("cxtj","cxtj_sjcx_jgtjbb_text_5"));
			         submitForm();//选择好时间段，才允许查询 		        
			         checkSubmitFlag = false;			         
			    }
			    else{
			      // $("a[name='research']").attr("title","您已经点过了查询按钮，请稍等!");		      
			    }
			}			
			
			
			//导出全部数据到excel
			$("#exportCondition").click(
			 function()
			 {
				
				  //if(confirm("确定要导出数据到excel?"))
				  if(confirm(getJsLocaleMessage("cxtj","cxtj_sjcx_xtsxjl_text_2")))
				   {
						<%
					   		List<MtDataReportVo> list = (List<MtDataReportVo>) request.getAttribute("resultList");
							if(list != null && list.size() > 0){
						%>			
								var queryTime=$("#queryTime").val();
						   		var sendtime = '<%=request.getParameter("begintime") != null ? request.getParameter("begintime") : beginTime%>';
						   		var recvtime = '<%=request.getParameter("endtime") != null ? request.getParameter("endtime") : endTime%>';
						   	    var depNam = '<%=request.getParameter("deptString") == null ? "" : request.getParameter("deptString")%>';
						   	    var datasourcetype='<%=request.getParameter("datasourcetype") == null ? "" : request.getParameter("datasourcetype")%>'
						   	    var mstype = '<%=msType == null ? "" : msType%>';
						   	    var spnumtype='<%=spnumtype==null?"":spnumtype %>';
						   	    var reporttype='<%=reportType!=null?reportType:"" %>';
								$.ajax({
									type: "POST",
									url: "<%=path%>/rep_sysDepReport.htm?method=r_sdRptExportExcel",
									data: {
										lguserid:'<%=request.getParameter("lguserid")%>',
										lgcorpcode:'<%=request.getParameter("lgcorpcode")%>',
										queryTime:queryTime,
										begintime:sendtime,
										endtime:recvtime,
										depNam:depNam,
										mstype:mstype,
										datasourcetype:datasourcetype,
										spnumtype:spnumtype,
										reportType:reporttype
									},
						            beforeSend:function () {
						                page_loading();
						            },
						            complete:function () {
						           	  	page_complete()
						            },
						            success:function (result) {
						                    if (result == 'true') {
						                        download_href("<%=path%>/rep_sysDepReport.htm?method=downloadFile");
						                    } else {
						                        //alert('导出失败！');
						                        alert(getJsLocaleMessage("cxtj","cxtj_sjcx_xtsxjl_text_3"));
						                    }
	           					}
				 			});			
							 //location.href="<%=path%>/rep_sysDepReport.htm?method=r_sdRptExportExcel&lguserid=<%=request.getParameter("lguserid")%>&lgcorpcode=<%=request.getParameter("lgcorpcode")%>&queryTime="+queryTime+"&begintime="+sendtime+"&endtime="+recvtime+"&depNam="+depNam+"&mstype="+mstype+"&datasourcetype="+datasourcetype+"&spnumtype="+spnumtype+"&reportType="+reporttype;
					   	<%	}else{	%>
					   		//alert("无数据可导出！");
					   		alert(getJsLocaleMessage("cxtj","cxtj_sjcx_xtsxjl_text_4"));
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
			$('#depNam').attr('value', '请选择');
			$('#deptString').attr('value', '');
		}
</script>
	</body>
</html>
