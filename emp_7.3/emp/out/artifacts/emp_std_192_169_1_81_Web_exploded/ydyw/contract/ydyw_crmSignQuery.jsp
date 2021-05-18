<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ page import="com.montnets.emp.common.constant.ViewParams" %>
<%@ page import="com.montnets.emp.entity.ydyw.LfBusTaoCan" %>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils" %>
<%@page import="com.montnets.emp.mobilebus.constant.MobileBusStaticValue" %>
<%@page import="com.montnets.emp.util.PageInfo" %>
<%@page import="org.apache.commons.beanutils.DynaBean" %>
<%@page import="org.apache.commons.lang.StringUtils" %>
<%@page import="java.util.Iterator" %>
<%@page import="java.util.List" %>
<%@page import="java.util.Map" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    String iPath = request.getRequestURI().substring(0, request.getRequestURI().lastIndexOf("/"));
    String inheritPath = iPath.substring(0, iPath.lastIndexOf("/"));
    String commonPath = inheritPath.substring(0, inheritPath.lastIndexOf("/"));

    @SuppressWarnings("unchecked")
    Map<String, String> btnMap = (Map<String, String>) session.getAttribute("btnMap");//按钮权限Map

    @SuppressWarnings("unchecked")
    Map<String, String> titleMap = (Map<String, String>) session.getAttribute("titleMap");
    String menuCode = titleMap.get("crmSignQuery");
    PageInfo pageInfo = request.getAttribute("pageInfo") == null ? new PageInfo() : (PageInfo) request.getAttribute("pageInfo");
    String skin = session.getAttribute("stlyeSkin") == null ? "default" : (String) session.getAttribute("stlyeSkin");
    List<DynaBean> lists = request.getAttribute("lists") != null ? (List<DynaBean>) request.getAttribute("lists") : null;
    String chargesType = StringUtils.defaultString(request.getParameter("chargesType"));
    String contractState = StringUtils.defaultString(request.getParameter("contractState"));
    boolean isFirstEnter = request.getAttribute("isFirstEnter") == null ? true : (Boolean) request.getAttribute("isFirstEnter");
    String isAll = StringUtils.defaultString(request.getParameter("isAll"));
    Map<String, String> taoCanType = MobileBusStaticValue.getTaoCanType();
    boolean add = btnMap.get(menuCode + "-1") != null;
    boolean edit = btnMap.get(menuCode + "-2") != null;
    boolean cancel = btnMap.get(menuCode + "-3") != null;
%>
<%!
    public String getZF(int type, String money, String empLangName) {
        String free = "zh_HK".equals(empLangName) ? "Free" : "zh_CN".equals(empLangName) ? "免费" : "免費";
        String monthly = "zh_HK".equals(empLangName) ? "%d RMB/month" : "%d元/月";
        String quarterly = "zh_HK".equals(empLangName) ? "%d RMB/quarter" : "%d元/季";
        String yearly = "zh_HK".equals(empLangName) ? "%d RMB/year" : "%d元/年";
        String[] zfs = new String[]{"", free, monthly, quarterly, yearly};
        return zfs[type].replace("%d", money);
    }
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
<head>
    <%@include file="/common/common.jsp" %>
    <title><%=titleMap.get(menuCode) %>
    </title>
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
    <link rel="stylesheet" type="text/css"
          href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion() %>"/>
    <link rel="stylesheet"
          href="<%=commonPath %>/common/widget/zTreeStyle/zTreeStyle.css?V=<%=StaticValue.getJspImpVersion() %>"
          type="text/css"/>
    <%if (StaticValue.ZH_HK.equals(empLangName)) {%>
    <link rel="stylesheet" type="text/css"
          href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
    <link rel="stylesheet" type="text/css"
          href="<%=commonPath%>/ydyw/contract/css/crmSignQuery_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
    <link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
    <%} else {%>
    <link rel="stylesheet" type="text/css"
          href="<%=commonPath%>/ydyw/contract/css/crmSignQuery.css?V=<%=StaticValue.getJspImpVersion()%>"/>
    <%}%>

    <style type="text/css">
        .c_selectBox {
            width: 208px !important;
        }

        #condition .c_selectBox ul {
            width: 208px !important;
        }

        #condition .c_selectBox ul li {
            width: 208px !important;
        }
    </style>

