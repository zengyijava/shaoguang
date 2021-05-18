<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.entity.passage.XtGateQueue"%>
<%@ page import="com.montnets.emp.entity.system.LfPageField"%>
<%@page import="com.montnets.emp.i18n.util.MessageUtils"%>
<%@page import="com.montnets.emp.report.bean.RptConfInfo"%>
<%@page import="com.montnets.emp.report.bean.RptStaticValue"%>
<%@page import="com.montnets.emp.report.biz.ReportBiz"%>
<%@ page import="com.montnets.emp.report.biz.RptConfBiz"%>
<%@ page import="com.montnets.emp.report.vo.BusNationtVo"%>
<%@page import="com.montnets.emp.util.PageInfo"%>
<%@ page
        import="java.net.URLDecoder" %>
<%@ page
        import="java.text.SimpleDateFormat" %>
<%@ page
        import="java.util.Calendar" %>
<%@ page
        import="java.util.HashMap" %>
<%@ page
        import="java.util.List" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%@page import="java.util.Map"%>

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
List<BusNationtVo> reportList=(List<BusNationtVo>)request.getAttribute("resultList");
@ SuppressWarnings("unchecked")
List<String> userList = (List<String>)request.getAttribute("spUserList");
List<String> mmsUserList = (List<String>)request.getAttribute("mmsUserList");
List<LfPageField> pagefileds=(List<LfPageField>)session.getAttribute("pagefiledsps");
String msType =(String)request.getAttribute("msType");
@ SuppressWarnings("unchecked")
List<XtGateQueue> xtList = (List<XtGateQueue>)session.getAttribute("mrXtList");

long[] sumArray = (long[]) session.getAttribute("bus_nation_sumArray");

String counTime = "";
if(request.getAttribute("countTime")!=null){
	counTime=request.getAttribute("countTime").toString();
}
    
    String findResult= (String)request.getAttribute("findresult");
    String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
    
    String corpcode = (String)request.getAttribute("corpcode");
   //String strshow="无记录";
    String strshow=MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_wjl", request);
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
	  
