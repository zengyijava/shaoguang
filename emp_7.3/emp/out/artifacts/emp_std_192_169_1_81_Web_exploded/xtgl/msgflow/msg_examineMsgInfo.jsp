<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.montnets.emp.common.constant.ViewParams,com.montnets.emp.entity.approveflow.LfFlowRecord" %>
<%@ page import="com.montnets.emp.entity.sms.LfMttask" %>
<%@ page import="com.montnets.emp.entity.sysuser.LfSysuser" %>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils" %>
<%@ page import="com.montnets.emp.servmodule.xtgl.constant.ServerInof" %>
<%@page import="org.apache.commons.lang.StringEscapeUtils" %>
<%@page import="java.util.*" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple" %>

<%
    String path = request.getContextPath();
    String langName = (String) session.getAttribute("emp_lang");
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    String iPath = request.getRequestURI().substring(0, request.getRequestURI().lastIndexOf("/"));
    String inheritPath = iPath.substring(0, iPath.lastIndexOf("/"));
    String commonPath = inheritPath.substring(0, inheritPath.lastIndexOf("/"));
    @SuppressWarnings("unchecked")
    Map<String, String> btnMap = (Map<String, String>) session.getAttribute("btnMap");//按钮权限Map
    @SuppressWarnings("unchecked")
    List<LfFlowRecord> recordList = (List<LfFlowRecord>) request.getAttribute("recordList");
    LfMttask lfmttask = (LfMttask) request.getAttribute("lfmttask");
    @SuppressWarnings("unchecked")
    LinkedHashMap<Long, String> usernameMap = (LinkedHashMap<Long, String>) request.getAttribute("usernameMap");

    //当前审批人的审核等级
    String rlevel = (String) request.getAttribute("rlevel");
    LfSysuser lfsysuser = (LfSysuser) request.getAttribute("lfsysuser");
    LfFlowRecord flowRecord = (LfFlowRecord) request.getAttribute("flowRecord");

    String flowid = String.valueOf(flowRecord.getFId());

    //判断是不是最后 审批人，并且允许发送
    String isLastCheck = (String) request.getAttribute("isLastCheck");

    //该任务的提交人名字
    String name = (String) request.getAttribute("name");
    //是否最后一级显示
    String isshow = (String) request.getAttribute("isshow");
    //1短信 2彩信
    Integer msgtype = (Integer) flowRecord.getInfoType();
    String menuname = "";
    String msgname = "";
    if (msgtype == 1 || msgtype == 5) {
        menuname = MessageUtils.extractMessage("xtgl", "xtgl_spgl_xxsp_dxsp", request);
        msgname = MessageUtils.extractMessage("xtgl", "xtgl_spgl_xxsp_dx", request);
        ;
    } else if (msgtype == 2) {
        menuname = MessageUtils.extractMessage("xtgl", "xtgl_spgl_xxsp_cxsp", request);
        ;
        msgname = MessageUtils.extractMessage("xtgl", "xtgl_spgl_xxsp_cx", request);
        ;
    }

    java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm");
    String nowtime = df.format(new Date());

    String skin = session.getAttribute("stlyeSkin") == null ? "default" : (String) session.getAttribute("stlyeSkin");
    String httpUrl = StaticValue.getFileServerViewurl();

    //服务器名称
    String serverName = new ServerInof().getServerName();

%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
<head>
    <%@include file="/common/common.jsp" %>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <link rel="stylesheet" type="text/css"
          href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>"/>
    <link rel="stylesheet" type="text/css" href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>"/>
    <link rel="stylesheet" type="text/css" href="<%=iPath%>/css/addMo.css?V=<%=StaticValue.getJspImpVersion() %>"/>
    <link rel="stylesheet" type="text/css" href="<%=skin %>/addMo.css?V=<%=StaticValue.getJspImpVersion() %>"/>
    <link rel="stylesheet" type="text/css"
          href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.dialog.new.css?V=<%=StaticValue.getJspImpVersion() %>">
    <link rel="stylesheet" type="text/css"
          href="<%=commonPath%>/common/css/batchFileSend.css?V=<%=StaticValue.getJspImpVersion() %>"/>
    <link rel="stylesheet" type="text/css" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>">
    <link rel="stylesheet" type="text/css" href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>">
    <%if (StaticValue.ZH_HK.equals(langName)) {%>
    <link rel="stylesheet" type="text/css"
          href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
    <link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
    <%}%>

    <link rel="stylesheet" type="text/css"
          href="<%=iPath%>/css/msg_examineMsgInfo.css?V=<%=StaticValue.getJspImpVersion() %>"/>
    <link rel="stylesheet" type="text/css"
          href="<%=skin %>/msg_examineMsgInfo.css?V=<%=StaticValue.getJspImpVersion() %>"/>

