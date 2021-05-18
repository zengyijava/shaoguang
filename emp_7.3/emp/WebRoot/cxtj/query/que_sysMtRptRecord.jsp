<%@ page contentType="text/html;charset=UTF-8" language="java"  pageEncoding="UTF-8" %>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.HashMap"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@include file="/common/common.jsp" %>
<c:set var="path" value="${pageContext.request.contextPath}" />
<%
    String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
    String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
    //按钮权限Map
    Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");
    if(btnMap == null)
    {
        btnMap = new HashMap<String,String>();
    }
    Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");

    if(titleMap == null)
    {
        titleMap = new HashMap<String,String>();
    }
    String menuCode = titleMap.get("mtRptRecord");
    menuCode = menuCode==null?"0-0-0":menuCode;

%>
<html>
<style type="text/css">
    .dg001 {
        border-width: 0px;
        position: absolute;
        left: 36px;
        top: 4px;
        width: 73px;
        height: 23px;
        font-size: 12px;
        background: inherit;
        background-color: rgba(102, 204, 204, 1);
        box-sizing: border-box;
        border-width: 1px;
        border-style: solid;
        border-color: rgba(242, 242, 242, 1);
        border-radius: 0px;
        -moz-box-shadow: none;
        -webkit-box-shadow: none;
        box-shadow: none;
        font-size: 12px;
        font-family: 'Arial Normal', 'Arial';
        font-weight: 400;
        font-style: normal;
        font-size: 13px;
        color: #333333;
        text-align: center;
        line-height: normal;
    }
    /*引入的left.css传统皮肤会影响页面样式，这里清除掉样式冲突*/
    .delbodycss{
        background-image:none;
    }
    .deltablecss{
        background-image:none;
    }
</style>
<head>
    <title><%=titleMap.get(menuCode) %></title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <link href="${path}/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
    <link href="${path}/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
    <link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
    <link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
    <link href="<%=skin %>/left.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
    <%if(StaticValue.ZH_HK.equals(langName)){%>
    <link rel="stylesheet" type="text/css" href="${path}/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
    <link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
    <%}%>
    <link rel="stylesheet" href="${path}/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>">
    <link href="${path}/common/css/select.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
    <link href="${path}/cxtj/common/css/inbox.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>

