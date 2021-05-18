<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page import="java.util.Map" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("rmsTaskHistory");
	menuCode = menuCode==null?"0-0-0":menuCode;
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<title>群发历史查询</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<%@include file="/common/common.jsp" %>
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet"/>
		<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
		<link href="<%=commonPath%>/common/css/reading.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/widget/zTreeStyle/zTreeStyle.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
        <link href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
		<link href="<%=skin%>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin%>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
        <%if(StaticValue.ZH_HK.equals(empLangName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
			<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
		<link href="<%=commonPath%>/rms/rmstask/css/rmsTaskRecord.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
	</head>
	<body id="rmsHistoryRecord" class="rmsHistoryRecord">
		<div id="container" class="container">
			<%-- 当前位置 --%>
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(empLangName,menuCode) %>
			<%-- 内容开始 --%>
			<div id="rContent" class="rContent">
				<form name="pageForm" action="rms_rmsTaskHistory.htm" method="post" id="pageForm">
					<div id="hiddenValueDiv" style="display: none"></div>
					<div class="buttons">
						<div id="toggleDiv">
						</div>
						<a id="exportCondition" onclick="downRptExcel('<c:out value="${requestScope.countSize}" default="0"/>','<c:out value="${requestScope.pageInfo.totalRec}" default="0"/>','rmsTaskHis')"><emp:message key="common_export" defVal="导出" fileName="common"/></a>
					</div>
					<div id="condition">
						<table>
							<tbody>
								<tr>
									<td>
										<emp:message key="common_organization" defVal="隶属机构" fileName="common"/>：
									</td>
									<td class="condi_f_l">
											<div class="condi_f_l_1">
												<input type="hidden" id="deptid" name="depid" value="${empty requestScope.mtVo.depIds ? "":requestScope.mtVo.depIds}"/>
                                                <input type="text" class="treeInput" id="depNam" name="depNam"
                                                       value="${empty requestScope.mtVo.depName ? "请选择":requestScope.mtVo.depName}"
                                                       onclick="showDepMenu();" readonly/>&nbsp;
											</div>
											<div id="dropMenu" >
												<iframe frameborder="0" src="about:blank"></iframe>
												<div class="dropMenuDiv">
													<input style="margin-left:90px;" type="checkbox" title="" id="isContainsSun" name="isContainsSun" value="1" <c:if test="${requestScope.mtVo.iscontainsSun == true}">checked="checked"</c:if>/><emp:message key="common_containsSun" defVal="包含子机构" fileName="common"/>&nbsp;&nbsp;
													<input type="button" value="<emp:message key='common_confirm' defVal='确定' fileName='common'/>" class="btnClass1" onclick="zTreeDepOnClickOK();" />&nbsp;&nbsp;
													<input type="button" value="<emp:message key='common_clean' defVal='清空' fileName='common'/>" class="btnClass1" onclick="zTreeDepOnClickClean();" />
												</div>
												<ul  id="dropdownMenu" class="tree"></ul>
											</div>
									</td>
									<td>
										<emp:message key="common_operator" defVal="操作员" fileName="common"/>：
									</td>
									<td class="condi_f_l">
											<div class="condi_f_l_1">
												<input type="hidden" id="userid" name="userid" value="${empty requestScope.mtVo.userIds ? "":requestScope.mtVo.userIds}"/>
                                                <input type="text" id="userName" class="treeInput" name="userName"
                                                       value="${empty requestScope.mtVo.userName ? "请选择":requestScope.mtVo.userName}"
                                                       onclick="showUserMenu();" readonly/>&nbsp;
											</div>
											<div id="dropMenu2">
												<iframe frameborder="0" src="about:blank"></iframe>
												<div class="dropMenu2_div1">
													<input type="button" value="<emp:message key='common_confirm' defVal='确定' fileName='common'/>" class="btnClass1" onclick="zTreeUserOnClickOK();" />&nbsp;&nbsp;
													<input type="button" value="<emp:message key='common_clean' defVal='清空' fileName='common'/>" class="btnClass1" onclick="zTreeUserOnClickClean();" />
												</div>
												<div class="dropMenu2_div2"><font class="zhu"><emp:message key="common_chooseOperator" defVal="注：请勾选操作员进行查询" fileName="common"/></font></div>
												<ul  id="dropdownMenu2" class="tree"></ul>
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
									<td><emp:message key="rms_taskrecord_sendStatus" defVal="发送状态" fileName="rms"/>：</td>
									<td>
									<label>
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
									</label>
								   </td>
									<td><emp:message key="rms_taskrecord_rwpc2" defVal="任务批次" fileName="rms"/>：</td>
									<td>
										<input type="text" id="taskId" name ="taskId" onkeyup="numberControl($(this))" value="${empty requestScope.mtVo.taskId ? "":requestScope.mtVo.taskId}" maxlength="19">
									</td>
									<%--<td><emp:message key="rms_fxapp_degreerep_range" defVal="档位" fileName="rms"/>：</td>--%>
									<%--<td>--%>
										<%--<input type="text" id="degree" name ="degree" onkeyup="numberControl($(this))" value="${empty requestScope.mtVo.degree ? "":requestScope.mtVo.degree}">--%>
									<%--</td>--%>
									<td><emp:message key="rms_fxapp_tempchoose_fxtopic" defVal="富信主题" fileName="rms"/>：</td>
									<td><input type="text" id="tmName" name ="tmName" value="${empty requestScope.mtVo.tmName ? "":requestScope.mtVo.tmName}"></td>
								</tr>
								<tr>
									<td>
										<emp:message key="rms_fxapp_dwtjbb_cjsj" defVal="创建时间" fileName="rms"/>：
								   </td>
								   <td>
										<input type="text" readonly="readonly" class="Wdate" onclick="stime()" value="${requestScope.sendTime}"  title="" id="sendtime" name="sendtime"/>
								   </td>
								   <td><emp:message key="text_to_p" defVal="至" fileName="common"/>：</td>
								   <td>
										<input type="text" readonly="readonly" class="Wdate"  onclick="rtime()" value="${requestScope.recvTime}" title="" id="recvtime" name="recvtime">
									</td>
                                    <td colspan="3">&nbsp;</td>
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
									<emp:message key="rms_fxapp_tempchoose_fxtopic" defVal="富信主题" fileName="rms"/>
								</th>
								<th>
									<emp:message key="rms_taskrecord_sendtopic" defVal="发送主题" fileName="rms"/>
								</th>
								<th>
									<emp:message key="rms_taskrecord_rwpc2" defVal="任务批次" fileName="rms"/>
								</th>
								<th>
									<emp:message key="rms_fxapp_fsmx_fssj" defVal="发送时间" fileName="rms"/>
								</th>
								<th>
									<emp:message key="rms_taskrecord_sendStatus" defVal="发送状态" fileName="rms"/>
								</th>
								<%--<th>--%>
									<%--<emp:message key="rms_fxapp_degreerep_range" defVal="档位" fileName="rms"/>--%>
								<%--</th>--%>
								<th>
									<emp:message key="rms_taskrecord_hmgs" defVal="号码个数" fileName="rms"/>
								</th>
								<th>
									<emp:message key="rms_fxapp_degreerep_fscgs" defVal="发送成功数" fileName="rms"/>
								</th>
								<th>
									<emp:message key="rms_taskrecord_failSubmit" defVal="提交失败数" fileName="rms"/>
								</th>
								<th>
									<emp:message key="rms_fxapp_degreerep_jssbs" defVal="接收失败数" fileName="rms"/>
								</th>
								<th>
									<emp:message key="rms_taskrecord_DelayNum" defVal="滞留数" fileName="rms"/>
								</th>
								<th style="width: 8%;">
									<emp:message key="rms_taskrecord_sendFile" defVal="发送文件" fileName="rms"/>
								</th>
								<th>
									<emp:message key="common_operation" defVal="操作" fileName="common"/>
								</th>
								<th>
									<emp:message key="rms_taskrecord_sendDetail" defVal="发送详情" fileName="rms"/>
								</th>
							</tr>
						</thead>
						<tbody>
							<c:if test="${empty requestScope.countSize}">
								<tr>
									<td align="center" colspan="18"><emp:message key="common_clickGetData" defVal="请点击查询获取数据" fileName="common"/></td>
								</tr>
							</c:if>
							<c:if test="${requestScope.countSize == 0}">
								<tr>
									<td align="center" colspan="18"><emp:message key="common_norecord" defVal="无记录" fileName="common"/></td>
								</tr>
							</c:if>
							<c:if test="${requestScope.countSize == -1}">
								<tr>
									<td align="center" colspan="18"><emp:message key="common_errorPage" defVal="=页面加载有误，请刷新页面重试。" fileName="common"/></td>
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
									<td>
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
									</td>
									<td>
										<c:choose>
											<c:when test="${empty mtVo.title}">-</c:when>
											<c:when test="${fn:length(mtVo.title) > 8}">
												<a style="text-decoration: none;" title="${mtVo.title}" onclick="showTitle(this)">
													<c:out value="${fn:substring(mtVo.title,0,8)}..."/>
												</a>
											</c:when>
											<c:otherwise>
												<a style="text-decoration: none;" title="${mtVo.title}" onclick="showTitle(this)">
													<c:out value="${mtVo.title}"/>
												</a>
											</c:otherwise>
										</c:choose>
									</td>
									<td><c:out value="${mtVo.taskId}" default="-"/></td>
									<td><c:out value="${fn:substring(mtVo.timerTime, 0, 19)}" default="-"/></td>
									<td><c:out value="${mtVo.sendStatus}" default="-"/></td>
									<%--<td><c:out value="${mtVo.degree}" default="-"/><emp:message key="rms_taskrecord_degree_p" defVal="档" fileName="rms"/></td>--%>
									<td><c:out value="${mtVo.taskType == 1 ? mtVo.effCount :'-'}" default="-"/></td>
									<td>
										<c:choose>
											<c:when test="${empty mtVo.sendState}">-</c:when>
											<c:when test="${mtVo.sendState == 2}">0</c:when>
											<c:otherwise><c:out value="${mtVo.icount2 - mtVo.faiCount}" default="-"/></c:otherwise>
										</c:choose>
									</td>
									<td>
										<c:out value="${mtVo.sendState == 2 ? (empty mtVo.icount ? '-':mtVo.icount) :(empty mtVo.icount2 ? '-':mtVo.faiCount)}" default="-"/>
									</td>
									<td>
										<c:out value="${mtVo.sendState == 2 ? '0':(empty mtVo.icount2 ? '-':(empty mtVo.rfail2 ? '-':mtVo.rfail2))}" default="-"/>
									</td>
									<td>
										<c:out value="${mtVo.delayNum}" default="0"/>
									</td>
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
									<td>
										<c:choose>
											<c:when test="${mtVo.sendState == 1}">
												<a onclick="location.href='rms_rmsTaskHistory.htm?method=findAllSendInfo&mtId=${mtVo.mtIdCipher}'"><emp:message key="common_view" defVal="查看" fileName="common"/></a>
											</c:when>
											<c:otherwise>-</c:otherwise>
										</c:choose>
									</td>
								</tr>
							</c:forEach>
							</c:if>
						</tbody>
						<tfoot>
							<tr>
								<td colspan="18">
									<div id="pageInfo"></div>
								</td>
							</tr>
						</tfoot>
					</table>
				</form>
			</div>
			<div id="detailDialog"></div>
			<%-- 内容结束 --%>
			<%-- foot开始 --%>
			<div class="bottom">
				<div id="bottom_right">
					<div id="bottom_left"></div>
					<div id="bottom_main"></div>
				</div>
			</div>
		</div>
    <div class="clear"></div>
	<div class="tempView" id="tempView" style="display: none">
		<div class="phone_background">
			<div class="preview_wrapper rollBak">
				<div id="cust_preview"></div>
			</div>
		</div>
	</div>
	<%--<div id="showTitle" title="<emp:message key="rms_taskrecord_sendtopic" defVal="发送主题" fileName="rms"/>"></div>--%>
	<%--<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.JSP_IMP_VERSION %>"></script>--%>
	<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-c.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/layer/layer.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=empLangName%>/rms_<%=empLangName%>.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=empLangName%>/common_<%=empLangName%>.js"></script>
    <%--<script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.JSP_IMP_VERSION %>"></script>--%>
	<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/jquery-ui.min.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.ztree-myjquery-b.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/checkFile.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath %>/rms/rmstask/js/rmsTaskCommon.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath%>/rms/rmstask/js/rmsHistoryRecord.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript">
		//页面加载，初始化相关数据
		$(document).ready(function() {
			showPageInfo2(<c:out value="${requestScope.pageInfo.totalPage}" default="1"/>,<c:out value="${requestScope.pageInfo.pageIndex}" default="1"/>,<c:out value="${requestScope.pageInfo.pageSize}" default="15"/>,<c:out value="${requestScope.pageInfo.totalRec}" default="0"/>,[5,10,15]);
		});
	</script>
	</body>
</html>
