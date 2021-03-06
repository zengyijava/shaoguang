<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>
<%@page import="com.montnets.emp.entity.monitorsys.MmonBufinfo"%>
<%@page import="com.montnets.emp.entity.monitorsys.MmonSysinfo"%>
<%@ page import="com.montnets.emp.common.constant.StaticValue"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String iPath = request.getRequestURI().substring(0,
			request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0, iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	MmonBufinfo bufinfo = (MmonBufinfo) request.getAttribute("bufinfo");
	MmonSysinfo sysinfo = (MmonSysinfo) request.getAttribute("sysinfo");
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	@SuppressWarnings("unchecked")
	Map<String, String> titleMap = (Map<String, String>) session.getAttribute("titleMap");
	String rTitle = (String)request.getAttribute("rTitle");
	String menuCode = titleMap.get(rTitle);
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<title></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=skin%>/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=skin%>/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/table_detail.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<style>
		#content tr td {
			font-size: 12px;
			text-align: left;
			height: 19px;
			border: #ddf0f3 1px solid;
			word-break: break-all;
			left-margin: 2px;
		}
		
		#content table {
			padding-left: 5px;
		}
		</style>
	</head>
	<body>
		<div id="container" class="container">
			<%-- header?????? --%>
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(menuCode) %>
			<%-- header?????? --%>
			<%-- ???????????? --%>
			<form name="pageForm"
				action="mon_systemRuntimerMonitor.htm?method=find" method="post"
				id="pageForm">
				<div id="rContent" class="rContent" style="padding:5px 0;">
					<div style="padding-top:5px;padding-bottom:5px;padding-left:5px;">
					<table id="content">
						<thead>
							<tr>
								<th>?????????</th>
								<th>??????MO</th>
								<th>??????MT</th>
								<th colspan="2">MO????????????</th>
								<th colspan="2">MO????????????</th>
								<th>????????????</th>
								<th>MT????????????</th>
								<th>??????</th>
							</tr>
						</thead>
						<tbody>
						<%
								if (bufinfo != null) {
							%>
						 <tr>
								<td><div align="center"><%=bufinfo.getEndCnt()%></div></td>
								<td><div align="center"><%=bufinfo.getMoTotalRecv()%></div></td>
								<td><div align="center"><%=bufinfo.getMtTotalSnd()%></div></td>
								<td colspan="2"><div align="center"><%=bufinfo.getWrMoBuf()%></div></td>
								<td colspan="2"><div align="center"><%=bufinfo.getUpDmoBuf()%></div></td>
								<td><div align="center"><%=bufinfo.getEndRspBuf()%></div></td>
								<td><div align="center"><%=bufinfo.getSmTsndBuf()%></div></td>
								<td><div align="center"><%=bufinfo.getMtWaitBuf()%></div></td>
							</tr>
							<%}else{
							 %>
							 <tr>
								<td colspan="10"><div align="center">?????????</div></td>
							</tr>
							 <%} %>
					    </tbody>
					    </table>
					    <br>
					    <table  id="content">
					    <thead>
							<tr>