</head>
<body>
<div id="container" class="container">
    <%-- 当前位置 --%>

    <%=ViewParams.getPosition(empLangName, menuCode) %>
    <%-- 内容开始 --%>
    <div id="rContent" class="rContent">
        <form name="pageForm" action="ydyw_crmSignQuery.htm" method="post" id="pageForm">
            <%-- 表示请求的名称 --%>
            <div id="loginUser" class="hidden"></div>
            <div class="buttons">
                <div id="toggleDiv">
                </div>
                <%if (add) {%><a id="add"><emp:message key="ydyw_qyjfcx_khqygl_btn_1" fileName="ydyw"
                                                       defVal="签约"></emp:message></a><%} %>
                <%--<a id="exportCondition">导出</a>  --%>
            </div>
            <div id="condition">
                <table>

                    <tbody>
                    <tr>
                        <td><emp:message key="ydyw_ywgl_ywbgl_text_39" defVal="手机号码："
                                         fileName="ydyw"></emp:message></td>
                        <td><input type="text" id="mobile" name="mobile" onkeyup="javascript:phoneInputCtrl($(this))"
                                   value="<%=StringUtils.defaultString(request.getParameter("mobile")) %>"/></td>
                        <td><emp:message key="ydyw_ywgl_ywmbpz_text_57" defVal="姓名：" fileName="ydyw"></emp:message></td>
                        <td><input type="text" id="name" name="name"
                                   value="<%=StringUtils.defaultString(request.getParameter("name")) %>"></td>
                        <td><emp:message key="ydyw_ywgl_ywbgl_text_35" defVal="套餐编号："
                                         fileName="ydyw"></emp:message></td>
                        <td><input type="text" id="tcCode" name="tcCode"
                                   value="<%=StringUtils.defaultString(request.getParameter("tcCode")) %>"></td>
                        <td class="tdSer">
                            <center>
                                <a id="search"></a>
                            </center>
                        </td>
                    </tr>
                    <tr>
                        <td><emp:message key="ydyw_ywgl_ywbgl_text_47" defVal="签约账号："
                                         fileName="ydyw"></emp:message></td>
                        <td><input type="text" id="acctNo" name="acctNo"
                                   value="<%=StringUtils.defaultString(request.getParameter("acctNo")) %>"/></td>
                        <td><emp:message key="ydyw_ywgl_ywbgl_text_31" defVal="状态：" fileName="ydyw"></emp:message></td>
                        <td>
                            <select class="newSelect" name="contractState" id="contractState" isInput="false">
                                <option value=""><emp:message key="common_whole" defVal="全部"
                                                              fileName="common"></emp:message></option>
                                <%
                                    /*签约类型*/
                                    Map<String, String> contractType;
                                    if ("zh_HK".equals(empLangName)) {
                                        contractType = MobileBusStaticValue.contractType_zh_HK;
                                    } else if ("zh_TW".equals(empLangName)) {
                                        contractType = MobileBusStaticValue.contractType_zh_TW;
                                    } else {
                                        contractType = MobileBusStaticValue.contractType;
                                    }
                                    Iterator<String> items = contractType.keySet().iterator();
                                    while (items.hasNext()) {
                                        String key = items.next();
                                %>
                                <option value="<%=key %>" <%=key.equals(contractState)?"selected='selected'":key %>  ><%= contractType.get(key) %>
                                </option>
                                <%
                                    }
                                %>
                            </select>
                        </td>
                        <td><emp:message key="ydyw_ywgl_ywbgl_text_24" defVal="操作员：" fileName="ydyw"></emp:message></td>
                        <td colspan="2"><input type="text" id="opName" name="opName"
                                               value="<%=StringUtils.defaultString(request.getParameter("opName")) %>"/>
                        </td>
                    </tr>
                    <tr>
                        <td><emp:message key="ydyw_ywgl_ywbgl_text_26" defVal="机构：" fileName="ydyw"></emp:message></td>
                        <td class="condi_f_l" style="position: relative;">
                            <input type="hidden" name="depName" id="depName"
                                   value="<%=StringUtils.defaultString(request.getParameter("depName")) %>"/>
                            <input id="opt-dep" class="treeInput" style="width:130px;" name="depNameStr" type="text"
                                   readonly
                                   value="<%=StringUtils.defaultString(request.getParameter("depNameStr"),MessageUtils.extractMessage("common","common_pleaseSelect",request)) %>"/>
                            <div id="dropMenu"
                                 style="left:5px;top:30px; display:none;margin-left:0px;height:450px; width:340px; background-color:white;border:1px solid;overflow-y:auto;overflow-x:auto;z-index:999;">
                                <div style="padding:6px;text-align: right;">
                                    <input type="checkbox" id="isAll" name="isAll"
                                           style="width:15px;height:15px;vertical-align:middle;margin-right:3px;" <%
                                        if (isFirstEnter || "on".equals(isAll)) {
                                            out.print("checked");
                                        }
                                    %>/><emp:message key="ydyw_qyjfcx_khtfgl_text_9" defVal="包含子机构"
                                                     fileName="ydyw"></emp:message>
                                    <input type="button"
                                           value="<emp:message key="common_confirm" defVal="确定" fileName="common"></emp:message>"
                                           class="btnClass1" onclick="zTreeOnSure();">&nbsp;&nbsp;
                                    <input type="button"
                                           value="<emp:message key="common_clean" defVal="清空" fileName="common"></emp:message>"
                                           class="btnClass1" onclick="zTreeOnCancel();">
                                </div>
                                <ul id="dropdownMenu" class="tree">
                                </ul>
                            </div>
                        </td>
                        <td><emp:message key="ydyw_qyjfcx_khqygl_text_1_p" defVal="签约时间："
                                         fileName="ydyw"></emp:message></td>
                        <td><input type="text" name="starttime" class="Wdate startdate" readonly="readonly"
                                   value="<%=StringUtils.defaultString(request.getParameter("starttime")) %>"/></td>
                        <td><emp:message key="common_to" defVal="至：" fileName="common"></emp:message></td>
                        <td colspan="2"><input name="endtime" type="text" class="Wdate enddate" readonly="readonly"
                                               value="<%=StringUtils.defaultString(request.getParameter("endtime")) %>"/>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <table id="content">
                <thead>
                <tr>
                    <th><emp:message key="ydyw_ywgl_ywbgl_text_38" defVal="手机号码" fileName="ydyw"></emp:message></th>
                    <th><emp:message key="ydyw_ywgl_ywmbpz_text_56" defVal="姓名" fileName="ydyw"></emp:message></th>
                    <th><emp:message key="ydyw_ywgl_ywbgl_text_46" defVal="签约账号" fileName="ydyw"></emp:message></th>
                    <th><emp:message key="ydyw_ywgl_ywbgl_text_40" defVal="扣费账号" fileName="ydyw"></emp:message></th>
                    <th><emp:message key="ydyw_ywgl_ywbgl_text_48" defVal="签约套餐" fileName="ydyw"></emp:message></th>
                    <th><emp:message key="ydyw_ywgl_ywbgl_text_50" defVal="签约状态" fileName="ydyw"></emp:message></th>
                    <th><emp:message key="ydyw_qyjfcx_khqygl_text_1" defVal="签约时间" fileName="ydyw"></emp:message></th>
                    <th><emp:message key="ydyw_qyjfcx_khqygl_text_2" defVal="签约来源" fileName="ydyw"></emp:message></th>
                    <th><emp:message key="ydyw_ywgl_ywbgl_text_25" defVal="机构" fileName="ydyw"></emp:message></th>
                    <th><emp:message key="ydyw_ywgl_ywbgl_text_23" defVal="操作员" fileName="ydyw"></emp:message></th>
                    <th><emp:message key="common_operation" defVal="操作" fileName="common"></emp:message></th>
                </tr>
                </thead>
                <tbody>
                <%
                    Map<String, List<LfBusTaoCan>> tcMap = (Map<String, List<LfBusTaoCan>>) request.getAttribute("tcMap");
                    if (tcMap != null && lists != null && lists.size() > 0) {
                        for (DynaBean bean : lists) {
                            String id = String.valueOf(bean.get("contract_id"));
                            String guid = String.valueOf(bean.get("guid"));
                            String mobile = String.valueOf(bean.get("mobile"));
                            String customName = String.valueOf(bean.get("custom_name"));
                            String cardIdentType = bean.get("acct_identtype") == null ? "" : String.valueOf(bean.get("acct_identtype"));
                            String cardNo = bean.get("acct_identno") == null ? "-" : String.valueOf(bean.get("acct_identno"));
                            String acctNo = bean.get("acct_no") == null ? "-" : String.valueOf(bean.get("acct_no"));
                            String acctName = bean.get("acct_name") == null ? "" : String.valueOf(bean.get("acct_name"));
                            String feeAcctNo = bean.get("debitaccount") == null ? "-" : String.valueOf(bean.get("debitaccount"));
                            String feeAcctName = bean.get("debitacct_name") == null ? "" : String.valueOf(bean.get("debitacct_name"));
                            String contractFlag = bean.get("contract_state") == null ? "" : String.valueOf(bean.get("contract_state"));
                            String contractDate = bean.get("contract_date") == null ? "-" : String.valueOf(bean.get("contract_date"));
                            String cancelTime = bean.get("cancel_contime") == null ? "-" : String.valueOf(bean.get("cancel_contime"));
                            if (cancelTime.length() > 19) {
                                cancelTime = cancelTime.substring(0, 19);
                            }
                            /*取消签约方式*/
                            Map<String, String> cancelconType;
                            if ("zh_HK".equals(empLangName)) {
                                cancelconType = MobileBusStaticValue.cancelconType_zh_HK;
                            } else if ("zh_TW".equals(empLangName)) {
                                cancelconType = MobileBusStaticValue.cancelconType_zh_TW;
                            } else {
                                cancelconType = MobileBusStaticValue.cancelconType;
                            }
                            String cancelType = bean.get("cancel_contype") == null ? "-" : String.valueOf(bean.get("cancel_contype"));
                            cancelType = StringUtils.defaultString(cancelconType.get(cancelType), "-");
                            if (!"1".equals(contractFlag)) {
                                cancelType = "-";
                            }//已取消签约状态才显示取消签约方式
                            if (!"1".equals(contractFlag) && !"2".equals(contractFlag)) {
                                cancelTime = "-";
                            }//取消签约和冻结显示
                            if (contractDate.length() > 19) {
                                contractDate = contractDate.substring(0, 19);
                            }
                            String contract_source = bean.get("contract_source") == null ? "" : String.valueOf(bean.get("contract_source"));
                            String depName = String.valueOf(bean.get("dep_name"));
                            String opName = String.valueOf(bean.get("name"));
                            String userName = String.valueOf(bean.get("user_name"));
                            String cliCode = bean.get("client_code") == null ? "" : String.valueOf(bean.get("client_code"));
                            String address = bean.get("address") == null ? "" : String.valueOf(bean.get("address"));
                            List<LfBusTaoCan> tcs = tcMap.get(id);
                %>
                <tr>
                    <td data-id="mobile" class="maxwidth8"><span><%=mobile %></span></td>
                    <td data-id="customName" class="maxwidth8"><span><%=customName %></span></td>
                    <td data-id="acct" class="maxwidth"><span><%=acctNo %><% if (StringUtils.isNotBlank(acctName)) {
                        out.print("(" + acctName + ")");
                    }%></span></td>
                    <td data-id="feeAcct" class="maxwidth"><span><%=feeAcctNo%><%
                        if (StringUtils.isNotBlank(feeAcctName)) {
                            out.print("(" + feeAcctName + ")");
                        }%></span></td>
                    <td data-id="tc" class="maxwidth"><span><%
                        String taocan = "";
                        if (tcs != null) {
                            for (LfBusTaoCan tc : tcs) {
                                String state = tc.getState() != 0 ? " class='line'" : "";
                                String st = tc.getState() != 0 ? "(" + MessageUtils.extractMessage("ydyw", "ydyw_ywgl_ywbgl_text_5", request) + ")" : "";
                                String type = taoCanType.get(String.valueOf(tc.getTaocan_type()));
                                String tcname = tc.getTaocan_name() + "(" + tc.getTaocan_code() + ")";
                                out.print("<b" + state + ">" + tcname + st + "</b>&nbsp;&nbsp;");
                                String zf = getZF(tc.getTaocan_type(), String.valueOf(tc.getTaocan_money()), empLangName);
                                taocan += "，" + "<b" + state + ">" + tcname + "&nbsp;&nbsp;" + zf + st + "</b>";
                            }
                        }
                        if (taocan.length() > 0) {
                            taocan = taocan.substring(1);
                        }
                    %></span></td>
                    <%
                        /*签约状态*/
                        String contracStatus = contractType.get(contractFlag);
                        /*签约来源*/
                        Map<String, String> contractSource;
                        if ("zh_HK".equals(empLangName)) {
                            contractSource = MobileBusStaticValue.contractSource_zh_HK;
                        } else if ("zh_TW".equals(empLangName)) {
                            contractSource = MobileBusStaticValue.contractSource_zh_TW;
                        } else {
                            contractSource = MobileBusStaticValue.contractSource;
                        }
                        String contractResource = contractSource.get(contract_source);
                        /*证件类型*/
                        Map<String, String> cardType;
                        if ("zh_HK".equals(empLangName)) {
                            cardType = MobileBusStaticValue.cardType_zh_HK;
                        } else if ("zh_TW".equals(empLangName)) {
                            cardType = MobileBusStaticValue.cardType_zh_TW;
                        } else {
                            cardType = MobileBusStaticValue.cardType;
                        }
                        String idCardType = cardType.get(cardIdentType);
                    %>
                    <td data-id="contractFlag"><%=StringUtils.defaultString(contracStatus, "-") %>
                    </td>
                    <td data-id="contractDate"><%=contractDate %>
                    </td>
                    <td data-id="contractSource"><%=StringUtils.defaultString(contractResource, "-")%>
                    </td>
                    <td data-id="depName" class="maxwidth8"><span><%=depName %></span></td>
                    <td data-id="opName" class="maxwidth8"><span><%=opName + "(" + userName + ")" %></span></td>
                    <td class="op" data-id="<%=id %>">
                        <div style="display:none;">
                            <em data-id="cardIdentType"><%=StringUtils.defaultString(idCardType, "-") %>
                            </em>
                            <em data-id="cardNo"><%=cardNo %>
                            </em>
                            <em data-id="taocan"><%=taocan %>
                            </em>
                            <em data-id="cliCode"><%=cliCode %>
                            </em>
                            <em data-id="address"><%=address %>
                            </em>
                            <em data-id="cancelTime"><%=cancelTime %>
                            </em>
                            <em data-id="cancelType"><%=cancelType %>
                            </em>
                        </div>
                        <a class="op more" data-id="<%=guid %>"><emp:message key="common_more" defVal="更多详情"
                                                                             fileName="common"></emp:message></a>
                        <%if (edit && "0".equals(contractFlag)) {//已签约才能修改 %><a class="op edit"><emp:message
                            key="ydyw_qyjfcx_khqygl_text_4" defVal="编辑" fileName="ydyw"></emp:message></a><%} %>
                        <%if (cancel) { %><%
                        if ("0".equals(contractFlag) || "2".equals(contractFlag)) {
                    %><a class="op state" state="1"><emp:message key="common_cancel" defVal="取消签约"
                                                                 fileName="common"></emp:message></a><%
                    } else if ("1".equals(contractFlag)) {
                    %><a class="state" state="0"><emp:message key="ydyw_qyjfcx_khqygl_text_6" defVal="恢复签约"
                                                              fileName="ydyw"></emp:message></a><%
                        }%><%} %>
                    </td>
                </tr>
                <%
                    }
                } else {
                %>
                <tr>
                    <td align="center" colspan="11"><emp:message key="common_norecord" defVal="无记录"
                                                                 fileName="common"></emp:message></td>
                </tr>
                <%}%>

                </tbody>
                <tfoot>
                <tr>
                    <td colspan="11">
                        <div id="pageInfo"></div>
                    </td>
                </tr>
                </tfoot>
            </table>
            <div id="baseparams" class="hidden"></div>
        </form>
    </div>

    <%-- foot开始 --%>
    <div class="bottom">
        <div id="bottom_right">
            <div id="bottom_left"></div>
            <div id="bottom_main">
            </div>
        </div>
    </div>
