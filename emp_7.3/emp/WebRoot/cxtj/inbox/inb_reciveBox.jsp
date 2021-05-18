<%@ page language="java" import="com.montnets.emp.common.constant.CommonVariables" pageEncoding="utf-8" %>
<%@page import="com.montnets.emp.entity.biztype.LfBusManager" %>
<%@page import="com.montnets.emp.entity.sysuser.LfSysuser" %>
<%@page import="com.montnets.emp.entity.template.LfTemplate" %>
<%@page import="com.montnets.emp.i18n.util.MessageUtils" %>
<%@page import="com.montnets.emp.inbox.vo.LfMotaskVo1" %>
<%@page import="com.montnets.emp.util.PageInfo" %>
<%@page import="java.text.SimpleDateFormat" %>
<%@page import="java.util.LinkedHashMap" %>
<%@page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple" %>
<%@include file="/common/common.jsp" %>

<%
    String path = request.getContextPath();
    String iPath = request.getRequestURI().substring(0, request.getRequestURI().lastIndexOf("/"));
    String inheritPath = iPath.substring(0, iPath.lastIndexOf("/"));
    String commonPath = inheritPath.substring(0, inheritPath.lastIndexOf("/"));

    String langName = (String) session.getAttribute(StaticValue.LANG_KEY);
    PageInfo pageInfo = new PageInfo();
    pageInfo = (PageInfo) request.getAttribute("pageInfo");

    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @SuppressWarnings("unchecked")
    List<String> userList = (List<String>) request.getAttribute("sendUserList");

    @SuppressWarnings("unchecked")
    Map<String, String> btnMap = (Map<String, String>) session.getAttribute("btnMap");//按钮权限Map

    LfSysuser currUser = (LfSysuser) request.getAttribute("currUser");
    boolean isFirstEnter = (Boolean) request.getAttribute("isFirstEnter");

    @SuppressWarnings("unchecked")
    Map<String, String> titleMap = (Map<String, String>) session.getAttribute("titleMap");
    String menuCode = titleMap.get("reciveBox");
    menuCode = menuCode == null ? "0-0-0" : menuCode;

    @SuppressWarnings("unchecked")
    List<LfMotaskVo1> moTaskList = (List<LfMotaskVo1>) request
            .getAttribute("moTaskList");

    String spUser = request.getParameter("spUser");
    String spgate = request.getParameter("spgate");
    String startTime = request.getParameter("sendtime");
    String endTime = request.getParameter("recvtime");
    String phone = request.getParameter("phone");
    String addrbookname = request.getParameter("addrbookname");
    String msgContent = request.getParameter("msgContent");

    String findResult = (String) request.getAttribute("findresult");

    //部门map
    @SuppressWarnings("unchecked")
    LinkedHashMap<Long, String> lfdepMap = (LinkedHashMap<Long, String>) request.getAttribute("lfdepMap");

    @SuppressWarnings("unchecked")
    LinkedHashMap<Long, String> phoneNameMap = (LinkedHashMap<Long, String>) request.getAttribute("phoneNameMap");

    @SuppressWarnings("unchecked")
    List<LfTemplate> tmpList = (List<LfTemplate>) request.getAttribute("tmpList");//获取短信模板
    @SuppressWarnings("unchecked")
    List<LfBusManager> busList = (List<LfBusManager>) request.getAttribute("busList");
    CommonVariables CV = new CommonVariables();
    String skin = session.getAttribute("stlyeSkin") == null ? "default" : (String) session.getAttribute("stlyeSkin");
    String specialChar = StaticValue.getSmscontentSpecialcharStr();

    //操作员
    String czy = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_grsjx_czy", request);
    //机构
    String jg = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_grsjx_jg", request);
    //通道号码
    String tdhm = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_xtsxjl_tdhm", request);
    if (tdhm != null && tdhm.length() > 1) {
        tdhm = tdhm.substring(0, tdhm.length() - 1);
    }
    //手机号码
    String sjhm = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_xtsxjl_sjhm", request);
    if (sjhm != null && sjhm.length() > 1) {
        sjhm = sjhm.substring(0, sjhm.length() - 1);
    }
    //姓名
    String xm = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_xtsxjl_xm", request);
    if (xm != null && xm.length() > 1) {
        xm = xm.substring(0, xm.length() - 1);
    }
    //接收时间
    String jssj = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_xtsxjl_jssj", request);
    if (jssj != null && jssj.length() > 1) {
        jssj = jssj.substring(0, jssj.length() - 1);
    }
    //短信内容
    String dxnr = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_xtsxjl_dxnr", request);
    if (dxnr != null && dxnr.length() > 1) {
        dxnr = dxnr.substring(0, dxnr.length() - 1);
    }
    //编码
    String bm = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_xtsxjl_bm", request);
    //操作
    String cz = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_grsjx_cz", request);
    //回复内容
    String hfnr = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_grsjx_hfnr", request);
    //发送
    String fs = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_grsjx_fs", request);
    //取消
    String qx = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_grsjx_qx", request);
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
<head>
    <title>个人收件箱</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <link href="<%=commonPath %>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet"
          type="text/css"/>
    <link href="<%=commonPath %>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet"
          type="text/css"/>
    <link href="<%=skin%>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
    <link href="<%=skin%>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
    <%if (StaticValue.ZH_HK.equals(langName)) {%>
    <link rel="stylesheet" type="text/css"
          href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
    <link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
    <%}%>
    <link rel="stylesheet"
          href="<%=commonPath %>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>"/>
    <link rel="stylesheet" href="<%=skin%>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css">
    <link href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet"
          type="text/css"/>
    <link href="<%=commonPath%>/cxtj/common/css/inbox.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet"
          type="text/css"/>
    <script>
        var specialChar = "<%=specialChar%>";
        var special = specialChar.split(",");
    </script>
