<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@ page language="java" import="com.montnets.emp.entity.blacklist.PbListBlack" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>

<%@ page import="com.montnets.emp.entity.pasgroup.Userdata" %>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils" %>
<%@ page import="com.montnets.emp.security.wgmsgconfig.WgMsgConfigBiz" %>
<%@page import="com.montnets.emp.util.PageInfo" %>
<%@page import="com.montnets.emp.util.PhoneUtil" %>
<%@page import="java.util.LinkedHashMap" %>
<%@page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
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
    Map<String, String> btnMap = (Map<String, String>) session.getAttribute("btnMap");//按钮权限Map

    @SuppressWarnings("unchecked")
    Map<String, String> titleMap = (Map<String, String>) session.getAttribute("titleMap");
    String menuCode = titleMap.get("blacklistSvt");
    menuCode = menuCode == null ? "0-0-0" : menuCode;
    @SuppressWarnings("unchecked")
    List<PbListBlack> pbList = (List<PbListBlack>) request.getAttribute("blackList");
    @SuppressWarnings("unchecked")
    Map<Long, String> keyIdMap = (Map<Long, String>) request.getAttribute("keyIdMap");
    @SuppressWarnings("unchecked")
    LinkedHashMap<String, String> conditionMap = (LinkedHashMap<String, String>) request.getAttribute("conditionMap");
//@ SuppressWarnings("unchecked")
//List<XtGateQueue> xtList =(List<XtGateQueue>)session.getAttribute("mrXtList");
    @SuppressWarnings("unchecked")
    List<Userdata> userList = (List<Userdata>) session.getAttribute("mrUserList");

    String spgate = conditionMap.get("spgate");
    @SuppressWarnings("unchecked")
    Map<String, String> busMap = (LinkedHashMap<String, String>) request.getAttribute("busMap");

    String findResult = (String) request.getAttribute("findresult");
    String uploadresult = (String) session.getAttribute("uploadresult");
    String skin = session.getAttribute("stlyeSkin") == null ? "default" : (String) session.getAttribute("stlyeSkin");
    String txglFrame = skin.replace(commonPath, inheritPath);


    String langName = (String) session.getAttribute(StaticValue.LANG_KEY);
//修改号码
    String xghm = MessageUtils.extractMessage("txgl", "txgl_wghdpz_dxhmd_xghm", request);
//新建短信黑名单
    String xjdxhmd = MessageUtils.extractMessage("txgl", "txgl_wghdpz_dxhmd_xjdxhmd", request);
//确定
    String qd = MessageUtils.extractMessage("common", "common_confirm", request);
//取消
    String qx = MessageUtils.extractMessage("txgl", "txgl_wghdpz_gjzsz_qx", request);
//导入短信黑名单
    String drdxhmd = MessageUtils.extractMessage("txgl", "txgl_wghdpz_dxhmd_drdxhmd", request);
//导出短信黑名单
    String dcdxhmd = MessageUtils.extractMessage("txgl", "txgl_wghdpz_dxhmd_dcdxhmd", request);
//EXCEL格式
    String egs = MessageUtils.extractMessage("txgl", "txgl_wghdpz_dxhmd_egs", request);
    //TXT格式
    String tgs = MessageUtils.extractMessage("txgl", "txgl_wghdpz_dxhmd_tgs", request);
    //手机号码
    String sjhm = MessageUtils.extractMessage("txgl", "txgl_wghdpz_dxhmd_sjhm", request);
    if (sjhm != null && sjhm.length() > 1) {
        sjhm = sjhm.substring(0, sjhm.length() - 1);
    }
    //业务名称
    String ywmc = MessageUtils.extractMessage("txgl", "txgl_wghdpz_dxhmd_ywmc", request);
    if (ywmc != null && ywmc.length() > 1) {
        ywmc = ywmc.substring(0, ywmc.length() - 1);
    }
//添加显示列(创建时间、备注)
//创建时间
    String cjsj = MessageUtils.extractMessage("txgl", "txgl_wgqdpz_dlzhgl_cjsj", request);//txgl_wgqdpz_dlzhgl_cjsj=创建时间
//备注
    String bz = MessageUtils.extractMessage("txgl", "txgl_wgqdpz_spzhczhs_bz", request);//txgl_wgqdpz_spzhczhs_bz=备注
    java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//删除记录  txgl_wghdpz_gjzsz_scjl
    String scjl = MessageUtils.extractMessage("txgl", "txgl_wghdpz_gjzsz_scjl", request);
