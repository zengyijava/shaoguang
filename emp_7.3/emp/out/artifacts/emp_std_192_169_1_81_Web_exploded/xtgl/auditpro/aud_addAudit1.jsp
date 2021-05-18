<%@ page language="java" import="com.montnets.emp.entity.sysuser.LfFlow" contentType="text/html; charset=UTF-8" %>
<%@ page import="com.montnets.emp.entity.sysuser.LfReviewSwitch" %>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils" %>
<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<%@ page import="java.util.HashSet" %>
<%@ page import="java.util.LinkedHashMap" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
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
    Map<String, String> titleMap = (Map<String, String>) session.getAttribute("titleMap");
    String menuCode = titleMap.get("auditpro");
    menuCode = menuCode == null ? "0-0-0" : menuCode;

    String lguserid = (String) request.getAttribute("lguserid");

    //审核流程ID
    String flowid = (String) request.getAttribute("flowid");
    //界面跳转类型
    String pathtype = (String) request.getAttribute("pathtype");
    //审核流程对象
    LfFlow lfflow = (LfFlow) request.getAttribute("lfflow");
    //审批DIV信息
    //String audtypeBuffer = (String)request.getAttribute("audtypeBuffer");
    @SuppressWarnings("unchecked")
    HashSet<Integer> audtypeSet = (HashSet<Integer>) request.getAttribute("audtypeSet");
    //审批名称信息
    @SuppressWarnings("unchecked")
    LinkedHashMap<String, String> LevelAmountMap = (LinkedHashMap<String, String>) request.getAttribute("LevelAmountMap");
    @SuppressWarnings("unchecked")
    LinkedHashMap<String, String> auditMap = (LinkedHashMap<String, String>) request.getAttribute("auditMap");
    @SuppressWarnings("unchecked")
    LinkedHashMap<String, Integer> auditUserCountMap = (LinkedHashMap<String, Integer>) request.getAttribute("auditUserCountMap");

    //审核范围常量
    @SuppressWarnings("unchecked")
    LinkedHashMap<Integer, Integer> auditTypeMap = (LinkedHashMap<Integer, Integer>) request.getAttribute("auditTypeMap");
    @SuppressWarnings("unchecked")
    List<LfReviewSwitch> switchList = (List<LfReviewSwitch>) request.getAttribute("switchList");
    Integer switchCount = 0;
    if (switchList == null || switchList.size() == 0) {
        switchCount = 0;
    } else {
        switchCount = switchList.size();
    }
    String skin = session.getAttribute("stlyeSkin") == null ? "default" :
            (String) session.getAttribute("stlyeSkin");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <%@include file="/common/common.jsp" %>
    <link rel="stylesheet" type="text/css"
          href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>"/>
    <link rel="stylesheet" type="text/css" href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>"/>
    <link rel="stylesheet" type="text/css" href="<%=iPath%>/css/addMo.css?V=<%=StaticValue.getJspImpVersion() %>"/>
    <link rel="stylesheet" type="text/css" href="<%=skin %>/addMo.css?V=<%=StaticValue.getJspImpVersion() %>"/>
    <link rel="stylesheet" type="text/css"
          href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>">
    <link rel="stylesheet" type="text/css" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>">
    <link rel="stylesheet" type="text/css"
          href="<%=commonPath%>/common/css/floating.css?V=<%=StaticValue.getJspImpVersion() %>"/>
    <link rel="stylesheet" type="text/css" href="<%=skin %>/floating.css?V=<%=StaticValue.getJspImpVersion() %>"/>
    <link rel="stylesheet"
          href="<%=commonPath %>/common/widget/zTreeStyle/zTreeStyle.css?V=<%=StaticValue.getJspImpVersion() %>"
          type="text/css"/>
    <%if (StaticValue.ZH_HK.equals(langName)) {%>
    <link rel="stylesheet" type="text/css"
          href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
    <link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
    <%}%>
    <link rel="stylesheet" type="text/css"
          href="<%=iPath%>/css/aud_addAudit1.css?V=<%=StaticValue.getJspImpVersion() %>"/>
    <%--	<link rel="stylesheet" type="text/css" href="<%=skin %>/aud_addAudit1.css?V=<%=StaticValue.JSP_IMP_VERSION %>"/>--%>

