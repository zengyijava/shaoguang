<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
String path = request.getContextPath();
String langName = (String)session.getAttribute("emp_lang");
String basePath = request.getScheme() + "://"
		+ request.getServerName() + ":" + request.getServerPort()
		+ path + "/";
String iPath = request.getRequestURI().substring(0,
		request.getRequestURI().lastIndexOf("/"));
String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));

	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
 	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("datasourceConf");
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<%@include file="/common/common.jsp" %>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" type="text/css" href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link href="<%=iPath %>/css/dataSource.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		
		<link href="<%=commonPath %>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		
		<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
			<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
		
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/dat_addDatasourceConf.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/dat_addDatasourceConf.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		
	</head>
	<body id="dat_addDatasourceConf" onload="show()">
		<div id="container" class="container">
			<div id="rContent">
			<%if(btnMap.get(menuCode+"-1")!=null){%>
            	<div id="detail"> 
				<form id="addDBConn" method="post" action="dat_datasourceConf.htm?method=add">
                	<div class="hiddenValueDiv" id="hiddenValueDiv"></div>
                 	<table id="content" border="0" class="content">
						<thead>
							<tr>
								<td>
									<span><emp:message key="xtgl_cswh_sjypz_sjymc" defVal="数据源名称：" fileName="xtgl"/></span>
								</td>
								<td >
									<input class="input_bd"  name="dbconName" onblur="checklink()" id="dbconName" type="text" value="" />
										<input type="hidden" id="hidOpType" name="hidOpType" value="add" /><font color="red">&nbsp;&nbsp;&nbsp;*</font>
								</td>
							</tr>
							<tr>
								<td>
									<span><emp:message key="xtgl_cswh_ywlxgl_ms_mh" defVal="描述：" fileName="xtgl"/></span>
								</td>
								<td>
									<input class="input_bd"  name="comments" id="comments" type="text"  value="" maxlength="256"/>
								</td>
							</tr>
							<tr>
								<td>
									<span><emp:message key="xtgl_cswh_sjypz_dk_mh" defVal="端口：" fileName="xtgl"/></span>
								</td>
								<td>
									<input class="input_bd"  name="port" onblur="checkData()" id="port" type="text"  value="1521" /><font color="red">&nbsp;&nbsp;&nbsp;&nbsp;*</font>
								</td>
							</tr>
							<tr>
								<td>
									<span><emp:message key="xtgl_cswh_sjypz_sjkdz_mh" defVal="数据库地址：" fileName="xtgl"/></span>
								</td>
								<td>
									<input class="input_bd"  name="dbconIp" onblur="checkData()" id="dbconIp" type="text" value="" placeholder="填写IP地址或域名" maxlength="32" /><font color="red">&nbsp;&nbsp;&nbsp;&nbsp;*</font>
								</td>
							</tr>
							<tr>
								<td><span><emp:message key="xtgl_cswh_sjypz_sjklx_mh" defVal="数据库类型：" fileName="xtgl"/></span></td>
								<td>
									<select class="input_bd dbType"  id="dbType" name="dbType"   onchange="changeDbType(this.value)">
										<option value="Oracle">Oracle</option>
										<option value="Sql Server">Sql Server</option>
										<option value="Mysql">Mysql</option>
										<option value="DB2">DB2</option>
									</select>
								</td>
							</tr>
							<tr>
								<td>
									<span><emp:message key="xtgl_cswh_sjypz_ljlx_mh" defVal="连接类型：" fileName="xtgl"/></span>
								</td>
								<td>
									<select class="input_bd dbConnType"  id="dbConnType" name="dbConnType"  >
										<option value="0">Service Name</option>
										<option value="1">SID</option>
									</select>
								</td>
							</tr>
							<tr>
								<td id="tdDbName">
									<span><emp:message key="xtgl_cswh_sjypz_fwslm_mh" defVal="服务/实例名：" fileName="xtgl"/></span>
								</td>
								<td>
									<input class="input_bd"  name="dbName" onblur="checkData()" id="dbName" type="text"  value="" maxlength="32"/><font color="red">&nbsp;&nbsp;&nbsp;&nbsp;*</font>
								</td>
							</tr>
							<tr>
								<td>
									<span><emp:message key="xtgl_cswh_sjypz_yhm_mh" defVal="用户名：" fileName="xtgl"/></span>
								</td>
								<td>
									<input class="input_bd"  name="dbUser" onblur="checkData()" id="dbUser" type="text"  value="" maxlength="32"/><font color="red">&nbsp;&nbsp;&nbsp;&nbsp;*</font>
								</td>
								</tr>
							<tr>
								<td>
									<span><emp:message key="xtgl_cswh_sjypz_mm_mh" defVal="密码：" fileName="xtgl"/></span>
								</td>
								<td>
									<input  class="input_bd dbPwd" name="dbPwd" onblur="checkData()" id="dbPwd" type="password"    value="" /><font color="red">&nbsp;&nbsp;&nbsp;&nbsp;*</font>
								</td>
							</tr>


							<tr><td colspan="2" height="23px">
							<label>
								<font id="waitTextConnection" color="#808080"></font>
								<font id="rightTextConnection" color="green"></font>
								<font id="errorTextConnection" color="red"></font>
							</label>
							</td></tr>
							<tr>
								<td colspan="2" id="btn" >
								<div style="padding-left: <%=StaticValue.ZH_HK.equals(langName)?"25px":"15px" %>">
								<input id="testDBConnBtn" type="button"  value="<emp:message key='xtgl_cswh_sjypz_cslj' defVal='测试连接' fileName='xtgl'/>" onclick="testConnection()" class="btnClass6 btnLetter4 indent_none"/>
								<input id="addDBConnBtn" type="button" onclick="addDbConnection()" value="<emp:message key='xtgl_spgl_shlcgl_qd' defVal='确 定' fileName='xtgl'/>" class="btnClass5"/>
								<input id="addDBConnBackBtn" type="button" onclick="javascript:window.parent.closeAddFrame()" value="<emp:message key='xtgl_spgl_shlcgl_fh' defVal='返 回' fileName='xtgl'/>" class="btnClass6"/>
								<%-- 为了兼容ie8下取消按钮右下角旁边能点击到确定按钮，增加这个换行 --%>
								</div>
								</td>
							</tr>
							</thead>
						</table>
                </form>
                </div>
                <%} %>
			</div>
			<%-- 内容结束 --%>
			<div class="clear"></div>
			<%-- foot开始 --%>
			<div class="bottom">
				<div id="bottom_right">
					<div id="bottom_left"></div>
				</div>
			</div>
			<%-- foot结束 --%>
		</div>
    <div class="clear"></div>
    <script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/xtgl_<%=langName%>.js"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script language="javascript" src="<%=commonPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>" type="text/javascript"></script>
	<script language="javascript" src="<%=iPath %>/js/outSystemManager.js?V=<%=StaticValue.getJspImpVersion() %>" type="text/javascript"></script>
	<script type="text/javascript">
	$(document).ready(function(){
		getLoginInfo1("#hiddenValueDiv");
	});

	function show(){
		<%String result = (String) session.getAttribute("result1");
			if (result != null && result.equals("1"))
			{%>
			alert(getJsLocaleMessage("xtgl","xtgl_cswh_sjypz_73"));
		<%} else if (result != null && result.equals("2"))
			{%>
			alert(getJsLocaleMessage("xtgl","xtgl_cswh_sjypz_74"));
		<%} else if (result != null && result.equals("-1"))
			{%>		
			alert(getJsLocaleMessage("xtgl","xtgl_cswh_sjypz_75"));
		<%} else if (result != null && result.equals("0"))
			{%>
			alert(getJsLocaleMessage("xtgl","xtgl_cswh_sjypz_76"));	
		<%}
			session.removeAttribute("result1");
			 if (result != null)
		    {%>
			     window.parent.location.href=window.parent.location.href;
			     $("#addDBConnBackBtn").trigger("click");
		    <%  }%>
	}
	</script>
	</body>
</html>
