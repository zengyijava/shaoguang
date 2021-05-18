<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.montnets.emp.util.PageInfo" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@page import="com.montnets.emp.entity.pasroute.LfSpDepBind" %>
<%@page import="com.montnets.emp.entity.biztype.LfBusManager" %>
<%@page import="com.montnets.emp.common.vo.LfMttaskVo" %>
<%@page import="com.montnets.emp.common.constant.StaticValue" %>
<%@page import="com.montnets.emp.common.constant.CommonVariables" %>
<%@page import="com.montnets.emp.common.constant.SystemGlobals" %>
<%@page import="com.montnets.emp.common.context.EmpExecutionContext" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    String iPath = request.getRequestURI().substring(0, request.getRequestURI().lastIndexOf("/"));
    String inheritPath = iPath.substring(0, iPath.lastIndexOf("/"));
    String commonPath = inheritPath.substring(0, inheritPath.lastIndexOf("/"));

    java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @SuppressWarnings("unchecked")
    Map<String, String> btnMap = (Map<String, String>) session.getAttribute("btnMap");//按钮权限Map

    //@ SuppressWarnings("unchecked")
    //LfSysuser sysuser=((LfSysuser)session.getAttribute("loginSysuser"));

    PageInfo pageInfo = new PageInfo();
    pageInfo = (PageInfo) request.getAttribute("pageInfo");

    @SuppressWarnings("unchecked")
    List<LfSpDepBind> userList = (List<LfSpDepBind>) request.getAttribute("sendUserList");

    @SuppressWarnings("unchecked")
    List<LfBusManager> busList = (List<LfBusManager>) request.getAttribute("busList");

    @SuppressWarnings("unchecked")
    List<LfMttaskVo> mtList = (List<LfMttaskVo>) request.getAttribute("mtList");

    @SuppressWarnings("unchecked")
    Map<Long, Long> taskRemains = (Map<Long, Long>) request.getAttribute("taskRemains");

    @SuppressWarnings("unchecked")
    Map<String, String> titleMap = (Map<String, String>) session.getAttribute("titleMap");
    String titlePath = (String) request.getAttribute("titlePath");
    // String menuCode = "1520-1700";
    String menuCode = titleMap.get(titlePath);

    String actionPath = (String) request.getAttribute("actionPath");
    menuCode = menuCode == null ? "0-0-0" : menuCode;

    String recvtime = request.getParameter("recvtime");
    String sendtime = request.getParameter("sendtime");
    String deptid = request.getParameter("depid");
    String busCode = request.getParameter("busCode");
    String userid = request.getParameter("userid");
    String userName = request.getParameter("userName");
    String depNam = request.getParameter("depNam");
    String spUser = request.getParameter("spUser");
    String title = request.getParameter("title");
    String currUserid = request.getParameter("lguserid");
    //任务批次
    String taskID = request.getParameter("taskID");
    //信息内容
    String msg = request.getParameter("msg");
    String sendstate = request.getParameter("sendstate");
    String taskType = request.getParameter("taskType");
    //是否包含子机构
    String isContainsSun = (String) request.getAttribute("isContainsSun");
    boolean isBack = request.getParameter("isback") == null ? false : true;//是否返回操作
    if (isBack) {
        recvtime = (String) request.getAttribute("recvtime");
        sendtime = (String) request.getAttribute("sendtime");
        deptid = (String) request.getAttribute("depid");
        userid = (String) request.getAttribute("userid");
        userName = (String) request.getAttribute("userName");
        depNam = (String) request.getAttribute("depNam");
        spUser = (String) request.getAttribute("spUser");
        title = (String) request.getAttribute("title");
        taskID = (String) request.getAttribute("taskID");
        msg = (String) request.getAttribute("msg");
        sendstate = (String) request.getAttribute("sendstate");
        taskType = (String) request.getAttribute("taskType");
    }

    String lguserid = null != request.getAttribute("lguserid") ? request.getAttribute("lguserid").toString() : "";
    String langName = (String) session.getAttribute(StaticValue.LANG_KEY);
    if (taskID == null || "".equals(taskID.trim())) {
        taskID = "";
    } else {
        try {
            Long.parseLong(taskID.trim());
            taskID = taskID.trim();
        } catch (Exception e) {
            EmpExecutionContext.error(e, "任务批次号转换异常!taskID:" + taskID);
            taskID = "";
        }
    }

    //对msg的处理
    if (msg == null || "".equals(msg.trim())) {
        msg = "";
    }

    if (sendstate == null || "".equals(sendstate)) {
        sendstate = "0";
    }

    if (taskType == null || "".equals(taskType)) {
        taskType = "0";
    }


    if (isContainsSun == null || "".equals(isContainsSun)) {
        isContainsSun = "1";
    }

    int corpType = StaticValue.getCORPTYPE();
    boolean isFirstEnter = (Boolean) request.getAttribute("isFirstEnter");
    String zNodes3 = (String) request.getAttribute("departmentTree");
    //String zNodes2 = (String)request.getAttribute("deptUserTree");

    String findResult = (String) request.getAttribute("findresult");
    CommonVariables CV = new CommonVariables();
    String skin = session.getAttribute("stlyeSkin") == null ? "default" : (String) session.getAttribute("stlyeSkin");

    String httpUrl = StaticValue.getFileServerViewurl();

    String lgcorpcode = request.getParameter("lgcorpcode");

    String empSd = com.montnets.emp.i18n.util.MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_156", request);
    String httpSd = com.montnets.emp.i18n.util.MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_157", request);

    String wfs = com.montnets.emp.i18n.util.MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_201", request);
    String wgjscg = com.montnets.emp.i18n.util.MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_189", request);
    String sb = com.montnets.emp.i18n.util.MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_190", request);
    String empfsz = com.montnets.emp.i18n.util.MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_202", request);
    String zzfs = com.montnets.emp.i18n.util.MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_203", request);
    String wgclwc = com.montnets.emp.i18n.util.MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_188", request);
    String wsy = com.montnets.emp.i18n.util.MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_204", request);
    String xjwj = com.montnets.emp.i18n.util.MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_205", request);
    String qxz = com.montnets.emp.i18n.util.MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_144", request);
    String cs = com.montnets.emp.i18n.util.MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_223", request);
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
<head>
    <title>sendSMS.html</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <%@include file="/common/common.jsp" %>
    <link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet"
          type="text/css"/>
    <link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet"
          type="text/css"/>
    <link rel="stylesheet"
          href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>"/>
    <link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css">
    <link href="<%=commonPath%>/common/css/reading.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet"
          type="text/css"/>
    <link href="<%=commonPath%>/common/widget/zTreeStyle/zTreeStyle.css?V=<%=StaticValue.getJspImpVersion() %>"
          rel="stylesheet" type="text/css"/>
    <link href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet"
          type="text/css"/>
    <link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
    <link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
    <%if (StaticValue.ZH_HK.equals(langName)) {%>
    <link rel="stylesheet" type="text/css"
          href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
    <link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
    <%}%>
    <%--<link href="<%=inheritPath %>/styles/easyui.css?V=<%=StaticValue.JSP_IMP_VERSION %>" rel="stylesheet" type="text/css" />--%>

    <style type="text/css">
        .smt_smsTaskRecord div#tooltip {
            position: absolute;
            z-index: 1000;
            max-width: 435px;
            _width: expression(this.scrollWidth > 435 ? "435px" : "auto");
            width: auto;
            background: #A8CFF6;
            border: #FEFFD4 solid 1px;
            text-align: left;
            padding: 6px;
        }
    </style>
    <link rel="stylesheet" type="text/css"
          href="<%=iPath %>/css/smt_smsTaskRecord.css?V=<%=StaticValue.getJspImpVersion() %>"/>
    <link rel="stylesheet" type="text/css"
          href="<%=skin %>/dxzs_YuanGongShengRiZhuFu.css?V=<%=StaticValue.getJspImpVersion() %>"/>
    <link rel="stylesheet" type="text/css"
          href="<%=iPath %>/css/smt_smsCustomField.css?V=<%=StaticValue.getJspImpVersion() %>"/>
