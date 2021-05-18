<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.montnets.emp.common.constant.ViewParams" %>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils" %>
<%@ page import="com.montnets.emp.jfcx.vo.CrmBillQueryVo" %>
<%@ page import="com.montnets.emp.jfcx.vo.CrmRfMgrVo" %>
<%@ page import="com.montnets.emp.mobilebus.constant.MobileBusStaticValue" %>
<%@ page import="com.montnets.emp.util.PageInfo" %>
<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<%@page import="java.text.SimpleDateFormat" %>
<%@page import="java.util.Calendar" %>
<%@page import="java.util.Iterator" %>
<%@page import="java.util.List" %>
<%@page import="java.util.Map" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple" %>
<%
    String path = request.getContextPath();
    String iPath = request.getRequestURI().substring(0, request.getRequestURI().lastIndexOf("/"));
    String inheritPath = iPath.substring(0, iPath.lastIndexOf("/"));
    String commonPath = inheritPath.substring(0, inheritPath.lastIndexOf("/"));

    PageInfo pageInfo = new PageInfo();
    if (request.getAttribute("pageInfo") != null) {
        pageInfo = (PageInfo) request.getAttribute("pageInfo");
    }

    int succ = 0;
    int rfail2 = 0;
    if (session.getAttribute("succ") != null && session.getAttribute("rfail2") != null) {
        succ = Integer.parseInt(session.getAttribute("succ").toString());

        rfail2 = Integer.parseInt(session.getAttribute("rfail2").toString());
    }
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    //Date date = new Date();
    Calendar c = Calendar.getInstance();
    c.add(Calendar.DAY_OF_MONTH, -1);
    c.add(Calendar.DATE, 1);
    String beginTime = df.format(c.getTime()).substring(0, 8) + "01 00:00:00";
    String endTime = df.format(c.getTime()).substring(0, 10) + " 23:59:59"; //change by dj

    @SuppressWarnings("unchecked")
    Map<String, String> btnMap = (Map<String, String>) session.getAttribute("btnMap");//按钮权限Map

    @SuppressWarnings("unchecked")
    Map<String, String> titleMap = (Map<String, String>) session.getAttribute("titleMap");

    @SuppressWarnings("unchecked")
    List<CrmRfMgrVo> resultListt = (List<CrmRfMgrVo>) request.getAttribute("resultList");

    String menuCode = titleMap.get("crmRfMgr");

    menuCode = menuCode == null ? "0-0-0" : menuCode;
    String findResult = null;
    findResult = (String) request.getAttribute("findresult");
    String skin = session.getAttribute("stlyeSkin") == null ? "default" : (String) session.getAttribute("stlyeSkin");
    String msType = (String) request.getAttribute("msType");
    //电话号码
    String mobile = request.getParameter("mobile");
    //姓名
    String customname = request.getParameter("customname");
    //签约账号
    String acctno = request.getParameter("acctno");
    //扣费帐号
    String debitaccount = request.getParameter("debitaccount");
    //套餐名称
    String taocanname = request.getParameter("taocanname");

    //是否包含子机构
    String isContainsSun = (String) request.getAttribute("isContainsSun");
    //操作员
    String username = request.getParameter("username");
    boolean isFirstEnter = (Boolean) request.getAttribute("isFirstEnter");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
<head>
    <%@include file="/common/common.jsp" %>
    <title><%=titleMap.get(menuCode)%>
    </title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet"
          type="text/css"/>
    <link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet"
          type="text/css"/>
    <link href="<%=skin%>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
    <link href="<%=skin%>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
    <link href="<%=commonPath%>/common/widget/zTreeStyle/zTreeStyle.css?V=<%=StaticValue.getJspImpVersion() %>"
          rel="stylesheet" type="text/css"/>
    <link rel="stylesheet"
          href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>">
    <link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css">
    <%if (StaticValue.ZH_HK.equals(empLangName)) {%>
    <link rel="stylesheet" type="text/css"
          href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
    <link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
    <%}%>
    <style type="text/css">
        .maxwidth span {
            margin: 0 auto;
            width: 70px;
            display: block;
            white-space: nowrap;
            overflow: hidden;
            -o-text-overflow: ellipsis; /* Opera */
            text-overflow: ellipsis; /* IE, Safari (WebKit) */
        }

        .maxwidth2 span {
            margin: 0 auto;
            width: 60px;
            display: block;
            white-space: nowrap;
            overflow: hidden;
            -o-text-overflow: ellipsis; /* Opera */
            text-overflow: ellipsis; /* IE, Safari (WebKit) */
        }

        .container {
            float: left;
            min-width: 1215px;
            _width: 1215px;
        }
    </style>
