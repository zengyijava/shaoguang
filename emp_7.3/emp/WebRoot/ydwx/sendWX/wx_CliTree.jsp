<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%
String path=request.getContextPath();
String iPath = request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	String context = request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	String userid=request.getParameter("userid");
 %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
  <head>
  	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>选择客户机构树</title>
	<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
	<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" href="<%=skin%>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion()%>" type="text/css" >
	<link rel="stylesheet" href="<%=commonPath %>/common/widget/zTreeStyle/zTreeStyle.css?V=<%=StaticValue.getJspImpVersion()%>" type="text/css"/>
	<link rel="stylesheet" href="<%=commonPath %>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion()%>" />
	<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.ztree-myjquery-b.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
  <script type="text/javascript">
	var zTree;
	var setting;
	var isHand = "true";
	var treeNode2;
	setting = {
			async: true,
			isSimpleData: true,
			rootPID : 0,
			treeNodeKey: "id",
			treeNodeParentKey: "pId",
			checkable: false,
			checkRadioType: "level",
			asyncUrl: "<%=request.getContextPath()%>/wx_send.htm?method=getClientSecondDepJson&userid=<%=userid%>",  //获取节点数据的URL地址
			asyncParam: ["name", "id"],  //获取节点数据时，必须的数据名称，例如：id、name
			asyncParamOther: ["test","true"], //其它参数 ( key, value 键值对格式)
			callback:{
				click: zTreeOnClick,
				asyncSuccess:function(event, treeId, treeNode, msg)
				{
					if(!treeNode)
					{
						var rootNode = zTree.getNodeByParam("level",0);
						zTree.expandNode(rootNode,true,false);
						$(window.parent.document).find("#etree").show();
					}
			    },
				beforeAsync: function(treeId, treeNode) {
			    	zTree.setting.asyncUrl="<%=request.getContextPath()%>/wx_send.htm?method=getClientSecondDepJson&userid=<%=userid%>&depId="+treeNode.id;
				}
			}
		};

	//点击事件 		点击上 框的树   显示其机构下的人员
	function zTreeOnClick(event, treeId, treeNode) {
		$(window.parent.document).find("#left").empty();
		depId=treeNode.id;
        $(window.parent.document).find("#depId").val(depId);	
        $(window.parent.document).find("#qztype").val("3");
		//$(window.parent.document).find("#prepage").attr("disabled",false);
		//$(window.parent.document).find("#nextpage").attr("disabled",false);
		var depId=treeNode.id;
		var depName=treeNode.name;
      	$(window.parent.document).find("#choiceId").val(depId);	
        $(window.parent.document).find("#choiceName").val(treeNode.name);
        //每点机构树的时候，查询的是第一个索引页面的数据 
        var pageIndex = 1; 
        $(window.parent.document).find("#pageIndex").val(pageIndex);
		$.post("<%=path %>/wx_send.htm", {method:"getClientByDepId", depId:depId,pageIndex:pageIndex}, function(result){
			$(window.parent.document).find("#pagecode").empty();
			if(result != "" ){
				//上一页按钮可见
				$(window.parent.document).find("#prepage").css("visibility","visible");
				//下一页按钮可见
				$(window.parent.document).find("#nextpage").css("visibility","visible");
				//获取分页信息
				var arr = result.substring(0,result.indexOf("#")).split(",");
				//总页数
				var pageTotal = parseInt(arr[0]);
				$(window.parent.document).find("#totalPage").val(pageTotal);
				//总记录数
				var pageRec = arr[1];
				//添加记录
				$(window.parent.document).find("#left").html(result.substring(result.indexOf("#")+1));
				//显示当前机构
				//var tempName = depName;
				//if(tempName.length>9){
				//	tempName = tempName.substr(0,9)+"...";
				//}
				//$(window.parent.document).find("#addrName").html("当前通讯录："+tempName);
				//显示该机构总人数
				//$(window.parent.document).find("#userTotal").html("["+pageRec+"人]");
				//如果是只有一页记录的话
				$(window.parent.document).find("#pagecode").html(pageIndex+"/"+pageTotal);
				//$(window.parent.document).find("#prepage").attr("disabled",true);
				if(pageTotal == 1){
					//$(window.parent.document).find("#prepage").css("visibility","hidden");
					//$(window.parent.document).find("#nextpage").css("visibility","hidden");
				}
			}else{
			//	var tempName = depName;
			//	if(tempName.length>9){
			//		tempName = tempName.substr(0,9)+"...";
			//	}
			//	$(window.parent.document).find("#addrName").html("当前通讯录："+tempName);
			//	$(window.parent.document).find("#userTotal").html("[0人]");
				$(window.parent.document).find("#prepage").css("visibility","hidden");
				$(window.parent.document).find("#nextpage").css("visibility","hidden");
				return;
			}
		});
		
	}
	
	var zNodes =[];
	//初始化相关数据
	$(document).ready(function(){
		setting.expandSpeed = ($.browser.msie && parseInt($.browser.version)<=7)?"":"fast";
		zTree = $("#tree").zTree(setting, zNodes);	
	});
	
	//获得树形数据
	function a(){
		zTree = $("#tree").zTree(setting, zNodes);	
	}
	//返回树状结构
	function returnZTree()
	{
		return zTree;
	}

  </script>


  </head>
  <body style="margin:0;padding:0;">
  		<label id="test"></label>
	    <ul id="tree" class="tree" style="width:230px;"></ul>
  </body>
</html>