</head>
<body id="aud_addAudit1">

<div id="container" class="container">
    <%-- 当前位置 --%>
    <%=com.montnets.emp.common.constant.ViewParams.getPosition(langName, menuCode, MessageUtils.extractMessage("xtgl", "xtgl_spgl_shlcgl_cjshlc", request)) %>

    <%-- 内容开始 --%>
    <div id="rContent" class="rContent">
        <form name="pageForm" action="aud_auditpro.htm?method=toAddAuditPro" method="post" id="serForm">
            <input type="hidden" id="pathUrl" value="<%=path %>"/>
            <input type="hidden" id="ipath" value="<%=iPath %>"/>
            <input type="hidden" id="lguserid" value="<%=lguserid %>"/>
            <%-- 标认界面有几个div   默认有一个等级--%>
            <input type="hidden" id="divlevel" name="divlevel"
                   value="<%if(pathtype != null && "2".equals(pathtype)){out.print(lfflow.getRLevelAmount());}else{out.print(1);}%>"/>
            <%-- div 序号 --%>
            <input type="hidden" id="divcount" name="divcount"
                   value="<%if(pathtype != null && "2".equals(pathtype)){out.print(lfflow.getRLevelAmount());}else{out.print(1);}%>"/>


            <%-- 假如是上一步操作 则存放审核流程ID --%>
            <input type="hidden" id="flowid" value="<%=flowid %>"/>
            <input type="hidden" id="pathtype" value="<%=pathtype %>"/>
            <input type="hidden" id="switchCount" value="<%=switchCount %>"/>

            <div id="getloginUser" class="getloginUser">
            </div>

            <div class="serForm_div">
                <table class="audLinebgimg1 serForm_div_table">
                    <tr>
                        <td class="serForm_div_table_td1"></td>
                        <td class="serForm_div_table_td2"><b>1.</b><emp:message key="xtgl_spgl_shlcgl_cjshlc"
                                                                                defVal="创建审核流程" fileName="xtgl"/></td>
                        <td class="serForm_div_table_td3"><b>2.</b><emp:message key="xtgl_spgl_shlcgl_szbshdx"
                                                                                defVal="设置被审核对象" fileName="xtgl"/></td>
                        <td class="serForm_div_table_td4"><b>3.</b><emp:message key="xtgl_spgl_shlcgl_wc" defVal="完成"
                                                                                fileName="xtgl"/></td>
                    </tr>
                </table>
            </div>
            <div class="itemDiv serForm_div_2">
                <table class="div_bg div_bd serForm_div_2_table">
                    <tr>
                        <td>
                            &nbsp;&nbsp;<emp:message key="xtgl_spgl_shlcgl_shlcmc_mh" defVal="审核流程名称：" fileName="xtgl"/>
                        </td>
                        <td class="serForm_div_2_table_td">
                            <%
                                String flowtaskname = "";
                                if (pathtype != null && "2".equals(pathtype)) {
                                    if (lfflow.getFTask() != null && !"".equals(lfflow.getFTask()) && !"null".equals(lfflow.getFTask())) {
                                        flowtaskname = lfflow.getFTask();
                                    }
                                }
                            %>
                            <input class="graytext input_bd flowtask" type="text"
                                   name="flowtask" id="flowtask"
                                   value='<%=StringEscapeUtils.unescapeHtml(flowtaskname) %>'
                                   maxlength="20" onfocus="checkText($(this),1)" onblur="checkText($(this),2)"/>
                            <label for="flowtask" class="placeholder adName_la" id="idPlaceholder1"><emp:message
                                    key="xtgl_spgl_shlcgl_qtxshlcmc" defVal="请填写审核流程名称" fileName="xtgl"/></label>
                        </td>
                    </tr>
                </table>

            </div>

            <div class="itemDiv serForm_div_3">
                <table class="div_bg div_bd serForm_div_3_table">
                    <tr>
                        <td class="serForm_div_3_table_td">
                            &nbsp;&nbsp;&nbsp;&nbsp;<emp:message key="xtgl_spgl_shlcgl_shfw_mh" defVal="审核范围："
                                                                 fileName="xtgl"/>
                        </td>
                        <td class="div_bd">

                            <%--1：短信发送；2：彩信发送 3：短信模板；4：彩信模板；switchSet --%>
                            <%
                                boolean isall = false;
                                boolean isshowall = true;
                                if (switchList.size() == 0) {
                                    isshowall = false;
                                } else if (audtypeSet != null && audtypeSet.size() == switchList.size()) {
                                    isall = true;
                                }

                                if (isshowall) {
                            %>
                            <input type='checkbox' name='checkall' id='checkall' class="select_check"
                                   onclick='checkAlls(this)'  <%
                                if (isall) {
                                    out.print("checked='checked'");
                                }
                            %>/><emp:message key="xtgl_spgl_shlcgl_quanx" defVal="全选" fileName="xtgl"/>
                            <br>
                            <br>
                            <%
                                } else {
                                    out.print("<label class='xtgl_label'>" + MessageUtils.extractMessage("xtgl", "xtgl_spgl_shlcgl_w", request) + "</label>");
                                }
                            %>

                            <%
                                if (switchList != null && switchList.size() > 0) {
                                    for (int m = 0; m < switchList.size(); m++) {
                                        LfReviewSwitch reviewSwitch = switchList.get(m);
                                        //审核范围  1：短信发送；2：彩信发送 3：短信模板；4：彩信模板；
                                        Integer audittype = reviewSwitch.getInfoType();
                                        //暂时将流量审核隐藏，待完全整合到标准版7.1
                                        if (audittype == 7) {
                                            continue;
                                        }
                            %>
                            <input type='checkbox' name='checklist' class="select_check" value='<%=audittype %>'
                                   onclick="checkCh()"   <% if (m == 0) {
                                out.print(" ");
                            } %>  <%
                                if (audtypeSet != null && audtypeSet.contains(audittype)) {
                                    out.print("checked='checked'");
                                }
                            %>  />
                            <% if (auditTypeMap != null && auditTypeMap.get(audittype) != null) {
                                out.print(auditTypeMap.get(audittype) + "&nbsp;&nbsp;&nbsp;");
                            } else {
                                out.print("-");
                            } %>
                            <%
                                    }
                                }
                            %>
                        </td>
                    </tr>
                </table>
            </div>


            <%-- 这里处理的是回填值--%>
            <%

                String flowid1 = "";
                //第一个审批DIV选择的对象类型
                String index1 = "0";
                //第一个审批DIV选择的审批条件    默认是全部审核才生效
                String checked1 = "1";
                String msg1 = "";
                if (pathtype != null && "2".equals(pathtype)) {
                    flowid1 = String.valueOf(lfflow.getFId());
                    if (LevelAmountMap != null && LevelAmountMap.size() > 0) {
                        String temp1 = auditMap.get("audit1");
                        if (temp1 != null && !"".equals(temp1)) {
                            String[] arr1 = temp1.split("_");
                            index1 = arr1[0];
                            checked1 = arr1[1];
                        }
                    }
                    if (LevelAmountMap != null && LevelAmountMap.size() > 0) {
                        msg1 = LevelAmountMap.get("LevelAmountID1");
                    }
                }
                String selindex1 = "1";
                if ("1".equals(index1)) {
                    selindex1 = "1";
                } else if ("4".equals(index1)) {
                    selindex1 = "3";
                } else if ("5".equals(index1)) {
                    selindex1 = "2";
                }

                //审核类型，1-操作员，2-群组，3-上级，4-机构审核员,5-逐级审核
            %>


            <div class="itemDiv addFlowDiv1" id="addFlowDiv1" attrnum="1" divattr='adddiv'>
                <table class="div_bg div_bd addFlowDiv1_table" border="1">
                    <tr>
                        <td rowspan="3" class="div_bd addFlowDiv1_table_td1">
                            <label class='nameclass' audlev='1'>&nbsp;&nbsp;&nbsp;&nbsp;<emp:message
                                    key="xtgl_spgl_shlcgl_d1jsp_mh" defVal="第1级审批：" fileName="xtgl"/></label>
                            <br>
                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:deleteDiv(1);"><emp:message
                                key="xtgl_spgl_shlcgl_sccj" defVal="删除此级" fileName="xtgl"/></a>
                        </td>
                        <td class="div_bd addFlowDiv1_table_td2">
                            <select id="type1" name="type1" onchange="changeObj(1)" class="type1">
                                <option value="1" <% if ("1".equals(index1)) {
                                    out.print("selected='selected'");
                                }%>><emp:message key="xtgl_spgl_shlcgl_czy" defVal="操作员" fileName="xtgl"/></option>
                                <option value="2" <% if ("5".equals(index1)) {
                                    out.print("selected='selected'");
                                }%>><emp:message key="xtgl_spgl_shlcgl_zjsp" defVal="逐级审批" fileName="xtgl"/></option>
                                <option value="3" <% if ("4".equals(index1)) {
                                    out.print("selected='selected'");
                                }%>><emp:message key="xtgl_spgl_shlcgl_jg" defVal="机构" fileName="xtgl"/></option>
                            </select>
                            <input type="hidden" id="selindex1" name="selindex1" value="<%=selindex1%>"/>
                            &nbsp;&nbsp;&nbsp;
                            <input type="button" id="add1" name="add1"
                                   value="<emp:message key='xtgl_spgl_shlcgl_tj' defVal='添加' fileName='xtgl'/>"
                                   class="btnClass1" <%if("5".equals(index1)){%>class="add1" <%}%>
                                   onclick="javascript:addobj(1);"/>
                            <label id="auditcount1" class="auditcount1">
                                <%
                                    if ("1".equals(index1) && auditUserCountMap != null && auditUserCountMap.size() > 0) {
                                        Integer count = auditUserCountMap.get("auditCount1");
                                        if (count != null) {
                                            out.print(MessageUtils.extractMessage("xtgl", "xtgl_spgl_shlcgl_g", request) + count + MessageUtils.extractMessage("xtgl", "xtgl_spgl_shlcgl_r", request));
                                        }
                                    }
                                %>
                            </label>
                            <a class="des"><emp:message key="xtgl_spgl_shlcgl_sm" defVal="说明" fileName="xtgl"/></a>
                        </td>
                    </tr>
                    <tr <% if ("5".equals(index1)) {
                        out.print("class='hidden'");
                    } %>>
                        <td class="div_bd addFlowDiv1_table_td3">
									<span id="flowobj1" class="flowobj1">
											<%=msg1%>
									</span>
                        </td>
                    </tr>
                    <tr>
                        <td class="div_bd addFlowDiv1_table_td4">
                            &nbsp;&nbsp;
                            <input name="isCheckFlow1" id="isAllFlowY" type="radio"
                                   value="1" <% if ("1".equals(checked1) && "2".equals(pathtype)) {
                                out.print("checked='checked'");
                            } else if ("1".equals(pathtype)) {
                                out.print("checked='checked'");
                            }%>/>
                            <emp:message key="xtgl_spgl_shlcgl_qbtgsx" defVal=" 全部通过生效" fileName="xtgl"/>&nbsp;&nbsp;&nbsp;
                            <input name="isCheckFlow1" id="isAllFlowN" type="radio"
                                   value="2" <% if ("2".equals(checked1) && "2".equals(pathtype)) {
                                out.print("checked='checked'");
                            }%>/>
                            <emp:message key="xtgl_spgl_shlcgl_qzyrspsx" defVal="其中一人审批生效" fileName="xtgl"/>&nbsp;
                        </td>
                    </tr>
                </table>
                <%-- 加载操作树   加载机构树 --%>
                <%-- 选择人员的操作树弹出框 --%>
                <div id="sysuserDiv1" title="<emp:message key='xtgl_spgl_shlcgl_xzczy' defVal='选择操作员' fileName='xtgl'/>"
                     class="sysuserDiv1">
                    <iframe id="flowuserFrame1" name="flowuserFrame1" src="" class="flowuserFrame1" marginwidth="0"
                            frameborder="no"></iframe>
                    <table>
                        <tr>
                            <td class="flowuserFrame1_td" align="center">
                                <input type="button" id="userok1"
                                       value="<emp:message key='xtgl_spgl_shlcgl_qd' defVal='确定' fileName='xtgl'/>"
                                       class="btnClass5" onclick="javascript:dook(1,1);"/>
                                &nbsp;<input type="button"
                                             value="<emp:message key='xtgl_spgl_shlcgl_qx' defVal='取消' fileName='xtgl'/>"
                                             class="btnClass6" onclick="javascript:closeuserdialog(1);"/>
                                <br/>
                            </td>
                        </tr>
                    </table>
                </div>
                <%-- 选择的操作员信息 --%>
                <input type="hidden" value="" id="addsysuserstr1" name="addsysuserstr1"/>
                <div id="sysdepDiv1" title="<emp:message key='xtgl_spgl_shlcgl_xzjg' defVal='选择机构' fileName='xtgl'/>"
                     class="sysdepDiv1">
                    <iframe id="flowdepFrame1" name="flowdepFrame1" src="" class="flowdepFrame1" marginwidth="0"
                            frameborder="no"></iframe>
                    <table>
                        <tr>
                            <td class="flowdepFrame1_tb" align="center">
                                <input type="button" id="depok1"
                                       value="<emp:message key='xtgl_spgl_shlcgl_qd' defVal='确定' fileName='xtgl'/>"
                                       class="btnClass5 mr23" onclick="javascript:dook(1,2);"/>
                                <input type="button"
                                       value="<emp:message key='xtgl_spgl_shlcgl_qx' defVal='取消' fileName='xtgl'/>"
                                       class="btnClass6" onclick="javascript:closedepdialog(1);"/>
                                <br/>
                            </td>
                        </tr>
                    </table>
                </div>
                <%-- 选择的操作员信息 --%>
                <input type="hidden" value="" id="addsysdepstr1" name="addsysdepstr1"/>

            </div>


            <%-- 处理其他审批DIV  大于第一级--%>

            <%
                if (pathtype != null && "2".equals(pathtype)) {
                    //表示该审核流程的审批等级
                    Integer auditLevel = lfflow.getRLevelAmount();
                    //从第2级开始输出其div审批对象
                    //第N个审批DIV选择的对象类型
                    String auditObjType = "";
                    //选择的对象内容
                    String auditObj = "";
                    //第N个审批DIV选择的审批条件    默认是全部审核才生效
                    String auditObjCon = "";

                    String userCountSer = "";

                    String selindextemp = "1";
                    for (int a = 2; a <= auditLevel; a++) {
                        userCountSer = "auditCount" + a;
                        if (LevelAmountMap != null && LevelAmountMap.size() > 0) {
                            String temp = auditMap.get("audit" + a);
                            if (temp != null && !"".equals(temp)) {
                                String[] arr = temp.split("_");
                                auditObjType = arr[0];
                                auditObjCon = arr[1];
                            }
                        }
                        if (LevelAmountMap != null && LevelAmountMap.size() > 0) {
                            auditObj = LevelAmountMap.get("LevelAmountID" + a);
                        }

                        if ("1".equals(auditObjType)) {
                            selindextemp = "1";
                        } else if ("4".equals(auditObjType)) {
                            selindextemp = "3";
                        } else if ("5".equals(auditObjType)) {
                            selindextemp = "2";
                        }

            %>
            <div class="itemDiv auditLevel_div" id="addFlowDiv<%=a%>" attrnum="<%=a%>" divattr='adddiv'>
                <table class="div_bg div_bd auditLevel_div_table" border="1">
                    <tr>
                        <td rowspan="3" class="div_bd auditLevel_div_table_td1">
                            <label class='nameclass' audlev='<%=a%>'>&nbsp;&nbsp;&nbsp;&nbsp;<emp:message
                                    key="xtgl_spgl_shlcgl_d" defVal="第" fileName="xtgl"/><%=a%><emp:message
                                    key="xtgl_spgl_shlcgl_jsp_mh" defVal="级审批：" fileName="xtgl"/></label>
                            <br>
                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:deleteDiv(<%=a%>);"><emp:message
                                key="xtgl_spgl_shlcgl_sccj" defVal="删除此级" fileName="xtgl"/></a>
                        </td>
                        <td class="div_bd auditLevel_div_table_td2">
                            <select id="type<%=a%>" name="type<%=a%>" onchange="changeObj(<%=a%>)"
                                    class="auditLevel_div_table_td2_select">
                                <option value="1" <% if ("1".equals(auditObjType)) {
                                    out.print("selected='selected'");
                                }%>><emp:message key="xtgl_spgl_shlcgl_czy" defVal="操作员" fileName="xtgl"/></option>
                                <option value="3" <% if ("4".equals(auditObjType)) {
                                    out.print("selected='selected'");
                                }%>><emp:message key="xtgl_spgl_shlcgl_jg" defVal="机构" fileName="xtgl"/></option>
                            </select>
                            <input type="hidden" id="selindex<%=a%>" name="selindex<%=a%>" value="<%=selindextemp%>"/>

                            &nbsp;&nbsp;&nbsp;
                            <input type="button" id="add<%=a%>" name="add<%=a%>"
                                   value="<emp:message key="xtgl_spgl_shlcgl_tj" defVal="添加" fileName="xtgl"/>"
                                   class="btnClass1" onclick="javascript:addobj(<%=a%>);"/>
                            <label id="auditcount<%=a%>" class="auditLevel_div_table_td2_label">
                                <%
                                    if ("1".equals(auditObjType) && auditUserCountMap != null && auditUserCountMap.size() > 0) {
                                        Integer count = auditUserCountMap.get(userCountSer);
                                        if (count != null) {
                                            out.print(MessageUtils.extractMessage("xtgl", "xtgl_spgl_shlcgl_g", request) + count + MessageUtils.extractMessage("xtgl", "xtgl_spgl_shlcgl_r", request));
                                        }
                                    }
                                %>
                            </label>
                            <a class='des'><emp:message key="xtgl_spgl_shlcgl_sm" defVal="说明" fileName="xtgl"/></a>
                        </td>
                    </tr>
                    <tr <% if ("5".equals(auditObjType)) {
                        out.print("class='hidden'");
                    } %>>
                        <td class="div_bd flowobj_td">
														<span id="flowobj<%=a%>" class="flowobj_span">
																<%=auditObj%>
														</span>
                        </td>
                    </tr>
                    <tr>
                        <td class="auditLevel_div_table_tr3_td">
                            &nbsp;&nbsp;
                            <input name="isCheckFlow<%=a%>" id="isAllFlowY" type="radio" value="1" <%
                                if ("1".equals(auditObjCon) && "2".equals(pathtype)) {
                                    out.print("checked='checked'");
                                }%>/>
                            <emp:message key="xtgl_spgl_shlcgl_qbtgsx" defVal="全部通过生效" fileName="xtgl"/>&nbsp;&nbsp;&nbsp;
                            <input name="isCheckFlow<%=a%>" id="isAllFlowN" type="radio" value="2" <%
                                if ("2".equals(auditObjCon) && "2".equals(pathtype)) {
                                    out.print("checked='checked'");
                                }%>/>
                            <emp:message key="xtgl_spgl_shlcgl_qz1rclsx" defVal=" 其中1人处理生效" fileName="xtgl"/>&nbsp;
                        </td>
                    </tr>
                </table>
                <%-- 加载操作树   加载机构树 --%>
                <%-- 选择人员的操作树弹出框 --%>
                <div id="sysuserDiv<%=a%>"
                     title="<emp:message key='xtgl_spgl_shlcgl_xzczy' defVal='选择操作员' fileName='xtgl'/>"
                     class="sysuserDiv">
                    <iframe id="flowuserFrame<%=a%>" name="flowuserFrame<%=a%>" src="" class="flowuserFrame"
                            marginwidth="0" frameborder="no"></iframe>
                    <table>
                        <tr>
                            <td class="flowuserFrame_td" align="center">
                                <input type="button" id="userok<%=a%>"
                                       value="<emp:message key='xtgl_spgl_shlcgl_qd' defVal='确定' fileName='xtgl'/>"
                                       class="btnClass5" onclick="javascript:dook(<%=a%>,1)"/>
                                &nbsp;<input type="button"
                                             value="<emp:message key='xtgl_spgl_shlcgl_qx' defVal='取消' fileName='xtgl'/>"
                                             class="btnClass6" onclick="javascript:closeuserdialog(<%=a%>);"/>
                                <br/>
                            </td>
                        </tr>
                    </table>
                </div>
                <%-- 选择的操作员信息 --%>
                <input type="hidden" value="" id="addsysuserstr<%=a%>" name="addsysuserstr<%=a%>"/>
                <div id="sysdepDiv<%=a%>"
                     title="<emp:message key='xtgl_spgl_shlcgl_xzjg' defVal='选择机构' fileName='xtgl'/>" class="sysdepDiv">
                    <iframe id="flowdepFrame<%=a%>" name="flowdepFrame<%=a%>" src="" class="flowdepFrame"
                            marginwidth="0" frameborder="no"></iframe>
                    <table>
                        <tr>
                            <td class="flowdepFrame_td" align="center">
                                <input type="button" id="depok<%=a%>"
                                       value="<emp:message key='xtgl_spgl_shlcgl_qd' defVal='确定' fileName='xtgl'/>"
                                       class="btnClass5 mr23" onclick="javascript:dook(<%=a%>,2)"/>
                                <input type="button"
                                       value="<emp:message key='xtgl_spgl_shlcgl_qx' defVal='取消' fileName='xtgl'/>"
                                       class="btnClass6" onclick="javascript:closedepdialog(<%=a%>);"/>
                                <br/>
                            </td>
                        </tr>
                    </table>
                </div>
                <%-- 选择的操作员信息 --%>
                <input type="hidden" value="" id="addsysdepstr<%=a%>" name="addsysdepstr<%=a%>"/>
            </div>
            <%
                        auditObjType = "";
                        auditObj = "";
                        auditObjCon = "";
                    }
                }
            %>


            <div class="itemDiv adddiv" id="adddiv">
                <table class="div_bg div_bd adddiv_table">
                    <tr>
                        <td align="center" class="adddiv_table_td">
                            &nbsp;&nbsp;&nbsp;<a href="javascript:addFlowDiv()"><emp:message
                                key="xtgl_spgl_shlcgl_djzjyjsp" defVal="点击增加一级审批" fileName="xtgl"/></a>
                        </td>
                    </tr>
                </table>
            </div>

            <div class="itemDiv biaoj_div">
                <table class="div_bg div_bd biaoj_div_table">
                    <tr>
                        <td class="biaoj_div_table_td1">
                            &nbsp;&nbsp;<emp:message key="xtgl_spgl_shlcgl_bz_mh" defVal="备注：" fileName="xtgl"/>
                        </td>
                        <td class="biaoj_div_table_td2">
                            <input class="graytext input_bd comment" type="text"
                                   name="comment" id="comment"
                                   value='<%
												if(pathtype!=null && "2".equals(pathtype)){
														if(lfflow.getComments() != null && !"".equals(lfflow.getComments())){
															out.print(lfflow.getComments());
														}else{
															out.print("");
														}
												}%>'
                                   maxlength="200"/>
                        </td>
                    </tr>
                </table>
            </div>


            <div class="itemDiv biaoj_div2">
                <table class="biaoj_div2_table">
                    <tr>
                        <td align="right">
                            <input type="button" id="nextbtn"
                                   value="<emp:message key='xtgl_spgl_shlcgl_xyb' defVal='下一步' fileName='xtgl'/>"
                                   class="btnClass5 mr10 btnLetter3 indent_none"
                                   onclick="javascript:nextsaveAudit(1);"/>&nbsp;&nbsp;
                            <input type="button" id="savebtn"
                                   value="<emp:message key='xtgl_spgl_shlcgl_bc' defVal='保存' fileName='xtgl'/>"
                                   class="btnClass6 mr10" onclick="javascript:nextsaveAudit(2);"/>&nbsp;&nbsp;
                            <input type="button" id="cancelbtn"
                                   value="<emp:message key='xtgl_spgl_shlcgl_fh' defVal='返回' fileName='xtgl'/>"
                                   class="btnClass6" onclick="javascript:cancelAudit();"/>
                            <br/>
                        </td>
                    </tr>
                </table>
            </div>


        </form>
    </div>
    <%-- 内容结束 --%>
    <%-- foot开始 --%>
    <div class="bottom">
        <div id="bottom_right">
            <div id="bottom_left"></div>
            <div id="bottom_main">
            </div>
        </div>
    </div>
    <%-- foot结束 --%>
    <div id="des-info">
        <h3><emp:message key="xtgl_spgl_shlcgl_spryszsm_mh" defVal="审批人员设置说明：" fileName="xtgl"/></h3>
        <emp:message key="xtgl_spgl_shlcgl_spryszsm_1" defVal=" 1、操作员：选择该操作员作为审核人员。" fileName="xtgl"/><br>
        <emp:message key="xtgl_spgl_shlcgl_spryszsm_2" defVal="2、逐级审核：发送者机构以上级的机构审核员都需要审批，一级一级的审批。"
                     fileName="xtgl"/><br>
        <emp:message key="xtgl_spgl_shlcgl_spryszsm_3" defVal="3、机构：选择该机构作为审核节点，系统自适配机构审核员。" fileName="xtgl"/>
    </div>
</div>
<div class="clear"></div>
<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
<script type="text/javascript"
        src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript"
        src="<%=commonPath%>/common/js/jquery.ztree-myjquery-b.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript"
        src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript"
        src="<%=commonPath %>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript"
        src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript"
        src="<%=commonPath%>/common/js/floating.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=iPath%>/js/addAudit.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/xtgl_<%=langName%>.js"></script>
<script>
    $(document).ready(function () {
        noquot('#flowtask');
        getLoginInfo("#hiddenValueDiv");
        <%--将输入框中的提示数据隐藏--%>
        <%
            if(pathtype != null && "2".equals(pathtype)){
                %>
        $("#idPlaceholder1").css("display", "none");
        <%
    }
%>
        var index =<%=index1 %>;
        if (index == 5) {
            $("#add1").css("visibility", "hidden");
        }
        $('#flowtask').bind('keyup blur', function () {
                var val = $(this).val();
                var reg = /[#$%@&*{}<>\/\^\?']/gi;
                if (reg.test(val)) {
                    $(this).val($(this).val().replace(reg, ''));
                }
            }
        );
    });
</script>
</body>
</html>