<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page import="java.util.Map"%>
<%@ page import="com.montnets.emp.common.constant.ViewParams"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils"%>
<%
    String iPath = request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
    String inheritPath = iPath.substring(0, iPath.lastIndexOf("/"));
    String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
    Map<String, String> titleMap = (Map<String, String>) session.getAttribute("titleMap");
    String menuCode = titleMap.get("sendDetail");
    //获取值
    String skin = session.getAttribute("stlyeSkin") == null ? "default" : (String) session.getAttribute("stlyeSkin");
    String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title><%=titleMap.get(menuCode) %></title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <%@ include file="/common/common.jsp"%>
    <link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
    <link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
    <link href="<%=skin%>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
    <link href="<%=skin%>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
    <%if(StaticValue.ZH_HK.equals(empLangName)){%>
    <link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
    <link rel="stylesheet" type="text/css" href="<%=skin%>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
    <%}%>
    <link href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
    <link href="<%=skin%>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css">
    <link href="<%=commonPath%>/common/widget/zTreeStyle/zTreeStyle.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
    <link href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>

    <style type="text/css">
        .phone_background{
            margin-left: 20px;
            margin-top: 10px;
            height: 592px;
            background: url("<%=commonPath%>/rms/samemms/img/phone_icon.png")no-repeat;
        }
        .preview_wrapper{
            width: 294px;
            position: relative;
            top: 48px;
            left: 12px;
            height: 498px;
            overflow-y: auto;
        }
        /*-------------滚动条样式------------------*/
        /*滚动条整体样式*/
        .rollBak::-webkit-scrollbar {
            /*高宽分别对应横竖滚动条的尺寸*/
            width: 2px;
            height: 2px;
        }
        /*滚动条里面小方块*/
        .rollBak::-webkit-scrollbar-thumb {
            border-radius: 5px;
            -webkit-box-shadow: inset 0 0 5px rgba(0,0,0,0.2);
            background: #0F3A56;
        }
        /*滚动条里面轨道*/
        .rollBak::-webkit-scrollbar-track {
            -webkit-box-shadow: inset 0 0 5px rgba(0,0,0,0.2);
            border-radius: 0;
            background: rgba(0,0,0,0.1);
        }
    </style>
