<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="com.montnets.emp.util.PageInfo"%>
<%@page import="com.montnets.emp.monitor.constant.MonDhostParams"%>
<%@ page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils"%>
<%
String path = request.getContextPath();
String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));

java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
@ SuppressWarnings("unchecked")
Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
@ SuppressWarnings("unchecked")
Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
String menuCode = titleMap.get("hostMon");
menuCode = menuCode==null?"0-0-0":menuCode;

PageInfo pageInfo = new PageInfo();
pageInfo=(PageInfo)request.getAttribute("pageInfo");

String skin = session.getAttribute("stlyeSkin")==null?
		"default":(String)session.getAttribute("stlyeSkin");
@SuppressWarnings("unchecked")
List<MonDhostParams> pageList = (List<MonDhostParams>)request.getAttribute("pageList");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    
    <title>My JSP 'mon_hostMoConfig.jsp' starting page</title>
    <%@include file="/common/common.jsp" %>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<%--
	<link rel="stylesheet" type="text/css" href="styles.css">
	--%>
	<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
	<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
	<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
	<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion()%>" />
	<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion()%>" type="text/css" >
  	<%if(StaticValue.ZH_HK.equals(langName)){%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
	<%}%>
  </head>
  
  <body>
	<table id="content">
		<thead>
			<tr>
				<th>
					<emp:message key="ptjk_common_zjbh" defVal="主机编号" fileName="ptjk"/>
				</th>
				<th>
					<emp:message key="ptjk_common_zjmc" defVal="主机名称" fileName="ptjk"/>
				</th>
				<th>
					<emp:message key="ptjk_common_gjjb" defVal="告警级别" fileName="ptjk"/>
				</th>
				<th>
					<emp:message key="ptjk_common_cpuzy" defVal="CPU占用" fileName="ptjk"/>
				</th>
				<th>
					<emp:message key="ptjk_common_wlncsyl" defVal="物理内存使用量(M)" fileName="ptjk"/>
				</th>
				<th>
					<emp:message key="ptjk_common_xnncsyl" defVal="虚拟内存使用量(M)" fileName="ptjk"/>
				</th>
				<th>
					<emp:message key="ptjk_common_cpsy" defVal="磁盘剩余(M)" fileName="ptjk"/>
				</th>
				<th>
					<emp:message key="ptjk_jkxq_zj_jcsg" defVal="进程数(个)" fileName="ptjk"/>
				</th>
				<th>
					<emp:message key="ptjk_jkxq_zj_zjzt" defVal="主机状态" fileName="ptjk"/>
				</th>
				<th>
					<emp:message key="ptjk_jkxq_zj_nwip" defVal="内网IP" fileName="ptjk"/>
				</th>
				<th>
					<emp:message key="ptjk_common_gxsj" defVal="更新时间" fileName="ptjk"/>
				</th>
			</tr>
		</thead>
		<tbody>
		<%
			if(pageList!=null && pageList.size()>0)
			{
				for(MonDhostParams mon : pageList)
				{
		%>
		<tr>
			<td >
				<%=mon.getHostid()!=null?mon.getHostid():"-"%>
			</td>
			<td>
				<xmp>
				<%=mon.getHostName()!=null?mon.getHostName():"-"%>
				</xmp>
			</td>
			<%
				String evttypeStr = mon.getEvtType()!=null?mon.getEvtType().toString():"30";
				if("1".equals(evttypeStr))
				{
					out.print("<td class='warn'>"+MessageUtils.extractMessage("ptjk","ptjk_common_jg",request)+"</td>");
				}
				else if("2".equals(evttypeStr))
				{
					out.print("<td class='bad'>"+MessageUtils.extractMessage("ptjk","ptjk_common_yz",request)+"</td>");
				}
				else
				{
					out.print("<td class='natural'>"+MessageUtils.extractMessage("ptjk","ptjk_common_zc",request)+"</td>");
				}
			%>
			<td>
				<%=mon.getCpuusage()!=null?mon.getCpuusage():"0" %>%
			</td>
			<td>
				<%=mon.getMemusage()!=null?mon.getMemusage():"0"%>
			</td>
			<td>
				<%=mon.getVmemusage()!=null?mon.getVmemusage():"0"%>
			</td>
			<td>
				<%=mon.getDiskfreespace()!=null?mon.getDiskfreespace():"0" %>
			</td>
			<td>
				<%=mon.getProcesscnt()!=null?mon.getProcesscnt():"0"%>
			</td>
			<td>
			<%
				String hoststatusStr = mon.getHoststatus()!=null?mon.getHoststatus().toString():"3";
				if("1".equals(hoststatusStr))
				{
					out.print(MessageUtils.extractMessage("ptjk","ptjk_jkxq_zj_zw",request));
				}
				else if("2".equals(hoststatusStr))
				{
					out.print(MessageUtils.extractMessage("ptjk","ptjk_jkxq_zj_tj",request));
				}
				else if("3".equals(hoststatusStr))
				{
					out.print(MessageUtils.extractMessage("ptjk","ptjk_common_yc",request));
				}
				else
				{
					out.print(MessageUtils.extractMessage("ptjk","ptjk_common_wz",request));
				}
			%>
			</td>
			<td>
				<%=mon.getAdapter1()!=null?mon.getAdapter1():"-" %>
			</td>
			<td>
				<%=mon.getUpdatetime()!=null?df.format(mon.getUpdatetime()):"-" %>
			</td>
		</tr>
		<%
				}
			}else{
		%>
		<tr><td align="center" colspan="13"><emp:message key="ptjk_common_wjl" defVal="无记录" fileName="ptjk"/></td></tr>
		<%} %>
		</tbody>
		<tfoot>
			<tr>
				<td colspan="13">
					<div id="pageInfo"></div>
				</td>
			</tr>
		</tfoot>
	</table>
	<script src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript" ></script>
	<script type="text/javascript">
		$(document).ready(function() {
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
			//解除绑定事件，使表格选中不变色
			$('#content table tbody tr,#content tbody tr').unbind("click");
		});
	</script>
  </body>
</html>