<%--								<th>?????????</th>--%>
<%--								<th>??????MT</th>--%>
<%--								<th>??????MO</th>--%>
<%--								<th>???MTTASK??????</th>--%>
<%--								<th>???MTTMR??????</th>--%>
<%--								<th>???MTVFY??????</th>--%>
<%--								<th>???MTLVL??????</th>--%>
<%--								<th>????????????</th>--%>
<%--								<th>MO????????????</th>--%>
<%--								<th>??????</th>--%>
								
								<th style="width: 109px;">?????????</th>
								<th style="width: 129px;">??????MT</th>
								<th style="width: 124px;">??????MO</th>
								<th style="width: 100px;">???MTTASK??????</th>
								<th style="width: 100px;">???MTTMR??????</th>
								<th style="width: 100px;">???MTVFY??????</th>
								<th style="width: 100px;">???MTLVL??????</th>
								<th style="width: 146px;">????????????</th>
								<th style="width: 239px;">MO????????????</th>
								<th style="width: 73px;">??????</th>
							</tr>
						</thead>
						<tbody>
						<%
								if (bufinfo != null) {
							%>
						 <tr>
								<td><div align="center"><%=bufinfo.getPreCnt()%></div></td>
								<td><div align="center"><%=bufinfo.getMtTotalRecv()%></div></td>
								<td><div align="center"><%=bufinfo.getMoTotalSnd()%> </div></td>
								<td><div align="center"><%=bufinfo.getWrMttaskBuf()%> </div></td>
								<td><div align="center"><%=bufinfo.getWrMtTmBuf()%></div></td>
								<td><div align="center"><%=bufinfo.getWrMtvfyBuf()%></div></td>
								<td><div align="center"><%=bufinfo.getWrMtLvlBuf()%></div></td>
								<td><div align="center"><%=bufinfo.getPreRspBuf()%></div></td>
								<td><div align="center"><%=bufinfo.getMoSndBuf()%></div></td>
								<td><div align="center"><%=bufinfo.getMoRptWaitBuf()%></div></td>
							</tr>
							<%}else{
							 %>
							 <tr>
								<td colspan="10"><div align="center">?????????</div></td>
							</tr>
							 <%} %>
					    </tbody>
					    
					</table>
					<br><br>
					<table style="width: 366px;" id="content">
					<thead>
						<%
							if (sysinfo != null) {
						%>
						<tr>
						    <th style="width: 109px;padding: 3px;" align="center">???????????????</th>
						    <td style="padding-left: 20px;"><%=sysinfo.getCpuUsage()%>% </td>
					    </tr>
					    <tr>
						    <th style="width: 109px;padding: 3px;" align="center">???????????????</th>
						    <td style="padding-left: 20px;"><%=sysinfo.getMemUsage()%>M</td>
					    </tr>
					    <tr>
						    <th style="width: 109px;padding: 3px;" align="center">???????????????</th>
						    <td style="padding-left: 20px;"><%=sysinfo.getVmemUsage()%>M</td>
					    </tr>
					    <tr>
						    <th style="width: 109px;padding: 3px;" align="center">???????????????</th>
						    <td style="padding-left: 20px;"><%=sysinfo.getDiskFreeSpace()%>M</td>
					    </tr>
					    <%
							}
							if(bufinfo != null)
							{
						%>
					    <tr>
						    <th style="width: 109px;padding: 3px;" align="center">???????????????</th>
						    <td style="padding-left: 20px;"><%=bufinfo.getLogFileNum()%></td>
					    </tr>
					    <tr>
						    <th style="width: 109px;padding: 3px;" align="center">???????????????</th>
						    <td style="padding-left: 20px;"><%=bufinfo.getLogBuf()%></td>
					    </tr>
					    <tr>
						    <th style="width: 109px;padding: 3px;" align="center">???????????????</th>
						    <td style="padding-left: 20px;"><%=bufinfo.getRecvBuf()%></td>
					    </tr>
					    <tr>
						    <th style="width: 109px;padding: 3px;" align="center">???????????????</th>
						    <td style="padding-left: 20px;"><%=bufinfo.getReSndBuf()%></td>
					    </tr>
					    <tr>
						    <th style="width: 109px;padding: 3px;" align="center">???????????????</th>
						    <td style="padding-left: 20px;"><%=bufinfo.getSuppSndBuf()%></td>
					    </tr>
					    <%} %>
<%--					    <tr>--%>
<%--						    <td>????????????</td>--%>
<%--						    <td></td>--%>
<%--					    </tr>--%>
</thead>
					</table>
					</div>
				</div>
				
				
				<%-- ???????????? --%>
				<%-- foot?????? --%>
				<div class="bottom">
					<div id="bottom_right">
						<div id="bottom_left"></div>
						<div id="bottom_main">
						</div>
					</div>
				</div>
			</form>
			<%-- foot?????? --%>
		</div>
		<div class="clear"></div>
		<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript">
		$(document).ready(function() {
			var findresult="<%=(String) request.getAttribute("findresult")%>";
		    if(findresult=="-1")
		    {
		       alert("??????????????????,???????????????????????????!");	
		       return;			       
		    }
			$("#content tbody tr").hover(function() {
						$(this).addClass("hoverColor");
			}, function() {
						$(this).removeClass("hoverColor");
			});
			setInterval("refeshPage()", 10000);
		});
		function refeshPage()
		{
			document.pageForm.submit();
		}
		</script>
	</body>
</html>
