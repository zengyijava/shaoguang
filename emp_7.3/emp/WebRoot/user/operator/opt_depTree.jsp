<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.LinkedHashMap" %>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>

<%@ taglib prefix="emp"
	uri="http://www.montnets.com/emp/i18n/tags/simple"%>
	
	
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String inheritPath = request.getRequestURI().substring(0,
			request.getRequestURI().lastIndexOf("/"));
	inheritPath	= inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	
	//Jsp页面中获取session中的语言设置
 	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
	
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

	String iPath = request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
%>
<HTML>
	<HEAD>
		<TITLE>ZTREE DEMO</TITLE>
		<%@include file="/common/common.jsp" %>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link rel="stylesheet" href="<%=empBasePath%>/common/widget/zTreeStyle/zTreeStyle.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" />
		<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=empBasePath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<%}%>
		
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/opt_depTree.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/user.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		
		<script type="text/javascript" src="<%=empBasePath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=empBasePath%>/common/i18n/<%=langName%>/user_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=empBasePath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=empBasePath %>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=empBasePath%>/common/js/jquery.ztree-myjquery-b.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=empBasePath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<SCRIPT LANGUAGE="JavaScript">
		    getLoginInfo("#loginUser");
		    var lguserid = GlobalVars.lguserid;
			var zTree;
			var demoIframe;
			var zTree1;
			var setting;
			setting = {
				async : true,
				asyncUrl : "<%=path%>/opt_department.htm?method=createTreejm&lguserid="+lguserid, //获取节点数据的URL地址
				isSimpleData : true,
				rootPID : 0,
				treeNodeKey : "id",
				treeNodeParentKey : "pId",
				//asyncParam: ["depId"],
				//addDiyDom:addDom,
				callback : {
					click:getDepInfo,
					beforeAsync: function(treeId, treeNode) {
						zTree.setting.asyncUrl="<%=path%>/opt_department.htm?method=createTreejm&depId="+treeNode.id+
						"&lguserid="+lguserid;
					},
					//rightClick: zTreeOnRightClick,
					asyncSuccess:function(event, treeId, treeNode, msg){
						if(!treeNode){
						   var rootNode = zTree.getNodeByParam("level", 0);
						   zTree.expandNode(rootNode, true, false);
						}
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
				var edit="";
				var del = "";
				if (<%=addcode%> )
				{
                    add= "<button type='button' class='diyBtn1' id='add_" +treeNode.id+ "' title='" + getJsLocaleMessage("user","user_xtgl_czygl_text_154") + "' onfocus='this.blur();'></button>&nbsp;";
				}
				if (<%=delcode%>&&treeNode.pId!=0)
				{
                    del="<button type='button' class='diyBtn2' id='del_" +treeNode.id+ "' title='"+getJsLocaleMessage("user","user_xtgl_czygl_text_155")+ "' onfocus='this.blur();'></button>";
				}
				edit="<button type='button' class='diyBtn3' id='edit_" +treeNode.id+ "' title='" + getJsLocaleMessage("user","user_xtgl_czygl_text_156")+ "' onfocus='this.blur();'></button>&nbsp;";
				var editStr = "<span id='hrefDiv'>&nbsp;&nbsp;&nbsp;"+add+edit+del+"</span>";
				$("#"+treeNode.tId+"_a").append(editStr);
				var btn = $("#add_"+treeNode.id);
				if (btn) btn.bind("click", function(){doAdd(treeNode.id,treeNode.name,treeNode.level);});
				var btn2 = $("#del_"+treeNode.id);
				if (btn2) btn2.bind("click", function(){delDeps(treeNode.id,treeNode.name,treeNode.pId);});
				var btn3 = $("#edit_"+treeNode.id);
				if (btn3) btn3.bind("click", function(){ 
					$("#opType").val("2");
					editDep(treeNode.id);
				});
			}
			
			var zNodes = [];
		
			function getDepInfo(event,treeId,treeNode)
			{
				$("#depId").val(treeNode.id);
				$("#upDepName").attr("value",treeNode.name);
				$("#pareDepId").val(treeNode.pId);
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

				id=$("#depId").val();
				name=$("#upDepName").val();
                $("#dId").val(id);
				$("#upDepName").attr("value",name);
				$("#level").attr("value",l);
				$("#opType").val("1");
				if(id !=""){
					$('#superDepId').val(id);
					$("#addDiv").dialog("open");
				}
			}
			//document.onmousedown=showRMenu("none",event.clientX, event.clientY);
		</SCRIPT>
	</HEAD>
	<BODY id="opt_depTree">
		
		<ul id="tree" class="tree tree2"  ></ul>
		<div id="rMenu" class="rMenu">
			 <div class="tjzjg_div "><emp:message key="user_xtgl_czygl_text_62" 
										defVal="添加子机构" fileName="user" /></div>
		</div>
	</BODY>
</HTML>
