<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@page import="com.montnets.emp.common.vo.SendedMttaskVo"%>
<%@page import="com.montnets.emp.common.constant.CommonVariables"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));

	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	
	@ SuppressWarnings("unchecked")
	List<SendedMttaskVo> mtList =(List<SendedMttaskVo>) request.getAttribute("mtList");

	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	
	String menuCode = titleMap.get("rmsTaskHistory");
	menuCode=menuCode==null?"0-0-0":menuCode;
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
	String fsxqck =  com.montnets.emp.i18n.util.MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_170", request);
	String sixHour = com.montnets.emp.i18n.util.MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_176", request);
	String mday = com.montnets.emp.i18n.util.MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_177", request);
	
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 4.1 Transitional//EN">
<html>
	<head>
		<title><%=titleMap.get(menuCode) %></title>
		<%@include file="/common/common.jsp" %>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<%if(StaticValue.ZH_HK.equals(langName)){%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/dxzs_<%=langName%>.js"></script>
		<link rel="stylesheet" href="<%=commonPath%>/rms/rmstask/css/rmsAllSendRecord.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
	</head>
	<body>
		<div id="container">
			<%-- 当前位置 --%>
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode,fsxqck) %>
				<div class="rContent">
					<%              		
						if(btnMap.get(menuCode+"-0")!=null) {
					%>
					<div class="titletop" style="padding-left: 0;padding-top: 0">
						<table class="titletop_table" style="width: 100%">
								<tr>
								    <td class="titletop_td">
								     <emp:message key="dxzs_xtnrqf_title_170" defVal="发送详情查看" fileName="dxzs"/>     
						        	</td>
									<td align="right">
										<span class="titletop_font" onclick="showback()">&larr;&nbsp;<emp:message key="dxzs_xtnrqf_title_89" defVal="返回上一级" fileName="dxzs"/></span>
									</td>
								</tr>
							</table>
					   </div>
						<div id="u_o_c_explain" class="div_bg">
							<h2>
								<emp:message key="dxzs_xtnrqf_title_171" defVal="基本信息：" fileName="dxzs"/>
							</h2>
                            <table class="showInfo">
                              <tr>
                                 <td class="titleTd"><b><emp:message key="rms_taskrecord_rwpc2" defVal="任务批次" fileName="rms"/>：</b></td>
								  <td><c:out value="${requestScope.taskId}" default=""/></td>
                              </tr>
                              <tr>
                                 <td class="titleTd"><b><emp:message key="rms_fxapp_tempchoose_fxtopic" defVal="富信主题" fileName="rms"/>：</b></td>
								  <td><c:out value="${requestScope.tmName}" default=""/></td>
                              </tr>
                              <tr>
                                 <td class="titleTd"><b><emp:message key="rms_fxapp_degreerep_range" defVal="档位" fileName="rms"/>：</b></td>
								  <td><c:out value="${requestScope.degree}" default=""/><emp:message key="rms_taskrecord_degree_p" defVal="档" fileName="rms"/></td>
							  </tr>
                              <tr>
                                 <td class="titleTd"><b><emp:message key="rms_fxapp_fsmx_fssj" defVal="发送时间" fileName="rms"/>：</b></td>
								  <td><c:out value="${requestScope.sendTime}" default=""/></td>
							  </tr>
                              <tr>
                                 <td class="titleTd"><b><emp:message key="dxzs_xtnrqf_title_173" defVal="发送情况" fileName="dxzs"/>：</b></td>
								  <td><c:out value="${requestScope.sendInfo}" default=""/></td>
                              </tr>
                              <tr>
								<td colspan="2">
                                <font class="zhu"><emp:message key="dxzs_xtnrqf_title_174" defVal="注：以上数据" fileName="dxzs"/><%=StaticValue.getCORPTYPE() ==0?sixHour:mday%><emp:message key="dxzs_xtnrqf_title_175" defVal="汇总一次,可能与下面的实时数据有一定误差。" fileName="dxzs"/></font>
                                </td>
							  </tr>
                            </table>
						</div>
						<form name="pageForm" action="rms_rmsTaskHistory.htm?method=findAllSendInfo" method="post">
							<div id="hiddenValueDiv" style="display: none"></div>
							<input type="hidden" name="mtId" id="mtId" value="${requestScope.mtId}">
							<input type="hidden" name="sendStatus" id="sendStatus" value="${requestScope.sendStatus}">
							<div class="buttons" style="padding-top: 10px;">
							  <div id="toggleDiv">
							  </div>
							  <span id="backgo" class="right mr5" onclick="showback()">&nbsp;<emp:message key="common_btn_10" defVal="返回" fileName="common"/></span>
							  <a id="exportCondition" onclick="downRptExcel('<c:out value="${requestScope.countSize}" default="0"/>','<c:out value="${requestScope.pageInfo.totalRec}" default="0"/>')"><emp:message key="common_export" defVal="导出" fileName="common"/></a>
							</div>
							<div id="condition">
								<table>
									<tr>
									<td><emp:message key="rms_fxapp_fsmx_operator2" defVal="运营商" fileName="rms"/>：</td>
									<td>
									<select id="spisuncm" name="spisuncm">
									<option value="" <c:if test="${empty requestScope.conditionMap.unicom}">selected</c:if>><emp:message key="common_whole" defVal="全部" fileName="common"/></option>
									<option value="0" <c:if test="${requestScope.conditionMap.unicom == '0'}">selected</c:if>><emp:message key="rms_fxapp_fsmx_yidong" defVal="移动" fileName="rms"/></option>
									<option value="1" <c:if test="${requestScope.conditionMap.unicom == '1'}">selected</c:if>><emp:message key="rms_fxapp_fsmx_liantong" defVal="联通" fileName="rms"/></option>
									<option value="21" <c:if test="${requestScope.conditionMap.unicom == '21'}">selected</c:if>><emp:message key="rms_fxapp_fsmx_dianxin" defVal="电信" fileName="rms"/></option>
									<%--<option value="5" <c:if test="${requestScope.conditionMap.unicom == '5'}">selected</c:if>>国外</option>--%>
									</select>
									</td>
									<td><emp:message key="rms_fxapp_fxsend_telphone" defVal="手机号码" fileName="rms"/>：</td>
									<td>
									<input type="text" value='<c:out value="${requestScope.conditionMap.phone}" default=""/>' id="phone" name="phone" maxlength="21" onkeyup="phoneInputCtrl($(this))"/>
									</td>
									<td><emp:message key="rms_taskrecord_sendStatus" defVal="发送状态" fileName="rms"/>：</td>
									<td>
										<select id="sendType" name="sendType" title="">
											<option value="" <c:if test="${empty requestScope.conditionMap.sendType}">selected</c:if>><emp:message key="common_pleaseSelect" defVal="请选择" fileName="common"/></option>
											<option value="4" <c:if test="${requestScope.conditionMap.sendType == '4'}">selected</c:if>><emp:message key="dxzs_xtnrqf_title_180" defVal="接收失败" fileName="dxzs"/></option>
											<option value="5" <c:if test="${requestScope.conditionMap.sendType == '5'}">selected</c:if>><emp:message key="dxzs_xtnrqf_title_181" defVal="提交失败" fileName="dxzs"/></option>
										</select>
									</td>
									<td class="tdSer">
									<center><a id="search"></a></center>
									</td>
									</tr>
									<tr>
									<c:if test="${requestScope.isRptFlag}">
										<td><emp:message key="rms_taskrecord_downStatus" defVal="下载状态" fileName="rms"/>：</td>
										<td>
											<select id="downType" name="downType" title="">
												<option value="" <c:if test="${empty requestScope.conditionMap.downType}">selected</c:if>><emp:message key="common_pleaseSelect" defVal="请选择" fileName="common"/></option>
												<option value="1" <c:if test="${requestScope.conditionMap.downType == '1'}">selected</c:if>><emp:message key="rms_fxapp_fsmx_succeed" defVal="成功" fileName="rms"/></option>
												<option value="2" <c:if test="${requestScope.conditionMap.downType == '2'}">selected</c:if>><emp:message key="rms_fxapp_fsmx_failure" defVal="失败" fileName="rms"/></option>
												<option value="3" <c:if test="${requestScope.conditionMap.downType == '3'}">selected</c:if>><emp:message key="rms_fxapp_fsmx_unback" defVal="未返" fileName="rms"/></option>
											</select>
										</td>
									</c:if>
									<td></td>
									<td></td>
									</tr>
							 </table>
							</div>
							<table id="content">
							<thead>
								<tr>
									<th>
										<emp:message key="common_serialNumber" defVal="序号" fileName="common"/>
									</th>
									<th>
										<emp:message key="rms_fxapp_fsmx_operator2" defVal="运营商" fileName="rms"/>
									</th>
									<th>
										<emp:message key="rms_fxapp_fxsend_telphone" defVal="手机号码" fileName="rms"/>
									</th>
									<th>
										<emp:message key="rms_task_sendStatusRpt" defVal="发送状态报告" fileName="rms"/>
									</th>
									<c:if test="${requestScope.isRptFlag}">
										<th>
											<emp:message key="rms_task_downStatusRpt" defVal="下载状态报告" fileName="rms"/>
										</th>
									</c:if>
								</tr>
							</thead>
							<tbody>
								<c:set value="1" var="sequence"/>
								<c:if test="${requestScope.countSize == 0}">
									<tr>
										<td align="center" colspan="<c:out value="${requestScope.isRptFlag ? 5 : 4}"/>"><emp:message key="common_norecord" defVal="无记录" fileName="common"/></td>
									</tr>
								</c:if>
								<c:if test="${requestScope.countSize == -1}">
									<tr>
										<td align="center" colspan="<c:out value="${requestScope.isRptFlag ? 5 : 4}"/>"><emp:message key="common_errorPage" defVal="页面加载有误，请刷新页面重试。" fileName="common"/></td>
									</tr>
								</c:if>
								<c:if test="${requestScope.countSize > 0 && !(empty requestScope.mtTaskVos)}">
									<c:forEach items="${requestScope.mtTaskVos}" var="mtVo">
										<tr>
											<td>${sequence}</td>
											<td><c:out value="${mtVo.unicomName}" default="-"/></td>
											<td><c:out value="${mtVo.phone}" default="-"/></td>
											<td><c:out value="${mtVo.sendStatus}" default="-"/></td>
											<c:if test="${requestScope.isRptFlag}">
												<td><c:out value="${mtVo.downStatus}" default="-"/></td>
											</c:if>
										</tr>
										<c:set value="${sequence + 1}" var="sequence"/>
									</c:forEach>
								</c:if>
							</tbody>
							<tfoot>
							<tr >
								<td colspan="<c:out value="${requestScope.isRptFlag ? 5 : 4}"/>">
									<div id="pageInfo"></div>
								</td>
							</tr>
							</tfoot>
						</table>
						</form>
						<div class="clear"></div>
						<%
							}
						%>
					</div>
			<%--end rContent--%>
			<div class="bottom">
				<div id="bottom_right">
					<div id="bottom_left"></div>
				</div>
			</div>
		</div>
		<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/dxzs_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/rms_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/rms/commontempl/js/jquery-cookie.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/rms/rmstask/js/rmsAllSendRecord.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript">
            //页面加载，初始化相关数据
            $(document).ready(function() {
                showPageInfo2(<c:out value="${requestScope.pageInfo.totalPage}" default="1"/>,<c:out value="${requestScope.pageInfo.pageIndex}" default="1"/>,<c:out value="${requestScope.pageInfo.pageSize}" default="15"/>,<c:out value="${requestScope.pageInfo.totalRec}" default="0"/>,[5,10,15]);
            });
		</script>
	</body>
</html>
