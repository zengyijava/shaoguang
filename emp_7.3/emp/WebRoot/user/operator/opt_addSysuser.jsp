<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.Iterator" %>
<%@page import="com.montnets.emp.common.constant.ViewParams" %>
<%@page import="com.montnets.emp.common.constant.StaticValue" %>
<%@page import="com.montnets.emp.common.constant.CommonVariables" %>
<%@page import="com.montnets.emp.entity.employee.LfEmployee" %>
<%@page import="java.util.List" %>
<%@page import="com.montnets.emp.entity.sysuser.LfRoles" %>
<%@page import="com.montnets.emp.employee.vo.LfEmployeeTypeVo" %>
<%@page import="com.montnets.emp.common.biz.SmsBiz" %>
<%@page import="com.montnets.emp.entity.sysuser.LfSysuser" %>
<%@page import="com.montnets.emp.i18n.util.MessageUtils" %>
<%@ taglib prefix="emp"
           uri="http://www.montnets.com/emp/i18n/tags/simple" %>

<%
    String path = request.getContextPath();
    String iPath = request.getRequestURI().substring(0, request.getRequestURI().lastIndexOf("/"));
    String inheritPath = iPath.substring(0, iPath.lastIndexOf("/"));
    inheritPath = inheritPath.substring(0, inheritPath.lastIndexOf("/"));
    String commonPath = inheritPath.substring(0, inheritPath.lastIndexOf("/"));
    @SuppressWarnings("unchecked")
    Map<String, String> btnMap = (Map<String, String>) session.getAttribute("btnMap");//按钮权限Map

    //Jsp页面中获取session中的语言设置
    String langName = (String) session.getAttribute(StaticValue.LANG_KEY);

    @SuppressWarnings("unchecked")
    Map<String, String> titleMap = (Map<String, String>) session.getAttribute("titleMap");
    String menuCode = titleMap.get("sysuser");
    LfEmployee le = new LfEmployee();
    if (request.getAttribute("le") != null) {
        le = (LfEmployee) request.getAttribute("le");
    }
    int lsex = 1;
    if (le.getSex() != null) {
        lsex = le.getSex();
    }
    Long guid = (Long) request.getAttribute("guid");
    String corpCode = (String) request.getAttribute("corpCode");
    Integer subnoDigit = (Integer) request.getAttribute("subnoDigit");
    @SuppressWarnings("unchecked")
    List<LfRoles> roleList = (List<LfRoles>) session.getAttribute("roleList");

    //String sysuserCode = ViewParams.SYSUSERCODE;
    String sysuserCode = MessageUtils.extractMessage("user", "user_xtgl_czygl_text_133", request);

    String loginId = MessageUtils.extractMessage("user", "user_xtgl_czygl_text_132", request);
    @SuppressWarnings("unchecked")
    List<LfEmployeeTypeVo> zwList = (List<LfEmployeeTypeVo>) request.getAttribute("zwList");
    //   1是开启审核范围开关    2是一个都没有开启
    String switchFlag = (String) request.getAttribute("switchFlag");
    LfSysuser curSysuser = (LfSysuser) session.getAttribute("loginSysuser");

    String skin = session.getAttribute("stlyeSkin") == null ? "default" :
            (String) session.getAttribute("stlyeSkin");
%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
<head>
    <title>新增操作员</title>
    <%@include file="/common/common.jsp" %>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <link href="<%=inheritPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet"
          type="text/css"/>
    <link href="<%=inheritPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet"
          type="text/css"/>
    <link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
    <link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
    <link href="<%=inheritPath%>/common/css/reading.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet"
          type="text/css"/>
    <link href="<%=inheritPath%>/common/css/floating.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet"
          type="text/css"/>
    <link rel="stylesheet"
          href="<%=inheritPath%>/common/widget/zTreeStyle/zTreeStyle.css?V=<%=StaticValue.getJspImpVersion() %>"
          type="text/css"/>
    <link rel="stylesheet"
          href="<%=inheritPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>">
    <link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css">
    <%if (StaticValue.ZH_HK.equals(langName)) {%>
    <link rel="stylesheet" type="text/css"
          href="<%=empBasePath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
    <link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
    <%}%>

    <link rel="stylesheet" type="text/css"
          href="<%=iPath%>/css/opt_addSysuser.css?V=<%=StaticValue.getJspImpVersion() %>"/>
    <link rel="stylesheet" type="text/css" href="<%=skin %>/user.css?V=<%=StaticValue.getJspImpVersion() %>"/>