</head>
<body>
<div id="container" class="container">
    <%-- 当前位置 --%>

    <%=ViewParams.getPosition(empLangName, menuCode) %>
    <%-- 内容开始 --%>
    <div id="rContent" class="rContent">
        <input type="hidden" value="<%=inheritPath%>" id="inheritPath"/>
        <input type="hidden" value="<%=path%>" id="path"/>
        <input type="hidden" id="lguserid" name="lguserid" value="<%=request.getParameter("lguserid") %>"/>
        <input type="hidden" id="lgcorpcode" name="lgcorpcode" value="<%=request.getParameter("lgcorpcode") %>"/>
        <form name="pageForm" action="ydyw_crmRfMgr.htm" method="post"
              id="pageForm">

            <input type="hidden" id="deptString" name="deptString"
                   value="<%=request.getAttribute("deptString") == null ? "" : request.getAttribute("deptString")%>"/>
            <input type="hidden" id="sp" name="sp"
                   value="<%=request.getAttribute("sp") == null ? "" : request.getAttribute("sp")%>"/>
            <div class="buttons">
                <div id="toggleDiv">
                </div>
                <%
                    if (btnMap.get(menuCode + "-5") != null) {
                %>
                <a id="exportCurrent"></a>
                <a id="exportCondition"></a>

                <input type="hidden" name="menucode" id="menucode" value="17"/>
                <%
                    }
                %>

                <%--						<a id="exportCondition">导出</a>--%>
                <%
                    if (btnMap.get(menuCode + "-1") != null) {
                %>
                <a id="delete" href="javascript:muliteRefund()"><emp:message key="ydyw_qyjfcx_khtfgl_text_10"
                                                                             defVal="退费"
                                                                             fileName="ydyw"></emp:message></a>
                <%
                    }
                %>


            </div>
            <div id="condition">
                <table>
                    <tbody>
                    <tr>
                        <td>
                            <emp:message key="ydyw_ywgl_ywbgl_text_39" defVal="手机号码：" fileName="ydyw"></emp:message>
                        </td>
                        <td>
                            <input type="text" style="width: 177px;" value="<%=mobile == null ? "" : mobile%>"
                                   id="mobile" name="mobile" maxlength="21"
                                   onkeyup="javascript:phoneInputCtrl($(this))"/>
                        </td>
                        <td>
                            <emp:message key="ydyw_ywgl_ywmbpz_text_57" defVal="姓名：" fileName="ydyw"></emp:message>
                        </td>
                        <td class="tableTime">
                            <input type="text" style="width: 177px;" value="<%=customname == null ? "" : customname%>"
                                   id="customname" name="customname" maxlength="21"/>
                        </td>
                        <td>
                            <emp:message key="ydyw_ywgl_ywbgl_text_47" defVal="签约账号：" fileName="ydyw"></emp:message>
                        </td>
                        <td>
                            <input type="text" style="width: 177px;" value="<%=acctno== null ? "" : acctno%>"
                                   id="acctno" name="acctno" maxlength="21"/>
                        </td>
                        <td class="tdSer">
                            <center>
                                <a id="search" name="research"></a>
                            </center>
                        </td>
                    </tr>
                    <tr>
                        <%
                            //计费类型
                            Map<String, String> taoCanType = MobileBusStaticValue.getTaoCanType();
                            if (taoCanType != null && taoCanType.size() > 0) {
                                Iterator<String> keyIterators = taoCanType.keySet().iterator();
                        %>
                        <td>
                            <%--<%=taoCanType.get("-1") %>--%>
                            <emp:message key="ydyw_ywgl_ywbgl_text_37" defVal="计费类型：" fileName="ydyw"></emp:message>
                        </td>
                        <td>
                            <select name="taocantype" id="taocantype" style="width: 182px">
                                <option value=""><emp:message key="common_whole" defVal="全部"
                                                              fileName="common"></emp:message></option>
                                <%
                                    while (keyIterators.hasNext()) {
                                        String key = keyIterators.next();
                                        String value = taoCanType.get(key);
                                        if ("VIP免费".equals(value)) {
                                            value = MessageUtils.extractMessage("ydyw", "ydyw_ywgl_tczlsz_text_5", request);
                                        } else if ("包月".equals(value)) {
                                            value = MessageUtils.extractMessage("ydyw", "ydyw_ywgl_tczlsz_text_by", request);
                                        } else if ("包季".equals(value)) {
                                            value = MessageUtils.extractMessage("ydyw", "ydyw_ywgl_tczlsz_text_bj", request);
                                        } else if ("包年".equals(value)) {
                                            value = MessageUtils.extractMessage("ydyw", "ydyw_ywgl_tczlsz_text_bn", request);
                                        }
                                        if (!"-1".equals(key)) {
                                %>
                                <option value=<%=key %> <%if(key.equals(request.getParameter("taocantype"))){%> selected="selected" <%}%>><%=value %>
                                </option>
                                <%
                                        }
                                    }
                                %>
                            </select>
                        </td>
                        <%
                            }
                        %>
                        <td>
                            <emp:message key="ydyw_ywgl_ywbgl_text_51" defVal="签约状态：" fileName="ydyw"></emp:message>
                        </td>
                        <td>
                            <select name="contractstate" id="contractstate" style="width: 182px">
                                <option value=""><emp:message key="common_whole" defVal="全部"
                                                              fileName="common"></emp:message></option>
                                <%
                                    Map<String, String> contractType;
                                    if ("zh_HK".equals(empLangName)) {
                                        contractType = MobileBusStaticValue.contractType_zh_HK;
                                    } else if ("zh_TW".equals(empLangName)) {
                                        contractType = MobileBusStaticValue.contractType_zh_TW;
                                    } else {
                                        contractType = MobileBusStaticValue.contractType;
                                    }
                                    if (contractType != null && contractType.size() > 0) {
                                        Iterator<String> keyIterator = contractType.keySet().iterator();
                                        while (keyIterator.hasNext()) {
                                            String key = keyIterator.next();
                                %>
                                <option value=<%=key%> <%if(key.equals(request.getParameter("contractstate"))){%> selected="selected" <%}%>><%= contractType.get(key) %>
                                </option>
                                <%
                                        }
                                    }
                                %>
                            </select>
                        </td>
                        <td>
                            <emp:message key="ydyw_ywgl_ywbgl_text_45" defVal="扣费状态：" fileName="ydyw"></emp:message>
                        </td>
                        <td>

                            <select name="deductionstype" id="deductionstype" style="width: 182px">
                                <option value=""><emp:message key="common_whole" defVal="全部"
                                                              fileName="common"></emp:message></option>
                                <option value="0" <%if ("0".equals(request.getParameter("deductionstype"))) {%>
                                        selected="selected" <%}%>><emp:message key="ydyw_qyjfcx_khtfgl_text_3"
                                                                               defVal="等待扣费"
                                                                               fileName="ydyw"></emp:message></option>
                                <option value="1" <%if ("1".equals(request.getParameter("deductionstype"))) {%>
                                        selected="selected" <%}%>><emp:message key="ydyw_qyjfcx_khtfgl_text_4"
                                                                               defVal="扣费成功"
                                                                               fileName="ydyw"></emp:message></option>
                                <option value="2" <%if ("2".equals(request.getParameter("deductionstype"))) {%>
                                        selected="selected" <%}%>><emp:message key="ydyw_qyjfcx_khtfgl_text_5"
                                                                               defVal="扣费失败"
                                                                               fileName="ydyw"></emp:message></option>
                                <option value="3" <%if ("3".equals(request.getParameter("deductionstype"))) {%>
                                        selected="selected" <%}%>><emp:message key="ydyw_qyjfcx_khtfgl_text_6"
                                                                               defVal="退费成功"
                                                                               fileName="ydyw"></emp:message></option>
                                <option value="4" <%if ("4".equals(request.getParameter("deductionstype"))) {%>
                                        selected="selected" <%}%>><emp:message key="ydyw_qyjfcx_khtfgl_text_7"
                                                                               defVal="退费失败"
                                                                               fileName="ydyw"></emp:message></option>
                                <option value="5" <%if ("5".equals(request.getParameter("deductionstype"))) {%>
                                        selected="selected" <%}%>><emp:message key="ydyw_qyjfcx_khtfgl_text_8"
                                                                               defVal="退费申请中"
                                                                               fileName="ydyw"></emp:message></option>
                            </select>
                        </td>
                        <td></td>
                    </tr>
                    <tr>
                        <td>
                            <emp:message key="ydyw_ywgl_ywbgl_text_41" defVal="扣费账号：" fileName="ydyw"></emp:message>
                        </td>
                        <td>
                            <input type="text" style="width: 177px;"
                                   value="<%=debitaccount == null ? "" : debitaccount%>" id="debitaccount"
                                   name="debitaccount" maxlength="21" onkeyup="javascript:phoneInputCtrl($(this))"/>
                        </td>
                        <td>
                            <emp:message key="ydyw_ywgl_ywbgl_text_49" defVal="签约套餐：" fileName="ydyw"></emp:message>
                        </td>
                        <td>
                            <input type="text" style="width: 177px;" value="<%=taocanname == null ? "" : taocanname%>"
                                   id="taocanname" name="taocanname" maxlength="21"/>
                        </td>
                        <td>
                            <emp:message key="ydyw_ywgl_ywbgl_text_24" defVal="操作员：" fileName="ydyw"></emp:message>
                        </td>
                        <td class="tableTime">
                            <input type="text" style="width: 177px;" value="<%=username == null ? "" : username%>"
                                   id="username" name="username" maxlength="21"/>
                        </td>
                        <td></td>
                    </tr>
                    <tr>
                        <td>
                            <emp:message key="ydyw_ywgl_ywbgl_text_43" defVal="扣费时间：" fileName="ydyw"></emp:message>
                        </td>
                        <td class="tableTime">
                            <input type="text" style="cursor: background-color: white;width: 181px;" class="Wdate"
                                   readonly="readonly" onclick="setime()"
                                   value="<%=request.getParameter("begintime") != null ? request.getParameter("begintime") : beginTime%>"
                                   id="sendtime" name="begintime">
                        </td>
                        <td>
                            <emp:message key="common_to" defVal="至：" fileName="common"></emp:message>
                        </td>
                        <td>
                            <input type="text" style="cursor: pointerund-color: white;width: 180px;" class="Wdate"
                                   readonly="readonly" onclick="retime()"
                                   value="<%=request.getParameter("endtime") != null ? request.getParameter("endtime") : endTime%>"
                                   id="recvtime" name="endtime">
                        </td>
                        <td>
                            <emp:message key="ydyw_ywgl_ywbgl_text_26" defVal="机构：" fileName="ydyw"></emp:message>
                        </td>
                        <td class="condi_f_l">
                            <div style="width: 220px;">
                                <input type="text" id="depNam" name="depNam"
                                       value="<%=request.getAttribute("dName") == null ? MessageUtils.extractMessage("common","common_pleaseSelect",request) : request.getAttribute("dName").toString().replace("<", "&lt;").replace(">", "&gt;")%>"
                                       onclick="javascript:showMenu();" readonly style="width: 160px; cursor: pointer;"
                                       class="treeInput"/>
                                &nbsp;
                            </div>
                            <div id="dropMenu">
                                <div style="margin-top: 3px; margin-right: 10px; text-align: right">
                                    <input type="checkbox" id="isContainsSun" name="isContainsSun"
                                           <%if("1".equals(isContainsSun)){%>checked="checked" <%}%> value="1"
                                           style="width:15px;height:15px;"/><emp:message key="ydyw_qyjfcx_khtfgl_text_9"
                                                                                         defVal="包含子机构"
                                                                                         fileName="ydyw"></emp:message>
                                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                    <input type="button"
                                           value="<emp:message key="common_confirm" defVal="确定" fileName="common"></emp:message>"
                                           class="btnClass1" onclick="javascript:zTreeOnClickOK3();"
                                           style="width: 50px;"/>&nbsp;&nbsp;
                                    <input type="button"
                                           value="<emp:message key="common_clean" defVal="清空" fileName="common"></emp:message>"
                                           class="btnClass1" onclick="javascript: cleanSelect_dep();"
                                           style="width: 50px;"/>
                                </div>
                                <ul id="dropdownMenu" class="tree"></ul>
                            </div>
                        </td>

                        <td></td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <table id="content">
                <thead>
                <tr>
                    <th>
                        <input type="checkbox" name="checkAll" id="checkAll">
                    </th>
                    <th>
                        <emp:message key="ydyw_ywgl_ywbgl_text_38" defVal="手机号码" fileName="ydyw"></emp:message>
                    </th>
                    <th>
                        <emp:message key="ydyw_ywgl_ywmbpz_text_56" defVal="姓名" fileName="ydyw"></emp:message>
                    </th>
                    <th>
                        <emp:message key="ydyw_ywgl_ywbgl_text_46" defVal="签约账号" fileName="ydyw"></emp:message>
                    </th>
                    <th>
                        <emp:message key="ydyw_ywgl_ywbgl_text_40" defVal="扣费账号" fileName="ydyw"></emp:message>
                    </th>
                    <th>
                        <emp:message key="ydyw_ywgl_ywbgl_text_48" defVal="签约套餐" fileName="ydyw"></emp:message>
                    </th>
                    <th>
                        <emp:message key="ydyw_ywgl_ywbgl_text_36" defVal="计费类型" fileName="ydyw"></emp:message>
                    </th>
                    <th>
                        <emp:message key="ydyw_ywgl_ywbgl_text_50" defVal="签约状态" fileName="ydyw"></emp:message>
                    </th>
                    <th>
                        <emp:message key="ydyw_qytjbb_jgjftj_text_6" defVal="资费（元）" fileName="ydyw"></emp:message>
                    </th>
                    <th>
                        <emp:message key="ydyw_ywgl_ywbgl_text_42" defVal="扣费时间" fileName="ydyw"></emp:message>
                    </th>
                    <th>
                        <emp:message key="ydyw_qyjfcx_khtfgl_text_1" defVal="退费时间" fileName="ydyw"></emp:message>
                    </th>
                    <th>
                        <emp:message key="ydyw_qyjfcx_khtfgl_text_2" defVal="退费金额（元）" fileName="ydyw"></emp:message>
                    </th>
                    <th>
                        <emp:message key="ydyw_ywgl_ywbgl_text_44" defVal="扣费状态" fileName="ydyw"></emp:message>
                    </th>
                    <th>
                        <emp:message key="ydyw_ywgl_ywbgl_text_23" defVal="操作员" fileName="ydyw"></emp:message>
                    </th>
                    <th>
                        <emp:message key="ydyw_ywgl_ywbgl_text_25" defVal="机构" fileName="ydyw"></emp:message>
                    </th>
                    <th>
                        <emp:message key="common_operation" defVal="操作" fileName="common"></emp:message>
                    </th>
                </tr>
                </thead>
                <tbody>
                <%
                    if (resultListt != null && resultListt.size() > 0) {
                        for (int i = 0; i < resultListt.size(); i++) {
                            CrmRfMgrVo crmRfMgrVo = resultListt.get(i);
                            if (crmRfMgrVo == null) {
                                crmRfMgrVo = new CrmRfMgrVo();
                            }

                %>
                <tr>
                    <td>
                        <%if (crmRfMgrVo.getDeductionstype() == 1) {%>
                        <input type="checkbox" name="crmRfmgrCB" id="crmRfmgrCB" value="<%=crmRfMgrVo.getId() %>">
                        <%
                            }%>
                    </td>
                    <%
                        String custom = crmRfMgrVo.getCustomname() == null ? "" : StringEscapeUtils.escapeHtml(crmRfMgrVo.getCustomname());
                        String acctNo = crmRfMgrVo.getAcctno() == null ? "" : crmRfMgrVo.getAcctno();
                        String debitAcctNo = crmRfMgrVo.getDebitaccount() != null ? crmRfMgrVo.getDebitaccount() : "";
                        String tcName = crmRfMgrVo.getTaocanname() != null ? crmRfMgrVo.getTaocanname() : "";
                        String tcCode = crmRfMgrVo.getTaocancode() != null ? crmRfMgrVo.getTaocancode() : "";
                        String tc = StringEscapeUtils.escapeHtml(tcName) + "(" + StringEscapeUtils.escapeHtml(tcCode) + ")";
                        String name = crmRfMgrVo.getName() == null ? "" : crmRfMgrVo.getName();
                        String userName = crmRfMgrVo.getUsername() == null ? "" : crmRfMgrVo.getUsername();
                        String uu = crmRfMgrVo.getName() == null ? "" : StringEscapeUtils.escapeHtml(name) + "(" + StringEscapeUtils.escapeHtml(userName) + ")";
                        String depName = crmRfMgrVo.getDepname() == null ? "" : StringEscapeUtils.escapeHtml(crmRfMgrVo.getDepname());
                    %>
                    <td><%=crmRfMgrVo.getMobile() == null ? "" : crmRfMgrVo.getMobile() %>
                    </td>
                    <td class="maxwidth"><span title="<%=custom %>"><%=custom%></span></td>
                    <td class="maxwidth"><span title="<%=acctNo %>"><%=acctNo%></span></td>
                    <td class="maxwidth"><span title="<%=debitAcctNo %>"><%=debitAcctNo %></span></td>
                    <td class="maxwidth"><span title="<%=tc %>"><%=tc %></span></td>
                    <td>
                        <%
                            //计费类型
                            if (taoCanType != null && taoCanType.size() > 0) {
                                Iterator<String> keyIterators = taoCanType.keySet().iterator();
                                while (keyIterators.hasNext()) {
                                    String key = keyIterators.next();
                                    String temp = taoCanType.get(key);
                                    if ("VIP免费".equals(temp)) {
                                        temp = MessageUtils.extractMessage("ydyw", "ydyw_ywgl_tczlsz_text_5", request);
                                    } else if ("包月".equals(temp)) {
                                        temp = MessageUtils.extractMessage("ydyw", "ydyw_ywgl_tczlsz_text_by", request);
                                    } else if ("包季".equals(temp)) {
                                        temp = MessageUtils.extractMessage("ydyw", "ydyw_ywgl_tczlsz_text_bj", request);
                                    } else if ("包年".equals(temp)) {
                                        temp = MessageUtils.extractMessage("ydyw", "ydyw_ywgl_tczlsz_text_bn", request);
                                    }
                                    if (crmRfMgrVo.getTaocantype() != null && crmRfMgrVo.getTaocantype() == Integer.parseInt(key)) {
                                        out.print(temp);
                                    }
                                }
                            }
                        %>
                    </td>
                    <td>
                        <%
                            //签约状态
                            if (contractType != null && contractType.size() > 0) {
                                Iterator<String> keyIterator = contractType.keySet().iterator();
                                while (keyIterator.hasNext()) {
                                    String key = (String) keyIterator.next();
                                    if (crmRfMgrVo.getContractstate() != null && crmRfMgrVo.getContractstate() == Integer.parseInt(key)) {
                                        out.print(contractType.get(key));
                                    }
                                }
                            }
                        %>
                    </td>
                    <td><%=crmRfMgrVo.getTaocanmoney() != null ? crmRfMgrVo.getTaocanmoney() : "" %>
                    </td>
                    <td><%=crmRfMgrVo.getBucklefeetime() != null ? df.format(crmRfMgrVo.getBucklefeetime()) : "" %>
                    </td>
                    <td><%=crmRfMgrVo.getBuckleupfeetime() != null ? df.format(crmRfMgrVo.getBuckleupfeetime()) : "" %>
                    </td>
                    <td><%=crmRfMgrVo.getBupsummoney() != null ? crmRfMgrVo.getBupsummoney() : "" %>
                    </td>
                    <td><%
                        if (crmRfMgrVo.getDeductionstype() != null && crmRfMgrVo.getDeductionstype() == 0) {
                    %>
                        <p style="color: orange"><emp:message key="ydyw_qyjfcx_khtfgl_text_3" defVal="等待扣费"
                                                              fileName="ydyw"></emp:message></p>
                        <%
                        } else if (crmRfMgrVo.getDeductionstype() != null && crmRfMgrVo.getDeductionstype() == 1) {
                        %>
                        <p style="color: green"><emp:message key="ydyw_qyjfcx_khtfgl_text_4" defVal="扣费成功"
                                                             fileName="ydyw"></emp:message></p>
                        <%
                        } else if (crmRfMgrVo.getDeductionstype() != null && crmRfMgrVo.getDeductionstype() == 2) {
                        %>
                        <p style="color: red"><emp:message key="ydyw_qyjfcx_khtfgl_text_5" defVal="扣费失败"
                                                           fileName="ydyw"></emp:message></p>
                        <%
                        } else if (crmRfMgrVo.getDeductionstype() != null && crmRfMgrVo.getDeductionstype() == 3) {
                        %>
                        <p style="color: green"><emp:message key="ydyw_qyjfcx_khtfgl_text_6" defVal="退费成功"
                                                             fileName="ydyw"></emp:message></p>
                        <%
                        } else if (crmRfMgrVo.getDeductionstype() != null && crmRfMgrVo.getDeductionstype() == 4) {
                        %>
                        <p style="color: red"><emp:message key="ydyw_qyjfcx_khtfgl_text_7" defVal="退费失败"
                                                           fileName="ydyw"></emp:message></p>
                        <%
                        } else if (crmRfMgrVo.getDeductionstype() != null && crmRfMgrVo.getDeductionstype() == 5) {
                        %>
                        <p style="color: orange"><emp:message key="ydyw_qyjfcx_khtfgl_text_8" defVal="退费申请中"
                                                              fileName="ydyw"></emp:message></p>
                        <%
                        } else if (crmRfMgrVo.getDeductionstype() != null && crmRfMgrVo.getDeductionstype() == -1) {
                        %>
                        -
                        <%
                            }
                        %>
                    </td>
                    <td class="maxwidth"><span title="<%=uu %>"><%=uu%></span></td>
                    <td class="maxwidth2"><span title="<%=depName %>"><%=depName%></span></td>
                    <td>
                        <%
                            if (btnMap.get(menuCode + "-1") != null) {
                                if (crmRfMgrVo.getDeductionstype() != null && crmRfMgrVo.getDeductionstype() == 1) {
                        %>
                        <a href="javascript: showRefundDiv('<%=crmRfMgrVo.getId() %>','1')"><emp:message
                                key="ydyw_qyjfcx_khtfgl_text_10" defVal="退费" fileName="ydyw"></emp:message></a>
                        <%
                        } else {
                        %>
                        --
                        <%
                                }
                            }
                        %>
                    </td>
                </tr>
                <%
                    }
                } else {
                %>
                <tr>
                    <td colspan="16" align="center">
                        <emp:message key="common_norecord" defVal="无记录" fileName="common"></emp:message>
                    </td>
                </tr>
                <%
                    }
                %>
                </tbody>
                <tfoot>
                <tr>
                    <td colspan="16">
                        <input type="hidden" name="pageTotalRec" id="pageTotalRec"
                               value="<%=pageInfo.getTotalRec()%>"/>
                        <input type="hidden" name="queryTime" id="queryTime"
                               value="<%=request.getParameter("begintime") == null ? "" : request.getParameter("begintime")%>至<%=request.getParameter("endtime") == null ? "" : request.getParameter("endtime")%>"/>
                        <div id="pageInfo"></div>
                    </td>
                </tr>

                </tfoot>
            </table>
            <div id="corpCode" class="hidden"></div>
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
<%-- foot结束 --%>
<div class="clear"></div>
<div id="backmoney" title="<emp:message key="ydyw_qyjfcx_khtfgl_text_14" defVal="退费申请" fileName="ydyw"></emp:message>"
     style="padding:5px;width:500px;height:100% ;display:none;word-wrap: break-word;word-break:break-all;overflow-y:auto;">
    <iframe id="backmoneyIframe" src="" style="width: 100%;height: 95%;" frameborder=0>
    </iframe>
