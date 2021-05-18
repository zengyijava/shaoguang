<%@ page language="java" import="java.util.List" pageEncoding="UTF-8"%>
<%@page import="com.montnets.emp.common.constant.SystemGlobals"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@page import="com.montnets.emp.entity.system.LfThiMenuControl"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String iPath = request.getRequestURI().substring(0,
			request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0, iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0, inheritPath.lastIndexOf("/"));
	String pageUrl = request.getContextPath() + "/"
			+ SystemGlobals.getValue(StaticValue.EMP_WEB_FRAME);

	String aproinfo = (String) request.getAttribute("proInfo");
	Integer validday = (Integer) session.getAttribute("ValidDay");
	validday = validday == null ? 0 : validday;

	String skin = session.getAttribute("stlyeSkin") == null ? "default"
			: (String) session.getAttribute("stlyeSkin");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head><%@ include file="/common/common.jsp"%>
		<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
		<script type="text/javascript"
			src="<%=path%>/common/js/myjquery-a.js"></script>
		<link href="<%=iPath %>/css/index.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/index.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<%if(StaticValue.ZH_HK.equals(empLangName)){%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<%}%>
		<!--[if IE 6]>
        <script type="text/javascript" src="<%=path%>/common/js//DD_belatedPNG.js"></script>
        <script language="javascript" type="text/javascript">
        DD_belatedPNG.fix(".act_btn, .png");
        </script>
        <![endif]-->
		<style>
		.menuDiv {
	    	height: 192px;
	    }
		</style>
	</head>
	<body >
	<div id="bg_top_line">&nbsp;</div>
		<input type="hidden" id="isMiddel" value="0" />
			<div id="perCount" class="act_content">
			</div>
			<div id="contents" style="display: none">
					
			</div>
		<script type="text/javascript">
	    	var aproinfo = '<%=aproinfo%>';
	        var validday = <%=validday%>;
	        var iPath = "<%=iPath%>";
	        var path="<%=path%>";
	        var urlRouter={};
	        $(function(){
	        	 urlRouter={
					  modId:20,//在线客服模块ID
					  flag:1,//1表示开启跳转
					  userid:getField('#userid')||getLoginparams('#lguserid'),
					  tkn:getField('#appTkn')
				};
		        function getField(obj){
		        	return $(window.parent.frames['topFrame'].document).find(obj).val();
		        }
		        function getLoginparams(obj){
		        	var $pa = $(window.parent.document);
		        	var pahtm = $pa.find("#loginparams").children(obj).val();
		        	return pahtm;
		        }
	        })
	       
        </script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=iPath %>/js/json2.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=iPath %>/js/jquery.touchslider.min.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=iPath %>/js/index.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript">
	        function doOpen1(priMenus) {
	        	$('#menu', window.parent.document).html(priMenus);
	        	var $par = $(window.parent.frames["topFrame"].document);
	        	$par.find("#onSys").attr("value", "2");
	        	location.href = iPath + "/middel.jsp?priMenus=" + priMenus;
	        }
        
        </script>
	</body>
</html>
