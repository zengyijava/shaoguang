<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.entity.biztype.LfBusManager"%>
<%@ page import="com.montnets.emp.entity.system.LfPageField"%>
<%@page import="com.montnets.emp.i18n.util.MessageUtils"%>
<%@page import="com.montnets.emp.report.bean.RptConfInfo"%>
<%@page import="com.montnets.emp.report.bean.RptStaticValue"%>
<%@ page
        import="com.montnets.emp.report.biz.ReportBiz" %>
<%@ page
        import="com.montnets.emp.report.biz.RptConfBiz" %>
<%@ page
        import="com.montnets.emp.report.vo.BusReportVo" %>
<%@ page
        import="com.montnets.emp.util.PageInfo" %>
<%@ page
        import="java.text.SimpleDateFormat" %>
<%@page import="java.util.Calendar"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%@page import="java.util.HashMap"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>

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
	

	long[] sumArray = (long[]) session.getAttribute("bus_sumArray");

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
	List<BusReportVo> resultList = (List<BusReportVo>) request.getAttribute("resultList");
	@SuppressWarnings("unchecked")
	List<LfBusManager> busList=(List<LfBusManager>)request.getAttribute("busList");
	@SuppressWarnings("unchecked")
	List<LfPageField> pagefileds=(List<LfPageField>)session.getAttribute("pagefiledusers");
	String menuCode = titleMap.get("busReport");
	menuCode = menuCode == null ? "0-0-0" : menuCode;
	String msType =(String)request.getParameter("msType");
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	String counTime = "";
	if(request.getAttribute("countTime")!=null){
		counTime=request.getAttribute("countTime").toString();
	}
	 String reportType=""; 
	   if(request.getAttribute("reportType")!=null){
		   reportType=request.getAttribute("reportType").toString();
	   }
		String excelURL="";
		if(request.getAttribute("excelURL")!=null){
			excelURL=request.getAttribute("excelURL").toString();
		  }
	
	//业务类型详细信息
	String ywlxxxxx = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_ywlxxxxx", request);
  	//业务类型各国发送详细信息
	String ywlxggfsxxxx = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_ywlxggfsxxxx", request);
	
	
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
	<body class="rep_busDetailReport">
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
				
				<form name="pageForm" action="rep_busReport.htm?method=detailInfo"  method="post" id="pageForm">
  					<input type="hidden"  id="sp" name="sp" value="<%=request.getAttribute("sp")==null ? "" :request.getAttribute("sp")%>"/>      					
					<div class="buttons">
					<span id="backgo" class="right mr5" onclick="showback()">&nbsp;<emp:message key="cxtj_sjcx_report_fh" defVal="返回" fileName="cxtj"></emp:message></span>
				 	   <a id="exportCondition" ><emp:message key="cxtj_sjcx_report_dc" defVal="导出" fileName="cxtj"></emp:message></a>
					</div>

					<input type="hidden" name="msType" value="<%=request.getParameter("msType")%>" />
					<input type="hidden" name="lgcorpcode" value="<%=request.getParameter("lgcorpcode")%>" />
					<input type="hidden" name="reportType" value="<%=request.getParameter("reportType")%>" />
					<input type="hidden" name="countTime" value="<%=request.getParameter("countTime")%>" />
					<input type="hidden" name="begintime" value="<%=request.getParameter("begintime")%>" />
					<input type="hidden" name="endtime" value="<%=request.getParameter("endtime")%>" />
					<%-- 原来界面上输入的值   --%>
					<input type="hidden" name="bustype" value="<%=request.getParameter("bustype")==null?"":request.getParameter("bustype")%>" />
					<input type="hidden" name="datasourcetype" value="<%=request.getParameter("datasourcetype")%>" />
					<input type="hidden" name="spisuncm" value="<%=request.getParameter("spisuncm")%>" />
					<table id="content">
						<thead>
							<tr>
							   <th><emp:message key="cxtj_sjcx_report_sj" defVal="时间" fileName="cxtj"></emp:message></th>
							   <th><emp:message key="cxtj_sjcx_report_ywlx" defVal="业务类型" fileName="cxtj"></emp:message></th>
							   <th><emp:message key="cxtj_sjcx_report_fslx" defVal="发送类型" fileName="cxtj"></emp:message></th>
                                <%
                                    List<RptConfInfo> rptConList = RptConfBiz.getRptConfMap().get(RptStaticValue.BUS_RPT_CONF_MENU_ID);
                                    //总列数
                                    int cols = 3+rptConList.size();
                                    String temp = null;
                                    for(RptConfInfo rptConf : rptConList)
                                    {
                                    temp = rptConf.getName();
                                %>
                                <th><%=MessageUtils.extractMessage("cxtj",temp,request) %></th>
                                <%  } %>
							</tr>
						</thead>
						<tbody>
							<%
							if(resultList!=null&& resultList.size()>0){
                                Map<String,String> sumCountMap = new HashMap<String, String>();
								for(int i=0;i<resultList.size();i++){
									BusReportVo busreportVo = resultList.get(i);
								
							%>							
							<tr>

								<td>
								<%
								if("1".equals(reportType)){//如果选择的是年
									if(busreportVo.getY()!=null&&busreportVo.getImonth()!=null){
										//out.println(busreportVo.getY()+MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_y", request)+busreportVo.getImonth()+MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_m", request));
										out.println(busreportVo.getY()+"-"+busreportVo.getImonth());
									}else if(busreportVo.getY()!=null){
										//out.println(busreportVo.getY()+MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_y", request));
										out.println(busreportVo.getY());
									}else{
										out.println("-");
									}
								}else if(busreportVo.getIymd()!=null){
									String date=busreportVo.getIymd()+"";
									//out.println(date.substring(0,4)+MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_y", request)+date.substring(4,6)+MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_m", request)+date.substring(6,8)+MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_d", request));
									out.println(date.substring(0,4)+"-"+date.substring(4,6)+"-"+date.substring(6,8));
								}else {
									out.println("-");
								}

								%>
								
								</td>
								<td class="textalign">
									<xmp><%=busreportVo.getBusName()==null?"-":busreportVo.getBusName().replace("<","&lt;").replace(">","&gt;")%></xmp>
								</td>
								<td class="textalign">
								<%
								if("0".equals(request.getParameter("datasourcetype"))){ 
								%>
									<emp:message key="cxtj_sjcx_report_qb" defVal="全部" fileName="cxtj"></emp:message>
								<%
								}else if("1".equals(request.getParameter("datasourcetype"))){
								%>
								<emp:message key="cxtj_sjcx_report_empfs" defVal="EMP发送" fileName="cxtj"></emp:message>
								<%
								}else if("2".equals(request.getParameter("datasourcetype"))){
								%>
								<emp:message key="cxtj_sjcx_report_httpjr" defVal="HTTP接入" fileName="cxtj"></emp:message>
								<%
								}else if("3".equals(request.getParameter("datasourcetype"))){
								%>
								<emp:message key="cxtj_sjcx_report_dbjr" defVal="DB接入" fileName="cxtj"></emp:message>
								<%
								}else if("4".equals(request.getParameter("datasourcetype"))){
								%>
								<emp:message key="cxtj_sjcx_report_zljr" defVal="直连接入" fileName="cxtj"></emp:message>
								<%
								}else{
								 %>
								 --
								 <%
								 } 
								 %>
								</td>
                                <%
                                    sumCountMap = ReportBiz.getRptNums(busreportVo.getIcount(),busreportVo.getRsucc(),busreportVo.getRfail1(),busreportVo.getRfail2(),busreportVo.getRnret(),RptStaticValue.BUS_RPT_CONF_MENU_ID);
                                    for(RptConfInfo rptConf : rptConList)
                                    {
                                %>
                                <td><%=sumCountMap.get(rptConf.getColId())%></td>
                                <%
                                    }
                                %>
							</tr>
						<%} %>
							<tr>
						    <td colspan="3"><b><emp:message key="cxtj_sjcx_report_hj" defVal="合计：" fileName="cxtj"></emp:message></b></td>
                            <%
                                if(sumArray != null && sumArray.length >=5)
                                {
                                    sumCountMap = ReportBiz.getRptNums(sumArray[0],sumArray[1],sumArray[2],sumArray[3],sumArray[4],RptStaticValue.BUS_RPT_CONF_MENU_ID);
                                }
                                for(RptConfInfo rptConf : rptConList)
                                {
                            %>
                            <td><%=sumCountMap.get(rptConf.getColId())%></td>
                            <%
                                }
                            %>
						    </tr>	
						<%}else{						%>
						<tr>
							<td colspan="<%=cols%>" align="center"><emp:message key="cxtj_sjcx_report_wjl" defVal="无记录" fileName="cxtj"></emp:message></td>
							
						</tr>
						<%} %>
												
						</tbody>
						
						<tfoot>
							<tr>
								<td colspan="<%=cols%>">
								    <input type="hidden" name="pageTotalRec" id="pageTotalRec" value="<%=pageInfo.getTotalRec() %>"/>
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
  		<div id="tempDiv" title="<%=ywlxxxxx %>">
			<iframe id="contentFrame" name="contentFrame" marginwidth="0" scrolling="no" frameborder="no" src ="" ></iframe>
		</div>
		<div id="sendInfoDiv" title="<%=ywlxggfsxxxx %>">
			<iframe id="sendInfoFrame" name="sendInfoFrame" marginwidth="0" scrolling="no" frameborder="no" src ="" ></iframe>
		</div>
		<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript"	src="<%=commonPath %>/common/js/jquery.ztree-myjquery-b.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/cxtj_<%=langName%>.js"></script>	
	
		<script type="text/javascript">

		$(document).ready(function(){
		getLoginInfo("#corpCode");
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

			//导出全部数据到excel
			$("#exportCondition").click(
			 function()
			 {
				
				 // if(confirm("确定要导出数据到excel?"))
				 if(confirm(getJsLocaleMessage("cxtj","cxtj_sjcx_report_text_1")))
				   {
				   		<%
				   		if(resultList!=null && resultList.size()>0){
				   		%>		
					   		var excelURL="<%=excelURL%>";
					   		if(excelURL==""){
							       //alert("加载页面失败,请检查网络是否正常!");	
							       alert(getJsLocaleMessage("cxtj","cxtj_sjcx_report_text_2"));	
							       return;		
						   	}	
						   	
						   	$.ajax({
									type: "POST",
									url: "<%=path%>/rep_busReport.htm?method=r_busRptExportExcel&"+excelURL,
						            beforeSend:function () {
						                page_loading();
						            },
						            complete:function () {
						           	  	page_complete()
						            },
						            success:function (result) {
						                if (result == 'true') {
						                       download_href("<%=path%>/rep_busReport.htm?method=busdetail_downloadFile");
						                } else {
						                      //alert('导出失败！');
						                      alert(getJsLocaleMessage("cxtj","cxtj_sjcx_report_text_3"));
						                }
			           				}
			           		});
						   	
				   			//location.href="<%=path%>/rep_busReport.htm?method=r_busRptExportExcel&"+excelURL;
				   		
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
		function numberControl(va)
		{
			var pat=/^\d*$/;
			if(!pat.test(va.val()))
			{
				va.val(va.val().replace(/[^\d]/g,''));
			}
		}
		function showback(){
			$("#pageForm").attr("action","rep_busReport.htm?method=find&isback=1");
			 submitForm();
			//location.href="<%=path%>/rep_busReport.htm?method=find&lguserid=<%=request.getParameter("lguserid")%>&lgcorpcode=<%=request.getParameter("lgcorpcode")%>";
		}
			</script>
	</body>
</html>
