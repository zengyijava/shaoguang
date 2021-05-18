<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.montnets.emp.entity.ydyw.LfBusTaoCan" %>
<%@ page import="com.montnets.emp.entity.ydyw.LfProCharges" %>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils" %>
<%@ page import="com.montnets.emp.mobilebus.constant.MobileBusStaticValue" %>
<%@page import="java.util.Map" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    String iPath = request.getRequestURI().substring(0, request.getRequestURI().lastIndexOf("/"));
    String inheritPath = iPath.substring(0, iPath.lastIndexOf("/"));
    String commonPath = inheritPath.substring(0, inheritPath.lastIndexOf("/"));
    @SuppressWarnings("unchecked")
    String skin = session.getAttribute("stlyeSkin") == null ? "default" : (String) session.getAttribute("stlyeSkin");
    Object packageList = request.getAttribute("packageList");
    LfBusTaoCan taocan = null;
    if (request.getAttribute("taocan") != null) {
        taocan = (LfBusTaoCan) request.getAttribute("taocan");
    }

    LfProCharges charges = null;
    if (request.getAttribute("charges") != null) {
        charges = (LfProCharges) request.getAttribute("charges");
    }
    String taocan_code = taocan != null ? taocan.getTaocan_code() : "";
    String info = "";
    if (request.getAttribute("packageInfo") != null) {
        info = (String) request.getAttribute("packageInfo");
    }
%>
<html>
<head>
    <%@include file="/common/common.jsp" %>
    <title></title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet"
          type="text/css"/>
    <link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet"
          type="text/css"/>
    <link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
    <link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
    <link href="<%=iPath%>/css/reading.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
    <link href="<%=commonPath%>/common/css/floating.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet"
          type="text/css"/>
    <link rel="stylesheet"
          href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>"/>
    <link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css">
    <%if (StaticValue.ZH_HK.equals(empLangName)) {%>
    <link rel="stylesheet" type="text/css"
          href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
    <link rel="stylesheet" type="text/css"
          href="<%=commonPath%>/ydyw/busTaoCan/css/msgInfo_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
    <%} else {%>
    <link rel="stylesheet" type="text/css"
          href="<%=commonPath%>/ydyw/busTaoCan/css/msgInfo.css?V=<%=StaticValue.getJspImpVersion()%>"/>
    <%}%>
