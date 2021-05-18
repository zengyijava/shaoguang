<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.Map" %>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String commonPath = request.getRequestURI().substring(0,
			request.getRequestURI().lastIndexOf("/"));
	commonPath = commonPath.substring(0,commonPath.lastIndexOf("/"));
	commonPath = commonPath.substring(0,commonPath.lastIndexOf("/"));
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("department");
	menuCode = menuCode==null?"0-0-0":menuCode;
	boolean addcode = false;
	if(btnMap.get(menuCode+"-1")!=null)
	{
		addcode = true;
	}
	boolean delcode = false;
	if(btnMap.get(menuCode+"-2")!=null)
	{
		delcode = true;
	}

	String iPath = request.getRequestURI().substring(0,
			request.getRequestURI().lastIndexOf("/"));

	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");

%>
<HTML>
	<HEAD>
		<TITLE>ZTREE DEMO</TITLE>
		<%@include file="/common/common.jsp" %>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link rel="stylesheet" href="<%=commonPath%>/common/widget/zTreeStyle/zTreeStyle.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" />
		
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/ctr_depMacIpTree.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/ctr_depMacIpTree.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		
		
		<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.ztree-myjquery-b.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<SCRIPT LANGUAGE="JavaScript">
			getLoginInfo("#loginUser");
			var zTree;
			var demoIframe;
			var zTree1;
			var setting;
			setting = {
				async : true,
				asyncUrl : "<%=path%>/cha_balanceMgr.htm?method=createTree2&lguserid="+GlobalVars.lguserid, //获取节点数据的URL地址
				isSimpleData : true,
				rootPID : 0,
				treeNodeKey : "id",
				treeNodeParentKey : "pId",
				callback : {
					beforeAsync : function(treeId, treeNode) {
					    zTree.setting.asyncUrl = "<%=path%>/cha_balanceMgr.htm?method=createTree2&depId="+treeNode.id+
						"&lguserid="+GlobalVars.lguserid;
					},
					click:getDep,
					asyncSuccess:function(event, treeId, treeNode, msg){
						if(!treeNode){
						   var rootNode = zTree.getNodeByParam("level", 0);
						   zTree.expandNode(rootNode, true, false);
						}
					}
				}
			};
			var zNodes = [];
		
			function getDep(event,treeId,treeNode)
			{
				$("#depTreeId").val(treeNode.id);
				submitForm();
			}
		
			$(document).ready(function(){
				setting.expandSpeed = ($.browser.msie && parseInt($.browser.version)<=7)?"":"fast";
				zTree = $("#tree").zTree(setting, zNodes);
				zTree.expandAll(true);
			});
		
		</SCRIPT>
	</HEAD>
	<BODY id="ctr_depMacIpTree">
		<ul id="tree" class="tree tree2"  ></ul>
	</BODY>
</HTML>
