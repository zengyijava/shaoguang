<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Map" %>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("/rmsTaskRecord");
	menuCode = menuCode==null?"0-0-0":menuCode;
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<title>群发任务查询</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<%@include file="/common/common.jsp" %>
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
		<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
		<link href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
		<link href="<%=skin%>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
	    <link href="<%=commonPath%>/common/css/reading.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
		<link href="<%=commonPath%>/common/widget/zTreeStyle/zTreeStyle.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
        <link href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
		<link href="<%=skin%>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
		<link href="<%=skin%>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
        <%if(StaticValue.ZH_HK.equals(empLangName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
			<link rel="stylesheet" type="text/css" href="<%=skin%>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
		<link href="<%=commonPath%>/rms/rmstask/css/rmsTaskRecord.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
	</head>
	<body id="rmsTaskRecord">
		<div id="container" class="container">
			<input type="hidden" id="langName" value="<%=langName%>" />
			<%-- 当前位置 --%>
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(empLangName,menuCode) %>
			<%-- 内容开始 --%>
			<div id="rContent" class="rContent">
				<form name="pageForm" action="rmsTaskRecord.htm" method="post" id="pageForm">
					<div id="hiddenValueDiv" style="display: none"></div>
					<div class="buttons">
						<div id="toggleDiv"></div>
						<a id="exportCondition" onclick="downRptExcel('<c:out value="${requestScope.countSize}" default="0"/>','<c:out value="${requestScope.pageInfo.totalRec}" default="0"/>','rmsTask')"><emp:message key="common_btn_18" defVal="导出" fileName="common"/></a>
					</div>
					<div id="condition">
						<table>

							<tbody>
								<tr>
									<td>
										<emp:message key="common_organization" defVal="隶属机构" fileName="common"/>：
									</td>
									<td class="condi_f_l">
									  		<div>
									  		 <input type="hidden" id="deptid" name="depid" value="${empty requestScope.mtVo.depIds ? "":requestScope.mtVo.depIds}"/>
									  		<input type="text" class="treeInput" title="" id="depNam" name="depNam" value="${empty requestScope.mtVo.depName ? "请选择":requestScope.mtVo.depName}" onclick="showDepMenu();" readonly/>&nbsp;
											</div>
											<div id="dropMenu" >
												<iframe frameborder="0" src="about:blank"></iframe>
												<div>
												 	<input type="checkbox" title="" id="isContainsSun" value="1" name="isContainsSun" <c:if test="${requestScope.mtVo.iscontainsSun == true}">checked="checked"</c:if> style="position: relative; left: 3px"/>
													<span><emp:message key="common_containsSun" defVal="包含子机构" fileName="common"/></span>
													<input type="button" value="<emp:message key='common_confirm' defVal='确定' fileName='common'/>" class="btnClass1" onclick="zTreeDepOnClickOK();"/>&nbsp;&nbsp;
													<input type="button" value="<emp:message key='common_clean' defVal='清空' fileName='common'/>" class="btnClass1" onclick="zTreeDepOnClickClean();"/>
												</div>
												<ul  id="dropdownMenu" class="tree"></ul>
											</div>
									</td>
									<td>
										<emp:message key="common_operator" defVal="操作员" fileName="common"/>：
									</td>
									<td class="condi_f_l">
											<div>
											 <input type="hidden" id="userid" name="userid" value="${empty requestScope.mtVo.userIds ? "":requestScope.mtVo.userIds}"/>
											<input type="text" title="" id="userName" class="treeInput" name="userName" value="${empty requestScope.mtVo.userName ? "请选择":requestScope.mtVo.userName}" onclick="showUserMenu();" readonly/>&nbsp;
											</div>
											<div id="dropMenu2">
												<iframe frameborder="0" src="about:blank"></iframe>
												<div class="dropMenu2_div1">
													<input type="button" value="<emp:message key='common_confirm' defVal='确定' fileName='common'/>" class="btnClass1" onclick="zTreeUserOnClickOK();"/>&nbsp;&nbsp;
													<input type="button" value="<emp:message key='common_clean' defVal='清空' fileName='common'/>" class="btnClass1" onclick="zTreeUserOnClickClean();"/>
												</div>
												<div class="dropMenu2_div2"><font class="zhu"><emp:message key="common_chooseOperator" defVal="注：请勾选操作员进行查询" fileName="common"/></font></div>
												<ul id="dropdownMenu2" class="tree"></ul>
											</div>
									</td>
									<td><emp:message key="rms_fxapp_fsmx_spzh" defVal="SP账号" fileName="rms"/>：</td>
									<td>
										<label>
											<select name="spUser" id="spUser">
												<option value="" <c:out value="${empty requestScope.mtVo.spUser?'selected':''}"/>><emp:message key="common_whole" defVal="全部" fileName="common"/></option>
												<c:forEach items="${requestScope.spList}" var="spUser">
													<c:if test="${spUser.spUser == requestScope.mtVo.spUser}">
														<option value="${spUser.spUser}" selected>${spUser.spUser}</option>
													</c:if>
													<c:if test="${spUser.spUser != requestScope.mtVo.spUser}">
														<option value="${spUser.spUser}">${spUser.spUser}</option>
													</c:if>
												</c:forEach>
											</select>
										</label>
									</td>
									<td class="tdSer">
									    <center><a id="search"></a></center>
								    </td>
								</tr>
								<tr>
								   <td><emp:message key="rms_taskrecord_rwpc2" defVal="任务批次" fileName="rms"/>：</td>
								   <td>
								      <input type="text" title="" id="taskId" name ="taskId" onkeyup="numberControl($(this))" value="${empty requestScope.mtVo.taskId ? "":requestScope.mtVo.taskId}" maxlength="19"/>
								    </td>
								    <td><emp:message key="rms_fxapp_tempchoose_fxtopic" defVal="富信主题：" fileName="rms"/></td>
								    <td>
								      <input type="text" title="" id="tmName" name ="tmName" value="${empty requestScope.mtVo.tmName ? "":requestScope.mtVo.tmName}"/>
									</td>
									<td><emp:message key="rms_fxapp_fsmx_tempid" defVal="模板ID：" fileName="rms"/></td>
								   <td>
								      <input type="text" title="" id="tempId" name ="tempId" onkeyup="numberControl($(this))" value="${empty requestScope.mtVo.tempId ? "":requestScope.mtVo.tempId}"/>
								    </td>
									<td>&nbsp;</td>
								</tr>
								<tr>
									<td><emp:message key="rms_taskrecord_taskStatus" defVal="任务状态" fileName="rms"/>：</td>
									<td>
										<select name="sendstate" id="sendstate" isInput = "false">
											<option value="0" <c:if test="${empty requestScope.mtVo.taskState}">selected</c:if>><emp:message key="common_whole" defVal="全部" fileName="common"/></option>
											<option value="1" <c:if test="${requestScope.mtVo.taskState == 1}">selected</c:if>><emp:message key="rms_taskrecord_tjcg" defVal="提交成功" fileName="rms"/></option>
											<option value="2" <c:if test="${requestScope.mtVo.taskState == 2}">selected</c:if>><emp:message key="rms_taskrecord_qbtjsb" defVal="全部提交失败" fileName="rms"/></option>
											<option value="7" <c:if test="${requestScope.mtVo.taskState == 7}">selected</c:if>><emp:message key="rms_taskrecord_bftjsb" defVal="部分提交失败" fileName="rms"/></option>
											<option value="3" <c:if test="${requestScope.mtVo.taskState == 3}">selected</c:if>><emp:message key="rms_taskrecord_timeing" defVal="定时中" fileName="rms"/></option>
											<option value="4" <c:if test="${requestScope.mtVo.taskState == 4}">selected</c:if>><emp:message key="rms_taskrecord_canceled" defVal="已撤销" fileName="rms"/></option>
											<option value="5" <c:if test="${requestScope.mtVo.taskState == 5}">selected</c:if>><emp:message key="rms_taskrecord_cswtj" defVal="超时未提交" fileName="rms"/></option>
											<option value="6" <c:if test="${requestScope.mtVo.taskState == 6}">selected</c:if>><emp:message key="rms_taskrecord_freezed" defVal="已冻结" fileName="rms"/></option>
										</select>
								   </td>
									<td>
										<emp:message key="rms_fxapp_fsmx_fssj" defVal="发送时间" fileName="rms"/>：
								   </td>
								   <td>
										<input type="text" readonly class="Wdate" onclick="stime()" value="${empty requestScope.mtVo.startSendTime ? "":requestScope.mtVo.startSendTime}"  title="" id="sendtime" name="sendtime">
								   </td>
								   <td><emp:message key="text_to_p" defVal="至" fileName="common"/>：</td>
								   <td>
										<input type="text" readonly class="Wdate"  title="" onclick="rtime()" value="${empty requestScope.mtVo.endSendTime ? "":requestScope.mtVo.endSendTime}" id="recvtime" name="recvtime">
									</td>
									<td>&nbsp;</td>
								</tr>
							</tbody>
						</table>
					</div>
					<table id="content">
						<thead>
							<tr>
								<th>
									<emp:message key="common_operator" defVal="操作员" fileName="common"/>
								</th>
								<th>
									<emp:message key="common_organization" defVal="隶属机构" fileName="common"/>
								</th>
								<th style="width: 5%;">
									<emp:message key="rms_fxapp_fsmx_spzh" defVal="SP账号" fileName="rms"/>
								</th>
								<th>
									<emp:message key="rms_fxapp_fxsend_bstype2" defVal="业务类型" fileName="rms"/>
								</th>
								<th>
									<emp:message key="rms_fxapp_fsmx_tempid2" defVal="模板ID" fileName="rms"/>
								</th>
								<th>
									<emp:message key="rms_fxapp_tempchoose_fxtopic" defVal="富信主题" fileName="rms"/>
								</th>
								<th style="width: 5%;">
									<emp:message key="rms_taskrecord_rwpc2" defVal="任务批次" fileName="rms"/>
								</th>
								<%--<th>--%>
									<%--<emp:message key="rms_fxapp_degreerep_range" defVal="档位" fileName="rms"/>--%>
								<%--</th>--%>
								<th>
									<emp:message key="rms_fxapp_fxsend_validatenos2" defVal="有效号码数" fileName="rms"/>
								</th>
								<th style="width: 8%;">
									<emp:message key="rms_taskrecord_taskStatus" defVal="任务状态" fileName="rms"/>
								</th>
								<th style="width: 10%;">
									<emp:message key="rms_fxapp_dwtjbb_cjsj" defVal="创建时间" fileName="rms"/>
								</th>
								<th style="width: 10%;">
									<emp:message key="rms_fxapp_fsmx_fssj" defVal="发送时间" fileName="rms"/>
								</th>
								<th style="width: 8%;">
								    <emp:message key="rms_taskrecord_sendFile" defVal="发送文件" fileName="rms"/>
								</th>
								<th>
								  	<emp:message key="common_operation" defVal="操作" fileName="common"/>
								</th>
							</tr>
						</thead>
						<tbody>
							<c:if test="${empty requestScope.countSize}">
								<tr>
									<td align="center" colspan="14"><emp:message key="common_clickGetData" defVal="请点击查询获取数据" fileName="common"/></td>
								</tr>
							</c:if>
							<c:if test="${requestScope.countSize == 0}">
								<tr>
									<td align="center" colspan="14"><emp:message key="common_norecord" defVal="无记录" fileName="common"/></td>
								</tr>
							</c:if>
							<c:if test="${requestScope.countSize == -1}">
								<tr>
									<td align="center" colspan="14"><emp:message key="common_errorPage" defVal="页面加载有误，请刷新页面重试。" fileName="common"/></td>
								</tr>
							</c:if>
							<c:if test="${requestScope.countSize > 0 && !(empty requestScope.mtVoList)}">
								<c:forEach items="${requestScope.mtVoList}" var="mtVo">
									<tr>
										<td style="display: none;"><c:out value="${mtVo.tmId}" default=""/></td>
										<td><c:out value="${mtVo.name}" default=""/><c:if test="${mtVo.userState == 2}"><span class="userNameSpan" style="color: red">(<emp:message key="common_canceled" defVal="已注销" fileName="rms"/>)</span></c:if></td>
										<td><c:out value="${mtVo.depName}" default="-"/></td>
										<td><c:out value="${mtVo.spUser}" default="-"/></td>
										<td><c:out value="${mtVo.busName}" default="-"/></td>
										<td><c:out value="${mtVo.tempId==0?'-':mtVo.tempId}" default="-"/></td>
										<td>
											<c:if test="${mtVo.msgType != 99}">
												<c:choose>
													<c:when test="${fn:length(mtVo.tmName) > 8}">
														<a id="showTemp" style="text-decoration: none;" title="${mtVo.tmName}" onclick="showTempView($(this).parent().parent().find('td:eq(0)').text())">
															<c:out value="${fn:substring(mtVo.tmName,0,8)}..."/>
														</a>
													</c:when>
													<c:when test="${empty mtVo.tmName}">-</c:when>
													<c:otherwise>
														<a id="showTemp" style="text-decoration: none;" title="${mtVo.tmName}" onclick="showTempView($(this).parent().parent().find('td:eq(0)').text())">
															<c:out value="${mtVo.tmName}"/>
														</a>
													</c:otherwise>
												</c:choose>
											</c:if>
											<c:if test="${mtVo.msgType == 99}">
												<c:choose>
													<c:when test="${fn:length(mtVo.tmName) > 8}">
														<span title="${mtVo.tmName}"><c:out value="${fn:substring(mtVo.tmName,0,8)}..."/></span>
													</c:when>
													<c:when test="${empty mtVo.tmName}">-</c:when>
													<c:otherwise>
														<span title="${mtVo.tmName}"><c:out value="${mtVo.tmName}..."/></span>
													</c:otherwise>
												</c:choose>
											</c:if>
										</td>
										<%--<td>--%>
											<%--<c:choose>--%>
												<%--<c:when test="${empty mtVo.title}">-</c:when>--%>
												<%--<c:when test="${fn:length(mtVo.title) > 8}">--%>
													<%--<a style="text-decoration: none;" title="${mtVo.title}" onclick="showTitle(this)">--%>
														<%--<c:out value="${fn:substring(mtVo.title,0,8)}..."/>--%>
													<%--</a>--%>
												<%--</c:when>--%>
												<%--<c:otherwise>--%>
													<%--<a style="text-decoration: none;" title="${mtVo.title}" onclick="showTitle(this)">--%>
														<%--<c:out value="${mtVo.title}"/>--%>
													<%--</a>--%>
												<%--</c:otherwise>--%>
											<%--</c:choose>--%>
										<%--</td>--%>
										<td><c:out value="${mtVo.taskId}" default="-"/></td>
										<%--<td><c:out value="${mtVo.degree}" default="-"/><emp:message key="rms_taskrecord_degree_p" defVal="档" fileName="rms"/></td>--%>
										<td><c:out value="${mtVo.taskType == 1 ? mtVo.effCount :'-'}" default="-"/></td>
										<td><c:out value="${mtVo.sendStatus}" default="-"/></td>
										<td><c:out value="${fn:substring(mtVo.submitTime, 0, 19)}" default="-"/></td>
										<td><c:out value="${fn:substring(mtVo.timerTime, 0, 19)}" default="-"/></td>
										<td>
											<c:choose>
												<c:when test="${empty requestScope.phoneLookPri || mtVo.taskType == 2}">-</c:when>
												<c:otherwise>
													<a href="javascript:checkFileOuter('${mtVo.mobileUrl}','${pageContext.request.contextPath}')"><emp:message key="common_view" defVal="查看" fileName="common"/></a>
													&nbsp;
													<a href="javascript:downloadFilesOuter('${mtVo.mobileUrl}','${pageContext.request.contextPath}')"><emp:message key="common_download" defVal="下载" fileName="common"/></a>
												</c:otherwise>
											</c:choose>
										</td>
										<td>
											<c:choose>
												<c:when test="${mtVo.timerStatus == 1}">
													<c:if test="${mtVo.sendState == 0}">
														<c:choose>
															<c:when test="${mtVo.subState == 3}">- -</c:when>
															<c:otherwise>
																<a title="<emp:message key="common_revoke" defVal="撤销任务" fileName="rms"/>" href="javascript:cancelTimer('${mtVo.taskId}')"><emp:message key="common_revoke" defVal="撤销任务" fileName="rms"/></a>
															</c:otherwise>
														</c:choose>
													</c:if>
													<c:if test="${mtVo.sendState == 2 && mtVo.userId == requestScope.currUserId && mtVo.isRetry == 1}">
														<emp:message key="rms_taskrecord_ybf" defVal="已补发" fileName="rms"/>
													</c:if>
													<c:if test="${mtVo.sendState == 2 && mtVo.userId == requestScope.currUserId && mtVo.isRetry == 0}">
														<a name="rsend" href="javascript:reSend('${mtVo.taskId}')"><emp:message key="rms_taskrecord_sbbf" defVal="失败补发" fileName="rms"/></a>
													</c:if>
													<c:if test="${mtVo.sendState == 1}">
														- -
													</c:if>
												</c:when>
												<c:otherwise>
													<c:if test="${mtVo.isRetry == 1}">
														<emp:message key="rms_taskrecord_ybf" defVal="已补发" fileName="rms"/>
													</c:if>
													<c:if test="${mtVo.sendState == 2 && mtVo.userId == requestScope.currUserId && mtVo.isRetry != 1}">
														<a name="rsend" href="javascript:reSend('${mtVo.taskId}')"><emp:message key="rms_taskrecord_sbbf" defVal="失败补发" fileName="rms"/></a>
													</c:if>
													<c:if test="${mtVo.sendState == 1}">
														- -
													</c:if>
												</c:otherwise>
											</c:choose>
										</td>
									</tr>
								</c:forEach>
							</c:if>
						</tbody>
						<tfoot>
							<tr>
								<td colspan="14">
									<div id="pageInfo"></div>
								</td>
							</tr>
						</tfoot>
					</table>
				</form>
			</div>
			<div id="detailDialog" style="padding: 0px; display: none; width: 550px;">
			</div>
			<%-- 内容结束 --%>
			<%--<div id="modify" title="<emp:message key='rms_taskrecord_msgContent' defVal='信息内容' fileName='common'/>"  style="padding:5px;width:300px;height:160px;display:none">--%>
				<%--<table width="100%">--%>
					<%--<thead>--%>
						<%--<tr style="padding-top:2px;margin-bottom: 2px;">--%>
							<%--<td style='word-break: break-all;'>--%>
<%--								用label显示短信内容<span><label id="msgcont" style="width:100%;height:100%"><xmp style='word-break: break-all;white-space:normal;'></xmp></label></span>--%>
								<%--<span><textarea id="msgcont" style="width:100%;height:100%;border:0px;" rows="15" readonly="readonly"></textarea></span>--%>
							<%--</td>--%>
						<%--</tr>--%>
					<%--</thead>--%>
				<%--</table>--%>
			<%--</div>--%>
			<%-- foot开始 --%>
			<div class="bottom">
				<div id="bottom_right">
					<div id="bottom_left"></div>
					<div id="bottom_main"></div>
				</div>
			</div>
		</div>
		<div class="tempView" id="tempView" style="display: none">
			<div class="phone_background">
				<div class="preview_wrapper rollBak">
					<div id="cust_preview"></div>
				</div>
			</div>
		</div>
	<div id="showTitle" title="<emp:message key="rms_taskrecord_sendtopic" defVal="发送主题" fileName="rms"/>"></div>
	<%--<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.JSP_IMP_VERSION %>"></script>--%>
	<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-c.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/layer/layer.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=empLangName%>/common_<%=empLangName%>.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=empLangName%>/rms_<%=empLangName%>.js"></script>
	<%--<script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.JSP_IMP_VERSION %>"></script>--%>
	<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/jquery-ui.min.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.ztree-myjquery-b.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/checkFile.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath %>/rms/rmstask/js/rmsTaskCommon.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath%>/rms/rmstask/js/rmsTaskRecord.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript">
		$(document).ready(function() {
            showPageInfo2(<c:out value="${requestScope.pageInfo.totalPage}" default="1"/>,<c:out value="${requestScope.pageInfo.pageIndex}" default="1"/>,<c:out value="${requestScope.pageInfo.pageSize}" default="15"/>,<c:out value="${requestScope.pageInfo.totalRec}" default="0"/>,[5,10,15]);
		});
	</script>
	</body>
</html>
