<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.montnets.emp.common.constant.ViewParams" %>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils" %>
<%@ page import="com.montnets.emp.mobilebus.constant.MobileBusStaticValue" %>
<%@ page import="com.montnets.emp.report.vo.ChargeStatsRptVo" %>
<%@ page import="com.montnets.emp.util.PageInfo" %>
<%@ page import="java.util.List" %>
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
    long sumtaocanmoney = 0l;
    if (session.getAttribute("sumtaocanmoney") != null) {
        sumtaocanmoney = Long.parseLong(session.getAttribute("sumtaocanmoney").toString());
    }
    @SuppressWarnings("unchecked")
    Map<String, String> btnMap = (Map<String, String>) session.getAttribute("btnMap");//按钮权限Map
    @SuppressWarnings("unchecked")
    Map<String, String> titleMap = (Map<String, String>) session.getAttribute("titleMap");
    @SuppressWarnings("unchecked")
    ChargeStatsRptVo chargestatsrptvo = (ChargeStatsRptVo) request.getAttribute("cstatrptvo");
    List<ChargeStatsRptVo> resultList = (List<ChargeStatsRptVo>) session.getAttribute("catresultList");
    //套餐编码
    String taocancode = chargestatsrptvo != null && chargestatsrptvo.getTaocancode() != null ? chargestatsrptvo.getTaocancode().toString() : "";
    //套餐名称
    String taocanname = chargestatsrptvo != null && chargestatsrptvo.getTaocanname() != null ? chargestatsrptvo.getTaocanname().toString() : "";
    //套餐类型
    String taocantype = chargestatsrptvo != null && chargestatsrptvo.getTaocantype() != null ? chargestatsrptvo.getTaocantype().toString() : "";

    String menuCode = titleMap.get("chargeStatsRpt");
    menuCode = menuCode == null ? "0-0-0" : menuCode;
    String findResult = null;
    findResult = (String) request.getAttribute("findresult");
    String skin = session.getAttribute("stlyeSkin") == null ? "default" : (String) session.getAttribute("stlyeSkin");
    String counTime = request.getAttribute("countTime").toString();
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
    <%if (StaticValue.ZH_HK.equals(empLangName)) {%>
    <link rel="stylesheet" type="text/css"
          href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
    <link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
    <%}%>