</head>

<body id="smt_smsTaskRecord">
<div id="container" class="container">
    <%-- 当前位置 --%>
    <%=com.montnets.emp.common.constant.ViewParams.getPosition(langName, menuCode) %>

    <%-- 内容开始 --%>
    <div id="rContent" class="rContent">
        <input type="hidden" name="httpUrl" id="httpUrl" value="<%=httpUrl %>">
        <input type="hidden" name="ISCLUSTER" id="isCluster" value="<%=StaticValue.getISCLUSTER() %>">
        <%-- 表示请求的名称 --%>
        <input type="hidden" name="htmName" id="htmName" value="<%=actionPath %>">
        <%-- 短信账号--%>
        <input type="hidden" name="smsAccount" id="smsAccount" value="<%=StaticValue.SMSACCOUNT %>">
        <form name="pageForm" action="<%=actionPath %>" method="post"
              id="pageForm">
            <div class="buttons">
                <div id="toggleDiv">
                </div>
                <a id="exportCondition"><emp:message key="dxzs_xtnrqf_button_12" defVal="导出" fileName="dxzs"/></a>
            </div>
            <div id="condition">
                <table>

                    <tbody>
                    <tr>
                        <td>
                            <emp:message key="dxzs_xtnrqf_title_143" defVal="隶属机构" fileName="dxzs"/>：
                        </td>
                        <td class="condi_f_l">
                            <div class="dxzs_div">

                                <input type="hidden" id="deptid" name="depid" value="<%=deptid==null?"":deptid%>"/>
                                <input type="text" class="treeInput dxzs_depNam" id="depNam" name="depNam"
                                       value="<%=depNam==null?qxz:depNam%>"
                                       onclick="javascript:showMenu();" readonly/>&nbsp;
                            </div>

                            <div id="dropMenu">
                                <iframe class="dxzs_iframe" frameborder="0" src="about:blank"></iframe>
                                <div class="dxzs_div1">
                                    <input type="checkbox" id="isContainsSun" name="isContainsSun"
                                           class="dxzs_isContainsSun"
                                           <%if(isContainsSun.equals("1")){%>checked="checked" <%}%>
                                           value="1"/><emp:message key="dxzs_xtnrqf_title_145" defVal="包含子机构"
                                                                   fileName="dxzs"/>&nbsp;&nbsp;
                                    <input type="button" style="width: 48px"
                                           value="<emp:message key='dxzs_xtnrqf_button_4' defVal='确定' fileName='dxzs'/>"
                                           class="btnClass1 dxzs_isContainsSun_ok"
                                           onclick="javascript:zTreeOnClickOK3();"/>&nbsp;&nbsp;
                                    <input type="button" style="width: 48px"
                                           value="<emp:message key='dxzs_xtnrqf_button_14' defVal='清空' fileName='dxzs'/>"
                                           class="btnClass1 dxzs_isContainsSun_cancel"
                                           onclick="javascript:cleanSelect_dep3();"/>
                                </div>
                                <ul id="dropdownMenu" class="tree"></ul>
                            </div>
                        </td>
                        <td>
                            <emp:message key="dxzs_xtnrqf_title_146" defVal="操作员" fileName="dxzs"/>：
                        </td>
                        <td class="condi_f_l">
                            <div class="dxzs_div">
                                <input type="hidden" id="userid" name="userid" value="<%=userid==null?"":userid %>"/>
                                <input type="text" id="userName" class="treeInput dxzs_depNam" name="userName"
                                       value="<%=userName==null?qxz:userName%>" onclick="javascript:showMenu2();"
                                       readonly
                                />&nbsp;
                            </div>
                            <div id="dropMenu2">
                                <iframe class="dxzs_iframe" frameborder="0" src="about:blank"></iframe>
                                <div class="dxzs_div1">
                                    <input type="button" style="width: 48px"
                                           value="<emp:message key='dxzs_xtnrqf_button_4' defVal='确定' fileName='dxzs'/>"
                                           class="btnClass1 dxzs_isContainsSun_ok"
                                           onclick="javascript:zTreeOnClickOK2();"/>&nbsp;&nbsp;
                                    <input type="button" style="width: 48px"
                                           value="<emp:message key='dxzs_xtnrqf_button_14' defVal='清空' fileName='dxzs'/>"
                                           class="btnClass1 dxzs_isContainsSun_cancel"
                                           onclick="javascript:cleanSelect_dep();"/>
                                </div>
                                <div class="dxzs_div2"><font class="zhu"><emp:message key="dxzs_xtnrqf_title_147"
                                                                                      defVal="注：请勾选操作员进行查询"
                                                                                      fileName="dxzs"/></font></div>
                                <ul id="dropdownMenu2" class="tree"></ul>
                            </div>
                        </td>
                        <td><emp:message key="dxzs_xtnrqf_title_99" defVal="SP账号" fileName="dxzs"/>：</td>

                        <td>
                            <label>
                                <select name="spUser" id="spUser">
                                    <option value="">
                                        <emp:message key="dxzs_xtnrqf_title_100" defVal="全部" fileName="dxzs"/>
                                    </option>
                                    <%
                                        if (userList != null && userList.size() > 0) {
                                            for (LfSpDepBind userdata : userList) {

                                    %>
                                    <option value="<%=userdata.getSpUser() %>"
                                            <%=userdata.getSpUser().equals(spUser) ? "selected" : "" %>>
                                        <%=userdata.getSpUser()%>
                                    </option>
                                    <%
                                            }
                                        }
                                    %>
                                </select>
                            </label>
                        </td>
                        <td class="tdSer">
                            <center><a id="search"></a></center>
                        </td>
                    </tr>
                    <tr>

                        <%--<td>
                            业务类型：
                        </td>
                        <td>
                           <label>
                                <select name="busCode" id="busCode" value="<%=busCode%>" style="width: 180px">
                                    <option value="">
                                        全部
                                    </option>
                                    <%
                                            if(busList != null && busList.size()>0){
                                              for(LfBusManager lfbusManager:busList){
                                         %>
                                    <option value="<%=lfbusManager.getBusCode() %>"
                                        <%=lfbusManager.getBusCode().equals(busCode)?"selected":"" %>>
                                        <%=lfbusManager.getBusName() %>
                                    </option>
                                    <%
                                            }
                                           }
                                          %>
                                </select>
                            </label>
                        </td> --%>
                        <td>
                            <emp:message key="dxzs_xtnrqf_title_158" defVal="发送类型" fileName="dxzs"/>：
                        </td>
                        <td>
                            <select name="taskType" id="taskType" isInput="false">
                                <option value="0"><emp:message key="dxzs_xtnrqf_title_100" defVal="全部"
                                                               fileName="dxzs"/></option>
                                <option value="1" <%="1".equals(taskType) ? "selected" : "" %>><emp:message
                                        key="dxzs_xtnrqf_title_156" defVal="EMP发送" fileName="dxzs"/></option>
                                <option value="2" <%="2".equals(taskType) ? "selected" : "" %>><emp:message
                                        key="dxzs_xtnrqf_title_157" defVal="HTTP接入" fileName="dxzs"/></option>
                            </select>
                        </td>
                        <td><emp:message key="dxzs_xtnrqf_title_159" defVal="任务批次" fileName="dxzs"/>：</td>
                        <td>
                            <input type="text" id="taskID" name="taskID" onkeyup="javascript:numberControl($(this))"
                                   value="<%="".equals(taskID)?"":taskID%>" maxlength="19">
                        </td>
                        <td><emp:message key="dxzs_xtnrqf_title_2" defVal="发送主题" fileName="dxzs"/>：</td>
                        <td><input type="text" id="title" name="title" value="<%=title== null?"":title %>"></td>
                        <td>&nbsp;</td>
                    </tr>
                    <tr>

                        <td><emp:message key="dxzs_xtnrqf_title_187" defVal="发送状态" fileName="dxzs"/>：</td>
                        <td>
                            <select name="sendstate" id="sendstate" isInput="false">
                                <option value="0"><emp:message key="dxzs_xtnrqf_title_100" defVal="全部"
                                                               fileName="dxzs"/></option>
                                <option value="3" <%="3".equals(sendstate) ? "selected" : "" %>><emp:message
                                        key="dxzs_xtnrqf_title_188" defVal="网关处理完成" fileName="dxzs"/></option>
                                <option value="1" <%="1".equals(sendstate) ? "selected" : "" %>><emp:message
                                        key="dxzs_xtnrqf_title_189" defVal="网关接收成功" fileName="dxzs"/></option>
                                <option value="2" <%="2".equals(sendstate) ? "selected" : "" %>><emp:message
                                        key="dxzs_xtnrqf_title_185" defVal="失败" fileName="dxzs"/></option>
                            </select>
                        </td>
                        <%--<td><emp:message key="dxzs_xtnrqf_title_12" defVal="发送内容" fileName="dxzs"/>：</td>--%>
                        <%--<td>--%>
                        <%--<input type="text" id="msg" name ="msg"  value="<%=msg== null?"":msg %>">	--%>
                        <%--</td>--%>
                        <td>
                            <emp:message key="dxzs_xtnrqf_title_25" defVal="发送时间" fileName="dxzs"/>：
                        </td>
                        <td>
                            <input type="text"
                                   readonly="readonly" class="Wdate" onclick="stime()"
                                   value="<%= sendtime== null?"":sendtime %>" id="sendtime" name="sendtime"
                            <%--onchange="onblourSendTime('end')" --%>>
                        </td>
                        <td><emp:message key="dxzs_xtnrqf_title_135" defVal="至" fileName="dxzs"/>：</td>
                        <td>
                            <input type="text"
                                   readonly="readonly" class="Wdate" onclick="rtime()"
                                   value="<%=recvtime==null?"":recvtime %>"
                                   id="recvtime" name="recvtime" <%--onchange="onblourSendTime('end')" --%>>
                        </td>
                        <td>&nbsp;</td>
                    </tr>
                    <tr>
                        <td colspan="7">
                            <input type="hidden" id="cookieName" value="smsTaskRecord<%=lguserid%>"/>
                            <div id="ctlIcon" onclick="customShowPanel()">
                                <img src="<%=commonPath%>/common/img/<%=skin.contains("frame3.0")?"icon_more_3.0":"icon_more_4.0"%>.png"
                                     alt="">
                                <i class="pull-icon" id="foldIcon"></i>
                            </div>
                            <div class="div-panel">
                                <div class="panel-title"><span><strong>自定义查询列表展示</strong></span></div>
                                <div class="div-panel-content">
                                    <div>
                                        <span>列表字段:</span>
                                        <input type="checkbox" id="selectAll" checked="checked"/><span
                                            class="m4">全选</span>
                                    </div>
                                    <div id="field-div">

                                    </div>
                                </div>

                                <div class="warm-tip"><p>注：该功能仅代表界面展示，导出数据仍将包含以上全部字段。</p></div>
                                <div class="btn-group">
                                    <span class="btnClass5 mr23" onclick="controlDisplay(true)">确定</span>
                                    <span class="btnClass6" onclick="controlHide()">取消</span>
                                </div>
                            </div>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <table id="content">
                <thead>
                <tr>
                    <th class="align-left">
                        <emp:message key="dxzs_xtnrqf_title_146" defVal="操作员" fileName="dxzs"/>
                    </th>
                    <th class="align-left">
                        <emp:message key="dxzs_xtnrqf_title_143" defVal="隶属机构" fileName="dxzs"/>
                    </th>
                    <th class="align-left">
                        <emp:message key="dxzs_xtnrqf_title_99" defVal="SP账号" fileName="dxzs"/>
                    </th>
                    <th class="dxzs_th">
                        <emp:message key="dxzs_xtnrqf_title_158" defVal="发送类型" fileName="dxzs"/>
                    </th>
                    <th class="align-left">
                        <emp:message key="dxzs_xtnrqf_title_2" defVal="发送主题" fileName="dxzs"/>
                    </th>
                    <th class="dxzs_th">
                        <emp:message key="dxzs_xtnrqf_title_159" defVal="任务批次" fileName="dxzs"/>
                    </th>
                    <th class="dxzs_th2">
                        <emp:message key="dxzs_xtnrqf_title_25" defVal="发送时间" fileName="dxzs"/>
                    </th>
                    <th class="align-left">
                        <emp:message key="dxzs_xtnrqf_title_187" defVal="发送状态" fileName="dxzs"/>
                    </th>
                    <th>
                        <emp:message key="dxzs_xtnrqf_title_191" defVal="号码个数" fileName="dxzs"/>
                    </th>
                    <th>
                        <emp:message key="dxzs_xtnrqf_title_192" defVal="提交信息数" fileName="dxzs"/>
                    </th>
                    <th>
                        <emp:message key="dxzs_xtnrqf_title_193" defVal="发送成功数" fileName="dxzs"/>
                    </th>
                    <th>
                        <emp:message key="dxzs_xtnrqf_title_194" defVal="提交失败数" fileName="dxzs"/>
                    </th>
                    <th>
                        <emp:message key="dxzs_xtnrqf_title_195" defVal="接收失败数" fileName="dxzs"/>
                    </th>
                    <th>
                        <emp:message key="dxzs_xtnrqf_title_196" defVal="滞留数" fileName="dxzs"/>
                    </th>
                    <!---->
                    <th class="align-left">
                        <emp:message key="dxzs_xtnrqf_title_12" defVal="发送内容" fileName="dxzs"/>
                    </th>
                    <!---->
                    <th class="align-left">
                        <emp:message key="dxzs_xtnrqf_title_163" defVal="发送文件" fileName="dxzs"/>
                    </th>
                    <th>
                        <emp:message key="dxzs_xtnrqf_title_6" defVal="操作" fileName="dxzs"/>
                    </th>
                    <th>
                        <emp:message key="dxzs_xtnrqf_title_197" defVal="回复详情" fileName="dxzs"/>
                    </th>
                    <th>
                        <emp:message key="dxzs_xtnrqf_title_198" defVal="发送详情" fileName="dxzs"/>
                    </th>
                </tr>
                </thead>
                <tbody>
                <%
                    if (isFirstEnter) {
                %>
                <tr>
                    <td colspan="19" align="center"><emp:message key="dxzs_xtnrqf_title_164" defVal="请点击查询获取数据"
                                                                 fileName="dxzs"/></td>
                </tr>
                <%
                } else if (mtList != null && mtList.size() > 0) {
                    for (LfMttaskVo mt : mtList) {
                %>
                <tr align="center">
                    <td class="align-left">
                        <%=mt.getName() %><%
                        if (mt.getUserState() == 2) {
                            out.print("<font color='red'>(已注销)</font>");
                        }
                    %>
                    </td>
                    <td class="align-left">
                        <xmp><%=mt.getDepName() %>
                        </xmp>
                    </td>
                    <td class="align-left">
                        <%=mt.getSpUser() %>
                    </td>
                    <td <%--class="textalign"--%>>
                        <%
                            if (mt.getTaskType() == 1) {
                                out.print(empSd);
                            } else {
                                out.print(httpSd);
                            }
                        %>
                    </td>
                    <td class="align-left">
                        <span class="maxwidth"><%=mt.getTitle() == null ? "" : (mt.getTitle().replaceAll("<", "&lt;").replaceAll(">", "&gt;")) %></span>
                    </td>
                    <td>
                        <%=mt.getTaskId() == 0 ? "-" : mt.getTaskId()%>
                    </td>
                    <td>
                        <%=mt.getTimerTime() == null ? "" : df.format(mt.getTimerTime()) %><%--发送时间 --%>
                    </td>
                    <td class="align-left">
                        <%
                            String sendState = mt.getSendstate().toString();
                            String error = "";
                            if (sendState.equals("0")) {
                                sendState = wfs;
                            } else if (sendState.equals("1")) {
                                sendState = wgjscg;
                            } else if (sendState.equals("2")) {
                                sendState = sb;
                                if (mt.getErrorCodes() != null && !"".equals(mt.getErrorCodes())) {
                                    error = "[" + mt.getErrorCodes() + "]";
                                }
                            } else if (sendState.equals("4")) {
                                sendState = empfsz;
                            } else if (sendState.equals("6")) {
                                sendState = zzfs;
                            } else if (sendState.equals("3")) {
                                sendState = wgclwc;
                            } else {
                                sendState = wsy;
                            }
                        %>
                        <%=sendState %><%=error %>
                    </td>
                    <td>
                        <%
                            if (mt.getTaskType() == 1) {
                                out.print(mt.getEffCount());
                            } else {
                                out.print("-");
                            }
                        %>
                    </td>
                    <%
                        if (mt.getFaiCount() == null) {
                    %>
                    <td>-</td>
                    <td>-</td>
                    <td>-</td>
                    <td>-</td>
                    <%
                    } else {
                    %>
                    <td>
                        <%=mt.getSendstate() == 2 ? (mt.getIcount() == null ? "0" : mt.getIcount()) : (mt.getIcount2() == null ? "-" : mt.getIcount2()) %>
                    </td>
                    <td>
                        <%
                            String fail_count = (mt.getFaiCount() == null ? "0" : mt.getFaiCount());
                            String icount = (mt.getIcount2() == null ? "0" : mt.getIcount2());

                            //提交总数
                            Long icount1 = Long.parseLong(icount);
                            //提交失败总数
                            Long fail = Long.parseLong(fail_count);
                            long suc = icount1 - fail;
                            if (mt.getSendstate() == 2) {
                                suc = 0;
                                out.print(suc);
                            } else if (mt.getIcount2() == null) {
                                out.print("-");
                            } else {
                                out.print(suc);
                            }
                        %>
                    </td>
                    <td>
                        <%=mt.getSendstate() == 2 ? (mt.getIcount() == null ? 0 : mt.getIcount()) : (mt.getIcount2() == null ? "-" : mt.getFaiCount()) %>
                    </td>
                    <td>
                        <%=mt.getSendstate() == 2 ? "0" : (mt.getIcount2() == null ? "-" : (mt.getRFail2() == null ? "-" : mt.getRFail2()))%>
                    </td>
                    <%
                        }
                    %>
                    <td>
                        <%
                            long remains = (taskRemains == null || taskRemains.get(mt.getTaskId()) == null) ? 0L : taskRemains.get(mt.getTaskId());
                            out.print(remains == 0 ? "0" : "<font color='red'>" + remains + "</font>");
                        %>
                    </td>
                    <!---->
                    <td class="textalign">
                        <%
                            if (mt.getMsg() == null || "".equals(mt.getMsg())) {
                                if (mt.getTaskType() == 1) {
                                    out.print(xjwj);
                                } else {
                                    out.print("-");
                                }
                            } else {
                        %>
                        <a onclick=javascript:modifyNew(this)>
                            <xmp><%
                                String st = "";
                                String temp = mt.getMsg().replaceAll("#[pP]_(\\d+)#", "{#" + cs + "$1#}");
                                if (temp.length() > 5) {
                                    st = temp.substring(0, 5) + "...";
                                } else {
                                    st = temp;
                                }
                                out.print(st);
                            %></xmp>
                            <%--								 	 用label显示短信内容<label style="display:none"><xmp><%//temp %></xmp></label>--%>
                            <textarea style="display:none"><%=temp %></textarea>
                            <xmp id="msgXmp" name="msgXmp" style="display:none"><%=temp %>
                            </xmp>
                        </a>
                        <%} %>
                    </td>
                    <!---->
                    <td class="align-left">
                        <%if (btnMap.get(StaticValue.PHONE_LOOK_CODE) == null || mt.getTaskType() == 2) { %>
                        <label>-</label>
                        <%} else { %>
                        <a href="javascript:checkFileOuter('<%=mt.getMobileUrl()%>','<%=request.getContextPath()%>')"><emp:message
                                key="dxzs_xtnrqf_title_106" defVal="查看" fileName="dxzs"/></a>
                        &nbsp;
                        <a href="javascript:downloadFilesOuter('<%=mt.getMobileUrl()%>','<%=request.getContextPath()%>')"><emp:message
                                key="dxzs_xtnrqf_title_165" defVal="下载" fileName="dxzs"/></a>
                        <%} %>
                    </td>

                    <td>
                        <%
                            if(mt.getTaskType()==1){
                                if(mt.getIsRetry() == 1){ %>
                        <label><emp:message key="dxzs_xtnrqf_title_200" defVal="已重发" fileName="dxzs"/>&nbsp;&nbsp;&nbsp;</label>
                        <label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label>

                        <%}else if(mt.getSendstate().toString().equals("2") && currUserid.toString().equals(mt.getUserId().toString())){%>
                        <a name="rsend"  href="javascript:reSend('<%=mt.getMtIdCipher()%>','<%=mt.getMsType() %>')"><emp:message key="dxzs_xtnrqf_title_199" defVal="失败重发" fileName="dxzs"/></a>
                        <label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label>
                        <%}else if(mt.getSendstate().toString().equals("2")){ %>
                        <%-- 任务发送失败，任务发送者不是当前登录操作员 此操作员不能点击失败重发按钮 也不能点击汇总--%>
                        <label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label>
                        <label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label>
                        <%} else {%>

                        <label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label>

                        <%
                            if(btnMap.get(menuCode+"-1")!=null)
                            {
                        %>
                        <a name="task"  href="javascript:task('<%=mt.getTaskId()%>','<%=mt.getMtId()%>')">汇总</a>

                        <%
                            }
                        %>

                        <%}%>
                        <%}else{ %>
                        <label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label>
                        <%} %>
                    </td>

                    <td>
                        <%
                            if (mt.getTaskType() == 1) {//回复详情
                                if (mt.getIsReply() == 1 && "3".equals(mt.getSendstate().toString())) { %>
                        <a href="javascript:searchNoticeDetail(1,'<%=mt.getMtId()%>','<%=lgcorpcode%>')"><emp:message
                                key="dxzs_xtnrqf_title_106" defVal="查看" fileName="dxzs"/>(<%= mt.getMoNumber()%>)</a>
                        <%} else {%>
                        -
                        <% }
                        } else {%>
                        -
                        <%} %>
                    </td>
                    <td>
                        <%if (mt.getSendstate().toString().equals("2")) { %>
                        -
                        <%} else {%>
                        <a onclick="javascript:location.href='<%=actionPath%>?method=findAllSendInfo&mtid=<%=mt.getMtIdCipher()%>&type=0'"><emp:message
                                key="dxzs_xtnrqf_title_106" defVal="查看" fileName="dxzs"/></a>
                        <%} %>
                    </td>
                </tr>
                <%
                    }
                } else {
                %>
                <tr>
                    <td align="center" colspan="19"><emp:message key="dxzs_xtnrqf_title_87" defVal="无记录"
                                                                 fileName="dxzs"/></td>
                </tr>
                <%} %>
                </tbody>
                <tfoot>
                <tr>
                    <td colspan="19">
                        <div id="pageInfo"></div>
                    </td>
                </tr>
                </tfoot>
            </table>
            <div id="smssendparams" class="hidden"></div>
        </form>
    </div>
    <div id="detailDialog">
    </div>
    <%-- 内容结束 --%>
    <div id="modify" title="<emp:message key='dxzs_xtnrqf_title_111' defVal='信息内容' fileName='dxzs'/>">
        <table width="100%">
            <thead>
            <tr class="dxzs_tr">
                <td style='word-break: break-all;'>
                    <%--								用label显示短信内容<span><label id="msgcont" style="width:100%;height:100%"><xmp style='word-break: break-all;white-space:normal;'></xmp></label></span>--%>
                    <span><textarea id="msgcont" rows="15" readonly="readonly"></textarea></span>
                </td>
            </tr>
            <%--					   <tr style="padding-top:2px;">--%>
            <%--							<td>--%>
            <%--							</td>--%>
            <%--							</tr>--%>

            </thead>
        </table>
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
<div class="clear"></div>
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
        src="<%=commonPath%>/common/js/jquery.ztree-myjquery-b.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript"
        src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"
        type="text/javascript"></script>
