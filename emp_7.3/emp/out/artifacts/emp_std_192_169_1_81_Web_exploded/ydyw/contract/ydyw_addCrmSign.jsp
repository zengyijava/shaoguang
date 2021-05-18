<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.montnets.emp.common.constant.ViewParams" %>
<%@ page import="com.montnets.emp.entity.ydyw.LfContract" %>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils" %>
<%@ page import="com.montnets.emp.mobilebus.constant.MobileBusStaticValue" %>
<%@page import="com.montnets.emp.util.PageInfo" %>
<%@page import="org.apache.commons.lang.StringUtils" %>
<%@page import="java.util.Iterator" %>
<%@page import="java.util.Map" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    String iPath = request.getRequestURI().substring(0, request.getRequestURI().lastIndexOf("/"));
    String inheritPath = iPath.substring(0, iPath.lastIndexOf("/"));
    String commonPath = inheritPath.substring(0, inheritPath.lastIndexOf("/"));

    PageInfo pageInfo = request.getAttribute("pageInfo") == null ? new PageInfo() : (PageInfo) request.getAttribute("pageInfo");
    Map<String, String> btnMap = (Map<String, String>) session.getAttribute("btnMap");//按钮权限Map

    Map<String, String> titleMap = (Map<String, String>) session.getAttribute("titleMap");
    String menuCode = titleMap.get("crmSignQuery");
    menuCode = menuCode == null ? "0-0-0" : menuCode;

    String skin = session.getAttribute("stlyeSkin") == null ? "default" : (String) session.getAttribute("stlyeSkin");

    Map<String, String> taoCanType = MobileBusStaticValue.getTaoCanType();
    String contractId = StringUtils.defaultString(request.getParameter("contractId"));
    LfContract contract = null;
    if (StringUtils.isNotBlank(contractId)) {
        contract = (LfContract) request.getAttribute("contract");
    }
    String phone = contract != null ? StringUtils.defaultString(contract.getMobile()) : "";
    String cliname = contract != null ? StringUtils.defaultString(contract.getCustomname()) : "";
    String identType = contract != null ? StringUtils.defaultString(contract.getAcctidenttype()) : "";
    String cardNo = contract != null ? StringUtils.defaultString(contract.getAcctidentno()) : "";
    String cliCode = contract != null ? StringUtils.defaultString(contract.getClientcode()) : "";
    String guid = contract != null ? String.valueOf(contract.getGuid()) : "";
    String address = contract != null ? StringUtils.defaultString(contract.getAddress()) : "";
    String account = contract != null ? StringUtils.defaultString(contract.getAcctno()) : "";
    String accountName = contract != null ? StringUtils.defaultString(contract.getAcctname()) : "";
    String feeAccount = contract != null ? StringUtils.defaultString(contract.getDebitaccount()) : "";
    String feeAccountName = contract != null ? StringUtils.defaultString(contract.getDebitacctname()) : "";
%>
<%!
    public String escape(String str) {
        if (StringUtils.isBlank(str)) {
            return "";
        }
        return str.replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\"", "&quot;");
    }
%>
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
    <link rel="stylesheet" type="text/css" href="<%=iPath%>/css/crmsign.css?V=<%=StaticValue.getJspImpVersion() %>"/>
    <link rel="stylesheet" type="text/css" href="<%=iPath%>/css/sign.css?V=<%=StaticValue.getJspImpVersion() %>"/>
    <link rel="stylesheet"
          href="<%=commonPath %>/common/widget/zTreeStyle/zTreeStyle.css?V=<%=StaticValue.getJspImpVersion() %>"
          type="text/css"/>
    <link rel="stylesheet" type="text/css"
          href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion()%>"/>
    <%if (StaticValue.ZH_HK.equals(empLangName)) {%>
    <link rel="stylesheet" type="text/css"
          href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
    <link rel="stylesheet" type="text/css"
          href="<%=commonPath%>/ydyw/contract/css/addCrmSign.css?V=<%=StaticValue.getJspImpVersion()%>"/>
    <%}%>
    <style type="text/css">
        .c_selectBox {
            width: 208px !important;
        }

        #condition table tr td > label > input[type="radio"] {
            width: 15px !important;
            height: 15px !important;
            outline: none;
            border-width: 2px;
            border-style: solid;
            border-color: rgb(201, 201, 201);
            border-image: initial;
            border-radius: 50%;
        }

        #taocan .serach-box {
            width: 242px !important;
        }

        #taocan .c_selectBox {
            width: 70px !important;
            height: 22px !important;
            margin-left: 5px;
        }

        #taocan #tcname {
            width: 80px !important;
            height: 18px !important;
        }

        #taocan .btnClass1 {
            width: 48px !important;
            height: 24px !important;
        }

        .div-text {
            float: left;
            width: 100px;
            text-align: right;
            padding: 10px;
        }

        .div-text label {
            padding-right: 5px;
            color: #666;
        }

        .btn-submit {
            clear: left;
        }

        .btn-submit input[type="button"] {
            margin-top: 15px;
        }

    </style>
