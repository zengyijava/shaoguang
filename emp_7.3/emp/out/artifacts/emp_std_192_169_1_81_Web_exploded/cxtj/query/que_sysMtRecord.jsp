<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="com.montnets.emp.util.PageInfo" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Date" %>
<%@page import="com.montnets.emp.common.constant.StaticValue" %>
<%@page import="com.montnets.emp.common.constant.CommonVariables" %>
<%@page import="org.apache.commons.beanutils.DynaBean" %>
<%@page import="java.util.LinkedHashMap" %>
<%@page import="com.montnets.emp.i18n.util.MessageUtils" %>
<%@ page import="com.montnets.emp.entity.dxzs.LfDfadvanced" %>
<%@ page import="com.montnets.emp.entity.pasgroup.Userdata" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple" %>
<%@include file="/common/common.jsp" %>
<%
    String path = request.getContextPath();
    String iPath = request.getRequestURI().substring(0, request.getRequestURI().lastIndexOf("/"));
    String inheritPath = iPath.substring(0, iPath.lastIndexOf("/"));
    String commonPath = inheritPath.substring(0, inheritPath.lastIndexOf("/"));
    String skin = session.getAttribute("stlyeSkin") == null ? "default" : (String) session.getAttribute("stlyeSkin");
    String langName = (String) session.getAttribute(StaticValue.LANG_KEY);
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    //按钮权限Map
    Map<String, String> btnMap = (Map<String, String>) session.getAttribute("btnMap");
    if (btnMap == null) {
        btnMap = new HashMap<String, String>();
    }
    Map<String, String> titleMap = (Map<String, String>) session.getAttribute("titleMap");
    if (titleMap == null) {
        titleMap = new HashMap<String, String>();
    }
    String menuCode = titleMap.get("sysMtRecord");
    menuCode = menuCode == null ? "0-0-0" : menuCode;

    PageInfo pageInfo = (PageInfo) request.getAttribute("pageInfo");
    if (pageInfo == null) {
        pageInfo = new PageInfo();
    }

    boolean isFirstEnter = (Boolean) request.getAttribute("isFirstEnter");

    //发送账号
    List<String> userList = (List<String>) request.getAttribute("mrUserList");
    //通道号码
    List<DynaBean> spList = (List<DynaBean>) request.getAttribute("spList");
    Map<String, String> busMap = (Map<String, String>) request.getAttribute("busMap");
    if (busMap == null) {
        busMap = new HashMap<String, String>();
    }

    //结果集合
    List<DynaBean> mtTaskList = (List<DynaBean>) request.getAttribute("sysMtTaskList");
    List<String> ptmsgidList = (List<String>) request.getAttribute("ptmsgidList");
    // 查询条件
    LinkedHashMap<String, String> conditionMap = (LinkedHashMap<String, String>) session.getAttribute("sysMtTaskCon");
    if (conditionMap == null) {
        conditionMap = new LinkedHashMap<String, String>();
    }

    String recordType = conditionMap.get("recordType");
    if (recordType == null) {
        recordType = "realTime";
    }
    String userid = conditionMap.get("userid");
    String phone = conditionMap.get("phone");
    String spgate = conditionMap.get("spgate");
    String startTime = conditionMap.get("sendtime");
    String endTime = conditionMap.get("recvtime");
    String buscode = conditionMap.get("buscode");
    String spisuncm = conditionMap.get("spisuncm");
    String usercode = conditionMap.get("p1");
    String taskid = conditionMap.get("taskid");
    String mterrorcode = conditionMap.get("mterrorcode");
    String usermsgid = conditionMap.get("usermsgid");

    //查询分页信息url
    String loadPageUrl = path + "/que_sysMtRecord.htm?method=getMtPageInfo";

    String findResult = (String) request.getAttribute("findresult");

    //错误码说明map，key为错误码，value为说明
    Map<String, String> errCodeDesMap = (Map<String, String>) request.getAttribute("errCodeDesMap");
    if (errCodeDesMap == null) {
        errCodeDesMap = new HashMap<String, String>();
    }

    List<Userdata> spUserList = (List<Userdata>) request.getAttribute("sendUserList");
    //高级默认设置
    LfDfadvanced lfDfadvanced = (LfDfadvanced) request.getAttribute("lfDfadvanced");

    MessageUtils messageUtils = new MessageUtils();
    //通道号码
    String tdhm = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_xtsxjl_tdhm", request);
    if (tdhm != null && tdhm.length() > 1) {
        tdhm = tdhm.substring(0, tdhm.length() - 1);
    }
    //运营商
    String yys = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_xtsxjl_yys", request);
    if (yys != null && yys.length() > 1) {
        yys = yys.substring(0, yys.length() - 1);
    }
    //手机号码
    String sjhm = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_xtsxjl_sjhm", request);
    if (sjhm != null && sjhm.length() > 1) {
        sjhm = sjhm.substring(0, sjhm.length() - 1);
    }
    //任务批次
    String rwpc = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_xtxxjl_rwpc", request);
    if (rwpc != null && rwpc.length() > 1) {
        rwpc = rwpc.substring(0, rwpc.length() - 1);
    }
    //状态报告
    String ztbg = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_xtxxjl_ztbg", request);
    //发送时间
    String fssj = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_xtxxjl_fssj", request);
    if (fssj != null && fssj.length() > 1) {
        fssj = fssj.substring(0, fssj.length() - 1);
    }
    //接收时间
    String jssj = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_xtsxjl_jssj", request);
    if (jssj != null && jssj.length() > 1) {
        jssj = jssj.substring(0, jssj.length() - 1);
    }
    //业务类型
    String ywlx = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_xtxxjl_ywlx", request);
    if (ywlx != null && ywlx.length() > 1) {
        ywlx = ywlx.substring(0, ywlx.length() - 1);
    }
    //分条
    String ft = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_xtxxjl_ft", request);
    //短信内容
    String dxnr = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_xtsxjl_dxnr", request);
    if (dxnr != null && dxnr.length() > 1) {
        dxnr = dxnr.substring(0, dxnr.length() - 1);
    }
    //自定义流水号
    String zdylsh = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_xtxxjl_zdylsh", request);
    if (zdylsh != null && zdylsh.length() > 1) {
        zdylsh = zdylsh.substring(0, zdylsh.length() - 1);
    }
    //编码
    String bm = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_xtsxjl_bm", request);
    //移动
    String yd = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_xtsxjl_yd", request);
    //联通
    String lt = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_xtsxjl_lt", request);
    //电信
    String dx = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_xtsxjl_dx", request);
    //国外
    String gw = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_xtsxjl_gw", request);
    //成功
    String cg = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_xtxxjl_cg", request);
    //失败
    String sb = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_xtxxjl_sb", request);
    //错误说明
    String cwsm = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_xtxxjl_cwsm", request);
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
<head>
    <title><%=titleMap.get(menuCode) %>
    </title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet"
          type="text/css"/>
    <link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet"
          type="text/css"/>
    <link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
    <link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
    <%if (StaticValue.ZH_HK.equals(langName)) {%>
    <link rel="stylesheet" type="text/css"
          href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
    <link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
    <%}%>
    <link rel="stylesheet"
          href="<%=commonPath %>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>">
    <link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css">
    <link href="<%=commonPath%>/common/css/reading.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet"
          type="text/css"/>
    <link href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet"
          type="text/css"/>
    <link href="<%=commonPath%>/cxtj/common/css/inbox.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet"
          type="text/css"/>
    <link href="<%=iPath %>/css/que_sysMtRecord.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet"
          type="text/css"/>
