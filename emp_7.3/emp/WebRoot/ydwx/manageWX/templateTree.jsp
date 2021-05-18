<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.LinkedHashMap" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String inheritPath = request.getRequestURI().substring(0,
			request.getRequestURI().lastIndexOf("/"));
	String iPath = request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
	inheritPath	= inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
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
%>
<HTML>
	<HEAD>
		<TITLE>ZTREE DEMO</TITLE>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link rel="stylesheet" href="<%=commonPath%>/common/widget/zTreeStyle/zTreeStyle.css" type="text/css" />
		<link rel="stylesheet" href="<%=iPath%>/css/templateManger.css" type="text/css" />
		<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.ztree-myjquery-b.js"></script>
	 
	</HEAD>
	<BODY>
		<ul id="tree" class="tree ydwx_tree"></ul>
		<div id="rMenu" class="ydwx_rMenu">
		</div>
		<SCRIPT LANGUAGE="JavaScript">
		    getLoginInfo("#loginUser");
			var zTree;
			var demoIframe;
			var setting;
			setting = {
				async : true,
				asyncUrl : "<%=path%>/wx_template.htm?method=getSort&lgcorpcode="+$("#lgcorpcode").val(), //获取节点数据的URL地址
				isSimpleData : true,
				rootPID : 0,
				treeNodeKey : "id",
				treeNodeParentKey : "pId",
				//asyncParam: ["depId"],
				//addDiyDom:addDom,
				callback : {
					click:getDepInfo,
					asyncSuccess:function(event, treeId, treeNode, msg){
					zTree.expandAll(true);
			    	},
					beforeAsync: function(treeId, treeNode) {
				    	return false;
					}	
					}
			};
			var zNodes = [];
		
			function getDepInfo(event,treeId,treeNode)
			{
			//alert(treeNode.id);
			$("#sortId").val(treeNode.id);
				submitForm();
			}
		
			$(document).ready(function(){
				
				setting.expandSpeed = ($.browser.msie && parseInt($.browser.version)<=7)?"":"fast";
				zTree = $("#tree").zTree(setting, zNodes);
				zTree.expandAll(true);
			});
		
			
		</SCRIPT>
	</BODY>
</HTML>