</head>
<body class="inb_reciveBox">
<div id="container" class="container">
    <%-- 当前位置 --%>
    <%=com.montnets.emp.common.constant.ViewParams.getPosition(langName, menuCode) %>

    <input type="hidden" id="gt1" name="gt1" value=""/>
    <input type="hidden" id="gt2" name="gt2" value=""/>
    <input type="hidden" id="gt3" name="gt3" value=""/>
    <input type="hidden" id="gt4" name="gt4" value=""/>
    <input type="hidden" value="" id="inputContent" name="inputContent"/><%--临时存储手工输入内容 --%>
    <%-- <div id="replayCon" title="回复内容" style="padding:5px;display:none">--%>
    <div id="replayCon" title="<%=hfnr %>">
        <center>
            <table>
                <tr height="10px;">
                    <td colspan="2"></td>
                </tr>
                <tr>
                    <td><emp:message key="cxtj_sjcx_grsjx_dxmb" defVal="短信模板：" fileName="cxtj"></emp:message></td>
                    <td align="left">
                        <select id="tempSelect" onchange="getTempMsg($(this))">
                            <option value=""><emp:message key="cxtj_sjcx_grsjx_qxz" defVal="请选择"
                                                          fileName="cxtj"></emp:message></option>
                            <%
                                if (tmpList != null && tmpList.size() > 0) {
                                    for (LfTemplate temp : tmpList) {
                                        if (null != temp.getTmid() && null != temp.getTmName()) {
                                            out.print("<option value='" + temp.getTmid() + "'>" + temp.getTmName().replace("<", "&lt;").replace(">", "&gt;") + "(ID:" + temp.getTmid() + ")</option>");
                                        }
                                    }
                                }
                            %>
                        </select>
                        <emp:message key="cxtj_sjcx_grsjx_fdqm" defVal="附带签名：" fileName="cxtj"></emp:message><input
                            type="checkbox" value="" name="isSign" id="isSign" onclick="setSign(this)"/>&nbsp;&nbsp;&nbsp;
                        <input type="text" value="" name="signStr" id="signStr" onmouseover="this.title=this.value"
                               readonly="readonly" onfocus="this.blur()"/>

                    </td>
                </tr>
                <tr height="4px;">
                    <td colspan="2"></td>
                </tr>
                <tr>
                    <td><emp:message key="cxtj_sjcx_grsjx_hfnrr" defVal="回复内容：" fileName="cxtj"></emp:message></td>
                    <td>
					<textarea name="msg" rows="6"
                              id="msg" onblur="eblur($(this))"></textarea>
                    </td>
                </tr>
                <tr>
                    <td></td>
                    <td align="left">
                        <label class="zhu">
                            <emp:message key="cxtj_sjcx_grsjx_xxcd" defVal="信息长度：" fileName="cxtj"></emp:message>
                            <font id="strlen" class="boldFont"> 0 </font><font id="maxLen" class="boldFont">/990</font>
                            ，<emp:message key="cxtj_sjcx_grsjx_xxcd" defVal="信息分条" fileName="cxtj"></emp:message>
                            (<emp:message key="cxtj_sjcx_grsjx_ydd" defVal="移动：" fileName="cxtj"></emp:message><font
                                id="ft1" class="boldFont">0</font>
                            <emp:message key="cxtj_sjcx_grsjx_ltt" defVal="联通：" fileName="cxtj"></emp:message><font
                                id="ft2" class="boldFont">0</font>
                            <emp:message key="cxtj_sjcx_grsjx_dxx" defVal="电信：" fileName="cxtj"></emp:message><font
                                id="ft3" class="boldFont">0</font>
                            <emp:message key="cxtj_sjcx_grsjx_gww" defVal="国外：" fileName="cxtj"></emp:message><font
                                id="ft4" class="boldFont">0</font>)
                        </label>
                    </td>
                </tr>
                <tr height="4px;">
                    <td colspan="2"></td>
                </tr>
                <%-- <tr>
                 <td>业务类型：</td>
                 <td align="left">
                  <select id="busCode"  name="busCode" style="width:120px">
                    <%
                        if (busList != null && busList.size() > 0) {
                            for (LfBusManager busManager : busList) {
                    %>
                    <option value="<%=busManager.getBusCode()%>">
                        <%=busManager.getBusName().replace("<","&lt;").replace(">","&gt;")%>
                    </option>
                    <%
                        }
                        }
                    %>
                  </select>
                 <font color="red" style="margin-left:10px">*</font>
                <span style="display:inline">发送级别：</span>
                <p style="display: inline;width:140px;">
                <label>
                    <select id="priority" name="priority" style="width:115px">
                    <option value="0">系统智能控制</option>
                    <option value="1">1(优先级最高)</option>
                    <option value="2">2</option>
                    <option value="3">3</option>
                    <option value="4">4</option>
                    <option value="5">5</option>
                    <option value="6">6</option>
                    <option value="7">7</option>
                    <option value="8">8</option>
                    <option value="9">9(优先级最低)</option>
                    </select>
                </label>
                <font color="red" style="margin-left:10px">*</font>
                </p>
                 </td>
                </tr> --%>
            </table>
            <div class="doOkDiv">
                <center>
                    <input id="ok" class="btnClass5 mr23" type="button" value="<%=fs %>" onclick="dook()" />
                    <input id="sc" onclick="docancel();" class="btnClass6" type="button" value="<%=qx %>" />
                </center>
            </div>
        </center>
    </div>
    <%-- 内容开始 --%>
    <%if (btnMap.get(menuCode + "-0") != null) { %>
    <div id="rContent" class="rContent">
        <form name="pageForm" action="inb_reciveBox.htm" method="post" id="pageForm">
            <div class="buttons">
                <div id="toggleDiv">
                </div>
                <a id="exportCondition"><emp:message key="cxtj_sjcx_xtsxjl_dc" defVal="导出"
                                                     fileName="cxtj"></emp:message></a>
            </div>
            <div id="condition">
                <table>
                    <tbody>
                    <tr>
                        <td>
                            <emp:message key="cxtj_sjcx_xtsxjl_spzh" defVal="sp账号" fileName="cxtj"/>
                        </td>
                        <td>
                            <label>
                                <select id="spUser" name="spUser">
                                    <option value="">
                                        <emp:message key="cxtj_sjcx_xtxxjl_qb" defVal="全部"
                                                     fileName="cxtj"></emp:message>
                                    </option>
                                    <%
                                        if (userList != null && userList.size() > 0) {
                                            for (String userdata : userList) {
                                    %>
                                    <option value="<%=userdata %>"
                                            <%=userdata.equals(spUser) ? "selected" : "" %>>
                                        <%=userdata %>
                                    </option>
                                    <%
                                            }
                                        }
                                    %>
                                </select>
                            </label>
                        </td>
                        <%--									<td>--%>
                        <%--										通道号码：--%>
                        <%--									</td>--%>
                        <%--									<td>--%>
                        <%--										<label>--%>
                        <%--											<select name="spgate" style="width:182px">--%>
                        <%--												<option value="">--%>
                        <%--													全部--%>
                        <%--												</option>--%>
                        <%--													<%--%>
                        <%--														if (spList != null && spList.size() > 0)--%>
                        <%--														{--%>
                        <%--															for(int i=0;i<spList.size();i++)--%>
                        <%--															{--%>
                        <%--													%>--%>
                        <%--												<option value="<%=spList.get(i).getSpgate()%>"--%>
                        <%--													<%=spList.get(i).getSpgate().equals(spgate)?"selected":""%>>--%>
                        <%--													<%=spList.get(i).getSpgate() %>--%>
                        <%--												</option>--%>
                        <%--											<%		}--%>
                        <%--												}--%>
                        <%--											%>--%>
                        <%--											</select>--%>
                        <%--										</label>--%>
                        <%--									</td>--%>
                        <td>
                            <emp:message key="cxtj_sjcx_xtsxjl_sjhm" defVal="手机号码：" fileName="cxtj"></emp:message>

                        </td>
                        <td>
                            <%
                                phone = phone == null ? "" : phone;
                                //if(phone!=null&&!"".equals(phone)&&btnMap.get(StaticValue.PHONE_LOOK_CODE)==null){
                                //	phone=phone.substring(0, 3)+"****"+phone.substring(7, phone.length());
                                //}

                            %>
                            <input type="text" value='<%=phone %>' id="phone" name="phone" maxlength="21"
                                   onkeyup="phoneInputCtrl($(this))"/>
                        </td>
                        <td class="tdSer">
                            <center><a id="search"></a></center>
                        </td>
                    </tr>

                    <tr>
                        <td>
                            <emp:message key="cxtj_sjcx_xtsxjl_xm" defVal="姓名：" fileName="cxtj"></emp:message>
                        </td>
                        <td>

                            <input type="text" value='<%=addrbookname==null?"":addrbookname %>' id="addrbookname"
                                   name="addrbookname"/>
                        </td>
                        <td>
                            <emp:message key="cxtj_sjcx_xtsxjl_dxnr" defVal="短信内容：" fileName="cxtj"></emp:message>
                        </td>
                        <td>
                            <input type="text" value="<%=msgContent==null?"":msgContent %>" id="msgContent"
                                   name="msgContent"/>
                        </td>
                        <td colspan=1></td>
                    </tr>
                    <tr>
                        <td>
                            <emp:message key="cxtj_sjcx_xtsxjl_jssj" defVal="接收时间：" fileName="cxtj"></emp:message>
                        </td>
                        <td class="tableTime">
                            <input type="text" class="Wdate" readonly="readonly" onclick="stime()"
                                   value="<%=startTime==null?"":startTime %>" id="sendtime" name="sendtime">
                        </td>
                        <td>
                            <emp:message key="cxtj_sjcx_xtsxjl_z" defVal="至：" fileName="cxtj"></emp:message>
                        </td>
                        <td>
                            <input type="text" class="Wdate" readonly="readonly" onclick="rtime()"
                                   value="<%=endTime==null?"":endTime %>" id="recvtime" name="recvtime">
                        </td>
                        <td>&nbsp;</td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <table id="content">
                <thead>
                <tr>
                    <th><%=czy%>
                    </th>
                    <th><%=jg%>
                    </th>
                    <th><emp:message key="cxtj_sjcx_report_spzh" defVal="sp账号" fileName="cxtj"/></th>
                    <th><%=tdhm%>
                    </th>
                    <th><%=sjhm%>
                    </th>
                    <th><%=xm%>
                    </th>
                    <th><%=jssj%>
                    </th>
                    <th><%=dxnr%>
                    </th>
                    <th><%=bm%>
                    </th>
                    <th><%=cz%>
                    </th>
                </tr>
                </thead>
                <tbody>
                <%if (isFirstEnter) { %>
                <tr>
                    <td colspan="10" class="queryData"><emp:message key="cxtj_sjcx_xtsxjl_qdjcxhqsj" defVal="请点击查询获取数据"
                                                                    fileName="cxtj"></emp:message></td>
                </tr>
                <%
                } else {
                    if (moTaskList != null && moTaskList.size() > 0) {
                        for (int i = 0; i < moTaskList.size(); i++) {
                            LfMotaskVo1 lfmotask = moTaskList.get(i);
                %>
                <tr>
                    <td class="textalign">
                        <%=lfmotask.getName() %>
                    </td>
                    <td class="textalign">
                        <%=lfdepMap == null ? "-" : lfdepMap.get(lfmotask.getSysdepId())%>
                    </td>
                    <td>
                        <%=lfmotask.getSpUser() %>
                    </td>
                    <td>
                        <%=lfmotask.getSpnumber() %>
                    </td>
                    <td>
                        <%
                            String phones = CV.replacePhoneNumber(btnMap, lfmotask.getPhone());
                            out.print(phones);
                        %>
                    </td>
                    <td>
                        <%
                            if (lfmotask.getEmployeeName() != null && !"".equals(lfmotask.getEmployeeName())) {
                                out.print(lfmotask.getEmployeeName());
                            } else if (phoneNameMap != null && phoneNameMap.size() > 0) {
                                String str = phoneNameMap.get(lfmotask.getPhone());
                                if (str != null && !"".equals(str)) {
                                    out.print(str);
                                } else {
                                    out.print("-");
                                }
                            } else {
                                out.print("-");
                            }
                        %>
                    </td>
                    <td>
                        <%=df.format(lfmotask.getDeliverTime()) %>
                    </td>
                    <td class="msgContentTd">
                        <a onclick=modify(this)>
                            <label style="display:none">
                                <xmp><%=lfmotask.getMsgContent()%>
                                </xmp>
                            </label>
                            <%=lfmotask.getMsgContent().length() > 5 ? (lfmotask.getMsgContent().replace("<", "&lt;").replace(">", "&gt;").substring(0, 5) + "...") : (lfmotask.getMsgContent().replace("<", "&lt;").replace(">", "&gt;"))%>
                        </a>
                    </td>
                    <td>
                        <%=lfmotask.getMsgFmt() == null ? "-" : lfmotask.getMsgFmt() %>
                    </td>
                    <td>
                        <%if (currUser != null && currUser.getUserId().equals(lfmotask.getUserId())) {%>
                        <a onclick="ksreplay(<%=lfmotask.getMoId() %>,'<%=lfmotask.getSpUser() %>')"><emp:message
                                key="cxtj_sjcx_grsjx_kjhf" defVal="快捷回复" fileName="cxtj"></emp:message></a>
                        <%} else { %>
                        -
                        <%} %>
                    </td>
                </tr>
                <%
                    }
                } else {
                %>
                <tr>
                    <td colspan="10" class="queryData"><emp:message key="cxtj_sjcx_xtxxjl_wjl" defVal="无记录"
                                                                    fileName="cxtj"></emp:message></td>
                </tr>
                <%
                        }
                    }
                %>
                </tbody>
                <tfoot>
                <tr>
                    <td colspan="10">
                        <div id="pageInfo"></div>
                    </td>
                </tr>
                </tfoot>
            </table>
            <div id="r_reciveBoxparams" class="hidden"></div>
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

    LoginInfo("r_reciveBoxparams");
</script>
<script type="text/javascript"
        src="<%=commonPath %>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript"
        src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript"
        src="<%=commonPath %>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript"
        src="<%=commonPath %>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript"
        src="<%=commonPath %>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"
        type="text/javascript"></script>
<script type="text/javascript"
        src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/cxtj_<%=langName%>.js"></script>
<script type="text/javascript">
    $(document).ready(function () {
        //getLoginInfo("#r_reciveBoxparams");
        var findresult = "<%=findResult%>";
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

        $("#addrbookname,#msgContent").live('keyup blur', function () {
            var value = $(this).val();
            if (value != filterString(value)) {
                $(this).val(filterString(value));
            }
        });
        $('#spUser').isSearchSelect({'width': '179', 'isInput': true, 'zindex': 0});
        //导出全部数据到excel
        $("#exportCondition").click(
            function () {
                // if(confirm("确定要导出数据到excel?")){
                if (confirm(getJsLocaleMessage("cxtj", "cxtj_sjcx_xtsxjl_text_2"))) {
                    <%
                      if(moTaskList!=null && moTaskList.size()>0){
                      %>
                    var sendtime = '<%=startTime%>';
                    var recvtime = '<%=endTime%>';
                    var phone = '<%=phone%>';
                    var addrbookname = '<%=addrbookname%>';
                    var msgContent = '<%=msgContent%>';
                    addrbookname = addrbookname;
                    msgContent = msgContent;
                    var spUser = '<%=spUser%>';
                    var lgcorpcode = $("#lgcorpcode").val();
                    var lguserid = $("#lguserid").val();
                    var lgguid = $("#lgguid").val();
                    $.ajax({
                        type: "POST",
                        url: "<%=path%>/inb_reciveBox.htm?method=exportCurrPageExcel",
                        data: {
                            sendtime: sendtime,
                            recvtime: recvtime,
                            phone: phone,
                            spUser: spUser,
                            pageIndex:<%=pageInfo.getPageIndex()%>,
                            pageSize:<%=pageInfo.getPageSize()%>,
                            lgcorpcode: lgcorpcode,
                            lgguid: lgguid,
                            lguserid: lguserid,
                            addrbookname: addrbookname,
                            msgContent: msgContent
                        },
                        beforeSend: function () {
                            page_loading();
                        },
                        complete: function () {
                            page_complete()
                        },
                        success: function (result) {
                            if (result == 'true') {
                                download_href("<%=path%>/inb_reciveBox.htm?method=downloadFile");
                            } else {
                                //alert('导出失败！');
                                alert(getJsLocaleMessage("cxtj", "cxtj_sjcx_xtsxjl_text_3"));
                            }
                        }
                    });
                    //location.href="<%=path%>/inb_reciveBox.htm?method=exportCurrPageExcel&startTime="+startTime+"&endTime="+endTime+"&phone="+phone+"&spUser="+spUser+"&pageIndex="+<%=pageInfo.getPageIndex()%>+"&pageSize="+<%=pageInfo.getPageSize()%>+"&lgcorpcode="+lgcorpcode+"&lgguid="+lgguid+"&lguserid="+lguserid+"&addrbookname="+addrbookname+"&msgContent="+msgContent;
                    <%
                    }else{
                    %>
                    alert(getJsLocaleMessage("cxtj", "cxtj_sjcx_xtsxjl_text_4"));
                    <%
                    }
                    %>

                }
            });

        initPage(<%=pageInfo.getTotalPage()%>, <%=pageInfo.getPageIndex()%>, <%=pageInfo.getPageSize()%>, <%=pageInfo.getTotalRec()%>);

        $('#search').click(function () {
            submitForm();
        });
        if (findresult == "-1") {
            //alert("加载页面失败,请检查网络是否正常!");
            alert(getJsLocaleMessage("cxtj", "cxtj_sjcx_xtsxjl_text_1"));
            return;
        }
        synlen();
    });


    //附带签名
    function setSign(isSign) {
        if (isSign.checked) {
            $("#signStr").css("display", "inline");
            $("#signStr").css("background-color", "#E8E8E8");
            $("#signStr").val("(<%=currUser!=null?currUser.getName()!=null?currUser.getName():"":"" %>)");
        } else {
            $("#signStr").css("display", "none");
            $("#signStr").css("background-color", "");
            $("#signStr").val("");
        }
        eblur($("#msg"));
    }

</script>
<script type="text/javascript" src="<%=iPath %>/js/inb_reciveBox.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
</body>
</html>
<%
    phoneNameMap.clear();
    phoneNameMap = null;
    lfdepMap.clear();
    lfdepMap = null;
%>
