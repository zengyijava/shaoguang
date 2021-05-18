<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page import="java.util.Map"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<%
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0, iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("degreeReport");
	menuCode = menuCode==null?"0-0-0":menuCode;
	String skin = session.getAttribute("stlyeSkin") == null ? "default" : (String) session.getAttribute("stlyeSkin");
    String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
%>
<html>
<head>
<title><emp:message key="rms_degree_report" defVal="档位统计报表" fileName="rms"/></title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="/common/common.jsp"%>
<link href="<%=commonPath%>/common/css/frame.css" rel="stylesheet" type="text/css" />
<link href="<%=commonPath%>/common/css/table.css" rel="stylesheet" type="text/css" />
<link href="<%=skin %>/frame.css" rel="stylesheet" type="text/css" />
<link href="<%=skin %>/table.css" rel="stylesheet" type="text/css" />
</head>
<body class="rep_showDetailsInfo">
	<div id="container" class="container">
		<%-- 当前位置 --%>
		<%=com.montnets.emp.common.constant.ViewParams.getPosition(menuCode) %>
		<%-- 内容开始 --%>
		<%if(btnMap.get(menuCode+"-0")!=null) { %>
		<form name="pageForm" action="rep_degreeReport.htm?method=showDetailsInfo" method="post" id="pageForm">
			<div id="hiddenValueDiv" style="display: none"></div>
			<div id="rContent" class="rContent">
				<div class="buttons">
					<span id="backgo" class="right mr5" onclick="back()">&nbsp;<emp:message key="rms_fxapp_fxsend_back" defVal="返回" fileName="rms"/></span>
					<a id="exportCondition" onclick="downDegreeRpt(${requestScope.countSize},'isDes')"><emp:message key="rms_fxapp_degreerep_export" defVal="导出" fileName="rms"/></a>
				</div>
				<table id="content" style="margin-top: 5px;">
					<thead>
						<th><emp:message key="rms_fxapp_degreerep_time" defVal="时间" fileName="rms"/></th>
						<th><emp:message key="rms_fxapp_fsmx_spzh" defVal="SP账号" fileName="rms"/></th>
						<th><emp:message key="rms_taskrecord_operator" defVal="运营商" fileName="rms"/></th>
						<th><emp:message key="rms_fxapp_degreerep_range" defVal="档位" fileName="rms"/></th>
						<th><emp:message key="rms_fxapp_degreerep_tjhms" defVal="提交号码数" fileName="rms"/></th>
						<th><emp:message key="rms_fxapp_degreerep_fscgs" defVal="发送成功数" fileName="rms"/></th>
						<th><emp:message key="rms_fxapp_degreerep_jssbs" defVal="接收失败数" fileName="rms"/></th>
						<%--<c:if test="${isRptFlag !=0}"><th>下载成功数</th></c:if>--%>
					</thead>
					<tbody>
					<c:if test="${empty requestScope.countSize}">
						<tr>
							<td align="center" colspan="7"><emp:message key="rms_degree_report_detail_error" defVal="页面错误，请重新加载" fileName="rms"/></td>
						</tr>
					</c:if>
					<c:if test="${requestScope.countSize == 0}">
						<tr>
							<td align="center" colspan="7"><emp:message key="rms_degree_report_detail_error" defVal="页面错误，请重新加载" fileName="rms"/></td>
						</tr>
					</c:if>
					<c:if test="${requestScope.countSize > 0 && !(empty requestScope.reportList)}">
						<c:set value="0" var="icountTemp"/>
						<c:set value="0" var="rsuccTemp"/>
						<c:set value="0" var="rfail1Temp"/>
						<%--<c:set value="0" var="dwsuccTemp"/>--%>
						<c:forEach items="${requestScope.reportList}" var="report">
							<tr>
								<td><c:out value="${report.showTime}" default="-"/></td>
								<td><c:out value="${report.userId}" default="-"/></td>
								<td><c:out value="${report.spisuncmName}" default="-"/></td>
								<td><c:out value="${report.chgrade}" default="-"/><emp:message key="rms_taskrecord_degree_p" defVal="档" fileName="rms"/></td>
								<td><c:out value="${report.icount}" default="0"/></td>
								<td><c:out value="${report.rsucc+report.rnret}" default="0"/></td>
								<td><c:out value="${report.recfail}" default="0"/></td>
								<%--<c:if test="${isRptFlag !=0}"><td><c:out value="${report.dwsucc}" default="0"/></td></c:if>--%>
							</tr>
							<c:set value="${icountTemp + report.icount}" var="icountTemp"/>
							<c:set value="${rsuccTemp + report.rsucc+report.rnret}" var="rsuccTemp"/>
							<c:set value="${rfail1Temp + report.recfail}" var="rfail1Temp"/>
							<%--<c:set value="${dwsuccTemp + report.dwsucc}" var="dwsuccTemp"/>--%>
						</c:forEach>
					</c:if>
					<tr>
						<td colspan="4"><b><emp:message key="rms_fxapp_degreerep_total" defVal="合计：" fileName="rms"/></b></td>
						<td>${icountTemp}</td>
						<td>${rsuccTemp}</td>
						<td>${rfail1Temp}</td>
						<%--<c:if test="${isRptFlag !=0}"><td>${dwsuccTemp}</td></c:if>--%>
					</tr>
					</tbody>
					<tfoot>
					<tr>
						<td colspan="7"><div id="pageInfo"></div></td>
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
					<div id="bottom_main"></div>
				</div>
			</div>
		</form>
		<%-- foot结束 --%>
	</div>
	<div class="clear"></div>
	<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/rms/commontempl/js/jquery-cookie.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/common.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/rms_<%=langName%>.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/rms/report/js/rep_rltcReport.js"></script>
	<script type="text/javascript">
        //页面加载，初始化相关数据
        $(document).ready(function() {
            showPageInfo2(<c:out value="${requestScope.pageInfo.totalPage}" default="1"/>,<c:out value="${requestScope.pageInfo.pageIndex}" default="1"/>,<c:out value="${requestScope.pageInfo.pageSize}" default="15"/>,<c:out value="${requestScope.pageInfo.totalRec}" default="0"/>,[5,10,15]);
        });
	</script>
</body>
</html>
