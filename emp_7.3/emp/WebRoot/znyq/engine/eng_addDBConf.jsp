<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>

<%
String path = request.getContextPath();
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
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<%@include file="/common/common.jsp" %>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" />
		<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<%}%>
		
		<link rel="stylesheet" type="text/css" href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link href="<%=iPath %>/css/dataSource.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		
		<link href="<%=commonPath %>/common/css/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/znyq/engine/scss/eng_common.css?V=<%=StaticValue.getJspImpVersion()%>" />
	</head>
	<body onload="show()" id="eng_addDBconf" class="eng_addDBconf">
		<div id="container" class="container">
			<div id="rContent">
			<%if(btnMap.get(menuCode+"-1")!=null){%>
            	<div id="detail"> 
				<form id="addDBConn" method="post" action="eng_mtProcess.htm?method=addDB">
                	<div id="hiddenValueDiv"></div>
                 	<table id="content">
						<thead>
							<tr>
								<td class="td1">
									<span><emp:message key="znyq_ywgl_xhywgl_sjymc_mh" defVal="数据源名称：" fileName="znyq"></emp:message></span>
								</td>
								<td >
									<input class="input_bd"  name="dbconName" onblur="checklink()" id="dbconName" type="text" value="" />
										<input type="hidden" id="hidOpType" name="hidOpType" value="add" /><font class="tipColor">&nbsp;&nbsp;&nbsp;*</font>
								</td>
							</tr>
							<tr>
								<td>
									<span><emp:message key="znyq_ywgl_xhywgl_ms_mh" defVal="描述：" fileName="znyq"></emp:message></span>
								</td>
								<td>
									<input class="input_bd"  name="comments" id="comments" type="text"  value="" maxlength="256"/>
								</td>
							</tr>
							<tr>
								<td>
									<span><emp:message key="znyq_ywgl_xhywgl_dk_mh" defVal="端口：" fileName="znyq"></emp:message></span>
								</td>
								<td>
									<input class="input_bd"  name="port" onblur="checkData()" id="port" type="text"  value="1521" /><font class="tipColor">&nbsp;&nbsp;&nbsp;&nbsp;*</font>
								</td>
							</tr>
							<tr>
								<td>
									<span><emp:message key="znyq_ywgl_xhywgl_sjydz_mh" defVal="数据库地址：" fileName="znyq"></emp:message></span>
								</td>
								<td>
									<input class="input_bd"  name="dbconIp" onblur="checkData()" id="dbconIp" type="text" value="" /><font class="tipColor">&nbsp;&nbsp;&nbsp;&nbsp;*</font>
								</td>
							</tr>
							<tr>
								<td><span><emp:message key="znyq_ywgl_xhywgl_sjylx_mh" defVal="数据库类型：" fileName="znyq"></emp:message></span></td>
								<td>
									<select class="input_bd"  id="dbType" name="dbType" onchange="changeDbType(this.value)">
										<option value="Oracle">Oracle</option>
										<option value="Sql Server">Sql Server</option>
										<option value="Mysql">Mysql</option>
										<option value="DB2">DB2</option>
									</select>
								</td>
							</tr>
							<tr>
								<td>
									<span><emp:message key="znyq_ywgl_xhywgl_ljlx_mh" defVal="连接类型：" fileName="znyq"></emp:message></span>
								</td>
								<td>
									<select class="input_bd"  id="dbConnType" name="dbConnType">
										<option value="0">Service Name</option>
										<option value="1">SID</option>
									</select>
								</td>
							</tr>
							<tr>
								<td id="tdDbName">
									<span><emp:message key="znyq_ywgl_xhywgl_fwslm_mh" defVal="服务/实例名：" fileName="znyq"></emp:message></span>
								</td>
								<td>
									<input class="input_bd"  name="dbName" onblur="checkData()" id="dbName" type="text"  value="" maxlength="32"/><font class="tipColor">&nbsp;&nbsp;&nbsp;&nbsp;*</font>
								</td>
							</tr>
							<tr>
								<td>
									<span><emp:message key="znyq_ywgl_xhywgl_yhm_mh" defVal="用户名：" fileName="znyq"></emp:message></span>
								</td>
								<td>
									<input class="input_bd"  name="dbUser" onblur="checkData()" id="dbUser" type="text"  value="" maxlength="32"/><font class="tipColor">&nbsp;&nbsp;&nbsp;&nbsp;*</font>
								</td>
								</tr>
							<tr>
								<td>
									<span><emp:message key="znyq_ywgl_xhywgl_mm_mh" defVal="密码：" fileName="znyq"></emp:message></span>
								</td>
								<td>
									<input  class="input_bd" name="dbPwd" onblur="checkData()" id="dbPwd" type="password"  value="" /><font class="tipColor">&nbsp;&nbsp;&nbsp;&nbsp;*</font>
								</td>
							</tr>


							<tr><td colspan="2" class="td2">
							<label>
								<font id="waitTextConnection"></font>
								<font id="rightTextConnection"></font>
								<font id="errorTextConnection"></font>
							</label>
							</td></tr>
							<tr>
								<td colspan="2" id="btn" >
								<input id="testDBConnBtn" type="button"  value="<emp:message key="znyq_ywgl_xhywgl_cslj" defVal="测试连接" fileName="znyq"></emp:message>" onclick="testConnection()" class="btnClass6 btnLetter4 indent_none"/>
								<input id="addDBConnBtn" type="button" onclick="addDBConn()" value="<emp:message key="znyq_ywgl_common_btn_7" defVal="确 定" fileName="znyq"></emp:message>" class="btnClass5"/>
								<input id="addDBConnBackBtn" type="button" onclick="javascript:window.parent.closeAddFrame()" value="<emp:message key="znyq_ywgl_common_btn_10" defVal="返 回" fileName="znyq"></emp:message>" class="btnClass6"/>
								<%-- 为了兼容ie8下取消按钮右下角旁边能点击到确定按钮，增加这个换行 --%>
								<br/>
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
    <script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script language="javascript" src="<%=commonPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript"></script>
	<script language="javascript" src="<%=iPath %>/js/eng_db.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/znyq_<%=langName%>.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
	<script type="text/javascript">
	$(document).ready(function(){
		getLoginInfo1("#hiddenValueDiv");
	});

	function show(){
		<%String result = (String) session.getAttribute("result1");
			if (result != null && result.equals("1"))
			{%>
			//alert("添加数据库连接成功！");
			alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_tjsjkljcg"));
		<%} else if (result != null && result.equals("-1"))
			{%>		
			//alert("已存在相同的数据源！");
			alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_yczxtdsjy"));
		<%} else if (result != null && result.equals("0"))
			{%>
			//alert("操作失败！");	
			alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_czsb"));	
		<%}
			session.removeAttribute("result1");
			 if (result != null)
		    {%>
		    		javascript:window.parent.getDBInfo();
		    		javascript:window.parent.closeAddFrame();
			     //window.parent.location.href=window.parent.location.href;
			     //$("#addDBConnBackBtn").trigger("click");
		    <%  }%>
	}
	</script>
	</body>
</html>