<script type="text/javascript"
        src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript"
        src="<%=commonPath%>/common/js/checkFile.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=iPath %>/js/smt_smsSendedBox.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript"
        src="<%=iPath %>/js/smt_smsTaskRecord.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript"
        src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript"
        src="<%=iPath %>/js/smt_smsCustomField.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript">
    $(document).ready(function () {
        // getLoginInfo("#smssendparams");
        //参数是要隐藏的下拉框的div的id数组，
        closeTreeFun(["dropMenu2", "dropMenu"]);
        var findresult = "<%=findResult%>";
        if (findresult == "-1") {
            alert(getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_77'));
            return;
        }

        $("#title").live("keyup blur", function () {
            var value = $(this).val();
            if (value != filterString(value)) {
                $(this).val(filterString(value));
            }
        });

        initPage(<%=pageInfo.getTotalPage()%>, <%=pageInfo.getPageIndex()%>, <%=pageInfo.getPageSize()%>, <%=pageInfo.getTotalRec()%>);
        $('#spUser').isSearchSelect({'width': '160', 'isInput': true, 'zindex': 0});
        //导出全部数据到excel
        $("#exportCondition").click(
            function () {

                if (confirm(getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_210'))) {
                    var mTitle = '<%=title!=null?title:""%>';
                    var spuser = '<%=spUser!=null?spUser:""%>';
                    var lgcorpcode = $("#lgcorpcode").val();
                    var lguserid = $("#lguserid").val();
                    var taskType = '<%=taskType%>';
                    var taskID = '<%=taskID%>';
                    var msg = '<%=msg%>';
                    var isContainsSun = '<%=isContainsSun%>';
                    var sendstate = '<%=sendstate%>';
                    <%
                    if(mtList!=null && mtList.size()>0){
                      if(pageInfo.getTotalRec()<=500000){
                    %>
                    $.ajax({
                        type: "POST",
                        url: "<%=path%>/<%=actionPath%>?method=ReportCurrPageExcel",
                        data: {
                            spuser: spuser,
                            mTitle: mTitle,
                            pageIndex:<%=pageInfo.getPageIndex()%>,
                            pageSize:<%=pageInfo.getPageSize()%>,
                            lguserid: lguserid,
                            lgcorpcode: lgcorpcode,
                            taskType: taskType,
                            taskID: taskID,
                            msg: msg,
                            isContainsSun: isContainsSun,
                            sendstate: sendstate
                        },
                        beforeSend: function () {
                            page_loading();
                        },
                        complete: function () {
                            page_complete();
                        },
                        success: function (result) {
                            if (result == 'true') {
                                download_href("<%=path%>/<%=actionPath%>?method=downloadFile&exporttype=smstaskrecord_export");
                            } else {
                                alert(getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_211'));
                            }
                        }
                    });
                    <%
                    }else{%>
                    alert(getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_272'));
                    <%}
                    }else{
                    %>
                    alert(getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_212'));
                    <%
                    }%>
                }
            });

    });
</script>
</body>
</html>