</head>
<body onload="show();" id="opt_addSysuser">
<input type="hidden" id="showZhezhao" value="false"/>
<input type="hidden" id="pathUrl" value="<%=path%>"/>
<div id="container" class="container">
    <%-- header开始 --%>
    <%=com.montnets.emp.common.constant.ViewParams.getPosition(langName, menuCode, "添加操作员  ") %>
    <%-- header结束 --%>
    <%-- 内容开始 --%>
    <div id="rContent" class="rContent">
        <%
            if (btnMap.get(menuCode + "-1") != null) {
        %>
        <div class="titletop">
            <table class="user_table1" style="">
                <tr>
                    <td class="titletop_td">
                        <emp:message key="user_xtgl_czygl_text_2" defVal="添加操作员" fileName="user"/>
                    </td>
                    <td align="right">
									<span class="titletop_font fhsyj_span" onclick="javascript:doreturn()">
									<emp:message key="user_xtgl_czygl_text_3" defVal="返回上一级" fileName="user"/></span>
                    </td>
                </tr>
            </table>
        </div>
        <div id="table_input" class="table_input">
            <form action="<%=request.getContextPath()%>/opt_sysuser.htm?method=add" method="post" id="Sysuser"
                  name="Sysuser" autocomplete="off">
                <div id="loginUser" class="hidden"></div>
                <input type="hidden" id="method" name="method" value="add"/>
                <input type="hidden" id="guid" name="guid" value="<%=guid%>"/>
                <input type="hidden" id="inheritPath" value="<%=inheritPath%>"/>
                <input type="hidden" id="eid" name="eid" value='<%=le.getEmployeeId()==null?"":le.getEmployeeId()%>'/>
                <input type="hidden" id="eguid" name="eguid" value='<%=le.getGuId()==null?"":le.getGuId()%>'/>

                <div class="tabyys jbxx_div"><emp:message key="user_xtgl_czygl_text_4" defVal="操作员基本信息"
                                                          fileName="user"/></div>
                <div id="lyout" class="div_bd lyout2">
                    <table id="sysTable" class="sysTable">
                        <tr>
                            <td class="loginId_td"><%=loginId%>：</td>
                            <td class="userNo_td">
                                <label class="userNo_label">
                                    <input class="input_bd" type="text" name="username" id="userNo" value=""
                                           maxlength="15" onkeyup="javascript:checkText($(this));"/>
                                    <label class="placeholder"> <emp:message key="user_xtgl_czygl_text_5"
                                                                             defVal="登录密码和账号一致"
                                                                             fileName="user"/></label>
                                    <font class="font_red">&nbsp;*</font>&nbsp;
                                    <input type="hidden" name="type" id="type" value="add"/>
                                </label>
                            </td>
                            <td class="czumc_td"><emp:message key="user_xtgl_czygl_text_6" defVal="操作员名称："
                                                              fileName="user"/></td>
                            <td>
                                <label>
                                    <input class="input_bd" type="text" name="name" id="name" onblur="checkData(1)"
                                           value='<%=le.getName()==null?"":le.getName()%>' onkeyup="validateInput(this)"
                                           maxlength="30"/><font class="font_red">&nbsp;&nbsp;*</font>
                                </label>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <%=sysuserCode%>：
                            </td>
                            <td>
                                <%
                                    if (StaticValue.getCORPTYPE() == 1) {
                                        out.print(corpCode);
                                    }
                                %>
                                <label>
                                    <input type="hidden" name="corptype" id="corptype"
                                           value="<%=StaticValue.getCORPTYPE()==1?corpCode:"" %>"/>
                                    <input type="text" name="userCode" id="userCode" size="10" <%
                                        if (StaticValue.getCORPTYPE() == 1) {
                                            out.print("class='input_bd userCode'");
                                        } else {
                                            out.print("class='input_bd'");
                                        }
                                    %> value='<%=le.getEmployeeNo()!=null?le.getEmployeeNo():guid%>'
                                           onkeyup="if(value != value.replace(/[\n\s*\r]/g,'')) value=value.replace(/[\n\s*\r]/g,'')"
                                           maxlength="20"/>
                                    <font class="font_red">&nbsp;*</font>
                                </label>
                            </td>
                            <td align="left">
                                <emp:message key="user_xtgl_czygl_text_7" defVal="所属机构：" fileName="user"/>
                            </td>
                            <td class="depNam_td">
                                <input type="hidden" name="depId" id="depId" value="0"/>
                                <div class="depNam_div">
                                    <input id="depNam" class="input_bd fontColor treeInput depNam"
                                           onclick="javascript:showMenu();"
                                           name="depNam" type="text" readonly
                                           value='<emp:message key="user_xtgl_czygl_text_8" defVal="点击选择机构" fileName="user" />'
                                    />
                                    <font class="font_red">&nbsp;*</font><a id="ssdep" onclick="javascript:showMenu();"
                                                                            class="ssdep"></a>
                                </div>
                                <div id="dropMenu_udep" class="dropMenu_udep">
                                    <%--											<div id='closeDepTreeClass' class='closeDepTreeClassHover'></div>--%>
                                    <ul id="dropdownMenu_udep" class="tree dropdownMenu_udep">
                                    </ul>
                                    <iframe class="dropMenu_udep_iframe"></iframe>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <emp:message key="user_xtgl_czygl_text_9" defVal="性别：" fileName="user"/>
                            </td>
                            <td><select name="sex" class="input_bd">
                                <option value="1"><emp:message key="user_xtgl_czygl_text_10" defVal="男"
                                                               fileName="user"/></option>
                                <option value="0" <%=lsex == 0 ? "selected" : ""%>><emp:message
                                        key="user_xtgl_czygl_text_11" defVal="女" fileName="user"/></option>
                            </select>
                            </td>
                            <td align="left">
                                <emp:message key="user_xtgl_czygl_text_12" defVal="生日：" fileName="user"/>
                            </td>
                            <td>
                                <label>
                                    <input type="text" value='' id="birthday" name="birthday"
                                           class="Wdate input_bd birthday" readonly="readonly"
                                           onclick="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'%yyyy-%MM-%dd'})"
                                           class="input_book">
                                </label>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <emp:message key="user_xtgl_czygl_text_13" defVal="手机：" fileName="user"/>
                            </td>
                            <td>
                                <label>
                                    <%
                                        String mobile = le.getMobile() != null ? le.getMobile() : "";
                                        if (btnMap.get(StaticValue.PHONE_LOOK_CODE) == null && !"".equals(mobile)) {
                                            //无号码的查看权限，需替换手机号码的星号
                                            mobile = new CommonVariables().replacePhoneNumber(mobile);
                                        }
                                    %>
                                    <input type="hidden" name="ishidephone" id="ishidephone" value="<%=mobile%>"/>
                                    <input type="hidden" name="mobile" id="mobile"
                                           value="<%=le.getMobile()!=null?le.getMobile():""%>"/>
                                    <input class="input_bd" type="text" name="tempMobile" id="tempMobile"
                                           onblur="checkData(1)" value='<%=mobile%>'
                                           maxlength="21" onkeyup="phoneInputCtrl($(this));"/>
                                    <font class="font_red">&nbsp;&nbsp;*</font>
                                </label>
                            </td>
                            <td align="left">
                                <emp:message key="user_xtgl_czygl_text_14" defVal="职位：" fileName="user"/>
                            </td>
                            <td>
                                <select name="job" id="job" class="input_bd">
                                    <option value=""><emp:message key="user_xtgl_czygl_text_15" defVal="请选择"
                                                                  fileName="user"/></option>
                                    <%
                                        if (zwList != null && zwList.size() > 0) {
                                            LfEmployeeTypeVo zw = null;
                                            for (int i = 0; i < zwList.size(); i++) {
                                                zw = zwList.get(i);
                                    %>
                                    <option value="<%=zw.getName() %>"><%=zw.getName().replace("<", "&lt;").replace(">", "&gt;")%>
                                    </option>
                                    <%
                                            }
                                        }
                                    %>
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <emp:message key="user_xtgl_czygl_text_16" defVal="分配角色：" fileName="user"/>
                            </td>
                            <td>
                                <label>
                                    <input id="roleName" onclick="javascript:openRoleChoose();" name="roleName"
                                           type="text"
                                           readonly
                                           value='<emp:message key="user_xtgl_czygl_text_72" defVal="点击选择角色" fileName="user" />'
                                           allRoleName="" class="input_bd fontColor roleName"/>
                                    <div id="allRoleName" class="allRoleName allRoleName2"></div>
                                </label><font class="font_red">&nbsp;*</font>
                                <input type="hidden" id="cheRoles" name="cheRoles" value=""/>
                            </td>
                            <td align="left">
                                <emp:message key="user_xtgl_czygl_text_17" defVal="账号状态：" fileName="user"/>
                            </td>
                            <td>
                                <select name="userState" id="userState">
                                    <option value="1"><emp:message key="user_xtgl_czygl_text_18" defVal="启用"
                                                                   fileName="user"/></option>
                                    <option value="0"><emp:message key="user_xtgl_czygl_text_19" defVal="禁用"
                                                                   fileName="user"/></option>
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <emp:message key="user_xtgl_czygl_text_20" defVal="设置审核流程：" fileName="user"/>
                            </td>
                            <td>
                                <select name="isaudited" class="input_bd"
                                        <%if ("2".equals(switchFlag)) {%>
                                        disabled="disabled"
                                        title="<emp:message key="user_xtgl_czygl_text_32" defVal="系统未开启审核功能" fileName="user" />"
                                        <%} %> >
                                    <option value="2"><emp:message key="user_xtgl_czygl_text_21" defVal="免审"
                                                                   fileName="user"/></option>
                                    <option value="1"><emp:message key="user_xtgl_czygl_text_22" defVal="必审"
                                                                   fileName="user"/></option>
                                </select>
                            </td>
                            <td align="left">
                                <emp:message key="user_xtgl_czygl_text_23" defVal="固定尾号：" fileName="user"/>
                            </td>
                            <td>
                                &nbsp;<input type="checkbox" id="isNeedSubno" name="isNeedSubno" class="isNeedSubno"
                                             onclick="javascript:clickSubno('<%=guid%>')"/>
                                <input type="text" id="addSubno" name="addSubno" size="<%=subnoDigit%>"
                                       maxlength="<%=subnoDigit%>" value="" class="addSubno"
                                       onkeyup="value=value.replace(/[^\d]/g,'')"/>
                                <input type="hidden" id="subno2" name="subno2" value=""/>
                                <input type="hidden" id="haveSubno" name="haveSubno" value="2"/>
                                <input type="hidden" id="isGiveSubno" name="isGiveSubno" value="2"/>
                            </td>


                        </tr>
                        <tr>
                            <td align="left">
                                <emp:message key="user_xtgl_czygl_text_24" defVal="是否接收审批提醒：" fileName="user"/>
                            </td>
                            <td>&nbsp;<input type="checkbox" id="msgRemind" name="msgRemind"
                                             class="msgRemind"/>&nbsp;<emp:message key="user_xtgl_czygl_text_25"
                                                                                   defVal="短信" fileName="user"/>
                                &nbsp;&nbsp;<input type="checkbox" id="emailRemind" name="emailRemind"
                                                   class="emailRemind"/>&nbsp;<emp:message key="user_xtgl_czygl_text_26"
                                                                                           defVal="邮箱" fileName="user"/>
                            </td>
                            <%if (new SmsBiz().isWyModule("20")) { %>
                            <td align="left"><emp:message key="user_xtgl_czygl_text_27" defVal="是否为客服人员："
                                                          fileName="user"/></td>
                            <td>&nbsp;<input type="checkbox" id="isCustome" name="isCustome" class="isCustome"/></td>
                            <%} %>
                        </tr>
                    </table>
                </div>


                <div class="tabyys <%=StaticValue.ZH_HK.equals(langName)?"sjqx_div1":"sjqx_div2"%>"><emp:message
                        key="user_xtgl_czygl_text_28" defVal="数据权限" fileName="user"/><img
                        src="<%=inheritPath%>/common/img/down.gif"></div>
                <div id="lyout" class="div_bd lyout2">
                    <table id="sysTable" class="sysTable">
                        <tr>
                            <td class="czysjqx_td">
                                <emp:message key="user_xtgl_czygl_text_29" defVal="操作员数据权限：" fileName="user"/>
                            </td>
                            <td class="domDepId_td">
                                <input type="hidden" name="domDepId" id="domDepId"/>
                                <select class="input_bd" id="userPerType" name="userPerType"
                                        onchange="changeUserPermType();" disabled="disabled">
                                    <option value="1"><emp:message key="user_xtgl_czygl_text_124" defVal="个人"
                                                                   fileName="user"/></option>
                                    <option id="depOption" value="2"><emp:message key="user_xtgl_czygl_text_30"
                                                                                  defVal="机构" fileName="user"/></option>
                                </select>
                                <span id="depPerm" class="depPerm">
											<input id="domdepName" name="domdepName" type="hidden" value=''
                                                   class="domdepName"/>
											&nbsp;<a id="selectDepBtn_user" onclick="javascript:showUserMenu();"
                                                     class="selectDepBtn_user">
											<font color="blue">	<emp:message key="user_xtgl_czygl_text_15"
                                                                                defVal="请选择"
                                                                                fileName="user"/></font></a>
										</span>
                                <div id="dropMenu_user" class="dropMenu_user">
                                    <%--											<div id='closeDepTreeClass' class='closeDepTreeClassHover'></div>--%>
                                    <ul id="dropdownMenu_user" class="tree dropdownMenu_user">
                                    </ul>
                                </div>
                            </td>
                            <td class="jgshy_td">
                                <emp:message key="user_xtgl_czygl_text_31" defVal="机构审核员：" fileName="user"/>
                            </td>
                            <td>
                                <select name="isReviewer" id="isReviewer" class="input_bd"
                                        <%if ("2".equals(switchFlag)) {%>
                                        disabled="disabled"
                                        title="<emp:message key="user_xtgl_czygl_text_32" defVal="系统未开启审核功能" fileName="user" />"
                                        <%} %>
                                >
                                    <option value="2"><emp:message key="user_xtgl_czygl_text_33" defVal="否"
                                                                   fileName="user"/></option>
                                    <option value="1"><emp:message key="user_xtgl_czygl_text_34" defVal="是"
                                                                   fileName="user"/></option>
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <emp:message key="user_xtgl_czygl_text_35" defVal="员工通讯录权限：" fileName="user"/>
                            </td>
                            <td class="domDepId_employ_td">
                                <input type="hidden" name="domDepId_employ" id="domDepId_employ"/>
                                <select class="input_bd" id="employPerType" name="employPerType"
                                        onchange="changeEmployPermType();">
                                    <option value="1"><emp:message key="user_xtgl_czygl_text_124" defVal="个人"
                                                                   fileName="user"/></option>
                                    <option id="depOption_employ" value="2"><emp:message key="user_xtgl_czygl_text_30"
                                                                                         defVal="机构"
                                                                                         fileName="user"/></option>
                                </select>
                                <span id="depPerm_employ" class="depPerm_employ">
												<input id="domdepName_employ" name="domdepName_employ" type="hidden"
                                                       value='' class="domdepName_employ"/>
												&nbsp;<a id="selectDepBtn_employ" onclick="javascript:showEmployMenu();"
                                                         class="selectDepBtn_employ">
												<font color="blue"><emp:message key="user_xtgl_czygl_text_15"
                                                                                defVal="请选择"
                                                                                fileName="user"/></font></a>
											</span>
                                <div id="dropMenu_employ" class="dropMenu_employ">
                                    <div class="qd_div">
                                        <input type="button" value="<emp:message key="common_btn_7"
										defVal="确定" fileName="common" />" class="btnClass1"
                                               onclick="javascript:zTreeOnClickOK_employ();" style=""/>&nbsp;&nbsp;
                                        <input type="button" value="<emp:message key="user_xtgl_czygl_text_15"
										defVal="请选择" fileName="user" />" class="btnClass1"
                                               onclick="javascript:cleanSelect_employ();" style=""/>
                                    </div>
                                    <ul id="dropdownMenu_employ" class="tree dropdownMenu_employ"></ul>
                                </div>
                            </td>
                            <td align="left">
                                <emp:message key="user_xtgl_czygl_text_38" defVal="客户通讯录权限：" fileName="user"/>
                            </td>
                            <td class="domDepId_client_td">
                                <input type="hidden" name="domDepId_client" id="domDepId_client"/>
                                <select class="input_bd" id="clientPerType" name="clientPerType"
                                        onchange="changeClientPermType();">
                                    <option value="1"><emp:message key="user_xtgl_czygl_text_124" defVal="个人"
                                                                   fileName="user"/></option>
                                    <option id="depOption_client" value="2"><emp:message key="user_xtgl_czygl_text_30"
                                                                                         defVal="机构"
                                                                                         fileName="user"/></option>
                                </select>
                                <span id="depPerm_client" class="depPerm_client">
											<input id="domdepName_client" name="domdepName_client" type="hidden"
                                                   value=''
                                                   class="domdepName_client"/>
											&nbsp;<a id="selectDepBtn_client" onclick="javascript:showClientMenu();"
                                                     class="selectDepBtn_client">
												<font color="blue">	<emp:message key="user_xtgl_czygl_text_15"
                                                                                    defVal="请选择"
                                                                                    fileName="user"/></font></a>
										</span>
                                <div id="dropMenu_client" class="dropMenu_client">
                                    <div class="dropMenu_client_down_div">
                                        <input type="button" value="<emp:message key="common_btn_7"
										defVal="确定" fileName="common" />" class="btnClass1"
                                               onclick="javascript:zTreeOnClickOK_client();" style=""/>&nbsp;&nbsp;
                                        <input type="button" value="<emp:message key="common_btn_20"
										defVal="清空" fileName="common" />" class="btnClass1"
                                               onclick="javascript:cleanSelect_client();" style=""/>
                                    </div>
                                    <ul id="dropdownMenu_client" class="tree dropdownMenu_client">
                                    </ul>
                                </div>
                            </td>
                        </tr>
                        <tr>
                        <%-- EMP5.7新需求：增加对操作员充值和回收权限   by pengj --%>
<%--                        <%
                            if ("admin".equals(curSysuser.getUserName())) {
                        %>--%>
                            <td>
                                <emp:message key="user_xtgl_czygl_text_39"
                                             defVal="充值回收权限：" fileName="user"/>
                            </td>
                            <td class="domDepId_dep_td">
                                <input type="hidden" name="domDepId_dep" id="domDepId_dep"/>
                                <select class="input_bd" id="depPerType" name="depPerType"
                                        onchange="changeDepPermType();" disabled="disabled">
                                    <option value="1"><emp:message key="user_xtgl_czygl_text_40" defVal="操作员所属机构"
                                                                   fileName="user"/></option>

                                    <option id="depOption_dep" value="2"><emp:message key="user_xtgl_czygl_text_30"
                                                                                      defVal="机构"
                                                                                      fileName="user"/></option>

                                </select>
                                <span id="depPerm_dep" class="depPerm_dep">
												<input id="domDepName_dep" name="domDepName_dep" type="hidden" value=''
                                                       class="domDepName_dep"/>
												&nbsp;<a id="selectDepBtn_dep" onclick="javascript:showDepMenu();"
                                                         class="selectDepBtn_dep">
												<font color="blue"><emp:message key="user_xtgl_czygl_text_15"
                                                                                defVal="请选择"
                                                                                fileName="user"/></font></a>
											</span>
                                <div id="dropMenu_dep" class="dropMenu_dep">
                                    <div class="dropMenu_dep_down_div">
                                        <input type="button"
                                               value="<emp:message key="common_btn_7" defVal="确定" fileName="common" />"
                                               class="btnClass1" onclick="javascript:zTreeOnClickOK_dep();" style=""/>&nbsp;&nbsp;
                                        <input type="button"
                                               value="<emp:message key="common_btn_20" defVal="清空" fileName="common" />"
                                               class="btnClass1" onclick="javascript:cleanSelect_dep();" style=""/>
                                    </div>
                                    <ul id="dropdownMenu_dep" class="tree dropdownMenu_dep"></ul>
                                </div>
                            </td>
<%--                        <%
                            }
                        %>--%>
                            <td>
                                <emp:message key="user_xtgl_czygl_text_136"
                                             defVal="下行内容查看权限：" fileName="user"/>
                            </td>
                            <td class="domDepId_dep_td">
                                <input name="showNum" type="radio" value="0"  checked="checked"/>全部可见
                                &nbsp;&nbsp;&nbsp;&nbsp;
                                <input name="showNum" type="radio" value="1" />数字不可见
                            </td>
                        </tr>
                        <%-- end --%>

                    </table>
                </div>

                <div class="tabyys <%=StaticValue.ZH_HK.equals(langName)?"qtlxfs_div1":"qtlxfs_div2"%>"><emp:message
                        key="user_xtgl_czygl_text_41" defVal="其他联系方式" fileName="user"/><img
                        src="<%=inheritPath%>/common/img/down.gif"></div>
                <div id="lyout" class="div_bd lyout2">
                    <table id="sysTable" class="sysTable">
                        <tr>
                            <td class="zj_td">
                                <emp:message key="user_xtgl_czygl_text_42" defVal="座机：" fileName="user"/>
                            </td>
                            <td class="oph_td">
                                <label>
                                    <input type="text" class="input_bd" name="oph" id="oph" onblur="checkData(1)"
                                           value='<%=le.getOph()==null?"":le.getOph()%>' maxlength="18"/>
                                </label>
                            </td>
                            <td class="mail_td">
                                E-mail：
                            </td>
                            <td>
                                <label>
                                    <input type="text" class="input_bd" name="EMail" id="EMail"
                                           onblur="checkData(1)" value='<%=le.getEmail()==null?"":le.getEmail()%>'
                                           maxlength="32"/>
                                </label><font id="addEmailCheck" class="addEmailCheck">&nbsp;*</font>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                QQ：
                            </td>
                            <td>
                                <label>
                                    <input type="text" class="input_bd" name="qq" id="qq" onblur="checkData(1)"
                                           value='<%=le.getQq()==null?"":le.getQq()%>' maxlength="16"/>
                                </label>
                            </td>
                            <td align="left">
                                MSN：
                            </td>
                            <td>
                                <label>
                                    <input type="text" class="input_bd" name="msn" id="msn"
                                           onblur="checkData(1)" value='<%=le.getMsn()==null?"":le.getMsn()%>'
                                           maxlength="32"/>
                                </label>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <emp:message key="user_xtgl_czygl_text_43" defVal="传真：" fileName="user"/>
                            </td>
                            <td>
                                <label>
                                    <input type="text" class="input_bd" name="fax" id="fax" onblur="checkData(1)"
                                           value='<%=le.getFax()==null?"":le.getFax()%>' maxlength="18"/>
                                </label>
                            </td>
                        </tr>
                    </table>
                </div>

                <div class="tabyys <%=StaticValue.ZH_HK.equals(langName)?"jrygtxl_div1":"jrygtxl_div2"%>"><emp:message
                        key="user_xtgl_czygl_text_44" defVal="加入员工通讯录" fileName="user"/><img
                        src="<%=inheritPath%>/common/img/down.gif" class="img-position"></div>
                <div id="lyout" class="div_bd lyout2">
                    <table id="sysTable" class="sysTable">
                        <tr>
                            <td colspan="4">&nbsp;
                                <input name="isEmploy" type="radio" value="0" checked="checked"
                                       onclick="javascript:$('#isJoinEmploy').hide()"/><emp:message
                                        key="user_xtgl_czygl_text_45" defVal="不加入" fileName="user"/>
                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                <input name="isEmploy" onclick="javascript:$('#isJoinEmploy').show()" type="radio"
                                       value="1"/><emp:message key="user_xtgl_czygl_text_46" defVal="加入"
                                                               fileName="user"/>&nbsp;
                            </td>
                        </tr>
                        <tr id="isJoinEmploy" class="isJoinEmploy">
                            <td class="gh_td">
                                <emp:message key="user_xtgl_czygl_text_47" defVal="工号：" fileName="user"/>
                            </td>
                            <td class="employeeNo_td">
                                <label>
                                    <input type="text" name="employeeNo" id="employeeNo" size="10" <%
                                        if (StaticValue.getCORPTYPE() == 1) {
                                            out.print("class='input_bd employeeNo'");
                                        } else {
                                            out.print("class='input_bd'");
                                        }
                                    %> value='<%=le.getEmployeeNo()!=null?le.getEmployeeNo():guid%>' maxlength="20"/>
                                    <%-- <font style="color: red;">&nbsp;*</font> --%>
                                </label>
                            </td>
                            <td class="ygjg_td">
                                <emp:message key="user_xtgl_czygl_text_48" defVal="员工机构：" fileName="user"/>
                            </td>
                            <td class="depNam_td">
                                <input type="hidden" name="employDepId" id="employDepId" value=""/>
                                <div class="depNam_div" id="depNam_tmp">
                                    <input id="employDepNam" class="input_bd fontColor treeInput depNam"
                                           onclick="javascript:showMenuEdep();" name="employDepNam" type="text" readonly
                                           value='<emp:message key="user_xtgl_czygl_text_49" defVal="点击选择机构" fileName="user" />'/>
                                    <font class="font_red">&nbsp;*</font><a id="ssdep" onclick="javascript:showMenu();"
                                                                            class="ssdep"></a>
                                </div>
                                <div id="dropMenu_edep" class="dropMenu_edep">
                                    <%--											<div id='closeDepTreeClass' class='closeDepTreeClassHover'></div>--%>
                                    <ul id="dropdownMenu_edep" class="tree dropdownMenu_edep">
                                    </ul>
                                </div>
                            </td>
                            <%--<td class="depNam_td">
                                <input type="hidden" name="depId" id="depId" value="0"/>
                                <div  class="depNam_div">
                                    <input id="depNam" class="input_bd fontColor treeInput depNam" onclick="javascript:showMenu();"
                                           name="depNam" type="text" readonly value='	<emp:message key="user_xtgl_czygl_text_8" defVal="点击选择机构" fileName="user" />'
                                    />
                                    <font class="font_red">&nbsp;*</font><a id="ssdep" onclick="javascript:showMenu();"
                                                                            class="ssdep" ></a>
                                </div>
                                <div id="dropMenu_udep" class="dropMenu_udep">
                                    &lt;%&ndash;											<div id='closeDepTreeClass' class='closeDepTreeClassHover'></div>&ndash;%&gt;
                                    <ul id="dropdownMenu_udep" class="tree dropdownMenu_udep" >
                                    </ul>
                                    <iframe class="dropMenu_udep_iframe"></iframe>
                                </div>
                            </td>--%>
                        </tr>
                        <tr class="bz_tr">
                            <td><emp:message key="user_xtgl_czygl_text_50"
                                             defVal="备注：" fileName="user"/></td>
                            <td colspan="3">
                                <textarea name="comments"></textarea>
                            </td>
                        </tr>

                    </table>
                </div>


                <table id="" class="hint-tr_table">
                    <tr id="hint-tr" style="">
                        <td colspan="4" id="hint">
                            <p id="zhu"></p>
                        </td>
                    </tr>
                    <tr>
                        <td class="isEmp_td">
                            <div class="buttonDiv">
                                <input type="button" name="button" id="button" value="<emp:message key="common_btn_7"
										defVal="确定" fileName="common" />" onclick="checkData(2)"
                                       class="btnClass5 mr23"/>
                                <input type="button" name="button" id="button" value="<emp:message key="common_btn_10"
										defVal="返回" fileName="common" />" onclick="javascript:doreturn()"
                                       class="btnClass6"/>
                                <br/>
                                <input type="hidden" id="isEmp" name="isEmp" value='<%=le.getName()!=null%>'/>
                            </div>
                        </td>
                    </tr>
                </table>


                <%-- 弹出添加角色 overflow-y:auto;border: 1px solid #cccccc;--%>
                <div id="roDiv" class="roDiv">

                    <div class="roDiv_down_div">
                        <%
                            if (roleList != null) {
                        %>
                        <div id="roleDiv" class="roleDiv">
                            <%
                                for (int i = 0; i < roleList.size(); i++) {
                                    LfRoles role = roleList.get(i);
                            %>
                            <input type="checkbox" name="roleId" id="roleId:<%=role.getRoleId() %>"
                                   value="<%=role.getRoleId() %>"/>
                            <label for="roleId:<%=role.getRoleId() %>"><%=role.getRoleName().replace("<", "&lt;").replace(">", "&gt;")%>
                            </label><br/>
                            <% }%>
                        </div>
                        <% } %>
                    </div>
                    <center>
                        <div style="" id="" class="roleBut">
                            <input type="button" class="btnClass5" value="<emp:message key="common_btn_7"
										defVal="确定" fileName="common" />" onclick="dorole()"/>
                        </div>
                    </center>
                </div>
            </form>
        </div>
        <div class="clear"></div>
    </div>
    <% } %>
    <%-- 内容结束 --%>
    <%-- foot开始 --%>
    <div class="bottom">
        <div id="bottom_right">
            <div id="bottom_left"></div>
        </div>
    </div>
    <%-- foot结束 --%>