</div>
<div id="more-info" title="<emp:message key="ydyw_qyjfcx_khqygl_text_7" defVal="更多签约详情" fileName="ydyw"></emp:message>"
     style="display: none;">
    <ul>
        <li><h3><emp:message key="ydyw_qyjfcx_khqygl_text_8" defVal="用户基本信息：" fileName="ydyw"></emp:message></h3></li>
        <li class="data" data-id="mobile"><emp:message key="ydyw_ywgl_ywbgl_text_39" defVal="手机号码："
                                                       fileName="ydyw"></emp:message>&nbsp;<font></font></li>
        <li class="data" data-id="customName"><emp:message key="ydyw_ywgl_ywmbpz_text_57" defVal="姓名："
                                                           fileName="ydyw"></emp:message>&nbsp;<font></font></li>
        <li class="data" data-id="cardIdentType"><emp:message key="ydyw_qyjfcx_khqygl_text_9" defVal="证件类型："
                                                              fileName="ydyw"></emp:message>&nbsp;<font></font></li>
        <li class="data" data-id="cardNo"><emp:message key="ydyw_qyjfcx_khqygl_text_10" defVal="证件号码："
                                                       fileName="ydyw"></emp:message>&nbsp;<font></font></li>
        <li class="data" data-id="cliCode"><emp:message key="ydyw_qyjfcx_khqygl_text_11" defVal="客户编号："
                                                        fileName="ydyw"></emp:message>&nbsp;<font></font></li>
        <li class="data" data-id="cliDep"><emp:message key="ydyw_qyjfcx_khqygl_text_12" defVal="客户机构："
                                                       fileName="ydyw"></emp:message>&nbsp;<font></font></li>
        <li class="data" data-id="address" style="width:95%;"><emp:message key="ydyw_qyjfcx_khqygl_text_13"
                                                                           defVal="联系地址：" fileName="ydyw"></emp:message>&nbsp;<font></font>
        </li>
        <li class="clear"><h3><emp:message key="ydyw_qyjfcx_khqygl_text_14" defVal="签约基本信息："
                                           fileName="ydyw"></emp:message></h3></li>
        <li class="data" data-id="acct"><emp:message key="ydyw_ywgl_ywbgl_text_47" defVal="签约账号："
                                                     fileName="ydyw"></emp:message>&nbsp;<font></font></li>
        <li class="data" data-id="feeAcct"><emp:message key="ydyw_ywgl_ywbgl_text_41" defVal="扣费账号："
                                                        fileName="ydyw"></emp:message>&nbsp;<font></font></li>
        <li class="data taocan" data-id="taocan" style="width:95%;position: relative;"><emp:message
                key="ydyw_ywgl_ywbgl_text_49" defVal="签约套餐：" fileName="ydyw"></emp:message>&nbsp;<font></font></li>
        <li class="data" data-id="contractDate"><emp:message key="ydyw_qyjfcx_khqygl_text_1_p" defVal="签约时间："
                                                             fileName="ydyw"></emp:message>&nbsp;<font></font></li>
        <li class="data" data-id="contractSource"><emp:message key="ydyw_qyjfcx_khqygl_text_2_p" defVal="签约来源："
                                                               fileName="ydyw"></emp:message>&nbsp;<font></font></li>
        <li class="data" data-id="contractFlag"><emp:message key="ydyw_ywgl_ywbgl_text_51" defVal="签约状态："
                                                             fileName="ydyw"></emp:message>&nbsp;<font></font></li>
        <li class="data" data-id="cancelTime"><emp:message key="ydyw_qyjfcx_khqygl_text_15" defVal="取消签约/冻结时间："
                                                           fileName="ydyw"></emp:message>&nbsp;<font></font></li>
        <li class="data" data-id="cancelType"><emp:message key="ydyw_qyjfcx_khqygl_text_16" defVal="取消签约方式："
                                                           fileName="ydyw"></emp:message>&nbsp;<font></font></li>
        <li class="data" data-id="depName"><emp:message key="ydyw_ywgl_ywbgl_text_26" defVal="机构："
                                                        fileName="ydyw"></emp:message>&nbsp;<font></font></li>
        <li class="data" data-id="opName"><emp:message key="ydyw_ywgl_ywbgl_text_24" defVal="操作员："
                                                       fileName="ydyw"></emp:message>&nbsp;<font></font></li>
    </ul>
    <div class="clear" style="text-align: center;padding-top:16px;"><input type="button" class="btnClass6"
                                                                           value="<emp:message key="ydyw_qyjfcx_khjfcx_text_12" defVal="关闭" fileName="ydyw"></emp:message>"/>
    </div>
</div>
<div class="clear"></div>
<script type="text/javascript"
        src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript"
        src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript"
        src="<%=commonPath%>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript"
        src="<%=commonPath%>/common/js/jquery.ztree-myjquery-b.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript"
        src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript"
        src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript"
        src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript"
        src="<%=commonPath%>/common/js/wdate_extend.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=iPath%>/js/util.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript"
        src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=iPath%>/js/crmsignquery.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=empLangName%>/ydyw_<%=empLangName%>.js"></script>
<script type="text/javascript">
    $(document).ready(function () {
        getLoginInfo('#loginUser');
        $('#chargesType option[value="<%=chargesType%>"]').attr('selected', true);
        $('#contractState option[value="<%=contractState%>"]').attr('selected', true);
        $(".newSelect").isSearchSelect({'width': '152', 'isInput': false, 'zindex': 0});
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
        $('#search').click(function () {
            submitForm();
        });
        $('#add').click(function () {
            window.location.href = 'ydyw_crmSignQuery.htm?method=toAdd';
        })
        $("#exportCondition").click(function () {
            if (confirm(getJsLocaleMessage("ydyw", "ydyw_text_error_5"))) {
            }
        });
    });
</script>
</body>
</html>