</head>
<body class="que_sysMtRecord delbodycss">
<div id="container" class="container">
    <%-- 当前位置 --%>
    <%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode) %>
        <%-- 内容开始 --%>
        <%if(btnMap.get(menuCode+"-0")!=null) { %>
        <div id="rContent" class="rContent">
            <form name="pageForm" action="${path}/que_mtRptRecord.htm?method=findPageList" method="post" id="pageForm">
                <div class="buttons">
                    <div id="toggleDiv" >
                    </div>
                    <a id="exportCondition" href="javascript:openExportDiv();"><emp:message key="cxtj_sjcx_xtxxdc_dc" defVal="导出" fileName="cxtj"/></a>
                </div>
                <div id="condition" >
                    <table class="deltablecss">
                        <tbody>
                            <tr>
                                <td>
                                    <emp:message key="cxtj_sjcx_xtxxdc_wjbt" defVal="文件标题" fileName="cxtj"/>：
                                </td>
                                <td>
                                    <input type="text" value='<c:if test="${not empty conditionMap}">${conditionMap["fileName"]}</c:if>' id="fileName" name="fileName" maxlength="50"/>
                                </td>

                                <td>
                                    <emp:message key="cxtj_sjcx_xtxxdc_czy" defVal="操作员：" fileName="cxtj"/>：
                                </td>
                                <td>
                                    <input type="text" value='<c:if test="${not empty conditionMap}">${conditionMap["name"]}</c:if>' id="name" name="name" maxlength="64"/>
                                </td>

                                <td>
                                    <emp:message key="cxtj_sjcx_xtxxdc_sczt" defVal="生成状态：" fileName="cxtj"/>：
                                </td>
                                <td>
                                    <select name="fileStatus" id="fileStatus" isInput="false" data-val="<c:if test='${not empty conditionMap}'>${conditionMap['fileStatus']}</c:if>">
                                        <option value="" <c:if test="${empty conditionMap['fileStatus']}">selected</c:if>><emp:message key="cxtj_sjcx_xtxxdc_qb" defVal="全部" fileName="cxtj"/></option>
                                        <option value="0" <c:if test="${not empty conditionMap['fileStatus'] and conditionMap['fileStatus'] == 0}">selected</c:if>><emp:message key="cxtj_sjcx_xtxxdc_ysc" defVal="已生成" fileName="cxtj"/></option>
                                        <option value="1" <c:if test="${conditionMap['fileStatus'] == 1}">selected</c:if>><emp:message key="cxtj_sjcx_xtxxdc_scz" defVal="生成中" fileName="cxtj"/></option>
                                        <option value="2" <c:if test="${conditionMap['fileStatus'] == 2}">selected</c:if>><emp:message key="cxtj_sjcx_xtxxdc_scsb" defVal="生成失败" fileName="cxtj"/></option>
                                    </select>
                                </td>

                                <td class="tdSer">
                                    <center><a id="search"></a></center>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <emp:message key="cxtj_sjcx_xtxxdc_scsj" defVal="生成时间：" fileName="cxtj"/>：
                                </td>
                                <td class="tableTime">
                                    <input type="text" class="Wdate" readonly="readonly" value="<c:if test='${not empty conditionMap}'>${conditionMap['startDate']}</c:if>" id="startDate" name="startDate" onclick="initStartDate('endDate')">
                                </td>
                                <td>
                                    <emp:message key="cxtj_sjcx_xtxxdc_z" defVal="至：" fileName="cxtj"/>：
                                </td>
                                <td>
                                    <input type="text" class="Wdate" readonly="readonly" value="<c:if test='${not empty conditionMap}'>${conditionMap['endDate']}</c:if>" id="endDate" name="endDate" onclick="initEndDate('startDate')">
                                </td>
                                <td></td>
                                <td></td>
                                <td>&nbsp;</td>
                            </tr>
                        </tbody>
                    </table>
                </div>
                <table id="content" class="deltablecss" style="background-color:white;">
                    <thead>
                    <tr>
                        <th><emp:message key="cxtj_sjcx_xtxxdc_wjbt" defVal="文件标题" fileName="cxtj"/> </th>
                        <th><emp:message key="cxtj_sjcx_xtxxdc_sczt" defVal="生成状态" fileName="cxtj"/></th>
                        <th><emp:message key="cxtj_sjcx_xtxxdc_cjsj" defVal="创建时间" fileName="cxtj"/></th>
                        <th><emp:message key="cxtj_sjcx_xtxxdc_scsj" defVal="生成时间" fileName="cxtj"/></th>
                        <th><emp:message key="cxtj_sjcx_xtxxdc_xzsj" defVal="下载时间" fileName="cxtj"/></th>
                        <th><emp:message key="cxtj_sjcx_xtxxdc_czy" defVal="操作员" fileName="cxtj"/></th>
                        <th><emp:message key="cxtj_sjcx_xtxxdc_cz" defVal="操作" fileName="cxtj"/></th>

                    </tr>
                    </thead>
                    <tbody>
                        <c:choose>
                            <c:when test="${isFirstEnter == true}">
                                <tr><td colspan="7" class="queryData"><emp:message key="cxtj_sjcx_xtxxdc_qdjcxhqsj" defVal="请点击查询获取数据" fileName="cxtj"/>
                                </td></tr>
                            </c:when>
                            <c:when test="${resultList == null or fn:length(resultList) == 0}">
                                <tr><td colspan="7" class="queryData"><emp:message key="cxtj_sjcx_xtxxdc_wjl" defVal="无记录" fileName="cxtj"/></td></tr>
                            </c:when>
                            <c:otherwise>
                                <c:forEach items="${resultList}" var="item">
                                    <tr>
                                        <%--dynabean必须通过get方法去取值--%>
                                        <td>${item['filename']}</td>
                                        <td id="${item['id']}">
                                            <c:choose>
                                                <c:when test="${item['filestatus'] eq 0}"><emp:message key="cxtj_sjcx_xtxxdc_ysc" defVal="已生成" fileName="cxtj"/></c:when>
                                                <c:when test="${item['filestatus'] eq 1}"><emp:message key="cxtj_sjcx_xtxxdc_scz" defVal="生成中" fileName="cxtj"/></c:when>
                                                <c:otherwise><emp:message key="cxtj_sjcx_xtxxdc_scsb" defVal="生成失败" fileName="cxtj"/></c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${empty item['createtime']}">-</c:when>
                                                <c:otherwise>${item['createtime']}</c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${item['filestatus'] eq 0}">
                                                    <c:choose>
                                                        <c:when test="${empty item['generatetime']}">-</c:when>
                                                        <c:otherwise>${item['generatetime']}</c:otherwise>
                                                    </c:choose>
                                                </c:when>
                                                <c:otherwise>
                                                    - -
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td class="downloadTime">
                                            <c:choose>
                                                <c:when test="${item['filestatus'] eq 0}">
                                                    <c:choose>
                                                        <c:when test="${empty item['downloadtime']}">- -</c:when>
                                                        <c:when test="${item['downloadtime'] == item['createtime']}">- -</c:when>
                                                        <c:otherwise>${item['downloadtime']}</c:otherwise>
                                                    </c:choose>
                                                </c:when>
                                                <c:otherwise>
                                                    - -
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>${item['optname']}</td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${item['filestatus'] eq 0}">
                                                    <a href="javascript:downloadFile('${item['id']}','${item['filename']}', this);">
                                                        <emp:message key="cxtj_sjcx_xtxxdc_xz" defVal="下载" fileName="cxtj"/>
                                                    </a>
                                                </c:when>
                                                <c:otherwise>
                                                    - -
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:otherwise>
                        </c:choose>
                    </tbody>
                    <tfoot>
                    <tr>
                        <td colspan="7">
                            <div id="pageInfo"></div>
                        </td>
                    </tr>
                    </tfoot>
                </table>
                <div id="r_sysMrparams" class="hidden"></div>
            </form>
        </div>
        <%}%>
        <%--导出的弹窗页面--%>
        <div id="exportDiv" style="display: none;background-color: white;" title="xxx">

            <form name="exportForm" action="${path}/export_sysMt.htm?method=export" method="post" id="exportForm">
                <div class="dg001" style="left: 36px; top: 0px;">
                    <div class="higehLisght" style="line-height: 22px;">
                        <p><span><emp:message key="cxtj_sjcx_xtxxdc_sxtj" defVal="筛选条件" fileName="cxtj"/></span></p>
                    </div>
                </div>
                <div id="condition" style="padding: 10px;height: 146px;">
                    <table class="deltablecss" style="height: 140px;margin-top: 7px;padding-top: 10px;">
                        <tbody style="width: 100%;height: 100%">
                        <tr>
                            <td>
                                <emp:message key="cxtj_sjcx_xtxxdc_jllx" defVal="记录类型：" fileName="cxtj"/>：
                            </td>
                            <td>
                                <select name="recordType" id="recordType" isInput="false" style="height: 20px !important;width: 150px !important;">
                                    <option value="realTime"><emp:message key="cxtj_sjcx_xtxxdc_ssjl" defVal="实时记录" fileName="cxtj"/></option>
                                    <option value="history"><emp:message key="cxtj_sjcx_xtxxdc_lljl" defVal="历史记录" fileName="cxtj"/></option>
                                </select>
                            </td>

                            <td>
                                <emp:message key="cxtj_sjcx_xtxxdc_rwpc" defVal="任务批次：" fileName="cxtj"/>：
                            </td>
                            <td>
                                <input type="text" value="" style="height: 20px !important;width: 150px !important;" id="taskid" name="taskid" onkeyup="numberControl($(this))"  onblur="numberControl($(this))" maxlength="16"/>
                            </td>

                            <td>
                                <emp:message key="cxtj_sjcx_xtxxdc_sjhm" defVal="手机号码：" fileName="cxtj"/>：
                            </td>
                            <td>
                                <input type="text" style="height: 20px !important;width: 150px !important;" value="" id="phone" name="phone" maxlength="21" onkeyup="phoneInputCtrl($(this))"/>
                            </td>

                        </tr>
                        <tr>
                            <td><emp:message key="cxtj_sjcx_xtxxdc_ywlx" defVal="业务类型：" fileName="cxtj"/>：</td>
                            <td>
                                <select id="busCode" name="busCode" isInput="false" style="height: 20px !important;width: 150px !important;">
                                    <option value=""><emp:message key="cxtj_sjcx_xtxxdc_qb" defVal="全部" fileName="cxtj"/></option>
                                    <option value="-1">-(<emp:message key="cxtj_sjcx_xtxxdc_wywlx" defVal="无业务类型" fileName="cxtj"/>)</option>
                                    <c:forEach items="${busMap}" var="item">
                                        <c:if test="${not empty item}">
                                            <option value="${item.key}">${item.value}(${item.key})</option>
                                        </c:if>
                                    </c:forEach>
                                </select>
                            </td>
                            <td>
                                <emp:message key="cxtj_sjcx_xtxxdc_spzh" defVal="sp账号：" fileName="cxtj"/>：
                            </td>
                            <td>
                                <label>
                                    <select name="userid" id="userid" style="height: 20px !important;width: 150px !important;">
                                        <%--<option value="">
                                            <emp:message key="cxtj_sjcx_xtxxjl_qb" defVal="全部" fileName="cxtj"/>
                                        </option>--%>
                                        <c:forEach items="${mrUserList}" var="item">
                                            <c:if test="${not empty item}">
                                                <option value="${item}" <c:if test="${item == spuser}">checked</c:if>>${item}</option>
                                            </c:if>
                                        </c:forEach>
                                    </select>
                                </label>
                            </td>

                            <td>
                                <emp:message key="cxtj_sjcx_xtxxdc_ztm" defVal="状态码：" fileName="cxtj"/>：
                            </td>
                            <td>
                                <input type="text" value="" style="height: 20px !important;width: 150px !important;" id="mterrorcode" name="mterrorcode" maxlength="7"/>
                            </td>

                            <td>&nbsp;</td>
                        </tr>
                        <tr>

                            <td>
                                <emp:message key="cxtj_sjcx_xtxxdc_tdhm" defVal="通道号码：" fileName="cxtj"/>：
                            </td>
                            <td>
                                <label>
                                    <select name="spgate" id="spgate" style="height: 20px !important;width: 150px !important;">
                                        <option value="">
                                            <emp:message key="cxtj_sjcx_xtxxdc_qb" defVal="全部" fileName="cxtj"/>
                                        </option>
                                        <c:forEach items="${spList}" var="item">
                                            <c:if test="${not empty item and not empty item['spgate']}">
                                                <option value="${item['spgate']}">${item['spgate']}</option>
                                            </c:if>
                                        </c:forEach>
                                    </select>
                                </label>
                            </td>
                            <td>
                                <emp:message key="cxtj_sjcx_xtxxdc_zdylsh" defVal="自定义流水号：" fileName="cxtj"/>：
                            </td>
                            <td>
                                <input type="text" value="" style="height: 20px !important;width: 150px !important;" id="usermsgid" name="usermsgid" maxlength="64"/>
                            </td>
                            <td></td>
                            <td></td>
                            <td>&nbsp;</td>
                        </tr>
                        <tr>
                            <td>
                                <emp:message key="cxtj_sjcx_xtxxdc_fssj" defVal="发送时间：" fileName="cxtj"/>：
                            </td>
                            <td class="tableTime">
                                <input type="text" style="height: 20px !important;width: 181px !important;" class="Wdate" readonly="readonly" value="" id="sendtime" name="sendtime" onclick="initStartDate('recvtime')">
                            </td>
                            <td>
                                <emp:message key="cxtj_sjcx_xtxxdc_z" defVal="至：" fileName="cxtj"/>：
                            </td>
                            <td>
                                <input type="text" style="height: 20px !important;width: 181px !important;" class="Wdate" readonly="readonly" value="" id="recvtime" name="recvtime" onclick="initEndDate('sendtime')">
                            </td>
                            <td></td>
                            <td></td>
                            <td>&nbsp;</td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <%--自定义表头--%>
                <div class="dg001" style="left: 36px; top: 170px;">
                    <div class="higehLisght" style="line-height: 22px;">
                        <p><span><emp:message key="cxtj_sjcx_xtxxdc_zdybt" defVal="自定义表头" fileName="cxtj"/></span></p>
                    </div>
                </div>
                <div id="condition" style="padding: 20px 10px 10px 10px;height: 100px;">
                    <table id="checktb" class="deltablecss" style="width: 100%;height: 100%;padding-top: 10px;">
                        <tr><td><input style="width:15px !important;height:20px !important;display:inline-block;vertical-align:middle;" type="checkbox" id="checkAll" checked value=""/>&nbsp;<span style="display:inline-block;vertical-align:middle;"><emp:message key="cxtj_sjcx_xtxxdc_qx" defVal="全选" fileName="cxtj"/></span></td></tr>
                        <tr>
                            <td><input style="width:15px !important;height:20px !important;display:inline-block;vertical-align:middle;" type="checkbox" name="columns" checked value="userid"/>&nbsp;<span style="display:inline-block;vertical-align:middle;"><emp:message key="cxtj_sjcx_xtxxdc_spzh" defVal="SP账号" fileName="cxtj"/></span></td>
                            <td><input style="width:15px !important;height:20px !important;display:inline-block;vertical-align:middle;" type="checkbox" name="columns" checked value="spgate"/>&nbsp;<span style="display:inline-block;vertical-align:middle;"><emp:message key="cxtj_sjcx_xtxxdc_tdhm" defVal="通道号码" fileName="cxtj"/></span></td>
                            <td><input style="width:15px !important;height:20px !important;display:inline-block;vertical-align:middle;" type="checkbox" name="columns" checked value="unicom"/>&nbsp;<span style="display:inline-block;vertical-align:middle;"><emp:message key="cxtj_sjcx_xtxxdc_yys" defVal="运营商" fileName="cxtj"/></span></td>
                            <td><input style="width:15px !important;height:20px !important;display:inline-block;vertical-align:middle;" type="checkbox" name="columns" checked value="phone"/>&nbsp;<span style="display:inline-block;vertical-align:middle;"><emp:message key="cxtj_sjcx_xtxxdc_sjhm" defVal="手机号码" fileName="cxtj"/></span></td>
                            <td><input style="width:15px !important;height:20px !important;display:inline-block;vertical-align:middle;" type="checkbox" name="columns" checked value="taskid"/>&nbsp;<span style="display:inline-block;vertical-align:middle;"><emp:message key="cxtj_sjcx_xtxxdc_rwpc" defVal="任务批次" fileName="cxtj"/></span></td>
                            <td><input style="width:15px !important;height:20px !important;display:inline-block;vertical-align:middle;" type="checkbox" name="columns" checked value="errorcode"/>&nbsp;<span style="display:inline-block;vertical-align:middle;"><emp:message key="cxtj_sjcx_xtxxdc_ztbg" defVal="状态报告" fileName="cxtj"/></span></td>
                            <td><input style="width:15px !important;height:20px !important;display:inline-block;vertical-align:middle;" type="checkbox" name="columns" checked value="sendtime"/>&nbsp;<span style="display:inline-block;vertical-align:middle;"><emp:message key="cxtj_sjcx_xtxxdc_fssj" defVal="发送时间" fileName="cxtj"/></span></td>
                        </tr>
                        <tr>
                            <td><input style="width:15px !important;height:20px !important;display:inline-block;vertical-align:middle;" type="checkbox" name="columns" checked value="recvtime"/>&nbsp;<span style="display:inline-block;vertical-align:middle;"><emp:message key="cxtj_sjcx_xtxxdc_jssj" defVal="接收时间" fileName="cxtj"/></span></td>
                            <td><input style="width:15px !important;height:20px !important;display:inline-block;vertical-align:middle;" type="checkbox" name="columns" checked value="svrtype"/>&nbsp;<span style="display:inline-block;vertical-align:middle;"><emp:message key="cxtj_sjcx_xtxxdc_ywlx" defVal="业务类型" fileName="cxtj"/></span></td>
                            <td><input style="width:15px !important;height:20px !important;display:inline-block;vertical-align:middle;" type="checkbox" name="columns" checked value="pknumAndPktotal"/>&nbsp;<span style="display:inline-block;vertical-align:middle;"><emp:message key="cxtj_sjcx_xtxxdc_ft" defVal="分条" fileName="cxtj"/></span></td>
                            <td><input style="width:15px !important;height:20px !important;display:inline-block;vertical-align:middle;" type="checkbox" name="columns" checked value="message"/>&nbsp;<span style="display:inline-block;vertical-align:middle;"><emp:message key="cxtj_sjcx_xtxxdc_dxnr" defVal="短信内容" fileName="cxtj"/></span></td>
                            <td><input style="width:15px !important;height:20px !important;display:inline-block;vertical-align:middle;" type="checkbox" name="columns" checked value="usermsgid"/>&nbsp;<span style="display:inline-block;vertical-align:middle;"><emp:message key="cxtj_sjcx_xtxxdc_zdylsh" defVal="自定义流水号" fileName="cxtj"/></span></td>
                            <td><input style="width:15px !important;height:20px !important;display:inline-block;vertical-align:middle;" type="checkbox" name="columns" checked value="msgfmt"/>&nbsp;<span style="display:inline-block;vertical-align:middle;"><emp:message key="cxtj_sjcx_xtxxdc_bm" defVal="编码" fileName="cxtj"/></span></td>
                            <td>&nbsp;</td>
                        </tr>
                    </table>
                </div>
                <%--文件标题--%>
                <div id="condition" style="padding: 20px 10px 10px 10px;">
                    <table class="dg006 deltablecss">
                        <tr>
                            <td style="width: 10%;"><emp:message key="cxtj_sjcx_xtxxdc_wjbt" defVal="文件标题：" fileName="cxtj"/>：</td>
                            <td style="width: 90%"><input style="width: 40% !important;height: 30px;float: left;" maxlength="50" type="text" name="sfileName" id="sfileName"/></td>
                        </tr>
                    </table>
                    <div class="buttons" style="margin-top: 20px;margin-left: 45%;"><a id="enablePwd" style="float: initial;" href="javascript:createData();"><emp:message key="cxtj_sjcx_xtxxdc_scdata" defVal="生成数据" fileName="cxtj"/></a></div>
                </div>
            </form>
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
    </div>

    <div class="clear"></div>
    <script type="text/javascript">
        function LoginInfo(idname){
            document.getElementById(idname).innerHTML=window.parent.parent.document.getElementById("loginparams").innerHTML;
        }
        LoginInfo("r_sysMrparams");
    </script>
