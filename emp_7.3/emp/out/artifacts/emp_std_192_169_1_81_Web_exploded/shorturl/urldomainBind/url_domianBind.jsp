<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="com.montnets.emp.util.PageInfo" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Calendar" %>
<%@page import="java.util.LinkedHashMap" %>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils" %>
<%@page import="com.montnets.emp.entity.pasgroup.Userdata" %>
<%@page import="com.montnets.emp.common.constant.StaticValue" %>
<%@page import="com.montnets.emp.common.constant.CommonVariables" %>
<%@page import="com.montnets.emp.shorturl.surlmanage.vo.LfDomainCorpVo" %>
<%
    String langName = (String) session.getAttribute("emp_lang");

    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
    String iPath = request.getRequestURI().substring(0,
            request.getRequestURI().lastIndexOf("/"));
    String inheritPath = iPath.substring(0, iPath.lastIndexOf("/"));
    String commonPath = inheritPath.substring(0, inheritPath.indexOf("/", 1));


    PageInfo pageInfo = new PageInfo();
    pageInfo.setPageSize(10);
    pageInfo = (PageInfo) request.getAttribute("pageInfo");
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @SuppressWarnings("unchecked")
    Map<String, String> btnMap = (Map<String, String>) session.getAttribute("btnMap");//按钮权限Map
    @SuppressWarnings("unchecked")
    Map<String, String> titleMap = (Map<String, String>) session.getAttribute("titleMap");
    String menuCode = titleMap.get("bind");
    menuCode = menuCode == null ? "0-0-0" : menuCode;

    @SuppressWarnings("unchecked")
    LinkedHashMap<String, String> conditionMap = (LinkedHashMap<String, String>) request.getAttribute("conditionMap");
    String corpCode, corpName, domain, flag, startTime, recvtime;


    corpCode = conditionMap.get("corpCode") == null ? "" : conditionMap.get("corpCode");
    corpName = conditionMap.get("corpName") == null ? "" : conditionMap.get("corpName");
    flag = conditionMap.get("flag") == null ? "" : conditionMap.get("flag");
    domain = conditionMap.get("domain") == null ? "" : conditionMap.get("domain");
    startTime = conditionMap.get("startTime") == null ? "" : conditionMap.get("startTime");
    recvtime = conditionMap.get("recvtime") == null ? "" : conditionMap.get("recvtime");


    @SuppressWarnings("unchecked")
    List<LfDomainCorpVo> domainCorps = (List<LfDomainCorpVo>) request.getAttribute("urlList");

    //@SuppressWarnings("unchecked")
    //Map<String, String> usersMap  = (Map<String, String>) request.getAttribute("usersMap");


    String findResult = (String) request.getAttribute("findresult");
    CommonVariables CV = new CommonVariables();
    String skin = session.getAttribute("stlyeSkin") == null ? "default" : (String) session.getAttribute("stlyeSkin");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
<head>
    <title>短域名管理</title>
    <%@include file="/common/common.jsp" %>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

    <link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet"
          type="text/css"/>
    <link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet"
          type="text/css"/>
    <link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css"/>
    <link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css"/>
    <link rel="stylesheet"
          href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion()%>"/>
    <link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion()%>" type="text/css">
    <link href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet"
          type="text/css"/>
    <link href="<%=commonPath%>/common/widget/zTreeStyle/zTreeStyle.css?V=<%=StaticValue.getJspImpVersion() %>"
          rel="stylesheet" type="text/css"/>