</head>
<body class="que_sysMtRecord">
<div id="container" class="container">

    <input type="hidden" id="spUserId" value=""/>
    <%-- 当前位置 --%>
    <%=com.montnets.emp.common.constant.ViewParams.getPosition(langName, menuCode) %>

    <%-- 内容开始 --%>
    <%if (btnMap.get(menuCode + "-0") != null) { %>
    <div id="rContent" class="rContent">
        <div id="errCodeDesDiv" style="display:none;">
            <%
                int i_err = 0;
                for (String key : errCodeDesMap.keySet()) {
            %>
            <input type="hidden" id="hdErrCode<%=i_err %>" value="<%=key + "---" + errCodeDesMap.get(key) %>"/>
            <%
                    i_err++;
                }
            %>
        </div>

        <form name="pageForm" action="que_sysMtRecord.htm" method="post" id="pageForm">
            <div class="buttons">
                <div id="toggleDiv">
                </div>
                		<%
						   if(btnMap.get(menuCode+"-1")!=null)
						   {
						%>
                <input id="reSend" onclick="toReSends()" type="button"
                       value="<emp:message key='cxtj_sjfx_btn1' defVal='批量补发' fileName='cxtj'/>"
                       class="btnClass4 mr23"/>
                       <%
                           }
                       %>
            </div>
            <div id="condition">
                <table>
                    <tbody>
                    <tr>
                        <td>
                            <emp:message key="cxtj_sjcx_xtsxjl_jllx" defVal="记录类型" fileName="cxtj"></emp:message>
                        </td>
                        <td>
                            <select name="recordType" id="recordType" isInput="false">
                                <option value="realTime"><emp:message key="cxtj_sjcx_xtsxjl_ssjl" defVal="实时记录"
                                                                      fileName="cxtj"></emp:message></option>
                                <option value="history" <%="history".equals(recordType) ? "selected" : "" %>>
                                    <emp:message key="cxtj_sjcx_xtsxjl_lsjl" defVal="历史记录"
                                                 fileName="cxtj"></emp:message></option>
                            </select>
                        </td>

                        <td>
                            <emp:message key="cxtj_sjcx_xtxxjl_rwpc" defVal="任务批次：" fileName="cxtj"></emp:message>
                        </td>
                        <td>
                            <input type="text" value='<%=taskid==null?"":taskid %>' id="taskid" name="taskid"
                                   onkeyup="numberControl($(this))" onblur="numberControl($(this))" maxlength="16"/>
                        </td>

                        <td>
                            <emp:message key="cxtj_sjcx_xtsxjl_sjhm" defVal="手机号码：" fileName="cxtj"></emp:message>
                        </td>
                        <td>
                            <input type="text" value='<%=phone==null?"":phone %>' id="phone" name="phone" maxlength="21"
                                   onkeyup="phoneInputCtrl($(this))"/>
                        </td>

                        <td class="tdSer">
                            <center><a id="search"></a></center>
                        </td>
                    </tr>
                    <tr>
                        <td><emp:message key="cxtj_sjcx_xtxxjl_ywlx" defVal="业务类型" fileName="cxtj"></emp:message></td>
                        <td>
                            <select id="busCode" name="busCode" isInput="false">
                                <option value=""><emp:message key="cxtj_sjcx_xtxxjl_qb" defVal="全部"
                                                              fileName="cxtj"></emp:message></option>
                                <option value="-1" <%if ("-1".equals(buscode)) { %> selected="selected" <%} %>>
                                    -(<emp:message key="cxtj_sjcx_xtxxjl_wywlx" defVal="无业务类型"
                                                   fileName="cxtj"></emp:message>)
                                </option>
                                <%
                                    if (busMap != null && busMap.size() > 0) {
                                        String value;
                                        for (String key : busMap.keySet()) {
                                            value = busMap.get(key);
                                            if (value == null) {
                                                value = "";
                                            }
                                %>
                                <option value="<%=key %>" <% if (key.equals(buscode)) {
                                    out.print("selected=\"selected\"");
                                } %>><%=value.replace("<", "&lt;").replace(">", "&gt;") %>(<%=key %>)
                                </option>
                                <%
                                        }
                                    }
                                %>
                            </select>
                        </td>

                        <td>
                            <emp:message key="cxtj_sjcx_xtsxjl_spzh" defVal="sp账号" fileName="cxtj"/>
                        </td>
                        <td>
                            <label>
                                <select name="userid" id="userid">
                                    <option value="">
                                        <emp:message key="cxtj_sjcx_xtxxjl_qb" defVal="全部"
                                                     fileName="cxtj"></emp:message>
                                    </option>
                                    <%
                                        if (userList != null && userList.size() > 0) {
                                            for (String userdata : userList) {
                                                if (userdata == null) {
                                                    continue;
                                                }
                                    %>
                                    <option value="<%=userdata %>" <%=userdata.equals(userid) ? "selected" : ""%>>
                                        <%=userdata %>
                                    </option>
                                    <%
                                            }
                                        }
                                    %>
                                </select>
                            </label>
                        </td>

                        <td>
                            <emp:message key="cxtj_sjcx_xtxxjl_ztm" defVal="状态码：" fileName="cxtj"></emp:message>
                        </td>
                        <td>
                            <input type="text" value='<%=mterrorcode==null?"":mterrorcode %>' id="mterrorcode"
                                   name="mterrorcode" maxlength="7"/>
                        </td>

                        <td>&nbsp;</td>
                    </tr>
                    <tr>

                        <td>
                            <emp:message key="cxtj_sjcx_xtxxjl_tdhm" defVal="通道号码：" fileName="cxtj"></emp:message>
                        </td>
                        <td>
                            <label>
                                <select name="spgate" id="spgate">
                                    <option value="">
                                        <emp:message key="cxtj_sjcx_xtxxjl_qb" defVal="全部"
                                                     fileName="cxtj"></emp:message>
                                    </option>
                                    <%
                                        if (spList != null && spList.size() > 0) {
                                            String tmpSpgate;
                                            for (DynaBean spgatevo : spList) {
                                                if (spgatevo == null || spgatevo.get("spgate") == null) {
                                                    continue;
                                                }
                                                tmpSpgate = spgatevo.get("spgate").toString().trim();
                                    %>
                                    <option value="<%=tmpSpgate %>" <%=tmpSpgate.equals(spgate) ? "selected" : ""%>>
                                        <%=tmpSpgate %>
                                    </option>
                                    <%
                                            }
                                        }
                                    %>
                                </select>
                            </label>
                        </td>
                        <td>
                            <emp:message key="cxtj_sjcx_xtxxjl_zdylsh" defVal="自定义流水号：" fileName="cxtj"></emp:message>
                        </td>
                        <td>
                            <%--<input type="text" style="width: 177px;" value='<%=usermsgid==null?"":usermsgid %>' id="usermsgid" name="usermsgid" onkeyup="javascript:numberControl($(this))"  onblur="javascript:numberControl($(this))" maxlength="19"/>--%>
                            <input type="text" style="width: 177px;" value='<%=usermsgid==null?"":usermsgid %>'
                                   id="usermsgid" name="usermsgid" maxlength="64"/>

                        </td>
                        <td></td>
                        <td></td>
                        <td>&nbsp;</td>
                    </tr>
                    <tr>
                        <td>
                            <emp:message key="cxtj_sjcx_xtxxjl_fssj" defVal="发送时间：" fileName="cxtj"></emp:message>
                        </td>
                        <td class="tableTime">
                            <input type="text" class="Wdate" readonly="readonly" onclick="sedtime()"
                                   value="<%=startTime==null?"":startTime %>" id="sendtime" name="sendtime">
                        </td>
                        <td>
                            <emp:message key="cxtj_sjcx_xtxxjl_z" defVal="至：" fileName="cxtj"></emp:message>
                        </td>
                        <td>
                            <input type="text" class="Wdate" readonly="readonly" onclick="revtime()"
                                   value="<%=endTime==null?"":endTime %>" id="recvtime" name="recvtime">
                            <input type="hidden" value='<%=usercode==null?"":usercode %>' id="usercode"
                                   name="usercode"/>
                        </td>
                        <td></td>
                        <td></td>
                        <td>&nbsp;</td>
                    </tr>
                    <%-- <tr>
                    <td>运营商：</td>
                    <td>
                       <select style="" id="spisuncm" name="spisuncm">
                            <option value="">全部</option>
                            <option value="0" <%="0".equals(spisuncm)?"selected":"" %>><emp:message key="cxtj_sjcx_xtsxjl_yd" defVal="移动" fileName="cxtj"></emp:message></option>
                            <option value="1" <%="1".equals(spisuncm)?"selected":"" %> ><emp:message key="cxtj_sjcx_xtsxjl_lt" defVal="联通" fileName="cxtj"></emp:message></option>
                            <option value="21" <%="21".equals(spisuncm)?"selected":"" %>><emp:message key="cxtj_sjcx_xtsxjl_dx" defVal="电信" fileName="cxtj"></emp:message></option>
                            <option value="5" <%="5".equals(spisuncm)?"selected":"" %>><emp:message key="cxtj_sjcx_xtsxjl_gw" defVal="国外" fileName="cxtj"></emp:message></option>
                        </select>
                    </td>

                        <td>
                        </td>
                        <td>
                        </td>
                        <td>
                        </td>
                        <td>
                        </td>
                        <td>&nbsp;</td>
                    </tr> --%>
                    </tbody>
                </table>
            </div>
            <table id="content">
                <thead>
                <tr>
                    <th><input type="checkbox" id="ckall" name="ckall" onclick="checkAlls(this,'reSendck');"/>
                    </th>
                    <th><emp:message key="cxtj_sjcx_report_spzh" defVal="sp账号" fileName="cxtj"/></th>
                    <th><%=tdhm %>
                    </th>
                    <th><%=yys %>
                    </th>
                    <th><%=sjhm %>
                    </th>
                    <th><%=rwpc %>
                    </th>
                    <th><%=ztbg %>
                    </th>
                    <th><%=fssj %>
                    </th>
                    <th><%=jssj %>
                    </th>
                    <th><%=ywlx %>
                    </th>
                    <th><%=ft %>
                    </th>
                    <th><%=dxnr %>
                    </th>
                    <th><%=zdylsh %>
                    </th>
                    <%
                        if ("realTime".equals(recordType)) {
                    %>
                    <th><%=bm %>
                    </th>
                    <%
                        }
                    %>
                </tr>
                </thead>
                <tbody>
                <%
                    if (isFirstEnter) {
                %>
                <tr>
                    <td colspan="13" class="queryData"><emp:message key="cxtj_sjcx_xtsxjl_qdjcxhqsj" defVal="请点击查询获取数据"
                                                                    fileName="cxtj"></emp:message>
                    </td>
                </tr>
                <%
                } else if (mtTaskList == null || mtTaskList.size() < 1) {
                %>
                <tr>
                    <td colspan="13" class="queryData"><emp:message key="cxtj_sjcx_xtxxjl_wjl" defVal="无记录"
                                                                    fileName="cxtj"></emp:message></td>
                </tr>
                <%
                } else {
                    CommonVariables CV = new CommonVariables();
                    for (int i = 0; i < mtTaskList.size(); i++) {
                        DynaBean mtTask = mtTaskList.get(i);
                        if (mtTask == null) {
                            continue;
                        }
                %>
                <tr>
                    <td>
                        <%
                            if ("realTime".equals(recordType)) {
                                //接口发送的taskid为0，只能重发通过接口发送的
                                //TODO
                                //if ("realTime".equals(recordType) && "0".equals(String.valueOf(mtTask.get("taskid")))) {
                                String erorcode1 = mtTask.get("errorcode") == null ? "" : mtTask.get("errorcode").toString().trim();
                                if (erorcode1.length() > 0 && !"DELIVRD".equals(erorcode1) && !"0".equals(erorcode1)) {%>
                        <input type="checkbox" id="reSendck" name="reSendck" value="<%=mtTask.get("id") %>"
                               ptmsgid="<%=ptmsgidList.get(i) %>"
                               phone="<%=mtTask.get("phone") %>"
                               taskid="<%=mtTask.get("taskid") %>"/>
                        <%
                                } else {
                                    out.print("");
                                }
                            } else {
                                out.print("");
                            }
                        %>
                    </td>
                    <td>
                        <%=mtTask.get("userid") == null ? "-" : mtTask.get("userid").toString() %>
                    </td>
                    <td>
                        <%=mtTask.get("spgate") == null ? "-" : mtTask.get("spgate").toString() %><%=mtTask.get("cpno") == null ? "" : mtTask.get("cpno").toString() %>
                    </td>
                    <td class="ztalign">
                        <%
                            String unicom = mtTask.get("unicom") == null ? "" : mtTask.get("unicom").toString();
                            if ("0".equals(unicom)) {
                                out.print(yd);
                            } else if ("1".equals(unicom)) {
                                out.print(lt);
                            } else if ("21".equals(unicom)) {
                                out.print(dx);
                            } else if ("5".equals(unicom)) {
                                out.print(gw);
                            } else {
                        %>
                        -
                        <%
                            }
                        %>
                    </td>
                    <td>
                        <%
                            if (mtTask.get("phone") != null && btnMap != null) {
                                String phones = CV.replacePhoneNumber(btnMap, mtTask.get("phone").toString());
                                out.print(phones);
                            } else {
                                out.print("-");
                            }
                        %>
                    </td>
                    <td>
                        <%
                            String strTaskid = mtTask.get("taskid") == null ? "-" : mtTask.get("taskid").toString();
                            if ("0".equals(strTaskid)) {
                                out.print("-");
                            } else {
                                out.print(strTaskid);
                            }
                        %>
                    </td>
                    <td class="ztalign">
                        <%
                            String erorcode = mtTask.get("errorcode") == null ? "" : mtTask.get("errorcode").toString().trim();
                            if (erorcode.length() == 0) {
                                out.print("-");
                            } else if ("DELIVRD".equals(erorcode) || "0".equals(erorcode)) {
                                out.print(cg);
                            } else {
                                if (errCodeDesMap.get(erorcode) != null) {
                        %>
                        <a onclick="showErrDis('<%=erorcode %>')"><%=sb %>[<%=erorcode %>]</a>
                        <%
                        } else {
                        %>
                        <%=sb %>[<%=erorcode %>]
                        <%
                                }
                            }
                        %>

                    </td>
                    <td>
                        <%
                            String sendtime;
                            if (mtTask.get("sendtime") != null) {
                                Date date = df.parse(mtTask.get("sendtime").toString());
                                sendtime = df.format(date);
                            } else {
                                sendtime = "-";
                            }
                            out.print(sendtime);
                        %>
                    </td>
                    <td>
                        <%
                            String recvtime;
                            if (mtTask.get("recvtime") != null) {
                                Date date = df.parse(mtTask.get("recvtime").toString());
                                recvtime = df.format(date);
                            } else {
                                recvtime = "-";
                            }
                            out.print(recvtime);
                        %>
                    </td>
                    <td class="textalign">
                        <%
                            String svrtype = mtTask.get("svrtype") == null ? "" : mtTask.get("svrtype").toString();
                        %>
                        <xmp><%=busMap.get(svrtype) == null ? "-" : busMap.get(svrtype) %>
                        </xmp>
                    </td>
                    <td><%=mtTask.get("pknumber") == null ? "-" : mtTask.get("pknumber").toString() %>
                        /<%=mtTask.get("pktotal") == null ? "-" : mtTask.get("pktotal").toString() %>
                    </td>
                    <td class="msgContentTd">
                        <a onclick="modify(this)">
                            <%
                                String xmessage = mtTask.get("message") == null ? "" : mtTask.get("message").toString();
                            %>
                            <label style="display:none">
                                <xmp><%=xmessage %>
                                </xmp>
                            </label>
                            <xmp><%=xmessage.length() > 5 ? xmessage.substring(0, 5) + "..." : xmessage %>
                            </xmp>
                        </a>
                    </td>
                    <td>
                        <%
                            //String strUsermsgid = mtTask.get("usermsgid") == null?"-":mtTask.get("usermsgid").toString();
                            //out.print(strUsermsgid);
                            //修改优先显示新版的自定义流水号custid，其次再显示旧版的usermsgid
                            String custid = mtTask.get("custid").toString();
                            String usermsgidStr = mtTask.get("usermsgid").toString();
                            if ((custid == null || "".equals(custid.trim())) && usermsgidStr != null && !"0".equals(usermsgidStr)) {
                                out.print(usermsgidStr);
                            } else {
                                String custidStr = (mtTask.get("custid") == null || "".equals(custid.trim())) ? "-" : mtTask.get("custid").toString();
                                out.print(custidStr);
                            }

                        %>
                    </td>
                    <%
                        if ("realTime".equals(recordType)) {
                    %>
                    <td><%=mtTask.get("msgfmt") == null ? "-" : mtTask.get("msgfmt").toString() %>
                    </td>
                    <%
                        }
                    %>
                </tr>
                <%
                        }
                    }
                %>
                </tbody>
                <tfoot>
                <tr>
                    <td colspan="13">
                        <div id="pageInfo"></div>
                    </td>
                </tr>
                </tfoot>
            </table>
            <div id="r_sysMrparams" class="hidden"></div>
        </form>
    </div>
    <%}%>
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

