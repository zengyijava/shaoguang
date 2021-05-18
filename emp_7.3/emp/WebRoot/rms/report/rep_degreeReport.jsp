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
	<title><emp:message key="rms_degree_report_title" defVal="容量套餐统计" fileName="rms"/></title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<%@ include file="/common/common.jsp"%>
	<link href="<%=commonPath%>/common/css/frame.css" rel="stylesheet" type="text/css"/>
	<link href="<%=commonPath%>/common/css/table.css" rel="stylesheet" type="text/css"/>
	<link href="<%=skin %>/frame.css" rel="stylesheet" type="text/css"/>
	<link href="<%=skin%>/table.css" rel="stylesheet" type="text/css"/>
	<link href="<%=commonPath%>/common/css/select.css" rel="stylesheet" type="text/css"/>
</head>

<body id="rep_degreeReport">
<div id="container" class="container">
	<input type="hidden" id="skin" value="<%=skin%>"/>
	<input type="hidden" id="startTimeTemp" value="<c:out value="${requestScope.rptVo.startTime}" default=""/>"/>
	<input type="hidden" id="endTimeTemp" value="<c:out value="${requestScope.rptVo.endTime}" default=""/>"/>
	<input type="hidden" id="countSize" value="<c:out value="${requestScope.countSize}" default="0"/>"/>
	<%-- 当前位置 --%>
    <%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode) %>

	<%-- 内容开始 --%>
	<%if(btnMap.get(menuCode+"-0")!=null) { %>
	<form name="pageForm" action="rep_degreeReport.htm?method=findDegreeRpt" method="post" id="pageForm">
		<div id="hiddenValueDiv" style="display: none"></div>
		<div id="rContent" class="rContent">
			<div class="buttons">
				<div id="toggleDiv"></div>
				<a id="exportCondition" onclick="downDegreeRpt('<c:out value="${requestScope.countSize}" default="0"/>','<c:out value="${requestScope.pageInfo.totalRec}" default="0"/>')"><emp:message key="rms_fxapp_degreerep_export" defVal="导出" fileName="rms"/></a>
			</div>
			<div id="condition">
				<table>
					<tbody>
						<tr>
							<td><emp:message key="rms_fxapp_degreerep_spzh" defVal="SP帐号：" fileName="rms"/></td>
							<td>
								<select name="spUser" id="spUser">
									<option value="" <c:out value="${empty requestScope.rptVo.userId?'selected':''}"/>><emp:message key="common_whole" defVal="全部" fileName="common"/></option>
									<c:forEach items="${requestScope.spList}" var="spUser">
										<c:if test="${spUser.spUser == requestScope.rptVo.userId}">
											<option value="${spUser.spUser}" selected>${spUser.spUser}</option>
										</c:if>
										<c:if test="${spUser.spUser != requestScope.rptVo.userId}">
											<option value="${spUser.spUser}">${spUser.spUser}</option>
										</c:if>
									</c:forEach>
								</select>
							</td>
							<td class="hideTd"><emp:message key="rms_degree_report_corname2" defVal="企业名称：" fileName="rms"/></td>
							<td class="hideTd"><input title='<emp:message key="rms_degree_report_corname_hint" defVal="请输入企业名称" fileName="rms"/>' type="text" value="<c:out value="${requestScope.rptVo.corpName}" default=""/>" id="cropName" name="cropName" maxlength="20"/></td>
							<td class="hideTd"><emp:message key="rms_degree_report_corcode" defVal="企业编码：" fileName="rms"/>：</td>
							<td class="hideTd"><input title='<emp:message key="rms_degree_report_corcode_hint" defVal="请输入企业编码" fileName="rms"/>' type="text" value="<c:out value="${requestScope.rptVo.corpCode}" default=""/>" id="corpCode" name="corpCode" maxlength="10"/></td>
							<td class="tdSer">
								<center><a id="search"></a></center>
							</td>
						</tr>
						<tr class="secondTr">
							<td><emp:message key="rms_fxapp_myscene_level" defVal="档位：" fileName="rms"/></td>
							<td>
								<label>
									<select name="degree" id="degree" isInput="false">
										<option value="" <c:out value="${empty requestScope.rptVo.chgrade?'selected':''}"/>><emp:message key="common_whole" defVal="全部" fileName="common"/></option>
										<c:forEach items="${requestScope.degreeList}" var="degree">
											<c:if test="${degree == requestScope.rptVo.chgrade}">
												<option value="${degree}" selected>${degree}<emp:message key="rms_taskrecord_degree_p" defVal="档" fileName="rms"/></option>
											</c:if>
											<c:if test="${degree != requestScope.rptVo.chgrade}">
												<option value="${degree}">${degree}<emp:message key="rms_taskrecord_degree_p" defVal="档" fileName="rms"/></option>
											</c:if>
										</c:forEach>
									</select>
								</label>
							</td>
							<td><emp:message key="rms_fxapp_fsmx_operator" defVal="运营商：" fileName="rms"/></td>
							<td>
								<select id="operator" name="operator" isInput="false">
									<option value="" <c:if test="${empty requestScope.rptVo.spisuncm}">selected</c:if>><emp:message key="common_whole" defVal="全部" fileName="common"/></option>
									<option value="0" <c:if test="${requestScope.rptVo.spisuncm == 0}">selected</c:if>><emp:message key="rms_fxapp_fsmx_yidong" defVal="移动" fileName="rms"/></option>
									<option value="1" <c:if test="${requestScope.rptVo.spisuncm == 1}">selected</c:if>><emp:message key="rms_fxapp_fsmx_liantong" defVal="联通" fileName="rms"/></option>
									<option value="21" <c:if test="${requestScope.rptVo.spisuncm == 21}">selected</c:if>><emp:message key="rms_fxapp_fsmx_dianxin" defVal="电信" fileName="rms"/></option>
									<option value="5" <c:if test="${requestScope.rptVo.spisuncm == 5}">selected</c:if>><emp:message key="rms_fxapp_fsmx_guowai" defVal="国外" fileName="rms"/></option>
								</select>
							</td>
						</tr>
						<tr>
							<td><emp:message key="rms_fxapp_degreerep_reptype" defVal="报表类型：" fileName="rms"/></td>
							<td>
								<select name="reportType" id="reportTypeSelect" isInput="false">
									<option value="0" <c:if test="${requestScope.rptVo.reportType == 0}">selected</c:if>><emp:message key="rms_fxapp_degreerep_dailyreport" defVal="日报表" fileName="rms"/></option>
									<option value="1" <c:if test="${requestScope.rptVo.reportType == 1}">selected</c:if>><emp:message key="rms_fxapp_degreerep_monthlyreport" defVal="月报表" fileName="rms"/></option>
									<option value="2" <c:if test="${requestScope.rptVo.reportType == 2}">selected</c:if>><emp:message key="rms_fxapp_degreerep_yearlyreport" defVal="年报表" fileName="rms"/></option>
								</select>
							</td>
							<td id="yearOrMonth"><emp:message key="rms_fxapp_degreerep_tjny" defVal="统计年月：" fileName="rms"/></td>
							<td>
								<input type="text" name="startTime" id="startTime" value="" readonly="readonly" class="Wdate"/>
							</td>
							<td><span class="toSpan"><emp:message key="rms_fxapp_fsmx_to" defVal="至：" fileName="rms"/></span></td>
							<td>
								<input type="text" name="endTime" value="" id="endTime" readonly="readonly" class="Wdate"/>
								<span class="reminder" style="display:none;"><emp:message key="rms_degree_report_excludecurrday" defVal="(不包括当天)" fileName="rms"/></span>
							</td>
							<td></td>
						</tr>
					</tbody>
				</table>
			</div>
			<table id="content">
				<thead>
					<tr>
						<th width="10%"><emp:message key="rms_fxapp_degreerep_time" defVal="时间" fileName="rms"/></th>
						<c:if test="${requestScope.corpCode == '100000'}">
							<th width="5%"><emp:message key="rms_degree_report_corcode" defVal="企业编码" fileName="rms"/></th>
							<th width="8%"><emp:message key="rms_degree_report_corname" defVal="企业名称" fileName="rms"/></th>
						</c:if>
						<th width="8%"><emp:message key="rms_taskrecord_spaccount" defVal="SP账号" fileName="rms"/></th>
						<th width="5%"><emp:message key="rms_taskrecord_operator" defVal="运营商" fileName="rms"/></th>
						<th width="5%"><emp:message key="rms_fxapp_degreerep_range" defVal="档位" fileName="rms"/></th>
						<th width="4%"><emp:message key="rms_fxapp_degreerep_tjhms" defVal="提交号码数" fileName="rms"/></th>
						<th width="5%"><emp:message key="rms_fxapp_degreerep_fscgs" defVal="发送成功数" fileName="rms"/></th>
						<th width="5%"><emp:message key="rms_fxapp_degreerep_jssbs" defVal="接收失败数" fileName="rms"/></th>
						<%--<c:if test="${requestScope.corpCode == '100000'|| ((requestScope.corpCode != '100000') && (isRptFlag ==3)) }"><th width="5%">下载成功数</th></c:if>--%>
						<th width="8%"><emp:message key="rms_fxapp_degreerep_detail" defVal="详情" fileName="rms"/></th>
					</tr>
				</thead>
					<tbody>
						<c:if test="${empty requestScope.countSize}">
							<tr>
								<td align="center" colspan="16"><emp:message key="rms_fxapp_fsmx_clickquery" defVal="请点击查询获取数据" fileName="rms"/></td>
							</tr>
						</c:if>
						<c:if test="${requestScope.countSize == 0}">
							<tr>
								<td align="center" colspan="16"><emp:message key="rms_fxapp_fsmx_norecord" defVal="无记录" fileName="rms"/></td>
							</tr>
						</c:if>
						<c:if test="${requestScope.countSize > 0 && !(empty requestScope.reportList)}">
							<c:set value="0" var="icountTemp"/>
							<c:set value="0" var="rsuccTemp"/>
							<c:set value="0" var="rfail1Temp"/>
							<c:set value="0" var="dwsuccTemp"/>
							<c:forEach items="${requestScope.reportList}" var="report">
								<tr>
									<td><c:out value="${report.showTime}" default="-"/></td>
									<c:if test="${requestScope.corpCode == '100000'}">
										<td><c:out value="${report.corpCode}" default="-"/></td>
										<td><c:out value="${report.corpName}" default="-"/></td>
									</c:if>
									<td><c:out value="${report.userId}" default="-"/></td>
									<td><c:out value="${report.spisuncmName}" default="-"/></td>
									<td><c:out value="${report.chgrade}" default="-"/><emp:message key="rms_taskrecord_degree_p" defVal="档" fileName="rms"/></td>
									<td><c:out value="${report.icount}" default="0"/></td>
									<%-- 发送成功数=成功数+未返 --%>
									<td><c:out value="${report.rsucc+report.rnret}" default="0"/></td>
									<td><c:out value="${report.recfail}" default="0"/></td>
									<%--<c:if test="${requestScope.corpCode == '100000'|| ((requestScope.corpCode != '100000') && (isRptFlag ==3)) }"><td><c:out value="${report.dwsucc}" default="0"/></td></c:if>--%>
									<td><a class="des" href="javascript:showDetailsInfo('${report.detailInfo}')"><emp:message key="rms_degree_report_lookup" defVal="查看" fileName="rms"/></a></td>
									<c:set value="${icountTemp + report.icount}" var="icountTemp"/>
									<c:set value="${rsuccTemp + report.rsucc+report.rnret}" var="rsuccTemp"/>
									<c:set value="${rfail1Temp + report.recfail}" var="rfail1Temp"/>
									<%--<c:set value="${dwsuccTemp + report.dwsucc}" var="dwsuccTemp"/>--%>
								</tr>
							</c:forEach>
							<tr>
								<td colspan="${requestScope.corpCode == '100000'? 6 : 4}"><b><emp:message key="rms_fxapp_degreerep_total" defVal="合计：" fileName="rms"/></b>
								</td>
								<td>${icountTemp}</td>
								<td>${rsuccTemp}</td>
								<td>${rfail1Temp}</td>
								<td></td>
								<%-- <c:if test="${isRptFlag !=0 }"><td>${dwsuccTemp}</td></c:if> --%>
							</tr>
						</c:if>
					</tbody>
				<tfoot>
					<tr>
						<td colspan="11"><div id="pageInfo"></div></td>
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
<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/rms_<%=langName%>.js"></script>
<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=commonPath%>/rms/commontempl/js/jquery-cookie.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript">
    //如果为10W号隐藏企业编码与企业名称
    var corpCode = '${requestScope.corpCode}';
    if("100000" !== corpCode){
        $(".secondTr").append("<td>"+getJsLocaleMessage('rms','rms_degreerep_spaccount')+"</td>");
        $(".secondTr").append("<td><select name='spUser' id='spUser'>"+ $("#spUser").html() +"</select></td>");
        $(".secondTr").append("<td class=\"tdSer\">"+ $(".tdSer").html() +"</td>");
        $("#spUser").parent().parent().remove();
    }
    //页面加载，初始化相关数据
    $(document).ready(function() {
        showPageInfo2(<c:out value="${requestScope.pageInfo.totalPage}" default="1"/>,<c:out value="${requestScope.pageInfo.pageIndex}" default="1"/>,<c:out value="${requestScope.pageInfo.pageSize}" default="15"/>,<c:out value="${requestScope.pageInfo.totalRec}" default="0"/>,[5,10,15]);
    });
</script>
<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=iPath%>/js/rep_rltcReport.js"></script>
</body>
</html>
