<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%@page import="com.montnets.emp.util.PageInfo" %>
<%@page import="com.montnets.emp.monitor.constant.MonitorStaticValue" %>
<%@page import="com.montnets.emp.common.constant.StaticValue" %>
<%@page import="org.apache.commons.lang.StringUtils" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple" %>
<%
    String path = request.getContextPath();
    String langName = (String) session.getAttribute(StaticValue.LANG_KEY);
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    String iPath = request.getRequestURI().substring(0, request.getRequestURI().lastIndexOf("/"));
    String inheritPath = iPath.substring(0, iPath.lastIndexOf("/"));
    String commonPath = inheritPath.substring(0, inheritPath.lastIndexOf("/"));

    java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @SuppressWarnings("unchecked")
    Map<String, String> btnMap = (Map<String, String>) session.getAttribute("btnMap");//按钮权限Map
    @SuppressWarnings("unchecked")
    Map<String, String> titleMap = (Map<String, String>) session.getAttribute("titleMap");
    String menuCode = titleMap.get("spAcctMon");
    menuCode = menuCode == null ? "0-0-0" : menuCode;

    PageInfo pageInfo = new PageInfo();
    pageInfo = (PageInfo) request.getAttribute("pageInfo");

    String skin = session.getAttribute("stlyeSkin") == null ?
            "default" : (String) session.getAttribute("stlyeSkin");

    String spaccountid = StringUtils.defaultIfEmpty(request.getParameter("spaccountid"), "");
    String spaccountType = StringUtils.defaultIfEmpty(request.getParameter("spaccountType"), "");
    String accountname = StringUtils.defaultIfEmpty(request.getParameter("accountname"), "");
    String mtremained = StringUtils.defaultIfEmpty(request.getParameter("mtremained"), "");
    String moremained = StringUtils.defaultIfEmpty(request.getParameter("moremained"), "");
    String rptremained = StringUtils.defaultIfEmpty(request.getParameter("rptremained"), "");
    String txtPage = request.getParameter("pageIndex");
    String pageSize = request.getParameter("pageSize");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
<head>

    <title>My JSP 'mon_index.jsp' starting page</title>
    <%@include file="/common/common.jsp" %>
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">
    <link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet"
          type="text/css"/>
    <link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet"
          type="text/css"/>
    <link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css"/>
    <link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css"/>
    <link rel="stylesheet"
          href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion()%>"/>
    <link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion()%>" type="text/css">
    <link rel="stylesheet" type="text/css"
          href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion()%>"/>
    <style type="text/css">
        #moreDes span {
            white-space: nowrap;
            width: 250px;
            overflow: hidden;
            -o-text-overflow: ellipsis; /* Opera */
            text-overflow: ellipsis; /* IE, Safari (WebKit) */
        }

        .c_selectBox {
            width: 208px !important;
        }

        .c_selectBox ul {
            width: 208px !important;
        }

        .c_selectBox ul li {
            width: 208px !important;
        }
    </style>
    <%if (StaticValue.ZH_HK.equals(langName)) {%>
    <link rel="stylesheet" type="text/css"
          href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
    <link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
    <%}%>
</head>

