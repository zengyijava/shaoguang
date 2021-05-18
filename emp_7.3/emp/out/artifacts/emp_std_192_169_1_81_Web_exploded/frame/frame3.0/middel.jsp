<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
String path = request.getContextPath()+session.getAttribute("empRoot");
String bpath = request.getContextPath();
String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");

String openMenuCode = request.getParameter("openMenuCode");
String priMenus = request.getParameter("priMenus");
String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head><%@ include file="/common/common.jsp"%>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title></title>
<jsp:include page="/common/commonfile.jsp"></jsp:include>

<link href="<%=iPath %>/css/middel.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
<link href="<%=skin %>/middel.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
<style type="text/css">
    #menuFrame{position:relative;}
    #load-bg{
        display: none;
        position: absolute;
        top: 0%;
        left: 0%;
        width: 100%;
        height: 100%;
        z-index:99999;
        -moz-opacity: 0.4;
        opacity:.40;
        filter: alpha(opacity=40);
        background:#eee url('<%=commonPath %>/common/img/loading-bg.gif') no-repeat center
    }
</style>
</head>
<body  onresize="setTableHeight2()" onload="setTableHeight()">
<div style="position: fixed;width:100%;height:100%;z-index: -10;display:none" id="fixedDiv"></div>
<input type="hidden" id="isMiddel" value="1"/>
<input type="hidden" id="path" value="<%=request.getContextPath() %>"/>
<input type="hidden" id="langName" value="<%=langName%>" />
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0" style="table-layout:fixed;">
  <tr>
    <td width="180" id=frmTitle noWrap name="fmTitle" align="center" valign="top" >
	        <iframe name="I1" height="100%" width="180" id="leftIframe" allowtransparency="true"  style="margin-left:7px;"
	        	src="<%=bpath %>/thirdMenu.htm?method=getAllPriList&priMenus=<%=priMenus %>&openMenuCode=<%=openMenuCode %>"
	         	border="0" frameborder="0" scrolling="no"> <emp:message key="common_frame2_middle_1" defVal="浏览器不支持嵌入式框架，或被配置为不显示嵌入式框架。" fileName="common"></emp:message></iframe>
    </td>
    <td width="6" style="width:6px;" valign="middle" id="middle_toggle" onclick="javascript:switchSysBar($(this))">
    <div style="width:100%;height:100%;display:block"><div></div></div>
    </td>
    <td align="center" valign="top">
    <table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0" style="able-layout:fixed;" id="mainTable">
   <tr height=31>
    <td  id="topLink"><div class="topMenu putdown" ></div></td>
    <td width="7"></td>
    </tr>
    <tr>
    <td id="menuFrame" class="mid_bd">
        <%-- 共享模板 DIV --%>
        <div id="shareTmpDiv" title="<emp:message key='xtgl_cswh_dxmbgl_mbgx' defVal='模板共享' fileName='xtgl'/>" class="shareTmpDiv">
            <input type="hidden" id="templType" value="4">
            <center>
                <iframe id="flowFrame" name="flowFrame" src=""  attrid="" tmpname="" class="flowFrame" marginwidth="0" scrolling="no" frameborder="no"></iframe>
            </center>
            <table>
                <tr>
                    <td class="shlcgl_qd_td" align="center">
                        <input type="button"  value="<emp:message key='common_spgl_shlcgl_qd' defVal='确定' fileName='common'/>" id="updateShareTemp" class="btnClass5 mr23" onclick="javascript:updateShareTemp();" />
                        <input type="button"  value="<emp:message key='common_spgl_shlcgl_qx' defVal='取消' fileName='xtgl'/> " class="btnClass6" onclick="javascript:closeShare();" />
                        <%-- 为了兼容ie8下取消按钮右下角旁边能点击到确定按钮，增加这个换行 --%>
                        <br/>
                    </td>
                </tr>
            </table>
        </div>
    <%--存放iframe的单元格 --%>
        <div id="load-bg"></div>
   	</td>
   	<td width="7"></td>
    </tr>
    </table>
    </td>
  </tr>
  <tr height="7"><td height="7px"></td></tr>
</table>
<div id="IMDiv" style="display:none;width:0;height:500px;left:158px;top:31px;position: absolute;z-index:9;overflow: visible;" >
		<iframe frameborder="0" height="100%" width="100%" src="" scrolling="no"  name="IMFrame" id="IMFrame" ></iframe>
	</div>
<div id="tab_hide" style="display:none;position:absolute;top:0;left:0px;z-index:4000;width:150px;">
	<ul id="tb_child" style="list-style:none;" class="mid_bd" >
		<li id="cloall" class="mid_bd"><a href="javascript:closeAllTab()"><emp:message key="common_frame3_middel_1" defVal="关闭其他标签" fileName="common"></emp:message></a></li>
	</ul>
</div>
	<iframe id="hid_ifr"></iframe>
	<div class="logininfo" style="display:none"></div>
<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
<script type="text/javascript" src="<%=iPath %>/js/middel.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=iPath %>/js/middle_func.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript">
	var iPath = "<%=iPath%>";
	function doOpen1(priMenus) {
		$('#menu', window.parent.document).html(priMenus);
		var $par = $(window.parent.frames["topFrame"].document);
		$par.find("#onSys").attr("value", "2");
		location.href = iPath + "/middel.jsp?priMenus=" + priMenus;
	}
</script>
</body>
</html>