<%-- SP账号选择 --%>
<div id="spUserDiv" class="spUserDiv"
     title="<emp:message key="cxtj_sjcx_xtxxdc_spuser" defVal="SP账号选择" fileName="cxtj"></emp:message>">
    <div id="selectDiv">
        <span></span><emp:message key="dxzs_xtnrqf_title_99" defVal="SP账号" fileName="dxzs"/>：</span>
        <select id="spSelect">
            <%
                if (spUserList != null && spUserList.size() > 0) {
                    String spUserId = lfDfadvanced != null ? lfDfadvanced.getSpuserid() : "";
                    for (Userdata spUser : spUserList) {
            %>
            <option value="<%=spUser.getUserId()%>"
                    <%=spUserId != null && !"".equals(spUserId) && spUserId.equals(spUser.getUserId()) ? "selected" : "" %>>
                <%=spUser.getUserId() + "(" + spUser.getStaffName() + ")"%>
            </option>
            <%
                    }
                }
            %>
        </select>
    </div>
    <div id="confirmDiv">
        <input name="" type="button" id="subBut" onclick="choiceSp()"
               value="<emp:message key='cxtj_sjcx_jgtjbb_qd' defVal='确定' fileName='cxtj'/>"
               class="btnClass5 mr23"/>
        &nbsp;
        &nbsp;
        &nbsp;
        <input name="" type="button" onclick="cancelChoice()"
               value="<emp:message key='cxtj_sjfx_ztfsqs_qx' defVal='取消' fileName='cxtj'/>"
               class="btnClass6"/>
        <%-- 为了兼容ie8下取消按钮右下角旁边能点击到确定按钮，增加这个换行 --%>
        <br/>
    </div>