<body>
<div id="container" class="container">
    <%-- 当前位置 --%>
    <%=com.montnets.emp.common.constant.ViewParams.getPosition(langName, menuCode) %>

    <%-- 内容开始 --%>
    <div id="rContent" class="rContent">
        <%-- 表示请求的名称 --%>
        <form name="pageForm" action="mon_spAcctMon.htm" method="post" id="pageForm">
            <div id="loginInfo" class="hidden"></div>
            <div class="buttons">
                <div id="toggleDiv">
                </div>
                <%--<a id="exportCondition">导出</a>--%>
            </div>
            <div id="condition">
                <table>
                    <tbody>
                    <tr>
                        <td>
                            <emp:message key="ptjk_common_spzh_mh" defVal="SP账号：" fileName="ptjk"/>
                        </td>
                        <td>
                            <select name="tdhm_key" style="width:158px;display:none" id="tdhm_key">
                                <option value=""><emp:message key='ptjk_common_qb' defVal='全部'
                                                              fileName='ptjk'/></option>
                            </select>
                            <input type="hidden" name="spaccountid" id="spaccountid" value="<%=spaccountid %>"/>
                            <input type="hidden" name="spaccountType" id="spaccountType" value="<%=spaccountType %>"/>
                            <%-- 0表示下拉框取值 1表示文本框 --%>
                        </td>
                        <td><emp:message key="ptjk_common_zhmc_mh" defVal="账号名称：" fileName="ptjk"/></td>
                        <td>
                            <input type="text" name="accountname" id="accountname" style="width: 206px" value="<%=accountname %>"/>
                        </td>
                        <td>
                            <emp:message key="ptjk_jkxq_spzh_zhlx_mh" defVal="账号类型：" fileName="ptjk"/>
                        </td>
                        <td>
                            <select id="accounttype" name="accounttype" isInput="false">
                                <option value=""><emp:message key='ptjk_common_qb' defVal='全部'
                                                              fileName='ptjk'/></option>
                                <option value="1"
                                        <%if("1".equals(request.getParameter("accounttype"))){%>selected="selected"<%}%>>
                                    <emp:message key="ptjk_jkxq_spzh_1" defVal="EMP应用帐号" fileName="ptjk"/></option>
                                <option value="2"
                                        <%if("2".equals(request.getParameter("accounttype"))){%>selected="selected"<%}%>>
                                    <emp:message key="ptjk_jkxq_spzh_2" defVal="EMP接入账号" fileName="ptjk"/></option>
                                <option value="3"
                                        <%if("3".equals(request.getParameter("accounttype"))){%>selected="selected"<%}%>>
                                    <emp:message key="ptjk_jkxq_spzh_zlzh" defVal="直连账号" fileName="ptjk"/></option>
                            </select>
                        </td>
                        <td class="tdSer">
                            <center><a id="search"></a></center>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <emp:message key="ptjk_common_gjjb_mh" defVal="告警级别：" fileName="ptjk"/>
                        </td>
                        <td>
                            <select id="evttype" name="evttype" isInput="false">
                                <option value=""><emp:message key='ptjk_common_qb' defVal='全部'
                                                              fileName='ptjk'/></option>
                                <option value="0"
                                        <%if("0".equals(request.getParameter("evttype"))){%>selected="selected"<%}%>>
                                    <emp:message key="ptjk_common_zc" defVal="正常" fileName="ptjk"/></option>
                                <option value="1"
                                        <%if("1".equals(request.getParameter("evttype"))){%>selected="selected"<%}%>>
                                    <emp:message key="ptjk_common_jg" defVal="警告" fileName="ptjk"/></option>
                                <option value="2"
                                        <%if("2".equals(request.getParameter("evttype"))){%>selected="selected"<%}%>>
                                    <emp:message key="ptjk_common_yz" defVal="严重" fileName="ptjk"/></option>
                            </select>
                        </td>
                        <td>
                            <emp:message key="ptjk_jkxq_tdzh_4" defVal="MT待发：" fileName="ptjk"/>
                        </td>
                        <td>
                            <input type="text" name="mtremained" id="mtremained" style="width: 206px" class="dis" value="<%=mtremained %>"/>
                        </td>
                        <td>
                            <emp:message key="ptjk_jkxq_tdzh_5" defVal="MO滞留：" fileName="ptjk"/>
                        </td>
                        <td>
                            <input type="text" name="moremained" style="width: 206px" id="moremained" class="dis" value="<%=moremained %>"/>
                        </td>

                        <td>
                        </td>
                    </tr>
                    <tr>
                        <td><emp:message key="ptjk_jkxq_tdzh_6" defVal="RPT滞留：" fileName="ptjk"/></td>
                        <td>
                            <input type="text" name="rptremained" id="rptremained" class="dis" style="width: 206px;"
                                   value="<%=rptremained %>"/>
                        </td>
                        <td><emp:message key="ptjk_jkxq_spzh_dllx_mh" defVal="登录类型：" fileName="ptjk"/></td>
                        <td>
                            <select id="logintype" name="logintype" isInput="false">
                                <option value=""><emp:message key='ptjk_common_qb' defVal='全部'
                                                              fileName='ptjk'/></option>
                                <option value="1"
                                        <%if("1".equals(request.getParameter("logintype"))){%>selected="selected"<%}%>>
                                    <emp:message key="ptjk_jkxq_spzh_wbsdl" defVal="WBS登录" fileName="ptjk"/></option>
                                <option value="2"
                                        <%if("2".equals(request.getParameter("logintype"))){%>selected="selected"<%}%>>
                                    <emp:message key="ptjk_jkxq_spzh_zldl" defVal="直连登录" fileName="ptjk"/></option>
                                <option value="0"
                                        <%if("0".equals(request.getParameter("logintype"))){%>selected="selected"<%}%>>
                                    <emp:message key="ptjk_common_wz" defVal="未知" fileName="ptjk"/></option>
                            </select>
                        </td>
                        <td><emp:message key="ptjk_common_zhzt_mh" defVal="账号状态：" fileName="ptjk"/></td>
                        <td>
                            <select id="onlinestatus" name="onlinestatus" isInput="false">
                                <option value=""><emp:message key='ptjk_common_qb' defVal='全部'
                                                              fileName='ptjk'/></option>
                                <option value="0"
                                        <%if("0".equals(request.getParameter("onlinestatus"))){%>selected="selected"<%}%>>
                                    <emp:message key="ptjk_common_zx" defVal="在线" fileName="ptjk"/></option>
                                <option value="1"
                                        <%if("1".equals(request.getParameter("onlinestatus"))){%>selected="selected"<%}%>>
                                    <emp:message key="ptjk_common_lx" defVal="离线" fileName="ptjk"/></option>
                            </select>
                        </td>
                        <td>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <div id="info"></div>
        </form>
    </div>

    <%-- 内容结束 --%>

    <div id="moreDes" title="<emp:message key='ptjk_jkxq_tdzh_gdxq' defVal='更多详情' fileName='ptjk'/>"
         style="display:none;line-height: 200%;">
        <span label='spaccountid'><emp:message key="ptjk_common_spzh_mh" defVal="SP账号：" fileName="ptjk"/></span><span
            label='accountName' style="left:300px;"><emp:message key="ptjk_common_zhmc_mh" defVal="账号名称："
                                                                 fileName="ptjk"/></span><br/>
        <span label='spAccountType'><emp:message key="ptjk_jkxq_spzh_zhlx_mh" defVal="账号类型："
                                                 fileName="ptjk"/></span><span label='loginip'
                                                                               style="left:300px;"> <emp:message
            key="ptjk_jkxq_tdzh_7" defVal="登录IP：" fileName="ptjk"/></span><br/>
        <span label='onlinestatusStr'><emp:message key="ptjk_common_zhzt_mh" defVal="账号状态："
                                                   fileName="ptjk"/></span><span label='submitStatusStr'
                                                                                 style="left:300px;"> <emp:message
            key="ptjk_jkxq_spzh_tjzt_mh" defVal="提交状态：" fileName="ptjk"/></span><br/>
        <span label='mtsndspd' class='speed'><emp:message key="ptjk_jkxq_tdzh_11" defVal="MT提交速度："
                                                          fileName="ptjk"/></span><span label='mtissuedspd'
                                                                                        class='speed'
                                                                                        style="left:300px;"><emp:message
            key="ptjk_jkxq_spzh_3" defVal="MT下发速度：" fileName="ptjk"/></span><br/>
        <span label='mtTotalSnd' class='count'><emp:message key="ptjk_jkxq_tdzh_10" defVal="MT提交总量："
                                                            fileName="ptjk"/></span><span label='mthavesnd'
                                                                                          class='count'
                                                                                          style="left:300px;"><emp:message
            key="ptjk_jkxq_tdzh_9" defVal="MT已转发量：" fileName="ptjk"/></span><br/>
        <span label='mtremained' class='count'><emp:message key="ptjk_jkxq_tdzh_12" defVal="MT待发量："
                                                            fileName="ptjk"/></span><span label='mohavesnd'
                                                                                          class='count'
                                                                                          style="left:300px;"> <emp:message
            key="ptjk_jkxq_spzh_4" defVal="MO转发量：" fileName="ptjk"/></span><br/>
        <span label='mototalrecv' class='count'><emp:message key="ptjk_jkxq_tdzh_14" defVal="MO接收总量："
                                                             fileName="ptjk"/></span><span label='mosndspd'
                                                                                           class='speed'
                                                                                           style="left:300px;"><emp:message
            key="ptjk_jkxq_tdzh_15" defVal="MO转发速度：" fileName="ptjk"/></span><br/>
        <span label='moremained' class='count'><emp:message key="ptjk_jkxq_tdzh_16" defVal="MO滞留量："
                                                            fileName="ptjk"/></span><span label='rpttotalrecv'
                                                                                          class='count'
                                                                                          style="left:300px;"><emp:message
            key="ptjk_jkxq_tdzh_18" defVal="RPT接收总量：" fileName="ptjk"/></span><br/>
        <span label='rptHaveSnd' class='count'><emp:message key="ptjk_jkxq_tdzh_17" defVal="RPT已转发量："
                                                            fileName="ptjk"/></span><span label='rptsndspd'
                                                                                          style="left:300px;"
                                                                                          class='speed'><emp:message
            key="ptjk_jkxq_spzh_5" defVal="RPT接收速度：" fileName="ptjk"/></span><br/>
        <span label='rptremained' class='count'><emp:message key="ptjk_jkxq_tdzh_20" defVal="RPT滞留量："
                                                             fileName="ptjk"/></span><span label='updatetime'
                                                                                           style="left:300px;"
                                                                                           class="time"> <emp:message
            key="ptjk_common_gxsj_mh" defVal="更新时间：" fileName="ptjk"/></span>
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
<div class="clear"></div>
<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/ptjk_<%=langName%>.js"></script>
<script type="text/javascript"
        src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