</head>
<body id="rms_sendDetail">
<div id="container" class="container">

    <%--当前位置  --%>
    <%=ViewParams.getPosition(langName,menuCode) %>
    <%--当前位置结束  --%>

    <div id="rContent" class="rContent">
        <form name="pageForm" action="rms_sendDetail.htm?method=findOperatorRms" method="post" id="pageForm">
            <div id="hiddenValueDiv" style="display: none"></div>
            <div class="buttons">
                <%--隐藏搜索--%>
                <div id="toggleDiv"></div>
                <a id="exportCondition" onclick="downDetailRpt('${requestScope.countSize}',${requestScope.pageInfo.totalRec})"><emp:message key="rms_fxapp_degreerep_export" defVal="导出" fileName="rms"/></a>
            </div>
            <%--查询--%>
            <div id="condition">
                <table>
                    <tbody>
                    <tr>
                        <td>
                            <emp:message key="rms_taskrecord_recotype" defVal="记录类型：" fileName="rms"/>
                        </td>
                        <td>
                            <select name="recordType" id="recordType" isInput="false">
                                <option value="realTime" <c:out value="${requestScope.queryDataMap['recordType']=='realTime'?'selected':''}"/>><emp:message key="rms_taskrecord_realtimerecord" defVal="实时记录" fileName="rms"/></option>
                                <option value="history" <c:out value="${requestScope.queryDataMap['recordType']=='history'?'selected':''}"/>><emp:message key="rms_taskrecord_historyrecord" defVal="历史记录" fileName="rms"/></option>
                            </select>
                        </td>
                        <td>
                            <emp:message key="rms_taskrecord_rwpc" defVal="任务批次：" fileName="rms"/>
                        </td>
                        <td>
                            <input type="text" value="<c:out value="${requestScope.queryDataMap['taskId']}" default=""/>" id="taskId" name="taskId" onkeyup="numberControl($(this))"  onblur="numberControl($(this))" maxlength="16"/>
                        </td>
                        <td>
                            <emp:message key="rms_fxapp_fsmx_sjhm" defVal="手机号码：" fileName="rms"/>
                        </td>
                        <td>
                            <input type="text" value="<c:out value="${requestScope.queryDataMap['phone']}" default=""/>" id="phone" name="phone" maxlength="21" onkeyup="phoneInputCtrl($(this))"/>
                        </td>
                        <td class="tdSer">
                            <center><a id="search"></a></center>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <emp:message key="rms_fxapp_fsmx_ztm" defVal="状态码：" fileName="rms"/>
                        </td>
                        <td>
                            <input type="text" value="<c:out value="${requestScope.queryDataMap['statusCode']}" default=""/>" id="statusCode" name="statusCode" maxlength="7"/>
                        </td>

                        <td><emp:message key="rms_fxapp_myscene_fxtheme" defVal="富信主题：" fileName="rms"/></td>
                        <td><input type="text" value="<c:out value="${requestScope.queryDataMap['rmsSubject']}" default=""/>" id="rmsSubject" name="rmsSubject"/></td>

                        <td>
                            <emp:message key="rms_fxapp_fxsend_sendtopic" defVal="发送主题：" fileName="rms"/>
                        </td>
                        <td>
                            <input type="text" value="<c:out value="${requestScope.queryDataMap['sendSubject']}" default=""/>" id="sendSubject" name="sendSubject"/>
                        </td>
                        <td>&nbsp;</td>
                    </tr>
                    <tr>
                        <td><emp:message key="rms_fxapp_fxsend_bstype" defVal="业务类型：" fileName="rms"/></td>
                        <td>
                            <select name="busType" id="busType" isInput="false">
                                <option value="" <c:out value="${empty requestScope.queryDataMap['busType']?'selected':''}"/>><emp:message key="common_whole" defVal="全部" fileName="common"/></option>
                                <option value="-1" <c:out value="${requestScope.queryDataMap['busType']== '-1'?'selected':''}"/>><emp:message key="rms_taskrecord_nobiztype" defVal="无业务类型" fileName="rms"/></option>
                                <c:forEach items="${requestScope.busTypeList}" var="busType">
                                    <c:if test="${busType.busCode == requestScope.queryDataMap['busType']}">
                                        <option value="${busType.busCode}" selected>${busType.busName}</option>
                                    </c:if>
                                    <c:if test="${busType.busCode != requestScope.queryDataMap['busType']}">
                                        <option value="${busType.busCode}">${busType.busName}</option>
                                    </c:if>
                                </c:forEach>
                            </select>
                        </td>

                        <td><emp:message key="rms_fxapp_fxsend_sendtime" defVal="发送时间：" fileName="rms"/></td>
                        <td class="tableTime">
                            <input type="text" class="Wdate" readonly="readonly" onclick="sedtime()" value="<c:out value="${requestScope.queryDataMap['sendTime']}" default=""/>" id="sendTime" name="sendTime">
                        </td>
                        <td>
                            <emp:message key="rms_fxapp_degreerep_to" defVal="至：" fileName="rms"/>
                        </td>
                        <td>
                            <input type="text" class="Wdate" readonly="readonly" onclick="revtime()" value="<c:out value="${requestScope.queryDataMap['recvTime']}" default=""/>" id="recvTime" name="recvTime">
                        </td>
                        <td>&nbsp;</td>
                    </tr>

                    <tr>
                        <td><emp:message key="rms_fxapp_fxsend_spact" defVal="SP帐号：" fileName="rms"/></td>
                        <td>
                            <select name="spUser" id="spUser">
                                <option value="" <c:out value="${empty requestScope.queryDataMap['spUser']?'selected':''}"/>><emp:message key="common_whole" defVal="全部" fileName="common"/></option>
                                <c:forEach items="${requestScope.spList}" var="spUser">
                                    <c:if test="${spUser.spUser == requestScope.queryDataMap['spUser']}">
                                        <option value="${spUser.spUser}" selected>${spUser.spUser}</option>
                                    </c:if>
                                    <c:if test="${spUser.spUser != requestScope.queryDataMap['spUser']}">
                                        <option value="${spUser.spUser}">${spUser.spUser}</option>
                                    </c:if>
                                </c:forEach>
                            </select>
                        </td>

                        <c:choose>
                            <c:when test="${corpCode == '100000'|| ((corpCode != '100000') && (isRptFlag ==3)) }">
                                <td>
                                    <emp:message key="rms_taskrecord_dlstatus2" defVal="下载状态：" fileName="rms"/>
                                </td>
                                <td>
                                    <select name="downStatus" id="downStatus" isInput="false">
                                        <option value="" <c:out value="${empty requestScope.queryDataMap['downStatus']?'selected':''}"/>><emp:message key="common_whole" defVal="全部" fileName="common"/></option>
                                        <option value="0" <c:out value="${requestScope.queryDataMap['downStatus']=='0'?'selected':''}"/>><emp:message key="rms_fxapp_fsmx_succeed" defVal="成功" fileName="rms"/></option>
                                        <option value="1" <c:out value="${requestScope.queryDataMap['downStatus']=='1'?'selected':''}"/>><emp:message key="rms_fxapp_fsmx_failure" defVal="失败" fileName="rms"/></option>
                                        <option value="2" <c:out value="${requestScope.queryDataMap['downStatus']=='2'?'selected':''}"/>><emp:message key="rms_fxapp_fsmx_unback" defVal="未返" fileName="rms"/></option>
                                    </select>
                                </td>
                            </c:when>
                            <c:otherwise>
                                <td></td>
                                <td></td>
                            </c:otherwise>
                        </c:choose>

                        <td></td>
                        <td></td>
                        <td>&nbsp;</td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <%--查询显示--%>
            <table id="content">
                <thead>
                <tr>
                    <th width="5%"><emp:message key="rms_taskrecord_spaccount" defVal="sp账号" fileName="rms"/></th>
                    <th width="5%"><emp:message key="rms_taskrecord_spgate" defVal="通道" fileName="rms"/></th>
                    <th width="8%"><emp:message key="rms_taskrecord_sendtopic" defVal="发送主题" fileName="rms"/></th>
                    <th width="5%"><emp:message key="rms_taskrecord_bstype" defVal="业务类型" fileName="rms"/></th>
                    <th width="8%"><emp:message key="rms_taskrecord_fuxintopic" defVal="富信主题" fileName="rms"/></th>
                    <th width="5%"><emp:message key="rms_taskrecord_sceneid" defVal="场景ID" fileName="rms"/></th>
                    <th width="4%"><emp:message key="rms_taskrecord_degree" defVal="档位" fileName="rms"/></th>
                    <th width="4%"><emp:message key="rms_taskrecord_operator" defVal="运营商" fileName="rms"/></th>
                    <th width="5%"><emp:message key="rms_taskrecord_phonenum" defVal="手机号码" fileName="rms"/></th>
                    <th width="5%"><emp:message key="rms_taskrecord_taskbatch" defVal="任务批次" fileName="rms"/></th>
                    <th width="4%"><emp:message key="rms_taskrecord_recstatus" defVal="接收状态" fileName="rms"/></th>
                    <c:if test="${corpCode == '100000'|| ((corpCode != '100000') && (isRptFlag ==3))}"><th width="4%"><emp:message key="rms_taskrecord_dlstatus" defVal="下载状态" fileName="rms"/></th></c:if>
                    <th width="8%"><emp:message key="rms_taskrecord_sendtime" defVal="发送时间" fileName="rms"/></th>
                    <th width="8%"><emp:message key="rms_taskrecord_rectime" defVal="接收时间" fileName="rms"/></th>
                    <c:if test="${corpCode == '100000'|| ((corpCode != '100000') && (isRptFlag ==3))}"><th width="8%"><emp:message key="rms_taskrecord_dltime" defVal="下载时间" fileName="rms"/></th></c:if>
                    <th width="8%"><emp:message key="rms_taskrecord_selfseq" defVal="自定义流水号" fileName="rms"/></th>
                    <th width="8%"><emp:message key="rms_taskrecord_operatorseq" defVal="运营商流水号" fileName="rms"/></th>
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
                <c:if test="${requestScope.countSize > 0 && !(empty requestScope.rmsMtRecords)}">
                    <c:forEach items="${requestScope.rmsMtRecords}" var="mtVo">
                        <tr>
                            <td style="display: none;"><c:out value="${mtVo.tmId}" default=""/></td>
                            <td><c:out value="${mtVo.spUser}" default="-"/></td>
                            <td><c:out value="${mtVo.spGate}" default="-"/></td>
                            <td>
                                <c:set var="sendSubject" value="${mtVo.sendSubject}"/>
                                <c:choose>
                                    <c:when test="${empty sendSubject}">
                                        <c:out value="-"/>
                                    </c:when>
                                    <c:when test="${fn:length(sendSubject) > 4}">
												<span id="showMsg" title="${sendSubject}">
													<c:out value="${fn:substring(sendSubject,0,4)}..." default="-"/>
												</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span id="showMsg" title="${sendSubject}">${sendSubject}</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td><c:out value="${mtVo.busTypeName}" default="-"/></td>
                            <td>
                                <c:set var="rmsSubject" value="${mtVo.rmsSubject}"/>
                                <c:choose>
                                    <c:when test="${empty rmsSubject}">
                                        <c:out value="-"/>
                                    </c:when>
                                    <c:when test="${fn:length(rmsSubject) > 4}">
                                        <a id="showMsg" style="text-decoration: none;" title="${rmsSubject}" href="javaScript:void(0)" onclick="showTempView($(this).parent().parent().find('td:eq(0)').text())">
                                            <c:out value="${fn:substring(rmsSubject,0,4)}..." default="-"/>
                                        </a>
                                    </c:when>
                                    <c:otherwise>
                                        <a id="showMsg" style="text-decoration: none;" href="javaScript:void(0)" onclick="showTempView($(this).parent().parent().find('td:eq(0)').text())" title="${rmsSubject}">${rmsSubject}</a>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td><c:out value="${mtVo.tmplId}" default="-"/></td>
                            <td><c:out value="${mtVo.degree}档" default="-"/></td>
                            <td><c:out value="${mtVo.unicomName}" default="-"/></td>
                            <td><c:out value="${mtVo.phone}" default="-"/></td>
                            <td><c:out value="${mtVo.taskId}" default="-"/></td>
                            <td><c:out value="${mtVo.receStatus}" default="-"/></td>
                            <c:if test="${corpCode == '100000'|| ((corpCode != '100000') && (isRptFlag ==3))}"><td><c:out value="${mtVo.downStatus}" default="-"/></td></c:if>
                            <td><c:out value="${mtVo.sendTime}" default="-"/></td>
                            <td><c:out value="${mtVo.recvTime}" default="-"/></td>
                            <c:if test="${corpCode == '100000'|| ((corpCode != '100000') && (isRptFlag ==3))}"><td><c:out value="${mtVo.downTime}" default="-"/></td></c:if>
                            <td><c:out value="${mtVo.custId}" default="-"/></td>
                            <td><c:out value="${mtVo.ptmsgid}" default="-"/></td>
                        </tr>
                    </c:forEach>
                </c:if>
                </tbody>
                <tfoot>
                <tr>
                    <td colspan="16"><div id="pageInfo"></div></td>
                </tr>
                </tfoot>
            </table>
        </form>
    </div>
