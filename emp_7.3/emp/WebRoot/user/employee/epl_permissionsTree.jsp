<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
    String path = request.getContextPath();
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String treemethod = request.getParameter("treemethod");
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));

	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
 %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
  <head>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  <%@include file="/common/common.jsp" %>
  	<title></title>
	<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
	<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" href="<%=commonPath %>/common/widget/zTreeStyle/zTreeStyle.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css"/>
	<link rel="stylesheet" href="<%=commonPath %>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>" />
	<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
	<%if(StaticValue.ZH_HK.equals(langName)){%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
	<%}%>
	<style type="text/css">
		#epl_permissionsTree{
			margin:0;padding:0;
		}
		#epl_permissionsTree #tree{
			width: auto;
		}
	</style>
  </head>
  <body id="epl_permissionsTree">
	 <ul id="tree" class="tree"></ul>
	 <script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/employee_<%=langName%>.js"></script>
	 <script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.ztree-myjquery-b.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		
  <script type="text/javascript">
	var zTree;
	var demoIframe;
	
	var setting;
	var isHand = "true";

	setting = {
		async: true,
		isSimpleData: true,
		asyncUrl: "<%=request.getContextPath()%>/epl_employeeBook.htm?method=<%=treemethod%>",  //获取节点数据的URL地址
		rootPID : -1,
		treeNodeKey: "id",
		treeNodeParentKey: "pId",
		callback:{
			beforeAsync: function(treeId, treeNode) {
		    	zTree.setting.asyncUrl="<%=request.getContextPath()%>/epl_employeeBook.htm?method=<%=treemethod%>&depId="+treeNode.id;
			},
			asyncSuccess:function(event, treeId, treeNode, msg){
				if(!treeNode){	//判断是 顶级机构就展开,其余的收缩+
				   var rootNode = zTree.getNodeByParam("level", 0);
				   zTree.expandNode(rootNode, true, false);
				}
			},
			click: zTreeOnClick
		}
	};

	function zTreeOnClick(event, treeId, treeNode) {
		//首先判断 机构是否被删除 新增 p-------------------------------------------------------------
		$.get("<%=request.getContextPath()%>/epl_permissions.htm?method=checkdepDel", {
			selDepId:treeNode.id
		}, function(result) {
			if(result!=null && result=="-1"){
				alert(getJsLocaleMessage('dxzs','employee_alert_114'));
				window.location.reload();
			}else{
		//----------------------------------------------------------------------------------------
		 		if (treeNode.isParent) {
					setting.asyncUrl =  "<%=request.getContextPath()%>/epl_employeeBook.htm?method=<%=treemethod%>&depId="+treeNode.id;;
				}
				$(window.parent.document).find("#depName").text(treeNode.name);
				$(window.parent.document).find("#depName2").val(treeNode.name);
				$(window.parent.document).find("#depId").val(treeNode.id);
				$(window.parent.document).find("#dName").val(treeNode.name);
			 	window.parent.submitForm();
			}
		});
	}
	
	var zNodes =[];

	$(document).ready(function(){
		setting.expandSpeed = ($.browser.msie && parseInt($.browser.version)<=7)?"":"fast";
		$.post("<%=path%>/epl_permissions.htm?method=isHandAdd&m=<%=treemethod%>",{},function(result){
             if(result != null && result == "false")
             {
            	isHand = "false";
             }
             zTree = $("#tree").zTree(setting, zNodes);
		});
		demoIframe = $("#testIframe");
	});
	function loadReady() {
		var h = demoIframe.contents().find("body").height();
		if (h < 600) h = 600;
		demoIframe.height(h);
	}
  </script>
  </body>
</html>
