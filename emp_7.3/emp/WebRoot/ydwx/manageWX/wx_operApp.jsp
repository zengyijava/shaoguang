<%@ page language="java" import="com.montnets.emp.i18n.util.MessageUtils" pageEncoding="UTF-8" %>
<%@page import="com.montnets.emp.util.PageInfo" %>
<%@page import="org.apache.commons.beanutils.DynaBean" %>
<%@ page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
    String iPath = request.getRequestURI().substring(0,
            request.getRequestURI().lastIndexOf("/"));
    String inheritPath = iPath.substring(0, iPath.lastIndexOf("/"));
    String commonPath = inheritPath.substring(0, inheritPath.lastIndexOf("/"));
    PageInfo pageInfo = new PageInfo();
    pageInfo = (PageInfo) request.getAttribute("pageInfo");


//按钮权限
    @SuppressWarnings("unchecked")
    Map<String, String> btnMap = (Map<String, String>) session.getAttribute("btnMap");//按钮权限Map

    @SuppressWarnings("unchecked")
    Map<String, String> titleMap = (Map<String, String>) session.getAttribute("titleMap");
    String rTitle = "operApp";//(String)request.getAttribute("rTitle");
    String menuCode = titleMap.get(rTitle);
    String skin = session.getAttribute("stlyeSkin") == null ? "default" : (String) session.getAttribute("stlyeSkin");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
