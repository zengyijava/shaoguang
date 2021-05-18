<%@ page language="java" import="com.montnets.emp.common.vo.LfMaterialVo" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.servmodule.ydcx.constant.ServerInof"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
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
	List<LfMaterialVo> lfMaterialVoList = (List<LfMaterialVo>)request.getAttribute("lfMaterialVoList");
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("mmsTemplate");
	menuCode = menuCode==null?"0-0-0":menuCode;
	//获得信息回显
	String skin = session.getAttribute("stlyeSkin")==null?"default":
		(String)session.getAttribute("stlyeSkin");
	String acc = "true";
	if(StaticValue.getCORPTYPE() ==1 && "false".equals((String)request.getAttribute("mmsacc")))
	{
		acc = "false";
	}
	//1静态彩信  2动态彩信  3新增
	String pathtype = (String)request.getParameter("type");
	//服务器名称
	String serverName = ServerInof.getServerName();
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
	
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<%@include file="/common/common.jsp" %>
		<title></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<%}%>
		<link href="<%=iPath%>/css/mmsTemplate.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/mmsTemplate.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/batchFileSend.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=commonPath %>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
		<link rel="stylesheet" href="<%=commonPath %>/common/widget/zTreeStyle/zTreeStyle.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css"/>
		<style type="text/css">
			#container {
				width: 500px;
				max-width: 500px;
				height: 600px;
				display: inline-block;
			}
			
			#cust_preview_outer {
				width: 320px;
				height: 568px;
				border: 1px solid;
				display: inline-block;
				vertical-align: top;
				overflow: hidden;
			}
			
			#cust_preview {
				overflow-y: scroll;
				width: 335px;
				height: 570px;
				overflow-x: hidden;
			}
		</style>
		
	</head>

	<body>
	 <script id="container" name="content" type="text/plain"></script>
	<div id="cust_preview_outer">
  		<div id="cust_preview"></div>
	</div>
	<script src="<%=inheritPath%>/ueditor/ueditor.config.js" type="text/javascript"></script>
	<script src="<%=inheritPath%>/ueditor/ueditor.all.js" type="text/javascript"></script>
	
	
	
	<script type="text/javascript">
		var uploadEditor = UE.getEditor('container', {
			//清空了工具栏
			toolbars : [ ['source','simpleupload','music','insertvideo'] ]
		});

		 uploadEditor.addListener('selectionchange', function () {
			 var div = "<div>"+uploadEditor.getContent()+"</div>";
			 console.log(document.getElementById("cust_preview"));
			 document.getElementById("cust_preview").innerHTML=div;
         });
		 
		function getContent() {
			alert(uploadEditor.getContent());
		}
	</script>
	 
	
	</body>
</html>