</head>

<body>
<input type="hidden" id="pathUrl" value="<%=path %>"/>
<div id="container" class="container">
    <%-- 当前位置 --%>

    <%=ViewParams.getPosition(empLangName, menuCode) %>

    <%-- 内容开始 --%>
    <div id="rContent" class="rContent">
        <form name="pageForm" action="ydyw_crmSignQuery.htm?method=add" method="post" id="pageForm">
            <div id="loginUser" class="hidden"></div>
            <input type="hidden" id="empLangName" value="<%= empLangName%>"/>
            <input type="hidden" id="_cliGuid" value="<%=guid %>"/><%-- 原始关联客户guid --%>
            <input type="hidden" id="cliGuid" value="<%=guid %>"/><%-- 当前关联客户guid --%>
            <input type="hidden" id="contractId" value="<%=contractId %>"/>
            <div id="condition">
                <table>
                    <tr>
                        <td class="caption"><emp:message key="ydyw_qyjfcx_khqygl_text_8" defVal="用户基本信息："
                                                         fileName="ydyw"></emp:message></td>
                        <td colspan="4" class="hr-wrap">
                            <div class="hr div_bd"></div>
                        </td>
                    </tr>
                    <tr>
                        <td class="label-td"><emp:message key="ydyw_ywgl_ywbgl_text_39" defVal="手机号码："
                                                          fileName="ydyw"></emp:message></td>
                        <td style="position: relative;z-index:99999;width:230px;">
                            <input id="phone" name="phone" type="text" maxlength="21" old="<%=phone %>"
                                   value="<%=phone %>"/><font class="no-blank">&#42;</font>
                            <ul id="phone-list">
                            </ul>
                        </td>
                        <td class="label-td"><emp:message key="ydyw_ywgl_ywmbpz_text_57" defVal="姓名："
                                                          fileName="ydyw"></emp:message></td>
                        <td style="width:230px;"><input type="text" id="cliname" name="cliname"
                                                        value="<%=escape(cliname)%>" maxlength="20"/><font
                                class="no-blank">&#42;</font></td>
                        <td></td>
                    </tr>
                    <tr>
                        <td class="label-td"><emp:message key="ydyw_qyjfcx_khqygl_text_9" defVal="证件类型："
                                                          fileName="ydyw"></emp:message></td>
                        <td>
                            <select id="cardType" name="cardType" class="newSelect" isInput="false">
                                <option value=""><emp:message key="common_pleaseSelect" defVal="请选择"
                                                              fileName="common"></emp:message></option>
                                <%
                                    /*证件类型*/
                                    Map<String, String> cardType;
                                    if ("zh_HK".equals(empLangName)) {
                                        cardType = MobileBusStaticValue.cardType_zh_HK;
                                    } else if ("zh_TW".equals(empLangName)) {
                                        cardType = MobileBusStaticValue.cardType_zh_TW;
                                    } else {
                                        cardType = MobileBusStaticValue.cardType;
                                    }
                                    Iterator<String> its = cardType.keySet().iterator();
                                    while (its.hasNext()) {
                                        String key = its.next();
                                %>
                                <option value="<%=key %>" <%=identType.equals(key)?"selected":"" %> ><%=cardType.get(key) %>
                                </option>
                                <%
                                    }
                                %>
                            </select></td>
                        <td class="label-td"><emp:message key="ydyw_qyjfcx_khqygl_text_10" defVal="证件号码："
                                                          fileName="ydyw"></emp:message></td>
                        <td><input id="cardNo" name="cardNo" type="text" value="<%=escape(cardNo) %>" maxlength="20"/>
                        </td>
                        <td></td>
                    </tr>
                    <tr>
                        <td class="label-td"><emp:message key="ydyw_qyjfcx_khqygl_text_11" defVal="客户编号："
                                                          fileName="ydyw"></emp:message></td>
                        <td><input id="cliCode" name="cliCode" type="text" value="<%=escape(cliCode) %>"
                                   maxlength="20"/></td>
                        <td class="label-td"><emp:message key="ydyw_qyjfcx_khqygl_text_12" defVal="客户机构："
                                                          fileName="ydyw"></emp:message></td>
                        <td style="position: relative;z-index:999;">
                            <input type="hidden" name="depId" id="depId" value="">
                            <input id="cli-dep" style="width:174px!important;" class="treeInput" type="text" readonly=""
                                   value="<emp:message key="ydyw_qyjfcx_khqygl_text_20" defVal="点击选择机构" fileName="ydyw"></emp:message>"/>
                            <div id="dropMenu" class="div_bd"
                                 style="position: absolute;left:5px;top:30px; display:none;margin-left:0px;height:450px; width:356px; background-color:white;overflow-y:auto;overflow-x:auto;z-index:999;">
                                <div style="margin-top: 3px;margin-right:10px;text-align:right">
                                    <input type="button"
                                           value="<emp:message key="common_confirm" defVal="确定" fileName="common"></emp:message>"
                                           class="btnClass1 mr23" onclick="javascript:zTreeOnClickOK();" style=""/>
                                    <input type="button"
                                           value="<emp:message key="common_clean" defVal="清空" fileName="common"></emp:message>"
                                           class="btnClass1" onclick="javascript:cleanSelect();" style=""/>
                                </div>
                                <ul id="dropdownMenu" class="tree">
                                </ul>
                            </div>
                            <font class="no-blank" style="margin-left:-1px;position:relative;left:2px;">&#42;</font>
                        </td>
                        <td></td>
                    </tr>
                    <tr>
                        <td class="label-td"><emp:message key="ydyw_qyjfcx_khqygl_text_13" defVal="联系地址："
                                                          fileName="ydyw"></emp:message></td>
                        <td colspan="4"><input type="text" id="address" name="address" value="<%=escape(address) %>"
                                               maxlength="60"/></td>
                    </tr>
                    <tr>
                        <td class="caption"><emp:message key="ydyw_qyjfcx_khqygl_text_14" defVal="签约基本信息："
                                                         fileName="ydyw"></emp:message></td>
                        <td colspan="4" class="hr-wrap">
                            <div class="hr div_bd"></div>
                        </td>
                    </tr>
                    <tr>
                        <td class="label-td"><emp:message key="ydyw_ywgl_ywbgl_text_47" defVal="签约账号："
                                                          fileName="ydyw"></emp:message></td>
                        <td><input id="account" name="account" type="text" old="<%=account %>" value="<%=account %>"
                                   maxlength="20"/></td>
                        <td class="label-td"><emp:message key="ydyw_qyjfcx_khqygl_text_18" defVal="签约账号姓名："
                                                          fileName="ydyw"></emp:message></td>
                        <td><input id="accountName" name="accountName" type="text" value="<%=escape(accountName) %>"
                                   maxlength="20"/></td>
                        <td></td>
                    </tr>
                    <tr>
                        <td class="label-td"><emp:message key="ydyw_ywgl_ywbgl_text_41" defVal="扣费账号："
                                                          fileName="ydyw"></emp:message></td>
                        <td colspan="4"><label><input type="radio" id="feeType" name="feeType"
                                                      value="0"/>&nbsp;<emp:message key="ydyw_ywgl_ywbgl_text_46"
                                                                                    defVal="签约账号"
                                                                                    fileName="ydyw"></emp:message></label><label><input
                                type="radio" name="feeType" value="1" checked="checked"/>&nbsp;<emp:message
                                key="ydyw_qyjfcx_khqygl_text_31" defVal="其他帐号" fileName="ydyw"></emp:message></label>
                        </td>
                    </tr>
                    <tr>
                        <td class="label-td"><emp:message key="ydyw_ywgl_ywbgl_text_41" defVal="扣费账号："
                                                          fileName="ydyw"></emp:message></td>
                        <td><input id="feeAccount" name="feeAccount" type="text" value="<%=feeAccount %>"
                                   maxlength="20"/></td>
                        <td class="label-td"><emp:message key="ydyw_qyjfcx_khqygl_text_19" defVal="扣费账号姓名："
                                                          fileName="ydyw"></emp:message></td>
                        <td><input id="feeAccountName" name="feeAccountName" type="text"
                                   value="<%=escape(feeAccountName) %>" maxlength="20"/></td>
                        <td></td>
                    </tr>

                </table>

            </div>

            <div style="float:left;">
                <div class="div-text"><label><emp:message key="ydyw_ywgl_ywbgl_text_49" defVal="签约套餐："
                                                          fileName="ydyw"></emp:message></label></div>
                <div id="taocan" class="div_bd" style="display:inline-block;float:left;">
                    <div style="margin-bottom: 8px;" class="clearfix">
                        <div class="serach-box">
                            <a id="search" href="javascript:void(0);" style="margin-left:5px;height:24px;"></a>
                            <input id="tcname" type="text"/>
                            <select style="height:22px;width:80px;" id="serach-sel" isInput="false">
                                <option value=""><emp:message key="common_whole" defVal="全部"
                                                              fileName="common"></emp:message></option>
                                <%
                                    Iterator<String> items = taoCanType.keySet().iterator();
                                    while (items.hasNext()) {
                                        String keys = items.next();
                                        String values = taoCanType.get(keys);
                                        if ("VIP免费".equals(values)) {
                                            values = MessageUtils.extractMessage("ydyw", "ydyw_ywgl_tczlsz_text_5", request);
                                        } else if ("包月".equals(values)) {
                                            values = MessageUtils.extractMessage("ydyw", "ydyw_ywgl_tczlsz_text_by", request);
                                        } else if ("包季".equals(values)) {
                                            values = MessageUtils.extractMessage("ydyw", "ydyw_ywgl_tczlsz_text_bj", request);
                                        } else if ("包年".equals(values)) {
                                            values = MessageUtils.extractMessage("ydyw", "ydyw_ywgl_tczlsz_text_bn", request);
                                        }
                                        if (StringUtils.isBlank(keys) || "-1".equals(keys)) {
                                            continue;
                                        }
                                %>
                                <option value="<%=keys %>"><%= values %>
                                </option>
                                <%
                                    }
                                %>
                            </select>
                        </div>
                        <div style="float: right;width: 150px;"><emp:message key="ydyw_qyjfcx_khqygl_text_17"
                                                                             defVal="已选择套餐"
                                                                             fileName="ydyw"></emp:message><span
                                id="total" style="margin-left:4px;color:blue"></span></div>
                    </div>
                    <select id="l-select" multiple>
                    </select>
                    <select id="select-tc" name="select-tc" class="sel-mut" style="margin-right:5px;" multiple>
                    </select>
                    <input class="btnClass1" type="button"
                           value="<emp:message key="common_option" defVal="选择" fileName="common"></emp:message>"/>
                    <input class="btnClass1" type="button"
                           value="<emp:message key="common_delete" defVal="删除" fileName="common"></emp:message>"/>
                </div>
                <font class="no-blank" style="margin-left: 2px;font-size: 16px;color: red;">&#42;</font>

            </div>

            <div class="btn-submit">
                <input type="button"
                       value="<emp:message key="common_confirm" defVal="确定" fileName="common"></emp:message>"
                       class="btnClass5 mr23" onclick="javascript:doOk()"/>
                <input type="button"
                       value="<emp:message key="common_btn_16" defVal="取消" fileName="common"></emp:message>"
                       class="btnClass6" onclick="javascript:doSelectEClose()"/>
            </div>


        </form>
    </div>
</div>

<%-- 内容结束 --%>
<div class="clear"></div>
<script type="text/javascript"
        src="<%=commonPath%>/common/js/myjquery-b.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript"
        src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript"
        src="<%=commonPath%>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript"
        src="<%=commonPath %>/common/js/jquery.ztree-myjquery-b.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript"
        src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript"
        src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript"
        src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript"
        src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript"
        src="<%=commonPath%>/common/js/wdate_extend.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=iPath%>/js/util.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=iPath%>/js/json2.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=iPath%>/js/jquery.zsysel.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=iPath%>/js/crmsign.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=iPath%>/js/editcrm.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=empLangName%>/ydyw_<%=empLangName%>.js"></script>
<script>
    $(document).ready(function () {
        getLoginInfo('#loginUser');
        $('#cardType option[value="<%=identType%>"]').attr('selected', true);
        $(".newSelect").isSearchSelect({'width': '212', 'isInput': false, 'zindex': 0});
    });
</script>
</body>
</html>