</div>

<%-- foot结束 --%>
<div id="modify" title="<%=dxnr%>">
    <table>
        <thead>
        <tr class="modify_tr1">
            <td>
                <label id="msgss"></label>
            </td>
        </tr>
        <tr class="modify_tr2">
            <td></td>
        </tr>
        </thead>
    </table>
</div>
<div class="clear"></div>
<script type="text/javascript">
    function LoginInfo(idname) {
        document.getElementById(idname).innerHTML = window.parent.parent.document.getElementById("loginparams").innerHTML;
    }

    LoginInfo("r_sysMrparams");
</script>
<script type="text/javascript"
        src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript"
        src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript"
        src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript"
        src="<%=commonPath %>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
<script type="text/javascript"
        src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
<script type="text/javascript" src="<%=iPath %>/js/exportexcel.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript"
        src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/cxtj_<%=langName%>.js"></script>
<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/dxzs_<%=langName%>.js"></script>
<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>

<script type="text/javascript">
    $(document).ready(function () {
        $('#spSelect').isSearchSelect({'width':'182','isInput':true,'zindex':0});
        $("#spUserId").val("");
        var findresult = "<%=findResult%>";
        if (findresult == "-1") {
            //alert("加载页面失败,请检查网络是否正常!");
            alert(getJsLocaleMessage("cxtj", "cxtj_sjcx_xtxxjl_text_1"));
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
        $('#spgate,#userid').isSearchSelect({'width': '179', 'isInput': true, 'zindex': 0});
        initPageSyn(<%=pageInfo.getTotalPage()%>, <%=pageInfo.getPageIndex()%>, <%=pageInfo.getPageSize()%>, <%=pageInfo.getTotalRec()%>, <%=pageInfo.getNeedNewData() %>, <%=isFirstEnter %>, "<%=loadPageUrl %>");

        $('#search').click(function () {
            submitForm();
        });

        $('#other').click(function () {
            location.href = "<%=path%>/r_sysMtRecord.htm?method=mmsFind";
        });
    });

    function checkAlls(e, str) {
        var a = document.getElementsByName(str);
        var n = a.length;
        for (var i = 0; i < n; i = i + 1)
            a[i].checked = e.checked;
    }

    /**
     * 选择弹窗
     */
    function spSelectAlert() {
        //统计勾选中的个数
        var n = 0;
        //批次号，用于判断勾选的是否是同一批次
        var batchId = "";
        var selected = document.getElementsByName("reSendck");
        for (i = 0; i < selected.length; i = i + 1) {
            if (selected[i].checked == true) {
                n = n + 1;
                if (batchId.length != "") {
                    if (batchId != selected[i].getAttribute("taskid")) {
                        if (batchId == 0 || selected[i].getAttribute("taskid") == 0) {
                            alert(getJsLocaleMessage("cxtj", "cxtj_sjcx_xtxxdc_bktsxz"));
                            return;
                        }
                        alert(getJsLocaleMessage("cxtj", "cxtj_sjcx_xtxxdc_pcxzcw"));
                        return;
                    }
                } else {
                    batchId = selected[i].getAttribute("taskid");
                }
            }
        }
        if (n < 1) {
            alert(getJsLocaleMessage("cxtj", "cxtj_sjcx_xtxxdc_jlxz"));
            return;
        }
        var isReturn = false;

        if(batchId != 0){
            $.ajax({
                type: "POST",
                async: false,
                url: "<%=path%>/que_sysMtRecord.htm?method=sendValidate",
                data: {
                    taskid: batchId,
                },
                beforeSend: function () {
                    page_loading();
                },
                complete: function () {
                    page_complete()
                },
                success: function (result) {
                    if (result == "ydcwnotsupport") {
                        //移动财务短信不支持补发
                        alert(getJsLocaleMessage('cxtj', 'cxtj_sjcx_xtxxdc_ydcwcfsb'));
                        isReturn = true;
                        return;
                        //企业富信不支持补发
                    } else if (result == "qyfxnotsupport") {
                        alert(getJsLocaleMessage('cxtj', 'cxtj_sjcx_xtxxdc_bkcffx'));
                        isReturn = true;
                        return;
                        //完美通知
                    } else if (result == "wmtznotsupport") {
                        alert(getJsLocaleMessage('cxtj', 'cxtj_sjcx_xtxxdc_bkcfwmtz'));
                        isReturn = true;
                        return
                    } else if (result == "success") {
                        //alert(getJsLocaleMessage('cxtj', 'cxtj_sjcx_xtxxdc_bfcg'));
                        return
                    }  else {
                        //验证失败
                        alert(getJsLocaleMessage('cxtj', 'cxtj_sjcx_xtxxdc_ydsb'));
                        isReturn = true;
                        return;
                    }
                },
                error: function () {
                    //验证失败
                    alert(getJsLocaleMessage('cxtj', 'cxtj_sjcx_xtxxdc_ydsb'));
                    isReturn = true;
                    return;
                }
            });
            if(isReturn){
                return;
            }
            $("#spUserDiv").dialog({
                autoOpen: true,
                height: 320,
                width: 500,
                resizable: false,
                modal: true,
                open: function () {
                },
                close: function () {
                    $("#spUserDiv").css("display", "none");
                    $("#spUserDiv").css("display", "none");
                }
            });
        }else {
            reSends();
        }


        //$("#changeTimingDiv").dialog("open");
    }

    /**
     * sp账号选择
     */
    function choiceSp() {
        var spSelect = $("#spSelect").val();
        if (typeof spSelect == "undefined" || spSelect == null || spSelect == "") {
            //无可用SP账号
            alert(getJsLocaleMessage('cxtj', 'cxtj_sjcx_xtxxdc_wkysp'));
            return;
        }
        $("#spUserId").val(spSelect);
        reSends();
    }

    /**
     * 退出选择框
     */
    function cancelChoice() {
        $("#spUserDiv").dialog("close");
        $("#spUserId").val("");
    }

    function toReSends() {
        spSelectAlert();
    }

    //批量重发
    function reSends() {
        var spCodesTemp = "";
        var ptmsgids = "";
        var phones = "";
        var taskids = "";
        var selected = document.getElementsByName("reSendck");
        var spUserId = $("#spUserId").val();
        var n = 0;		//统计勾选中的个数
        var batchId = ""; //批次号，用于判断勾选的是否是同一批次
        if (spCodesTemp == "") {
            for (i = 0; i < selected.length; i = i + 1) {
                if (selected[i].checked == true) {
                    spCodesTemp = spCodesTemp + selected[i].value;
                    ptmsgids = ptmsgids + selected[i].getAttribute("ptmsgid");
                    phones = phones + selected[i].getAttribute("phone");
                    taskids = taskids + selected[i].getAttribute("taskid");


                    if (batchId.length != "") {
                        if (batchId != selected[i].getAttribute("taskid")) {
                            alert(getJsLocaleMessage("cxtj", "cxtj_sjcx_xtxxdc_pcxzcw"));
                            return;
                        }
                    } else {
                        batchId = selected[i].getAttribute("taskid");
                    }

                    spCodesTemp = spCodesTemp + ",";
                    ptmsgids = ptmsgids + ",";
                    phones = phones + ",";
                    // taskids = taskids + ",";

                    n = n + 1;
                }
            }
            if (n < 1) {
                alert(getJsLocaleMessage("cxtj", "cxtj_sjcx_xtxxdc_jlxz"));
                return;
            }
            spCodesTemp = spCodesTemp.substring(0, spCodesTemp.lastIndexOf(','));
            ptmsgids = ptmsgids.substring(0, ptmsgids.lastIndexOf(','));
            phones = phones.substring(0, phones.lastIndexOf(','));
            //  taskids = taskids.substring(0, taskids.lastIndexOf(','));

        }
        var method = "";
        if (batchId == 0) {//接口发送重发
            method = "reSendSMSAllMt";
        } else {//web发送重发
            method = "resendBatchSms";
        }
        if (spCodesTemp != "") {
            if (confirm(getJsLocaleMessage("cxtj", "cxtj_sjcx_xtxxdc_jlxzqd"))) {
                $.ajax({
                    type: "POST",
                    url: "<%=path%>/que_sysMtRecord.htm?method=" + method,
                    data: {
                        msgIds: spCodesTemp,
                        ptmsgIds: ptmsgids,
                        phones: phones,
                        taskid: batchId,
                        spUser: spUserId,
                        pageSize:<%=pageInfo.getPageSize()%>
                    },
                    beforeSend: function () {
                        page_loading();
                    },
                    complete: function () {
                        page_complete()
                    },
                    success: function (result) {
                        debugger;
                        var res = result;
                        if (res.indexOf("createSuccess") != -1) {
                            alert('已重发' + res.substr(19) + "条选中数据");
                            //initPageSyn(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>,<%=pageInfo.getNeedNewData() %>, <%=isFirstEnter %>, "<%=loadPageUrl %>");
                            //submitForm();
                        }
                        else {
                            if (result != "-1") {
                                if (result.indexOf("empex") == 0) {
                                    if (result.indexOf("V10016") >= 0) {
                                        //发送内容包含如下违禁词组
                                        alert(getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_41') + "\n     " + result.substr(result.indexOf("[V10016]") + 8) + "\n" + getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_42'));
                                    } else {
                                        //任务创建失败
                                        alert(getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_1') + result.substr(5));
                                    }
                                } else if (result == "timerSuccess") {
                                    //创建短信任务及定时任务添加成功
                                    alert(getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_2'));
                                } else if (result == "timerFail") {
                                    //创建定时任务失败，取消任务创建
                                    alert(getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_3'));
                                } else if (result == "uploadFileFail") {
                                    //上传号码文件失败，取消任务创建
                                    alert(getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_4'));
                                } else if (result == "createSuccess") {
                                    //创建短信任务及提交到审批流成功
                                    alert(getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_5'));
                                } else if (result == "timeError") {
                                    //发送时间已经超过定时时间，不能发送
                                    alert(getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_6'));
                                } else if (result == "000") {
                                    //创建短信任务及发送到网关成功
                                    alert(getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_260'));
                                } else if (result == "saveSuccess") {
                                    //暂存草稿成功
                                    alert(getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_7'));
                                } else if (result == "error") {
                                    //请求响应超时，创建短信任务失败
                                    alert(getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_8'));
                                } else if (result == "nospnumber") {
                                    //发送失败
                                    alert(getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_9') + <%=StaticValue.SMSACCOUNT%> +getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_10'));
                                } else if (result == "depfee:-2") {
                                    //机构余额不足,创建短信任务失败
                                    alert(getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_11'));
                                } else if (result == "depfee:-1") {
                                    //创建短信任务时,修改计费信息失败
                                    alert(getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_12'));
                                } else if (result == "spuserfee:-2") {
                                    //SP账号余额不足,创建短信任务失败
                                    alert(getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_13'));
                                } else if (result == "spuserfee:-1" || result == "spuserfee:-3") {
                                    //创建短信任务时,检查SP账号费用失败
                                    alert(getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_14'));
                                } else if (result == "subnoFailed") {
                                    //拓展尾号处理失败
                                    alert(getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_15'));
                                } else if ("nogwfee" == result || "feefail" == result) {
                                    //获取运营商余额失败，取消任务创建
                                    alert(getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_16'));
                                } else if (result.indexOf("lessgwfee") == 0) {
                                    //运营商余额不足，取消任务创建
                                    alert(getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_17'));
                                } else if (result == "error") {
                                    //预览异常，操作失败
                                    alert(getJsLocaleMessage('common', 'common_js_wxSend_16') + "[EXFV014]");
                                } else if (result == "overstep") {
                                    //文件内有效号码大于
                                    alert(getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_55') + MAX_PHONE_NUM + getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_56'));
                                } else if (result == "overSize") {
                                    //上传文件过大，单次上传TXT文件或EXCEL文件最大支持
                                    alert(getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_57') + MAX_SIZE + getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_58') + ZIP_SIZE + getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_59'));
                                } else if (result == "noPhone") {
                                    //没有可发送的有效号码！[EXFV016]
                                    alert(getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_66'));
                                } else if (result == "ydcwnotsupport") {
                                    //移动财务短信不支持补发
                                    alert(getJsLocaleMessage('cxtj', 'cxtj_sjcx_xtxxdc_ydcwcfsb'));
                                }   else if (result == "noEffPhone") {
                                    alert(getJsLocaleMessage('cxtj', 'cxtj_sjcx_xtxxdc_sbts'));
                                } else {
                                    //向网关发送请求失败
                                    alert(getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_18') + result);
                                }
                            }
                            //alert(getJsLocaleMessage("cxtj", "cxtj_sjcx_xtxxdc_cfsb"));
                            cancelChoice();
                        }
                    }
                });

            }
        } else {
            //没有选中重发项
            alert(getJsLocaleMessage("cxtj", "cxtj_sjcx_xtxxdc_jlxzwk"));
            cancelChoice();
        }

    }
</script>
<script type="text/javascript" src="<%=iPath %>/js/que_sysMtRecord.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
</body>
</html>
