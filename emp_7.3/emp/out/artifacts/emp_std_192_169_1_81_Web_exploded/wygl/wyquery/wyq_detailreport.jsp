<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="com.montnets.emp.wyquery.bean.RptWyConfInfo"%>
<%@ page import="com.montnets.emp.wyquery.bean.RptWyStaticValue"%>
<%@ page import="com.montnets.emp.wyquery.biz.ReportWyBiz" %>
<%@page import="com.montnets.emp.wyquery.biz.RptWyConfBiz"%>
<%@page import="org.apache.commons.beanutils.DynaBean"%>
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
	 Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("statisticalReport");
	menuCode = menuCode==null?"0-0-0":menuCode;
	@ SuppressWarnings("unchecked")
	List<DynaBean> reportList=(List<DynaBean>)session.getAttribute("detailwyreportList");
	long[] sumCount=(long[])session.getAttribute("detailsumCount");
	//excel自动识别用
	Map<String,String> excelConditionMap = new java.util.LinkedHashMap<String,String>();
	//excelConditionMap.put("时间","imonth");
	excelConditionMap.put(MessageUtils.extractMessage("wygl","wygl_common_text49",request),"imonth");
	//excelConditionMap.put("通道号码","spagte");
	excelConditionMap.put(MessageUtils.extractMessage("wygl","wygl_common_text47",request),"spagte");
	//excelConditionMap.put("通道名称","gatename");
	excelConditionMap.put(MessageUtils.extractMessage("wygl","wygl_common_text48",request),"gatename");
	session.setAttribute("DETAIL_EXCE_MAP",excelConditionMap);
	java.util.Iterator<Map.Entry<String, String>> iter = null;
    Map.Entry<String, String> e = null;
    String reportType=request.getParameter("reportType");
    String findResult= (String)request.getAttribute("findresult");
    String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
    Boolean isFirstEnter=(Boolean)request.getAttribute("isFirstEnter");
    //String strshow="无记录";
    String strshow=MessageUtils.extractMessage("wygl","wygl_common_text87",request);
   if(isFirstEnter!=null&&isFirstEnter){
   		reportList=null;
   		//strshow="请点击查询获取数据 ";
   		strshow=MessageUtils.extractMessage("wygl","wygl_common_text29",request);
   }
    String txtPage = request.getParameter("pageIndex");
	String pageSize = request.getParameter("pageSize");
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
    