<script type="text/javascript" src="${path}/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="${path}/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="${path}/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="${path}/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="${path}/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
<script type="text/javascript" src="${path}/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
<script type="text/javascript" src="${path}/cxtj/query/js/exportexcel.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="${path}/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="${path}/common/i18n/i18nUtil.js"></script>
<script type="text/javascript" src="${path}/common/i18n/<%=langName%>/cxtj_<%=langName%>.js"></script>
<script type="text/javascript">
    $(document).ready(function(){
        //处理简约皮肤下弹出框页面样式不兼容的问题
        if($('#exportForm div.c_selectBox').html()){
            $('#exportForm div.c_selectBox').remove();
            $('#exportForm div select').show();
            $('#checktb input:checkbox').css({'padding-left':'0px'});
            $('#condition table tr td').css({'padding':'5px'});
            $('#condition table tr td span').css({'font-family':'΢���ź�'});
        }
        if("${result}"){
            var result="${result}";
            if(result=="-1")
            {
                alert(getJsLocaleMessage("cxtj","cxtj_sjcx_xtxxdc_cxyj"));
                return;
            }
        }
        if("${exportErr}"){
            alert(getJsLocaleMessage("cxtj","cxtj_sjcx_xtxxdc_dcyc"));
            return;
        }
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
		initPage('${pageInfo.totalPage}','${pageInfo.pageIndex}','${pageInfo.pageSize}','${pageInfo.totalRec}');
        $('#search').click(function(){submitForm();});
        $("#checkAll").click(function() {
            $(":checkbox[name='columns']").attr("checked", this.checked);
        });
        //定义定时任务，实时更新列表中文件记录的状态
        var flushInterval;
        //页面刷新存在就清掉
        clearInterval(flushInterval);
        if("${not empty resultList}"){
            var needFlushIds = [];//定义任务队列
            var i = 0;
            <c:forEach items="${resultList}" var="item">
                var filestatus = "${item['filestatus']}";
                //如果文件状态为生成中，或者已生成但是未下载的记录添加到队列中
                if(filestatus == 1){
                    needFlushIds[i++] = "${item['id']}";
                }
            </c:forEach>
            //如果任务队列有任务就开启定时任务
            if(needFlushIds.length > 0){
                flushInterval = setInterval(function () {
                    $.ajax({
                        url: "${path}/que_mtRptRecord.htm?method=getFlushFileList",
                        type: "get",
                        data: {"pageSize":$("#pageSize").val(),"pageIndex":$("#txtPage").val(),"totalPage":$("#totalPage").val(),"totalRec":$("#totalRec").val()},
                        success: function (data) {
                            if(data){
                                var flushFileList = eval(data);
                                $.each(flushFileList,function (i,item) {
                                    //根据队列中的id找到查询返回的记录，判断文件生成状态和文件下载状态，然后更新列表页中的值
                                    if($.inArray(item.id + "",needFlushIds) > -1){
                                        var filestatus = item.filestatus;
                                        if(filestatus == 0){
                                            //文件已生成
                                            $("#" + item.id).text(getJsLocaleMessage("cxtj","cxtj_sjcx_xtxxdc_ysc"));
                                            var tdArray = $("#" + item.id).nextAll();
                                            $(tdArray[1]).text(item.generatetime);
                                            $(tdArray[4]).html("<a href='javascript:downloadFile(" + item.id + ",\"" + item.filename + "\");'>" + getJsLocaleMessage("cxtj","cxtj_sjcx_xtxxdc_xz") + "</a>");
                                            needFlushIds.splice($.inArray(item.id,needFlushIds),1);
                                        }
                                        if(filestatus == 2){
                                            //生成失败
                                            $("#" + item.id).text(getJsLocaleMessage("cxtj","cxtj_sjcx_xtxxdc_scsb"));
                                            needFlushIds.splice($.inArray(item.id,needFlushIds),1);
                                        }
                                    }
                                });
                                if(needFlushIds.length === 0){
                                    clearInterval(flushInterval);
                                }
                            }
                        },
                        error: function (data) {
                            console.log(data);
                        }
                    });
                },3000);
            }
        }

    });
    //发送导出请求到后台生成数据文件
    function createData() {
        var v_href = "#;";
        var r_href = "javascript:createData();";
        //防止重复提交
        $("#enablePwd").attr("href",v_href);
        if($.isBlank($("#sfileName").val())){
            alert(getJsLocaleMessage("cxtj","cxtj_sjcx_xtxxdc_wjbtwk"));
            $("#enablePwd").attr("href",r_href);
            return;
        }

        if($.isBlank($("input[name='columns']:checked").val())){
            alert(getJsLocaleMessage("cxtj","cxtj_sjcx_xtxxdc_btbnwk"));
            $("#enablePwd").attr("href",r_href);
            return;
        }

        $.ajax({
            url: "${path}/export_sysMt.htm?method=queryEptList",
            type: "get",
            data: $("#exportForm").serialize(),
            success: function (data) {
                if(data){
                    var rtData = eval("(" + data + ")");
                    if(rtData.rtCode == '0'){//校验通过提交导出请求
                        $('#exportForm').submit();
                    }else if(rtData.rtCode == "1"){//导出记录为空
                        if(rtData.sendtime){
                            $("#sendtime").val(rtData.sendtime);
                        }
                        if(rtData.recvtime){
                            $("#recvtime").val(rtData.recvtime);
                        }
                        alert(getJsLocaleMessage("cxtj","cxtj_sjcx_xtxxdc_dcjlwk"));
                    }else if(rtData.rtCode == "2"){//导出记录超出最大限制
                        if(rtData.sendtime){
                            $("#sendtime").val(rtData.sendtime);
                        }
                        if(rtData.recvtime){
                            $("#recvtime").val(rtData.recvtime);
                        }
                        alert(getJsLocaleMessage("cxtj","cxtj_sjcx_xtxxdc_cczdxz"));
                    }
                }else{//导出异常
                    alert(getJsLocaleMessage("cxtj","cxtj_sjcx_xtxxdc_dcyc"));
                }
                //3秒后才可再次提交
                setTimeout(function () {
                    $("#enablePwd").attr("href",r_href);
                },3000);
            },
            error: function () {
                alert(getJsLocaleMessage("cxtj","cxtj_sjcx_xtxxdc_dcyc"));
                $("#enablePwd").attr("href",r_href);
            }
        });

    }
    //弹窗导出的弹窗
    function openExportDiv() {
        $('#exportDiv').dialog({
            autoOpen: false,
            width:830,
            title: getJsLocaleMessage("cxtj","cxtj_sjcx_xtxxdc_scdcsjsz"),
            height:465,
            modal:true,
            position:['25%', 100]
        });
        $('#exportDiv').dialog('open');
    }

    function downloadFile(id, filename, obj) {
        //更新当前下载时间
        $.ajax({
                url:"que_mtRptRecord.htm?method=getDownloadTimeById",
                type:"POST",
                data:{
                    id:id,
                    fileName:encodeURI(encodeURI(filename))
                },
                async : false,
                success:function(time){
                    var tdArray = $("#" + id).nextAll();
                    $(tdArray[2]).text(time);
                    var filepath = "que_mtRptRecord.htm?method=downloadFile&fileid=" + id + "&fileName=" + encodeURI(encodeURI(filename));
                    download_href(filepath);
                }
            }
        );
    }
    //初始化日期时间规则
    function initStartDate(idName){
        var max = "2099-12-31 23:59:59";
        var v = $("#" + idName).attr("value");
        var min = "1900-01-01 00:00:00";
        if(v.length != 0)
        {
            max = v;
            var year = v.substring(0,4);
            var mon = v.substring(5,7);
            if (mon != "01")
            {
                mon = String(parseInt(mon,10)-1);
                if (mon.length == 1)
                {
                    mon = "0"+mon;
                }
            }
            else
            {
                year = String((parseInt(year,10)-1));
                mon = "12";
            }
            min = year+"-"+mon+"-01 00:00:00"
        }
        WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:min,maxDate:max});

    };
    function initEndDate(idName){
        var max = "2099-12-31 23:59:59";
        var v = $("#" + idName).attr("value");
        if(v.length != 0)
        {
            var year = v.substring(0,4);
            var mon = v.substring(5,7);
            var day = 31;
            if (mon != "12")
            {
                mon = String(parseInt(mon,10)+1);
                if (mon.length == 1)
                {
                    mon = "0"+mon;
                }
                switch(mon){
                    case "01":day = 31;break;
                    case "02":day = 28;break;
                    case "03":day = 31;break;
                    case "04":day = 30;break;
                    case "05":day = 31;break;
                    case "06":day = 30;break;
                    case "07":day = 31;break;
                    case "08":day = 31;break;
                    case "09":day = 30;break;
                    case "10":day = 31;break;
                    case "11":day = 30;break;
                }
            }
            else
            {
                year = String((parseInt(year,10)+1));
                mon = "01";
            }
            max = year+"-"+mon+"-"+day+" 23:59:59"
        }
        WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:v,maxDate:max});

    };
    /**
     *  给jquery添加源方法
     */
    $.isBlank = function(obj){
        return(!obj || $.trim(obj) === "");
    };
    /**
     *  select 下拉选择框如果有默认值时，自动选中该值。
     *  值属性：data-val
     */
    function selected() {
        $('select').each(function () {
            var that = $(this);
            var val = $.trim(that.attr('data-val'));
            if (!$.isBlank(val)) {
                that.val(val);
            }
        });
    }
    selected();
    
</script>

</body>
</html>
