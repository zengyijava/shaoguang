<%@ page language="java" import="com.montnets.emp.entity.biztype.LfBusManager" pageEncoding="UTF-8" %>
<%@page import="com.montnets.emp.entity.sysuser.LfSysuser" %>
<%@page import="com.montnets.emp.entity.template.LfTemplate" %>
<%@page import="com.montnets.emp.i18n.util.MessageUtils" %>
<%@page import="org.apache.commons.lang.StringEscapeUtils" %>
<%@page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    String iPath = request.getRequestURI().substring(0, request.getRequestURI().lastIndexOf("/"));
    String inheritPath = iPath.substring(0, iPath.lastIndexOf("/"));
    String commonPath = inheritPath.substring(0, inheritPath.lastIndexOf("/"));

    @SuppressWarnings("unchecked")
    Map<String, String> btnMap = (Map<String, String>) session.getAttribute("btnMap");//按钮权限Map

    //获取当前登录用户
    LfSysuser sysuser = (LfSysuser) (request.getSession(false) != null ? request.getSession(false).getAttribute("loginSysuser") : new LfSysuser());

    //业务类型
    List<LfBusManager> busList = (List<LfBusManager>) request.getAttribute("busList");

    //所有短信模板
    List<LfTemplate> tempList = (List<LfTemplate>) request.getAttribute("tempList");

    //已配置过的短信模板
    List<LfTemplate> tmpList = (List<LfTemplate>) request.getAttribute("tmpList");

    //业务ID
    String busId = (String) request.getAttribute("busId");

    //模板名称
    String tName = (String) request.getAttribute("tName");

    String type = (String) request.getAttribute("type");

    String yesOrNo = "";
    if (null != request.getAttribute("yesOrNo") &&
            !"".equals(request.getAttribute("yesOrNo"))) {
        yesOrNo = (String) request.getAttribute("yesOrNo");
    }

    @SuppressWarnings("unchecked")
    Map<String, String> titleMap = (Map<String, String>) session.getAttribute("titleMap");
    String menuCode = titleMap.get("busTempConf");
    menuCode = menuCode == null ? "0-0-0" : menuCode;

    //业务类型新建权限
    String btypeCode = titleMap.get("busType");
    //新建短信模板权限
    String tempCode = titleMap.get("smsTemplate");
    String skin = session.getAttribute("stlyeSkin") == null ? "default" : (String) session.getAttribute("stlyeSkin");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <%@include file="/common/common.jsp" %>
    <title>模板配置</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <link href="<%=commonPath %>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet"
          type="text/css"/>
    <link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
    <link href="<%=commonPath %>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet"
          type="text/css"/>
    <link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
    <link rel="stylesheet"
          href="<%=commonPath %>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>"/>
    <link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css">
    <%if (StaticValue.ZH_HK.equals(empLangName)) {%>
    <link rel="stylesheet" type="text/css"
          href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
    <link rel="stylesheet" type="text/css"
          href="<%=commonPath%>/ydyw/ywpz/css/cfg_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
    <%} else {%>
    <link rel="stylesheet" type="text/css"
          href="<%=commonPath%>/ydyw/ywpz/css/cfg.css?V=<%=StaticValue.getJspImpVersion()%>"/>
    <%}%>
    <style type="text/css">
        xmp {
            white-space: pre-wrap;
            word-break: break-all;
            word-wrap: break-word;
        }

        select#busId {
            margin-left: 30px;
        }
    </style>

</head>