%>
<html>
<%@include file="/common/common.jsp" %>
	<head>
		<title><%=titleMap.get(menuCode) %></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<%}%>
		<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion() %>" />
	</head>

	<body>
		<div id="container" class="container">
			<%-- 当前位置 --%>
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(menuCode) %>
			
			<%-- 内容开始 --%>
			<%if(btnMap.get(menuCode+"-0")!=null) { %>
			<form name="pageForm" action="wyq_statisticalReport.htm?method=findInfoById" method="post" id="pageForm">
				   	<input type="hidden" id="reportType" name="reportType" value="<%=request.getParameter("reportType") == null ? "" : request.getParameter("reportType") %>" />
					<input type="hidden" id="begintime" name="begintime" value="<%=request.getParameter("begintime") == null ? "" : request.getParameter("begintime") %>" />
					<input type="hidden" id="endtime" name="endtime" value="<%=request.getParameter("endtime") == null ? "" : request.getParameter("endtime") %>" />
					<input type="hidden" id="spgate" name="spgate" value="<%=request.getParameter("spgate")==null?"":request.getParameter("spgate") %>" />
					<input type="hidden" id="lgspgate" name="lgspgate" value="<%=request.getParameter("lgspgate")==null?"":request.getParameter("lgspgate") %>" />
					<input type="hidden" id="lggatename" name="lggatename" value="<%=request.getParameter("lggatename")==null?"":request.getParameter("lggatename") %>" />
					<input type="hidden" id="lgspgate" name="lgpageindex" value="<%=request.getParameter("lgpageindex")==null?"":request.getParameter("lgpageindex") %>" />
					<input type="hidden" id="lggatename" name="lgpagesize" value="<%=request.getParameter("lgpagesize")==null?"":request.getParameter("lgpagesize") %>" />
					
				<div id="rContent" class="rContent">
						<div class="buttons">
							 <a id="exportCondition" ><emp:message key="wygl_common_text69" defVal="导出" fileName="wygl"></emp:message></a>
	                 	    <input type="hidden" id="menucode" value="24" name="menucode" />
	                 	    <input type="hidden" id="pageTotalRec" name="pageTotalRec" value="<%=pageInfo.getTotalRec() %>"/>
	                 	    <span id="backgo" class="right" style="display:inline;margin-right:10px;" onclick="javascript:back()"><emp:message key="wygl_common_text68" defVal="返回" fileName="wygl"></emp:message></span>
						</div>
						<table id="content" style="margin-top: 4px;">
							<thead>
							    <tr>
							  <%
								 iter = excelConditionMap.entrySet().iterator();
								 while (iter.hasNext()){
		 							e = iter.next();
							  %>
							  <th><%=e.getKey() %></th>
						      <% 
						    	}
						      %>
						      <%
						      	List<RptWyConfInfo> rptConList = RptWyConfBiz.getRptConfMap().get(RptWyStaticValue.WY_RPT_CONF_MENU_ID);
						      	for(RptWyConfInfo rptConf : rptConList){
						      %>
								<%--<th><%=rptConf.getName()%></th>--%>
								
									<%if("发送成功数".equals(rptConf.getName())){%>
										<th><emp:message key="wygl_common_text25" defVal="发送成功数" fileName="wygl"></emp:message>
									<%}else if("接收失败数".equals(rptConf.getName())){%>
										<th><emp:message key="wygl_common_text27" defVal="接收失败数" fileName="wygl"></emp:message>
									<%}else if("提交总数".equals(rptConf.getName())){%>
										<th><emp:message key="wygl_common_text101" defVal="提交总数" fileName="wygl"></emp:message>
									<%}else if("发送失败数".equals(rptConf.getName())){%>
										<th><emp:message key="wygl_common_text102" defVal="发送失败数" fileName="wygl"></emp:message>
									<%}else if("接收成功数".equals(rptConf.getName())){%>
										<th><emp:message key="wygl_common_text103" defVal="接收成功数" fileName="wygl"></emp:message>
									<%}else if("未返数".equals(rptConf.getName())){%>
										<th><emp:message key="wygl_common_text104" defVal="未返数" fileName="wygl"></emp:message>
									<%}else if("发送成功率".equals(rptConf.getName())){%>
										<th><emp:message key="wygl_common_text105" defVal="发送成功率" fileName="wygl"></emp:message>
									<%}else if("发送失败率".equals(rptConf.getName())){%>
										<th><emp:message key="wygl_common_text106" defVal="发送失败率" fileName="wygl"></emp:message>
									<%}else if("接收成功率".equals(rptConf.getName())){%>
										<th><emp:message key="wygl_common_text107" defVal="接收成功率" fileName="wygl"></emp:message>
									<%}else if("接收失败率".equals(rptConf.getName())){%>
										<th><emp:message key="wygl_common_text108" defVal="接收失败率" fileName="wygl"></emp:message>
									<%}else if("未返率".equals(rptConf.getName())){%>
										<th><emp:message key="wygl_common_text109" defVal="未返率" fileName="wygl"></emp:message>
									<%}else{%>
										<th><%=rptConf.getName()%></th>
									<%}%>
							  <%
								}
							  %>
						        </tr>
							</thead>
							<tbody>
							<%
								if (reportList != null && reportList.size() > 0){
								for(DynaBean report : reportList){
							%>
								<tr>								    
									<td>
										<%
											//SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月d日");
											SimpleDateFormat sdf =null;
											if(StaticValue.ZH_HK.equals(langName)){
												sdf = new SimpleDateFormat("yyyy-M-d");
											}else{
												sdf = new SimpleDateFormat("yyyy年M月d日");
											}
											SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
											if("2".equals(reportType)){
												//out.print((report.get("y")!=null?report.get("y").toString():"2015")+"年"+(report.get("imonth")!=null?report.get("imonth").toString():"01")+"月");
												out.print((report.get("y")!=null?report.get("y").toString():"2015")+MessageUtils.extractMessage("wygl","wygl_common_text97",request)+(report.get("imonth")!=null?report.get("imonth").toString():"01")+MessageUtils.extractMessage("wygl","wygl_common_text98",request));
											}else{
												out.print(sdf.format(sdf1.parse(report.get("iymd")!=null?report.get("iymd").toString():"20150101")));
											}
										%>
									</td>	
																	
									<td>
										<%--<xmp><%=report.get("spgate")==null?"未知":report.get("spgate").toString().toUpperCase()%></xmp>
										--%><xmp><%=report.get("spgate")==null?MessageUtils.extractMessage("wygl","wygl_common_text56",request):report.get("spgate").toString().toUpperCase()%></xmp>
									</td>
									<td><xmp><%=report.get("gatename")!=null?report.get("gatename").toString():"--"%></xmp></td>
									
									<%
									Map<String,String> map=ReportWyBiz.getRptNums(Long.parseLong(report.get("icount").toString()),
									Long.parseLong(report.get("rsucc").toString()),Long.parseLong(report.get("rfail1").toString()),
									Long.parseLong(report.get("rfail2").toString()),Long.parseLong(report.get("rnret").toString()),RptWyStaticValue.WY_RPT_CONF_MENU_ID);
									for(RptWyConfInfo rptConf : rptConList){
										if(RptWyStaticValue.RPT_ICOUNT_COLUMN_ID.equals(rptConf.getColId())){//提交总数
									%>
								    <td><%=report.get("icount")%></td>
								    <%
										}else if(RptWyStaticValue.RPT_FSSUCC_COLUMN_ID.equals(rptConf.getColId())){//发送成功数
									%>
									<td><%=map.get(RptWyStaticValue.RPT_FSSUCC_COLUMN_ID)!=null?map.get(RptWyStaticValue.RPT_FSSUCC_COLUMN_ID):0%></td>
									<%
										}else if(RptWyStaticValue.RPT_RFAIL1_COLUMN_ID.equals(rptConf.getColId())){//发送失败数
									%>
									<td><%=report.get("rfail1")%></td>
									<%
										}else if(RptWyStaticValue.RPT_RFAIL2_COLUMN_ID.equals(rptConf.getColId())){//接收失败数
									%>
									<td><%=report.get("rfail2")%></td>
									<%
										}else if(RptWyStaticValue.RPT_JSSUCC_COLUMN_ID.equals(rptConf.getColId())){//接收成功数
									%>
									<td><%=map.get(RptWyStaticValue.RPT_JSSUCC_COLUMN_ID)!=null?map.get(RptWyStaticValue.RPT_JSSUCC_COLUMN_ID):0%></td>
									<%
										}else if(RptWyStaticValue.RPT_RNRET_COLUMN_ID.equals(rptConf.getColId())){//未返数
									%>
									<td><%=report.get("rnret")%></td>
									<%
										}else if(RptWyStaticValue.RPT_FSSUCC_ICOUNT_COLUMN_ID.equals(rptConf.getColId())){//发送成功率
									%>
									<td><%=map.get(RptWyStaticValue.RPT_FSSUCC_ICOUNT_COLUMN_ID)!=null?map.get(RptWyStaticValue.RPT_FSSUCC_ICOUNT_COLUMN_ID):0%></td>
									<%
										}else if(RptWyStaticValue.RPT_RFAIL1_ICOUNT_COLUMN_ID.equals(rptConf.getColId())){//发送失败率
									%>
									<td><%=map.get(RptWyStaticValue.RPT_RFAIL1_ICOUNT_COLUMN_ID)!=null?map.get(RptWyStaticValue.RPT_RFAIL1_ICOUNT_COLUMN_ID):0%></td>
									<%
										}else if(RptWyStaticValue.RPT_JSSUCC_FSSUCC_COLUMN_ID.equals(rptConf.getColId())){//接收成功率
									%>
									<td><%=map.get(RptWyStaticValue.RPT_JSSUCC_FSSUCC_COLUMN_ID)!=null?map.get(RptWyStaticValue.RPT_JSSUCC_FSSUCC_COLUMN_ID):0%></td>
									<%
										}else if(RptWyStaticValue.RPT_RFAIL2_FSSUCC_COLUMN_ID.equals(rptConf.getColId())){//接收失败率
									%>
									<td><%=map.get(RptWyStaticValue.RPT_RFAIL2_FSSUCC_COLUMN_ID)!=null?map.get(RptWyStaticValue.RPT_RFAIL2_FSSUCC_COLUMN_ID):0%></td>
									<%
										}else if(RptWyStaticValue.RPT_RNRET_FSSUCC_COLUMN_ID.equals(rptConf.getColId())){//未返率
									%>
									<td><%=map.get(RptWyStaticValue.RPT_RNRET_FSSUCC_COLUMN_ID)!=null?map.get(RptWyStaticValue.RPT_RNRET_FSSUCC_COLUMN_ID):0%></td>
									<%
										}
									}
								%>
								</tr>
								<%
								}
								%>
								<tr>
								<td colspan="3"><b><emp:message key="wygl_common_text57" defVal="合计：" fileName="wygl"></emp:message></b></td>
								<%
								//从公用方法中获取 计算结果map
								Map<String,String> mapsum=ReportWyBiz.getRptNums(Long.valueOf(icount),Long.valueOf(rsucc),Long.valueOf(rfail1),
								Long.valueOf(rfail2),Long.valueOf(rnret),RptWyStaticValue.WY_RPT_CONF_MENU_ID);
								for(RptWyConfInfo rptConf : rptConList){
									if(RptWyStaticValue.RPT_ICOUNT_COLUMN_ID.equals(rptConf.getColId())){//提交总数
									%>
								<td><%=icount%></td>
								<%
									}
									else if(RptWyStaticValue.RPT_FSSUCC_COLUMN_ID.equals(rptConf.getColId()))
									{//发送成功数
								%>
								<td><%=mapsum.get(RptWyStaticValue.RPT_FSSUCC_COLUMN_ID)!=null?mapsum.get(RptWyStaticValue.RPT_FSSUCC_COLUMN_ID):0%></td>
								<%
									}
									else if(RptWyStaticValue.RPT_RFAIL1_COLUMN_ID.equals(rptConf.getColId()))
									{//发送失败数
								%>
								<td><%=rfail1%></td>
								<%
									}
									else if(RptWyStaticValue.RPT_RFAIL2_COLUMN_ID.equals(rptConf.getColId()))
									{//接收失败数
								%>
								<td><%=rfail2%></td>
								<%
									}
									else if(RptWyStaticValue.RPT_JSSUCC_COLUMN_ID.equals(rptConf.getColId()))
									{//接收成功数
								%>
								<td><%=mapsum.get(RptWyStaticValue.RPT_JSSUCC_COLUMN_ID)!=null?mapsum.get(RptWyStaticValue.RPT_JSSUCC_COLUMN_ID):0%></td>
								<%
									}
									else if(RptWyStaticValue.RPT_RNRET_COLUMN_ID.equals(rptConf.getColId()))
									{//未返数
								%>
								<td><%=rnret%></td>
								<%
									}
									else if(RptWyStaticValue.RPT_FSSUCC_ICOUNT_COLUMN_ID.equals(rptConf.getColId()))
									{//发送成功率
								%>
								<td><%=mapsum.get(RptWyStaticValue.RPT_FSSUCC_ICOUNT_COLUMN_ID)!=null?mapsum.get(RptWyStaticValue.RPT_FSSUCC_ICOUNT_COLUMN_ID):0%></td>
								<%
									}
									else if(RptWyStaticValue.RPT_RFAIL1_ICOUNT_COLUMN_ID.equals(rptConf.getColId()))
									{//发送失败率
								%>
								<td><%=mapsum.get(RptWyStaticValue.RPT_RFAIL1_ICOUNT_COLUMN_ID)!=null?mapsum.get(RptWyStaticValue.RPT_RFAIL1_ICOUNT_COLUMN_ID):0%></td>
								<%
									}
									else if(RptWyStaticValue.RPT_JSSUCC_FSSUCC_COLUMN_ID.equals(rptConf.getColId()))
									{//接收成功率
								%>
								<td><%=mapsum.get(RptWyStaticValue.RPT_JSSUCC_FSSUCC_COLUMN_ID)!=null?mapsum.get(RptWyStaticValue.RPT_JSSUCC_FSSUCC_COLUMN_ID):0%></td>
								<%
									}
									else if(RptWyStaticValue.RPT_RFAIL2_FSSUCC_COLUMN_ID.equals(rptConf.getColId()))
									{//接收失败率
								%>
								<td><%=mapsum.get(RptWyStaticValue.RPT_RFAIL2_FSSUCC_COLUMN_ID)!=null?mapsum.get(RptWyStaticValue.RPT_RFAIL2_FSSUCC_COLUMN_ID):0%></td>
								<%
									}
									else if(RptWyStaticValue.RPT_RNRET_FSSUCC_COLUMN_ID.equals(rptConf.getColId()))
									{//未返率
								%>
								<td><%=mapsum.get(RptWyStaticValue.RPT_RNRET_FSSUCC_COLUMN_ID)!=null?mapsum.get(RptWyStaticValue.RPT_RNRET_FSSUCC_COLUMN_ID):0%></td>
								<%}
								} %> 
								</tr>
								<%}else{%> 
								<tr>
									<td colspan="<%=5+rptConList.size() %>" align="center"><%=strshow %></td>
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
<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=commonPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=commonPath %>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript">

		function back(){
			var pageIndex='<%=request.getParameter("lgpageindex")==null?"":request.getParameter("lgpageindex")%>';
			var pageSize='<%=request.getParameter("lgpagesize")==null?"":request.getParameter("lgpagesize")%>';
	   	    $("#pageForm").attr("action","wyq_statisticalReport.htm?method=find&spgate=<%=request.getParameter("lgspgate")%>&gatename=<%=request.getParameter("lggatename") %>&pageIndex="+pageIndex+"&pageSize="+pageSize+"&lgcorpcode=<%=request.getParameter("lgcorpcode")%>&reportType=<%=request.getParameter("reportType")%>");
			submitForm();
			//location.href="<%=path%>/wyq_statisticalReport.htm?method=find&pageIndex="+pageIndex+"&pageSize="+pageSize+"&lgcorpcode=<%=request.getParameter("lgcorpcode")%>&reportType=<%=request.getParameter("reportType")%>";
		}

		$(document).ready(function(){
			getLoginInfo("#corpCode");
			var findresult="<%=findResult%>";
		    if(findresult=="-1")
		    {
		       //alert("加载页面失败,请检查网络是否正常!");	
		       alert(getJsLocaleMessage("wygl","wygl_common_text1"));	
		       //window.history.go(-1);
		       return;			       
		    }
		
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
				  if(confirm(getJsLocaleMessage("wygl","wygl_common_text8")))
				   {
				   		var langName ='<%=langName %>';	
				   		var spgate='<%=request.getParameter("spgate")==null?"":request.getParameter("spgate") %>';
				   		var begintime = '<%=request.getParameter("begintime") != null ? request.getParameter("begintime") : beginTime%>';
				   		var recvtime = '<%=request.getParameter("endtime") != null ? request.getParameter("endtime") : endTime%>';
				   		var reportType = $("#reportType").val();
				   		var lgcorpcode='<%=request.getParameter("lgcorpcode")!=null?request.getParameter("lgcorpcode"):"" %>'
				   		<%
				   		if(reportList!=null && reportList.size()>0){
				   		%>			
				   				$.ajax({
									type: "POST",
									url: "<%=path%>/wyq_statisticalReport.htm?method=r_smRptDetailExportExcel",
									data: {
										langName:langName,
										spgate:spgate,
										reportType:reportType,
										begintime:begintime,
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
						                        download_href("<%=path%>/wyq_statisticalReport.htm?method=detaildownloadFile");
						                    } else {
						                        //alert('导出失败！');
						                        alert(getJsLocaleMessage("wygl","wygl_common_text5"));
						                    }
			           				}
				 				});
				   			//location.href="<%=path%>/wyq_statisticalReport.htm?method=r_smRptDetailExportExcel&spgate="+spgate+"&reportType="+reportType+"&begintime="+begintime+"&endtime="+recvtime+"&lgcorpcode="+lgcorpcode;
				   		<%	
				   		}else{
				   		%>
				   		//alert("无数据可导出！");
				   		alert(getJsLocaleMessage("wygl","wygl_common_text4"));
				   		<%
				   		}%>
				   }				 
			  });		
			
			
		});
	
</script>
<script type="text/javascript" src="<%=iPath %>/js/wyq_report.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/wygl_<%=langName%>.js"></script>
	
	</body>
</html>
