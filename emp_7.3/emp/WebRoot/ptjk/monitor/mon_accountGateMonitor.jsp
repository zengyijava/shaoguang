<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="java.util.Map"%>
<%@page import="com.montnets.emp.entity.monitorpas.MmonSpateinfo"%>
<%@ page import="com.montnets.emp.common.constant.StaticValue"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0,
			iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	PageInfo pageInfo  = (PageInfo) request.getAttribute("pageInfo");
	@SuppressWarnings("unchecked")
	List<MmonSpateinfo> moniList = (List<MmonSpateinfo>) request
			.getAttribute("monitorList");
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
	</head>
	<body>
		<div id="container" class="container">
			<%-- header开始 --%>
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(menuCode) %>
			<%-- header结束 --%>
			<%-- 内容开始 --%>
			<form name="pageForm" action="mon_accountGateMonitor.htm?method=find" method="post"
					id="pageForm">
			<div id="rContent" class="rContent" style="padding:5px 0;">
					<div style="padding-top:5px;padding-bottom:5px;padding-left:5px;">
					<table id="content">
						<thead>
							<tr>
								<th>通道账号</th>
								<th>通道账号名称</th>
								<th>在线状态</th>
								<th>下行提交总量</th>
								<th>下行已转发量</th>
								<th>下行滞留量</th>
								<th>下行速度</th>
								<th>上行接收总量</th>
								<th>上行已转发量</th>
								<th>上行滞留量</th>
								<th>状态报告接收总量</th>
								<th>状态报告已转发量</th>
								<th>状态报告滞留量</th>
								<th>Rpt转发速度</th>
								<th>Mo转发速度</th>
							</tr>
						</thead>
						<tbody>
							<%
							if (moniList != null && moniList.size() > 0)
							{
								for (MmonSpateinfo gateVo : moniList) {
							%>
							<tr>
								<td>
									<%=gateVo.getUserId()%>
								</td>
								<td class="textalign">
									<xmp><%= null == gateVo.getUserName()?"-":gateVo.getUserName()%></xmp>
								</td>
								<td class="ztalign">
									<%
										if (gateVo.getOnLineStatus() == 0) {
									%>
									在线
									<%
										} else if (gateVo.getOnLineStatus() == 1) {
									%>
									离线
									<%
										} else {
									%>
									未知
									<%
										}
									%>
								</td>
								<td><%=gateVo.getMtHaveSnd()+gateVo.getMtRemained()%></td>
								<td><%=gateVo.getMtHaveSnd() %></td>
								<td><%=gateVo.getMtRemained()%></td>
								<td><%=gateVo.getMtRecvSpd()%></td>
								<td><%=gateVo.getMoTotalRecv()%></td>
								<td><%=gateVo.getMoHaveSnd()%></td>
								<td><%=gateVo.getMoRemained()%></td>
								<td><%=gateVo.getRptTotalRecv()%></td>
								<td><%=gateVo.getRptHaveSnd()%></td>
								<td><%=gateVo.getRptRemained()%></td>
								<td><%=gateVo.getRptSndSpd()%></td>
								<td><%=gateVo.getMoSndSpd()%></td>
							</tr>
							<%
								}
							}else{
							%>
							<tr><td colspan="15" align="center">无记录</td></tr><%} %>
						</tbody>
						<tfoot>
							<tr>
								<td colspan="15">
									<div id="pageInfo"></div>
								</td>
							</tr>
						</tfoot>
					</table>
					</div>
			</div>
			<%-- 内容结束 --%>
			<%-- foot开始 --%>
			<div >
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
		<script language="javascript" type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript">
		$(document).ready(function() {
			var findresult="<%=(String)request.getAttribute("findresult")%>";
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
			initPage(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>);
			setInterval("refeshPage()", 10000);
		});
		function refeshPage()
		{
			document.pageForm.submit();
		}
		</script>
	</body>
</html>