%>
<html>
<head>
    <%@include file="/common/common.jsp" %>
    <title></title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet"
          type="text/css"/>
    <link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet"
          type="text/css"/>
    <link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css"/>
    <link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css"/>
    <link href="<%=commonPath%>/common/css/empSelect.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet"
          type="text/css"/>
    <link href="<%=skin %>/empSelect.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css"/>
    <link rel="stylesheet"
          href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion()%>"/>
    <link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion()%>" type="text/css">
    <link rel="stylesheet" type="text/css"
          href="<%=commonPath%>/common/css/file.css?V=<%=StaticValue.getJspImpVersion() %>"/>
    <%if (StaticValue.ZH_HK.equals(empLangName)) {%>
    <link rel="stylesheet" type="text/css"
          href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
    <link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
    <%}%>

    <link rel="stylesheet" type="text/css"
          href="<%=iPath%>/css/bla_blacklist.css?V=<%=StaticValue.getJspImpVersion() %>"/>
    <link rel="stylesheet" type="text/css" href="<%=skin %>/txgl.css?V=<%=StaticValue.getJspImpVersion() %>"/>
    <!-- new add link -->
    <link href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet"
          type="text/css"/>
    <link href="<%=commonPath%>/cxtj/common/css/inbox.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet"
          type="text/css"/>
