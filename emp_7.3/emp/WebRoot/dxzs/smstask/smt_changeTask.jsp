<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.montnets.emp.util.PageInfo" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@page import="com.montnets.emp.common.vo.SendedMttaskVo" %>
<%@page import="com.montnets.emp.common.constant.CommonVariables" %>
<%@page import="com.montnets.emp.common.constant.StaticValue" %>
<%@ page import="com.montnets.emp.entity.sms.LfMttask" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    String iPath = request.getRequestURI().substring(0, request.getRequestURI().lastIndexOf("/"));
    String inheritPath = iPath.substring(0, iPath.lastIndexOf("/"));
    String commonPath = inheritPath.substring(0, inheritPath.lastIndexOf("/"));
    java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm");

    @SuppressWarnings("unchecked")
    Map<String, String> btnMap = (Map<String, String>) session.getAttribute("btnMap");//按钮权限Map


    PageInfo pageInfo = new PageInfo();
    pageInfo = (PageInfo) request.getAttribute("pageInfo");


    @SuppressWarnings("unchecked")
    Map<String, String> titleMap = (Map<String, String>) session.getAttribute("titleMap");

    String actionPath = (String) request.getAttribute("actionPath");
    String menuCode = titleMap.get("smsTaskRecord");
    menuCode = menuCode == null ? "0-0-0" : menuCode;
    CommonVariables CV = new CommonVariables();
    String skin = session.getAttribute("stlyeSkin") == null ? "default" : (String) session.getAttribute("stlyeSkin");

    String langName = (String) session.getAttribute(StaticValue.LANG_KEY);

    String mtId = String.valueOf(request.getAttribute("mtId"));
    Integer msgType = Integer.valueOf(String.valueOf(request.getAttribute("msgType")));
    String mtMsg = String.valueOf(request.getAttribute("mtMsg"));
    String busCode = String.valueOf(request.getAttribute("busCode"));
    String spUser = String.valueOf(request.getAttribute("spUser"));
    String timerTime = String.valueOf(request.getAttribute("timerTime"));

%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 4.1 Transitional//EN">
<html style="width: 100%;height: 100%;">
<head>
    <title><%=titleMap.get(menuCode) %>
    </title>
    <%@include file="/common/common.jsp" %>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet"
          type="text/css"/>
    <link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet"
          type="text/css"/>
    <link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
    <link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
    <link rel="stylesheet"
          href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>"/>
    <link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css">
    <%if (StaticValue.ZH_HK.equals(langName)) {%>
    <link rel="stylesheet" type="text/css"
          href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
    <link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
    <%}%>
    <script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
    <link rel="stylesheet" type="text/css"
          href="<%=iPath %>/css/smt_changeTask.css?V=<%=StaticValue.getJspImpVersion() %>"/>