</head>
<body>
<div id="container" class="container">
    <%-- 当前位置 --%>

    <%=ViewParams.getPosition(empLangName, menuCode) %>
    <%-- 内容开始 --%>
    <%
        if (btnMap.get(menuCode + "-0") != null) {
    %>
    <div id="rContent" class="rContent">
        <input type="hidden" value="<%=inheritPath%>" id="inheritPath"/>
        <input type="hidden" value="month" name="yearOrMonth"/>
        <input type="hidden" value="2" name="count"/>
        <form name="pageForm" action="ydyw_chargeStatsRpt.htm" method="post" id="pageForm">
            <div class="buttons">
                <div id="toggleDiv">
                </div>
                <%
                    if (btnMap.get(menuCode + "-5") != null) {
                %>
                <a id="exportCurrent"></a>
                <a id="exportCondition"></a>
                <input type="hidden" name="menucode" id="menucode" value="17"/>
                <%}%>
                <a id="exportCondition"><emp:message key="common_export" defVal="导出"
                                                     fileName="common"></emp:message></a>
            </div>
            <div id="condition">
                <table>
                    <tbody>
                    <tr>
                        <td>
                            <emp:message key="ydyw_ywgl_ywbgl_text_53" defVal="时间类型：" fileName="ydyw"></emp:message>
                        </td>
                        <td>
                            <select name="reportType" id="reportType" style="width:180px">
                                <%if (chargestatsrptvo != null) {%>
                                <option value="0" <%=chargestatsrptvo.getImonth() == null ? "" : "selected" %>>
                                    <emp:message key="ydyw_qytjbb_ywtcjftj_text_1" defVal="按月查询"
                                                 fileName="ydyw"></emp:message></option>
                                <option value="1" <%=chargestatsrptvo.getImonth() == null ? "selected" : "" %>>
                                    <emp:message key="ydyw_qytjbb_ywtcjftj_text_2" defVal="按年查询"
                                                 fileName="ydyw"></emp:message></option>
                                <%} else {%>
                                <option value="0"><emp:message key="ydyw_qytjbb_ywtcjftj_text_1" defVal="按月查询"
                                                               fileName="ydyw"></emp:message></option>
                                <option value="1"><emp:message key="ydyw_qytjbb_ywtcjftj_text_2" defVal="按年查询"
                                                               fileName="ydyw"></emp:message></option>
                                <%} %>
                            </select>
                        </td>
                        <td>
                            <emp:message key="ydyw_qytjbb_ywtcjftj_text_3" defVal="统计年月：" fileName="ydyw"></emp:message>
                        </td>
                        <td>
                            <%if (chargestatsrptvo != null) {%>
                            <input type="text" name="countTime" id="countTime"
                                   value="<%=chargestatsrptvo.getImonth() == null?counTime.toString().substring(0,4):counTime %>"
                                   onclick="showDate()" readonly="readonly" class="Wdate"
                                   style="cursor: pointer; width: 178px;"/>
                            <input type="hidden" id="tTime" value="<%=counTime %>"/>
                            <%} else { %>
                            <input type="text" name="countTime" id="countTime" onclick="showDate()"
                                   readonly="readonly" class="Wdate"
                                   style="cursor: pointer; width: 178px;"/>
                            <input type="hidden" id="tTime" value="<%=counTime %>"/>
                            <%} %>
                        </td>
                        <%
                            String typename = "";
                            Map<String, String> tctypemap = MobileBusStaticValue.getTaoCanType();
                            if (tctypemap != null && tctypemap.get("-1") != null) {
                                typename = tctypemap.get("-1");
                            }
                        %>
                        <td>
                            <%--<%=typename%>：--%>
                            <emp:message key="ydyw_ywgl_ywbgl_text_37" defVal="计费类型：" fileName="ydyw"></emp:message>
                        </td>
                        <td>
                            <select name="taocantype" id="taocantype" style="width: 182px">
                                <option value=""><emp:message key="common_whole" defVal="全部"
                                                              fileName="common"></emp:message></option>
                                <%
                                    if (tctypemap != null && tctypemap.get("-1") != null) {
                                        for (String key : tctypemap.keySet()) {
                                            String value = tctypemap.get(key);
                                            if ("VIP免费".equals(value)) {
                                                value = MessageUtils.extractMessage("ydyw", "ydyw_ywgl_tczlsz_text_5", request);
                                            } else if ("包月".equals(value)) {
                                                value = MessageUtils.extractMessage("ydyw", "ydyw_ywgl_tczlsz_text_by", request);
                                            } else if ("包季".equals(value)) {
                                                value = MessageUtils.extractMessage("ydyw", "ydyw_ywgl_tczlsz_text_bj", request);
                                            } else if ("包年".equals(value)) {
                                                value = MessageUtils.extractMessage("ydyw", "ydyw_ywgl_tczlsz_text_bn", request);
                                            }
                                            if (key != null && !"-1".equals(key)) {
                                %>
                                <option value="<%=key %>" <%=taocantype.equals(key) ? "selected" : ""%>><%= value %>
                                </option>
                                <%
                                            }
                                        }
                                    }
                                %>
                            </select>
                        </td>

                        <td class="tdSer">
                            <center>
                                <a id="search" name="research"></a>
                            </center>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <emp:message key="ydyw_ywgl_ywbgl_text_33" defVal="套餐名称：" fileName="ydyw"></emp:message>
                        </td>
                        <td>
                            <input type="text" style="width: 177px;" value="<%=taocanname == null ? "" : taocanname%>"
                                   id="taocanname" name="taocanname" maxlength="25"/>
                        </td>
                        <td>
                            <emp:message key="ydyw_ywgl_ywbgl_text_35" defVal="套餐编号：" fileName="ydyw"></emp:message>
                        </td>
                        <td>
                            <input type="text" style="width: 177px;" onkeyup="value=value.replace(/[^\w\.\/]/ig,'')"
                                   value="<%=taocancode == null ? "" : taocancode%>" id="taocancode" name="taocancode"
                                   maxlength="25"/>
                        </td>
                        <td></td>
                        <td></td>
                        <td></td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <table id="content">
                <thead>
                <tr>
                    <th style="width: 8%;">
                        <emp:message key="ydyw_qytjbb_ywtcjftj_text_4" defVal="时间" fileName="ydyw"></emp:message>
                    </th>
                    <th>
                        <emp:message key="ydyw_ywgl_ywbgl_text_32" defVal="套餐名称" fileName="ydyw"></emp:message>
                    </th>
                    <th>
                        <emp:message key="ydyw_ywgl_ywbgl_text_34" defVal="套餐编号" fileName="ydyw"></emp:message>
                    </th>
                    <th>
                        <emp:message key="ydyw_ywgl_ywbgl_text_36" defVal="计费类型" fileName="ydyw"></emp:message>
                    </th>
                    <th>
                        <emp:message key="ydyw_qytjbb_jgjftj_text_6" defVal="资费（元）" fileName="ydyw"></emp:message>
                    </th>
                    <th>
                        <emp:message key="ydyw_qytjbb_jgjftj_text_7" defVal="签约人数" fileName="ydyw"></emp:message>
                    </th>
                    <th>
                        <emp:message key="ydyw_qytjbb_jgjftj_text_8" defVal="扣费成功数" fileName="ydyw"></emp:message>
                    </th>
                    <th>
                        <emp:message key="ydyw_qytjbb_jgjftj_text_9" defVal="扣费失败数" fileName="ydyw"></emp:message>
                    </th>
                    <th>
                        <emp:message key="ydyw_qytjbb_jgjftj_text_3" defVal="扣费总金额（元）" fileName="ydyw"></emp:message>
                    </th>
                    <th>
                        <emp:message key="ydyw_qytjbb_jgjftj_text_4" defVal="退费总金额（元）" fileName="ydyw"></emp:message>
                    </th>
                    <th>
                        <emp:message key="ydyw_qytjbb_jgjftj_text_5" defVal="实际总收入（元）" fileName="ydyw"></emp:message>
                    </th>
                </tr>
                </thead>
                <tbody>
                <%
                    if (isFirstEnter) {
                %>
                <tr>
                    <td colspan="11" align="center">
                        <emp:message key="ydyw_qyjfcx_khjfcx_text_1" defVal="请点击查询获取数据" fileName="ydyw"></emp:message>
                    </td>
                </tr>
                <%
                } else {
                    if (findResult != null) {
                %>
                <tr>
                    <td colspan="11" align="center">
                        <emp:message key="common_norecord" defVal="无记录" fileName="common"></emp:message>
                    </td>
                </tr>
                <%
                } else {
                    if (resultList != null && resultList.size() > 0) {
                        for (int i = 0; i < resultList.size(); i++) {
                            ChargeStatsRptVo chargestatevo = resultList.get(i);
                            if (chargestatevo == null) {
                                chargestatevo = new ChargeStatsRptVo();
                            }
                %>
                <tr>
                    <td><%=chargestatsrptvo.getImonth() == null ? chargestatsrptvo.getY() : chargestatsrptvo.getY() + "-" + chargestatsrptvo.getImonth()%>
                    </td>
                    <td><%=chargestatevo.getTaocanname() != null ? chargestatevo.getTaocanname() : "" %>
                    </td>
                    <td><%=chargestatevo.getTaocancode() != null ? chargestatevo.getTaocancode() : "" %>
                    </td>
                    <%
                        String tctype = null;
                        if (chargestatevo.getTaocantype() != null && !"".equals(chargestatevo.getTaocantype())) {
                            tctype = tctypemap.get(chargestatevo.getTaocantype().toString());
                            if ("VIP免费".equals(tctype)) {
                                tctype = MessageUtils.extractMessage("ydyw", "ydyw_ywgl_tczlsz_text_5", request);
                            } else if ("包月".equals(tctype)) {
                                tctype = MessageUtils.extractMessage("ydyw", "ydyw_ywgl_tczlsz_text_by", request);
                            } else if ("包季".equals(tctype)) {
                                tctype = MessageUtils.extractMessage("ydyw", "ydyw_ywgl_tczlsz_text_bj", request);
                            } else if ("包年".equals(tctype)) {
                                tctype = MessageUtils.extractMessage("ydyw", "ydyw_ywgl_tczlsz_text_bn", request);
                            }
                        }
                    %>
                    <td><%=chargestatevo.getTaocantype() != null ? tctype != null ? tctype : MessageUtils.extractMessage("common", "text_null", request) : MessageUtils.extractMessage("common", "text_null", request)%>
                    </td>
                    <td><%=chargestatevo.getTaocanmoney() != null ? chargestatevo.getTaocanmoney() : "0" %>
                    </td>
                    <td><%=chargestatevo.getContractcount() != null ? chargestatevo.getContractcount() : "0" %>
                    </td>
                    <td><%=chargestatevo.getDeductioncount() != null ? chargestatevo.getDeductioncount() : 0 %>
                    </td>
                    <td><%=chargestatevo.getDeductionfailcount() != null ? chargestatevo.getDeductionfailcount() : 0 %>
                    </td>
                    <td><%=chargestatevo.getDeductiontotalcount() != null ? chargestatevo.getDeductiontotalcount() : 0 %>
                    </td>
                    <td><%=chargestatevo.getBackmoney() != null ? chargestatevo.getBackmoney() : 0 %>
                    </td>
                    <td><%=chargestatevo.getTotalmoney() != null ? chargestatevo.getTotalmoney() : 0 %>
                    </td>
                </tr>
                <%
                    }
                %>
                <tr>
                    <td colspan="10">
                        <b><emp:message key="ydyw_qytjbb_ywtcjftj_text_5" defVal="合计："
                                        fileName="ydyw"></emp:message></b>
                    </td>
                    <td>
                        <b><%=sumtaocanmoney %>
                        </b>
                    </td>
                </tr>
                <%
                } else {
                %>
                <tr>
                    <td colspan="11" align="center">
                        <emp:message key="common_norecord" defVal="无记录" fileName="common"></emp:message>
                    </td>
                </tr>
                <%
                            }
                        }
                    }
                %>
                </tbody>
                <tfoot>
                <tr>
                    <td colspan="11">
                        <input type="hidden" name="pageTotalRec" id="pageTotalRec" value="<%=pageInfo.getTotalRec()%>"/>
                        <input type="hidden" name="queryTime" id="queryTime"
                               value="<%=request.getParameter("begintime") == null ? "" : request.getParameter("begintime")%><emp:message key="text_to_p" defVal="至" fileName="common"></emp:message><%=request.getParameter("endtime") == null ? "" : request.getParameter("endtime")%>"/>
                        <div id="pageInfo"></div>
                    </td>
                </tr>
                </tfoot>
            </table>
            <div id="corpCode" class="hidden"></div>
        </form>
    </div>
    <%
        }
    %>
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
<script type="text/javascript"
        src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript"
        src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<link rel="stylesheet"
      href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>">
