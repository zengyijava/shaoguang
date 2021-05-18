<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.montnets.emp.common.constant.ViewParams" %>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils" %>
<%@ page import="com.montnets.emp.mobilebus.constant.MobileBusStaticValue" %>
<%@ page import="com.montnets.emp.util.PageInfo" %>
<%@ page import="org.apache.commons.beanutils.DynaBean" %>
<%@page import="java.util.Iterator" %>
<%@page import="java.util.LinkedHashMap" %>
<%@page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.Map.Entry" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    String iPath = request.getRequestURI().substring(0, request.getRequestURI().lastIndexOf("/"));
    String inheritPath = iPath.substring(0, iPath.lastIndexOf("/"));
    String commonPath = inheritPath.substring(0, inheritPath.lastIndexOf("/"));
    PageInfo pageInfo = new PageInfo();
    pageInfo = (PageInfo) request.getAttribute("pageInfo");
    @SuppressWarnings("unchecked")
    List<DynaBean> recordList = (List<DynaBean>) request.getAttribute("recordList");
    @SuppressWarnings("unchecked")
    LinkedHashMap<String, String> conditionMap = (LinkedHashMap<String, String>) request.getAttribute("conditionMap");
    String state = "";
    if (conditionMap.get("state") != null) {
        state = (String) conditionMap.get("state");
    }
    String depNam = request.getParameter("depNam");
    java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    //*******是否包含子机构*****
    String isContainsSun = (String) request.getAttribute("isContainsSun");
    //************按钮权限处理**************
    @SuppressWarnings("unchecked")
    Map<String, String> btnMap = (Map<String, String>) session.getAttribute("btnMap");
    @SuppressWarnings("unchecked")
    Map<String, String> titleMap = (Map<String, String>) session.getAttribute("titleMap");
    String reTitle = (String) request.getAttribute("rTitle");
    String menuCode = titleMap.get(reTitle);
    menuCode = menuCode == null ? "0-0-0" : menuCode;

    @SuppressWarnings("unchecked")
    String skin = session.getAttribute("stlyeSkin") == null ? "default" : (String) session.getAttribute("stlyeSkin");
    @SuppressWarnings("unchecked")
    LinkedHashMap<String, LinkedHashMap> tailList = (LinkedHashMap<String, LinkedHashMap>) request.getAttribute("taocanList");
%>
<html>
<head>
    <%@include file="/common/common.jsp" %>
    <title></title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet"
          type="text/css"/>
    <link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet"
          type="text/css"/>
    <link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
    <link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
    <link rel="stylesheet"
          href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>"/>
    <link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css">
    <link href="<%=commonPath%>/common/widget/zTreeStyle/zTreeStyle.css?V=<%=StaticValue.getJspImpVersion() %>"
          rel="stylesheet" type="text/css"/>
    <link href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet"
          type="text/css"/>
    <%if (StaticValue.ZH_HK.equals(empLangName)) {%>
    <link rel="stylesheet" type="text/css"
          href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
    <link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
    <%}%>
    <style type="text/css">
        <%if("zh_HK".equals(empLangName)){%>
        #moreInfoFrame {
            display: none;
            width: 800px;
            height: 350px;
            border: 0;
        }

        <%}else{%>
        #moreInfoFrame {
            display: none;
            width: 730px;
            height: 350px;
            border: 0;
        }

        <%}%>
        #condition .c_selectBox {
            width: 208px !important;
        }

        #condition .c_selectBox ul {
            width: 208px !important;
        }

        #condition .c_selectBox ul li {
            width: 208px !important;
        }
    </style>
</head>

