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
			<%-- header开始 --%>
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(menuCode) %>
			<%-- header结束 --%>
			<%-- 内容开始 --%>
			<form name="pageForm"
				action="mon_systemRuntimerMonitor.htm?method=find" method="post"
				id="pageForm">
				<div id="rContent" class="rContent" style="padding:5px 0;">
					<div style="padding-top:5px;padding-bottom:5px;padding-left:5px;">
					<table id="content">
						<thead>
							<tr>
								<th>通道数</th>
								<th>接收MO</th>
								<th>转发MT</th>
								<th colspan="2">MO写库缓冲</th>
								<th colspan="2">MO更新缓冲</th>
								<th>回应缓冲</th>
								<th>MT发送缓冲</th>
								<th>等待</th>
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
								<td colspan="10"><div align="center">无记录</div></td>
							</tr>
							 <%} %>
					    </tbody>
					    </table>
					    <br>
					    <table  id="content">
					    <thead>
							<tr>
<%--								<th>连接数</th>--%>
<%--								<th>接收MT</th>--%>
<%--								<th>转发MO</th>--%>
<%--								<th>写MTTASK缓冲</th>--%>
<%--								<th>写MTTMR缓冲</th>--%>
<%--								<th>写MTVFY缓冲</th>--%>
<%--								<th>写MTLVL缓冲</th>--%>
<%--								<th>回应缓冲</th>--%>
<%--								<th>MO发送缓冲</th>--%>
<%--								<th>等待</th>--%>
								
								<th style="width: 109px;">连接数</th>
								<th style="width: 129px;">接收MT</th>
								<th style="width: 124px;">转发MO</th>
								<th style="width: 100px;">写MTTASK缓冲</th>
								<th style="width: 100px;">写MTTMR缓冲</th>
								<th style="width: 100px;">写MTVFY缓冲</th>
								<th style="width: 100px;">写MTLVL缓冲</th>
								<th style="width: 146px;">回应缓冲</th>
								<th style="width: 239px;">MO发送缓冲</th>
								<th style="width: 73px;">等待</th>
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
								<td colspan="10"><div align="center">无记录</div></td>
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
						    <th style="width: 109px;padding: 3px;" align="center">系统状态：</th>
						    <td style="padding-left: 20px;"><%=sysinfo.getCpuUsage()%>% </td>
					    </tr>
					    <tr>
						    <th style="width: 109px;padding: 3px;" align="center">物理内存：</th>
						    <td style="padding-left: 20px;"><%=sysinfo.getMemUsage()%>M</td>
					    </tr>
					    <tr>
						    <th style="width: 109px;padding: 3px;" align="center">虚拟内存：</th>
						    <td style="padding-left: 20px;"><%=sysinfo.getVmemUsage()%>M</td>
					    </tr>
					    <tr>
						    <th style="width: 109px;padding: 3px;" align="center">磁盘剩余：</th>
						    <td style="padding-left: 20px;"><%=sysinfo.getDiskFreeSpace()%>M</td>
					    </tr>
					    <%
							}
							if(bufinfo != null)
							{
						%>
					    <tr>
						    <th style="width: 109px;padding: 3px;" align="center">日志文件：</th>
						    <td style="padding-left: 20px;"><%=bufinfo.getLogFileNum()%></td>
					    </tr>
					    <tr>
						    <th style="width: 109px;padding: 3px;" align="center">日志缓冲：</th>
						    <td style="padding-left: 20px;"><%=bufinfo.getLogBuf()%></td>
					    </tr>
					    <tr>
						    <th style="width: 109px;padding: 3px;" align="center">接收缓冲：</th>
						    <td style="padding-left: 20px;"><%=bufinfo.getRecvBuf()%></td>
					    </tr>
					    <tr>
						    <th style="width: 109px;padding: 3px;" align="center">重发缓冲：</th>
						    <td style="padding-left: 20px;"><%=bufinfo.getReSndBuf()%></td>
					    </tr>
					    <tr>
						    <th style="width: 109px;padding: 3px;" align="center">补发缓冲：</th>
						    <td style="padding-left: 20px;"><%=bufinfo.getSuppSndBuf()%></td>
					    </tr>
					    <%} %>
<%--					    <tr>--%>
<%--						    <td>系统状态</td>--%>
<%--						    <td></td>--%>
<%--					    </tr>--%>
</thead>
					</table>
					</div>
				</div>
				
				
				<%-- 内容结束 --%>
				<%-- foot开始 --%>
				<div class="bottom">
					<div id="bottom_right">
						<div id="bottom_left"></div>
						<div id="bottom_main">
						</div>
					</div>
				</div>
			</form>
			<%-- foot结束 --%>
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
		       alert("加载页面失败,请检查网络是否正常!");	
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