</div>
<iframe id="ifr" class="ifr"></iframe>
<div class="clear"></div>
<%-- 国际化js加载 --%>
<script type="text/javascript" src="<%=empBasePath%>/common/i18n/i18nUtil.js"></script>
<script type="text/javascript" src="<%=empBasePath%>/common/i18n/<%=langName%>/user_<%=langName%>.js"></script>
<script type="text/javascript" src="<%=empBasePath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
<script language="javascript" src="<%=inheritPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"
        type="text/javascript"></script>
<script type="text/javascript"
        src="<%=inheritPath%>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript"
        src="<%=inheritPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript"
        src="<%=inheritPath%>/common/js/jquery.ztree-myjquery-b.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript"
        src="<%=inheritPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=inheritPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script src="<%=inheritPath%>/common/js/floating.js?V=<%=StaticValue.getJspImpVersion() %>"
        type="text/javascript"></script>
<script language="javascript" src="<%=iPath%>/js/sysuser.js?V=<%=StaticValue.getJspImpVersion() %>"
        type="text/javascript"></script>
<script language="javascript" src="<%=iPath%>/js/dataPerm.js?V=<%=StaticValue.getJspImpVersion() %>"
        type="text/javascript"></script>
<script>
    var str_val = "";

    function validateInput(obj) {
        obj.value = obj.value.replace(/[']+/img, '');
        str = obj.value;
        var Expression = /^[\u4e00-\u9fa5]+$/;
        var poiseision = /^[a-zA-Z]+$/;
        var objExp = new RegExp(Expression);
        var pinExp = new RegExp(poiseision);
        if (objExp.test(str)) {
            if (str.length > 30) {
                $(obj).val(str_val);
                alert("操作员姓名长度不得超过30字符");
                return;
            }
        } else if (pinExp.test(str)) {
            if (str.length > 30) {
                $(obj).val(str_val);
                alert("操作员姓名长度不得超过30字符");
                return;
            }
        } else {
            if (str.length > 30) {
                $(obj).val(str_val);
                alert("操作员姓名长度不得超过30字符");
                return;
            }
        }
        str_val = str;
        obj.val(str_val);
    }

    $(function () {
        //修复下拉框双箭头的问题 ---- 20180820
        $(".input_bd").find("div").hide();
        $('.tabyys').each(function (i) {

            $(this).bind('click', function () {
                if (i == 0) {
                    return false;
                }
                var _src = $(this).find('img').attr('src');
                if ($(this).next().is(":visible")) {
                    _src = _src.replace('down.gif', 'up.gif');
                    $(this).addClass('mt10').find('img').attr('src', _src);

                } else {
                    _src = _src.replace('up.gif', 'down.gif');
                    $(this).removeClass('mt10').find('img').attr('src', _src);
                }
                $(this).next().toggle();

            })
        })
    });
    $(document).ready(function () {

        setting_udep.expandSpeed = ($.browser.msie && parseInt($.browser.version) <= 7) ? "" : "fast";
        reloadTree();
        $("#selectDepBtn_user").hide();

        var isIE = false;
        var isFF = false;
        var isSa = false;
        if ((navigator.userAgent.indexOf("MSIE") > 0)
            && (parseInt(navigator.appVersion) >= 4))
            isIE = true;
        if (navigator.userAgent.indexOf("Firefox") > 0)
            isFF = true;
        if (navigator.userAgent.indexOf("Safari") > 0)
            isSa = true;
        $('#name').keypress(function (e) {
            var iKeyCode = window.event ? e.keyCode
                : e.which;
            if (iKeyCode == 60 || iKeyCode == 62) {
                if (isIE) {
                    event.returnValue = false;
                } else {
                    e.preventDefault();
                }
            }
        });
        $('#name').blur(function (e) {
            $(this).val($(this).val().replaceAll("<", "").replaceAll(">", ""));
        });

        closeTreeFunSel(["dropMenu_udep", "dropMenu_edep"]);
        closeTreeFunOnSpc(["dropMenu_employ", "dropMenu_client", "dropMenu_user"]);
        $("#roleName").myFloating({
            divId: "allRoleName",
            align: "right",
            paddingSize: 7,
            beforeShow: function () {
                var roleName = $("#roleName").attr("allRoleName");
                if (roleName == "") {
                    <%String rolename = "";
                    if (roleList != null){
                        for (int i = 0; i < roleList.size(); i++){
                            LfRoles role = roleList.get(i);
                            rolename += role.getRoleName().replaceAll("<","&lt;").replaceAll(">","&gt;")+"<br/>";
                        }
                    }%>
                    $("#allRoleName").html(getJsLocaleMessage("user", "user_xtgl_czygl_text_145"));
                }
                else {
                    var name = roleName.split(",");
                    var str = "";
                    for (var i = 0; i < name.length; i++) {
                        str += name[i].replaceAll("<", "&lt;").replaceAll(">", "&gt;") + "<br/>";
                    }
                    $("#allRoleName").html(str);
                }
            }
        });

        $(".placeholder").click(function () {
            $(this).parent().find("input").focus();
        });

    });

    function show() {
        <%if(StaticValue.getCORPTYPE()==0){%>
        //$("#userCode").css("width","160px");
        <%}%>
        <%String result=(String)request.getAttribute("result");
            if(result!=null && result.equals("true")){%>
        alert(getJsLocaleMessage("user", "user_xtgl_czygl_text_25"));
        <%}else if(result!=null && result.equals("false")){%>
        alert(getJsLocaleMessage("user", "user_xtgl_czygl_text_26"));
        <%}else if(result!=null && result.equals("errer")){%>
        alert(getJsLocaleMessage("user", "user_xtgl_czygl_text_26"));
        <%}else if(result!=null && result.equals("2")){%>
        alert(getJsLocaleMessage("user", "user_xtgl_czygl_text_146"));
        <%}
            if(result !=null)
            {
                if(request.getAttribute("le")!=null)
                {%>
        location.href = "<%=path%>/epl_employeeBook.htm?method=find&lguserid=" + $("#lguserid").val() +
            "&lgcorpcode=" + $("#lgcorpcode").val() + "&lgguid=" + $("#lgguid").val();
        <%}
    else
    {%>
        location.href = "<%=path%>/opt_sysuser.htm?method=find&lguserid=" + $("#lguserid").val() +
            "&lgcorpcode=" + $("#lgcorpcode").val() + "&lgguid=" + $("#lgguid").val();
        <%}
}%>
    }

    <%--跳转审核管理界面--%>

    function locationAudMgr(menuCode) {
        var lguserid = $("#lguserid").val();
        <%
            if(StaticValue.getInniMenuMap().containsKey("/aud_auditpro.htm"))
            {

        %>
        window.parent.openNewTab(menuCode, "<%=path%>/aud_auditpro.htm?method=find&lguserid=" + lguserid);
        <%
            }else{
        %>
        alert(getJsLocaleMessage("user", "user_xtgl_czygl_text_147"));
        return;
        <%
            }
        %>

    }
</script>
</body>
</html>