<body>
<div id="container" class="container">
    <%-- 当前位置 --%>

    <%=ViewParams.getPosition(empLangName, menuCode) %>

    <%-- 内容开始 --%>
    <div id="rContent" class="rContent">
        <form name="pageForm" action="<%=path %>/ydyw_busTaocanMgr.htm" method="post"
              id="pageForm">
            <input type="hidden" id="lguserid" name="lguserid" value="<%=request.getParameter("lguserid") %>"/>
            <input type="hidden" id="lgcorpcode" name="lgcorpcode" value="<%=request.getParameter("lgcorpcode") %>"/>
            <div class="buttons">
                <div id="toggleDiv">
                </div>
                <%if (btnMap.get(menuCode + "-1") != null) {%>
                <a id="add" href="javascript:toAdd()"><emp:message key="common_establish" fileName="common"
                                                                   defVal="新建"></emp:message></a>
                <%} %>
            </div>
            <input type="hidden" id="pathUrl" value="<%=path %>"/>
            <div id="condition">
                <table>
                    <tbody>
                    <tr>
                        <td>
                            <emp:message key="ydyw_ywgl_ywbgl_text_33" defVal="套餐名称：" fileName="ydyw"></emp:message>
                        </td>
                        <td>
                            <input type="text" maxlength="30"
                                   value="<%=null==conditionMap.get("taocan_name")?"":conditionMap.get("taocan_name") %>"
                                   id="taocan_name" name="taocan_name" style="width: 154px;">
                        </td>
                        <td>
                            <emp:message key="ydyw_ywgl_ywbgl_text_35" defVal="套餐编号：" fileName="ydyw"></emp:message>
                        </td>
                        <td>
                            <input type="text" maxlength="30"
                                   value="<%=null==conditionMap.get("taocan_code")?"":conditionMap.get("taocan_code") %>"
                                   id="taocan_code" name="taocan_code" style="width: 154px;">
                        </td>
                        <td>
                            <emp:message key="ydyw_ywgl_ywbgl_text_31" fileName="ydyw" defVal="状态："></emp:message>
                        </td>
                        <td>
                            <select id="state" name="state" style="width: 154px;" isInput="false">
                                <option value=""><emp:message key="common_whole" defVal="全部"
                                                              fileName="common"></emp:message></option>
                                <option value="0"  <%="0".equals(state) ? "selected" : "" %>><emp:message
                                        key="ydyw_ywgl_ywbgl_text_4" fileName="ydyw"
                                        defVal="已启用"></emp:message></option>
                                <option value="1"  <%="1".equals(state) ? "selected" : "" %>><emp:message
                                        key="ydyw_ywgl_ywbgl_text_5" fileName="ydyw"
                                        defVal="已禁用"></emp:message></option>
                            </select>
                        </td>


                        <td class="tdSer">
                            <center><a id="search"></a></center>
                        </td>
                    </tr>
                    <tr>

                        <td>
                            <emp:message key="ydyw_ywgl_ywbgl_text_37" defVal="计费类型：" fileName="ydyw"></emp:message>
                        </td>
                        <td>

                            <select id="freeType" name="freeType" style="width: 158px;" isInput="false">
                                <option value=""><emp:message key="common_whole" defVal="全部"
                                                              fileName="common"></emp:message></option>
                                <%
                                    Map<String, String> type = MobileBusStaticValue.getTaoCanType();
                                    Iterator it = type.entrySet().iterator();
                                    while (it.hasNext()) {
                                        Map.Entry<String, String> ent = (Map.Entry<String, String>) it.next();
                                        String key = ent.getKey();
                                        String value = ent.getValue();
                                        if ("VIP免费".equals(value)) {
                                            value = MessageUtils.extractMessage("ydyw", "ydyw_ywgl_tczlsz_text_5", request);
                                        } else if ("包月".equals(value)) {
                                            value = MessageUtils.extractMessage("ydyw", "ydyw_ywgl_tczlsz_text_by", request);
                                        } else if ("包季".equals(value)) {
                                            value = MessageUtils.extractMessage("ydyw", "ydyw_ywgl_tczlsz_text_bj", request);
                                        } else if ("包年".equals(value)) {
                                            value = MessageUtils.extractMessage("ydyw", "ydyw_ywgl_tczlsz_text_bn", request);
                                        }
                                        if ("-1".equals(key)) {
                                            continue;
                                        }
                                %>
                                <option value="<%=key %>" <%=key.equals(conditionMap.get("freeType")) ? "selected" : "" %>><%=value %>
                                </option>
                                <%
                                    }
                                %>


                            </select>
                        </td>
                        <td>
                            <emp:message key="ydyw_ywgl_ywbgl_text_3_p" fileName="ydyw" defVal="业务包编号："></emp:message>
                        </td>
                        <td>
                            <input type="text" maxlength="30"
                                   value="<%=null==conditionMap.get("pageckcode")?"":conditionMap.get("pageckcode") %>"
                                   id="pageckcode" name="pageckcode" style="width: 154px;">
                        </td>
                        <td>
                            <emp:message key="ydyw_ywgl_ywbgl_text_24" defVal="操作员：" fileName="ydyw"></emp:message>
                        </td>
                        <td>
                            <input type="text" maxlength="30" id="user" name="user"
                                   value="<%=null==conditionMap.get("user")?"":conditionMap.get("user") %>"
                                   style="width: 154px;"/>
                        </td>
                    </tr>

                    <tr>
                        <td>
                            <emp:message key="ydyw_ywgl_ywbgl_text_26" defVal="机构：" fileName="ydyw"></emp:message>
                        </td>
                        <td class="condi_f_l">
                            <div style="width: 154px;">
                                <input type="hidden" id="deptid" name="deptid"
                                       value="<%=conditionMap.get("deptid")==null?"":conditionMap.get("deptid")%>"/>
                                <input type="text" class="treeInput" id="depNam" name="depNam"
                                       value="<%=depNam==null? MessageUtils.extractMessage("common","common_pleaseSelect",request):depNam%>"
                                       onclick="javascript:showMenu();" readonly style="width:134px;cursor: pointer;"/>
                            </div>

                            <div id="dropMenu">
                                <iframe style="position: absolute; z-index: -1; width: 100%; height: 100%; top: 0; left: 0; scrolling: no;"
                                        frameborder="0" src="about:blank"></iframe>
                                <div style="margin-top: 3px;margin-right:10px;text-align:center">
                                    <input type="checkbox" id="isContainsSun" name="isContainsSun"
                                           <%if(isContainsSun.equals("1")){%>checked="checked" <%}%> value="1"
                                           style="width:15px;height:15px;"/><emp:message key="ydyw_qyjfcx_khtfgl_text_9"
                                                                                         defVal="包含子机构"
                                                                                         fileName="ydyw"></emp:message>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                    &nbsp;&nbsp;&nbsp;&nbsp;<input type="button"
                                                                   value="<emp:message key="common_confirm" defVal="确定" fileName="common"></emp:message>"
                                                                   class="btnClass1"
                                                                   onclick="javascript:zTreeOnClickOK3();" style=""/>&nbsp;&nbsp;
                                    <input type="button"
                                           value="<emp:message key="common_clean" defVal="清空" fileName="common"></emp:message>"
                                           class="btnClass1" onclick="javascript:cleanSelect_dep3();" style=""/>
                                </div>
                                <ul id="dropdownMenu" class="tree"></ul>
                            </div>
                        </td>

                        <td>
                            <emp:message key="ydyw_ywgl_ywbgl_text_28" defVal="创建时间：" fileName="ydyw"></emp:message>
                        </td>
                        <td>
                            <input type="text"
                                   value="<%=null==conditionMap.get("startSubmitTime")?"":conditionMap.get("startSubmitTime") %>"
                                   id="startSubmitTime" name="startSubmitTime"
                                   style="cursor: pointer; width: 154px;background-color: white;" class="Wdate"
                                   readonly="readonly" onclick="stime()">
                        </td>
                        <td align="left">
                            <emp:message key="common_to" defVal="至：" fileName="common"></emp:message>
                        </td>
                        <td>
                            <input type="text"
                                   value="<%=null==conditionMap.get("endSubmitTime")?"":conditionMap.get("endSubmitTime") %>"
                                   id="endSubmitTime" name="endSubmitTime"
                                   style="cursor: pointer; width: 154px;background-color: white;" class="Wdate"
                                   readonly="readonly" onclick="rtime()">
                        </td>
                        <td></td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <table id="content">
                <thead>
                <tr>
                    <th>
                        <emp:message key="ydyw_ywgl_ywbgl_text_32" defVal="套餐名称" fileName="ydyw"></emp:message>
                    </th>
                    <th>
                        <emp:message key="ydyw_ywgl_ywbgl_text_34" defVal="套餐编号" fileName="ydyw"></emp:message>
                    </th>
                    <th>
                        <emp:message key="ydyw_ywgl_ywtcgl_text_1" defVal="包含业务包" fileName="ydyw"></emp:message>
                    </th>
                    <th>
                        <emp:message key="ydyw_ywgl_ywbgl_text_36" defVal="计费类型" fileName="ydyw"></emp:message>
                    </th>
                    <th>
                        <emp:message key="ydyw_qytjbb_jgjftj_text_6" defVal="资费（元）" fileName="ydyw"></emp:message>
                    </th>
                    <th>
                        <emp:message key="ydyw_ywgl_ywbgl_text_42" defVal="扣费时间" fileName="ydyw"></emp:message>
                    </th>
                    <th>
                        <emp:message key="ydyw_ywgl_ywtcgl_text_2" defVal="套餐有效起止时间" fileName="ydyw"></emp:message>
                    </th>
                    <th>
                        <emp:message key="ydyw_ywgl_ywbgl_text_30" fileName="ydyw" defVal="状态"></emp:message>
                    </th>
                    <th>
                        <emp:message key="ydyw_ywgl_ywbgl_text_27" defVal="创建时间" fileName="ydyw"></emp:message>
                    </th>
                    <th>
                        <emp:message key="ydyw_ywgl_ywbgl_text_29" defVal="修改时间" fileName="ydyw"></emp:message>
                    </th>
                    <th>
                        <emp:message key="ydyw_ywgl_ywbgl_text_25" defVal="机构" fileName="ydyw"></emp:message>
                    </th>
                    <th>
                        <emp:message key="ydyw_ywgl_ywbgl_text_23" defVal="操作员" fileName="ydyw"></emp:message>
                    </th>
                    <th>
                        <emp:message key="common_operation" defVal="操作" fileName="common"></emp:message>
                    </th>
                </tr>
                </thead>
                <tbody>
                <%
                    if (recordList != null && recordList.size() > 0) {
                        for (int k = 0; k < recordList.size(); k++) {
                            DynaBean db = recordList.get(k);
                            //获得套餐编号
                            String taocan_code = db.get("taocan_code").toString();
                            String str = "";
                            if (tailList != null) {
                                LinkedHashMap nameList = tailList.get(taocan_code);
                                if (nameList != null) {
                                    Iterator itr = nameList.entrySet().iterator();
                                    while (itr.hasNext()) {
                                        Map.Entry entity = (Entry) itr.next();
                                        str = str + entity.getValue() + "(" + entity.getKey() + ")" + ",";
                                    }
                                }
                            }
                %>
                <tr align="center">
                    <td>
                        <% String taocan_name = db.get("taocan_name").toString();%>
                        <a onclick=javascript:showName(this)>
                            <label style="display:none">
                                <xmp><%=taocan_name%>
                                </xmp>
                            </label>
                            <xmp><%=taocan_name.length() > 5 ? taocan_name.toString().substring(0, 5) + "..." : taocan_name%>
                            </xmp>
                        </a>
                    </td>
                    <td>

                        <a onclick=javascript:showCode(this)>
                            <label style="display:none">
                                <xmp><%=taocan_code%>
                                </xmp>
                            </label>
                            <xmp><%=taocan_code.length() > 5 ? taocan_code.substring(0, 5) + "..." : taocan_code %>
                            </xmp>
                        </a>

                    </td>
                    <td>

                        <%
                            if (!"".equals(str)) {
                                str = str.substring(0, str.length() - 1);
                            }
                        %>
                        <a onclick=javascript:showTaoCan(this)>
                            <label style="display:none">
                                <xmp><%=str %>
                                </xmp>
                            </label>
                            <xmp><%=str.length() > 5 ? str.substring(0, 5) + "..." : str %>
                            </xmp>
                        </a>
                    </td>
                    <td>

                        <%
                            if (db.get("taocan_type") != null) {
                                Map<String, String> typeMap = MobileBusStaticValue.getTaoCanType();
                                String key = db.get("taocan_type").toString();
                                String name = typeMap.get(key);
                                if ("VIP免费".equals(name)) {
                                    name = MessageUtils.extractMessage("ydyw", "ydyw_ywgl_tczlsz_text_5", request);
                                } else if ("包月".equals(name)) {
                                    name = MessageUtils.extractMessage("ydyw", "ydyw_ywgl_tczlsz_text_by", request);
                                } else if ("包季".equals(name)) {
                                    name = MessageUtils.extractMessage("ydyw", "ydyw_ywgl_tczlsz_text_bj", request);
                                } else if ("包年".equals(name)) {
                                    name = MessageUtils.extractMessage("ydyw", "ydyw_ywgl_tczlsz_text_bn", request);
                                }
                                if (name != null) {
                                    out.print(name);
                                }
                            }
                        %>
                    </td>
                    <td>
                        <%=db.get("taocan_money") == null ? "" : db.get("taocan_money")%>
                    </td>
                    <td>

                        <%
                            if (db.get("buckle_type") != null) {
                                String buckletype = db.get("buckle_type").toString();
                                String buckledate = "";
                                if (db.get("buckle_date") != null) {
                                    buckledate = db.get("buckle_date").toString();
                                }
                                if ("zh_HK".equals(empLangName)) {
                                    switch (Integer.parseInt(buckledate)) {
                                        case 1:
                                            buckledate += "st";
                                            break;
                                        case 2:
                                            buckledate += "nd";
                                            break;
                                        case 3:
                                            buckledate += "rd";
                                            break;
                                        default:
                                            buckledate += "th";
                                    }
                                    if (("1").equals(buckletype)) {
                                        /*订购生效次月 号开始扣费*/
                                        out.print(buckledate + MessageUtils.extractMessage("ydyw", "ydyw_ywgl_ywtcgl_text_4", request));
                                    } else if (("2").equals(buckletype)) {
                                        /*订购生效当天开始扣费*/
                                        out.print(MessageUtils.extractMessage("ydyw", "ydyw_ywgl_ywtcgl_text_6", request));
                                    } else if (("3").equals(buckletype)) {
                                        /*订购生效当月 号开始扣费*/
                                        out.print(buckledate + MessageUtils.extractMessage("ydyw", "ydyw_ywgl_ywtcgl_text_7", request));
                                    } else {
                                        out.print("-");
                                    }
                                } else {
                                    if (("1").equals(buckletype)) {
                                        /*订购生效次月 号开始扣费*/
                                        out.print(MessageUtils.extractMessage("ydyw", "ydyw_ywgl_ywtcgl_text_4", request) + buckledate + MessageUtils.extractMessage("ydyw", "ydyw_ywgl_ywtcgl_text_5", request));
                                    } else if (("2").equals(buckletype)) {
                                        /*订购生效当天开始扣费*/
                                        out.print(MessageUtils.extractMessage("ydyw", "ydyw_ywgl_ywtcgl_text_6", request));
                                    } else if (("3").equals(buckletype)) {
                                        /*订购生效当月 号开始扣费*/
                                        out.print(MessageUtils.extractMessage("ydyw", "ydyw_ywgl_ywtcgl_text_7", request) + buckledate + MessageUtils.extractMessage("ydyw", "ydyw_ywgl_ywtcgl_text_5", request));
                                    } else {
                                        out.print("-");
                                    }
                                }
                            } else {
                                out.print("-");
                            }


                        %>
                    </td>
                    <td>
                        <%
                            String start_date = "";
                            String end_date = "";
                            if (db.get("start_date") != null && db.get("end_date") != null) {
                                String start = db.get("start_date").toString();
                                String[] first = start.split(" ");

                                if (first.length == 2) {
                                    start_date = first[0];
                                }
                                String end = db.get("end_date").toString();
                                String[] ends = end.split(" ");

                                if (ends.length == 2) {
                                    end_date = ends[0];
                                }
                            }
                            if (!"".equals(start_date) && !"".equals(end_date)) {
                                out.println(start_date + " " + MessageUtils.extractMessage("common", "text_to_p", request) + " " + end_date);
                            }
                        %>
                    </td>
                    <td>
                        <%
                            if (db.get("state") != null) {
                                String sta = db.get("state").toString();
                                if ("0".equals(sta)) {
                                    out.print(MessageUtils.extractMessage("ydyw", "ydyw_ywgl_ywbgl_text_4", request));
                                } else if ("1".equals(sta)) {
                                    out.print(MessageUtils.extractMessage("ydyw", "ydyw_ywgl_ywbgl_text_5", request));
                                }
                            }


                        %>
                    </td>
                    <td>
                        <%=db.get("create_time") == null ? "-" : db.get("create_time").toString().substring(0, db.get("create_time").toString().lastIndexOf("."))%>
                    </td>
                    <td>
                        <%=db.get("update_time") == null ? "-" : db.get("update_time").toString().substring(0, db.get("update_time").toString().lastIndexOf("."))%>
                    </td>
                    <td>
                        <%=db.get("dep_name") == null ? "" : db.get("dep_name")%>
                    </td>
                    <td>
                        <%=db.get("name") == null ? "" : db.get("name")%>
                        (<%=db.get("user_name") == null ? "" : db.get("user_name")%>)
                    </td>
                    <td>
                        <a id="modif"
                           href="javascript:moreInfo('<%=db.get("taocan_code")%>','<%=empLangName%>')"><emp:message
                                key="ydyw_qyjfcx_khjfcx_text_10" defVal="更多详情" fileName="ydyw"></emp:message></a>
                        <%if (btnMap.get(menuCode + "-2") != null) {%>
                        <a id="modif" href="javascript:toModif('<%=db.get("taocan_code")%>')"><emp:message
                                key="common_modify" defVal="修改" fileName="common"></emp:message></a>
                        <%} %>
                    </td>
                </tr>
                <%
                    }
                } else {
                %>
                <tr>
                    <td colspan="13"><emp:message key="common_norecord" defVal="无记录"
                                                  fileName="common"></emp:message></td>
                </tr>
                <%} %>

                </tbody>
                <tfoot>
                <tr>
                    <td colspan="13">
                        <div id="pageInfo"></div>
                    </td>
                </tr>
                </tfoot>
            </table>

        </form>
    </div>
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
<div class="clear"></div>
<div id="nameInfo" title="<emp:message key="ydyw_ywgl_ywbgl_text_32" defVal="套餐名称" fileName="ydyw"></emp:message>"
     style="padding:5px;width:300px;height:160px;display:none;word-wrap: break-word;word-break:break-all;">
    <table width="240px">
        <thead>
        <tr style="padding-top:2px;margin-bottom: 2px;">
            <td>
                <span style="display:block;width:240px;"><label id="nameMsg"
                                                                style="width:100%;height:100%;word-break: break-all;white-space:normal;"></label></span>
            </td>
        </tr>
        <tr style="padding-top:2px;">
            <td>
            </td>
        </tr>
        </thead>
    </table>
