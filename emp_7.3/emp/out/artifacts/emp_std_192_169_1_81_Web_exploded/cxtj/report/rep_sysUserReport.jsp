<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.entity.system.LfPageField"%>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils"%>
<%@ page import="com.montnets.emp.report.bean.RptConfInfo"%>
<%@ page import="com.montnets.emp.report.bean.RptStaticValue"%>
<%@ page import="com.montnets.emp.report.biz.ReportBiz"%>
<%@ page import="com.montnets.emp.report.biz.RptConfBiz"%>
<%@page import="com.montnets.emp.report.vo.MtDataReportVo"%>
<%@page import="com.montnets.emp.util.PageInfo"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%@include file="/common/common.jsp" %>
<%
	String path = request.getContextPath();
    String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
    String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
    String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
    
	PageInfo pageInfo = new PageInfo();
	if (request.getAttribute("pageInfo")!=null){
		pageInfo = (PageInfo) request.getAttribute("pageInfo");
	}
	
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

	@SuppressWarnings("unchecked")
	Map<String, String> btnMap = (Map<String, String>) session.getAttribute("btnMap");//按钮权限Map
	
	@SuppressWarnings("unchecked")	
	Map<String, String> titleMap = (Map<String, String>) session.getAttribute("titleMap");
	
	@SuppressWarnings("unchecked")	
	List<MtDataReportVo> resultListt = (List<MtDataReportVo>) request.getAttribute("resultList");
	List<LfPageField> pagefileds=(List<LfPageField>)session.getAttribute("pagefiledusers");
	boolean isFirstEnter = (Boolean) request.getAttribute("isFirstEnter");
	
	String menuCode = titleMap.get("sysUserReport");
	
	menuCode = menuCode == null ? "0-0-0" : menuCode;
	
	String msType =request.getParameter("msType");
	String spnumtype=request.getParameter("spnumtype");
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	String reportType=request.getParameter("reportType");
	
	MessageUtils messageUtils = new MessageUtils();
	//给属性赋值  确定
	String qd = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_jgtjbb_qd", request);
	//清空
	String qk = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_jgtjbb_qk", request);
   // String tdhm = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_xtsxjl_tdhm", request);
    //if(tdhm!=null&&tdhm.length()>1){
    	//tdhm = tdhm.substring(0,tdhm.length()-1);
    //}
    
    //时间
    String sj = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_yystjbb_sj", request);
	//操作员
	String czy = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_jgtjbb_czy", request);
    if(czy!=null&&czy.length()>1){
    	czy = czy.substring(0,czy.length()-1);
    }
	//机构
	String jg = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_jgtjbb_jg", request);
    if(jg!=null&&jg.length()>1){
    	jg = jg.substring(0,jg.length()-1);
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
		<title><%=titleMap.get(menuCode) %></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
		<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
		<%if(StaticValue.ZH_HK.equals(langName)){%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
		<link href="<%=commonPath %>/common/widget/zTreeStyle/zTreeStyle.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
		<link rel="stylesheet" href="<%=commonPath %>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>">
		<link rel="stylesheet" href="<%=commonPath %>/common/skin/<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css">
		<link href="<%=commonPath%>/cxtj/common/css/inbox.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
	</head>
	<body class="rep_sysUserReport">
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
				<input type="hidden"  id="hsendtime"  value="<%=request.getParameter("begintime")==null?"":request.getParameter("begintime") %>"/>
				<input type="hidden"  id="hendtime"  value="<%=request.getParameter("endtime")==null?"":request.getParameter("endtime") %>"/>  
				<input type="hidden"  id="hmsType"  value="<%=request.getParameter("msType")==null?"":request.getParameter("msType") %>"/> 
				<input type="hidden"  id="hreportType"  value="<%=request.getParameter("reportType")==null?"":request.getParameter("reportType") %>"/> 
					
					
				<form name="pageForm" action="rep_sysUserReport.htm"  method="post" id="pageForm">
					<input type="hidden"  id="deptString" name="deptString" value="<%=request.getParameter("deptString")==null ? "" :request.getParameter("deptString")%>"/>  
  					<input type="hidden"  id="userString" name="userString" value="<%=request.getParameter("userString")==null ? "" :request.getParameter("userString")%>"/>
  					<input type="hidden"  id="sp" name="sp" value="<%=request.getAttribute("sp")==null ? "" :request.getAttribute("sp")%>"/>      					
					<div class="buttons">
						<div id="toggleDiv">
						</div>
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
						
				 	   <a id="exportCondition" ><emp:message key="cxtj_sjcx_yystjbb_dc" defVal="导出" fileName="cxtj"></emp:message></a>
					</div>
					<div id="condition">
						<table>
							<tbody>
								<tr>
									<td>
										<emp:message key="cxtj_sjcx_jgtjbb_jg" defVal="机构：" fileName="cxtj"></emp:message>
									</td>
									 <td class="condi_f_l">
									 	<div class="depNamWrapper">
									 	<input type="text" id="depNam" name="depNam" value="<%=request.getParameter("depNam")==null?MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_qxz", request):request.getParameter("depNam").toString().replace("<","&lt;").replace(">","&gt;")%>"
									 	    readonly class="treeInput" onclick="javascript:showMenu();" />
									 	</div>									 								 	
									 	<div id="dropMenu" >
										<div>
											<input type="button" value="<%=qd %>" class="btnClass1 dropMenu_btn" style="width: 50px" onclick="zTreeOnClickOK3();"/>&nbsp;&nbsp;
											<input type="button" value="<%=qk %>" class="btnClass1 dropMenu_btn" style="width: 50px" onclick="cleanSelect_dep();"/>
										</div>	
										<ul  id="dropdownMenu" class="tree"></ul>	
										</div>									 
									 </td>		
									<td>
										<emp:message key="cxtj_sjcx_jgtjbb_czy" defVal="操作员：" fileName="cxtj"></emp:message>
									</td>									  
									<td class="condi_f_l">
									<div class="userNameWrapper">
									<input type="text" id="userName" name="userName" value="<%=request.getParameter("userName")==null?MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_qxz", request):request.getParameter("userName").toString().replace("<","&lt;").replace(">","&gt;")%>" 
										readonly class="treeInput" onclick="javascript:showMenu2();" />
									</div>
									<div id="dropMenu2" >
									<div>
										<input type="button" value="<%=qd %>" class="btnClass1 dropMenu_btn"
											   style="width: 50px" onclick="zTreeOnClickOK2();"/>&nbsp;&nbsp;
										<input type="button" value="<%=qk %>" class="btnClass1 dropMenu_btn"
											   style="width: 50px" onclick="cleanSelect_user();"/>
									</div>
									<div class="attentionDiv"><font class="zhu"><emp:message key="cxtj_sjcx_czytjbb_qgxczyjxcx" defVal="注：请勾选操作员进行查询" fileName="cxtj"></emp:message></font></div>
									<ul  id="dropdownMenu2" class="tree"></ul>	
									</div>
									</td>
									<%
									String typename="";
									if(pagefileds!=null&&pagefileds.size()>0){
										LfPageField first=pagefileds.get(0);
										typename=first.getField()+"：";
									} 
								%>
								<td>
								<emp:message key="cxtj_sjcx_yystjbb_xxlx" defVal="信息类型" fileName="cxtj"/>
								</td>
									
									<td><%--
										<select name="msType" id="msType" style="width:180px">
										<%
										if(pagefileds!=null&&pagefileds.size()>1){
											for(int i=1;i<pagefileds.size();i++){
											LfPageField pagefid=pagefileds.get(i);
										%>
										     <option value="<%=pagefid.getSubFieldValue() %>" <%=pagefid.getSubFieldValue().equals(msType==null?"":msType)?"selected":"" %>><%=pagefid.getSubFieldName() %></option>
										<% 
											}
										}
										
										%>
										</select> --%>
										<select name="msType" id="msType">
											<option value="0" <%="0".equals(msType==null?"":msType)?"selected":"" %>><emp:message key="cxtj_sjcx_yystjbb_dx" defVal="短信" fileName="cxtj"/></option>
											<option value="1" <%="1".equals(msType==null?"":msType)?"selected":"" %>><emp:message key="cxtj_sjcx_yystjbb_cx" defVal="彩信" fileName="cxtj"/></option>
										</select>
										
								</td>  
									<td class="tdSer">
												<center><a id="search" name="research"></a></center>
									</td>
								</tr>
								<tr>
									<td>
										<emp:message key="cxtj_sjcx_yystjbb_yys" defVal="运营商：" fileName="cxtj"></emp:message>
									</td>
									<td>
										<select name="spnumtype" id="spnumtype">
									     	<option value="0" <%if("0".equals(spnumtype)){out.print("selected"); } %>><emp:message key="cxtj_sjcx_zhtjbb_gn" defVal="国内" fileName="cxtj"></emp:message></option>
									     	<option value="1" <%if("1".equals(spnumtype)){out.print("selected"); } %>><emp:message key="cxtj_sjcx_zhtjbb_gw" defVal="国外" fileName="cxtj"></emp:message></option>
									     	<option value="-1" <%if("-1".equals(spnumtype)){out.print("selected"); } %>><emp:message key="cxtj_sjcx_yystjbb_qb" defVal="全部" fileName="cxtj"></emp:message></option>
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
										<emp:message key="cxtj_sjcx_zhtjbb_bblx" defVal="报表类型：" fileName="cxtj"></emp:message>
									</td>
									<td>
										<select name="reportType" id="reportType">
										<%if(reportType!=null) {
											String Strtype=reportType.toString();
										%>
									     <option value="0" <%if("0".equals(Strtype)){out.print("selected"); } %>><emp:message key="cxtj_sjcx_yystjbb_rbb" defVal="日报表" fileName="cxtj"></emp:message></option>
									     <option value="1" <%if("1".equals(Strtype)){out.print("selected"); } %>><emp:message key="cxtj_sjcx_yystjbb_ybb" defVal="月报表" fileName="cxtj"></emp:message></option>
									     <option value="2" <%if("2".equals(Strtype)){out.print("selected"); } %>><emp:message key="cxtj_sjcx_yystjbb_nbb" defVal="年报表" fileName="cxtj"></emp:message></option>
								     <%} else{%>
										<option value="0"><emp:message key="cxtj_sjcx_yystjbb_rbb" defVal="日报表" fileName="cxtj"></emp:message></option>
										<option value="1"><emp:message key="cxtj_sjcx_yystjbb_ybb" defVal="月报表" fileName="cxtj"></emp:message></option>
										<option value="2"><emp:message key="cxtj_sjcx_yystjbb_nbb" defVal="年报表" fileName="cxtj"></emp:message></option>
								     <%} %>
										</select>
									</td>
									<td>
										<label id="sjlabel"><emp:message key="cxtj_sjcx_yystjbb_tjsj" defVal="统计时间：" fileName="cxtj"></emp:message></label>
									</td>
									<td class="tableTime">

										<input type="text"
											class="Wdate" readonly="readonly" onclick="setime()"
											value="<%=request.getParameter("begintime") != null ? request.getParameter("begintime") : beginTime%>" id="sendtime"
											name="begintime">
											<input type="hidden" id="sTime" value="<%=beginTime %>"/>
									</td>
									<td>
										<span id="zhilabel"><emp:message key="cxtj_sjcx_yystjbb_z" defVal="至：" fileName="cxtj"></emp:message></span>
									</td>
									<td >
										<input type="text"
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
							   <th><%=sj %></th>
							   <th><%=czy %></th>
							   <th><%=jg %></th>
							   <th><%=yys %></th>
							   
							   <%
								List<RptConfInfo> rptConList = RptConfBiz.getRptConfMap().get(RptStaticValue.USER_RPT_CONF_MENU_ID);
								String temp = null;
								for(RptConfInfo rptConf : rptConList)
								{
									temp = rptConf.getName();
								%>
								<th><%=MessageUtils.extractMessage("cxtj",temp,request) %></th>
								<%} %>
							   
							   <th><emp:message key="cxtj_sjcx_yystjbb_xq" defVal="详情" fileName="cxtj"></emp:message></th>							 
							</tr>
						</thead>
						<tbody>
							<%
							//时间格式显示处理  eg：2012年12月12日 
							String btime = request.getParameter("begintime") != null ? request.getParameter("begintime") : beginTime;
							String etime = request.getParameter("endtime") != null ? request.getParameter("endtime") : endTime;
							String showTime = "";
							if( !"".equals(btime) && null != btime && 0 != btime.length())
							{
								String btemp[] = btime.split("-");
								if(btemp.length==1){
									//btime = btemp[0] + "年";
									btime = btemp[0];
									//btime = btemp[0] + MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_year", request);
								}else if(btemp.length==2){
									//btime = btemp[0] + "年" + btemp[1] + "月";
									btime = btemp[0] + "-" + btemp[1];
									//btime = btemp[0] + MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_year", request) + btemp[1] + MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_month", request);
								}else{
									//btime = btemp[0] + "年" + btemp[1] + "月" + btemp[2] + "日";
									btime = btemp[0] + "-" + btemp[1] + "-" + btemp[2];
									//btime = btemp[0] + MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_year", request) + btemp[1] + MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_month", request) + btemp[2] + MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_day", request);
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
									//etime = " - " + etemp[0] + "年" + etemp[1] + "月" + etemp[2] + "日";
									etime = "  "+MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_z", request)+"  "  + etemp[0] + "-" + etemp[1] + "-" + etemp[2];
									//etime = " - " + etemp[0] + MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_year", request) + etemp[1] + MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_month", request) + etemp[2] + MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_day", request);
								}
							}
							
							showTime = btime +  etime;
							session.setAttribute("showTime",showTime);
							
							if(isFirstEnter)
							{
							%>
							    <tr><td colspan="<%=5+rptConList.size() %>" align="center"><emp:message key="cxtj_sjcx_yystjbb_qdjcxhqsj" defVal="请点击查询获取数据" fileName="cxtj"></emp:message></td></tr>
											
							<%}else if(resultListt!=null&& resultListt.size()>0){
								for(int i=0;i<resultListt.size();i++){
									MtDataReportVo mdreportVo = resultListt.get(i);
								
							%>							
							<tr>
								<td><%=showTime%></td>
								<td class="textalign">
									<%=mdreportVo.getUserName()==null?MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_wz", request):mdreportVo.getUserName().replace("<","&lt;").replace(">","&gt;")%>
									<%if(mdreportVo.getUserName()!=null && mdreportVo.getUserState() != null && mdreportVo.getUserState()==2){out.print("<font color='red'>("+MessageUtils.extractMessage("cxtj", "cxtj_sjcx_jgtjbb_yzx", request)+")</font>");}%>
								</td>
								<td class="textalign"><xmp><%=mdreportVo.getDepName()==null?MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_wz", request):mdreportVo.getDepName().replace("<","&lt;").replace(">","&gt;")%>	</xmp></td>
								<td>
									<%
									if(spnumtype!=null&&"0".equals(spnumtype)){
										out.print(MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_gn", request));
									}else if(spnumtype!=null&&"1".equals(spnumtype)){
										out.print(MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_gw", request));
									}else if(spnumtype!=null&&"-1".equals(spnumtype)){
										out.print(MessageUtils.extractMessage("cxtj", "cxtj_sjcx_yystjbb_qb", request));
									}else{
										out.print("--");
									}
									%>
								</td>		
	 							
	 							<%
	 							//从公用方法中获取 计算结果map
	 							Map<String,String> map=ReportBiz.getRptNums(mdreportVo.getIcount(),mdreportVo.getRsucc(),mdreportVo.getRfail1(),
	 							mdreportVo.getRfail2(),mdreportVo.getRnret(),RptStaticValue.USER_RPT_CONF_MENU_ID);
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
	 								<a id="details"  onclick="dataReportfind('<%=mdreportVo.getSecretId()%>',1)"><emp:message key="cxtj_sjcx_yystjbb_ck" defVal="查看" fileName="cxtj"></emp:message></a>
	 								&nbsp;
	 								<% if(spnumtype!=null&&"1".equals(spnumtype)){ %>
	 									<a onclick="dataReportfind('<%=mdreportVo.getSecretId() %>',2)"><emp:message key="cxtj_sjcx_jgtjbb_ggxq" defVal="各国详情" fileName="cxtj"></emp:message></a>
	 								<% } %>	
	 							</td>
							</tr>
						<%} %>
							<tr>
						    <td colspan="4"><b><emp:message key="cxtj_sjcx_yystjbb_hj" defVal="合计：" fileName="cxtj"></emp:message></b></td>

								<%
	 							//从公用方法中获取 计算结果map
	 							Map<String,String> mapsum=ReportBiz.getRptNums(Long.valueOf(icount),Long.valueOf(rsucc),Long.valueOf(rfail1),
	 							Long.valueOf(rfail2),Long.valueOf(rnret),RptStaticValue.USER_RPT_CONF_MENU_ID);
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

						    <td><b>-</b></td>
						   </tr>	
						<%}else{						%>
						<tr>
							<td colspan="<%=5+rptConList.size() %>" align="center"><emp:message key="cxtj_sjcx_yystjbb_wjj" defVal="无记录" fileName="cxtj"></emp:message></td>
							
						</tr>
						<%} %>
												
						</tbody>
						
						<tfoot>
							<tr>
								<td colspan="<%=5+rptConList.size() %>">
								    <input type="hidden" name="pageTotalRec" id="pageTotalRec" value="<%=pageInfo.getTotalRec() %>"/>
								    <input type="hidden" name="queryTime" id="queryTime" value="<%=request.getParameter("begintime") == null ? "" : request.getParameter("begintime")%><%= MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_z", request)%><%=request.getParameter("endtime") == null ? "" : request.getParameter("endtime")%>"/>
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
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/cxtj_<%=langName%>.js"></script>
		<script type="text/javascript">
		
		var zTree3;
		var zTree2;		
		
		var setting3;
		var setting2;
		
		var deptArray=[];
		var userArray=[];

		//获取机构代码
		setting3 = {									
				async : true,				
				asyncUrl : "<%=path%>/rep_sysDepReport.htm?method=createDeptTree", //获取节点数据的URL地址
				
				//checkable : true,
			    //checkStyle : "radio",
			    //checkType : { "Y": "s", "N": "s" },
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
					//zTree3.expandAll(false);
					}
				}
		};

		//获取人员代码
		setting2 = {    			    
				async : true,
				asyncUrl :"<%=path%>/rep_sysUserReport.htm?method=createUserTree&lgcorpcode=<%=request.getParameter("lgcorpcode")%>&lguserid=<%=request.getParameter("lguserid")%>", //获取节点数据的URL地址
				
				checkable : true,
			    checkStyle : "checkbox",
			    checkType : { "Y": "s", "N": "s" },	
			    
				isSimpleData: true,
				rootPID : -1,
				treeNodeKey: "id",
				treeNodeParentKey: "pId",
				asyncParam: ["depId"],
				callback: {				  
					//beforeAsync: function(treeId, treeNode) {return false;},
					change: zTreeOnClick2,
					asyncSuccess:function(event, treeId, treeNode, msg){
						if(!treeNode){
						   var rootNode = zTree2.getNodeByParam("level", 0);
						   zTree2.expandNode(rootNode, true, false);
						}
					}
				}
		};
		
		var zNodes3 =[];
		var zNodes2 =[];

		//隐藏人员树形控件
		function showMenu() {
			hideMenu2();
			var sortSel = $("#depNam");
			var sortOffset = $("#depNam").offset();
			$("#dropMenu").toggle();
			//if($("#msType").is(":hidden")){
			//	$("#msType").show();
			//}else{
			//	$("#msType").hide();
			//}
		}
		
		//隐藏机构树形控件
		function showMenu2() {
			hideMenu();
			var sortSel = $("#userName");
			var sortOffset = $("#userName").offset();
			$("#dropMenu2").toggle();
		}
		
		//隐藏机构树形控件
		function hideMenu() {
			$("#dropMenu").hide();
		}
		
		
		//隐藏人员树形控件
		function hideMenu2() {
			$("#dropMenu2").hide();
		}
		

		
		//选中的机构显示文本框
		function zTreeOnClick3(event, treeId, treeNode) {
			if (treeNode) {
				$("#depNam").attr("value", treeNode.name); //设置机构属性
				$("#deptString").attr("value", treeNode.id); //设置机构代码	
				
				//if(zTreeNodes3.length==0){
				//	cleanSelect_dep();			
				//}
				
			}
			
		}	
		
		//选中的机构显示文本框
		function zTreeOnClickOK3() {
				hideMenu();
				//if(zTreeNodes3.length==0){
				//	cleanSelect_dep();				
				//}			
				//$("#msType").show();	
				cleanSelect_user();
		}	
		
		
		//选中的机构显示文本框
		/*function zTreeOnClickOK3() {
				hideMenu();
				
				var zTreeNodes3=zTree3.getChangeCheckedNodes();
				
				var pops="";
				
				for(var i=0; i<zTreeNodes3.length; i++){					
					pops+=zTreeNodes3[i].name+";";					
				}
				$("#depNam").attr("value", pops);	
				
				if(zTreeNodes3.length==0){
				
				 //$("#depNam").attr("value", "");	
					cleanSelect_dep();				
				}				
				//$("#userName").attr("value", "");
				cleanSelect_user();
		}*/
		
		
		//选中的人员显示文本框
		function zTreeOnClick2(event, treeId, treeNode) {
			if (treeNode) {				
				var zTreeNodes2=zTree2.getChangeCheckedNodes();
				
				var pops="";
				var params=[]; //获取人员字符串     				
				for(var i=0; i<zTreeNodes2.length; i++){				
					pops+=zTreeNodes2[i].name+";";
					params[i]=zTreeNodes2[i].id;	//人员编号     								
				}					
				$("#userName").attr("value", pops); //设置人员属性
				$("#userString").attr("value", params);	//设置人员代码
				
				if(zTreeNodes2.length==0){
				
				// $("#userName").attr("value", "");
				 cleanSelect_user();
				}
			}
			
		}
		
		//选中的人员显示文本框
		function zTreeOnClickOK2() {
			
			    hideMenu2();
				var zTreeNodes2=zTree2.getChangeCheckedNodes();
				
				var pops="";				
				for(var i=0; i<zTreeNodes2.length; i++){
				
					pops+=zTreeNodes2[i].name+";";					
				}					
				$("#userName").attr("value", pops);
				
				if(zTreeNodes2.length==0){
				
				 //$("#userName").attr("value", "");
				 cleanSelect_user();
				}
				//$("#depNam").attr("value", "");			
				//cleanSelect_dep();
		 }
		
   		// 加载人员/机构树形控件
		function reloadTree() {
			hideMenu();
			hideMenu2();
			
			zTree3 = $("#dropdownMenu").zTree(setting3, zNodes3);
			zTree2 = $("#dropdownMenu2").zTree(setting2, zNodes2);		
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


		
		
		$(document).ready(function(){
			getLoginInfo("#corpCode");
			closeTreeFun(["dropMenu"],[""]);
			closeTreeFun(["dropMenu2"],[""]);
		    setting2.expandSpeed = ($.browser.msie && parseInt($.browser.version)<=7)?"":"fast";
		    setting3.expandSpeed = ($.browser.msie && parseInt($.browser.version)<=7)?"":"fast";
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
					
					if("" == $.trim(judgeSendtime) || "" == $.trim(judgeendtime) )
					{
						//alert("请选择统计时间！");
						  alert(getJsLocaleMessage("cxtj","cxtj_sjcx_czytjbb_text_1"));
								
					}else
					{

						
					    //submitForm();//选择好时间段，才允许查询 
					    checkSubmit();
					}
			
			});
			
			function checkSubmit(){
			    if(checkSubmitFlag ==true){
			         submitForm();//选择好时间段，才允许查询 	
			         //$("a[name='research']").attr("title","您已经点过了查询按钮，请稍等!");	   
			         $("a[name='research']").attr("title",getJsLocaleMessage("cxtj","cxtj_sjcx_jgtjbb_text_5"));	      
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
				   		//List<MtDataReportVo> list=(List<MtDataReportVo>)r.getAttribute("resultList");
				   		if(resultListt!=null && resultListt.size()>0){
				   		%>			
					   		var queryTime=$("#queryTime").val();
					   		var sendtime = '<%=request.getParameter("begintime") != null ? request.getParameter("begintime") : beginTime%>';
					   		var recvtime = '<%=request.getParameter("endtime") != null ? request.getParameter("endtime") : endTime%>';
					   		var userName = '<%=request.getParameter("userString")==null ? "" :request.getParameter("userString")%>';
					   	    var depNam = '<%=request.getParameter("deptString")==null ? "" :request.getParameter("deptString")%>';
					   	    var mstype = '<%=msType==null?"":msType %>';
					   		var reportType=$("#hreportType").val();
					   		var spnumtype='<%=spnumtype==null?"":spnumtype %>';
					   		$.ajax({
									type: "POST",
									url: "<%=path%>/rep_sysUserReport.htm?method=r_suRptExportExcel",
									data: {
										lgcorpcode:'<%=request.getParameter("lgcorpcode")%>',
										lguserid:'<%=request.getParameter("lguserid")%>',
										queryTime:queryTime,
										begintime:sendtime,
										endtime:recvtime,
										userName:userName,
										depNam:depNam,
										mstype:mstype,
										reportType:reportType,
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
						                      download_href("<%=path%>/rep_sysUserReport.htm?method=downloadFile");
						                } else {
						                      //alert('导出失败！');
						                      alert(getJsLocaleMessage("cxtj","cxtj_sjcx_xtsxjl_text_3"));
						                      
						                }
			           				}
				 				});
				   			//location.href="<%=path%>/rep_sysUserReport.htm?method=r_suRptExportExcel&lgcorpcode=<%=request.getParameter("lgcorpcode")%>&lguserid=<%=request.getParameter("lguserid")%>&queryTime="+queryTime+"&begintime="+sendtime+"&endtime="+recvtime+"&userName="+userName+"&depNam="+depNam+"&mstype="+mstype+"&reportType="+reportType+"&spnumtype="+spnumtype;
				   		<%	
				   		}else{
				   		%>
				   		//alert("无数据可导出！");
				   		alert(getJsLocaleMessage("cxtj","cxtj_sjcx_xtsxjl_text_4"));
				   		<%
				   		}%>
				   }				 
			  });
		
			 if ($("#reportType").attr("value") == "1"){
					$("#recvtime").hide();
					$("#zhilabel").hide();
					//$("#sjlabel").text("统计年月");
					$("#sjlabel").text(getJsLocaleMessage("cxtj","cxtj_sjcx_jgtjbb_text_1"));
			}else if ($("#reportType").attr("value") == "2"){
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
		    

			$("#reportType").change(function(){
				var t = $("#sTime").attr("value");
				var r = $("#eTime").val();//结束时间
				if ($("#reportType").attr("value") == "1")
				{
					$("#sendtime").attr("value",t.toString().substring(0,7));
					$("#recvtime").attr("value",r.toString().substring(0,7));
					$("#recvtime").hide();
					$("#zhilabel").hide();
					//$("#sjlabel").text("统计年月");
					$("#sjlabel").text(getJsLocaleMessage("cxtj","cxtj_sjcx_jgtjbb_text_1"));
				}else if ($("#reportType").attr("value") == "2")
				{
					$("#sendtime").attr("value",t.toString().substring(0,4));
					$("#recvtime").attr("value",r.toString().substring(0,4));
					$("#recvtime").hide();
					$("#zhilabel").hide();
					//$("#sjlabel").text("统计年月");
					$("#sjlabel").text(getJsLocaleMessage("cxtj","cxtj_sjcx_jgtjbb_text_1"));
				}
				else
				{
					$("#sendtime").attr("value",t);
					$("#recvtime").attr("value",r);
					$("#recvtime").show();
					$("#zhilabel").show();
					//$("#sjlabel").text("统计时间");
					$("#sjlabel").text(getJsLocaleMessage("cxtj","cxtj_sjcx_jgtjbb_text_2"));
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

		function cleanSelect_user()
		{
		    var checkNodes = zTree2.getCheckedNodes();
		    for(var i=0;i<checkNodes.length;i++){
		    	checkNodes[i].checked=false;
		    }
		    zTree2.refresh();
			$('#userName').attr('value', '');
			//$('#userName').attr('value', '请选择');
			$('#userName').attr('value', getJsLocaleMessage("cxtj","cxtj_sjcx_grsjx_qxz"));
			$('#userString').attr('value', '');
			
		}

		function cleanSelect_dep()
		{
			//var checkNodes = zTree3.getCheckedNodes();
		   // for(var i=0;i<checkNodes.length;i++){
		   // 	checkNodes[i].checked=false;
		   // }
		   // zTree3.refresh();
			$('#depNam').attr('value', '');
			//$('#depNam').attr('value', '请选择');
			$('#depNam').attr('value', getJsLocaleMessage("cxtj","cxtj_sjcx_grsjx_qxz"));
			$('#deptString').attr('value', '');
		}
		
		function dataReportfind(id,type){
			var sendtime = '<%=request.getParameter("begintime") != null ? request.getParameter("begintime") : beginTime%>';
			var recvtime = '<%=request.getParameter("endtime") != null ? request.getParameter("endtime") : endTime%>';
			var mstype = '<%=msType==null?"":msType %>';
			var reporttype=$("#hreportType").val();
			var spnumtype='<%=spnumtype==null?"":spnumtype %>';
			var userString = '<%=request.getParameter("userString")==null ? "" :request.getParameter("userString")%>';
			var deptString = '<%=request.getParameter("deptString")==null ? "" :request.getParameter("deptString")%>';
			 var pageIndex='<%=request.getParameter("pageIndex")==null?"1":request.getParameter("pageIndex")%>';
	   	    var pageSize='<%=request.getParameter("pageSize")==null?"15":request.getParameter("pageSize")%>';
			var method="";
	   	    if(type==1){
	   	    	method="findInfoById";
	   	    }else{
	   	    	method="findAreaInfoById";
	   	    }
			location.href="<%=path%>/rep_sysUserReport.htm?method="+method+"&lgcorpcode=<%=request.getParameter("lgcorpcode")%>&lguserid=<%=request.getParameter("lguserid")%>&lgpageIndex="+pageIndex+"&lgpageSize="+pageSize+"&userString="+userString+"&deptString="+deptString+"&userid="+id+"&sendtime="+sendtime+"&endtime="+recvtime+"&mstype="+mstype+"&reporttype="+reporttype+"&spnumtype="+spnumtype;
		}  

		</script>
	</body>
</html>
