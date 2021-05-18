<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>

<%
    String path = request.getContextPath();
    String langName = (String)session.getAttribute("emp_lang");
	String iPath = request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	String lguserid = request.getParameter("lguserid");
	String tempId = request.getParameter("tempId");
	
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
 %>
 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
  <head>
  		
<title></title>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  <%@include file="/common/common.jsp" %>
  	<link rel="stylesheet" href="<%=commonPath %>/common/widget/zTreeStyle/zTreeStyle.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css"/>
	<link rel="stylesheet" href="<%=commonPath %>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>" />
	<link href="<%=commonPath %>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
	<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
	<%if(StaticValue.ZH_HK.equals(langName)){%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
	<%}%>
	
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/tem_seltree.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/tem_seltree.css?V=<%=StaticValue.getJspImpVersion() %>"/>
	
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/xtgl_<%=langName%>.js"></script>
	<script type="text/javascript" src="<%=commonPath %>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath %>/common/js/jquery.ztree-myjquery-b.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
 	 <script type="text/javascript">
	var zTree1;
	var setting1;
	setting1 = {
			async: true,
			isSimpleData: true,
			rootPID : 0,
			treeNodeKey: "id",
			treeNodeParentKey: "pId",
			asyncParam: ["depId"],
			asyncUrl: "<%=request.getContextPath()%>/tem_smsTemplate.htm?method=createinstalltree&lguserid=<%=lguserid%>&tempId=<%=tempId%>", 
			callback:{
				click: zTreeOnClick,
				dblclick:zTreeDbClick,
				asyncSuccess:function(event, treeId, treeNode, msg)
				{
					if(!treeNode)
					{
						var rootNode = zTree1.getNodeByParam("level",0);
						zTree1.expandNode(rootNode,true,false);
					}
			    },
				beforeAsync: function(treeId, treeNode) {
					zTree1.setting.asyncUrl="<%=request.getContextPath()%>/tem_smsTemplate.htm?method=createinstalltree&depId="+treeNode.id+
						"&lguserid=<%=lguserid%>&tempId=<%=tempId%>";
				}
			}
		};

	function zTreeDbClick(event,treeId,treeNode) 
	{
		var hasChild=false;
		if(treeNode.nodes&&treeNode.nodes.length>0&&confirm(getJsLocaleMessage("xtgl","xtgl_cswh_dxmbgl_1"))){
			hasChild=true;
		}
		parent.window.addSeldep(treeNode,hasChild);
	}

	//处理 选择的操作员
	function zTreeOnClick(event, treeId, treeNode) {
		$(window.parent.document).find("#left").empty();
		var depId=treeNode.id;
		var depName=treeNode.name;
        var searchname = $(window.parent.document).find("#searchname").val();
        var lgcorpcode = $(window.parent.document).find("#lgcorpcode").val();
		$.post("<%=path %>/tem_smsTemplate.htm", {method:"getSysuserByDepId", depId:depId,searchname:searchname,lgcorpcode:lgcorpcode}, function(returnmsg){
			if(returnmsg != "" && returnmsg.indexOf("suceess#") != -1){
				var arr = returnmsg.split("#");
				$(window.parent.document).find("#left").html(arr[1]);
			}
		});
	}
	function zTreeBeforeAsync(treeId, treeNode) {
	    if (treeNode.id == 1) return false;
	    return true;
	}
	
	var zNodes =[];

	$(document).ready(function(){
		setting1.expandSpeed = ($.browser.msie && parseInt($.browser.version)<=7)?"":"fast";
		zTree1 = $("#tree").zTree(setting1, zNodes);	
	});

	function returnZTree()
	{
		return zTree1;
	}

  </script>
  </head>
  <body id="tem_seltree" class="tem_seltree">
  		<label id="test"></label>
	    <ul id="tree" class="tree tree2"  ></ul>
  </body>
</html>