</div>
<script type="text/javascript"
        src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript"
        src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript"
        src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript"
        src="<%=commonPath%>/common/js/jquery.ztree-myjquery-b.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript"
        src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript"
        src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=empLangName%>/ydyw_<%=empLangName%>.js"></script>
<script type="text/javascript" src="<%=iPath%>/js/ydyw_crmRfMgr.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript">

    var zTree3;
    var setting3;
    var deptArray = [];
    var zNodes3 = [];

    //获取机构代码
    setting3 = {
        async: true,
        asyncUrl: "<%=path%>/ydyw_crmBillQuery.htm?method=createDeptTree&lgcorpcode=<%=request.getParameter("lgcorpcode")%>&lguserid=<%=request.getParameter("lguserid")%>", //获取节点数据的URL地址
        isSimpleData: true,
        rootPID: 0,
        treeNodeKey: "id",
        treeNodeParentKey: "pId",
        asyncParam: ["depId"],
        callback: {
            click: zTreeOnClick3,
            asyncSuccess: function (event, treeId, treeNode, msg) {
                if (!treeNode) {
                    var rootNode = zTree3.getNodeByParam("level", 0);
                    zTree3.expandNode(rootNode, true, false);
                }
            }
        }
    };


    //隐藏机构树形控件
    function showMenu() {
        hideMenu();
        var sortSel = $("#depNam");
        var sortOffset = $("#depNam").offset();
        $("#dropMenu").toggle();
    }

    //隐藏机构树形控件
    function hideMenu() {
        $("#dropMenu").hide();
    }

    //选中的机构显示文本框
    function zTreeOnClick3(event, treeId, treeNode) {
        if (treeNode) {
            $("#depNam").attr("value", treeNode.name); //设置机构属性
            $("#deptString").attr("value", treeNode.id); //设置机构代码

        }

    }

    //选中的机构显示文本框
    function zTreeOnClickOK3() {
        hideMenu();
    }

    // 加载机构树形控件
    function reloadTree() {
        hideMenu();
        zTree3 = $("#dropdownMenu").zTree(setting3, zNodes3);
    }

    //开始时间
    function setime() {
        var max = "2099-12-31 23:59:59";
        var v = $("#recvtime").attr("value");
        var min = "1900-01-01 00:00:00";
        WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss', minDate: min, maxDate: v});

    }

    //发送起止时间控制
    function retime() {
        var max = "2099-12-31 23:59:59";
        var v = $("#sendtime").attr("value");
        WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss', minDate: v, maxDate: max});
    }

    $(document).ready(function () {
        getLoginInfo("#corpCode");
        var findresult = "<%=findResult%>";
        closeTreeFun(["dropMenu"], [""]);
        if (findresult == "-1") {
            alert(getJsLocaleMessage("ydyw", "ydyw_text_error_1"));
            return;
        }

        reloadTree();
        $("#toggleDiv").toggle(function () {
            $("#condition").hide();
            $(this).addClass("collapse");
        }, function () {
            $("#condition").show();
            $(this).removeClass("collapse");
        });
        $("#content tbody tr").hover(function () {
            $(this).addClass("hoverColor");
        }, function () {
            $(this).removeClass("hoverColor");
        });

        initPage(<%=pageInfo.getTotalPage()%>, <%=pageInfo.getPageIndex()%>, <%=pageInfo.getPageSize()%>, <%=pageInfo.getTotalRec()%>);

        var checkSubmitFlag = true;
        $('#search').click(function () {

            var judgeSendtime = $("#sendtime").val();//开始时间

            var judgeendtime = $("#recvtime").val();//结束时间

            <%--				var datasourcetype = $("#datasourcetype").val();//数据源--%>
            <%--				--%>
            <%--				if(datasourcetype==null||""==datasourcetype)--%>
            <%--				{--%>
            <%--					alert("您当前的角色未赋予查看[EMP界面发送、接口发送]的数据权限");	--%>
            <%--					return;--%>
            <%--				}--%>

            if ("" == $.trim(judgeSendtime) || "" == $.trim(judgeendtime)) {
                alert(getJsLocaleMessage("ydyw", "ydyw_text_error_2"));
            } else {
                checkSubmit();
            }

        });

        function checkSubmit() {
            if (checkSubmitFlag == true) {
                $("a[name='research']").attr("title", getJsLocaleMessage("ydyw", "ydyw_text_error_3"));
                submitForm();//选择好时间段，才允许查询
                checkSubmitFlag = false;
            }
            else {
                // $("a[name='research']").attr("title","您已经点过了查询按钮，请稍等!");
            }
        }


        //导出全部数据到excel
        $("#exportCondition").click(
            function () {
                if (confirm(getJsLocaleMessage("ydyw", "ydyw_text_error_5"))) {
                    var queryTime = encodeURIComponent(encodeURIComponent($("#queryTime").val()));

                    var sendtime = '<%=request.getParameter("begintime") != null ? request.getParameter("begintime") : beginTime%>';

                    var recvtime = '<%=request.getParameter("endtime") != null ? request.getParameter("endtime") : endTime%>';

                    var depNam = '<%=request.getAttribute("deptString") == null ? "" : request.getAttribute("deptString")%>';

                    var datasourcetype = '<%=request.getParameter("datasourcetype") == null ? "" : request.getParameter("datasourcetype")%>'

                    var mstype = '<%=msType == null ? "" : msType%>';
                    <%if(succ == 0 && rfail2 == 0)
                     {%>
                    alert(getJsLocaleMessage("ydyw", "ydyw_text_error_4"));
                    <%	}
                     else
                     {%>

                    <%List<CrmBillQueryVo> list = (List<CrmBillQueryVo>) session.getAttribute("resultList");

                     if(list != null && list.size() > 0)
                     {%>
                    location.href = "<%=path%>/ydyw_crmBillQuery.htm?method=r_sdRptExportExcel&lguserid=<%=request.getParameter("lguserid")%>&lgcorpcode=<%=request.getParameter("lgcorpcode")%>&queryTime=" + queryTime + "&begintime=" + sendtime + "&endtime=" + recvtime + "&depNam=" + depNam + "&mstype=" + mstype + "&datasourcetype=" + datasourcetype;
                    <%}
                 else
                 {%>
                    alert(getJsLocaleMessage("ydyw", "ydyw_text_error_4"));
                    <%}
             }%>
                }
            });


    });


    function cleanSelect_dep() {
        $('#depNam').attr('value', '');
        $('#depNam').attr('value', getJsLocaleMessage("common", "common_pleaseSelect"));
        $('#deptString').attr('value', '');
    }


</script>
</body>
</html>