</head>
<body>
<div id="container" class="container">
    <%-- 当前位置 --%>
    <%=com.montnets.emp.common.constant.ViewParams.getPosition(menuCode) %>

    <%-- 内容开始 --%>
    <%
        if (btnMap.get(menuCode + "-0") != null) {
    %>
    <div id="rContent" class="rContent">
        <input type="hidden" value="<%=inheritPath %>" id="inheritPath"/>
        <input type="hidden" value="<%=path %>" id="path1"/>
        <form name="pageForm" action="<%=path%>/surl_bind.htm?method=find" method="post" id="pageForm">
            <div id="hiddenValueDiv" style="display:none;"></div>
            <div id="r_sysMoRparams" class="hidden"></div>
            <div class="buttons">
                <div id="toggleDiv">
                </div>

                <a id="add" href="<%=path%>/surl_bind.htm?method=toAdd">新建</a>

            </div>
            <div id="condition">
                <table>
                    <tbody>
                    <tr>

                        <td>
                            企业编号：
                        </td>
                        <td>
                            <input type="text" style="width: 181px;" value="<%=corpCode==null?"":corpCode %>"
                                   id="corpCode"
                                   name="corpCode"/>
                        </td>

                        <td>
                            企业名称：
                        </td>
                        <td>
                            <input type="text" style="width: 181px;" value="<%=corpName==null?"":corpName %>"
                                   id="corpName"
                                   name="corpName"/>
                        </td>

                        <td>
                            状态：
                        </td>
                        <td>
                            <label>
                                <select id="flag" name="flag" isInput="false">
                                    <option value="">全部</option>
                                    <option value="0" <%=flag.equals("0") ? "selected" : "" %> >已启用</option>
                                    <option value="1" <%=flag.equals("1") ? "selected" : "" %>>已禁用</option>
                                </select>
                            </label>
                        </td>

                        <td class="tdSer">
                            <center><a id="search"></a></center>
                        </td>


                        <td>&nbsp;</td>
                    </tr>
                    <tr>
                        <td>
                            短域名：
                        </td>
                        <td>
                            <input type="text" style="width: 181px;" value="<%=domain == null ? "" : domain%>"
                                   id="domain"
                                   name="domain"/>
                        </td>
                        <td>
                            创建时间：
                        </td>
                        <td class="tableTime">

                            <input type="text"
                                   style="cursor: pointer; width: 181px; background-color: white;"
                                   class="Wdate" readonly="readonly" onclick="sedtime()"
                                   value="<%=startTime == null ? "" : startTime%>" id="startTime"
                                   name="startTime">
                        </td>
                        <td>
                            至：
                        </td>
                        <td>
                            <input type="text"
                                   style="cursor: pointer; width: 181px; background-color: white;"
                                   class="Wdate" readonly="readonly" onclick="revtime()"
                                   value="<%=recvtime == null ? "" : recvtime%>" id="recvtime"
                                   name="recvtime">
                        </td>

                        <td colspan=2>&nbsp;</td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <table id="content">
                <thead>
                <tr>
                    <th width="11%">企业编号</th>
                    <th width="11%">企业名称</th>
                    <th width="11%">绑定短域名</th>
                    <th width="11%">状态</th>
                    <th width="11%">创建人</th>
                    <th width="15%">创建时间</th>
                    <th width="15%">修改时间</th>
                    <th width="15%">操作</th>

                </tr>
                </thead>
                <tbody>
                <%
                    if (domainCorps != null && domainCorps.size() > 0) {
                        for (LfDomainCorpVo domainCorp : domainCorps) {

                %>
                <tr>

                    <td>
                        <label><%=domainCorp.getCorpCode() %>
                        </label>
                    </td>

                    <td>
                        <label><%=domainCorp.getCorpName() %>
                        </label>
                    </td>

                    <td>
                        <label><%=domainCorp.getDomain() %>
                        </label>
                    </td>

                    <td class="ztalign">
                        <center>
                            <div class="setControl">
                                <%
                                    if (domainCorp.getFlag() == 0) {
                                %>
                                已启用
                                <%
                                } else {
                                %>
                                已禁用
                                <%
                                    }
                                %>

                            </div>

                            <div class="setControl01" style="display:none;">
                                已启用
                            </div>

                            <div class="setControl02" style="display:none;">
                                已禁用
                            </div>

                            <%
                                if (domainCorp.getFlag() == 0) {//启用
                            %>
                            <select style="display:none;" name="flag<%=domainCorp.getId() %>"
                                    id="flag<%=domainCorp.getId() %>" class="input_bd"
                                    onchange="javascript:changeStatu('<%=domainCorp.getId()%>','<%=domainCorp.getCorpCode()%>')">
                                <option value="0" selected="selected">已启用</option>
                                <option value="1">已禁用</option>
                            </select>
                            <%
                            } else {//禁用
                            %>
                            <select style="display:none;" name="flag<%=domainCorp.getId() %>"
                                    id="flag<%=domainCorp.getId() %>" class="input_bd"
                                    onchange="javascript:changeStatu('<%=domainCorp.getId()%>','<%=domainCorp.getCorpCode()%>')">
                                <option value="0">已启用</option>
                                <option value="1" selected="selected">已禁用</option>
                            </select>
                            <%
                                }
                            %>
                        </center>
                    </td>

                    <td>
                        <label><%=domainCorp.getCreateUser() %>
                        </label>
                    </td>

                    <td>
                        <label><%=domainCorp.getCreateTm().toString().substring(0, 19)%>
                        </label>
                    </td>

                    <td>
                        <label><%=domainCorp.getUpdateTm().toString().substring(0, 19)%>
                        </label>
                    </td>

                    <td>
                        <%if (btnMap.get(menuCode + "-0") != null) { %>
                        <a href="<%=path %>/surl_bind.htm?method=see&id=<%=domainCorp.getCorpID()%>">查看</a>
                        <%
                        } else {
                        %>
                        <label>--</label>
                        <%
                            }%>
                        &nbsp;&nbsp;
                        <%if (btnMap.get(menuCode + "-3") != null) { %>

                        <a href="<%=path %>/surl_bind.htm?method=toEdit&corpid=<%=domainCorp.getCorpID()%>">修改</a>
                        <%
                        } else {
                        %>
                        <label>--</label>
                        <%
                            }%>

                    </td>
                </tr>
                <%
                    }
                } else {
                %>
                <tr>
                    <td colspan="10" align="center">无记录</td>
                </tr>
                <%
                    }
                %>
                </tbody>
                <tfoot>
                <tr>
                    <td colspan="10">
                        <input type="hidden" name="pageTotalRec" id="pageTotalRec"
                               value="<%=pageInfo.getTotalRec() %>"/>
                        <div id="pageInfo"></div>
                    </td>
                </tr>
                </tfoot>
            </table>
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
<div id="singledetail" style="padding:5px;display:none;word-wrap: break-word;word-break:break-all;overflow-y:auto;">
    <div id="msg" style="width:100%;height:100%;overflow-y:auto;">
        <xmp></xmp>
    </div>
</div>

<div id="showUrl" style="display:none;">
    <iframe id="kkuu" name="kkuu" style="padding:5px;height: 337px;overflow: auto;" src=""></iframe>
</div>

<div id="addDiv" style="display:none">
    <table style="width:100%;height:100%;font-size: 12px; ">
        <tr style="height: 39px;">
            <td width="28%" align="right">
                链接名称：
            </td>
            <td width="72%">
                <input type="text" style="width: 266px;height: 23px;line-height: 23px;" class="input_bd"
                       name="urlNameAdd" id="urlNameAdd" maxlength="150"/>
                &nbsp;&nbsp;&nbsp;&nbsp;<font color="red">*</font>
            </td>
        </tr>

        <tr style="height: 39px;">
            <td width="28%" align="right">
                链接地址：
            </td>
            <td width="72%">
                <input type="text" style="width: 266px;" class="input_bd" name="urlCodeAdd" id="urlCodeAdd"
                       maxlength="1024"/>
                &nbsp;&nbsp;&nbsp;&nbsp;<font color="red">*</font>
            </td>
        </tr>
        <tr style="height: 65px;">
            <td align="right">
                业务描述：
            </td>
            <td width="72%"><textarea style="height:150px;width: 266px; line-height: 18px;" class="input_bd"
                                      name="urlDescriptionAdd" id="urlDescriptionAdd"></textarea></td>
        </tr>
        <tr>
            <td colspan="2" style="text-align:center">
                <input name="addsubmit" id="addsubmit" class="btnClass5 mr23" type="button" value="确定"
                       onclick="javascript: addUrl();"/>
                <input name="addcancel" id="addcancel" class="btnClass6" type="button" value="取消"
                       onclick="javascript:doCancel();"/>
                <%-- 为了兼容ie8下取消按钮右下角旁边能点击到确定按钮，增加这个换行 --%>
                <br/>
            </td>
        </tr>
    </table>
</div>
<div class="clear"></div>
<script type="text/javascript"
        src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
<script type="text/javascript"
        src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
<script type="text/javascript"
        src="<%=commonPath%>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
<script type="text/javascript"
        src="<%=commonPath %>/common/js/jquery.ztree-myjquery-b.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
<script type="text/javascript"
        src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript"
        src="<%=commonPath%>/common/js/empSelect.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript"
        src="<%=commonPath%>/frame/frame2.5/js/showbox.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript"
        src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript">

    $(document).ready(function () {
        closeTreeFun(["dropMenu"]);
        getLoginInfo("#hiddenValueDiv");

        $("#toggleDiv").toggle(function () {
            $("#condition").hide();
            $(this).addClass("collapse");
        }, function () {
            $("#condition").show();
            $(this).removeClass("collapse");
        });
        $("#content tbody tr").hover(function () {
            var _this = $(this);
            changeStatus1(_this);
        }, function () {
            var _this = $(this);
            changeStatus2(_this);
        });
        //IE浏览器下拉框消失未解之谜的终极解决办法
        $("select").mouseleave(function () {
            return false;
        });
        initPage(<%=pageInfo.getTotalPage()%>, <%=pageInfo.getPageIndex()%>, <%=pageInfo.getPageSize()%>, <%=pageInfo.getTotalRec()%>);

        $('#search').click(function () {
            submitForm();
        });


    });

    function changeStatus1(obj) {
        obj.addClass("hoverColor");
        obj.find('select')[0].style.display = 'block';
        obj.find('div.setControl')[0].style.display = 'none';
        obj.find('div.setControl01')[0].style.display = 'none';
        obj.find('div.setControl02')[0].style.display = 'none';
    };

    function changeStatus2(obj) {
        obj.removeClass("hoverColor");
        obj.find('select')[0].style.display = 'none';
        obj.find('div.setControl')[0].style.display = 'none';
        //获取下拉框被选中的value值
        var flagValue = obj.find("option:selected").attr("value");

        if (flagValue == 0) {
            obj.find('div.setControl01')[0].style.display = 'block';
        } else {
            obj.find('div.setControl02')[0].style.display = 'block';
        }
    };


    function showMenu() {
        //hideMenu2();
        var sortSel = $("#depNam");
        var sortOffset = $("#depNam").offset();
        $("#dropMenu").toggle();
    }
</script>
<script type="text/javascript"
        src="<%=commonPath%>/shorturl/urldomainBind/js/domainBind.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
</body>
</html>