<head>
    <%@include file="/common/common.jsp" %>
    <style>
        #overlay1 {
            position: absolute;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background: white;
            opacity: 0.1;
            filter: alpha(opacity=10);
            -moz-opacity: 0.1;
            display: none;
        }

        #overlay2 {
            position: absolute;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background: white;
            opacity: 0.1;
            filter: alpha(opacity=10);
            -moz-opacity: 0.1;
            display: none;
        }

    </style>
    <link rel="stylesheet" type="text/css"
          href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>"/>
    <link rel="stylesheet" type="text/css" href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion()%>"/>
    <link rel="stylesheet" type="text/css"
          href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion()%>"/>
    <link rel="stylesheet" type="text/css" href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion()%>"/>
    <link rel="stylesheet" type="text/css"
          href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion()%>">
    <link rel="stylesheet" type="text/css" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion()%>">
    <link rel="stylesheet" type="text/css"
          href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion()%>"/>

    <script type="text/javascript"
            src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
    <script type="text/javascript"
            src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
    <script type="text/javascript"
            src="<%=inheritPath%>/scripts/netManger.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
    <script type="text/javascript"
            src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
    <script type="text/javascript"
            src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
    <script type="text/javascript"
            src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
    <script type="text/javascript" src="<%=iPath%>/js/function.js?V=<%=StaticValue.getJspImpVersion()%>"></script>

    <%if (StaticValue.ZH_HK.equals(empLangName)) {%>
    <link rel="stylesheet" type="text/css"
          href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
    <link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
    <%}%>

    <link rel="stylesheet" type="text/css" href="<%=iPath%>/css/wx_operApp.css?V=<%=StaticValue.getJspImpVersion() %>"/>
    <link rel="stylesheet" type="text/css" href="<%=skin %>/ydwx.css?V=<%=StaticValue.getJspImpVersion() %>"/>

    <script type="text/javascript">
        $(document).ready(function () {
            getLoginInfo("#hiddenValueDiv");
            $('#search').click(function () {
                submitForm();
            });
            initPage(<%=pageInfo.getTotalPage()%>, <%=pageInfo.getPageIndex()%>, <%=pageInfo.getPageSize()%>, <%=pageInfo.getTotalRec()%>);
            $("#pres").hide();
            showaddtem();
            $("#toggleDiv").toggle(function () {
                $("#condition").hide();
                $(this).addClass("collapse");
            }, function () {
                $("#condition").show();
                $(this).removeClass("collapse");
            });
            loadNetmanger();
            /* 转到第n页*/
            $(".wtime").each(function () {
                var wtime = $(this).html();
                if ($(wtime.length == 19)) {
                    $(this).html(wtime.substr(0, 11));
                }
            });

            //页码改变时  div层内容变化的方法
            $(".ym").change(function () {
                var id = $(this).val();
                for (var i = 0; i < listpage.length; i++) {
                    if (id == listpage[i].id) {
                        // 此处为显示错误页面，避免进入登录页面
                        if (listpage[i].content == "notexists") {
                            $("#nm_preview_common").attr("src", "ydwx/wap/404.jsp");
                        } else {
                            $("#nm_preview_common").attr("src", "file/wx/PAGE/wx_" + listpage[i].id + ".jsp");
                        }
                    }
                }
            });
            $("#divBox").dialog({
                autoOpen: false,
                height: 530,
                width: 300,
                modal: true,
                close: function () {
                }
            });

            $("#pres").dialog({
                autoOpen: false,
                height: 200,
                width: 600,
                modal: true,
                open: function () {
                },
                close: function () {

                }
            });
            $("#smsdetailinfo").dialog({
                autoOpen: false,
                modal: true,
                title: getJsLocaleMessage("ydwx", "ydwx_wxgl_xxxx"),
                width: 680,
                height: 'auto',
                minHeight: 170,
                maxHeight: 650,
                closeOnEscape: false,
                resizable: false,
                open: function () {
                },
                close: function () {
                }
            });
        });

        function showaddtem() {
            <% String result=(String)session.getAttribute("msg");
                if(result!=null ){
                    session.removeAttribute("msg");
                %>
            alert("<%=result%>");
            <%}%>
        }

        function closediv() {
            //$("#pres").hide();
            $("#contentText").val("");
            $("#pres").dialog("close");

        }

        function back() {
            var url = "<%=path %>/wx_operApp.htm?method=find";
            var conditionUrl = "";
            if (url.indexOf("?") > -1) {
                conditionUrl = "&";
            } else {
                conditionUrl = "?";
            }
            conditionUrl = conditionUrl + backfind("#loginparams");
            location.href = url + conditionUrl;
        }

        function Look(netId) {
            $.post('wx_manger.htm?method=showNetById', {netId: netId}, function (data) {
                data = eval("(" + data + ")");
                listpage = data;
                $(".ym").children().remove();
                for (var i = 0; i < listpage.length; i++) {
                    $("<option>").val(listpage[i].id).html(listpage[i].name).appendTo($(".ym"));
                }
                // 此处为显示错误页面，避免进入登录页面
                if (listpage[0].content == "notexists") {
                    $("#nm_preview_common").attr("src", "ydwx/wap/404.jsp");
                } else {
                    $("#nm_preview_common").attr("src", "file/wx/PAGE/wx_" + listpage[0].id + ".jsp");
                }
                $("#divBox").dialog("open");
            });
        }

        function review(state) {
            var frId = $("#id").val();
            $("#ok3").attr("disabled", "disabled");
            $("#rj3").attr("disabled", "disabled");
            var cont = $("#contentText").val();
            if (cont.length > 200) {
                /*审批意见信息过长，字符长度不能大于200！*/
                alert(getJsLocaleMessage("ydwx", "ydwx_wxshh_1"));
                $("#ok3").attr("disabled", "");
                $("#rj3").attr("disabled", "");
                return;
            }
            if (cont.indexOf("'") != -1 || outSpecialChar(cont)) {
                /*请输入合法审批意见！*/
                alert(getJsLocaleMessage("ydwx", "ydwx_wxshh_2"));
                return;
            }
            $.post("<%=path%>/wx_operApp.htm?method=updateState", {
                frId: frId,
                state: state,
                cont: cont
            }, function (result) {
                if (result != null && result == "true") {
                    /*审批成功！*/
                    alert(getJsLocaleMessage("ydwx", "ydwx_wxshh_3"));
                    submitForm();
                } else {
                    /*审批失败！*/
                    alert(getJsLocaleMessage("ydwx", "ydwx_wxshh_5"));
                    closediv();
                }
            });
        }

        <%--模板审批详情--%>

        function opentmpAudmsg(tmpid) {
            $("#pres").dialog("open");
            $("#id").val(tmpid);
        }
    </script>
