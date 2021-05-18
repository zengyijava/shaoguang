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

	String iPath = request.getRequestURI().substring(0,
			request.getRequestURI().lastIndexOf("/"));
	String skin = session.getAttribute("stlyeSkin")==null?"default":
		(String)session.getAttribute("stlyeSkin");
		
	String inheritPath = request.getRequestURI().substring(0,
			request.getRequestURI().lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	inheritPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("balanceMgr");
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
		<link href="<%=commonPath %>/common/widget/zTreeStyle/zTreeStyle.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/cha_depBalance.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/cha_depBalance.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		
		<script type="text/javascript"	src="<%=commonPath %>/common/js/jquery.ztree-myjquery-b.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<SCRIPT LANGUAGE="JavaScript">
			var zTree;
			var demoIframe;
			var zTree1;
			var setting;
			setting = {
				async : true,
				asyncUrl : "<%=path%>/cha_balanceMgr.htm?method=createTree", //获取节点数据的URL地址
				isSimpleData : true,
				rootPID : 0,
				treeNodeKey : "id",
				treeNodeParentKey : "pId",
				addDiyDom:addDom,
				callback : {
					beforeAsync : function(treeId, treeNode) {
						return false;
					},
					click:getDepInfo,
					//rightClick: zTreeOnRightClick,
					asyncSuccess:function(event, treeId, treeNode, msg){
						zTree.expandAll(true);
					}
				}
			};
			function zTreeBeforeAsync(treeId, treeNode) {
				if (treeNode.id == 1)
					return false;
				return true;
			}
			function addDom(treeId, treeNode) {
				var add = "";
				var del = "";
				if(treeNode.level <=2){ 
                    add= "<button type='button' class='diyBtn1' id='add_" +treeNode.id+ "' title='余额充值' onfocus='this.blur();'></button>&nbsp;";
                    del="<button type='button' class='diyBtn2' id='del_" +treeNode.id+ "' title='余额回收' onfocus='this.blur();'></button>";
				}
				var editStr = "<span id='hrefDiv'>&nbsp;&nbsp;&nbsp;"+add+del+"</span>";
				$("#"+treeNode.tId+"_a").append(editStr);
				var btn = $("#add_"+treeNode.id);
				if (btn) btn.bind("click", function(){
				  // doAdd(treeNode.id,treeNode.name,treeNode.level);
				    alert(treeNode.id+"    "+treeNode.name+"    "+treeNode.level+"    充值");
				});
				var btn2 = $("#del_"+treeNode.id);
				if (btn2) btn2.bind("click", function(){
				   //delDeps(treeNode.id,treeNode.name);
				   alert(treeNode.id+"    "+treeNode.name+"    "+treeNode.level+"    回收");
				});
			}
			
			var zNodes = [];
		
			function getDepInfo(event,treeId,treeNode)
			{
				$("#depId").val(treeNode.id);
				$("#upDepName").attr("value",treeNode.name);
				submitForm();
			}
		
			$(document).ready(function(){
				setting.expandSpeed = ($.browser.msie && parseInt($.browser.version)<=7)?"":"fast";
				zTree = $("#tree").zTree(setting, zNodes);
				demoIframe = $("#testIframe");
				zTree.expandAll(true);
			});
		
			function zTreeOnRightClick(event, treeId, treeNode) {
				zTree.cancelSelectedNode();
				zTree.selectNode(treeNode);
				showRMenu("block",event.clientX, event.clientY);
			}
			function showRMenu(isShow, x, y) {
				y=y-0+8;
				x=x-0+5;
				$("#rMenu ").attr("display",isShow);
				$("#rMenu").css( {
					"top" : y + "px",
					"left" : x + "px",
					"visibility" : "visible"
				});
			}
			function doAdd(id,name,l){
				$("#depId").val(id);
				$("#upDepName").attr("value",name);
				$("#level").attr("value",l);
				if($('#depId').val()!=""){
					$('#superDepId').val($('#depId').val());
					$("#superDepName").attr("value",$("#upDepName").attr("value"));
					$("#addDiv").dialog("open");
				}
			}
			//document.onmousedown=showRMenu("none",event.clientX, event.clientY);
		</SCRIPT>
	</HEAD>
	<BODY id="cha_depBalance">
		<div id="tree" class="tree tree2"></div>
		<div id="rMenu" class="rMenu">
			 <div class="rMenu_div">添加子机构</div>
		</div>
	</BODY>
</HTML>