</div>
<%-- 内容结束 --%>
<%-- foot开始 --%>
<div class="bottom">
    <div id="bottom_right">
        <div id="bottom_left"></div>
        <div id="bottom_main"></div>
    </div>
</div>
<%--富信预览--%>
<div class="tempView" id="tempView" style="display: none">
    <div class="phone_background">
        <div class="preview_wrapper rollBak">
            <div id="cust_preview"></div>
        </div>
    </div>
</div>
<script type="text/javascript"
	src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
<script type="text/javascript"
	src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
<script type="text/javascript"
	src="<%=commonPath%>/common/i18n/<%=langName%>/rms_<%=langName%>.js"></script>
<%--<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.JSP_IMP_VERSION %>"></script>--%>
<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-c.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=commonPath%>/common/js/layer/layer.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<%--<script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.JSP_IMP_VERSION %>"></script>--%>
<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/jquery-ui.min.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.ztree-myjquery-b.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=commonPath%>/common/js/jquery_Ul_Send.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=commonPath %>/rms/detailsend/js/rms_sendDetailCommon.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=commonPath%>/rms/detailsend/js/rms_sendDetail.js?V=<%=StaticValue.getJspImpVersion() %>"></script>

<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=empLangName%>/common_<%=empLangName%>.js"></script>
<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=empLangName%>/rms_<%=empLangName%>.js"></script>

<script type="text/javascript">
    //页面加载，初始化相关数据
    $(document).ready(function() {
        showPageInfo2(<c:out value="${requestScope.pageInfo.totalPage}" default="1"/>,<c:out value="${requestScope.pageInfo.pageIndex}" default="1"/>,<c:out value="${requestScope.pageInfo.pageSize}" default="15"/>,<c:out value="${requestScope.pageInfo.totalRec}" default="0"/>,[5,10,15]);
    });
    //-------------模板预览---------------
    function tempPreview(tmId) {
        $("#tempView").dialog({
            modal:true,
            title:"预览",
            closeOnEscape: true,
            height:660,
            width:360
        });
        $("#ui-dialog-title-tempView").parent().parent().css("border-radius","10px");
        $.post("rms_sendDetail.htm?method=tempPreview",
            {
                tmId:tmId
            },
            function(result){
                if (result != null && result !== "null" && result !== "") {
                    $("#cust_preview").html(result);
                } else {
                    alert("内容文件不存在，无法预览！");
                    $("#cust_preview").html("");
                }
            });
        $("#tempView").dialog("open");
    }
</script>
</body>
</html>