<body>
<div id="container">
    <%
        String wz = "";
        if (type != null) {
            if (type.equals("1")) {
                wz = "<div class='top'><div id='top_right'><div id='top_left'></div><div id='top_main'>" + MessageUtils.extractMessage("ydyw", "ydyw_common_text_1", request) + "&nbsp;" +
                        MessageUtils.extractMessage("ydyw", "ydyw_common_text_2", request) + "&nbsp;&gt;&nbsp;" + MessageUtils.extractMessage("ydyw", "ydyw_common_text_3", request) + "&nbsp;&gt;&nbsp;" + MessageUtils.extractMessage("ydyw", "ydyw_common_text_4", request) + "&nbsp;&gt;&nbsp;" + MessageUtils.extractMessage("ydyw", "ydyw_common_text_5", request) + "</div></div></div>";
            } else {
                wz = "<div class='top'><div id='top_right'><div id='top_left'></div><div id='top_main'>" + MessageUtils.extractMessage("ydyw", "ydyw_common_text_1", request) + "&nbsp;" +
                        MessageUtils.extractMessage("ydyw", "ydyw_common_text_2", request) + "&nbsp;&gt;&nbsp;" + MessageUtils.extractMessage("ydyw", "ydyw_common_text_3", request) + "&nbsp;&gt;&nbsp;" + MessageUtils.extractMessage("ydyw", "ydyw_common_text_4", request) + "&nbsp;&gt;&nbsp;" + MessageUtils.extractMessage("ydyw", "ydyw_common_text_5", request) + "</div></div></div>";
            }
        }
    %><%=wz %>
    <div style="height: 20px;"></div>
    <div style="margin-left: 30px;">
        <input type="hidden" id="lguserid" value="<%=sysuser.getUserId() %>"/>
        <input type="hidden" id="lgcorpcode" value="<%=sysuser.getCorpCode() %>"/>
        <table style="height: 370px;font-size: 13px;">
            <tr>
                <td id="tdName">
                    <emp:message key="ydyw_ywgl_ywbgl_text_21" defVal="业务名称：" fileName="ydyw"></emp:message>
                </td>
                <td>
                    <select style="width: 221px;height:23px;overflow:auto;"
                            class="input_select" id="busId"
                            <% if (type.equals("2")) { %> disabled="disabled" <%} %>>
                        <option value="0"><emp:message key="common_pleaseSelect" defVal="请选择"
                                                       fileName="common"></emp:message></option>
                        <%
                            if (busList != null && busList.size() > 0) {
                                for (LfBusManager lb : busList) {
                        %>
                        <option value="<%=lb.getBusId() %>"
                                <%
                                    if (busId != null && !busId.equals("")) {
                                        if (Integer.valueOf(busId) == lb.getBusId().intValue()) {
                                %>
                                selected="selected"
                                <%
                                        }
                                    }
                                %> title="<%=lb.getBusName() %>（<%=lb.getBusCode() %>）"><%=lb.getBusName() %>
                            （<%=lb.getBusCode() %>）
                        </option>
                        <%
                                }
                            }
                        %>
                    </select>
                    <font style="color: red;">*</font>
                    <%
                        if (btnMap.get(btypeCode + "-1") != null) {
                    %>
                    &nbsp;<a href="javascript:addBus();"><emp:message key="ydyw_ywgl_ywmbpz_text_4" defVal="新建业务"
                                                                      fileName="ydyw"></emp:message></a>
                    <%
                        }
                    %>
                </td>
            </tr>
            <tr>
                <td style="vertical-align: top;padding-top:15px;">
                    <span><emp:message key="ydyw_ywgl_ywmbpz_text_58" defVal="模板配置："
                                       fileName="ydyw"></emp:message></span>
                </td>
                <td colspan="2" style="padding-top:15px;">
                    <div class="div_bd"
                         style="height: 400px;width:616px;float: left;margin-left: 30px;margin-top: -0px;background-color: #F1F1F9;">
                        <div style="margin: 10px 0px 2px 0px;">
                            <table style="font-size: 13px;margin-left: 16px;">
                                <tr>
                                    <td colspan="2" style="margin-top: 0px;">
                                        <%
                                            if (null == tName) {
                                                tName = "";
                                            }
                                        %>
                                        <input class="input_bd div_bd wth2" onkeyup="javascript:nobfh();" id="tName"
                                               style="float:left;width: 177px;margin-right: -3px;"
                                               title="<emp:message key="ydyw_qyjfcx_khjfcx_text_4" defVal="请输入模板名称" fileName="ydyw"></emp:message>"
                                               value="<%=tName %>" maxlength="20"/>
                                        <a class="searIcon">
                                            <img id="searchIcon" src="<%=skin %>/images/query.png" border="0"/>
                                        </a>
                                    </td>
                                    <%
                                        if (btnMap.get(tempCode + "-1") != null) {
                                    %>
                                    <td>&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:addTemp();"><emp:message
                                            key="ydyw_ywgl_ywmbpz_text_59" defVal="新建模板"
                                            fileName="ydyw"></emp:message></a></td>
                                    <%
                                        }
                                    %>
                                </tr>
                            </table>
                        </div>

                        <div id="hrLine" class="div_bd" style="border-top: 1px;"></div>

                        <table style="font-size: 13px;margin-left: 16px;">
                            <tr>
                                <td>
                                    <span style="font-size: 13px;margin-left: 11px;"><input type="checkbox" id="allCk"
                                                                                            value=""/>
                                        <emp:message
                                                key="ydyw_qyjfcx_khjfcx_text_2" defVal="全选"
                                                fileName="ydyw"></emp:message></span>
                                    <br/>
                                    <div class="div_bd"
                                         style="height: 303px; width: 255px;overflow:auto;float: left;margin-right: 10px;margin-top: 5px;background-color: white;">
                                        <table id="" style="width:200px;font-size: 13px;margin: 5px 0px 0px 10px;"
                                               align="left">
                                            <%
                                                int count = 0;
                                                if (tempList != null && tempList.size() > 0) {
                                                    for (LfTemplate lt : tempList) {
                                                        if (null == lt.getTmid()) {
                                                            continue;
                                                        }
                                                        count++;
                                                        String tname = lt.getTmName() == null ? "" : lt.getTmName();
                                                        if (tname.length() > 5) {
                                                            tname = tname.substring(0, 5) + "...";
                                                        }
                                            %>
                                            <tr id="tr_<%=lt.getTmid() %>">
                                                <td style="width: 100px;">
                                                    <input type="checkbox" id="ck_<%=lt.getTmid() %>" name="cks"
                                                           value="<%=lt.getTmid() %>"/>
                                                    <a href="javascript:void(0);" title="<%=tname %>"
                                                       id="aCount<%=count %>"
                                                       style="color: black;"><%=tname %></a>
                                                    <input type="hidden" id="hid_<%=lt.getTmid() %>"
                                                           value="<%=tname %>"/>
                                                    <%
                                                        String hidMsg = lt.getTmMsg();
                                                        hidMsg = hidMsg.replace("<", "&lt;");
                                                        hidMsg = hidMsg.replace(">", "&gt;");
                                                        hidMsg = hidMsg.replace("<=", "&le;");
                                                        hidMsg = hidMsg.replace(">=", "&ge;");
                                                    %>
                                                    <input type="hidden" id="msg_<%=lt.getTmid() %>"
                                                           value="<%=hidMsg %>"/>
                                                </td>
                                                <td>&nbsp;</td>
                                                <td>
                                                    <%
                                                        String tpMsg = lt.getTmMsg().replaceAll("#[pP]_([1-9][0-9]*)#", "{#" + MessageUtils.extractMessage("ydyw", "ydyw_ywgl_ywmbpz_text_44", request) + "$1#}");
                                                        String msg = tpMsg;

                                                        if (msg.length() > 5) {
                                                            msg = msg.substring(0, 5) + "...";
                                                        }
                                                    %>
                                                    <a href="javascript:showMsg(<%=lt.getTmid() %>);" align="right"
                                                       name="left_a"
                                                       title="<emp:message key="ydyw_qyjfcx_khjfcx_text_3" defVal="点击查看" fileName="ydyw"></emp:message>">
                                                        <xmp align="left"><%=StringEscapeUtils.unescapeHtml(msg.trim()) %></xmp>
                                                    </a>
                                                    <label id="la_<%=lt.getTmid() %>" style="display: none;">
                                                        <xmp><%=StringEscapeUtils.unescapeHtml(tpMsg.trim()) %>
                                                        </xmp>
                                                    </label>
                                                </td>
                                            </tr>
                                            <%
                                                }
                                            } else {%>
                                            <tr align="center">
                                                <td><emp:message key="common_norecord" defVal="无记录！"
                                                                 fileName="common"></emp:message></td>
                                            </tr>
                                            <%
                                                }
                                            %>
                                        </table>
                                    </div>
                                </td>

                                <td>
                                    <input class="btnClass1" type="button" onclick="javascript:router();"
                                           value="<emp:message key="common_option" defVal="选择" fileName="common"></emp:message>"/>
                                    <br/>
                                    <div style="height: 10px;"></div>
                                    <input class="btnClass1" type="button" onclick="javascript:moveRight();"
                                           value="<emp:message key="common_delete" defVal="删除" fileName="common"></emp:message>"/>
                                </td>

                                <td>
                                    <span style="margin-left: 21px;float: left;"><input type="checkbox" id="rallCk"
                                                                                        value=""/><emp:message
                                            key="ydyw_qyjfcx_khjfcx_text_2" defVal="全选"
                                            fileName="ydyw"></emp:message></span>
                                    <span style="margin-top: 2px;float: right;margin-right: 3px;"><emp:message
                                            key="ydyw_ywgl_ywmbpz_text_5" defVal="已选择模板数："
                                            fileName="ydyw"></emp:message>
					    					<%
                                                int manCount = 0;

                                                if (tmpList != null && tmpList.size() > 0) {
                                                    manCount = tmpList.size();
                                                }
                                            %>
					    					<label id="manCount"><%=manCount %></label>
					    				</span>
                                    <br/>
                                    <div id="rightDiv" class="div_bd"
                                         style="height: 303px; width: 255px;overflow:auto;float: left;margin-left: 10px;margin-top: 5px;background-color: white;">
                                        <table id="rightTb"
                                               style="width:200px;font-size: 13px;margin: 5px 0px 0px 10px;"
                                               align="left">
                                            <%
                                                if (tmpList != null && tmpList.size() > 0) {
                                                    for (LfTemplate temp : tmpList) {
                                            %>
                                            <tr id="rtr_<%=temp.getTmid() %>">
                                                <td style="width: 100px;">
                                                    <input type="checkbox" id="rck_<%=temp.getTmid() %>" name="rcks"
                                                           value="<%=temp.getTmid() %>"/>
                                                    <%
                                                        String tname = temp.getTmName();
                                                        if (tname.length() > 5) {
                                                            tname = tname.substring(0, 5) + "...";
                                                        }
                                                    %>
                                                    <a href="javascript:void(0);" title="<%=temp.getTmName() %>"
                                                       style="color: black;"><%=tname %></a>
                                                    <input type="hidden" id="rhid_<%=temp.getTmid() %>"
                                                           value="<%=temp.getTmName() %>"/>
                                                </td>
                                                <td>&nbsp;</td>
                                                <td>
                                                    <%
                                                        String msge = temp.getTmMsg().replaceAll("#[pP]_([1-9][0-9]*)#", "{#" + MessageUtils.extractMessage("ydyw", "ydyw_ywgl_ywmbpz_text_44", request) + "$1#}");

                                                        if (msge.length() > 5) {
                                                            msge = msge.substring(0, 5) + "...";
                                                        }
                                                    %>
                                                    <a href="javascript:showMsg(<%=temp.getTmid() %>);" align="left"
                                                       name="left_a"
                                                       title="<emp:message key="ydyw_ywgl_ywmbpz_text_45" defVal="点击查看" fileName="ydyw"></emp:message>">
                                                        <xmp align="left"><%=msge %></xmp>
                                                    </a>
                                                </td>
                                            </tr>
                                            <%
                                                    }
                                                }
                                            %>
                                        </table>

                                        <ul id="getChooseMan" style="display: none;">
                                            <%
                                                if (tmpList != null && tmpList.size() > 0) {
                                            %>
                                            $('#busId').attr("disabled", "disabled")
                                            <%
                                                for (LfTemplate ltemp : tmpList) {
                                            %>
                                            <li id='rli_<%=ltemp.getTmid() %>'
                                                dataval='<%=ltemp.getTmid() %>'><%=ltemp.getTmName() %>
                                            </li>
                                            <%
                                                    }
                                                }
                                            %>
                                        </ul>
                                    </div>
                                </td>
                                <td><font color="red" style="margin-right: 5px;">*</font></td>
                            </tr>
                        </table>
                    </div>
                </td>
            </tr>

            <tfoot>
            <tr height="65px;">
                <td></td>
                <td colspan="2">
                    <center>
                        <input class="btnClass5 mr23" type="button" id="btnSub" onclick="javascript:saveTail();"
                               value="<emp:message key="common_btn_8" defVal="保存" fileName="common"></emp:message>"/>
                        &nbsp;
                        <input class="btnClass6" type="button" id="back"
                               value="<emp:message key="common_btn_10" defVal="返回" fileName="common"></emp:message>"/>
                    </center>
                </td>
            </tr>
            </tfoot>
        </table>
    </div>

    <%-- 模板内容 --%>
    <div id="tempMsg" style="margin: 5px 0px 5px 5px;font-size: 13px;overflow: hidden;" align="left">
        <div id="tMsg"
             style="width: 390px;word-break:break-all;word-wrap: break-word;overflow: auto;margin: 6px 0px 4px 10px;">
            <xmp></xmp>
        </div>
    </div>

    <%-- 新建业务 --%>
    <div id="addDiv" style="padding:5px;display:none">
        <table style="width:100%;height:95%;font-size: 13px;margin-top: 12px;">
            <tr>
                <td>
                    <emp:message key="ydyw_ywgl_ywbgl_text_21" defVal="业务名称：" fileName="ydyw"></emp:message>
                </td>
                <td>
                    <input type="text" class="input_bd" name="busNameAdd" id="busNameAdd" maxlength="16"/>
                    <font style="color: red;">*</font>
                </td>
            </tr>
            <tr>
                <td height="3px"></td>
            </tr>
            <tr>
                <td>
                    <emp:message key="ydyw_ywgl_ywbgl_text_22" defVal="业务编码：" fileName="ydyw"></emp:message>
                </td>
                <td>
                    <input type="text" class="input_bd" name="busCodeAdd" id="busCodeAdd" maxlength="16"/>
                    <font style="color: red;">*</font>
                </td>
            </tr>
            <tr>
                <td height="3px"></td>
            </tr>
            <tr>
                <td><emp:message key="ydyw_ywgl_ywmbpz_text_6" defVal="业务类型：" fileName="ydyw"></emp:message></td>
                <td>
                    <select name="busTypeAdd" id="busTypeAdd" class="input_bd">
                        <option selected="selected" value=""><emp:message key="common_pleaseSelect" defVal="请选择"
                                                                          fileName="common"></emp:message></option>
                        <option value="0"><emp:message key="ydyw_ywgl_ywmbpz_text_12" defVal="手动"
                                                       fileName="ydyw"></emp:message></option>
                        <option value="1"><emp:message key="ydyw_ywgl_ywmbpz_text_13" defVal="触发"
                                                       fileName="ydyw"></emp:message></option>
                        <option value="2"><emp:message key="ydyw_ywgl_ywmbpz_text_12" defVal="手动"
                                                       fileName="ydyw"></emp:message>+<emp:message
                                key="ydyw_ywgl_ywmbpz_text_13" defVal="触发" fileName="ydyw"></emp:message></option>
                    </select>
                    <font style="color: red;">*</font>
                </td>
            </tr>
            <tr>
                <td height="3px"></td>
            </tr>
            <tr>
                <td>
                    <emp:message key="ydyw_ywgl_ywmbpz_text_7" defVal="优先级别：" fileName="ydyw"></emp:message>
                </td>
                <td>
                    <select name="riseLevelAdd" id="riseLevelAdd" class="input_bd">
                        <option value="-99" selected="selected"><emp:message key="ydyw_ywgl_ywmbpz_text_9" defVal="无优先级"
                                                                             fileName="ydyw"></emp:message></option>
                        <option value="1">1 &nbsp;&nbsp;<emp:message key="ydyw_ywgl_ywmbpz_text_10" defVal="（最高优先级）"
                                                                     fileName="ydyw"></emp:message></option>
                        <option value="2">2</option>
                        <option value="3">3</option>
                        <option value="4">4</option>
                        <option value="5">5</option>
                        <option value="6">6</option>
                        <option value="7">7</option>
                        <option value="8">8</option>
                        <option value="9">9&nbsp;&nbsp;<emp:message key="ydyw_ywgl_ywmbpz_text_11" defVal="（最低优先级）"
                                                                    fileName="ydyw"></emp:message></option>
                    </select>
                    <font style="color: red;">*</font>
                </td>
            </tr>
            <tr>
                <td height="3px"></td>
            </tr>
            <tr>
                <td>
                    <emp:message key="ydyw_ywgl_ywmbpz_text_8" defVal="业务描述：" fileName="ydyw"></emp:message>
                </td>
                <td>
                    <textarea class="input_bd"
                              title="<emp:message key="ydyw_qyjfcx_khjfcx_text_5" defVal="最大长度" fileName="ydyw"></emp:message>&nbsp;&nbsp;200"
                              name="busDescriptionAdd" id="busDescriptionAdd"></textarea>
                </td>
            </tr>
            <tr height="60px;">
                <td colspan="2" style="text-align:center;">
                    <input class="btnClass5 mr23" type="button"
                           value="<emp:message key="common_confirm" defVal="确定" fileName="common"></emp:message>"
                           onclick="javascript:addBusType();">
                    <input class="btnClass6" type="button"
                           value="<emp:message key="common_btn_16" defVal="取消" fileName="common"></emp:message>"
                           onclick="javascript:doCancel();"/>
                    <%-- 为了兼容ie8下取消按钮右下角旁边能点击到确定按钮，增加这个换行 --%>
                    <br/>
                </td>
            </tr>
        </table>
    </div>

    <%-- 新建短信模板 --%>
    <div id="addSmsTmpDiv1"
         title="<emp:message key="ydyw_ywgl_ywmbpz_text_15" defVal="新建短信模板" fileName="ydyw"></emp:message>"
         style="padding:5px;display:none;height:530px;">
        <iframe id="addSmsTmpFrame" name="addSmsTmpFrame" style="display:none;width:610px;height:480px;border: 0;"
                marginwidth="0" scrolling="no" frameborder="no"></iframe>
    </div>