<script type="text/javascript"
        src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript"
        src="<%=commonPath%>/common/js/jquery.ztree-myjquery-b.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript"
        src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript"
        src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=empLangName%>/ydyw_<%=empLangName%>.js"></script>
<script type="text/javascript">

    function showDate() {
        var r = $("#reportType").attr("value");
        if (r == 0) {
            //WdatePicker({skin:'simple',dateFmt:'yyyy-MM',isShowClear:true});
            WdatePicker({dateFmt: 'yyyy-MM', isShowClear: false});
        }
        else {
            //WdatePicker({skin:'simple',dateFmt:'yyyy',isShowClear:true});
            WdatePicker({dateFmt: 'yyyy', isShowClear: false});
        }
    }


    $(document).ready(function () {
        getLoginInfo("#corpCode");
        var findresult = "<%=findResult%>";
        if (findresult == "-1") {
            alert(getJsLocaleMessage("ydyw", "ydyw_text_error_1"));
            return;
        }
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
            checkSubmit();
        });
        $("#reportType").change(function () {
            var t = $("#countTime").attr("value");
            if ($("#reportType").attr("value") == "1") {
                $("#countTime").attr("value", t.toString().substring(0, 4));
            }
            else {
                $("#countTime").attr("value", $("#tTime").val());
            }
        });

        function checkSubmit() {
            if (checkSubmitFlag == true) {
                $("a[name='research']").attr("title", getJsLocaleMessage("ydyw", "ydyw_text_error_3"));
                submitForm();//选择好时间段，才允许查询
                checkSubmitFlag = false;
            }
        }


        //导出全部数据到excel
        $("#exportCondition").click(
            function () {
                if (confirm(getJsLocaleMessage("ydyw", "ydyw_text_error_5"))) {
                    var countTime = encodeURIComponent(encodeURIComponent('<%=chargestatsrptvo.getImonth() == null?counTime.toString().substring(0,4):counTime %>'));
                    var reportType = '<%=chargestatsrptvo!=null?(chargestatsrptvo.getImonth() == null?"1":"0"):"0"%>';
                    var taocanname = '<%=taocanname %>';
                    var taocantype = '<%=taocantype %>';
                    var taocancode = '<%=taocancode %>';
                    <%
                    //备份 p
                    //List<ChargeStatsRptVo> list = (List<ChargeStatsRptVo>) session.getAttribute("catresultList");
                    //新增  p
                 List<ChargeStatsRptVo> list = (List<ChargeStatsRptVo>)request.getAttribute("catresultList_p");
                 if(list != null && list.size() > 0){
                 %>

                    <%--新增 p--%>
                    $.ajax({
                        type: "POST",
                        url: "ydyw_chargeStatsRpt.htm?method=r_sdRptExportExcel",
                        data: {
                            lgcorpcode: '<%=request.getParameter("lgcorpcode")%>',
                            countTime: countTime,
                            reportType: reportType,
                            taocanname: taocanname,
                            taocantype: taocantype,
                            taocancode: taocancode,
                            empLangName: "<%=empLangName%>"
                        },
                        beforeSend: function () {
                            page_loading();
                        },
                        complete: function () {
                            page_complete();
                        },
                        success: function (result) {
                            if (result == 'true') {
                                download_href("ydyw_chargeStatsRpt.htm?method=downloadFile");
                            } else {
                                alert(getJsLocaleMessage("ydyw", "ydyw_qytjbb_ywtcjftj_text_6"));
                            }
                        }
                    });

                    <%-- 备份 p
                    location.href="<%=path%>/ydyw_chargeStatsRpt.htm?method=r_sdRptExportExcel&lgcorpcode=<%=request.getParameter("lgcorpcode")%>&countTime="
                    +countTime+"&reportType="+reportType+"&taocanname="+taocanname+"&taocantype="+taocantype+"&taocancode="+taocancode;
                       --%>
                    <%}else{%>
                    alert(getJsLocaleMessage("ydyw", "ydyw_text_error_4"));
                    <%}%>
                }
            });
    });

    function numberControl(va) {
        var pat = /^\d*$/;
        if (!pat.test(va.val())) {
            va.val(va.val().replace(/[^\d]/g, ''));
        }
    }
</script>
</body>
</html>