<script type="text/javascript"
        src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
<script type="text/javascript"
        src="<%=commonPath%>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
<script type="text/javascript"
        src="<%=commonPath %>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
<script type="text/javascript"
        src="<%=commonPath%>/common/js/monPageInfo.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
<script src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript"></script>
<script src="<%=iPath%>/js/dis.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript"></script>
<script src="<%=iPath%>/js/jquery.qtip-myjquery-q.js?V=<%=StaticValue.getJspImpVersion()%>"
        type="text/javascript"></script>
<script type="text/javascript"
        src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
<script type="text/javascript">
    var count = 1;
    $(document).ready(function () {
        getLoginInfo("#loginInfo");
        $('#search').click(function () {
            //点击查询显示第一页
            $("#txtPage").val('1');
            submitForm();
        });
        //定时刷新时间
        refreshTime = <%=MonitorStaticValue.getRefreshTime()%>;
        //window.clearInterval();
        //定时刷新
        reTimer = window.setInterval("submitForm()", refreshTime);

        $("#evttype,#tdhm_key,#accounttype,#logintype,#onlinestatus").isSearchSelect({
            'width': '152',
            'isInput': false,
            'zindex': 0
        });
        submitForm();
        setInterval("refresh()", 15 * 60 * 1000);
    });

    function submitForm() {
        count++;
        var search = document.getElementById('search');
        if (search) {
            search.isClick = true;
        }
        window.parent.loading();

        $("#p_jump_menu").remove();
        var time = new Date();
        var evttype = $("#evttype").val();
        var accountname = $("#accountname").val();
        var spaccountid = $("#spaccountid").val();
        var spaccountType = $("#spaccountType").val();
        var mtremained = $("#mtremained").val();
        var moremained = $("#moremained").val();
        var rptremained = $("#rptremained").val();
        var lguserid = $("#lguserid").val();
        var lgcorpcode = $("#lgcorpcode").val();
        var accounttype = $("#accounttype").val();
        var logintype = $("#logintype").val();
        var onlinestatus = $("#onlinestatus").val();
        $('#info').load("mon_spAcctMon.htm", {
            method: 'getInfo',
            lgcorpcode: lgcorpcode,
            lguserid: lguserid,
            accountname: accountname,
            spaccountid: spaccountid,
            spaccountType: spaccountType,
            evttype: evttype,
            mtremained: mtremained,
            moremained: moremained,
            rptremained: rptremained,
            accounttype: accounttype,
            logintype: logintype,
            onlinestatus: onlinestatus,
            pageIndex: $("#txtPage").val() ||<%=txtPage%>,
            pageSize: $("#pageSize").val() ||<%=pageSize%>,
            time: time
        }, function () {
        });
        //加载账号数据
        $.ajax({
            type: "post",
            url: "mon_spAcctMon.htm?method=getSpAccts",
            async: false,
            success: function (data) {
                data = eval('(' + data + ')');
                $("#tdhm_key option:gt(0)").remove();
                for (var i = 0; i < data.length; i++) {
                    var value = data[i].spAcct;
                    $("#tdhm_key").append("<option value='" + value + "'>" + value + "</option>");
                }
            }
        });
        if (count <= 1) {
            // $('#tdhm_key').next().remove();
            $('#tdhm_key').isSearchSelect({'width': '152', 'zindex': 0}, function (data) {
                //keyup click触发事件
                $("#spaccountid").val(data.value);
            }, function (data) {
                //初始化加载
                var val = $("#spaccountid").val();
                if (val) {
                    data.box.input.val(val);
                }

            });
        }
        $('#tdhm_key').next().find('.c_input').bind('click blur', function () {
            $("#spaccountType").val("1");
        });
        $('#tdhm_key').next().find('.c_result li').click(function () {
            $("#spaccountType").val("0");
        });
    }

    function refresh() {
        $("#pageForm").submit();
    }
</script>
</body>
</html>