</head>
<body id="bla_blacklist">
<div id="container" class="container">
    <%-- 当前位置 --%>
    <%=com.montnets.emp.common.constant.ViewParams.getPosition(langName, menuCode) %>

    <div id="modify" title="<%=xghm %>" class="modify">
        <emp:message key="txgl_wghdpz_dxhmd_sjhm" defVal="手机号码：" fileName="txgl"></emp:message>
        <input type="text"   value=""
				             id="mobile1"
				             maxlength="11"
				             onkeyup="numberControl($(this))"
				             onblur="numberControl($(this))"/>
        <input type="hidden" value="" id="blId"/>
        <input type="hidden" value="" id="spisuncm"/>
        <input type="hidden" value="" id="blBusCode"/>
        <input type="hidden" value="" id="blSpgate"/>
        <input type="hidden" id="pnoticeSize" name="pnoticeSize" value="<%=pbList != null ? pbList.size():0%>"/>
        <input type="hidden" id="totalSize" name="totalSize" value="<%=pageInfo != null ? pageInfo.getTotalRec():0%>"/>
    </div>
    <!-- 新增短信黑名单的div -->
    <div id="addmmsBl" title="<%=xjdxhmd %>" class="addmmsBl">
        <div class="addmmsBl_down_div">
            <center>
                <table>
                    <!-- 业务名称 添加搜索组件-->
                    <tr class="ywmc_mh_tr">
                        <td>
	                         		<span>
	                         		<emp:message key="txgl_wghdpz_dxhmd_ywmc" defVal="业务名称："
                                                 fileName="txgl"></emp:message>
	                         		</span>
                        </td>
                        <td align="left">
                            <select name="addbusCode" id="addbusCode" class="input_bd addbusCode">
                                <option value="">
                                	<emp:message key="txgl_wghdpz_dxhmd_qxzywmc" defVal="请选择业务名称" fileName="txgl"></emp:message>
                                </option>
                                <%
                                    if (busMap != null && busMap.size() > 0) {
                                        Object s[] = busMap.keySet().toArray();

                                        for (int i = 0; i < busMap.size(); i++) {
                                %>
                                <option value="<%=s[i]%>"><%=StringEscapeUtils.unescapeHtml((busMap.get(s[i]).toString())) %>(<%=s[i] %>)
                                </option>
                                <%
                                        }
                                    }
                                %>
                            </select>
                        </td>
                    </tr>
                    <!-- 手机号码 -->
                    <tr>
                        <td class="definitionDiv_table_td_ver">
                            <emp:message key="txgl_wghdpz_dxhmd_sjhm" defVal="手机号码：" fileName="xtgl"/>
                        </td>
                        <td>
                            <input type="text" name="mobile" id="mobile" maxlength="21" onblur="phoneInputCtrl($(this))"
                                   onkeyup="phoneInputCtrl($(this))" class="input_bd mobile"/>
                        </td>
                    </tr>
                    <!-- 备注信息 -->
                    <tr>
                        <td class="definitionDiv_table_tr_td"></td>
                    </tr>
                    <tr>
                        <td class="definitionDiv_table_td_ver_msg">
                            <emp:message key="txgl_wgqdpz_spzhczhs_bz" defVal="备注：" fileName="xtgl">：</emp:message>
                        </td>
                        <td>
                            <textarea id="msgNote" name="msgNote" class="input_bd msgNote" maxlength="120"></textarea>
                        </td>
                    </tr>
                    <tr >
						<td align="center"  colspan="2" style="height: 20px;">
							<span><font color="red">注：备注不超过120位</font></span>
						</td>
					</tr>
                </table>
            </center>
        </div>
        <div class="blok_div">
            <center>
                <input id="blok" class="btnClass5 mr23" type="button" value="<%=qd %>"
                       onclick="javascript:checkVal('add')"/>
                <input id="blc" onclick="javascript:btcancel();" class="btnClass6" type="button" value="<%=qx %>"/>
                <br/>
            </center>
        </div>
    </div>
    <div id="uploadmmsBl" title="<%=drdxhmd %>" class="uploadmmsBl">
        <form name="updateInfo" method="post" action="bla_blacklistSvt.htm?method=update" enctype="multipart/form-data">
            <div id="blloginparams1" class="hidden"></div>
            <div class="blloginparams1_down_div">
                <center>
                    <table class="blloginparams1_down_div_table">
                        <tr>
                            <td>
								<span class="ywmc_span">
									<emp:message key="txgl_wghdpz_dxhmd_ywmc" defVal="业务名称："
                                                 fileName="txgl"></emp:message>
								</span>
                            </td>
                            <td>
                                <select id="uploadbusCode" name="uploadbusCode" class="input_bd uploadbusCode">
                                    <option value="">
                                    	<emp:message key="txgl_wghdpz_dxhmd_qxzywmc" defVal="请选择业务名称" fileName="txgl"></emp:message>
                                    </option>
                                    <%
                                        if (busMap != null && busMap.size() > 0) {
                                            Object s[] = busMap.keySet().toArray();

                                            for (int i = 0; i < busMap.size(); i++) {
                                    %>
                                    <option value="<%=s[i]%>">
                                    	<%=StringEscapeUtils.unescapeHtml(busMap.get(s[i]).toString()) %>(<%=s[i] %>)
                                    </option>
                                    <%
                                            }
                                        }
                                    %>
                                </select>
                            </td>
                        </tr>
                        <tr class="xzwj_up_tr">
                            <td colspan="3"></td>
                        </tr>
                        <tr>
                            <td><span class="xzwj_span"><emp:message key="txgl_wghdpz_dxhmd_xzwj" defVal="选择文件："
                                                                     fileName="txgl"></emp:message></span></td>
                            <td>
                                <div>
                                    <a href="javascript:;" class="file"><emp:message key='wxgl_button_14' defVal='浏览'
                                                                                     fileName='wxgl'/>
                                        <input type="file" name="phone1" id="phone1">
                                    </a>
                                    <p id="filename"></p>
                                </div>
                        </tr>
                        <tr class="zmh_up_tr">
                            <td colspan="3"></td>
                        </tr>
                        <tr>
                            <td class="<%="zh_HK".equals(empLangName)?"zmh_td1":"zmh_td2"%>"><emp:message
                                    key="txgl_wgqdpz_spzhczhs_z" defVal="注：" fileName="txgl"></emp:message></td>
                            <td class="z1zcetwjgssc_td"><emp:message key="txgl_wghdpz_dxhmd_z1zcetwjgssc"
                                                                     defVal="1、支持Excel、txt文件格式上传，单次上传不超过20万；"
                                                                     fileName="txgl"></emp:message></td>
                        </tr>
                        <tr>
                            <td></td>
                            <td class="2xtzdglgsbzq_td"><emp:message key="txgl_wghdpz_dxhmd_2xtzdglgsbzq"
                                                                     defVal="2、系统自动过滤格式不正确或不属于运营商号段的手机号码；"
                                                                     fileName="txgl"></emp:message></td>
                        </tr>
                        <tr>
                            <td></td>
                            <td class="3xtzdglxtywlx_td"><emp:message key="txgl_wghdpz_dxhmd_3xtzdglxtywlx"
                                                                      defVal="3、系统自动过滤相同业务类型中重复的手机号码。"
                                                                      fileName="txgl"></emp:message></td>
                        </tr>
                    </table>
                </center>
            </div>
            <div class="impfont_div">
                <center>
                    <font id="impfont" color="#F1F1F9;" class="impfont"><emp:message key="txgl_wghdpz_dxhmd_zzdrzqsh"
                                                                                     defVal="正在导入中,请稍后..."
                                                                                     fileName="txgl"></emp:message></font>
                    <input id="bloks" class="btnClass5 mr23" type="button" value="<%=qd %>"
                           onclick="javascript:checkVal('upload')"/>
                    <input id="blcs" onclick="javascript:btcancel();" class="btnClass6" type="button" value="<%=qx %>"/>
                    <br/>
                </center>
            </div>
        </form>
    </div>
    <div id="exportDiv" title="<%=dcdxhmd %>" class="exportDiv">
				<span class="<%="zh_HK".equals(empLangName)?"excelBut_span1":"excelBut_span2"%>">
					<input id="excelBut" class="btnClass4" type="button" value="<%=egs %>"
                           onclick="javascript:exportExcel()"/>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<input id="txtBut" class="btnClass4" type="button" value="<%=tgs %>"
                           onclick="javascript:exportTxt()"/>
				</span>
        <span>
			<font id="outfont" color="#F1F1F9;"
                  class="<%="zh_HK".equals(empLangName)?"outfont1":"outfont2"%>"><emp:message
                    key="txgl_wghdpz_dxhmd_zzdczqsh" defVal="正在导出中,请稍后..." fileName="txgl"></emp:message></font>
			</span>
        <span>
			<font class="zhu qxzdcgscg"><emp:message key="txgl_wghdpz_dxhmd_qxzdcgscg"
                                                     defVal="请选择导出格式，超过50万以上数据，请您选择TXT格式。"
                                                     fileName="txgl"></emp:message></font>
			</span>
    </div>

    <%-- 内容开始 --%>
    <%if (btnMap.get(menuCode + "-0") != null) { %>
    <div id="rContent" class="rContent">
        <form name="pageForm" action="bla_blacklistSvt.htm?method=find" method="post"
              id="pageForm">
            <div class="buttons">
                <div id="toggleDiv">
                </div>
                <%if (btnMap.get(menuCode + "-1") != null) { %>
	                <a id="add" onclick="javascript:doadd()">
	                	<emp:message key="txgl_wgqdpz_qyhdgl_xj" defVal="新建" fileName="txgl"></emp:message>
	                </a>
	                <a id="upload" onclick="javascript:doupload()">
	                	<emp:message key="txgl_wghdpz_gjzsz_dr" defVal="导入" fileName="txgl"></emp:message>
	                </a>
                <%} %>
                <%if (btnMap.get(menuCode + "-4") != null) { %>
	                <a id="exportCondition" onclick="javascript:exportAll()">
	                	<emp:message key="txgl_wghdpz_dxhmd_dc" defVal="导出" fileName="txgl"></emp:message>
	                </a>
                <%} %>
                <%-- 添加权限验证，只有具备‘删除记录’的权限才可以浏览 --%>
                <%if (btnMap.get(menuCode + "-5") != null) { %>
	                <a id="deleteRecord" onclick="javascript:findDeleteRecord()">
	                	<emp:message key="txgl_wghdpz_gjzsz_scjl" defVal="删除记录" fileName="txgl"></emp:message>
	                </a>
                <%} %>

                <%if (btnMap.get(menuCode + "-2") != null) { %>
	                <a id="delete" onclick="javascript:delAll()">
	                	<emp:message key="txgl_wghdpz_gjzsz_sc" defVal="删除" fileName="txgl"></emp:message>
	                </a>
                <%} %>
            </div>
            <div id="condition">
                <table>
                    <tr>
                        <td>
                            <emp:message key="txgl_wghdpz_dxhmd_sjhm" defVal="手机号码：" fileName="txgl"></emp:message>
                        </td>
                        <td>

                            <input name="phone" id="phone" type="text" class="phone" onkeyup="phoneInputCtrl($(this))"
                                   onblur="phoneInputCtrl($(this))"
                                   maxlength="21"
                                   value="<%=request.getParameter("phone")==null?"":request.getParameter("phone") %>"/>
                        </td>
                        <td><span><emp:message key="txgl_wghdpz_dxhmd_ywmc" defVal="业务名称："
                                               fileName="txgl"></emp:message></span></td>
                        <td>
                            <select id="busCode" name="busCode" class="busCode">
                                <option value="">
                                	<emp:message key="txgl_wghdpz_gjzsz_qb" defVal="全部" fileName="txgl"></emp:message>
                                </option>
                                <%
                                    if (busMap != null && busMap.size() > 0) {
                                        Object s[] = busMap.keySet().toArray();

                                        for (int i = 0; i < busMap.size(); i++) {
                                %>
                                <option value="<%=s[i]%>"
                                        <% if (conditionMap.get("svrType") != null
                                        	   && ((" ".equals(conditionMap.get("svrType")) && "@".equals(s[i]))|| conditionMap.get("svrType").equals(s[i]))) {
                                            out.print("selected=\"selected\"");
                                        } %>>
                                        <%=StringEscapeUtils.unescapeHtml(busMap.get(s[i]).toString()) %>(<%=s[i] %>)
                                </option>
                                <%
                                        }
                                    }
                                %>
                            </select>
                        </td>
                        <td class="tdSer">
                            <center><a id="search"></a></center>
                        </td>
                    </tr>
                </table>
            </div>
            <table id="content">
                <thead>
                <tr>
                    <th class="content_th1">
                        <input type="checkbox" name="dels" value="" onclick="checkAlls(this,'checklist')"/>
                    </th>
                    <%--									<th> --%>
                    <%--										通道号码--%>
                    <%--									</th>--%>
                    <%--									<th>--%>
                    <%--										运营商--%>
                    <%--									</th>--%>
                    <th class="content_th2">
                        <%=sjhm %>
                    </th>
                    <th class="content_th3">
                        <%=ywmc %>
                    </th>
                    <th class="content_th5">
                        <%=cjsj %>
                    </th>
                    <th class="content_th4">
                        <%=bz %>
                    </th>
                    <th class="content_th6">
                        <emp:message key="txgl_wghdpz_gjzsz_cz" defVal="操作" fileName="txgl"></emp:message>
                    </th>
                </tr>
                </thead>
                <tbody>
                <%
                    if (pbList != null && pbList.size() > 0) {
                        PhoneUtil phoneUtil = new PhoneUtil();
                        String[] haoduan = new WgMsgConfigBiz().getHaoduan();
                        String keyId;
                        for (PbListBlack pb : pbList) {
                            keyId = keyIdMap.get(pb.getId());
                %>
                <tr>
                    <td>
                        <input type="checkbox" name="checklist" value="<%=keyId %>"/>
                    </td>
                    <%--								<td><%=pb.getSpgate()%></td>--%>
                    <%--								<td>--%>
                    <%--									<%--%>
                    <%--										String spisuncm=pb.getSpisuncm().toString();--%>
                    <%--									if("0".equals(spisuncm))--%>
                    <%--										out.print("移动");--%>
                    <%--									if("1".equals(spisuncm))--%>
                    <%--										out.print("联通");--%>
                    <%--									if("21".equals(spisuncm))--%>
                    <%--										out.print("电信");--%>
                    <%--									%>--%>
                    <%--								</td>--%>
                    <td>
                        <%
                            String phone = String.valueOf(pb.getPhone());
                            if (phoneUtil.getPhoneType(phone, haoduan) + 1 == 0) {
                                phone = "00" + phone;
                            }
                        %>
                        <label id="la<%=pb.getId() %>"><%=phone%>
                        </label>
                    </td>
                    <%-- 手机号 去掉 class="textalign" --%>
                    <td>
                        <% String busCode = " ".equals(pb.getSvrType()) ? "@" : pb.getSvrType(); %>
                        <xmp><%=busMap.get(busCode) == null ? "-" : busMap.get(busCode).toString() %>
                        </xmp>
                    </td>
                    <%-- 创建时间 --%>
                    <td>
                        <%=df.format(pb.getOptTime()) %>
                    </td>
                    <%-- 备注 --%>
                    <td>
                        <%=pb.getMsg() %>
                    </td>
                    <%--<td class="ztalign">
                        <center>
                           <%
                                if(pb.getBlState()==1)
                                {
                            %>
                                <select  name="blState<%=pb.getBlId() %>" id="blState<%=pb.getBlId() %>" class="input_bd" onchange="javascript:changeState(<%=pb.getBlId()%>)">
                                  <option value="1" selected="selected">已启用</option>
                                  <option value="0" >禁用</option>
                                </select>
                            <%
                                }else
                                {
                            %>
                               <select  name="blState<%=pb.getBlId()%>" id="blState<%=pb.getBlId()%>" class="input_bd" onchange="javascript:changeState(<%=pb.getBlId()%>)">
                                  <option value="0" selected="selected">已禁用</option>
                                  <option value="1" >启用</option>
                                </select>
                            <%
                                }
                             %>
                        </center>
                    </td> --%>
                    <td>
                        <%if (btnMap.get(menuCode + "-2") != null) { %>
                        <a href="javascript:del(<%=pb.getId()%>,'<%=pb.getPhone()%>','<%=pb.getSvrType() %>','<%=keyId %>')"><emp:message
                                key="txgl_wghdpz_gjzsz_sc" defVal="删除" fileName="txgl"></emp:message></a>
                        <%} %>
                    </td>
                </tr>
                <% }
                } else {
                %>
                <tr>
                    <Td colspan="6"><emp:message key="txgl_wghdpz_gjzsz_wjl" defVal="无记录"
                                                 fileName="txgl"></emp:message></Td>
                </tr>
                <%} %>
                </tbody>
                <tfoot>
                <tr>
                    <td colspan="6">
                        <div id="pageInfo"></div>
                    </td>
                </tr>
                </tfoot>
            </table>
            <div id="blloginparams" class="hidden"></div>
        </form>
    </div>
    <%} %>
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
</div>
<div class="clear"></div>
<script type="text/javascript">
    var cond_phone = '<%=request.getParameter("phone")==null?"":request.getParameter("phone") %>';
    var cond_buscode = '<%=conditionMap.get("svrType")==null?"":conditionMap.get("svrType")%>';