//国家名称
String gjmc = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_gjmc", request);
    if(gjmc!=null&&gjmc.length()>1){
    	gjmc = gjmc.substring(0,gjmc.length()-1);
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
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link href="<%=commonPath%>/cxtj/common/css/inbox.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
	</head>

	<body class="rep_busReportNation">
		<div id="container" class="container">
			<%-- 当前位置 --%>
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode) %>
			
			<%-- 内容开始 --%>
			<%if(btnMap.get(menuCode+"-0")!=null) { %>
			<form name="pageForm" action="rep_busReport.htm?method=busNation" method="post" id="pageForm">
				<div id="rContent" class="rContent">
						<div class="buttons">
						<div id="toggleDiv" >
							</div>
							<span id="backgo" class="right mr5" onclick="showback()">&nbsp;<emp:message key="cxtj_sjcx_report_fh" defVal="返回" fileName="cxtj"></emp:message></span>
							 <a id="exportCondition" ><emp:message key="cxtj_sjcx_report_dc" defVal="导出" fileName="cxtj"></emp:message></a>
	                 	    <input type="hidden" id="menucode" value="24" name="menucode" />
	                 	    <input type="hidden" id="pageTotalRec" name="pageTotalRec" value="<%=pageInfo.getTotalRec() %>"/>
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
                                  <th><emp:message key="cxtj_sjcx_report_sj" defVal="记录类型" fileName="cxtj"></emp:message></th>
                                  <th><emp:message key="cxtj_sjcx_report_gjdqdm" defVal="国家/地区代码" fileName="cxtj"></emp:message></th>
                                  <th><%=gjmc %></th>
                                  <th><emp:message key="cxtj_sjcx_report_ywlx" defVal="业务类型" fileName="cxtj"></emp:message></th>
                                  <th><emp:message key="cxtj_sjcx_report_tdhm" defVal="通道号码" fileName="cxtj"></emp:message></th>
                                  <th><emp:message key="cxtj_sjcx_report_tdmc" defVal="通道名称" fileName="cxtj"></emp:message></th>
                                  <th><emp:message key="cxtj_sjcx_report_fslx" defVal="发送类型" fileName="cxtj"></emp:message></th>
                                    <%
                                        List<RptConfInfo> rptConList = RptConfBiz.getRptConfMap().get(RptStaticValue.BUS_RPT_CONF_MENU_ID);
                                        //总列数
                                        int cols = 7+rptConList.size();
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
								if (reportList != null && reportList.size() > 0)
								{
                                    Map<String,String> sumCountMap = new HashMap<String, String>();
									for(BusNationtVo report : reportList)
									{
							%>
								<tr>								    
									<% 
									String btime = request.getParameter("begintime") != null ? request.getParameter("begintime") : beginTime;
									String etime = request.getParameter("endtime") != null ? request.getParameter("endtime") : endTime;
									String showTime = "";
									if("2".equals(reportType)){
									if( !"".equals(btime) && null != btime && 0 != btime.length())
									{
										String btemp[] = btime.split("-");
										
										btime = btemp[0]+MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_y", request)+btemp[1]+MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_m", request)+btemp[2]+MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_d", request);
								 	 		 
									}
									
									if( !"".equals(etime) && null != etime && 0 != etime.length() )
									{
										String etemp[] = etime.split("-");
										
										etime = etemp[0]+MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_y", request)+etemp[1]+MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_m", request)+etemp[2]+MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_d", request);
									}
									showTime = btime+" - "+etime;
									
									}else if("0".equals(reportType)){
										if(counTime!=null){
											String time[]=counTime.split("-");
											showTime=time[0]+MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_y", request)+time[1]+MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_m", request);
										}else{
											showTime=MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_wz", request);
										}
									}else {
										if(counTime!=null){
											String time[]=counTime.split("-");
											showTime=time[0]+MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_y", request);
										}else{
											showTime=MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_wz", request);
										}
									}
									%>
										
										<td><%=showTime%></td>							
									<td>
										<xmp><%=report.getNationcode()==null?"-":report.getNationcode()%></xmp>
									</td>
									<td><xmp><%=report.getNationname()!=null?MessageUtils.extractMessage("cxtj","cxtj_country_"+report.getNationcode(),request) :"-" %></xmp></td>
									<td>
										<xmp><%=report.getBusName()==null?"-":report.getBusName()%></xmp>
									</td>
									<td>
										<xmp><%=report.getSpgatecode()==null?"-":report.getSpgatecode()%></xmp>
									</td>
									<td>
										<xmp><%=report.getSpgatename()==null?"-":report.getSpgatename()%></xmp>
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
                                        sumCountMap = ReportBiz.getRptNums(report.getIcount(),report.getRsucc(),report.getRfail1(),report.getRfail2(),report.getRnret(),RptStaticValue.BUS_RPT_CONF_MENU_ID);
                                        for(RptConfInfo rptConf : rptConList)
                                        {
                                    %>
                                    <td><%=sumCountMap.get(rptConf.getColId())%></td>
                                    <%
                                        }
                                    %>
								</tr>
								<%
								}
								%>
								<tr>
									<td colspan="7"><b><emp:message key="cxtj_sjcx_report_hj" defVal="合计：" fileName="cxtj"></emp:message></b></td>
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
								<%}else{
							%> 
							<tr>
									<td colspan="<%=cols%>" align="center"><%=strshow %></td>
									
								</tr>
							<%} %>
							
							</tbody>
							<tfoot>
								<tr>
									<td colspan="<%=cols%>">
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
<script type="text/javascript" src="<%=commonPath %>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
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
		      // alert("加载页面失败,请检查网络是否正常!");	
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
				location.href="<%=path%>/rep_busReport.htm?method=busNation&nationCode="+nationCode+"&nationName="+nationName+"&"+excelURL;
		});
			//导出全部数据到excel
			$("#exportCondition").click(
			 function()
			 {

				  //if(confirm("确定要导出数据到excel?"))
				 if(confirm(getJsLocaleMessage("cxtj","cxtj_sjcx_report_text_1")))
				   {
				   		<%
				   			
				   		if(reportList!=null && reportList.size()>0){
				   		%>
				   		if(excelURL==""){
						       //alert("加载页面失败,请检查网络是否正常!");	
						       alert(getJsLocaleMessage("cxtj","cxtj_sjcx_report_text_2"));	
						       return;		
					   	}
						var nationCode=$("#nationCode").val();
						var nationName=$("#nationName").val();
						//nationName=encodeURI(encodeURI(nationName));	
						$.ajax({
							type: "POST",
							url: "<%=path%>/rep_busReport.htm?method=r_busNationExcel&"+excelURL,
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
						            download_href("<%=path%>/rep_busReport.htm?method=busnation_downloadFile");
						        } else {
						            //alert('导出失败！');
						            alert(getJsLocaleMessage("cxtj","cxtj_sjcx_report_text_3"));
						        }
			           		}
			           	});
								
				   		//location.href="<%=path%>/rep_busReport.htm?method=r_busNationExcel&"+excelURL+"&nationCode="+nationCode+"&nationName="+nationName;
				   		
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
			$("#pageForm").attr("action","rep_busReport.htm?method=find&isback=1");
			 submitForm();
			//location.href="<%=path%>/rep_busReport.htm?method=find&lguserid=<%=request.getParameter("lguserid")%>&lgcorpcode=<%=request.getParameter("lgcorpcode")%>";
		}
</script>
	</body>
</html>