</head>
<body id="smt_changeTask">
<div id="container">
    <input type="hidden" name="mtId" id="mtId" value="<%=mtId %>">
    <input type="hidden" name="msgType" id="msgType" value="<%=msgType %>">
    <input type="hidden" name="busCode" id="busCode" value="<%=busCode %>">
    <input type="hidden" name="spUser" id="spUser" value="<%=spUser %>">
    <%--<input type="hidden" name="textAreaDefault" id="textAreaDefault" value="<%=mtMsg %>">--%>
    <xmp id="msgDefault" name="msgDefault" style="display:none"><%=mtMsg %></xmp>
    <%-- 内容开始 --%>
    <div id="taskContent" class="taskContent">
        <form id="changeForm" action="<%=path %>/smt_smsSendedBox.htm?method=changeTiming" method="post">
            <%--<table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tbody style="width: 100%">
                <tr>
                    <td width="20%" align="left"><label>发送时间：</label></td>
                    <td width="20%">
                        <input type="text" class="Wdate div_bd" readonly="readonly"
                               onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'%y-%M-%d %H:%m:%s',isShowToday:false})"
                               id="timerTime" name="timerTime" value="">
                    </td>
                    <td width="20%"></td>
                    <td width="20%"></td>
                </tr>
                </tbody>
            </table>--%>
            <div id="eq_sendDiv" class="eq_sendDiv_c">
                <table id="tableDiv">
                    <tbody style="width: 100%">
                    <% if (msgType == 1) {%>
                    <tr id="msgDiv">
                        <td width="25%" valign="top">
                                <span id="sendSpan"><emp:message key="dxzs_xtnrqf_title_12" defVal="发送内容"
                                                                 fileName="dxzs"/>：</span>
                        </td>
                        <td width="75%" colspan="2">
                            <textarea id="textArea" name="mtMsg" id="contents" rows="5"
                                      maxlength="980" onblur="textInput()"  style="word-break: break-all"><%=mtMsg %></textarea>
                            <xmp id="msgXmp" name="msgXmp" style="display:none"><%=mtMsg%></xmp>
                        </td>
                    </tr>
                    <tr id="tail-area">
                        <td colspan="1"></td>
                        <td id="tail-text" colspan="2">
                            <emp:message key="dxzs_xtnrqf_title_20" defVal="贴尾内容" fileName="dxzs"/>：<label id="showtailcontent"></label>
                        </td>
                    </tr>
                    <tr>
                        <td width="15%"></td>
                        <td colspan="3"><b id="strlen"> 0 </b><b id="maxLen">/980</b></td>
                    </tr>
                    <% }%>
                    <tr id="timeTr">
                        <td width="15%">
                            <span><emp:message key="dxzs_xtnrqf_title_25" defVal="发送时间" fileName="dxzs"/>：</span>
                        </td>
                        <td width="15%">
                            <input type="radio" name="timerStatus" value="0" id="sendNow"
                                   onclick="javascript:$('#time2').hide()"/>&nbsp;<emp:message
                                key="dxzs_xtnrqf_title_26" defVal="立即发送" fileName="dxzs"/>
                        </td>
                        <td width="70%">
                            <input id="time" type="radio" name="timerStatus" class="dxzs_timerStatus" value="1" checked="checked"
                                   onclick="javascript:$('#time2').show()"/>&nbsp;<emp:message
                                key="dxzs_xtnrqf_title_27"
                                defVal="定时发送" fileName="dxzs"/>
                            <label id="time2" class="dxzs_display_none">
                                <input type="text" class="Wdate div_bd" readonly="readonly"
                                       onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'%y-%M-%d %H:%m:%s',isShowToday:false})"
                                       id="timerTime" name="timerTime" value="<%=timerTime%>">
                            </label>
                        </td>
                    </tr>

                    </tbody>
                </table>
            </div>
            <div id="confirmDiv">
                <input name="" type="button" id="subBut" onclick="confirmSub()"
                       value="<emp:message key='xtgl_spgl_shlcgl_qd' defVal='确定' fileName='xtgl'/>"
                       class="btnClass5 mr23"/>
                &nbsp;
                &nbsp;
                &nbsp;
                <input name="" type="button" onclick="closeChangeTaskdiv()"
                       value="<emp:message key='xtgl_spgl_shlcgl_qx' defVal='取消' fileName='xtgl'/>"
                       class="btnClass6"/>
                <%-- 为了兼容ie8下取消按钮右下角旁边能点击到确定按钮，增加这个换行 --%>
                <br/>
            </div>
        </form>
    </div>
    <%-- 内容结束 --%>


</div>
<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/dxzs_<%=langName%>.js"></script>
<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
<script type="text/javascript"
        src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript"
        src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript"
        src="<%=commonPath%>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript"
        src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"
        type="text/javascript"></script>
<script type="text/javascript"
        src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=iPath %>/js/smt_changeTask.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript"
        src="<%=commonPath%>/common/js/myjquery-c.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript"
        src="<%=commonPath%>/common/js/layer/layer.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript">
    $(document).ready(function () {
        $("#time").click();
        var msgType = $("#msgType").val();
        if (msgType != "1") {
            $("#eq_sendDiv").css("margin-top", "10%");
            $("#eq_sendDiv").css("margin-left", "10px");
            $("#confirmDiv").css("margin-bottom", "10%");
            $("#confirmDiv").css("margin-left", "50%")
        } else {
            $("#eq_sendDiv").css("margin-top", "");
        }

        //用textarea显示短信内容
        $("#textArea").empty();
        $("#textArea").text($("#msgXmp").text());

        var textOb = $("#textArea").val();
        //贴尾内容
        var showtailcontent = $("#showtailcontent").val() || '';
        if (textOb) {
            //回车长度
            var huiche = textOb.length - textOb.replaceAll("\n", "").length;
            $("#textArea").val().length + huiche + showtailcontent.length
            $("#strlen").text(textOb.length + huiche + showtailcontent.length);
        }
        //短信内容键盘事件监听统计字数
        synlen();
        //设置贴尾
        setTailInfo();
        //根据sp账号设置最大长度
        setContentMaxLen($("#spUser").val());
    });
</script>

</body>
</html>
