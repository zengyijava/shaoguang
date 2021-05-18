<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.Map" %>
<%@page import="com.montnets.emp.common.constant.StaticValue" %>
<%@page import="com.montnets.emp.common.constant.ViewParams" %>
<%@page import="java.net.URLDecoder" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple" %>
<%
    String path = request.getContextPath();
    String langName = (String) session.getAttribute("emp_lang");
    String iPath = request.getRequestURI().substring(0, request.getRequestURI().lastIndexOf("/"));
    String inheritPath = iPath.substring(0, iPath.lastIndexOf("/"));
    String commonPath = inheritPath.substring(0, inheritPath.lastIndexOf("/"));
    String skin = session.getAttribute("stlyeSkin") == null ? "default" : (String) session.getAttribute("stlyeSkin");
    String lguserid = request.getParameter("lguserid");
    String lgcorpcode = (String) request.getParameter("lgcorpcode");
    String tempId = request.getParameter("tempId");
    String tempName = request.getParameter("tempName");
    String userId = request.getParameter("userId");
    String spTempId = request.getParameter("spTempId");
    if (tempId == null || "".equals(tempId.trim())) {
        tempId = "0";
    }
    if (tempName == null || "".equals(tempName.trim())) {
        tempName = "-";
    } else {
        tempName = URLDecoder.decode(tempName, "UTF-8");
    }
    String tmCode = request.getParameter("tmCode") == null ? "" : request.getParameter("tmCode");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
<head>
    <%@include file="/common/common.jsp" %>
    <title><emp:message key="xtgl_cswh_dxmbgl_xzgxmb" defVal="选择共享模板" fileName="xtgl"/></title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
    <link href="<%=iPath%>/css/installsel.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
    <link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
    <%if (StaticValue.ZH_HK.equals(langName)) {%>
    <link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
    <link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
    <%}%>

    <link rel="stylesheet" type="text/css" href="<%=iPath%>/css/tem_shareTemplate.css?V=<%=StaticValue.getJspImpVersion() %>"/>
    <link rel="stylesheet" type="text/css" href="<%=iPath %>/css/tem_shareTemplate.css?V=<%=StaticValue.getJspImpVersion() %>"/>

</head>
<body id="tem_shareTemplate">
<div id="container" class="container">
    <%-- 内容开始 --%>
    <center>
        <div class="selectForm_div">
            <form method="post" id="selectForm">
                <input type="hidden" id="pathUrl" value="<%=path %>">
                <input type="hidden" id="lguserid" value="<%=lguserid %>">
                <input type="hidden" id="lgcorpcode" value="<%=lgcorpcode %>">
                <input type="hidden" id="tempId" value="<%=tempId %>">
                <input type="hidden" id="userid" value="<%=userId %>">
                <table class="mbbh_mh_table">
                    <tr class="mbbh_mh_tr">
                        <td class="mbmc_mh_td" align="left">
                            <emp:message key="rms_cswh_dxmbgl_mbname" defVal="富信主题：" fileName="rms"/><span id="tmpname"></span>
                        </td>
                        <td id="tempIdView" class="mbbh_mh_td" colspan="2">
                            <emp:message key="rms_cswh_dxmbgl_mbid" defVal="模板ID：" fileName="rms"/><%=spTempId %>
                        </td>
                    </tr>
                    <tr class="searchname_tr">
                        <td class="searchname_td" align="left" colspan="2">
                            <div class="div_bd searchbox">

                                <input id="searchname" name="searchname" placeholder="<emp:message key="rms_spgl_shlcgl_ts" defVal="操作员姓名/手机号" fileName="rms"/>"
                                       type="text" maxlength="16" class="graytext" value=""
                                       onkeypress="if(event.keyCode==13) {searchbyname();event.returnValue=false;}"/>

                                <a onclick="searchbyname()" class="searcha"><img id="searchIcon" src="<%=skin %>/images/query.png" border="0"/></a>

                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td class="etree_td">
                            <table>
                                <tr>
                                    <td>
                                        <div id="etree" class="dept div_bd">
                                            <iframe id="sonFrame" name="sonFrame" class="sonFrame" frameborder="0"
                                                    src="<%=iPath %>/tem_seltree.jsp?lguserid=<%=lguserid %>&tempId=<%=tempId %>"></iframe>
                                        </div>
                                        <div class="shownameDiv div_bd showUserName_div">
											<span id="showUserName" class="title_bg showUserName"><emp:message key="rms_spgl_shlcgl_cylb_mh" defVal="成员列表："
                                                                                                               fileName="rms"/>
											</span>
                                        </div>
                                        <div class="dept div_bd left_div">
                                            <select multiple name="left" id="left" size="16" onfocus="treeLoseFocus()" class="left"
                                                    ondblclick="">
                                            </select>
                                        </div>
                                    </td>
                                </tr>
                            </table>
                        </td>
                        <td align="center" class="shlcgl_xz_td">
                            <input class="btnClass1" type="button" id="toLeft" value="<emp:message key='rms_spgl_shlcgl_xz' defVal='选择' fileName='rms'/>"
                                   onclick="javascript:router();">
                            <br/>
                            <br/>
                            <input class="btnClass1" type="button" id="toRight" value="<emp:message key='rms_spgl_shlcgl_sc' defVal='删除' fileName='rms'/>"
                                   onclick="javascript:moveOut();">
                            <br/>
                            <br/>
                            <br/>
                            <br/>
                            <br/>
                            <br/>
                            <br/>
                            <br/>
                        </td>
                        <td class="right_td">
                            <div class="showchoiceDiv div_bd">
                                <select multiple name="right" id="right" class="right">
                                </select>
                            </div>
                        </td>
                    </tr>
                </table>
            </form>
        </div>
    </center>
    <%-- 内容结束 --%>
    <%-- foot开始 --%>
    <div class="bottom">
        <div id="bottom_right">
            <div id="bottom_left"></div>
        </div>
    </div>
    <%-- foot结束 --%>
</div>
<div class="clear"></div>
<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/xtgl_<%=langName%>.js"></script>
<script language="javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>" type="text/javascript"></script>
<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=iPath%>/js/shareTemp.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript">
    $(document).ready(function () {
        $('#tmpname').text($(window.parent.document).find('#flowFrame').attr('tmpname'));
        $.post("<%=path%>/meditor/getSel", {
            tempid:<%=tempId%>,
            infoType: "4"
        }, function (returnmsg) {
            if (returnmsg != "") {
                $("#right").empty();
                $("#right").html(returnmsg);
            }
        });
    });
</script>
</body>
</html>