</div>

<div id="codeInfo" title="<emp:message key="ydyw_ywgl_ywbgl_text_34" defVal="套餐编号" fileName="ydyw"></emp:message>"
     style="padding:5px;width:300px;height:160px;display:none;word-wrap: break-word;word-break:break-all;">
    <table width="240px">
        <thead>
        <tr style="padding-top:2px;margin-bottom: 2px;">
            <td>
                <span style="display:block;width:240px;"><label id="codeMsg"
                                                                style="width:100%;height:100%;word-break: break-all;white-space:normal;"></label></span>
            </td>
        </tr>
        <tr style="padding-top:2px;">
            <td>
            </td>
        </tr>
        </thead>
    </table>
</div>

<div id="buslist" title="<emp:message key="ydyw_ywgl_ywtcgl_text_1" defVal="包含业务包" fileName="ydyw"></emp:message>"
     style="padding:5px;width:200px;height:160px;display:none;word-wrap: break-word;word-break:break-all;overflow-x:hidden;overflow-y:auto;">
    <table width="240px">
        <thead>
        <tr style="padding-top:2px;margin-bottom: 2px;">
            <td>
                <span style="display:block;width:240px;"><label id="msgshow2"
                                                                style="width:100%;height:100%;word-break: break-all;white-space:normal;"></label></span>
            </td>
        </tr>
        <tr style="padding-top:2px;">
            <td>
            </td>
        </tr>
        </thead>
    </table>