</head>
<body id="msg_examineMsgInfo">
<div id="container" class="container">
    <%-- 当前位置 --%>
    <%=ViewParams.getPosition(langName, ViewParams.MSRECODE, MessageUtils.extractMessage("xtgl", "xtgl_spgl_xxsp_spxq", request)) %>
    <div id="rContent" class="rContent">
        <div class="titletop rContent_div">
            <table class="titletop_table rContent_div_table">
                <tr>
                    <td class="titletop_td">
                        <%=menuname %>
                    </td>
                    <td align="right">
                        <font class="titletop_font" onclick="javascript:updateToList()">&larr;&nbsp;<emp:message
                                key="xtgl_spgl_xxsp_fhsyj" defVal="返回上一级" fileName="xtgl"/></font>
                    </td>
                </tr>
            </table>
        </div>
        <form method="post" action="" name="form2">
            <input type="hidden" id="pathUrl" value="<%=path %>">
            <input type="hidden" name="httpUrl" id="httpUrl" value="<%=httpUrl %>">
            <input type="hidden" name="ISCLUSTER" id="isCluster" value="<%=StaticValue.getISCLUSTER() %>">
            <%-- 预发送条数--%>
            <input type="hidden" name="count" id="count" value="<%=lfmttask!=null ? lfmttask.getIcount() : "0"%>"/>
            <input type="hidden" name="mtId" id="mtId" value="<%=lfmttask.getMtId()%>"/>
            <input type="hidden" name="flowid" id="flowid" value="<%=flowid%>"/>
            <input type="hidden" name="rlevel" id="rlevel" value="<%=flowRecord.getRLevel()%>"/>
            <%-- 流程序列ID--%>
            <input type="hidden" name="frId" id="frId" value="<%=flowRecord.getFrId()%>"/>
            <%-- 审核类型   1短信  2彩信--%>
            <input type="hidden" name="msgtype" id="msgtype" value="<%=flowRecord.getInfoType()%>"/>
            <input type="hidden" name="msgname" id="msgname" value="<%=msgname%>"/>

            <input type="hidden" name="isLastCheck" id="isLastCheck" value="<%=isLastCheck%>"/>

            <div id="hiddenValueDiv" class="hidden"></div>
            <%--
            <div  id="PconfigDiv" style="padding-top: 0px;">
                <div id="tabContainer1" onclick="changflod('a')"  style="width:520px;display: block;font-size: 13px;font-weight: normal;" class="div_bg div_bd">
                     <label>任务信息</label>
                     <a id="imga" class="fold" style="text-decoration: none;margin-left: 428px;margin-top: 5px;">&nbsp;&nbsp;&nbsp;&nbsp;</a>
                </div>
            </div>
            --%>
            <div class="itemDiv msgdiva" id="msgdiva">
                <table class="msgdiva_table">
                    <tr>
                        <td align="right" class="div_bd div_bg tjr_mh_td">
                            <emp:message key="xtgl_spgl_xxsp_tjr_mh" defVal="提交人：" fileName="xtgl"/>
                        </td>
                        <td align="left" class="div_bd tjr_mh_down_td" colspan="3">
                            <%=name.replaceAll("<", "&lt;").replaceAll(">", "&gt;") %>
                        </td>
                    </tr>
                    <tr>
                        <td align="right" class="div_bd div_bg zt_mh_td">
                            <emp:message key="xtgl_spgl_xxsp_zt_mh" defVal="主题：" fileName="xtgl"/>
                        </td>
                        <td align="left" class="div_bd zt_mh_down_up" colspan="3">
                            <%
                                if (lfmttask.getTitle() != null && !"".equals(lfmttask.getTitle())) {
                                    out.print(lfmttask.getTitle().replaceAll("<", "&lt;").replaceAll(">", "&gt;"));
                                } else {
                                    out.print("");
                                }
                            %>
                        </td>
                    </tr>
                    <tr>
                        <td align="right" class="div_bd div_bg yfsts_mh_td">
                            <emp:message key="xtgl_spgl_xxsp_yfsts_mh" defVal="预发送条数：" fileName="xtgl"/>
                        </td>
                        <td align="left" class="div_bd yfsts_mh_down_td">
                            <%
                                if (lfmttask != null && (lfmttask.getMsType() == 1 || lfmttask.getMsType() == 6)) {
                                    out.print(lfmttask.getIcount());
                                } else if (lfmttask != null && lfmttask.getMsType() == 2) {
                                    out.print(lfmttask.getEffCount());
                                } else {
                                    out.print("0");
                                }
                            %>
                        </td>
                        <td align="right" class="div_bd div_bg yxhms_mh_td">
                            <emp:message key="xtgl_spgl_xxsp_yxhms_mh" defVal="有效号码数：" fileName="xtgl"/>
                        </td>
                        <td align="left" class="div_bd yxhms_mh_down_td">
                            <%
                                if (lfmttask != null && !"0".equals(lfmttask.getEffCount().toString()) && btnMap.get(StaticValue.PHONE_LOOK_CODE) != null) {
                            %>
                            <a href="javascript:checkFile('<%=lfmttask.getMobileUrl() %>','<%=request.getContextPath()%>')">
                                <%=lfmttask.getEffCount() %>
                            </a>
                            <%
                                } else {
                                    out.print(lfmttask.getEffCount());
                                }
                            %>
                        </td>
                    </tr>
                    <tr>
                        <td align="right" class="div_bd div_bg tjsj_mh_td">
                            <emp:message key="xtgl_spgl_xxsp_tjsj_mh" defVal="提交时间：" fileName="xtgl"/>
                        </td>
                        <td align="left" class="div_bd tjsj_mh_down_td">
                            <%=df.format(lfmttask.getSubmitTime()) %>
                        </td>
                        <td align="right" class="div_bd div_bg dssj_mh_td">
                            <emp:message key="xtgl_spgl_xxsp_dssj_mh" defVal="定时时间：" fileName="xtgl"/>
                        </td>
                        <td align="left" class="div_bd dssj_mh_down_td">
                            <%
                                if (lfmttask.getTimerStatus() == 0) {
                                    out.print("-");
                                } else if (lfmttask.getTimerStatus() == 1) {
                                    out.print(df.format(lfmttask.getTimerTime()));
                                } else {
                                    out.print("-");
                                }
                            %>
                        </td>
                    </tr>

                    <%if ((msgtype == 1 || msgtype == 5) && lfmttask.getMsg() != null && !"".equals(lfmttask.getMsg())) {%>
                    <tr>
                        <td align="right" class="div_bd div_bg fsnr_mh_td">
                            <emp:message key="xtgl_spgl_xxsp_fsnr_mh" defVal="发送内容：" fileName="xtgl"/>
                        </td>
                        <td align="left" class="div_bd fsnr_mh_down_td" colspan="3">
                            <textarea class="fsnr_mh_down_td_text"
                                      readonly><%=StringEscapeUtils.escapeHtml(lfmttask.getMsg()) %></textarea>
                        </td>
                    </tr>
                    <%
                    } else if (msgtype == 2) {
                    %>
                    <tr>
                        <td align="right" class="div_bd div_bg fsnr_mh_td">
                            <emp:message key="xtgl_spgl_xxsp_fsnr_mh" defVal="发送内容：" fileName="xtgl"/>
                        </td>
                        <td align="left" class="div_bd fsnr_mh_down_td" colspan="3">
                            <a href="javascript:doPreview('<%=lfmttask.getMsg() %>','<%=lfmttask.getBmtType() %>','<%=lfmttask.getTmplPath()%>')"><emp:message
                                    key="xtgl_spgl_xxsp_yl" defVal="预览" fileName="xtgl"/></a>
                        </td>
                    </tr>
                    <%
                        }
                    %>
                </table>
            </div>


            <%
                Integer level = Integer.valueOf(flowRecord.getRLevelAmount());
                if (recordList != null && recordList.size() > 0) {
                    List<LfFlowRecord> records = new ArrayList<LfFlowRecord>();
                    for (int i = 1; i <= level; i++) {
                        records.clear();
                        String rtype = "0";
                        for (int k = 0; k < recordList.size(); k++) {
                            LfFlowRecord recordk = recordList.get(k);
                            if (recordk.getRLevel() - 0 == i) {
                                records.add(recordk);
                                if ("0".equals(rtype)) {
                                    rtype = recordk.getRType().toString();
                                }
                            }
                        }
                        if (records.size() == 0) {
                            continue;
                        }
                        String leveltype = "";
                        if (rtype != null && !"".equals(rtype)) {
                            if ("1".equals(rtype)) {
                                leveltype = "[" + MessageUtils.extractMessage("xtgl", "xtgl_spgl_xxsp_czysh", request) + "]";
                            } else if ("4".equals(rtype)) {
                                leveltype = "[" + MessageUtils.extractMessage("xtgl", "xtgl_spgl_xxsp_jgsh", request) + "]";
                            } else if ("5".equals(rtype)) {
                                leveltype = "[" + MessageUtils.extractMessage("xtgl", "xtgl_spgl_xxsp_zjsh", request) + "]";
                            }
                        }

            %>
            <div id="PconfigDiv" class="PconfigDiv">
                <div id="tabContainer1" onclick="changflod(<%=i%>)" class="div_bg div_bd tabContainer1">
                    <label class="jsp_label"> <emp:message key="xtgl_spgl_shlcgl_d" defVal="第"
                                                           fileName="xtgl"/><%=i%><emp:message
                            key="xtgl_spgl_shlcgl_jsp" defVal="级审批" fileName="xtgl"/> </label>
                    <a id="img<%=i%>" class="fold img_a">&nbsp;&nbsp;&nbsp;&nbsp;</a>
                </div>
            </div>
            <div class="itemDiv msgdiv_div" id="msgdiv<%=i%>">
                <table class="msgdiv_table">
                    <%
                        for (int j = 0; j < records.size(); j++) {
                            LfFlowRecord temp = records.get(j);
                    %>
                    <tr>
                        <td align="center" class="div_bd div_bg UserCode_td">
                            <%
                                if (usernameMap.get(temp.getUserCode()) != null && !"".equals(usernameMap.get(temp.getUserCode()))) {
                                    out.print(usernameMap.get(temp.getUserCode()).replaceAll("<", "&lt;").replaceAll(">", "&gt;"));
                                } else {
                                    out.print("-");
                                }
                            %>
                        </td>
                        <td align="left" class="div_bd RTime_td">
                            <%
                                if (temp.getRTime() != null && !"".equals(temp.getRTime())) {
                                    out.print(df.format(temp.getRTime()));
                                } else {
                                    out.print(df.format("-"));
                                }
                            %>
                        </td>
                    </tr>
                    <tr>
                        <td align="left" class="div_bd Comments_td" colspan="2">
                            <%
                                if (temp.getComments() != null && !"".equals(temp.getComments())) {
                                    out.print(temp.getComments().replaceAll("<", "&lt;").replaceAll(">", "&gt;"));
                                } else {
                                    out.print("");
                                }
                            %>
                        </td>
                    </tr>
                    <%
                        }
                    %>
                </table>
            </div>
            <%
                    }
                }
            %>


            <%
                if (flowRecord.getIsComplete() == 2 && flowRecord.getRState() + 1 == 0) {
            %>

            <div class="itemDiv div_bd dqspr_mh_div">
                <table class="dqspr_mh_table">
                    <tr>
                        <td align="right" class="div_bd div_bg dqspr_mh_td">
                            <emp:message key="xtgl_spgl_xxsp_dqspr_mh" defVal="当前审批人：" fileName="xtgl"/>
                        </td>
                        <td align="left" class="div_bd dqspr_mh_down_td">
                            <%=lfsysuser.getName().replaceAll("<", "&lt;").replaceAll(">", "&gt;")%>
                        </td>
                        <td align="right" class="div_bd div_bg spjb_mh_td">
                            <emp:message key="xtgl_spgl_xxsp_spjb_mh" defVal="审批级别：" fileName="xtgl"/>
                        </td>
                        <td align="left" class="div_bd spjb_mh_down_td">
                            <%=flowRecord.getRLevel() %>/<%=flowRecord.getRLevelAmount() %>
                        </td>
                    </tr>

                    <tr>
                        <td align="right" class="div_bd div_bg spyj_mh_td">
                            <emp:message key="xtgl_spgl_xxsp_spyj_mh" defVal="审批意见：" fileName="xtgl"/>
                        </td>
                        <td align="left" class="div_bd spyj_mh_down_td" colspan="3">
                            <textarea id="content" name="content" class="spyj_mh_down_td_text"></textarea>
                        </td>
                    </tr>
                    <%if (lfmttask.getTimerStatus() - 1 == 0) {%>
                    <tr>
                        <td align="right" class="div_bd div_bg fssh_mh_td">
                            <emp:message key="xtgl_spgl_xxsp_fssh_mh" defVal="发送时间：" fileName="xtgl"/>
                        </td>
                        <td align="left" class="div_bd fssh_mh_down_td" colspan="3">
                            <input type="hidden" id="timerStatus" name="timerStatus" value="1"/>
                            <input type="text" readonly="readonly" value="<%=sdf.format(lfmttask.getTimerTime()) %>"
                                   class="Wdate div_bd timerTime"
                                   onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',minDate:'<%=nowtime %>'})"
                                   id="timerTime" name="timerTime"/>
                        </td>
                    </tr>
                    <%} else {%>
                    <tr class="timerStatus_tr">
                        <td align="center" class="div_bd div_bg timerStatus_td" colspan="4">
                            <input type="hidden" id="timerStatus" name="timerStatus" value="0"/>
                        </td>
                    </tr>
                    <%}%>
                </table>
            </div>


            <%
                }
            %>

            <div class="itemDiv ty_div">
                <%
                    if (flowRecord.getIsComplete() == 2 && flowRecord.getRState() + 1 == 0) {
                %>
                <input type="button" value=" <emp:message key='xtgl_spgl_xxsp_ty' defVal='同意' fileName='xtgl'/> "
                       id="butt"
                       onclick="javascript:submitForm2(<%=flowid %>,1);" class="btnClass5 mr23"/>

                <input type="button" value=" <emp:message key='xtgl_spgl_xxsp_jj' defVal='拒绝' fileName='xtgl'/>"
                       id="butt1"
                       onclick="javascript:submitForm2(<%=flowid %>,2);" class="btnClass6 mr23"/>
                <%
                    }
                %>
                <input type="button" onclick="javascript:updateToList()"
                       value="<emp:message key='xtgl_spgl_shlcgl_fh' defVal='返回 ' fileName='xtgl'/>" class="btnClass6"/>
                <br/>
            </div>

        </form>
    </div>


    <div id="tempView" title="<emp:message key='xtgl_spgl_xxsp_cxnr' defVal='彩信内容' fileName='xtgl'/>" class="tempView">
        <input type="hidden" id="tmmsId" value=""/>
        <input type="hidden" id="commonPath" value="<%=commonPath %>"/>

        <div class="mobile_up_div">
            <div id="mobile" class="mobile">
                <center>

                    <div id="pers" class="pers">

                        <div id="showtime" class="showtime"></div>

                        <div id='chart' class="chart">
                        </div>
                    </div>
                </center>

                <div id="screen" class="screen">
                </div>

                <center>
                    <table>
                        <tr>
                            <td>
                                <label id="pointer" class="pointer"></label>
                                <label id="nextpage" class="nextpage"></label>
                            </td>
                        </tr>
                        <tr align="center">
                            <td>
                                <label id="currentpage" class="currentpage"></label>
                            </td>
                        </tr>
                    </table>
                </center>
            </div>
        </div>
        <div id="inputParamCnt1" class="inputParamCnt1 ">
        </div>
    </div>
</div>
<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/xtgl_<%=langName%>.js"></script>
<script type="text/javascript"
        src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript"
        src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript"
        src="<%=commonPath %>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript"
        src="<%=commonPath %>/common/js/mmsPreview.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript"
        src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript"
        src="<%=commonPath%>/common/js/checkFile.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=iPath%>/js/examineMsg.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script>
    var serverName = "<%=serverName%>";
</script>
</body>
</html>