</head>
<body id="wx_operApp">
<input type="hidden" id="pathUrl" value="<%=path %>"/>
<input id="b" type="hidden" value="<%=request.getParameter("b")%>">
<input type="hidden" id="ba" value="<%=request.getParameter("ba")%>">
<input type="hidden" id="corp" value="${corp}">
<input type="hidden" id="id" value="">
<div id="frame">
    <%-- header开始 --%>
    <%=com.montnets.emp.common.constant.ViewParams.getPosition(empLangName, menuCode) %>
    <%-- header结束 --%>
    <%-- 内容开始 --%>
    <div id="rContent" class="rContent rContent2">
        <form name="pageForm" action="<%=path%>/wx_operApp.htm?method=find" method="post" id="pageForm">

            <div id="overlay2"></div>

            <div class="buttons">
                <div id="toggleDiv">
                </div>
            </div>
            <div id="condition">

                <div class="hiddenValueDiv" id="hiddenValueDiv"></div>
                <table>
                    <tr>
                        <td>
                            <span><emp:message key="ydwx_wxgl_wxbh" defVal="网讯编号：" fileName="ydwx"></emp:message></span>
                        </td>
                        <td align="left">
                            <input type="text" onkeyup="numberControl($(this))" name="wxid" id="wxid"
                                   value='<%=request.getParameter("wxid")==null?"": (String)request.getParameter("wxid")%>'
                                   class="wxid" style="width: 198px !important;"/>
                        </td>
                        <td>
                            <span> <emp:message key="ydwx_wxgl_wxmc" defVal="网讯名称："
                                                fileName="ydwx"></emp:message></span>
                        </td>
                        <td align="left">
                            <input type="text" name="wxname" id="wxname" class="wxname"
                                   value='<%=request.getParameter("wxname")==null?"": (String)request.getParameter("wxname")%>'>
                        </td>
                        <td>
                            <span> <emp:message key="ydwx_shengpi_3" defVal="公司名称："
                                                fileName="common"></emp:message></span>
                        </td>
                        <td align="left">
                            <input type="text" name="corpname" id="corpname" class="corpname"
                                   value='<%=request.getParameter("corpname")==null?"": (String)request.getParameter("corpname")%>'>
                        </td>
                        <td>
                            <emp:message key="ydwx_shengpi_5" defVal="运营商状态：" fileName="common"></emp:message>
                        </td>
                        <td align="left">
                            <select id="operState" name="operState" class="input_bd operState">
                                <option value="">
                                    <emp:message key="common_whole" defVal="全部" fileName="common"></emp:message>
                                </option>
                                <option value="0" <%="0".equals(request.getAttribute("operState")) ? "selected" : ""%>>
                                    <emp:message key="ydwx_wxgl_wxshh_weishp" defVal="未审批"
                                                 fileName="ydwx"></emp:message>
                                </option>
                                <option value="1" <%="1".equals(request.getAttribute("operState")) ? "selected" : ""%>>
                                    <emp:message key="ydwx_wxgl_wxshh_shptongguo" defVal="审批通过"
                                                 fileName="ydwx"></emp:message>
                                </option>
                                <option value="2" <%="2".equals(request.getAttribute("operState")) ? "selected" : ""%>>
                                    <emp:message key="ydwx_wxgl_wxshh_shpbutongguo" defVal="审批不通过"
                                                 fileName="ydwx"></emp:message>
                                </option>

                            </select>
                        </td>

                        <td class="tdSer">
                            <center><a id="search"></a></center>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <emp:message key="ydwx_wxgl_mblx" defVal="模板类型：" fileName="ydwx"></emp:message>
                        </td>
                        <td align="left">
                            <select id="temptype" name="temptype" class="input_bd temptype">
                                <option value="">
                                    <emp:message key="common_whole" defVal="全部" fileName="common"></emp:message>
                                </option>
                                <option value="1" <%="1".equals(request.getAttribute("temptype")) ? "selected" : ""%> >
                                    <emp:message key="ydwx_wxgl_mblx_options1" defVal="静态模板"
                                                 fileName="ydwx"></emp:message>
                                </option>
                                <option value="2" <%="2".equals(request.getAttribute("temptype")) ? "selected" : ""%> >
                                    <emp:message key="ydwx_wxgl_mblx_options2" defVal="动态模板"
                                                 fileName="ydwx"></emp:message>
                                </option>
                            </select>
                        </td>
                        <td>
                            <emp:message key="ydwx_common_time_chuangjianriqi" defVal="创建日期："
                                         fileName="ydwx"></emp:message>
                        </td>
                        <td align="left">
                            <input type="text" name="startdate" id="startdate"
                                   class="Wdate startdate" readonly="readonly"
                                   onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})"
                                   value='<%=request.getParameter("startdate")==null?"": (String)request.getParameter("startdate")%>'/>
                        </td>
                        <td>
                            &nbsp;&nbsp;&nbsp;&nbsp;<emp:message key="common_to" defVal="至："
                                                                 fileName="common"></emp:message>
                        </td>
                        <td>
                            <input type="text" name="enddate" id="enddate"
                                   class="Wdate enddate" readonly="readonly"
                                   onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})"
                                   value='<%=request.getParameter("enddate")==null?"": (String)request.getParameter("enddate")%>'/>
                        </td>
                        <td colspan="3"></td>
                    </tr>
                </table>

            </div>
            <div class="content_up_div">
                <table id="content">
                    <thead>
                    <tr>
                        <th class="checkall_th" style="display: none;">
                            <input type="checkbox" name="checkall" id="checkall"
                                   value="checkbox" onclick="checkAlls(this,'checklist');"/>
                        </th>
                        <th>
                            <emp:message key="common_companyCode" defVal="企业编码" fileName="common"></emp:message>
                        </th>
                        <th>
                            <emp:message key="ydwx_shengpi_2" defVal="公司名称" fileName="common"></emp:message>
                        </th>
                        <th>
                            <emp:message key="ydwx_wxgl_wxshh_bianhao" defVal="网讯编号" fileName="ydwx"></emp:message>
                        </th>
                        <th>
                            <emp:message key="ydwx_wxgl_wxshh_mingchen" defVal="网讯名称" fileName="ydwx"></emp:message>
                        </th>
                        <th>
                            <emp:message key="ydwx_wxfs_jtwxfs_wxnr" defVal="网讯内容：" fileName="ydwx"></emp:message>
                        </th>
                        <th>
                            <emp:message key="ydwx_wxgl_lx" defVal="类型" fileName="ydwx"></emp:message>
                        </th>
                        <th>
                            <emp:message key="ydwx_shengpi_4" defVal="运营商状态" fileName="ydwx"></emp:message>
                        </th>
                        <th>
                            <emp:message key="ydwx_wxgl_cjr" defVal="创建人" fileName="ydwx"></emp:message>
                        </th>
                        <th>
                            <emp:message key="common_smsTemplate_6" defVal="创建日期" fileName="common"></emp:message>
                        </th>
                        <th>
                            <emp:message key="ydwx_common_time_youxiaoriqi" defVal="有效日期" fileName="ydwx"></emp:message>
                        </th>

                        <th>
                            <emp:message key="common_operation" defVal="操作" fileName="common"></emp:message>
                        </th>
                    </tr>
                    </thead>
                    <tbody id="tbs">
                    <%
                        @SuppressWarnings("unchecked")
                        List<DynaBean> beans = (ArrayList<DynaBean>) request.getAttribute("pagebaseinfo");
                        if (beans != null && beans.size() > 0) {
                            for (DynaBean bean : beans) {
                    %>
                    <tr>
                        <td class="checklist_td" style="display: none;">
                            <input type="checkbox" name="checklist" id="checklist"
                                   value='<%=bean.get("netid") %>'/>
                        </td>
                        <td class="no_l_b">
                            <%=bean.get("corp_code") %>
                        </td>
                        <td class="no_l_b">
                            <%=bean.get("corp_name") %>
                        </td>
                        <td class="no_l_b">
                            <%=bean.get("netid") %>
                        </td>
                        <td title='<%=bean.get("name") %>' class="name_td">
                            <%=bean.get("name") %>
                        </td>
                        <td>
                            <%
                                if (btnMap.get(menuCode + "-0") != null) {
                            %>
                            <a href="javascript:Look('<%=bean.get("netid") %>');"><emp:message key="ydwx_common_yulan"
                                                                                               defVal="预览"
                                                                                               fileName="ydwx"></emp:message></a>

                            <%
                                } else {
                                    out.print("-");
                                }
                            %>
                        </td>
                        <td>
                            <%
                                Short temptype = Short.valueOf(bean.get("temptype").toString());
                                if (temptype == 1) {
                                    out.print(MessageUtils.extractMessage("ydwx", "ydwx_wxgl_mblx_options1", request));
                                } else if (temptype == 2) {
                                    out.print(MessageUtils.extractMessage("ydwx", "ydwx_wxgl_mblx_options2", request));
                                }
                            %>
                        </td>
                        <td>
                            <%
                                Integer sta = 0;
                                Object operappstatus = bean.get("operappstatus");
                                if (operappstatus == null || "".equals(operappstatus)) {
                                    sta = 0;
                                } else {
                                    sta = Integer.valueOf(operappstatus.toString());
                                }
                                String s = "";
                                if (sta == 0) {
                                    s = MessageUtils.extractMessage("ydwx", "ydwx_wxgl_wxshh_weishp", request);
                                } else if (sta == 1) {
                                    s = MessageUtils.extractMessage("ydwx", "ydwx_wxgl_wxshh_shptongguo", request);
                                } else if (sta == 2) {
                                    s = MessageUtils.extractMessage("ydwx", "ydwx_wxgl_wxshh_shpbutongguo", request);
                                }
                                out.print(s);
                            %>
                        </td>
                        <td title='<%=bean.get("creatname") %>'
                            class="creatname_td">
                            <%=bean.get("creatname") %>
                        </td>

                        <td title='<%=bean.get("creatdate") %>'
                            class="creatdate_td">
                            <% String time = bean.get("creatdate") == null ? "" : bean.get("creatdate") + "";
                                time = time.substring(0, time.lastIndexOf("."));
                            %>
                            <%=time %>
                            <%--<%=bean.get("creatdate") %>--%>
                        </td>
                        <td class="creatdate_td" title='<%=bean.get("timeout") %>'>
                            <% String timeout = bean.get("timeout") == null ? "" : bean.get("timeout") + "";
                                timeout = timeout.substring(0, timeout.lastIndexOf("."));
                            %>
                            <%=timeout %>
                            <%--<b  class="wtime timeout_b"><%=bean.get("timeout") %></b>
                            <input type="text" name="wxTime" id="wxTime"
                                value='<%=bean.get("timeout") %>' class="wxTime">--%>
                        </td>

                        <td nowrap="nowrap">
                            <%
                                if (sta == 0) {
                            %>
                            <a href="javascript:opentmpAudmsg('<%=bean.get("id") %>')"><%
                                out.print(MessageUtils.extractMessage("ydwx", "ydwx_wxgl_wxshh_shenpi", request));%></a>
                            <%
                                } else {
                                    out.print(MessageUtils.extractMessage("ydwx", "ydwx_wxgl_wxshh_shenpi", request));
                                }
                            %>
                        </td>


                    </tr>
                    <%
                        }
                    } else {
                    %>
                    <tr>
                        <td colspan="11">
                            <emp:message key="common_norecord" defVal="无记录" fileName="common"></emp:message>
                        </td>
                    </tr>
                    <%} %>
                    </tbody>
                    <tfoot>
                    <tr>
                        <td colspan="11">
                            <div id="pageInfo"></div>
                        </td>
                    </tr>
                    </tfoot>
                </table>
            </div>
        </form>
    </div>
    <div id="divBox" class="divBox" style="display: none"
         title="<emp:message key='ydwx_common_btn_chakan' defVal='查看' fileName='ydwx'></emp:message>">
        <div class="yemiandaohang_div">
            <emp:message key="ydwx_common_yemiandaohang" defVal="页面导航：" fileName="ydwx"></emp:message>
            <select class="ym yemiandaohang_select"></select>
        </div>
        <div style="width:240px;height:460px;margin-left:30px;background:url(<%=commonPath %>/common/img/iphone5.jpg);">
            <iframe style="width:195px;height:295px;margin-top:85px;margin-left:22px;" class="nm_preview_common"
                    id="nm_preview_common" src=""></iframe>
        </div>
    </div>

    <div id="smsdetailinfo" title="<emp:message key='ydwx_wxgl_xxxx' defVal='详细信息' fileName='ydwx'></emp:message>"
         class="smsdetailinfo">
        <div class="recordTableDiv" id="recordTableDiv">
            <table id="recordTable" class="recordTable">
            </table>
        </div>
        <div class="nextrecordmgs" id="nextrecordmgs">
        </div>
    </div>


    <%-- 设置模板弹出层 --%>
    <div id="pres" class='hidden'
         title="<emp:message key="ydwx_shengpi_6" defVal="运营商审批" fileName="ydwx"></emp:message>">
        <form method="post" id="SetWxTemp" name="SetWxTemp">
            <table class="SetWxTemp_table">
                <tr>
                    <td class="spyjmh_td">
                        <emp:message key="ydwx_wxshh_add_11" defVal="审批意见：" fileName="ydwx"></emp:message>
                    </td>
                    <td class="div_bd contentText_td">
						<textarea id="contentText" name="content"
                                  class="contentText"></textarea>
                    </td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                </tr>
                <tr>
                    <td></td>
                    <td>
                        <input type="button"
                               value=" <emp:message key="ydwx_wxshh_add_13" defVal="同意" fileName="ydwx"></emp:message> "
                               id="oks"
                               onclick="javascript:review(1);" class="btnClass5 mr23"/>

                        <input type="button"
                               value=" <emp:message key="ydwx_wxshh_add_14" defVal="拒绝" fileName="ydwx"></emp:message> "
                               id="rjs"
                               onclick="javascript:review(2);" class="btnClass6 mr23"/>

                        <input type="button"
                               value=" <emp:message key="common_cancel" defVal="取消" fileName="common"></emp:message> "
                               onclick="closediv();" class="btnClass6"/>
                        <br/>
                    </td>
                    <td>
                    </td>
                </tr>
            </table>
        </form>
    </div>

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
        src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=empLangName%>/common_<%=empLangName%>.js"></script>
<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=empLangName%>/ydwx_<%=empLangName%>.js"></script>

<script type="text/javascript">
    $(function () {
        $('#temptype,#rState').isSearchSelect({'width': '180', 'isInput': false, 'zindex': 0});
    })
</script>
</body>
</html>