</div>
<script language="javascript" src="<%=commonPath %>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"
        type="text/javascript"></script>
<script type="text/javascript"
        src="<%=commonPath %>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript"
        src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<%-- 为了解决ie8空白双击导致浏览器崩溃，增加日期控件的引用，可解决这问题 --%>
<script type="text/javascript"
        src="<%=commonPath %>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript"
        src="<%=commonPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=iPath%>/js/ydyw_cfg.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript"
        src="<%=commonPath %>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script src="<%=commonPath%>/common/js/floating.js?V=<%=StaticValue.getJspImpVersion() %>"
        type="text/javascript"></script>
<script src="<%=commonPath%>/common/js/tipcontent.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script src="<%=commonPath %>/common/js/jquery.selection.js?V=116.1" type="text/javascript"></script>
<script src="<%=commonPath %>/common/js/param_cg2.js?V=116.1" type="text/javascript"></script>
<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=empLangName%>/ydyw_<%=empLangName%>.js"></script>
<script type="text/javascript">
    $(document).ready(function () {
        noquot($("#busNameAdd"));
        noquot($("#tName"));

        //判断是否是IE浏览器： -1、非IE浏览器
        var isIE = navigator.userAgent.toUpperCase().indexOf("MSIE") == -1 ? false : true;
        if (!isIE) {
            $('#tdName').attr("width", "95px");
            $('#hrLine').css("margin", "10px 0px 10px 0px");
            $('#tMsg').css("height", "280px");
            $("#addDiv").dialog({
                autoOpen: false,
                width:<%=StaticValue.ZH_HK.equals(empLangName)?500:460%>,
                height: 300,
                title: getJsLocaleMessage("ydyw", "ydyw_ywgl_ywmbpz_text_14"),
                modal: true,
                resizable: false
            });
        } else {
            $('#tMsg').css("height", "300px");
            $('#hrLine').css("margin", "6px 0px 7px 0px");
            $('#tdName').attr("width", "52px;");
            $('#aCount1').css("margin-left", "-3px");

            $("#addDiv").dialog({
                autoOpen: false,
                width:<%=StaticValue.ZH_HK.equals(empLangName)?500:460%>,
                title: getJsLocaleMessage("ydyw", "ydyw_ywgl_ywmbpz_text_14"),
                modal: true,
                resizable: false
            });
        }

        <%
        //if (yesOrNo.equals("yes")) {
        //	request.removeAttribute("yesOrNo");
        %>
        //alert("搜索无记录！");
        <%
        //}
        %>

        $("#addSmsTmpDiv1").dialog({
            autoOpen: false,
            width: 410,
            height: 620,
            title: getJsLocaleMessage("ydyw", "ydyw_ywgl_ywmbpz_text_15"),
            modal: true,
            resizable: false
        });

        $("#tempMsg").dialog({
            autoOpen: false,
            width: 420,
            height: 330,
            title: getJsLocaleMessage("ydyw", "ydyw_ywgl_ywmbpz_text_16"),
            modal: true,
            resizable: false
        });

        $('#back').click(function () {

            var url = "<%=path %>/ydyw_busTempConf.htm?method=find";
            //$.post(url,{method:'toConfig'},function() {});
            location.href = url;
        });

        $('#searchIcon').click(function () {
            searchTmp();
        });

        //左边全选
        $('#allCk').click(function () {
            if ($('#allCk').is(':checked')) {
                $('[name=cks]:checkbox').attr("checked", true);
            } else {
                $('[name=cks]:checkbox').attr("checked", false);
            }
        });

        //左边去掉全选勾
        $('[name=cks]:checkbox').click(function () {
            $('[name=cks]:checkbox').each(function () {
                if (!this.checked) {
                    $('#allCk').attr("checked", false);
                }
            });
        });


        //右边全选
        $('#rallCk').click(function () {
            if ($('#rallCk').is(':checked')) {
                $('[name=rcks]:checkbox').attr("checked", true);
            } else {
                $('[name=rcks]:checkbox').attr("checked", false);
            }
        });

        //右边去掉全选勾
        $('[name=rcks]:checkbox').click(function () {
            $('[name=rcks]:checkbox').each(function () {
                if (!this.checked) {
                    $('#rallCk').attr("checked", false);
                }
            });
        });
    });

    //新建业务类型
    function addBusType() {
        var url = "<%=path %>/ydyw_busTempConf.htm";
        var lgcorpcode = $('#lgcorpcode').val();
        var lguserid = $('#lguserid').val();
        var busName = $('#busNameAdd').val();
        var busCode = $('#busCodeAdd').val();
        var busType = $('#busTypeAdd').val();
        var riseLevel = $('#riseLevelAdd').val();
        var busDescription = $.trim($('#busDescriptionAdd').val());

        if ($.trim(busName) == "") {
            alert(getJsLocaleMessage("ydyw", "ydyw_ywgl_ywmbpz_text_17"));
            return;
        }

        if ($.trim(busCode) == "") {
            alert(getJsLocaleMessage("ydyw", "ydyw_ywgl_ywmbpz_text_18"));
            return;
        }

        var pattern = /^[A-z0-9]+$/;
        busCode = busCode.replace(/\\/g, '替换');
        if (!pattern.test(busCode)) {
            alert(getJsLocaleMessage("ydyw", "ydyw_ywgl_ywmbpz_text_19"));
            return;
        }

        if (busType == "") {
            alert(getJsLocaleMessage("ydyw", "ydyw_ywgl_ywmbpz_text_20"));
            return;
        }

        var desLen = busDescription.length;
        if (desLen > 200) {
            var setCount = busDescription.length - 200;
            alert(getJsLocaleMessage("ydyw", "ydyw_ywgl_ywmbpz_text_21") + setCount + getJsLocaleMessage("ydyw", "ydyw_ywgl_ywmbpz_text_22"));
            return;
        }

        $.post(url, {
                method: 'addBus', busName: busName, busCode: busCode,
                lgcorpcode: lgcorpcode, lguserid: lguserid,
                busType: busType, riseLevel: riseLevel, busDescription: busDescription
            },
            function (result) {
                if (result == "true") {
                    alert(getJsLocaleMessage("ydyw", "ydyw_ywgl_ywmbpz_text_23"));
                    $("#addDiv").dialog("close");
                    window.location.reload();
                } else if (result == "nameExists") {
                    alert(getJsLocaleMessage("ydyw", "ydyw_ywgl_ywmbpz_text_24"));
                } else if (result == "codeExists") {
                    alert(getJsLocaleMessage("ydyw", "ydyw_ywgl_ywmbpz_text_25"));
                } else {
                    alert(getJsLocaleMessage("ydyw", "ydyw_ywgl_ywmbpz_text_26"));
                }
            });
    }

    //新建短信模板
    function addTemp() {
        $("#addSmsTmpDiv1").css("display", "block");
        $("#addSmsTmpFrame").css("display", "block");
        $("#addSmsTmpDiv1").dialog({
            autoOpen: false,
            height: 520,
            width: 620,
            resizable: false,
            modal: true,
            position: ['center', 20],
            open: function () {
                $("#addSmsTmpDiv1").css("height", "530px");
            },
            close: function () {
                $("#addSmsTmpDiv1").css("display", "none");
                $("#addSmsTmpFrame").css("display", "none");
            }
        });

        //模板id
        var tmIds = "";
        var type = "<%=type %>";
        var showType = "2";
        //业务id
        var busId = $('#busId').val();
        //模板名称
        var tName = $('#tName').val();
        tName = encodeURIComponent($('#tName').val());

        if (type == "2") {
            showType = "3";

            $("#getChooseMan li").each(function () {
                var dataval = $(this).attr("dataval");
                if (dataval != null && dataval != "") {
                    tmIds = tmIds + dataval + ",";
                }
            });
        }

        //新增
        $("#addSmsTmpFrame").attr("src", "tem_smsTemplate.htm?method=doAdd&fromState=3&lguserid=<%=sysuser.getUserId()%>"
            + "&lgcorpcode=<%=sysuser.getCorpCode()%>&showType=2&time=" + new Date().getTime());
        $("#addSmsTmpDiv1").dialog("open");
    }

    //新建业务模板配置
    function saveTail() {
        //业务id
        var busId = $('#busId').val();
        if (busId == "0") {
            alert(getJsLocaleMessage("ydyw", "ydyw_ywgl_ywmbpz_text_27"));
            return;
        }

        //模板id
        var tmIds = "";
        $("#getChooseMan li").each(function () {
            var dataval = $(this).attr("dataval");
            if (dataval != null && dataval != "") {
                tmIds = tmIds + dataval + ",";
            }
        });

        if (tmIds == "") {
            alert(getJsLocaleMessage("ydyw", "ydyw_ywgl_ywmbpz_text_28"));
            return;
        }

        var type = "<%=type %>";
        var url = "<%=path %>/ydyw_busTempConf.htm?method=update";

        $.post(url, {type: type, busId: busId, tmIds: tmIds},
            function (result) {
                if (result == "true") {
                    alert(getJsLocaleMessage("ydyw", "ydyw_ywgl_ywmbpz_text_29"));

                    var url = "<%=path %>/ydyw_busTempConf.htm?method=find";
                    location.href = url;
                    //window.location.reload();
                } else {
                    alert(getJsLocaleMessage("ydyw", "ydyw_ywgl_ywmbpz_text_30"));
                }
            }
        );
    }

    function searchTmp() {
        var busId = $('#busId').val();
        var tName = $.trim($('#tName').val());

        tName = encodeURIComponent(encodeURIComponent($('#tName').val()));

        //模板id
        var tmIds = "";
        $("#getChooseMan li").each(function () {
            var dataval = $(this).attr("dataval");
            if (dataval != null && dataval != "") {
                tmIds = tmIds + dataval + ",";
            }
        });

        var urlr = "<%=path %>/ydyw_busTempConf.htm?method=getTempByName";
        urlr = urlr + "&busId=" + busId + "&tName=" + tName + "&type=<%=type %>&tmIds=" + tmIds;
        window.location.href = urlr;
    }

    function showMsg(tmid) {
        var msgid = "msg_" + tmid
        var msg = $('#' + msgid).val();
        var laid = $('#la_' + tmid).children("xmp").text();

        //$("#tMsg").children("xmp").empty();

        //$("#tMsg").html("<xmp nowrap='nowrap' style='white-space:nowrap;overflow:hidden;word-break:keep-all;'>"+laid+"</xmp>");
        $("#tMsg").children("xmp").text(laid);
        $('#tempMsg').dialog("open");
    }

    function closeAddSmsTmpdiv() {
        $("#addSmsTmpDiv1").dialog("close");
        $("#addSmsTmpFrame").attr("src", "");
    }

    function closeSmsTmp() {
        $("#addSmsTmpDiv1").dialog("close");
        $("#addSmsTmpFrame").attr("src", "");
        window.location.reload();
    }

    function nobfh() {
        var tName = $('#tName').val();
        tName = tName.replaceAll("%", "");
        $('#tName').val(tName);
    }
</script>
</body>
</html>