</script>
<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/txgl_<%=langName%>.js"></script>
<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
<script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
<script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
<script type="text/javascript" src="<%=commonPath%>/common/js/empSelect.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
<script type="text/javascript" src="<%=iPath %>/js/blacklist.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion() %>"></script>

<script type="text/javascript">
    function findDeleteRecord() {
        //请求黑明单删除记录,第一次请求
        window.location = "bla_blacklistSvt.htm?method=findAllDelete";
    }

    var maxCount = "<%=StaticValue.getBlackMaxcount()%>";
    $(document).ready(function () {
        getLoginInfo("#blloginparams");
        getLoginInfo("#blloginparams1");
        var findresult = "<%=findResult%>";

        if (findresult == "-1") {
            //alert("加载页面失败,请检查网络是否正常!");
            alert(getJsLocaleMessage("txgl", "txgl_wghdpz_gjzsz_text_1"));
            return;
        }
        noquot("#phone");

        var uploadresult = "<%=uploadresult%>";

        if (uploadresult.indexOf("true") == 0) {
            //alert("新建成功,有效黑名单号码个数为："+uploadresult.substr(4)+"！");
            alert(getJsLocaleMessage("txgl", "txgl_wghdpz_dxhmd_text_1") + uploadresult.substr(4) + "！");
            black();
        }
        else if (uploadresult == "noPhone") {
            //alert("没有有效的黑名单记录可以添加！");
            alert(getJsLocaleMessage("txgl", "txgl_wghdpz_dxhmd_text_2"));
        } else if (uploadresult == "overCount") {
            //alert("上传号码个数超过20万，请分批次重新上传！");
            alert(getJsLocaleMessage("txgl", "txgl_wghdpz_dxhmd_text_3"));
        } else if (uploadresult == "false") {
            //alert("新建失败！");
            alert(getJsLocaleMessage("txgl", "txgl_wghdpz_dxhmd_text_4"));
        } else if (uploadresult == "outCount") {
            //alert("企业黑名单总数超过"+maxCount+"个，不允许再添加！");
            alert(getJsLocaleMessage("txgl", "txgl_wghdpz_dxhmd_text_5") + maxCount + getJsLocaleMessage("txgl", "txgl_wghdpz_dxhmd_text_6"));
        }
        <%session.removeAttribute("uploadresult");%>

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
        //addbusCode  添加插件
        $('#addbusCode').isSearchSelectNew({'width': '178', 'isInput': true, 'zindex': 0, "isFixed": true});
        initPage(<%=pageInfo.getTotalPage()%>, <%=pageInfo.getPageIndex()%>, <%=pageInfo.getPageSize()%>, <%=pageInfo.getTotalRec()%>);
        $('#search').click(function () {
            var phone = $.trim($("#phone").val());
            if (phone.length > 0 && !checkPhone(phone)) {
                //alert("请输入合法的手机号码进行查询！");
                alert(getJsLocaleMessage("txgl", "txgl_wghdpz_dxhmd_text_7"));
                return;
            }
            submitForm();
        });
    });

</script>
</body>
</html>