</head>
<body>
<div id="container" class="container">
    <%-- 当前位置 --%>
    <input id="lguserid" name="lguserid" type="hidden" value="<%=request.getParameter("lguserid") %>"/>
    <input id="lgcorpcode" name="lgcorpcode" type="hidden" value="<%=request.getParameter("lgcorpcode") %>"/>
    <input type="hidden" id="pathUrl" value="<%=path %>"/>
    <input type="hidden" id="packagecodes" value=""/>

    <%-- 内容开始 --%>
    <div id="rContent" class="rContent">
        <table id="sysTable">
            <tr>
                <td class="colName"><emp:message key="ydyw_ywgl_ywbgl_text_33" defVal="套餐名称："
                                                 fileName="ydyw"></emp:message></td>
                <td class="colValue"><%=taocan != null ? taocan.getTaocan_name() : "" %>
                </td>
                <td class="colspace"></td>
                <td class="colName"><emp:message key="ydyw_ywgl_ywbgl_text_35" defVal="套餐编号："
                                                 fileName="ydyw"></emp:message></td>
                <td class="colValue"><%=taocan_code %>
                </td>
            </tr>
            <tr>
                <td class="colName"><emp:message key="ydyw_ywgl_ywbgl_text_37" defVal="计费类型："
                                                 fileName="ydyw"></emp:message></td>
                <td class="colValue"><%
                    Map<String, String> typeMap = MobileBusStaticValue.getTaoCanType();
                    String type = "";
                    if (taocan != null) {
                        type = taocan.getTaocan_type() + "";
                        String tcType = typeMap.get(type);
                        if ("VIP免费".equals(tcType)) {
                            tcType = MessageUtils.extractMessage("ydyw", "ydyw_ywgl_tczlsz_text_5", request);
                        } else if ("包月".equals(tcType)) {
                            tcType = MessageUtils.extractMessage("ydyw", "ydyw_ywgl_tczlsz_text_by", request);
                        } else if ("包季".equals(tcType)) {
                            tcType = MessageUtils.extractMessage("ydyw", "ydyw_ywgl_tczlsz_text_bj", request);
                        } else if ("包年".equals(tcType)) {
                            tcType = MessageUtils.extractMessage("ydyw", "ydyw_ywgl_tczlsz_text_bn", request);
                        }
                        out.print(tcType);
                    }
                %></td>
                <td class="colspace"></td>
                <td class="colName"><emp:message key="ydyw_qytjbb_jgjftj_text_6_p" defVal="资费："
                                                 fileName="ydyw"></emp:message></td>
                <td class="colValue"><%=taocan != null ? taocan.getTaocan_money() : "0" %>
                    <% if ("3".equals(type)) {
                        out.print(MessageUtils.extractMessage("ydyw", "ydyw_qyjfcx_khjfcx_text_14", request));
                    } else if ("4".equals(type)) {
                        out.print(MessageUtils.extractMessage("ydyw", "ydyw_qyjfcx_khjfcx_text_15", request));
                    } else {
                        out.print(MessageUtils.extractMessage("ydyw", "ydyw_qyjfcx_khjfcx_text_8", request));
                    }
                    %>
                </td>
            </tr>
            <tr>
                <td class="colName"><emp:message key="ydyw_ywgl_ywtcgl_text_17" fileName="ydyw"
                                                 defVal="免费试用："></emp:message></td>
                <td class="colValue"><%=charges != null && charges.getTrydays() != null ? charges.getTrydays() : "0" %>
                    <emp:message key="text_sky" fileName="common" defVal="天"></emp:message></td>
                <td class="colspace"></td>
                <td class="colName"><emp:message key="ydyw_ywgl_ywtcgl_text_18" fileName="ydyw"
                                                 defVal="试用时间："></emp:message></td>
                <td class="colValue">
                    <% if (charges != null && "0".equals(charges.getTrytype() + "")) {
                        out.print(MessageUtils.extractMessage("ydyw", "ydyw_qyjfcx_khqygl_text_30", request));
                    } else if (charges != null && "1".equals(charges.getTrytype() + "")) {
                        out.print(charges.getTrystartdate());
                    }%>
                </td>
            </tr>
            <tr>
                <td class="colName"><emp:message key="ydyw_ywgl_ywbgl_text_43" defVal="扣费时间："
                                                 fileName="ydyw"></emp:message></td>
                <td class="colValue">
                    <%
                        if (charges != null) {
                            String buckleDate = charges.getBuckledate().toString();
                            Integer buckleType = charges.getBuckletype();
                            if ("zh_HK".equals(empLangName)) {
                                switch (Integer.parseInt(buckleDate)) {
                                    case 1:
                                        buckleDate += "st";
                                        break;
                                    case 2:
                                        buckleDate += "nd";
                                        break;
                                    case 3:
                                        buckleDate += "rd";
                                        break;
                                    default:
                                        buckleDate += "th";
                                }
                                if (buckleType == 1) {
                                    /*订购生效次月 号开始扣费*/
                                    out.print(buckleDate + MessageUtils.extractMessage("ydyw", "ydyw_ywgl_ywtcgl_text_4", request));
                                } else if (buckleType == 2) {
                                    /*订购生效当天开始扣费*/
                                    out.print(MessageUtils.extractMessage("ydyw", "ydyw_ywgl_ywtcgl_text_6", request));
                                } else if (buckleType == 3) {
                                    /*订购生效当月 号开始扣费*/
                                    out.print(buckleDate + MessageUtils.extractMessage("ydyw", "ydyw_ywgl_ywtcgl_text_7", request));
                                } else {
                                    out.print("-");
                                }
                            } else {
                                if (charges != null && ("1").equals(charges.getBuckletype() + "")) {
                                    out.print(MessageUtils.extractMessage("ydyw", "ydyw_ywgl_ywtcgl_text_4", request) + buckleDate + MessageUtils.extractMessage("ydyw", "ydyw_ywgl_ywtcgl_text_5", request));
                                } else if (charges != null && ("2").equals(charges.getBuckletype() + "")) {
                                    out.print(MessageUtils.extractMessage("ydyw", "ydyw_ywgl_ywtcgl_text_6", request));
                                } else if (charges != null && ("3").equals(charges.getBuckletype() + "")) {
                                    out.print(MessageUtils.extractMessage("ydyw", "ydyw_ywgl_ywtcgl_text_7", request) + buckleDate + MessageUtils.extractMessage("ydyw", "ydyw_ywgl_ywtcgl_text_5", request));
                                } else {
                                    out.print(MessageUtils.extractMessage("common", "text_null", request));
                                }
                            }
                        }
                    %>
                </td>
                <td class="colspace"></td>
                <td class="colName"><emp:message key="ydyw_ywgl_ywtcgl_text_15" fileName="ydyw"
                                                 defVal="补扣次数："></emp:message></td>
                <td class="colValue"><%=charges != null && charges.getBuckupmaxtimer() != null ? charges.getBuckupmaxtimer() : "0" %>
                    <%if (!"zh_HK".equals(empLangName)) {%>
                    <emp:message key="text_times" fileName="common" defVal="次"></emp:message>
                    <%}%>
                </td>
            </tr>
            <tr>
                <td class="colName"><emp:message key="ydyw_qyjfcx_khqygl_text_28" fileName="ydyw"
                                                 defVal="补扣间隔时间："></emp:message></td>
                <td class="colValue"><%=charges != null && charges.getBuckupintervalday() != null ? charges.getBuckupintervalday() : "0" %>
                    <emp:message key="text_sky" fileName="common" defVal="天"></emp:message>
                </td>
                <td class="colspace"></td>
                <td class="colName"><emp:message key="ydyw_ywgl_ywtcgl_text_8" defVal="套餐有效期："
                                                 fileName="ydyw"></emp:message></td>
                <%
                    String start_date = "";
                    String end_date = "";
                    if (taocan != null) {
                        if (taocan.getStart_date() != null) {
                            String start = taocan.getStart_date() + "";
                            if (start.indexOf(" ") > -1) {
                                String[] starts = start.split(" ");
                                if (starts.length > 1) {
                                    start_date = starts[0];
                                }

                            }
                        }
                        if (taocan.getEnd_date() != null) {
                            String end = taocan.getEnd_date() + "";
                            if (end.indexOf(" ") > -1) {
                                String[] ends = end.split(" ");
                                if (ends.length > 1) {
                                    end_date = ends[0];
                                }

                            }
                        }

                    }
                %>
                <td class="colValue"><%=start_date %> <emp:message key="text_to_p" defVal="至"
                                                                   fileName="common"></emp:message> <%=end_date %>
                </td>
            </tr>
            <tr>
                <td class="colName" valign="top" style="padding-top: 12px;"><emp:message
                        key="ydyw_qyjfcx_khqygl_text_29" defVal="已添加业务包：" fileName="ydyw"></emp:message></td>
                <td class="colValue" valign="top" colspan="4">
                    <div style="display:block;padding: 8px;background-color: whitesmoke; word-wrap: break-word;word-break:break-all;overflow-y:auto;height: 150px;line-height: 22px;width: 580px;"><%=info %>
                    </div>
                </td>
            </tr>
        </table>
    </div>
</div>
<script type="text/javascript"
        src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript"
        src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript"
        src="<%=commonPath%>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript"
        src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=iPath%>/js/taocanList.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=empLangName%>/ydyw_<%=empLangName%>.js"></script>
</body>
</html>