</div>
<div id="moreInfoDiv"
     title="<emp:message key="ydyw_ywgl_ywtcgl_text_3" defVal="业务套餐详细信息" fileName="common"></emp:message>"
     style="padding:5px;display:none">
    <iframe id="moreInfoFrame" name="moreInfoFrame" marginwidth="0" scrolling="no" frameborder="no"></iframe>
    <div align="center">
        <input type="reset" value="<emp:message key="common_btn_10" defVal="返回" fileName="common"></emp:message>"
               class="btnClass6" onclick="javascript:closeInfo()"/>
    </div>
</div>

<script type="text/javascript"
        src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript"
        src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript"
        src="<%=commonPath%>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript"
        src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript"
        src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript"
        src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript"
        src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript"
        src="<%=commonPath%>/common/js/jquery.ztree-myjquery-b.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=iPath%>/js/taocanList.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=empLangName%>/ydyw_<%=empLangName%>.js"></script>
<script>
    var zTree3;
    var setting3;
    var zNodes3 = [];
    var lguserid = $("#lguserid").val();
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
            $(this).addClass("hoverColor");
        }, function () {
            $(this).removeClass("hoverColor");
        });
        //翻页
        initPage(<%=pageInfo.getTotalPage()%>, <%=pageInfo.getPageIndex()%>, <%=pageInfo.getPageSize()%>, <%=pageInfo.getTotalRec()%>);
        //查询
        $('#search').click(function () {
            submitForm();
        });
        $("#moreInfoDiv").dialog({
            autoOpen: false,
            height: 440,
            width: 730,
            title: getJsLocaleMessage("ydyw", "ydyw_ywgl_ywtcgl_text_3"),
            modal: true,
            resizable: false
        });

        setting3.asyncUrl = "ydyw_busTaocanMgr.htm?method=createDeptTree&lguserid=" + lguserid;
        reloadTree(zNodes3);

    });
</script>
</body>
</html>
