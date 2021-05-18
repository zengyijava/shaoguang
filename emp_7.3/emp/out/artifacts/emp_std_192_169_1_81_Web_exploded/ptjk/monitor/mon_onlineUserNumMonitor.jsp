<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>
<%@page import="com.montnets.emp.entity.monitoronline.LfMonOnlcfg"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils"%>
<%
	String path = request.getContextPath();
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String inheritPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
	inheritPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
	LfMonOnlcfg onlUser = (LfMonOnlcfg) request.getAttribute("onlUser");
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
    @ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("onlineUserNum");
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	LfMonOnlcfg molcfg = StaticValue.getMonOnlinecfg();
	String freq = "30000";//默认30s刷新一次
	if(molcfg.getMonfreq()!=null&&molcfg.getMonfreq()>0){
		freq = String.valueOf(molcfg.getMonfreq()*1000);
	}
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
   	<head>
	<title><emp:message key="ptjk_jkxq_zxyhs_1" defVal="在线用户数" fileName="ptjk"/></title>
	<%@include file="/common/common.jsp" %>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
	<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
	<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
	<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" href="<%=commonPath %>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion()%>">
	<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion()%>" type="text/css" >
  </head>
  
  <body>
   		<div id="container" class="container">
		<%-- 当前位置 --%>
		<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode) %>
		<%-- 内容开始 --%>
		<div id="rContent" class="rContent">
		<form name="pageForm" id="pageForm">
				<table id="content">
						<thead>
							<tr>
								<th>
									<emp:message key="ptjk_jkxq_zxyhs_2" defVal="在线用户数(人)" fileName="ptjk"/>
								</th>
								<th>
									<emp:message key="ptjk_common_gjjb" defVal="告警级别" fileName="ptjk"/>
								</th>
								<th>
									<emp:message key="ptjk_jkxq_zxyhs_sxsj" defVal="刷新时间" fileName="ptjk"/>
								</th>
								<th>
									<emp:message key="ptjk_jkxq_zxyhs_xq" defVal="详情" fileName="ptjk"/>
								</th>
							</tr>
						</thead>
						<tbody>
						<%
						if (onlUser != null&&onlUser.getMonstatus()==1)
							{
						%>
							<tr>
								<td>
									<%=onlUser.getOnlinenum()%>
								</td>
									<%
									if(onlUser.getEvttype() == 0)
									{
										out.print("<td class='natural'>"+MessageUtils.extractMessage("ptjk","ptjk_common_zc",request)+"</td>");
									}
									else
									{
										out.print("<td class='warn'>"+MessageUtils.extractMessage("ptjk","ptjk_common_jg",request)+"</td>");
									}
								 	%>
								<td>
									<%=sdf.format(new Date()) %>
								</td>
								<td>
								<a href="javascript:dispdetail()"><emp:message key="ptjk_common_ck" defVal="查看" fileName="ptjk"/></a>
								</td>
							</tr>
						<%
							}else{
						%>
								<tr><td align="center" colspan="10"><emp:message key="ptjk_jkxq_zxyhs_3" defVal="在线用户数未开启监控，" fileName="ptjk"/><a href="javascript:toEditOnl();"><emp:message key="ptjk_jkxq_zxyhs_jrsz" defVal="进入设置" fileName="ptjk"/></a></td></tr>
							<%} %>
						</tbody>
				</table>
		</form>
		</div>
		</div>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/ptjk_<%=langName%>.js"></script>
	    <script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>" ></script>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>" ></script>
		<script type="text/javascript">
		$(document).ready(function() {
			var findresult="<%=(String)request.getAttribute("findresult")%>";
		    if(findresult=="-1")
		    {
		       alert(getJsLocaleMessage("ptjk","ptjk_jkxq_zxyhs_1"));	
		       return;			       
		    }
			$("#content tbody tr").hover(function() {
						$(this).addClass("hoverColor");
			}, function() {
						$(this).removeClass("hoverColor");
			});
		});
		function dispdetail(){
			window.location.href = "<%=path %>/mon_onlineUserNum.htm?method=getOnlineUserInfo";
		}
		function myrefresh() 
		{ 
			window.location.reload(); 
		} 
		setTimeout('myrefresh()',<%=freq%>); //指定1秒刷新一次 

		function toEditOnl()
		{
			window.parent.openNewTab("3000-1500","<%=basePath%>mon_onlUserNumCfg.htm");
		}
		</script>
  </body>
</html>
